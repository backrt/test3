package com.mygdx.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GdxAbsScrollGroup extends GdxBaseGroupView {

    /**
     * 滚动方向
     *
     */
	public enum ScrollOrientation {
		left, right, up, down
	}

    /**
     * 滚动速度 类型
     *
     */
	public enum ScrollSpeedType {
		fast, medium, slow
	}

    protected boolean isRollBack;

	protected float scrollDistance;
	protected float scrollSpeed;
	protected ScrollOrientation scrollOrientation; // 滚动方向
	protected ScrollSpeedType speedType; // 滚动速递

	/**
	 * 滚动到某一个位置 
	 * 
	 * @param orientation
	 * @param toActor
	 */
	public abstract boolean scrollTo(ScrollOrientation orientation, Actor toActor);

	/**
	 * 滚动一定距离
	 * @param orientation
	 * @param distance
	 */
	public abstract void scrollBy(ScrollOrientation orientation,float distance);

	/**
	 * 从某一位置 回滚
	 * 
	 * @param orientation
	 * @param fromActor
	 */
	public abstract void rollBack(ScrollOrientation orientation, Actor fromActor);

	public GdxAbsScrollGroup() {
		super();
	}

	public float getScrollSpeed() {
		return scrollSpeed;
	}

	public void setScrollSpeed(float scrollSpeed) {
		this.scrollSpeed = scrollSpeed;
	}

	public ScrollSpeedType getSpeedType() {
		return speedType;
	}

	public abstract void setSpeedType(ScrollSpeedType speedType);

}
