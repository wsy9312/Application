package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/7/24.
 */

public class NewsInfoEntity {


    private List<NewsRrdBean> NewsRrd;

    public List<NewsRrdBean> getNewsRrd() {
        return NewsRrd;
    }

    public void setNewsRrd(List<NewsRrdBean> NewsRrd) {
        this.NewsRrd = NewsRrd;
    }

    public static class NewsRrdBean {
        /**
         * Title : ?
         * Context : ?
         * Picture1 : ?
         * Picture2 : ?
         * Picture3 : ?
         * Picture4 : ?
         * Picture5 : ?
         * Picture1Len : ?
         * Picture2Len : ?
         * Picture3Len : ?
         * Picture4Len : ?
         * Picture5Len : ?
         */

        private String Title;
        private String Context;
        private String Picture1;
        private String Picture2;
        private String Picture3;
        private String Picture4;
        private String Picture5;
        private String Picture1Len;
        private String Picture2Len;
        private String Picture3Len;
        private String Picture4Len;
        private String Picture5Len;

        public NewsRrdBean() {

        }

        public NewsRrdBean(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10, String s11) {

        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getContext() {
            return Context;
        }

        public void setContext(String Context) {
            this.Context = Context;
        }

        public String getPicture1() {
            return Picture1;
        }

        public void setPicture1(String Picture1) {
            this.Picture1 = Picture1;
        }

        public String getPicture2() {
            return Picture2;
        }

        public void setPicture2(String Picture2) {
            this.Picture2 = Picture2;
        }

        public String getPicture3() {
            return Picture3;
        }

        public void setPicture3(String Picture3) {
            this.Picture3 = Picture3;
        }

        public String getPicture4() {
            return Picture4;
        }

        public void setPicture4(String Picture4) {
            this.Picture4 = Picture4;
        }

        public String getPicture5() {
            return Picture5;
        }

        public void setPicture5(String Picture5) {
            this.Picture5 = Picture5;
        }

        public String getPicture1Len() {
            return Picture1Len;
        }

        public void setPicture1Len(String Picture1Len) {
            this.Picture1Len = Picture1Len;
        }

        public String getPicture2Len() {
            return Picture2Len;
        }

        public void setPicture2Len(String Picture2Len) {
            this.Picture2Len = Picture2Len;
        }

        public String getPicture3Len() {
            return Picture3Len;
        }

        public void setPicture3Len(String Picture3Len) {
            this.Picture3Len = Picture3Len;
        }

        public String getPicture4Len() {
            return Picture4Len;
        }

        public void setPicture4Len(String Picture4Len) {
            this.Picture4Len = Picture4Len;
        }

        public String getPicture5Len() {
            return Picture5Len;
        }

        public void setPicture5Len(String Picture5Len) {
            this.Picture5Len = Picture5Len;
        }
    }
}
