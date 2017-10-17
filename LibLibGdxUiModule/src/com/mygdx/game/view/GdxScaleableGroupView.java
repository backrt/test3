package com.mygdx.game.view;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class GdxScaleableGroupView extends GdxBaseGroupView {

    private final float DEFAULT_ORIGIN = 1.0f;
    private final float DEFAULT_SCALE_X = 1.10f; //默认 X 轴放大倍数
    private final float DEFAULT_SCALE_Y = 1.10f; //默认 Y 轴放大倍数
    private final float DEFAULT_ANIMATION_TIME = 0.35f; //默认的动画时间
    private final float DEFAULT_ALPHA_ = 0.75f; //默认丢失焦点透明度


    private float animationScaleX;
    private float animationScaleY;
    private float animationTime;
    private float loseFocusAlpha;


    public GdxScaleableGroupView() {
        super();
        this.animationScaleX = DEFAULT_SCALE_X;
        this.animationScaleY = DEFAULT_SCALE_Y;
        this.animationTime = DEFAULT_ANIMATION_TIME;
        this.loseFocusAlpha = DEFAULT_ALPHA_;
    }

    public GdxScaleableGroupView(float scaleX, float scaleY, float animationTime, float loseFocusAlpha) {
        super();
        this.animationScaleX = scaleX;
        this.animationScaleY = scaleY;
        this.animationTime = animationTime;
        this.loseFocusAlpha = loseFocusAlpha;
    }

    @Override
    public void focusChange(boolean isGainFocus) {
        super.focusChange(isGainFocus);

        if(isGainFocus){
            gainFocusAnimation();
        }else
            loseFocusAnimation();
    }

    public void gainFocusAnimation() {
        this.clearActions();
        this.addAction(Actions.parallel(Actions.scaleTo(animationScaleX, animationScaleY, animationTime),
                Actions.alpha(DEFAULT_ORIGIN, animationTime)));
//        this.toFront();
    }

    public void loseFocusAnimation() {
        this.clearActions();
        this.addAction(Actions.parallel(Actions.scaleTo(DEFAULT_ORIGIN, DEFAULT_ORIGIN, animationTime),
                Actions.alpha(loseFocusAlpha, animationTime)));
//        this.toBack();
    }


    public float getAnimationScaleX() {
        return animationScaleX;
    }

    public float getAnimationScaleY() {
        return animationScaleY;
    }

    public float getAnimationTime() {
        return animationTime;
    }

    public void setAnimationScaleX(float animationScaleX) {
        this.animationScaleX = animationScaleX;
    }

    public void setAnimationScaleY(float animationScaleY) {
        this.animationScaleY = animationScaleY;
    }

    public void setAnimationTime(float animationTime) {
        this.animationTime = animationTime;
    }
}
