package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.UserDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 18/04/17.
 */
@Service
public class UserService extends BaseService<User> {

    @Autowired
    private UserDAO userDAO;

    @Override
    protected JpaRepository<User, Integer> getDao() {
        return userDAO;
    }


    public User findByUnionId(String unionId) {
        return userDAO.findOneByUnionId(unionId);
    }

    public User findOneByInvitationCode(String invitationCode) {
        return userDAO.findOneByInvitationCode(invitationCode);
    }

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public void changeSet(){
//        jdbcTemplate.update("set names utf8mb4;");
//    }

}
