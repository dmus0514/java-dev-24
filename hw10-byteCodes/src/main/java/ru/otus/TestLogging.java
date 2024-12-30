package ru.otus;

import lombok.extern.slf4j.Slf4j;
import ru.otus.annotations.Log;

@Slf4j
public class TestLogging implements TestLoggingInterface{

    @Log
    @Override
    public void calculation() { }

    @Log
    @Override
    public void calculation(int param1) {
        param1 += 1;
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        param1 += param2;
    }

    @Override
    public void calculation(int param1, int param2, int param3) {
        param1 = param2 + param3;
    }
}
