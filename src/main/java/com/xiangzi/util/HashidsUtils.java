package com.xiangzi.util;

public class HashidsUtils {

	private static final String salt = "xiangzi.com";

	public static String encode(long... numbers) {
		return encodeEx(numbers);
	}

	public static long[] decode(String hash) {
		return decodeEx(hash);
	}

	public static String encode(final String salt, long... numbers) {
		Hashids hashids = new Hashids(salt);
		return hashids.encode(numbers);
	}

	public static long[] decode(final String salt, String hash) {
		Hashids hashids = new Hashids(salt);
		return hashids.decode(hash);
	}

	public static String encodeEx(long... numbers) {
		if (numbers.length == 0) {
			return "";
		}

		StringBuilder result = new StringBuilder();

		int inx = 0;
		for (long number : numbers) {
			if (number < 0) {
				return "";
			}
			String hexString = Long.toHexString(number);
			String hexStringLength = String.format("%02x", hexString.length());
			result.append(hexStringLength + hexString);
			inx++;
		}
		Hashids hashids = new Hashids(salt);
		return hashids.encodeHex(String.format("%02x", inx) + result.toString());
	}

	public static long[] decodeEx(String hash) {
		int offset = 2;
		if (hash.isEmpty() || hash.length() < offset) {
			return new long[0];
		}
		Hashids hashids = new Hashids(salt);
		String decodeString = hashids.decodeHex(hash);
		// 取两个字符长度
		int counts = Integer.parseInt(decodeString.substring(0, offset), 16);
		long[] result = new long[counts];

		for (int i = 0; i < counts; i++) {
			int len = Integer.parseInt(decodeString.substring(offset, offset + 2), 16);
			offset += 2;
			result[i] = Long.parseLong(decodeString.substring(offset, offset + len), 16);
			offset += len;
		}

		return result;
	}

	public static String encodeOpen(final String salt, long... numbers) {
		return encodeEx(salt, numbers);
	}

	public static long[] decodeOpen(final String salt, String hash) {
		return decodeEx(salt, hash);
	}

	private static String encodeEx(final String salt, long... numbers) {
		if (numbers.length == 0) {
			return "";
		}

		StringBuilder result = new StringBuilder();

		int inx = 0;
		for (long number : numbers) {
			if (number < 0) {
				return "";
			}
			String hexString = Long.toHexString(number);
			String hexStringLength = String.format("%02x", hexString.length());
			result.append(hexStringLength + hexString);
			inx++;
		}
		Hashids hashids = new Hashids(salt);
		return hashids.encodeHex(String.format("%02x", inx) + result.toString());
	}

	private static long[] decodeEx(final String salt, String hash) {
		int offset = 2;
		if (hash.isEmpty() || hash.length() < offset) {
			return new long[0];
		}
		Hashids hashids = new Hashids(salt);
		String decodeString = hashids.decodeHex(hash);
		// 取两个字符长度
		int counts = Integer.parseInt(decodeString.substring(0, offset), 16);
		long[] result = new long[counts];

		for (int i = 0; i < counts; i++) {
			int len = Integer.parseInt(decodeString.substring(offset, offset + 2), 16);
			offset += 2;
			result[i] = Long.parseLong(decodeString.substring(offset, offset + len), 16);
			offset += len;
		}

		return result;
	}

}
