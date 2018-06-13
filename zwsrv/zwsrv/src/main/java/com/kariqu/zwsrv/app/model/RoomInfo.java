package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Room;
import com.kariqu.zwsrv.thelib.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 24/11/17.
 */
public class RoomInfo extends BaseModel {

    private int roomId;
    private int roomType;
    private int authorId; //主播ID,如果authorId为0, 则使用roomId
    private int hasParent;
    private int childNum;
    private int freeNum;
    private int inUsingNum;
    private int faultNum;
    private String name;
    private List<RoomDescItem> roomDescItems; //{list:RoomDescItem[] 数组}
    private List<String> sounds; //字符串数组["",""]
    private String imageUrl;//图片
    private String snapshotUrl;//快照,暂时不用
    private String successImgUrl;//抓取成功后,显示的图片
    private long startTime; //如果不为0，则表示该房间中，使用有时间限制的比如：晚上8点开始
    private int duration; //和startTime对应,精确到秒
    private int isSpecial;//是否特价
    private int unlimitTimes;//是否支持无限模式，即:多少次没有抓到后，免费抓直到抓到
    private int roomCost;//房间单次花费金币数
    private int canDelivery;//货品能否邮寄
    private int exchangeCoins;//如果成功的话兑换金币数
    private int rewardCoins;//如果成功的话奖励金币数
    private int points; //游戏开始获取的积分
    private String deviceParams; //参数,客户端不需要这个
    private int userNum; //当前用户数
    private int goodsNum; //还有多少礼物
    private int status; //机器状态: '0空闲,2游戏中',
    private int maintStatus; //管理状态:0正常 1测试中, 2补货中, 3维护中
    private int isOnline; //0未上线 1上线
    private int leftCornerMark;     // 左角标
    private int rightCornerMark;    // 右角标
    private int roomCostOrigin;     // 原价

    private String deviceId;        // 推币机
    private String deviceUrl;
    private int roomClassify;       // 分类0-娃娃机 1-推币机
    private String roomSupport;     // 玩法

    public RoomInfo() {
        name="";
        roomDescItems=new ArrayList<>();
        sounds=new ArrayList<>();
        imageUrl="";
        snapshotUrl="";
        successImgUrl="";
        deviceParams="";

        this.deviceId = "";
        this.deviceUrl = "";
        this.roomClassify = 0;
        this.roomSupport = "";
    }

