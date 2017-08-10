package com.example.hgtxxgl.application.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.NewsItemActivity;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//通知中心
public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener {

    private int beginNum = 1;
    private int endNum = 6;
    private boolean hasMore = true;
    private ImageView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "NotificationFragment";
    SimpleListView lv;

    public NotificationFragment() {

    }

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    private List<NewsInfoEntity.NewsRrdBean> entityList = new ArrayList<>();
    private List<NewsInfoEntity.NewsRrdBean> baseEntityList;

    ListAdapter<NewsInfoEntity.NewsRrdBean> adapter = new ListAdapter<NewsInfoEntity.NewsRrdBean>((ArrayList<NewsInfoEntity.NewsRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, NewsInfoEntity.NewsRrdBean bean) {
            holder.setText(R.id.tv_title, bean.getTitle());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, bean.getContent());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(beginNum,endNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listview_libmain, null, false);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (ImageView) view.findViewById(R.id.iv_empty);
        pb = (ProgressBar) view.findViewById(R.id.mycommission_pb);
        lv.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (adapter.getCount() == 0) {
                    ivEmpty.setVisibility(View.VISIBLE);
                } else {
                    ivEmpty.setVisibility(View.GONE);
                }
                pb.setVisibility(View.GONE);
                super.onChanged();
            }
        });
        lv.setOnRefreshListener(this);
        lv.setOnItemClickListener(this);
        return view;
    }

    void loadData(final int beginNum, final int endNum) {
        if (callback != null) {
            callback.onLoadData();
        }
        NewsInfoEntity newsInfoEntity = new NewsInfoEntity();
        NewsInfoEntity.NewsRrdBean newsRrdBean = new NewsInfoEntity.NewsRrdBean();
        newsRrdBean.setTitle("?");
        newsRrdBean.setContent("?");
        newsRrdBean.setModifyTime("?");
        newsRrdBean.setBeginNum(beginNum+"");
        newsRrdBean.setEndNum(endNum+"");
        newsRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsRrdBean);
        newsInfoEntity.setNewsRrd(list);
        String json = new Gson().toJson(newsInfoEntity);
        final String s = "get " + json;
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, NewsInfoEntity.class,new HttpManager.ResultCallback<NewsInfoEntity>() {
            @Override
            public void onSuccess(final String json, final NewsInfoEntity newsInfoEntity) throws InterruptedException {
                if (newsInfoEntity != null && newsInfoEntity.getNewsRrd().size() > 0) {
                    if (beginNum == 1 && endNum == 6){
                        entityList.clear();
                    }
                    hasMore = true;
                    entityList.addAll(newsInfoEntity.getNewsRrd());
                    adapter.notifyDataSetChanged();
                } else {
                    hasMore = false;
                }
                pb.setVisibility(View.GONE);
                lv.completeRefresh();
            }

            @Override
            public void onFailure(String msg) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv.completeRefresh();
                        pb.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(String response) {
                ivEmpty.setVisibility(View.VISIBLE);
            }
        });

    }

    private void loadMore() {
        if (hasMore) {
            beginNum += 6;
            endNum += 6;
            loadData(beginNum, endNum);
        } else {
            lv.completeRefresh();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lv.getCurrentState() == 2) return;
        position -= 1;
        checkDetail(position);

    }
    private void checkDetail(int position) {
        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
        intent.putExtra("title", adapter.getItem(position).getTitle());
        intent.putExtra("content", adapter.getItem(position).getContent());
        intent.putExtra("modifyTime",adapter.getItem(position).getModifyTime());
//        intent.putExtra("picture1", adapter.getItem(position).getPicture1());
//        intent.putExtra("picture2", adapter.getItem(position).getPicture2());
//        intent.putExtra("picture3", adapter.getItem(position).getPicture3());
//        intent.putExtra("picture4", adapter.getItem(position).getPicture4());
//        intent.putExtra("picture5", adapter.getItem(position).getPicture5());
//        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    private DetailFragment.DataCallback callback;

    public NotificationFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    public void filter(String key) {
        if (entityList != null && !key.equals("")) {
            if (baseEntityList == null) {
                baseEntityList = new ArrayList<>();
                baseEntityList.addAll(entityList);
            }
            List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
            for (NewsInfoEntity.NewsRrdBean bean : baseEntityList) {
                if (bean.getTitle().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (bean.getContent().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss").replace(" ", "").contains(key)) {
                    list.add(bean);
                }
            }
            entityList.clear();
            entityList.addAll(list);
            adapter.notifyDataSetChanged();
        } else if (entityList != null) {
            if (baseEntityList != null) {
                entityList.clear();
                entityList.addAll(baseEntityList);
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onPullRefresh() {
        hasMore = true;
        beginNum = 1;
        endNum = 6;
        loadData(beginNum, endNum);
        lv.completeRefresh();
    }

    @Override
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }
}
