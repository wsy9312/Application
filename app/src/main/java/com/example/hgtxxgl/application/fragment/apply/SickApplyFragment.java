package com.example.hgtxxgl.application.fragment.apply;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.example.hgtxxgl.application.attachment.ImagePickerAdapter;
import com.example.hgtxxgl.application.bean.LoginInfoBean;
import com.example.hgtxxgl.application.bean.PeopleApplyBean;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class SickApplyFragment extends CommonFragment/* implements ImagePickerAdapter.OnRecyclerViewItemClickListener*/{
    private final static String TAG = "SickApplyFragment";
    private String name;
    private String unit;
    private String department;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    private LoginInfoBean.ApiAddLoginBean loginBean;

    ArrayList<ImageItem> images = null;

    public SickApplyFragment() {
    }

    public static SickApplyFragment newInstance() {
        SickApplyFragment sickApplyFragment = new SickApplyFragment();
        return sickApplyFragment;
    }

    public static SickApplyFragment newInstance(Bundle bundle) {
        SickApplyFragment fragment = new SickApplyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBean = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                    Log.e(TAG, "onActivityResult: "+
                            selImageList.get(0).mimeType+"-"
                            +selImageList.get(0).size+"-"
                            +selImageList.get(0).path+"-"
                            +selImageList.get(0).name+"-"
                            +selImageList.get(0).addTime+"-"
                            +selImageList.get(0).height+"-"
                            +selImageList.get(0).width);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }

    @Override
    public List<Group> getGroupList() {
        if (!ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().isEmpty()){
            name = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName();
            unit = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getUnit();
            department = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getDepartment();
        }
        List<CommonFragment.Group> groups = new ArrayList<>();
        List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
        baseHolder.add(new HandInputGroup.Holder("申请人",false,false,name,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("所属单位",false,false,unit,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("所属部门",false,false,department,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("申请类型",false,false,"病假申请",HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("离队时间",true,false,"/请选择",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("归队时间",true,false,"/请选择",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("疗养地址",true,false,"/请输入疗养地址",HandInputGroup.VALUE_TYPE.TEXTFILED));
        baseHolder.add(new HandInputGroup.Holder("去向",true,false,"/请输入去向",HandInputGroup.VALUE_TYPE.TEXTFILED));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("事由",true,false,"/请输入病假申请事由",HandInputGroup.VALUE_TYPE.BIG_EDIT));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        groups.add(0,new CommonFragment.Group("基本信息", null,true,null,baseHolder));
        return groups;
    }

    @Override
    public void setToolbar(HandToolbar toolbar) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("病假申请");
        toolbar.setTitleSize(16);
    }

    @Override
    public String[] getBottomButtonsTitles() {
        return new String[]{"提 交"};
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBottomButtonsClick(final String title, final List<CommonFragment.Group> groups) {
        if (title.equals("提 交")){
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setMessage("是否确认提交?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String over = isOver(groups);
                    if (over != null){
                        ToastUtil.showToast(getContext(),over+"不能为空!");
                        setButtonllEnable(true);
                    }else {
                        //申请人ID
                        String realValueNO = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo();
                        String name = getDisplayValueByKey("申请人").getRealValue();
                        String unit = getDisplayValueByKey("所属单位").getRealValue();
                        String department = getDisplayValueByKey("所属部门").getRealValue();
                        String applicantType = getDisplayValueByKey("申请类型").getRealValue();
                        String leaveTime = getDisplayValueByKey("离队时间").getRealValue();
                        String returnTime = getDisplayValueByKey("归队时间").getRealValue();
                        String vacationAddr  = getDisplayValueByKey("疗养地址").getRealValue();
                        String argument = getDisplayValueByKey("事由").getRealValue();
                        String goDirection  = getDisplayValueByKey("去向").getRealValue();
//                        String bFillup = getDisplayValueByKey("是否后补申请").getRealValue();
                        PeopleApplyBean.ApiApplyPeopleLeaveBean peopleApplyBean = new PeopleApplyBean.ApiApplyPeopleLeaveBean();
                        peopleApplyBean.setDestination(goDirection);
                        peopleApplyBean.setNo(realValueNO);
                        peopleApplyBean.setOutType(applicantType);
                        peopleApplyBean.setOutTime(leaveTime);
                        peopleApplyBean.setInTime(returnTime);
                        peopleApplyBean.setContent(argument);
                        peopleApplyBean.setVacationAddr(vacationAddr);
//                        peopleLeaveRrdBean.setBFillup(bFillup.equals("否")?"0":"1");
                        peopleApplyBean.setAuthenticationNo(loginBean.getAuthenticationNo());
                        peopleApplyBean.setIsAndroid("1");
                        peopleApplyBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
                        String json = new Gson().toJson(peopleApplyBean);
                        String s1 = "Api_Apply_PeopleLeave " + json;
                        Log.e(TAG,s1);
                        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleApplyBean.class, new HttpManager.ResultNewCallback<PeopleApplyBean>() {
                            @Override
                            public void onSuccess(String json, PeopleApplyBean peopleApplyBean) throws Exception {
                                if (peopleApplyBean.getApi_Apply_PeopleLeave().get(0) == null){
                                    show("提交成功");
                                    getActivity().finish();
                                }else{
                                    show("提交失败");
                                    getActivity().finish();
                                }
                            }

                            @Override
                            public void onError(String msg) throws Exception {

                            }

                            @Override
                            public void onResponse(String response) throws Exception {

                            }

                            @Override
                            public void onBefore(Request request, int id) throws Exception {

                            }

                            @Override
                            public void onAfter(int id) throws Exception {

                            }

                            @Override
                            public void inProgress(float progress, long total, int id) throws Exception {

                            }


                        });
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    notifyDataSetChanged();
                }
            });
            builder.create().show();
        }
    }

    public void show(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getContext(),msg);
            }
        });
    }

    @Override
    public void onClickItemContentSetter(final HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.DATE) {
            showDateTimePicker(holder,true);
        } else if (holder.getKey().equals("是否取消请假")){
            showSelector(holder,new String[]{"是","否"});
        } /*else if (holder.getKey().equals("是否后补申请")){
            showSelector(holder,new String[]{"是","否"});
        }*/
    }

    @Override
    public void onDataChanged(HandInputGroup.Holder holder) throws ParseException {
        CommonFragment.Group group = getGroup().get(0);
        if (holder.getKey().equals("离队时间")){
            HandInputGroup.Holder holder1 = group.getHolderByKey("归队时间");
            if (!holder1.getRealValue().isEmpty()){
                String leave = holder.getRealValue();
                String returnt = holder1.getRealValue();
                int getday = getday(leave, returnt);
                if (getday == -1) {
                    holder.setDispayValue("");
                    ToastUtil.showToast(getContext(),"归队时间不能在离队时间之前!");
                }
            }
        }else if (holder.getKey().equals("归队时间")){
            HandInputGroup.Holder holder1 = group.getHolderByKey("离队时间");
            if (!holder1.getRealValue().isEmpty()){
                String returnt = holder.getRealValue();
                String leave = holder1.getRealValue();
                int getday = getday(leave, returnt);
                if (getday == -1) {
                    holder.setDispayValue("");
                    ToastUtil.showToast(getContext(),"归队时间不能在离队时间之前!");
                }
            }
        }
    }

//    @Override
//    public void initWidget(RelativeLayout layout) {
//        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
//        selImageList = new ArrayList<>();
//        adapter = new ImagePickerAdapter(getActivity(), selImageList, maxImgCount);
//        adapter.setOnItemClickListener(this);
//
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
//    }

//    @Override
//    public void onItemClick(View view, int position) {
//        switch (position) {
//            case IMAGE_ITEM_ADD:
//                //打开选择,本次允许选择的数量
//                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
//                Intent intent1 = new Intent(getActivity(), ImageGridActivity.class);
//                /* 如果需要进入选择的时候显示已经选中的图片，
//                 * 详情请查看ImagePickerActivity
//                 * */
//                //intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
//                startActivityForResult(intent1, REQUEST_CODE_SELECT);
//                break;
//            default:
//                //打开预览
//                Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
//                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
//                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
//                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
//                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
//                break;
//        }
//    }
}
