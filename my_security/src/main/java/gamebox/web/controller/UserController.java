package gamebox.web.controller;


import gamebox.web.model.UserModel;
import gamebox.web.persistence.entity.UserEntity;
import gamebox.web.persistence.service.UserService;
import gamebox.web.utility.DataTableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "user")
    public String user(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        return "/user/user";
    }

    @RequestMapping(value = "user_table")
    @ResponseBody
    public DataTableResponse user_table(HttpServletRequest request,
                             @RequestParam Map<String,String> params, Model model)
    {
        /*
        for (Map.Entry<String, String> kv : params.entrySet()) {
            logger.warn("user_table {} {}", kv.getKey(), kv.getValue());
        }
        */

        PageRequest pageRequest = PageRequest.of(0, 1, Sort.Direction.DESC, "user_id");
        List<UserEntity> userEntityList = userService.findAllUser(pageRequest);
        try {
            int start = Integer.valueOf(request.getParameter("start"));
            int length = Integer.valueOf(request.getParameter("length"));
            int draw = Integer.valueOf(request.getParameter("draw"));

            long recordsTotal = userService.count();
            long recordsFiltered = recordsTotal;

            DataTableResponse resp = new DataTableResponse<UserModel>();
            resp.setRecordsTotal(recordsTotal);
            resp.setRecordsFiltered(recordsFiltered);
            resp.setDraw(draw);

            for (UserEntity userEntity : userEntityList) {
                resp.add(new UserModel(userEntity));
            }
            return resp;
        } catch (Exception e) {
        }
        return new DataTableResponse();
    }

}
