package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.model.PlatDictInfo;
import com.kariqu.zwsrv.app.service.PlatDictServiceCache;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("plat/v1")
public class PlatformController extends BaseController {

    @Autowired
    PlatDictServiceCache platDictServiceCache;

    @RequestMapping(value="/time_now")
    public ResponseData time() {
        long timeNow = System.currentTimeMillis();
        return new ResponseData().put("time_now", timeNow);
    }

    @RequestMapping(value="/check_update")
    public ResponseData check_update(HttpServletRequest request,
                                     @RequestParam Map<String,String> allRequestParams) {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
            requestContext.validateInputParams("platform","version_code");
            String platform = requestContext.getStringValue("platform");
            int versionCode = requestContext.getInegerValue("version_code");
            if (platform.equalsIgnoreCase("ios")) {

            }
            else if (platform.equalsIgnoreCase("android")) {

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

    @RequestMapping(value="/plat_config")
    public ResponseData plat_config() {
        List<PlatDictInfo> list = platDictServiceCache.getPlatConfigList();
        if (list==null) {
            list=new ArrayList<>();
        }
        return new ResponseData().put("list", list);
    }


}
