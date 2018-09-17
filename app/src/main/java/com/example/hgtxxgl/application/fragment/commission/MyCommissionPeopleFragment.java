package com.example.hgtxxgl.application.fragment.commission;

//人员审批列表
public class MyCommissionPeopleFragment /*extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener*/{

//    private int beginNum = 1;
//    private int endNum = 10;
//    private boolean hasMore = true;
//    private TextView ivEmpty;
//    private ProgressBar pb;
//    private static final String TAG = "MyCommissionPeopleFragment";
//    private String tempIP;
//    SimpleListView lv;
//    private String authenticationNo;
//
//    public MyCommissionPeopleFragment() {
//
//    }
//
//    public static MyCommissionPeopleFragment newInstance(int tabIndex) {
//        Bundle args = new Bundle();
//        MyCommissionPeopleFragment fragment = new MyCommissionPeopleFragment();
//        args.putInt(DetailFragment.ARG_TAB, tabIndex);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> entityList = new ArrayList<>();
//    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> baseEntityList;
//
//    ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean> adapter = new ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean>
//            ((ArrayList<PeopleLeaveEntity.PeopleLeaveRrdBean>) entityList, R.layout.layout_commission) {
//        @Override
//        public void bindView(ViewHolder holder, PeopleLeaveEntity.PeopleLeaveRrdBean bean) {
//                holder.setText(R.id.tv_title, "申请人:" + bean.getName());
//                holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
//                holder.setText(R.id.tv_sketch, "申请事由:" + (bean.getContent().isEmpty() ? "无" : bean.getContent()));
//                if (bean.getApproverNo().contains(authenticationNo)){
//                    holder.setImageResource(R.id.image_flow,R.drawable.ic_approved);
//                    holder.setTextColor(R.id.tv_sketch, Color.rgb(0,128,0));
//                    holder.setTextColor(R.id.tv_title, Color.rgb(0,128,0));
//                }else {
//                    holder.setImageResource(R.id.image_flow,R.drawable.ic_no_approve);
//                    holder.setTextColor(R.id.tv_sketch, Color.rgb(214,16,24));
//                    holder.setTextColor(R.id.tv_title, Color.rgb(214,16,24));
//                }
//        }
//    };
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        authenticationNo = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo();
//        loadData(beginNum, endNum);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.main_listview_libmain, null, false);
//        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
//        ivEmpty = (TextView) view.findViewById(iv_empty);
//        ivEmpty.setText(getString(R.string.no_current_record));
//        pb = (ProgressBar) view.findViewById(R.id.mycommission_pb);
//        lv.setAdapter(adapter);
//        adapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                if (adapter.getCount() == 0) {
//                    ivEmpty.setVisibility(View.VISIBLE);
//                } else {
//                    ivEmpty.setVisibility(View.GONE);
//                }
//                pb.setVisibility(View.GONE);
//                super.onChanged();
//            }
//        });
//        lv.setOnRefreshListener(this);
//        lv.setOnItemClickListener(this);
//        return view;
//    }
//
//    public void loadData(final int beginNum, final int endNum) {
//        if (callback != null) {
//            callback.onLoadData();
//        }
//        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
//        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
//        peopleLeaveRrdBean.setNo("?");
//        peopleLeaveRrdBean.setAuthenticationNo(authenticationNo);
//        peopleLeaveRrdBean.setIsAndroid("1");
//        peopleLeaveRrdBean.setRegisterTime("?");
//        peopleLeaveRrdBean.setOutTime("?");
//        peopleLeaveRrdBean.setInTime("?");
//        peopleLeaveRrdBean.setContent("?");
//        peopleLeaveRrdBean.setModifyTime("?");
//        peopleLeaveRrdBean.setProcess("?");
//        peopleLeaveRrdBean.setBCancel("0");
//        peopleLeaveRrdBean.setBFillup("?");
//        peopleLeaveRrdBean.setNoIndex("?");
//        peopleLeaveRrdBean.setResult("?");
//        peopleLeaveRrdBean.setOutType("?");
//        peopleLeaveRrdBean.setApproverNo("?");
//        peopleLeaveRrdBean.setHisAnnotation("?");
//        peopleLeaveRrdBean.setDestination("?");
//        peopleLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
//        peopleLeaveRrdBean.setEndNum(String.valueOf(endNum));
//        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
//        list.add(peopleLeaveRrdBean);
//        peopleLeaveEntity.setPeopleLeaveRrd(list);
//        String json = new Gson().toJson(peopleLeaveEntity);
//        String s = "get " + json;
//        L.e(TAG+"MyCommissionPeopleFragment",s);
////        String url = CommonValues.BASE_URL;
////        String url = ApplicationApp.getIP();
//        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
//        tempIP = share.getString("tempIP", "IP address is empty");
//        HttpManager.getInstance().requestResultForm(tempIP, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
//            @Override
//            public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
//                if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
//                    if (beginNum == 1 && endNum == 10){
//                        entityList.clear();
//                    }
//                    L.e(TAG+"MyCommissionPeopleFragment",peopleLeaveEntity1.toString());
//                    hasMore = true;
//                    entityList.addAll(peopleLeaveEntity1.getPeopleLeaveRrd());
//                    adapter.notifyDataSetChanged();
//                } else {
//                    hasMore = false;
//                }
//                pb.setVisibility(View.GONE);
//                ivEmpty.setVisibility(View.GONE);
//                lv.completeRefresh();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        lv.completeRefresh();
//                        pb.setVisibility(View.GONE);
//                    }
//                });
//            }
//            @Override
//            public void onResponse(String response) {
//                ivEmpty.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (lv.getCurrentState() == 2) return;
//        position -= 1;
//        checkDetail(position, PageConfig.PAGE_COMMISSION_PEOPLE_DETAIL);
//    }
//
//    private void checkDetail(int position, int pageApplyBleave) {
//        Intent intent = new Intent(getActivity(), ItemActivity.class);
//        intent.putExtra(PageConfig.PAGE_CODE, pageApplyBleave);
//        Bundle bundle = new Bundle();
//        bundle.putString("no", adapter.getItem(position).getNo());
//        bundle.putString("name", adapter.getItem(position).getName());
//        bundle.putString("outtime",adapter.getItem(position).getOutTime());
//        bundle.putString("intime", adapter.getItem(position).getInTime());
//        bundle.putString("content", adapter.getItem(position).getContent());
//        bundle.putString("process", adapter.getItem(position).getProcess());
//        bundle.putString("modifyTime",adapter.getItem(position).getModifyTime());
//        bundle.putString("bcancel",adapter.getItem(position).getBCancel());
//        bundle.putString("bfillup",adapter.getItem(position).getBFillup());
//        bundle.putString("noindex",adapter.getItem(position).getNoIndex());
//        bundle.putInt("item",position);
//        intent.putExtra("data", bundle);
//        startActivityForResult(intent,CommonValues.MYCOMM);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CommonValues.MYCOMM){
//            if (beginNum == 1 && endNum == 10){
//                entityList.clear();
//            }
//            loadData(1,10);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    private DetailFragment.DataCallback callback;
//
//    public MyCommissionPeopleFragment setCallback(DetailFragment.DataCallback callback) {
//        this.callback = callback;
//        return this;
//    }
//
//
//    public void filter(String key) {
//        if (lv == null || key == null) return;
//        if (entityList != null && !key.equals("")) {
//            if (baseEntityList == null) {
//                baseEntityList = new ArrayList<>();
//                baseEntityList.addAll(entityList);
//            }
//            List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
//            for (PeopleLeaveEntity.PeopleLeaveRrdBean bean : baseEntityList) {
//                if (bean.getName().replace(" ", "").contains(key)){
//                    list.add(bean);
//                }
//                if ((bean.getApproverNo().contains(authenticationNo)?"已审批":"未审批")
//                        .replace(" ","").contains(key)){
//                    list.add(bean);
//                }
//                if ((DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss")).replace(" ", "").contains(key)) {
//                    list.add(bean);
//                }
//                if ((bean.getContent().isEmpty()?"无":bean.getContent()).replace(" ", "").contains(key)) {
//                    list.add(bean);
//                }
//            }
//            entityList.clear();
//            entityList.addAll(list);
//            adapter.notifyDataSetChanged();
//        } else if (entityList != null) {
//            if (baseEntityList != null) {
//                entityList.clear();
//                entityList.addAll(baseEntityList);
//                adapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    @Override
//    public void onPullRefresh() {
//        hasMore = true;
//        beginNum = 1;
//        endNum = 10;
//        loadData(beginNum, endNum);
//        lv.completeRefresh();
//    }
//
//    @Override
//    public void onLoadingMore() {
//        if (hasMore) {
//            beginNum += 10;
//            endNum += 10;
//            loadData(beginNum, endNum);
//        }
//        lv.completeRefresh();
//        ivEmpty.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onScrollOutside() {
//
//    }

}
