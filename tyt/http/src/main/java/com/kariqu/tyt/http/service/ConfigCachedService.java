package com.kariqu.tyt.http.service;

import com.kariqu.tyt.common.persistence.entity.ConfigEntity;
import com.kariqu.tyt.common.persistence.service.ConfigService;
import com.kariqu.tyt.http.utility.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ConfigCachedService extends ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigCachedService.class);

    public ConfigCachedService() {
        lk = new ReentrantLock();
        cached = new HashMap<Integer, ConfigEntity>();
    }

    public void saveEntity(ConfigEntity entity) {
        getConfigRepository().save(entity);
    }

    public Date getValueAsDate(int id) {
        String val = getValue(id);
        if (val == null)
            return null;
        return LocalDateTimeUtils.stringToMysqlDatetime(val);
    }

    public String getValueAsString(int id) {
        return getValue(id);
    }

    public Integer getValueAsInteger(int id) {
        try {
            String val = getValue(id);
            if (val == null)
                return new Integer(0);
            return Integer.valueOf(val);
        } catch (Exception e) {
            return 0;
        }
    }

    public List<ConfigEntity> getAllClientUse() {
        List<ConfigEntity> all = new ArrayList<>();
        try {
            lk.lock();
            for (Map.Entry<Integer, ConfigEntity> kv : cached.entrySet()) {
                if (kv.getValue().getClientUse() != 0)
                    all.add(kv.getValue());
            }
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
        } finally {
            lk.unlock();
        }
        return all;
    }

    public void refreshCached() {
        List<ConfigEntity> all = getConfigRepository().findAll();
        try {
            lk.lock();
            cached = listToMap(all);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
        } finally {
            lk.unlock();
        }
    }

    private String getValue(int id) {
        try {
            lk.lock();
            ConfigEntity entity = cached.get(id);
            if (entity != null)
                return entity.getValue();
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            lk.unlock();
        }
    }

    private static Map<Integer, ConfigEntity> listToMap(List<ConfigEntity> configEntityList) {
        Map<Integer, ConfigEntity> map = new HashMap<>();
        for (ConfigEntity e : configEntityList) {
            map.put(e.getId(), e);
        }
        return map;
    }

    private ReentrantLock lk;
    private Map<Integer, ConfigEntity> cached;
}
