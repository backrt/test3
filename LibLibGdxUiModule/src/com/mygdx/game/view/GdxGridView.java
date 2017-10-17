package com.mygdx.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.view.data.BaseAdapter;

public class GdxGridView extends GdxAbsAdapterView<BaseAdapter> {
    /**
     * 行 数
     */
    private int columnCount = 5;

    @Override
    public void initView(BaseAdapter adapter) {

        if (adapter == null)
            return;

        for (int i = 0; i < adapter.getCount(); i++) {

            int row = i / columnCount;
            int col = i % columnCount;

            Actor actor = adapter.initItemView(i);

            float itemWidth = actor.getWidth();
            float itemHeight = actor.getHeight();

            actor.setPosition(col * (itemWidth + super.getHorizontalSpace())
                    + marginLeft, this.getHeight() - row
                    * (itemHeight + super.getVerticalSpace()) - itemHeight
                    - marginTop);

            actor.setOrigin(itemWidth / 2, itemHeight / 2);
            this.addActor(actor);

        }
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int mColumnCount) {
        this.columnCount = mColumnCount;
    }


    @Override
    protected float getContentWidth() {

        if (this.getChildren().size == 0)
            return super.getContentHeight();

        return (((Actor) this.getChildren().get(0)).getWidth() + super.getHorizontalSpace()) * columnCount - super.getHorizontalSpace()
                + this.getMarginLeft() + this.getMarginRight();
    }


    @Override
    protected float getContentHeight() {

        if (this.getChildren().size == 0)
            return super.getContentWidth();

        int rowCount = 0;
        int itemCount = this.getChildren().size;
        if (this.isShowingFloatView
                && this.mFloatView != null) {
            itemCount--;
        }

        int intRowCount = itemCount / columnCount;
        rowCount = itemCount % columnCount == 0 ? intRowCount : (intRowCount + 1);

        return (((Actor) this.getChildren().get(0)).getHeight() + super.getVerticalSpace()) * rowCount - super.getVerticalSpace()
                + this.getMarginRight() + this.getMarginLeft();

    }
}
