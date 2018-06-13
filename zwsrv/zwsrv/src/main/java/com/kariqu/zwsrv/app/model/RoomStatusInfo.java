package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Room;

/**
 * Created by simon on 26/11/17.
 */
public class RoomStatusInfo extends BaseModel {
    private int roomId;
    private int authorId; //主播ID,如果authorId为0, 则使用roomId
    private int hasParent;
    private int childNum;
    private int freeNum;
    private int inUsingNum;
    private int faultNum;
    private int userNum; //当前用户数
    private int goodsNum; //还有多少礼物
    private int status; //机器状态: '0空闲,1游戏中,2游戏中(无限模式)',
    private int maintStatus; //管理状态:0正常 1测试中, 2补货中, 3维护中
    private int isOnline; //0未上线 1上线

    public RoomStatusInfo() {

    }

    public RoomStatusInfo(Room room) {
        this.roomId=room.getRoomId();
        this.authorId=room.getAuthorId();
        this.childNum=room.getChildNum();
        this.freeNum=room.getFreeNum();
        this.inUsingNum=room.getInUsingNum();
        this.faultNum=room.getFaultNum();
        this.userNum=room.getUserNum();
        this.goodsNum=room.getGoodsNum();
        this.status=room.getStatus();
        this.maintStatus=room.getMaintStatus();
        this.isOnline=room.getIsOnline();
    }


}
