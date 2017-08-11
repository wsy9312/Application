package com.example.hgtxxgl.application.entity;

import java.util.List;

/**
 * Created by HGTXxgl on 2017/8/10.
 */

public class MessageEntity {

    private List<MessageRrdBean> MessageRrd;

    public List<MessageRrdBean> getMessageRrd() {
        return MessageRrd;
    }

    public void setMessageRrd(List<MessageRrdBean> MessageRrd) {
        this.MessageRrd = MessageRrd;
    }

    public static class MessageRrdBean {
        /**
         * Time : 2017-8-10 12:00:00
         * Content : 这是第一条消息示例
         * Objects : 44C6E4A9;8CF26CD5;77528C96
         * ModifyTime : 最后修改时间
         * NoIndex : 序号
         * AuthenticationNo : 44C6E4A9
         */

        private String Time;
        private String Content;
        private String Objects;
        private String ModifyTime;
        private String NoIndex;
        private String AuthenticationNo;

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getObjects() {
            return Objects;
        }

        public void setObjects(String Objects) {
            this.Objects = Objects;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public String getNoIndex() {
            return NoIndex;
        }

        public void setNoIndex(String NoIndex) {
            this.NoIndex = NoIndex;
        }

        public String getAuthenticationNo() {
            return AuthenticationNo;
        }

        public void setAuthenticationNo(String AuthenticationNo) {
            this.AuthenticationNo = AuthenticationNo;
        }
    }
}
