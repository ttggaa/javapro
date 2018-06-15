package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.web.persistance.entityex.WebAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class WebAccountService {
    @PersistenceContext
    protected EntityManager entityManager;

    public boolean login(String account, String password) {
        String sql = "select * from web_account where account = ?;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, account);

        List l = query.getResultList();
        if (l.size() == 0)
            return false;

        WebAccount ret = WebAccount.parseOneRow(l.get(0));
        if (ret.getPassword().equals(password))
            return true;
        else
            return false;
    }

    public long count() {
        String sql = "select count(1) from web_account;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);

        BigInteger v = (BigInteger) query.getResultList().get(0);
        return v.longValue();
    }

    public List<WebAccount> limit(int start, int count) {
        String sql = "select * from web_account" +
                " order by account limit ?, ?;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, start);
        query.setParameter(2, count);
        return WebAccount.parseRowList(query.getResultList());
    }

    @Transactional
    public boolean add(String account, String password, String remark) {
        String sql = "insert into web_account(`account`,`password`,`remark`)" +
                "value(?,?,?);";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, account);
        query.setParameter(2, password);
        query.setParameter(3, remark);

        int ret = query.executeUpdate();
        if (ret > 0)
            return true;
        return false;
    }
}
