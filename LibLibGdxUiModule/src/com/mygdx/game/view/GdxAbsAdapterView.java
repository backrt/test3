package com.mygdx.game.view;

import com.mygdx.game.view.data.BaseAdapter;

public abstract class GdxAbsAdapterView<T extends BaseAdapter> extends
		GdxScrollGroup {

	private T mAdapter;

	private int horizontalSpace ;
	private int verticalSpace;

	/**
	 * construct
	 */
	public GdxAbsAdapterView() {

	}

	public void setAdapter(T adapter) {
		if (mAdapter != null)
			mAdapter.clear();
		this.mAdapter = (T) adapter;
		this.initView(mAdapter);
	}

	public abstract void initView(BaseAdapter adapter);

	public T getAdapter() {
		return mAdapter;
	}

	public int getHorizontalSpace() {
		return horizontalSpace;
	}

	public void setHorizontalSpace(int horizontalSpace) {
		this.horizontalSpace = horizontalSpace;
	}

	public int getVerticalSpace() {
		return verticalSpace;
	}

	public void setVerticalSpace(int verticalSpace) {
		this.verticalSpace = verticalSpace;
	}
}
