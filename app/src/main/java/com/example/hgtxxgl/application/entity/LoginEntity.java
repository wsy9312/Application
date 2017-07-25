package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/7/17.
 * 登录实体
 */
public class LoginEntity {

    private List<LoginBean> Login;

    public List<LoginBean> getLogin() {
        return Login;
    }

    public void setLogin(List<LoginBean> Login) {
        this.Login = Login;
    }

    public static class LoginBean {
        /**
         * LoginName : Admin
         * Password : 123456
         */

        private String LoginName;
        private String Password;

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
    }
}
