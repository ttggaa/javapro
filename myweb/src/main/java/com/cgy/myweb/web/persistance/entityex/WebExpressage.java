package com.kariqu.zwsrv.web.persistance.entityex;

import com.kariqu.zwsrv.web.utilityex.CityJson;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WebExpressage {

    public static WebExpressage parseOneRow(Object row)
    {
        if (row == null)
            return null;
        WebExpressage record = new WebExpressage();
        Object[] cells = (Object[]) row;
        record.setOrder_id((int)cells[0]);
        record.setShipping_name((String)cells[1]);
        record.setConsignee((String)cells[2]);
        record.setMobile((String)cells[3]);
        record.setAddress((String)cells[4]);
        record.setRemarks((String)cells[5]);
        record.setShipping_status(((Boolean)cells[6])? 1 : 0);
        record.setCreatetime(((BigInteger)cells[7]).longValue());
        record.setUpdatetime(((BigInteger)cells[8]).longValue());

        record.setProvince((int)cells[9]);
        record.setCity((int)cells[10]);
        record.setDistrict((int)cells[11]);

        record.setUser_id((int)cells[12]);

        /*
        List<String> pcd = CityJson.getInstance().getLocation(record.getProvince(), record.getCity(), record.getDistrict());
        record.setProvinceStr(pcd.get(0));
        record.setCityStr(pcd.get(1));
        record.setDistrictStr(pcd.get(2));
        */
        return record;
    }

    public static List<WebExpressage> parseRowList(List rows)
    {
        List<WebExpressage> ret = new ArrayList<WebExpressage>();
        for (Object row : rows) {
            WebExpressage record = parseOneRow(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    private int order_id;
    private String shipping_name;
    private String good_name;
    private String consignee;
    private String mobile;
    private String address;
    private String remarks;
    private int shipping_status;
    private long createtime;
    private long updatetime;

    private int province;
    private int city;
    private int district;
    private int user_id;

    private String provinceStr;
    private String cityStr;
    private String districtStr;


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(int shipping_status) {
        this.shipping_status = shipping_status;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public String getProvinceStr() {
        return provinceStr;
    }

    public void setProvinceStr(String provinceStr) {
        this.provinceStr = provinceStr;
    }

    public String getCityStr() {
        return cityStr;
    }

    public void setCityStr(String cityStr) {
        this.cityStr = cityStr;
    }

    public String getDistrictStr() {
        return districtStr;
    }

    public void setDistrictStr(String districtStr) {
        this.districtStr = districtStr;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }
}
