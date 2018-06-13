package com.kariqu.zwsrv.app.pay.tenpay.entity;

import lombok.Data;

@Data
public class WXResponseResult {

	//协议层
    private String return_code ;
    private String return_msg ;
    
    public WXResponseResult() {
    	
    }
    
    public WXResponseResult(String code, String msg) {
    	this.return_code = code;
    	this.return_msg = msg;
    }
}

