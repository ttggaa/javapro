package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.City;
import com.kariqu.zwsrv.thelib.persistance.entity.DeliveryGoods;
import com.kariqu.zwsrv.thelib.persistance.service.CityService;
import com.kariqu.zwsrv.web.persistance.entityex.WebExpressage;
import com.kariqu.zwsrv.web.persistance.entityex.WebExpressageGood;
import com.kariqu.zwsrv.web.persistance.service.WebDeliveryGoodsService;
import com.kariqu.zwsrv.web.persistance.service.WebDeliveryOrderService;
import com.kariqu.zwsrv.web.utilityex.AjaxResponse;
import com.kariqu.zwsrv.web.utilityex.CityJson;
import com.kariqu.zwsrv.web.utilityex.DataTableServiceSide;
import com.kariqu.zwsrv.web.utilityex.Utility;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/expressage")
public class ExpressageOrderController {

    private static final Logger log = LoggerFactory.getLogger(ExpressageOrderController.class);

    @Autowired
    WebDeliveryOrderService webDeliveryOrderService;

    @Autowired
    WebDeliveryGoodsService webDeliveryGoodsService;

    @Autowired
    CityService cityService;

    // 未处理订单
    @RequestMapping(value="order")
    public String order(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        // TODO
        //CityJson.getInstance().init(webDeliveryOrderService);
        return "expressage/order";
    }

