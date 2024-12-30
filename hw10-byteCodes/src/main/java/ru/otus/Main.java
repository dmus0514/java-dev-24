package ru.otus;

import ru.otus.aop.Ioc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        TestLoggingInterface testLogging = Ioc.createLoggingService();
        testLogging.calculation();
        testLogging.calculation(5);
        testLogging.calculation(5, 10);
        testLogging.calculation(5, 10, 15);
    }
}