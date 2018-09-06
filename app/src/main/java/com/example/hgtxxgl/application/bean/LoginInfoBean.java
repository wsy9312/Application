package com.example.hgtxxgl.application.bean;

import java.util.List;

public class LoginInfoBean {

    private List<ApiAddLoginBean> Api_Add_Login;

    public List<ApiAddLoginBean> getApi_Add_Login() {
        return Api_Add_Login;
    }

    public void setApi_Add_Login(List<ApiAddLoginBean> Api_Add_Login) {
        this.Api_Add_Login = Api_Add_Login;
    }

    public static class ApiAddLoginBean {
        /**
         * AuthenticationNo : 3BF5A7F8
         * Authority : 0
         * TimeStamp : 1536223180
         */

        private String AuthenticationNo;
        private int Authority;
        private String TimeStamp;

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String AuthenticationNo) {
            this.AuthenticationNo = AuthenticationNo;
        }

        public int getAuthority() {
            return Authority;
        }

        public void setAuthority(int Authority) {
            this.Authority = Authority;
        }

        public String getTimeStamp() {
            return TimeStamp;
        }

        public void setTimeStamp(String TimeStamp) {
            this.TimeStamp = TimeStamp;
        }
    }
}
