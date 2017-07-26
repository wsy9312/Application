package com.example.hgtxxgl.application.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.NewsItemActivity;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.GsonUtil;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class NewFragment extends Fragment /*implements*/ /*AdapterView.OnItemClickListener*//*, PullToRefreshBase.OnRefreshListener<ListView>*/ {

    private View view;

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

    public PullToRefreshListView getmPullRefreshListView() {
        return mPullRefreshListView;
    }

    private PullToRefreshListView mPullRefreshListView;
    private List<NewsInfoEntity.NewsRrdBean> entityList = new ArrayList<NewsInfoEntity.NewsRrdBean>();
    private List<NewsInfoEntity.NewsRrdBean> baseEntityList;

    ListAdapter<NewsInfoEntity.NewsRrdBean> adapter = new ListAdapter<NewsInfoEntity.NewsRrdBean>((ArrayList<NewsInfoEntity.NewsRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, NewsInfoEntity.NewsRrdBean obj) {
            holder.setText(R.id.tv_title, obj.getTitle());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(obj.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, obj.getContext());
        }
    };
    private NewsInfoEntity newsInfoEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_viewpage_page_content, null, false);
//        mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
        // Set a listener to be invoked when the list should be refreshed.
        /*SwipeRefreshLayout retest = (SwipeRefreshLayout) view.findViewById(R.id.retest);
//        mPullRefreshListView.setOnRefreshListener(this);
//        mPullRefreshListView.setVisibility(View.GONE);
        retest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("test","onRefresh");
            }
        });*/
       /* // Add an end-of-list listener
        mPullRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                Toast.makeText(getContext(), "End of List!", Toast.LENGTH_SHORT).show();
            }
        });*/

        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
//        mPullRefreshListView.setAdapter(adapter);
//        //设置首次进入自动刷新数据
//        new Handler().postDelayed(new Runnable(){
//            public void run(){
//                mPullRefreshListView.setRefreshing();
//            }
//        },200);
        return view;
    }

    private int index;

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        position -= 1;
//        checkDetail(position);
//    }

//    @Override
//    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//        String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
//                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//        // Update the LastUpdatedLabel
//        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//        Log.d("test","onRefresh");
//        // Do work to refresh the list here.
//        new GetDataTask().execute();
//    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            Log.d("test","GetDataTask");
            // Simulates a background job.
            try {
                index = 1;
                getNewsDataFromNetWork();
            } catch (Exception e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            Log.d("test","onPostExecute");
            entityList.addAll(newsInfoEntity.getNewsRrd());
            adapter.notifyDataSetChanged();
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private void getNewsDataFromNetWork(/*int index, int itemNumber*/) {
        NewsInfoEntity newsInfoEntity1 = new NewsInfoEntity();
        NewsInfoEntity.NewsRrdBean newsRrdBean1 = new NewsInfoEntity.NewsRrdBean();
        newsRrdBean1.setTitle("?");
        newsRrdBean1.setContext("?");
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
        newsRrdBean1.setRegisterTime("?");
//		newsRrdBean1.setBeginNum("1");
//		newsRrdBean1.setEndNum("5");
        List<NewsInfoEntity.NewsRrdBean> beanList1 = new ArrayList<NewsInfoEntity.NewsRrdBean>();
        beanList1.add(newsRrdBean1);
        newsInfoEntity1.setNewsRrd(beanList1);
        String json1 = new Gson().toJson(newsInfoEntity1);
        String s1 = "get " + json1;
        okhttp3.Response execute = null;
        try {
            execute = OkHttpUtils
                    .postString()
                    .url("http://192.168.1.106:8080/")
                    .mediaType(okhttp3.MediaType.parse("application/json; charset=utf-8"))
                    .content(s1)
                    .build()
                    .readTimeOut(10000L)
                    .writeTimeOut(10000L)
                    .connTimeOut(10000L)
                    .execute();
            if (execute!=null){
                String ResponseStr = null;
                ResponseStr = execute.body().string();
                if (ResponseStr != null && ResponseStr.contains("ok")) {
                    if (index == 1){
                        entityList.clear();
                    }
                    String newRes = ResponseStr.substring(ResponseStr.indexOf("{"),ResponseStr.length());
                    newsInfoEntity = GsonUtil.parseJsonToBean(newRes, NewsInfoEntity.class);
                } else {
                }
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkDetail(int position) {
        Intent intent = new Intent(getContext(), NewsItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", adapter.getItem(position).getTitle());
        bundle.putString("context", adapter.getItem(position).getContext());
        bundle.putString("modifytime", adapter.getItem(position).getModifyTime());
        bundle.putString("picture1", adapter.getItem(position).getPicture1());
        bundle.putString("picture2", adapter.getItem(position).getPicture2());
        bundle.putString("picture3", adapter.getItem(position).getPicture3());
        bundle.putString("picture4", adapter.getItem(position).getPicture4());
        bundle.putString("picture5", adapter.getItem(position).getPicture5());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private String[] mStrings = { "12baye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler" };
    /*public void filter(String key) {
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
    }*/


}