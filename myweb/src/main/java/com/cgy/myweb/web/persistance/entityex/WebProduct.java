package com.kariqu.zwsrv.web.persistance.entityex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WebProduct {

    public static WebProduct parseOneRow(Object row) {
        if (row == null)
            return null;
        WebProduct record = new WebProduct();
        Object[] cells = (Object[]) row;
        int index = 0;
        record.setRoom_id((Integer) cells[index++]);
        record.setRoom_name((String) cells[index++]);

        record.sum = new ArrayList<String>();
        for(int i = index;i<cells.length;i++){
            record.sum.add(String.valueOf((BigInteger) cells[i]));
        }

        return record;
    }

    public static List<WebProduct> parseRowList(List rows) {
        List<WebProduct> ret = new ArrayList<WebProduct>();
        for (Object row : rows) {
            WebProduct record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    public static List<String> getDays(List rows) {
        List<String> ret = new ArrayList<String>();
        for (Object row : rows) {
            if (row == null)
                return null;
//            Object[] cells = (Object[]) row;
//            ret.add((String) cells[0]);
            ret.add((String)row);
        }
        return ret;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }


    private int room_id;
    private String room_name;

    public List<String> getSum() {
        return sum;
    }

    public void setSum(List<String> sum) {
        this.sum = sum;
    }

    private List<String> sum;
}
