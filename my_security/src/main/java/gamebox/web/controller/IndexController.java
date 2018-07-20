package gamebox.web.controller;


import gamebox.web.model.Response;
import gamebox.web.model.ResponseError;
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
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "index")
    //@PreAuthorize("hasRole('BOSS')")
    public String index(HttpServletRequest request)
    {
        return "index";
    }

    @RequestMapping(value = "login")
    public String login(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        return "login";
    }

    @RequestMapping(value = "logout")
    public String logOut(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        request.getSession().removeAttribute("admin");
        return "login";
    }

    @RequestMapping(value="sign_in", method = RequestMethod.POST)
    @ResponseBody
    public Response sign_in(HttpServletRequest request, Model model
            , @RequestParam(name = "username") String username
            , @RequestParam(name = "password") String password) {
        try {
            if (username.isEmpty() || password.isEmpty()) {
                return ResponseError.ERROR_PASSWORD;
            }
            return new Response().put("url", "/index");
        } catch (Exception e) {
            logger.info("-----{}", e);
            return ResponseError.ERROR_UNKNOWN;
        }
    }
}
