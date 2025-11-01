package com.miku.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})   // 可打在类或方法上
@Retention(RetentionPolicy.RUNTIME)               // 运行期保留
public @interface SkipJwt {
}