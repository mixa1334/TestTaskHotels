package com.hotels.hotels.logging;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut(value = "execution(* *(..)) && @annotation(com.hotels.hotels.logging.Loggable)")
    public void allLoggableMethods() {
    }
}
