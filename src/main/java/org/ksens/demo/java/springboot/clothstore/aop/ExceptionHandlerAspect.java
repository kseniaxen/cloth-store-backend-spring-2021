package org.ksens.demo.java.springboot.clothstore.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.utils.ErrorsGetter;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class ExceptionHandlerAspect {
    @Around("execution(* org.ksens.demo.java.springboot.clothstore.repositories.*.*(..))")
    public Object onDaoException(ProceedingJoinPoint pjp) throws Exception {
        Object output = null;
        try {
            output = pjp.proceed();
        } catch (Exception ex) {
            if (ErrorsGetter.getException(ex).contains("ConstraintViolationException")){
                throw new ConstraintViolationException("", null, "");
            }
            throw ex;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return output;
    }

    @Around("execution(* org.ksens.demo.java.springboot.clothstore.services.*.*(..))")
    public Object onServiceException(ProceedingJoinPoint pjp) {
        Object output = null;
        try {
            output = pjp.proceed();
        } catch (ConstraintViolationException ex) {
            output =
                    ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message("This name is already taken")
                            .build();
        } catch (Exception ex) {
            System.err.println("Unknown Error");
            ex.printStackTrace();
            output =
                    ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message("Unknown Error")
                            .build();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return output;
    }
}
