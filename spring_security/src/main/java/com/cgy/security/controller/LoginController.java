package com.cgy.security.controller;

import com.cgy.security.utility.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login_get(HttpServletRequest request , @RequestParam Map<String,String> params, Model model)
    {
        logger.warn("xxxxxxxxxxxxxxxx-> get");
        return "login";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login_post(HttpServletRequest request , @RequestParam Map<String,String> params, Model model)
    {
        logger.info("xxxxxxxxxxxxxx -> redirect:index");
        return "redirect:index";
    }

    @RequestMapping(value="index")
    //@PreAuthorize("hasRole('USER')")
    public String index(HttpServletRequest request , @RequestParam Map<String,String> params, Model model)
    {
        logger.info("xxxxxxxxxxxxxx-> index");
        return "index";

    }

    @RequestMapping(value="sign_in")
    //@ResponseBody
    //@PreAuthorize("hasRole('USER')")
    public String sign_in(HttpServletRequest request , @RequestParam Map<String,String> params, Model model)
    {
        /*
        if (null != request.getSession().getAttribute("admin")) {
            ResponseData aj = new ResponseData();
            aj.put("url", "/login/index");
            return aj;
        }
        */
        logger.info("xxxxxxxxxxxxxx -> redirect:index");

        return "redirect:index";

        /*
        try {
            ResponseData aj = new ResponseData();
                //request.getSession().setAttribute("admin", account);
                //request.getSession().setMaxInactiveInterval(3600);
                /// 成功后从定向
            aj.put("url", "/index");
            return aj;
            //return ResponseData.Success;
        } catch (Exception e) {
            //System.out.println(e);
            logger.info("-----{}", e);
            return new ResponseData(100, "xxxxx error");
            //return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        */
    }
}
