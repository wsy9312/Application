package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/8/2.
 */

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

        public CarLeaveRrdBean() {
        }

        public CarLeaveRrdBean(String no, String approverNo, String carNo, String process, String result, String bCancel, String bFillup, String registerTime, String outTime, String inTime, String content, String actualOutTime, String actualInTime, String modifyTime, String noIndex) {
            No = no;
            ApproverNo = approverNo;
            CarNo = carNo;
            Process = process;
            Result = result;
            this.bCancel = bCancel;
            this.bFillup = bFillup;
            RegisterTime = registerTime;
            OutTime = outTime;
            InTime = inTime;
            Content = content;
            ActualOutTime = actualOutTime;
            ActualInTime = actualInTime;
            ModifyTime = modifyTime;
            NoIndex = noIndex;
        }

        public String getNo() {
            return No;
        }

        public void setNo(String No) {
            this.No = No;
        }

        public String getApproverNo() {
            return ApproverNo;
        }

        public void setApproverNo(String ApproverNo) {
            this.ApproverNo = ApproverNo;
        }

        public String getCarNo() {
            return CarNo;
        }

        public void setCarNo(String CarNo) {
            this.CarNo = CarNo;
        }

        public String getProcess() {
            return Process;
        }

        public void setProcess(String Process) {
            this.Process = Process;
        }

        public String getResult() {
            return Result;
        }

        public void setResult(String Result) {
            this.Result = Result;
        }

        public String getBCancel() {
            return bCancel;
        }

        public void setBCancel(String bCancel) {
            this.bCancel = bCancel;
        }

        public String getBFillup() {
            return bFillup;
        }

        public void setBFillup(String bFillup) {
            this.bFillup = bFillup;
        }

        public String getRegisterTime() {
            return RegisterTime;
        }

        public void setRegisterTime(String RegisterTime) {
            this.RegisterTime = RegisterTime;
        }

        public String getOutTime() {
            return OutTime;
        }

        public void setOutTime(String OutTime) {
            this.OutTime = OutTime;
        }

        public String getInTime() {
            return InTime;
        }

        public void setInTime(String InTime) {
            this.InTime = InTime;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getActualOutTime() {
            return ActualOutTime;
        }

        public void setActualOutTime(String ActualOutTime) {
            this.ActualOutTime = ActualOutTime;
        }

        public String getActualInTime() {
            return ActualInTime;
        }

        public void setActualInTime(String ActualInTime) {
            this.ActualInTime = ActualInTime;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public String getNoIndex() {
            return NoIndex;
        }

        public void setNoIndex(String NoIndex) {
            this.NoIndex = NoIndex;
        }
    }
}
