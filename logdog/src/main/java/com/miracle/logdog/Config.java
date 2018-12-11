package com.miracle.logdog;

import com.miracle.logdog.network.LogPush;
import com.miracle.logdog.network.LogPusher;

import java.util.HashMap;
import java.util.Map;

/**
 * miracle
 * 2018/12/5 15:40
 */
public class Config {

    public Map<String, String> header = new HashMap<>();
    public String jsonBody;
    public LogPush logPush;
    public String url;
//    public boolean deleteLogsAfterPushed;


    public Config(Builder builder) {
        header.putAll(builder.extraHttpHeader);
        jsonBody = builder.extraJsonBody;
        logPush = builder.logPush;
        url = builder.url;
//        deleteLogsAfterPushed = builder.deleteLogsAfterPushed;
    }

    public static class Builder {
        private Map<String, String> extraHttpHeader = new HashMap<>();
        private String extraJsonBody;
        private LogPush logPush;
        private String url;
        private boolean deleteLogsAfterPushed;

        public Builder() {
            this.url = "http://192.168.6.176:8091";
            this.logPush = new LogPusher();
//            this.deleteLogsAfterPushed = false;
        }

        public Builder extraHttpHeader(Map<String, String> headers) {
            if (headers == null) {
                throw new LogdogException("header is null!");
            }
            this.extraHttpHeader.putAll(headers);
            return this;
        }

        public Builder extraHttpBody(String jsonBody) {
            if (jsonBody == null) {
                throw new LogdogException("jsonBody is null!");
            }
            this.extraJsonBody = jsonBody;
            return this;
        }

        public Builder logpush(LogPush logPush) {
            if (logPush == null) {
                throw new LogdogException("logpush is null");
            }
            this.logPush = logPush;
            return this;
        }

        public Builder url(String url) {
            if (url == null) {
                throw new LogdogException("url is null!");
            }
            this.url = url;
            return this;
        }

//        public Builder deleteLogsAfterPushed(boolean delete) {
//            this.deleteLogsAfterPushed = delete;
//            return this;
//        }

        public Config build() {
            return new Config(this);
        }

    }
}
