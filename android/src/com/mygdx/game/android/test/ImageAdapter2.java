package com.mygdx.game.android.test;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.android.R;
import com.mygdx.game.view.GdxBaseGroupView;
import com.mygdx.game.view.GdxScaleableGroupView;
import com.mygdx.game.view.GdxTextView;
import com.mygdx.game.view.data.BaseAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tt.util.Art;

import java.util.Random;

/**
 * Created by Administrator on 2016/11/19.
 */

public class ImageAdapter2 extends BaseAdapter {

    private DisplayImageOptions options;
    private Random random;

    public ImageAdapter2() {

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .delayBeforeLoading(2000)
                .showImageOnLoading(R.drawable.default_snap2)
//                .showImageOnFail(R.drawable.zj_b)
                .build();

        random = new Random();
    }


    @Override
    public Actor initItemView(int postion) {


        // group
        GdxScaleableGroupView gdxBaseGroupView = new GdxScaleableGroupView();
        gdxBaseGroupView.setDescendantFocusability(GdxBaseGroupView.FOCUS_BEFORE_DESCENDANTS);
        gdxBaseGroupView.setSize(250, 200);
        gdxBaseGroupView.setName("test-group-name-" + postion);
        Pixmap pixmap = ImageLoader.getInstance().loadImageSync("assets://image/default_snap2.png");
        if (pixmap != null)
            gdxBaseGroupView.setBackgroundTexture(new Texture(pixmap));


        // textview
        GdxTextView textView = new GdxTextView();
        textView.setFocusAble(true);
        textView.setAlign(Align.left);
        textView.setMargeen(true);
        textView.setName("test-child-name-" + postion);
        textView.setSize(250, 100);
        textView.setOrigin(textView.getWidth() / 2, textView.getHeight() / 2);
        Pixmap pixmap1 = ImageLoader.getInstance().loadImageSync("assets://image/test_textview_bg.png");
        if (pixmap1 != null) {
            textView.setBackgroundTexture(new Texture(pixmap1));
        }
        TestBean bean = (TestBean) this.getItemAtPostion(postion);
        textView.setText(bean.getText(), Art.getInstance().generateFont(bean.getText(), 30));
        gdxBaseGroupView.addActor(textView);

        return gdxBaseGroupView;


//        GdxImageView imageView = new GdxImageView();
//        imageView.setName("test-child-name-" + postion);
//        imageView.setSize(250, 200);
//        imageView.setFocusAble(true);
//        //加载本地图片
//        imageView.setImageTexture(TextureCache.getInstance().load("image/default_snap2.png"));
//        return imageView;

    }


    private int randomHeight() {
//        return random.nextInt(150) + random.nextInt(10) + random.nextInt(30) + random.nextInt(30) + 100;
        return 200;
    }

    @Override
    protected void updateItemView(Actor actor, int position) {
        TestBean bean = (TestBean) this.getItemAtPostion(position);
        ImageLoader.getInstance().displayImage(bean.getImg(), actor, options);
    }

    @Override
    protected void disposeOldItemView(Actor actor, int position) {
//        ImageLoader.getInstance().cancelDisplayTask(actor);
    }
}
