package com.example.hgtxxgl.application.rest;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.attachment.GlideImageLoader;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

public abstract class CommonFragment extends Fragment implements HandInputGroup.Callback, RadioBarViewPager.OnCheckedChangeListener {
    public RecyclerView itemCont;
    private HolderAdapter adpter;
    private List<Group> groupList;
    private RelativeLayout stubEmpty;
    private boolean displayTabs = false;
    private HandToolbar toolbar;
    private String[] buttonsTitles = new String[]{};
    private ProgressDialog dialog;
    private CustomDatePicker customDatePicker;
    public RelativeLayout pb;
    private LinearLayout buttonll;
    private String tempIP;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    public String getTempIP() {
        return tempIP;
    }

    public List<Group> getGroup() {
        return groupList;
    }

    public void setDisplayTabs(boolean displayTabs) {
        this.displayTabs = displayTabs;
        if (groupList != null && displayTabs) {
            pager = (RadioBarViewPager) getView().findViewById(R.id.rb_viewpager);
            pager.setVisibility(View.VISIBLE);
            String preKey = "##";
            for (Group group : groupList) {
                String title = group.getTitle();
                if (title != null) {
                    if (!title.startsWith(preKey)) {
                        pager.add(title.split("-")[0]);
                    }
                    preKey = title.split("-")[0];
                }
            }
            pager.setOnCheckedChangeListener(this);
            pager.create();
        }
    }

    public void setGroup(List<Group> groupList) {
        this.groupList.clear();
        this.groupList.addAll(groupList);
    }

    @Override
    public boolean removeItemCallback(final int index, HandInputGroup inputGroup) {
        if (groupList.get(index).getDrawableRes() != null){
            groupList.get(index - 1).setDrawableRes(R.mipmap.add_detail3x);
        }
        groupList.remove(index);
        adpter.notifyItemRemoved(index-1);
        adpter.notifyItemRangeChanged(0,groupList.size());
        return true;
    }

    public void addGroup(Integer index, Group group) {
        if (groupList != null) {
            if (index != null && index > 0) {
                groupList.add(index, group);
            }
        }
    }

