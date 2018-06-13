package com.kariqu.zwsrv.app.model;

import com.kariqu.zwsrv.thelib.util.StringUtil;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by simon on 23/04/17.
 */
@Data
public class PaginationRequestData {

    public static final Timestamp MAX_TIME = new Timestamp(4099651200000L);

    public static final boolean ORDER_BY_TIME_DESC = true;

    int page;
    int size;
    long timestamp;

    public PaginationRequestData(){
        this(0,40,0L);
    }

    public PaginationRequestData(int page, int size){
        this(page,size,0L);
    }

    public PaginationRequestData(int page, int size, long timestamp){
        this.page = page;
        this.size = size;
        this.timestamp = timestamp;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static PaginationRequestData create(Map<String, String> allRequestParams) {
        return create(allRequestParams, ORDER_BY_TIME_DESC);
    }

    public static PaginationRequestData create(Map<String,String> allRequestParams,
                                               boolean orderByTimeDesc) {

        PaginationRequestData data = new PaginationRequestData();


        if(!StringUtil.isEmpty(allRequestParams.get("page")) ){
            data.setPage(Math.max(Integer.parseInt(allRequestParams.get("page")), 0) );
        }

        if( !StringUtil.isEmpty(allRequestParams.get("size")) ){
            data.setSize(Math.max(Integer.parseInt(allRequestParams.get("size")), 1) );
        }

        //上次请求服务器的时间戳，目前没有用
        if (allRequestParams.get("timestamp")!=null) {
            if(orderByTimeDesc && data.getPage()==0) {
                data.setTimestamp(PaginationRequestData.MAX_TIME.getTime()); //刷新时间＝最大值
            }else if(!StringUtil.isEmpty(allRequestParams.get("timestamp")) ) {
                data.setTimestamp(Math.max(Long.parseLong(allRequestParams.get("timestamp")), 0)) ;
            }
        }
        return data;
    }
}
