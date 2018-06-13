package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.AccountLogDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import com.kariqu.zwsrv.thelib.persistance.entity.AccountLog;
import com.kariqu.zwsrv.thelib.persistance.entity.AccountLogData;
import com.kariqu.zwsrv.thelib.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 23/02/17.
 */
@Service
public class AccountLogService  extends BaseService<AccountLog> {

    public static final int NO_CHANGED = 0;

    /// 金币
    public static final int CHANGE_TYPE_COINS = 100;
    public static final int CHANGE_TYPE_COINS_GAIN_BUY_ALIPAY=CHANGE_TYPE_COINS+1; //金币获得，支付宝充值
    public static final int CHANGE_TYPE_COINS_GAIN_BUY_WX=CHANGE_TYPE_COINS+2; //金币获得，微信充值

    public static final int CHANGE_TYPE_COINS_GAIN_REWARDS_REGISTER=CHANGE_TYPE_COINS+5; //金币获得，注册/首次登陆
    public static final int CHANGE_TYPE_COINS_GAIN_REWARDS_BIND_INVITATION_CODE=CHANGE_TYPE_COINS+6; //金币获得，邀请
    public static final int CHANGE_TYPE_COINS_GAIN_REWARDS_SHARED=CHANGE_TYPE_COINS+8; //金币获得，分享奖励
    public static final int CHANGE_TYPE_COINS_GAIN_REWARDS_GAME=CHANGE_TYPE_COINS+10; //金币获得，游戏奖励

    public static final int CHANGE_TYPE_COINS_GAIN_EXCHANGE_GOODS=CHANGE_TYPE_COINS+12; //金币获得，礼物兑换
    public static final int CHANGE_TYPE_COINS_GAIN_EXCHANGE_POINTS=CHANGE_TYPE_COINS+13; //金币获得，积分兑换

    public static final int CHANGE_TYPE_COINS_GAIN_REWARDS_SIGNIN = CHANGE_TYPE_COINS + 15;  // 金币获得，签到

    public static final int CHANGE_TYPE_COINS_GAIN_REWARD_CARD_WEEKLY = CHANGE_TYPE_COINS + 16;    // 金币获得，周卡奖励
    public static final int CHANGE_TYPE_COINS_GAIN_REWARD_CARD_MONTHLY = CHANGE_TYPE_COINS + 17;    // 金币获得，月卡奖励


    public static final int CHANGE_TYPE_COINS_EXPEND_PAY_GAME = CHANGE_TYPE_COINS+20; //金币消耗，游戏消耗
    public static final int CHANGE_TYPE_COINS_EXPEND_PAY_DELIVERY = CHANGE_TYPE_COINS+22; //金币消耗，邮费

    public static final int CHANGE_TYPE_COINS_GAIN_REWARDS_SYSTEM = CHANGE_TYPE_COINS + 23;  // 金币获得，官方赠送

    public static final int CHANGE_TYPE_COINS_TBJ_DROP = CHANGE_TYPE_COINS + 24;            // 推笔币机投币
    public static final int CHANGE_TYPE_COINS_TBJ_REWARD = CHANGE_TYPE_COINS + 25;          // 推笔币机奖励

    /// 积分
    public static final int CHANGE_TYPE_POINTS = 200;
    public static final int CHANGE_TYPE_POINTS_GAIN_GAME=CHANGE_TYPE_POINTS+10; //积分获得，游戏奖励
//    public static final int CHANGE_TYPE_POINTS_GAIN_SUCCESS_GAME=CHANGE_TYPE_POINTS+11; //积分获得，游戏成功
//
//    public static final int CHANGE_TYPE_POINTS_GAIN_EXCHANGE_GOODS=CHANGE_TYPE_POINTS+13; //积分获得，礼物兑换

    public static final int CHANGE_TYPE_POINTS_EXPEND_PAY_GAME = CHANGE_TYPE_POINTS+20; //积分消耗，邮费
    public static final int CHANGE_TYPE_POINTS_EXPEND_PAY_DELIVERY = CHANGE_TYPE_POINTS+21; //积分消耗，邮费
    public static final int CHANGE_TYPE_POINTS_EXPEND_EXCHANGE_COINS = CHANGE_TYPE_POINTS+22; //积分消耗，积分兑换

