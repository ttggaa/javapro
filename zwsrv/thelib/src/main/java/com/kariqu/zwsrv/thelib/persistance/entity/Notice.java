package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.CreateTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 4/26/16.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_notice")
public class Notice extends CreateTimedEntity {
    private static final long serialVersionUID = 6099840979842714903L;

    public Notice() {
        imageUrl="";
        title="";
        content="";
        redirectURI="";
    }

    @Id
    @Column(name="notice_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int noticeId;

    @Column(name="type", nullable = false)
    private int type;

    @Column(name="user_group", nullable = false)
    private int userGroup;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="style", nullable = false)
    private int style;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="content", nullable = false)
    private String content;

    @Column(name="redirect_uri", nullable = false)
    private String redirectURI;

    @Column(name="has_sent", nullable = false)
    private int hasSent;

    @Column(name="sendtime", nullable = false)
    private long sendTime;

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

    public int getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(int userGroup) {
        this.userGroup = userGroup;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
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

    public int getHasSent() {
        return hasSent;
    }

    public void setHasSent(int hasSent) {
        this.hasSent = hasSent;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}


//CREATE TABLE `zww_notice` (
//        `notice_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `type` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '滴滴专车，顺风车',
//        `user_group` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '>0:用户组',
//        `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '<=0:全部用户, >0:特定用户',
//        `style` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0:个推默认,1:安卓原生,2:纯图展示',
//        `image_url` varchar(255) NOT NULL DEFAULT '',
//        `title` varchar(80) NOT NULL DEFAULT '',
//        `content` varchar(255) NOT NULL DEFAULT '',
//        `redirect_uri` varchar(255) NOT NULL DEFAULT '',
//        `has_sent` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
//        `sendtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`notice_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;
