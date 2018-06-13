package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 25/11/16.
 */
public interface CityDAO extends JpaRepository<City, Integer> {

}

