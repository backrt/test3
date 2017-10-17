package com.mygdx.game.android.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.UIConfig;
import com.tt.util.LogUtil;

/**
 * Created by Administrator on 2016/11/19.
 */

public class BaseScreen extends InputListener implements Screen {

    protected Stage mStage;

    public BaseScreen() {
        mStage = new Stage(new FitViewport(UIConfig.DESIGN_WIDTH,
                UIConfig.DESIGN_HEIGHT));
    }


    @Override
    public void show() {
        LogUtil.show(this.getClass().getName() + "--show -");

        Gdx.input.setInputProcessor(mStage);
        mStage.addListener(this);
    }


    @Override
    public void resize(int width, int height) {
        LogUtil.show(this.getClass().getName() + "--resize -" + width + "," + height);
    }

    @Override
    public void pause() {
        LogUtil.show(this.getClass().getName() + "--pause");
    }

    @Override
    public void resume() {
        LogUtil.show(this.getClass().getName() + "--resume");
    }

    @Override
    public void hide() {
        LogUtil.show(this.getClass().getName() + "--hide");
        mStage.removeListener(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mStage.act(Gdx.graphics.getDeltaTime());
        mStage.draw();
    }

    @Override
    public void dispose() {
        LogUtil.show(this.getClass().getName() + "--dispose");
        mStage.dispose();
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return super.keyDown(event, keycode);
    }
}
