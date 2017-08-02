package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/8/2.
 */

public class PeopleLeaveEntity {

    private List<PeopleLeaveRrdBean> PeopleLeaveRrd;

    public List<PeopleLeaveRrdBean> getPeopleLeaveRrd() {
        return PeopleLeaveRrd;
    }

    public void setPeopleLeaveRrd(List<PeopleLeaveRrdBean> PeopleLeaveRrd) {
        this.PeopleLeaveRrd = PeopleLeaveRrd;
    }

    public static class PeopleLeaveRrdBean {
        /**
         * No : 申请人ID.非空
         * Approver1No : 审批人1ID
         * Approver2No : 审批人2ID
         * Approver3No : 审批人3ID
         * Approver4No : 审批人4ID
         * Approver5No : 审批人5ID
         * RegisterTime : 申请时间.系统生成
         * OutTime : 预计外出时间.非空
         * InTime : 预计归来时间.非空
         * Content : 请假原因
         * ActualOutTime : 实际外出时间
         * ActualInTime : 实际归来时间
         * ModifyTime : 最后修改时间.系统生成
         * MultiLevelResult : 审批结果.非空
         * Process : 0：审批中，1：审批结束.非空.默认0
         * LevelNum : 审批级数.非空.默认1
         * bCancel : 取消请假 0:正常/1:取消.默认0
         * bFillup : 是否后补请假0：否，1：是.默认0
         * NoIndex : 序号.系统生成
         */

        private String No;
        private String Approver1No;
        private String Approver2No;
        private String Approver3No;
        private String Approver4No;
        private String Approver5No;
        private String RegisterTime;
        private String OutTime;
        private String InTime;
        private String Content;
        private String ActualOutTime;
        private String ActualInTime;
        private String ModifyTime;
        private String MultiLevelResult;
        private String Process;
        private String LevelNum;
        private String bCancel;
        private String bFillup;
        private String NoIndex;

        public String getNo() {
            return No;
        }

        public void setNo(String No) {
            this.No = No;
        }

        public String getApprover1No() {
            return Approver1No;
        }

        public void setApprover1No(String Approver1No) {
            this.Approver1No = Approver1No;
        }

        public String getApprover2No() {
            return Approver2No;
        }

        public void setApprover2No(String Approver2No) {
            this.Approver2No = Approver2No;
        }

        public String getApprover3No() {
            return Approver3No;
        }

        public void setApprover3No(String Approver3No) {
            this.Approver3No = Approver3No;
        }

        public String getApprover4No() {
            return Approver4No;
        }

        public void setApprover4No(String Approver4No) {
            this.Approver4No = Approver4No;
        }

        public String getApprover5No() {
            return Approver5No;
        }

        public void setApprover5No(String Approver5No) {
            this.Approver5No = Approver5No;
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

        public String getMultiLevelResult() {
            return MultiLevelResult;
        }

        public void setMultiLevelResult(String MultiLevelResult) {
            this.MultiLevelResult = MultiLevelResult;
        }

        public String getProcess() {
            return Process;
        }

        public void setProcess(String Process) {
            this.Process = Process;
        }

        public String getLevelNum() {
            return LevelNum;
        }

        public void setLevelNum(String LevelNum) {
            this.LevelNum = LevelNum;
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

        public String getNoIndex() {
            return NoIndex;
        }

        public void setNoIndex(String NoIndex) {
            this.NoIndex = NoIndex;
        }
    }
}
