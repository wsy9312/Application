package com.example.hgtxxgl.application.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
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

import java.util.ArrayList;
import java.util.List;

import static com.example.hgtxxgl.application.utils.hand.PageConfig.PAGE_LEAVE_APPLY_PEOPLE;

public class MyLaunchFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private int beginNum = 1;
    private int endNum = 6;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "MyLaunchFragment";
    private FloatingActionButton fbcPeople;
    private FloatingActionButton fbcApply;
    private FloatingActionsMenu fbcMenu;

    public MyLaunchFragment() {

    }

    public static MyLaunchFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        MyLaunchFragment fragment = new MyLaunchFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> entityList = new ArrayList<>();
    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> baseEntityList;

    ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean> adapter = new ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean>((ArrayList<PeopleLeaveEntity.PeopleLeaveRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, PeopleLeaveEntity.PeopleLeaveRrdBean bean) {
            holder.setText(R.id.tv_title, "审批进度:"+(bean.getProcess().equals("1")?"已结束":"未结束"));
            holder.setText(R.id.tv_date, "修改时间:"+DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, bean.getContent().isEmpty()?"请假原因:无":"请假原因:"+bean.getContent());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tabIndex = getArguments().getInt(DetailFragment.ARG_TAB);
        loadData(beginNum, endNum);
    }

    SimpleListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listview_libmain, null, false);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (TextView) view.findViewById(R.id.iv_empty);
        ivEmpty.setText("当前无记录");
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
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        peopleLeaveRrdBean.setMultiLevelResult("?");
        peopleLeaveRrdBean.setProcess("?");
        peopleLeaveRrdBean.setLevelNum("?");
        peopleLeaveRrdBean.setContent("?");
        peopleLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean.setNoIndex("?");
        peopleLeaveRrdBean.setModifyTime("?");
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String json = new Gson().toJson(peopleLeaveEntity);
        final String s = "get " + json;
        Log.e(TAG, "loadData()查看申请记录"+s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
                    if (beginNum==1 && endNum == 6){
                        entityList.clear();
                    }
                    hasMore = true;
                    entityList.addAll(peopleLeaveEntity1.getPeopleLeaveRrd());
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
        checkDetail(position, PageConfig.PAGE_LEAVE_DETAIL_PEOPLE);
    }

    private void checkDetail(int position, int pageApplyBleave) {
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra(PageConfig.PAGE_CODE, pageApplyBleave);
        Bundle bundle = new Bundle();
        bundle.putString("no", adapter.getItem(position).getNo());
        bundle.putString("outtime",adapter.getItem(position).getOutTime());
        bundle.putString("intime", adapter.getItem(position).getInTime());
        bundle.putString("content", adapter.getItem(position).getContent());
        bundle.putString("levelnum", adapter.getItem(position).getLevelNum());
        bundle.putString("process", adapter.getItem(position).getProcess());
        bundle.putString("multiLevelResult",adapter.getItem(position).getMultiLevelResult());
        bundle.putString("modifyTime",adapter.getItem(position).getModifyTime());
        bundle.putString("bcancel",adapter.getItem(position).getBCancel());
        bundle.putString("bfillup",adapter.getItem(position).getBFillup());
        bundle.putString("noindex",adapter.getItem(position).getNoIndex());
        bundle.putInt("item",position);
//        bundle.putInt("tabIndex",tabIndex);
        intent.putExtra("data", bundle);
        startActivityForResult(intent,CommonValues.MYLAUN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonValues.MYLAUN){
            if (resultCode == Activity.RESULT_OK){
                final int item = data.getExtras().getInt("item");
//                final int tabIndex = data.getExtras().getInt("tabIndex");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if (tabIndex == 0){
                        entityList.remove(item);
                        adapter.notifyDataSetChanged();
//                        }
                    }
                });

            }
        }
    }

    private DetailFragment.DataCallback callback;

    public MyLaunchFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    public void filter(String key) {
        if (lv == null || key == null) return;
        if (entityList != null && !key.equals("")) {
            if (baseEntityList == null) {
                baseEntityList = new ArrayList<>();
                baseEntityList.addAll(entityList);
            }
            List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
            for (PeopleLeaveEntity.PeopleLeaveRrdBean bean : baseEntityList) {
                if (("审批进度:"+(bean.getProcess().equals("1")?"已结束":"未结束")).replace(" ", "").contains(key)){
                    list.add(bean);
                }
                if ((bean.getContent().isEmpty()?"请假原因:无":"请假原因:"+bean.getContent()).replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (("修改时间:"+DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss")).replace(" ", "").contains(key)) {
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

}
