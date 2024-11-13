package ru.otus;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class HelloOtus {
    public static void main(String[] args) {
        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(10).build();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        System.out.println("Guava cache example output: " + cache.asMap());
    }
}
