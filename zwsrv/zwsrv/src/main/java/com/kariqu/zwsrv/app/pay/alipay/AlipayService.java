package com.kariqu.zwsrv.app.pay.alipay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * @author darren
 *
 * Creation:2014年12月20日
 */
public class AlipayService {


	/**
	 * 生成支付宝订单信息APP
	 * 
	 * @param //notifyUrl
	 *            异步通知URL, 支付宝返回支付结果时请求的url
	 * @param orderSN
	 *            本地生成的订单编号
	 * @param orderSubject
	 *            订单标题
	 * @param orderDesc
	 *            订单具体商品描述(可为空)
	 * @param totalFee
	 *            订单金额(两位小数)
	 * @return 客户端支付URL
	 * @throws Exception
	 */
	public static String buildPayParams(String orderSN,
										String orderSubject,
										String orderDesc,
										int totalFee,
										long startTimeInMillis, long remainTimeInMillis) throws Exception {

		//订单金额(两位小数)
		BigDecimal totalFeeDouble = new BigDecimal(totalFee);
		totalFeeDouble = totalFeeDouble.divide(totalFeeDouble, 100, RoundingMode.HALF_UP);
		String totalFeeString = String.valueOf(totalFeeDouble.doubleValue());

		long remainInMin = remainTimeInMillis/(60*1000);
		String remainInMinString = String.valueOf(remainInMin)+"m";

		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.NOTIFY_URL);
		sParaTemp.put("it_b_pay", remainInMinString);
		/* 业务参数 */
		sParaTemp.put("seller_id", AlipayConfig.seller_email);// 卖家支付宝帐户
		sParaTemp.put("out_trade_no", orderSN); // 商户订单号
		sParaTemp.put("subject", orderSubject); // 订单名称
		sParaTemp.put("total_fee", totalFeeString); // 付款金额
		sParaTemp.put("body", orderDesc); // 订单描述
		StringBuilder orderInfo = new StringBuilder(
				AlipayCore.createLinkString(sParaTemp));

		String sign = RSA.sign(orderInfo.toString(), AlipayConfig.private_key,
				"utf-8");
		String encodeSign = encode(sign, "utf-8");
		orderInfo.append("&sign=\"" + encodeSign + "\"").append(
				"&sign_type=\"" + AlipayConfig.sign_type + "\"");

		return orderInfo.toString();
	}

	public static String encode(String str, String code) {
		try {
			return URLEncoder.encode(str, code);
		} catch (Exception ex) {
			ex.fillInStackTrace();
			return "";
		}
	}

	public static boolean empty(String param) {
		return param == null || param.trim().length() < 1;
	}

	/**
	 * Desc: 生成WAP/WEB端支付宝支付表单
	 *
	 * @param orderSN
	 * @param orderSN
	 * @param orderDesc
	 * @param totalFee
	 * @param payOn
	 * @param returnUrl
	 * @return
	 * @throws Exception
	 *             return:String
	 */
	public static String buildPayParams(String orderSN,
										String orderSubject,
										String orderDesc,
										int totalFee,
										long startTimeInMillis, long remainTimeInMillis,
										int payOn,
										String returnUrl) throws Exception {

		//订单金额(两位小数)
		BigDecimal totalFeeDouble = new BigDecimal(totalFee);
		totalFeeDouble = totalFeeDouble.divide(totalFeeDouble, 100, RoundingMode.HALF_UP);
		String totalFeeString = String.valueOf(totalFeeDouble.doubleValue());


		// //////////////////////////////////调用授权接口alipay.wap.trade.create.direct获取授权码token//////////////////////////////////////

		// 支付类型
		String payment_type = "1";
		// 必填，不能修改
		// 服务器异步通知页面路径
		String notify_url = AlipayConfig.NOTIFY_URL;
		// 需http://格式的完整路径，不能加?id=123这类自定义参数

		// 页面跳转同步通知页面路径
		String return_url = empty(returnUrl) ? AlipayConfig.CALL_BACK_URL:returnUrl;
		// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost

		// 防钓鱼时间戳
		String anti_phishing_key = AlipaySubmit.query_timestamp();
		// 若要使用请调用类文件submit中的query_timestamp函数

		// 客户端的IP地址
		String exter_invoke_ip = "";
		// 非局域网的外网IP地址，如：221.0.0.1

		// ////////////////////////////////////////////////////////////////////////////////

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.pc_service);

		if(payOn == AlipayConfig.PAY_ON_PC) {
			sParaTemp.put("seller_email", AlipayConfig.seller_email);
		} else {
			sParaTemp.put("service",AlipayConfig.wap_service);
			sParaTemp.put("seller_id", AlipayConfig.seller_email);
		}

		sParaTemp.put("partner", AlipayConfig.partner);

		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("out_trade_no", orderSN);
		sParaTemp.put("subject", orderSubject);
		sParaTemp.put("total_fee", totalFeeString);
		sParaTemp.put("body", orderDesc);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
		return sHtmlText;
	}
}
