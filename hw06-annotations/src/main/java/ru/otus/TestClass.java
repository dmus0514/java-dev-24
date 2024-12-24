package ru.otus;

import lombok.extern.slf4j.Slf4j;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

@Slf4j
public class TestClass {

    @Before
    public void testBefore() {
        log.info("testBefore > start testBefore");
    }

    @Test
    public void test1() {
        log.info("test1 > start test1");
    }

    @Test
    public void test2() {
        log.info("test2 > start test2");
        throw new RuntimeException("Runtime exception is occurred");
    }

    @Test
    public void test3() {
        log.info("test3 > start test3");
    }

    @Test
    public void test4() {
        log.info("test4 > start test4");
        throw new IllegalArgumentException("IllegalArgumentException is occurred");
    }

    @After
    public void testAfter() {
        log.info("testAfter > start testAfter");
    }
}
