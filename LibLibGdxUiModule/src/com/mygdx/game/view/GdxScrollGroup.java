package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.UIConfig;


public class GdxScrollGroup extends GdxAbsScrollGroup {

    private float scrollAmend;
    private float defaultRollBackDis;

    private Camera camera;

    // 支持回滚的开关,默认下关闭O
    private boolean isCanRollBackLeft;
    private boolean isCanRollBackRight;
    private boolean isCanRollBackTop;
    private boolean isCanRollBackBottom;
    private boolean isFocusInMiddle = true; //控制浮动框的位置


    private Actor curRollbackingActor;

    private Vector2 curRollbackingActorPos;
    private Vector2 curScrollActorPos;

    private float floatViewPosX;
    private float floatViewPosY;
    private final float DEFAULT_FLOATVIEW_ANIMATION_TIME = 0.125f;


    public GdxScrollGroup() {
        super();
        this.init();
        this.setCullingArea(new Rectangle(
                        0,
                        0,
                        Gdx.graphics.getWidth(),
                        Gdx.graphics.getHeight()
                )
        );
    }

    public GdxScrollGroup(int x,
                          int y,
                          int width,
                          int height) {
        this.init();
        this.setPosition(x, y);
        this.setSize(width, height);
        this.setCullingArea(new Rectangle(x, y, width, height));
    }


    private void initData() {

        this.isCanRollBackBottom = true;
        this.isCanRollBackLeft = true;
        this.isCanRollBackTop = true;
        this.isCanRollBackRight = true;

        curScrollActorPos = new Vector2();
        curRollbackingActorPos = new Vector2();
    }

