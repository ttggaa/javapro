package com.kariqu.zwsrv.web.persistance.entityex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebAppData {
    public static WebAppData parseOneRow(Object row) {
        if (row == null)
            return null;
        WebAppData record = new WebAppData();
        Object[] cells = (Object[]) row;
        int index = 0;
        record.setApp_name((String) cells[index++]);
        record.setDate((String) cells[index++]);
        record.setRegister(((BigDecimal) cells[index++]).intValue());
        record.setActivety(((BigDecimal) cells[index++]).intValue());
        record.setCharge(((BigDecimal) cells[index++]).intValue());


        float val = record.getRegister() > 0 ? record.getCharge() * 1.00f / record.getRegister() : 0.00f;
        BigDecimal bigdecimal = new BigDecimal(val);
        BigDecimal setScale = bigdecimal.setScale(2,BigDecimal.ROUND_HALF_DOWN);

        record.setRegister_arpu(setScale.floatValue());

        val = record.getActivety() > 0 ? record.getCharge() * 1.00f / record.getActivety() : 0.00f;
        bigdecimal = new BigDecimal(val);
        setScale = bigdecimal.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        record.setCharge_arpu(setScale.floatValue());

        return record;
    }

    public static List<WebAppData> parseRowList(List rows) {
        List<WebAppData> ret = new ArrayList<WebAppData>();
        for (Object row : rows) {
            WebAppData record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    public static List<String> channelList(List rows) {
        List<String> ret = new ArrayList<String>();
        for (Object row : rows) {
            if (row == null)
                return null;
            ret.add((String) row);
        }
        return ret;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public int getActivety() {
        return activety;
    }

    public void setActivety(int activety) {
        this.activety = activety;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public float getRegister_arpu() {
        return register_arpu;
    }

    public void setRegister_arpu(float register_arpu) {
        this.register_arpu = register_arpu;
    }

    public float getCharge_arpu() {
        return charge_arpu;
    }

    public void setCharge_arpu(float charge_arpu) {
        this.charge_arpu = charge_arpu;
    }

    private String app_name;
    private String date;
    private int register;
    private int activety;
    private int charge;
    private float register_arpu;
    private float charge_arpu;
}
