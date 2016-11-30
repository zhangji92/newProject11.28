package com.yoka.mrskin.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aijifu.skintest.api.FaceDetectManager;
import com.aijifu.skintest.api.SkinTestUtil;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.util.AppUtil;

/**
 * 肌肤测试页（拍照）
 * 
 * @author z l l
 * 
 */
public class SkinTestActivity extends BaseActivity
{

    // private final String TAG = "SkinTestActivity";
    private Activity mActivity;

    // view
    private SurfaceView mSurfaceView;
    private View mCancelLayout, mCheckCourseLayout;

    // 相机
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mParams;
    private Camera.Size mPreviewSize;
    private Camera.Size mPictureSize;
    private int mPreviewWidth;
    private int mPreviewHeight;

    // 光线传感器
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float mBrightness = 0f;
    private float DEFAULT_BRIGHTNESS = 500f;

    // 人脸识别
    private FaceDetectManager faceDetectManager;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skin_test_activity);
        mActivity = this;
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_OPEN + "SkinTestActivity");
        // findViewById
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mCancelLayout = findViewById(R.id.skin_test_cancel_layout);
        mCheckCourseLayout = findViewById(R.id.skin_test_check_course_layout);

        mCancelLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCheckCourseLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(AppUtil.isNetworkAvailable(mActivity)){
                    startActivity(new Intent(SkinTestActivity.this,
                            TestCourseActivity.class));
                    //TrackManager.getInstance().addTrack(TrackUrl.SKIN_TEST_COURSE);
                }else{
                    Toast.makeText(mActivity, R.string.intent_no, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 光线传感器
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mSensor == null) {
            mBrightness = DEFAULT_BRIGHTNESS;
            Toast.makeText(mActivity, "该设备没有光线传感器", Toast.LENGTH_SHORT).show();
        }

        // mSurfaceView
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new MySurfaceHolderCallback());

        
        // 人脸识别
        	faceDetectManager = new FaceDetectManager(mActivity);
        	faceDetectManager.setIntentClass(SkinResultActivity.class); // 设置取样完成后的跳转页面
        	faceDetectManager.setPreviewCallback(new MyCameraPreviewCallback()); // 设置相机预览回调
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(new MySensorEventListener(), mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(new MySensorEventListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraStopAndRelease();
//        TrackManager.getInstance().addTrack(TrackUrl.PAGE_CLOSE + "SkinTestActivity");
        System.out.println("skintestactivity ondeastory");
    }

    @Override
    public void onBackPressed() {
        finish();
        mCameraStopAndRelease();
    }

    /**
     * 光线感应器获取亮度
     */
    private class MySensorEventListener implements SensorEventListener
    {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mBrightness = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    /**
     * SurfaceView监听
     */
    private class MySurfaceHolderCallback implements SurfaceHolder.Callback
    {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCameraStopAndRelease();
                mCamera = Camera.open();
                // 人脸识别
                faceDetectManager.cameraOpened(mCamera); // 通知相机被打开，准备处理预览图片，一定在Camera.open()之后
//                TrackManager.getInstance().addTrack(
//                        TrackUrl.SKIN_TEST_STEP + "show_camera");
            } catch (Exception e) {
                showDialog();
                e.printStackTrace();
            }
        }

        @SuppressLint("InlinedApi")
        @SuppressWarnings("deprecation")
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                int height) {
            try {
                mParams = mCamera.getParameters();
                mPreviewSize = SkinTestUtil.getFitPreviewSize(mParams
                        .getSupportedPreviewSizes());
                mPictureSize = SkinTestUtil.getFitPictureSize(mParams
                        .getSupportedPictureSizes());
                mPreviewWidth = mPreviewSize.width;
                mPreviewHeight = mPreviewSize.height;

                // 设置 mSurfaceView
                float previewRate = mPreviewWidth * 1.0f / mPreviewHeight;
                int screenWidth = SkinTestUtil.getScreenWidth(mActivity);
                int screenHeight = (int) (screenWidth * previewRate);
                ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
                        mSurfaceView.getLayoutParams());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        margin);
                layoutParams.height = screenHeight;
                mSurfaceView.setLayoutParams(layoutParams);

                // 设置 mParams
                mParams.setPictureFormat(PixelFormat.JPEG);
                mParams.setPictureSize(mPictureSize.width, mPictureSize.height);
                mParams.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

                // 设置 自动对焦
                List<String> focusModes = mParams.getSupportedFocusModes();
                if (focusModes
                        .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                // 设置 mCamera
                mCamera.setDisplayOrientation(90);
                mCamera.setParameters(mParams);

                // 开始预览
                mCamera.setPreviewDisplay(holder);
                mCamera.setPreviewCallback(new MyCameraPreviewCallback());
                mCamera.startPreview();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCameraStopAndRelease();
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setMessage("糟糕，肌肤测试崩溃了。\n建议您清理手机内存帮助肤君复活")
                .setCancelable(false)
                .setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
    }

    /**
     * 正在预览
     */
    private class MyCameraPreviewCallback implements Camera.PreviewCallback
    {
        @Override
        public void onPreviewFrame(final byte[] data, Camera camera) {

            // 人脸识别
            faceDetectManager.onPreviewFrame(data, mBrightness); // 监听实时预览，处理图片
        }
    }

    /**
     * 释放相机资源
     */
    private void mCameraStopAndRelease() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            System.out.println("skintestactivity ondeastory mCameraStopAndRelease");
        }
    }
}