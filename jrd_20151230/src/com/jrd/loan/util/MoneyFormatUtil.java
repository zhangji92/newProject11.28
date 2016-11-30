package com.jrd.loan.util;

import java.text.DecimalFormat;

public final class MoneyFormatUtil {
	private final static DecimalFormat format = new DecimalFormat("#,##0.00");

	public static String getMoney(String money) {
		return format.format(Double.parseDouble(money));
	}

	private final static DecimalFormat formatTwo = new DecimalFormat("#,##0");

	public static String getMoneyTwo(String money) {
		return formatTwo.format(Double.parseDouble(money));
	}
}