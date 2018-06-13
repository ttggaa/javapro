package com.kariqu.zwsrv.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by simon on 09/12/17.
 */
@RestController
@RequestMapping("rong/v1")
public class RongController extends BaseController {

//    @Autowired
//    UserServiceWithCache userService;
//
//    @Autowired
//    RongCloudService rongCloudService;
//
//
//    @RequestMapping(value="/request_rong_token")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseData request_rong_token(HttpServletRequest request,
//                                           @RequestParam Map<String,String> allRequestParams) {
//
//        int currentUserId = SecurityUtil.currentUserId();
//        if (currentUserId==0) {
//            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
//        }
//
//        try {
//            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
//            int refresh = requestContext.getInegerValue("refresh",0,"");
//            User user = userService.findOne(currentUserId);
//            if (user!=null) {
//
//
//                RongToken rongToken = rongCloudService.requestToken(user.getUserId(), user.getNickName(), URLHelper.fullUrl(user.getAvatar()),refresh>0?true:false);
//                if (rongToken!=null
//                        &&rongToken.getToken()!=null
//                        &&rongToken.getToken().length()>0) {
//                    ResponseData responseData= new ResponseData();
//                    responseData.put("rong_token", rongToken);
//                    return responseData;
//                }
//            }
//        }catch(ServerException e){
//            e.printStackTrace();
//            return e.toResponseData();
//        }catch(Exception e){
//            e.printStackTrace();
//            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
//        }
//        return new ErrorResponse(ErrorCode.ERROR_FAILED);
//    }

}
