package com.kariqu.tyt.http.model;

import com.kariqu.tyt.common.persistence.entity.ConfigEntity;

public class ConfigModel {
    private int id;
    private String value;

    public ConfigModel() {
        this.id = 0;
        this.value = "";
    }

    public ConfigModel(ConfigEntity entity) {
        this.id = entity.getId();
        this.value = entity.getValue();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
