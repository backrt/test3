/*******************************************************************************
 * Copyright 2013-2014 Sergey Tarasevich
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
package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * ImageAware which provides needed info for processing of original image but do nothing for displaying image. It's
 * used when user need just load and decode image and get it in {@linkplain
 * Bitmap) callback}.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.9.0
 */
public class NonViewAware implements ImageAware {

    protected final String imageUri;
    protected final ImageSize imageSize;

    public NonViewAware(ImageSize imageSize) {
        this(null, imageSize);
    }

    public NonViewAware(String imageUri, ImageSize imageSize) {
        if (imageSize == null) throw new IllegalArgumentException("imageSize must not be null");

        this.imageUri = imageUri;
        this.imageSize = imageSize;
    }

    @Override
    public float getWidth() {
        return imageSize.getWidth();
    }

    @Override
    public float getHeight() {
        return imageSize.getHeight();
    }


    @Override
    public Actor getWrappedView() {
        return null;
    }

    @Override
    public boolean isCollected() {
        return false;
    }

    @Override
    public int getId() {
        return TextUtils.isEmpty(imageUri) ? super.hashCode() : imageUri.hashCode();
    }

    @Override
    public boolean setImagePixmap(Pixmap Pixmap) {
        return false;
    }
}