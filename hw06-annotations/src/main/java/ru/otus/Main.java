package ru.otus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        TestResult result = TestRunner.runTests("ru.otus.TestClass");

        log.info("Test results:");
        log.info("-".repeat(30));
        log.info("Total executed tests count: " + result.getTotalCount());
        log.info("Successful tests count: " + result.getSuccessfulCount());
        log.info("Failed tests count: " + result.getFailedCount());

        if (result.getFailedCount() > 0) {
            log.info("Failed tests details:");
            for (String detail : result.getFailedTestsDetails()) {
                log.info("* " + detail);
            }
        }

    }
}