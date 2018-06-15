package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.persistance.entity.Shipping;
import com.kariqu.zwsrv.thelib.persistance.service.ShippingService;
import com.kariqu.zwsrv.web.utilityex.AjaxResponse;
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
@RequestMapping(value = "/expressage")
public class ExpressageNameController {

    @Autowired
    ShippingService shippingService;

    @RequestMapping(value="name")
    public String name(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "expressage/name";
    }

    @RequestMapping(value="name_table")
    @ResponseBody
    public AjaxResponse nameTable(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        /*for (Map.Entry<String, String> it : params.entrySet()) {
            System.out.println(String.format("key:%s \t\t\t value:%s", it.getKey(), it.getValue()));
        }*/

        try {
            //HttpRequestContext requestCtx = new HttpRequestContext(request, params);

            List<Shipping> all_shipping = shippingService.findAllShippings();
            AjaxResponse aj = new AjaxResponse<Shipping>();
            for (int i = 0; i != all_shipping.size(); ++i) {
                aj.add(all_shipping.get(i));
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }
}
