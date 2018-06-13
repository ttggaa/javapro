package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.model.ShippingInfo;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.Shipping;
import com.kariqu.zwsrv.thelib.persistance.service.ShippingService;
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
 * Created by simon on 08/12/17.
 */

@RestController
@RequestMapping("shipping/v1")
public class ShippingController extends BaseController {

    @Autowired
    ShippingService shippingService;

    @RequestMapping(value="/list_shippings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_shippings(HttpServletRequest request,
                                       @RequestParam Map<String,String> allRequestParams) {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            List<Shipping> list= shippingService.findAllShippings();
            if (list==null) {
                list=new ArrayList<>();
            }
            List<ShippingInfo> shippingInfoList = new ArrayList<>();
            for (Shipping shipping : list) {
                shippingInfoList.add(new ShippingInfo(shipping));
            }
            return new ResponseData().put("list",shippingInfoList);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

}
