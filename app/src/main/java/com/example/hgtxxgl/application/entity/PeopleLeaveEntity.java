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
         * No : 申请人
         * Approver1No : 审批人1
         * Approver2No : 审批人2
         * Approver3No : 审批人3
         * Approver4No : 审批人4
         * Approver5No : 审批人5
         * RegisterTime : 申请时间
         * OutTime : 预计外出时间
         * InTime : 预计归来时间
         * Content : 请假原因
         * ActualOutTime : 实际外出时间
         * ActualInTime : 实际归来时间
         * ModifyTime : 最后修改时间
         * MultiLevelResult : 审批结果
         * Process : 审批状态
         * LevelNum : 审批级数
         * bCancel : 是否取消请假
         * bFillup : 是否后补请假
         * NoIndex : 序号
         * BeginNum : 1
         * EndNum : 10
         */

        private String No;
        private String Result;
        private String CurrentApproveNo;
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
        private String BeginNum;
        private String EndNum;

        public PeopleLeaveRrdBean() {
        }

        public PeopleLeaveRrdBean(String no, String result, String currentApproveNo, String approver1No, String approver2No, String approver3No, String approver4No, String approver5No, String registerTime, String outTime, String inTime, String content, String actualOutTime, String actualInTime, String modifyTime, String multiLevelResult, String process, String levelNum, String bCancel, String bFillup, String noIndex, String beginNum, String endNum) {
            No = no;
            Result = result;
            CurrentApproveNo = currentApproveNo;
            Approver1No = approver1No;
            Approver2No = approver2No;
            Approver3No = approver3No;
            Approver4No = approver4No;
            Approver5No = approver5No;
            RegisterTime = registerTime;
            OutTime = outTime;
            InTime = inTime;
            Content = content;
            ActualOutTime = actualOutTime;
            ActualInTime = actualInTime;
            ModifyTime = modifyTime;
            MultiLevelResult = multiLevelResult;
            Process = process;
            LevelNum = levelNum;
            this.bCancel = bCancel;
            this.bFillup = bFillup;
            NoIndex = noIndex;
            BeginNum = beginNum;
            EndNum = endNum;
        }

        //审批
        public PeopleLeaveRrdBean(String currentApproveNo, String no, String approver1No, String approver2No, String approver3No, String approver4No, String approver5No, String registerTime, String outTime, String inTime, String content, String actualInTime, String actualOutTime, String modifyTime, String multiLevelResult, String process, String levelNum, String bCancel, String bFillup, String noIndex, String beginNum, String endNum) {
            CurrentApproveNo = currentApproveNo;
            No = no;
            Approver1No = approver1No;
            Approver2No = approver2No;
            Approver3No = approver3No;
            Approver4No = approver4No;
            Approver5No = approver5No;
            RegisterTime = registerTime;
            OutTime = outTime;
            InTime = inTime;
            Content = content;
            ActualInTime = actualInTime;
            ActualOutTime = actualOutTime;
            ModifyTime = modifyTime;
            MultiLevelResult = multiLevelResult;
            Process = process;
            LevelNum = levelNum;
            this.bCancel = bCancel;
            this.bFillup = bFillup;
            NoIndex = noIndex;
            BeginNum = beginNum;
            EndNum = endNum;
        }

        //申请和详情
        public PeopleLeaveRrdBean(String no, String approver1No, String approver2No, String approver3No, String approver4No, String approver5No, String registerTime, String outTime, String inTime, String content, String actualOutTime, String actualInTime, String modifyTime, String multiLevelResult, String process, String levelNum, String bCancel, String bFillup, String noIndex, String beginNum, String endNum) {
            No = no;
            Approver1No = approver1No;
            Approver2No = approver2No;
            Approver3No = approver3No;
            Approver4No = approver4No;
            Approver5No = approver5No;
            RegisterTime = registerTime;
            OutTime = outTime;
            InTime = inTime;
            Content = content;
            ActualOutTime = actualOutTime;
            ActualInTime = actualInTime;
            ModifyTime = modifyTime;
            MultiLevelResult = multiLevelResult;
            Process = process;
            LevelNum = levelNum;
            this.bCancel = bCancel;
            this.bFillup = bFillup;
            NoIndex = noIndex;
            BeginNum = beginNum;
            EndNum = endNum;
        }

        public String getResult() {
            return Result;
        }

        public void setResult(String result) {
            Result = result;
        }

        public String getNo() {
            return No;
        }

        public void setNo(String No) {
            this.No = No;
        }

        public String getCurrentApproveNo() {
            return CurrentApproveNo;
        }

        public void setCurrentApproveNo(String currentApproveNo) {
            CurrentApproveNo = currentApproveNo;
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

        public String getBeginNum() {
            return BeginNum;
        }

        public void setBeginNum(String BeginNum) {
            this.BeginNum = BeginNum;
        }

        public String getEndNum() {
            return EndNum;
        }

        public void setEndNum(String EndNum) {
            this.EndNum = EndNum;
        }
    }
}
