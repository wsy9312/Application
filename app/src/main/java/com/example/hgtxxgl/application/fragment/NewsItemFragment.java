package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HGTXxgl on 2017/8/7.
 */
public class NewsItemFragment extends CommonFragment{

    private NewsInfoEntity.NewsRrdBean bean = null;

    public NewsItemFragment() {
    }

    public static NewsItemFragment newInstance() {
        NewsItemFragment newsItemFragment = new NewsItemFragment();
        return newsItemFragment;
    }

    public static NewsItemFragment newInstance(Bundle bundle) {
        NewsItemFragment fragment = new NewsItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.mainColor_blue);
        loadData();
    }

    private void loadData() {
        String title =  getArguments().getString("title");
        String content = getArguments().getString("content");
        String modifyTime = getArguments().getString("modifyTime");
        NewsInfoEntity newsInfoEntity = new NewsInfoEntity();
        NewsInfoEntity.NewsRrdBean newsRrdBean = new NewsInfoEntity.NewsRrdBean();
        newsRrdBean.setTitle(title);
        newsRrdBean.setContent(content);
        newsRrdBean.setModifyTime(modifyTime);
        newsRrdBean.setPicture1("?");
        newsRrdBean.setPicture2("?");
        newsRrdBean.setPicture3("?");
        newsRrdBean.setPicture4("?");
        newsRrdBean.setPicture5("?");
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsRrdBean);
        newsInfoEntity.setNewsRrd(list);
        String json = new Gson().toJson(newsInfoEntity);
        final String s = "get " + json;
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url,s,NewsInfoEntity.class, new HttpManager.ResultCallback<NewsInfoEntity>() {
            @Override
            public void onSuccess(String json, final NewsInfoEntity newsInfoEntity) throws InterruptedException {
                if (newsInfoEntity != null && newsInfoEntity.getNewsRrd().size() > 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (newsInfoEntity != null){
                                setBean(newsInfoEntity.getNewsRrd().get(0));
                                setGroup(getGroupList());
                                setPb(false);
                                setButtonllEnable(true);
                                setDisplayTabs(true);
                                notifyDataSetChanged();
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    @Override
    public List<Group> getGroupList() {
        if (bean == null) return new ArrayList<>();
        List<Group> groups = new ArrayList<>();

//        if (bean == null) {
            List<HandInputGroup.Holder> titleHolder = new ArrayList<>();
            titleHolder.add(new HandInputGroup.Holder("新闻标题",false,false,bean.getTitle(),HandInputGroup.VALUE_TYPE.TITLE));
            groups.add(0,new CommonFragment.Group("新闻标题", null,true,null,titleHolder));

            List<HandInputGroup.Holder> contentHolder = new ArrayList<>();
            contentHolder.add(new HandInputGroup.Holder("新闻内容",false,false,bean.getContent(),HandInputGroup.VALUE_TYPE.TITLE));
            groups.add(1,new CommonFragment.Group("新闻内容", null,true,null,contentHolder));

//            List<HandInputGroup.Holder> pictureHolder = new ArrayList<>();
//            pictureHolder.add(new HandInputGroup.Holder("申请人",true,false,"",HandInputGroup.VALUE_TYPE.SUB_LIST));
//            pictureHolder.add(new HandInputGroup.Holder("申请人",true,false,"",HandInputGroup.VALUE_TYPE.TEXTFILED));
//            pictureHolder.add(new HandInputGroup.Holder("申请人",true,false,name,HandInputGroup.VALUE_TYPE.TEXTFILED));
//            pictureHolder.add(new HandInputGroup.Holder("申请人",true,false,name,HandInputGroup.VALUE_TYPE.TEXTFILED));
//            pictureHolder.add(new HandInputGroup.Holder("申请人",true,false,name,HandInputGroup.VALUE_TYPE.TEXTFILED));
//            groups.add(2,new CommonFragment.Group("新闻图片", null,true,null,pictureHolder));

//        } else {
//            PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = entity.getPeopleLeaveRrd().get(0);
//            String outTime = peopleLeaveRrdBean.getOutTime();
//            String inTime = peopleLeaveRrdBean.getInTime();
//            String content = peopleLeaveRrdBean.getContent();
//            String bCancel = peopleLeaveRrdBean.getBCancel();
//            String bFillup = peopleLeaveRrdBean.getBFillup();
//            List<HandInputGroup.Holder> subHolder1 = new ArrayList<>();
//            subHolder1.add(new HandInputGroup.Holder("申请人", true, false, name, HandInputGroup.VALUE_TYPE.TEXTFILED));
//            subHolder1.add(new HandInputGroup.Holder("预计外出时间", true, false, outTime, HandInputGroup.VALUE_TYPE.DATE));
//            subHolder1.add(new HandInputGroup.Holder("预计归来时间", true, false, inTime, HandInputGroup.VALUE_TYPE.DATE));
//            subHolder1.add(new HandInputGroup.Holder("请假原因", false, false, content, HandInputGroup.VALUE_TYPE.TEXTFILED));
//            subHolder1.add(new HandInputGroup.Holder("是否取消请假", false, false, bCancel.equals("0") ? "否" : "是", HandInputGroup.VALUE_TYPE.SELECT));
//            subHolder1.add(new HandInputGroup.Holder("是否后补请假", false, false, bFillup.equals("0") ? "否" : "是", HandInputGroup.VALUE_TYPE.SELECT));
//            groups.add(0, new CommonFragment.Group("基本信息", null, true, null, subHolder1));
//        }
        return groups;
    }

    public NewsInfoEntity.NewsRrdBean getBean() {
        return bean;
    }

    public void setBean(NewsInfoEntity.NewsRrdBean bean) {
        this.bean = bean;
    }
}
