package com.nostra13.universalimageloader.cache.texture;

public interface CacheMapProtocol<M, T> {

    boolean containsKey(M key);

    T get(M key);

    boolean put(M key, T t);

    T create(M key);

//    T create(M key, int radius);

    void clear();

    int size();

    T load(M key);

//    T load(M key, int radius);

    void remove(M key);
}
