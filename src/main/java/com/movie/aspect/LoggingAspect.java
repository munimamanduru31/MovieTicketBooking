package com.movie.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.movie.service.MovieService.*(..))")
    public void logBeforeServiceMethod() {
        System.out.println("AOP: Movie service method called.");
    }
}