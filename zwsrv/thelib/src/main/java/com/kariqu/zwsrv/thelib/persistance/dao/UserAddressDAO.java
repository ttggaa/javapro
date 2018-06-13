package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.AccountLog;
import com.kariqu.zwsrv.thelib.persistance.entity.UserAddress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 04/11/17.
 */
public interface UserAddressDAO extends JpaRepository<UserAddress, Integer> {

    @Query("select l from UserAddress l where l.userId=:userId and l.deleted=0 order by createTime desc")
    public List<UserAddress> findAddressByUserId(@Param("userId") int userId);

    int countByUserId(int userId);

}
