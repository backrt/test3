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
 * exceed size limit. When cache reaches limit size then the Pixmap which used the least frequently is deleted from
 * cache.<br />
 * <br />
 * <b>NOTE:</b> This cache uses strong and weak references for stored Pixmaps. Strong references - for limited count of
 * Pixmaps (depends on cache size), weak references - for all other cached Pixmaps.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public class UsingFreqLimitedMemoryCache extends LimitedMemoryCache {
	/**
	 * Contains strong references to stored objects (keys) and last object usage date (in milliseconds). If hard cache
	 * size will exceed limit then object with the least frequently usage is deleted (but it continue exist at
	 * {@link #softMap} and can be collected by GC at any time)
	 */
	private final Map<Pixmap, Integer> usingCounts = Collections.synchronizedMap(new HashMap<Pixmap, Integer>());

	public UsingFreqLimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}

	@Override
	public boolean put(String key, Pixmap value) {
		if (super.put(key, value)) {
			usingCounts.put(value, 0);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Pixmap get(String key) {
		Pixmap value = super.get(key);
		// Increment usage count for value if value is contained in hardCahe
		if (value != null) {
			Integer usageCount = usingCounts.get(value);
			if (usageCount != null) {
				usingCounts.put(value, usageCount + 1);
			}
		}
		return value;
	}

	@Override
	public Pixmap remove(String key) {
		Pixmap value = super.get(key);
		if (value != null) {
			usingCounts.remove(value);
		}
		return super.remove(key);
	}

	@Override
	public void clear() {
		usingCounts.clear();
		super.clear();
	}

	@Override
	protected int getSize(Pixmap value) {
		return value.getWidth() * value.getHeight();
	}

	@Override
	protected Pixmap removeNext() {
		Integer minUsageCount = null;
		Pixmap leastUsedValue = null;
		Set<Entry<Pixmap, Integer>> entries = usingCounts.entrySet();
		synchronized (usingCounts) {
			for (Entry<Pixmap, Integer> entry : entries) {
				if (leastUsedValue == null) {
					leastUsedValue = entry.getKey();
					minUsageCount = entry.getValue();
				} else {
					Integer lastValueUsage = entry.getValue();
					if (lastValueUsage < minUsageCount) {
						minUsageCount = lastValueUsage;
						leastUsedValue = entry.getKey();
					}
				}
			}
		}
		usingCounts.remove(leastUsedValue);
		return leastUsedValue;
	}

	@Override
	protected Reference<Pixmap> createReference(Pixmap value) {
		return new WeakReference<Pixmap>(value);
	}
}
