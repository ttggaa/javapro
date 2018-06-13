package com.kariqu.zwsrv.thelib.base.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by simon on 4/27/16.
 */
public class CreateTimedEntityListener {

    @PrePersist
    public void onPrePersist(CreateTimedEntity e) {
        e.setCreateTime(System.currentTimeMillis());
    }

    @PreUpdate
    public void onPreUpdate(CreateTimedEntity e) {

    }
}
