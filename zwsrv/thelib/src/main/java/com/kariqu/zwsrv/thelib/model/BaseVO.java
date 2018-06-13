package com.kariqu.zwsrv.thelib.model;

/**
 * Created by simon on 05/04/17.
 */
public class BaseVO extends BaseModel {

    int version;

    String type;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


/*
* banner_scroll:可滚动的banner
* title:标题
* recommend_apps: 推荐APP
* separator_line: 分割线
* separator: 分隔条
* cards: 可滚动的卡片
*
*
* */