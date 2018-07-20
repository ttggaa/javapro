package gamebox.web.model;

import gamebox.web.persistence.entity.UserEntity;
import org.apache.catalina.User;

public class UserModel {
    private int userId;
    private String nickname;
    private String openid;
    private String unionid;
    private int coins;
    private String createtime;

    public UserModel(UserEntity entity) {
        this.userId = entity.getUserId();
        this.openid = entity.getWechatOpenid();
        this.unionid = entity.getWechatUnionid();
        this.coins = entity.getCoins();
        this.createtime = entity.getCreatetime().toString();
    }

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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
