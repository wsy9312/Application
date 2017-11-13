package com.example.hgtxxgl.application.entity;

import java.util.List;
//车辆信息类
public class CarLeaveEntity {

    private List<CarLeaveRrdBean> CarLeaveRrd;

    public List<CarLeaveRrdBean> getCarLeaveRrd() {
        return CarLeaveRrd;
    }

    public void setCarLeaveRrd(List<CarLeaveRrdBean> CarLeaveRrd) {
        this.CarLeaveRrd = CarLeaveRrd;
    }

    public static class CarLeaveRrdBean {
        /**
         * No : 申请人ID.非空
         * ApproverNo : 审批人ID
         * CarNo : 申请车辆号牌.非空
         * Process : 0：审批中，1：审批结束.非空
         * Result : 审批结果 1:同意，0:不同意
         * bCancel : 取消请假 0:正常/1:取消.默认0
         * bFillup : 是否后补请假0：否，1：是.默认0
         * RegisterTime : 申请时间.系统生成
         * OutTime : 预计外出时间.非空
         * InTime : 预计归来时间.非空
         * Content : 外出原因
         * ActualOutTime : 实际外出时间
         * ActualInTime : 实际归来时间
         * ModifyTime : 最后修改时间.系统生成
         * NoIndex : 序号.系统生成.系统生成
         */

        private String No;
        private String Name;
        private String Onduty;
        private String ApproverNo;
        private String CarNo;
        private String Process;
        private String Result;
        private String bCancel;
        private String bFillup;
        private String RegisterTime;
        private String OutTime;
        private String InTime;
        private String Content;
        private String ActualOutTime;
        private String ActualInTime;
        private String ModifyTime;
        private String NoIndex;
        private String BeginNum;
        private String EndNum;
        private String AuthenticationNo;
        private String IsAndroid;

        public CarLeaveRrdBean() {
        }

        public String getNo() {
            return No;
        }

        public void setNo(String no) {
            No = no;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getOnduty() {
            return Onduty;
        }

        public void setOnduty(String onduty) {
            Onduty = onduty;
        }

        public String getApproverNo() {
            return ApproverNo;
        }

        public void setApproverNo(String approverNo) {
            ApproverNo = approverNo;
        }

        public String getCarNo() {
            return CarNo;
        }

        public void setCarNo(String carNo) {
            CarNo = carNo;
        }

        public String getProcess() {
            return Process;
        }

        public void setProcess(String process) {
            Process = process;
        }

        public String getResult() {
            return Result;
        }

        public void setResult(String result) {
            Result = result;
        }

        public String getbCancel() {
            return bCancel;
        }

        public void setbCancel(String bCancel) {
            this.bCancel = bCancel;
        }

        public String getbFillup() {
            return bFillup;
        }

        public void setbFillup(String bFillup) {
            this.bFillup = bFillup;
        }

        public String getRegisterTime() {
            return RegisterTime;
        }

        public void setRegisterTime(String registerTime) {
            RegisterTime = registerTime;
        }

        public String getOutTime() {
            return OutTime;
        }

        public void setOutTime(String outTime) {
            OutTime = outTime;
        }

        public String getInTime() {
            return InTime;
        }

        public void setInTime(String inTime) {
            InTime = inTime;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public String getActualOutTime() {
            return ActualOutTime;
        }

        public void setActualOutTime(String actualOutTime) {
            ActualOutTime = actualOutTime;
        }

        public String getActualInTime() {
            return ActualInTime;
        }

        public void setActualInTime(String actualInTime) {
            ActualInTime = actualInTime;
        }

        public String getNoIndex() {
            return NoIndex;
        }

        public void setNoIndex(String noIndex) {
            NoIndex = noIndex;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String modifyTime) {
            ModifyTime = modifyTime;
        }

        public String getBeginNum() {
            return BeginNum;
        }

        public void setBeginNum(String beginNum) {
            BeginNum = beginNum;
        }

        public String getEndNum() {
            return EndNum;
        }

        public void setEndNum(String endNum) {
            EndNum = endNum;
        }

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String authenticationNo) {
            AuthenticationNo = authenticationNo;
        }

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String isAndroid) {
            IsAndroid = isAndroid;
        }
    }
}
