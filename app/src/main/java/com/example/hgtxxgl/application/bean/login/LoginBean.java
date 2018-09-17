package com.example.hgtxxgl.application.bean.login;
//request 登录请求
public class LoginBean {

    /**
     * IsAndroid : 1
     * LoginName : dwayg1
     * Password : 666666
     */

    private String IsAndroid;
    private String LoginName;
    private String Password;
    private String Authority;
    private String ModifyTime;
    private String NoIndex;
    private String AuthenticationNo;

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

    public String getAuthenticationNo() {
        return AuthenticationNo;
    }

    public void setAuthenticationNo(String authenticationNo) {
        AuthenticationNo = authenticationNo;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
