package com.yoka.mrskin.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers.GetTaskByIdCallBack;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;
import com.yoka.mrskin.util.YKUrlUtil;

public abstract class AddTaskWebViewActivity extends BaseActivity implements
OnClickListener

{
    private final static int REQUEST_CODE_LOGIN_WEBVIEWPM = 110;
    protected ProgressWebView mWebView;

    private String requstTaskId;
    protected YKURIHandler urlHandler = new YKURIHandler() {

        @Override
        public boolean handleURI(String url) {
            int id = YKUrlUtil.tryToGetTaskIdFormUrl(
                    AddTaskWebViewActivity.this, url);
            if (id != -1) {
                requstTaskId = Integer.toString(id);
                if (!YKCurrentUserManager.getInstance().isLogin()) {
                    Intent intent = new Intent(AddTaskWebViewActivity.this,
                            LoginActivity.class);
                    AddTaskWebViewActivity.this.startActivityForResult(intent,
                            REQUEST_CODE_LOGIN_WEBVIEWPM);
                } else {
                    getTaskAndShowDetail();
                }
                return true;
            }
            return false;
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN_WEBVIEWPM
                && resultCode == RESULT_OK) {
            getTaskAndShowDetail();
        }
    };

    private void getTaskAndShowDetail() {
        YKSyncTaskManagers.getInstance().getNewTaskFromServer(requstTaskId,
                new GetTaskByIdCallBack() {

            @Override
            public void callback(YKTask task, YKResult result) {
                if (result.isSucceeded() && task != null) {
                    YKTask nativeTask = YKTaskManager.getInstnace().getTaskById(task.getID());
                    if(nativeTask!=null){
                        task = nativeTask;
                    }
                    openTaskDetailPage(task);
                } else {
                    Toast.makeText(AppContext.getInstance(),
                            R.string.task_gain_plan, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openTaskDetailPage(YKTask ykTask) {
        Intent intent = new Intent(this, TaskBriefActivity.class);
        if (ykTask != null) {
            intent.putExtra("task", ykTask);
        }
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }else{
                finish();
            }
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }
}
