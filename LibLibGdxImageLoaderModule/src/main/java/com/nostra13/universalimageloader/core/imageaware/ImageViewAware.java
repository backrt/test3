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

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.view.GdxImageView;

/**
 * Wrapper for Android {@link com.badlogic.gdx.scenes.scene2d.Actor Actor}. Keeps weak reference of Actor to prevent memory
 * leaks.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.9.0
 */
public class ImageViewAware extends ViewAware {

    /**
     * Constructor. <br />
     *
     * @param Actor {@link com.badlogic.gdx.scenes.scene2d.Actor Actor} to work with
     */
    public ImageViewAware(Actor Actor) {
        super(Actor);
    }

    /**
     * Constructor
     *
     * @param Actor               {@link com.badlogic.gdx.scenes.scene2d.Actor Actor} to work with
     * @param checkActualViewSize <b>true</b> - then {@link #getWidth()} and {@link #getHeight()} will check actual
     *                            size of Actor. It can cause known issues like
     *                            <a href="https://github.com/nostra13/Android-Universal-Image-Loader/issues/376">this</a>.
     *                            But it helps to save memory because memory cache keeps Pixmaps of actual (less in
     *                            general) size.
     *                            <p/>
     *                            <b>false</b> - then {@link #getWidth()} and {@link #getHeight()} will <b>NOT</b>
     *                            consider actual size of Actor, just layout parameters. <br /> If you set 'false'
     *                            it's recommended 'android:layout_width' and 'android:layout_height' (or
     *                            'android:maxWidth' and 'android:maxHeight') are set with concrete values. It helps to
     *                            save memory.
     *                            <p/>
     */
    public ImageViewAware(Actor Actor, boolean checkActualViewSize) {
        super(Actor, checkActualViewSize);
    }

    @Override
    protected void setImagePixmapInto(Pixmap pixmap, Actor actor) {

        if (actor instanceof GdxImageView) {
            ((GdxImageView) actor).setImageTexture(new Texture(pixmap));
        }
    }

    @Override
    protected void setImageTextureInto(Texture texture, Actor actor) {
        if (actor instanceof GdxImageView) {
            ((GdxImageView) actor).setImageTexture(texture);
        }
    }
}
