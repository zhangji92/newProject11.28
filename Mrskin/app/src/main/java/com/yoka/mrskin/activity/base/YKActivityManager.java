package com.yoka.mrskin.activity.base;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;

import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.http.IHttpTaskObserver;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKCurrentUserManager.ILoginCallback;

public class YKActivityManager implements IHttpTaskObserver
{
    private static final String TAG = YKActivityManager.class.getName();
    private static YKActivityManager instance = null;
    private static Object lock = new Object();
    private ArrayList<Activity> mActivityList = new ArrayList<Activity>();

    private Activity mCurrentacActivity;
    private String mNotificationURL;

    private HashMap<Activity, ArrayList<YKHttpTask>> mTaskTable;

    private YKActivityManager()
    {
        super();
        mNotificationURL = null;
        mTaskTable = new HashMap<Activity, ArrayList<YKHttpTask>>();
    }

    public void addActivity(Activity activity) {
        if (activity == null)
            return;
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activity == null)
            return;
        mActivityList.remove(activity);
    }

    public void finishAllActivity() {
        if (mActivityList.size() == 0) {
            return;
        }
        for (int i = mActivityList.size() - 1; i > -1; i--) {
            try {
                mActivityList.get(i).finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mActivityList.clear();
    }

    public static YKActivityManager getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new YKActivityManager();
            }
        }
        return instance;
    }

    public void registerRootActivity(Activity activity) {
        if (activity == null)
            return;

        mCurrentacActivity = activity;
    }

    public void showMain(Activity activity) {
        if (activity == null)
            return;
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    public void showURL(String url) {
        // if (url == null)
        // return;
        // Intent intent =
        // YKURIParser.getInstance().handleURL(mCurrentacActivity,
        // url);
        // System.out.println("YKActivityManager showURL = " +
        // mCurrentacActivity);
        // if (intent != null) {
        // mCurrentacActivity.startActivity(intent);
        // }
    }

    public void showAbout() {
        // Intent intent = new Intent(mCurrentacActivity,
        // AboutUsActivity.class);
        //
        // mCurrentacActivity.startActivity(intent);
    }

    public void setNotificationInfo(String info) {
        if (info == null)
            return;
        try {
            JSONObject object = new JSONObject(info);
            mNotificationURL = object.optString("target");
        } catch (Exception e) {
        }
    }

    public String getNotificationURL() {
        return mNotificationURL;
    }

    public void handleNotificationURL() {
        if (mNotificationURL != null) {
            showURL(mNotificationURL);
            mNotificationURL = null;
        }
    }

    // @Override
    // // public boolean handleURI(String URI) {
    // Intent intent = YKURIParser.getInstance().handleURL(mCurrentacActivity,
    // URI);
    // if (intent != null) {
    // mCurrentacActivity.startActivity(intent);
    // return true;
    // }
    // return false;
    // }

    // public boolean hasNotificationURL() {
    // return mNotificationURL != null;
    // }

    public void showLogin(Activity activity, ILoginCallback callback) {
        if (activity != null) {
            Intent i = new Intent(activity, LoginActivity.class);
            activity.startActivity(i);
            YKCurrentUserManager.getInstance().setCallback(callback);
        }
    }

    @Override
    public void taskCreated(YKHttpTask task) {
        if (task == null)
            return;
        if (mCurrentacActivity != null) {
            ArrayList<YKHttpTask> taskList = mTaskTable.get(mCurrentacActivity);
            if (taskList == null) {
                taskList = new ArrayList<YKHttpTask>();
                mTaskTable.put(mCurrentacActivity, taskList);
            }
            taskList.add(task);
        }
    }

    @Override
    public void taskFinished(YKHttpTask task) {
        if (task == null)
            return;
        if (mCurrentacActivity != null) {
            ArrayList<YKHttpTask> taskList = mTaskTable.get(mCurrentacActivity);
            if (taskList != null) {
                taskList.remove(task);
            }
        }

    }

    public void activityHidden(Activity activity) {
        if (activity == null)
            return;
        ArrayList<YKHttpTask> tasklist = mTaskTable.get(activity);
        if (tasklist != null) {
            for (YKHttpTask task : tasklist) {
                task.cancel();
            }
            tasklist.clear();
        }
    }
}
