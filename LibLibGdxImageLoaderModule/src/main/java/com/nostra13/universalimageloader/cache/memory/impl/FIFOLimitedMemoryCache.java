/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.cache.memory.impl;

import com.badlogic.gdx.graphics.Pixmap;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Limited {@link Pixmap Pixmap} cache. Provides {@link Pixmap Pixmaps} storing. Size of all stored Pixmaps will not to
 * exceed size limit. When cache reaches limit size then cache clearing is processed by FIFO principle.<br />
 * <br />
 * <b>NOTE:</b> This cache uses strong and weak references for stored Pixmaps. Strong references - for limited count of
 * Pixmaps (depends on cache size), weak references - for all other cached Pixmaps.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public class FIFOLimitedMemoryCache extends LimitedMemoryCache {

    private final List<Pixmap> queue = Collections.synchronizedList(new LinkedList<Pixmap>());

    public FIFOLimitedMemoryCache(int sizeLimit) {
        super(sizeLimit);
    }

    @Override
    public boolean put(String key, Pixmap value) {
        if (super.put(key, value)) {
            queue.add(value);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Pixmap remove(String key) {
        Pixmap value = super.get(key);
        if (value != null) {
            queue.remove(value);
        }
        return super.remove(key);
    }

    @Override
    public void clear() {
        queue.clear();
        super.clear();
    }

    @Override
    protected int getSize(Pixmap value) {
        return value.getWidth() * value.getHeight();
    }

    @Override
    protected Pixmap removeNext() {
        return queue.remove(0);
    }

    @Override
    protected Reference<Pixmap> createReference(Pixmap value) {
        return new WeakReference<Pixmap>(value);
    }
}
