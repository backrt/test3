package com.mygdx.game.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.view.listener.IFocusChangeCallback;
import com.mygdx.game.view.listener.OnClickListener;
import com.mygdx.game.view.listener.OnFocusChangeListener;
import com.mygdx.game.view.listener.OnKeyListener;

public class GdxBaseView extends Actor implements
        OnKeyListener,
        IFocusChangeCallback,
        Disposable {

    public static final String TAG_FLOATVIEW = "FLOATVIEW";

    private OnKeyListener mKeyListener;
    private OnClickListener mClickListener;
    private OnFocusChangeListener onFocusChangeListener;

    private Texture backgroundTexture;

    private String nextFocusRight;
    private String nextFocusLeft;
    private String nextFocusUp;
    private String nextFocusDown;

    /**
     * 暂时还没用
     */
    private float paddingLeft;
    private float paddingRight;
    private float paddingBottom;
    private float paddingTop;

    private float marginLeft;
    private float marginRight;
    private float marginBottom;
    private float marginTop;

    private boolean isEnable = true;
    private boolean isFocusAble = true;
    private boolean isFocus;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // draw background texture
        if (backgroundTexture == null)
            return;
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        batch.draw(backgroundTexture,
                super.getX(),
                super.getY(),
                super.getOriginX(),
                super.getOriginY(),
                super.getWidth(),
                super.getHeight(),
                super.getScaleX(),
                super.getScaleY(),
                super.getRotation(),
                0,
                0,
                backgroundTexture.getWidth(),
                backgroundTexture.getHeight(),
                false,
                false);
    }

    @Override
    public boolean onKey(keyEvent event, int keyCode) {

        if (!isEnable)
            return false;

        if (keyCode == Input.Keys.DPAD_CENTER
                || keyCode == Input.Keys.ENTER) {
            if (mKeyListener != null)
                return mKeyListener.onKey(event, keyCode);
        }
        return true; // 消耗掉onkey事件
    }


    public void setPostion(float x, float y,
                           float width, float height) {
        super.setPosition(x, y);
        super.setSize(width, height);
    }

    public String getNextFocusRight() {
        return nextFocusRight;
    }

    public void setNextFocusRight(String nextFocusRight) {
        this.nextFocusRight = nextFocusRight;
    }

    public String getNextFocusLeft() {
        return nextFocusLeft;
    }

    public void setNextFocusLeft(String nextFocusLeft) {
        this.nextFocusLeft = nextFocusLeft;
    }

    public String getNextFocusUp() {
        return nextFocusUp;
    }

    public void setNextFocusUp(String nextFocusUp) {
        this.nextFocusUp = nextFocusUp;
    }

    public String getNextFocusDown() {
        return nextFocusDown;
    }

    public void setNextFocusDown(String nextFocusDown) {
        this.nextFocusDown = nextFocusDown;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void setmKeyListener(OnKeyListener mKeyListener) {
        this.mKeyListener = mKeyListener;
    }

    public void setmClickListener(OnClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public void setBackgroundTexture(Texture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    public float getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public float getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    public float getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }


    public void setMargin(int marginLeft, int marginTop, int marginRight, int marginBottom) {

        this.setMarginLeft(marginLeft);
        this.setMarginTop(marginTop);
        this.setMarginRight(marginRight);
        this.setMarginBottom(marginBottom);
    }

    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {

        this.setPaddingLeft(paddingLeft);
        this.setPaddingTop(paddingTop);
        this.setPaddingRight(paddingRight);
        this.setPaddingBottom(paddingBottom);
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null)
            backgroundTexture.dispose();
    }

    public boolean isFocusAble() {
        return isFocusAble;
    }

    public void setFocusAble(boolean focusAble) {
        isFocusAble = focusAble;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public OnFocusChangeListener getOnFocusChangeListener() {
        return onFocusChangeListener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void focusChange(boolean isGainFocus) {
        if (!this.isEnable()
                || !this.isFocusAble())
            return;

        this.isFocus = isGainFocus;

        //焦点变化的动画
        // ..


        // listener
        if (this.onFocusChangeListener != null)
            this.onFocusChangeListener.onFocusChange(isGainFocus);

    }
}
