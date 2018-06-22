package com.kariqu.tyt.common.persistence.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class DatetimeEntityListener {

    @PrePersist
    public void onPrePersist(DatetimeEntity entity) {
        Date tnow = new Date();
        entity.setCreatetime(tnow);
        entity.setUpdatetime(tnow);
    }

    @PreUpdate
    public void onPreUpdate(DatetimeEntity entity) {
        entity.setUpdatetime(new Date());
    }
}
