package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.model.PaginationRequestData;
import com.kariqu.zwsrv.app.pay.tenpay.Util;
import com.kariqu.zwsrv.app.utility.Utility;
import com.kariqu.zwsrv.thelib.persistance.entity.UserItem;
import com.kariqu.zwsrv.thelib.persistance.service.UserItemService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserItemServiceCache extends UserItemService {

    public static class FragmentAggregate
    {
        private int itemType;
        private int count;          // 拥有数量

        public FragmentAggregate()
        {
            this.itemType = 0;
            this.count = 0;
        }

        public int getItemType() { return itemType; }
        public void setItemType(int itemType) { this.itemType = itemType; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }

    @PersistenceContext
    protected EntityManager entityManager;

    // 获取一个道具
    public UserItem findById(int userId, int id)
    {
        return userItemDAO.findUserItem(userId, id);
    }

    // 获取娃娃碎片
    public List<UserItem> findFragmentList(int userId)
    {
        List<UserItem> list = userItemDAO.findAllByClassify(userId, ItemServiceCache.CLASSIFY_FRAGMENT);
        if (list == null)
            return new ArrayList<>();
        return list;
    }

    // 获取娃娃碎片
    public List<UserItem> findFragmentListCanMerge(int userId, int itemType, long storage_tm, long tnow)
    {
        List<UserItem> list = userItemDAO.findAllByClassify_CanMerge(userId, ItemServiceCache.CLASSIFY_FRAGMENT
                , itemType, storage_tm, tnow);
        if (list == null)
            return new ArrayList<>();
        return list;
    }

    // 获取娃娃碎片
    public List<UserItem> findFragmentList(int userId, PaginationRequestData paginationRequest)
    {
        List<UserItem> list = userItemDAO.findAllByClassify(userId, ItemServiceCache.CLASSIFY_FRAGMENT
                , Utility.createPageable(paginationRequest));
        if (list == null)
            return new ArrayList<>();
        return list;
    }

    // 获取娃娃碎片
    public List<UserItem> findFragmentListByType(int userId, int itemType)
    {
        List<UserItem> list = userItemDAO.findAllByClassify(userId, ItemServiceCache.CLASSIFY_FRAGMENT, itemType);
        if (list == null)
            return new ArrayList<>();
        return list;
    }

    // 获取娃娃碎片
    public List<UserItem> findFragmentList(int userId, Integer[] id_array)
    {
        List<UserItem> list = userItemDAO.findAllByClassify(userId, ItemServiceCache.CLASSIFY_FRAGMENT, id_array);
        if (list == null)
            return new ArrayList<>();
        return list;
    }

    public List<FragmentAggregate> findFragmentAggregate(int userId, long interval, long tnow, PaginationRequestData paginationRequest)
    {
        String sql = "select item_type, count(item_type) from zww_user_item "
                + " where is_valid = 1 and user_id = ? and item_classify = ? and createtime + ? > ? "
                + " group by item_type limit ?, ?";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userId);
        query.setParameter(2, ItemServiceCache.CLASSIFY_FRAGMENT);
        query.setParameter(3, interval);
        query.setParameter(4, tnow);
        query.setParameter(5,paginationRequest.getPage());
        query.setParameter(6,paginationRequest.getSize());
        return parseFragmentAggregate(query.getResultList());
    }

    // 获取有效的娃娃碎片
    public List<UserItem> findUnexpiredFragmentList(int userId, int itemType, long interval, long tnow)
    {
        List<UserItem> list = userItemDAO.findUnexpiredFragmentList(userId, itemType, interval, tnow);
        if (list == null)
            return new ArrayList<>();
        return list;
    }

    // 获取过期的碎片
    public List<UserItem> findExpiredFragment(int userId, long interval, long tnow, PaginationRequestData paginationRequest)
    {
        return userItemDAO.findExpiredFragmentList(userId, ItemServiceCache.CLASSIFY_FRAGMENT
                , interval, tnow, Utility.createPageable(paginationRequest));
    }

    public List<UserItem> findBagList(int userId, PaginationRequestData paginationRequest)
    {
        List<UserItem> list = userItemDAO.findAllByClassify(userId, ItemServiceCache.getBagClassify()
                , Utility.createPageable(paginationRequest));
        if (list == null)
            return new ArrayList<>();
        return list;
    }

    public void UpdateExchangeCoinsStatus(int item_id, int status_value, int coins)
    {
        String sql = "update zww_user_item set exchange_coins_status = ?, exchange_coins_num = ? where item_id = ? ";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, status_value);
        query.setParameter(2, coins);
        query.setParameter(3, item_id);
        query.executeUpdate();
    }

    public void UpdateExpressStatus(int item_id, int status_value, int orderId)
    {
        String sql = "update zww_user_item set express_status = ?, order_id = ?, express_tm = NOW() where item_id = ? ";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, status_value);
        query.setParameter(2, orderId);
        query.setParameter(3, item_id);
        query.executeUpdate();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static List<FragmentAggregate> parseFragmentAggregate(List rows)
    {
        List<FragmentAggregate> ret = new ArrayList<FragmentAggregate>();
        for (Object row : rows) {
            FragmentAggregate record = parseFragmentAggregateOne(row);
            if (record != null) {
                ret.add(record);
            }
        }
        return ret;
    }

    private static FragmentAggregate parseFragmentAggregateOne(Object row)
    {
        if (row == null)
            return null;
        FragmentAggregate record = new FragmentAggregate();
        Object[] cells = (Object[]) row;
        record.setItemType((int)cells[0]);
        record.setCount(((BigInteger)cells[1]).intValue());
        return record;
    }
}
