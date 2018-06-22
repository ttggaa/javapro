package com.kariqu.tyt.common.persistence.service;

import com.kariqu.tyt.common.persistence.entity.SignInEntity;
import com.kariqu.tyt.common.persistence.repository.SignInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SignInService {

    @Autowired
    private SignInRepository signInRepository;

    public SignInRepository getSignInRepository() {
        return signInRepository;
    }

    public SignInEntity findEntity(int val) {
        Optional<SignInEntity> opt = getSignInRepository().findById(val);
        if (opt == null)
            return null;
        return opt.get();
    }
}
