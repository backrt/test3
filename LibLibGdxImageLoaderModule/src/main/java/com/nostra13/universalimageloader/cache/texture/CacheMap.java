package com.nostra13.universalimageloader.cache.texture;

import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public abstract class CacheMap<M extends Disposable, T extends Disposable> implements
        CacheMapProtocol<M, T> {

    private final HashMap<M, T> cacheMap;
//    private HashMap<M, Integer> textureQuotesLists;

    protected CacheMap() {
        cacheMap = new HashMap<M, T>();
//        textureQuotesLists = new HashMap<M, Integer>();
    }

    @Override
    public boolean containsKey(M key) {
        return cacheMap.containsKey(key);
    }

    @Override
    public T get(M key) {
//        if (cacheMap.containsKey(key))
//            checkQuotes(key);

        return cacheMap.get(key);
    }

//    @Override

    @Override
    public T load(M key) {

        synchronized (cacheMap) {

//            checkQuotes(key);

            if (cacheMap.containsKey(key))
                return cacheMap.get(key);

            T t = create(key);
            if (t != null)
                cacheMap.put(key, t);

            return t;
        }
    }
/*    public T load(String key, int radius) {

        synchronized (cacheMap) {

            String url = key;
            key = key + "-" + radius;

//            if (!key.contains("image")) {
            checkQuotes(key);
//            }

            if (cacheMap.containsKey(key))
                return cacheMap.get(key);

            T t = create(url, radius);
            if (t != null)
                cacheMap.put(key, t);

            return t;
        }
    }*/

 /*   public void checkQuotes(M key) {

        if (textureQuotesLists.containsKey(key)) {
            int cNum = textureQuotesLists.get(key).intValue();
            textureQuotesLists.put(key, cNum + 1);
        } else {
            textureQuotesLists.put(key, 1);
        }
    }*/

    /*public boolean reduceQuotes(M key) {

        boolean isNeedRemove = true;

        if (textureQuotesLists.containsKey(key)) {
            int cNum = textureQuotesLists.get(key).intValue();
            if (cNum <= 1) {
                textureQuotesLists.remove(key);
            } else {
                textureQuotesLists.put(key, cNum - 1);
                isNeedRemove = false;
            }
        }

        return isNeedRemove;
    }*/

    @Override
    public boolean put(M key, T t) {

        synchronized (cacheMap) {
            if (cacheMap.containsKey(key))
                return false;

            cacheMap.put(key, t);
            return true;
        }
    }

    @Override
    public void clear() {

        for (M key : cacheMap.keySet()) {
            T t = cacheMap.get(key);
            if (t == null)
                continue;
            t.dispose();
            key.dispose();
        }
        cacheMap.clear();
    }

    @Override
    public int size() {
        return cacheMap.size();
    }

    @Override
    public void remove(final M key) {

        System.out.println("-- do remove --");

        synchronized (cacheMap) {
            if (cacheMap.containsKey(key)) {
                final T t = cacheMap.get(key);
                if (t != null) {
                    t.dispose();
                    key.dispose();
                }
            }
        }

    }

    public HashMap<M, T> getCacheMap() {
        return cacheMap;
    }

    public void clearSome() {
        synchronized (cacheMap) {
            for (Map.Entry<M, T> entry : cacheMap.entrySet()) {

                M key = entry.getKey();
//                if (key.contains("images/"))
//                    continue;
                T t = cacheMap.get(key);
                if (t != null)
                    t.dispose();
                key.dispose();
                cacheMap.remove(key);
                t = null;
            }
        }
    }
}
