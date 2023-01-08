package com.quanta.vi.interceptor;

import java.lang.annotation.*;

/**
 * @description 权限注解，在controller中对方法添加该注解可以结合拦截器检验用户是否有权限访问该接口
 * @author quanta
 * @date 2021/9/25
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiredPermission {
    int[] value();
}
