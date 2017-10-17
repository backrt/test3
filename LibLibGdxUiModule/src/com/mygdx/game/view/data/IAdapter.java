package com.mygdx.game.view.data;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.view.GdxBaseGroupView;

import java.util.List;

public interface IAdapter<T> {

    void setData(List<T> list);

    List<T> getData();

    int getCount();

    Object getItemAtPostion(int postion);

    Actor initItemView(int postion);

    void addData(List<T> shortList);

    void clear();

    void notifyDataSetChanged();

    void updateView(GdxBaseGroupView viewGroup,int position);
}
