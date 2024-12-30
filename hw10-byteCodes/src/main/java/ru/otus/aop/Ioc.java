package ru.otus.aop;

import lombok.extern.slf4j.Slf4j;
import ru.otus.TestLogging;
import ru.otus.TestLoggingInterface;
import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class Ioc {
    private Ioc() {}

    public static TestLoggingInterface createLoggingService() {
        InvocationHandler handler = new MyInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    static class MyInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;

        MyInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method myMethod = myClass.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (myMethod.isAnnotationPresent(Log.class)) {
                log.info("executed method: {}, params: {}", myMethod.getName(), args);
            }
            return myMethod.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "MyInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
