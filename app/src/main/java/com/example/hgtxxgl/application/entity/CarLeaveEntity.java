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
         * AuthenticationNo :
         * No :
         * Name :
         * IsAndroid :
         * ApproverNo :
         * ApproverName :
         * CurrentApproveNo :
         * CarNo :
         * Process :
         * Result :审批结果 1:同意，0:不同意（最终结果）2：被退回
         * bCancel :
         * bFillup :
         * RegisterTime :
         * OutTime :
         * InTime :
         * Content :
         * ActualOutTime :
         * ActualInTime :
         * ModifyTime :
         * NoIndex :
         * bMessage :
         * OutType :
         * HisAnnotation :
         * Curannotation :
         * DriverNo :驾驶员的No
         * LeaderNo :带车干部的No
         * DriverName :驾驶员的Name
         * LeaderName :带车干部的Name
         * OutStatus :
         * Destination :
         * CurResult :
         * BeginNum :
         * EndNum :
         */

        private String AuthenticationNo;
        private String No;
        private String Name;
        private String IsAndroid;
        private String ApproverNo;
        private String ApproverName;
        private String CurrentApproveNo;
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
        private String bMessage;
        private String OutType;
        private String HisAnnotation;
        private String Curannotation;
        private String DriverNo;
        private String LeaderNo;
        private String DriverName;
        private String LeaderName;
        private String OutStatus;
        private String Destination;
        private String CurResult;
        private String BeginNum;
        private String EndNum;

        public String getDriverName() {
            return DriverName;
        }

        public void setDriverName(String driverName) {
            DriverName = driverName;
        }

        public String getLeaderName() {
            return LeaderName;
        }

        public void setLeaderName(String leaderName) {
            LeaderName = leaderName;
        }

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String AuthenticationNo) {
            this.AuthenticationNo = AuthenticationNo;
        }

        public String getNo() {
            return No;
        }

        public void setNo(String No) {
            this.No = No;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String IsAndroid) {
            this.IsAndroid = IsAndroid;
        }

        public String getApproverNo() {
            return ApproverNo;
        }

        public void setApproverNo(String ApproverNo) {
            this.ApproverNo = ApproverNo;
        }

        public String getApproverName() {
            return ApproverName;
        }

        public void setApproverName(String approverName) {
            ApproverName = approverName;
        }

        public String getCurrentApproveNo() {
            return CurrentApproveNo;
        }

        public void setCurrentApproveNo(String CurrentApproveNo) {
            this.CurrentApproveNo = CurrentApproveNo;
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

        public String getBMessage() {
            return bMessage;
        }

        public void setBMessage(String bMessage) {
            this.bMessage = bMessage;
        }

        public String getOutType() {
            return OutType;
        }

        public void setOutType(String OutType) {
            this.OutType = OutType;
        }

        public String getHisAnnotation() {
            return HisAnnotation;
        }

        public void setHisAnnotation(String HisAnnotation) {
            this.HisAnnotation = HisAnnotation;
        }

        public String getCurannotation() {
            return Curannotation;
        }

        public void setCurannotation(String Curannotation) {
            this.Curannotation = Curannotation;
        }

        public String getDriverNo() {
            return DriverNo;
        }

        public void setDriverNo(String DriverNo) {
            this.DriverNo = DriverNo;
        }

        public String getLeaderNo() {
            return LeaderNo;
        }

        public void setLeaderNo(String LeaderNo) {
            this.LeaderNo = LeaderNo;
        }

        public String getOutStatus() {
            return OutStatus;
        }

        public void setOutStatus(String OutStatus) {
            this.OutStatus = OutStatus;
        }

        public String getDestination() {
            return Destination;
        }

        public void setDestination(String Destination) {
            this.Destination = Destination;
        }

        public String getCurResult() {
            return CurResult;
        }

        public void setCurResult(String CurResult) {
            this.CurResult = CurResult;
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
    }
}
