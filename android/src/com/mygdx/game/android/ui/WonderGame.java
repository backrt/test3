package com.mygdx.game.android.ui;

import com.badlogic.gdx.Screen;
import com.mygdx.game.android.test.ITTMediaController;
import com.tt.cache.TextureCache;
import com.tt.util.Art;
import com.tt.util.LogUtil;

public class WonderGame extends BaseGame {

    private static WonderGame instance = null;

    private ITTMediaController ittMediaController;

    public interface IScreenType {

        int SCREEN_WELCOME = 0x1;
        int SCREEN_MAIN = 0x2;
        int SCREEN_IMAGE = 0x3 ;
        int SCREEN_WALTERFLOW = 0x4;
        int SCREEN_ParticleEffect = 0x5 ;  //粒子1
        int SCREEN_ParticleEffect2 = 0x6 ;  //粒子2
    }

    public static WonderGame getInstance() {
        if (instance == null) {
            synchronized (WonderGame.class) {
                if (instance == null) {
                    instance = new WonderGame();
                }
            }
        }
        return instance;
    }


    private void initData() {
    }


    @Override
    public void create() {
        super.create();

        initData();
        launcherScreen(IScreenType.SCREEN_WELCOME, false);
    }

    public void launcherScreen(int screenId, boolean isDisposeCurrentScreen) {

        Screen targetScreen = null;
        Screen currentScreen = screen;

        switch (screenId) {

            case IScreenType.SCREEN_WELCOME:
                targetScreen = new WelcomeScreen();
                break;

            case IScreenType.SCREEN_MAIN:
                targetScreen = new ChannelListScreen();
                break;

            case IScreenType.SCREEN_IMAGE:
                targetScreen = new ImageScreen();
                break;

            case IScreenType.SCREEN_WALTERFLOW:
                targetScreen = new WaterflowScreen();
                break;

            case IScreenType.SCREEN_ParticleEffect:
                targetScreen = new ParticleEffectScreen();
                break;

            case IScreenType.SCREEN_ParticleEffect2:
                targetScreen = new ParticleEffectScreen2();
                break;
        }

        setScreen(targetScreen);
        if (isDisposeCurrentScreen && currentScreen != null) {
            currentScreen.dispose();
        }
    }

    @Override
    public void pause() {
        super.pause();
        LogUtil.show(this.getClass().getName() + "--pause --");
    }

    @Override
    public void resume() {
        super.resume();
        LogUtil.show(this.getClass().getName() + "--resume --");
    }


    @Override
    public void dispose() {
        super.dispose();
        LogUtil.show(this.getClass().getName() + "--dispose --");

        instance = null;
        TextureCache.getInstance().clear();
        Art.getInstance().dispose();

        if (ittMediaController != null)
            ittMediaController.stopPlay();
    }

    public void setIttMediaController(ITTMediaController ittMediaController) {
        this.ittMediaController = ittMediaController;
    }


    public ITTMediaController getIttMediaController() {
        return ittMediaController;
    }
}
