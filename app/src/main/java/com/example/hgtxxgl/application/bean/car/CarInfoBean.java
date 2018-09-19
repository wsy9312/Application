package com.example.hgtxxgl.application.bean.car;

import java.util.List;

//Api_Get_CarInfo
//得到车辆

public class CarInfoBean {

    private List<ApiGetCarInfoBean> Api_Get_CarInfo;

    public List<ApiGetCarInfoBean> getApi_Get_CarInfo() {
        return Api_Get_CarInfo;
    }

    public void setApi_Get_CarInfo(List<ApiGetCarInfoBean> Api_Get_CarInfo) {
        this.Api_Get_CarInfo = Api_Get_CarInfo;
    }

    public static class ApiGetCarInfoBean {
        /**
         * Owner1No :
         * Owner1Name :
         * Owner2No :
         * Owner2Name :
         * AuthenticationNo :
         * No :
         * IsAndroid : 1
         * ModifyTime : 0
         * RegisterTime : 0
         * NoIndex : 0
         * CarType : 0
         * SeatCount : 0
         * PurchaseDate : 0
         * MaintenancePeriodT : 0
         * MaintenancePeriodM : 0
         * BeginNum : 0
         * EndNum : 0
         * OutStatus : 0
         * TimeStamp : 0
         */

        private String Owner1No;
        private String Owner1Name;
        private String Owner2No;
        private String Owner2Name;
        private String AuthenticationNo;
        private String No;
        private String IsAndroid;
        private String ModifyTime;
        private String RegisterTime;
        private String NoIndex;
        private String CarType;
        private String SeatCount;
        private String PurchaseDate;
        private String MaintenancePeriodT;
        private String MaintenancePeriodM;
        private String BeginNum;
        private String EndNum;
        private String OutStatus;
        private String TimeStamp;

        public String getTimeStamp() {
            return TimeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            TimeStamp = timeStamp;
        }

        public String getOwner1No() {
            return Owner1No;
        }

        public void setOwner1No(String Owner1No) {
            this.Owner1No = Owner1No;
        }

        public String getOwner1Name() {
            return Owner1Name;
        }

        public void setOwner1Name(String Owner1Name) {
            this.Owner1Name = Owner1Name;
        }

        public String getOwner2No() {
            return Owner2No;
        }

        public void setOwner2No(String Owner2No) {
            this.Owner2No = Owner2No;
        }

        public String getOwner2Name() {
            return Owner2Name;
        }

        public void setOwner2Name(String Owner2Name) {
            this.Owner2Name = Owner2Name;
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

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String IsAndroid) {
            this.IsAndroid = IsAndroid;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public String getRegisterTime() {
            return RegisterTime;
        }

        public void setRegisterTime(String RegisterTime) {
            this.RegisterTime = RegisterTime;
        }

        public String getNoIndex() {
            return NoIndex;
        }

        public void setNoIndex(String NoIndex) {
            this.NoIndex = NoIndex;
        }

        public String getCarType() {
            return CarType;
        }

        public void setCarType(String CarType) {
            this.CarType = CarType;
        }

        public String getSeatCount() {
            return SeatCount;
        }

        public void setSeatCount(String SeatCount) {
            this.SeatCount = SeatCount;
        }

        public String getPurchaseDate() {
            return PurchaseDate;
        }

        public void setPurchaseDate(String PurchaseDate) {
            this.PurchaseDate = PurchaseDate;
        }

        public String getMaintenancePeriodT() {
            return MaintenancePeriodT;
        }

        public void setMaintenancePeriodT(String MaintenancePeriodT) {
            this.MaintenancePeriodT = MaintenancePeriodT;
        }

        public String getMaintenancePeriodM() {
            return MaintenancePeriodM;
        }

        public void setMaintenancePeriodM(String MaintenancePeriodM) {
            this.MaintenancePeriodM = MaintenancePeriodM;
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

        public String getOutStatus() {
            return OutStatus;
        }

        public void setOutStatus(String OutStatus) {
            this.OutStatus = OutStatus;
        }
    }
}
