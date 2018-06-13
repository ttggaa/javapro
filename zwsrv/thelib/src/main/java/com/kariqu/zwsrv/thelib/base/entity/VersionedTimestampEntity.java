package com.kariqu.zwsrv.thelib.base.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Created by simon on 14/03/17.
 */

@MappedSuperclass
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class VersionedTimestampEntity extends TimestampEntity {

    @Version
    @Column(name = "opt_lock")
    private long optLock;

    public long getOptLock() {
        return optLock;
    }

    public void setOptLock(long optLock) {
        this.optLock = optLock;
    }

}
