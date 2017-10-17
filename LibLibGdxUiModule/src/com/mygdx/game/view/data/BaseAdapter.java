package com.mygdx.game.view.data;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.view.GdxAbsAdapterView;
import com.mygdx.game.view.GdxBaseGroupView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> implements IAdapter<T> {

    protected List<T> dataList;
    private int visiableItemCount = 10;
    private int lastPosition = -1;
    private final float FLESH_UI_PERIOD = 0.35f;  // second

    private GdxAbsAdapterView mGdxAbsAdapterView;
    private Timer timer = new Timer();
    private List<Integer> positionList = new ArrayList<Integer>();

    public BaseAdapter() {

    }

    public BaseAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setData(List list) {
        dataList = list;
    }

    @Override
    public List getData() {
        return dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItemAtPostion(int postion) {

        if (postion < 0
                || dataList == null
                || postion >= dataList.size())
            return null;

        return dataList.get(postion);
    }

    @Override
    public Actor initItemView(int postion) {
        return null;
    }

    @Override
    public void addData(List shortList) {

        if (shortList == null
                || shortList.size() == 0)
            return;

        if (dataList != null)
            dataList.addAll(shortList);
        else
            dataList = shortList;
    }

    @Override
    public void clear() {

        if (dataList != null)
            dataList.clear();
    }

    @Override
    public void notifyDataSetChanged() {

    }


    private void delearyToFlesh() {
        timer.clear();
        timer.scheduleTask(mTimerTask, FLESH_UI_PERIOD);
    }


    private final Timer.Task mTimerTask = new Timer.Task() {
        @Override
        public void run() {
            int position = positionList.remove(positionList.size() - 1);
            fleshAdapter(mGdxAbsAdapterView, position);

            System.out.println("do flesh adapter ..form positon =" + position);
        }
    };

    @Override
    public void updateView(GdxBaseGroupView viewGroup, int position) {

        if (mGdxAbsAdapterView == null)
            mGdxAbsAdapterView = (GdxAbsAdapterView) viewGroup;

        positionList.add(position);

        Actor actor = viewGroup.getChildren().get(position);
        visiableItemCount = (int) (viewGroup.getWidth() * viewGroup.getHeight() / actor.getWidth() / actor.getHeight());

        //test
        System.out.println("visiableItemCount == "+visiableItemCount);

        delearyToFlesh();
    }


    protected abstract void updateItemView(Actor actor, int position);

    protected abstract void disposeOldItemView(Actor actor, int position);


    private void fleshAdapter(GdxBaseGroupView groupView, int position) {

        // 计算当前需要显示的item区域
        Actor[] items = groupView.getChildren().items;
        int startIndex;
        int endIndex;
        if (position >= visiableItemCount) {
            startIndex = position - visiableItemCount;
        } else {
            startIndex = 0;
        }
        if (position + visiableItemCount <= items.length - 2) {
            endIndex = position + visiableItemCount;
        } else {
            endIndex = items.length - 2;
        }

        //计算显示的区域
        Rectangle cullingArea = groupView.getCullingArea();
        if (cullingArea == null) {
            cullingArea = Rectangle.tmp.set(groupView.getX(), groupView.getY(), groupView.getWidth(), groupView.getHeight());
        }

        if (cullingArea != null) {
            // Draw children only if inside culling area.
            float cullLeft = cullingArea.x;
            float cullRight = cullLeft + cullingArea.width;
            float cullBottom = cullingArea.y;
            float cullTop = cullBottom + cullingArea.height;
            // No transform for this group, offset each child.
            for (int i = startIndex; i <= endIndex; i++) {
                Actor child = items[i];
                if (child == null)
                    continue;
                if (!child.isVisible())
                    continue;
                float cx = child.getX(), cy = child.getY();
                if (cx <= cullRight
                        && cy <= cullTop
                        && cx + child.getWidth() >= cullLeft
                        && cy + child.getHeight() >= cullBottom) {
                    //需要显示的item
                    updateItemView(child, i);
                } else {
                    //不需要显示的item
                    disposeOldItemView(child, i);
                }
            }
        }
    }

}