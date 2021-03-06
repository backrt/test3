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
package com.nostra13.universalimageloader.core.listener;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nostra13.universalimageloader.core.assist.FailReason;

/**
 * Listener for image loading process.<br />
 * You can use {@link SimpleImageLoadingListener} for implementing only needed methods.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see SimpleImageLoadingListener
 * @see com.nostra13.universalimageloader.core.assist.FailReason
 * @since 1.0.0
 */
public interface ImageLoadingListener {

    /**
     * Is called when image loading task was started
     *
     * @param imageUri Loading image URI
     */
    void onLoadingStarted(String imageUri, Actor actor);

    /**
     * Is called when an error was occurred during image loading
     *
     * @param imageUri   Loading image URI
     * @param failReason {@linkplain com.nostra13.universalimageloader.core.assist.FailReason The reason} why image
     *                   loading was failed
     */
    void onLoadingFailed(String imageUri, Actor actor, FailReason failReason);

    /**
     * Is called when image is loaded successfully (and displayed in View if one was specified)
     *
     * @param imageUri    Loaded image URI
     * @param loadedImage Pixmap of loaded and decoded image
     */
    void onLoadingComplete(String imageUri, Actor actor, Pixmap loadedImage);

    /**
     * Is called when image loading task was cancelled because View for image was reused in newer task
     *
     * @param imageUri Loading image URI
     */
    void onLoadingCancelled(String imageUri, Actor actor);
}
