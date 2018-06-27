package com.cgy.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping(value = "test")
    @PreAuthorize("hasRole('ADMIN')")
    public Object test(@RequestParam(required = false) Map<String, String> reqParam) {

        for (Map.Entry<String, String> kv : reqParam.entrySet()) {
            logger.info("key: {}   value: {}", kv.getKey(), kv.getValue());
        }

        return new String("aaaa");
    }
}
