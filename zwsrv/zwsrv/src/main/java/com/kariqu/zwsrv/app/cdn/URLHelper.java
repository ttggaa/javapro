package com.kariqu.zwsrv.app.cdn;

import com.kariqu.zwsrv.app.Application;

/**
 * Created by simon on 25/11/17.
 */
public class URLHelper {

    public static final String baseImageUrl = "http://image.kawaji.17tcw.com";
    public static final String baseCdnUrl = "http://cdn.kawaji.17tcw.com";

    public static String fullUrl(String relativeFilePath) {
        String url=relativeFilePath;
        if (url!=null&&url.length()>0) {
            if (url.startsWith("http://")||url.startsWith("https://")) {
                //...
            }
            else {
                url = baseCdnUrl+":"+Application.CDN_PORT+"/file/v1/"+relativeFilePath;
                /*
                // 生成环境就用CDN
                if (Application.isProdEnv) {
                    url = baseCdnUrl+":"+Application.port+"/file/v1/"+relativeFilePath;
                } else {
                    url = "http://"+Application.hostAddressAndPort()+"/file/v1/"+relativeFilePath;
                }
                */
            }
        }
        return url;
    }
}


//m.kawaji.17tcw.com (h5页面)
//        api.kawaji.17tcw.com(API)
//        image.kawaji.17tcw.com(图片)
