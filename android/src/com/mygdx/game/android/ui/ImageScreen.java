package com.mygdx.game.android.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.android.test.DataProvider;
import com.mygdx.game.android.test.ImageAdapter;
import com.mygdx.game.android.test.TestBean;
import com.mygdx.game.view.GdxBaseGroupView;
import com.mygdx.game.view.GdxBaseView;
import com.mygdx.game.view.GdxGridView;
import com.mygdx.game.view.GdxNinePathImageView;
import com.mygdx.game.view.GdxWaterfallFlowView;
import com.mygdx.game.view.listener.OnItemClickListener;
import com.mygdx.game.view.listener.OnItemSelectChangedListener;
import com.mygdx.game.view.listener.OnKeyListener;
import com.tt.util.Art;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/2.
 */

public class ImageScreen extends BaseScreen {

    private ArrayList<TestBean> dateList;

    @Override
    public void show() {
        super.show();

        // test -- gridView
        GdxGridView gridView = new GdxGridView();
        gridView.setPosition(30, 40);
        gridView.setSize(1920, 980);
        gridView.setHorizontalSpace(30);
        gridView.setVerticalSpace(30);
        gridView.setColumnCount(6);
        gridView.setMargin(70, 70, 70, 70);
        gridView.setRollBackLeft(false);
        gridView.setRollBackRight(false);
        gridView.setRollBackBottom(false);
        gridView.setRollBackTop(false);
        gridView.setDescendantFocusability(GdxBaseGroupView.FOCUS_AFTER_DESCENDANTS);


//        GdxWaterfallFlowView gridView = new GdxWaterfallFlowView();
//        gridView.setPosition(0, 0);
//        gridView.setSize(1000, 980);
//        gridView.setHorizontalSpace(30);
//        gridView.setVerticalSpace(30);
//        gridView.setColumnCount(3);
//        gridView.setMargin(70, 70, 70, 70);
//        gridView.setRollBackLeft(false);
//        gridView.setRollBackRight(false);
//        gridView.setRollBackBottom(true);
//        gridView.setRollBackTop(true);
//        gridView.setDescendantFocusability(GdxBaseGroupView.FOCUS_AFTER_DESCENDANTS);


        // new float view
        GdxNinePathImageView mFloatView = new GdxNinePathImageView();
        mFloatView.setName(GdxBaseView.TAG_FLOATVIEW);
        mFloatView.setZIndex(0);
        mFloatView.setNinePatch(Art.getInstance().
                        generateNinePath("image/live_screen_focus.9.png",
                                24, 24, 24, 29, 202 - 48, 169 - 53),
                202, 169, 0);


        //添加到listview
        gridView.addFloatView(mFloatView);
        gridView.setFloatViewInBack(false);
        gridView.showFloatView();

        mStage.addActor(gridView);


        // fetch data to apdater
        ImageAdapter adapter = new ImageAdapter();
        dateList = new ArrayList<TestBean>();
        for (int i = 0; i < 1200; i++) {
            TestBean bean = new TestBean();
            bean.setName("test");
            bean.setText("测试数据不是我要的频道。。。。。。。。。。。。。。。。。。。!");
            bean.setImg(DataProvider.getImgs());
//            bean.setImg("kkkkkkkkk");
            dateList.add(bean);
        }
        adapter.setData(dateList);
        gridView.setAdapter(adapter);
        gridView.requestFocus();


        gridView.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Actor actor, int index) {
            }
        });


        gridView.setItemSelectedChangeListener(new OnItemSelectChangedListener() {
            @Override
            public void onItemSelectChanged(Actor actor, int index) {



            }
        });

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

