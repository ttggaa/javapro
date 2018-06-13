package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.BaseModel;

/**
 * Created by simon on 15/01/2018.
 */
public class CoinRewardInfo  extends BaseModel {

    public static final String kCoinRewardSecretKey = "87ea4e210c334b9aaa0d3ef7e25c9d14";

    //"kawaji://reward?title=" + title + "&coins=" + coins + "&points=" + points+ "&redirectURI=" + redirectURI;
    private String title;
    private int    coins;
    private int    points;
    private String redirectURI;

    public CoinRewardInfo() {
        title="";
        redirectURI="";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

//    public String toJsonString() {
//        String jsonString = JsonUtil.convertObject2Json(this);
//        if (jsonString!=null&&jsonString.length()>0) {
//
//        }
//        AESCoder.encrypt(kCoinRewardSecretKey.getBytes(),)
//        return JsonUtil.convertObject2Json(this);
//    }

    //87ea4e210c334b9aaa0d3ef7e25c9d14

}
