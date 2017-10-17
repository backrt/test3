package com.mygdx.game.android.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.view.GdxBaseView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleEffectScreen extends BaseScreen {

    ParticleEffectPool mParticleEffectPool;
    ParticleEffectPool mParticleEffectPool2;
    ParticleEffectPool mParticleEffectPool3;
    ParticleEffectPool mParticleEffectPool4;

    List<ParticleEffect> particlelist;

    Random mRandom = new Random();

    int i;


    @Override
    public void show() {
        super.show();

        //初始化粒子
        this.initParticleEffect();
    }


    private void make_draw_shooter() {

        GdxBaseView shootActor;
        shootActor = new GdxBaseView();
        Pixmap pixmap = ImageLoader.getInstance().loadImageSync("assets://particle/shoot.png");
        if (pixmap != null)
            shootActor.setBackgroundTexture(new Texture(pixmap));

        shootActor.setPostion(mRandom.nextInt(Gdx.graphics.getWidth()), -mRandom.nextInt(100), 8, 124);
        mStage.addActor(shootActor);

        shootActor.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.fadeIn(0.25f),
                                Actions.moveTo(
                                        shootActor.getX(),
                                        mRandom.nextInt(
                                                Gdx.graphics.getHeight() / 2) +
                                                Gdx.graphics.getHeight() / 2,
                                        0.45f)
                        ),
                        Actions.delay(0.30f, Actions.visible(false))
                )

        );
//        Timer.instance().scheduleTask(new Timer.Task() {
//            @Override
//            public void run() {
//
//            }
//        },0.80f);
//


//        shootActor.remove();
    }


    private void initParticleEffect() {

        ParticleEffect mParticleEffect = new ParticleEffect();
        mParticleEffect.load(
                Gdx.files.internal("particle/yanhua1.plist"),
                Gdx.files.internal("particle")
        );
        mParticleEffectPool = new ParticleEffectPool(mParticleEffect, 2, 10);

        ParticleEffect mParticleEffect2 = new ParticleEffect();
        mParticleEffect2.load(
                Gdx.files.internal("particle/yanhua2.plist"),
                Gdx.files.internal("particle")
        );
        mParticleEffectPool2 = new ParticleEffectPool(mParticleEffect2, 2, 10);

        ParticleEffect mParticleEffect3 = new ParticleEffect();
        mParticleEffect3.load(
                Gdx.files.internal("particle/yanhua3_1.plist"),
                Gdx.files.internal("particle")
        );
        mParticleEffectPool3 = new ParticleEffectPool(mParticleEffect3, 2, 10);

        ParticleEffect mParticleEffect4 = new ParticleEffect();
        mParticleEffect4.load(
                Gdx.files.internal("particle/yanhua4.plist"),
                Gdx.files.internal("particle")
        );
        mParticleEffectPool4 = new ParticleEffectPool(mParticleEffect4, 2, 10);

        particlelist = new ArrayList<>();
    }


    private void drawParticleEffect(Batch batch, ParticleEffectPool pool) {

        // obtain particleEffect and add to list
        ParticleEffect mTempParticleEffect = pool.obtain();
        mTempParticleEffect.setPosition(
                mRandom.nextInt(Gdx.graphics.getWidth()),
                mRandom.nextInt(Gdx.graphics.getHeight() / 2) + Gdx.graphics.getHeight() / 2
        );
        particlelist.add(mTempParticleEffect);

        // draw particleEffect
        batch.begin();
        for (int i = 0; i < particlelist.size(); i++) {
            particlelist.get(i).draw(batch, Gdx.graphics.getDeltaTime());
        }
        batch.end();


        //清除已经播放完成的粒子系统
        ParticleEffect temparticle;
        for (int i = 0; i < particlelist.size(); i++) {
            temparticle = particlelist.get(i);
            if (temparticle.isComplete()) {
                particlelist.remove(i);
            }
        }
    }


    @Override
    public void render(float delta) {
        super.render(delta);

        make_draw_shooter();

        i++;
        this.drawParticleEffect(mStage.getBatch(), mParticleEffectPool);
        if (i % 7 == 0)
            this.drawParticleEffect(mStage.getBatch(), mParticleEffectPool2);
        if (i % 5 == 0)
            this.drawParticleEffect(mStage.getBatch(), mParticleEffectPool3);
        if (i % 3 == 0)
            this.drawParticleEffect(mStage.getBatch(), mParticleEffectPool4);
    }

    private void disposeParticleEffect() {
        mParticleEffectPool.clear();
    }


    @Override
    public void dispose() {
        super.dispose();
        this.disposeParticleEffect();
    }

}
