package com.tt.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.util.PixmapUtil;

public class TextureCache extends CacheMap<Texture> {

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
    public Texture create(String key) {

        if (key == null)
            return null;

        Texture t = new Texture(Gdx.files.internal(key));
        return t;
    }


    @Override
    public Texture create(String key, int radius) {

        if (key == null)
            return null;

        Texture t = null;
        Pixmap radiusPixmap = PixmapUtil.createRoundedPixmap(key, radius);
        if (radiusPixmap != null)
            t = new Texture(radiusPixmap);
        return t;
    }

    private long getNativeHeap() {
        return Gdx.app.getNativeHeap() / 1024 / 1024;
    }

}
