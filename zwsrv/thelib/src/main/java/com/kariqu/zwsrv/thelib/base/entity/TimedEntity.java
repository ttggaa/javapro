package com.kariqu.zwsrv.thelib.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Created by simon on 4/26/16.
 */

@MappedSuperclass
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners({ TimedEntityListener.class })
public class TimedEntity extends BaseEntity {

    private static final long serialVersionUID = 3229116235159293505L;

//    @Version
//    @JsonFormat(pattern = TIMESTAMP, timezone="GMT+8")
    @Column(name="updatetime", nullable = false)
    protected long updateTime;

    @Column(name="createtime", updatable = false, nullable = false)
    protected long createTime;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
