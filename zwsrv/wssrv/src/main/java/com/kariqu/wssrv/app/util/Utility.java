package com.kariqu.wssrv.app.util;

import com.kariqu.wssrv.app.room.pkg.RoomCmd;
import com.kariqu.wssrv.app.ws.WsCmdHead;
import com.kariqu.wssrv.app.ws.WsCmdType;
import com.kariqu.wssrv.app.ws.WsSessionBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static String encrypt(String data) {
        try {
            String jsonStringEncrypt = AESCoder.encrypt(WsSessionBase.cmdSecretKey, data);
            return jsonStringEncrypt;
        } catch (Exception e) {
        }
        return "";
    }

    public static String createTbjCmdJsonString(RoomCmd.CommandBase base)
    {
        WsCmdHead cmd = new WsCmdHead();
        cmd.setType(WsCmdType.REQ_TBJ_CMD.getType());
        cmd.setContent(base.toString());
        String str = JsonUtil.convertObject2Json(cmd);
        return encrypt(str);
    }

    public static int ParseUserIdToInt(String useridStr) {
        if (useridStr == null || useridStr.isEmpty())
            return -1;
        try {
            //娃娃机 userid: krq_author_%d
            String reg = "^krq_author_(\\d+)$";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(useridStr);
            if (m.find()) {
                return Integer.valueOf(m.group(1));
            }

            reg = "^krq_audience_(\\d+)$";
            p = Pattern.compile(reg);
            m = p.matcher(useridStr);
            if (m.find()) {
                return Integer.valueOf(m.group(1));
            }
        } catch (Exception e) {
        }
        return -1;
    }
}
