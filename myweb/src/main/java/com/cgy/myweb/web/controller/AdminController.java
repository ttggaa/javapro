package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import com.kariqu.zwsrv.web.persistance.entityex.WebAccount;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.kariqu.zwsrv.thelib.error.ErrorCode;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    WebAccountService webAccountService;

    @RequestMapping(value = "admin_list")
    public String admin(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "admin/admin_list";
    }

    @RequestMapping(value = "admin_table")
    @ResponseBody
    public AjaxResponse adminTable(HttpServletRequest request,@RequestParam Map<String,String> params, Model model) {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);

            long recordTotal = webAccountService.count();
            AjaxResponse aj = new AjaxResponse<WebAccount>();
            aj.setRecordsTotal(recordTotal);
            aj.setRecordsFiltered(recordTotal);
            aj.setDraw(draw);

            if (start > recordTotal)
                return aj;

            List<WebAccount> result = webAccountService.limit(start, length);
            if (result != null) {
                for (WebAccount admin : result) {
                    aj.add(admin);
                }
            }
            return aj;

        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    @RequestMapping(value = "admin_add")
    public String adminAdd(HttpServletRequest request,@RequestParam Map<String,String> params, Model model)
    {
       return "admin/admin_add";
    }

    @RequestMapping(value = "add_data")
    @ResponseBody
    public ResponseData adminAddData(HttpServletRequest request,@RequestParam Map<String,String> params,Model model) {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            String account = requestCtx.getStringValue("account");
            String password = requestCtx.getStringValue("password");
            String remark = requestCtx.getStringValue("remark");

            boolean ret = webAccountService.add(account,password,remark);
            if(!ret)
                return new ErrorResponse(ErrorCode.ERROR_INVALID_PARAMETERS);

            ResponseData aj = new ResponseData();
            aj.put("url","/admin/admin_list");
            return aj;
        } catch (Exception e) {
            log.error("-----{}", e);
            return new ErrorResponse(ErrorCode.ERROR_INVALID_PARAMETERS);
        }
    }

}
