package com.kariqu.zwsrv.thelib.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Created by simon on 4/27/16.
 */

@MappedSuperclass
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners({ CreateTimedEntityListener.class })
public class CreateTimedEntity extends BaseEntity {


    private static final long serialVersionUID = -7471549227069776225L;


//    @JsonFormat(pattern = TIMESTAMP, timezone="GMT+8")
    @Column(name="createtime", updatable = false, nullable = false)
    protected long createTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
