package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.error.ErrorDefs;
import com.kariqu.zwsrv.app.transaction.IsTryAgain;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.persistance.entity.*;
import com.kariqu.zwsrv.thelib.persistance.service.DeliveryGoodsService;
import com.kariqu.zwsrv.thelib.persistance.service.DeliveryOrderService;
import com.kariqu.zwsrv.thelib.persistance.service.ShippingService;
import com.kariqu.zwsrv.thelib.persistance.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by simon on 08/12/17.
 */
@Service
public class DeliveryBusinessService {

    public static final int SHIPPING_PAY_WAY_FREE = 0;
    public static final int SHIPPING_PAY_WAY_POINTS = 1;
    public static final int SHIPPING_PAY_WAY_COINS = 2;

    @Autowired
    DeliveryGoodsService deliveryGoodsService;

    @Autowired
    DeliveryOrderService deliveryOrderService;

    @Autowired
    ShippingService shippingService;

    @Autowired
    UserAddressService userAddressService;

    @Autowired
    AccountBusinessService accountBusinessService;

    @Autowired
    PlatDictServiceCache platDictServiceCache;

    public int getSHIPPING_FREE_DOLLS_NUM() {
        return platDictServiceCache.getShippingFreeDollsNum();
    }

    public int getSHIPPING_PAY_COINS_NUM() {
        return platDictServiceCache.getShippingPayCoinsNum();
    }

    public long getGoodsMaxStorageTime() { return platDictServiceCache.getGoodsMaxStorageTime(); }
    public int getGoodsDeliveryPoints() { return  platDictServiceCache.getGoodsDeliveryPoints(); }

    public boolean isFirstDelivery(int userId) {
        DeliveryOrder deliveryOrder= deliveryOrderService.findOneByUserId(userId);
        if (deliveryOrder!=null) {
            return false;
        } else {
            return true;
        }
    }

    public ErrorCode.ErrorEntry delivery(int pay_type,
                                         int userId, List<Game> gameList,
                                         int addressId, List<DeliveryOrder> deliveryOrderListReturn) {

        UserAddress userAddress = userAddressService.findOne(addressId);
        if (userAddress!=null) {
            ///不检查检查账户是否有足够的积分或金币,小概率,客户端需要一次判断
            //...
            Account account = accountBusinessService.findAccount(userId);
            if (account==null) {
                account=new Account();
                account.setUserId(userId);
            }
            if (pay_type == SHIPPING_PAY_WAY_COINS) {
                /// 金币支付
                int PAY_COINS_NUM = getSHIPPING_PAY_COINS_NUM();
                if (account.getAvailableCoins() >= PAY_COINS_NUM) {
                    DeliveryOrder deliveryOrder = createDeliveryOrder(userId, gameList, addressId, userAddress);
                    deliveryOrder=deliveryOrderService.save(deliveryOrder);

                    List<DeliveryGoods> deliveryGoodsList=new ArrayList<>();
                    for (Game game : gameList) {
                        DeliveryGoods deliveryGoods=new DeliveryGoods();
                        deliveryGoods.setType(0);
                        deliveryGoods.setIdvalue(game.getGameId());
                        deliveryGoods.setItemId(game.getItemId());
                        deliveryGoods.setImageUrl(game.getImageUrl());
                        deliveryGoods.setName(game.getName());
                        deliveryGoods.setOrderId(deliveryOrder.getOrderId());
                        deliveryGoodsList.add(deliveryGoods);
                    }
                    deliveryGoodsList = deliveryGoodsService.save(deliveryGoodsList);

                    ErrorCode.ErrorEntry result = accountBusinessService.payDeliveryWithCoins(userId,
                            PAY_COINS_NUM, deliveryGoodsList);
                    if (result.isSuccess()) {
                        deliveryOrder.setPayWay(SHIPPING_PAY_WAY_COINS);
                        deliveryOrder.setPayStatus(1);
                        deliveryOrder.setCoinsPaid(PAY_COINS_NUM);
                        deliveryOrderService.save(deliveryOrder);

                        deliveryOrder.setDeliveryGoodsList(deliveryGoodsList);
                        if (deliveryOrderListReturn!=null) {
                            deliveryOrderListReturn.add(deliveryOrder);
                        }
                        //....通知
                    } else {
                        //小概率,客户端需要一次判断
                        deliveryOrderService.delete(deliveryOrder);
                        deliveryGoodsService.delete(deliveryGoodsList);
                    }
                    return result;
                } else {
                    return ErrorDefs.ERROR_CODE_ACCOUNT_NO_ENOUGH_COINS;
                }
            } else if (pay_type == SHIPPING_PAY_WAY_POINTS) {
                /// 积分支付
                int PAY_POINTS_NUM = platDictServiceCache.getGoodsDeliveryPoints();
                if (account.getAvailablePoints() >= PAY_POINTS_NUM) {
                    DeliveryOrder deliveryOrder = createDeliveryOrder(userId, gameList, addressId, userAddress);
                    deliveryOrder=deliveryOrderService.save(deliveryOrder);

                    List<DeliveryGoods> deliveryGoodsList=new ArrayList<>();
                    for (Game game : gameList) {
                        DeliveryGoods deliveryGoods=new DeliveryGoods();
                        deliveryGoods.setType(0);
                        deliveryGoods.setIdvalue(game.getGameId());
                        deliveryGoods.setItemId(game.getItemId());
                        deliveryGoods.setImageUrl(game.getImageUrl());
                        deliveryGoods.setName(game.getName());
                        deliveryGoods.setOrderId(deliveryOrder.getOrderId());
                        deliveryGoodsList.add(deliveryGoods);
                    }
                    deliveryGoodsList = deliveryGoodsService.save(deliveryGoodsList);

                    ErrorCode.ErrorEntry result = accountBusinessService.payDeliveryWithPoints(userId,
                            PAY_POINTS_NUM, deliveryGoodsList);
                    if (result.isSuccess()) {
                        deliveryOrder.setPayWay(SHIPPING_PAY_WAY_POINTS);
                        deliveryOrder.setPayStatus(1);
                        deliveryOrder.setCoinsPaid(PAY_POINTS_NUM);
                        deliveryOrderService.save(deliveryOrder);

                        deliveryOrder.setDeliveryGoodsList(deliveryGoodsList);
                        if (deliveryOrderListReturn!=null) {
                            deliveryOrderListReturn.add(deliveryOrder);
                        }
                        //....通知
                    } else {
                        //小概率,客户端需要一次判断
                        deliveryOrderService.delete(deliveryOrder);
                        deliveryGoodsService.delete(deliveryGoodsList);
                    }
                    return result;
                } else {
                    return ErrorDefs.ERROR_CODE_ACCOUNT_NO_ENOUGH_POINTS;
                }
            } else {
                return ErrorDefs.ERROR_CODE_GAME_PAY_INVALID_PAY_METHOD;
            }
        } else {
            return ErrorDefs.ERROR_CODE_DELIVERY_INVALID_USER_ADDRESS;
        }
    }

