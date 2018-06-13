package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.Application;
import com.kariqu.zwsrv.app.controller.BillingController;
import com.kariqu.zwsrv.app.error.ErrorDefs;
import com.kariqu.zwsrv.app.model.AccountLogInfo;
import com.kariqu.zwsrv.app.transaction.IsTryAgain;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.*;
import com.kariqu.zwsrv.thelib.persistance.service.*;
import com.kariqu.zwsrv.thelib.util.JsonUtil;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by simon on 27/11/17.
 */
@Service
public class AccountBusinessService {

    // 辅助类，打包返回多个结果
    private class PackageTool
    {
        public PackageTool(Account account)
        {
            this.accout = account;
            this.coins = 0;
            this.totalFee = 0;
            this.extraData = "";
        }

        public Account accout;
        public int coins;
        public int totalFee;
        public String extraData;
    }

    private static final int ORDER_VALID_MILLISECONDS = 15 * 60 * 1000;


    protected final transient Logger logger = LoggerFactory.getLogger("AccountBusiness");

    @Autowired
    private ChargeServiceCache chargeServiceCache;

    @Autowired
    private CardServiceCache cardServiceCache;

    @Autowired
    CoinsOrderService coinsOrderService;

    @Autowired
    CoinsService coinsService;

    @Autowired
    PayOrderService payOrderService;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountLogService accountLogService;

    @Autowired
    PayService payService;

    @Autowired
    PlatDictServiceCache platDictServiceCache;

    @Autowired
    NoticeBusinessService noticeBusinessService;

    @Autowired
    UserServiceCache userServiceCache;

    @Autowired
    UserCardServiceCache userCardServiceCache;

    @Autowired
    private UserItemServiceCache userItemServiceCache;

    public AccountService getAccountService() { return accountService; }


    public int ratioOfExchangePointToCoins() {
        return platDictServiceCache.getExchangePointToCoinsRatio();
    }

    public int getInvitationMaxCount() {
        return platDictServiceCache.getInvitationMaxCount();
    }
    public int getNewUserRewardCoinsNum() { return platDictServiceCache.getNewUserRewardCoinsNum(); }

    public List<AccountLogInfo> findAccountCoinLogs(int userId, List<Integer> coinsChangedTypes, int page, int size) {
        List<AccountLog> listTemp = accountLogService.findAccountCoinLogs(userId, coinsChangedTypes, page, size);
        if (listTemp == null) {
            listTemp = new ArrayList<>();
        }
        List<AccountLogInfo> list = new ArrayList<>();
        for (AccountLog accountLog : listTemp) {
            list.add(new AccountLogInfo(accountLog, true));
        }
        return list;
    }

    public List<AccountLogInfo> findAccountPointLogs(int userId, List<Integer> pointsChangedTypes, int page, int size) {
        List<AccountLog> listTemp = accountLogService.findAccountPointLogs(userId, pointsChangedTypes, page, size);
        if (listTemp == null) {
            listTemp = new ArrayList<>();
        }
        List<AccountLogInfo> list = new ArrayList<>();
        for (AccountLog accountLog : listTemp) {
            list.add(new AccountLogInfo(accountLog, false));
        }
        return list;
    }

    public void rewardCoinsForBindInvitationCode(String inviteeNickName, int userId, int invitorId) {
        int rewardCoins = platDictServiceCache.getBindInvitationCodeRewardCoinsNum();
        rewardCoins(userId, rewardCoins, AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_BIND_INVITATION_CODE);
        rewardCoins(invitorId, rewardCoins, AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_BIND_INVITATION_CODE);
        noticeBusinessService.sendBindInvitationCodeNotification(inviteeNickName, invitorId, rewardCoins);
    }

    public void rewardCoinsForRegister(int userId) {
        rewardCoins(userId, platDictServiceCache.getNewUserRewardCoinsNum(), AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_REGISTER);
    }

    public void rewardCoinsForSignin(int userId, int count) {
        rewardCoins(userId, count, AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_SIGNIN);
    }

    public boolean rewardForCard(int userId, int count, int cardId, int cardType) {
        return rewardCoinsByCard(userId, count, cardId, cardType);
    }

    private boolean rewardCoinsByCard(int userId, int count, int cardId, int cardType) {
        Account account = accountService.findOne(userId);
        if (account == null) {
            return false;
        }
        Account accountOld = new Account(account);
        account.setCoins(account.getCoins() + count);
        account.setAvailableCoins(account.getAvailableCoins() + count);

        account = accountService.save(account);

        AccountLogExtraData.JsonCardReward extra_obj = new AccountLogExtraData().createJsonCardReward(cardId, cardType);
        String extra_data = JsonUtil.convertObject2Json(extra_obj);

        int changeType = 0;
        if (cardType == CardService.CARD_TYPE_WEEKLY) {
            changeType = AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARD_CARD_WEEKLY;
        } else if (cardType == CardService.CARD_TYPE_MONTHLY) {
            changeType = AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARD_CARD_MONTHLY;
        }
        accountLogService.saveCardReward(accountOld, account, changeType, 0, 0, extra_data);
        return true;
    }

