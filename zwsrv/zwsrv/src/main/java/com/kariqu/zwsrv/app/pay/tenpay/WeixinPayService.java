package com.kariqu.zwsrv.app.pay.tenpay;

import com.kariqu.zwsrv.app.pay.tenpay.entity.WXPayReqData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Service
public abstract class WeixinPayService {
	
	protected final transient Logger logger = LoggerFactory.getLogger("weixin-finance-service");
	
	//API的地址
    private String apiURL;

    //发请求的HTTPS请求器
    private IServiceRequest serviceRequest;

    public WeixinPayService() {
        apiURL = Configure.getPAY_API();
        Class c;
		try {
			c = Class.forName(Configure.HttpsRequestClassName);
			serviceRequest = (IServiceRequest) c.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {

			e.printStackTrace();
		}
    }


	//ForApp
	public Map<String, Object> buildPayParams(String orderSN,
													String orderSubject,
													String orderDesc,
													int totalFee,
													long startTimeInMillis, long remainTimeInMillis) {
		return buildPayParams(orderSN,orderSubject,orderDesc,totalFee,startTimeInMillis,remainTimeInMillis,"");
	}


	public abstract Map<String, Object> buildPayParams(String orderSN,
													   String orderSubject,
													   String orderDesc,
													   int totalFee,
													   long startTimeInMillis, long remainTimeInMillis,
													   String openId);

	protected WXPayReqData createPayReqData(String orderSN,
			  String orderSubject,
			  String orderDesc,
			  int totalFee,
			  long startTimeInMillis, long remainTimeInMillis) throws Exception {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStart = simpleDateFormat.format(startTimeInMillis);
		String timeExpire = simpleDateFormat.format(startTimeInMillis+remainTimeInMillis);

		WXPayReqData reqData = new WXPayReqData(Configure.getAppid(),
				Configure.getMchid(),
				orderDesc,null,orderSN,totalFee,Configure.getBillCreateIP(),timeStart,timeExpire);
		return reqData;
	}

	/**
	 * 请求支付服务
	 * @param wxPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	protected String request(WXPayReqData wxPayReqData) throws Exception {
		//--------------------------------------------------------------------
		//发送HTTPS的Post请求到API地址
		//--------------------------------------------------------------------
		String responseString = sendPost(wxPayReqData);
		return responseString;
	}

	protected String sendPost(Object xmlObj) throws UnrecoverableKeyException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		if (serviceRequest!=null) {
			return serviceRequest.sendPost(apiURL,xmlObj);
		}
		return "";
	}
}
