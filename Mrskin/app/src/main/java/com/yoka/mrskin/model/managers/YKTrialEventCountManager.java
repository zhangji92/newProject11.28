package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;

import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.AppUtil;

public class YKTrialEventCountManager extends YKManager
{
    private static final String PATH = getBase() + "try/message";
    private static YKTrialEventCountManager instance = null;
    private static Object lock = new Object();
    private int mCount;

    public static YKTrialEventCountManager getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new YKTrialEventCountManager();
            }
        }
        return instance;
    }

    public int getEventCount(Context context) {
        return AppUtil.getSharedPreferences(context).getInt(
                "trial_event_count", 0);
    }

    public YKHttpTask requestEventCount(String userId,
            final TrialEventCountCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("user_id", userId);
        return super.postURL(PATH, parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        if (result.isSucceeded()) {
                            mCount = object.optInt("trial_event_count");
                            saveEventCount(AppContext.getInstance(), mCount);
                        }
                        if (callback != null) {
                            callback.callback(result, mCount);
                        }
                    }
                });
    }

    private void saveEventCount(Context context, int count) {
        if (context != null) {
            AppUtil.getSharedPreferencesEditor(context)
                    .putInt("trial_event_count", count).commit();
        }
    }

    public interface TrialEventCountCallback
    {
        public void callback(YKResult result, int count);
    }
}
