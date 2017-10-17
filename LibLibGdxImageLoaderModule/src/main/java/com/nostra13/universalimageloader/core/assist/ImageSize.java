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
package com.nostra13.universalimageloader.core.assist;

/**
 * Present width and height values
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public class ImageSize {

    private static final int TO_STRING_MAX_LENGHT = 9; // "9999x9999".length()
    private static final String SEPARATOR = "x";

    private final float width;
    private final float height;

    public ImageSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public ImageSize(float width, float height, int rotation) {
        if (rotation % 180 == 0) {
            this.width = width;
            this.height = height;
        } else {
            this.width = height;
            this.height = width;
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /**
     * Scales down dimensions in <b>sampleSize</b> times. Returns new object.
     */
    public ImageSize scaleDown(int sampleSize) {
        return new ImageSize(width / sampleSize, height / sampleSize);
    }

    /**
     * Scales dimensions according to incoming scale. Returns new object.
     */
    public ImageSize scale(float scale) {
        return new ImageSize((float) (width * scale), (float) (height * scale));
    }

    @Override
    public String toString() {
        return new StringBuilder(TO_STRING_MAX_LENGHT).append(width).append(SEPARATOR).append(height).toString();
    }
}
