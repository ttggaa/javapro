package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.UserSigninDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.UserSignin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserSigninService extends BaseService<UserSignin> {

    @Autowired
    protected UserSigninDAO userSigninDAO;

    @Override
    protected JpaRepository<UserSignin, Integer> getDao() {
        return userSigninDAO;
    }

    public boolean saveUserSignin(UserSignin signin)
    {
        try {
            getDao().save(signin);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UserSignin loadUserSignin(int user_id)
    {
        return userSigninDAO.findOne(user_id);
    }
}
