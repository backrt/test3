/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Limited {@link Pixmap Pixmap} cache. Provides {@link Pixmap Pixmaps} storing. Size of all stored Pixmaps will not to
 * exceed size limit. When cache reaches limit size then the Pixmap which has the largest size is deleted from
 * cache.<br />
 * <br />
 * <b>NOTE:</b> This cache uses strong and weak references for stored Pixmaps. Strong references - for limited count of
 * Pixmaps (depends on cache size), weak references - for all other cached Pixmaps.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public class LargestLimitedMemoryCache extends LimitedMemoryCache {
	/**
	 * Contains strong references to stored objects (keys) and sizes of the objects. If hard cache
	 * size will exceed limit then object with the largest size is deleted (but it continue exist at
	 * {@link #softMap} and can be collected by GC at any time)
	 */
	private final Map<Pixmap, Integer> valueSizes = Collections.synchronizedMap(new HashMap<Pixmap, Integer>());

	public LargestLimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}

	@Override
	public boolean put(String key, Pixmap value) {
		if (super.put(key, value)) {
			valueSizes.put(value, getSize(value));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Pixmap remove(String key) {
		Pixmap value = super.get(key);
		if (value != null) {
			valueSizes.remove(value);
		}
		return super.remove(key);
	}

	@Override
	public void clear() {
		valueSizes.clear();
		super.clear();
	}

	@Override
	protected int getSize(Pixmap value) {
		return value.getWidth() * value.getHeight();
	}

	@Override
	protected Pixmap removeNext() {
		Integer maxSize = null;
		Pixmap largestValue = null;
		Set<Entry<Pixmap, Integer>> entries = valueSizes.entrySet();
		synchronized (valueSizes) {
			for (Entry<Pixmap, Integer> entry : entries) {
				if (largestValue == null) {
					largestValue = entry.getKey();
					maxSize = entry.getValue();
				} else {
					Integer size = entry.getValue();
					if (size > maxSize) {
						maxSize = size;
						largestValue = entry.getKey();
					}
				}
			}
		}
		valueSizes.remove(largestValue);
		return largestValue;
	}

	@Override
	protected Reference<Pixmap> createReference(Pixmap value) {
		return new WeakReference<Pixmap>(value);
	}
}
