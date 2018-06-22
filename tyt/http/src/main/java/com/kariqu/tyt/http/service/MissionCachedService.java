package com.kariqu.tyt.http.service;

import com.kariqu.tyt.common.persistence.entity.MissionEntity;
import com.kariqu.tyt.common.persistence.service.MissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class MissionCachedService extends MissionService {
    private static final Logger logger = LoggerFactory.getLogger(MissionCachedService.class);

    public MissionCachedService() {
        lk = new ReentrantLock();
        cached = new HashMap<>();
        missionTypeMap = new HashMap<>();
    }

    public List<MissionEntity> randomMission(int count) {
        List<Integer> allType = new ArrayList<>();
        List<MissionEntity> entityList = new ArrayList<>();
        try {
            lk.lock();

            // 获取type
            for (Map.Entry<Integer, List<MissionEntity>> kv : missionTypeMap.entrySet()) {
                allType.add(kv.getKey());
            }
            Collections.shuffle(allType);

            for (int i = 0; i != allType.size(); ++i) {
                if (i == count)
                    break;
                int type = allType.get(i);
                List<MissionEntity> list = missionTypeMap.get(type);
                Collections.shuffle(list);
                entityList.add(list.get(0));
            }
            return entityList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            lk.unlock();
        }
    }

    public MissionEntity findMission(int mission_id) {
        try {
            lk.lock();
            return cached.get(mission_id);
        } catch (Exception e) {
            return null;
        } finally {
            lk.unlock();
        }
    }

    public void refreshCached() {
        List<MissionEntity> all = getMissionRepository().findAll();
        try {
            lk.lock();
            listToMap(all);
        } catch (Exception e) {
            logger.warn("exception: {}", e.toString());
        } finally {
            lk.unlock();
        }
    }

    private void listToMap(List<MissionEntity> configEntityList) {
        cached.clear();
        missionTypeMap.clear();
        for (MissionEntity e : configEntityList) {
            cached.put(e.getMissionId(), e);
            List<MissionEntity> list = missionTypeMap.get(e.getType());
            if (list == null) {
                list = new ArrayList<>();
                missionTypeMap.put(e.getType(), list);
            }
            list.add(e);
        }
    }

    private ReentrantLock lk;
    private Map<Integer, MissionEntity> cached;
    private Map<Integer, List<MissionEntity>> missionTypeMap;
}
