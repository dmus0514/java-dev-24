package ru.otus.aop;

import lombok.extern.slf4j.Slf4j;
import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class Ioc {
    private Ioc() {}

    public static <T> T createLoggingService(T object) {
        InvocationHandler handler = new MyInvocationHandler<>(object);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(), object.getClass().getInterfaces(), handler);
    }

    static class MyInvocationHandler<T> implements InvocationHandler {
        private final T myObject;
        private final Set<String> annotatedMethods;

        MyInvocationHandler(T myObject) {
            this.myObject = myObject;
            annotatedMethods = Arrays.stream(myObject.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .map(method -> method.getName() + Arrays.toString(method.getParameterTypes()))
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodSignature = method.getName() + Arrays.toString(method.getParameterTypes());
            if (annotatedMethods.contains(methodSignature)) {
                log.info("executed method: {}, params: {}", method.getName(), (args != null) ? args : "[]");
            }
            return method.invoke(myObject, args);
        }

    }
}
