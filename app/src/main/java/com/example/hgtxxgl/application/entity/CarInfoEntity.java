package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2018/6/9.
 */

public class CarInfoEntity {

    private List<CarInfoBean> CarInfo;

    public List<CarInfoBean> getCarInfo() {
        return CarInfo;
    }

    public void setCarInfo(List<CarInfoBean> CarInfo) {
        this.CarInfo = CarInfo;
    }

    @Override
    public String toString() {
        return "CarInfoEntity{" +
                "CarInfo=" + CarInfo +
                '}';
    }

    public static class CarInfoBean {
        /**
         * AuthenticationNo :
         * No : 
         * IsAndroid :
         * Owner1No :
         * Owner1Name :
         * Owner2No :
         * Owner2Name :
         * ModifyTime :
         * RegisterTime :
         * NoIndex :
         * CarType :
         * SeatCount :
         * PurchaseDate :
         * MaintenancePeriodT :
         * MaintenancePeriodM :
         * BeginNum :
         * EndNum :
         */
        private String AuthenticationNo;
        private String No;
        private String IsAndroid;
        private String Owner1No;
        private String Owner1Name;
        private String Owner2No;
        private String Owner2Name;
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

        @Override
        public String toString() {
            return "CarInfoBean{" +
                    "AuthenticationNo='" + AuthenticationNo + '\'' +
                    ", No='" + No + '\'' +
                    ", IsAndroid='" + IsAndroid + '\'' +
                    ", Owner1No='" + Owner1No + '\'' +
                    ", Owner1Name='" + Owner1Name + '\'' +
                    ", Owner2No='" + Owner2No + '\'' +
                    ", Owner2Name='" + Owner2Name + '\'' +
                    ", ModifyTime='" + ModifyTime + '\'' +
                    ", RegisterTime='" + RegisterTime + '\'' +
                    ", NoIndex='" + NoIndex + '\'' +
                    ", CarType='" + CarType + '\'' +
                    ", SeatCount='" + SeatCount + '\'' +
                    ", PurchaseDate='" + PurchaseDate + '\'' +
                    ", MaintenancePeriodT='" + MaintenancePeriodT + '\'' +
                    ", MaintenancePeriodM='" + MaintenancePeriodM + '\'' +
                    ", BeginNum='" + BeginNum + '\'' +
                    ", EndNum='" + EndNum + '\'' +
                    '}';
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

        public String getOwner1No() {
            return Owner1No;
        }

        public void setOwner1No(String Owner1No) {
            this.Owner1No = Owner1No;
        }

        public String getOwner2No() {
            return Owner2No;
        }

        public void setOwner2No(String Owner2No) {
            this.Owner2No = Owner2No;
        }

        public String getOwner1Name() {
            return Owner1Name;
        }

        public void setOwner1Name(String owner1Name) {
            Owner1Name = owner1Name;
        }

        public String getOwner2Name() {
            return Owner2Name;
        }

        public void setOwner2Name(String owner2Name) {
            Owner2Name = owner2Name;
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
