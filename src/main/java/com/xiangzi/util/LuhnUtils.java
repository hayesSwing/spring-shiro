package com.xiangzi.util;

public class LuhnUtils {

	/**
	 * 获取校验位
	 */
	public static int getCheckNumber(String cardNumber) {
		int totalNumber = 0;
		for (int i = cardNumber.length() - 1; i >= 0; i -= 2) {
			int tmpNumber = calculate(Integer.parseInt(String.valueOf(cardNumber.charAt(i))) * 2);
			if (i == 0) {
				totalNumber += tmpNumber;
			} else {
				totalNumber += tmpNumber + Integer.parseInt(String.valueOf(cardNumber.charAt(i - 1)));
			}

		}
		if (totalNumber >= 0 && totalNumber < 9) {
			return (10 - totalNumber);
		} else {
			String str = String.valueOf(totalNumber);
			if (Integer.parseInt(String.valueOf(str.charAt(str.length() - 1))) == 0) {
				return 0;
			} else {
				return (10 - Integer.parseInt(String.valueOf(str.charAt(str.length() - 1))));
			}
		}

	}

	/**
	 * 计算数字各位和
	 */
	protected static int calculate(int number) {
		String str = String.valueOf(number);
		int total = 0;
		for (int i = 0; i < str.length(); i++) {
			total += Integer.valueOf(Integer.parseInt(String.valueOf(str.charAt(i))));
		}
		return total;
	}

	public static boolean luhnTest(String number) {
		int s1 = 0, s2 = 0;
		String reverse = new StringBuffer(number).reverse().toString();
		for (int i = 0; i < reverse.length(); i++) {
			int digit = Character.digit(reverse.charAt(i), 10);
			if (i % 2 == 0) {
				s1 += digit;
			} else {
				s2 += 2 * digit;
				if (digit >= 5) {
					s2 -= 9;
				}
			}
		}
		return (s1 + s2) % 10 == 0;
	}

}
