package com.kariqu.zwsrv.app.pay;

import com.kariqu.zwsrv.app.pay.wxpay.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon on 02/03/17.
 */
@Service
public class PayRouteService {

    public static final String PAY_WAY_ALIPAY = "alipay";
    public static final String PAY_WAY_WXPAY = "wxpay";

    //对于支付宝，网页支付(PC，H5)需要传递pay_on参数
    public static final int PAY_ON_PC = 0;//AlipayConfig.PAY_ON_PC;
    public static final int PAY_ON_WAP = 1;//AlipayConfig.PAY_ON_WAP;
    public static final int PAY_ON_APP = 10;//

    static public boolean isValidPayWay(String payWay) {
        if (payWay!=null&&payWay.equalsIgnoreCase(PAY_WAY_WXPAY)) {
            return true;
        }
        return false;
    }

    static public boolean isValidPayOn(int payOn) {
        if (payOn==PAY_ON_APP) {
            return true;
        }
        return false;
    }


    public static Map<String,String> buildWeixinPayParams(int payOn, String orderSN,
                                                   String orderSubject,
                                                   String orderDesc,
                                                   int totalFee,long remainTimeInMillis) {
        if (payOn==PAY_ON_APP) {
            WXPayConfigImpl config = new WXPayConfigImpl();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<>();
            data.put("body", orderDesc!=null?orderDesc:"");
            data.put("out_trade_no", orderSN);
            data.put("device_info", "");
            data.put("fee_type", "CNY");
            data.put("total_fee", String.valueOf(totalFee));
            data.put("spbill_create_ip", getSpbillCreateIp());
            data.put("notify_url", getNotifyUrl());
            data.put("trade_type", "APP");

            try {
                // {nonce_str=T6MTIq9oUHS70PtD, appid=wx10772583ba2a35a4, sign=7E6CEB14B6F1017B6ECB13A051FDAE03, trade_type=APP, return_msg=OK, result_code=SUCCESS, mch_id=1493968322, return_code=SUCCESS, prepay_id=wx20171218174418e143b220170749834710}

                Map<String, String> resp = wxpay.unifiedOrder(data);
                System.out.println(resp);
                if (resp.get("appid")!=null
                        &&resp.get("nonce_str")!=null
                        &&resp.get("prepay_id")!=null
                        &&resp.get("sign")!=null) {

                    Map<String,String> result = new HashMap<>();
                    result.put("appid",resp.get("appid"));
                    result.put("nonce_str",resp.get("nonce_str"));
                    result.put("package", "Sign=WXPay");
                    result.put("partner_id", config.getMchID());
                    result.put("prepay_id", resp.get("prepay_id"));
                    result.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
                    result.put("sign", WXPayUtil.generateSignedXml(result, config.getKey()));

                    return result;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }



    static String notifyUrl;
    static String spbillCreateIp;

    @Value("${wxpay.notify_url}")
    public void setNotifyUrl(String notifyUrl){
        PayRouteService.notifyUrl = notifyUrl;
    }



    @Value("${wxpay.spbill_create_ip}")
    public void setSpbillCreateIp(String spbillCreateIp) {
        PayRouteService.spbillCreateIp = spbillCreateIp;
    }

    private static String getNotifyUrl() {
        return PayRouteService.notifyUrl!=null?PayRouteService.notifyUrl:"";
    }

    private static String getSpbillCreateIp() {
        return PayRouteService.spbillCreateIp!=null?PayRouteService.spbillCreateIp:"";
    }
}

//
//微信支付商户号 1493968322
//        商户平台登录帐号 1493968322@1493968322
//        商户平台登录密码 260613
//        申请对应的应用 咔哇机抓娃娃（）
//        应用APPID wx10772583ba2a35a4
//
//API安全key: 0e3fbc2a4fbb4707a14184e6323b327c
//
//

