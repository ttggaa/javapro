package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.model.ConfigInfo;
import com.kariqu.zwsrv.app.service.ConfigServiceCache;
import com.kariqu.zwsrv.app.service.NoticeBusinessService;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 03/11/17.
 */
@RestController
@RequestMapping("config/v1")
public class ConfigController extends BaseController {

    @Autowired
    ConfigServiceCache configServiceCache;

    @Autowired
    NoticeBusinessService noticeBusinessService;

    //模块:config/v1
    //接口:list
    //参数: module: feeds 首页配置
    @RequestMapping(value="/list_configs")
    public ResponseData list(HttpServletRequest request,
                             @RequestParam Map<String,String> allRequestParams) {

        HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);

        try {
            requestContext.validateInputParams("module");
            String module = requestContext.getStringValue("module");

//            int test = requestContext.getInegerValue("test");
//            if (test==1) {
//                noticeBusinessService.sendBroadcastNotification("testBroadcast","kawaji://openurl?http://www.baidu.com");
//            } else {
//                noticeBusinessService.sendNotification(13,"test","kawaji://openurl?http://www.baidu.com");
//            }

            if (module!=null&&module.length()>0) {
                List<Config> list = configServiceCache.findAllValidByModule(module);
                if (list==null) {
                    list=new ArrayList<>();
                }

                List<ConfigInfo> configInfoList = new ArrayList<>();
                for (Config config : list) {
                    configInfoList.add(new ConfigInfo(config));
                }
                return new ResponseData().put("list",configInfoList);
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
