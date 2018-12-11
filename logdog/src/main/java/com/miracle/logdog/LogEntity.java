package com.miracle.logdog;

import java.util.Date;

/**
 * miracle
 * 2018/12/4 10:43
 */
public class LogEntity {

    public LogEntity() {
    }

    private transient int id;

    private String content;

    private String createTime;

    private boolean isPushed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isPushed() {
        return isPushed;
    }

    public void setPushed(boolean pushed) {
        isPushed = pushed;
    }
}
