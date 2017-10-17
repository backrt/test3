package com.mygdx.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.view.data.BaseAdapter;

/**
 * listView
 *
 * @author Administrator
 */
public class GdxListView extends GdxAbsAdapterView<BaseAdapter> {

    /**
     * 列表的滚动方向
     *
     * @author Administrator
     */
    public enum ListViewOrientation {
        LISTVIEW_ORNENTATION_HORIZONTAL, LISTVIEW_ORNENTATION_VERTICAL
    }

    /**
     * 默认listview的排列的方式是横向
     */
    private ListViewOrientation mOrientation = ListViewOrientation.LISTVIEW_ORNENTATION_HORIZONTAL;

    /**
     * construct
     */
    public GdxListView() {
        super();
    }

    /**
     *
     */
    public void initView(BaseAdapter adapter) {

        if (adapter == null)
            return;

        int COLUMN_NUM = 1; // 列的数量

        if (this.mOrientation == ListViewOrientation.LISTVIEW_ORNENTATION_HORIZONTAL) {
            COLUMN_NUM = adapter.getCount();
        }

        for (int i = 0; i < adapter.getCount(); i++) {

            int row = i / COLUMN_NUM;
            int col = i % COLUMN_NUM;

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

    public ListViewOrientation getmOrientation() {
        return mOrientation;
    }

    public void setmOrientation(ListViewOrientation mOrientation) {
        this.mOrientation = mOrientation;
    }

    @Override
    public boolean isCanRollBackLeft() {
        return this.mOrientation == ListViewOrientation.LISTVIEW_ORNENTATION_HORIZONTAL ? super
                .isCanRollBackLeft() : false;
    }

    @Override
    public boolean isCanRollBackRight() {
        return this.mOrientation == ListViewOrientation.LISTVIEW_ORNENTATION_HORIZONTAL ? super
                .isCanRollBackRight() : false;
    }

    @Override
    public boolean isCanRollBackTop() {
        return this.mOrientation == ListViewOrientation.LISTVIEW_ORNENTATION_HORIZONTAL ? false
                : super.isCanRollBackTop();
    }

    @Override
    public boolean isCanRollBackBottom() {
        return this.mOrientation == ListViewOrientation.LISTVIEW_ORNENTATION_HORIZONTAL ? false
                : super.isCanRollBackBottom();
    }

}
