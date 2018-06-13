package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.thelib.model.BaseModel;
import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import com.kariqu.zwsrv.thelib.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 21/12/17.
 */
public class ConfigInfo extends BaseModel {

    private int configId;
    private String module;
    private String type;
    private int version;
    private int sort;
    private int valid;
    private int width;
    private int height;
    private List<RedirectInfo> data;

    public ConfigInfo() {
        this.module="";
        this.type="";
        this.data=new ArrayList<>();
    }

    public ConfigInfo(Config config) {
        this.configId=config.getConfigId();
        this.module=config.getModule();
        this.type=config.getType();
        this.version=config.getVersion();
        this.sort=config.getSort();
        this.valid=config.getValid();
        this.width=config.getWidth();
        this.height=config.getHeight();
        if (config.getData()!=null&&config.getData().length()>0) {
            this.data=JsonUtil.convertJson2ModelList(config.getData(),RedirectInfo.class);
        }
        if (this.data==null) {
            this.data=new ArrayList<>();
        }

        for (RedirectInfo redirectInfo : this.data) {
            redirectInfo.setImageUrl(URLHelper.fullUrl(redirectInfo.getImageUrl()));
        }
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<RedirectInfo> getData() {
        return data;
    }

    public void setData(List<RedirectInfo> data) {
        this.data = data;
    }
}
