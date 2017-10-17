package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * Created by Administrator on 2016/11/18.
 */

public class PixmapUtil {

    //创建rouned rectangle
    public static Pixmap getPixmapRoundedRectangle(int width, int height, int radius, Color color) {

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        // 横着的矩形
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight() - 2 * radius);
        // 垂直矩形
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2 * radius, pixmap.getHeight());
        // 左上 circle
        pixmap.fillCircle(radius, radius, radius);
        // 左下 circle
        pixmap.fillCircle(radius, pixmap.getHeight() - radius, radius);
        // 右上 circle
        pixmap.fillCircle(pixmap.getWidth() - radius, radius, radius);
        // 右下 circle
        pixmap.fillCircle(pixmap.getWidth() - radius, pixmap.getHeight() - radius, radius);
        return pixmap;
    }

    public static void pixmapMask(Pixmap pixmap, Pixmap mask, Pixmap result, boolean invertMaskAlpha) {
        int pixmapWidth = pixmap.getWidth();
        int pixmapHeight = pixmap.getHeight();
        Color pixelColor = new Color();
        Color maskPixelColor = new Color();

        Pixmap.Blending blending = Pixmap.getBlending();
        Pixmap.setBlending(Pixmap.Blending.None);
        for (int x = 0; x < pixmapWidth; x++) {
            for (int y = 0; y < pixmapHeight; y++) {
                Color.rgba8888ToColor(pixelColor, pixmap.getPixel(x, y));                           // 获取原图像素颜色
                Color.rgba8888ToColor(maskPixelColor, mask.getPixel(x, y));                         // 获取掩码像素颜色

                maskPixelColor.a = (invertMaskAlpha) ? 1.0f - maskPixelColor.a : maskPixelColor.a;    // 如果转换掩码
                pixelColor.a = pixelColor.a * maskPixelColor.a;                                     // 颜色吸相乘
                result.setColor(pixelColor);
                result.drawPixel(x, y);
            }
        }
        Pixmap.setBlending(blending);
    }


    public static Pixmap createRoundedPixmap(String url, int radius) {

        Pixmap pixmap = new Pixmap(Gdx.files.internal(url));//原图
        Pixmap mask = getPixmapRoundedRectangle(pixmap.getWidth(), pixmap.getWidth(), radius, Color.WHITE);//掩码
        Pixmap result = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);//保存结果
        pixmapMask(pixmap, mask, result, false);
        return result;
    }
}
