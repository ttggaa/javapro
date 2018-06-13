package com.kariqu.zwsrv.thelib.persistance.entity;

import com.kariqu.zwsrv.thelib.base.entity.VersionedTimedEntity;
import com.kariqu.zwsrv.thelib.util.LocalDateTimeUtils;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name="zww_user_signin")
public class UserSignin extends VersionedTimedEntity {
    public UserSignin()
    {
        signinDateTime = LocalDateTimeUtils.localDateTimeToDate(LocalDateTime.now());
    }

    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "signin_id")
    private int signinId;

    @Column(name = "signin_datetime")
    private Date signinDateTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSigninId() {
        return signinId;
    }

    public void setSigninId(int signinId) {
        this.signinId = signinId;
    }

    public Date getSigninDateTime() {
        return signinDateTime;
    }

    public void setSigninDateTime(Date signinDateTime) {
        this.signinDateTime = signinDateTime;
    }
}
