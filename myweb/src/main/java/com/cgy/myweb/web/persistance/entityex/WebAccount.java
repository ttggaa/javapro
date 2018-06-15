package com.kariqu.zwsrv.web.persistance.entityex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WebAccount {

    public static WebAccount parseOneRow(Object row) {
        if (row == null)
            return null;
        WebAccount record = new WebAccount();
        Object[] cells = (Object[]) row;
        int index = 0;
        record.setAccount((String) cells[index++]);
        record.setPassword((String) cells[index++]);
        record.setRemark((String) cells[index++]);

        return record;
    }

    public static List<WebAccount> parseRowList(List rows) {
        List<WebAccount> ret = new ArrayList<WebAccount>();
        for (Object row : rows) {
            WebAccount record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String account;
    private String password;
    private String remark;
}
