package com.example.hgtxxgl.application.bean.file;

import java.util.List;

public class AddFileBean {

    private List<ApiAddFileBean> Api_Add_File;

    public List<ApiAddFileBean> getApi_Add_File() {
        return Api_Add_File;
    }

    public void setApi_Add_File(List<ApiAddFileBean> Api_Add_File) {
        this.Api_Add_File = Api_Add_File;
    }

    public static class ApiAddFileBean {
        /**
         * AuthenticationNo : 1E79E134
         * File : asdasdasdasd
         * Ext : png
         * FileName  : "文件名"
         * IsAndroid  :
         * TimeStamp  :
         */

        private String AuthenticationNo;
        private String File;
        private String Ext;
        private String FileName;
        private String IsAndroid;
        private String TimeStamp;

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String fileName) {
            FileName = fileName;
        }

        public String getIsAndroid() {
            return IsAndroid;
        }

        public void setIsAndroid(String isAndroid) {
            IsAndroid = isAndroid;
        }

        public String getTimeStamp() {
            return TimeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            TimeStamp = timeStamp;
        }

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String AuthenticationNo) {
            this.AuthenticationNo = AuthenticationNo;
        }

        public String getFile() {
            return File;
        }

        public void setFile(String File) {
            this.File = File;
        }

        public String getExt() {
            return Ext;
        }

        public void setExt(String Ext) {
            this.Ext = Ext;
        }
    }
}