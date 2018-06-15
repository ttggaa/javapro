package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.web.persistance.entityex.WebAppData;
import com.kariqu.zwsrv.web.persistance.entityex.WebProduct;
import com.kariqu.zwsrv.web.persistance.service.WebAppDataService;
import com.kariqu.zwsrv.web.persistance.service.WebProductService;
import com.kariqu.zwsrv.web.utilityex.AjaxResponse;
import com.kariqu.zwsrv.web.utilityex.DataTableServiceSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    WebProductService webProductService;

    @Autowired
    WebAppDataService webAppDataService;

    @RequestMapping(value = "list")
    public String gameStatis(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "room/data_list";
    }


    @RequestMapping(value="listDetail")
    @ResponseBody
    public AjaxResponse gameStatisTableGet(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            AjaxResponse aj = new AjaxResponse<WebProduct>();

            List<String> days = webProductService.getDays();
            WebProduct firstLineData = new WebProduct();
            firstLineData.setRoom_id(0);
            firstLineData.setRoom_name("");
            firstLineData.setSum(days);
            aj.add(firstLineData);

            List<WebProduct> result = webProductService.getGameData();
            if (result != null) {
                for (WebProduct statis : result) {
                    aj.add(statis);
                }
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    @RequestMapping(value = "list2")
    public String gameStatis2(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "room/data_list2";
    }


    @RequestMapping(value="listDetail2")
    @ResponseBody
    public AjaxResponse gameStatisTableGet2(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);
            String app_channel = requestCtx.getStringValue("app_channel");

            long recordsTotal = webAppDataService.count(app_channel);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<WebAppData>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<WebAppData> result = webAppDataService.limit(start, length,app_channel);
            if (result != null) {
                for (WebAppData data : result) {
                    aj.add(data);
                }
            }
            return aj;

        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    @RequestMapping(value = "initselect")
    @ResponseBody
    public AjaxResponse initselect(HttpServletRequest request, @RequestParam Map<String,String> params, Model model) {
        try {
            AjaxResponse aj = new AjaxResponse<String>();

            List<String> result = webAppDataService.getChannelList();
            if (result != null) {
                for (String channel : result) {
                    aj.add(channel);
                }
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }
}
