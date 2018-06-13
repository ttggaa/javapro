package com.cgy.test.demo.persistence.service;


import com.cgy.test.demo.persistence.dao.UserBasicDao;
import com.cgy.test.demo.persistence.daoweb.UserBasicDaoweb;
import com.cgy.test.demo.persistence.entity.UserBasicEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class UserBasicService {
    private static final Logger logger = LoggerFactory.getLogger(UserBasicService.class);

    @Autowired
    private UserBasicDao userBasicDao;

    @Autowired
    UserBasicDaoweb userBasicDaoweb;

    @Autowired
    @Qualifier("mywebEntityManager")
    private EntityManager entityManager;

    public UserBasicEntity save(UserBasicEntity entity) {
        logger.info("xxxxx entityManager: {}", entityManager != null);

        //return userBasicDaoweb.save(entity);
        return userBasicDao.save(entity);

        //return userBasicDao.save(entity);
    }
}
