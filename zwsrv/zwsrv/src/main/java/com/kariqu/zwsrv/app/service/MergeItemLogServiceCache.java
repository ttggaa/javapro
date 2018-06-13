package com.kariqu.zwsrv.app.service;

import com.kariqu.zwsrv.app.model.PaginationRequestData;
import com.kariqu.zwsrv.app.utility.Utility;
import com.kariqu.zwsrv.thelib.persistance.entity.MergeItemLog;
import com.kariqu.zwsrv.thelib.persistance.service.MergeItemLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MergeItemLogServiceCache extends MergeItemLogService {

    public List<MergeItemLog> findMergeItemList(int userId, PaginationRequestData paginationRequest)
    {
        return mergeItemLogDAO.findAll(userId, Utility.createPageable(paginationRequest));
    }
}
