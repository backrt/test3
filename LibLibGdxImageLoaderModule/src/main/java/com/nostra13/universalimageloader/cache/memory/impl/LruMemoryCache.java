package com.nostra13.universalimageloader.cache.memory.impl;

import com.badlogic.gdx.graphics.Pixmap;

import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.texture.TextureCache;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A cache that holds strong references to a limited number of Pixmaps. Each time a Pixmap is accessed, it is moved to
 * the head of a queue. When a Pixmap is added to a full cache, the Pixmap at the end of that queue is evicted and may
 * become eligible for garbage collection.<br />
 * <br />
 * <b>NOTE:</b> This cache uses only strong references for stored Pixmaps.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.1
 */
public class LruMemoryCache implements MemoryCache {

    private final LinkedHashMap<String, Pixmap> map;

    private final int maxSize;
    /**
     * Size of this cache in bytes
     */
    private int size;

    /**
     * @param maxSize Maximum sum of the sizes of the Pixmaps in this cache
     */
    public LruMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<String, Pixmap>(0, 0.75f, true);
    }

    /**
     * Returns the Pixmap for {@code key} if it exists in the cache. If a Pixmap was returned, it is moved to the head
     * of the queue. This returns null if a Pixmap is not cached.
     */
    @Override
    public final Pixmap get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        synchronized (this) {
            return map.get(key);
        }
    }

    /**
     * Caches {@code Pixmap} for {@code key}. The Pixmap is moved to the head of the queue.
     */
    @Override
    public final boolean put(String key, Pixmap value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        synchronized (this) {
            size += sizeOf(key, value);
            Pixmap previous = map.put(key, value);
            if (previous != null) {
                size -= sizeOf(key, previous);
            }
        }

        trimToSize(maxSize);
        return true;
    }

    /**
     * Remove the eldest entries until the total of remaining entries is at or below the requested size.
     *
     * @param maxSize the maximum size of the cache before returning. May be -1 to evict even 0-sized elements.
     */
    private void trimToSize(int maxSize) {
        while (true) {
            String key;
            Pixmap value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize || map.isEmpty()) {
                    break;
                }

                Map.Entry<String, Pixmap> toEvict = map.entrySet().iterator().next();
                if (toEvict == null) {
                    break;
                }
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= sizeOf(key, value);
                TextureCache.getInstance().remove(value);
            }
        }
    }

    /**
     * Removes the entry for {@code key} if it exists.
     */
    @Override
    public final Pixmap remove(String key) {
        System.out.println("---------- memory remove -----------"+key);

        if (key == null) {
            throw new NullPointerException("key == null");
        }

        synchronized (this) {
            Pixmap previous = map.remove(key);
            if (previous != null) {
                size -= sizeOf(key, previous);
                TextureCache.getInstance().remove(previous);
            }
            return previous;
        }
    }

    @Override
    public Collection<String> keys() {
        synchronized (this) {
            return new HashSet<String>(map.keySet());
        }
    }

    @Override
    public void clear() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }

    /**
     * Returns the size {@code Pixmap} in bytes.
     * <p/>
     * An entry's size must not change while it is in the cache.
     */
    private int sizeOf(String key, Pixmap value) {
        return value.getWidth() * value.getHeight();
    }

    @Override
    public synchronized final String toString() {
        return String.format("LruCache[maxSize=%d]", maxSize);
    }
}