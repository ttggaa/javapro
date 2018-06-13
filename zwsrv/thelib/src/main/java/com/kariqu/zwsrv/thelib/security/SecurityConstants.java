package com.kariqu.zwsrv.thelib.security;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 11/04/17.
 */
public class SecurityConstants {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_CUSTOMER_SERVICE = "ROLE_CUSTOMER_SERVICE";

    public static final String ROLE_USER = "ROLE_USER";

    public static final String ROLE_AUTHOR = "ROLE_AUTHOR";

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String ROLE_SERVER = "ROLE_SERVER";

    public static final int ROLE_TYPE_ANONYMOUS = 0x0000;
    public static final int ROLE_TYPE_USER = 0x0001;
    public static final int ROLE_TYPE_AUTHOR = 0x0010;
    public static final int ROLE_TYPE_ADMIN = 0x0002;
    public static final int ROLE_TYPE_CUSTOM_SERVICE = 0x0004;

    public static List<String> roleStringList(int role) {
        List<String> list = new ArrayList<String>();
        int tmp = role&ROLE_TYPE_USER;
        if (tmp>0) {
            list.add(ROLE_USER);
        }
        tmp = role&ROLE_TYPE_AUTHOR;
        if (tmp>0) {
            list.add(ROLE_AUTHOR);
        }
        tmp = role&ROLE_TYPE_ADMIN;
        if (tmp>0) {
            list.add(ROLE_ADMIN);
        }
        tmp = role&ROLE_TYPE_CUSTOM_SERVICE;
        if (tmp>0) {
            list.add(ROLE_CUSTOMER_SERVICE);
        }
        return list;
    }


    public final static String AUTHORIZATION_HEADER = "X-Auth-Token";

    public final static String AUTHORIZATION_COOKIE_NAME = AUTHORIZATION_HEADER;

    public final static String HTTP_HEADER_KEY_OS = "os";

    public final static String HTTP_HEADER_KEY_DEVICE_ID = "deviceId";

}
