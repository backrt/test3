package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.mygdx.game.view.listener.IDisposeListener;
import com.mygdx.game.view.listener.IFocusChangeCallback;
import com.mygdx.game.view.listener.IItemClickedCallback;
import com.mygdx.game.view.listener.IItemSelectChangedCallback;
import com.mygdx.game.view.listener.OnFocusChangeListener;
import com.mygdx.game.view.listener.OnItemClickListener;
import com.mygdx.game.view.listener.OnItemSelectChangedListener;
import com.mygdx.game.view.listener.OnKeyListener;
import com.mygdx.game.view.utils.Tool;

import static com.mygdx.game.view.GdxBaseView.TAG_FLOATVIEW;

public class GdxBaseGroupView
        extends Group
        implements
        OnKeyListener,
        IItemSelectChangedCallback,
        IItemClickedCallback,
        IFocusChangeCallback,
        IDisposeListener {

    private Actor curFocusChild;  //当前获得焦点的view

    private Texture backgroundTexture;  // 背景图

    // 一些简单的回调接口
    private OnItemSelectChangedListener itemSelectedChangeListener;
    private OnItemClickListener itemClickListener;
    private OnFocusChangeListener onFocusChangeListener;
    private OnKeyListener keyListener;

    protected int marginLeft, marginRight, marginTop, marginBottom;

    protected GdxNinePathImageView mFloatView;  //浮动框view

    private boolean isFocusAble = true;
    private boolean isFocus;
    protected boolean isShowingFloatView;  // 是否显示浮动框的标记
    protected boolean isFloatViewInBack;  // 浮动框的显示 zIndex
    protected boolean enable = true;

    private String nextFocusRight;
    private String nextFocusLeft;
    private String nextFocusUp;
    private String nextFocusDown;

    private Vector2 vt1 = new Vector2();
    private Vector2 vt2 = new Vector2();
    protected Vector2 mFloatLoc;

    public static final int FOCUS_BEFORE_DESCENDANTS = 0x10000;
    public static final int FOCUS_AFTER_DESCENDANTS = 0x20000;

    private int descendantFocusability = FOCUS_BEFORE_DESCENDANTS;


    public void addFloatView(GdxNinePathImageView mfloatView) {
        this.mFloatView = mfloatView;
        this.mFloatLoc = new Vector2();
        this.addActor(this.mFloatView);
    }

    public void showFloatView() {
        if (isShowingFloatView
                || this.mFloatView == null)
            return;

        mFloatView.setVisible(true);
        isShowingFloatView = true;
    }

    public void hideFloatView() {

        if (!isShowingFloatView
                || this.mFloatView == null)
            return;

        mFloatView.setVisible(false);
        isShowingFloatView = false;
    }


    /**
     * 浮动框移动
     *
     * @param actor
     */
    protected void onViewFloat(Actor actor) {
        if (!isShowingFloatView
                || mFloatLoc == null
                || mFloatView == null) {
            return;
        }

        mFloatLoc.setZero();
        actor.localToParentCoordinates(mFloatLoc);

        mFloatView.clearActions();

        if (!mFloatView.isShow())
            mFloatView.setShow(true);

        float px = (actor.getWidth() - mFloatView.getWidth()) / 2;
        float py = (actor.getHeight() - mFloatView.getHeight()) / 2;

        if (isFloatViewInBack)
            mFloatView.toBack();
        else
            mFloatView.toFront();

        mFloatView.updatePositon(actor.getWidth(), actor.getHeight());

        float moveSpeed = Gdx.graphics.getDeltaTime() * 1920 / 440;
        float moveDistance = Math.abs(mFloatLoc.x + px - mFloatView.getX() + 30);
//
        float moveActionTime = 0;
        float smallDis = 0;
        while (moveDistance >= 3.0f) {
            moveActionTime += Gdx.graphics.getDeltaTime();
            smallDis = moveSpeed * moveDistance;
            moveDistance -= smallDis;
        }

        moveActionTime /= 3;

        if (actor instanceof GdxScaleableView) {

            mFloatView.addAction(Actions.parallel(
                    Actions.scaleTo(
                            ((GdxScaleableView) actor).getAnimationScaleX(),
                            ((GdxScaleableView) actor).getAnimationScaleY(),
                            ((GdxScaleableView) actor).getAnimationTime()),
                    Actions.moveTo(mFloatLoc.x + px,
                            mFloatLoc.y + py, moveActionTime)
                    ) // 0.35f
            );

        } else if (actor instanceof GdxScaleableGroupView) {

            mFloatView.addAction(Actions.parallel(
                    Actions.scaleTo(
                            ((GdxScaleableGroupView) actor).getAnimationScaleX(),
                            ((GdxScaleableGroupView) actor).getAnimationScaleY(),
                            ((GdxScaleableGroupView) actor).getAnimationTime()),
                    Actions.moveTo(mFloatLoc.x + px,
                            mFloatLoc.y + py, moveActionTime)
                    )
            );

        } else
            mFloatView.addAction(Actions.parallel(
                    Actions.moveTo(mFloatLoc.x + px,
                            mFloatLoc.y + py, moveActionTime)
                    )
            );
    }


    /**
     * 简单算法，获取当前按键最靠近的view
     *
     * @param event
     * @param keyCode
     * @return
     */
    private Actor findNearestActor(keyEvent event, int keyCode) {

        boolean isMostLeft = false;
        boolean isMostTop = false;

        switch (keyCode) {

            case Input.Keys.DPAD_DOWN:
                isMostLeft = true;
                isMostTop = true;
                break;

            case Input.Keys.DPAD_UP:
                isMostLeft = true;
                isMostTop = false;
                break;

            case Input.Keys.DPAD_LEFT:
                isMostLeft = false;
                break;

            case Input.Keys.DPAD_RIGHT:
                isMostLeft = true;
                break;

        }

        Actor tempActor = null;
        for (Actor childActor : this.getChildren().items) {

            if (!(childActor instanceof IFocusChangeCallback))
                continue;

            if (childActor == null
                    || (childActor.getName() != null
                    && childActor.getName().equals(TAG_FLOATVIEW)))
                continue;


            if (childActor instanceof GdxBaseView
                    && !((GdxBaseView) childActor).isFocusAble())
                continue;


            if (childActor instanceof GdxBaseGroupView
                    && !((GdxBaseGroupView) childActor).isFocusAble())
                continue;


            if (tempActor == null) {
                tempActor = childActor;
                continue;
            } else {

                if (isMostLeft) {
                    if (childActor.getX() < tempActor.getX()) {
                        tempActor = childActor;
                    } else {
                        if (isMostTop) {
                            if (childActor.getY() < tempActor.getY())
                                tempActor = childActor;
                        } else {
                            if (childActor.getY() < tempActor.getY()) {
                                tempActor = childActor;
                            }
                        }
                    }
                } else {
                    if (childActor.getX() > tempActor.getX()) {
                        tempActor = childActor;
                    }
                }
            }
        }

        return tempActor;
    }


    /**
     * @return Actor
     */
    public Actor findDefaultSelctedChild() {

        if (this.hasChildren()) {
            Actor temp = this.getChildren().get(0);
            if (temp != null
                    && temp.getName() != null
                    && temp.getName().equals(TAG_FLOATVIEW))
                temp = this.getChildren().get(1);
            return temp;
        }
        return null;
    }

    public void requestFocus() {
        if (this.curFocusChild == null) {
            curFocusChild = this.findDefaultSelctedChild();
        }

        if (curFocusChild != null
                && curFocusChild instanceof IFocusChangeCallback) {
            ((IFocusChangeCallback) curFocusChild).focusChange(true);
            this.itemSelectChangedCallback(curFocusChild);
        }
    }

    public boolean isFloatViewInBack() {
        return isFloatViewInBack;
    }

    public void setFloatViewInBack(boolean floatViewInBack) {
        isFloatViewInBack = floatViewInBack;
    }

    public Actor getcurFocusChild() {
        return curFocusChild;
    }

    public void setcurFocusChild(Actor curFocusChild) {
        this.curFocusChild = curFocusChild;
    }


    public void setItemSelectedChangeListener(OnItemSelectChangedListener itemSelectedChangeListener) {
        this.itemSelectedChangeListener = itemSelectedChangeListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OnKeyListener getKeyListener() {
        return keyListener;
    }

    public void setKeyListener(OnKeyListener keyListener) {
        this.keyListener = keyListener;
    }


    @Override
    public void itemSelectChangedCallback(Actor actor) {

        onViewFloat(actor);

        if (this.getParent() instanceof GdxBaseGroupView) {
            ((GdxBaseGroupView) this.getParent()).itemSelectChangedCallback(this);
        }

        this.curFocusChild = actor;
        //gain focus
        if (actor instanceof IFocusChangeCallback) {
            ((IFocusChangeCallback) actor).focusChange(true);
        }

        // callback
        int position = calcutlateItemosition(actor);
        if (itemSelectedChangeListener != null) {
            itemSelectedChangeListener.onItemSelectChanged(actor, position);
        }

        // adapterView callback ，处理动态的加载和移除，主要是一些消耗资源的视图操作。比如 加载图片
        if (this instanceof GdxAbsAdapterView) {
            ((GdxAbsAdapterView) this).getAdapter().updateView(this, position);
        }

    }

    @Override
    public void itemClickCallback(Actor actor) {
        if (itemClickListener != null) {
            int position = calcutlateItemosition(actor);
            itemClickListener.onItemClick(actor, position);
        }
    }

    public int getMarginLeft() {
        return marginLeft;
    }


    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }


    public int getMarginRight() {
        return marginRight;
    }


    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }


    public int getMarginTop() {
        return marginTop;
    }


    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }


    public int getMarginBottom() {
        return marginBottom;
    }


    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }


    public void setMargin(int marginLeft, int marginTop,
                          int marginRight, int marginBottom) {
        this.setMarginLeft(marginLeft);
        this.setMarginTop(marginTop);
        this.setMarginRight(marginRight);
        this.setMarginBottom(marginBottom);
    }

    @Override
    public boolean onKey(keyEvent event, int keyCode) {

        if (keyListener != null)
            return keyListener.onKey(event, keyCode);

        // 默认处理
        switch (descendantFocusability) {
            case FOCUS_AFTER_DESCENDANTS:
                return this.dispatchOnkey(event, keyCode);

            case FOCUS_BEFORE_DESCENDANTS:
                this.handleOnKey(this, keyCode);
                break;
        }
        return true;
    }

    @Override
    public void dispose() {

        curFocusChild = null;
        keyListener = null;
        itemSelectedChangeListener = null;
        itemClickListener = null;
    }


    public int calcutlateItemosition(Actor actor) {

        if (actor == null)
            return -1;

        int index1 = 0;
        if (mFloatView != null) {
            index1 = this.getChildren().indexOf(mFloatView, true);
        }
        int index = this.getChildren().indexOf(actor, true);
        return index < index1 ? index : index - 1;
    }


    /**
     * @return Actor
     */
    public Actor nextLeftOf(Actor childActor) {
        Actor temp = null;

        String childNextFocusLeft = null;
        if (childActor instanceof GdxBaseView) {
            childNextFocusLeft = ((GdxBaseView) childActor).getNextFocusLeft();
        } else if (childActor instanceof GdxBaseGroupView) {
            childNextFocusLeft = ((GdxBaseGroupView) childActor).getNextFocusLeft();
        }

        if (childNextFocusLeft != null) {
            temp = this.findActor(childNextFocusLeft);
            if (temp != null)
                return temp;
        }

        vt1.setZero();
        childActor.localToParentCoordinates(vt1);

        float h1 = childActor.getHeight();

        float rx = Float.MAX_VALUE;
        float ry = 0;
        float top = Float.MAX_VALUE;

        for (Actor aTemp : this.getChildren()) {

            if (!(aTemp instanceof IFocusChangeCallback))
                continue;

            if (childActor.equals(aTemp)
                    || (aTemp.getName() != null
                    && aTemp.getName().equals(TAG_FLOATVIEW))) {
                continue;
            }


            if (aTemp instanceof GdxBaseView
                    && !((GdxBaseView) aTemp).isFocusAble())
                continue;


            if (aTemp instanceof GdxBaseGroupView
                    && !((GdxBaseGroupView) aTemp).isFocusAble())
                continue;


            vt2.setZero();
            aTemp.localToParentCoordinates(vt2);

            float h2 = aTemp.getHeight();
            float lx = vt1.x - vt2.x;

            if (lx >= 0 && Tool.checkLine(vt1.y, h1, vt2.y, h2)) {
                float ly = Tool.lineCloss(vt1.y, h1, vt2.y, h2);
                if ((lx < rx
                        || (lx == rx && (vt2.y < top || ly > ry)))
                        && (ly >= h2 / 2 || ly >= h1 / 2)) {
                    top = vt2.y;
                    rx = lx;
                    ry = ly;
                    temp = aTemp;
                }
            }
        }
        if (temp != null && temp.getName() != null) {
            if (temp != null && temp.getName() != null) {
                if (childActor instanceof GdxBaseView) {
                    ((GdxBaseView) childActor).setNextFocusLeft(temp.getName());
                } else if (childActor instanceof GdxBaseGroupView) {
                    ((GdxBaseGroupView) childActor).setNextFocusLeft(temp.getName());
                }
            }
        }
        return temp;
    }

    /**
     * @return Actor
     */
    public Actor nextRightOf(Actor childActor) {

        if (childActor == null)
            return null;

        Actor temp = null;


        String ChildNextFocusRight = null;
        if (childActor instanceof GdxBaseView) {
            ChildNextFocusRight = ((GdxBaseView) childActor).getNextFocusRight();
        } else if (childActor instanceof GdxBaseGroupView) {
            ChildNextFocusRight = ((GdxBaseGroupView) childActor).getNextFocusRight();
        }

        if (ChildNextFocusRight != null) {
            temp = this.findActor(ChildNextFocusRight);
            if (temp != null)
                return temp;
        }

        vt1.setZero();
        childActor.localToParentCoordinates(vt1);

        float h1 = childActor.getHeight();

        float rx = Float.MAX_VALUE;
        float ry = 0;
        float top = Float.MAX_VALUE;


        for (Actor aTemp : this.getChildren()) {

            if (!(aTemp instanceof IFocusChangeCallback))
                continue;

            if (childActor.equals(aTemp)
                    || (aTemp.getName() != null
                    && aTemp.getName().equals(TAG_FLOATVIEW))) {
                continue;
            }


            if (aTemp instanceof GdxBaseView
                    && !((GdxBaseView) aTemp).isFocusAble())
                continue;


            if (aTemp instanceof GdxBaseGroupView
                    && !((GdxBaseGroupView) aTemp).isFocusAble())
                continue;


            vt2.setZero();
            aTemp.localToParentCoordinates(vt2);

            float h2 = aTemp.getHeight();
            float lx = vt2.x - vt1.x;

            if (lx >= 0 && Tool.checkLine(vt1.y, h1, vt2.y, h2)) {
                float ly = Tool.lineCloss(vt1.y, h1, vt2.y, h2);
                if ((lx < rx || (lx == rx && (vt2.y < top || ly > ry)))
                        && (ly >= h2 / 2 || ly >= h1 / 2)) {
                    top = vt2.y;
                    rx = lx;
                    ry = ly;
                    temp = aTemp;
                }
            }
        }
        if (temp != null && temp.getName() != null) {
            if (childActor instanceof GdxBaseView) {
                ((GdxBaseView) childActor).setNextFocusRight(temp.getName());
            } else if (childActor instanceof GdxBaseGroupView) {
                ((GdxBaseGroupView) childActor).setNextFocusRight(temp.getName());
            }
        }

        return temp;
    }

    /**
     * @return Actor
     */
    public Actor nextDownOf(Actor childActor) {

        if (childActor == null)
            return null;

        Actor temp = null;

        String childNextFocusDown = null;
        if (childActor instanceof GdxBaseView) {
            childNextFocusDown = ((GdxBaseView) childActor).getNextFocusDown();
        } else if (childActor instanceof GdxBaseGroupView) {
            childNextFocusDown = ((GdxBaseGroupView) childActor).getNextFocusDown();
        }

        if (childNextFocusDown != null) {
            temp = this.findActor(childNextFocusDown);
            if (temp != null)
                return temp;
        }

        vt1.setZero();
        childActor.localToParentCoordinates(vt1);

        float w1 = childActor.getWidth();

        float rx = Float.MAX_VALUE;
        float left = Float.MAX_VALUE;
        float ry = 0;


        for (Actor aTemp : this.getChildren()) {
            if (!(aTemp instanceof IFocusChangeCallback))
                continue;

            if (childActor.equals(aTemp)
                    || (aTemp.getName() != null
                    && aTemp.getName().equals(TAG_FLOATVIEW))) {
                continue;
            }


            if (aTemp instanceof GdxBaseView
                    && !((GdxBaseView) aTemp).isFocusAble())
                continue;


            if (aTemp instanceof GdxBaseGroupView
                    && !((GdxBaseGroupView) aTemp).isFocusAble())
                continue;

            vt2.setZero();
            aTemp.localToParentCoordinates(vt2);

            float w2 = aTemp.getWidth();
            float lx = vt1.y - vt2.y;
            if (lx >= 0 && Tool.checkLine(vt1.x, w1, vt2.x, w2)) {
                float ly = Tool.lineCloss(vt1.x, w1, vt2.x, w2);
                if ((lx < rx || (lx == rx && (vt2.x < left || ly > ry)))
                        && (ly >= w2 / 2 || ly >= w1 / 2)) {
                    left = vt2.x;
                    rx = lx;
                    ry = ly;
                    temp = aTemp;
                }
            }
        }
        if (temp != null && temp.getName() != null) {
            if (temp != null && temp.getName() != null) {
                if (childActor instanceof GdxBaseView) {
                    ((GdxBaseView) childActor).setNextFocusDown(temp.getName());
                } else if (childActor instanceof GdxBaseGroupView) {
                    ((GdxBaseGroupView) childActor).setNextFocusDown(temp.getName());
                }
            }
        }
        return temp;
    }

    /**
     * @return Actor
     */
    public Actor nextUpOf(Actor childActor) {
        if (childActor == null)
            return null;


        Actor temp = null;

        String childNextFocusUp = null;
        if (childActor instanceof GdxBaseView) {
            childNextFocusUp = ((GdxBaseView) childActor).getNextFocusUp();
        } else if (childActor instanceof GdxBaseGroupView) {
            childNextFocusUp = ((GdxBaseGroupView) childActor).getNextFocusUp();
        }

        if (childNextFocusUp != null) {
            temp = this.findActor(childNextFocusUp);
            if (temp != null)
                return temp;
        }

        vt1.setZero();
        childActor.localToParentCoordinates(vt1);

        float w1 = childActor.getWidth();

        float rx = Float.MAX_VALUE;
        float left = Float.MAX_VALUE;
        float ry = 0;

        for (Actor aTemp : this.getChildren()) {

            if (!(aTemp instanceof IFocusChangeCallback)) {
                continue;
            }

            if (childActor.equals(aTemp)
                    || (aTemp.getName() != null
                    && aTemp.getName().equals(TAG_FLOATVIEW))) {
                continue;
            }


            if (aTemp instanceof GdxBaseView
                    && !((GdxBaseView) aTemp).isFocusAble()) {
                continue;
            }


            if (aTemp instanceof GdxBaseGroupView
                    && !((GdxBaseGroupView) aTemp).isFocusAble()) {
                continue;
            }

            vt2.setZero();
            aTemp.localToParentCoordinates(vt2);

            float w2 = aTemp.getWidth();
            float lx = vt2.y - vt1.y;
            if (lx >= 0 && Tool.checkLine(vt1.x, w1, vt2.x, w2)) {
                float ly = Tool.lineCloss(vt1.x, w1, vt2.x, w2);
                if ((lx < rx || (lx == rx && (vt2.x < left || ly > ry)))
                        && (ly >= w2 / 2 || ly >= w1 / 2)) {
                    left = vt2.x;
                    rx = lx;
                    ry = ly;
                    temp = aTemp;
                }
            }
        }
        if (temp != null
                && temp.getName() != null) {
            if (temp != null && temp.getName() != null) {
                if (childActor instanceof GdxBaseView) {
                    ((GdxBaseView) childActor).setNextFocusUp(temp.getName());
                } else if (childActor instanceof GdxBaseGroupView) {
                    ((GdxBaseGroupView) childActor).setNextFocusUp(temp.getName());
                }
            }
        }
        return temp;
    }


    public String getNextFocusRight() {
        return nextFocusRight;
    }

    public void setNextFocusRight(String nextFocusRight) {
        this.nextFocusRight = nextFocusRight;
    }

    public String getNextFocusLeft() {
        return nextFocusLeft;
    }

    public void setNextFocusLeft(String nextFocusLeft) {
        this.nextFocusLeft = nextFocusLeft;
    }

    public String getNextFocusUp() {
        return nextFocusUp;
    }

    public void setNextFocusUp(String nextFocusUp) {
        this.nextFocusUp = nextFocusUp;
    }

    public String getNextFocusDown() {
        return nextFocusDown;
    }

    public void setNextFocusDown(String nextFocusDown) {
        this.nextFocusDown = nextFocusDown;
    }

    public int getDescendantFocusability() {
        return descendantFocusability;
    }

    public void setDescendantFocusability(int descendantFocusability) {
        this.descendantFocusability = descendantFocusability;
    }


    private Actor findPeerActor(Actor curActor, int keyCode) {

        if (curActor == null)
            return null;

        Actor childActor = null;

        switch (keyCode) {

            case Input.Keys.DPAD_DOWN:
                childActor = this.nextDownOf(curActor);
                break;
            case Input.Keys.DPAD_UP:
                childActor = this.nextUpOf(curActor);
                break;
            case Input.Keys.DPAD_LEFT:
                childActor = this.nextLeftOf(curActor);
                break;
            case Input.Keys.DPAD_RIGHT:
                childActor = this.nextRightOf(curActor);
                break;
            case Input.Keys.DPAD_CENTER:
            case Input.Keys.ENTER:
                childActor = curActor;
                break;
        }
        return childActor;
    }

    /**
     * 按键分发
     *
     * @param event
     * @param keyCode
     * @return
     */
    private boolean dispatchOnkey(keyEvent event, int keyCode) {

        Actor childActor = null;
        if (curFocusChild == null) {
            childActor = findNearestActor(event, keyCode);
        } else {
            //判断当前焦点view 的分发按键事件
            if (curFocusChild instanceof GdxBaseGroupView
                    && !curFocusChild.equals(this)) {
                int descendantFocusability = ((GdxBaseGroupView) curFocusChild).getDescendantFocusability();
                if (descendantFocusability == GdxBaseGroupView.FOCUS_AFTER_DESCENDANTS) {
                    if (curFocusChild instanceof OnKeyListener) {
                        boolean flag = ((OnKeyListener) curFocusChild).onKey(event, keyCode);
                        if (flag) return true;
                    }
                }
            }

            childActor = findPeerActor(curFocusChild, keyCode);
        }

        if (childActor != null) {
            loseFucusOnActor(curFocusChild);

            if (childActor instanceof GdxBaseGroupView) {
                // ..单独view 的焦点处理
                if (childActor instanceof OnKeyListener) {
                    boolean flag = ((OnKeyListener) childActor).onKey(event, keyCode);
                    if (!flag) {
                        if (((GdxBaseGroupView) childActor).getDescendantFocusability() ==
                                GdxBaseGroupView.FOCUS_AFTER_DESCENDANTS) {
                            handleOnKey(childActor, keyCode);
                            return true;
                        } else {
                            return dispatchOnKeyToPeers(childActor, event, keyCode);
                        }
                    }
                }
            } else {
                handleOnKey(childActor, keyCode);
                if (childActor instanceof OnKeyListener) {
                    ((OnKeyListener) childActor).onKey(event, keyCode);
                }
            }
            return true;
        }

        return false;
    }


    private void loseFucusOnActor(Actor actor) {

        if (actor == null)
            return;

        if (actor instanceof IFocusChangeCallback) {
            ((IFocusChangeCallback) actor).focusChange(false);
        }

        // 判断是否是group
        if (actor instanceof GdxBaseGroupView) {
            Actor curFocus = ((GdxBaseGroupView) actor).getcurFocusChild();
            if (curFocus != null) {
                if (!curFocus.equals(actor))
                    loseFucusOnActor(curFocus);
                ((GdxBaseGroupView) actor).setcurFocusChild(null);
            }
        }

        if (actor.getParent() instanceof GdxBaseGroupView) {
            ((GdxBaseGroupView) actor.getParent()).setcurFocusChild(null);
        }

    }

    /**
     * 把按键事件传递给兄弟view
     *
     * @param event
     * @param keyCode
     */
    private boolean dispatchOnKeyToPeers(Actor selfActor,
                                         keyEvent event,
                                         int keyCode) {

        Actor peerActor = findPeerActor(selfActor, keyCode);
        if (peerActor != null) {
            if (peerActor instanceof GdxBaseGroupView) {

                // ..单独view 的焦点处理
                if (peerActor instanceof OnKeyListener) {
                    boolean flag = ((OnKeyListener) peerActor).onKey(event, keyCode);
                    if (!flag) {
                        if (((GdxBaseGroupView) peerActor).getDescendantFocusability() ==
                                GdxBaseGroupView.FOCUS_AFTER_DESCENDANTS) {
                            System.out.println("1111");
                            handleOnKey(peerActor, keyCode);
                            return true;
                        }
                        return dispatchOnKeyToPeers(peerActor, event, keyCode);
                    }
                }
            } else {
                handleOnKey(peerActor, keyCode);
            }

            return true;
        }
        return false;
    }


    private void handleOnKey(Actor actor, int keyCode) {
        if (actor == null)
            return;
        //滚动
        checkIfScroll(actor, keyCode);
        //callback
        itemSelectChangedCallback(actor);
    }


    public boolean checkIfScroll(Actor actor, int keyCode) {
        if (actor == null)
            return false;

        //滚动处理
        if (actor.getParent() instanceof GdxScrollGroup) {

            GdxAbsScrollGroup.ScrollOrientation scrollOrientation = null;
            switch (keyCode) {
                case Input.Keys.DPAD_RIGHT:
                    scrollOrientation = GdxAbsScrollGroup.ScrollOrientation.right;
                    break;
                case Input.Keys.DPAD_LEFT:
                    scrollOrientation = GdxAbsScrollGroup.ScrollOrientation.left;
                    break;
                case Input.Keys.DPAD_DOWN:
                    scrollOrientation = GdxAbsScrollGroup.ScrollOrientation.down;
                    break;
                case Input.Keys.DPAD_UP:
                    scrollOrientation = GdxAbsScrollGroup.ScrollOrientation.up;
                    break;
            }

            if (scrollOrientation != null) {
                return ((GdxScrollGroup) actor.getParent()).scrollTo(scrollOrientation, actor);
            }
        } else {
            //抛给父类处理滚动
            if (this.getParent() instanceof GdxScrollGroup) {
                return ((GdxScrollGroup) this.getParent()).checkIfScroll(this, keyCode);
            }
        }

        return false;
    }

    /**
     * 设置焦点变化的回调接口
     *
     * @param onFocusChangeListener
     */
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    public boolean isFocusAble() {
        return isFocusAble;
    }

    public void setFocusAble(boolean focusAble) {
        isFocusAble = focusAble;
    }


    /**
     * 焦点变化
     *
     * @param isGainFocus
     */
    @Override
    public void focusChange(boolean isGainFocus) {

        if (!this.isEnable()
                || !this.isFocusAble())
            return;

        this.isFocus = isGainFocus;

        //做一些焦点变化的动画
        // ...

        //listener
        if (this.onFocusChangeListener != null)
            this.onFocusChangeListener.onFocusChange(isGainFocus);
    }


    public void setBackgroundTexture(Texture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //画背景
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture,
                    super.getX(),
                    super.getY(),
                    super.getOriginX(),
                    super.getOriginY(),
                    super.getWidth(),
                    super.getHeight(),
                    super.getScaleX(),
                    super.getScaleY(),
                    super.getRotation(),
                    0,
                    0,
                    backgroundTexture.getWidth(),
                    backgroundTexture.getHeight(),
                    false,
                    false);
        }

        super.draw(batch, parentAlpha);
    }
}
