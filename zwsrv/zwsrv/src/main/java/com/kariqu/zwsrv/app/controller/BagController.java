package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.error.ErrorDefs;
import com.kariqu.zwsrv.app.model.*;
import com.kariqu.zwsrv.app.pay.tenpay.Util;
import com.kariqu.zwsrv.app.service.*;
import com.kariqu.zwsrv.app.transaction.IsTryAgain;
import com.kariqu.zwsrv.app.utility.Utility;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.*;
import com.kariqu.zwsrv.thelib.persistance.service.ItemService;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("bag/v1")
public class BagController {
    private static final Logger log = LoggerFactory.getLogger(BagController.class);

    @Autowired
    private BagService bagService;

    @Autowired
    private UserServiceCache userServiceCache;

    @Autowired
    private AccountBusinessService accountBusinessService;

    // 获取背包
    @RequestMapping(value = "/bag_list")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData bag_list(HttpServletRequest request, @RequestParam Map<String, String> requestParams)
    {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            requestContext.validateInputParams("user_id");
            int userId = requestContext.getInegerValue("user_id");
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            PaginationRequestData pagination = Utility.createPaginationRequest(requestContext, requestParams);
            List<UserItem> item_list = bagService.getUserItemServiceCache().findBagList(userId, pagination);

            long goodsMaxStorageTime = 0;
            long currentMilliseconds = 0;
            Map<Integer, Item> item_map = new HashMap<>();
            if (!item_list.isEmpty()) {
                item_map = bagService.getItemServiceCache().findItemAsMap();
                goodsMaxStorageTime = bagService.getGoodsMaxStorageTime();
                currentMilliseconds = System.currentTimeMillis();
            }
            List<UserItemInfo> item_info_list = new ArrayList<>();
            for (UserItem ui : item_list) {
                Item item = item_map.get(ui.getItemType());
                if (item == null) {
                    log.warn("user has item, but can't find from item. uid: {} item_type: {}", userId, ui.getItemType());
                    continue;
                }
                item_info_list.add(new UserItemInfo(ui, item, goodsMaxStorageTime, currentMilliseconds));
            }

            PaginationRspData rsp = new PaginationRspData(item_info_list
                    , item_list.size() >= pagination.getSize());
            rsp.put("shipping_max_storage_expired", goodsMaxStorageTime);
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // 获取碎片
    @RequestMapping(value = "/fragment_list")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData fragment_list(HttpServletRequest request, @RequestParam Map<String, String> requestParams)
    {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            PaginationRequestData pagination = Utility.createPaginationRequest(requestContext, requestParams);
            long max_storage_time = bagService.getGoodsMaxStorageTime();
            long tnow = System.currentTimeMillis();
            List<UserItemServiceCache.FragmentAggregate> fragment_list =
                    bagService.getUserItemServiceCache().findFragmentAggregate(userId, max_storage_time, tnow
                            , pagination);

            Map<Integer, Item> item_map = new HashMap<>();
            if (!fragment_list.isEmpty()) {
                item_map = bagService.getItemServiceCache().findItemAsMap();
            }
            List<FragmentAggregateInfo> result_list = new ArrayList<>();
            for (UserItemServiceCache.FragmentAggregate f : fragment_list) {
                // 如果出现无法在配置表中找到该项，不返回给用户
                Item item = item_map.get(f.getItemType());
                if (item == null) {
                    log.warn("user has item. can't find from item. uid: {} itemType: {} ", userId, f.getItemType());
                    continue;
                }
                result_list.add(new FragmentAggregateInfo(f, item));
            }
            PaginationRspData rsp = new PaginationRspData(result_list
                    , result_list.size() >= pagination.getSize());
            rsp.put("shipping_max_storage_expired", max_storage_time);
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // 获取碎片详情
    @RequestMapping(value = "/fragment_list_detail")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData fragment_list_detail(HttpServletRequest request, @RequestParam Map<String, String> requestParams)
    {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            PaginationRequestData pagination = Utility.createPaginationRequest(requestContext, requestParams);

            requestContext.validateInputParams("itemType");
            int itemType = requestContext.getInegerValue("itemType");

            long max_storage_time = bagService.getGoodsMaxStorageTime();
            long tnow = System.currentTimeMillis();
            List<UserItem> user_item_list =
                    bagService.getUserItemServiceCache().findUnexpiredFragmentList(userId, itemType, max_storage_time, tnow);

            Map<Integer, Item> item_map = new HashMap<>();
            if (!user_item_list.isEmpty()) {
                item_map = bagService.getItemServiceCache().findItemAsMap();
            }
            List<UserItemInfo> result_list = new ArrayList<>();
            for (UserItem ui : user_item_list) {
                // 如果出现无法在配置表中找到该项，不返回给用户
                Item item = item_map.get(ui.getItemType());
                if (item == null) {
                    log.warn("user has item. can't find from item. uid: {} itemType: {} ", userId, ui.getItemType());
                    continue;
                }
                result_list.add(new UserItemInfo(ui, item, max_storage_time, tnow));
            }
            PaginationRspData rsp = new PaginationRspData(result_list
                    , result_list.size() >= pagination.getSize());
            long goodsMaxStorageTime = bagService.getGoodsMaxStorageTime();
            rsp.put("shipping_max_storage_expired", goodsMaxStorageTime);
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // 获取过期碎片
    @RequestMapping(value = "/expired_fragment_list")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData expired_fragment_list(HttpServletRequest request, @RequestParam Map<String, String> requestParams)
    {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            PaginationRequestData pagination = Utility.createPaginationRequest(requestContext, requestParams);
            long max_storage_time = bagService.getGoodsMaxStorageTime();
            long tnow = System.currentTimeMillis();
            List<UserItem> fragment_list = bagService.getUserItemServiceCache().findExpiredFragment(userId
                    , max_storage_time, tnow, pagination);

            Map<Integer, Item> item_map = new HashMap<>();
            if (!fragment_list.isEmpty()) {
                item_map = bagService.getItemServiceCache().findItemAsMap();
            }
            List<UserItemInfo> result_list = new ArrayList<>();
            for (UserItem ui : fragment_list) {
                Item item = item_map.get(ui.getItemType());
                if (item == null) {
                    log.warn("user has item. can't find from item. uid: {} itemType: {} ", userId,  ui.getItemType());
                    continue;
                }
                result_list.add(new UserItemInfo(ui, item, max_storage_time, tnow));
            }
            PaginationRspData rsp = new PaginationRspData(result_list
                    , result_list.size() >= pagination.getSize());
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // 娃娃碎片合成
    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value = "/merge_fragment")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData merge_fragment(HttpServletRequest request, @RequestParam Map<String, String> requestParams)
    {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId == 0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            requestContext.validateInputParams("itemType");
            int itemType = requestContext.getInegerValue("itemType");
            Item fragment_item = bagService.getItemServiceCache().findOne(itemType);
            if (fragment_item == null || fragment_item.getCanMerge() == 0) {
                log.warn("fragment can not merge. uid: {} item_type: {}", userId, itemType);
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_UNKNOWN_FRAGMENT);
            }

            Item target_item = bagService.getItemServiceCache().findOne(fragment_item.getMergeItemType());
            if (target_item == null) {
                log.warn("fragment can not find merge target item. uid: {} item_type: {} target_type: {} "
                        , userId, itemType, fragment_item.getMergeItemType());
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_UNKNOWN_FRAGMENT);
            }

            long goodsMaxStorageTime = bagService.getGoodsMaxStorageTime();
            long tnow = System.currentTimeMillis();
            List<UserItem> user_item_list = bagService.getUserItemServiceCache().findFragmentListCanMerge(
                    userId, itemType, goodsMaxStorageTime, tnow);

            // 数量不足
            if (user_item_list.size() < fragment_item.getMergeCount()) {
                log.warn("item size insufficent.  uid: {} item_type: {} {} < {}", userId, itemType
                        , user_item_list.size(), fragment_item.getMergeCount());
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_FRAGMENT_INSUFFICIENT);
            }

            UserItem new_item = bagService.mergeFragment(userId, user_item_list.subList(0, fragment_item.getMergeCount()), target_item);
            return new ResponseData().put("data", new UserItemInfo(new_item, target_item, goodsMaxStorageTime, tnow));
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // 请求兑换话费
    @RequestMapping(value = "/exchange_hua_fei")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData exchange_hua_fei(HttpServletRequest request, @RequestParam Map<String, String> requestParams)
    {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            requestContext.validateInputParams("id", "mobile");
            int id = requestContext.getInegerValue("id");
            String mobile = requestContext.getStringValue("mobile");

            UserItem user_item = bagService.getUserItemServiceCache().findById(userId, id);
            if (user_item == null) {
                log.warn("exchange_hua_fei can't find user_item. uid: {} item_id: {}", userId, id);
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_UNKNOWN_USER_ITEM);
            }
            if (user_item.getExchangeHuaFeiStatus() != UserItem.EXCHANGE_HF_STATUS_UNEXCHANGE
                    || user_item.getExchangeCoinsStatus() != UserItem.EXCHANGE_COINS_STATUS_UNEXCHANGE
                    || user_item.getExpressStatus() != UserItem.EXPRESS_STATUS_UNEXPRESS
                    ) {
                // 已经兑换过了
                log.warn("exchange_hua_fei repeated exchange hua fei. uid: {} item_id: {}", userId, id);
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_REPEATED_EXCHANGE_HUA_FEI);
            }

