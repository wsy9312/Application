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

    public static class CarInfoBean {
        /**
         * AuthenticationNo :
         * No : 
         * IsAndroid :
         * Owner1No :
         * Owner2No :
         * ModifyTime :
         * RegisterTime :
         * GroupName :
         * NoIndex :
         * CarType :
         * SeatCount :
         * PurchaseDate :
         * MaintenancePeriodT :
         * MaintenancePeriodM :
         */

        private String AuthenticationNo;
        private String No;
        private String IsAndroid;
        private String Owner1No;
        private String Owner2No;
        private String ModifyTime;
        private String RegisterTime;
        private String GroupName;
        private String NoIndex;
        private String CarType;
        private String SeatCount;
        private String PurchaseDate;
        private String MaintenancePeriodT;
        private String MaintenancePeriodM;

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

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
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
    }
}
