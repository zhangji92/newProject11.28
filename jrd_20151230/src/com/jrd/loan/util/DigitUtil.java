package com.jrd.loan.util;

import java.text.DecimalFormat;

public final class DigitUtil {
    public static String getMoney(String money) {
        double value = Double.parseDouble(money);

        if (value >= 10000) {
            value = value / 10000;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            return new StringBuffer(decimalFormat.format(value)).append("万").toString();
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#");

            return new StringBuffer(decimalFormat.format(value)).append("元").toString();
        }
    }

    public static String getMoneys(String money) {
        double value = Double.parseDouble(money);

        if (value >= 10000) {
            value = value / 10000;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            return new StringBuffer(decimalFormat.format(value)).toString();
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#");

            return new StringBuffer(decimalFormat.format(value)).toString();
        }
    }

    public static String formatMoney(String money) {
        return new DecimalFormat("#.##").format(Double.parseDouble(money));
    }

    public static String getMonth(String month) {
        double value = Double.parseDouble(month);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return decimalFormat.format(value);
    }
}