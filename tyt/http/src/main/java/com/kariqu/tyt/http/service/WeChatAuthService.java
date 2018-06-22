package com.kariqu.tyt.http.service;

import com.kariqu.tyt.http.Application;
import com.kariqu.tyt.http.ApplicationConfig;
import com.kariqu.tyt.http.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Service
public class WeChatAuthService {
    public static class WechatSession {
        private String openid;
        private String session_key;
        private String uinionid;
        private long errcode;
        private String errMsg;

        public WechatSession() {
            this.openid = "";
            this.session_key = "";
            this.uinionid = "";
            this.errcode = 0;
            this.errMsg = "";
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSession_key() {
            return session_key;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }

        public String getUinionid() {
            return uinionid;
        }

        public void setUinionid(String uinionid) {
            this.uinionid = uinionid;
        }

        public long getErrcode() {
            return errcode;
        }

        public void setErrcode(long errcode) {
            this.errcode = errcode;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }
    }

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

    private String wechatAuthUrl(String code) {
        // GET https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
        String url = ApplicationConfig.getInstance().getWechatLoginUrl();
        String appid = ApplicationConfig.getInstance().getWechatAppid();
        String secret = ApplicationConfig.getInstance().getWechatLoginSecret();
        String jscode = code;

        try {
            jscode = URLEncoder.encode(code, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", url, appid, secret, jscode);
    }

    public WechatSession getAccessToken(String code) {
        String url = wechatAuthUrl(code);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();

        String resp = restTemplate.getForObject(uri, String.class);
        WechatSession session = Utility.parseJsonToObject(resp, WechatSession.class);
        return session;
    }

    /*
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
    */

    //微信的token只有2小时的有效期，过时需要重新获取，所以官方提供了
    //根据refresh_token 刷新获取token的方法，本项目仅仅是获取用户
    //信息，并将信息存入库，所以两个小时也已经足够了
    /*
    public String refreshToken(String refresh_token) {

        String url = String.format(REFRESH_TOKEN_URL,APP_ID,refresh_token);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();

        ResponseEntity<JSONObject> resp = restTemplate.getForEntity(uri,JSONObject.class);
        JSONObject jsonObject = resp.getBody();

        String access_token = jsonObject.getString("access_token");
        return access_token;
    }
    */

    //retJson = "{\"openid\":\"oJsEcw90K5RZGjhMPhsS_kG1RHJQ\",\"nickname\":\"livwCyz\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"Dalian\",\"province\":\"Liaoning\",\"country\":\"CN\",\"headimgurl\":\"http:\\/\\/wx.qlogo.cn\\/mmopen\\/PiajxSqBRaEKkiatIuYpZO40zAU8wiar61NiaibFV1cWGNkJTYDkgjjB1OTbcEicZEgaoRpkzKNfRYvLzIsZY3ia9rG6Q\\/0\",\"privilege\":[],\"unionid\":\"o9bzTwwRdWmFkpOWFDFboPQa30HM\"}";
    //retJson = "{\"errcode\":40029,\"errmsg\":\"invalid code, hints: [ req_id: 1OaupA0216s110 ]\"}";
}

