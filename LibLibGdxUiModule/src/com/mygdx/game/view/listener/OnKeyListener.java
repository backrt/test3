package com.mygdx.game.view.listener;

public interface OnKeyListener {

    boolean onKey(keyEvent event, int keyCode);

    enum keyEvent {
        KEY_DOWN, KEY_UP
    }

}
