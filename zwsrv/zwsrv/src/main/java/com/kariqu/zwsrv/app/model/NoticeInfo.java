package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Notice;

/**
 * Created by simon on 21/12/17.
 */
public class NoticeInfo extends BaseModel {

    public NoticeInfo() {
        this.imageUrl="";
        this.title="";
        this.content="";
        this.redirectURI="";
    }

    public NoticeInfo(Notice notice) {
        this.noticeId=notice.getNoticeId();
        this.type=notice.getType();
        this.userId=notice.getUserId();
        this.imageUrl=notice.getImageUrl();
        this.title=notice.getTitle();
        this.content=notice.getContent();
        this.redirectURI=notice.getRedirectURI();
        this.createTime=notice.getCreateTime();
    }

    private int noticeId;
    private int type; //不使用
    private int userId;
    private String imageUrl;
    private String title;
    private String content;
    private String redirectURI;
    private long createTime;

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
