package com.xiangzi.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class MathUtil {

	// 默认除法运算精度
	private static final int DEFAULT_DIV_SCALE = 10;

	/**
	 * 大于
	 * 
	 * @param x
	 * @param y
	 * @return x>y,结果为true
	 */
	public static boolean gt(String x, String y) {
		return new BigDecimal(x).compareTo(new BigDecimal(y)) == 1;
	}

	/**
	 * 大于等于
	 * 
	 * @param x
	 * @param y
	 * @return x>=y,结果为true
	 */
	public static boolean gte(String x, String y) {
		return new BigDecimal(x).compareTo(new BigDecimal(y)) >= 0;
	}

	/**
	 * 小于
	 * 
	 * @param x
	 * @param y
	 * @return x<y,结果为true
	 */
	public static boolean lt(String x, String y) {
		return new BigDecimal(x).compareTo(new BigDecimal(y)) == -1;
	}

	/**
	 * 小于等于
	 * 
	 * @param x
	 * @param y
	 * @return x<=y,结果为true
	 */
	public static boolean lte(String x, String y) {
		return new BigDecimal(x).compareTo(new BigDecimal(y)) <= 0;
	}

	/**
	 * 相等
	 * 
	 * @param x
	 * @param y
	 * @return x=y,结果为true
	 */
	public static boolean eq(String x, String y) {
		return new BigDecimal(x).compareTo(new BigDecimal(y)) == 0;
	}

	/**
	 * 相等
	 * 
	 * @param x
	 * @param y
	 * @return x=y,结果为true
	 */
	public static boolean eq(double x, double y) {
		return eq(Double.toString(x), Double.toString(y));
	}

	/**
	 * 加法运算
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static BigDecimal add(BigDecimal x, BigDecimal y) {
		return x.add(y);
	}

	/**
	 * 减法运算
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal x, BigDecimal y) {
		return x.subtract(y);
	}

	/**
	 * 乘法运算
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal x, BigDecimal y) {
		return x.multiply(y);
	}

	public static BigDecimal multiply(BigDecimal x, BigDecimal y, int sacle, int roundingMode) {
		return x.multiply(y).setScale(sacle, roundingMode);
	}

	public static double multiply(double x, double y) {
		return multiply(new BigDecimal(x), new BigDecimal(y)).doubleValue();
	}

	public static double multiply(double x, double y, int sacle, int roundingMode) {
		return multiply(new BigDecimal(x), new BigDecimal(y)).setScale(sacle, roundingMode).doubleValue();
	}

	/**
	 * 除法运算
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static BigDecimal divide(BigDecimal x, BigDecimal y) {
		return divide(x, y, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_DOWN);
	}

	public static BigDecimal divide(BigDecimal x, BigDecimal y, int sacle, int roundingMode) {
		return x.divide(y, sacle, roundingMode);
	}

	/**
	 * 百分比格式化
	 * 
	 * @param x
	 * @return
	 */
	public static String percent(BigDecimal x) {
		NumberFormat percent = NumberFormat.getPercentInstance(); // 建立百分比格式化引用
		percent.setMaximumFractionDigits(3); // 百分比小数点最多3位
		return percent.format(x);
	}

	/**
	 * 货币格式化
	 * 
	 * @param x
	 * @return
	 */
	public static String currency(BigDecimal x) {
		NumberFormat currency = NumberFormat.getCurrencyInstance(); // 建立货币格式化引用
		return currency.format(x);
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		return round(new BigDecimal(Double.toString(v)), scale);
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(BigDecimal v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		return v.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 大于
	 * 
	 * @param x
	 * @param y
	 * @return x>y,结果为true
	 */
	public static boolean gt(BigDecimal x, BigDecimal y) {
		return x.compareTo(y) == 1;
	}

	/**
	 * 大于等于
	 * 
	 * @param x
	 * @param y
	 * @return x>=y,结果为true
	 */
	public static boolean gte(BigDecimal x, BigDecimal y) {
		return x.compareTo(y) >= 0;
	}

	/**
	 * 小于
	 * 
	 * @param x
	 * @param y
	 * @return x<y,结果为true
	 */
	public static boolean lt(BigDecimal x, BigDecimal y) {
		return x.compareTo(y) == -1;
	}

	/**
	 * 小于等于
	 * 
	 * @param x
	 * @param y
	 * @return x<=y,结果为true
	 */
	public static boolean lte(BigDecimal x, BigDecimal y) {
		return x.compareTo(y) <= 0;
	}

}
