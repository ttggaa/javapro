package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.app.model.PlatDictInfo;
import com.kariqu.zwsrv.thelib.persistance.entity.PlatDict;
import com.kariqu.zwsrv.thelib.persistance.service.PlatDictService;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by simon on 21/12/17.
 */
@Service
public class PlatDictServiceCache extends PlatDictService {

    public static final String kPrefsBindInvitationCodeRewardCoinsNum = "kPrefsBindInvitationCodeRewardCoinsNum"; //默认 50,绑定邀请码,双方获得50
    public static final String kPrefsNewUserRewardCoinsNum = "kPrefsNewUserRewardCoinsNum"; //默认 50, 新用户注册
    public static final String kPrefsExchangePointToCoinsRatio = "kPrefsExchangePointToCoinsRatio"; //默认 10,金币兑换比率

    public static final String kPrefsShippingFreeDollsNum = "kPrefsShippingFreeDollsNum"; //默认 2,几个玩偶免邮
    public static final String kPrefsShippingPayCoinsNum = "kPrefsShippingPayCoinsNum"; //默认 2,邮费金币数

//    public static final String kPrefsAppQRCodeImageUrl = "kPrefsAppQRCodeImageUrl"; //分享app二维码地址
//    public static final String kPrefsWxServiceId = "kPrefsWxServiceId"; //微信客服号
//    public static final String kPrefsWxServiceQRCodeImageUrl = "kPrefsWxServiceQRCodeImageUrl"; //微信二维码地址

    public static final String kSignin = "kSignin"; // 签到奖励配置项

    public static final String kInvitationMaxCount = "kInvitationMaxCount";     // 被绑定邀请最大次数(含)
    public static final String kGoodsMaxStorageTime = "kGoodsMaxStorageTime";   // 抓到娃娃寄存时间(毫秒)
    public static final String kGoodsDeliveryPoints = "kGoodsDeliveryPoints"; // 邮寄娃娃所需积分

    Map<String,String> nameValueMap = new ConcurrentHashMap<String,String>() {
        {
            put(kPrefsBindInvitationCodeRewardCoinsNum,String.valueOf(50));

            put(kPrefsNewUserRewardCoinsNum,String.valueOf(50));
            put(kPrefsExchangePointToCoinsRatio,String.valueOf(10));

            put(kPrefsShippingFreeDollsNum,String.valueOf(2));
            put(kPrefsShippingPayCoinsNum,String.valueOf(2));

            put(kInvitationMaxCount, String.valueOf(10));

//            put(kPrefsAppQRCodeImageUrl,"");
//            put(kPrefsWxServiceId,"");
//            put(kPrefsWxServiceQRCodeImageUrl,"");
        }
    };

    public int getBindInvitationCodeRewardCoinsNum() {
        return getInteger(kPrefsBindInvitationCodeRewardCoinsNum);
    }

    public int getNewUserRewardCoinsNum() {
        return getInteger(kPrefsNewUserRewardCoinsNum);
    }

    public int getExchangePointToCoinsRatio() {
        return getInteger(kPrefsExchangePointToCoinsRatio);
    }

    public int getShippingFreeDollsNum() {
        return getInteger(kPrefsShippingFreeDollsNum);
    }

    public int getShippingPayCoinsNum() {
        return getInteger(kPrefsShippingPayCoinsNum);
    }

    public int getInvitationMaxCount() { return getInteger(kInvitationMaxCount);}
    public long getGoodsMaxStorageTime() { return getLong(kGoodsMaxStorageTime); }
    public int getGoodsDeliveryPoints() { return getInteger(kGoodsDeliveryPoints); }

    public List<PlatDictInfo> getPlatConfigList() {
        List<PlatDictInfo> platDictInfoList = new ArrayList<>();
        List<PlatDict> list = findAllPlatConfig();
        if (list==null) {
            list=new ArrayList<>();
        }

        for (PlatDict platDict : list) {
            PlatDictInfo dictInfo = new PlatDictInfo(platDict);
            if (platDict.getIsUrl()>0) {
                dictInfo.setValue(URLHelper.fullUrl(platDict.getValue()));
            }
            platDictInfoList.add(dictInfo);
        }
        return platDictInfoList;
    }

    public long getLong(String name) {
        List<String> nameList = new ArrayList<>();
        nameList.add(name);
        List<PlatDict> list = findByName(nameList);
        if (list!=null&&list.size()>0) {
            String value = list.get(0).getValue();
            if (NumberValidationUtil.isWholeNumber(value)) {
                return Long.valueOf(value);
            }
        }
        String value = nameValueMap.get(name);
        if (value!=null) {
            return Long.valueOf(value);
        }
        return 0;
    }

    public int getInteger(String name) {
        List<String> nameList = new ArrayList<>();
        nameList.add(name);
        List<PlatDict> list = findByName(nameList);
        if (list!=null&&list.size()>0) {
            String value = list.get(0).getValue();
            if (NumberValidationUtil.isWholeNumber(value)) {
                return Integer.valueOf(value);
            }
        }
        String value = nameValueMap.get(name);
        if (value!=null) {
            return Integer.valueOf(value);
        }
        return 0;
    }

}
