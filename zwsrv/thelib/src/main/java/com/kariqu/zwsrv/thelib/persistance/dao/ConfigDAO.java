package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 26/11/17.
 */
public interface ConfigDAO extends JpaRepository<Config, Integer> {

    @Query("select l from Config l where l.module=:module and l.valid=1 order by sort desc")
    public List<Config> findAllValidByModule(@Param("module") String module);

    @Query("select l from Config l where l.module=:module order by sort desc")
    public List<Config> findAllByModule(@Param("module") String module);
}
