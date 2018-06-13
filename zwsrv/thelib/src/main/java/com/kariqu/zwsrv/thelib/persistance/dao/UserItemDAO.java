package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.UserItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserItemDAO extends JpaRepository<UserItem, Integer> {

    /*
    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemType in :type_array")
    public List<UserItem> findAllByTypeList(@Param("userId") int userId, @Param("type_array") Integer[] type_array);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemType = :type")
    public List<UserItem> findAllByType(@Param("userId") int userId, @Param("type") int type);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemType in :type_array")
    public List<UserItem> findAllCanExpress(@Param("type") int userId,  @Param("type_array") Integer[] type_array);
    */

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemClassify = :classify")
    public List<UserItem> findAllByClassify(@Param("userId") int userId, @Param("classify") int classify);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemClassify = :classify"
            + " and l.itemType = :itemType")
    public List<UserItem> findAllByClassify(@Param("userId") int userId, @Param("classify") int classify
            , @Param("itemType") int itemType);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemClassify = :classify"
            + " and l.itemType = :itemType and :tnow < l.createTime + :tm order by l.createTime")
    public List<UserItem> findAllByClassify_CanMerge(@Param("userId") int userId, @Param("classify") int classify
            , @Param("itemType") int itemType, @Param("tm") long storage_tm, @Param("tnow") long tnow);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemClassify in :classify_list")
    public List<UserItem> findAllByClassify(@Param("userId") int userId, @Param("classify_list") Integer[] classify
            , Pageable pageable);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemClassify = :classify")
    public List<UserItem> findAllByClassify(@Param("userId") int userId, @Param("classify") Integer classify
            , Pageable pageable);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemClassify = :classify "
            + " and l.itemId in :id_array")
    public List<UserItem> findAllByClassify(@Param("userId") int userId, @Param("classify") int classify
            , @Param("id_array") Integer[] id_array);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemId = :id")
    public UserItem findUserItem(@Param("userId") int userId, @Param("id") int id);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemId in :id_array")
    public List<UserItem> findUserItemList(@Param("userId") int userId, @Param("id_array") Integer[] id_array);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId")
    public List<UserItem> findUserItemList(@Param("userId") int userId, Pageable pageable);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemClassify = :classify "
            + " and l.createTime + :tm < :tnow")
    public List<UserItem> findExpiredFragmentList(@Param("userId") int userId, @Param("classify") int classify
            , @Param("tm") long tm, @Param("tnow") long tnow, Pageable pageable);

    @Query("select l from UserItem l where l.isValid = 1 and l.userId = :userId and l.itemType = :itemType "
            + " and :tnow < l.createTime + :tm")
    public List<UserItem> findUnexpiredFragmentList(@Param("userId") int userId, @Param("itemType") int itemType
            , @Param("tm") long tm, @Param("tnow") long tnow);

}
