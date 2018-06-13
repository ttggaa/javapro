package com.kariqu.zwsrv.app.error;

import com.kariqu.zwsrv.thelib.error.ErrorCode;

/**
 * Created by simon on 27/11/17.
 */
public class ErrorDefs extends ErrorCode {

    public static final int ERROR_CODE_GAME_BASE = ErrorCode.ERROR_CODE_BASE+600;
    public static final ErrorEntry ERROR_CODE_ACCOUNT_NO_ENOUGH_COINS = new ErrorEntry(ERROR_CODE_GAME_BASE+1, "账户中没有足够的金币");
    public static final ErrorEntry ERROR_CODE_ACCOUNT_NO_ENOUGH_POINTS = new ErrorEntry(ERROR_CODE_GAME_BASE+2, "账户中没有足够的积分");

    public static final ErrorEntry ERROR_CODE_THIRDPARTY_TIMEOUT = new ErrorEntry(ERROR_CODE_GAME_BASE+5, "第三方支付延迟,请重试");

    public static final ErrorEntry ERROR_CODE_GAME_ROOM_UNDER_LINE = new ErrorEntry(ERROR_CODE_GAME_BASE+21, "已下线,请更换房间");
    public static final ErrorEntry ERROR_CODE_GAME_ROOM_BREAKDOWN = new ErrorEntry(ERROR_CODE_GAME_BASE+22, "很抱歉出了故障,我们会很快恢复");
    public static final ErrorEntry ERROR_CODE_GAME_QUERY_DELIVERY_NO_VALID_GAMES = new ErrorEntry(ERROR_CODE_GAME_BASE+25, "不存在有效的Game");


    public static final ErrorEntry ERROR_CODE_ROOM_STATUS_IN_GAME = new ErrorEntry(ERROR_CODE_GAME_BASE+31, "被别人抢先开始了");
    public static final ErrorEntry ERROR_CODE_ROOM_STATUS_IN_GAME_UNLIMIT = new ErrorEntry(ERROR_CODE_GAME_BASE+32, "被别人抢先开始了");


    public static final ErrorEntry ERROR_CODE_GAME_MAINT_STATUS_TESTING = new ErrorEntry(ERROR_CODE_GAME_BASE+51, "测试中请耐心等待");
    public static final ErrorEntry ERROR_CODE_GAME_MAINT_STATUS_FILLING = new ErrorEntry(ERROR_CODE_GAME_BASE+52, "正在补货,请耐心等待");
    public static final ErrorEntry ERROR_CODE_GAME_MAINT_STATUS_UNDER_MAINTING = new ErrorEntry(ERROR_CODE_GAME_BASE+53, "维护中,请耐心等待");


    public static final ErrorEntry ERROR_CODE_GAME_PAY_INVALID_PAY_METHOD = new ErrorEntry(ERROR_CODE_GAME_BASE+61, "无效支付方式");

    public static final ErrorEntry ERROR_CODE_DELIVERY_INVALID_USER_ADDRESS = new ErrorEntry(ERROR_CODE_GAME_BASE+71, "无效配送地址");
    public static final ErrorEntry ERROR_CODE_DELIVERY_INVALID_SHIPPING = new ErrorEntry(ERROR_CODE_GAME_BASE+73, "配送方式无效,请更换配送方式");
    public static final ErrorEntry ERROR_CODE_DELIVERY_INVALID_PAY_METHOD = new ErrorEntry(ERROR_CODE_GAME_BASE+74, "无效支付方式");

//    public static final ErrorEntry ERROR_CODE_DELIVERY_NO_ENOUGH_POINTS = new ErrorEntry(ERROR_CODE_GAME_BASE+77, "没有足够的积分");
//    public static final ErrorEntry ERROR_CODE_DELIVERY_NO_ENOUGH_COINS = new ErrorEntry(ERROR_CODE_GAME_BASE+78, "没有足够的金币");

