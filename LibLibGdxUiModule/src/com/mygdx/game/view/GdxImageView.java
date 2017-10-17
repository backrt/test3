package com.mygdx.game.view;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by Administrator on 2016/11/19.
 */

public class GdxImageView extends GdxScaleableView {

    private Texture imageTexture;
    private Texture defaultTexture;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (!drawImage(batch, imageTexture)) {
            drawImage(batch, defaultTexture);
        }
    }


    /**
     * 渲染图片
     *
     * @param batch
     * @param texture
     */
    private boolean drawImage(Batch batch, Texture texture) {
        // draw image
        if (texture == null)
            return false;

        batch.draw(texture, super.getX(), super.getY(),
                super.getOriginX(), super.getOriginY(), super.getWidth(),
                super.getHeight(), super.getScaleX(), super.getScaleY(),
                super.getRotation(), 0, 0, texture.getWidth(),
                texture.getHeight(), false, false);

        return true;
    }


    public Texture getImageTexture() {
        return imageTexture;
    }

    public void setImageTexture(Texture imageTexture) {
        this.imageTexture = imageTexture;
    }


    public void setImagePixmap(Pixmap pixmap) {
        this.setImageTexture(new Texture(pixmap));
    }

    public void dispose() {
        super.dispose();
        if (this.imageTexture != null) {
            this.imageTexture.dispose();
        }
    }


    public void setDefaultTexture(Texture defaultTexture) {
        this.defaultTexture = defaultTexture;
    }
}
