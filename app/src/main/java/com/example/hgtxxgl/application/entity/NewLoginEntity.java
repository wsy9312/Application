package com.example.hgtxxgl.application.entity;

import java.util.List;
//登录实体
public class NewLoginEntity {

    private List<LoginBean> Login;

    public List<LoginBean> getLogin() {
        return Login;
    }

    public void setLogin(List<LoginBean> Login) {
        this.Login = Login;
    }

    public static class LoginBean {
        /**
         * AuthenticationNo : 77528C96
         * Authority : 3
         */

        private String AuthenticationNo;
        private int Authority;

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
    }
}
