package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.model.ResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 23/04/17.
 */
@EqualsAndHashCode(callSuper=false)
@SuppressWarnings("rawtypes")
@Data
public class PaginationRspData extends ResponseData {

    public PaginationRspData() {
        put("hasNextPage",0);
        put("list",new ArrayList<>());
        put("timestamp",System.currentTimeMillis());
    }

    public PaginationRspData(List list, boolean hasNextPage){
        put("hasNextPage",hasNextPage?1:0);
        put("list",list);
        put("timestamp",System.currentTimeMillis());
    }
}
