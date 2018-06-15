package com.kariqu.zwsrv.web.utilityex;

import java.util.Map;

public class City {
    private int districtId;
    private String shortName;
    private String fName;
    private String pinyin;
    private Map<Integer, City> list;

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Map<Integer, City> getList() {
        return list;
    }

    public void setList(Map<Integer, City> list) {
        this.list = list;
    }
}


