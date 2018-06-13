package com.kariqu.zwsrv.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.kariqu.zwsrv.app.model.UserInfo;
import com.kariqu.zwsrv.app.service.AccountBusinessService;
import com.kariqu.zwsrv.app.service.UserServiceCache;
import com.kariqu.zwsrv.app.service.WeChatAuthService;
import com.kariqu.zwsrv.app.model.WeChatUserInfo;
import com.kariqu.zwsrv.app.transaction.IsTryAgain;
import com.kariqu.zwsrv.thelib.Constants;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import com.kariqu.zwsrv.thelib.persistance.entity.Auth;
import com.kariqu.zwsrv.thelib.persistance.entity.User;
import com.kariqu.zwsrv.thelib.persistance.service.AuthService;
import com.kariqu.zwsrv.thelib.security.CurrentUserDetails;
import com.kariqu.zwsrv.thelib.security.JwtAuthenticationTokenProvider;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import com.kariqu.zwsrv.thelib.util.ShareCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by simon on 08/11/17.
 */
@RestController
@RequestMapping("auth/v1")
public class AuthController extends BaseController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtAuthenticationTokenProvider tokenProvider;

    @Autowired
    WeChatAuthService weChatAuthService;

    @Autowired
    AuthService authService;

    @Autowired
    UserServiceCache userServiceCache;

