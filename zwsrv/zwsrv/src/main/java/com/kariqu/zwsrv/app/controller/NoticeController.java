package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.model.NoticeInfo;
import com.kariqu.zwsrv.app.model.PaginationRequestData;
import com.kariqu.zwsrv.app.model.PaginationRspData;
import com.kariqu.zwsrv.app.service.NoticeBusinessService;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.Notice;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 24/11/17.
 */

@RestController
@RequestMapping("notice/v1")
public class NoticeController extends BaseController {

    @Autowired
    NoticeBusinessService noticeBusinessService;


    @RequestMapping(value="/list_notices")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData list_gain_points(HttpServletRequest request,
                                         @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }

        try {
            HttpRequestContext requestContext = new HttpRequestContext(request,allRequestParams);

            PaginationRequestData paginationData = PaginationRequestData.create(allRequestParams);
            List<Notice> list = noticeBusinessService.findNotices(currentUserId, paginationData.getPage(), paginationData.getSize());
            if (list==null) {
                list=new ArrayList<>();
            }
            List<NoticeInfo> noticeInfoList = new ArrayList<>();
            for (Notice notice : list) {
                noticeInfoList.add(new NoticeInfo(notice));
            }
            boolean hasMore = noticeInfoList!=null&&noticeInfoList.size()>=paginationData.getSize()?true:false;
            return new PaginationRspData(noticeInfoList,hasMore);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }

}
