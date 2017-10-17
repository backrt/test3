package com.mygdx.game.android;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.android.ui.WonderGame;
import com.tt.config.AppConfig;

public class AndroidLauncher extends AndroidApplication {

    private RelativeLayout rootView;
//    private TTPlayView videoView;

    private View gdxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(this.initView());
    }

    /**
     * 初始化界面
     */
    private View initView() {

        rootView = new RelativeLayout(this);

        //gdx view
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.r = config.g = config.b = config.a = 8;
        gdxView = initializeForView(WonderGame.getInstance(), config);
        gdxView.setFocusable(true);


        //设置透明
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.setZOrderOnTop(true);
        }
        RelativeLayout.LayoutParams layoutParamsGdx = new RelativeLayout.LayoutParams(AppConfig.DESIGN_WIDTH, AppConfig.DESIGN_HEIGHT);
        rootView.addView(gdxView, layoutParamsGdx);


        // jikVideoView
//        videoView = new TTPlayView(this);
//        videoView.setUsingMediaCodec(true);
//        videoView.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT); //全屏
//        videoView.setFocusable(false);
//        RelativeLayout.LayoutParams layoutParamsVideo = new RelativeLayout.LayoutParams(AppConfig.DESIGN_WIDTH, AppConfig.DESIGN_HEIGHT);
//        rootView.addView(videoView, layoutParamsVideo);
//
//        WonderGame.getInstance().setIttMediaController(videoView);
//
//        TestBean tt = new TestBean();
//        tt.setName("http://125.88.92.166:30001/PLTV/88888956/224/3221227713/1.m3u8");
//        WonderGame.getInstance().getIttMediaController().startPlay(tt);

        return rootView;
    }


    @Override
    protected void onResume() {
        super.onResume();
        gdxView.requestFocus();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // kill process
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
