package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.GsonUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.litesuits.common.utils.HandlerUtil.runOnUiThread;


public class NewFragment extends Fragment {
    private View view;
    private ListView listView;
    private int totalNumber;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "NewFragment";
    public NewFragment() {

    }

    public static NewFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        NewFragment fragment = new NewFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DetailFragment.DataCallback callback;

    public NewFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_viewpage_page_content, null, false);
        initRefresh(view);
        return view;
    }

    private void initData() {
        /*new Thread(new Runnable() {

            @Override
            public void run() {
                NewsInfoEntity newsInfoEntity1 = new NewsInfoEntity();
                NewsInfoEntity.NewsRrdBean newsRrdBean1 = new NewsInfoEntity.NewsRrdBean();
                newsRrdBean1.setTitle("?");
                *//*newsRrdBean1.setContent("?");
                newsRrdBean1.setPicture1("?");
                newsRrdBean1.setPicture2("?");
                newsRrdBean1.setPicture3("?");
                newsRrdBean1.setPicture4("?");
                newsRrdBean1.setPicture5("?");
                newsRrdBean1.setPicture1Len("?");
                newsRrdBean1.setPicture2Len("?");
                newsRrdBean1.setPicture3Len("?");
                newsRrdBean1.setPicture4Len("?");
                newsRrdBean1.setPicture5Len("?");
                newsRrdBean1.setModifyTime("?");
                newsRrdBean1.setRegisterTime("?");*//*
//		newsRrdBean1.setBeginNum("1");
//		newsRrdBean1.setEndNum("5");
                List<NewsInfoEntity.NewsRrdBean> beanList1 = new ArrayList<>();
                beanList1.add(newsRrdBean1);
                newsInfoEntity1.setNewsRrd(beanList1);
                String json1 = new Gson().toJson(newsInfoEntity1);
                String s1 = "get " + json1;
                Log.e(TAG,s1);
                Response execute = null;
                try {
                    execute = OkHttpUtils
                            .postString()
                            .url(CommonValues.BASE_URL)
                            .mediaType(MediaType.parse("application/json; charset=utf-8"))
                            .content(s1)
                            .build()
                            .execute();
                        String ResponseStr = null;
                        ResponseStr = execute.body().toString();
                        if (ResponseStr != null && ResponseStr.contains("ok")) {
                            Log.e(TAG,ResponseStr);
                            String newRes = ResponseStr.substring(ResponseStr.indexOf("{"),ResponseStr.length());
                            newsInfoEntity = GsonUtil.parseJsonToBean(newRes, NewsInfoEntity.class);
                            totalNumber = newsInfoEntity.getNewsRrd().size();
                            L.d(newsInfoEntity.toString());
                            L.d(totalNumber+"");
                            if (!entityList.containsAll(newsInfoEntity.getNewsRrd())){
                                entityList.clear();
                                entityList.addAll(newsInfoEntity.getNewsRrd());
                            }
                        } else {
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/


        NewsInfoEntity newsInfoEntity = new NewsInfoEntity();
        NewsInfoEntity.NewsRrdBean newsRrdBean = new NewsInfoEntity.NewsRrdBean();
        newsRrdBean.setTitle("?");
        newsRrdBean.setContent("?");
        newsRrdBean.setModifyTime("?");
        newsRrdBean.setBeginNum("1");
        newsRrdBean.setEndNum("5");
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsRrdBean);
        newsInfoEntity.setNewsRrd(list);
        String json = new Gson().toJson(newsInfoEntity);
        String s = "get " + json;
        Log.e(TAG, "postString: "+s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, NewsInfoEntity.class, "",new HttpManager.ResultCallback<NewsInfoEntity>() {
            @Override
            public void onSuccess(final String json, final NewsInfoEntity newsInfoEntity) throws InterruptedException {
                if (newsInfoEntity != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG,"onSuccess: "+json);
                            int totalNumber = newsInfoEntity.getNewsRrd().size();
                            Log.e(TAG ,"onSuccess: "+newsInfoEntity.toString());
                            Log.e(TAG ,"onSuccess: "+totalNumber+"");
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(getContext(),json);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
                Log.e(TAG,"onFailure: "+msg);
                String s1 = msg + "}]}";
                newsInfoEntity1 = GsonUtil.parseJsonToBean(s1, NewsInfoEntity.class);
                Log.e(TAG, "onFailure: "+newsInfoEntity1.toString());
                if (!entityList.containsAll(newsInfoEntity1.getNewsRrd())){
                    entityList.clear();
                    entityList.addAll(newsInfoEntity1.getNewsRrd());
                }
            }
        });
    }

    private List<NewsInfoEntity.NewsRrdBean> entityList = new ArrayList<>();
    private NewsInfoEntity newsInfoEntity1;

    ListAdapter<NewsInfoEntity.NewsRrdBean> adapter = new ListAdapter<NewsInfoEntity.NewsRrdBean>((ArrayList<NewsInfoEntity.NewsRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, NewsInfoEntity.NewsRrdBean obj) {
            holder.setText(R.id.tv_title, obj.getTitle());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(obj.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, obj.getContent());
        }
    };

    private void initRefresh(View v) {
        listView = (ListView)v.findViewById(R.id.news_listview);

        listView.setAdapter(adapter);

        //findview
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        //设置卷内的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.mainColor_blue);
        //设置下拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initData();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Log.e(TAG, "run:");

                    adapter.notifyDataSetChanged();
                    //停止刷新动画
                    if (swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 2000);
        }
    };


}