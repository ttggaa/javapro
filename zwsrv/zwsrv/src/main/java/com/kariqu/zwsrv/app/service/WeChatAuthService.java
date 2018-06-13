package com.kariqu.zwsrv.app.service;

import com.alibaba.fastjson.JSONObject;
import com.kariqu.zwsrv.app.model.WeChatUserInfo;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by simon on 24/11/17.
 */
@Service
public class WeChatAuthService {

    @Autowired
    RestTemplate restTemplate;

    //请求此地址即跳转到二维码登录界面
    private static final String AUTHORIZATION_URL =
            "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";

    // 获取用户 openid 和access——toke 的 URL
    private static final String ACCESSTOKE_OPENID_URL =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private static final String REFRESH_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";

    private static final String USER_INFO_URL =
            "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    private static final String APP_ID="wx10772583ba2a35a4";
    private static final String APP_SECRET="5ce1809b57db192ffecb9b9008fad7ca";
    private static final String SCOPE = "snsapi_login";

    private String CALLBACK_URL = "https://www.xxx.cn/auth/wechat"; //回调域名


    public String getAuthorizationUrl() throws UnsupportedEncodingException {
        String url = String.format(AUTHORIZATION_URL,APP_ID,URLEncoder.encode(CALLBACK_URL, "utf-8"),SCOPE,System.currentTimeMillis());
        return url;
    }

    public String getAccessToken(String code) {
        String url = String.format(ACCESSTOKE_OPENID_URL,APP_ID,APP_SECRET,code);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();

        String resp = restTemplate.getForObject(uri, String.class);
        if(resp.contains("openid")){
            JSONObject jsonObject = JSONObject.parseObject(resp);
            String access_token = jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");;

            JSONObject res = new JSONObject();
            res.put("access_token",access_token);
            res.put("openId",openId);
            res.put("refresh_token",jsonObject.getString("refresh_token"));
            return res.toJSONString();
        }else{
            //"获取token失败，msg = "+resp
            throw new ServerException(ErrorCode.ERROR_FAILED);
        }
    }

    public WeChatUserInfo getUserInfo(String accessToken, String openId){
        String url = String.format(USER_INFO_URL, accessToken, openId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();

        String resp = restTemplate.getForObject(uri, String.class);
        if(resp.contains("errcode")) {
            //"获取用户信息错误，msg = "+resp
            throw new ServerException(ErrorCode.ERROR_FAILED);
        } else {
            JSONObject data =JSONObject.parseObject(resp);

            WeChatUserInfo userInfo =new WeChatUserInfo();

            userInfo.openId=data.getString("openid")!=null?data.getString("openid"):"";
            userInfo.nickName = data.getString("nickname")!=null?data.getString("nickname"):"";
            userInfo.sex = data.getInteger("sex")!=null?data.getInteger("sex").intValue():0;
            userInfo.language = data.getString("language")!=null?data.getString("language"):"";
            userInfo.city = data.getString("city")!=null?data.getString("city"):"";
            userInfo.province = data.getString("province")!=null?data.getString("province"):"";
            userInfo.country = data.getString("country")!=null?data.getString("country"):"";
            userInfo.avatarUrl = data.getString("headimgurl")!=null?data.getString("headimgurl"):"";
            userInfo.unionId = data.getString("unionid")!=null?data.getString("unionid"):"";
            //用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。

            return userInfo;
        }
    }

    //微信的token只有2小时的有效期，过时需要重新获取，所以官方提供了
    //根据refresh_token 刷新获取token的方法，本项目仅仅是获取用户
    //信息，并将信息存入库，所以两个小时也已经足够了
    public String refreshToken(String refresh_token) {

        String url = String.format(REFRESH_TOKEN_URL,APP_ID,refresh_token);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();

        ResponseEntity<JSONObject> resp = restTemplate.getForEntity(uri,JSONObject.class);
        JSONObject jsonObject = resp.getBody();

        String access_token = jsonObject.getString("access_token");
        return access_token;
    }

    //retJson = "{\"openid\":\"oJsEcw90K5RZGjhMPhsS_kG1RHJQ\",\"nickname\":\"livwCyz\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"Dalian\",\"province\":\"Liaoning\",\"country\":\"CN\",\"headimgurl\":\"http:\\/\\/wx.qlogo.cn\\/mmopen\\/PiajxSqBRaEKkiatIuYpZO40zAU8wiar61NiaibFV1cWGNkJTYDkgjjB1OTbcEicZEgaoRpkzKNfRYvLzIsZY3ia9rG6Q\\/0\",\"privilege\":[],\"unionid\":\"o9bzTwwRdWmFkpOWFDFboPQa30HM\"}";
    //retJson = "{\"errcode\":40029,\"errmsg\":\"invalid code, hints: [ req_id: 1OaupA0216s110 ]\"}";
}
//
//http://blog.csdn.net/u010978008/article/details/73896306



//AppID: wx10772583ba2a35a4
//AppSecret: 5ce1809b57db192ffecb9b9008fad7ca


//融云:
//893274551@qq.com   qiqi1234
//App Key: c9kqb3rdcmh8j
//App Secret: GyQqVVAEz1EpW

//融云：893274551@qq.com  qiqi1234


//友盟
//893274551@qq.com  difference


