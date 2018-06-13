package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.service.UserCountServiceCache;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.UserCount;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by simon on 27/11/17.
 */
@RestController
@RequestMapping("user_count/v1")
public class UserCountController extends BaseController {

    @Autowired
    UserCountServiceCache userCountServiceCache;

    @RequestMapping(value="/get_user_count")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData get_account(HttpServletRequest request,
                                    @RequestParam Map<String,String> allRequestParams,
                                    HttpServletResponse response) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            int userId = requestContext.getInegerValue("user_id");
            if (userId==0) {
                userId=currentUserId;
            }
            UserCount userCount= userCountServiceCache.findOne(userId);
            if (userCount==null) {
                userCount=new UserCount();
                userCount.setUserId(userId);
            }
            return new ResponseData().put("user_count",userCount);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

}
