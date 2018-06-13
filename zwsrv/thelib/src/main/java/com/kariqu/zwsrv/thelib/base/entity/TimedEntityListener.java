package com.kariqu.zwsrv.thelib.base.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by simon on 4/27/16.
 */
public class TimedEntityListener {

    @PrePersist
    public void onPrePersist(TimedEntity e) {
        e.setUpdateTime(System.currentTimeMillis());
        e.setCreateTime(System.currentTimeMillis());
    }

    @PreUpdate
    public void onPreUpdate(TimedEntity e) {
        e.setUpdateTime(System.currentTimeMillis());
    }
}
