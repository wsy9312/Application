package com.example.hgtxxgl.application.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.activity.NewsItemActivity;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.GlideImageLoader;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.hgtxxgl.application.utils.hand.PageConfig.PAGE_LEAVE_APPLY_PEOPLE;

public class NewFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener, OnBannerListener, View.OnClickListener {
    private int beginNum = 1;
    private int endNum = 6;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "NewFragment";
    SimpleListView lv;
    private List<Bitmap> bitmapList;
    private FloatingActionButton fbcPeople;
    private FloatingActionButton fbcApply;
    private FloatingActionsMenu fbcMenu;

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
    private List<NewsInfoEntity.NewsRrdBean> baseEntityList;

    ListAdapter<NewsInfoEntity.NewsRrdBean> adapter = new ListAdapter<NewsInfoEntity.NewsRrdBean>((ArrayList<NewsInfoEntity.NewsRrdBean>) entityList, R.layout.layout_news) {

        private Bitmap bitmap;

        @Override
        public void bindView(ViewHolder holder, NewsInfoEntity.NewsRrdBean bean) {
            bitmap = stringtoBitmap(bean.getPicture1());
            holder.setText(R.id.tv_title, bean.getTitle());
            holder.setBitmap(R.id.image_news,bitmap);
            holder.setText(R.id.tv_sketch, bean.getContent());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            View image = view.findViewById(R.id.image_news);
            if (bitmap==null){
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
        View view = inflater.inflate(R.layout.main_listview_news, null, false);

        Banner banner = (Banner) view.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(ApplicationApp.images);
        banner.setDelayTime(4000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (TextView) view.findViewById(R.id.iv_empty);
        ivEmpty.setText("当前无新闻");
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
        fbcPeople = (FloatingActionButton) view.findViewById(R.id.button_fbc_people);
        fbcApply = (FloatingActionButton) view.findViewById(R.id.button_fbc_apply);
        fbcMenu = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions_up);
        fbcPeople.setOnClickListener(this);
        fbcApply.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_fbc_people:
                startActivity(new Intent(getContext(),PersonalActivity.class));
                fbcMenu.collapse();
                break;
            case R.id.button_fbc_apply:
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(PageConfig.PAGE_CODE, PAGE_LEAVE_APPLY_PEOPLE);
                startActivity(intent);
                fbcMenu.collapse();
                break;
        }
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
        newsRrdBean.setPicture5("?");
        newsRrdBean.setPicture4("?");
        newsRrdBean.setPicture3("?");
        newsRrdBean.setPicture2("?");
        newsRrdBean.setPicture1("?");
        newsRrdBean.setBeginNum(beginNum+"");
        newsRrdBean.setEndNum(endNum+"");
        newsRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        newsRrdBean.setIsAndroid("1");
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsRrdBean);
        newsInfoEntity.setNewsRrd(list);
        String json = new Gson().toJson(newsInfoEntity);
        String s = "get " + json;
        Log.e(TAG,s);
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
                            setBannerBitmap();
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

    private void setBannerBitmap() {
        bitmapList = new ArrayList<>();
        String picture1 = entityList.get(0).getPicture1();
        String picture2 = entityList.get(0).getPicture2();
        String picture3 = entityList.get(0).getPicture3();
        String picture4 = entityList.get(0).getPicture4();
        String picture5 = entityList.get(0).getPicture5();
        Bitmap stringtoBitmap = stringtoBitmap(picture1);
        Bitmap stringtoBitmap1 = stringtoBitmap(picture2);
        Bitmap stringtoBitmap2 = stringtoBitmap(picture3);
        Bitmap stringtoBitmap3 = stringtoBitmap(picture4);
        Bitmap stringtoBitmap4 = stringtoBitmap(picture5);
        bitmapList.add(0,stringtoBitmap);
        bitmapList.add(1,stringtoBitmap1);
        bitmapList.add(2,stringtoBitmap2);
        bitmapList.add(3,stringtoBitmap3);
        bitmapList.add(4,stringtoBitmap4);
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
//        intent.putExtra("picture1", adapter.getItem(position).getPicture1());
//        intent.putExtra("picture2", adapter.getItem(position).getPicture2());
//        intent.putExtra("picture3", adapter.getItem(position).getPicture3());
//        intent.putExtra("picture4", adapter.getItem(position).getPicture4());
//        intent.putExtra("picture5", adapter.getItem(position).getPicture5());
//        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    private DetailFragment.DataCallback callback;

    public NewFragment setCallback(DetailFragment.DataCallback callback) {
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
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }

    @Override
    public void OnBannerClick(int position) {

    }

}