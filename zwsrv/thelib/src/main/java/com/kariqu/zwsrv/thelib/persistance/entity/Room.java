package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by simon on 26/11/17.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_room")
public class Room extends VersionedTimedEntity {

    //        `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0空闲,2游戏中,3游戏无限模式中',
//        `maint_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1测试中,2补货中,3维护中',
//        `error_code` int(10) UNSIGNED NOT NULL DEFAULT '0',

    public static final int ROOM_STATUS_IDLE = 0;//空闲
    public static final int ROOM_STATUS_IN_GAME = 2;//游戏中

    public static final int ROOM_MAINT_STATUS_INITIAL = 0;//正常
    public static final int ROOM_MAINT_STATUS_TESTING = 1; //测试中
    public static final int ROOM_MAINT_STATUS_FILLING = 2; //补货中
    public static final int ROOM_MAINT_STATUS_UNDER_MAINTING = 3; //维护中

    // 房间角标
    public static final int ROOM_CORNER_MARK_HOT    = 100;       // hot
    public static final int ROOM_CORNER_MARK_XSZK   = 101;       // 限时折扣
    public static final int ROOM_CORNER_MARK_XLSC   = 102;       // 限量收藏
    public static final int ROOM_CORNER_MARK_HJJD   = 103;       // 怀旧经典
    public static final int ROOM_CORNER_MARK_XP     = 104;       // 新品
    public static final int ROOM_CORNER_MARK_ZBSQ   = 200;       // 正版授权
    public static final int ROOM_CORNER_MARK_JKSP   = 201;       // 进口商品
    public static final int ROOM_CORNER_MARK_DJDZ   = 202;       // 独家定制


    // 房间分类
    public static final int ROOM_CLASSIFY_ZWW       = 0;        // 抓娃娃
    public static final int ROOM_CLASSIFY_TBJ       = 1;        // 推币机


    public Room() {
        this.name="";
        this.roomDesc="";
        this.sounds="";
        this.imageUrl="";
        this.snapshotUrl="";
        this.successImgUrl="";
        this.deviceParams="";
        this.status=ROOM_STATUS_IDLE;
        this.maintStatus=ROOM_MAINT_STATUS_INITIAL;
        this.isBreakdown=0;
        this.errorCode=0;
        this.itemType = 0;

        this.deviceId = "";
        this.deviceUrl = "";
        this.roomClassify = 0;
        this.roomSupport = "";
    }

    @Id
    @Column(name="room_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int roomId;

    @Column(name="room_type", nullable = false)
    private int roomType;

    @Column(name="author_id", nullable = false)
    private int authorId;

    @Column(name="has_parent", nullable = false)
    private int hasParent;

    @Column(name="child_num", nullable = false)
    private int childNum;

    @Column(name="free_num", nullable = false)
    private int freeNum;

    @Column(name="in_using_num", nullable = false)
    private int inUsingNum;

    @Column(name="fault_num", nullable = false)
    private int faultNum;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String roomDesc;

    @Column(name="sounds", nullable = false)
    private String sounds;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="snapshot_url", nullable = false)
    private String snapshotUrl;

    @Column(name="success_img_url", nullable = false)
    private String successImgUrl;

    @Column(name="start_time", nullable = false)
    private long startTime;

    @Column(name="duration", nullable = false)
    private int duration;

    @Column(name="is_special", nullable = false)
    private int isSpecial;

    @Column(name="unlimit_times", nullable = false)
    private int unlimitTimes;

    @Column(name="room_cost", nullable = false)
    private int roomCost;

    @Column(name="can_delivery", nullable = false)
    private int canDelivery;

    @Column(name="exchange_coins", nullable = false)
    private int exchangeCoins;

    @Column(name="reward_coins", nullable = false)
    private int rewardCoins;

    @Column(name="points", nullable = false)
    private int points;

    @Column(name="device_params", nullable = false)
    private String deviceParams;

    @Column(name="user_num", nullable = false)
    private int userNum;

    @Column(name="goods_num", nullable = false)
    private int goodsNum;

    @Column(name="is_online", nullable = false)
    private int isOnline;

    @Column(name="is_in_unlimit", nullable = false)
    private int isInUnlimit;

    @Column(name="status", nullable = false)
    private int status;

    @Column(name="maint_status", nullable = false)
    private int maintStatus;

    @Column(name="is_breakdown", nullable = false)
    private int isBreakdown;

    @Column(name="error_code", nullable = false)
    private int errorCode;

    @Column(name="last_report_time", nullable = false)
    private long lastReportTime;

    @Column(name="sort", nullable = false)
    private int sort;

    @Column(name="valid", nullable = false)
    private int valid;

    @Column(name="itemType", nullable = false)
    private int itemType;

    @Column(name="left_corner_mark", nullable = false)
    private int leftCornerMark;

