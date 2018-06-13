package com.kariqu.zwsrv.app.pay.tenpay;

import com.kariqu.zwsrv.app.pay.tenpay.entity.WXPayReqData;
import com.kariqu.zwsrv.app.pay.tenpay.entity.WXPayResData;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceWeb extends WeixinPayService {

	public Map<String, Object> generateWeixinReq(WXPayReqData reqData) {
		reqData.setAppid(Configure.getJs_appID());
		reqData.setTrade_type(Configure.getTRADE_TYPE_JS());
		reqData.setMch_id(Configure.getJs_mchID());
		String payServiceResponseString = null;
		try {
			payServiceResponseString = request(reqData);
			if (!Signature.checkIsJsSignValidFromResponseString(payServiceResponseString)) {
	        	logger.warn("【支付失败】支付请求PrepayId返回的数据签名验证失败，有可能数据被篡改了");
	            return null;
	        }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WXPayResData wxPayResData = (WXPayResData) Util.getObjectFromXML(payServiceResponseString, WXPayResData.class);
		
		if (wxPayResData == null || wxPayResData.getReturn_code() == null) {
			logger.warn("【H5支付失败】支付请求PrepayId逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
			return null;
		}
		
		if (wxPayResData.getResult_code().equals("SUCCESS") && 
        		wxPayResData.getReturn_code().equals("SUCCESS")) {
        	
        	String prepayid = wxPayResData.getPrepay_id();
            logger.info("【H5获取prepayId成功: {} 】",prepayid);
            
    		Map<String,Object> result = new HashMap<String, Object>(); 
    		if (null != prepayid && !"".equals(prepayid)) {
    			result.put("appId", Configure.getJs_appID());
    			result.put("timeStamp",Util.getTimeStamp());
    			result.put("nonceStr", wxPayResData.getNonce_str());
    			result.put("package", "prepay_Id="+prepayid);
    			result.put("signType","MD5");
    			
    			result.put("paySign", Signature.getJsSign(result));
    			
    			logger.info("生成H5支付sign是:{}",wxPayResData.getSign());
    			logger.info("返回H5支付Map是:{}",result);
    			
    			return result;
    			
    		}
        } else if(wxPayResData.getResult_code().equals("FAIL")){
          
          logger.warn("[H5中的微信支付]: 请求prepayId失败, return_code为FAIL错误");
        	
          return null;
        } else {
        	
        	//获取错误码
            String errorCode = wxPayResData.getErr_code();
            //获取错误描述
            String errorCodeDes = wxPayResData.getErr_code_des();
            logger.warn("[H5中微信支付]: 请求prepayId失败, 错误码:{},错误描述:{}",errorCode,errorCodeDes);
        	
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

			WXPayReqData reqData = new WXPayReqData(Configure.getJs_appID(),
					Configure.getMchid(),
					orderDesc,null,orderSN,totalFee,Configure.getBillCreateIP(),timeStart,timeExpire);

//			WXPayReqData reqData = createPayReqData(orderSN, orderSubject, orderDesc, totalFee, startTimeInMillis, remainTimeInMillis);
			reqData.setOpenid(openId);
			return generateWeixinReq(reqData);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
