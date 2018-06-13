package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.CityDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by simon on 25/11/16.
 */

@Service
public class CityService extends BaseService<City> {

    @Autowired
    CityDAO cityDAO;

    @Override
    protected JpaRepository<City, Integer> getDao() {
        return cityDAO;
    }

    Map<Integer,City> cityMap = new ConcurrentHashMap<Integer,City>();

    public City findOneWithCached(Integer id) {
        City city = cityMap.get(id);
        if (city==null) {
            city = super.findOne(id);
            if (city!=null) {
                cityMap.put(id,city);
            }
        }
        return city;
    }

    public String getCityNameWithCached(Integer id) {
        City city = findOneWithCached(id);
        if (city!=null) {
            return city.getName();
        }
        return "";
    }
}
