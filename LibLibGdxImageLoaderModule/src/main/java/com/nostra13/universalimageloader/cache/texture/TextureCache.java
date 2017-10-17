package com.nostra13.universalimageloader.cache.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.util.PixmapUtil;

public class TextureCache extends CacheMap<Pixmap, Texture> {

    private static TextureCache instance = null;

    public static TextureCache getInstance() {

        synchronized (TextureCache.class) {
            if (instance == null)
                instance = new TextureCache();
            return instance;
        }
    }

    public TextureCache() {
        super();
    }

    public static void destory() {
        if (instance == null)
            return;
        instance = null;
    }

    @Override
    public Texture create(Pixmap key) {
        System.out.println("create texture -- ");

        if (key == null)
            return null;

        return new Texture(key);
    }

    private long getNativeHeap() {
        return Gdx.app.getNativeHeap() / 1024 / 1024;
    }

}
