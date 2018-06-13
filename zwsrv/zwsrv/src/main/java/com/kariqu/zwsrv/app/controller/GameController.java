package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.Application;
import com.kariqu.zwsrv.app.cdn.URLHelper;
import com.kariqu.zwsrv.app.model.*;
import com.kariqu.zwsrv.app.service.*;
import com.kariqu.zwsrv.app.transaction.IsTryAgain;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.*;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import com.kariqu.zwsrv.thelib.util.JsonUtil;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by simon on 01/12/17.
 */

@RestController
@RequestMapping("game/v1")
public class GameController extends BaseController {

    @Autowired
    GameServiceCache gameServiceCache;

    @Autowired
    AccountBusinessService accountBusinessService;

    @Autowired
    DeliveryBusinessService deliveryBusinessService;

    @Autowired
    GameRewardService gameRewardService;

    @Autowired
    UserCountServiceCache userCountServiceCache;

    @Autowired
    UserServiceCache userServiceCache;

    @Autowired
    CatchHistoryServiceCache catchHistoryServiceCache;

    @Autowired
    private BagService bagService;

    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/request_exchange_coins")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData request_exchange_coins(HttpServletRequest request,
                                               @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("ids");
            String idsString=requestContext.getStringValue("ids");

            if (idsString!=null&&idsString.length()>0) {
                List<Integer> ids = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(idsString,"|");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken();
                    if (NumberValidationUtil.isWholeNumber(nextToken)) {
                        ids.add(Integer.valueOf(nextToken));
                    }
                }
                if (ids.size()>0) {
                    List<Game> gameListTemp=new ArrayList<>();
                    List<Game> gameList = gameServiceCache.findAll(ids);
                    if (gameList!=null&&gameList.size()>0) {
                        for (Game game : gameList) {
                            if (game.getPlayerId()==currentUserId
                                    && game.getResult()==Game.GAME_RESULT_SUCCESS
                                    &&game.getIsDelivery()==0
                                    &&game.getIsExchange()==0) {
                                gameListTemp.add(game);
                            }
                        }
                    }

                    List<Account> accountListReturn = new ArrayList<>();
                    if (gameListTemp.size()>0) {
                        ErrorCode.ErrorEntry result = accountBusinessService.goodsExchangeToCoins(currentUserId, gameListTemp, accountListReturn);
                        if (result.isSuccess()) {
                            List<GameStatusInfo> gameStatusInfoList=new ArrayList<>();

                            // 2.0版本以后，为了止2.0之前兑换了，2.0后又可以兑换，必须同步修改UserItem中的兑换金币状态
                            Map<Integer, Integer> itemIdMap = new HashMap<>();
                            for (Game game : gameListTemp) {
                                game.setIsExchange(1);
                                game.setExchangeStatus(Game.GAME_GOODS_EXCHANGE_STATUS_DONE);
                                gameStatusInfoList.add(new GameStatusInfo(game));
                                if (game.getItemId() != 0) {
                                    itemIdMap.put(game.getItemId(), game.getExchangeCoins());
                                }
                            }
                            gameServiceCache.save(gameListTemp);

                            // 同步保存UserItem表
                            bagService.updateUserItemExchangeCoinsStatus(itemIdMap);

                            ResponseData responseData = new ResponseData();
                            responseData.put("game_status_list",gameStatusInfoList);
                            if (accountListReturn.size()>0) {
                                responseData.put("account",accountListReturn.get(0));
                            }
                            return responseData;
                        }
                        else {
                            return new ErrorResponse(result);
                        }
                    }
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }


    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/request_delivery")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData request_delivery(HttpServletRequest request,
                                         @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);

        // "pay_type"参数，旧版本没有
        int pay_type = 0;
        try {
            requestContext.validateInputParams("pay_type");
            pay_type = requestContext.getInegerValue("pay_type");
        } catch (ServerException e) {
            // 旧版本pay_type默认设置成金币支付
            pay_type = DeliveryBusinessService.SHIPPING_PAY_WAY_COINS;
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }

        try {
            requestContext.validateInputParams("ids","address_id");
            String idsString=requestContext.getStringValue("ids");
            int addressId=requestContext.getInegerValue("address_id");
            if (idsString!=null&&idsString.length()>0) {
                List<Integer> ids = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(idsString,"|");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken();
                    if (NumberValidationUtil.isWholeNumber(nextToken)) {
                        ids.add(Integer.valueOf(nextToken));
                    }
                }
                if (ids.size()>0) {
                    List<Game> gameListTemp=new ArrayList<>();
                    List<Game> gameList = gameServiceCache.findAll(ids);
                    if (gameList!=null&&gameList.size()>0) {
                        for (Game game : gameList) {
                            if (game.getPlayerId()==currentUserId
                                    && game.getResult()==Game.GAME_RESULT_SUCCESS
                                    &&game.getIsDelivery()==0
                                    &&game.getIsExchange()==0) {
                                gameListTemp.add(game);
                            }
                        }
                    }

                    // 判断抓到娃娃是不是超出寄存时间限制
                    long tnow_milliseconds = System.currentTimeMillis();
                    if (gameListTemp.size() > 0) {
                        long delta = deliveryBusinessService.getGoodsMaxStorageTime();
                        for (Game game : gameListTemp) {
                            if (tnow_milliseconds - game.getEndTime() > delta) {
                                Application.getLog().warn("GoodsStorage expired {} {} > {} {}",
                                        currentUserId, tnow_milliseconds, game.getEndTime(), delta);
                                return new ErrorResponse(ErrorCode.ERROR_FAILED);
                            }
                        }
                    }

                    List<DeliveryOrder> deliveryOrderListReturn = new ArrayList<>();
                    ErrorCode.ErrorEntry result = null;
                    if (gameListTemp.size()>=deliveryBusinessService.getSHIPPING_FREE_DOLLS_NUM()
                            /*||deliveryBusinessService.isFirstDelivery(currentUserId)*/) {
                        result = deliveryBusinessService.deliveryFree(currentUserId, gameListTemp, addressId, deliveryOrderListReturn);
                    } else if (gameListTemp.size()>0) {
                        result = deliveryBusinessService.delivery(pay_type, currentUserId,gameListTemp,addressId,deliveryOrderListReturn);
                    }
                    if (result !=null) {
                        if (result.isSuccess()) {
                            List<GameStatusInfo> gameStatusInfoList=new ArrayList<>();
                            for (Game game : gameListTemp) {
                                game.setIsDelivery(1);
                                game.setShippingStatus(Game.GAME_GOODS_SHIPPING_STATUS_INITIAL);
                                gameStatusInfoList.add(new GameStatusInfo(game));
                            }
                            gameServiceCache.save(gameListTemp);
                            ResponseData responseData = new ResponseData();
                            responseData.put("game_status_list", gameStatusInfoList);
                            DeliveryOrder deliveryOrder = null;
                            if (deliveryOrderListReturn.size()>0) {
                                responseData.put("delivery_order",new DeliveryOrderInfo(deliveryOrderListReturn.get(0)));
                                deliveryOrder = deliveryOrderListReturn.get(0);
                            }

                            // 防止用户在2.0版本之前快递了，升级到2.0版本以后，又再次快递，必须同步修改UserItem中的快递状态
                            List<Integer> itemIdList = new ArrayList<>();
                            for (Game game : gameListTemp) {
                                if (game.getItemId() != 0) {
                                    itemIdList.add(game.getItemId());
                                }
                            }

                            // 同步保存UserItem表
                            bagService.updateUserItemExpressStatus(itemIdList, deliveryOrder != null ? deliveryOrder.getOrderId() : 0);

                            return responseData;
                        } else {
                            return new ErrorResponse(result);
                        }
                    }
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }

    @RequestMapping(value="/get_game_info")
    public ResponseData get_game_info(HttpServletRequest request,
                                      @RequestParam Map<String,String> allRequestParams) {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("ids");
            String idsString=requestContext.getStringValue("ids");

            if (idsString!=null&&idsString.length()>0) {
                List<Integer> ids = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(idsString,"|");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken();
                    if (NumberValidationUtil.isWholeNumber(nextToken)) {
                        ids.add(Integer.valueOf(nextToken));
                    }
                }

                if (ids.size()>0) {
                    List<Game> list = gameServiceCache.findAll(ids);
                    if (list==null) {
                        list=new ArrayList<>();
                    }
                    List<GameStorageSecondsInfo> gameInfoList=new ArrayList<>();
                    long goodsMaxStorageTime = deliveryBusinessService.getGoodsMaxStorageTime();
                    long currentMilliseconds = System.currentTimeMillis();
                    for (Game game : list) {
                        gameInfoList.add(new GameStorageSecondsInfo(game, goodsMaxStorageTime, currentMilliseconds));
                    }
                    return new ResponseData().put("list",gameInfoList);
                } else {
                    return new ResponseData().put("list",new ArrayList<>());//返回空
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }

    @RequestMapping(value="/list_games_deliverable")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_games_deliverable(HttpServletRequest request,
                                               @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            List<Game> list = gameServiceCache.findGamesDeliverable(currentUserId);
            if (list==null) {
                list=new ArrayList<>();
            }
            List<GameStorageSecondsInfo> gameInfoList=new ArrayList<>();
            long goodsMaxStorageTime = deliveryBusinessService.getGoodsMaxStorageTime();
            long currentMilliseconds = System.currentTimeMillis();
            for (Game game : list) {
                GameStorageSecondsInfo temp = new GameStorageSecondsInfo(game, goodsMaxStorageTime, currentMilliseconds);
                if (temp.getStorageExpiredTime() > 0) {
                    gameInfoList.add(temp);
                }
            }

            int isFirstDelivery = 0;//deliveryBusinessService.isFirstDelivery(currentUserId)?1:0;
            ResponseData responseData = new ResponseData();
            responseData.put("list", gameInfoList);
            responseData.put("shipping_is_free", isFirstDelivery);
            responseData.put("shipping_free_dolls_num", deliveryBusinessService.getSHIPPING_FREE_DOLLS_NUM());
            responseData.put("shipping_pay_coins", deliveryBusinessService.getSHIPPING_PAY_COINS_NUM());
            responseData.put("shipping_pay_points", deliveryBusinessService.getGoodsDeliveryPoints());
            return responseData;
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    @RequestMapping(value="/list_games")
    public ResponseData list_games(HttpServletRequest request,
                                   @RequestParam Map<String,String> allRequestParams) {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
            requestContext.validateInputParams("user_id");
            int userId = requestContext.getInegerValue("user_id");
            String resultsString = requestContext.getStringValue("results"); //0|1|2

            List<Integer> results = new ArrayList<>();
            if (resultsString!=null&&resultsString.length()>0) {
                StringTokenizer tokenizer = new StringTokenizer(resultsString,"|");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken();
                    if (NumberValidationUtil.isWholeNumber(nextToken)) {
                        results.add(Integer.valueOf(nextToken));
                    }
                }
            }

            PaginationRequestData paginationData = PaginationRequestData.create(allRequestParams);
            List<Game> list;
            if (results!=null&&results.size()>0) {
                list = gameServiceCache.findGames(userId,results,paginationData.getPage(), paginationData.getSize());
            } else {
                list = gameServiceCache.findGames(userId,paginationData.getPage(), paginationData.getSize());
            }
            if (list==null) {
                list=new ArrayList<>();
            }
            List<GameStorageSecondsInfo> gameInfoList=new ArrayList<>();
            long goodsMaxStorageTime = deliveryBusinessService.getGoodsMaxStorageTime();
            long currentMilliseconds = System.currentTimeMillis();
            for (Game game : list) {
                gameInfoList.add(new GameStorageSecondsInfo(game, goodsMaxStorageTime, currentMilliseconds));
            }
            boolean hasMore = gameInfoList!=null&&gameInfoList.size()>=paginationData.getSize()?true:false;
            PaginationRspData rsp = new PaginationRspData(gameInfoList,hasMore);
            rsp.put("shipping_max_storage_expired", goodsMaxStorageTime);
            return rsp;
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    @RequestMapping(value="/get_room_rewards")
    public ResponseData list_room_games(HttpServletRequest request,
                                        @RequestParam Map<String,String> allRequestParams) {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
            requestContext.validateInputParams("room_id");
            int roomId = requestContext.getInegerValue("room_id");

            List<GameRewardInfo> list = gameRewardService.range(roomId,0,16);
            if (list==null) {
                list=new ArrayList<>();
            }
            return new ResponseData().put("list",list);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    @RequestMapping(value="/get_recent_rewards")
    public ResponseData get_recent_rewards(HttpServletRequest request,
                                           @RequestParam Map<String,String> allRequestParams) {
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);
            List<GameRewardInfo> list = gameRewardService.range(0, 8);
            if (list==null) {
                list = new ArrayList<>();
            }
            return new ResponseData().put("list",list);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }


    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/report_game_start")
    //@PreAuthorize("hasRole('ROLE_AUTHOER')")
    public ResponseData report_game_start(HttpServletRequest request,
                                          @RequestParam Map<String,String> allRequestParams) {

        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            //return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("start_info");
            String jsonString=requestContext.getStringValue("start_info");
            if (jsonString!=null) {
                ReportGameStartInfo startInfo= JsonUtil.convertJson2Model(jsonString, ReportGameStartInfo.class);
                if (startInfo!=null) {
                    Game game = gameServiceCache.findOne(startInfo.getGameId());
                    if (game!=null
                            &&game.getRoomId()==startInfo.getRoomId()
                            &&game.getPlayerId()== startInfo.getPlayerId()) {

                        game.setIsInUnlimit(startInfo.getIsInUnlimit());
                        game.setStatus(Game.GAME_STATUS_IN_GAME);
                        game.setStartTime(System.currentTimeMillis());
                        gameServiceCache.save(game);

                        return new ResponseData();
                    }
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }

    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/report_game_result")
    //@PreAuthorize("hasRole('ROLE_AUTHOER')")
    public ResponseData report_game_result(HttpServletRequest request,
                                           @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            //return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("result_info");
            String jsonString=requestContext.getStringValue("result_info");
            if (jsonString!=null) {
                ReportGameResultInfo resultInfo=JsonUtil.convertJson2Model(jsonString, ReportGameResultInfo.class);
                if (resultInfo!=null) {
                    Game game = gameServiceCache.findOne(resultInfo.getGameId());
                    if (game != null
                            && game.getRoomId() == game.getRoomId()
                            && game.getPlayerId() == resultInfo.getPlayerId()) {
                        game.setIsInUnlimit(resultInfo.getIsInUnlimit());
                        game.setStatus(Game.GAME_STATUS_GAME_OVER);
                        if (resultInfo.getResult() > 0) {
                            game.setResult(Game.GAME_RESULT_SUCCESS);
                        } else {
                            game.setResult(Game.GAME_RESULT_FAILURE);
                        }
                        game.setEndTime(System.currentTimeMillis());

                        if (resultInfo.getResult()>0) {

                            User user = userServiceCache.findOne(game.getPlayerId());

                            GameRewardInfo gameRewardInfo = new GameRewardInfo();
                            gameRewardInfo.setUserId(game.getPlayerId());
                            String avatarUrl = "";
                            String nickName = resultInfo.getNickName()!=null?resultInfo.getNickName():"";

                            if (user!=null&&user.getAvatar()!=null&&user.getAvatar().length()>0) {
                                avatarUrl = URLHelper.fullUrl(user.getAvatar());
                            } else {
                                avatarUrl = resultInfo.getAvatar()!=null?resultInfo.getAvatar():"";
                            }
                            gameRewardInfo.setAvatar(avatarUrl);
                            gameRewardInfo.setNickName(nickName);
                            gameRewardInfo.setGameId(game.getGameId());
                            gameRewardInfo.setRoomId(game.getRoomId());
                            String name = game.getName()!=null?game.getName():"";
                            gameRewardInfo.setName(name);
                            String fullImageUrl = URLHelper.fullUrl(game.getImageUrl());
                            if (fullImageUrl == null)
                                fullImageUrl = "";
                            gameRewardInfo.setImageUrl(fullImageUrl);
                            gameRewardInfo.setTimestamp(System.currentTimeMillis());
                            gameRewardService.leftPush(gameRewardInfo);

                            // 历史抓取记录
                            CatchHistoryInfo catch_history_info = new CatchHistoryInfo();
                            catch_history_info.setUserId(game.getPlayerId());
                            catch_history_info.setNickName(nickName);
                            catch_history_info.setUserIcon(avatarUrl);
                            catch_history_info.setRoomId(game.getRoomId());
                            catch_history_info.setRoomName(name);
                            catch_history_info.setRoomIcon(fullImageUrl);
                            catch_history_info.setTimestamp(System.currentTimeMillis());
                            catchHistoryServiceCache.leftPush(catch_history_info);

                            // 保存历史
                            UserItem new_item = bagService.userCatch(user, game.getGameId(), game.getRoomId());
                            if (new_item != null) {
                                logger.info("user catch success, acquire item. uid: {} game_id: {} item_type: {} item_id: {}"
                                        , user.getUserId(), game.getGameId(), new_item.getItemType()
                                        , new_item.getItemId());

                                // 抓到的不是碎片，抓到成功数量加1
                                if (new_item.getItemClassify() != ItemServiceCache.CLASSIFY_FRAGMENT) {
                                    UserCount userCount = userCountServiceCache.findOne(game.getPlayerId());
                                    if (userCount==null) {
                                        userCount=new UserCount();
                                        userCount.setUserId(game.getPlayerId());
                                    }
                                    userCount.setSuccessGrabs(userCount.getSuccessGrabs() + 1);
                                    userCountServiceCache.save(userCount);
                                }

                                // 2.0开始，game抓取成功时会和user_item绑定
                                game.setItemId(new_item.getItemId());
                            }
                        }
                        gameServiceCache.save(game);
                        return new ResponseData();
                    }
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }
}

