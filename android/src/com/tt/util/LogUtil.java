package com.tt.util;

import android.util.Log;

public class LogUtil {

	private static final String TAG = "LogUtil";

	public static void show(String s){
		System.out.println(s);
	}

	public static void d(String text){
		Log.d(TAG,text);
	}

	public static  void d(String tag ,String text){
		Log.d(tag,text);
	}
	
}
