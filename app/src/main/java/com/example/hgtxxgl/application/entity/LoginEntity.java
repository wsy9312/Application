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
        private String Authority;
        private String ModifyTime;
        private String NoIndex;

        public String getAuthority() {
            return Authority;
        }

        public void setAuthority(String authority) {
            Authority = authority;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String modifyTime) {
            ModifyTime = modifyTime;
        }

        public String getNoIndex() {
            return NoIndex;
        }

        public void setNoIndex(String noIndex) {
            NoIndex = noIndex;
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
    }
}
