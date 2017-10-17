/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
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

import android.os.Looper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nostra13.universalimageloader.cache.texture.TextureCache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.9.2
 */
public abstract class ViewAware implements ImageAware {

    public static final String WARN_CANT_SET_DRAWABLE = "Can't set a drawable into Actor. You should call ImageLoader on UI thread for it.";
    public static final String WARN_CANT_SET_Pixmap = "Can't set a Pixmap into Actor. You should call ImageLoader on UI thread for it.";

    protected Reference<Actor> ActorRef;
    protected boolean checkActualActorSize;

    /**
     * Constructor. <br />
     */
    public ViewAware(Actor Actor) {
        this(Actor, true);
    }

    /**
     * Constructor
     *
     * @param checkActualActorSize <b>true</b> - then {@link #getWidth()} and {@link #getHeight()} will check actual
     *                             size of Actor. It can cause known issues like
     *                             <a href="https://github.com/nostra13/Android-Universal-Image-Loader/issues/376">this</a>.
     *                             But it helps to save memory because memory cache keeps Pixmaps of actual (less in
     *                             general) size.
     *                             <p/>
     *                             <b>false</b> - then {@link #getWidth()} and {@link #getHeight()} will <b>NOT</b>
     *                             consider actual size of Actor, just layout parameters. <br /> If you set 'false'
     *                             it's recommended 'android:layout_width' and 'android:layout_height' (or
     *                             'android:maxWidth' and 'android:maxHeight') are set with concrete values. It helps to
     *                             save memory.
     */
    public ViewAware(Actor Actor, boolean checkActualActorSize) {
        if (Actor == null) throw new IllegalArgumentException("Actor must not be null");

        this.ActorRef = new WeakReference<Actor>(Actor);
        this.checkActualActorSize = checkActualActorSize;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * parameters or device display dimensions.<br />
     * Size computing algorithm (go by steps until get non-zero value):<br />
     * 1) Get the actual drawn <b>getWidth()</b> of the Actor<br />
     * 2) Get <b>layout_width</b>
     */
    @Override
    public float getWidth() {
        Actor actor = ActorRef.get();
        if (actor != null) {
            return actor.getWidth();
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * parameters or device display dimensions.<br />
     * Size computing algorithm (go by steps until get non-zero value):<br />
     * 1) Get the actual drawn <b>getHeight()</b> of the Actor<br />
     * 2) Get <b>layout_height</b>
     */
    @Override
    public float getHeight() {
        Actor actor = ActorRef.get();
        if (actor != null) {
            return actor.getHeight();
        }
        return 0;
    }


    @Override
    public Actor getWrappedView() {
        return ActorRef.get();
    }

    @Override
    public boolean isCollected() {
        return ActorRef.get() == null;
    }

    @Override
    public int getId() {
        Actor Actor = ActorRef.get();
        return Actor == null ? super.hashCode() : Actor.hashCode();
    }


    @Override
    public boolean setImagePixmap(Pixmap pixmap) {
        return this.setImageTexture(pixmap);
    }


    private boolean setImageTexture(final Pixmap pixmap) {

        if (pixmap == null)
            return true;

        if (Looper.myLooper() == Looper.getMainLooper()) {
            Actor actor = ActorRef.get();
            if (actor != null) {
//                setImagePixmapInto(pixmap, actor);
                final Texture texture = TextureCache.getInstance().load(pixmap);
                setImageTextureInto(texture, actor);
                return true;
            }
        } else {

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Actor actor = ActorRef.get();
                    if (actor != null) {
                        final Texture texture = TextureCache.getInstance().load(pixmap);
//                        setImagePixmapInto(pixmap, actor);
                        setImageTextureInto(texture, actor);
                    }
                }
            });
        }
        return true;
    }

    /**
     * Should set Pixmap into incoming Actor. Incoming Actor is guaranteed not null.< br />
     * This method is called on UI thread.
     */
    protected abstract void setImagePixmapInto(Pixmap Pixmap, Actor Actor);

    /**
     * set texture to actor ;
     *
     * @param texture
     * @param actor
     */
    protected abstract void setImageTextureInto(Texture texture, Actor actor);
}
