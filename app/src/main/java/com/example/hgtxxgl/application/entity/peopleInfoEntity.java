package com.example.hgtxxgl.application.entity;

import java.util.List;

//个人资料实体
public class PeopleInfoEntity {

    public List<PeopleInfoBean> getPeopleInfo() {
        return PeopleInfo;
    }

    public void setPeopleInfo(List<PeopleInfoBean> peopleInfo) {
        PeopleInfo = peopleInfo;
    }

    private List<PeopleInfoBean> PeopleInfo;

    public static class PeopleInfoBean {
        /**
         * AuthenticationNo : 3A447500
         * Authority : ?
         * CardNo : ?
         * GroupName : ?
         * IsAndroid : 1
         * LoginName : 81
         * ModifyTime : ?
         * Name : ?
         * No : ?
         * NoIndex : ?
         * Password : 1
         * PhoneNo : ?
         * Position : ?
         * RegisterTime : ?
         * Sex : ?
         * TelNo : ?
         * Unit : ?
         * Department : ?
         *  NoIndex  : ?
         */

        private String AuthenticationNo;
        private String Authority;//
        private String CardNo;//
        private String GroupName;//
        private String IsAndroid;
        private String LoginName;//
        private String ModifyTime;//
        private String Name;//
        private String No;//
        private String Password;//
        private String PhoneNo;//
        private String Position;//
        private String RegisterTime;//
        private String Sex;//
        private String TelNo;//
        private String Unit;//
        private String Department;//
        private String NoIndex;//
        private String bClosed;//

        public String getbClosed() {
            return bClosed;
        }

        public void setbClosed(String bClosed) {
            this.bClosed = bClosed;
        }

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String AuthenticationNo) {
            this.AuthenticationNo = AuthenticationNo;
        }

        public String getAuthority() {
            return Authority;
        }

        public void setAuthority(String Authority) {
            this.Authority = Authority;
        }

        public String getCardNo() {
            return CardNo;
        }

        public void setCardNo(String CardNo) {
            this.CardNo = CardNo;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String IsAndroid) {
            this.IsAndroid = IsAndroid;
        }

        public String getLoginName() {
            return LoginName;
        }

        public void setLoginName(String LoginName) {
            this.LoginName = LoginName;
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

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getPhoneNo() {
            return PhoneNo;
        }

        public void setPhoneNo(String PhoneNo) {
            this.PhoneNo = PhoneNo;
        }

        public String getPosition() {
            return Position;
        }

        public void setPosition(String Position) {
            this.Position = Position;
        }

        public String getRegisterTime() {
            return RegisterTime;
        }

        public void setRegisterTime(String RegisterTime) {
            this.RegisterTime = RegisterTime;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String Sex) {
            this.Sex = Sex;
        }

        public String getTelNo() {
            return TelNo;
        }

        public void setTelNo(String TelNo) {
            this.TelNo = TelNo;
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
    }
}
