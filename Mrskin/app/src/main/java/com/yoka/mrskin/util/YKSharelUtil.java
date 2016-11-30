package com.yoka.mrskin.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

public class YKSharelUtil
{
    private static final String MRSKIN_PROTOCOL_SHARE = "fujun://fujunaction.com/share?";
    private static final String MRSKIN_PROTOCOL_WEBPAGE = "fujun://fujunaction.com/webpage?";
    private static final String MRSKIN_PROTOCOL_TASK = "fujun://fujunaction.com/task";
    private static final String MRSKIN_PROTOCOL_SEARCH = "fujun://fujunaction.com/search";
    private static final String MRSKIN_PROTOCOL_TRYLOGIN = "fujun://fujunaction.com/trylogin";
    private static final String MRSKIN_PROTOCOL_CURSE = "http://hzp.yoka.com/fujun/web";
    private static final String MRSKIN_PROTOCOL_REPORT = "fujun://fujunaction.com/productreport?";
    private static final String MRSKIN_PROTOCOL_COMMENT = "fujun://fujunaction.com/experienceComment?";
    private static final String MRSKIN_PROTOCOL_COMMENTLIST = "fujun://fujunaction.com/commentList?";
    private static final String MRSKIN_PROTOCOL_EXPERIENCE = "fujun://fujunaction.com/experienceweb?";
    private static final String MRSKIN_PROTOCOL_PUSHLOCAL = "fujun://fujunaction.com/pushtolocal?";
    private static final String MRSKIN_PROTOCOL_PRODUCTWEB = "fujun://fujunaction.com/topicweb?"; 
    private static final String MRSKIN_PROTOCOL_WEBAD = "fujun://fujunaction.com/webadvert?";
    private static final String MRSKIN_COMMENT_IMAGE = "fujun://fujunaction.com/commentimage?";
    private static final String MRSKIN_COMMENT_TRYWEB = "fujun://fujunaction.com/tryweb?";
    private static final String MRSKIN_PRODUCT_WEB = "fujun://fujunaction.com/productweb?";
    
    
    

    private static Object lock = new Object();
    private static YKSharelUtil singleton = null;

    public static YKSharelUtil getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKSharelUtil();
            }
        }
        return singleton;
    }

    public static String tryToGetWebPagemUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_WEBPAGE)) {
            String content = url.substring(MRSKIN_PROTOCOL_WEBPAGE.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String tryToGetShareFormUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_SHARE)) {
            String content = url.substring(MRSKIN_PROTOCOL_SHARE.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String tryToGetTaskmUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_TASK)) {
            String content = url.substring(MRSKIN_PROTOCOL_TASK.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String tryToSearchmUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_SEARCH)) {
            String content = url.substring(MRSKIN_PROTOCOL_SEARCH.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String tryToCommentmUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_COMMENT)) {
            String content = url.substring(MRSKIN_PROTOCOL_COMMENT.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String tryToCommenListtmUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_COMMENTLIST)) {
            String content = url.substring(MRSKIN_PROTOCOL_COMMENTLIST.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String tryTotryLoginmUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_TRYLOGIN)) {
            String content = url.substring(MRSKIN_PROTOCOL_TRYLOGIN.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }
    public static String webAdvertmUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_WEBAD)) {
            String content = url.substring(MRSKIN_PROTOCOL_WEBAD.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String tryToCursemUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_CURSE)) {
            String content = url.substring(MRSKIN_PROTOCOL_CURSE.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String tryToReportmUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_REPORT)) {
            String content = url.substring(MRSKIN_PROTOCOL_REPORT.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String experiencemUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_EXPERIENCE)) {
            String content = url.substring(MRSKIN_PROTOCOL_EXPERIENCE.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static String pushLocalUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_PUSHLOCAL)) {
            String content = url.substring(MRSKIN_PROTOCOL_PUSHLOCAL.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }else{
                //            	String[] strSplit = content.split("=");
                //            	String type = strSplit[1];
                Uri uri = Uri.parse(url);
                String type = uri.getQueryParameter("type");
                return type;
            }
        }
        return null;
    }

    public static String productwebUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PROTOCOL_PRODUCTWEB)) {
            String content = url.substring(MRSKIN_PROTOCOL_PRODUCTWEB.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }
    
    public static String commentImageUrl(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_COMMENT_IMAGE)) {
            String content = url.substring(MRSKIN_COMMENT_IMAGE.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }
    
    
    public static String tryWeb(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_COMMENT_TRYWEB)) {
            String content = url.substring(MRSKIN_COMMENT_TRYWEB.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }
    
    public static String ProWeb(final Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith(MRSKIN_PRODUCT_WEB)) {
            String content = url.substring(MRSKIN_PRODUCT_WEB.length());
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            try {
                return content;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

}
