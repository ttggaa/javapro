package gamebox.web.controller;


import gamebox.web.model.Response;
import gamebox.web.model.ResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping(value = "/game")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @RequestMapping(value = "game")
    @PreAuthorize("hasAnyAuthority('admin', 'guest')")
    public String game(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        HttpSession session = request.getSession();
        logger.info("session : {}", session.getId());

        Authentication auth = (Authentication)request.getSession().getAttribute("auth");
        if (auth == null) {
            logger.warn("auth is null");
        } else {
            for (GrantedAuthority it : auth.getAuthorities()) {
                logger.info("auth: {}", it.getAuthority());
            }
        }

        String aaa = (String)request.getSession().getAttribute("a");
        if (aaa == null) {
            logger.warn("aaa is null");
        } else {
            logger.info("aaa is {}", aaa);
        }

        return "/game/game";
    }
}
