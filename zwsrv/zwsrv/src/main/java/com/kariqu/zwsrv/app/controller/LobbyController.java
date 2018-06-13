package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.error.ErrorDefs;
import com.kariqu.zwsrv.app.model.CatchHistoryInfo;
import com.kariqu.zwsrv.app.service.CatchHistoryServiceCache;
import com.kariqu.zwsrv.app.service.RaiseProblemSerivceCache;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.RaiseProblem;
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
@RequestMapping("lobby/v1")
public class LobbyController {
    @Autowired
    private CatchHistoryServiceCache catchHistoryServiceCache;

    @Autowired
    private RaiseProblemSerivceCache raiseProblemSerivceCache;


    @RequestMapping(value="/add_catch_history")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData add_catch_history(HttpServletRequest request,
                            @RequestParam Map<String,String> allRequestParams) {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("uid", "nick_name", "user_icon", "room_id"
                    , "room_name", "room_icon", "timestamp");
            int user_id = requestContext.getInegerValue("uid");
            String nick_name = requestContext.getStringValue("nick_name");
            String user_icon = requestContext.getStringValue("user_icon");
            int room_id = requestContext.getInegerValue("room_id");
            String room_name = requestContext.getStringValue("room_name");
            String room_icon = requestContext.getStringValue("room_icon");
            int timestamp = requestContext.getInegerValue("timestamp");

            // 历史抓取记录
            CatchHistoryInfo catch_history_info = new CatchHistoryInfo();
            catch_history_info.setUserId(user_id);
            catch_history_info.setNickName(nick_name);
            catch_history_info.setUserIcon(user_icon);
            catch_history_info.setRoomId(room_id);
            catch_history_info.setRoomName(room_name);
            catch_history_info.setRoomIcon(room_icon);

            long t = ((long)timestamp) * 1000;
            catch_history_info.setTimestamp(t);
            catchHistoryServiceCache.leftPush(catch_history_info);
            return new ResponseData().put("data", "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_FAILED);
        }
    }

    @RequestMapping(value="/get_catch_history")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData get_catch_history(HttpServletRequest request,
                            @RequestParam Map<String,String> allRequestParams) {
        try {
            //HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
            List<CatchHistoryInfo> list = catchHistoryServiceCache.range(0, 16);
            if (list==null) {
                list = new ArrayList<>();
            }
            return new ResponseData().put("list",list);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    @RequestMapping(value="/raise_problem")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData raise_problem(HttpServletRequest request, @RequestParam Map<String,String> requestParams)
    {
        int userId = SecurityUtil.currentUserId();
        if (userId == 0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            String app_channel = requestContext.getStringValue("app_channel");
            String version = requestContext.getStringValue("version");
            int type = requestContext.getInegerValue("type");
            int room_id = requestContext.getInegerValue("room_id");
            String content = requestContext.getStringValue("content");
            String type_str = raiseProblemSerivceCache.getTypeStr(type);

            if (content.length() > raiseProblemSerivceCache.MAX_CONTEXT_LENGTH) {
                return new ErrorResponse(ErrorDefs.ERROR_CODE_TOO_LONG);
            }

            RaiseProblem rp = new RaiseProblem();
            rp.setUserId(userId);
            rp.setType(type);
            rp.setTypeStr(type_str);
            rp.setVersion(version);
            rp.setRoomId(room_id);
            rp.setAppChannel(app_channel);
            rp.setContent(content);
            rp = raiseProblemSerivceCache.save(rp);
            return new ResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }
}