    public void setButtonsTitles(String[] buttonsTitles) {
        this.buttonsTitles = buttonsTitles;
        if (buttonsTitles != null && buttonsTitles.length > 0 && buttonsTitles.length < 5) {
            LinearLayout vsActionBar = (LinearLayout) layout.findViewById(R.id.vs_action_buttons);
            Button start = (Button) vsActionBar.findViewById(R.id.btn_start);
            Button save = (Button) vsActionBar.findViewById(R.id.btn_save);
            Button reject = (Button) vsActionBar.findViewById(R.id.btn_reject);
            Button approve = (Button) vsActionBar.findViewById(R.id.btn_approve);
            Button toother = (Button) vsActionBar.findViewById(R.id.btn_toother);
            Button toother1 = (Button) vsActionBar.findViewById(R.id.btn_toother1);
            Button toother2 = (Button) vsActionBar.findViewById(R.id.btn_toother2);
            Button again = (Button) vsActionBar.findViewById(R.id.btn_again);
            Button[] buttons = new Button[]{start,save,reject,approve,toother,toother1,toother2,again};
            List<String> strings = Arrays.asList(buttonsTitles);
            for (final Button button : buttons) {
                if (strings.contains(button.getText())){
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBottomButtonsClick(button.getText().toString(),groupList);
                        }
                    });
                }else {
                    button.setVisibility(View.GONE);
                }
            }
        }
    }

    public void setButtonllEnable(boolean eable){
        Button reject = (Button) buttonll.findViewById(R.id.btn_reject);
        reject.setEnabled(eable);
        Button approve = (Button) buttonll.findViewById(R.id.btn_approve);
        approve.setEnabled(eable);
        Button toother = (Button) buttonll.findViewById(R.id.btn_toother);
        toother.setEnabled(eable);
        Button toother1 = (Button) buttonll.findViewById(R.id.btn_toother1);
        toother1.setEnabled(eable);
        Button toother2 = (Button) buttonll.findViewById(R.id.btn_toother2);
        toother2.setEnabled(eable);
        Button start = (Button) buttonll.findViewById(R.id.btn_start);
        start.setEnabled(eable);
        Button save = (Button) buttonll.findViewById(R.id.btn_save);
        save.setEnabled(eable);
        Button again = (Button) buttonll.findViewById(R.id.btn_again);
        again.setEnabled(eable);
    }

    public String[] getButtonsTitles() {
        return buttonsTitles;
    }
    /**
     * @param title 标题
     * @return Group 集合
     * @usage 找到所有同名地组合框
     */
    public List<Group> getGroupsByTitle(String title) {
        List<Group> groups = new ArrayList<>();
        if (groupList != null && groupList.size() > 0) {
            for (Group g : groupList) {
                if (g.getTitle().equals(title)) {
                    groups.add(g);
                }
            }
            return groups;
        }
        return null;
    }

    public HandInputGroup.Holder getDisplayValueByKey(String key) {
        if (!key.isEmpty() && groupList != null) {
            for (int i = 0; i < groupList.size(); i++) {
                List<HandInputGroup.Holder> holders = groupList.get(i).getHolders();
                if (holders!=null){
                    for (int j = 0; j < holders.size(); j++) {
                        if (holders.get(j).getKey().equals(key)) {
                            return holders.get(j);
                        }
                    }
                }

            }
        }
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        tempIP = share.getString("tempIP", "IP address is empty");
        if (groupList == null) {
            groupList = new ArrayList<>();
        }
        if (getArguments() == null) {
            setGroup(getGroupList());
        }
    }

    @Override
    public void onClickItemContentSetter(HandInputGroup.Holder holder) {

    }

    @Override
    public void onDataChanged(HandInputGroup.Holder holder) throws ParseException {

    }

    @Override
    public void removeGroups(int index,HandInputGroup group) {
        if (group.getTitle().equals("明细信息")) {
            groupList.remove(index);
            adpter.notifyDataSetChanged();
            notifyDataSetChanged();
        }
    }

    public HandToolbar getToolbar() {
        return toolbar;
    }

    RadioBarViewPager pager;

    private RelativeLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_layout_container, container, false);
        initImagePicker();
        initWidget(layout);
        buttonll = (LinearLayout) layout.findViewById(R.id.vs_action_buttons);
        setButtonllEnable(getArguments() == null);
        toolbar = (HandToolbar) layout.findViewById(R.id.toolbar);
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.mainColor_blue);
        pb = (RelativeLayout) layout.findViewById(R.id.frag_pb);
        setPb(getArguments() != null);
        setToolbar(toolbar);
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setBackHome(true,0);
        stubEmpty = (RelativeLayout) layout.findViewById(R.id.vs_empty);
        stubEmpty.setVisibility(groupList == null || groupList.size() == 0 ? View.VISIBLE : View.GONE);
        stubEmpty.setVisibility(View.GONE);
        setButtonsTitles(getBottomButtonsTitles());
        if (getGroup() != null) {
            itemCont = (RecyclerView) layout.findViewById(R.id.frag_list_container);
            itemCont.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            itemCont.setItemAnimator(new DefaultItemAnimator());
            itemCont.addItemDecoration(new RecycleViewItemDecoration(getResources().getDimensionPixelSize(R.dimen.list_item_halving)));
            if (adpter == null) {
                adpter = new HolderAdapter();
                adpter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        if (adpter.getItemCount() == 0) {
                            stubEmpty.setVisibility(groupList == null ? View.VISIBLE : View.GONE);
//                            stubEmpty.setVisibility(View.GONE);
                        }
                    }
                });
            }
            itemCont.setAdapter(adpter);
        }
        return layout;
    }

    public void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(8);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    public void initWidget(RelativeLayout layout){

    }

    public void setPb(boolean able){
        pb.setVisibility(able? View.VISIBLE: View.GONE);
    }

    @Override
    public void onBarCodeChanged(int main, int index, HandInputGroup.Holder holder, String barcode, SubListLayout.ActionType type, int id) {

    }

    public void showDialog(String msg, @DrawableRes int resId) {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void updateDialog(String msg, @DrawableRes int resId) {
        if (dialog != null && dialog.isShowing()) {
            dialog.setMessage(msg);
        }
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    public abstract List<Group> getGroupList();
    /**
     * 设置标题栏
     *
     * @param toolbar 组合控件，设置它的参数
     */
    public abstract void setToolbar(HandToolbar toolbar);

    /**
     * 返回底部按钮设置
     *
     * @return 返回一个字符串数组，为空则不显示
     */
    public String[] getBottomButtonsTitles() {
        return null;
    }


    /**
     * 底部按钮的点击执行方法
     *
     * @param title  传出的点击的参数
     * @param groups 传出的所有数据
     */
    public void onBottomButtonsClick(String title, List<Group> groups) {

    }
    @Override
    public void FocusChange(HandInputGroup.Holder item, boolean hasFocus) {

    }

    public void onOneItemBottomDrawableResClick(int index) {

    }

    @Override
    public void onCheckedChanged(String title, int index) {
        for (int i = 0; i < groupList.size(); i++) {
            Group group = groupList.get(i);
            if (group == null) {
                return;
            }
            if (group.getTitle().startsWith(title)) {
                group.setVisible(true);
            } else {
                group.setVisible(false);
            }
        }
        notifyDataSetChanged();
    }

    protected interface OnSelectedResultCallback {
        void onSelected(Group ownGroup, HandInputGroup.Holder holder, int mainIndex, int itemIndex);
    }

    public void checkedButton(HandInputGroup.Holder holder){

    }

    public void checkedButton(HandInputGroup.Holder holder, final OnSelectedResultCallback callback){
        if (callback != null) {
            for (int i = 0; i < groupList.size(); i++) {
                if (groupList.get(i).getHolders() != null){
                    int inx = groupList.get(i).getHolders().indexOf(holder);
                    if (inx != -1) {
                        callback.onSelected(groupList.get(i), holder, i, inx);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void showSelector(final HandInputGroup.Holder holder, final String[] args) {
        showSelector(holder,args,null);
    }

    public void showSelector(final HandInputGroup.Holder holder, final String[] args, final OnSelectedResultCallback callback) {
        if (args == null || args.length == 0) {
            Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        int defaultIndex = -1;
        for (int i = 0; i < args.length; i++) {
            if (args[i].trim().equals(holder.getDispayValue())) {
                defaultIndex = i;
            }
        }
        final int finalDefaultIndex = defaultIndex;
        ActionSheetActivity.openActionSheet(getActivity(), holder.getKey(),args, args[defaultIndex == -1 ? 0 : defaultIndex], new ActionSheetActivity.OnResult() {
            @Override
            public void onResult(int index, String value) {
                if (finalDefaultIndex == index) return;
                holder.setDispayValue(value);
                if (callback != null) {
                    for (int i = 0; i < groupList.size(); i++) {
                        if (groupList.get(i).getHolders() != null){
                            int inx = groupList.get(i).getHolders().indexOf(holder);
                            if (inx != -1) {
                                callback.onSelected(groupList.get(i), holder, i, inx);
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    public void showDateTimePicker(final HandInputGroup.Holder holder,boolean bolean) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        // 回调接口，获得选中的时间
        initDatePicker(holder,bolean);
        if (holder.getRealValue().isEmpty()){
            customDatePicker.show(now);
        }else {
            customDatePicker.show(holder.getRealValue());
        }
        notifyDataSetChanged();
    }

    private void initDatePicker(final HandInputGroup.Holder holder, final boolean bolean) {
        customDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if (bolean){
                    holder.setDispayValue(time);
                }else {
                    holder.setDispayValue(time.split(" ")[0]);
                }
                try {
                    onDataChanged(holder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        }, "1986-01-01 00:00", "2986-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(bolean); // 是否显示时和分
        customDatePicker.setIsLoop(true); // 是否循环滚动
    }

    public void notifyDataSetChanged(int index) {
        if (adpter != null) {
            adpter.notifyItemChanged(index - 1);
            adpter.notifyItemInserted(index);
        }
    }

    public void notifyGroupChanged(final int startIndex, final int count) {
        if (adpter != null) {
            itemCont.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adpter.notifyItemRangeChanged(startIndex, count);
                }
            }, 10);
        }
    }

    public void notifyDataSetChanged() {
        if (adpter != null)
            adpter.notifyDataSetChanged();
        stubEmpty.setVisibility(View.GONE);
        if (groupList != null && groupList.size() <= 0) {
            stubEmpty.setVisibility(View.VISIBLE);
        } else if (groupList != null && groupList.size() > 0) {
            stubEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * 根组设置
     */
    public static class Group {
        private int id;
        private String title;
        private Integer drawableRes;
        private Boolean hasExpanded;
        private boolean hasDelete;
        private String v1;
        private String v2;
        private boolean rl;
        private String groupTopRightTitle;
        private List<HandInputGroup.Holder> holders;
        private boolean visible = true;
        private boolean isopen = true;
        private Object value;
        private String oldBarcode;
        private String drawable;

        public Group() {
        }

        public boolean gethasDelete(){
            return hasDelete;
        }

        public Group sethasDelete(boolean has){
            this.hasDelete = has;
            return this;
        }

        public boolean getisopen(){
            return this.isopen;
        }

        public Group setisopen(boolean isopen){
            this.isopen = isopen;
            return this;
        }

        public Object getValue() {
            return value;
        }

        public Group setValue(Object value) {
            this.value = value;
            return this;
        }
        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public Group(String title, Integer drawableRes, Boolean hasExpanded, String groupTopRightTitle, List<HandInputGroup.Holder> holders) {
            this.title = title;
            this.drawableRes = drawableRes;
            this.hasExpanded = hasExpanded;
            this.groupTopRightTitle = groupTopRightTitle;
            this.holders = holders;
            this.rl = false;
        }
        public Group(String title, Integer drawableRes, Boolean hasExpanded, String groupTopRightTitle, List<HandInputGroup.Holder> holders, String drawable) {
            this.title = title;
            this.drawableRes = drawableRes;
            this.hasExpanded = hasExpanded;
            this.groupTopRightTitle = groupTopRightTitle;
            this.holders = holders;
            this.rl = false;
            this.drawable = drawable;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Group setv1(String v1){
            this.v1 = v1;
            return this;
        }

        public String getV1(){
            return this.v1;
        }

        public Group setv2(String v2){
            this.v2 = v2;
            return this;
        }

        public String getv2(){
            return this.v2;
        }

        public Integer getDrawableRes() {
            return drawableRes;
        }

        public void setDrawableRes(Integer drawableRes) {
            this.drawableRes = drawableRes;
        }

        public Boolean getHasExpanded() {
            return hasExpanded;
        }

        public void setHasExpanded(Boolean hasExpanded) {
            this.hasExpanded = hasExpanded;
        }

        public Boolean getrl() {
            return rl;
        }

        public Group setrl(Boolean rl) {
            this.rl = rl;
            return this;
        }

        public String getGroupTopRightTitle() {
            return groupTopRightTitle;
        }

        public void setGroupTopRightTitle(String groupTopRightTitle) {
            this.groupTopRightTitle = groupTopRightTitle;
        }

        public List<HandInputGroup.Holder> getHolders() {
            return holders;
        }

        public void setHolders(List<HandInputGroup.Holder> holders) {
            this.holders = holders;
        }

        /**
         * 此方法用于找同名的Holder，因此需要在每个group中找到
         *
         * @param key holder的key
         * @return holder
         */
        public HandInputGroup.Holder getHolderByKey(String key) {
            if (holders != null && holders.size() > 0) {
                for (int i = 0; i < holders.size(); i++) {
                    if (holders.get(i).getKey().equals(key)) {
                        return holders.get(i);
                    }
                }

            }
            return null;
        }

        public Group setoldBarcode(String oldBarcode) {
            this.oldBarcode = oldBarcode;
            return this;
        }

        public String getOldBarcode() {
            return oldBarcode;
        }

        public Group setDrawable(String drawable) {
            this.drawable = drawable;
            return this;
        }

        public String getDrawable() {
            return drawable;
        }
    }

    private class HolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private String title;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                RecyclerView rvHistory = new RecyclerView(getActivity());
                rvHistory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rvHistory.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new HistoryViewHolder(rvHistory);
            } else {
                HandInputGroup inputGroup = new HandInputGroup(getActivity());
                inputGroup.setLayoutParams(new RecyclerView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                return new ContentViewHolder(inputGroup);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == 1) {
                RecyclerView listView = ((HistoryViewHolder) holder).rvHistory;
                Object value = groupList.get(position).getValue();
                if (listView.getAdapter() == null) {
//                    listView.setAdapter(new HistortFlowAdapter(getActivity(),
//                            (HisDataBean) value));
                }
                if (displayTabs) {
                    setVisibility(listView, groupList.get(position).isVisible());
                }

                return;
            }else if (holder.getItemViewType() == 2){
                HandInputGroup group = ((ContentViewHolder) holder).inputGroup;
                group.setIndex(position);
                Group temp = groupList.get(position);
                if (groupList.get(position).getDrawableRes() != null) {
                    group.setGroupBottomDrawableRes(temp.getDrawableRes(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onOneItemBottomDrawableResClick(holder.getAdapterPosition());
                        }
                    });
                } else {
                    group.setGroupBottomDrawableRes(null, null);
                }
                group.setHolders(temp.getHolders());
                group.setTopRightValue(temp.getGroupTopRightTitle());
                group.setrl(temp.getrl());
                group.setisopen(temp.getisopen());
                group.setv1(temp.getV1());
                group.setv2(temp.getv2());
                group.setoldBarcode(temp.getOldBarcode());
                group.sethasDelete(temp.gethasDelete());
                group.setdrawable(temp.getDrawable());
                if (displayTabs){
                    title = temp.getTitle().split("-")[1];
                }else {
                    title = temp.getTitle();
                }

                if (title != null) {
                    group.setTitle(title);
                }
                if (displayTabs) {
                    setVisibility(group, groupList.get(position).isVisible());
                }
                group.setCallback(CommonFragment.this);
            }
            HandInputGroup group = ((ContentViewHolder) holder).inputGroup;
            group.setIndex(position);
            Group temp = groupList.get(position);
            if (groupList.get(position).getDrawableRes() != null) {
                group.setGroupBottomDrawableRes(temp.getDrawableRes(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOneItemBottomDrawableResClick(holder.getAdapterPosition());
                    }
                });
            } else {
                group.setGroupBottomDrawableRes(null, null);
            }
            group.setHolders(temp.getHolders());
            group.setTopRightValue(temp.getGroupTopRightTitle());
            group.setisopen(temp.getisopen());
            group.setoldBarcode(temp.getOldBarcode());
            group.sethasDelete(temp.gethasDelete());
            if (displayTabs){
                title = temp.getTitle().split("-")[1];
            }else {
                title = temp.getTitle();
            }

            if (title != null) {
                group.setTitle(title);
            }
            if (displayTabs) {
                setVisibility(group, groupList.get(position).isVisible());
            }
            group.setCallback(CommonFragment.this);
        }

        private void setVisibility(View itemView, boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (groupList.get(position).getTitle().equals("流程摘要-摘要")) {
                return 1;
            }else if(groupList.get(position).getTitle().equals("流程摘要-摘要内容")){
                return 2;
            }else {
                return 0;
            }
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        class ContentViewHolder extends RecyclerView.ViewHolder {

            HandInputGroup inputGroup;

            public ContentViewHolder(View itemView) {
                super(itemView);
                inputGroup = (HandInputGroup) itemView;
            }
        }

        class HistoryViewHolder extends RecyclerView.ViewHolder {
            RecyclerView rvHistory;

            public HistoryViewHolder(View itemView) {
                super(itemView);
                rvHistory = (RecyclerView) itemView;
            }
        }
    }

    @Override
    public void onHolderTextChanged(int main, int index, HandInputGroup.Holder holder) {
        //输入框改变事件回调，重写此方法
    }

    public void uploadSingleFileAndData(final Uri uri, final String uuid, final String workflowType, final String fileGroupName, final String fileGroupValue){
        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showDialog("提交数据中，请稍等...", 0);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                Map<String, Object> map = CommonValues.getCommonParams(getActivity());
                map.put("fileGroupName", fileGroupName);
                map.put("workflowIdentifier", uuid);
                map.put("workflowType", workflowType);
                map.put("createBy", "");
                map.put("fileGroupValue", fileGroupValue);
                int index = 1;
//                publishProgress(index++);
//                        String path = HttpManager.getRealPathFromUri(uri, getActivity());
                String path = FileUtils.getPath(getActivity(), uri);
                String name = path.substring(path.lastIndexOf("/") + 1);
                map.put("orignFileName", name);
                map.put("fileName", name.substring(0, name.lastIndexOf(".")));
                map.put("picLocalId", index);
                String fileBase64 = HttpManager.uri2Base64(uri, getActivity());
                if (fileBase64 == null) {
                    return false;
                }
                map.put("fileBase64", fileBase64);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                if (!aVoid){
                    updateDialog("上传失败!", 0);
                    itemCont.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                        }
                    },1000);
                }else{
                    updateDialog("上传成功!", 0);
                }
            }

            @Override
            protected void onProgressUpdate(final Integer... values) {
                super.onProgressUpdate(values);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDialog("正在上传...",0);
                    }
                });
            }
        }.execute();
    }

    public void uploadFileAndData(final HashSet<Uri> paths, final String title, final Map allParams, final String uuid, final String workflowType, final String fileGroupName, final String fileGroupValue, final String applyUrl) {
        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showDialog("提交数据中，请稍等...", 0);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                Map<String, Object> map = CommonValues.getCommonParams(getActivity());
                map.put("fileGroupName", fileGroupName);
                map.put("workflowIdentifier", uuid);
                map.put("workflowType", workflowType);
                map.put("createBy", "");
                map.put("fileGroupValue", fileGroupValue);
                int index = 0;
                if (paths != null && paths.size() > 0) {
                    for (Uri uri : paths) {
                        publishProgress(index++);
                        String path = FileUtils.getPath(getActivity(), uri);
                        String name = path.substring(path.lastIndexOf("/") + 1);
                        String fileName = name.substring(0, name.lastIndexOf("."));
                        String fileBase64 = HttpManager.uri2Base64(uri, getActivity());
                        map.put("orignFileName", name);
                        map.put("fileName", fileName);
                        map.put("picLocalId", index);
                        map.put("fileBase64", fileBase64);
//                        HttpManager.getInstance().requestResultFormSync(CommonValues.REQ_ADD_ATTACHMENT, map, AddAttachmentEntity.class);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                if (aVoid) {
//                    applySaveOrStart(applyUrl, allParams, title);
                } else {
                    updateDialog("上传失败", 0);
                    itemCont.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                        }
                    },1000);
                }
            }

            @Override
            protected void onProgressUpdate(final Integer... values) {
                super.onProgressUpdate(values);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDialog(String.format("正在上传：%s/%s", values[0] + 1, paths.size()), 0);
                    }
                });
            }
        }.execute();
    }

    private HashSet<Uri> removeDuplicate(HashSet<Uri> set) {
        Map<String, Uri> map = new HashMap<String, Uri>();
        HashSet<Uri> tempSet = new HashSet<Uri>();
        for(Uri uri : set) {
            String path = FileUtils.getPath(getActivity(), uri);
            String name = path.substring(path.lastIndexOf("/") + 1);
            if(map.get(name) == null ) {
                map.put(name,uri);
            } else {
                tempSet.add(uri);
            }
        }
        set.removeAll(tempSet);
        return set;
    }

    public String isOver(List<Group> groups) {
        for (int i = 0; i < groups.size(); i++) {
            List<HandInputGroup.Holder> holders = groups.get(i).getHolders();
            if (holders != null) {
                for (int j = 0; j < holders.size(); j++) {
                    if (holders.get(j).getHasIndicator() && holders.get(j).getType()!= HandInputGroup.VALUE_TYPE.TEXT&& holders.get(j).getRealValue().isEmpty()) {
                        return ":" + holders.get(j).getKey();
                    }
                }
            }
        }
        return null;
    }

    public int getitemnum(HandInputGroup.Holder holder) {
        List<Group> group = getGroup();
        for (int i = 0; i < group.size(); i++) {
            List<HandInputGroup.Holder> holders = group.get(i).getHolders();
            for (int j = 0; j < holders.size(); j++) {
                if (holders.get(j) == holder) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getday(String leave, String returnt) {
        long LeaveTime = getTimes(leave);
        long ReturnTime = getTimes(returnt);
        if (ReturnTime < LeaveTime) {
            return -1;
        } else {
            int day = (int) ((ReturnTime - LeaveTime) / 1000 / 60 /60);
            return day;
        }

    }

    public long getTimes(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        long time = 0;
        try {
            time = sdf.parse(data).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public void onHolderChangedOver(int main, int index,HandInputGroup.Holder holder) {

    }

    @Override
    public void onPause() {
        super.onPause();
        setButtonllEnable(true);
    }

//    public void getBottomTitles() {
//        Map<String, Object> param = CommonValues.getCommonParams(getActivity());
//        param.put("barCode", getArguments().getString("barCode"));
//        param.put("sn", getArguments().getString("SN"));
//        param.put("identifier",getArguments().getString("WorkflowIdentifier"));
//        HttpManager.getInstance().requestResultForm(CommonValues.GET_PROCESS_ACTION, param, ProcessActionEmtity.class, new HttpManager.ResultCallback<ProcessActionEmtity>() {
//            @Override
//            public void onSuccess(String content, final ProcessActionEmtity entity) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (entity != null && entity.getRetData() != null) {
//                            if (entity.getCode().equals("100")) {
//                                List<String> retData = entity.getRetData();
//                                String[] bottomString = new String[retData.size()];
//                                for (int i = 0; i < retData.size(); i++) {
//                                    if (retData.get(i).equals("拒绝")){
//                                        bottomString[i] = "驳回";
//                                    }else if (retData.get(i).equals("转签")){
//                                        bottomString[i] = "转办";
//                                    }else {
//                                        bottomString[i] = retData.get(i);
//                                    }
//                                }
//                                setButtonsTitles(bottomString);
//                                setButtonllEnable(true);
//                                notifyDataSetChanged();
//                                return;
//                            }
//                        } else {
//                            Toast.makeText(getActivity(), entity.getMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(String content) {
//            }
//
//            @Override
//            public void onResponse(String response) {
//
//            }
//        });
//    }

}
