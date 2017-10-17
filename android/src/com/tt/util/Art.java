package com.tt.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.mygdx.game.view.utils.StringUtils;

import java.io.File;

public class Art {

    private FreeTypeFontGenerator ffg;
    private AssetManager assetManager;
    private boolean isLoaded;
    private boolean isLoading;

    private static Art instance = null;

    public static Art getInstance() {
        if (instance == null) {
            synchronized (Art.class) {

                if (instance == null)
                    instance = new Art();
            }
        }
        return instance;
    }

    private Art() {
        this.init();
    }

    public void init() {
        isLoaded = false;
        assetManager = new AssetManager();
        assetManager.setLoader(FreeTypeFontGenerator.class,
                new FreeTypeFontGeneratorLoader(
                        new InternalFileHandleResolver()));
    }


    public boolean update() {
        return isLoaded = assetManager.update();
    }

    public void load() {

        if (isLoading)
            return;

        isLoading = true;
        new Thread(new Runnable() {

            @Override
            public void run() {

                File file = new File("/system/fonts/DroidSansFallback.ttf");
                if (!file.exists()) {
                    file = new File("/system/fonts/DroidSansMono.ttf");
                }

                ffg = new FreeTypeFontGenerator(new FileHandle(file));
                isLoaded = true;
                isLoading = false;
            }
        }).start();

    }

    /**
     * 根据需要显示的文字,返回BitmapFont
     *
     * @param text
     * @param size
     * @return
     */
    public BitmapFont generateFont(String text, int size) {

        if (!isLoaded || ffg == null || text == null)
            return null;

        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.minFilter = TextureFilter.Linear;
        param.magFilter = TextureFilter.Linear;
        param.characters = StringUtils.getInstance().removeRepeatedChar(text);
        param.flip = false;
        param.size = size;

        return ffg.generateFont(param);

    }


    public NinePatch generateNinePath(String imgPath, int left, int top, int right, int bottom, int middleHeight, int middleWidth) {

        Texture focusTexture = new Texture(Gdx.files.internal(imgPath));
        NinePatch ninePath = new NinePatch(focusTexture, left, right, top, bottom);
        ninePath.setMiddleHeight(middleHeight);
        ninePath.setMiddleWidth(middleWidth);

        return ninePath;
    }


    /**
     * 资源销毁
     */
    public void dispose() {
        if (ffg != null)
            ffg.dispose();

        if (assetManager != null)
            assetManager.dispose();
    }

    public boolean isLoaded() {
        return isLoaded;
    }

}