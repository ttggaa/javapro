package com.kariqu.zwsrv.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kariqu.zwsrv.app.Application;
import com.kariqu.zwsrv.app.model.UserSigninConfirmInfo;
import com.kariqu.zwsrv.app.model.UserSigninInfo;
import com.kariqu.zwsrv.app.service.AccountBusinessService;
import com.kariqu.zwsrv.app.service.PlatDictServiceCache;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.PlatDict;
import com.kariqu.zwsrv.thelib.persistance.entity.UserSignin;
import com.kariqu.zwsrv.thelib.persistance.service.UserSigninService;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import com.kariqu.zwsrv.thelib.util.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("signin/v1")
public class SigninController extends BaseController {

    class SigninReward
    {
        private int id;
        private int coins;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCoins() {
            return coins;
        }

        public void setCoins(int coins) {
            this.coins = coins;
        }
    }


    @Autowired
    UserSigninService userSigninService;

    @Autowired
    PlatDictServiceCache platDictServiceCache;

    @Autowired
    AccountBusinessService accountBusinessService;

    private boolean isSameDay(LocalDateTime d1, LocalDateTime d2)
    {
        return d1.getYear() == d2.getYear()
                && d1.getMonth() == d2.getMonth()
                && d1.getDayOfMonth() == d2.getDayOfMonth();
    }

    private SigninReward getRewardCoins(int signin_id)
    {
        ArrayList<String> names = new ArrayList<String>();
        names.add(PlatDictServiceCache.kSignin);

        try {
            List<PlatDict> rows = platDictServiceCache.findByName(names);
            if (rows.isEmpty()) {
                Application.getLog().warn("can't load signin config {}", signin_id);
                return null;
            }
            PlatDict row = rows.get(0);
            String json_str = row.getValue();

            int max_id = 0;
            Map<Integer, SigninReward> rewards = new HashMap<Integer, SigninReward>();
            JSONArray json_array = JSONObject.parseObject(json_str).getJSONArray("day");
            for (int i = 0; i < json_array.size(); ++i) {
                JSONObject json_obj = json_array.getJSONObject(i);
                SigninReward reward = new SigninReward();
                reward.setId(json_obj.getInteger("id"));
                reward.setCoins(json_obj.getInteger("coins"));
                rewards.put(reward.getId(), reward);
                if (reward.getId() > max_id)
                    max_id = reward.getId();
            }

            // 超出最大值，也算最大值
            if (signin_id >= max_id) {
                return rewards.get(max_id);
            } else {
                return rewards.get(signin_id);
            }
        } catch (Exception e) {
            Application.getLog().error(e.toString());
        }
        return null;
    }

    @RequestMapping(value="/get_signin")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData signin(HttpServletRequest request,
                                      @RequestParam Map<String,String> allRequestParams,
                                      HttpServletResponse response) {
        int currentUserId = 0;
        currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        UserSigninInfo info = null;
        try {
            UserSignin user_signin = userSigninService.loadUserSignin(currentUserId);

            // 插入新的用户签到数据
            if (user_signin == null) {
                user_signin = new UserSignin();
                user_signin.setUserId(currentUserId);
                user_signin.setSigninId(0);
                // 默认是昨天
                user_signin.setSigninDateTime(LocalDateTimeUtils.localDateTimeToDate(LocalDateTime.now().minusDays(1)));

                boolean ret = userSigninService.saveUserSignin(user_signin);
                if (ret) {
                    info = new UserSigninInfo(user_signin);
                    info.setCanSignin(1);
                } else {
                    Application.getLog().warn("save new user signin failed: {}", currentUserId);
                }
            } else {
                LocalDateTime tnow = LocalDateTime.now();
                LocalDateTime signin_time = LocalDateTimeUtils.dateToLocalDateTime(user_signin.getSigninDateTime());
                if (isSameDay(signin_time, tnow)) {
                    info = new UserSigninInfo(user_signin);
                    info.setCanSignin(0);
                } else {
                    info = new UserSigninInfo(user_signin);
                    info.setCanSignin(1);
                    if (!isSameDay(signin_time.plusDays(1), tnow)) {
                        info.setAlreadSigninId(0);
                    }
                }
            }

            if (info != null)
                return new ResponseData().put("data", info);
            else
                return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
            /*
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("ids");
            String idsString=requestContext.getStringValue("ids");
            */
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    @RequestMapping(value="/signin_confirm")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData signinConfirm(HttpServletRequest request,
                               @RequestParam Map<String,String> allRequestParams,
                               HttpServletResponse response) {

        int userId = SecurityUtil.currentUserId();
        if (userId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            UserSignin user_signin = userSigninService.loadUserSignin(userId);

            if (user_signin == null) {
                Application.getLog().warn("signin is null: {}", userId);
                return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
            }

            int next_id = 0;
            LocalDateTime tnow = LocalDateTime.now();
            LocalDateTime signin_time = LocalDateTimeUtils.dateToLocalDateTime(user_signin.getSigninDateTime());
            if (isSameDay(signin_time, tnow)) {
                return new ErrorResponse(ErrorCode.ERROR_FAILED);
            } else {
                if (!isSameDay(signin_time.plusDays(1), tnow)) {
                    next_id = 1;
                } else {
                    next_id = user_signin.getSigninId() + 1;
                }
            }

            SigninReward reward = getRewardCoins(next_id);
            if (reward == null) {
                Application.getLog().warn("can't find signin_id reward. uid: {} signin_id: {}", userId, next_id);
                return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
            }

            // 保存签名数据
            user_signin.setSigninId(next_id);
            user_signin.setSigninDateTime(LocalDateTimeUtils.localDateTimeToDate(tnow));
            if (!userSigninService.saveUserSignin(user_signin)) {
                Application.getLog().warn("saveUserSignin failed. uid: {} signin_id: {}", userId, next_id);
                return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
            } else {
                Application.getLog().info("DEBUG: saveUserSignin success. uid: {} signin_id: {}", userId, next_id);
            }

            // 保存用户coins,point
            accountBusinessService.rewardCoinsForSignin(userId, reward.getCoins());

            UserSigninConfirmInfo info = new UserSigninConfirmInfo();
            info.setSigninId(next_id);
            info.setRewardCoins(reward.getCoins());

            if (info != null)
                return new ResponseData().put("data", info);
            else
                return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);

            /*
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("ids");
            String idsString=requestContext.getStringValue("ids");
            */
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }
}
