package com.kariqu.zwsrv.app.service;


import com.kariqu.zwsrv.app.error.ErrorDefs;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.persistance.entity.*;
import com.kariqu.zwsrv.thelib.persistance.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.work.glassfish.GlassFishWorkManagerTaskExecutor;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BagService {

    private Map.Entry<Integer, Integer> pair;

    public class DeliveryResult
    {
        public ErrorCode.ErrorEntry ret;
        public DeliveryOrder deliveryOrder;

        public DeliveryResult(ErrorCode.ErrorEntry error_code, DeliveryOrder deliveryOrder)
        {
            this.ret = error_code;
            this.deliveryOrder = deliveryOrder;
        }

        public DeliveryResult(ErrorCode.ErrorEntry error_code)
        {
            this.ret = error_code;
            this.deliveryOrder = null;
        }

        public DeliveryResult()
        {
            this.ret = ErrorCode.ERROR_UNKNOWN;
            this.deliveryOrder = null;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(BagService.class);

    @Autowired
    private DeliveryGoodsService deliveryGoodsService;

    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private AccountBusinessService accountBusinessService;

    @Autowired
    private PlatDictServiceCache platDictServiceCache;

    @Autowired
    private UserItemServiceCache userItemServiceCache;

    @Autowired
    private ItemPresentLogServiceCache itemPresentLogServiceCache;

    @Autowired
    private RoomServiceCache roomServiceCache;

    @Autowired
    private ItemServiceCache itemServiceCache;

    @Autowired
    private MergeCostFragmentLogServiceCache mergeCostFragmentLogServiceCache;

    @Autowired
    private MergeItemLogServiceCache mergeItemLogServiceCache;

    @Autowired
    private GameServiceCache gameServiceCache;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AccountService getAccountService()
    {
        return getAccountBusinessService().getAccountService();
    }

    public UserAddressService getUserAddressService() {
        return userAddressService;
    }

    public AccountBusinessService getAccountBusinessService() {
        return accountBusinessService;
    }

    public ItemServiceCache getItemServiceCache() {
        return itemServiceCache;
    }

    public UserItemServiceCache getUserItemServiceCache() {
        return userItemServiceCache;
    }

    public PlatDictServiceCache getPlatDictServiceCache() {
        return platDictServiceCache;
    }

    public long getGoodsMaxStorageTime() { return platDictServiceCache.getGoodsMaxStorageTime(); }

    public MergeItemLogServiceCache getMergeItemLogServiceCache() {
        return mergeItemLogServiceCache;
    }

    // 收费快递
    public DeliveryResult delivery(int pay_type
            , int userId, UserAddress userAddress
            , Account account, List<UserItem> user_item_list)
    {
        if (pay_type == DeliveryBusinessService.SHIPPING_PAY_WAY_COINS) {
            return delivery_PayForCoins(userId, userAddress, account, user_item_list);
        } else if (pay_type == DeliveryBusinessService.SHIPPING_PAY_WAY_POINTS) {
            return delivery_PayForPoints(userId, userAddress, account, user_item_list);
        } else {
            return new DeliveryResult(ErrorDefs.ERROR_CODE_GAME_PAY_INVALID_PAY_METHOD);
        }
    }

    // 包邮
    public DeliveryResult deliveryFree(int userId, UserAddress userAddress, List<UserItem> user_item_list)
    {

        DeliveryOrder deliveryOrder = createDeliveryOrder(userId, user_item_list.size(), userAddress, true);
        deliveryOrder = deliveryOrderService.save(deliveryOrder);
        List<DeliveryGoods> deliveryGoodsList = new ArrayList<>();
        for (UserItem ui : user_item_list) {
            deliveryGoodsList.add(createDeliverGoods(deliveryOrder.getOrderId(), ui));
        }

        // 用户道具设置成已经未发货
        updateUserItemSet_EXPRESS_STATUS_WAIT_EXPRESS(user_item_list, deliveryOrder.getOrderId());

        deliveryGoodsList = deliveryGoodsService.save(deliveryGoodsList);
        deliveryOrder.setDeliveryGoodsList(deliveryGoodsList);
        return new DeliveryResult(ErrorDefs.ERROR_SUCCESS, deliveryOrder);
    }

    // 碎片合成
    public UserItem mergeFragment(int userId, List<UserItem> user_item_list, Item target_item)
    {
        List<MergeCostFragmentLog> user_item_merge_list = new ArrayList<>();
        for (UserItem ui : user_item_list) {
            MergeCostFragmentLog merge = MergeCostFragmentLog.create(ui);
            user_item_merge_list.add(merge);
        }

        // 删除碎片
        userItemServiceCache.delete(user_item_list);

        // 获得新的item
        UserItem new_item = UserItem.create(target_item, userId, ItemServiceCache.ORIGIN_MERGE);
        new_item = userItemServiceCache.save(new_item);

        // 添加合成记录
        Date tnow = new Date();
        for (MergeCostFragmentLog uim : user_item_merge_list) {
            uim.setMergeItemId(new_item.getItemId());
            uim.setMergeTm(tnow);
        }

        // 保存删除碎片记录
        mergeCostFragmentLogServiceCache.save(user_item_merge_list);

        // 保存合成记录
        MergeItemLog mergeItemLog = MergeItemLog.create(new_item);
        mergeItemLogServiceCache.save(mergeItemLog);

        log.info("user merge fragment success. uid: {} fragment count: {} new item_id: {} "
                , userId, user_item_list.size(), new_item.getItemId());
        return new_item;
    }

    // 通过web赠送item
    public ItemPresentLog webPresentItem(User user, Item item, int itemType, int itemNum, String webName, String remark)
    {
        UserItem user_item = UserItem.create(item, user.getUserId(), ItemServiceCache.ORIGIN_SYSTEM_PRESENT);
        user_item = userItemServiceCache.save(user_item);

        ItemPresentLog present_log = ItemPresentLog.create(item, user_item.getItemId(), user.getUserId(), webName, remark);
        present_log = itemPresentLogServiceCache.save(present_log);
        return present_log;
    }

    // 抓取成功，获得item
    public UserItem userCatch(User user, int gameId, int roomId)
    {
        Room room = roomServiceCache.findOne(roomId);
        if (room == null) {
            log.warn("userCatch success but can't find room. uid: {} game_id: {} room_id: {}"
                    , user.getUserId(), gameId, roomId);
            return null;
        }
        Item item = itemServiceCache.findOne(room.getItemType());
        if (item == null) {
            log.warn("userCatch success but can't find item. uid: {} game_id: {} room_id: {} item_type: {}"
                    , user.getUserId(), gameId, roomId, room.getItemType());
            return null;
        }

        UserItem new_item = UserItem.create(item, user.getUserId(), ItemServiceCache.ORIGIN_USER_CATCH, gameId);
        new_item = userItemServiceCache.save(new_item);
        return new_item;
    }

    // 2.0之前版本娃娃兑换金币后，会调用次接口，防止用户升级版本后可以再次兑换
    public void updateUserItemExchangeCoinsStatus(Map<Integer, Integer> itemIdList)
    {
        for (Map.Entry<Integer, Integer> pair : itemIdList.entrySet()) {
            int itemId = pair.getKey();
            int coins = pair.getValue();
            userItemServiceCache.UpdateExchangeCoinsStatus(itemId, UserItem.EXCHANGE_COINS_STATUS_EXCHANGED, coins);
        }
    }

    // 2.0之前版本快递后，会调用此接口，防止用户升级版本后可以再次快递
    public void updateUserItemExpressStatus(List<Integer> itemIdList, int orderId)
    {
        for (int itemId : itemIdList) {
            userItemServiceCache.UpdateExpressStatus(itemId, UserItem.EXPRESS_STATUS_WAIT_EXPRESS, orderId);
        }
    }

    // 把Game表中的兑换状态设置成已经兑换
    public void updateGameSetExchangeCoins(final UserItem userItem)
    {
        // 如果该道具是通过抓取获得，把game中的兑换状态改成已经兑换，防止从新版本切换到旧版本可以再次兑换
        if (userItem.getOriginType() == ItemServiceCache.ORIGIN_USER_CATCH && userItem.getOriginValue() != 0) {
            Game game =gameServiceCache.findOne(userItem.getOriginValue());
            if (game != null) {
                game.setIsExchange(1);
                game.setExchangeStatus(Game.GAME_GOODS_EXCHANGE_STATUS_DONE);
                gameServiceCache.save(game);
            }
        }
    }

    // 把Game表中的快递状态设置成未发货
    public void updateGameSetDeliveryState(final List<UserItem> userItemList)
    {
        // 如果该道具是通过抓取获得，把game中的快递状态设置成未发货，防止从新版本切换到旧版本可以再次发货
        List<Integer> gameIdList = new ArrayList<>();
        for (UserItem ui : userItemList) {
            if (ui.getOriginType() == ItemServiceCache.ORIGIN_USER_CATCH && ui.getOriginValue() != 0) {
                gameIdList.add(ui.getOriginValue());
            }
        }
        List<Game> gameList = gameServiceCache.findAll(gameIdList);
        for (Game game : gameList) {
            if (game == null)
                continue;
            game.setIsDelivery(1);
            game.setShippingStatus(Game.GAME_GOODS_SHIPPING_STATUS_INITIAL);
        }
        gameServiceCache.save(gameList);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private DeliveryResult delivery_PayForCoins(int userId, UserAddress userAddress, Account account
            , List<UserItem> user_item_list)
    {
        // 金币支付不足
        final int PAY_COINS_NUM = getSHIPPING_PAY_COINS_NUM();
        if (account.getAvailableCoins() <= PAY_COINS_NUM) {
            return new DeliveryResult(ErrorDefs.ERROR_CODE_ACCOUNT_NO_ENOUGH_COINS);
        }

        DeliveryOrder deliveryOrder = createDeliveryOrder(userId, user_item_list.size(), userAddress, false);
        deliveryOrder = deliveryOrderService.save(deliveryOrder);

        // 用户道具设置成已经未发货
        updateUserItemSet_EXPRESS_STATUS_WAIT_EXPRESS(user_item_list, deliveryOrder.getOrderId());

        List<DeliveryGoods> deliveryGoodsList = new ArrayList<>();
        for (UserItem ui : user_item_list) {
            deliveryGoodsList.add(createDeliverGoods(deliveryOrder.getOrderId(), ui));
        }
        deliveryGoodsList = deliveryGoodsService.save(deliveryGoodsList);

        ErrorCode.ErrorEntry pay_result = accountBusinessService.payDeliveryWithCoins(userId,
                PAY_COINS_NUM, deliveryGoodsList);
        if (!pay_result.isSuccess()) {
            //小概率,客户端需要一次判断
            deliveryOrderService.delete(deliveryOrder);
            deliveryGoodsService.delete(deliveryGoodsList);
            return new DeliveryResult(pay_result);
        }

        // 支付成功后通知
        deliveryOrder.setPayWay(DeliveryBusinessService.SHIPPING_PAY_WAY_COINS);
        deliveryOrder.setPayStatus(1);
        deliveryOrder.setCoinsPaid(PAY_COINS_NUM);
        deliveryOrderService.save(deliveryOrder);
        deliveryOrder.setDeliveryGoodsList(deliveryGoodsList);
        return new DeliveryResult(pay_result, deliveryOrder);
    }

    private DeliveryResult delivery_PayForPoints(int userId, UserAddress userAddress, Account account
            , List<UserItem> user_item_list)
    {
        // 积分支付不足
        final int PAY_POINTS_NUM = platDictServiceCache.getGoodsDeliveryPoints();
        if (account.getAvailablePoints() <= PAY_POINTS_NUM) {
            return new DeliveryResult(ErrorDefs.ERROR_CODE_ACCOUNT_NO_ENOUGH_POINTS);
        }

        DeliveryOrder deliveryOrder = createDeliveryOrder(userId, user_item_list.size(), userAddress, false);
        deliveryOrder = deliveryOrderService.save(deliveryOrder);

        // 用户道具设置成已经未发货
        updateUserItemSet_EXPRESS_STATUS_WAIT_EXPRESS(user_item_list, deliveryOrder.getOrderId());

        List<DeliveryGoods> deliveryGoodsList = new ArrayList<>();
        for (UserItem ui : user_item_list) {
            deliveryGoodsList.add(createDeliverGoods(deliveryOrder.getOrderId(), ui));
        }
        deliveryGoodsList = deliveryGoodsService.save(deliveryGoodsList);

        ErrorCode.ErrorEntry pay_result = accountBusinessService.payDeliveryWithPoints(userId
                , PAY_POINTS_NUM, deliveryGoodsList);
        if (!pay_result.isSuccess()) {
            //小概率,客户端需要一次判断
            deliveryOrderService.delete(deliveryOrder);
            deliveryGoodsService.delete(deliveryGoodsList);
            return new DeliveryResult(pay_result);
        }

        // 支付成功后通知
        deliveryOrder.setPayWay(DeliveryBusinessService.SHIPPING_PAY_WAY_POINTS);
        deliveryOrder.setPayStatus(1);
        deliveryOrder.setPointsPaid(PAY_POINTS_NUM);
        deliveryOrderService.save(deliveryOrder);
        deliveryOrder.setDeliveryGoodsList(deliveryGoodsList);
        return new DeliveryResult(pay_result, deliveryOrder);
    }

    private static DeliveryOrder createDeliveryOrder(int userId, int goods_num, UserAddress userAddress, boolean free) {
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setOrderSN(DeliveryBusinessService.generateOrderSN());
        deliveryOrder.setGoodsNum(goods_num);
        deliveryOrder.setShippingId(0);
        deliveryOrder.setShippingName("");
        deliveryOrder.setShippingFee(0);
        deliveryOrder.setShippingPoints(0);
        deliveryOrder.setShippingCoins(0);
        deliveryOrder.setIsFreeShipping(free ? 1 : 0);
        deliveryOrder.setPayWay(0);
        deliveryOrder.setPayStatus(0);
        deliveryOrder.setCoinsPaid(0);
        deliveryOrder.setPointsPaid(0);
        deliveryOrder.setShippingStatus(DeliveryOrder.DELIVERY_ORDER_SHIPPING_STATUS_INITIAL);
        deliveryOrder.setSigninStatus(DeliveryOrder.DELIVERY_ORDER_SIGNIN_STATUS_INITIAL);
        deliveryOrder.setUserId(userId);
        deliveryOrder.setAddressId(userAddress.getAddressId());
        deliveryOrder.setConsignee(userAddress.getConsignee());
        deliveryOrder.setAddress(userAddress.getAddress());
        deliveryOrder.setCountry(userAddress.getCountry());
        deliveryOrder.setProvince(userAddress.getProvince());
        deliveryOrder.setCity(userAddress.getCity());
        deliveryOrder.setDistrict(userAddress.getDistrict());
        deliveryOrder.setMobile(userAddress.getMobile());
        deliveryOrder.setRemarks(userAddress.getRemarks());
        return deliveryOrder;
    }

    private static DeliveryGoods createDeliverGoods(int orderId, UserItem userItem)
    {
        DeliveryGoods deliveryGoods = new DeliveryGoods();
        deliveryGoods.setType(0);
        deliveryGoods.setIdvalue(userItem.getOriginValue());       // 娃娃来源类型，如果是抓取，就是game_id
        deliveryGoods.setItemId(userItem.getItemId());
        deliveryGoods.setImageUrl(userItem.getItemIcon());
        deliveryGoods.setName(userItem.getItemName());
        deliveryGoods.setOrderId(orderId);
        return deliveryGoods;
    }

    private void updateUserItemSet_EXPRESS_STATUS_WAIT_EXPRESS(List<UserItem> userItemList, int orderId)
    {
        Date tnow = new Date();
        for (UserItem ui : userItemList) {
            ui.setExpressStatus(UserItem.EXPRESS_STATUS_WAIT_EXPRESS);
            ui.setExpressTm(tnow);
            ui.setOrderId(orderId);
        }
        userItemServiceCache.save(userItemList);
    }

    private int getSHIPPING_PAY_COINS_NUM() {
        return platDictServiceCache.getShippingPayCoinsNum();
    }

    private int getGoodsDeliveryPoints() { return  platDictServiceCache.getGoodsDeliveryPoints(); }

}
