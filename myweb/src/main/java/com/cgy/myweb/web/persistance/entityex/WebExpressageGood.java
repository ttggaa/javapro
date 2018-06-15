package com.kariqu.zwsrv.web.persistance.entityex;

import com.kariqu.zwsrv.web.utilityex.CityJson;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WebExpressageGood {

    public  static WebExpressageGood parseOneRow(Object row) {
        if (row == null)
            return null;
        WebExpressageGood record = new WebExpressageGood();
        Object[] cells = (Object[]) row;
        //record.setGood_id((int) cells[0]);
        record.setgood_name((String) cells[0]);
        record.setgood_sum(((BigInteger) cells[1]).longValue());
        return record;
    }

    public static List<WebExpressageGood> parseRowList(List rows) {
        List<WebExpressageGood> ret = new ArrayList<WebExpressageGood>();
        for (Object row : rows) {
            WebExpressageGood record = parseOneRow(row);
            if (record != null)
                ret.add(record);
        }
        return ret;
    }

    //private int good_id;            //货物id
    private String good_name;       //货物名
    private long good_sum;           //货物数量

   /* public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }*/

    public String getgood_name() {
        return good_name;
    }

    public void setgood_name(String good_name) {
        this.good_name = good_name;
    }

    public long getgood_sum() {
        return good_sum;
    }

    public void setgood_sum(long good_sum) {
        this.good_sum = good_sum;
    }
}
