package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.service.AccountBusinessService;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.service.CoinsService;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("billing/v1")
public class BillingController {

    // 计费类型
    public static final int BILLING_TYPE_CHARGE = 1;    // 充值金币
    public static final int BILLING_TYPE_CARD = 2;    // 充值周卡月卡
    public static final int BILLING_TYPE_NEW_USER_GIFT = 3;    // 新手礼包
    public static final int BILLING_TYPE_CHARGE_FIRST_TIME = 4; // 充值金币首冲

    public static final String BILLING_BIZ_CHARGE = "charge";    // 充值金币
    public static final String BILLING_BIZ_CARD = "card";    // 充值周卡月卡
    public static final String BILLING_BIZ_NEW_USER_GIFT = "new_user_gift";    // 新手礼包
    public static final String BILLING_BIZ_CHARGE_FIRST_TIME = "charge_first_time"; // 充值金币首冲

    /**
    序号	渠道名称	渠道ID
1	官方（自持）	OFFICAL_ANDROID
2	VIVO	VIVO
3	OPPO	OPPO
4	应用宝	TENCENT
5	小米	XIAOMI
6	360	ANDROID_360
7	华为	HUAWEI
8	搜狗	SOUGOU
9	豌豆荚	WANDOUJIA
10	百度	BAIDU
     **/

    @Autowired
    CoinsService coinsService;

    @Autowired
    AccountBusinessService accountBusinessService;

    @RequestMapping(value="/billing")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData billing(HttpServletRequest request,
                                  @RequestParam Map<String,String> allRequestParams) {

        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);

            requestContext.validateInputParams("pay_way","pay_on", "billing_type", "billing_id", "channel");
            String payWay =requestContext.getStringValue("pay_way");
            int payOn = requestContext.getInegerValue("pay_on");
            int billingType = requestContext.getInegerValue("billing_type");
            int billingId = requestContext.getInegerValue("billing_id");
            String channel = requestContext.getStringValue("channel");
            if (payWay.length() > 0 && billingType > 0 && billingId > 0) {
                return accountBusinessService.clientBilling(currentUserId, payWay, payOn, billingType, billingId, channel);
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }
}
