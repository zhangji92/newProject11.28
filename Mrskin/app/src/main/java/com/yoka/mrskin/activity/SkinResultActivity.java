package com.yoka.mrskin.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aijifu.skintest.api.DetectCallback;
import com.aijifu.skintest.api.DetectRet;
import com.aijifu.skintest.api.SkinComputeManager;
import com.aijifu.skintest.api.SkinData;
import com.aijifu.skintest.api.SkinPart;
import com.aijifu.skintest.util.ComUtils;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKUploadSkinDataManager;
import com.yoka.mrskin.model.managers.YKUploadSkinDataManager.OnUploadCompleteListener;
import com.yoka.mrskin.model.managers.YKUploadSkinDataManager.YKGeneralCallBack;
import com.yoka.mrskin.util.AppUtil;

/**
 * 肌肤测试结果页
 * 
 * @author z l l
 * 
 */
public class SkinResultActivity extends BaseActivity implements
        OnClickListener, OnUploadCompleteListener
{
    private Activity mActivity;

    // view
    private TextView mProgressNum, mProgressText;
    private View mReTestLayout, mGiveUpLayout;
    private ProgressBar mProgressBar;
    private ArrayList<SkinPart> mSkinParts;
    private String mUrl;
    private View mLoadingLayout;
    private ImageView mLoadingImage;
    private ArrayList<String> mRightPaths, mLeftPaths, mHeadPaths, mJawPaths,
            mPartTPaths;
    private ArrayList<ArrayList<String>> mWholePath;
    private String mOrignPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skin_result_activity);
        mActivity = this;
        initView();
        startTest();
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_OPEN + "SkinResultActivity");
    }

    @Override
    protected void onDestroy()
    {
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_CLOSE + "SkinResultActivity");
        super.onDestroy();
    }

    private void initView()
    {
        mLoadingLayout = findViewById(R.id.skin_result_loading_layout);
        mLoadingImage = (ImageView) findViewById(R.id.skin_result_loading_big);
        mReTestLayout = findViewById(R.id.skin_result_retest_layout);
        mGiveUpLayout = findViewById(R.id.skin_result_give_up_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.skin_result_progress_bar);
        mProgressNum = (TextView) findViewById(R.id.skin_retule_progress_number);
        mProgressText = (TextView) findViewById(R.id.skin_result_progress_text);
        mProgressText.setText(getString(R.string.skin_result_test_ing));
        mReTestLayout.setOnClickListener(this);
        mGiveUpLayout.setOnClickListener(this);

        mProgressBar.setMax(100);
    }

    private void startTest()
    {
        // 分数计算
        SkinComputeManager skinComputeManager = new SkinComputeManager(
                mActivity, "aijifu");
        // 设置回调
        skinComputeManager.setDetectCallback(new DetectCallback()
        {

            @Override
            public void onProgress(int process, int processCount, String hint)
            {
                // mTvHint.setText("\n进度：" + process + "/" + processCount
                // + "\n当前操作：" + hint);
                // Toast.makeText(
                // mActivity,
                // "\n进度：" + process + "/" + processCount + "\n当前操作："
                // + hint, Toast.LENGTH_LONG).show();
                int progress = process + 2;
                mProgressBar.setProgress(progress * (100 / processCount));
                mProgressNum.setText((progress * (100 / processCount)) + "%");
            }

            @Override
            public void onComplete(DetectRet ret, final SkinData data)
            {
                String message = null;
                if (ret == DetectRet.SUCCESSED) {
//                    TrackManager.getInstance().addTrack(
//                            TrackUrl.SKIN_TEST_STEP + "start_analize");
//                    TrackManager.getInstance().addTrack(
//                            TrackUrl.SKIN_TEST_STEP + "analizing1");
                    YKUploadSkinDataManager.getInstnace()
                            .setOnCompletedListener(SkinResultActivity.this);
                    message = "计算已完成";
                    // StringBuilder builderResult = new StringBuilder();
                    // builderResult.append("\n肌肤年龄：" + data.getSkinAge());
                    // System.out.println("skinresultactivity skindata1 = "
                    // + builderResult.toString());
                    // builderResult.append("\n性别：" + data.getGender());
                    // builderResult.append("\n右面颊分数："
                    // + data.getFaceRight().getOrigin().getScore());
                    // builderResult.append("\n左面颊分数："
                    // + data.getFaceLeft().getOrigin().getScore());
                    // builderResult.append("\n额头分数："
                    // + data.getHead().getOrigin().getScore());
                    // builderResult.append("\n下巴分数："
                    // + data.getJaw().getOrigin().getScore());
                    // builderResult.append("\nT型区分数："
                    // + data.getPartT().getOrigin().getScore());
                    // System.out.println("skinresultactivity skindata = "
                    // + builderResult.toString());

                    mSkinParts = new ArrayList<SkinPart>();
                    mSkinParts.add(data.getFaceRight());
                    mSkinParts.add(data.getFaceLeft());
                    mSkinParts.add(data.getHead());
                    mSkinParts.add(data.getJaw());
                    mSkinParts.add(data.getPartT());
                    Toast.makeText(mActivity, "正在上传数据...", Toast.LENGTH_LONG)
                            .show();
                    startAnim();
                    addData(data);
                    mOrignPath = data.getOriginImgPath();
                    if (mWholePath != null && mWholePath.size() > 0) {
                        uploadData(data, mOrignPath, mSkinParts, mWholePath);
                    }
                } else if (ret == DetectRet.FAILED_FACE_DETECT) {
                    message = "面部信息获取失败，建议重新拍照获取";
                } else if (ret == DetectRet.FAILED_NETWORK) {
                    message = "网络异常(请重新测试)";
                } else if (ret == DetectRet.FAILED_OOM) {
                    showDialog();
                    message = "内存溢出";
                } else if (ret == DetectRet.FAILED_DATA) {
                    message = "数据异常(请重新测试)";
                } else if (ret == DetectRet.FAILED_FACE_SERVER) {
                    message = "人脸识别请求超时(请重新测试)";
                } else if (ret == DetectRet.FAILED_IMAGE_NOT_FOUND) {
                    message = "SD卡缓存图片未找到(请重新测试)";
                }
                final String tmpMessage = message;
                mProgressText.setText(tmpMessage);
                // Toast.makeText(mActivity, tmpMessage,
                // Toast.LENGTH_LONG).show();
            }
        });
        // 执行计算操作
        skinComputeManager.start(); // 开始计算
    }

    private void showDialog()
    {
        new AlertDialog.Builder(this)
                .setMessage("糟糕，肌肤测试崩溃了。\n建议您清理手机内存帮助肤君复活")
                .setCancelable(false)
                .setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
        case R.id.skin_result_retest_layout:
            startSkinTestActivity();
            break;
        case R.id.skin_result_give_up_layout:
            finish();
            break;
        }
    }

    private void startSkinTestActivity()
    {
        Intent intent = new Intent(this, SkinTestActivity.class);
        startActivity(intent);
        finish();
//        TrackManager.getInstance().addTrack(
//                TrackUrl.SKIN_TEST_ENTRY + "re_test");
    }

    private void uploadData(SkinData data, String path,
            ArrayList<SkinPart> skinParts, ArrayList<ArrayList<String>> filePath)
    {
        AppUtil.saveUploadTime(mActivity, System.currentTimeMillis());
        YKUploadSkinDataManager.getInstnace().uploadWholeSkinData(data, path,
                AppUtil.getUploadTime(mActivity), new YKGeneralCallBack()
                {

                    @Override
                    public void callback(YKResult result, String url)
                    {
                        if (result.isSucceeded()) {
                            // mUrl =
                            // "http://192.168.57.86:8028/fujun/web/skin/main?clientid=a97ba693-8660-48de-b79d-2ef7b08fede4";
                            mUrl = url;
                            // mUrl =
                            // "http://hzp.yoka.com/fujun/web/skin/main?clientid=7fea049a-6974-4a51-ac82-c6bc63baff5f";
                        }
                    }
                });

        YKUploadSkinDataManager.getInstnace().uploadLocalSkinData(skinParts,
                AppUtil.getUploadTime(mActivity), filePath,
                new YKGeneralCallBack()
                {

                    @Override
                    public void callback(YKResult result, String url)
                    {
                        if (result.isSucceeded()) {
                            // mUrl =
                            // "http://192.168.57.86:8028/fujun/web/skin/main?clientid=a97ba693-8660-48de-b79d-2ef7b08fede4";
                            mUrl = url;
                            // mUrl =
                            // "http://hzp.yoka.com/fujun/web/skin/main?clientid=7fea049a-6974-4a51-ac82-c6bc63baff5f";
//                            TrackManager.getInstance().addTrack(
//                                    TrackUrl.SKIN_TEST_STEP + "analizing2");
                        } else {
//                            TrackManager.getInstance().addTrack(
//                                    TrackUrl.SKIN_TEST_STEP
//                                            + "analizing_failed");
                        }
                    }
                });
    }

    private void startAnim()
    {
        mLoadingLayout.setVisibility(View.VISIBLE);
        final Animation rotateAnim = AnimationUtils.loadAnimation(this,
                R.anim.refresh_img_rotate_anim);
        // 匀速旋转
        LinearInterpolator polator = new LinearInterpolator();
        rotateAnim.setInterpolator(polator);
        mLoadingImage.startAnimation(rotateAnim);
    }

    private void clearAnim()
    {
        if (mLoadingImage != null) {
            mLoadingImage.clearAnimation();
        }
        mLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCompleted()
    {
        ComUtils.deleteSkinCache();

//        TrackManager.getInstance().addTrack(
//                TrackUrl.SKIN_TEST_STEP + "analizing_succeed");
        clearAnim();
        deleteFile();
        releaseData();
        Intent intent = new Intent(mActivity, YKWebViewActivity.class);
        intent.putExtra("report_url", mUrl);
        intent.putExtra("report_skin_result", true);
        startActivity(intent);
        if (YKCurrentUserManager.getInstance().isLogin()) {
            YKCurrentUserManager.getInstance().saveIsSkinTest(mActivity, true);
        }
        finish();
    }

    private void addData(SkinData data)
    {
        mWholePath = new ArrayList<ArrayList<String>>();
        mRightPaths = new ArrayList<String>();
        mLeftPaths = new ArrayList<String>();
        mHeadPaths = new ArrayList<String>();
        mJawPaths = new ArrayList<String>();
        mPartTPaths = new ArrayList<String>();

        mRightPaths.add(data.getFaceRight().getOrigin().getImgPath());
        mRightPaths.add(data.getFaceRight().getColor().getImgPath());
        mRightPaths.add(data.getFaceRight().getMoisture().getImgPath());
        mRightPaths.add(data.getFaceRight().getUniformity().getImgPath());
        mRightPaths.add(data.getFaceRight().getHoles().getImgPath());
        mRightPaths.add(data.getFaceRight().getMicrogroove().getImgPath());
        mRightPaths.add(data.getFaceRight().getStain().getImgPath());

        mLeftPaths.add(data.getFaceLeft().getOrigin().getImgPath());
        mLeftPaths.add(data.getFaceLeft().getColor().getImgPath());
        mLeftPaths.add(data.getFaceLeft().getMoisture().getImgPath());
        mLeftPaths.add(data.getFaceLeft().getUniformity().getImgPath());
        mLeftPaths.add(data.getFaceLeft().getHoles().getImgPath());
        mLeftPaths.add(data.getFaceLeft().getMicrogroove().getImgPath());
        mLeftPaths.add(data.getFaceLeft().getStain().getImgPath());

        mHeadPaths.add(data.getHead().getOrigin().getImgPath());
        mHeadPaths.add(data.getHead().getColor().getImgPath());
        mHeadPaths.add(data.getHead().getMoisture().getImgPath());
        mHeadPaths.add(data.getHead().getUniformity().getImgPath());
        mHeadPaths.add(data.getHead().getHoles().getImgPath());
        mHeadPaths.add(data.getHead().getMicrogroove().getImgPath());
        mHeadPaths.add(data.getHead().getStain().getImgPath());

        mJawPaths.add(data.getJaw().getOrigin().getImgPath());
        mJawPaths.add(data.getJaw().getColor().getImgPath());
        mJawPaths.add(data.getJaw().getMoisture().getImgPath());
        mJawPaths.add(data.getJaw().getUniformity().getImgPath());
        mJawPaths.add(data.getJaw().getHoles().getImgPath());
        mJawPaths.add(data.getJaw().getMicrogroove().getImgPath());
        mJawPaths.add(data.getJaw().getStain().getImgPath());

        mPartTPaths.add(data.getPartT().getOrigin().getImgPath());
        mPartTPaths.add(data.getPartT().getColor().getImgPath());
        mPartTPaths.add(data.getPartT().getMoisture().getImgPath());
        mPartTPaths.add(data.getPartT().getUniformity().getImgPath());
        mPartTPaths.add(data.getPartT().getHoles().getImgPath());
        mPartTPaths.add(data.getPartT().getMicrogroove().getImgPath());
        mPartTPaths.add(data.getPartT().getStain().getImgPath());

        System.out.println("SkinResult ImagePath rignt = "
                + mRightPaths.toString() + "  left = " + mLeftPaths.toString()
                + "  head = " + mHeadPaths.toString() + "  jaw = "
                + mJawPaths.toString() + "  partt = " + mPartTPaths.toString());

        mWholePath.add(mRightPaths);
        mWholePath.add(mLeftPaths);
        mWholePath.add(mHeadPaths);
        mWholePath.add(mJawPaths);
        mWholePath.add(mPartTPaths);
    }

    public void deleteFile()
    {
        if (mRightPaths != null) {
            for (int i = 0; i < mRightPaths.size(); i++) {
                deleteFile(mRightPaths.get(i));
            }
        }

        if (mLeftPaths != null) {
            for (int i = 0; i < mLeftPaths.size(); i++) {
                deleteFile(mLeftPaths.get(i));
            }
        }

        if (mHeadPaths != null) {
            for (int i = 0; i < mHeadPaths.size(); i++) {
                deleteFile(mHeadPaths.get(i));
            }
        }

        if (mJawPaths != null) {
            for (int i = 0; i < mJawPaths.size(); i++) {
                deleteFile(mJawPaths.get(i));
            }
        }

        if (mPartTPaths != null) {
            for (int i = 0; i < mPartTPaths.size(); i++) {
                deleteFile(mPartTPaths.get(i));
            }
        }
    }

    public boolean deleteFile(String filePath)
    {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                return file.delete();
            }
        }
        return false;
    }

    private void releaseData()
    {
        if (mRightPaths != null) {
            mRightPaths.clear();
            mRightPaths = null;
        }
        if (mLeftPaths != null) {
            mLeftPaths.clear();
            mLeftPaths = null;
        }
        if (mHeadPaths != null) {
            mHeadPaths.clear();
            mHeadPaths = null;
        }
        if (mJawPaths != null) {
            mJawPaths.clear();
            mJawPaths = null;
        }
        if (mPartTPaths != null) {
            mPartTPaths.clear();
            mPartTPaths = null;
        }
        if (mWholePath != null) {
            mWholePath.clear();
            mWholePath = null;
        }
    }
}
