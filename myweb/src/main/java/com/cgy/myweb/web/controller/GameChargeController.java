package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.web.persistance.entityex.GameCoinsOrder;
import com.kariqu.zwsrv.web.persistance.service.GameChargeService;
import com.kariqu.zwsrv.web.persistance.service.GameUserService;
import com.kariqu.zwsrv.web.utilityex.AjaxResponse;
import com.kariqu.zwsrv.web.utilityex.DataTableServiceSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/gameuser")
public class GameChargeController {

    private static final Logger log = LoggerFactory.getLogger(GameUserController.class);

    @Autowired
    GameUserService gameUserService;

    @Autowired
    GameChargeService gameChargeService;

    @RequestMapping (value = "chargeinfo")
    public String info(HttpServletRequest request)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "gameuser/chargeinfo";
    }

    @RequestMapping(value = "chargeinfotable",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse infotablePost(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);
            int user_id = requestCtx.getInegerValue("input_userid");
            String date_start = requestCtx.getStringValue("date_start");
            String date_end = requestCtx.getStringValue("date_end");

            long recordsTotal = gameChargeService.count_search(user_id, date_start, date_end);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<GameCoinsOrder>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<GameCoinsOrder> result = gameChargeService.limit_search(start, length, user_id, date_start, date_end);
            if (result != null) {
                for (GameCoinsOrder coinsOrder : result) {
                    aj.add(coinsOrder);
                }
            }
            return aj;
        } catch (ServerException e) {
            log.error("-----{}", e);
        }
        return new AjaxResponse();
    }

}
