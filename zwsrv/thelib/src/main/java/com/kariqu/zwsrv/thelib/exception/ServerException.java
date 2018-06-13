package com.kariqu.zwsrv.thelib.exception;

import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.model.ResponseData;

/**
 * Created by simon on 4/27/16.
 */
public class ServerException extends RuntimeException {

    public ErrorCode.ErrorEntry entry;

    public ServerException(){};

    public ServerException(Throwable t)  {
        super(t.toString(), t);
    }

    public ServerException(ErrorCode.ErrorEntry entry){
        this.entry = entry;
    }

    public ResponseData toResponseData() {
        if (getCause()!=null) {
            return new ResponseData(ErrorCode.ERROR_EXCEPTION.getKey(),getMessage());
        }
        if (entry==null) {
            entry=ErrorCode.ERROR_UNKNOWN;
        }
        return new ResponseData(entry.getKey(),entry.getValue());
    }

    public String toString(){
        String s = "code: " + entry.getKey()
                + "\nmsg: " + entry.getValue()
                + "\n";
        s += super.toString();
        return s;
    }
}
