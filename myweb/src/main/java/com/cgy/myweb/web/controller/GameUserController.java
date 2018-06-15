package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.persistance.entity.Account;
import com.kariqu.zwsrv.thelib.persistance.entity.AccountLogExtraData;
import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import com.kariqu.zwsrv.thelib.persistance.service.AccountLogService;
import com.kariqu.zwsrv.thelib.persistance.service.AccountService;
import com.kariqu.zwsrv.thelib.persistance.service.CardService;
import com.kariqu.zwsrv.thelib.util.JsonUtil;
import com.kariqu.zwsrv.web.persistance.entityex.GameUser;
import com.kariqu.zwsrv.web.persistance.entityex.GameUserLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kariqu.zwsrv.thelib.error.ErrorCode;
import org.springframework.web.bind.annotation.RestController;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.web.persistance.service.GameUserService;
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
@RequestMapping(value = "/gameuser")
public class GameUserController {

    private static final Logger log = LoggerFactory.getLogger(GameUserController.class);

    @Autowired
    GameUserService gameUserService;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountLogService accountLogService;

    @RequestMapping (value = "userinfo")
    public String info(HttpServletRequest request)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "gameuser/userinfo";
    }

    @RequestMapping(value = "userinfotable",method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse infotableGet(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
       /* for (Map.Entry<String, String> it : params.entrySet()) {
            System.out.println(String.format("key:%s \t\t\t value:%s", it.getKey(), it.getValue()));
        }*/

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);

            long recordsTotal = gameUserService.count();
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<GameUser>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<GameUser> result = gameUserService.limit(start, length);
            if (result != null) {
                for (GameUser user : result) {
                    aj.add(user);
                }
            }
            return aj;
        } catch (ServerException e) {
            log.error("-----{}", e);
        }
        return new AjaxResponse();
    }

    @RequestMapping(value="userinfotable", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse infotablePost(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        /*for (Map.Entry<String, String> it : params.entrySet()) {
            System.out.println(String.format("key:%s \t\t\t value:%s", it.getKey(), it.getValue()));
        }*/

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);
            int user_id = requestCtx.getInegerValue("input_userid");
            String date_start = requestCtx.getStringValue("date_start");
            String date_end = requestCtx.getStringValue("date_end");

            long recordsTotal = gameUserService.count_search(user_id, date_start, date_end);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<GameUser>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<GameUser> result = gameUserService.limit_search(start, length, user_id, date_start, date_end);
            if (result != null) {
                for (GameUser user : result) {
                    aj.add(user);
                }
            }
            return aj;
        } catch (ServerException e) {
            log.error("-----{}", e);
        }
        return new AjaxResponse();
    }

    @RequestMapping(value = "userloginfotable",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse loginfo (HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);
            int user_id = requestCtx.getInegerValue("sub_id");

            long recordsTotal = gameUserService.countlog_search(user_id);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<GameUser>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<GameUserLog> result = gameUserService.limitlog_search(start, length, user_id);
            if (result != null) {
                for (GameUserLog log : result) {
                    aj.add(log);
                }
            }
            return aj;
        } catch (ServerException e) {
            log.error("-----{}", e);
        }
        return new AjaxResponse();
    }

    @RequestMapping (value = "usermodify")
    public String hrefModify(HttpServletRequest request,@RequestParam Map<String,String> params, Model model) {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int user_id = requestCtx.getInegerValue("user_id");

            model.addAttribute("user_id", user_id);
            return "gameuser/usermodify";
        } catch (ServerException e) {
            return Utility.server_error_html;
        }
    }

    @RequestMapping(value = "addCoins",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData addCoins (HttpServletRequest request, @RequestParam Map<String,String> params, Model model) {
        if (null == request.getSession().getAttribute("admin")) {
            return new ErrorResponse(ErrorCode.ERROR_FAILED);
        }

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);
            int user_id = requestCtx.getInegerValue("account");
            int count = requestCtx.getInegerValue("coins");

            Account account = accountService.findOne(user_id);
            if (account == null) {
                return new ErrorResponse(ErrorCode.ERROR_INVALID_PARAMETERS);

            }
            Account accountOld = new Account(account);
            account.setCoins(account.getCoins() + count);
            account.setAvailableCoins(account.getAvailableCoins() + count);

            account = accountService.save(account);

            String extra_data = "";

            int changeType = AccountLogService.CHANGE_TYPE_COINS_GAIN_REWARDS_SYSTEM;
            accountLogService.saveCardReward(accountOld, account, changeType, 0, 0, extra_data);

            ResponseData aj = new ResponseData();
            aj.put("url", "/gameuser/userinfo");
            return aj;
        } catch (ServerException e) {
            log.error("-----{}", e);
            return new ErrorResponse(ErrorCode.ERROR_INVALID_PARAMETERS);
        }
    }
}
