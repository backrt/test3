package com.mygdx.game.android.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.android.R;
import com.mygdx.game.view.GdxImageView;
import com.mygdx.game.view.data.BaseAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tt.util.LogUtil;

import java.util.Random;

/**
 * Created by Administrator on 2016/11/19.
 */

public class ImageAdapter extends BaseAdapter {

    private DisplayImageOptions options;
    private Random random;

//    private Texture defaultTexture;

    public ImageAdapter() {

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
//                .delayBeforeLoading(1000)
                .showImageOnLoading(R.drawable.default_snap2)
                .showImageOnFail(R.drawable.zj_b)
                .build();

        random = new Random();

//        Pixmap pixmap = ImageLoader
//                .getInstance()
//                .loadImageSync("assets://image/default_snap2.png");
//        if (pixmap != null)
//            defaultTexture = new Texture(pixmap);
    }


    @Override
    public Actor initItemView(int postion) {

        GdxImageView imageView = new GdxImageView();
        imageView.setName("name-" + postion);
        imageView.setSize(350, randomHeight());
//        imageView.setDefaultTexture(defaultTexture);

        return imageView;
    }


    private int randomHeight() {
        return 250;
//        return random.nextInt(150) + random.nextInt(10) + random.nextInt(30) + random.nextInt(30) + 100;
    }

    @Override
    protected void updateItemView(Actor actor, int position) {
        TestBean bean = (TestBean) this.getItemAtPostion(position);
        ImageLoader.getInstance().displayImage(bean.getImg(), actor, options);

        LogUtil.d("heapSize", Gdx.app.getJavaHeap() / 1024 / 1024 + "_||_" + Gdx.app.getNativeHeap() / 1024 / 1024);
    }

    @Override
    protected void disposeOldItemView(Actor actor, int position) {

//        LogUtil.d("disposeOldItem == " + position);
//        ImageLoader.getInstance().cancelDisplayTask(actor);
//        ((GdxBaseView)actor).dispose();
    }
}
