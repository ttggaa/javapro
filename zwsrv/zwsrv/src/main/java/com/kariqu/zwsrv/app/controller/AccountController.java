package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.Application;
import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.app.model.AccountInfo;
import com.kariqu.zwsrv.app.model.AccountLogInfo;
import com.kariqu.zwsrv.app.model.PaginationRequestData;
import com.kariqu.zwsrv.app.model.PaginationRspData;
import com.kariqu.zwsrv.app.service.AccountBusinessService;
import com.kariqu.zwsrv.app.service.ChargeServiceCache;
import com.kariqu.zwsrv.app.transaction.IsTryAgain;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import com.kariqu.zwsrv.thelib.persistance.entity.Charge;
import com.kariqu.zwsrv.thelib.persistance.service.AccountLogService;
import com.kariqu.zwsrv.thelib.persistance.service.ChargeService;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 24/11/17.
 */

@RestController
@RequestMapping("account/v1")
public class AccountController extends BaseController {

    @Autowired
    AccountBusinessService accountBusinessService;

    @Autowired
    ChargeServiceCache chargeServiceCache;

    //get_account
    @RequestMapping(value="/get_account")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData get_account(HttpServletRequest request,
                                    @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            Account account = accountBusinessService.findAccount(currentUserId);
            if (account==null) {
                Application.getLog().warn("get_account can't find account. user: {}", currentUserId);
                return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
                //account = new Account();
                //account.setUserId(currentUserId);
            }

            int newUserGiftSeconds = 0;
            // 只能买1次
            if (account.getNewUserGift() == 0) {
                Charge charge = chargeServiceCache.findByType(ChargeService.CHARGE_TYPE_NEW_USER_GIFT);
                if (charge != null) {
                    long delta = System.currentTimeMillis() - account.getCreateTime();
                    newUserGiftSeconds = (int)((charge.getDuration() - delta)/1000);
                    if (newUserGiftSeconds < 0)
                        newUserGiftSeconds = 0;
                }
            }
            return new ResponseData().put("account",new AccountInfo(account, newUserGiftSeconds));
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    //金币充值|消耗
    @RequestMapping(value="/list_gain_points")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_gain_points(HttpServletRequest request,
                                         @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);

            List<Integer> types = new ArrayList<>();
            types.add(AccountLogService.CHANGE_TYPE_POINTS_GAIN_GAME); //抓娃娃

            PaginationRequestData paginationData = PaginationRequestData.create(allRequestParams);
            List<AccountLogInfo> list = accountBusinessService.findAccountPointLogs(currentUserId, types, paginationData.getPage(), paginationData.getSize());
            if (list==null) {
                list=new ArrayList<>();
            }
            for (AccountLogInfo logInfo : list) {
                logInfo.setImageUrl(URLHelper.fullUrl(logInfo.getImageUrl()));
            }
            boolean hasMore = list!=null&&list.size()>=paginationData.getSize()?true:false;
            return new PaginationRspData(list,hasMore);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    //金币充值|消耗
    @RequestMapping(value="/list_expend_points")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_expend_points(HttpServletRequest request,
                                           @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
            List<Integer> types = new ArrayList<>();

            types.add(AccountLogService.CHANGE_TYPE_POINTS_EXPEND_EXCHANGE_COINS); //兑换
            types.add(AccountLogService.CHANGE_TYPE_POINTS_EXPEND_PAY_DELIVERY); //抵邮费

            PaginationRequestData paginationData = PaginationRequestData.create(allRequestParams);
            List<AccountLogInfo> list = accountBusinessService.findAccountPointLogs(currentUserId, types, paginationData.getPage(), paginationData.getSize());
            if (list==null) {
                list=new ArrayList<>();
            }
            for (AccountLogInfo logInfo : list) {
                logInfo.setImageUrl(URLHelper.fullUrl(logInfo.getImageUrl()));
            }
            boolean hasMore = list!=null&&list.size()>=paginationData.getSize()?true:false;
            return new PaginationRspData(list,hasMore);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }


    @RequestMapping(value="/list_coins")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_gain_coins(HttpServletRequest request,
                                          @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);

            List<Integer> types = new ArrayList<>();

            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_REGISTER); //注册
            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_BIND_INVITATION_CODE); //邀请

            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_SHARED);

            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_EXCHANGE_GOODS); //礼物兑换
            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_EXCHANGE_POINTS); //积分兑换

            types.add(AccountLogService.CHANGE_TYPE_COINS_EXPEND_PAY_GAME); //
            types.add(AccountLogService.CHANGE_TYPE_COINS_EXPEND_PAY_DELIVERY); //

            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_BUY_ALIPAY); //支付宝充值
            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_BUY_WX); //微信充值

            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_SIGNIN);   // 金币获得，签到
            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARD_CARD_WEEKLY);  // 金币获得，周卡奖励
            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARD_CARD_MONTHLY);// 金币获得，月卡奖励

            types.add(AccountLogService.CHANGE_TYPE_COINS_TBJ_DROP);    // "推币机投币"
            types.add(AccountLogService.CHANGE_TYPE_COINS_TBJ_REWARD);  //  "推币机出币"

            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_SYSTEM);// 官方赠送

            //public static final int CHANGE_TYPE_COINS_GAIN_REWARDS_GAME=CHANGE_TYPE_COINS+10; //金币获得，游戏奖励

            PaginationRequestData paginationData = PaginationRequestData.create(allRequestParams);
            List<AccountLogInfo> list= accountBusinessService.findAccountCoinLogs(currentUserId, types, paginationData.getPage(), paginationData.getSize());
            if (list==null) {
                list=new ArrayList<>();
            }
            for (AccountLogInfo logInfo : list) {
                logInfo.setImageUrl(URLHelper.fullUrl(logInfo.getImageUrl()));
            }
            boolean hasMore = list!=null&&list.size()>=paginationData.getSize()?true:false;
            return new PaginationRspData(list,hasMore);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    @RequestMapping(value="/list_buy_coins")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_buy_coins(HttpServletRequest request,
                                       @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);

            List<Integer> types = new ArrayList<>();

            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_BUY_ALIPAY); //支付宝充值
            types.add(AccountLogService.CHANGE_TYPE_COINS_GAIN_BUY_WX); //微信充值

            PaginationRequestData paginationData = PaginationRequestData.create(allRequestParams);
            List<AccountLogInfo> list= accountBusinessService.findAccountCoinLogs(currentUserId, types, paginationData.getPage(), paginationData.getSize());
            if (list==null) {
                list=new ArrayList<>();
            }
            for (AccountLogInfo logInfo : list) {
                logInfo.setImageUrl(URLHelper.fullUrl(logInfo.getImageUrl()));
            }
            boolean hasMore = list!=null&&list.size()>=paginationData.getSize()?true:false;
            return new PaginationRspData(list,hasMore);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    //获取积分兑换金币额度
    @RequestMapping(value="/points_exchange_to_coins_num")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData points_exchange_to_coins_num(HttpServletRequest request,
                                                     @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            return accountBusinessService.pointsExchangeToCoinsNum(currentUserId);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    //积分兑换金币
    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/points_exchange_to_coins")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData points_exchange_to_coins(HttpServletRequest request,
                                                 @RequestParam Map<String,String> allRequestParams) {

        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            return accountBusinessService.pointsExchangeToCoins(currentUserId);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

}