    private void rewardCoins(int userId, int coins, int changeType) {
        Account account = accountService.findOne(userId);
        if (account == null) {
            account = new Account();
            account.setUserId(userId);
        }
        Account accountOld = new Account(account);
        account.setCoins(account.getCoins() + coins);
        account.setAvailableCoins(account.getAvailableCoins() + coins);

        account = accountService.save(account);
        accountLogService.save(accountOld,
                account,
                changeType,
                0,
                null);
    }


    public Account findAccount(int userId) {
        Account account = accountService.findOne(userId);
        if (account == null) {
            account = new Account();
            account.setUserId(userId);
        }
        return account;
    }


    public ResponseData pointsExchangeToCoinsNum(int userId) {
        Account account = accountService.findOne(userId);
        if (account == null) {
            account = new Account();
            account.setUserId(userId);
        }
        int toCoinsNum = account.getPoints() / ratioOfExchangePointToCoins();
        return new ResponseData().put("coins_num", toCoinsNum);
    }

    public ResponseData pointsExchangeToCoins(int userId) {
        Account account = accountService.findOne(userId);
        if (account == null) {
            account = new Account();
            account.setUserId(userId);
        }
        Account accountOld = new Account(account);

        int POINTS_TO_COINS_RADIO = ratioOfExchangePointToCoins();

        int toCoinsNum = account.getPoints() / POINTS_TO_COINS_RADIO;

        account.setCoins(account.getCoins() + toCoinsNum);
        account.setAvailableCoins(account.getAvailableCoins() + toCoinsNum);

        account.setPoints(account.getPoints() % POINTS_TO_COINS_RADIO);
        account.setAvailablePoints(account.getAvailablePoints() % POINTS_TO_COINS_RADIO);

        account = accountService.save(account);
        accountLogService.save(accountOld, account, AccountLogService.CHANGE_TYPE_COINS_GAIN_EXCHANGE_POINTS, AccountLogService.CHANGE_TYPE_POINTS_EXPEND_EXCHANGE_COINS, null);

        return new ResponseData().put("account", account);
    }

    public ErrorCode.ErrorEntry goodsExchangeToCoins(int userId, List<Game> gameList, List<Account> accountListReturn) {
        Account account = accountService.findOne(userId);
        if (account == null) {
            account = new Account();
            account.setUserId(userId);
        }

        Account accountOld = new Account(account);
        int totalExchangeCoins = 0;
        for (Game game : gameList) {
            totalExchangeCoins += game.getExchangeCoins();
        }
        account.setCoins(account.getCoins() + totalExchangeCoins);
        account.setAvailableCoins(account.getAvailableCoins() + totalExchangeCoins);

        List<AccountLogData> dataList = new ArrayList<>();
        for (Game game : gameList) {
            AccountLogData data = new AccountLogData(game.getGameId(), game.getName(), game.getImageUrl());
            dataList.add(data);
        }

        account = accountService.save(account);
        accountLogService.save(accountOld, account, AccountLogService.CHANGE_TYPE_COINS_GAIN_EXCHANGE_GOODS, 0, dataList);
        if (accountListReturn != null) {
            accountListReturn.add(account);
        }
        return ErrorDefs.ERROR_SUCCESS;
    }

    // 道具兑换成金币
    public UserItem userItemExchangeToCoins(int userId, Account account, UserItem userItem, Item item) {
        Account accountOld = new Account(account);
        int totalExchangeCoins = item.getExchangeCoinsNum();
        account.setCoins(account.getCoins() + totalExchangeCoins);
        account.setAvailableCoins(account.getAvailableCoins() + totalExchangeCoins);

        AccountLogData accountLogData = new AccountLogData(userItem.getOriginType(), userItem.getItemName()
                , userItem.getItemIcon());
        List<AccountLogData> list = new ArrayList<>();
        list.add(accountLogData);
        account = accountService.save(account);
        accountLogService.save(accountOld, account, AccountLogService.CHANGE_TYPE_COINS_GAIN_EXCHANGE_GOODS
                , 0, list);

        // 保存user_item状态
        userItem.setExchangeCoinsStatus(1);
        userItem.setExchangeCoinsNum(totalExchangeCoins);
        userItem = userItemServiceCache.save(userItem);
        return userItem;
    }

    public ErrorCode.ErrorEntry payDeliveryWithPoints(int userId, int points, List<DeliveryGoods> deliveryGoodsList) {
        List<AccountLogData> dataList = new ArrayList<>();
        if (deliveryGoodsList != null) {
            for (DeliveryGoods deliveryGoods : deliveryGoodsList) {
                AccountLogData data = new AccountLogData(deliveryGoods.getIdvalue(), deliveryGoods.getName(), deliveryGoods.getImageUrl());
                dataList.add(data);
            }
        }
        return payPoints(userId, points, AccountLogService.CHANGE_TYPE_POINTS_EXPEND_PAY_DELIVERY, dataList);
    }

    public ErrorCode.ErrorEntry payDeliveryWithCoins(int userId, int coins, List<DeliveryGoods> deliveryGoodsList) {
        List<AccountLogData> dataList = new ArrayList<>();
        if (deliveryGoodsList != null) {
            for (DeliveryGoods deliveryGoods : deliveryGoodsList) {
                AccountLogData data = new AccountLogData(deliveryGoods.getIdvalue(), deliveryGoods.getName(), deliveryGoods.getImageUrl());
                dataList.add(data);
            }
        }
        return payCoins(userId, coins, AccountLogService.CHANGE_TYPE_POINTS_EXPEND_PAY_DELIVERY, 0, 0, dataList);
    }

