package com.kariqu.zwsrv.app.pay.tenpay;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:40
 * 这里放置各种配置数据
 */

//支付｛
//		商户ID：1485897822
//		公众号APPID wxc404d5aa4b781b89
//		api密钥：fd50ee907a3a7f0c7a5ddda88d2056da
//		｝


@Component
@Data
public class Configure {

    //sdk的版本号
    private static final String sdkVersion = "java sdk 1.0.1";
    //这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	private static String key = "5ce1809b57db192ffecb9b9008fad7ca";//"74a6bdf3ae2b355b6aff589b928aee0e";
	//微信分配的公众号ID（开通公众号之后可以获取到）
	private static String appID = "wx10772583ba2a35a4";//"wx093f3eb21ceb7937";
	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	private static String mchID = "1493968322";//"1240473202";
	
	
	private static String js_key = "VKHe1CKGmZds6mkiHsELDwaeC4RBIhwA";
	private static String js_appID = "wxaa6b08745d827e94";
	private static String js_mchID = "1226141302";


	private static String billCreateIP = "192.168.1.111";//"120.27.250.213";//"121.41.46.28";

	//受理模式下给子商户分配的子商户号
	private static String subMchID = "";

	//HTTPS证书的本地路径
	private static String certLocalPath = "/usr/local/aidingmao/weixin/apiclient_cert.p12";

	//HTTPS证书密码，默认密码等于商户号MCHID
	private static String certPassword = "1240473202";

	//是否使用异步线程的方式来上报API测速，默认为异步模式
	private static boolean useThreadToDoReport = true;

	//机器IP
	private static String ip = "";
	
	//交易类型
	private static String TRADE_TYPE_APP = "APP";
	
	private static String TRADE_TYPE_JS = "JSAPI";
	
	//1）统一下单API
	private static String PAY_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	//2）退款API
    private static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	//3）退款查询API
	public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

	public static String NOTIFY_URL;
	@Value("${wxpay.notify_url}")
	public void setAPPKEY(String appkey){
		Configure.NOTIFY_URL = appkey;
	}

	public static boolean isUseThreadToDoReport() {
		return useThreadToDoReport;
	}

	public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
		Configure.useThreadToDoReport = useThreadToDoReport;
	}

	public static String HttpsRequestClassName = "com.kariqu.zwsrv.app.pay.tenpay.HttpsRequest";

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static void setMchID(String mchID) {
		Configure.mchID = mchID;
	}

	public static void setSubMchID(String subMchID) {
		Configure.subMchID = subMchID;
	}

	public static void setCertLocalPath(String certLocalPath) {
		Configure.certLocalPath = certLocalPath;
	}

	public static void setCertPassword(String certPassword) {
		Configure.certPassword = certPassword;
	}

	public static void setIp(String ip) {
		Configure.ip = ip;
	}

	public static String getKey(){
		return key;
	}
	
	public static String getAppid(){
		return appID;
	}
	
	public static String getMchid(){
		return mchID;
	}

	public static String getSubMchid(){
		return subMchID;
	}
	
	public static String getCertLocalPath(){
		return certLocalPath;
	}
	
	public static String getCertPassword(){
		return certPassword;
	}

	public static String getIP(){
		return ip;
	}

	public static void setHttpsRequestClassName(String name){
		HttpsRequestClassName = name;
	}

	public static String getPAY_API() {
		return PAY_API;
	}

    public static String getRefundApi() {
        return REFUND_API;
    }

	public static String getJs_appID() {
		return js_appID;
	}

	public static void setJs_appID(String js_appID) {
		Configure.js_appID = js_appID;
	}

	public static String getJs_key() {
		return js_key;
	}

	public static void setJs_key(String js_key) {
		Configure.js_key = js_key;
	}

	public static String getTRADE_TYPE_APP() {
		return TRADE_TYPE_APP;
	}

	public static void setTRADE_TYPE_APP(String tRADE_TYPE_APP) {
		TRADE_TYPE_APP = tRADE_TYPE_APP;
	}

	public static String getTRADE_TYPE_JS() {
		return TRADE_TYPE_JS;
	}

	public static void setTRADE_TYPE_JS(String tRADE_TYPE_JS) {
		TRADE_TYPE_JS = tRADE_TYPE_JS;
	}

	public static String getJs_mchID() {
		return js_mchID;
	}

	public static void setJs_mchID(String js_mchID) {
		Configure.js_mchID = js_mchID;
	}
    public static String getSdkVersion(){
        return sdkVersion;
    }


	public static String getBillCreateIP() {
		return Configure.billCreateIP;
	}
}
