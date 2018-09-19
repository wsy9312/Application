package com.example.hgtxxgl.application.bean.car;

import java.util.List;

//Api_Get_MyApproveForCarHis
//得到我的审批(车辆)(我审批过的)

public class CarApproveFinishBean {

    private List<ApiGetMyApproveForCarHisBean> Api_Get_MyApproveForCarHis;

    public List<ApiGetMyApproveForCarHisBean> getApi_Get_MyApproveForCarHis() {
        return Api_Get_MyApproveForCarHis;
    }

    public void setApi_Get_MyApproveForCarHis(List<ApiGetMyApproveForCarHisBean> Api_Get_MyApproveForCarHis) {
        this.Api_Get_MyApproveForCarHis = Api_Get_MyApproveForCarHis;
    }

    public static class ApiGetMyApproveForCarHisBean {
        /**
         * ActualInTime :
         * ActualOutTime :
         * ApproverName : 部门B负责人1;部门B领导1;
         * ApproverNo : B456D901;610A69C4;
         * Content : 1123
         * Count : 40
         * CurrentApproverName : 单位B领导2;单位B负责人1;
         * CurrentApproverNo : 1FC7C8E0;D0350C13;
         * Destination : 123
         * HisAnnotation : 123
         * InTime : 2018-09-10 09:12:10
         * ModifyTime : 2018-09-10 09:22:04
         * Name : 部门B员工1
         * No : 54969542
         * NoIndex : 35
         * OutStatus : 0
         * OutTime : 2018-09-10 09:12:07
         * OutType : 事假申请
         * Process : 2
         * RegisterTime : 2018-09-10 09:12:39
         * Result : 0
         * bCancel : 0
         * bFillup : 0
         * bMessage : 0
         * AuthenticationNo : 0
         * IsAndroid : 0
         * CurResult : 0
         * BeginNum : 0
         * EndNum : 0
         * Unit : 0
         * Department : 0
         * Curannotation : 0
         * TimeStamp : 0
         * CarNo : 0
         * DriverNo : 0
         * LeaderNo : 0
         * DriverName : 0
         * LeaderName : 0
         */

        private String ActualInTime;
        private String ActualOutTime;
        private String ApproverName;
        private String ApproverNo;
        private String Content;
        private String Count;
        private String CurrentApproverName;
        private String CurrentApproverNo;
        private String Destination;
        private String HisAnnotation;
        private String InTime;
        private String ModifyTime;
        private String Name;
        private String No;
        private String NoIndex;
        private String OutStatus;
        private String OutTime;
        private String OutType;
        private String Process;
        private String RegisterTime;
        private String Result;
        private String bCancel;
        private String bFillup;
        private String bMessage;
        private String AuthenticationNo;
        private String IsAndroid;
        private String CurResult;
        private String BeginNum;
        private String EndNum;
        private String Unit;
        private String Department;
        private String Curannotation;
        private String TimeStamp;
        private String CarNo;
        private String DriverNo;
        private String LeaderNo;
        private String DriverName;
        private String LeaderName;

        public String getActualInTime() {
            return ActualInTime;
        }

        public void setActualInTime(String ActualInTime) {
            this.ActualInTime = ActualInTime;
        }

        public String getActualOutTime() {
            return ActualOutTime;
        }

        public void setActualOutTime(String ActualOutTime) {
            this.ActualOutTime = ActualOutTime;
        }

        public String getApproverName() {
            return ApproverName;
        }

        public void setApproverName(String ApproverName) {
            this.ApproverName = ApproverName;
        }

        public String getApproverNo() {
            return ApproverNo;
        }

        public void setApproverNo(String ApproverNo) {
            this.ApproverNo = ApproverNo;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String Count) {
            this.Count = Count;
        }

        public String getCurrentApproverName() {
            return CurrentApproverName;
        }

        public void setCurrentApproverName(String CurrentApproverName) {
            this.CurrentApproverName = CurrentApproverName;
        }

        public String getCurrentApproverNo() {
            return CurrentApproverNo;
        }

        public void setCurrentApproverNo(String CurrentApproverNo) {
            this.CurrentApproverNo = CurrentApproverNo;
        }

        public String getDestination() {
            return Destination;
        }

        public void setDestination(String Destination) {
            this.Destination = Destination;
        }

        public String getHisAnnotation() {
            return HisAnnotation;
        }

        public void setHisAnnotation(String HisAnnotation) {
            this.HisAnnotation = HisAnnotation;
        }

        public String getInTime() {
            return InTime;
        }

        public void setInTime(String InTime) {
            this.InTime = InTime;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getNo() {
            return No;
        }

        public void setNo(String No) {
            this.No = No;
        }

        public String getNoIndex() {
            return NoIndex;
        }

        public void setNoIndex(String NoIndex) {
            this.NoIndex = NoIndex;
        }

        public String getOutStatus() {
            return OutStatus;
        }

        public void setOutStatus(String OutStatus) {
            this.OutStatus = OutStatus;
        }

        public String getOutTime() {
            return OutTime;
        }

        public void setOutTime(String OutTime) {
            this.OutTime = OutTime;
        }

        public String getOutType() {
            return OutType;
        }

        public void setOutType(String OutType) {
            this.OutType = OutType;
        }

        public String getProcess() {
            return Process;
        }

        public void setProcess(String Process) {
            this.Process = Process;
        }

        public String getRegisterTime() {
            return RegisterTime;
        }

        public void setRegisterTime(String RegisterTime) {
            this.RegisterTime = RegisterTime;
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

        public String getBMessage() {
            return bMessage;
        }

        public void setBMessage(String bMessage) {
            this.bMessage = bMessage;
        }

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String AuthenticationNo) {
            this.AuthenticationNo = AuthenticationNo;
        }

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String IsAndroid) {
            this.IsAndroid = IsAndroid;
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

        public void setBeginNum(String BeginNum) {
            this.BeginNum = BeginNum;
        }

        public String getEndNum() {
            return EndNum;
        }

        public void setEndNum(String EndNum) {
            this.EndNum = EndNum;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }

        public String getDepartment() {
            return Department;
        }

        public void setDepartment(String Department) {
            this.Department = Department;
        }

        public String getCurannotation() {
            return Curannotation;
        }

        public void setCurannotation(String Curannotation) {
            this.Curannotation = Curannotation;
        }

        public String getTimeStamp() {
            return TimeStamp;
        }

        public void setTimeStamp(String TimeStamp) {
            this.TimeStamp = TimeStamp;
        }

        public String getCarNo() {
            return CarNo;
        }

        public void setCarNo(String CarNo) {
            this.CarNo = CarNo;
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

        public String getDriverName() {
            return DriverName;
        }

        public void setDriverName(String DriverName) {
            this.DriverName = DriverName;
        }

        public String getLeaderName() {
            return LeaderName;
        }

        public void setLeaderName(String LeaderName) {
            this.LeaderName = LeaderName;
        }
    }
}
