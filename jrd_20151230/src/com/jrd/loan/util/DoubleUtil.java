package com.jrd.loan.util;

import java.text.NumberFormat;

public final class DoubleUtil {
    public static String getMoney(String money) {
        Double d = new Double(money);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        return nf.format(d);
    }
}