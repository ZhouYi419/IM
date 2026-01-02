/*
package com.zy.im.infrastructure.mybatis;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class TimeAutoFillInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object parameter = invocation.getArgs()[1];

        if (parameter != null) {
            fillTime(parameter);
        }
        return invocation.proceed();
    }

//=======================private==============================
    private void fillTime(Object obj){
        LocalDateTime now = LocalDateTime.now();

        try {
            Class<?> clazz = obj.getClass();

            Field createdTime = getField(clazz , "createdTime");
            Field updatedTime = getField(clazz , "updatedTime");

            if (createdTime != null){
                createdTime.setAccessible(true);
                if (createdTime.get(obj) == null){
                    createdTime.set(obj,now);
                }
            }

            if (updatedTime != null) {
                updatedTime.setAccessible(true);
                updatedTime.set(obj, now);
            }
        }catch (Exception e){
            throw new RuntimeException("自动填充时间失败", e);
        }
    }

    private Field getField(Class<?> clazz , String name){
        while(clazz != null){
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }
}
*/
