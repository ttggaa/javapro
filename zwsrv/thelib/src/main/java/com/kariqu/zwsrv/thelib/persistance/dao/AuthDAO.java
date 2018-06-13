package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 6/15/16.
 */
public interface AuthDAO extends JpaRepository<Auth, Integer> {

    public List<Auth> findAllByAuthType(String authType);

    @Query("select l from Auth l where l.appId = :appId  and l.authType = :authType and l.identifier = :identifier")
    public Auth findByAuthTypeAndIdentifier(@Param("appId") String appId, @Param("authType") String authType, @Param("identifier") String identifier);

    @Query("select l from Auth l where l.userId = :userId")
    public List<Auth> findAllByUserId(@Param("userId") int userId);

//    public User findOneByUserId(int user_id);
//
//    public User findOneByUserName(String user_name);
//
//    public User findOneByPhoneNumber(String phone_number);
//
//    @Query("select u from User u where u.userName = ?1 and u.password = ?2 and deleted = 0")
//    public User findByUserNameAndPassword(String user_name, String password);
//
//    @Query("select u from User u where u.phoneNumber = ?1 and u.password = ?2 and deleted = 0")
//    public User findByPhoneNumberAndPassword(String phoneN_number, String password);

}