    @RequestMapping(value="order_table", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse orderTableGet(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
       /* for (Map.Entry<String, String> it : params.entrySet()) {
            System.out.println(String.format("key:%s \t\t\t value:%s", it.getKey(), it.getValue()));
        }*/

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);

            long recordsTotal = webDeliveryOrderService.count(0);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<WebExpressage>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<WebExpressage> result = webDeliveryOrderService.limit(start, length, 0);
            if (result != null) {
                for (WebExpressage order : result) {
                    {
                        City city = cityService.findOneWithCached(order.getProvince());
                        if (city != null) {
                            order.setProvinceStr(city.getName());
                        }
                    }

                    {
                        City city = cityService.findOneWithCached(order.getCity());
                        if (city != null) {
                            order.setCityStr(city.getName());
                        }
                    }

                    {
                        City city = cityService.findOneWithCached(order.getDistrict());
                        if (city != null) {
                            order.setDistrictStr(city.getName());
                        }
                    }
                    String goodName = webDeliveryGoodsService.getContactGoodName(order.getOrder_id());
                    order.setGood_name(goodName);

                    aj.add(order);
                }
            }


            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    @RequestMapping(value="order_table", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse orderTablePost(HttpServletRequest request, @RequestParam Map<String,String> params, Model model) {
        /*for (Map.Entry<String, String> it : params.entrySet()) {
            System.out.println(String.format("key:%s \t\t\t value:%s", it.getKey(), it.getValue()));
        }*/

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);
            int order_id = requestCtx.getInegerValue("input_orderid");
            String ship_name = requestCtx.getStringValue("input_shipname");
            String rev_name = requestCtx.getStringValue("input_revname");
            long mobile = requestCtx.getLongValue("input_mobile");
            int user_id = requestCtx.getInegerValue("input_userid");
            String date_start = requestCtx.getStringValue("date_start");
            String date_end = requestCtx.getStringValue("date_end");

            long recordsTotal = webDeliveryOrderService.count_search(0, order_id, ship_name, rev_name, mobile, user_id, date_start, date_end);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<WebExpressage>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<WebExpressage> result = webDeliveryOrderService.limit_search(start, length, 0, order_id, ship_name, rev_name, mobile, user_id, date_start, date_end);
            if (result != null) {
                for (WebExpressage order : result) {
                    {
                        City city = cityService.findOneWithCached(order.getProvince());
                        if (city != null) {
                            order.setProvinceStr(city.getName());
                        }
                    }

                    {
                        City city = cityService.findOneWithCached(order.getCity());
                        if (city != null) {
                            order.setCityStr(city.getName());
                        }
                    }

                    {
                        City city = cityService.findOneWithCached(order.getDistrict());
                        if (city != null) {
                            order.setDistrictStr(city.getName());
                        }
                    }
                    String goodName = webDeliveryGoodsService.getContactGoodName(order.getOrder_id());
                    order.setGood_name(goodName);

                    aj.add(order);
                }
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    /// 请求明细
    @RequestMapping(value="order_sub_table")
    @ResponseBody
    public AjaxResponse orderSubTable(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            AjaxResponse aj = new AjaxResponse<Integer>();

            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int sub_id = requestCtx.getInegerValue("sub_id");
            List<Integer> order_id = new ArrayList<Integer>();
            order_id.add(sub_id);
            List<DeliveryGoods> data = webDeliveryGoodsService.findDeliveryGoodsByOrderID(0, order_id);
            for (DeliveryGoods it : data) {
                aj.add(it);
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    /// 请求发货
    @RequestMapping(value="order_fa_huo")
    public String orderFaHuo(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int order_id = requestCtx.getInegerValue("order_id");
            WebExpressage exp = webDeliveryOrderService.loadByOrderID(order_id);
            if (exp == null) {
                //System.out.println(String.format("expressage_order_fa_huo order_id is %d", order_id));
                log.info("-----expressage_order_fa_huo order_id is {}", order_id);
                return Utility.server_error_html;
            }
            model.addAttribute("order", exp);
            return "expressage/order_fa_huo";
        } catch (ServerException e) {
            return Utility.server_error_html;
        }
    }

    /// 请求发货确认
    @RequestMapping(value="order_fa_huo_confirm")
    @ResponseBody
    public ResponseData orderFaHuoConfirm(HttpServletRequest request, @RequestParam Map<String,String> params, Model model) {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int order_id = requestCtx.getInegerValue("order_id");
            int user_id = requestCtx.getInegerValue("user_id");
            if (order_id != 0) {
                boolean success = webDeliveryOrderService.orderFaHuo(order_id, user_id, 1, 2, System.currentTimeMillis());
                if (success) {
                    ResponseData aj = new ResponseData();
                    /// 成功后从定向
                    aj.put("url", "/expressage/order");
                    return aj;
                }
            }
            return new ErrorResponse(ErrorCode.ERROR_UNKNOWN);
        } catch (Exception e) {
            //System.out.println(e);
            log.info("-----{}", e);
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    /***
     *
     *
     *
     *
     *
     */

    /// 请求删除订单
    @RequestMapping(value="order_delete")
    @ResponseBody
    public ResponseData orderDelete(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int order_id = requestCtx.getInegerValue("order_id");
            if (order_id != 0) {
                boolean success = webDeliveryOrderService.orderDelete(order_id);
                if (success) {
                    ResponseData aj = new ResponseData();
                    /// 成功后从定向
                    //aj.put("url", "/expressage/order");
                    return aj;
                }
            }
            return new ErrorResponse(ErrorCode.ERROR_UNKNOWN);
        } catch (Exception e) {
            //System.out.println(e);
            log.info("-----{}", e);
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    /***
     *
     *
     *
     *
     *
     *
     *
     */

    // 已发货订单
    @RequestMapping(value="order_delivery")
    public String orderDelivery(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "expressage/order_delivery";
    }

    @RequestMapping(value="order_delivery_table", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse orderDeliveryTableGet(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);

            long recordsTotal = webDeliveryOrderService.count(1);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<WebExpressage>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<WebExpressage> result = webDeliveryOrderService.limit(start, length, 1);
            if (result != null) {
                for (WebExpressage order : result) {
                    {
                        City city = cityService.findOneWithCached(order.getProvince());
                        if (city != null) {
                            order.setProvinceStr(city.getName());
                        }
                    }

                    {
                        City city = cityService.findOneWithCached(order.getCity());
                        if (city != null) {
                            order.setCityStr(city.getName());
                        }
                    }

                    {
                        City city = cityService.findOneWithCached(order.getDistrict());
                        if (city != null) {
                            order.setDistrictStr(city.getName());
                        }
                    }
                    aj.add(order);
                }
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    @RequestMapping(value="order_delivery_table", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse orderDeliveryTablePost(HttpServletRequest request, @RequestParam Map<String,String> params, Model model) {
        /*for (Map.Entry<String, String> it : params.entrySet()) {
            System.out.println(String.format("key:%s \t\t\t value:%s", it.getKey(), it.getValue()));
        }*/

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);
            int order_id = requestCtx.getInegerValue("input_orderid");
            String ship_name = requestCtx.getStringValue("input_shipname");
            String rev_name = requestCtx.getStringValue("input_revname");
            int mobile = requestCtx.getInegerValue("input_mobile");
            int user_id = requestCtx.getInegerValue("input_userid");
            String date_start = requestCtx.getStringValue("date_start");
            String date_end = requestCtx.getStringValue("date_end");

            long recordsTotal = webDeliveryOrderService.count_search(1, order_id, ship_name, rev_name, mobile, user_id, date_start, date_end);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<WebExpressage>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<WebExpressage> result = webDeliveryOrderService.limit_search(start, length, 1, order_id, ship_name, rev_name, mobile, user_id, date_start, date_end);
            if (result != null) {
                for (WebExpressage order : result) {
                    {
                        City city = cityService.findOneWithCached(order.getProvince());
                        if (city != null) {
                            order.setProvinceStr(city.getName());
                        }
                    }

                    {
                        City city = cityService.findOneWithCached(order.getCity());
                        if (city != null) {
                            order.setCityStr(city.getName());
                        }
                    }

                    {
                        City city = cityService.findOneWithCached(order.getDistrict());
                        if (city != null) {
                            order.setDistrictStr(city.getName());
                        }
                    }
                    aj.add(order);
                }
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }


    /***
     *
     *
     *
     *
     *
     *
     */
    // 待处理快递物品统计
    @RequestMapping(value="order_statis")
    public String orderStatis(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "expressage/order_statis";
    }

    @RequestMapping(value="order_statis_table")
    @ResponseBody
    public AjaxResponse orderStatisTableGet(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        /*for (Map.Entry<String, String> it : params.entrySet()) {
            System.out.println(String.format("key:%s \t\t\t value:%s", it.getKey(), it.getValue()));
        }*/

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);

            long recordsTotal = webDeliveryOrderService.goodcount();
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<WebExpressageGood>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<WebExpressageGood> result = webDeliveryOrderService.goodlimit(start, length);
            if (result != null) {
                for (WebExpressageGood order : result) {
                    aj.add(order);
                }
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    @RequestMapping(value = "order_statis_table_top10")
    @ResponseBody
    public ResponseData orderStatisTableTop10(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            long totoalsum = webDeliveryOrderService.goodsum();
            ResponseData aj = new ResponseData();

            List<WebExpressageGood> result = webDeliveryOrderService.goodlimit(0, 10);
            if (result != null) {
                int i = 1;
                for (WebExpressageGood order : result) {
                    aj.put("name"+i,order.getgood_name());
                    aj.put("sum"+i,order.getgood_sum());
                    i++;
                }
            }

            aj.put("totoalsum",totoalsum);
            return aj;
        }
        catch (ServerException e){
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    /***
     *
     *
     *
     */
    // 未处理订单回收站
    @RequestMapping(value="order_recycle")
    public String orderRecycle(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        // TODO
        //CityJson.getInstance().init(webDeliveryOrderService);
        return "expressage/order_recycle";
    }

    @RequestMapping(value="order_recycle_table", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse orderTableRecycleGet(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
       /* for (Map.Entry<String, String> it : params.entrySet()) {
            System.out.println(String.format("key:%s \t\t\t value:%s", it.getKey(), it.getValue()));
        }*/

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = DataTableServiceSide.start(requestCtx);
            int length = DataTableServiceSide.length(requestCtx);
            int draw = DataTableServiceSide.draw(requestCtx);

            long recordsTotal = webDeliveryOrderService.count(99);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<WebExpressage>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<WebExpressage> result = webDeliveryOrderService.limit(start, length, 99);
            if (result != null) {
                for (WebExpressage order : result) {
                    aj.add(order);
                }
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    /// 请求恢复订单
    @RequestMapping(value="order_recover")
    @ResponseBody
    public ResponseData orderRecover(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int order_id = requestCtx.getInegerValue("order_id");
            if (order_id != 0) {
                boolean success = webDeliveryOrderService.orderRecover(order_id);
                if (success) {
                    ResponseData aj = new ResponseData();
                    /// 成功后从定向
                    //aj.put("url", "/expressage/order");
                    return aj;
                }
            }
            return new ErrorResponse(ErrorCode.ERROR_UNKNOWN);
        } catch (Exception e) {
            //System.out.println(e);
            log.info("-----{}", e);
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }
}
