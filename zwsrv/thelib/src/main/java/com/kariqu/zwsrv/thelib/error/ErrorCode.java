package com.kariqu.zwsrv.thelib.error;

import com.kariqu.zwsrv.thelib.model.BaseModel;

/**
 * Created by simon on 10/03/17.
 */
public class ErrorCode {

    public static class ErrorEntry extends BaseModel {

        private int key;
        private String value;

        public ErrorEntry(int key, String value) {
            this.key   = key;
            this.value = value;
        }

        public ErrorEntry(ErrorEntry entry) {
            this.key   = entry.getKey();
            this.value = entry.getValue();
        }

        public ErrorEntry setErrMsg(String msg) {
            this.value = value;
            return this;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public boolean isSuccess() {
            return getKey()==ERROR_SUCCESS.getKey()?true:false;
        }
    }


    public static final int ERROR_CODE_BASE = 6000;
    public static final ErrorEntry ERROR_SUCCESS = new ErrorEntry(0, "success");
    public static final ErrorEntry ERROR_UNKNOWN = new ErrorEntry(ERROR_CODE_BASE, "未知错误");
    public static final ErrorEntry ERROR_ACCESS_DENIED = new ErrorEntry(ERROR_CODE_BASE+1, "Access Denied");
    public static final ErrorEntry ERROR_UNAUTHORIZED_ENTRY = new ErrorEntry(ERROR_CODE_BASE+2, "Unauthorized Entry");
    public static final ErrorEntry ERROR_NO_IMPL = new ErrorEntry(ERROR_CODE_BASE+3, "接口未实现");
    public static final ErrorEntry ERROR_INVALID_PARAMETERS = new ErrorEntry(ERROR_CODE_BASE+4, "参数有误");
    public static final ErrorEntry ERROR_EXCEPTION = new ErrorEntry(ERROR_CODE_BASE+5, "服务器异常");
    public static final ErrorEntry ERROR_SERVER_INNER = new ErrorEntry(ERROR_CODE_BASE+6, "服务器内部错误");
    public static final ErrorEntry ERROR_FAILED = new ErrorEntry(ERROR_CODE_BASE+7, "本次操作失败");
    public static final ErrorEntry ERROR_ALREADY_EXIST = new ErrorEntry(ERROR_CODE_BASE+8, "已存在");

    public static final ErrorEntry ERROR_SERVER_CONCURRENT_OPERATION_RETRY = new ErrorEntry(ERROR_CODE_BASE+10, "请稍后再次重试");//乐观锁

    public static final ErrorEntry ERROR_AUTHENTICATION = new ErrorEntry(ERROR_CODE_BASE+49, "用户名或密码错误");
    public static final ErrorEntry ERROR_TOKEN_EXPIRED= new ErrorEntry(ERROR_CODE_BASE+50, "token无效");
    public static final ErrorEntry ERROR_INVALID_CAPTCHA = new ErrorEntry(ERROR_CODE_BASE+51, "验证码无效");

}
