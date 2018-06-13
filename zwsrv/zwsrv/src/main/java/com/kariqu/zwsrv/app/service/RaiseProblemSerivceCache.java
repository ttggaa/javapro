package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.thelib.persistance.service.RaiseProblemService;
import org.springframework.stereotype.Service;

@Service
public class RaiseProblemSerivceCache extends RaiseProblemService {

    public static final int TYPE_HMHP_KZBD  = 1;  // 画面黑屏或卡住不动
    public static final int TYPE_CZANSL     = 2;  // 操作按钮失灵
    public static final int TYPE_ZJKZBD     = 3;  // 爪子卡住不动
    public static final int TYPE_SXTWFQH    = 4;  // 摄像头无法切换
    public static final int TYPE_ZZXSBCG    = 5;  // 抓中显示不成功
    public static final int TYPE_OTHER      = 100;  // 其他

    public static final int MAX_CONTEXT_LENGTH = 100;

    public static String getTypeStr(int type)
    {
        switch (type) {
            case TYPE_HMHP_KZBD: return "画面黑屏或卡住不动";
            case TYPE_CZANSL: return "操作按钮失灵";
            case TYPE_ZJKZBD: return "爪子卡住不动";
            case TYPE_SXTWFQH: return "摄像头无法切换";
            case TYPE_ZZXSBCG: return "抓中显示不成功";
            case TYPE_OTHER: return "其他";
            default:
                return "";
        }
    }

}
