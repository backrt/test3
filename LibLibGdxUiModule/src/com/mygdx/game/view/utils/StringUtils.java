package com.mygdx.game.view.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {

	private static StringUtils instance = null;

	public static StringUtils getInstance() {

		if (instance == null) {
			synchronized (StringUtils.class) {
				if (instance == null)
					instance = new StringUtils();
			}
		}

		return instance;

	}

	/**
	 * 去掉重复的字符
	 * 
	 * @param s
	 * @return
	 */
	public String removeRepeatedChar(String s) {
		if (s == null)
			return s;
		s = s.replaceAll("(?s)(.)(?=.*\\1)", "");
		return s;
	}

	/**
	 * 计算字符串的 md5
	 * 
	 * @param s
	 * @return
	 */
	public String md5(String s) {
		if (s == null)
			return s;

		byte[] buf = s.getBytes();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(buf);
			byte[] temp = md5.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : temp) {
				sb.append(Integer.toHexString(b & 0xff));
			}
			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
		} catch (NullPointerException e) {
		}

		return s;
	}

}
