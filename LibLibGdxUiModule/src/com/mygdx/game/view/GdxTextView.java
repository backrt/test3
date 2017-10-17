package com.mygdx.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.FloatArray;

public class GdxTextView extends GdxScaleableView {

    protected BitmapFont font;
    private Color textColor = Color.WHITE;

    private int align = Align.center;
    private int textMarginLeft;
    protected int cursor, selectionStart;
    private int visibleTextEnd;
    private int visibleTextEnd2;

    protected final GlyphLayout layout = new GlyphLayout();
    protected final FloatArray glyphPositions = new FloatArray();

    protected CharSequence displayText;
    protected String text;

    private float textOffset;

    private long lastTextMargeenTime;
    private final long DEFAULT_MARGEEN_TIME = 410;  //跑马灯的时间间隔
    private final long DEFAULT_MARGIN_TEXT_SPACE = 50; // 第二拼接字段的默认距离

    private boolean isDrawSecond;  // 开关标记: 是否绘制第二拼接字段
    private boolean isMargeen;  // 开关标记:是否需要开启跑马灯
    private boolean isUpdatedDrawText;

    public GdxTextView() {
        super();
        this.isMargeen = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.drawText(batch, parentAlpha);
    }


    /**
     * 渲染文字
     *
     * @param batch
     * @param parentAlpha
     */
    private void drawText(Batch batch, float parentAlpha) {

        if (this.isTextInVaild())
            return;

        boolean isOverFlow = isTextOverFlow();
        float textX = this.getTextPositonX(isOverFlow);
        float textY = this.getTextPositionY(font);
        float scaleX = super.getScaleX();
        float scaleY = super.getScaleY();

        textColor.a = parentAlpha;
        font.setColor(textColor);
        font.getData().setScale(scaleX, scaleY);

        try {
            this.calculateOffsets(isOverFlow);
            drawText(batch, font, textX, textY);
            if (isOverFlow) {
                if (this.isFocus())
                    margeen();
                else {
                    cursor = 0;
                    isDrawSecond = false;
                }
            }
        } catch (Exception e) {
        }

    }

    public void dispose() {
        super.dispose();

        if (font != null) {
            font.dispose();
            font = null;
            text = null;
        }

    }

    public void clearFont() {
        if (this.font != null) {
            font.dispose();
            font = null;
        }
    }

    public void setText(String text, BitmapFont textFont,
                        Color textColor) {
        this.text = text;
        this.font = textFont;
        if (textColor != null)
            this.textColor = textColor;
        updateDisplayText();
    }

    public void setText(String text, BitmapFont textFont) {
        this.setText(text, textFont, Color.WHITE);
    }

    public CharSequence getText() {
        return this.text;
    }

    public Color getFontColor() {
        return textColor;
    }

    public void setFontColor(Color fontColor) {
        this.textColor = fontColor;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public int getTextMarginLeft() {
        return textMarginLeft;
    }

    public void setTextMarginLeft(int textMarginLeft) {
        this.textMarginLeft = textMarginLeft;
    }

    public boolean isMargeen() {
        return isMargeen;
    }

    public void setMargeen(boolean isNeedMargreen) {
        this.isMargeen = isNeedMargreen;
    }

    public void setAlign(int align) {
        this.align = align;
    }


    protected float getTextPositionY(BitmapFont font) {

        float height = getHeight() * getScaleY();
        float textHeight = font.getCapHeight() - font.getDescent() * 2;
        float textY = textHeight / 2 + font.getDescent();
        textY = textY + height / 2;
        if (font.usesIntegerPositions()) textY = (int) textY;

        textY += this.getY();
        textY -= this.getHeight() * (getScaleY() - 1) / 2;
        return textY;
    }

    private void updateDisplayText() {

        BitmapFont.BitmapFontData data = font.getData();
        int textLength = text.length();

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < textLength; i++) {
            char c = text.charAt(i);
            buffer.append(data.hasGlyph(c) ? c : ' ');
        }
        String newDisplayText = buffer.toString();

        displayText = newDisplayText;

        layout.setText(font, displayText);
        glyphPositions.clear();
        float x = 0;
        if (layout.runs.size > 0) {
            GlyphLayout.GlyphRun run = layout.runs.first();
            FloatArray xAdvances = run.xAdvances;
            for (int i = 1, n = xAdvances.size; i < n; i++) {
                glyphPositions.add(x);
                x += xAdvances.get(i);
            }
        }
        glyphPositions.add(x);
        if (selectionStart > newDisplayText.length()) selectionStart = textLength;

        isUpdatedDrawText = true;
    }


