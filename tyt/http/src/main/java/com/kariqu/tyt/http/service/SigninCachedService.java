package com.kariqu.tyt.http.service;


import com.kariqu.tyt.common.persistence.entity.ConfigEntity;
import com.kariqu.tyt.common.persistence.entity.SignInEntity;
import com.kariqu.tyt.common.persistence.service.SignInService;
import com.kariqu.tyt.http.utility.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class SigninCachedService extends SignInService {

    private static final Logger logger = LoggerFactory.getLogger(SigninCachedService.class);

    public SigninCachedService() {
        lk = new ReentrantLock();
        cachedMap = new HashMap<Integer, SignInEntity>();
    }

    public SignInEntity findEntity(int val) {
        try {
            lk.lock();
            return cachedMap.get(val);
        } catch (Exception e) {
            return null;
        } finally {
            lk.unlock();
        }
    }

    public List<SignInEntity> findAll() {
        List<SignInEntity> cp = new ArrayList<>();
        try {
            lk.lock();
            for (Map.Entry<Integer, SignInEntity> kv : cachedMap.entrySet()) {
                cp.add(kv.getValue());
            }
        } catch (Exception e) {

        } finally {
             lk.unlock();
        }
        return cp;
    }

    public void refreshCached() {
        List<SignInEntity> all = getSignInRepository().findAll();
        try {
            lk.lock();
            cachedMap = listToMap(all);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
        } finally {
            lk.unlock();
        }
    }

    private static Map<Integer, SignInEntity> listToMap(List<SignInEntity> list) {
        Map<Integer, SignInEntity> map = new HashMap<>();
        for (SignInEntity e : list) {
            map.put(e.getId(), e);
        }
        return map;
    }

    private ReentrantLock lk;
    private Map<Integer, SignInEntity> cachedMap;
}