    public ErrorCode.ErrorEntry payGameCoins(int userId, Game game) {
        List<AccountLogData> dataList = new ArrayList<>();
        dataList.add(new AccountLogData(game.getGameId(), game.getName(), game.getImageUrl()));
        return payCoins(userId
                , game.getRoomCost(), AccountLogService.CHANGE_TYPE_COINS_EXPEND_PAY_GAME
                , game.getPoints(), game.getPoints() > 0 ? AccountLogService.CHANGE_TYPE_POINTS_GAIN_GAME : 0
                , dataList);
    }

    public ErrorCode.ErrorEntry payGamePoints(int userId, Game game) {
        List<AccountLogData> dataList = new ArrayList<>();
        dataList.add(new AccountLogData(game.getGameId(), game.getName(), game.getImageUrl()));

        int POINTS_TO_COINS_RADIO = platDictServiceCache.getExchangePointToCoinsRatio();

        int pointsPaid = game.getRoomCost() * POINTS_TO_COINS_RADIO;
        Account account = accountService.findOne(userId);
        if (account == null) {
            account = new Account();
            account.setUserId(userId);
        }
        if (account.getAvailablePoints() + game.getPoints() >= pointsPaid) {

            Account accountOld = new Account(account);

            int points = account.getPoints();
            int availablePoints = account.getAvailablePoints();

            points += game.getPoints();
            availablePoints += game.getPoints();

            points -= pointsPaid;
            availablePoints -= pointsPaid;
            account.setPoints(points < 0 ? 0 : points);
            account.setAvailablePoints(availablePoints < 0 ? 0 : availablePoints);


            account = accountService.save(account);
            accountLogService.save(accountOld,
                    account,
                    0,
                    account.getAvailablePoints() < accountOld.getAvailablePoints() ?
                            AccountLogService.CHANGE_TYPE_POINTS_EXPEND_PAY_GAME : AccountLogService.CHANGE_TYPE_POINTS_GAIN_GAME,
                    dataList);
            return ErrorDefs.ERROR_SUCCESS;
        } else {
            return ErrorDefs.ERROR_CODE_ACCOUNT_NO_ENOUGH_POINTS;
        }
    }

    public ErrorCode.ErrorEntry payPoints(int userId, int pointsPaid, int pointsChangedType, List<AccountLogData> dataList) {
        Account account = accountService.findOne(userId);
        if (account == null) {
            account = new Account();
            account.setUserId(userId);
        }
        if (account.getAvailablePoints() >= pointsPaid) {

            Account accountOld = new Account(account);

            int points = account.getPoints();
            int availablePoints = account.getAvailablePoints();

            points -= pointsPaid;
            availablePoints -= pointsPaid;
            account.setPoints(points < 0 ? 0 : points);
            account.setAvailablePoints(availablePoints < 0 ? 0 : availablePoints);

            account = accountService.save(account);
            accountLogService.save(accountOld,
                    account,
                    0,
                    pointsChangedType,
                    dataList);
            return ErrorDefs.ERROR_SUCCESS;
        } else {
            return ErrorDefs.ERROR_CODE_ACCOUNT_NO_ENOUGH_POINTS;
        }
    }

    public ErrorCode.ErrorEntry payCoins(int userId,
                                         int coinsPaid, int coinsChangedType,
                                         int rewardPoints, int pointsChangedType,
                                         List<AccountLogData> dataList) {
        Account account = accountService.findOne(userId);
        if (account == null) {
            account = new Account();
            account.setUserId(userId);
        }
        if (account.getAvailableCoins() >= coinsPaid) {

            Account accountOld = new Account(account);

            int coins = account.getCoins();
            int availableCoins = account.getAvailableCoins();

            coins -= coinsPaid;
            availableCoins -= coinsPaid;
            account.setCoins(coins < 0 ? 0 : coins);
            account.setAvailableCoins(availableCoins < 0 ? 0 : availableCoins);

            account.setPoints(account.getPoints() + rewardPoints);
            account.setAvailablePoints(account.getAvailablePoints() + rewardPoints);

            account = accountService.save(account);
            accountLogService.save(accountOld,
                    account,
                    coinsChangedType,
                    pointsChangedType,
                    dataList);
            return ErrorDefs.ERROR_SUCCESS;
        } else {
            return ErrorDefs.ERROR_CODE_ACCOUNT_NO_ENOUGH_COINS;
        }
    }