    public static final ErrorEntry ERROR_CODE_USER_INVALID_INVITATION_CODE = new ErrorEntry(ERROR_CODE_GAME_BASE+81, "无效的邀请码");
    public static final ErrorEntry ERROR_CODE_USER_ALREADY_BIND_INVITATION_CODE = new ErrorEntry(ERROR_CODE_GAME_BASE+82, "已绑定邀请码");

    public static final ErrorEntry ERROR_CARD_UNKNOWN_TYPE = new ErrorEntry(ERROR_CODE_GAME_BASE + 100, "领取类型错误");
    public static final ErrorEntry ERROR_CARD_UNKNOWN_ID = new ErrorEntry(ERROR_CODE_GAME_BASE + 102, "未知的奖励");
    public static final ErrorEntry ERROR_CARD_EXPIRED = new ErrorEntry(ERROR_CODE_GAME_BASE + 103, "已经超过期限");
    public static final ErrorEntry ERROR_CARD_NONE_CAN_REWARD = new ErrorEntry(ERROR_CODE_GAME_BASE + 104, "没有可以领取的奖励");

    public static final ErrorEntry ERROR_CODE_BILLING_TYPE_ERROR = new ErrorEntry(ERROR_CODE_GAME_BASE+150, "计费类型错误");
    public static final ErrorEntry ERROR_CODE_UNKNOWN_CHARGE_ID = new ErrorEntry(ERROR_CODE_GAME_BASE+151, "无效的充值选项");
    public static final ErrorEntry ERROR_CODE_NEW_USER_GIFT_REPEATED = new ErrorEntry(ERROR_CODE_GAME_BASE+152, "已经获得过新手礼包，无法再次获得");
    public static final ErrorEntry ERROR_CODE_CHARGE_FIRST_REPEATED = new ErrorEntry(ERROR_CODE_GAME_BASE+153, "已经冲过首冲，无法再次获得");

    public static final ErrorEntry ERROR_CODE_UNKNWON_USER = new ErrorEntry(ERROR_CODE_GAME_BASE + 160, "用户不存在");

    public static final ErrorEntry ERROR_CODE_ITEM_CAN_NOT_EXPRESS = new ErrorEntry(ERROR_CODE_GAME_BASE + 170, "物品不能选择发货");
    public static final ErrorEntry ERROR_CODE_ITEM_UNKNOWN_USER_ITEM = new ErrorEntry(ERROR_CODE_GAME_BASE + 171, "没有该物品");
    public static final ErrorEntry ERROR_CODE_ITEM_UNKNOWN_ITEM = new ErrorEntry(ERROR_CODE_GAME_BASE + 172, "无法找到物品类型");
    public static final ErrorEntry ERROR_CODE_ITEM_EXCHANGE_HUA_FEI = new ErrorEntry(ERROR_CODE_GAME_BASE + 173, "无法兑换成话费");
    public static final ErrorEntry ERROR_CODE_ITEM_REPEATED_EXCHANGE_HUA_FEI = new ErrorEntry(ERROR_CODE_GAME_BASE + 174, "重复兑换话费");
    public static final ErrorEntry ERROR_CODE_ITEM_UNKNOWN_FRAGMENT = new ErrorEntry(ERROR_CODE_GAME_BASE + 175, "无效的碎片");
    public static final ErrorEntry ERROR_CODE_ITEM_FRAGMENT_INSUFFICIENT = new ErrorEntry(ERROR_CODE_GAME_BASE + 176, "碎片数量类型不同");
    public static final ErrorEntry ERROR_CODE_ITEM_EXCHANGE_COINS_FAILED = new ErrorEntry(ERROR_CODE_GAME_BASE + 177, "无法兑换成娃娃币");

    public static final ErrorEntry ERROR_CODE_TOO_LONG = new ErrorEntry(ERROR_CODE_GAME_BASE + 180, "内容太长");


//    delivery
}
