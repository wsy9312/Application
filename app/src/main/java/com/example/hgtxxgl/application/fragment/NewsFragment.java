package com.example.hgtxxgl.application.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CacheManger;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.GsonUtil;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.SimpleListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//原本的我的待办fragment,现在的新闻中心
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener {

    private int tabIndex;
    private int index = 1;
    private boolean hasMore = true;
    private ImageView ivEmpty;
    private TextView tvEmpty;
    private Button btnEmpty;
    private ProgressBar pb;
    public Map<String, Object> map;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }


    private List<NewsInfoEntity.NewsRrdBean> entityList = getData();
    private List<NewsInfoEntity.NewsRrdBean> baseEntityList;
    ListAdapter<NewsInfoEntity.NewsRrdBean> adapter =
            new ListAdapter<NewsInfoEntity.NewsRrdBean>((ArrayList<NewsInfoEntity.NewsRrdBean>) entityList, R.layout.layout_my_todo_too) {
                @Override
                public void bindView(ViewHolder holder, NewsInfoEntity.NewsRrdBean obj) {
                    holder.setText(R.id.tv_title, obj.getTitle());
                    holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(obj.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
                    holder.setText(R.id.tv_sketch, obj.getContext());
                }
            };
    //填充预留的假数据
    public List<NewsInfoEntity.NewsRrdBean> getData() {
        String data = CacheManger.getInstance().getData(CommonValues.BASE_URL_NEWS_SAVE);
        NewsInfoEntity newsInfoEntity = GsonUtil.parseJsonToBean(data, NewsInfoEntity.class);
        String s = newsInfoEntity.toString();
        L.e(TAG, s);
        boolean empty = newsInfoEntity.getNewsRrd().isEmpty();
        if (empty) {
            ToastUtil.showToast(ApplicationApp.context, "为空");
        }
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsInfoEntity.getNewsRrd().get(0));
        return list;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndex = getArguments().getInt(DetailFragment.ARG_TAB);
        loadData(tabIndex, index, 10);
    }
    SimpleListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_viewpage_page_content, null, false);
//        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
//        ivEmpty = (ImageView) view.findViewById(R.id.iv_empty);
//        tvEmpty = (TextView) view.findViewById(R.id.tv_reload);
//        btnEmpty = (Button) view.findViewById(R.id.btn_reload);
//        pb = (ProgressBar) view.findViewById(R.id.mycommission_pb);
        //3设置adapter从2
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

    //根据顶部菜单按钮点击切换到对应的功能模块
    void loadData(int tabIndex, final int pageIndex, int pageSize) {

    }

    //加载更多
    private void loadMore() {
        if (hasMore) {
            index += 1;
            loadData(tabIndex, index, 10);
        } else {
            lv.completeRefresh();
        }
    }

    //根据条目在listview中的位置选择进入对应的详情页
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        NewsInfoEntity.NewsRrdBean item = adapter.getItem(position - 1);
        position -= 1;

    }
    public static final String TAG = "NewsFragment";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonValues.MYCOMM){
            if (resultCode == Activity.RESULT_OK){
                final int item = data.getExtras().getInt("item");
                final int tabIndex = data.getExtras().getInt("tabIndex");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tabIndex == 0){
                            entityList.remove(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }
    }

    private DetailFragment.DataCallback callback;

    public NewsFragment setCallback(DetailFragment.DataCallback callback) {
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
                if (bean.getContext().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm").replace(" ", "").contains(key)) {
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

    //下拉刷新
    @Override
    public void onPullRefresh() {
        pb.setVisibility(View.GONE);
        hasMore = true;
        index = 1;
        loadData(tabIndex, index, 10);
    }

    //加载更多
    @Override
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }

}
