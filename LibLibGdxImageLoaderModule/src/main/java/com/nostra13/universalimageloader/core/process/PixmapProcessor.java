/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
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
package com.nostra13.universalimageloader.core.process;

import com.badlogic.gdx.graphics.Pixmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Makes some processing on {@link Pixmap}. Implementations can apply any changes to original {@link Pixmap}.<br />
 * Implementations have to be thread-safe.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
public interface PixmapProcessor {
    /**
     * @param Pixmap Original {@linkplain Pixmap Pixmap}
     * @return Processed {@linkplain Pixmap Pixmap}
     */
    Pixmap process(Pixmap Pixmap);
}
