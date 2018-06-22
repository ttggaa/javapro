package com.kariqu.tyt.http.pkg;

public class ResponseError {
    public static final ResponseData Success = new ResponseData();
    public static final ResponseData BodyEmpty = new ResponseData(1, "body empty");
    public static final ResponseData ContentInvalid = new ResponseData(2, "content invalid");
    public static final ResponseData WechatSessionError = new ResponseData(3, "get wechat session error");
    public static final ResponseData UnknownError = new ResponseData(4, "unknown error");
    public static final ResponseData LoginError = new ResponseData(5, "login failed");
    public static final ResponseData CoinsInsufficient = new ResponseData(6, "coin insufficient");

    // 无法领取任务奖励
    public static final ResponseData MissionCannotReward = new ResponseData(1000, "mission reward can not reward");

    // 已经签到了，不能重复签到
    public static final ResponseData RepeatedSignin = new ResponseData(2000, "repeated signin");
}