            final Item item = bagService.getItemServiceCache().findOne(user_item.getItemType());
            if (item == null) {
                log.warn("exchange_hua_fei can't find item. uid: {} item_id: {}", userId, id);
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_UNKNOWN_ITEM);
            }
            if (item.getCanExchangeHuaFei() == 0) {
                // 不能兑换
                log.warn("exchange_hua_fei can't find item. uid: {} item_id: {}", userId, id);
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_EXCHANGE_HUA_FEI);
            }

            // 正在处理
            user_item.setExchangeHuaFeiStatus(UserItem.EXCHANGE_HF_STATUS_PROCESSING);
            user_item.setExchangeHuaFeiTm(new Date());
            user_item.setExchangeHuaFeiMobile(mobile);
            user_item.setExchangeHuaFeiNum(item.getExchangeFhNum());
            bagService.getUserItemServiceCache().save(user_item);
            return new ResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // 请求快递
    @RequestMapping(value="/request_express")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData request_express(HttpServletRequest request, @RequestParam Map<String,String> requestParams)
    {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            PaginationRequestData pagination = Utility.createPaginationRequest(requestContext, requestParams);

            List<UserItem> user_item_list = bagService.getUserItemServiceCache().findBagList(userId, pagination);

            long goodsMaxStorageTime = 0;
            long tnow = 0;
            Map<Integer, Item> item_map = new HashMap<>();
            if (!user_item_list.isEmpty()) {
                item_map = bagService.getItemServiceCache().findItemAsMap();
                goodsMaxStorageTime = bagService.getGoodsMaxStorageTime();
                tnow = System.currentTimeMillis();
            }

            List<UserItemInfo> ret_list = new ArrayList<>();
            for (UserItem ui : user_item_list) {
                // 已经快递了
                if (ui.getExpressStatus() != UserItem.EXPRESS_STATUS_UNEXPRESS)
                    continue;
                // 已经兑换了
                if (ui.getExchangeCoinsStatus() == UserItem.EXCHANGE_COINS_STATUS_EXCHANGED)
                    continue;
                // 此道具属于不能快递
                Item item = item_map.get(ui.getItemType());
                if (item == null || item.getCanExpress() == 0) {
                    continue;
                }

                // 寄存中
                if (Utility.getExpiredTime(ui.getCreateTime(), goodsMaxStorageTime, tnow) > 0) {
                    UserItemInfo userItemInfo = new UserItemInfo(ui, item, goodsMaxStorageTime, tnow);
                    ret_list.add(userItemInfo);
                }
            }
            PaginationRspData rsp = new PaginationRspData(ret_list
                    , ret_list.size() >= pagination.getSize());

            // 多少个包邮
            rsp.put("shipping_free_dolls_num", bagService.getPlatDictServiceCache().getShippingFreeDollsNum());

            // 运费所需积分
            rsp.put("shipping_pay_points", bagService.getPlatDictServiceCache().getGoodsDeliveryPoints());

            return rsp;
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    // 确定快递
    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/request_express_confirm")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData request_express_confirm(HttpServletRequest request, @RequestParam Map<String,String> requestParams)
    {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }

            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            requestContext.validateInputParams("ids", "address_id", "pay_type");
            String ids = requestContext.getStringValue("ids");
            int address_id = requestContext.getInegerValue("address_id");
            int pay_type = requestContext.getInegerValue("pay_type");

            List<Integer> id_list = Utility.parseRequestToList(ids, log);
            if (id_list.isEmpty()) {
                return new ErrorResponse(ErrorDefs.ERROR_INVALID_PARAMETERS);
            }
            List<UserItem> user_item_list = bagService.getUserItemServiceCache().findAll(id_list);
            if (user_item_list == null || user_item_list.size() != id_list.size()) {
                log.warn("user_item_list != id_list. uid: {} ids: {}", userId, ids);
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_CAN_NOT_EXPRESS);
            }

            long goodsMaxStorageTime = 0;
            long tnow = 0;
            if (!user_item_list.isEmpty()) {
                goodsMaxStorageTime = bagService.getGoodsMaxStorageTime();
                tnow = System.currentTimeMillis();
            }

            Map<Integer, Item> item_map = bagService.getItemServiceCache().findItemAsMap();
            for (UserItem ui : user_item_list) {
                // 不是自己的道具
                if (ui.getUserId() != userId) {
                    log.warn("user item user_id != self. self: {} {}", userId, ui.getUserId());
                    return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_CAN_NOT_EXPRESS);
                }
                if (ui.getExpressStatus() != UserItem.EXPRESS_STATUS_UNEXPRESS) {
                    log.warn("user item express status != 0. uid: {}  item_id: {}", userId, ui.getItemId());
                    return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_CAN_NOT_EXPRESS);
                }

                final Item item = item_map.get(ui.getItemType());
                if (item == null) {
                    log.warn("user item can't find item type. uid: {}  item_id: {} item_type: {}", userId
                            , ui.getItemId(), ui.getItemType());
                    return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_CAN_NOT_EXPRESS);
                }
                if (item.getCanExpress() == 0) {
                    // item不是快递类型
                    log.warn("user item can't express. uid: {}  item_id: {} item_type: {}", userId, ui.getItemId()
                            , ui.getItemType());
                    return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_CAN_NOT_EXPRESS);
                }

                // 不在寄存中
                if (Utility.getExpiredTime(ui.getCreateTime(), goodsMaxStorageTime, tnow) == 0) {
                    log.warn("user item can't expired. uid: {} item_id: {} createtime: {} max_tm: {} tnow: {}",
                            userId, ui.getItemId(), ui.getCreateTime(), goodsMaxStorageTime, tnow);
                    return new ErrorResponse(ErrorCode.ERROR_FAILED);
                }
            }

            UserAddress user_address = bagService.getUserAddressService().findOne(address_id);
            if (user_address == null) {
                return new ErrorResponse(ErrorDefs.ERROR_CODE_DELIVERY_INVALID_USER_ADDRESS);
            }
            if (pay_type != DeliveryBusinessService.SHIPPING_PAY_WAY_FREE
                    && pay_type != DeliveryBusinessService.SHIPPING_PAY_WAY_POINTS) {
                return new ErrorResponse(ErrorDefs.ERROR_CODE_DELIVERY_INVALID_PAY_METHOD);
            }
            Account account = bagService.getAccountService().findOne(userId);
            if (account == null) {
                log.warn("user account is null. uid: {}" ,userId);
                return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
            }

            BagService.DeliveryResult result = null;
            if (user_item_list.size() >= bagService.getPlatDictServiceCache().getShippingFreeDollsNum()) {
                result = bagService.deliveryFree(userId, user_address, user_item_list);
            } else {
                result = bagService.delivery(pay_type, userId, user_address, account, user_item_list);
            }
            if (result != null) {
                if (result.ret.isSuccess()) {
                    // 更新game表
                    bagService.updateGameSetDeliveryState(user_item_list);

                    List<UserItemInfo> userItemInfoList = new ArrayList<>();
                    for (UserItem ui : user_item_list) {
                        userItemInfoList.add(new UserItemInfo(ui, item_map.get(ui.getItemType()), goodsMaxStorageTime, tnow));
                    }
                    ResponseData responseData = new ResponseData();
                    responseData.put("list", userItemInfoList);
                    return responseData;
                } else {
                    return new ErrorResponse(result.ret);
                }
            }
            return new ErrorResponse(result.ret);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // 赠送道具
    @RequestMapping(value="/web_present_item")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData web_present_item(HttpServletRequest request, @RequestParam Map<String,String> requestParams)
    {
        int userId = 0;
        int itemType = 0;
        int itemNum = 0;
        String webName = "";
        String remark;
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            requestContext.validateInputParams("userId", "itemType", "itemNum", "webName", "remark");
            userId = requestContext.getInegerValue("userId");
            itemType = requestContext.getInegerValue("itemType");
            itemNum = requestContext.getInegerValue("itemNum");
            webName = requestContext.getStringValue("webName");
            remark = requestContext.getStringValue("remark");
            if (itemNum <= 0 || webName.isEmpty() || remark.isEmpty())
                return new ErrorResponse(ErrorDefs.ERROR_INVALID_PARAMETERS);
        } catch (Exception e) {
            return new ErrorResponse(ErrorDefs.ERROR_INVALID_PARAMETERS);
        }

        try {
            User user = userServiceCache.findOne(userId);
            if (user == null) {
                return new ErrorResponse(ErrorDefs.ERROR_CODE_UNKNWON_USER);
            }
            Item item = bagService.getItemServiceCache().findOne(itemType);
            if (item == null) {
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_UNKNOWN_ITEM);
            }
            ItemPresentLog presentLog = bagService.webPresentItem(user, item, itemType, itemNum, webName, remark);
            return new ResponseData().put("data", new ItemPresentLogInfo(presentLog));
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // 获取合成记录
    @RequestMapping(value="/merge_item_list")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData merge_item_list(HttpServletRequest request, @RequestParam Map<String,String> requestParams)
    {
        try {
            int userId = SecurityUtil.currentUserId();
            if (userId==0) {
                return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
            }
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            PaginationRequestData pagination = Utility.createPaginationRequest(requestContext, requestParams);
            List<MergeItemLog> item_list = bagService.getMergeItemLogServiceCache().findMergeItemList(userId, pagination);

            List<MergeItemLogInfo> result_list = new ArrayList<>();
            for (MergeItemLog item : item_list) {
                result_list.add(new MergeItemLogInfo(item));
            }
            PaginationRspData rsp = new PaginationRspData(result_list
                    , result_list.size() >= pagination.getSize());
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("server exception");
        }
        return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
    }

    // item兑换金币
    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/item_exchange_coins")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData item_exchange_coins(HttpServletRequest request, @RequestParam Map<String,String> requestParams) {
        int userId = SecurityUtil.currentUserId();
        if (userId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            requestContext.validateInputParams("ids");
            String idsString = requestContext.getStringValue("ids");
            int itemId = Integer.valueOf(idsString);

            UserItem userItem = bagService.getUserItemServiceCache().findById(userId, itemId);
            if (userItem == null
                    || userItem.getExchangeCoinsStatus() != UserItem.EXCHANGE_COINS_STATUS_UNEXCHANGE
                    || userItem.getExpressStatus() != UserItem.EXPRESS_STATUS_UNEXPRESS
                    || userItem.getExchangeHuaFeiStatus() != UserItem.EXCHANGE_HF_STATUS_UNEXCHANGE) {
                log.warn("can't exchange coins. uid: {} item_id: {}", userId, itemId);
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_EXCHANGE_COINS_FAILED);
            }

            Account account = bagService.getAccountService().findOne(userId);
            if (account == null) {
                log.warn("can't exchange coins. account null uid: {} item_id: {}", userId, itemId);
                return new ErrorResponse(ErrorDefs.ERROR_SERVER_INNER);
            }

            Item item = bagService.getItemServiceCache().findOne(userItem.getItemType());
            if (item == null) {
                log.warn("can't exchange coins. item is null uid: {} item_id: {} item_type: {}", userId, itemId
                        , userItem.getItemType());
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_EXCHANGE_COINS_FAILED);
            }
            if (item.getCanExchangeCoins() == 0) {
                log.warn("can't exchange coins. item can not exchange coins. uid: {} item_id: {} item_type: {}"
                        , userId, itemId, userItem.getItemType());
                return new ErrorResponse(ErrorDefs.ERROR_CODE_ITEM_EXCHANGE_COINS_FAILED);
            }

            UserItem newUserItem = accountBusinessService.userItemExchangeToCoins(userId, account, userItem, item);
            if (newUserItem == null) {
                log.warn("userItemExchangeToCoins fail. uid: {} item_id: {}", userId, userItem.getItemId());
                return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
            }

            // 更新game表
            bagService.updateGameSetExchangeCoins(userItem);

            long goodsMaxStorageTime = bagService.getGoodsMaxStorageTime();
            long currentMilliseconds = System.currentTimeMillis();
            ResponseData responseData = new ResponseData();
            responseData.put("data", new UserItemInfo(newUserItem, item, goodsMaxStorageTime, currentMilliseconds));
            //responseData.put("account",accountListReturn.get(0));
            return responseData;
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }
}