    @Column(name="right_corner_mark", nullable = false)
    private int rightCornerMark;

    @Column(name="room_cost_origin", nullable = false)
    private int roomCostOrigin;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "device_url", nullable = false)
    private String deviceUrl;

    @Column(name = "room_classify", nullable = false)
    private int roomClassify;

    @Column(name = "room_support", nullable = false)
    private String roomSupport;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public String getSounds() {
        return sounds;
    }

    public void setSounds(String sounds) {
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

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getIsInUnlimit() {
        return isInUnlimit;
    }

    public void setIsInUnlimit(int isInUnlimit) {
        this.isInUnlimit = isInUnlimit;
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

    public int getIsBreakdown() {
        return isBreakdown;
    }

    public void setIsBreakdown(int isBreakdown) {
        this.isBreakdown = isBreakdown;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public long getLastReportTime() {
        return lastReportTime;
    }

    public void setLastReportTime(long lastReportTime) {
        this.lastReportTime = lastReportTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
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



//CREATE TABLE `zww_room_link` (
//        `room_link_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `parent_room_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `room_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `sort` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `is_normal` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否处于正常状态，zww_room:is_online,is_breakdown',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`room_link_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;

//CREATE TABLE `zww_room` (
//        `room_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `room_type` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `author_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `has_parent` tinyint(1) NOT NULL DEFAULT '0' COMMENT '为了支持一个子房间可以在多个父房间',
//        `child_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `free_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `in_using_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `fault_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `name` varchar(80) NOT NULL DEFAULT '',
//        `description` varchar(128) NOT NULL DEFAULT '',
//        `sounds` varchar(255) NOT NULL DEFAULT '' COMMENT '背景音乐',
//        `image_url` varchar(255) NOT NULL DEFAULT '',
//        `snapshot_url` varchar(255) NOT NULL DEFAULT '',
//        `success_img_url` varchar(255) NOT NULL DEFAULT '',
//        `start_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `duration` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `is_special` tinyint(1) NOT NULL DEFAULT '0',
//        `unlimit_times` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '如果不为0则表示连续多少次可进入无限模式',
//        `room_cost` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '单次多少金币',
//        `can_delivery` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '可否邮寄',
//        `exchange_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '兑换金币数,如果为0则不能兑换金币',
//        `reward_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '奖励金币数',
//        `points` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '积分',
//        `device_params` varchar(255) NOT NULL DEFAULT '' COMMENT '设备配置参数:抓力{power:200}',
//        `user_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `goods_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `is_online` tinyint(1) NOT NULL DEFAULT '0',
//        `is_in_unlimit` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否进入无限模式',
//        `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0空闲,1游戏中',
//        `maint_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1测试中,2补货中,3维护中',
//        `is_breakdown` tinyint(1) NOT NULL DEFAULT '0',
//        `error_code` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `last_report_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `sort` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `valid` tinyint(1) NOT NULL DEFAULT '0',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`room_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;



































//不要这个设计
//CREATE TABLE `zww_room` (
//        `room_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
//        `room_type` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `author_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `parent_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `child_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `free_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `in_using_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `fault_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `name` varchar(80) NOT NULL DEFAULT '',
//        `description` varchar(128) NOT NULL DEFAULT '',
//        `sounds` varchar(255) NOT NULL DEFAULT '' COMMENT '背景音乐',
//        `image_url` varchar(255) NOT NULL DEFAULT '',
//        `snapshot_url` varchar(255) NOT NULL DEFAULT '',
//        `success_img_url` varchar(255) NOT NULL DEFAULT '',
//        `start_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `duration` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `is_special` tinyint(1) NOT NULL DEFAULT '0',
//        `unlimit_times` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '如果不为0则表示连续多少次可进入无限模式',
//        `room_cost` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '单次多少金币',
//        `can_delivery` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '可否邮寄',
//        `exchange_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '兑换金币数,如果为0则不能兑换金币',
//        `reward_coins` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '奖励金币数',
//        `points` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '积分',
//        `device_params` varchar(255) NOT NULL DEFAULT '' COMMENT '设备配置参数:抓力{power:200}',
//        `user_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `goods_num` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `is_online` tinyint(1) NOT NULL DEFAULT '0',
//        `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0空闲,1游戏中,2游戏无限模式中',
//        `maint_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1测试中,2补货中,3维护中',
//        `is_breakdown` tinyint(1) NOT NULL DEFAULT '0',
//        `error_code` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `last_report_time` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `sort` int(10) UNSIGNED NOT NULL DEFAULT '0',
//        `valid` tinyint(1) NOT NULL DEFAULT '0',
//        `opt_lock` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
//        `updatetime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        `createtime` bigint(13) UNSIGNED NOT NULL DEFAULT '0',
//        PRIMARY KEY (`room_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;