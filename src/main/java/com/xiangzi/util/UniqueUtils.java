package com.xiangzi.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UniqueUtils {
	public static final UniqueUtils DEFAULT = new UniqueUtils();

	private static int seq = 0;

	private static final int MAX = 999;

	public Serializable generate() {

		StringBuffer buffer = new StringBuffer();

		DateFormat df = new SimpleDateFormat("MMdd-HH:mm:ss");
		String dateString = df.format(new Date());
		String[] template = dateString.split("-");
		buffer.append(template[0]);
		String[] second = template[1].split(":");
		Integer prefix = Integer.parseInt(second[0]) * 60 * 60 + Integer.parseInt(second[1]) * 60 + Integer.parseInt(second[2]);

		buffer.append(SplitUtils.leftPad(prefix.toString(), '0', 5));

		buffer.append(SplitUtils.leftPad(String.valueOf(seq), '0', 3));

		if (seq == MAX) {
			seq = 0;
		} else {
			seq++;
		}

		return buffer;
	}

	public static final synchronized String generator() {
		String result = String.valueOf(UniqueUtils.DEFAULT.generate());
		result = SplitUtils.rightPad(result, '0', 12);
		return result;
	}

	// public static void main(final String[] args) {
	//
	// for(int i=0;i<1000;i++){
	// String unique=UniqueUtils.generator();
	// System.out.println(unique);
	// if(unique.length()<12){
	// System.err.println(unique);
	// break;
	// }
	// }
	//
	// }

}
