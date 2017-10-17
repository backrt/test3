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

public class ParticleEffectScreen2 extends BaseScreen {

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


    private void addShooterParticles(final ParticleEffectPool pool) {

        final GdxBaseView shootActor = new GdxBaseView();
        Pixmap pixmap = ImageLoader.getInstance().loadImageSync("assets://particle/shoot.png");
        if (pixmap != null)
            shootActor.setBackgroundTexture(new Texture(pixmap));

        shootActor.setPostion(mRandom.nextInt(Gdx.graphics.getWidth()), -mRandom.nextInt(100), 8, 124);
        mStage.addActor(shootActor);


        float x = shootActor.getX();
        float y = mRandom.nextInt(Gdx.graphics.getHeight() / 2) + Gdx.graphics.getHeight() / 2;
        shootActor.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.fadeIn(0.25f),
                                Actions.moveTo(
                                        x,
                                        y,
                                        0.45f
                                )
                        ),
                        Actions.delay(0.10f, Actions.fadeOut(0.14f))
                )
        );


        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                float x = shootActor.getX();
                float y = shootActor.getY();

                makeParticleEffect(pool, x, y);

                //移除actor
                shootActor.remove();
            }
        }, 0.85f);
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


    private void makeParticleEffect(ParticleEffectPool pool,
                                    float positionX,
                                    float positionY) {
        // 生成一个新的粒子
        ParticleEffect mTempParticleEffect = pool.obtain();
        mTempParticleEffect.setPosition(
                positionX,
                positionY
        );
        mTempParticleEffect.scaleEffect(mRandom.nextFloat() + 0.52f);
        particlelist.add(mTempParticleEffect);


        //清除已经播放完成的粒子系统
        for (int i = 0; i < particlelist.size(); i++) {
            if (particlelist.get(i).isComplete()) {
                particlelist.remove(i);
            }
//            else {
//                particlelist.get(i).scaleEffect(
//                        particlelist.get(i)
//                                .getEmitters()
//                                .get(0).getScale()
//                                .getScaling()[0] + 0.25f);
//            }
        }

    }


    private void drawParticles(Batch batch) {

        batch.begin();
        for (int i = 0; i < particlelist.size(); i++) {
            particlelist.get(i).draw(batch, Gdx.graphics.getDeltaTime());
        }
        batch.end();
    }


    @Override
    public void render(float delta) {
        super.render(delta);

        switch (i++ % 4) {

            case 0:
                if (mRandom.nextInt(7) == 0)
                    addShooterParticles(mParticleEffectPool);
                break;

            case 1:
                if (mRandom.nextInt(16) == 1)
                    addShooterParticles(mParticleEffectPool2);
                break;

            case 2:
                if (mRandom.nextInt(7) == 1)
                    addShooterParticles(mParticleEffectPool3);
                break;

            case 3:
                if (mRandom.nextInt(5) == 0)
                    addShooterParticles(mParticleEffectPool4);
                break;
        }


        drawParticles(mStage.getBatch());

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
