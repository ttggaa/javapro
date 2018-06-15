package com.kariqu.zwsrv.web.persistance.entityex;

import com.kariqu.zwsrv.web.utilityex.CityJson;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WebRoom {
    public static WebRoom parseOneRow(Object row) {
        if (row == null)
            return null;
        WebRoom record = new WebRoom();
        Object[] cells = (Object[]) row;
        int index = 0;
        record.setRoom_id((int) cells[index++]);
        record.setName((String) cells[index++]);
        record.setUnlimit_times((int) cells[index++]);
        record.setRoom_cost((int) cells[index++]);
        record.setCan_delivery(((Boolean) cells[index++]) ? 1 : 0);
        record.setExchange_coins((int) cells[index++]);
        record.setReward_coins((int) cells[index++]);
        record.setIs_online(((Boolean) cells[index++]) ? 1 : 0);

        record.setPoints((int) cells[index++]);
        record.setUser_num((int) cells[index++]);
        record.setGoods_num((int) cells[index++]);
        record.setIs_in_unlimit(((Boolean) cells[index++]) ? 1 : 0);
        record.setStatus(((Boolean) cells[index++]) ? 1 : 0);
        record.setMaint_status(((Boolean) cells[index++]) ? 1 : 0);

        record.setDevice_params((String) cells[index++]);
        record.setSort((int) cells[index++]);

        record.setCreatetime(((BigInteger) cells[index++]).longValue());
        record.setUpdatetime(((BigInteger) cells[index++]).longValue());

        return record;
    }

    public static List<WebRoom> parseRowList(List rows) {
        List<WebRoom> ret = new ArrayList<WebRoom>();
        for (Object row : rows) {
            WebRoom record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    private int room_id;
    private String name;
    private int room_cost;          //单次多少金币
    private int can_delivery;       //可否邮寄
    private int exchange_coins;     //兑换金币数,如果为0则不能兑换金币
    private int reward_coins;       //奖励金币数

    private int points;             //积分
    private int user_num;
    private int goods_num;
    private int unlimit_times;      //如果不为0则表示连续多少次可进入无限模式
    private int is_in_unlimit;      //是否进入无限模式
    private int status;             //0空闲,1游戏中
    private int maint_status;       //1测试中,2补货中,3维护中
    private int is_online;

    private String device_params;   //设备配置参数:抓力{power:200}    
    private int sort;               //排序

    private long createtime;
    private long updatetime;

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoom_cost() {
        return room_cost;
    }

    public void setRoom_cost(int room_cost) {
        this.room_cost = room_cost;
    }

    public int getCan_delivery() {
        return can_delivery;
    }

    public void setCan_delivery(int can_delivery) {
        this.can_delivery = can_delivery;
    }

    public int getExchange_coins() {
        return exchange_coins;
    }

    public void setExchange_coins(int exchange_coins) {
        this.exchange_coins = exchange_coins;
    }

    public int getReward_coins() {
        return reward_coins;
    }

    public void setReward_coins(int reward_coins) {
        this.reward_coins = reward_coins;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getUser_num() {
        return user_num;
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public int getUnlimit_times() {
        return unlimit_times;
    }

    public void setUnlimit_times(int unlimit_times) {
        this.unlimit_times = unlimit_times;
    }

    public int getIs_in_unlimit() {
        return is_in_unlimit;
    }

    public void setIs_in_unlimit(int is_in_unlimit) {
        this.is_in_unlimit = is_in_unlimit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMaint_status() {
        return maint_status;
    }

    public void setMaint_status(int maint_status) {
        this.maint_status = maint_status;
    }

    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public String getDevice_params() {
        return device_params;
    }

    public void setDevice_params(String device_params) {
        this.device_params = device_params;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }
}