    //免运费
    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    public ErrorCode.ErrorEntry deliveryFree(int userId, List<Game> gameList, int addressId, List<DeliveryOrder> deliveryOrderListReturn) {

        UserAddress userAddress = userAddressService.findOne(addressId);
        if (userAddress!=null) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setOrderSN(generateOrderSN());
            deliveryOrder.setGoodsNum(gameList.size());
            deliveryOrder.setShippingId(0);
            deliveryOrder.setShippingName("");
            deliveryOrder.setShippingFee(0);
            deliveryOrder.setShippingPoints(0);
            deliveryOrder.setShippingCoins(0);
            deliveryOrder.setIsFreeShipping(1);
            deliveryOrder.setPayWay(0);
            deliveryOrder.setPayStatus(0);
            deliveryOrder.setCoinsPaid(0);
            deliveryOrder.setPointsPaid(0);
            deliveryOrder.setShippingStatus(DeliveryOrder.DELIVERY_ORDER_SHIPPING_STATUS_INITIAL);
            deliveryOrder.setSigninStatus(DeliveryOrder.DELIVERY_ORDER_SIGNIN_STATUS_INITIAL);
            deliveryOrder.setUserId(userId);
            deliveryOrder.setAddressId(addressId);
            deliveryOrder.setConsignee(userAddress.getConsignee());
            deliveryOrder.setAddress(userAddress.getAddress());
            deliveryOrder.setCountry(userAddress.getCountry());
            deliveryOrder.setProvince(userAddress.getProvince());
            deliveryOrder.setCity(userAddress.getCity());
            deliveryOrder.setDistrict(userAddress.getDistrict());
            deliveryOrder.setMobile(userAddress.getMobile());
            deliveryOrder.setRemarks(userAddress.getRemarks());

            deliveryOrder=deliveryOrderService.save(deliveryOrder);

            List<DeliveryGoods> deliveryGoodsList=new ArrayList<>();
            for (Game game : gameList) {
                DeliveryGoods deliveryGoods=new DeliveryGoods();
                deliveryGoods.setType(0);
                deliveryGoods.setIdvalue(game.getGameId());
                deliveryGoods.setItemId(game.getItemId());
                deliveryGoods.setImageUrl(game.getImageUrl());
                deliveryGoods.setName(game.getName());
                deliveryGoods.setOrderId(deliveryOrder.getOrderId());
                deliveryGoodsList.add(deliveryGoods);
            }
            deliveryGoodsList = deliveryGoodsService.save(deliveryGoodsList);
            if (deliveryGoodsList==null) {
                deliveryGoodsList=new ArrayList<>();
            }
            deliveryOrder.setDeliveryGoodsList(deliveryGoodsList);

            if (deliveryOrderListReturn!=null) {
                deliveryOrderListReturn.add(deliveryOrder);
            }

            //....通知

            return ErrorDefs.ERROR_SUCCESS;
        } else {
            return ErrorDefs.ERROR_CODE_DELIVERY_INVALID_USER_ADDRESS;
        }
    }

    static String generateOrderSN() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    protected static DeliveryOrder createDeliveryOrder(int userId, List<Game> gameList, int addressId, UserAddress userAddress) {
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setOrderSN(generateOrderSN());
        deliveryOrder.setGoodsNum(gameList.size());
        deliveryOrder.setShippingId(0);
        deliveryOrder.setShippingName("");
        deliveryOrder.setShippingFee(0);
        deliveryOrder.setShippingPoints(0);
        deliveryOrder.setShippingCoins(0);
        deliveryOrder.setIsFreeShipping(1);
        deliveryOrder.setPayWay(0);
        deliveryOrder.setPayStatus(0);
        deliveryOrder.setCoinsPaid(0);
        deliveryOrder.setPointsPaid(0);
        deliveryOrder.setShippingStatus(DeliveryOrder.DELIVERY_ORDER_SHIPPING_STATUS_INITIAL);
        deliveryOrder.setSigninStatus(DeliveryOrder.DELIVERY_ORDER_SIGNIN_STATUS_INITIAL);
        deliveryOrder.setUserId(userId);
        deliveryOrder.setAddressId(addressId);
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
}
