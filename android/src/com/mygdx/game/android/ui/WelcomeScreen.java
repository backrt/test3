package com.mygdx.game.android.ui;

import com.badlogic.gdx.graphics.Texture;
import com.tt.cache.TextureCache;
import com.tt.util.Art;
import com.mygdx.game.view.GdxImageView;
import com.tt.config.AppConfig;

public class WelcomeScreen extends BaseScreen {

    boolean isInit;

    @Override
    public void show() {
        super.show();

        GdxImageView bgImageView = new GdxImageView();
        bgImageView.setPostion(0, 0, AppConfig.DESIGN_WIDTH, AppConfig.DESIGN_HEIGHT);
        Texture texture = TextureCache.getInstance().load("image/welcome_bg.jpg");
        bgImageView.setBackgroundTexture(texture);
        mStage.addActor(bgImageView);

        //初始化资源
        Art.getInstance();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!Art.getInstance().isLoaded()) {
            Art.getInstance().load();
        } else {
            if (!isInit) {
                isInit = true;
//                WonderGame.getInstance().launcherScreen(WonderGame.IScreenType.SCREEN_MAIN, true);
                WonderGame.getInstance().launcherScreen(WonderGame.IScreenType.SCREEN_IMAGE, true);
//                WonderGame.getInstance().launcherScreen(WonderGame.IScreenType.SCREEN_WALTERFLOW, true);
//                WonderGame.getInstance().launcherScreen(WonderGame.IScreenType.SCREEN_ParticleEffect, true);
//                WonderGame.getInstance().launcherScreen(WonderGame.IScreenType.SCREEN_ParticleEffect2, true);
            }
        }
    }

}
