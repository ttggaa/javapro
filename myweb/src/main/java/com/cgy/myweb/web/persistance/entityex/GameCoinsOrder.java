package com.kariqu.zwsrv.web.persistance.entityex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameCoinsOrder {
    public static GameCoinsOrder parseOneRow(Object row) {
        if (row == null)
            return null;
        GameCoinsOrder record = new GameCoinsOrder();
        Object[] cells = (Object[]) row;
        int index = 0;
        record.setOrder_id((int) cells[index++]);
        record.setOrder_sn((String) cells[index++]);
        record.setOrder_subject((String) cells[index++]);
        record.setOrder_desc((String) cells[index++]);
        record.setUser_id((int) cells[index++]);
        record.setCoins_id((int) cells[index++]);
        record.setUnit_amount((int) cells[index++]);
        record.setUnit_coins((int) cells[index++]);
        record.setIsPromotion(((Boolean) cells[index++])? 1 : 0);
        record.setTotal_amount((int) cells[index++]);
        record.setCoins((int) cells[index++]);
        record.setIsPaid(((Boolean) cells[index++])? 1 : 0);
        record.setOpt_lock( ((BigInteger) cells[index++]).longValue());
        record.setUpdatetime( ((BigInteger) cells[index++]).longValue());
        record.setCreatetime( ((BigInteger) cells[index++]).longValue());
        record.setBilling_type((int) cells[index++]);
        record.setBilling_id((int) cells[index++]);

        return record;
    }

    public static List<GameCoinsOrder> parseRowList(List rows) {
        List<GameCoinsOrder> ret = new ArrayList<GameCoinsOrder>();
        for (Object row : rows) {
            GameCoinsOrder record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }


    private int order_id; // 支付记录自增id
    private String order_sn; // 临时支付号ID
    private String order_subject; // 订单名称
    private String order_desc; // 详细描述
    private int user_id; //
    private int coins_id; //
    private int unit_amount; // unit金额,精确到分10
    private int unit_coins; // unit金币数,10
    private int isPromotion; // 是否批量购买
    private int total_amount; // 总金额,精确到分
    private int coins; // 购买金币数
    private int isPaid; // 是否已支付,0否;1是
    private long opt_lock; //
    private long updatetime; //
    private long createtime; //
    private int billing_type; // 计费类型
    private int billing_id; // 计费类型对应计费id

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_subject() {
        return order_subject;
    }

    public void setOrder_subject(String order_subject) {
        this.order_subject = order_subject;
    }

    public String getOrder_desc() {
        return order_desc;
    }

    public void setOrder_desc(String order_desc) {
        this.order_desc = order_desc;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCoins_id() {
        return coins_id;
    }

    public void setCoins_id(int coins_id) {
        this.coins_id = coins_id;
    }

    public int getUnit_amount() {
        return unit_amount;
    }

    public void setUnit_amount(int unit_amount) {
        this.unit_amount = unit_amount;
    }

    public int getUnit_coins() {
        return unit_coins;
    }

    public void setUnit_coins(int unit_coins) {
        this.unit_coins = unit_coins;
    }

    public int getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(int isPromotion) {
        this.isPromotion = isPromotion;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public long getOpt_lock() {
        return opt_lock;
    }

    public void setOpt_lock(long opt_lock) {
        this.opt_lock = opt_lock;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getBilling_type() {
        return billing_type;
    }

    public void setBilling_type(int billing_type) {
        this.billing_type = billing_type;
    }

    public int getBilling_id() {
        return billing_id;
    }

    public void setBilling_id(int billing_id) {
        this.billing_id = billing_id;
    }
}
