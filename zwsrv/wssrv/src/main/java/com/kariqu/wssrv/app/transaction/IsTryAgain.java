package com.kariqu.wssrv.app.transaction;

import java.lang.annotation.*;

/**
 * Created by simon on 10/06/17.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsTryAgain {
    // marker annotation
}