    private void init() {
        this.updaeCamera();
        this.setSpeedType(ScrollSpeedType.fast);
        this.initData();
    }


    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
//        if (width < viewPortWidth / 2
//                || height < viewPortHeight / 2)
//            this.isFocusInMiddle = false;
    }

    public void updaeCamera() {

        camera = new OrthographicCamera(UIConfig.DESIGN_WIDTH, UIConfig.DESIGN_HEIGHT);
        camera.position.set(UIConfig.DESIGN_WIDTH / 2, UIConfig.DESIGN_HEIGHT / 2, 0);
        camera.project(new Vector3(UIConfig.DESIGN_WIDTH, UIConfig.DESIGN_HEIGHT, 0));
        camera.update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //更新相机视角
        camera.update();
        batch.setProjectionMatrix(camera.combined);


        // 设置剪裁区域
        this.clipBegin(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        super.draw(batch, parentAlpha);
        this.clipEnd();


        //还原相机视角
        batch.setProjectionMatrix(this.getStage().getCamera().combined);
    }

    /**
     * 实际的滚动操作，主要通过改变摄像机的位置
     */
    private void doScroll() {
        float cameraMoveDistanceTemp = scrollDistance * Gdx.graphics.getDeltaTime() * scrollSpeed;
        float floatViewDistanceYTemp = Math.abs(floatViewPosY - mFloatView.getY()) * Gdx.graphics.getDeltaTime() * scrollSpeed;
        float floatViewDistanceXTemp = Math.abs(floatViewPosX - mFloatView.getX()) * Gdx.graphics.getDeltaTime() * scrollSpeed;

        //test
        System.out.println("帧数 : " + (int) (1 / Gdx.graphics.getDeltaTime()));

        if (cameraMoveDistanceTemp <= 1f) {
            scrollDistance = 0;
        }

        float disX = 0;
        float disY = 0;
        float disX_ = 0;
        float disY_ = 0;

        switch (this.scrollOrientation) {

            case left:
                disX = -cameraMoveDistanceTemp;
                disX_ = -floatViewDistanceXTemp;
                break;
            case right:
                disX = cameraMoveDistanceTemp;
                disX_ = floatViewDistanceXTemp;
                break;
            case up:
                disY = cameraMoveDistanceTemp;
                disY_ = floatViewDistanceYTemp;
                break;
            case down:
                disY = -cameraMoveDistanceTemp;
                disY_ = -floatViewDistanceYTemp;
                break;
        }

        //移动相机视角
        camera.position.set(
                camera.position.x + disX,
                camera.position.y + disY,
                camera.position.z);


        // 移动浮动框
        mFloatView.setPosition(
                mFloatView.getX() + disX_,
                mFloatView.getY() + disY_
        );

        scrollDistance -= cameraMoveDistanceTemp;
        if (scrollDistance <= 2.0f) {
            scrollDistance = 0;
            if (isRollBack) {
                //回滚反弹
                doRollBackReverse();
            }

            //修正浮动框
            mFloatView.clearActions();
            mFloatView.addAction(
                    Actions.moveTo(
                            floatViewPosX,
                            floatViewPosY,
                            DEFAULT_FLOATVIEW_ANIMATION_TIME
                    )
            );
        }
    }


    /**
     * 回滚反向滚动
     */
    private void doRollBackReverse() {
        if (curRollbackingActor == null)
            return;

        curRollbackingActorPos.setZero();
        curRollbackingActor.localToStageCoordinates(curRollbackingActorPos);

        switch (scrollOrientation) {

            case down:
                float py2 = this.getY() - camera.viewportHeight / 2;
                float py2_ = curRollbackingActorPos.y - camera.position.y;
                this.scrollBy(ScrollOrientation.up,
                        Math.abs(py2 - py2_));
                break;
            case up:
                float py1 = this.getY() + this.getHeight() - camera.viewportHeight / 2;
                float py1_ = curRollbackingActorPos.y + curRollbackingActor.getHeight()
                        - camera.position.y;
                this.scrollBy(ScrollOrientation.down,
                        Math.abs(py1_ - py1));
                break;
            case left:
                float px1 = this.getX() - camera.viewportWidth / 2;
                float px1_ = curRollbackingActorPos.x - camera.position.x;
                this.scrollBy(ScrollOrientation.right,
                        Math.abs(px1 - px1_));
                break;
            case right:
                float px2 = this.getWidth() + this.getX() - camera.viewportWidth / 2;
                float px2_ = curRollbackingActorPos.x + curRollbackingActor.getWidth()
                        - camera.position.x;
                this.scrollBy(ScrollOrientation.left,
                        Math.abs(px2 - px2_));
                break;
        }

        this.isRollBack = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (scrollDistance > 2.0f) {
            doScroll();
        }
    }

    public float getScrollAmend() {
        return scrollAmend;
    }

    public void setScrollAmend(float scrollAmend) {
        this.scrollAmend = scrollAmend;
    }

    public float getDefaultRollBackDis() {
        return defaultRollBackDis;
    }

    public void setDefaultRollBackDis(float defaultRollBackDis) {
        this.defaultRollBackDis = defaultRollBackDis;
    }

    public boolean isCanRollBackLeft() {
        return isCanRollBackLeft;
    }

    public void setRollBackLeft(boolean isCanRollBackLeft) {
        this.isCanRollBackLeft = isCanRollBackLeft;
    }

    public boolean isCanRollBackRight() {
        return isCanRollBackRight;
    }

    public void setRollBackRight(boolean isCanRollBackRight) {
        this.isCanRollBackRight = isCanRollBackRight;
    }

    public boolean isCanRollBackTop() {
        return isCanRollBackTop;
    }

    public void setRollBackTop(boolean isCanRollBackTop) {
        this.isCanRollBackTop = isCanRollBackTop;
    }

    public boolean isCanRollBackBottom() {
        return isCanRollBackBottom;
    }

    public void setRollBackBottom(boolean isCanRollBackBottom) {
        this.isCanRollBackBottom = isCanRollBackBottom;
    }

    @Override
    public void rollBack(ScrollOrientation orientation, Actor fromActor) {

        if (fromActor == null || isRollBack)
            return;

        float rollBackDisance = 0;

        switch (orientation) {

            case up:
                if (!isCanRollBackTop)
                    return;
                rollBackDisance = (defaultRollBackDis == 0 ? fromActor
                        .getHeight() / 6 : defaultRollBackDis) + scrollAmend;
                break;
            case down:
                if (!isCanRollBackBottom)
                    return;
                rollBackDisance = (defaultRollBackDis == 0 ? fromActor
                        .getHeight() / 6 : defaultRollBackDis) + scrollAmend;
                break;
            case left:
                if (!isCanRollBackLeft)
                    return;
                rollBackDisance = (defaultRollBackDis == 0 ? fromActor
                        .getWidth() / 6 : defaultRollBackDis) + scrollAmend;
                break;
            case right:
                if (!isCanRollBackRight)
                    return;
                rollBackDisance = (defaultRollBackDis == 0 ? fromActor
                        .getWidth() / 6 : defaultRollBackDis) + scrollAmend;
                break;
        }

        if (this.scrollDistance < rollBackDisance)
            this.scrollDistance += rollBackDisance;
        else
            return;
        this.scrollOrientation = orientation;
        this.curRollbackingActor = fromActor;
        this.isRollBack = true;
    }

    @Override
    public void scrollBy(ScrollOrientation orientation, float dis) {
        this.scrollDistance = dis;
        this.scrollOrientation = orientation;
    }

    @Override
    public synchronized boolean scrollTo(ScrollOrientation orientation, Actor actor) {

        System.out.println("--------------------------- scrollTo-------------------------------------");

        if (actor == null)
            return false;

        curScrollActorPos.setZero();
        actor.localToStageCoordinates(curScrollActorPos);


        float offsetChild = 0;
        float offset = 0;
        float margin = 0;
        switch (orientation) {

            case left:
                offset = camera.position.x - camera.viewportWidth / 2;
                if (offset < 0)
                    return false;

                if (isFocusInMiddle) {
                    float px1 = this.getX();
                    float px1_ = curScrollActorPos.x + actor.getWidth() / 2 - camera.position.x;
                    offsetChild = px1 - px1_;
                    offsetChild = Math.min(offset, offsetChild);
                } else {
                    float px1 = this.getX() - camera.viewportWidth / 2;
                    float px1_ = curScrollActorPos.x - camera.position.x;
                    offsetChild = px1 - px1_;

                    //item view margin
                    if (actor instanceof GdxBaseView) {
                        margin = ((GdxBaseView) actor).getMarginLeft();
                    } else if (actor instanceof GdxBaseGroupView) {
                        margin = ((GdxBaseGroupView) actor).getMarginLeft();
                    }
                    //add father view margin
                    margin += super.getMarginLeft();

                    //add spaceing
                    if (this instanceof GdxAbsAdapterView) {
                        margin += ((GdxAbsAdapterView) this).getHorizontalSpace();
                    }
                }

                break;
            case right:

                if (isFocusInMiddle) {
                    float px2 = this.getWidth() + this.getX() - camera.viewportWidth;
                    float px2_ = curScrollActorPos.x + actor.getWidth() / 2 - camera.position.x;
                    offsetChild = px2_ - px2;
                    offset = (this.getContentWidth() - this.getWidth()) - (camera.position.x - camera.viewportWidth / 2); //总共需要滚动的距离 - 已经滚动的距离
                    scrollAmend = 0;
                    if (offset > 0)
                        offsetChild = Math.min(offset, offsetChild);
                } else {
                    float px2 = this.getWidth() + this.getX() - camera.viewportWidth / 2;
                    float px2_ = curScrollActorPos.x + actor.getWidth() - camera.position.x;
                    offsetChild = px2_ - px2;

                    // item view margin
                    if (actor instanceof GdxBaseView) {
                        margin = ((GdxBaseView) actor).getMarginRight();
                    } else if (actor instanceof GdxBaseGroupView) {
                        margin = ((GdxBaseGroupView) actor).getMarginRight();
                    }

                    // add father margin
                    margin += super.getMarginRight();

                    //add spaceing
                    if (this instanceof GdxAbsAdapterView) {
                        margin += ((GdxAbsAdapterView) this).getHorizontalSpace();
                    }
                }

                break;
            case up:
                offset = camera.viewportHeight / 2 - camera.position.y;
                if (offset < 0)
                    return false;

                if (isFocusInMiddle) {
                    float py1 = this.getY() + this.getHeight() - camera.viewportHeight;
                    float py1_ = curScrollActorPos.y + actor.getHeight() / 2 - camera.position.y;
                    offsetChild = py1_ - py1;
                    scrollAmend = 0;
                    offsetChild = Math.min(offsetChild, offset);
                } else {
                    float py1 = this.getY() + this.getHeight() - camera.viewportHeight / 2;
                    float py1_ = curScrollActorPos.y + actor.getHeight() - camera.position.y;
                    offsetChild = py1_ - py1;

                    // item view margin
                    if (actor instanceof GdxBaseView) {
                        margin = ((GdxBaseView) actor).getMarginTop();
                    } else if (actor instanceof GdxBaseGroupView) {
                        margin = ((GdxBaseGroupView) actor).getMarginTop();
                    }

                    // father view margin
                    margin += super.getMarginTop();

                    // add sapceing
                    if (this instanceof GdxAbsAdapterView) {
                        margin += ((GdxAbsAdapterView) this).getVerticalSpace();
                    }
                }
                break;
            case down:

                if (isFocusInMiddle) {
                    float py2 = this.getY();
                    float py2_ = curScrollActorPos.y + actor.getHeight() / 2 - camera.position.y;
                    offsetChild = py2 - py2_;
                    offset = (this.getContentHeight() - this.getHeight())
                            - (camera.position.y > 0 ? (camera.viewportHeight / 2 - camera.position.y)
                            : (Math.abs(camera.position.y) + camera.viewportHeight / 2)); //总共需要滚动的距离 - 已经滚动的距离
                    scrollAmend = 0;
                    if (offset > 0)
                        offsetChild = Math.min(offset, offsetChild);
                } else {
                    float py2 = this.getY() - camera.viewportHeight / 2;
                    float py2_ = curScrollActorPos.y - camera.position.y;
                    offsetChild = py2 - py2_;

                    // item view margin
                    if (actor instanceof GdxBaseView) {
                        margin = ((GdxBaseView) actor).getMarginBottom();
                    } else if (actor instanceof GdxBaseGroupView) {
                        margin = ((GdxBaseGroupView) actor).getMarginBottom();
                    }

                    // father view margin
                    margin += super.getMarginBottom();

                    // add sapceing
                    if (this instanceof GdxAbsAdapterView) {
                        margin += ((GdxAbsAdapterView) this).getVerticalSpace();
                    }
                }

                break;
        }


        //判断需要滚动的情况下，计算剪裁区域;(其实就是划出需要渲染的item的区域)
        if (offsetChild > 0) { // need scroll
            scrollDistance = offsetChild + margin + scrollAmend;
            this.scrollOrientation = orientation;

            /**
             * 计算剪裁区域
             */
            float cullAreaPosX = 0;
            float cullAreaPosY = 0;
            float cullAreaWidth = 0;
            float cullAreaHeight = 0;

            switch (orientation) {

                case up:
                    cullAreaPosX = actor.getX() - this.getWidth() - actor.getWidth();
                    cullAreaPosY = actor.getY() - this.getHeight() * 2.5f;
                    cullAreaWidth = this.getWidth() * 2f;
                    cullAreaHeight = this.getHeight() * 3.5f + actor.getHeight();
                    break;

                case down:
                    cullAreaPosX = actor.getX() - this.getWidth() - actor.getWidth();
                    cullAreaPosY = actor.getY() - this.getHeight() / 2f;
                    cullAreaWidth = this.getWidth() * 2f;
                    cullAreaHeight = this.getHeight() * 3.5f + actor.getHeight();
                    break;

                case left:
                    cullAreaPosX = actor.getX() - actor.getWidth();
                    cullAreaPosY = actor.getY() - actor.getHeight() - this.getHeight();
                    cullAreaWidth = this.getWidth() * 2.0f;
                    cullAreaHeight = this.getHeight() * 2f;
                    break;

                case right:
                    cullAreaPosX = actor.getX() - this.getWidth() * 1.5f - actor.getWidth();
                    cullAreaPosY = actor.getY() - this.getHeight() - actor.getHeight();
                    cullAreaWidth = this.getWidth() * 2.25f;
                    cullAreaHeight = this.getHeight() * 2f;
                    break;
            }

            this.getCullingArea().set(cullAreaPosX, cullAreaPosY, cullAreaWidth, cullAreaHeight);
            //test
            System.out.println(this.getCullingArea().toString());
            return true;
        }
        return false;
    }

    @Override
    public void setSpeedType(ScrollSpeedType speedType) {
        this.speedType = speedType;

        switch (speedType) {

            case fast:
                scrollSpeed = this.camera.viewportWidth / 250f;
                break;
            case medium:
                scrollSpeed = (this.camera.viewportWidth + this.camera.viewportHeight) / 300f;
                break;
            case slow:
                scrollSpeed = this.camera.viewportWidth / 440f;
                break;
        }
    }


    public Camera getCamera() {
        return camera;
    }

    @Override
    public void dispose() {
        super.dispose();
        camera = null;
        curRollbackingActorPos = null;
        curScrollActorPos = null;
        curRollbackingActor = null;
    }


    @Override
    protected void onViewFloat(Actor actor) {

        if (scrollDistance > 0) {
            this.floatViewAnimation(actor);
        } else
            super.onViewFloat(actor);
    }


    private void floatViewAnimation(Actor actor) {
        mFloatView.clearActions();

        mFloatLoc.setZero();
        actor.localToParentCoordinates(mFloatLoc);

        float px = (actor.getWidth() - mFloatView.getWidth()) / 2;
        float py = (actor.getHeight() - mFloatView.getHeight()) / 2;

        floatViewPosX = mFloatLoc.x + px;
        floatViewPosY = mFloatLoc.y + py;

        if (!mFloatView.isShow())
            mFloatView.setShow(true);

        if (isFloatViewInBack)
            mFloatView.toBack();
        else
            mFloatView.toFront();

        //自己控制浮动框的动画
        mFloatView.updatePositon(actor.getWidth(), actor.getHeight());

        if (actor instanceof GdxScaleableView) {
            mFloatView.addAction(
                    Actions.scaleTo(
                            ((GdxScaleableView) actor).getAnimationScaleX(),
                            ((GdxScaleableView) actor).getAnimationScaleY(),
                            ((GdxScaleableView) actor).getAnimationTime())
            );

        } else if (actor instanceof GdxScaleableGroupView) {
            mFloatView.addAction(
                    Actions.scaleTo(
                            ((GdxScaleableGroupView) actor).getAnimationScaleX(),
                            ((GdxScaleableGroupView) actor).getAnimationScaleY(),
                            ((GdxScaleableGroupView) actor).getAnimationTime())
            );
        }
    }


    /**
     * 获取所有item的宽度和
     *
     * @return
     */
    protected float getContentWidth() {
        return this.getWidth();
    }


    /**
     * 获取所有item的高度和
     *
     * @return
     */
    protected float getContentHeight() {
        return this.getHeight();
    }


}
