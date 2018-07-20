package gamebox.web.persistence.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
public class UserEntity {
    public UserEntity() {
        this.userId = 0;
        this.nickname = "";
        this.wechatUnionid = "";
        this.wechatOpenid = "";
        this.coins = 0;
        this.createtime = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "wechat_unionid", nullable = false)
    private String wechatUnionid;

    @Column(name = "wechat_openid", nullable = false)
    private String wechatOpenid;

    @Column(name = "coins", nullable = false)
    private int coins;

    @Column(name = "createtime", nullable = false)
    private Date createtime;

    public int getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getWechatUnionid() {
        return wechatUnionid;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public int getCoins() {
        return coins;
    }

    public Date getCreatetime() {
        return createtime;
    }
}