    public static final Map<Integer,String> changeTypeDescMap = new HashMap<Integer,String>()
    {
        {
            put(CHANGE_TYPE_COINS_GAIN_BUY_ALIPAY,"支付宝充值");
            put(CHANGE_TYPE_COINS_GAIN_BUY_WX,"微信充值");

            put(CHANGE_TYPE_COINS_GAIN_REWARDS_REGISTER,"注册");
            put(CHANGE_TYPE_COINS_GAIN_REWARDS_BIND_INVITATION_CODE,"邀请");

            put(CHANGE_TYPE_COINS_GAIN_REWARDS_GAME,"游戏奖励");
            put(CHANGE_TYPE_COINS_GAIN_REWARDS_SHARED,"分享奖励");

            put(CHANGE_TYPE_COINS_GAIN_EXCHANGE_GOODS,"礼物兑换");
            put(CHANGE_TYPE_COINS_GAIN_EXCHANGE_POINTS,"积分兑换");

            put(CHANGE_TYPE_COINS_EXPEND_PAY_GAME,"消耗");
            put(CHANGE_TYPE_COINS_EXPEND_PAY_DELIVERY,"抵邮费");

            put(CHANGE_TYPE_COINS_TBJ_DROP, "推币机投币");
            put(CHANGE_TYPE_COINS_TBJ_REWARD, "推币机出币");

            // points
            put(CHANGE_TYPE_POINTS_GAIN_GAME,"抓娃娃");
//            put(CHANGE_TYPE_POINTS_GAIN_SUCCESS_GAME,"抓取成功");
//            put(CHANGE_TYPE_POINTS_GAIN_EXCHANGE_GOODS,"兑换积分");

            put(CHANGE_TYPE_POINTS_EXPEND_PAY_GAME,"抓娃娃");
            put(CHANGE_TYPE_POINTS_EXPEND_PAY_DELIVERY,"抵邮费");
            put(CHANGE_TYPE_POINTS_EXPEND_EXCHANGE_COINS,"兑换");

            put(CHANGE_TYPE_COINS_GAIN_REWARDS_SIGNIN, "签到");
            put(CHANGE_TYPE_COINS_GAIN_REWARD_CARD_WEEKLY, "周卡");
            put(CHANGE_TYPE_COINS_GAIN_REWARD_CARD_MONTHLY, "月卡");

            put(CHANGE_TYPE_COINS_GAIN_REWARDS_SYSTEM, "官方赠送");
        }
    };

    @Autowired
    private AccountLogDAO accountLogDAO;

    @Override
    protected JpaRepository<AccountLog, Integer> getDao() {
        return accountLogDAO;
    }

    public List<AccountLog> findAccountCoinLogs(int userId, List<Integer> coinsChangedTypes, int page, int size) {
        return accountLogDAO.findAccountCoinLogs(userId, coinsChangedTypes.toArray(new Integer[coinsChangedTypes.size()]), new PageRequest(page, size, Sort.Direction.DESC, "timestamp"));
    }

    public List<AccountLog> findAccountPointLogs(int userId, List<Integer> pointsChangedTypes, int page, int size) {
        return accountLogDAO.findAccountPointLogs(userId, pointsChangedTypes.toArray(new Integer[pointsChangedTypes.size()]), new PageRequest(page, size, Sort.Direction.DESC, "timestamp"));
    }

    public void save(Account accountOld, Account account,
                     int coinsChangedType,
                     int pointsChangedType,
                     List<AccountLogData> dataList) {
        save(accountOld,account,coinsChangedType,pointsChangedType,0,dataList);
    }

    public void save(Account accountOld, Account account,
                     int coinsChangedType,
                     int pointsChangedType,
                     int fee,
                     List<AccountLogData> dataList) {
        AccountLog accountLog=create(accountOld,account,coinsChangedType,pointsChangedType,fee,
                dataList);
        super.save(accountLog);
    }

    public void saveAccountLog(Account accountOld, Account account, int coinsChangedType, int pointsChangedType, int fee
            , String extra_data_str)
    {
        AccountLog accountLog = create(accountOld, account, coinsChangedType, pointsChangedType, fee, extra_data_str);
        super.save(accountLog);
    }

    public void saveCardReward(Account accountOld
            , Account account
            , int coinsChangedType
            , int pointsChangedType
            , int fee
            , String extra_data)
    {
        AccountLog accountLog = create(accountOld, account, coinsChangedType, pointsChangedType, fee, extra_data);
        super.save(accountLog);
    }

    public void saveTbjCoinsChange(int userId, int dropNum, int type) {
        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(userId);
        accountLog.setCoins(dropNum);
        accountLog.setAvailableCoins(dropNum);
        accountLog.setCoinsChangedType(type);
        accountLog.setPoints(0);
        accountLog.setAvailablePoints(0);
        accountLog.setPointsChangedType(0);
        accountLog.setFee(0);
        accountLog.setData("");
        super.save(accountLog);
    }

    private static AccountLog create(Account accountOld, Account account,
                                    int coinsChangedType,
                                    int pointsChangedType,
                                    int fee,
                                    List<AccountLogData> dataList) {

        String dataJsonString = "";
        if (dataList!=null) {
            dataJsonString = JsonUtil.convertObject2Json(dataList);
        }
        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(account.getUserId());
        accountLog.setCoins(account.getCoins()-accountOld.getCoins());
        accountLog.setAvailableCoins(account.getAvailableCoins()-accountOld.getAvailableCoins());
        accountLog.setCoinsChangedType(coinsChangedType);
        accountLog.setPoints(account.getPoints()-accountOld.getPoints());
        accountLog.setAvailablePoints(account.getAvailablePoints()-accountOld.getAvailablePoints());
        accountLog.setPointsChangedType(pointsChangedType);
        accountLog.setFee(fee);
        accountLog.setData(dataJsonString);
        return accountLog;
    }

    private static AccountLog create(Account accountOld
            , Account account
            , int coinsChangedType
            , int pointsChangedType
            , int fee
            , String extra_data)
    {
        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(account.getUserId());
        accountLog.setCoins(account.getCoins()-accountOld.getCoins());
        accountLog.setAvailableCoins(account.getAvailableCoins()-accountOld.getAvailableCoins());
        accountLog.setCoinsChangedType(coinsChangedType);
        accountLog.setPoints(account.getPoints()-accountOld.getPoints());
        accountLog.setAvailablePoints(account.getAvailablePoints()-accountOld.getAvailablePoints());
        accountLog.setPointsChangedType(pointsChangedType);
        accountLog.setFee(fee);
        accountLog.setData("");
        accountLog.setExtraData(extra_data);
        return accountLog;
    }

}


//
