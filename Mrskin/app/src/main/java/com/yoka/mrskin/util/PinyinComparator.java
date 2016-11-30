package com.yoka.mrskin.util;

import java.util.Comparator;

import com.yoka.mrskin.model.data.YKBrand;

/**
 * 
 *
 */
public class PinyinComparator implements Comparator<YKBrand> {

    public int compare(YKBrand o1, YKBrand o2) {
        if (o1.getmSortLetters().equals("@")
                || o2.getmSortLetters().equals("#")) {
            return -1;
        } else if (o1.getmSortLetters().equals("#")
                || o2.getmSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getmSortLetters().compareTo(o2.getmSortLetters());
        }
    }

}
