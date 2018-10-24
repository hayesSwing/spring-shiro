package com.xiangzi.util;

import java.util.ArrayList;
import java.util.List;

public class SplitUtils {

	public static String[] splitEx(String str, String spilter) {
		if (str == null) {
			return null;
		}
		if (spilter == null || spilter.equals("") || str.length() < spilter.length()) {
			String[] t = { str };
			return t;
		}
		List<String> al = new ArrayList<String>();
		char[] cs = str.toCharArray();
		char[] ss = spilter.toCharArray();
		int length = spilter.length();
		int lastIndex = 0;
		for (int i = 0; i <= str.length() - length;) {
			boolean notSuit = false;
			for (int j = 0; j < length; j++) {
				if (cs[i + j] != ss[j]) {
					notSuit = true;
					break;
				}
			}
			if (!notSuit) {
				al.add(str.substring(lastIndex, i));
				i += length;
				lastIndex = i;
			} else {
				i++;
			}
		}
		if (lastIndex <= str.length()) {
			al.add(str.substring(lastIndex, str.length()));
		}
		String[] t = new String[al.size()];
		for (int i = 0; i < al.size(); i++) {
			t[i] = (String) al.get(i);
		}
		return t;
	}

	/**
	 * 在一字符串左边填充若干指定字符，使其长度达到指定长度
	 * 
	 * @param srcString
	 * @param c
	 * @param length
	 * @return
	 */
	public static String leftPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();
		int i, iMax;
		if (tLen >= length) {
			return srcString;
		}
		iMax = length - tLen;
		StringBuffer sb = new StringBuffer();
		for (i = 0; i < iMax; i++) {
			sb.append(c);
		}
		sb.append(srcString);
		return sb.toString();
	}

	/**
	 * 在一字符串右边填充若干指定字符，使其长度达到指定长度
	 * 
	 * @param srcString
	 * @param c
	 * @param length
	 * @return
	 */
	public static String rightPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();
		int i, iMax;
		if (tLen >= length) {
			return srcString;
		}
		iMax = length - tLen;
		StringBuffer sb = new StringBuffer();
		sb.append(srcString);
		for (i = 0; i < iMax; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

}
