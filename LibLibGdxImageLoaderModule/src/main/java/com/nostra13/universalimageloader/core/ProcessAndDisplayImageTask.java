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
package com.nostra13.universalimageloader.core;

import com.badlogic.gdx.graphics.Pixmap;

import android.os.Handler;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.process.PixmapProcessor;
import com.nostra13.universalimageloader.utils.L;

/**
 * Presents process'n'display image task. Processes image {@linkplain Pixmap} and display it in {@link Actor} using
 * {@link DisplayPixmapTask}.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
final class ProcessAndDisplayImageTask implements Runnable {

    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";

    private final ImageLoaderEngine engine;
    private final Pixmap Pixmap;
    private final ImageLoadingInfo imageLoadingInfo;
    private final Handler handler;

    public ProcessAndDisplayImageTask(ImageLoaderEngine engine, Pixmap Pixmap, ImageLoadingInfo imageLoadingInfo,
                                      Handler handler) {
        this.engine = engine;
        this.Pixmap = Pixmap;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
    }

    @Override
    public void run() {
        L.d(LOG_POSTPROCESS_IMAGE, imageLoadingInfo.memoryCacheKey);

        PixmapProcessor processor = imageLoadingInfo.options.getPostProcessor();
        Pixmap processedPixmap = processor.process(Pixmap);
        DisplayPixmapTask displayPixmapTask = new DisplayPixmapTask(processedPixmap, imageLoadingInfo, engine,
                LoadedFrom.MEMORY_CACHE);
        LoadAndDisplayImageTask.runTask(displayPixmapTask, imageLoadingInfo.options.isSyncLoading(), handler, engine);
    }
}
