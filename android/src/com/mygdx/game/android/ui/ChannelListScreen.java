package com.mygdx.game.android.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.android.test.TestAdapter;
import com.mygdx.game.android.test.TestBean;
import com.mygdx.game.view.GdxBaseGroupView;
import com.mygdx.game.view.GdxBaseView;
import com.mygdx.game.view.GdxListView;
import com.mygdx.game.view.GdxNinePathImageView;
import com.mygdx.game.view.listener.OnItemClickListener;
import com.mygdx.game.view.listener.OnKeyListener;
import com.tt.util.Art;

import java.util.ArrayList;

public class ChannelListScreen extends BaseScreen {

    private ArrayList<TestBean> dateList;

    @Override
    public void show() {
        super.show();

        // test -- gridView
        GdxListView listView = new GdxListView();
        listView.setDescendantFocusability(GdxBaseGroupView.FOCUS_AFTER_DESCENDANTS);
        listView.setPosition(0, 0);
        listView.setSize(1000, 980);
        listView.setmOrientation(GdxListView.ListViewOrientation.LISTVIEW_ORNENTATION_VERTICAL);
        listView.setHorizontalSpace(30);
        listView.setVerticalSpace(10);
        listView.setMargin(70, 70, 70, 70);


        // new float view
        GdxNinePathImageView mFloatView = new GdxNinePathImageView();
        mFloatView.setName(GdxBaseView.TAG_FLOATVIEW);
        mFloatView.setZIndex(0);
        mFloatView.setNinePatch(Art.getInstance().
                        generateNinePath("image/live_screen_focus.9.png",
                                24, 24, 24, 29, 202 - 48, 169 - 53),
                202, 169, 0);



        //添加到listview
        listView.addFloatView(mFloatView);
        listView.setFloatViewInBack(false);
        listView.showFloatView();

        mStage.addActor(listView);


        // fetch data to apdater
        TestAdapter adapter = new TestAdapter();
        dateList = new ArrayList<TestBean>();
        for (int i = 0; i < 40; i++) {
            TestBean bean = new TestBean();
            bean.setName("test");
            bean.setText("测试数据不是我要的频道。。。。。。。。。。。。。。。。。。。!");
            dateList.add(bean);
        }
        adapter.setData(dateList);
        listView.setAdapter(adapter);
        listView.requestFocus();


        listView.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Actor actor, int index) {

                if (WonderGame.getInstance().getIttMediaController() != null) {

                    TestBean tt = new TestBean();
                    tt.setName("http://125.88.92.166:30001/PLTV/88888956/224/3221227713/1.m3u8");
                    WonderGame.getInstance().getIttMediaController().startPlay(tt);

                }
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
