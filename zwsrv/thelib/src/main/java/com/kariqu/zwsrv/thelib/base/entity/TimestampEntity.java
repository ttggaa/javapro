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
@EntityListeners({ TimestampEntityListener.class })
public class TimestampEntity extends BaseEntity {

    private static final long serialVersionUID = 5952027122905864718L;


//    @Version
//    @JsonFormat(pattern = TIMESTAMP, timezone="GMT+8")
    @Column(updatable = true, nullable = false)
    protected long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
