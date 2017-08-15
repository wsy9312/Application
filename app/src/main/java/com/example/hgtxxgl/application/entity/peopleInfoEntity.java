package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/7/21.
 * 个人资料实体
 */

public class PeopleInfoEntity {

    private List<PeopleInfoBean> PeopleInfo;

    public List<PeopleInfoBean> getPeopleInfo() {
        return PeopleInfo;
    }

    public void setPeopleInfo(List<PeopleInfoBean> PeopleInfo) {
        this.PeopleInfo = PeopleInfo;
    }

    public static class PeopleInfoBean {
        /**
         * No : 个人唯一编号
         * Name : 王城
         * CardNo : 321281199521233221
         * Position : 一垒士兵
         * Sex : 性别
         * Unit : 炊事班一组
         * ArmyGroup : 221
         * PhoneNo : 123456782
         * TelNo : 12345678902
         * GroupName : B
         * LoginName : xc123
         * Password : xc123
         * Authority : 权限
         * ModifyTime : 最后修改时间
         * RegisterTime : 登记时间
         */

        private String No;
        private String Name;
        private String CardNo;
        private String Position;
        private String Sex;
        private String Unit;
        private String ArmyGroup;
        private String PhoneNo;
        private String TelNo;
        private String GroupName;
        private String LoginName;
        private String Password;
        private String Authority;
        private String ModifyTime;
        private String RegisterTime;
        private String NoIndex;
        private String AuthenticationNo;
        private String IsAndroid;

        public PeopleInfoBean() {
        }

        public PeopleInfoBean(String no, String name, String cardNo, String position, String sex, String unit, String armyGroup, String phoneNo, String telNo, String groupName, String loginName, String password, String authority, String modifyTime, String registerTime, String noIndex, String authenticationNo, String isAndroid) {
            No = no;
            Name = name;
            CardNo = cardNo;
            Position = position;
            Sex = sex;
            Unit = unit;
            ArmyGroup = armyGroup;
            PhoneNo = phoneNo;
            TelNo = telNo;
            GroupName = groupName;
            LoginName = loginName;
            Password = password;
            Authority = authority;
            ModifyTime = modifyTime;
            RegisterTime = registerTime;
            NoIndex = noIndex;
            AuthenticationNo = authenticationNo;
            IsAndroid = isAndroid;
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

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getCardNo() {
            return CardNo;
        }

        public void setCardNo(String CardNo) {
            this.CardNo = CardNo;
        }

        public String getPosition() {
            return Position;
        }

        public void setPosition(String Position) {
            this.Position = Position;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String Sex) {
            this.Sex = Sex;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }

        public String getArmyGroup() {
            return ArmyGroup;
        }

        public void setArmyGroup(String ArmyGroup) {
            this.ArmyGroup = ArmyGroup;
        }

        public String getPhoneNo() {
            return PhoneNo;
        }

        public void setPhoneNo(String PhoneNo) {
            this.PhoneNo = PhoneNo;
        }

        public String getTelNo() {
            return TelNo;
        }

        public void setTelNo(String TelNo) {
            this.TelNo = TelNo;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public String getLoginName() {
            return LoginName;
        }

        public void setLoginName(String LoginName) {
            this.LoginName = LoginName;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getAuthority() {
            return Authority;
        }

        public void setAuthority(String Authority) {
            this.Authority = Authority;
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

        public void setNoIndex(String noIndex) {
            NoIndex = noIndex;
        }

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String authenticationNo) {
            AuthenticationNo = authenticationNo;
        }

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String isAndroid) {
            IsAndroid = isAndroid;
        }

    }
}
