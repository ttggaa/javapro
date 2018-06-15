package com.kariqu.zwsrv.web.persistance.entityex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebOp {
    public static WebOp parseOneRow(Object row) {
        if (row == null)
            return null;
        WebOp record = new WebOp();
        Object[] cells = (Object[]) row;
        int index = 0;
        record.setId(((BigInteger) cells[index++]).longValue());
        record.setOpkey((String) cells[index++]);
        record.setOpvalue((String) cells[index++]);
        record.setOptime((Date) cells[index++]);

        return record;
    }

    public static List<WebOp> parseRowList(List rows) {
        List<WebOp> ret = new ArrayList<WebOp>();
        for (Object row : rows) {
            WebOp record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    public String getOpkey() {
        return opkey;
    }

    public void setOpkey(String opkey) {
        this.opkey = opkey;
    }

    public String getOpvalue() {
        return opvalue;
    }

    public void setOpvalue(String opvalue) {
        this.opvalue = opvalue;
    }

    public Date getOptime() {
        return optime;
    }

    public void setOptime(Date optime) {
        this.optime = optime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private String opkey;
    private String opvalue;
    private Date optime;
}
