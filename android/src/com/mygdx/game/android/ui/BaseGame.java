package com.mygdx.game.android.ui;

import com.badlogic.gdx.Game;

/**
 * Created by Administrator on 2016/11/19.
 */

public class BaseGame extends Game {

    @Override
    public void create() {
    }

    @Override
    public void dispose() {
        super.dispose();

        if (screen != null)
            screen.dispose();
    }
}
