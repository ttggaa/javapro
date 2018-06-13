package com.kariqu.zwsrv.thelib.model;

import com.kariqu.zwsrv.thelib.error.ErrorCode;

/**
 * Created by simon on 07/04/17.
 */
public class ErrorResponse extends ResponseData {
    public ErrorResponse(ErrorCode.ErrorEntry errorCode) {
        super(errorCode.getKey(),errorCode.getValue());
    }
}
