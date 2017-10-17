package com.mygdx.game.android.test;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.view.GdxTextView;
import com.mygdx.game.view.data.BaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tt.util.Art;

/**
 * Created by Administrator on 2016/11/19.
 */

public class TestAdapter extends BaseAdapter {

    @Override
    protected void updateItemView(Actor actor, int position) {

    }

    @Override
    protected void disposeOldItemView(Actor actor, int position) {

    }

    @Override
    public Actor initItemView(int postion) {

        TestBean bean = (TestBean) this.getItemAtPostion(postion);

        GdxTextView textView = new GdxTextView();
        textView.setName("name-" + postion);
//        textView.setBackgroundTexture(TextureCache.getInstance().load("xx.jpg"));
        textView.setSize(300, 80);
        textView.setText(bean.getText() + "-" + postion,
                Art.getInstance().generateFont(bean.getText() + "-" + postion, 40));
        textView.setPadding(20, 0, 20, 0);


        ImageLoader.getInstance().displayImage("http://p1.meituan.net/deal/aae6379995cedb5cb5e47505f60a894f57540.jpg", textView);

        return textView;
    }


}
