package com.kariqu.zwsrv.thelib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by simon on 19/04/17.
 */
public class StringUtil {

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isMobilePhone(String phone) {
        if(StringUtil.isEmpty(phone)) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

}
