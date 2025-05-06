package ru.otus.cachehw;

import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> dataStore = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        dataStore.put(key, value);
        executeListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var value = dataStore.remove(key);
        executeListeners(key, value, "remove");
    }

    @Override
    public void removeAll() {
        Set<K> keys = new HashSet<>(dataStore.keySet());
        for (K key : keys) {
            remove(key);
        }
    }

    @Override
    public V get(K key) {
        var value = dataStore.get(key);
        executeListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void executeListeners(K key, V value, String action) {
        listeners.forEach(listener ->  listener.notify(key, value, action));
    }
}
