package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.AuthDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 18/04/17.
 */
@Service
public class AuthService extends BaseService<Auth> {

    @Autowired
    private AuthDAO authDAO;

    @Override
    protected JpaRepository<Auth, Integer> getDao() {
        return authDAO;
    }


    public Auth findByAuthTypeAndIdentifier(String appId, String authType, String identifier) {
        return authDAO.findByAuthTypeAndIdentifier(appId,authType,identifier);
    }

    public List<Auth> findAllByUserId(int userId) {
        return authDAO.findAllByUserId(userId);
    }

}
