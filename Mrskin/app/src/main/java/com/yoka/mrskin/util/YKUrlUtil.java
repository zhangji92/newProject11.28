package com.yoka.mrskin.util;

import android.content.Context;
import android.text.TextUtils;

public class YKUrlUtil
{

    private static final String MRSKIN_PROTOCOL_ADDTASK = "fujun://fujunaction/addtask/";

    public static int tryToGetTaskIdFormUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return -1;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_ADDTASK)) {
            String content = url.substring(MRSKIN_PROTOCOL_ADDTASK.length());
            if (TextUtils.isEmpty(content)) {
                return -1;
            }
            try {
                return Integer.parseInt(content);
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }
}
