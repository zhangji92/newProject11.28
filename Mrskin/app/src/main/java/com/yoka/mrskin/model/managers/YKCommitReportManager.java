package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.text.TextUtils;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTrialProductInfo;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

/**
 * 点评商品&试用报告
 * 
 * @author z l l
 * 
 */
public class YKCommitReportManager extends YKManager
{
    private static final String PATH = "try/report";
    private static final String COMMENT = "add/review";

    private static YKCommitReportManager singleton = null;
    private static Object lock = new Object();

    public static YKCommitReportManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKCommitReportManager();
            }
        }
        return singleton;
    }

    /**
     * 点评商品
     * 
     * @param info
     * @param urls
     * @param callback
     * @return
     */
    public YKHttpTask requestAddComment(YKTrialProductInfo info,
            ArrayList<String> urls, final YKGeneralCallBack callback) {
        String url = getBase() + COMMENT;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (info != null) {
            if (!TextUtils.isEmpty(info.getmTrialId())) {
                parameters.put("product_id", info.getmTrialId());
            }
            if (!TextUtils.isEmpty(info.getmRating())) {
                parameters.put("rating", info.getmRating());
            }
            if (!TextUtils.isEmpty(info.getmTitle())) {
                parameters.put("title", info.getmTitle());
            }
            if (!TextUtils.isEmpty(info.getmDesc())) {
                parameters.put("description", info.getmDesc());
            }
            String userId = YKCurrentUserManager.getInstance().getUser().getId();
            if (!TextUtils.isEmpty(userId)) {
                parameters.put("user_id", userId);
            }
        }
        if (urls != null) {
            parameters.put("images", urls);
        }
        return super.postURL(url, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {
                    // parse data;
                }

                // do callback
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    /**
     * 提交试用报告
     * 
     * @param info
     * @param urls
     * @param callback
     * @return
     */
    public YKHttpTask requestCommitReport(YKTrialProductInfo info,
            ArrayList<String> urls, final YKGeneralCallBack callback) {
        String url = getBase() + PATH;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (info != null) {
            if (!TextUtils.isEmpty(info.getmTrialId())) {
                parameters.put("trial_id", info.getmTrialId());
            }
            if (!TextUtils.isEmpty(info.getmRating())) {
                parameters.put("rating", info.getmRating());
            }
            if (!TextUtils.isEmpty(info.getmExterior())) {
                parameters.put("exterior_rating", info.getmExterior());
            }
            if (!TextUtils.isEmpty(info.getmEffect())) {
                parameters.put("effect_rating", info.getmEffect());
            }
            if (!TextUtils.isEmpty(info.getmFeel())) {
                parameters.put("feel_rating", info.getmFeel());
            }
            if (!TextUtils.isEmpty(info.getmTitle())) {
                parameters.put("title", info.getmTitle());
            }
            if (!TextUtils.isEmpty(info.getmDesc())) {
                parameters.put("description", info.getmDesc());
            }
            String userId = YKCurrentUserManager.getInstance().getUser().getId();
            if (!TextUtils.isEmpty(userId)) {
                parameters.put("user_id", userId);
            }
        }
        if (urls != null) {
            parameters.put("images", urls);
        }
        return super.postURL(url, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {
                    // parse data;
                }

                // do callback
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    public interface YKGeneralCallBack
    {
        public void callback(YKResult result);
    }
}
