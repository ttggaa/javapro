package com.kariqu.zwsrv.app.pay.alipay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */
@Component
public class AlipayConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088711949161205";
	// 商户的私钥
	public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMWt1x7R72xTFm04"
			+ "fVHG3NxnGNj0h2zTcRpfOOGS5LJBli4cZxBYNPPHDed1q75TZJ7aS7IvzB3dKbMZ"
			+ "v5VUfitYXkhjUJPuAHLf8rEguFahW00RMN3E8OcWAUfFPwbCeeLJwyxcNUWESCxF"
			+ "4Wf5uAjYCpOJORWQTA8GaCgy4tKlAgMBAAECgYA3HSC9FwsJyKCYEwfdpPQu/w+O"
			+ "bZ7UWJ9QGvxg5/ObP23/rSuPAwjAbt8RiV0400dagsJYC7t9GAxoQuWHXOdP8DuI"
			+ "orisFKqtnHWM+ksC1r+1DQHdmrnDwUYA81XydOzA8LLPBQsqPTiX8dxg8dbfl8w5"
			+ "t9robmAyWwZy/4c4AQJBAPtxvGqW4t15tHuWyG1mJki1pBb/spsVU2d0mm8mh/Tc"
			+ "bwvf0wJrC0+XJV6QEcTLR8bQqIVfKOzr6L9K9PMuRRECQQDJQroststUWsZcsWiz"
			+ "4bJW0W6D9WQmZFc52Got33m4GyDV0lDyTkW+vUipnGTNeUmasyv3dQtxfUf4TsTf"
			+ "16RVAkEAvKRHx3AMJDz6uv4UwH6ymjosMVUEmnUVbbh9ZIZz2bDHXxE9LcNBaSTs"
			+ "VFWh/xLDdtV0mRsbiSQ0KdACla9RAQJAOwIcceW6YeYniJ/fcfY3gqru+zOkhHkg"
			+ "9e3U29RM7MTPcMG0SAbY/h6jQk9/YpswEHJ97pTboQ+5XT1DDdo87QJBAJS894WY"
			+ "SkxAM7d1Xiw6D9V91tJ8SFoT8poL+81uB3Y1y3a8FLtnElAd9QT5ZqBVLdLGoy42"
			+ "gJAeW+6wS43LW+Q=";
	// 签约支付宝账号或卖家收款支付宝帐户
	public static String seller_email = "caiwu@aidingmao.com";
	// 支付类型
	public static String payment_type = "1";
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	public static String key = "y9oh7fhqscazmp0108hw17qreq2epeee";
	// 接口名称
	public static String service = "mobile.securitypay.pay";

    public static String wap_service = "alipay.wap.create.direct.pay.by.user";

    public static String pc_service = "create_direct_pay_by_user";

	public static String NOTIFY_URL;

	@Value("${alipay.notify_url}")
	public void setNotify_URL(String notify_url) {
		AlipayConfig.NOTIFY_URL = notify_url;
	}

	public static String CALL_BACK_URL;

	@Value("${alipay.call_back_url}")
	public void setCall_Back_URL(String call_back_url) {
		AlipayConfig.CALL_BACK_URL = call_back_url;
	}

    public static String REFUND_NOTIFY_URL;
    @Value("${alipay.refund_notify_url}")
    public void setRefund_Notify_URL(String refund_notify_url) {
        AlipayConfig.REFUND_NOTIFY_URL = refund_notify_url;
    }

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "/alipay_log/";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
//	public static String input_charset = "gbk";

	// 签名方式 不需修改
	public static String sign_type = "RSA";
	
	public static String md5_sign_type = "MD5";

    public static int PAY_ON_PC = 0;
    public static int PAY_ON_WAP = 1;

}
