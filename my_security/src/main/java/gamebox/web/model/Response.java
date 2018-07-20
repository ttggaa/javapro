package gamebox.web.model;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private int code;
    private String msg;
    private Map<String, Object> data;

    public Response(){
        this.code = 0;
        this.msg = "success";
        this.data = new HashMap<String, Object>();
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = new HashMap<String, Object>();
    }

    public Response put(String key, Object value) {
        this.data.put(key,value);
        return this;
    }

    public Response remove(String key) {
        this.data.remove(key);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
