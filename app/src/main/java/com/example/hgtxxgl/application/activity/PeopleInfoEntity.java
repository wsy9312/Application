package com.example.hgtxxgl.application.activity;

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
         * Sex : ?
         * Name : ?
         * CardNo : ?
         * BirthDay : ?
         */

        private String Sex;
        private String Name;
        private String CardNo;
        private String BirthDay;

        public String getSex() {
            return Sex;
        }

        public void setSex(String Sex) {
            this.Sex = Sex;
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

        public String getBirthDay() {
            return BirthDay;
        }

        public void setBirthDay(String BirthDay) {
            this.BirthDay = BirthDay;
        }
    }
}
