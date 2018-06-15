package com.kariqu.zwsrv.web.utilityex;

import com.kariqu.zwsrv.thelib.http.HttpRequestContext;

public class DataTableServiceSide {
    public static int draw(HttpRequestContext ctx) { return ctx.getInegerValue("draw");  }
    public static int start(HttpRequestContext ctx) { return ctx.getInegerValue("start"); }
    public static int length(HttpRequestContext ctx) { return ctx.getInegerValue("length"); }
}
