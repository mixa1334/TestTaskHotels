package com.hotels.hotels.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("com.hotels.hotels.logging.Pointcuts.allLoggableMethods()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = methodSignature.getName();
        logger.info("!!!!!!!!!!!!!!!!!method call {}.{} with args:", className, methodName);
        Class<?>[] paramTypes = methodSignature.getParameterTypes();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            logger.info("!!!!!!!!!!!!!!!!!arg[{}]: type={}, value={}", i, paramTypes[i].getSimpleName(), args[i]);
        }
        Object result;
        try {
            result = joinPoint.proceed();
            logger.info("!!!!!!!!!!!!!!!!!method call {}.{}() returned[{}] = {}", className, methodName,
                    methodSignature.getReturnType(), result);
        } catch (Throwable t) {
            logger.info("!!!!!!!!!!!!!!!!!error on call: {}.{}() threw {} with message {}", className, methodName, t.toString(),
                    t.getMessage());
            throw t;
        }
        return result;
    }
}
