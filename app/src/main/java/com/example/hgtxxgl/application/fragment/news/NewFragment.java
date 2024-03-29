package com.example.hgtxxgl.application.fragment.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.NewsItemActivity;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.fragment.DetailFragment;
import com.example.hgtxxgl.application.utils.TimeUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;
//新闻列表
public class NewFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener {
    private int beginNum = 1;
    private int endNum = 6;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "NewFragment";
    SimpleListView lv;

    public NewFragment() {

    }

    public static NewFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        NewFragment fragment = new NewFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private List<NewsInfoEntity.NewsRrdBean> entityList = new ArrayList<>();

    ListAdapter<NewsInfoEntity.NewsRrdBean> adapter = new ListAdapter<NewsInfoEntity.NewsRrdBean>
            ((ArrayList<NewsInfoEntity.NewsRrdBean>) entityList, R.layout.layout_news) {

        private Bitmap bitmap = null;

        @Override
        public void bindView(ViewHolder holder, NewsInfoEntity.NewsRrdBean bean) {
            if (!bean.getPicture1().isEmpty()){
                bitmap = stringtoBitmap(bean.getPicture1());
                holder.setBitmap(R.id.image_news,bitmap);
            }
            holder.setText(R.id.tv_title, bean.getTitle());
            holder.setText(R.id.tv_sketch, bean.getContent());
//            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_date, TimeUtil.getTimeFormatText(DataUtil.parseDateToText(bean.getModifyTime())));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            View image = view.findViewById(R.id.image_news);
            if (bitmap == null){
                image.setVisibility(View.GONE);
            }else{
                image.setVisibility(View.VISIBLE);
            }
            return view;
        }
    };

    //将字符串转换成Bitmap类型
    public Bitmap stringtoBitmap(String string){
        Bitmap bitmap = null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(beginNum,endNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listview_news, container, false);
        HandToolbar handToolbar = (HandToolbar) view.findViewById(R.id.news_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(false, getActivity());
        handToolbar.setBackHome(false,0);
        handToolbar.setTitle("新闻");
        handToolbar.setTitleSize(18);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (TextView) view.findViewById(R.id.iv_empty);
        ivEmpty.setText(R.string.no_current_news);
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

    public void loadData(final int beginNum, final int endNum) {
        if (callback != null) {
            callback.onLoadData();
        }
        NewsInfoEntity newsInfoEntity = new NewsInfoEntity();
        NewsInfoEntity.NewsRrdBean newsRrdBean = new NewsInfoEntity.NewsRrdBean();
        newsRrdBean.setTitle("?");
        newsRrdBean.setContent("?");
        newsRrdBean.setModifyTime("?");
        newsRrdBean.setPicture1("?");
        newsRrdBean.setBeginNum(beginNum+"");
        newsRrdBean.setEndNum(endNum+"");
        newsRrdBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        newsRrdBean.setIsAndroid("1");
        newsRrdBean.setNoIndex("?");
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsRrdBean);
        newsInfoEntity.setNewsRrd(list);
        String json = new Gson().toJson(newsInfoEntity);
        String s = "get " + json;
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        String tempIP = share.getString("tempIP", "IP address is empty");
        L.e(TAG,s);
        HttpManager.getInstance().requestResultForm(tempIP, s, NewsInfoEntity.class,new HttpManager.ResultCallback<NewsInfoEntity>() {
            @Override
            public void onSuccess(final String json, final NewsInfoEntity newsInfoEntity) throws InterruptedException {
                if (newsInfoEntity != null && newsInfoEntity.getNewsRrd().size() > 0 && newsInfoEntity.getNewsRrd().get(0) != null) {
                    L.e(TAG,newsInfoEntity.toString());
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
                ivEmpty.setVisibility(View.GONE);
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
                lv.completeRefresh();
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
    public void onPullRefresh() {
        hasMore = true;
        beginNum = 1;
        endNum = 6;
        loadData(beginNum, endNum);
        lv.completeRefresh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lv.getCurrentState() == 2) return;
        position -= 1;
        checkDetail(position);
    }

    private void checkDetail(int position) {
        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
        intent.putExtra("tab", "新闻内容");
        intent.putExtra("title", adapter.getItem(position).getTitle());
        intent.putExtra("content", adapter.getItem(position).getContent());
        intent.putExtra("modifyTime",adapter.getItem(position).getModifyTime());
        intent.putExtra("NoIndex",adapter.getItem(position).getNoIndex());
        startActivity(intent);
    }

    private DetailFragment.DataCallback callback;

    public NewFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }

}