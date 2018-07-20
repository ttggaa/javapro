package gamebox.web.security;

import com.google.gson.Gson;
import gamebox.web.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            logger.info("auth: {}", auth.getAuthority());
        }

        String username = request.getParameter("username");
        request.getSession().setAttribute("auth", authentication);
        request.getSession().setAttribute("username", username);

        Map<String, Boolean> menu = new HashMap<>();
        menu.put("user", true);
        menu.put("game", true);
        request.getSession().setAttribute("menu", menu);

        response.setCharacterEncoding("UTF8");
        response.setContentType("application/json");
        Response resp = new Response().put("url", "/index");

        Gson gson = new Gson();
        String str = gson.toJson(resp);
        response.getWriter().write(str);
    }
}
