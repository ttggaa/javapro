package com.cgy.test.demo.controller;


import com.cgy.test.demo.persistence.dao.UserBasicDao;
import com.cgy.test.demo.persistence.daoweb.UserBasicDaoweb;
import com.cgy.test.demo.persistence.entity.UserBasicEntity;
import com.cgy.test.demo.persistence.service.UserBasicService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    public static class Req {
        String name;
    }

    @Autowired
    private UserBasicService userBasicService;
    //@Autowired
    //@Qualifier("mytest")
    //private UserBasicDao userBasicDao;

    @Autowired
    private UserBasicDao dao;


    @Autowired
    private UserBasicDaoweb daoWeb;

    @PostMapping(value = "/save1")
    public @ResponseBody String save1(@RequestBody(required = false) String body) {
        if (body == null)
            return "error";
        Gson gson = new Gson();
        Req req = gson.fromJson(body, Req.class);
        if (req.name == null || req.name.isEmpty())
            return "error 2";
        if (userBasicService == null) {
            return "null";
        }

        UserBasicEntity entity = new UserBasicEntity();
        entity.setNickname(req.name);
        entity = userBasicService.save(entity);
        return Integer.toString(entity.getUserId());
    }

    @PostMapping(value = "/save2")
    public @ResponseBody String save2(@RequestBody(required = false) String body) {
        if (body == null)
            return "error";
        Gson gson = new Gson();
        Req req = gson.fromJson(body, Req.class);
        if (req.name == null || req.name.isEmpty())
            return "error 2";
        return "success";
    }

    @PostMapping(value = "/save3")
    public @ResponseBody String save3(@RequestBody(required = false) String body) {

        try {
            if (body == null)
                return "error";
            Gson gson = new Gson();
            Req req = gson.fromJson(body, Req.class);
            if (req.name == null || req.name.isEmpty())
                return "error 2";

            UserBasicEntity entity = new UserBasicEntity();
            entity.setNickname(req.name);

            logger.info("save dao");
            dao.save(entity);

            UserBasicEntity entity2 = new UserBasicEntity();
            entity2.setNickname(req.name);
            daoWeb.save(entity2);

            return "success";
        } catch (Exception e) {
            logger.warn("exception {}", e.toString());
        }
        return "fail";
    }

}
