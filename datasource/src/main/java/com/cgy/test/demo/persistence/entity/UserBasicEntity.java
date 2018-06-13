package com.cgy.test.demo.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_basic")
public class UserBasicEntity {

    public UserBasicEntity() {
        this.userId = 0;
        this.nickname = "";
    }

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "nickname")
    private String nickname;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
