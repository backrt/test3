package com.mygdx.game.android.test;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;

import java.util.Random;

/**
 * Created by Administrator on 2016/12/2.
 */

public class DataProvider {


    private static final String[] IMGS = {
            "http://b.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=6046b2f46509c93d07a706f3aa0dd4ea/4a36acaf2edda3ccd58840da03e93901203f92d8.jpg"
            ,
            "http://g.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=a1ab21c9cefcc3ceb495c137a775fabe/37d12f2eb9389b50a9794db98335e5dde6116e53.jpg"
            ,
            "http://imga.mumayi.com/android/wallpaper/2012/01/21/sl_600_2012012105503827801816.jpg"
            ,
            "http://imgsrc.baidu.com/forum/w%3D580/sign=35f486b5b9389b5038ffe05ab534e5f1/ba7350fbb2fb43166e07e6d120a4462309f7d31b.jpg"
            ,
            "http://a.hiphotos.baidu.com/zhidao/pic/item/5366d0160924ab18fffea7dd31fae6cd7a890b6b.jpg"
            ,
            "http://img5.duitang.com/uploads/item/201409/28/20140928221838_sMkEM.jpeg"
            ,
            "http://pic1.win4000.com/wallpaper/c/53d715df6274d.jpg"
            ,
            "http://www.desktx.com/d/file/p/20160824/small9f7b06367e0c7bc2e825a96e30bb771c.jpg"
            ,
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494569902&di=1ac2712b184861c045bb0a467c6ff631&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_1_2387069514D246472357_23.jpg"
    };


    public static String getImgs() {

        int index = new Random().nextInt(IMGS.length);
        return IMGS[index];
    }


}
