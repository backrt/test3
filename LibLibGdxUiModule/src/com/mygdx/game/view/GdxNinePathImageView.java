package com.mygdx.game.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;

public class GdxNinePathImageView extends GdxScaleableView {

    private boolean isShow;

    private float px = 0;
    private float py = 0;

    private int radius; //图的圆角

    private NinePatch ninePatch;


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (!isShow)
            return;

        if (ninePatch == null)
            return;

        float x = super.getX();
        float y = super.getY();
        float w = super.getWidth();
        float h = super.getHeight();
        float scaleX = super.getScaleX();
        float scaleY = super.getScaleY();

        float w1 = (w + 2 * px) * scaleX;
        float h1 = (h + 2 * py) * scaleY;
        float px1 = (w1 - w) / 2;
        float py1 = (h1 - h) / 2;


        ninePatch.draw(
                batch,
                x - px1 - ninePatch.getLeftWidth() + radius,
                y - py1 - ninePatch.getBottomHeight() + radius,
                w1 + ninePatch.getLeftWidth() + ninePatch.getRightWidth() - 2 * radius,
                h1 + ninePatch.getTopHeight() + ninePatch.getBottomHeight() - 2 * radius
        );
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public float getPx() {
        return px;
    }

    public void setPx(float px) {
        this.px = px;
    }

    public float getPy() {
        return py;
    }

    public void setPy(float py) {
        this.py = py;
    }

    public void updatePositon(float objWidth, float objHeight) {
        this.setPx((objWidth - this.getWidth()) / 2);
        this.setPy((objHeight - this.getHeight()) / 2);
    }


    public void setNinePatch(NinePatch ninePatch,
                             int pixelWidth,
                             int pixelHeigth,
                             int radius) {

        this.setSize(pixelWidth, pixelHeigth);
        this.setNinePatch(ninePatch);
        this.setRadius(radius);
        this.setOrigin(pixelWidth / 2, pixelHeigth / 2);
    }

    public void setNinePatch(NinePatch ninePatch) {
        this.ninePatch = ninePatch;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

}
