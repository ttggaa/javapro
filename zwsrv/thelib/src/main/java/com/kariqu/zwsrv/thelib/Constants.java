package com.kariqu.zwsrv.thelib;

/**
 * Created by simon on 25/11/17.
 */
public class Constants {

    ///
    //头像审核状态
    public static final int AvatarStatusNormal=0; //正常
    public static final int AvatarStatusInReview=1; //审核中
    public static final int AvatarStatusIllegal=2;//不合法

    ///
    public static final String AppIdZW = "zw";
    public static boolean isValidAppId(String appId) {
        if (appId!=null
                &&Constants.AppIdZW.equalsIgnoreCase(appId)) {
            return true;
        }
        return false;
    }



    ///
    public static final String AuthTypeUserName = "username";
    public static final String AuthTypeWeChat = "wechat";

    public static final String AuthTypePhone = "phone";
    public static final String AuthTypeQQ = "qq";
    public static final String AuthTypeSina = "sina";

    public static boolean isValidAuthType(String authType) {
        if (authType!=null) {
            if (authType.equalsIgnoreCase(AuthTypeWeChat)
                    || authType.equalsIgnoreCase(AuthTypeUserName)
                    || authType.equalsIgnoreCase(AuthTypePhone))
                return true;
        }
        return false;
    }


    public static final String TMP_DIR_NAME = "tmp";
    public static final String DATA_DIR_NAME = "data";

    public static final String SUB_DIR_NAME_PLATFORM = "platform";
    public static final String SUB_DIR_NAME_ROOM = "room";

}