//    @Autowired
//    RongCloudService rongCloudService;

    @Autowired
    AccountBusinessService accountBusinessService;


    //测试用
    @RequestMapping(value="/authorize")
    public ResponseData authorize_device(
            HttpServletRequest request,
            @RequestHeader(value="User-Agent", required=false) String userAgent,
            @RequestParam Map<String,String> allRequestParams,
            HttpServletResponse response) {

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            String app_channel = getAppChannel(requestContext);

            requestContext.validateInputParams("identifier", "credential", "authtype");
            String appId = requestContext.getHeader("appid");
            String identifier = requestContext.getStringValue("identifier");
            String credential = requestContext.getStringValue("credential");
            String authtype = requestContext.getStringValue("authtype");

            if (identifier!=null
                    && credential!=null
                    && Constants.isValidAppId(appId)
                    && Constants.isValidAuthType(authtype)) {

                Auth auth=authService.findByAuthTypeAndIdentifier(appId, authtype, identifier);
                if (auth!=null) {
                    LinkedHashMap<String,String> detailsMap = new LinkedHashMap<>();
                    detailsMap.put("authtype",authtype);
                    detailsMap.put("appid",appId);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(identifier,credential);
                    authenticationToken.setDetails(detailsMap);

                    Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    CurrentUserDetails currentUser = SecurityUtil.currentUser();
                    if (currentUser!=null) {
                        String token = tokenProvider.createToken(currentUser, true);
                        if (token != null
                                && token.length() > 0) {
                            User user = userServiceCache.findOne(SecurityUtil.currentUserId());
                            if (user!=null) {
                                String invitationCode = user.getInvitationCode();
                                if (invitationCode==null||invitationCode.length()==0) {
                                    user.setInvitationCode(ShareCodeUtil.toSerialCode(user.getUserId()));
                                }
                                user.setAppChannel(app_channel);
                                user.setLastLogin(System.currentTimeMillis());
                                String remoteAddr = requestContext.getRemoteAddr();
                                user.setLastIp(remoteAddr!=null?remoteAddr:"");
                                user = userServiceCache.save(user);

                                UserInfo userInfo= new UserInfo(user);

                                ResponseData responseData= new ResponseData();
                                responseData.put("token",token);
                                responseData.put("user",userInfo);
                                return responseData;
                            }
                        }
                    }
                }
                else {
                    //新用户
                    User user=new User();
                    user.setRegAppChannel(app_channel);
                    user.setAppChannel(app_channel);
                    user.setNickName(identifier);
                    user.setAvatarStatus(Constants.AvatarStatusNormal);
                    user.setLastLogin(System.currentTimeMillis());
                    String remoteAddr = requestContext.getRemoteAddr();
                    user.setLastIp(remoteAddr!=null?remoteAddr:"");
                    user= userServiceCache.save(user);

                    {
                        // 创见user时创建新account
                        Account account = accountBusinessService.getAccountService().findOne(user.getUserId());
                        if (account == null) {
                            account = new Account();
                            account.setUserId(user.getUserId());
                            account = accountBusinessService.getAccountService().save(account);
                        }
                    }

                    user.setInvitationCode(ShareCodeUtil.toSerialCode(user.getUserId()));
                    user= userServiceCache.save(user);

                    auth=new Auth();
                    auth.setAppId(appId);
                    auth.setAuthType(authtype);
                    auth.setIdentifier(identifier);
                    auth.setCredential(passwordEncoder.encode(credential));
                    auth.setUserId(user.getUserId());
                    auth=authService.save(auth);

                    //兼容oauth2
                    LinkedHashMap<String,String> detailsMap = new LinkedHashMap<>();
                    detailsMap.put("authtype",authtype);
                    detailsMap.put("appid",appId);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(identifier,credential);
                    authenticationToken.setDetails(detailsMap);

                    Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    CurrentUserDetails currentUser = SecurityUtil.currentUser();
                    if (currentUser!=null) {
                        String token = tokenProvider.createToken(currentUser, true);
                        if (token != null
                                && token.length() > 0) {

                            UserInfo userInfo= new UserInfo(user);

                            ResponseData responseData= new ResponseData();
                            responseData.put("token",token);
                            responseData.put("user",userInfo);
                            return responseData;
                        }
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


    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/login_with_wechat")
    public ResponseData login_with_wechat(
            HttpServletRequest request,
            @RequestHeader(value="User-Agent", required=false) String userAgent,
            @RequestParam Map<String,String> allRequestParams,
            HttpServletResponse response) {

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
            String app_channel = getAppChannel(requestContext);
            requestContext.validateInputParams("code");
            String appId = requestContext.getHeader("appid");
            String authType=Constants.AuthTypeWeChat;
            if (Constants.isValidAppId(appId)) {
                String code=requestContext.getStringValue("code");
                if (code!=null&&code.length()>0) {

                    String result=weChatAuthService.getAccessToken(code);
                    if (result!=null&&result.length()>0) {

                        JSONObject jsonObject = JSONObject.parseObject(result);
                        String access_token = jsonObject.getString("access_token");
                        String openId = jsonObject.getString("openId");

                        if (access_token!=null&&openId!=null) {
                            WeChatUserInfo weChatUserInfo=weChatAuthService.getUserInfo(access_token, openId);
                            if (weChatUserInfo!=null
                                    &&weChatUserInfo.getOpenId().length()>0
                                    &&weChatUserInfo.getUnionId().length()>0) {
                                Auth auth=authService.findByAuthTypeAndIdentifier(appId, authType, weChatUserInfo.getOpenId());
                                if (auth!=null) {
                                    User user= userServiceCache.findOne(auth.getUserId());
                                    if (user!=null) {
                                        //检查invitationCode,老用户
                                        String invitationCode = user.getInvitationCode();
                                        if (invitationCode==null||invitationCode.length()==0) {
                                            user.setInvitationCode(ShareCodeUtil.toSerialCode(user.getUserId()));
                                        }

                                        user.setAppChannel(app_channel);
                                        user.setUnionId(weChatUserInfo.getUnionId());
                                        user.setAvatarStatus(Constants.AvatarStatusNormal);
                                        user.setAvatar(weChatUserInfo.getAvatarUrl());
                                        user.setNickName(weChatUserInfo.getNickName());
                                        user.setGender(weChatUserInfo.getSex());
                                        user.setLastLogin(System.currentTimeMillis());
                                        String remoteAddr = requestContext.getRemoteAddr();
                                        user.setLastIp(remoteAddr!=null?remoteAddr:"");
                                        user= userServiceCache.save(user);

                                        auth.setIdentifier(weChatUserInfo.getOpenId());
                                        auth.setCredential(passwordEncoder.encode(access_token));
                                        auth=authService.save(auth);


                                        LinkedHashMap<String,String> detailsMap = new LinkedHashMap<>();
                                        detailsMap.put("authtype",authType);
                                        detailsMap.put("appid",appId);
                                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(weChatUserInfo.getOpenId(),access_token);
                                        authenticationToken.setDetails(detailsMap);

                                        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                                        SecurityContextHolder.getContext().setAuthentication(authentication);

                                        CurrentUserDetails currentUser = SecurityUtil.currentUser();
                                        if (currentUser!=null) {
                                            String token = tokenProvider.createToken(currentUser,true);
                                            if (token!=null&&token.length()>0) {
                                                UserInfo userInfo= new UserInfo(user);
                                                ResponseData responseData= new ResponseData();
                                                responseData.put("token",token);
                                                responseData.put("user",userInfo);
                                                return responseData;
                                            }
                                        }
                                    }
                                } else {
                                    boolean isNewUser = false;
                                    User user= userServiceCache.findByUnionId(weChatUserInfo.getUnionId());
                                    if (user==null) {
                                        user=new User();
                                        user.setRegAppChannel(app_channel);
                                        isNewUser=true;
                                    } else {
                                        //老用户
                                        String invitationCode = user.getInvitationCode();
                                        if (invitationCode==null||invitationCode.length()==0) {
                                            user.setInvitationCode(ShareCodeUtil.toSerialCode(user.getUserId()));
                                        }
                                    }
                                    user.setAppChannel(app_channel);
                                    user.setUnionId(weChatUserInfo.getUnionId());
                                    user.setAvatarStatus(Constants.AvatarStatusNormal);
                                    user.setAvatar(weChatUserInfo.getAvatarUrl());
                                    user.setNickName(weChatUserInfo.getNickName());
                                    user.setGender(weChatUserInfo.getSex());
                                    user.setLastLogin(System.currentTimeMillis());
                                    String remoteAddr = requestContext.getRemoteAddr();
                                    user.setLastIp(remoteAddr!=null?remoteAddr:"");
                                    user= userServiceCache.save(user);

                                    {
                                        // 创见user时创建新account
                                        Account account = accountBusinessService.getAccountService().findOne(user.getUserId());
                                        if (account == null) {
                                            account = new Account();
                                            account.setUserId(user.getUserId());
                                            account = accountBusinessService.getAccountService().save(account);
                                        }
                                    }

                                    //新用户
                                    String invitationCode = user.getInvitationCode();
                                    if (invitationCode==null||invitationCode.length()==0) {
                                        user.setInvitationCode(ShareCodeUtil.toSerialCode(user.getUserId()));
                                        user= userServiceCache.save(user);
                                    }


                                    auth=new Auth();
                                    auth.setAppId(appId);
                                    auth.setAuthType(authType);
                                    auth.setIdentifier(weChatUserInfo.getOpenId());
                                    auth.setCredential(passwordEncoder.encode(access_token));
                                    auth.setUserId(user.getUserId());
                                    auth=authService.save(auth);

                                    //兼容oauth2
                                    LinkedHashMap<String,String> detailsMap = new LinkedHashMap<>();
                                    detailsMap.put("authtype",authType);
                                    detailsMap.put("appid",appId);
                                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(weChatUserInfo.getOpenId(),access_token);
                                    authenticationToken.setDetails(detailsMap);

                                    Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                                    SecurityContextHolder.getContext().setAuthentication(authentication);

                                    CurrentUserDetails currentUser = SecurityUtil.currentUser();
                                    if (currentUser!=null) {
                                        String token = tokenProvider.createToken(currentUser,true);
                                        if (token!=null&&token.length()>0) {

                                            if (isNewUser) {
                                                accountBusinessService.rewardCoinsForRegister(user.getUserId());
                                            }

                                            // 新用户
                                            UserInfo userInfo= new UserInfo(user);
                                            if (isNewUser) {
                                                // 返回新用户获得多少金币
                                                userInfo.setNewRegister(accountBusinessService.getNewUserRewardCoinsNum());
                                            }

                                            ResponseData responseData= new ResponseData();
                                            responseData.put("token",token);
                                            responseData.put("user",userInfo);
                                            return responseData;
                                        }
                                    }
                                }
                            }
                        }
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

    @RequestMapping(value="/logout", method= RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData logout(HttpServletRequest request,
                               @RequestParam Map<String,String> allRequestParams) {
        if (SecurityUtil.currentUser()!=null) {
            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContextHolder.clearContext();
        }

        HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
        String appid = requestContext.getHeader("appid");
        if (Constants.isValidAppId(appid)) {
            return new ResponseData();
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }


    private static String getAppChannel(HttpRequestContext requestContext)
    {
        try {
            requestContext.validateInputParams("app_channel");
            return requestContext.getStringValue("app_channel");
        } catch (Exception e) {
            return "";
        }
    }
}
