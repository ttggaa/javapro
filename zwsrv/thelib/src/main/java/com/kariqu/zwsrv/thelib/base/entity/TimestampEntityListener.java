package com.kariqu.zwsrv.thelib.base.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by simon on 4/27/16.
 */
public class TimestampEntityListener {

    @PrePersist
    public void onPrePersist(TimestampEntity e) {
        e.setTimestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void onPreUpdate(TimestampEntity e) {
        e.setTimestamp(System.currentTimeMillis());
    }
}
