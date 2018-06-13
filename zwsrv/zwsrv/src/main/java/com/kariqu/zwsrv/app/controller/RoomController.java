package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.error.ErrorDefs;
import com.kariqu.zwsrv.app.model.*;
import com.kariqu.zwsrv.app.service.*;
import com.kariqu.zwsrv.app.transaction.IsTryAgain;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.*;
import com.kariqu.zwsrv.thelib.persistance.service.GameService;
import com.kariqu.zwsrv.thelib.security.CurrentUserDetails;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import com.kariqu.zwsrv.thelib.util.JsonUtil;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by simon on 24/11/17.
 */
@RestController
@RequestMapping("room/v1")
public class RoomController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    RoomServiceCache roomServiceCache;

    @Autowired
    AccountBusinessService accountBusinessService;

    @Autowired
    UserCountServiceCache userCountServiceCache;

    @Autowired
    GameService gameService;

    @Autowired
    NoticeBusinessService noticeBusinessService;

    @Autowired
    private RoomSortServiceCache roomSortServiceCache;

    @Autowired
    private RoomTypeServiceCache roomTypeServiceCache;


    public static final int GAME_PAY_WAY_POINTS = 1;
    public static final int GAME_PAY_WAY_COINS = 2;

    //下位机:
    //1. 下位机启动获取roomInfo 根据roomId;


    //客户端开始游戏过程:
    //1.获取房间信息(room/v1/get_room_info)更新本地房间信息
    //2.如果房间空闲，则通过IM系统预约(保证排队在最前面)
    //3.预约成功后，调用接口付款并开始游戏(room/v1/pay_game) 返回gameInfo
    //4.如果成功正式开始游戏传gameSN，如果失败看返回代码提示用户；
    //5.下位机报告游戏开始状态以及游戏结果等:gameSN

    @IsTryAgain
    @Transactional(readOnly = false, rollbackFor={ Exception.class})
    @RequestMapping(value="/pay_game")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData pay_game(HttpServletRequest request,
                                 @RequestParam Map<String,String> allRequestParams) {

        CurrentUserDetails currentUserDetails = SecurityUtil.currentUser();
        if (currentUserDetails==null || currentUserDetails.getUserId()==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        int currentUserId = SecurityUtil.currentUserId();

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("room_id","is_in_unlimit");
            int roomId = requestContext.getInegerValue("room_id");
            int isInUnlimit = requestContext.getInegerValue("is_in_unlimit");
            if (roomId>0) {
                Room room= roomServiceCache.findOne(roomId);
                if (room!=null) {
                    if (room.getIsOnline()>0) {
                        if (room.getStatus()==Room.ROOM_STATUS_IDLE) {
                            if (room.getMaintStatus()==Room.ROOM_MAINT_STATUS_INITIAL) {

                                //进入无线模式后，不需要付款
                                Game game = new Game(room);
                                game.setPlayerId(currentUserId);
                                game.setPlayerName(currentUserDetails.getNickName()!=null?currentUserDetails.getNickName():"");
                                game.setPlayerRole(currentUserDetails.getRole());
                                game.setIsInUnlimit(isInUnlimit);

                                if (isInUnlimit>0) {
                                    //进入无线模式
                                    game.setIsInUnlimit(isInUnlimit);
                                    game.setPayStatus(Game.GAME_PAY_STATUS_INITIAL);
                                    game.setCoinsPaid(0);
                                    game = gameService.save(game);

                                    UserCount userCount = userCountServiceCache.findOne(currentUserId);
                                    if (userCount==null) {
                                        userCount=new UserCount();
                                        userCount.setUserId(currentUserId);
                                    }
                                    userCount.setTotalGrabs(userCount.getTotalGrabs()+1);
                                    userCountServiceCache.save(userCount);
                                    return new ResponseData().put("game_info",new GameInfo(game));
                                } else {
                                    //付款
                                    game = gameService.save(game);
                                    ErrorCode.ErrorEntry result=accountBusinessService.payGameCoins(currentUserId, game);
                                    if (result.isSuccess()) {
                                        game.setPayStatus(Game.GAME_PAY_STATUS_PAID);
                                        game.setPayWay(GAME_PAY_WAY_COINS);
                                        game.setCoinsPaid(room.getRoomCost());
                                        game = gameService.save(game);

                                        UserCount userCount = userCountServiceCache.findOne(currentUserId);
                                        if (userCount==null) {
                                            userCount=new UserCount();
                                            userCount.setUserId(currentUserId);
                                        }
                                        userCount.setTotalGrabs(userCount.getTotalGrabs()+1);
                                        userCountServiceCache.save(userCount);
                                        return new ResponseData().put("game_info",new GameInfo(game));
                                    } else {
                                        return new ErrorResponse(result);
                                    }
                                }
                            }  else {
                                if (room.getMaintStatus()==Room.ROOM_MAINT_STATUS_TESTING) {
                                    return new ErrorResponse(ErrorDefs.ERROR_CODE_GAME_MAINT_STATUS_TESTING);
                                }
                                else if (room.getMaintStatus()==Room.ROOM_MAINT_STATUS_FILLING) {
                                    return new ErrorResponse(ErrorDefs.ERROR_CODE_GAME_MAINT_STATUS_FILLING);
                                }
                                else if (room.getMaintStatus()==Room.ROOM_MAINT_STATUS_UNDER_MAINTING) {
                                    return new ErrorResponse(ErrorDefs.ERROR_CODE_GAME_MAINT_STATUS_UNDER_MAINTING);
                                }
                            }


                        } else {
                            if (room.getStatus()==Room.ROOM_STATUS_IN_GAME) {
                                return new ErrorResponse(ErrorDefs.ERROR_CODE_ROOM_STATUS_IN_GAME);
                            }
                        }
                    } else {
                        return new ErrorResponse(ErrorDefs.ERROR_CODE_GAME_ROOM_UNDER_LINE);
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



    //分页 order
    @RequestMapping(value="/list_online_rooms")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_online_rooms(HttpServletRequest request,
                                          @RequestParam Map<String,String> allRequestParams) {

        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            //return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            int parentId=requestContext.getInegerValue("parent_id",0,"");

            String appId = requestContext.getHeader("appid");

            List<RoomInfo> roomInfoList=new ArrayList<>();

            PaginationRequestData paginationData = PaginationRequestData.create(allRequestParams);
            List<Room> roomList= roomServiceCache.findOnlineRooms(paginationData.getPage(), paginationData.getSize());
            if (roomList!=null) {
                for (Room room : roomList) {
                    roomInfoList.add(new RoomInfo(room));
                }
            }
            boolean hasMore = roomInfoList.size()>=paginationData.getSize()?true:false;
            return new PaginationRspData(roomInfoList,hasMore);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

    // 获取房间列表，新版本，没有分页
    @RequestMapping(value="/list_online_rooms_type")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_online_rooms_type(HttpServletRequest request, @RequestParam Map<String,String> requestParams)
    {
        /*
        int user_id = SecurityUtil.currentUserId();
        if (user_id == 0) {
            //return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        */
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, requestParams);
            int sort_group = requestContext.getInegerValue("sort_group");
            int type = requestContext.getInegerValue("type");

            // 只能是2倍数
            if (type >= 2) {
                if ( type % 2 != 0) {
                    type = 0;
                }
            }

            List<RoomSort> room_sort_list = null;
            if (sort_group == RoomSortServiceCache.ROOM_SROT_GROUP_RANDOM) {
                // 获取所有房间排序
                List<RoomSort> all_room_sort_list = roomSortServiceCache.findAllValidRoomSort();
                // 随机取一个
                room_sort_list = randomOneGroup(all_room_sort_list);
            } else {
                // 获取指定组
                room_sort_list = roomSortServiceCache.findValidRoomSortBySortGroup(sort_group);
            }
            // 去重同一个组内相同的room_id, 并排序
            room_sort_list = groupDistinctSort(room_sort_list);

            int gp = 0;
            if (!room_sort_list.isEmpty())
                gp = room_sort_list.get(0).getSortGroup();

            // 获取房间
            List<Room> room_list = roomServiceCache.findAllOnlineRooms();

            room_list = roomSort(room_list, room_sort_list);

            // 获取分类
            List<RoomInfo> roomInfoList = new ArrayList<>();
            for (Room room : room_list) {
                // 0是全部
                if (type == 0 || (room.getRoomType() & type) != 0)
                    roomInfoList.add(new RoomInfo(room));
            }

            int rsp_sort_troup = RoomSortServiceCache.ROOM_SROT_GROUP_RANDOM;
            ResponseData rsp = new ResponseData().put("list", roomInfoList);
            if (!room_sort_list.isEmpty()) {
                rsp_sort_troup = room_sort_list.get(0).getSortGroup();
            }
            //logger.warn("srot_group: {} rsp_sort_group: {}", sort_group, rsp_sort_troup);
            rsp.put("sort_group", rsp_sort_troup);
            return rsp;
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }


    @RequestMapping(value="/get_room_info")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData get_room_info(HttpServletRequest request,
                                      @RequestParam Map<String,String> allRequestParams,
                                      HttpServletResponse response) {

        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            //return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
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
                    List<RoomInfo> roomInfoList=new ArrayList<>();
                    List<Room> roomList= roomServiceCache.findAll(ids);
                    if (roomList!=null) {
                        for (Room room : roomList) {
                            roomInfoList.add(new RoomInfo(room));
                        }
                    }
                    return new ResponseData().put("list",roomInfoList);
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
    @RequestMapping(value="/report_room_status")
    //@PreAuthorize("hasRole('ROLE_AUTHOER')")
    public ResponseData report_room_status(HttpServletRequest request,
                                           @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            //return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("status_info");
            String jsonString=requestContext.getStringValue("status_info");
            if (jsonString!=null) {
                ReportRoomStatusInfo reportInfo=JsonUtil.convertJson2Model(jsonString, ReportRoomStatusInfo.class);
                if (reportInfo!=null) {
                    Room room= roomServiceCache.findOne(reportInfo.getRoomId());
                    if (room!=null) {
                        room.setUserNum(reportInfo.getUserNum());
                        room.setGoodsNum(reportInfo.getGoodsNum());
                        room.setIsInUnlimit(reportInfo.getIsInUnlimit());
                        room.setStatus(reportInfo.getStatus());
                        room.setMaintStatus(reportInfo.getMaintStatus());
                        room.setIsOnline(reportInfo.getIsOnline());
                        room.setLastReportTime(System.currentTimeMillis());
                        room= roomServiceCache.save(room);
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
    @RequestMapping(value="/report_room_error")
    //@PreAuthorize("hasRole('ROLE_AUTHOER')")
    public ResponseData report_room_error(HttpServletRequest request,
                                          @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            //return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("error_info");
            String jsonString=requestContext.getStringValue("error_info");
            if (jsonString!=null) {
                ReportRoomErrorInfo errorInfo=JsonUtil.convertJson2Model(jsonString, ReportRoomErrorInfo.class);
                if (errorInfo!=null) {
                    Room room= roomServiceCache.findOne(errorInfo.getRoomId());
                    if (room!=null) {
                        room.setErrorCode(errorInfo.getErrorCode());
                        room.setLastReportTime(System.currentTimeMillis());
                        roomServiceCache.save(room);
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

    // 获取room_type
    @RequestMapping(value="/get_room_type")
    //@PreAuthorize("hasRole('ROLE_AUTHOER')")
    public ResponseData get_room_type(HttpServletRequest request, @RequestParam Map<String,String> requestParams)
    {
        try {
            List<RoomType> list = roomTypeServiceCache.findAllValidRoomType();
            List<RoomTypeInfo> info_list = new ArrayList<>();
            for (RoomType rt : list) {
                info_list.add(new RoomTypeInfo(rt));
            }
            return new ResponseData().put("list", info_list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 随机选择一个组
    private List<RoomSort> randomOneGroup(List<RoomSort> all_room_sort)
    {
        // 按sort_group 分组
        try {
            Map<Integer, List<RoomSort>> group = new HashMap<>();
            for (RoomSort rs : all_room_sort) {
                int sort_group = rs.getSortGroup();
                List<RoomSort> list = group.get(sort_group);
                if (list == null) {
                    list = new ArrayList<>();
                    group.put(sort_group, list);
                }
                list.add(rs);
            }

            List<Integer> temp = new ArrayList<>();
            for (Integer v : group.keySet()) {
                temp.add(v);
            }
            if (temp.isEmpty())
                return new ArrayList<>();
            Collections.shuffle(temp);
            int sort_group = temp.get(0);
            List<RoomSort> ret = group.get(sort_group);
            if (ret != null)
                return ret;
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("randomOneGroup exception {}", e.toString());
            return new ArrayList<>();
        }
    }

    // 对组去重，排序
    private List<RoomSort> groupDistinctSort(List<RoomSort> group)
    {
        if (group.isEmpty())
            return group;
        Map<Integer, RoomSort> distinct_map = new HashMap<>();
        for (RoomSort rs : group) {
            if (distinct_map.containsKey(rs.getRoomId())) {
                log.warn("repeated room sort. room_id: {} group: {} ", rs.getRoomId(), rs.getSortGroup());
                continue;
            }
            distinct_map.put(rs.getRoomId(), rs);
        }
        List<RoomSort> distinct_list = null;
        if (distinct_map.size() == group.size()) {
            distinct_list = group;
        } else {
            distinct_list = new ArrayList<>();
            for (Map.Entry<Integer, RoomSort> e : distinct_map.entrySet()) {
                distinct_list.add(e.getValue());
            }
        }

        Collections.sort(distinct_list, new Comparator<RoomSort>() {
            @Override
            public int compare(RoomSort a, RoomSort b) {
                return a.getSortValue() - b.getSortGroup();
            }
        });
        return distinct_list;
    }

    // 对room按 room sort 排序
    private List<Room> roomSort(List<Room> room_list, List<RoomSort> group)
    {
        if (group == null || group.isEmpty())
            return room_list;
        List<Room> room_order = new ArrayList<>();

        for (RoomSort rs : group) {
            Room room = pickUpRoom(room_list, rs.getRoomId());
            if (room != null) {
                room_order.add(room);
            }
        }
        // 可能存在room比room_sort多的情况, 把剩下room添加在最后
        for (Room r : room_list) {
            room_order.add(r);
        }
        return room_order;
    }

    private Room pickUpRoom(List<Room> room_list, int room_id)
    {
        for (int i = 0; i != room_list.size(); ++ i) {
            Room room = room_list.get(i);
            if (room.getRoomId() == room_id) {
                room_list.remove(i);
                return room;
            }
        }
        return null;
    }

}
