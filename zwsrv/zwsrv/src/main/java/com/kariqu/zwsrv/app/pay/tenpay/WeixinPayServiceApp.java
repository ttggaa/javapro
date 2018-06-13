package com.kariqu.zwsrv.app.pay.tenpay;

import com.kariqu.zwsrv.app.pay.tenpay.entity.WXPayReqData;
import com.kariqu.zwsrv.app.pay.tenpay.entity.WXPayResData;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceApp extends WeixinPayService {

	Map<String, Object> generateWeixinReq(WXPayReqData reqData) {
		if(reqData == null) {
			return null;
		}
		reqData.setAppid(Configure.getAppid());
		reqData.setTrade_type(Configure.getTRADE_TYPE_APP());
		reqData.generateSign();
		String payServiceResponseString = null;
		try {
			payServiceResponseString = request(reqData);
			if (!Signature.checkIsSignValidFromResponseString(payServiceResponseString)) {
	        	logger.warn("【支付失败】支付请求PrepayId返回的数据签名验证失败，有可能数据被篡改了");
	            return null;
	        }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WXPayResData wxPayResData = (WXPayResData) Util.getObjectFromXML(payServiceResponseString, WXPayResData.class);
		
		if (wxPayResData == null || wxPayResData.getReturn_code() == null) {
			logger.warn("【支付失败】支付请求PrepayId逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
			return null;
		}
		
		if (wxPayResData.getResult_code().equals("SUCCESS") && 
        		wxPayResData.getReturn_code().equals("SUCCESS")) {
        	
        	String prepayid = wxPayResData.getPrepay_id();
            logger.info("【获取prepayId成功: {} 】",prepayid);
            
    		Map<String,Object> result = new HashMap<String, Object>(); 
    		if (null != prepayid && !"".equals(prepayid)) {
    			result.put("appid", wxPayResData.getAppid());
    			result.put("nonceStr", wxPayResData.getNonce_str());
    			result.put("package", "Sign=WXPay");
    			result.put("partnerId", wxPayResData.getMch_id());
    			result.put("prepayId", prepayid);
    			result.put("timestamp", Util.getTimeStamp());
    			
    			result.put("sign", Signature.getSign(result));
    			result.remove("package");
    			result.put("packagestr", "Sign=WXPay");
    			
    			logger.info("生成的支付sign是:{}",wxPayResData.getSign());
    			logger.info("返回的支付Map是:{}",result);
    			
    			return result;
    			
    		}
        } else if(wxPayResData.getResult_code().equals("FAIL")){
          
          logger.warn("[微信支付]: 请求prepayId失败, return_code为FAIL错误");
        	
          return null;
        } else {
        	
        	//获取错误码
            String errorCode = wxPayResData.getErr_code();
            //获取错误描述
            String errorCodeDes = wxPayResData.getErr_code_des();
            logger.warn("[微信支付]: 请求prepayId失败, 错误码:{},错误描述:{}",errorCode,errorCodeDes);
        	
            return null;
        }
		
		return null;
	}

	@Override
	public Map<String, Object> buildPayParams(String orderSN, String orderSubject, String orderDesc, int totalFee, long startTimeInMillis, long remainTimeInMillis,String openId) {
		try {

			logger.info("用户:{}通过App进行微信支付生成支付订单{}\n",orderSN);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStart = simpleDateFormat.format(startTimeInMillis);
			String timeExpire = simpleDateFormat.format(startTimeInMillis+remainTimeInMillis);

			WXPayReqData reqData = new WXPayReqData(Configure.getAppid(),
					Configure.getMchid(),
					orderDesc,"",orderSN,totalFee,Configure.getBillCreateIP(),timeStart,timeExpire);

//			WXPayReqData reqData = createPayReqData(orderSN, orderSubject, orderDesc, totalFee, startTimeInMillis, remainTimeInMillis);
			return generateWeixinReq(reqData);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
