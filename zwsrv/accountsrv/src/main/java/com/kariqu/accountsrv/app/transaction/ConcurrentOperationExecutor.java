package com.kariqu.accountsrv.app.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Component;

/**
 * Created by simon on 10/06/17.
 */

/**
 * Class for implementing a finite number of retries when a GAE datastore transaction fail.
 * @author mattiaslevin
 */
@Aspect
@Component
public class ConcurrentOperationExecutor implements Ordered {

    static final Logger LOG = LoggerFactory.getLogger(ConcurrentOperationExecutor.class);

    private static final int DEFAULT_MAX_RETRIES = 5;
    private static final int DEFAULT_WAIT = 120;

    private int maxRetries = DEFAULT_MAX_RETRIES;
    private int order = 1;

    @Around(value = "@annotation(isTryAgain)")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp, IsTryAgain isTryAgain) throws Throwable {


//        int numAttempts = 0;
//        PessimisticLockingFailureException lockFailureException = null;
//        do {
//            numAttempts++;
//            try {
//                return pjp.proceed();
//            } catch (PessimisticLockingFailureException ex) {
//                lockFailureException = ex;
//            }
//        } while (numAttempts <= this.maxRetries);
//        throw lockFailureException;

        int numberOfAttempts = 0;

        ConcurrencyFailureException concurrencyFailureException=null;
        do {
            numberOfAttempts++;
            try {
                return pjp.proceed();
            }
            catch(ConcurrencyFailureException exception) {
                LOG.info("Datastore transaction failed due to Concurrent Failure Exception. Attempt: " + numberOfAttempts);
                concurrencyFailureException = exception;
            }

            // Sleep for a short time
            if (numberOfAttempts <= this.maxRetries)
                Thread.sleep(DEFAULT_WAIT * numberOfAttempts);

        }
        while(numberOfAttempts <= this.maxRetries);

        LOG.warn("Datastore transaction failed max number of tries due to Concurrent Modification Exception, not more retries.");
        throw concurrencyFailureException;
    }

    // Getters and setters
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

//@Aspect
//@Component
//public class ConcurrentOperationExecutor implements Ordered {
//
//    private static final int DEFAULT_MAX_RETRIES = 2;
//
//    private int maxRetries = DEFAULT_MAX_RETRIES;
//    private int order = 1;
//
//    public void setMaxRetries(int maxRetries) {
//        this.maxRetries = maxRetries;
//    }
//
//    @Override
//    public int getOrder() {
//        return this.order;
//    }
//
//    // @Pointcut("execution(* com..creepers.service.impl..saveOrUpdate(..))")
//    // public void saveOrUpdate() {
//    // }
//
//    @Around(value = "@annotation(isTryAgain)")
//    public Object doConcurrentOperation(ProceedingJoinPoint pjp,IsTryAgain isTryAgain) throws Throwable {
//        int numAttempts = 0;
//        PessimisticLockingFailureException lockFailureException = null;
//        do {
//            numAttempts++;
//            try {
//                return pjp.proceed();
//            } catch (PessimisticLockingFailureException ex) {
//                lockFailureException = ex;
//            }
//        } while (numAttempts <= this.maxRetries);
//        throw lockFailureException;
//    }
//
//}

//http://lib.csdn.net/article/javaee/62590
//http://blog.csdn.net/lzp158869557/article/details/53096231

//https://github.com/sosandstrom/open-server/blob/master/transactions/src/main/java/com/wadpam/open/transaction/ConcurrentOperationExecutor.java


//http://blog.csdn.net/zhanghongzheng3213/article/details/50819539


