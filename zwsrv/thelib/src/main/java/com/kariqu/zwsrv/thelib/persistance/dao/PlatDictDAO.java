package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.AccountLog;
import com.kariqu.zwsrv.thelib.persistance.entity.PlatDict;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 21/12/17.
 */
public interface PlatDictDAO extends JpaRepository<PlatDict, Integer> {

    @Query("select l from PlatDict l where l.name in :names")
    public List<PlatDict> findByName(@Param("names") String[] names);

    @Query("select l from PlatDict l where l.isPlatConfig=1")
    public List<PlatDict> findAllPlatConfig();

}

