package com.kariqu.zwsrv.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by simon on 08/11/17.
 */
public class BaseController {

    /**
     * The Logger for this class.
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Handles JPA NoResultExceptions thrown from web service controller
     * methods. Creates a response with Exception Attributes as JSON and HTTP
     * status code 404, not found.
     *
     * @param noResultException A NoResultException instance.
     * @param request The HttpServletRequest in which the NoResultException was
     *        raised.
     * @return A ResponseEntity containing the Exception Attributes in the body
     *         and HTTP status code 404.
     */
//    @ExceptionHandler(NoResultException.class)
//    public ResponseEntity<Map<String, Object>> handleNoResultException(
//            NoResultException noResultException, HttpServletRequest request) {
//
//        logger.info("> handleNoResultException");
//
//        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();
//
//        Map<String, Object> responseBody = exceptionAttributes
//                .getExceptionAttributes(noResultException, request,
//                        HttpStatus.NOT_FOUND);
//
//        logger.info("< handleNoResultException");
//        return new ResponseEntity<Map<String, Object>>(responseBody,
//                HttpStatus.NOT_FOUND);
//    }


}
