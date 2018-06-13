package com.kariqu.wssrv.app.error;

public class ErrorCode {

    public static class ErrorResult
    {
        private int code;
        private String msg;

        public ErrorResult(int code, String msg)
        {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }


    public static final ErrorResult ERROR_SUCCESS = new ErrorResult(0, "success");

    public static final int ERROR_CODE_ERROR = 5000;
    public static final ErrorResult ERROR_UNKNOWN = new ErrorResult(ERROR_CODE_ERROR + 1, "未知错误");
    public static final ErrorResult ERROR_UNLOGIN = new ErrorResult(ERROR_CODE_ERROR + 2, "未登陆");
    public static final ErrorResult ERROR_REPEATED_LOGIN = new ErrorResult(ERROR_CODE_ERROR + 3, "已经login,无法再次login");
    public static final ErrorResult ERROR_PARSE_PARAMS = new ErrorResult(ERROR_CODE_ERROR + 4, "解析data参数出错");
    public static final ErrorResult ERROR_ROUTER_TARGET_NULL = new ErrorResult(ERROR_CODE_ERROR + 5, "无法找到路由目标");
    public static final ErrorResult ERROR_ENTER_ROOM_NULL = new ErrorResult(ERROR_CODE_ERROR + 6, "无法找到房间");

}
