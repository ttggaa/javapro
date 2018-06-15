package com.kariqu.zwsrv.web.controller;

import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.service.RoomService;
import com.kariqu.zwsrv.web.persistance.entityex.WebRoom;
import com.kariqu.zwsrv.web.persistance.service.WebRoomService;
import com.kariqu.zwsrv.web.persistance.entityex.WebExpressage;
import com.kariqu.zwsrv.web.persistance.service.WebDeliveryOrderService;
import com.kariqu.zwsrv.web.utilityex.AjaxResponse;
import com.kariqu.zwsrv.web.utilityex.DataTableServiceSide;
import com.kariqu.zwsrv.web.utilityex.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping(value = "/room")
public class RoomController {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    WebRoomService webRoomService;
    /*@Autowired
    WebDeliveryOrderService webDeliveryOrderService;*/

    @RequestMapping(value="room_list")
    public String roomList(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "room/room_list";
    }

    @RequestMapping(value="room_list_table", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse roomListTableGet(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        /*for (Map.Entry<String, String> it : params.entrySet()) {
            log.info("key:{} \t\t\t value:{}",it.getKey(), it.getValue());
        }*/

        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int start = requestCtx.getInegerValue("start");
            int length = requestCtx.getInegerValue("length");
            int draw = requestCtx.getInegerValue("draw");
            //System.out.println(String.format("-----start:%d length:%d draw:%d", start, length, draw));
            log.info("-----start:{} length:{} draw:{}",start, length, draw);

            long recordsTotal = webRoomService.count(1);
            long recordsFiltered = recordsTotal;

            AjaxResponse aj = new AjaxResponse<WebRoom>();
            aj.setRecordsTotal(recordsTotal);
            aj.setRecordsFiltered(recordsFiltered);
            aj.setDraw(draw);

            if (start > recordsTotal) {
                return aj;
            }

            List<WebRoom> result = webRoomService.limit(start, length, 1);
            if (result != null) {
                for (WebRoom order : result) {
                    aj.add(order);
                }
            }
            return aj;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return new AjaxResponse();
    }

    //
    @RequestMapping(value="test")
    public String test(HttpServletRequest request, @RequestParam Map<String,String> params, Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int room_id = requestCtx.getInegerValue("room_id");
            WebRoom exp = webRoomService.loadRoomInfo(room_id);
            if (exp == null) {
                //System.out.println(String.format("room room_id is %d", room_id));
                log.info("-----room room_id is {}",room_id);
                return Utility.server_error_html;
            }
            model.addAttribute("roominfo", exp);
            return "room/test";
        } catch (ServerException e) {
            return Utility.server_error_html;
        }
    }

    @RequestMapping(value = "room_pro")
    public  String roomPro(HttpServletRequest request,@RequestParam Map<String ,String> params,Model model)
    {
        if (null == request.getSession().getAttribute("admin")) {
            return "login";
        }
        return "room/room_pro";
    }

    @RequestMapping(value = "room_pro_data")
    @ResponseBody
    public ResponseData roomProData(HttpServletRequest request, @RequestParam Map<String ,String> params, Model model) {
        try {
            HttpRequestContext requestCtx = new HttpRequestContext(request, params);
            int is_online = requestCtx.getInegerValue("is_online");
            ResponseData aj = new ResponseData();

            // test
            // appid = 3177435262;
            // loginTokenAppKey = "166c578bb0b551fdc4d9b7af961f1382";

            // 正式
            // appid =1781550680;
            // loginTokenAppKey = "13ad4e35dd28b7ee710efde67dd769f2";
            // server = 'wss://wsliveroom' + appid + '-api.zego.im:8282/ws'; //wawaji接入服务器地址    --- 即构下发的server地址

            String appid;
            String loginTokenAppKey;
            String aeskey = "8e36246eeb6a43e7";
            String server;

            if (is_online == 0) {
                appid = "3177435262";
                loginTokenAppKey = "166c578bb0b551fdc4d9b7af961f1382";
            } else {
                appid = "1781550680";
                loginTokenAppKey = "13ad4e35dd28b7ee710efde67dd769f2";
            }
            server = "wss://wsliveroom" + appid + "-api.zego.im:8282/ws";
            aj.put("appid", appid);
            aj.put("loginTokenAppKey", loginTokenAppKey);
            aj.put("aeskey", aeskey);
            aj.put("server", server);
            return aj;
        } catch (Exception e) {
            //System.out.println(e);
            log.error("-----{}",e);
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }
}
