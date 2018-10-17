package com.example.hgtxxgl.application.bean.login;

import java.util.List;

//Api_Get_MyInfoSim
//得到本人简单信息

public class PeopleInfoBean {

    private List<ApiGetMyInfoSimBean> Api_Get_MyInfoSim;

    public List<ApiGetMyInfoSimBean> getApi_Get_MyInfoSim() {
        return Api_Get_MyInfoSim;
    }

    public void setApi_Get_MyInfoSim(List<ApiGetMyInfoSimBean> Api_Get_MyInfoSim) {
        this.Api_Get_MyInfoSim = Api_Get_MyInfoSim;
    }

    public static class ApiGetMyInfoSimBean {
        /**
         * AuthenticationNo : 权限NO
         * Authority : 权限 0：不是管理员，1：是管理员 默认 0
         * CardNo : 身份证
         * IsAndroid : 是否Android
         * LoginName : 用户名
         * ModifyTime : 修改时间
         * Name : 名字
         * No : 1
         * NoIndex : 1
         * Password : 666666
         * PhoneNo : 123
         * Position : 123
         * RegisterTime : 123
         * Sex : 1
         * TelNo : 123
         * Unit : 123
         * Department : 1
         * IMSI : 1122
         * IMEI : 222111
         * OutStatus : 1
         * RetireTime : 22
         * bClosed : 1
         * TimeStamp : 1536283273
         * TFSerNo : 123
         * "EditMenuAuth" : 1, 菜单管理权限
         * "CheckInApplyAuth" : 0, 外来申请进入权限
         * "CheckOutForCarAuth" : 0, 车辆外出申请权限
         * "CheckOutForPersonAuth" : 0, 人物外出申请权限(非本人)
         * "CheckStatisticsAuth" : 0, 查看统计数据权限(领导)
         */

        private String AuthenticationNo;
        private String Authority;
        private String CardNo;
        private String IsAndroid;
        private String LoginName;
        private String ModifyTime;
        private String Name;
        private String No;
        private String NoIndex;
        private String Password;
        private String PhoneNo;
        private String Position;
        private String RegisterTime;
        private String Sex;
        private String TelNo;
        private String Unit;
        private String Department;
        private String IMSI;
        private String IMEI;
        private String OutStatus;
        private String RetireTime;
        private String bClosed;
        private String TimeStamp;
        private String TFSerNo;
        private String EditMenuAuth;
        private String CheckInApplyAuth;
        private String CheckOutForCarAuth;
        private String CheckOutForPersonAuth;
        private String CheckStatisticsAuth;

        public String getEditMenuAuth() {
            return EditMenuAuth;
        }

        public void setEditMenuAuth(String editMenuAuth) {
            EditMenuAuth = editMenuAuth;
        }

        public String getCheckInApplyAuth() {
            return CheckInApplyAuth;
        }

        public void setCheckInApplyAuth(String checkInApplyAuth) {
            CheckInApplyAuth = checkInApplyAuth;
        }

        public String getCheckOutForCarAuth() {
            return CheckOutForCarAuth;
        }

        public void setCheckOutForCarAuth(String checkOutForCarAuth) {
            CheckOutForCarAuth = checkOutForCarAuth;
        }

        public String getCheckOutForPersonAuth() {
            return CheckOutForPersonAuth;
        }

        public void setCheckOutForPersonAuth(String checkOutForPersonAuth) {
            CheckOutForPersonAuth = checkOutForPersonAuth;
        }

        public String getCheckStatisticsAuth() {
            return CheckStatisticsAuth;
        }

        public void setCheckStatisticsAuth(String checkStatisticsAuth) {
            CheckStatisticsAuth = checkStatisticsAuth;
        }

        public String getbClosed() {
            return bClosed;
        }

        public void setbClosed(String bClosed) {
            this.bClosed = bClosed;
        }

        public String getTFSerNo() {
            return TFSerNo;
        }

        public void setTFSerNo(String TFSerNo) {
            this.TFSerNo = TFSerNo;
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

        public String getIMSI() {
            return IMSI;
        }

        public void setIMSI(String IMSI) {
            this.IMSI = IMSI;
        }

        public String getIMEI() {
            return IMEI;
        }

        public void setIMEI(String IMEI) {
            this.IMEI = IMEI;
        }

        public String getOutStatus() {
            return OutStatus;
        }

        public void setOutStatus(String OutStatus) {
            this.OutStatus = OutStatus;
        }

        public String getRetireTime() {
            return RetireTime;
        }

        public void setRetireTime(String RetireTime) {
            this.RetireTime = RetireTime;
        }

        public String getTimeStamp() {
            return TimeStamp;
        }

        public void setTimeStamp(String TimeStamp) {
            this.TimeStamp = TimeStamp;
        }
    }
}
