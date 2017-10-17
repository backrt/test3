package com.tt.cache;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Disposable;
import com.tt.util.LogUtil;

public abstract class CacheMap<T extends Disposable> implements
        CacheMapProtocol<T> {

    private final HashMap<String, T> cacheMap;
    private HashMap<String, Integer> textureQuotesLists;

    protected CacheMap() {
        cacheMap = new HashMap<String, T>();
        textureQuotesLists = new HashMap<String, Integer>();
    }

    @Override
    public boolean containsKey(String key) {
        return cacheMap.containsKey(key);
    }

    @Override
    public T get(String key) {

        if (cacheMap.containsKey(key))
//            if (!key.contains("image")) {
            checkQuotes(key);
//            }

        return cacheMap.get(key);
    }

    @Override
    public T load(String key) {

        synchronized (cacheMap) {

//            if (!key.contains("image")) {
            checkQuotes(key);
//            }

            if (cacheMap.containsKey(key))
                return cacheMap.get(key);

            T t = create(key);
            if (t != null)
                cacheMap.put(key, t);

            return t;
        }
    }

    @Override
    public T load(String key, int radius) {

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
    }

    public void checkQuotes(String key) {

        if (textureQuotesLists.containsKey(key)) {
            int cNum = textureQuotesLists.get(key).intValue();
            textureQuotesLists.put(key, cNum + 1);
        } else {
            textureQuotesLists.put(key, 1);
        }
    }

    public boolean reduceQuotes(String key) {

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
    }

    @Override
    public boolean put(String key, T t) {

        synchronized (cacheMap) {
            if (cacheMap.containsKey(key))
                return false;

            cacheMap.put(key, t);
            return true;
        }
    }

    @Override
    public void clear() {

        for (String key : cacheMap.keySet()) {
            T t = cacheMap.get(key);
            if (t == null)
                continue;
            t.dispose();
            LogUtil.show("-- dispose img -" + key);
        }
        cacheMap.clear();
    }

    @Override
    public int size() {
        return cacheMap.size();
    }

    @Override
    public void remove(String key) {

        synchronized (cacheMap) {
            if (cacheMap.containsKey(key)) {
                T t = cacheMap.get(key);
                if (t != null) {
                    if (reduceQuotes(key)) {
                        t.dispose();
                        cacheMap.remove(key);
                    }
                }
            }
        }

    }

    public HashMap<String, T> getCacheMap() {
        return cacheMap;
    }

    public void clearSome() {
        synchronized (cacheMap) {
            for (Map.Entry<String, T> entry : cacheMap.entrySet()) {

                String key = entry.getKey();
//                if (key.contains("images/"))
//                    continue;
                T t = cacheMap.get(key);
                if (t != null)
                    t.dispose();
                cacheMap.remove(key);
                t = null;
            }
        }
    }
}
