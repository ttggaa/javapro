package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.TimedEntity;
import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;



@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_raise_problem")
public class RaiseProblem extends VersionedTimedEntity {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="app_channel", nullable = false)
    private String appChannel;

    @Column(name="version", nullable = false)
    private String version;

    @Column(name="room_id", nullable = false)
    private int roomId;

    @Column(name="type", nullable = false)
    private int type;

    @Column(name="type_str", nullable = false)
    private String typeStr;

    @Column(name="content", nullable = false)
    private String content;

    public RaiseProblem()
    {
        this.id = 0;
        this.userId = 0;
        this.appChannel = "";
        this.version = "";
        this.roomId = 0;
        this.type = 0;
        this.typeStr = "";
        this.content = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
