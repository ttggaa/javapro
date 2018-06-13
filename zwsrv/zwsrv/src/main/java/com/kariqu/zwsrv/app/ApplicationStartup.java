package com.kariqu.zwsrv.app;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 22/07/17.
 */
@Service
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

//    @Autowired
//    RongCloudService rongCloudService;

//    @Autowired
//    UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        if (rongCloudService!=null) {
//            rongCloudService.requestToken(-1001,"系统消息","");
//        }
//        userService = contextRefreshedEvent.getApplicationContext().getAutowireCapableBeanFactory().getBean(UserService.class);
//        userService.changeSet();

    }
}

//https://www.cnblogs.com/zhangwufei/p/7017325.html

//http://blog.csdn.net/hhaojian/article/details/79243763

//http://blog.csdn.net/u013013170/article/details/79209444

//ALTER DATABASE `zww` CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
//use zww
//ALTER TABLE `zww_users` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
//ALTER TABLE `zww_users` MODIFY COLUMN `nickname`  varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '';

//ALTER TABLE `zww_game` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
//ALTER TABLE `zww_game` MODIFY COLUMN `player_name`  varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '';