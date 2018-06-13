package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.Application;
import com.kariqu.zwsrv.thelib.util.MD5;
import org.springframework.stereotype.Service;
import push.AndroidNotification;
import push.PushClient;
import push.android.AndroidBroadcast;
import push.android.AndroidCustomizedcast;
import push.ios.IOSBroadcast;
import push.ios.IOSCustomizedcast;

/**
 * Created by simon on 22/12/17.
 */
@Service
public class UmengPushService {

    private static final String NOTIFICATION_TITLE = "咔哇机";
    private static final String NOTIFICATION_ALIAS_TYPE = "kawaji";

    private static final String ANDROID_APPKEY = "5a2b71fbb27b0a25c300005b";
    private static final String ANDROID_APP_MASTER_SECRET = "kjesgulav56etrymcwvlvh97kyhxpll5";

    private static final String IOS_APPKEY = "5a2b7216b27b0a653a0001a2";
    private static final String IOS_APP_MASTER_SECRET = "qdbab7j4knisdl9jpktiesgqoasivcmy";


    static boolean PRODUCTION_MODE() {
        return Application.isProdEnv;
    }

    static String NOTIFICATION_ALIAS(int userId) {
        return "kawaji_"+MD5.encode(String.valueOf(userId));
    }

    //platform new String[]{"IOS","Android"}
    public static final int PLATFORM_IOS     = 0x0001;
    public static final int PLATFORM_ANDROID = 0x0010;
    public boolean sendNotification(int userId, String alert, String redirectURI, int platform) {
        try {
            PushClient client = new PushClient();

            AndroidCustomizedcast androidCustomizedcast = new AndroidCustomizedcast(ANDROID_APPKEY,ANDROID_APP_MASTER_SECRET);
            // TODO Set your alias here, and use comma to split them if there are multiple alias.
            // And if you have many alias, you can also upload a file containing these alias, then
            // use file_id to send customized notification.
            androidCustomizedcast.setAlias(UmengPushService.NOTIFICATION_ALIAS(userId), UmengPushService.NOTIFICATION_ALIAS_TYPE);
            androidCustomizedcast.setTicker(alert);
            androidCustomizedcast.setTitle(UmengPushService.NOTIFICATION_TITLE);
            androidCustomizedcast.setText(alert);
            androidCustomizedcast.goAppAfterOpen();
            androidCustomizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // TODO Set 'production_mode' to 'false' if it's a test device.
            // For how to register a test device, please see the developer doc.
            androidCustomizedcast.setProductionMode(UmengPushService.PRODUCTION_MODE());
            androidCustomizedcast.setExtraField("redirectURI", redirectURI != null ? redirectURI : "");


            IOSCustomizedcast customizedcast = new IOSCustomizedcast(IOS_APPKEY,IOS_APP_MASTER_SECRET);
            // TODO Set your alias and alias_type here, and use comma to split them if there are multiple alias.
            // And if you have many alias, you can also upload a file containing these alias, then
            // use file_id to send customized notification.
            customizedcast.setAlias(UmengPushService.NOTIFICATION_ALIAS(userId), UmengPushService.NOTIFICATION_ALIAS_TYPE);
            customizedcast.setAlert(alert);
            customizedcast.setBadge(0);
            customizedcast.setSound("default");
            customizedcast.setProductionMode(UmengPushService.PRODUCTION_MODE());
            customizedcast.setCustomizedField("redirectURI", redirectURI!=null?redirectURI:"");

            if (platform==0) {
                return client.send(androidCustomizedcast)&&client.send(customizedcast);
            } else {
                int tmp = 0;
                tmp = platform&PLATFORM_ANDROID;
                if (tmp>0) {
                    return client.send(androidCustomizedcast);
                }
                tmp = platform&PLATFORM_IOS;
                if (tmp>0) {
                    return client.send(customizedcast);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendBroadcastNotification(String alert, String redirectURI) {
        try {
            PushClient client = new PushClient();

            AndroidBroadcast androidBroadcast = new AndroidBroadcast(ANDROID_APPKEY,ANDROID_APP_MASTER_SECRET);
            androidBroadcast.setTicker(alert);
            androidBroadcast.setTitle(UmengPushService.NOTIFICATION_TITLE);
            androidBroadcast.setText(alert);
            androidBroadcast.goAppAfterOpen();
            androidBroadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // For how to register a test device, please see the developer doc.
            androidBroadcast.setProductionMode(UmengPushService.PRODUCTION_MODE());
            // Set customized fields
            androidBroadcast.setExtraField("redirectURI", redirectURI!=null?redirectURI:"");

            IOSBroadcast broadcast = new IOSBroadcast(IOS_APPKEY,IOS_APP_MASTER_SECRET);
            broadcast.setAlert(alert);
            broadcast.setBadge(0);
            broadcast.setSound("default");
            broadcast.setProductionMode(UmengPushService.PRODUCTION_MODE());
            broadcast.setCustomizedField("redirectURI", redirectURI!=null?redirectURI:"");

            return client.send(androidBroadcast) && client.send(broadcast);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getTimeStamp() {
        return Integer.toString((int)(System.currentTimeMillis() / 1000));
    }

}

//
//zww_auth {deivce_id,deivce_brand,os,sys_lang,sys_version,sys_model}
//        zww_user { }
//
//        deivce_id    //设备id
//        deivce_brand //手机厂商
//        os      //android/ios/win/mac
//        lang    //获取当前手机系统语言
//        version //系统版本号
//        model   //手机型号

