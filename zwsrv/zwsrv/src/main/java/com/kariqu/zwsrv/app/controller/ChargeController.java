package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.Application;
import com.kariqu.zwsrv.app.model.ChargeInfo;
import com.kariqu.zwsrv.app.service.ChargeServiceCache;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import com.kariqu.zwsrv.thelib.persistance.entity.Charge;
import com.kariqu.zwsrv.thelib.persistance.service.AccountService;
import com.kariqu.zwsrv.thelib.persistance.service.ChargeService;
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
@RequestMapping("charge/v1")
public class ChargeController {

    @Autowired
    private ChargeServiceCache chargeServiceCache;

    @Autowired
    private AccountService accountService;

    // 依照type分开
    private void classifyType(List<Charge> all, List<Charge> fixed, List<Charge> firestTime)
    {
        if (all == null)
            return;
        for (Charge c : all) {
            int type = c.getChargeType();
            if (type == ChargeService.CHARGE_TYPE_FIXED) {
                fixed.add(c);
            } else if (type == ChargeService.CHARGE_TYPE_FIRST_TIME) {
                firestTime.add(c);
            }
        }
    }

    @RequestMapping(value="/list_charge")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_coins(HttpServletRequest request,
                                   @RequestParam Map<String,String> allRequestParams) {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            //HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            List<Charge> all = chargeServiceCache.findAllChargeAsList();
            List<Charge> fixed = new ArrayList<>();
            List<Charge> firstTime = new ArrayList<>();
            classifyType(all, fixed, firstTime);

            Account accout = accountService.findOne(userId);
            if (accout == null) {
                // 不应该发生这种情况，account必须是存在的。
                Application.getLog().error("list_coins account is null. user: {}", userId);
                accout = new Account();
            }

            if (accout.getChargeFirstTime() != 0) {
                firstTime.clear();
            }

            // 固定代码,删除fixd中chargeId 1000 存在在firstTime
            if (!firstTime.isEmpty()) {
                Charge id = null;
                for (Charge c : fixed) {
                    if (c.getChargeId() == 1000) {
                        id = c;
                        break;
                    }
                }
                if (id != null) {
                    fixed.remove(id);
                }
            }

            List<ChargeInfo> info_list = new ArrayList<>();
            // 合并
            for (Charge c : firstTime) {
                // 是首冲
                info_list.add(new ChargeInfo(c, 1));
            }
            for (Charge c : fixed) {
                info_list.add(new ChargeInfo(c, 0));
            }
            return new ResponseData().put("list", info_list);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }
}