    public final String BIZ_CODE = "coin";

    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor = {Exception.class})
    public ResponseData buyCoins(int currentUserId, String payWay, int payOn, int coinsId, int coinsNum) {

        if (PayService.isValidPayWay(payWay) && PayService.isValidPayOn(payOn)) {
            Coins coins = coinsService.findOne(coinsId);
            if (coins != null
                    && coins.getUnitAmount() > 0 && coins.getUnitCoins() > 0) {
                String orderSN = generateOrderSN();
                String orderSubject = "娃娃币";
                String orderDesc = "娃娃币";

                int remainTimeInMillis = 15 * 60 * 1000;

                int totalAmount = 0;
                if (coins.getIsPromotion() > 0) {
                    totalAmount = coins.getUnitAmount();
                    coinsNum = coins.getUnitCoins();
                } else {
                    if (coinsNum > 0) {
                        float totalYuan = ((float) (coins.getUnitAmount() * coinsNum)) / (coins.getUnitCoins() * 100.f);
                        int totalAmountFen = (int) (totalYuan * 1000.f);
                        if (totalAmountFen % 10 > 0) {
                            totalAmountFen = totalAmountFen / 10;
                            totalAmountFen += 1; //0.22223333- >0.23
                        } else {
                            totalAmountFen = totalAmountFen / 10;
                        }
                        totalAmount = totalAmountFen;
                    }
                }

                CoinsOrder order = new CoinsOrder();
                order.setUserId(currentUserId);
                order.setOrderSN(orderSN);
                order.setOrderSubject(orderSubject);
                order.setOrderDesc(orderDesc);
                order.setUnitAmount(coins.getUnitAmount());
                order.setUnitCoins(coins.getUnitCoins());
                order.setIsPromotion(coins.getIsPromotion());
                order.setTotalAmount(totalAmount);
                order.setCoins(coinsNum);
                order.setIsPaid(0);
                order = coinsOrderService.save(order);

                String paySN = generateOrderSN();
                PayOrder payOrder = new PayOrder();
                payOrder.setPaySN(paySN);
                payOrder.setRequestBiz(BIZ_CODE);
                payOrder.setOrderId(order.getOrderId());
                payOrder.setOrderSN(orderSN);
                payOrder.setOrderSubject(orderSubject);
                payOrder.setOrderDesc(orderDesc);
                payOrder.setPayWay(payWay);
                payOrder.setPayOn(payOn);
                payOrder.setTotalAmount(totalAmount);
                payOrder.setIsPaid(0);
                payOrder.setPayReqParams("");
                payOrder.setNotifyTime(0);
                payOrder.setNotifyData("");
                payOrder.setTradeNo("");
                payOrder.setTradeStatus("");
                payOrder.setBuyerEmail("");
                payOrder.setRemainInMillis(remainTimeInMillis);

                Object payParams = null;
                if (payWay.equalsIgnoreCase(PayService.PAY_WAY_WXPAY)) {
                    payParams = payService.buildWeixinPayParams(payOn, paySN, orderSubject, orderDesc, totalAmount, remainTimeInMillis);
                }

                if (payParams != null) {
                    String payParamsString;
                    if (payParams instanceof String) {
                        payParamsString = (String) payParams;
                    } else {
                        payParamsString = JsonUtil.convertObject2Json(payParams);
                    }
                    payOrder.setPayReqParams(payParamsString);

                    payOrderService.save(payOrder);

                    ResponseData responseData = new ResponseData();
                    payOrder.setOrderSN(orderSN);
                    responseData.put("paySN", payOrder.getPaySN());
                    responseData.put("orderSN", payOrder.getOrderSN());
                    responseData.put("requestBiz", payOrder.getRequestBiz());
                    responseData.put("payWay", payOrder.getPayWay());
                    responseData.put("payOn", payOrder.getPayOn());
                    responseData.put("payParams", payParamsString);
                    return responseData;
                } else {
                    return new ErrorResponse(ErrorDefs.ERROR_CODE_THIRDPARTY_TIMEOUT);
                }
            }
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }

    static String generateOrderSN() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor = {Exception.class})
    public boolean handleWeiXinPayNotify(String paySN, String tradeNo, String totalFeeString, String responseData) {
        boolean result = false;
        if (paySN == null)
            return false;
        PayOrder payOrder = payOrderService.findByPaySN(paySN);
        if (payOrder == null) {
            Application.getLog().warn("handleWeiXinPayNotify can't find paySN. paySn {}", paySN);
            return false;
        }

        // 新的billing
        if (!payOrder.getRequestBiz().equalsIgnoreCase(BIZ_CODE)) {
            return handleWeiXinPayNotify_Billing(paySN, tradeNo, totalFeeString, responseData, payOrder);
        }
        if (payOrder.getIsPaid() == 0
                && payOrder.getRequestBiz().equalsIgnoreCase(BIZ_CODE)
                && totalFeeString != null
                && NumberValidationUtil.isPositiveInteger(totalFeeString)) {

            //logger.info("handleWeiXinPayNotify:{} ",totalFeeString!=null?totalFeeString:"");

            CoinsOrder coinsOrder = coinsOrderService.findByOrderSN(payOrder.getOrderSN());
            if (coinsOrder != null) {
                coinsOrder.setIsPaid(1);

                int totalFee = Integer.valueOf(totalFeeString);

                payOrder.setTradeNo(tradeNo != null ? tradeNo : "");
                payOrder.setTradeStatus("");
                //coinsOrder.setTotalFee(totalFee);
                payOrder.setBuyerEmail("");
                payOrder.setIsPaid(1);
                payOrder.setNotifyData(responseData != null ? responseData : "");
                payOrder.setNotifyTime(System.currentTimeMillis());


                Account account = accountService.findOne(coinsOrder.getUserId());
                if (account == null) {
                    account = new Account();
                    account.setUserId(coinsOrder.getUserId());
                }
                Account accountOld = new Account(account);

                account.setCoins(account.getCoins() + coinsOrder.getCoins());
                account.setAvailableCoins(account.getAvailableCoins() + coinsOrder.getCoins());

                coinsOrderService.save(coinsOrder);
                payOrderService.save(payOrder);
                account = accountService.save(account);

                List<AccountLogData> dataList = new ArrayList<>();
                dataList.add(new AccountLogData(coinsOrder.getOrderId(), "", ""));

                accountLogService.save(accountOld,
                        account,
                        AccountLogService.CHANGE_TYPE_COINS_GAIN_BUY_WX,
                        0,
                        totalFee,
                        dataList);

                noticeBusinessService.sendBuyCoinsNotification(coinsOrder.getUserId(), totalFee);
                result = true;
            }
        }
        return result;
    }


    // 新的计费
    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor = {Exception.class})
    public ResponseData clientBilling(int userId, String payWay, int payOn, int billingType, int billingId, String channel) {
        if (!PayService.isValidPayWay(payWay) || !PayService.isValidPayOn(payOn)) {
            return new ErrorResponse(ErrorCode.ERROR_FAILED);
        }

        switch (billingType) {
            case BillingController.BILLING_TYPE_CHARGE:             // 充值金币
                return billingCharge(userId, payWay, payOn, billingType, billingId, channel);
            case BillingController.BILLING_TYPE_CARD:               // 充值周卡月卡
                return billingCard(userId, payWay, payOn, billingType, billingId, channel);
            case BillingController.BILLING_TYPE_NEW_USER_GIFT:      // 新手礼包
                return billingNewUserGift(userId, payWay, payOn, billingType, billingId, channel);
            case BillingController.BILLING_TYPE_CHARGE_FIRST_TIME:  // 充值金币首冲
                return billingChargeFirstTime(userId, payWay, payOn, billingType, billingId, channel);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }

    // 充值金币
    private ResponseData billingCharge(int userId, String payWay, int payOn, int billingType, int chargeId, String channel) {
        Charge charge = chargeServiceCache.findOne(chargeId);
        if (charge == null || charge.getRmb() <= 0 || charge.totalCoins() <= 0) {
            return new ErrorResponse(ErrorDefs.ERROR_CODE_UNKNOWN_CHARGE_ID);
        }

        CoinsOrder coinOrder = initCoinOrder(userId, charge.getChargeSubject(), charge.getChargeDesc());
        coinOrder.setUnitAmount(charge.getRmb());
        coinOrder.setUnitCoins(charge.totalCoins());
        coinOrder.setTotalAmount(charge.getRmb());
        coinOrder.setCoins(charge.totalCoins());
        coinOrder.setBillingType(billingType);
        coinOrder.setBillingId(chargeId);
        coinOrder = coinsOrderService.save(coinOrder);

        PayOrder payOrder = initPayOrder(coinOrder, payWay, payOn);
        payOrder.setRequestBiz(BillingController.BILLING_BIZ_CHARGE);
        payOrder.setChannel(channel);
        PayOrder ret = processPayOrder(coinOrder, payOrder);
        if (ret == null) {
            return new ErrorResponse(ErrorDefs.ERROR_CODE_THIRDPARTY_TIMEOUT);
        } else {
            payOrder = ret;
        }
        ResponseData responseData = createBillingResponse(payOrder);
        return responseData;
    }

    // 充值周卡，月卡
    private ResponseData billingCard(int userId, String payWay, int payOn, int billingType, int cardId, String channel) {
        Card card = cardServiceCache.findOne(cardId);
        if (card == null || card.getCanBuy() == 0 || card.getRmb() <= 0) {
            return new ErrorResponse(ErrorDefs.ERROR_CODE_UNKNOWN_CHARGE_ID);
        }

        CoinsOrder coinOrder = initCoinOrder(userId, card.getCardSubject(), card.getCardDesc());
        coinOrder.setUnitAmount(card.getRmb());
        coinOrder.setUnitCoins(card.getImmediateCoins());
        coinOrder.setTotalAmount(card.getRmb());
        coinOrder.setCoins(card.getImmediateCoins());
        coinOrder.setBillingType(billingType);
        coinOrder.setBillingId(cardId);
        coinOrder = coinsOrderService.save(coinOrder);

        PayOrder payOrder = initPayOrder(coinOrder, payWay, payOn);
        payOrder.setRequestBiz(BillingController.BILLING_BIZ_CARD);
        payOrder.setChannel(channel);
        PayOrder ret = processPayOrder(coinOrder, payOrder);
        if (ret == null) {
            return new ErrorResponse(ErrorDefs.ERROR_CODE_THIRDPARTY_TIMEOUT);
        } else {
            payOrder = ret;
        }
        ResponseData responseData = createBillingResponse(payOrder);
        return responseData;
    }

    // 新手礼包
    private ResponseData billingNewUserGift(int userId, String payWay, int payOn, int billingType, int chargeId, String channel) {
        Charge charge = chargeServiceCache.findByType(ChargeService.CHARGE_TYPE_NEW_USER_GIFT);
        if (charge == null || charge.getRmb() <= 0) {
            return new ErrorResponse(ErrorDefs.ERROR_CODE_UNKNOWN_CHARGE_ID);
        }

        User user = userServiceCache.findOne(userId);
        if (user == null) {
            Application.getLog().warn("billingNewUserGift can not find user: {}", userId);
            return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
        }

        // 超出时间限制
        long delta = System.currentTimeMillis() - user.getCreateTime();
        if (delta > charge.getDuration()) {
            Application.getLog().warn("billingNewUserGift time expired. user: {} delta: {}", userId, delta);
            return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
        }

        // 已经获得过新手礼包
        Account accout = accountService.findOne(userId);
        if (accout == null) {
            Application.getLog().warn("billingNewUserGift account is null. user: {}", userId);
            return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
        }
        if (accout.getNewUserGift() != 0) {
            Application.getLog().warn("billingNewUserGift can't repeated. user: {}", userId);
            return new ErrorResponse(ErrorDefs.ERROR_CODE_NEW_USER_GIFT_REPEATED);
        }

        CoinsOrder coinOrder = initCoinOrder(userId, charge.getChargeSubject(), charge.getChargeDesc());
        coinOrder.setUnitAmount(charge.getRmb());
        coinOrder.setUnitCoins(charge.getBasicCoins());
        coinOrder.setTotalAmount(charge.getRmb());
        coinOrder.setCoins(charge.getBasicCoins());
        coinOrder.setBillingType(billingType);
        coinOrder.setBillingId(chargeId);
        coinOrder = coinsOrderService.save(coinOrder);

        PayOrder payOrder = initPayOrder(coinOrder, payWay, payOn);
        payOrder.setRequestBiz(BillingController.BILLING_BIZ_NEW_USER_GIFT);
        payOrder.setChannel(channel);
        PayOrder ret = processPayOrder(coinOrder, payOrder);
        if (ret == null) {
            return new ErrorResponse(ErrorDefs.ERROR_CODE_THIRDPARTY_TIMEOUT);
        } else {
            payOrder = ret;
        }
        ResponseData responseData = createBillingResponse(payOrder);
        return responseData;
    }

    // 充值金币首冲
    private ResponseData billingChargeFirstTime(int userId, String payWay, int payOn, int billingType, int chargeId, String channel) {
        Charge charge = chargeServiceCache.findByType(ChargeService.CHARGE_TYPE_FIRST_TIME);
        if (charge == null || charge.getRmb() <= 0) {
            return new ErrorResponse(ErrorDefs.ERROR_CODE_UNKNOWN_CHARGE_ID);
        }

        User user = userServiceCache.findOne(userId);
        if (user == null) {
            Application.getLog().warn("billingChargeFirstTime can not find user: {}", userId);
            return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
        }

        // 已经冲过首冲
        Account accout = accountService.findOne(userId);
        if (accout == null) {
            Application.getLog().warn("billingChargeFirstTime account is null. user: {}", userId);
            return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
        }
        if (accout.getChargeFirstTime() != 0) {
            Application.getLog().warn("billingChargeFirstTime can't repeated. user: {}", userId);
            return new ErrorResponse(ErrorDefs.ERROR_CODE_CHARGE_FIRST_REPEATED);
        }

        CoinsOrder coinOrder = initCoinOrder(userId, charge.getChargeSubject(), charge.getChargeDesc());
        coinOrder.setUnitAmount(charge.getRmb());
        coinOrder.setUnitCoins(charge.getBasicCoins());
        coinOrder.setTotalAmount(charge.getRmb());
        coinOrder.setCoins(charge.getBasicCoins());
        coinOrder.setBillingType(billingType);
        coinOrder.setBillingId(chargeId);
        coinOrder = coinsOrderService.save(coinOrder);

        PayOrder payOrder = initPayOrder(coinOrder, payWay, payOn);
        payOrder.setRequestBiz(BillingController.BILLING_BIZ_CHARGE_FIRST_TIME);
        payOrder.setChannel(channel);
        PayOrder ret = processPayOrder(coinOrder, payOrder);
        if (ret == null) {
            return new ErrorResponse(ErrorDefs.ERROR_CODE_THIRDPARTY_TIMEOUT);
        } else {
            payOrder = ret;
        }
        ResponseData responseData = createBillingResponse(payOrder);
        return responseData;
    }

    private CoinsOrder createCoinsOrderByCharge(int userId, String orderSN, String orderSub, String orderDesc, Charge charge) {
        CoinsOrder order = new CoinsOrder();
        order.setUserId(userId);
        order.setOrderSN(orderSN);
        order.setOrderSubject(orderSub);
        order.setOrderDesc(orderDesc);
        order.setUnitAmount(charge.getRmb());
        order.setUnitCoins(charge.totalCoins());
        order.setIsPromotion(0);
        order.setTotalAmount(charge.getRmb());
        order.setCoins(charge.totalCoins());
        order.setIsPaid(0);
        return order;
    }

    private CoinsOrder initCoinOrder(int userId, String orderSub, String orderDesc) {
        CoinsOrder order = new CoinsOrder();
        order.setUserId(userId);
        order.setOrderSN(generateOrderSN());
        order.setOrderSubject(orderSub);
        order.setOrderDesc(orderDesc);
        order.setIsPromotion(0);
        order.setIsPaid(0);
        return order;
    }

    private PayOrder initPayOrder(CoinsOrder coinOrder, String payWay, int payOn) {
        PayOrder payOrder = new PayOrder();
        payOrder.setPaySN(generateOrderSN());
        payOrder.setOrderId(coinOrder.getOrderId());
        payOrder.setOrderSN(coinOrder.getOrderSN());
        payOrder.setOrderSubject(coinOrder.getOrderSubject());
        payOrder.setOrderDesc(coinOrder.getOrderDesc());
        payOrder.setPayWay(payWay);
        payOrder.setPayOn(payOn);
        payOrder.setTotalAmount(coinOrder.getTotalAmount());
        payOrder.setIsPaid(0);
        payOrder.setPayReqParams("");
        payOrder.setNotifyTime(0);
        payOrder.setNotifyData("");
        payOrder.setTradeNo("");
        payOrder.setTradeStatus("");
        payOrder.setBuyerEmail("");
        payOrder.setRemainInMillis(ORDER_VALID_MILLISECONDS);
        return payOrder;
    }

    private PayOrder processPayOrder(CoinsOrder coinsOrder, PayOrder payOrder) {
        Object payParams = null;
        if (payOrder.getPayWay().equalsIgnoreCase(PayService.PAY_WAY_WXPAY)) {
            payParams = payService.buildWeixinPayParams(payOrder.getPayOn()
                    , payOrder.getPaySN(), coinsOrder.getOrderSubject(), coinsOrder.getOrderDesc()
                    , payOrder.getTotalAmount(), payOrder.getRemainInMillis());
        }
        if (payParams == null) {
            return null;
        }

        String payParamsString;
        if (payParams instanceof String) {
            payParamsString = (String) payParams;
        } else {
            payParamsString = JsonUtil.convertObject2Json(payParams);
        }
        payOrder.setPayReqParams(payParamsString);
        payOrderService.save(payOrder);
        return payOrder;
    }

    private ResponseData createBillingResponse(PayOrder payOrder)
    {
        ResponseData responseData = new ResponseData();
        responseData.put("paySN", payOrder.getPaySN());
        responseData.put("orderSN", payOrder.getOrderSN());
        responseData.put("requestBiz", payOrder.getRequestBiz());
        responseData.put("payWay", payOrder.getPayWay());
        responseData.put("payOn", payOrder.getPayOn());
        responseData.put("payParams", payOrder.getPayReqParams());
        return responseData;
    }

    // 新的计费系统
    public boolean handleWeiXinPayNotify_Billing(String paySN, String tradeNo, String totalFeeString
            , String responseData, PayOrder payOrder)
    {
        if (payOrder.getIsPaid() != 0) {
            Application.getLog().warn("handleWeiXinPayNotify_Billing pay order is paid. paySN: {}", paySN);
            return false;
        }
        if (totalFeeString == null) {
            Application.getLog().warn("handleWeiXinPayNotify_Billing totalFeeString is null. paySN: {}", paySN);
            return false;
        }
        if (!NumberValidationUtil.isPositiveInteger(totalFeeString)) {
            Application.getLog().warn("handleWeiXinPayNotify_Billing totalFeeString format error. paySN: {} totalFeeString: {}"
                    , paySN, totalFeeString);
            return false;
        }

        CoinsOrder coinsOrder = coinsOrderService.findByOrderSN(payOrder.getOrderSN());
        if (coinsOrder == null) {
            Application.getLog().warn("handleWeiXinPayNotify_Billing can not find coinsOrder. paySN: {} orderSN: {}"
                    , paySN, payOrder.getOrderSN());
            return false;
        }
        Account account = accountService.findOne(coinsOrder.getUserId());
        if (account == null) {
            Application.getLog().warn("handleWeiXinPayNotify_Billing can not find Account. paySN: {} orderSN: {} userId: {}"
                    , paySN, payOrder.getOrderSN(), coinsOrder.getUserId());
            return false;
        }

        coinsOrder.setIsPaid(1);
        int totalFee = Integer.valueOf(totalFeeString);

        payOrder.setTradeNo(tradeNo != null ? tradeNo : "");
        payOrder.setTradeStatus("");
        payOrder.setBuyerEmail("");
        payOrder.setIsPaid(1);
        payOrder.setNotifyData(responseData != null ? responseData : "");
        payOrder.setNotifyTime(System.currentTimeMillis());

        Account accountOld = new Account(account);

        PackageTool pt = calcAccountDataByBillingNotify(account, coinsOrder);
        if (pt == null) {
            Application.getLog().warn("handleWeiXinPayNotify_Billing calc account data error.paySN: {} orderSN: {} userId: {}"
                    , paySN, payOrder.getOrderSN(), coinsOrder.getUserId());
            return false;
        }

        account = pt.accout;
        coinsOrderService.save(coinsOrder);
        payOrderService.save(payOrder);
        account = accountService.save(account);
        accountLogService.saveAccountLog(accountOld,
                account,
                AccountLogService.CHANGE_TYPE_COINS_GAIN_BUY_WX,
                0,
                totalFee,
                pt.extraData);
        noticeBusinessService.sendBuyCoinsNotification(coinsOrder.getUserId(), totalFee);
        return true;
    }

    private PackageTool calcAccountDataByBillingNotify(Account account, CoinsOrder coinsOrder)
    {
        int billingType = coinsOrder.getBillingType();
        int billingId = coinsOrder.getBillingId();
        switch (billingType) {
            case BillingController.BILLING_TYPE_CHARGE:             // 充值金币
                return calcAccountDataByBillingNotify_Charge(account, billingType, billingId);
            case BillingController.BILLING_TYPE_CARD:               // 充值周卡月卡
                return calcAccountDataByBillingNotify_Card(account, billingType, billingId);
            case BillingController.BILLING_TYPE_NEW_USER_GIFT:      // 新手礼包
                return calcAccountDataByBillingNotify_NewUserGift(account, billingType, billingId);
            case BillingController.BILLING_TYPE_CHARGE_FIRST_TIME:  // 充值金币首冲
                return calcAccountDataByBillingNotify_ChargeFirstTime(account, billingType, billingId);
        }
        return null;
    }

    // 充值
    private PackageTool calcAccountDataByBillingNotify_Charge(Account account, int billingType, int billingId)
    {
        Charge charge = chargeServiceCache.findOne(billingId);
        if (charge == null) {
            Application.getLog().warn("calcAccountDataByBillingNotify_Charge can't find charge. chargeId: {}", billingId);
            return null;
        }
        Date tnow = new Date();
        account.setCoins(account.getCoins() + charge.totalCoins());
        account.setAvailableCoins(account.getAvailableCoins() + charge.totalCoins());
        // 设置已经冲过值
        account.setChargeFirstTime(1);
        account.setChargeFirstTimeDatetime(tnow);
        PackageTool pt = new PackageTool(account);
        pt.coins = charge.totalCoins();
        return pt;
    }

    // 购买周卡月卡
    private PackageTool calcAccountDataByBillingNotify_Card(Account account, int billingType, int billingId)
    {
        Card card = cardServiceCache.findOne(billingId);
        if (card == null) {
            Application.getLog().warn("calcAccountDataByBillingNotify_Card can't find card. cardId: {}", billingId);
            return null;
        }

        Date tnow = new Date();
        account.setCoins(account.getCoins() + card.getImmediateCoins());
        account.setAvailableCoins(account.getAvailableCoins() + card.getImmediateCoins());
        // 设置已经冲过值
        account.setChargeFirstTime(1);
        account.setChargeFirstTimeDatetime(tnow);

        // 保存新买的卡
        UserCard user_card = new UserCard();
        user_card.setUserId(account.getUserId());
        user_card.setCardType(card.getCardType());

        // 充值完成后可以立即获得
        //user_card.setRewardDatetime(tnow);
        user_card.setExpiredDatetime(new Date(tnow.getTime() + card.getDuration()));
        user_card = userCardServiceCache.save(user_card);

        AccountLogExtraData.JsonCardReward json_obj = new AccountLogExtraData().createJsonCardReward(user_card.getCardId()
                , card.getCardType());
        PackageTool pt = new PackageTool(account);
        pt.coins = card.getImmediateCoins();
        pt.extraData = JsonUtil.convertObject2Json(json_obj);
        return pt;
    }

    // 购买新手礼包
    private PackageTool calcAccountDataByBillingNotify_NewUserGift(Account account, int billingType, int billingId)
    {
        Charge charge = chargeServiceCache.findOne(billingId);
        if (charge == null) {
            Application.getLog().warn("calcAccountDataByBillingNotify_NewUserGift can't find charge. chargeId: {}", billingId);
            return null;
        }

        Date tnow = new Date();
        account.setCoins(account.getCoins() + charge.totalCoins());
        account.setAvailableCoins(account.getAvailableCoins() + charge.totalCoins());
        // 设置已经冲过值
        account.setChargeFirstTime(1);
        account.setChargeFirstTimeDatetime(tnow);
        // 设置新手礼包领取
        account.setNewUserGift(1);
        account.setGiftDatetime(tnow);

        PackageTool pt = new PackageTool(account);
        pt.coins = charge.totalCoins();
        return pt;
    }

    // 购买首冲
    private PackageTool calcAccountDataByBillingNotify_ChargeFirstTime(Account account, int billingType, int billingId)
    {
        Charge charge = chargeServiceCache.findOne(billingId);
        if (charge == null) {
            Application.getLog().warn("calcAccountDataByBillingNotify_ChargeFirstTime can't find charge. chargeId: {}", billingId);
            return null;
        }

        Date tnow = new Date();
        account.setCoins(account.getCoins() + charge.totalCoins());
        account.setAvailableCoins(account.getAvailableCoins() + charge.totalCoins());
        // 设置已经冲过值
        account.setChargeFirstTime(1);
        account.setChargeFirstTimeDatetime(tnow);

        PackageTool pt = new PackageTool(account);
        pt.coins = charge.totalCoins();
        return pt;
    }
}
