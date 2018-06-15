package com.kariqu.zwsrv.web.controller;


import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kariqu.zwsrv.thelib.error.ErrorCode;
import org.springframework.web.bind.annotation.RestController;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.service.RoomService;
import com.kariqu.zwsrv.web.persistance.entityex.WebRoom;
import com.kariqu.zwsrv.web.persistance.service.WebRoomService;
import com.kariqu.zwsrv.web.persistance.entityex.WebExpressage;
import com.kariqu.zwsrv.web.persistance.service.WebAccountService;
import com.kariqu.zwsrv.web.utilityex.AjaxResponse;
import com.kariqu.zwsrv.web.utilityex.DataTableServiceSide;
import com.kariqu.zwsrv.web.utilityex.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    WebAccountService webAccountService;

    @RequestMapping(value = "index")
    public String xxx(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "index";
    }

    @RequestMapping(value="")
    public String loginSign(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        return "login";
    }

    @RequestMapping(value = "logout")
    public String logOut(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        request.getSession().removeAttribute("admin");
        return "login";
    }

    @RequestMapping(value="sigh_in")
    @ResponseBody
    public  ResponseData loginSigh(HttpServletRequest request , @RequestParam Map<String,String> params,Model model)
    {
        if (null != request.getSession().getAttribute("admin")) {
            ResponseData aj = new ResponseData();
            aj.put("url", "/login/index");
            return aj;
        }

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            String account = requestCtx.getStringValue("account");
            String password = requestCtx.getStringValue("password");
            if (account != "") {
                boolean success = webAccountService.login(account, password);
                ResponseData aj = new ResponseData();
                if (!success) {
                    aj.setCode(6049);
                    aj.setMsg("用户名或密码错误");
                }else {
                    request.getSession().setAttribute("admin",account);
                    request.getSession().setMaxInactiveInterval(3600);
                    /// 成功后从定向
                    aj.put("url", "/login/index");
                }
                return aj;
            }
            return new ErrorResponse(ErrorCode.ERROR_AUTHENTICATION);
        } catch (Exception e) {
            //System.out.println(e);
            log.info("-----{}", e);
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }
}
