package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.base.controller.BaseController;
import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import com.kariqu.zwsrv.thelib.persistance.service.ConfigService;
import com.kariqu.zwsrv.web.persistance.entityex.WebExpressageGood;
import com.kariqu.zwsrv.web.persistance.service.WebDeliveryOrderService;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
@RestController
@RequestMapping("/test")
public class MyTest extends BaseController {

    @Autowired
    private ConfigService configService;
    @Autowired
    WebDeliveryOrderService webDeliveryOrderService;

    @ResponseBody
    @RequestMapping(value = "/x")
    public String test() {
        System.out.println("-----------------xxxxxxxx");
        String str = "";
        List<Config> conf_list = configService.findAllByModule("home");
        for (Config c : conf_list) {
            str = c.getData();
        }
        if (str.isEmpty())
            str = "xxx";
        return str;
    }


}*/

@Controller
@RequestMapping("/expressage")
public class MyTest {
    @Autowired
    WebDeliveryOrderService webDeliveryOrderService;

    @RequestMapping(value = "test")
    public String xx(HttpServletRequest request, @RequestParam Map<String, String> params, Model model) {
        return "expressage/test";
    }

    @RequestMapping(value = "test_pie", method = RequestMethod.GET)
    @ResponseBody
    public List<WebExpressageGood> selectGood() {
        List<WebExpressageGood> result = webDeliveryOrderService.goodlimit(1, 100);
        return result;
    }
}