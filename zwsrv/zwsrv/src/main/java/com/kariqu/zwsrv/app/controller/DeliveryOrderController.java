package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.model.DeliveryOrderInfo;
import com.kariqu.zwsrv.app.transaction.IsTryAgain;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryGoods;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryOrder;
import com.kariqu.zwsrv.thelib.persistance.service.DeliveryGoodsService;
import com.kariqu.zwsrv.thelib.persistance.service.DeliveryOrderService;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by simon on 16/12/17.
 */

@RestController
@RequestMapping("delivery_order/v1")
public class DeliveryOrderController extends BaseController {

    @Autowired
    DeliveryOrderService deliveryOrderService;

    @Autowired
    DeliveryGoodsService deliveryGoodsService;


    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/get_orders")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData get_orders(HttpServletRequest request,
                                   @RequestParam Map<String,String> allRequestParams) {

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("ids");
            String idsString = requestContext.getStringValue("ids");

            if (idsString!=null&&idsString.length()>0) {
                List<Integer> ids = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(idsString,"|");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken();
                    if (NumberValidationUtil.isWholeNumber(nextToken)) {
                        ids.add(Integer.valueOf(nextToken));
                    }
                }

                if (ids.size()>0) {
                    List<DeliveryOrder> deliveryOrderList = deliveryOrderService.findAll(ids);
                    if (deliveryOrderList==null) {
                        deliveryOrderList=new ArrayList<>();
                    }

                    List<DeliveryOrderInfo> deliveryOrderInfoList = new ArrayList<>();
                    for (DeliveryOrder deliveryOrder : deliveryOrderList) {
                        deliveryOrderInfoList.add(new DeliveryOrderInfo(deliveryOrder));
                    }
                    return new ResponseData().put("list",deliveryOrderInfoList);
                }
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

    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/get_order_by_game_ids")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData get_order_by_game_ids(HttpServletRequest request,
                                              @RequestParam Map<String,String> allRequestParams) {

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("ids");
            String idsString=requestContext.getStringValue("ids");

            if (idsString!=null&&idsString.length()>0)
            {
                List<Integer> ids = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(idsString,"|");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken();
                    if (NumberValidationUtil.isWholeNumber(nextToken)) {
                        ids.add(Integer.valueOf(nextToken));
                    }
                }

                if (ids.size()>0) {
                    List<DeliveryGoods> list = deliveryGoodsService.findDeliveryGoods(0, ids);
                    if (list==null) {
                        list=new ArrayList<>();
                    }
                    Map<Integer,DeliveryGoods> gameMap = new HashMap<>();
                    for (DeliveryGoods deliveryGoods : list) {
                        gameMap.put(deliveryGoods.getOrderId(),deliveryGoods);
                    }

                    if (gameMap.keySet().size()>0) {
                        List<DeliveryOrder> deliveryOrderList = deliveryOrderService.findAll(gameMap.keySet());
                        if (deliveryOrderList==null) {
                            deliveryOrderList=new ArrayList<>();
                        }

                        List<DeliveryOrderInfo> deliveryOrderInfoList = new ArrayList<>();
                        for (DeliveryOrder deliveryOrder : deliveryOrderList) {
                            deliveryOrderInfoList.add(new DeliveryOrderInfo(deliveryOrder));
                        }
                        return new ResponseData().put("list",deliveryOrderInfoList);
                    }
                }
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
