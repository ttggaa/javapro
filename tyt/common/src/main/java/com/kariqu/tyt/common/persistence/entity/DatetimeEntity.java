package com.kariqu.tyt.common.persistence.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;


@MappedSuperclass
@EntityListeners({DatetimeEntityListener.class})
public class DatetimeEntity {

    @Column(name = "updatetime", nullable = false)
    protected Date updatetime;

    @Column(name = "createtime", updatable = false, nullable = false)
    protected Date createtime;

    @Version
    @Column(name = "opt_lock", updatable = false, nullable = false)
    private long optLock;

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public long getOptLock() {
        return optLock;
    }

    public void setOptLock(long optLock) {
        this.optLock = optLock;
    }
}
