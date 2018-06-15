package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import com.kariqu.zwsrv.web.persistance.entityex.WebOp;
import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.zookeeper.Op;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.web.persistance.service.OpService;
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
public class KrqOpController {
    private static final Logger log = LoggerFactory.getLogger(KrqOpController.class);

    @Autowired
    OpService opService;

    @RequestMapping(value = "krq")
    public String admin(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        return "admin/krq";
    }

    @RequestMapping(value = "krq_table")
    @ResponseBody
    public AjaxResponse krqTable(HttpServletRequest request,@RequestParam Map<String,String> params, Model model) {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);

            long recordTotal = opService.count();
            AjaxResponse aj = new AjaxResponse<WebOp>();
            aj.setRecordsTotal(recordTotal);
            aj.setRecordsFiltered(recordTotal);
            aj.setDraw(draw);

            if (start > recordTotal)
                return aj;

            List<WebOp> result = opService.limit(start, length);
            if (result != null) {
                for (WebOp op : result) {
                    aj.add(op);
                }
            }
            return aj;

        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    @RequestMapping(value = "krq_add")
    public String krqAdd(HttpServletRequest request,@RequestParam Map<String,String> params, Model model)
    {
        return "admin/krq_add";
    }

    @RequestMapping(value = "add")
    @ResponseBody
    public ResponseData krqAddData(HttpServletRequest request,@RequestParam Map<String,String> params,Model model) {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            String opkey = requestCtx.getStringValue("opkey");
            String opvalue = requestCtx.getStringValue("opvalue");

            boolean ret = opService.add(opkey,opvalue);
            if(!ret)
                return new ErrorResponse(ErrorCode.ERROR_INVALID_PARAMETERS);

            ResponseData aj = new ResponseData();
            aj.put("url","/admin/krq");
            return aj;
        } catch (Exception e) {
            log.error("-----{}", e);
            return new ErrorResponse(ErrorCode.ERROR_INVALID_PARAMETERS);
        }
    }

}
