package com.Optimart.aspects;


import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.logging.Logger;

@Component
@Aspect
public class UserActivityLogger {
    private Logger logger = Logger.getLogger(getClass().getName());

    //named pointcut
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Around("controllerMethods() && execution(* com.project.shopapp.controllers.UserController.*(..))")
    public Object logUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        logger.info("User activity started: " + methodName + ", IP address: " + remoteAddress);

        Object result = joinPoint.proceed();

        logger.info("User activity finished: " + methodName);
        return result;
    }
}

