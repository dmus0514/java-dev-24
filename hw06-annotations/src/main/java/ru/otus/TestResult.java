package ru.otus;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TestResult {
    private int totalCount;
    private int successfulCount;
    private int failedCount;
    private final List<String> failedTestsDetails = new ArrayList<>();

    public void processTestResult(String testName, boolean isSuccess, Throwable e) {
        totalCount++;
        if (isSuccess) {
            successfulCount++;
        } else {
            failedCount++;
            failedTestsDetails.add("Test case \"" + testName + "\": " + e.getCause());
        }
    }
}
