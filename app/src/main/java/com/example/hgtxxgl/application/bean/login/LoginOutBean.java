package com.example.hgtxxgl.application.bean.login;

//Api_Add_LogOut
//退出登录

import java.util.List;

public class LoginOutBean {

    private List<ApiAddLogOutBean> Api_Add_LogOut;

    public List<ApiAddLogOutBean> getApi_Add_LogOut() {
        return Api_Add_LogOut;
    }

    public void setApi_Add_LogOut(List<ApiAddLogOutBean> Api_Add_LogOut) {
        this.Api_Add_LogOut = Api_Add_LogOut;
    }

    public static class ApiAddLogOutBean {
        /**
         * AuthenticationNo :
         * IsAndroid : 1
         */

        private String AuthenticationNo;
        private String IsAndroid;

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String AuthenticationNo) {
            this.AuthenticationNo = AuthenticationNo;
        }

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String IsAndroid) {
            this.IsAndroid = IsAndroid;
        }
    }
}
