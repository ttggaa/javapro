package com.kariqu.zwsrv.app.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by simon on 22/12/17.
 */
public class URLScheme {

    //启动app kawaji://launch 参数无
    public static String kURLSchemeLaunch() {
        return "kawaji://launch";
    }

    //跳转到充值账单 kawaji://listBuyCoins 参数无
    public static String kURLSchemeListBuyCoins() {
        return "kawaji://listBuyCoins";
    }

    //跳转到娃娃币账单 kawaji://listCoins 参数无
    public static String kURLSchemeListCoins() {
        return "kawaji://listCoins";
    }

    //打开一个h5页面
    public static String kURLSchemeOpenUrl(String url) {
        return "kawaji://openUrl?url="+urlEncode(url);
    }

    //跳转到某个房间
    public static String kURLSchemeRoom(int roomId) {
        return "kawaji://room?roomId="+roomId;
    }

    //获奖励通知
    public static String kURLSchemeReward(String title, int coins, int points, String redirectURI) {
        return "kawaji://reward?title=" + title + "&coins=" + coins + "&points=" + points+ "&redirectURI=" + redirectURI;
    }

    static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
