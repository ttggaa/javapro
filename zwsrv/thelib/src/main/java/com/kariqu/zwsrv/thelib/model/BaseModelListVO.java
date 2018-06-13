package com.kariqu.zwsrv.thelib.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 05/04/17.
 */
public class BaseModelListVO extends BaseVO {

    List<BaseModel> list;

    public List<BaseModel> getList() {
        return list;
    }

    public void setList(List<BaseModel> list) {
        if (this.list==null) {
            this.list=new ArrayList<>();
        }
        this.list.clear();
        this.list.addAll(list);
    }

    public BaseModelListVO() {

    }

    public BaseModelListVO(String type) {
        setType(type);
    }

    public void addModel(BaseModel model) {
        if (list==null) {
            list=new ArrayList<>();
        }
        list.add(model);
    }
}
