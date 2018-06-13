package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.pay.tenpay.entity.WXResponseResult;
import com.kariqu.zwsrv.app.pay.wxpay.WXPayConfigImpl;
import com.kariqu.zwsrv.app.pay.wxpay.WXPayUtil;
import com.kariqu.zwsrv.app.service.AccountBusinessService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by simon on 24/11/17.
 */
@RestController
@RequestMapping("wxpay/v1")
public class WXController extends BaseController {

    protected final transient Logger logger = LoggerFactory.getLogger("weixin-finance");

    @Autowired
    AccountBusinessService accountBusinessService;


    static String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 微信返回异步通知
     *
     * @param request
     * @param response
     * @throws Exception
     * @throws NumberFormatException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/notify")
    public void notify(HttpServletRequest request,HttpServletResponse response) throws Exception  {

        InputStream inputStream = request.getInputStream();

        String responseData = inputStreamToString(inputStream);

        if (responseData!=null&&responseData.length()>0) {
            logger.info("[收到微信支付的回复为:{}]",responseData);

            if (!WXPayUtil.isSignatureValid(responseData, WXPayConfigImpl.key)) {
                logger.warn("【支付失败】异步通知返回数据校验失败,支付请求API返回的数据签名验证失败，有可能数据被篡改了");
                return;
            }

            Map<String,String> wxNotifyDataMap = WXPayUtil.xmlToMap(responseData);
            if (wxNotifyDataMap==null) {
                logger.warn("【支付失败】异步通知返回数据解析失败");
                return;
            }

            String return_code=wxNotifyDataMap.get("return_code");
            String return_msg=wxNotifyDataMap.get("return_msg");
            String transaction_id=wxNotifyDataMap.get("transaction_id");
            String out_trade_no=wxNotifyDataMap.get("out_trade_no");
            String total_fee=wxNotifyDataMap.get("total_fee");

            if(return_code!=null&&return_code.equals("SUCCESS")
                    &&out_trade_no!=null
                    &&transaction_id!=null
                    &&total_fee!=null)
            {
                String paySN = out_trade_no;
                String tradeNo = transaction_id;
                String totalFeeString = total_fee;

                logger.info("收到微信支付回调\n" + "商户订单号:{} 微信交易号:{}",paySN, tradeNo);

                if (accountBusinessService.handleWeiXinPayNotify(paySN, tradeNo, totalFeeString,responseData)) {

                    WXResponseResult resResult = new WXResponseResult("SUCCESS","OK");
                    XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
                    xStreamForRequestPostData.alias("xml", resResult.getClass());
                    //以XML格式返回给微信
                    String postDataXML = xStreamForRequestPostData.toXML(resResult);

                    response.getWriter().print(postDataXML);
                    logger.info("微信支付处理成功");
                }
            } else {
                logger.info("微信支付失败:{}, 错误原因:{}",return_code!=null?return_code:"",return_msg!=null?return_msg:"");
            }
        }
    }

    /**
     * 事后回滚异步通知
     *
     * @param
     * @param
     * @throws Exception
     * @throws NumberFormatException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/fix_notify", method = RequestMethod.POST)
    public void fixNotify(@RequestBody Map<String,Object> wxNotify) throws Exception  {

        String responseData = (String)wxNotify.get("notify_body");

        if (responseData!=null&&responseData.length()>0) {
            logger.info("[补偿微信支付的回复为:{}]",responseData);

            if (!WXPayUtil.isSignatureValid(responseData, WXPayConfigImpl.key)) {
                logger.warn("【补偿支付失败】异步通知返回数据校验失败,支付请求API返回的数据签名验证失败，有可能数据被篡改了");
                return;
            }

            Map<String,String> wxNotifyDataMap = WXPayUtil.xmlToMap(responseData);
            if (wxNotifyDataMap==null) {
                logger.warn("【补偿支付失败】异步通知返回数据解析失败");
                return;
            }

            String return_code=wxNotifyDataMap.get("return_code");
            String return_msg=wxNotifyDataMap.get("return_msg");
            String transaction_id=wxNotifyDataMap.get("transaction_id");
            String out_trade_no=wxNotifyDataMap.get("out_trade_no");
            String total_fee=wxNotifyDataMap.get("total_fee");

            if(return_code!=null&&return_code.equals("SUCCESS")
                    &&out_trade_no!=null
                    &&transaction_id!=null
                    &&total_fee!=null)
            {

                String paySN = out_trade_no;
                String tradeNo = transaction_id;
                String totalFeeString = total_fee;

                logger.info("补偿收到微信支付回调\n" + "商户订单号:{} 微信交易号:{}",paySN, tradeNo);

                accountBusinessService.handleWeiXinPayNotify(paySN, tradeNo, totalFeeString,responseData);

            } else {
                logger.info("微信支付失败:{}, 错误原因:{}",return_code!=null?return_code:"",return_msg!=null?return_msg:"");
            }
        }

    }


}
