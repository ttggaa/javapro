package com.kariqu.tyt.common.persistence.repository;

import com.kariqu.tyt.common.persistence.entity.SignInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface SignInRepository extends JpaRepository<SignInEntity, Integer> {
}
