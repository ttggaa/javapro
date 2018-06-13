package com.kariqu.zwsrv.app.utility;

import com.kariqu.zwsrv.app.model.PaginationRequestData;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.persistance.entity.UserItem;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import jdk.internal.dynalink.beans.StaticClass;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Utility {

    public static List<Integer> parseRequestToList(String str)
    {
        List<Integer> list = new ArrayList<>();
        if (str == null || str.isEmpty())
            return list;
        StringTokenizer tokenizer = new StringTokenizer(str, "|");
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            if (NumberValidationUtil.isWholeNumber(nextToken)) {
                list.add(Integer.valueOf(nextToken));
            }
        }
        return list;
    }

    public static List<Integer> parseRequestToList(String str, Logger log)
    {
        try {
            return parseRequestToList(str);
        } catch (Exception e) {
            log.warn("parseRequestToList exception: {}", str);
            return new ArrayList<>();
        }
    }

    public static Integer[] listToArray(List<Integer> list)
    {
        if (list.isEmpty())
            return new Integer[0];
        Integer[] arr = new Integer[list.size()];
        list.toArray(arr);
        return arr;
    }

    public static PageRequest createPageable(PaginationRequestData paginationParams) {
        PageRequest pageable = new PageRequest(paginationParams.getPage()
                , paginationParams.getSize()
                , Sort.Direction.DESC, "createTime");
        return pageable;
    }

    public static PaginationRequestData createPaginationRequest(HttpRequestContext requestContext, Map<String, String> originParams)
    {
        return PaginationRequestData.create(originParams);
    }

    public static long getExpiredTime(final long createtime, long goodsMaxStorageTime, long currentMilliseconds)
    {
        long temp = createtime + goodsMaxStorageTime - currentMilliseconds;
        if (temp < 0)
            temp = 0;
        return temp;
    }
}