    /**
     * 计算显示的文字
     */
    protected void calculateOffsets(boolean isTextOverFlow) {

        if (!isTextOverFlow) {
            cursor = 0;
            visibleTextEnd = glyphPositions.size - 1;
            return;
        }

        float visibleWidth = getPreWidth() * (getScaleX() + 1) / 2;
        float[] glyphPositions = this.glyphPositions.items;

        int lastIndex = cursor;
        for (int i = cursor; i < glyphPositions.length; i++) {

            if ((glyphPositions[i] - glyphPositions[cursor]) * getScaleX() >= visibleWidth) {
                lastIndex = i - 1;
                break;
            }
        }


        //截取第二段
        if (lastIndex == cursor) {
            if (lastIndex < text.length()) {

                lastIndex = text.length();
                isDrawSecond = true;
                textOffset = glyphPositions[lastIndex] - glyphPositions[cursor] + DEFAULT_MARGIN_TEXT_SPACE;
                textOffset *= getScaleX();
                for (int i = 0; i < glyphPositions.length; i++) {

                    if ((glyphPositions[i] - glyphPositions[0]) * getScaleX() >= (visibleWidth - textOffset)) {
                        visibleTextEnd2 = i - 1;

                        if (visibleTextEnd2 < 0)
                            isDrawSecond = false;
                        break;
                    }
                }

            } else {
                isDrawSecond = false;
                lastTextMargeenTime = 0;
            }
        }

        visibleTextEnd = lastIndex;
        if (visibleTextEnd >= this.glyphPositions.size) {
            visibleTextEnd = this.glyphPositions.size - 1;
        }
    }


    private void margeen() {

        if (!isMargeen)
            return;

        if (!isTextOverFlow()) {
            return;
        }

        long time = System.currentTimeMillis();
        if (time - lastTextMargeenTime > DEFAULT_MARGEEN_TIME) {
            if (cursor >= 0 && cursor < glyphPositions.size - 1) {
                cursor++;
            } else {
                cursor = 0;
            }
            lastTextMargeenTime = time;
        }
    }


    /**
     * 判断文字是否合法
     *
     * @return
     */
    private boolean isTextInVaild() {
        return text == null || text.length() == 0 || this.font == null || !this.isUpdatedDrawText;
    }

    /**
     * 判断文字是否超过控件本身可绘制宽度
     *
     * @return
     */
    private boolean isTextOverFlow() {
        return glyphPositions.items[glyphPositions.size - 1] - getPreWidth() > 0;
    }


    private float getTextPositonX(boolean isTextOverFlow) {

        float posX = this.getX() - (this.getWidth() * (this.getScaleX() - 1)) / 2;

        switch (align) {

            case Align.center:

                if (isTextOverFlow) {
                    posX += (getWidth() - getPreWidth()) / 2;
                } else {
                    posX += (this.getWidth() - glyphPositions.items[glyphPositions.size - 1]) / 2;
                }
                break;

            case Align.left:
                posX += this.getMarginLeft() + this.getPaddingLeft();
                break;

            case Align.bottom:
            case Align.bottomLeft:
            case Align.bottomRight:
            case Align.right:
            case Align.top:
            case Align.topLeft:
            case Align.topRight:
            default:
                break;
        }

        return posX;
    }


    /**
     * 文字的可绘制宽度
     *
     * @return
     */
    private float getPreWidth() {
        return this.getWidth() - this.getMarginRight() - this.getMarginLeft() - this.getPaddingLeft() - this.getPaddingRight();
    }


    /**
     * 渲染字体
     *
     * @param batch
     * @param font
     * @param x
     * @param y
     */
    protected void drawText(Batch batch, BitmapFont font, float x, float y) {

        font.draw(batch, displayText, x, y, cursor % glyphPositions.size, visibleTextEnd % glyphPositions.size, 0, Align.left, false);
        if (isDrawSecond) {
            font.draw(batch, displayText, x + textOffset, y, 0, visibleTextEnd2 % glyphPositions.size, 0, Align.left, false);
        }
    }

}
