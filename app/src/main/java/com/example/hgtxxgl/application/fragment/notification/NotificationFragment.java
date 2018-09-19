package com.example.hgtxxgl.application.fragment.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.NotificationItemActivity;
import com.example.hgtxxgl.application.entity.MessageEntity;
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
import static com.example.hgtxxgl.application.utils.DateUtil.getCurrentDateLater;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;
//通知列表
public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener{

    private int beginNum = -2;
    private int endNum = 0;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "NotificationFragment";
    SimpleListView lv;
    private String authenticationNo;

    public NotificationFragment() {

    }

    public static NotificationFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private List<MessageEntity.MessageRrdBean> entityList = new ArrayList<>();
    private List<MessageEntity.MessageRrdBean> baseEntityList;

    ListAdapter<MessageEntity.MessageRrdBean> adapter = new ListAdapter<MessageEntity.MessageRrdBean>
            ((ArrayList<MessageEntity.MessageRrdBean>) entityList, R.layout.layout_notification) {
        @Override
        public void bindView(ViewHolder holder, MessageEntity.MessageRrdBean bean) {
            holder.setText(R.id.tv_date, bean.getModifyTime());
            holder.setText(R.id.tv_sketch, bean.getContent());
            holder.setText(R.id.tv_name, bean.getObjects());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationNo = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo();
        loadData(beginNum,endNum);
//        PollingUtils.startPollingService(getContext(), 1, PollingService.class, PollingService.ACTION);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData(beginNum,endNum);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listview_libmain, container, false);
        HandToolbar handToolbar = (HandToolbar) view.findViewById(R.id.notification_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(false, getActivity());
        handToolbar.setBackHome(false,0);
        handToolbar.setTitle("通知");
        handToolbar.setTitleSize(18);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (TextView) view.findViewById(R.id.iv_empty);
        ivEmpty.setText(R.string.no_current_notification);
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
        MessageEntity messageEntity = new MessageEntity();
        MessageEntity.MessageRrdBean messageRrdBean = new MessageEntity.MessageRrdBean();
        messageRrdBean.setAuthenticationNo(authenticationNo);
        messageRrdBean.setTime(getCurrentDateLater(beginNum)+"&&"+ getCurrentDateLater(endNum));
        messageRrdBean.setContent("?");
        messageRrdBean.setModifyTime("?");
        messageRrdBean.setObjects(authenticationNo);
        messageRrdBean.setNoIndex("?");
        messageRrdBean.setIsAndroid("1");
        List<MessageEntity.MessageRrdBean> list = new ArrayList<>();
        list.add(messageRrdBean);
        messageEntity.setMessageRrd(list);
        String json = new Gson().toJson(messageEntity).replace("\\u0026","&");
        String s = "get " + json;
        L.e(s+"====1");
        //        String url = CommonValues.BASE_URL;
//        String url = ApplicationApp.getIP();
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        String tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestResultForm(tempIP, s, MessageEntity.class, new HttpManager.ResultCallback<MessageEntity>() {
            @Override
            public void onSuccess(String json, MessageEntity messageEntity) throws InterruptedException {
                L.e(TAG,"onSuccess"+json);
                /*if (messageEntity != null && messageEntity.getMessageRrd().size() > 0){
                    if (beginNum == -2 && endNum == 0){
                        entityList.clear();
                    }
                    hasMore = true;
//                    Collections.reverse(messageEntity.getMessageRrd());
                    Collections.sort(messageEntity.getMessageRrd(), new Comparator<MessageEntity.MessageRrdBean>() {
                        @Override
                        public int compare(MessageEntity.MessageRrdBean lhs, MessageEntity.MessageRrdBean rhs) {
                            Date date1 = DateUtil.stringToDate(lhs.getTime());
                            Date date2 = DateUtil.stringToDate(rhs.getTime());
                            // 对日期字段进行升序，如果欲降序可采用after方法
                            if (date1.before(date2)) {
                                return 1;
                            }
                            return -1;
                        }
                    });
                    entityList.addAll(messageEntity.getMessageRrd());
                    adapter.notifyDataSetChanged();
                } else {
                    hasMore = false;
                }
                pb.setVisibility(View.GONE);
                lv.completeRefresh();*/
            }

            @Override
            public void onFailure(String msg) {
                L.e(TAG,"onFailure"+msg);
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
                if (entityList!=null){
                    entityList.clear();
                    MessageEntity messageEntity = new MessageEntity();
                    List<MessageEntity.MessageRrdBean> list = new ArrayList<>();
                    MessageEntity.MessageRrdBean messageRrdBean5 = new MessageEntity.MessageRrdBean();
                    messageRrdBean5.setContent("一营二连三班班长陈鸿财同志，系浙江省宁波市鄞州区人，在二零一八年中，在团部联谊武术比赛中，成绩突出，经团委武装部批准，给予三等功奖励。特此通知。");
                    messageRrdBean5.setModifyTime("2018/8/20");
                    messageRrdBean5.setObjects("团委武装部通知");
                    MessageEntity.MessageRrdBean messageRrdBean6 = new MessageEntity.MessageRrdBean();
                    messageRrdBean6.setContent("2018 年，广大一营官兵，听党话、强素质、谋打赢、当尖兵，为国防和军队建设做出积极共享，涌现出了一大批立功受奖的先进个人，为团委赢得荣誉。现上级部门经讨论研究决定，对一营下级表现特别突出的一排、三班团体分别授予团体二等功、三等功奖励。特此通知。");
                    messageRrdBean6.setModifyTime("2018/9/10");
                    messageRrdBean6.setObjects("团委政治部通知");
                    MessageEntity.MessageRrdBean messageRrdBean7 = new MessageEntity.MessageRrdBean();
                    messageRrdBean7.setContent("你于2018年9月18日上午9点18分提交的申请信息已通过上级审批，具体详情请查看我发起的申请信息，提醒：外出回营别忘记销假。");
                    messageRrdBean7.setModifyTime("2018/9/11");
                    messageRrdBean7.setObjects("外出申请审批通知");
                    MessageEntity.MessageRrdBean messageRrdBean8 = new MessageEntity.MessageRrdBean();
                    messageRrdBean8.setContent("你于2018年9月16日中午12点05分提交的休假申请信息未通过审批，具体详情请查看我发起的申请信息。");
                    messageRrdBean8.setModifyTime("2018/9/11");
                    messageRrdBean8.setObjects("休假申请拒绝通知");
                    MessageEntity.MessageRrdBean messageRrdBean9 = new MessageEntity.MessageRrdBean();
                    messageRrdBean9.setContent("你于2018年9月10日至2018年9月16日的体能报告已经生成，具体详情请在体能管理里查看。");
                    messageRrdBean9.setModifyTime("2018/9/11");
                    messageRrdBean9.setObjects("本周体能报告");
                    MessageEntity.MessageRrdBean messageRrdBean10 = new MessageEntity.MessageRrdBean();
                    messageRrdBean10.setContent("你于2018年9月18日上午9点18分发起的外出申请即将到达归队时间，请归队之后及时销假。");
                    messageRrdBean10.setModifyTime("2018/9/12");
                    messageRrdBean10.setObjects("销假通知");
                    MessageEntity.MessageRrdBean messageRrdBean11 = new MessageEntity.MessageRrdBean();
                    messageRrdBean11.setContent("你于2018年9月10日至2018年9月16日的学习报告已经生成，具体详情请在学习中心里查看。");
                    messageRrdBean11.setModifyTime("2018/9/13");
                    messageRrdBean11.setObjects("本周学习周报");
                    MessageEntity.MessageRrdBean messageRrdBean12 = new MessageEntity.MessageRrdBean();
                    messageRrdBean12.setContent("武警老兵退伍卸衔，钢铁英汉瞬间哭成泪人军旅数载，哨位相伴，今夕离别，难舍难分。8月31日，武警海南省总队三亚支队举行告别仪式暨告别卸衔告别仪式，这标志着满服役退伍老兵一圆满完成光荣的军旅使命");
                    messageRrdBean12.setModifyTime("2018/9/13");
                    messageRrdBean12.setObjects("新闻看点");

                    MessageEntity.MessageRrdBean messageRrdBean = new MessageEntity.MessageRrdBean();
                    messageRrdBean.setContent("一营全体领导到会议厅集合");
                    messageRrdBean.setModifyTime("2018/8/20");
                    messageRrdBean.setObjects("杨慧国");
                    MessageEntity.MessageRrdBean messageRrdBean1 = new MessageEntity.MessageRrdBean();
                    messageRrdBean1.setContent("二连全体领导到操场集合");
                    messageRrdBean1.setModifyTime("2018/8/21");
                    messageRrdBean1.setObjects("李成龙");
                    MessageEntity.MessageRrdBean messageRrdBean2 = new MessageEntity.MessageRrdBean();
                    messageRrdBean2.setContent("三营全体官兵到体育馆集合");
                    messageRrdBean2.setModifyTime("2018/8/22");
                    messageRrdBean2.setObjects("张昌林");
                    MessageEntity.MessageRrdBean messageRrdBean3 = new MessageEntity.MessageRrdBean();
                    messageRrdBean3.setContent("一班全员到会议厅集合");
                    messageRrdBean3.setModifyTime("2018/8/23");
                    messageRrdBean3.setObjects("许三多");
                    MessageEntity.MessageRrdBean messageRrdBean4 = new MessageEntity.MessageRrdBean();
                    messageRrdBean4.setContent("一营全体领导到会议厅集合");
                    messageRrdBean4.setModifyTime("2018/8/24");
                    messageRrdBean4.setObjects("杨慧国");
                    list.add(0,messageRrdBean5);
                    list.add(1,messageRrdBean6);
                    list.add(2,messageRrdBean7);
                    list.add(3,messageRrdBean8);
                    list.add(4,messageRrdBean9);
                    list.add(5,messageRrdBean10);
                    list.add(6,messageRrdBean11);
                    list.add(7,messageRrdBean12);
                    list.add(8,messageRrdBean);
                    list.add(9,messageRrdBean1);
                    list.add(10,messageRrdBean2);
                    list.add(11,messageRrdBean3);
                    list.add(12,messageRrdBean4);
                    messageEntity.setMessageRrd(list);
                    entityList.addAll(messageEntity.getMessageRrd());
                }
                L.e(TAG,"onResponse"+response);
//                ivEmpty.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                lv.completeRefresh();
            }
        });
    }

    private void loadMore() {
//        hasMore = true;
//        beginNum = 1;
//        endNum = 6;
//        loadData(beginNum, endNum);
//        lv.completeRefresh();

        if (hasMore) {
            endNum -= 2;
            beginNum -= 2;
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
        Intent intent = new Intent(getActivity(), NotificationItemActivity.class);
        intent.putExtra("tab", "通知内容");
        intent.putExtra("content", adapter.getItem(position).getContent());
        intent.putExtra("modifyTime",adapter.getItem(position).getModifyTime());
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
            List<MessageEntity.MessageRrdBean> list = new ArrayList<>();
            for (MessageEntity.MessageRrdBean bean : baseEntityList) {
                if (bean.getContent().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (TimeUtil.getTimeFormatText(DataUtil.parseDateToText(bean.getModifyTime())).replace(" ", "").contains(key)) {
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
        beginNum = -2;
        endNum = 0;
        loadData(beginNum, endNum);
    }

    @Override
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }
}
