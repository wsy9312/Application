package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/7/21.
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
         * No : 编号
         * Name : 姓名
         * CardNo : 身份证号
         * Position : 职务
         * Sex : 性别
         * Unit : 所属单位
         * ArmyGroup : 所属部门
         * PhoneNo : 固定电话
         * TelNo : 手机号码
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

        public PeopleInfoBean(String no, String name, String cardNo, String position, String sex, String unit, String armyGroup, String phoneNo, String telNo) {
            No = no;
            Name = name;
            CardNo = cardNo;
            Position = position;
            Sex = sex;
            Unit = unit;
            ArmyGroup = armyGroup;
            PhoneNo = phoneNo;
            TelNo = telNo;
        }

        @Override
        public String toString() {
            return "PeopleInfoBean{" +
                    "No='" + No + '\'' +
                    ", Name='" + Name + '\'' +
                    ", CardNo='" + CardNo + '\'' +
                    ", Position='" + Position + '\'' +
                    ", Sex=" + Sex +
                    ", Unit='" + Unit + '\'' +
                    ", ArmyGroup='" + ArmyGroup + '\'' +
                    ", PhoneNo='" + PhoneNo + '\'' +
                    ", TelNo='" + TelNo + '\'' +
                    '}';
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
    }
}
