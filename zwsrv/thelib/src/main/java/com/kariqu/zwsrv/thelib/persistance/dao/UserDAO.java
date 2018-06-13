package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 4/26/16.
 */
public interface UserDAO extends JpaRepository<User, Integer> {

//    public User findOneByUserId(int user_id);
//
    public User findOneByUnionId(String unionId);

    public User findOneByInvitationCode(String invitationCode);

//
//    public User findOneByPhoneNumber(String phone_number);
//
//    @Query("select u from User u where u.userName = ?1 and u.password = ?2 and deleted = 0")
//    public User findByUserNameAndPassword(String user_name, String password);
//
//    @Query("select u from User u where u.phoneNumber = ?1 and u.password = ?2 and deleted = 0")
//    public User findByPhoneNumberAndPassword(String phoneN_number, String password);


}