package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/8/1.
 */

public class LeaveEntity {

    private List<ApplyBean> apply;

    public List<ApplyBean> getApply() {
        return apply;
    }

    public void setApply(List<ApplyBean> apply) {
        this.apply = apply;
    }

    public static class ApplyBean {
        private List<PeopleLeaveRrdBean> PeopleLeaveRrd;
        private List<CarLeaveRrdBean> CarLeaveRrd;

        public List<PeopleLeaveRrdBean> getPeopleLeaveRrd() {
            return PeopleLeaveRrd;
        }

        public void setPeopleLeaveRrd(List<PeopleLeaveRrdBean> PeopleLeaveRrd) {
            this.PeopleLeaveRrd = PeopleLeaveRrd;
        }

        public List<CarLeaveRrdBean> getCarLeaveRrd() {
            return CarLeaveRrd;
        }

        public void setCarLeaveRrd(List<CarLeaveRrdBean> CarLeaveRrd) {
            this.CarLeaveRrd = CarLeaveRrd;
        }
    }
}
