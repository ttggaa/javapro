package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.model.CoinInfo;
import com.kariqu.zwsrv.app.service.AccountBusinessService;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.Coins;
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

/**
 * Created by simon on 26/11/17.
 */
@RestController
@RequestMapping("coins/v1")
public class CoinsController {

    @Autowired
    CoinsService coinsService;

    @Autowired
    AccountBusinessService accountBusinessService;

    @RequestMapping(value="/list_coins")
    public ResponseData list_coins(HttpServletRequest request,
                                   @RequestParam Map<String,String> allRequestParams) {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            List<Coins> list = coinsService.findAllCoins();
            if (list==null) {
                list=new ArrayList<>();
            }
            List<CoinInfo> coinInfoList = new ArrayList<>();
            for (Coins coins : list) {
                coinInfoList.add(new CoinInfo(coins));
            }
            return new ResponseData().put("list",coinInfoList);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }


    @RequestMapping(value="/buy_coins")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData buy_coins(HttpServletRequest request,
                                  @RequestParam Map<String,String> allRequestParams) {

        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);

            requestContext.validateInputParams("pay_way","pay_on","coins_id","coins_num");
            String payWay =requestContext.getStringValue("pay_way");
            int payOn = requestContext.getInegerValue("pay_on");
            int coinsId = requestContext.getInegerValue("coins_id");
            int coinsNum = requestContext.getInegerValue("coins_num");
            if (payWay.length()>0
                    && coinsId>0) {
                return accountBusinessService.buyCoins(currentUserId, payWay, payOn, coinsId, coinsNum);
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
