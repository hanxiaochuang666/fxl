package com.by.blcu.core.aop;


import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CourseCheck {
    boolean isCheck() default true;
}
