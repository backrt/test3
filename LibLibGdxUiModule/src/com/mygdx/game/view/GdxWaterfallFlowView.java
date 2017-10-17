package com.mygdx.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.view.data.BaseAdapter;

public class GdxWaterfallFlowView extends GdxAbsAdapterView<BaseAdapter> {

    private int columnCount = 2; // 列数
    private int[] mLengths;

    @Override
    public void initView(BaseAdapter adapter) {

        if (adapter == null)
            return;

        mLengths = new int[columnCount];

        for (int i = 0; i < adapter.getCount(); i++) {

            int col = calculateCurrentShortestColumm();
            int top = mLengths[col];

            Actor actor = adapter.initItemView(i);

            float itemWidth = actor.getWidth();
            float itemHeight = actor.getHeight();

            actor.setPosition(col * (itemWidth + super.getHorizontalSpace())
                    + marginLeft, this.getHeight() - top - itemHeight
                    - marginTop);
            actor.setOrigin(itemWidth / 2, itemHeight / 2);
            this.addActor(actor);

            mLengths[col] += itemHeight + super.getVerticalSpace();
        }

    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }


    /**
     * 计算当前最低的列
     *
     * @return
     */
    private int calculateCurrentShortestColumm() {

        int temp = mLengths[0];
        int index = 0;

        for (int i = 1; i < mLengths.length; i++) {

            if (temp > mLengths[i]) {
                temp = mLengths[i];
                index = i;
            }
        }
        return index;
    }
}
