package com.yoka.mrskin.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.yoka.share.manager.ShareQQManager;
import com.yoka.share.manager.ShareQzoneManager;
import com.yoka.share.manager.ShareSinaManager;
import com.yoka.share.manager.ShareWxManager;
import com.yoka.swipebacklayout.SwipeBackActivity;
import com.yoka.swipebacklayout.SwipeBackLayout;

public class BaseActivity extends SwipeBackActivity implements OnTouchListener
{
    private static final String TAG = "BaseActivity";
    private GestureDetector mGesture;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        YKActivityManager.getInstance().registerRootActivity(this);
        YKActivityManager.getInstance().addActivity(this);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        initShare();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 友盟统计
        // MobclickAgent.onPageStart(this.getClass().getSimpleName()); // 统计页面
        // MobclickAgent.onResume(this); // 统计时长
        YKActivityManager.getInstance().registerRootActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 友盟统计
        // MobclickAgent.onPageEnd(this.getClass().getSimpleName()); // 保证
        // // onPageEnd
        // // 在onPause
        // // 之前调用,因为 onPause 中会保存信息
        // MobclickAgent.onPause(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        YKActivityManager.getInstance().activityHidden(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }

    class YKGestureListener extends SimpleOnGestureListener
    {
        private Activity mActivity;

        public YKGestureListener(Activity activity)
        {
            mActivity = activity;
        }

        @Override
        public boolean onDown(MotionEvent arg0) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent arg0, MotionEvent arg1,
                float velocityX, float velocityY) {
            
            if (velocityX > 3000 && Math.abs(velocityX) > Math.abs(velocityY)
                    && Math.abs(velocityY) < 1000) {
                if (mActivity != null) {
                    mActivity.finish();
                }
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
                float arg3) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onShowPress(MotionEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent arg0) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mGesture != null) {
            mGesture.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void handleBackGesture(View view, Activity activity) {
        mGesture = new GestureDetector(this, new YKGestureListener(activity));
        // view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }
    
    private void initShare(){
	ShareSinaManager.getInstance().init(this);
	ShareQQManager.getInstance().init(this);
	ShareWxManager.getInstance().init(this);
	ShareQzoneManager.getInstance().init(this);
    }
}
