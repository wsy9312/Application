package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/7/24.\
 * 新闻实体
 */

public class NewsInfoEntity {

    private List<NewsRrdBean> NewsRrd;

    public List<NewsRrdBean> getNewsRrd() {
        return NewsRrd;
    }

    public void setNewsRrd(List<NewsRrdBean> NewsRrd) {
        this.NewsRrd = NewsRrd;
    }

    @Override
    public String toString() {
        return "NewsInfoEntity{" +
                "NewsRrd=" + NewsRrd +
                '}';
    }

    public static class NewsRrdBean {
        /**
         * Title : ?
         * Content : ?
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
        private String Content;
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
        private String ModifyTime;
        private String RegisterTime;
        private String BeginNum;
        private String EndNum;
        private String NoIndex;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NewsRrdBean that = (NewsRrdBean) o;

            if (Title != null ? !Title.equals(that.Title) : that.Title != null) return false;
            if (Content != null ? !Content.equals(that.Content) : that.Content != null)
                return false;
            if (Picture1 != null ? !Picture1.equals(that.Picture1) : that.Picture1 != null)
                return false;
            if (Picture2 != null ? !Picture2.equals(that.Picture2) : that.Picture2 != null)
                return false;
            if (Picture3 != null ? !Picture3.equals(that.Picture3) : that.Picture3 != null)
                return false;
            if (Picture4 != null ? !Picture4.equals(that.Picture4) : that.Picture4 != null)
                return false;
            if (Picture5 != null ? !Picture5.equals(that.Picture5) : that.Picture5 != null)
                return false;
            if (Picture1Len != null ? !Picture1Len.equals(that.Picture1Len) : that.Picture1Len != null)
                return false;
            if (Picture2Len != null ? !Picture2Len.equals(that.Picture2Len) : that.Picture2Len != null)
                return false;
            if (Picture3Len != null ? !Picture3Len.equals(that.Picture3Len) : that.Picture3Len != null)
                return false;
            if (Picture4Len != null ? !Picture4Len.equals(that.Picture4Len) : that.Picture4Len != null)
                return false;
            if (Picture5Len != null ? !Picture5Len.equals(that.Picture5Len) : that.Picture5Len != null)
                return false;
            if (ModifyTime != null ? !ModifyTime.equals(that.ModifyTime) : that.ModifyTime != null)
                return false;
            if (RegisterTime != null ? !RegisterTime.equals(that.RegisterTime) : that.RegisterTime != null)
                return false;
            if (BeginNum != null ? !BeginNum.equals(that.BeginNum) : that.BeginNum != null)
                return false;
            return EndNum != null ? EndNum.equals(that.EndNum) : that.EndNum == null;

        }

        @Override
        public int hashCode() {
            int result = Title != null ? Title.hashCode() : 0;
            result = 31 * result + (Content != null ? Content.hashCode() : 0);
            result = 31 * result + (Picture1 != null ? Picture1.hashCode() : 0);
            result = 31 * result + (Picture2 != null ? Picture2.hashCode() : 0);
            result = 31 * result + (Picture3 != null ? Picture3.hashCode() : 0);
            result = 31 * result + (Picture4 != null ? Picture4.hashCode() : 0);
            result = 31 * result + (Picture5 != null ? Picture5.hashCode() : 0);
            result = 31 * result + (Picture1Len != null ? Picture1Len.hashCode() : 0);
            result = 31 * result + (Picture2Len != null ? Picture2Len.hashCode() : 0);
            result = 31 * result + (Picture3Len != null ? Picture3Len.hashCode() : 0);
            result = 31 * result + (Picture4Len != null ? Picture4Len.hashCode() : 0);
            result = 31 * result + (Picture5Len != null ? Picture5Len.hashCode() : 0);
            result = 31 * result + (ModifyTime != null ? ModifyTime.hashCode() : 0);
            result = 31 * result + (RegisterTime != null ? RegisterTime.hashCode() : 0);
            result = 31 * result + (BeginNum != null ? BeginNum.hashCode() : 0);
            result = 31 * result + (EndNum != null ? EndNum.hashCode() : 0);
            return result;
        }

        public NewsRrdBean() {

        }

        public NewsRrdBean(String title, String content, String picture1, String picture2, String picture3, String picture4, String picture5, String picture1Len, String picture2Len, String picture3Len, String picture4Len, String picture5Len, String modifyTime, String registerTime, String beginNum, String endNum, String noIndex) {
            Title = title;
            Content = content;
            Picture1 = picture1;
            Picture2 = picture2;
            Picture3 = picture3;
            Picture4 = picture4;
            Picture5 = picture5;
            Picture1Len = picture1Len;
            Picture2Len = picture2Len;
            Picture3Len = picture3Len;
            Picture4Len = picture4Len;
            Picture5Len = picture5Len;
            ModifyTime = modifyTime;
            RegisterTime = registerTime;
            BeginNum = beginNum;
            EndNum = endNum;
            NoIndex = noIndex;
        }

        @Override
        public String toString() {
            return "NewsRrdBean{" +
                    "Title='" + Title + '\'' +
                    ", Content='" + Content + '\'' +
                    ", Picture1='" + Picture1 + '\'' +
                    ", Picture2='" + Picture2 + '\'' +
                    ", Picture3='" + Picture3 + '\'' +
                    ", Picture4='" + Picture4 + '\'' +
                    ", Picture5='" + Picture5 + '\'' +
                    ", Picture1Len='" + Picture1Len + '\'' +
                    ", Picture2Len='" + Picture2Len + '\'' +
                    ", Picture3Len='" + Picture3Len + '\'' +
                    ", Picture4Len='" + Picture4Len + '\'' +
                    ", Picture5Len='" + Picture5Len + '\'' +
                    ", ModifyTime='" + ModifyTime + '\'' +
                    ", RegisterTime='" + RegisterTime + '\'' +
                    ", BeginNum='" + BeginNum + '\'' +
                    ", EndNum='" + EndNum + '\'' +
                    '}';
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Context) {
            this.Content = Context;
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

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String modifyTime) {
            ModifyTime = modifyTime;
        }

        public String getRegisterTime() {
            return RegisterTime;
        }

        public void setRegisterTime(String registerTime) {
            RegisterTime = registerTime;
        }

        public String getBeginNum() {
            return BeginNum;
        }

        public void setBeginNum(String beginNum) {
            BeginNum = beginNum;
        }

        public String getEndNum() {
            return EndNum;
        }

        public void setEndNum(String endNum) {
            EndNum = endNum;
        }

        public String getNoIndex() {
            return NoIndex;
        }

        public void setNoIndex(String noIndex) {
            NoIndex = noIndex;
        }

    }
}
