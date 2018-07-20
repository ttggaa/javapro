package gamebox.web.model;

public class ResponseError {
    public static final Response ERROR_SUCCESS = new Response(0, "success");
    public static final Response ERROR_UNKNOWN = new Response(400, "未知错误");
    public static final Response ERROR_PASSWORD = new Response(401, "用户名密码错误");
}
