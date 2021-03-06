package com.kariqu.zwsrv.thelib.util;

import java.lang.annotation.*;

/**
 * @Title: SensitiveInfo.java
 * @Copyright: Copyright (c) 2015
 * @Description: <br>
 *               敏感信息注解标记 <br>
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SensitiveInfo {

    public SensitiveInfoUtil.SensitiveType type() ;
}