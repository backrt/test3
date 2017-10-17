package com.mygdx.game.view.utils;

public class Tool {

	public static boolean checkLine(float x1, float l1, float x2, float l2) {
		return !(x1 > x2 + l2 || x1 + l1 < x2);
	}

	public static float lineCloss(float x1, float l1, float x2, float l2) {
		return Math.min(x1 + l1, x2 + l2) - Math.max(x1, x2);
	}
}
