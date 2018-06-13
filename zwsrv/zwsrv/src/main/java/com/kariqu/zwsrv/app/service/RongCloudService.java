//package com.kariqu.zwsrv.app.service;
//
//import com.kariqu.zwsrv.app.Application;
//import com.kariqu.zwsrv.thelib.persistance.entity.RongToken;
//import com.kariqu.zwsrv.thelib.persistance.service.RongTokenService;
//import com.kariqu.zwsrv.thelib.util.AESCoder;
//import io.rong.RongCloud;
//import io.rong.messages.TxtMessage;
//import io.rong.models.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Base64;
//import java.util.Map;
//
///**
// * Created by simon on 25/11/17.
// */
//@Service
//public class RongCloudService {
//
//    String appKeyDev = "c9kqb3rdcmh8j";//替换成您的appkey
//    String appSecretDev = "GyQqVVAEz1EpW";//替换成匹配上面key的secret
//
//    String appKey = "c9kqb3rdcmh8j";//"pvxdm17jplt1r";//替换成您的appkey
//    String appSecret = "GyQqVVAEz1EpW";//"DZsxQ18VjVMpKv";//替换成匹配上面key的secret
//
//
//    private RongCloud rongCloud;
//    private synchronized RongCloud getRongCloud() {
//        if (rongCloud==null) {
//            if (Application.isProdEnv) {
//                rongCloud = RongCloud.getInstance(appKey, appSecret);
//            } else {
//                rongCloud = RongCloud.getInstance(appKeyDev, appSecretDev);
//            }
//        }
//        return rongCloud;
//    }
//
//    @Autowired
//    RongTokenService rongTokenService;
//
//    // 获取 Token
//    public RongToken requestToken(int userId, String nickname, String avatarUrl) {
//        return requestToken(userId,nickname,avatarUrl,false);
//    }
//    public RongToken requestToken(int userId, String nickname, String avatarUrl, boolean refresh) {
//        RongToken rongToken = rongTokenService.findOne(userId);
//        if (rongToken==null) {
//            rongToken = new RongToken();
//            rongToken.setUserId(userId);
//        }
//
//        String token = "";
//
//        if (rongToken.getToken()!=null
//                &&rongToken.getToken().length()>0
//                &&rongToken.getUserSN()!=null
//                &&rongToken.getUserSN().length()>0
//                &&!refresh) {
//            if (!rongToken.getNickName().equalsIgnoreCase(nickname)
//                    ||!rongToken.getAvatarUrl().equalsIgnoreCase(avatarUrl)) {
//                if (refresh(rongToken.getUserSN(),rongToken.getNickName(),rongToken.getAvatarUrl())) {
//                    rongToken.setNickName(nickname);
//                    rongToken.setAvatarUrl(avatarUrl);
//                    rongToken = rongTokenService.save(rongToken);
//                }
//            }
//            token = rongToken.getToken();
//        } else {
//            String userSN = rongToken.getUserSN();
//            if (userSN==null||userSN.length()==0) {
//                userSN=generateUserSN(userId);
//                if (userSN==null||userSN.length()==0) {
//                    return null;//生成userSN失败直接返回NULL
//                }
//                rongToken.setUserSN(userSN);
//            }
//            rongToken.setNickName(nickname);
//            rongToken.setAvatarUrl(avatarUrl);
//
//            token = requestToken(userSN,nickname,avatarUrl);
//            if (token!=null&&token.length()>0) {
//                rongToken.setToken(token);
//                rongToken = rongTokenService.save(rongToken);
//            }
//        }
//        if (token!=null&&token.length()>0) {
//            return rongToken;
//        }
//        return null;
//    }
//
//    String requestToken(String userSN, String nickname, String avatarUrl) {
//        String token = "";
//        try {
//            TokenResult reslut = getRongCloud().user.getToken(userSN, nickname, avatarUrl!=null?avatarUrl:"");
//            if (reslut.getCode()==200) {
//                token = reslut.getToken();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return token;
//    }
//
//    boolean refresh(String userSN, String nickname, String avatarUrl) {
//        try {
//            CodeSuccessResult reslut = getRongCloud().user.refresh(userSN,nickname, avatarUrl!=null?avatarUrl:"");
//            if (reslut.getCode()==200) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public static final byte[] userSNEncryptKey = new byte[] {'A','1', '2', '3', 'E', 'f',
//            'g', 'H', 'i', 'j', 'k', 'L', '2', '0', '1', '7'};
//
//    public static String generateUserSN(int userId) {
//        String valueToEnc="RongUsrSN|"+String.valueOf(userId);
//        try {
//            return Base64.getEncoder().encodeToString(AESCoder.encrypt(userSNEncryptKey, valueToEnc).getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//
//    /**
//     * 发送广播消息方法（发送消息给一个应用下的所有注册用户，如用户未在线会对满足条件（绑定手机终端）的用户发送 Push 信息，单条消息最大 128k，会话类型为 SYSTEM。每小时只能发送 1 次，每天最多发送 3 次。）
//     *
//     * @param  fromUserId:发送人用户 Id。（必传）
//     * @param  txtMessage:文本消息。
//     * @param  pushContent:定义显示的 Push 内容，如果 objectName 为融云内置消息类型时，则发送后用户一定会收到 Push 信息. 如果为自定义消息，则 pushContent 为自定义消息显示的 Push 内容，如果不传则用户不会收到 Push 通知.（可选）
//     * @param  pushData:针对 iOS 平台为 Push 通知时附加到 payload 中，Android 客户端收到推送消息时对应字段名为 pushData。（可选）
//     * @param  os:针对操作系统发送 Push，值为 iOS 表示对 iOS 手机用户发送 Push ,为 Android 时表示对 Android 手机用户发送 Push ，如对所有用户发送 Push 信息，则不需要传 os 参数。（可选）
//     *
//     * @return CodeSuccessResult
//     **/
//    //
//    //String fromUserId, BaseMessage message, String pushContent, String pushData, String os
//    public boolean broadcastMessage(String fromUserId, String content, String pushContent) {
//        try {
//            TxtMessage message= new TxtMessage(pushContent,content);
//            CodeSuccessResult result= getRongCloud().message.broadcast(fromUserId,message,pushContent,content,"");
//            if (result.getCode()==200) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean broadcastPush(String fromUserId, String alert, Map<String,String> contentTempdddd) {
//
//        try {
//            TagObj tagObj=new TagObj(null,null,true);
//
//            PlatformNotification iosNotification = new PlatformNotification(alert,contentTempdddd);
//            PlatformNotification androidNotification = new PlatformNotification(alert,contentTempdddd);
//            Notification notification=new Notification(alert,iosNotification,androidNotification);
//
//            PushMessage pushMessage = new PushMessage(new String[]{"IOS","Android"},fromUserId,tagObj,null,notification);
//            CodeSuccessResult result=getRongCloud().push.broadcastPush(pushMessage);
//            if (result.getCode()==200) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//
//    public boolean sendPush(String fromUserId, String toUserId, String alert, Map<String,String> content) {
//
//        try {
//            TagObj tagObj=new TagObj(null,new String[]{toUserId},false);
//
//            PlatformNotification iosNotification = new PlatformNotification(alert,content);
//            PlatformNotification androidNotification = new PlatformNotification(alert,content);
//            Notification notification=new Notification(alert,iosNotification,androidNotification);
//
//
////            MsgObj msgObj = new MsgObj("ddddddddd","");
//
//            PushMessage pushMessage = new PushMessage(new String[]{"IOS","Android"},fromUserId,tagObj,null,notification);
//            CodeSuccessResult result=getRongCloud().push.broadcastPush(pushMessage);
//            if (result.getCode()==200) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//}
