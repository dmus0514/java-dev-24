package ru.otus;

import lombok.extern.slf4j.Slf4j;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
public class TestRunner {
    public static TestResult runTests(String className) {
        TestResult testResult = new TestResult();
        try {
            Class<?> testClass = Class.forName(className);
            var testMethods = new ArrayList<Method>();
            var beforeMethods = new ArrayList<Method>();
            var afterMethods = new ArrayList<Method>();

            Arrays.stream(testClass.getDeclaredMethods()).forEach(method -> {
                if (method.isAnnotationPresent(Test.class)) {
                    testMethods.add(method);
                } else if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.add(method);
                } else if (method.isAnnotationPresent(After.class)) {
                    afterMethods.add(method);
                }
            });

            for (Method testMethod : testMethods) {
                Object testClassInstance = testClass.getDeclaredConstructor().newInstance();
                boolean isSuccess = true;
                TestExecutionException testException = null;

                try {
                    for (Method beforeMethod : beforeMethods) {
                        callMethod(beforeMethod, testClassInstance);
                    }
                    callMethod(testMethod, testClassInstance);
                } catch (TestExecutionException e) {
                    isSuccess = false;
                    testException = e;
                } finally {
                    try {
                        for (Method method : afterMethods) {
                            callMethod(method, testClassInstance);
                        }
                    } catch (TestExecutionException e) {
                        isSuccess = false;
                        testException = e;
                    }
                }

                testResult.processTestResult(testMethod.getName(), isSuccess, testException);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return testResult;
    }

    private static void callMethod(Method method, Object instance) throws TestExecutionException{
        try {
            method.setAccessible(true);
            method.invoke(instance);
        } catch (Exception e) {
            throw new TestExecutionException("Exception occurred while execution " + method.getName(), e.getCause());
        }
    }
}
