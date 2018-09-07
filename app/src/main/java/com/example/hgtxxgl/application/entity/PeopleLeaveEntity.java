package com.example.hgtxxgl.application.entity;

import java.util.List;

//人员外出实体
public class PeopleLeaveEntity {

    private List<PeopleLeaveRrdBean> PeopleLeaveRrd;

    public List<PeopleLeaveRrdBean> getPeopleLeaveRrd() {
        return PeopleLeaveRrd;
    }

    public void setPeopleLeaveRrd(List<PeopleLeaveRrdBean> PeopleLeaveRrd) {
        this.PeopleLeaveRrd = PeopleLeaveRrd;
    }

    @Override
    public String toString() {
        return "PeopleLeaveEntity{" +
                "PeopleLeaveRrd=" + PeopleLeaveRrd +
                '}';
    }

    public static class PeopleLeaveRrdBean {
        /**
         * AuthenticationNo :
         * No :
         * Name :
         * IsAndroid :
         * RegisterTime :
         * OutTime :
         * InTime :
         * Content :
         * ActualOutTime :
         * ActualInTime :
         * ModifyTime :
         * Process :
         * bCancel :
         * bFillup :
         * NoIndex :
         * CurrentApproverNo :当前审批人ID(第一个;第二个;第三个...)
         * Result :审批结果 1:同意，0:不同意（最终结果）2：被退回
         * bMessage :
         * OutType :
         * ApproverNo :
         * ApproverName :
         * HisAnnotation :历史批注
         * Curannotation :当前批注
         * OutStatus :外出状态：0：在岗，1：外出 默认0
         * Destination :去向
         * CurResult :当前次审批结果 0：不同意，1：退回充填，2：同意，提交上级，3：同意并结束
         * BeginNum :
         * EndNum :
         * Unit :单位(自加)
         * Department :部门(自加)
         * VacationAddr :疗养地址
         * VacationDays :假期天数
         * JourneyDays :路途天数
         */

        private String AuthenticationNo;
        private String No;
        private String Name;
        private String IsAndroid;
        private String RegisterTime;
        private String OutTime;
        private String InTime;
        private String Content;
        private String ActualOutTime;
        private String ActualInTime;
        private String ModifyTime;
        private String Process;
        private String bCancel;
        private String bFillup;
        private String NoIndex;
        private String CurrentApproverNo;
        private String Result;
        private String bMessage;
        private String OutType;
        private String ApproverNo;
        private String ApproverName;
        private String HisAnnotation;
        private String Curannotation;
        private String OutStatus;
        private String Destination;
        private String CurResult;
        private String BeginNum;
        private String EndNum;
        private String Unit;
        private String Department;
        private String VacationAddr;
        private String VacationDays;
        private String JourneyDays;

        public String getVacationAddr() {
            return VacationAddr;
        }

        public void setVacationAddr(String vacationAddr) {
            VacationAddr = vacationAddr;
        }

        public String getVacationDays() {
            return VacationDays;
        }

        public void setVacationDays(String vacationDays) {
            VacationDays = vacationDays;
        }

        public String getJourneyDays() {
            return JourneyDays;
        }

        public void setJourneyDays(String journeyDays) {
            JourneyDays = journeyDays;
        }

        @Override
        public String toString() {
            return "PeopleLeaveRrdBean{" +
                    "AuthenticationNo='" + AuthenticationNo + '\'' +
                    ", No='" + No + '\'' +
                    ", Name='" + Name + '\'' +
                    ", IsAndroid='" + IsAndroid + '\'' +
                    ", RegisterTime='" + RegisterTime + '\'' +
                    ", OutTime='" + OutTime + '\'' +
                    ", InTime='" + InTime + '\'' +
                    ", Content='" + Content + '\'' +
                    ", ActualOutTime='" + ActualOutTime + '\'' +
                    ", ActualInTime='" + ActualInTime + '\'' +
                    ", ModifyTime='" + ModifyTime + '\'' +
                    ", Process='" + Process + '\'' +
                    ", bCancel='" + bCancel + '\'' +
                    ", bFillup='" + bFillup + '\'' +
                    ", NoIndex='" + NoIndex + '\'' +
                    ", CurrentApproverNo='" + CurrentApproverNo + '\'' +
                    ", Result='" + Result + '\'' +
                    ", bMessage='" + bMessage + '\'' +
                    ", OutType='" + OutType + '\'' +
                    ", ApproverNo='" + ApproverNo + '\'' +
                    ", ApproverName='" + ApproverName + '\'' +
                    ", HisAnnotation='" + HisAnnotation + '\'' +
                    ", Curannotation='" + Curannotation + '\'' +
                    ", OutStatus='" + OutStatus + '\'' +
                    ", Destination='" + Destination + '\'' +
                    ", CurResult='" + CurResult + '\'' +
                    ", BeginNum='" + BeginNum + '\'' +
                    ", EndNum='" + EndNum + '\'' +
                    ", Unit='" + Unit + '\'' +
                    ", Department='" + Department + '\'' +
                    '}';
        }

        public String getDepartment() {
            return Department;
        }

        public void setDepartment(String department) {
            Department = department;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String unit) {
            Unit = unit;
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

        public String getProcess() {
            return Process;
        }

        public void setProcess(String Process) {
            this.Process = Process;
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

        public String getCurrentApproverNo() {
            return CurrentApproverNo;
        }

        public void setCurrentApproverNo(String CurrentApproveNo) {
            this.CurrentApproverNo = CurrentApproveNo;
        }

        public String getResult() {
            return Result;
        }

        public void setResult(String Result) {
            this.Result = Result;
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

        public String getApproverNo() {
            return ApproverNo;
        }

        public void setApproverNo(String ApproveNo) {
            this.ApproverNo = ApproveNo;
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

        public String getApproverName() {
            return ApproverName;
        }

        public void setApproverName(String approverName) {
            ApproverName = approverName;
        }

    }
}
