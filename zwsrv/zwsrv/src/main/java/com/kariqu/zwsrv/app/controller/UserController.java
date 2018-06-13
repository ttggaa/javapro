package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.error.ErrorDefs;
import com.kariqu.zwsrv.app.model.UserInfo;
import com.kariqu.zwsrv.app.service.AccountBusinessService;
import com.kariqu.zwsrv.app.service.UserServiceCache;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.User;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by simon on 08/11/17.
 */
@RestController
@RequestMapping("user/v1")
public class UserController extends BaseController {

    @Autowired
    UserServiceCache userServiceCache;

    @Autowired
    AccountBusinessService accountBusinessService;

    @RequestMapping(value="/bind_invitation_code")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData input_invitation_code(HttpServletRequest request,
                                              @RequestParam Map<String,String> allRequestParams,
                                              HttpServletResponse response) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("invitation_code");
            String invitationCode=requestContext.getStringValue("invitation_code");
            if (invitationCode!=null&&invitationCode.length()>0) {
                User user = userServiceCache.findOne(currentUserId);
                if (user!=null) {
                    if (user.getInvitorCode()!=null&&user.getInvitorCode().length()>0) {
                        return new ErrorResponse(ErrorDefs.ERROR_CODE_USER_ALREADY_BIND_INVITATION_CODE);
                    } else {
                        if (user.getInvitationCode()!=null&&user.getInvitationCode().equalsIgnoreCase(invitationCode)) {
                            //不能输入自己的邀请码
                            return new ErrorResponse(ErrorDefs.ERROR_CODE_USER_INVALID_INVITATION_CODE);
                        } else {
                            User invitor = userServiceCache.findOneByInvitationCode(invitationCode);
                            if (invitor!=null) {
                                // 超过最大绑定次数
                                int max_count = accountBusinessService.getInvitationMaxCount();
                                if (invitor.getInvitationCount() >= max_count) {
                                    System.out.println(String.format("WARNING invitation count > max_count %d %d %d"
                                            , currentUserId, invitor.getUserId(), invitor.getInvitationCount()));
                                    return new ErrorResponse(ErrorDefs.ERROR_CODE_USER_ALREADY_BIND_INVITATION_CODE);
                                }

                                // 被绑定人被绑定次数+1
                                invitor.setInvitationCount(invitor.getInvitationCount() + 1);
                                userServiceCache.save(invitor);

                                // 绑定人记录绑定的邀请码和user_id
                                int invitorId = invitor.getUserId();
                                user.setInvitorId(invitorId);
                                user.setInvitorCode(invitationCode);
                                userServiceCache.save(user);
                                accountBusinessService.rewardCoinsForBindInvitationCode(user.getNickName(), user.getUserId(), invitorId);
                                return new ResponseData().put("invitor_id",invitorId).put("invitor_code",invitationCode);
                            } else {
                                return new ErrorResponse(ErrorDefs.ERROR_CODE_USER_INVALID_INVITATION_CODE);
                            }
                        }
                    }
                }
            } else {
                return new ErrorResponse(ErrorDefs.ERROR_CODE_USER_INVALID_INVITATION_CODE);
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


    @RequestMapping(value="/get_user_info")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData get_user_info(HttpServletRequest request,
                                      @RequestParam Map<String,String> allRequestParams,
                                      HttpServletResponse response) {

        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            //return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("ids");
            String idsString=requestContext.getStringValue("ids");

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
                    List<UserInfo> userInfoList=new ArrayList<>();
                    List<User> userList= userServiceCache.findAll(ids);
                    if (userList!=null) {
                        for (User user : userList) {
                            userInfoList.add(new UserInfo(user));
                        }
                    }
                    return new ResponseData().put("list",userInfoList);
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
