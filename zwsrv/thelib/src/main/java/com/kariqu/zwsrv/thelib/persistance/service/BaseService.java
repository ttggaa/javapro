package com.kariqu.zwsrv.thelib.persistance.service;


import com.kariqu.zwsrv.thelib.base.entity.BaseEntity;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by simon on 20/11/15.
 */
public abstract class BaseService<T extends BaseEntity> {
    static {
        // date
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
        ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
        ConvertUtils.register(new SqlTimeConverter(null), Time.class);
        ConvertUtils.register(new SqlTimestampConverter(null), Timestamp.class);
        // number
        ConvertUtils.register(new BooleanConverter(null), Boolean.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new FloatConverter(null), Float.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);
    }

    /**
     * 获取相关的Repository
     *
     * @return
     */
    protected abstract JpaRepository<T, Integer> getDao();

//    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    public <S extends T> S save(S entity) {
        return getDao().save(entity);
    }

//    @Transactional(readOnly = false, rollbackFor={ Exception.class},isolation = Isolation.READ_COMMITTED)
    public <S extends T> List<S> save(Iterable<S> entities) {
        return getDao().save(entities);
    }

    public T findOne(Integer id) {
        return getDao().findOne(id);
    }

    public List<T> findAll() {
        return getDao().findAll();
    }

    public List<T> findAll(Iterable<Integer> ids) {
        return getDao().findAll(ids);
    }

    public Long count() {
        return getDao().count();
    }

//    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    public void delete(T entity) {
        getDao().delete(entity);
    }

//    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    public void delete(Integer id) {
        getDao().delete(id);
    }

//    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    public void delete(Iterable<? extends T> entities) {
        getDao().delete(entities);
    }

    public boolean exists(Integer id) {
        return getDao().exists(id);
    }

    public void flush() {
        getDao().flush();
    }
}
