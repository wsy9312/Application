package com.example.hgtxxgl.application.bean.people;

import java.util.List;

//Api_Get_Count
//得到数据总量

public class PeopleLeaveCountBean {

    private List<ApiGetCountBean> Api_Get_Count;

    public List<ApiGetCountBean> getApi_Get_Count() {
        return Api_Get_Count;
    }

    public void setApi_Get_Count(List<ApiGetCountBean> Api_Get_Count) {
        this.Api_Get_Count = Api_Get_Count;
    }

    public static class ApiGetCountBean {
        /**
         * Count : 110
         * TableName : PeopleLeaveRrd
         * AuthenticationNo : "54969542"
         * OutStatus : "0"
         * TimeStamp : "1536719570"
         */

        private String Count;
        private String TableName;
        private String AuthenticationNo;
        private String OutStatus;
        private String TimeStamp;
        private String IsAndroid;
        private String bClosed;
        private String OutType;
        private String RegisterTime;

        public String getRegisterTime() {
            return RegisterTime;
        }

        public void setRegisterTime(String registerTime) {
            RegisterTime = registerTime;
        }

        public String getOutType() {
            return OutType;
        }

        public void setOutType(String outType) {
            OutType = outType;
        }

        public String getbClosed() {
            return bClosed;
        }

        public void setbClosed(String bClosed) {
            this.bClosed = bClosed;
        }

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String isAndroid) {
            IsAndroid = isAndroid;
        }

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String authenticationNo) {
            AuthenticationNo = authenticationNo;
        }

        public String getOutStatus() {
            return OutStatus;
        }

        public void setOutStatus(String outStatus) {
            OutStatus = outStatus;
        }

        public String getTimeStamp() {
            return TimeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            TimeStamp = timeStamp;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String Count) {
            this.Count = Count;
        }

        public String getTableName() {
            return TableName;
        }

        public void setTableName(String TableName) {
            this.TableName = TableName;
        }
    }
}