    public RoomInfo(Room room) {
        this.roomId=room.getRoomId();
        this.roomType=room.getRoomType();
        this.authorId=room.getAuthorId();
        this.hasParent=room.getHasParent();
        this.childNum=room.getChildNum();
        this.freeNum=room.getFreeNum();
        this.inUsingNum=room.getInUsingNum();
        this.faultNum =room.getFaultNum();
        this.name=room.getName();

        this.startTime=room.getStartTime();
        this.duration=room.getDuration();
        this.isSpecial=room.getIsSpecial();
        this.unlimitTimes=room.getUnlimitTimes();
        this.roomCost=room.getRoomCost();
        this.canDelivery=room.getCanDelivery();
        this.exchangeCoins=room.getExchangeCoins();
        this.rewardCoins=room.getRewardCoins();
        this.points=room.getPoints();
        this.deviceParams=room.getDeviceParams();
        this.userNum=room.getUserNum();
        this.goodsNum=room.getGoodsNum();
        this.status=room.getStatus();
        this.maintStatus=room.getMaintStatus();
        this.isOnline=room.getIsOnline();

        this.imageUrl=URLHelper.fullUrl(room.getImageUrl());
        this.snapshotUrl=URLHelper.fullUrl(room.getSnapshotUrl());
        this.successImgUrl=URLHelper.fullUrl(room.getSuccessImgUrl());

        this.leftCornerMark = room.getLeftCornerMark();     // 左角标
        this.rightCornerMark = room.getRightCornerMark();    // 右角标
        this.roomCostOrigin = room.getRoomCostOrigin();     // 原价

        this.deviceId = room.getDeviceId();
        this.deviceUrl = room.getDeviceUrl();
        this.roomClassify = room.getRoomClassify();
        this.roomSupport = room.getRoomSupport();

        if (room.getRoomDesc()!=null&&room.getRoomDesc().length()>0) {
            this.roomDescItems=JsonUtil.convertJson2ModelList(room.getRoomDesc(),RoomDescItem.class);
            if (this.roomDescItems!=null) {
                for (RoomDescItem item : this.roomDescItems) {
                    item.setImageUrl(URLHelper.fullUrl(item.getImageUrl()));
                }
            }
        }
        if (room.getSounds()!=null&&room.getSounds().length()>0) {
            List<String> sounds = new ArrayList<>();
            List<String> list=JsonUtil.convertJson2RawTypeList(room.getSounds(), String.class);
            if (list!=null) {
                for (String relativeUrl : list) {
                    sounds.add(URLHelper.fullUrl(relativeUrl));
                }
            }
            this.sounds=sounds;
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getHasParent() {
        return hasParent;
    }

    public void setHasParent(int hasParent) {
        this.hasParent = hasParent;
    }

    public int getChildNum() {
        return childNum;
    }

    public void setChildNum(int childNum) {
        this.childNum = childNum;
    }

    public int getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(int freeNum) {
        this.freeNum = freeNum;
    }

    public int getInUsingNum() {
        return inUsingNum;
    }

    public void setInUsingNum(int inUsingNum) {
        this.inUsingNum = inUsingNum;
    }

    public int getFaultNum() {
        return faultNum;
    }

    public void setFaultNum(int faultNum) {
        this.faultNum = faultNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RoomDescItem> getRoomDescItems() {
        return roomDescItems;
    }

    public void setRoomDescItems(List<RoomDescItem> roomDescItems) {
        this.roomDescItems = roomDescItems;
    }

    public List<String> getSounds() {
        return sounds;
    }

    public void setSounds(List<String> sounds) {
        this.sounds = sounds;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSnapshotUrl() {
        return snapshotUrl;
    }

    public void setSnapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl;
    }

    public String getSuccessImgUrl() {
        return successImgUrl;
    }

    public void setSuccessImgUrl(String successImgUrl) {
        this.successImgUrl = successImgUrl;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(int isSpecial) {
        this.isSpecial = isSpecial;
    }

    public int getUnlimitTimes() {
        return unlimitTimes;
    }

    public void setUnlimitTimes(int unlimitTimes) {
        this.unlimitTimes = unlimitTimes;
    }

    public int getRoomCost() {
        return roomCost;
    }

    public void setRoomCost(int roomCost) {
        this.roomCost = roomCost;
    }

    public int getCanDelivery() {
        return canDelivery;
    }

    public void setCanDelivery(int canDelivery) {
        this.canDelivery = canDelivery;
    }

    public int getExchangeCoins() {
        return exchangeCoins;
    }

    public void setExchangeCoins(int exchangeCoins) {
        this.exchangeCoins = exchangeCoins;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(int rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDeviceParams() {
        return deviceParams;
    }

    public void setDeviceParams(String deviceParams) {
        this.deviceParams = deviceParams;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMaintStatus() {
        return maintStatus;
    }

    public void setMaintStatus(int maintStatus) {
        this.maintStatus = maintStatus;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getLeftCornerMark() {
        return leftCornerMark;
    }

    public void setLeftCornerMark(int leftCornerMark) {
        this.leftCornerMark = leftCornerMark;
    }

    public int getRightCornerMark() {
        return rightCornerMark;
    }

    public void setRightCornerMark(int rightCornerMark) {
        this.rightCornerMark = rightCornerMark;
    }

    public int getRoomCostOrigin() {
        return roomCostOrigin;
    }

    public void setRoomCostOrigin(int roomCostOrigin) {
        this.roomCostOrigin = roomCostOrigin;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceUrl() {
        return deviceUrl;
    }

    public void setDeviceUrl(String deviceUrl) {
        this.deviceUrl = deviceUrl;
    }

    public int getRoomClassify() {
        return roomClassify;
    }

    public void setRoomClassify(int roomClassify) {
        this.roomClassify = roomClassify;
    }

    public String getRoomSupport() {
        return roomSupport;
    }

    public void setRoomSupport(String roomSupport) {
        this.roomSupport = roomSupport;
    }
}
