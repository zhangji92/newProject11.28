package com.yoka.mrskin.util;

import java.net.URLDecoder;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.ProductItemActivity;
import com.yoka.mrskin.activity.YKWebViewActivity;

/**
 * 
 * 用于拦截各种Webview链接
 * 
 */
public class WebViewObserver  {

    private static final String LINKMARK_FUJUN = "fujun";

    private static final String LINKMARK_COSMETICENTRY = "/cosmeticentry"; // 妆品库品牌

    private static final String LINKMARK_WEBPAGE = "/webpage"; // 打开新的webview页面

    private static final String TYPE = "type";

    private static final String ID = "id";

    private static final String TITLE = "title";

    private static final String URL = "url";

    private Activity mActivity;

    private HashMap<String, ActionHandler> mTable = null;

    public WebViewObserver(Activity activity) {
        this.mActivity = activity;
        init();
    }

    private void init() {
        mTable = new HashMap<String, ActionHandler>();
        registerHandler(LINKMARK_COSMETICENTRY, new CosmeticentryActionHandler());
        registerHandler(LINKMARK_WEBPAGE, new HttpActionHandler());
    }

    private void registerHandler(String action, ActionHandler handler) {
        if (TextUtils.isEmpty(action))
            return;
        if (handler == null)
            return;
        mTable.put(action, handler);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (TextUtils.isEmpty(url))
            return true;
//        url = URLDecoder.decode(url);
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        if (LINKMARK_FUJUN.equals(scheme)) {
            return handleURL(uri);
        } else {
            return false;
        }

    }

    public boolean handleURL(Uri uri) {
        String path = uri.getPath();
        ActionHandler handler = mTable.get(path);
        boolean result = false;
        if (handler != null) {
            result = handler.execute(uri);
        }
        return result;
    }

    private abstract class ActionHandler {
        public abstract boolean execute(Uri uri);
    }

    private class CosmeticentryActionHandler extends ActionHandler {
        public boolean execute(Uri uri) {
            Intent goBrand = new Intent(mActivity, ProductItemActivity.class);
            goBrand.putExtra("id", uri.getQueryParameter(ID));
            goBrand.putExtra("type", uri.getQueryParameter(TYPE));
            goBrand.putExtra("title", uri.getQueryParameter(TITLE));
            mActivity.startActivity(goBrand);
            return true;
        }
    }

    private class HttpActionHandler extends ActionHandler {
        public boolean execute(Uri uri) {
            Intent course = new Intent(mActivity, YKWebViewActivity.class);
            course.putExtra("productdetalis", URLDecoder.decode(uri.getQueryParameter(URL)));
            course.putExtra("title", mActivity.getString(R.string.course_class));
            mActivity.startActivity(course);
            return true;
        }
    }

}
