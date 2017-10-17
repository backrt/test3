package com.tt.cache;

public interface CacheMapProtocol<T> {

    boolean containsKey(String key);

    T get(String key);

    boolean put(String key, T t);

    T create(String key);

    T create(String key, int radius);

    void clear();

    int size();

    T load(String key);

    T load(String key, int radius);

    void remove(String key);
}
