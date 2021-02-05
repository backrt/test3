package com.mygdx.game.android.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.android.test.DataProvider;
import com.mygdx.game.android.test.ImageAdapter;
import com.mygdx.game.android.test.ImageAdapter2;
import com.mygdx.game.android.test.TestBean;
import com.mygdx.game.view.GdxBaseGroupView;
import com.mygdx.game.view.GdxBaseView;
import com.mygdx.game.view.GdxImageView;
import com.mygdx.game.view.GdxListView;
import com.mygdx.game.view.GdxNinePathImageView;
import com.mygdx.game.view.GdxScrollGroup;
import com.mygdx.game.view.listener.OnItemClickListener;
import com.mygdx.game.view.listener.OnItemSelectChangedListener;
import com.mygdx.game.view.listener.OnKeyListener;
import com.tt.cache.TextureCache;
import com.tt.util.Art;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class WaterflowScreen extends BaseScreen {

    @Override
    public void show() {
        super.show();


        GdxScrollGroup rootView = new GdxScrollGroup();
        rootView.setName("rootView");
        rootView.setDescendantFocusability(GdxBaseGroupView.FOCUS_AFTER_DESCENDANTS);
        rootView.setPosition(0, 0);
        rootView.setSize(1920, 1080);
        mStage.addActor(rootView);


        int rowHeight = 400;
        for (int i = 0; i < 1; i++) {

            // test -- gridView
            GdxListView gridView = new GdxListView();
            gridView.setName("gridView-" + i);
            gridView.setDescendantFocusability(GdxBaseGroupView.FOCUS_AFTER_DESCENDANTS);
            gridView.setPosition(0, (rowHeight + 100) * i);
            gridView.setSize(1920, rowHeight);
            gridView.setHorizontalSpace(30);
            gridView.setVerticalSpace(30);
            gridView.setmOrientation(GdxListView.ListViewOrientation.LISTVIEW_ORNENTATION_HORIZONTAL);
            gridView.setMargin(120, 20, 120, 20);
            gridView.setRollBackLeft(false);
            gridView.setRollBackRight(false);
            gridView.setRollBackBottom(false);
            gridView.setRollBackTop(false);


//        // new float view
            GdxNinePathImageView mFloatView = new GdxNinePathImageView();
            mFloatView.setName(GdxBaseView.TAG_FLOATVIEW);
            mFloatView.setZIndex(0);
            mFloatView.setNinePatch(
                    Art.getInstance().generateNinePath(
                            "image/live_screen_focus.9.png",
                            24,
                            24,
                            24,
                            29,
                            202 - 48,
                            169 - 53
                    ),
                    202,
                    169,
                    0
            );


            //添加到listview
            gridView.addFloatView(mFloatView);
            gridView.setFloatViewInBack(false);
            gridView.showFloatView();

            rootView.addActor(gridView);


            // fetch data to apdater
            ImageAdapter2 adapter = new ImageAdapter2();
            List<TestBean> dateList = new ArrayList<TestBean>();
            for (int j = 0; j < 20; j++) {
                TestBean bean = new TestBean();
                bean.setName("test");
                bean.setText("测试数测试跑马风效果的风格--" + j);
                bean.setImg(DataProvider.getImgs());
                dateList.add(bean);
            }
            adapter.setData(dateList);
            gridView.setAdapter(adapter);
            gridView.requestFocus();

//
            gridView.setItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(Actor actor, int index) {
                    System.out.println("onClick == " + index);
                }
            });
//
//
            gridView.setItemSelectedChangeListener(new OnItemSelectChangedListener() {
                @Override
                public void onItemSelectChanged(Actor actor, int index) {

                }
            });
        }
    }


    @Override
    public boolean keyDown(InputEvent event, int keycode) {

        Actor a = mStage.getRoot().getChildren().get(0);
        if (a != null && a instanceof OnKeyListener) {
            boolean flag = ((OnKeyListener) a).onKey(OnKeyListener.keyEvent.KEY_DOWN, keycode);
            return flag;
        }
        return super.keyDown(event, keycode);
    }


    @Override
    public void dispose() {
        super.dispose();
    }

}

