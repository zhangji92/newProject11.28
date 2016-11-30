package com.yoka.mrskin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aijifu.skintest.api.DetectCallback;
import com.aijifu.skintest.api.DetectRet;
import com.aijifu.skintest.api.SkinComputeManager;
import com.aijifu.skintest.api.SkinData;
import com.yoka.mrskin.R;

public class SkinResult2Activity extends Activity
{

    private final String TAG = "SkinResultActivity";
    private Activity mActivity;

    // view
    private TextView mTvHint;
    private ImageView mIvImg;
    private Button mBtnDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_result);
        mActivity = this;

        // ActionBar
        // if (getSupportActionBar() != null) {
        // getSupportActionBar().hide();
        // }

        findViewById(R.id.tv_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SkinResult2Activity.this,
                        SkinTest2Activity.class));
            }
        });

        // view
        mTvHint = (TextView) findViewById(R.id.tv_hint);
        mIvImg = (ImageView) findViewById(R.id.iv_img);
        mBtnDetail = (Button) findViewById(R.id.btn_detail);

        // 分数计算
        SkinComputeManager skinComputeManager = new SkinComputeManager(
                mActivity, "aijifu");
        // 设置回调
        skinComputeManager.setDetectCallback(new DetectCallback() {
            @Override
            public void onComplete(DetectRet ret, SkinData data) {
                if (ret == DetectRet.SUCCESSED) {

                    // Bitmap bitmap = BitmapFactory.decodeFile(data
                    // .getOriginImgPath());
                    // mIvImg.setImageBitmap(bitmap);
                    // mIvImg.setVisibility(View.VISIBLE);

                    StringBuilder builderResult = new StringBuilder();
                    builderResult.append("\n肌肤年龄：" + data.getSkinAge());
                    builderResult.append("\n性别：" + data.getGender());
                    builderResult.append("\n右面颊分数："
                            + data.getFaceRight().getOrigin().getScore());
                    builderResult.append("\n左面颊分数："
                            + data.getFaceLeft().getOrigin().getScore());
                    builderResult.append("\n额头分数："
                            + data.getHead().getOrigin().getScore());
                    builderResult.append("\n下巴分数："
                            + data.getJaw().getOrigin().getScore());
                    builderResult.append("\nT型区分数："
                            + data.getPartT().getOrigin().getScore());
                    mTvHint.setText(builderResult.toString());

                } else if (ret == DetectRet.FAILED_NETWORK) {
                    mTvHint.setText("网络异常");
                } else if (ret == DetectRet.FAILED_FACE_DETECT) {
                    mTvHint.setText("面部信息获取失败，建议重新拍照获取");
                } else if (ret == DetectRet.FAILED_OOM) {
                    mTvHint.setText("内存溢出");
                } else if (ret == DetectRet.FAILED_DATA) {
                    mTvHint.setText("数据异常");
                } else if (ret == DetectRet.FAILED_FACE_SERVER) {
                    mTvHint.setText("人脸识别请求超时");
                } else if (ret == DetectRet.FAILED_IMAGE_NOT_FOUND) {
                    mTvHint.setText("SD卡缓存图片未找到");
                }
            }

            @Override
            public void onProgress(int process, int processCount, String hint) {
                mTvHint.setText("\n进度：" + process + "/" + processCount
                        + "\n当前操作：" + hint);
            }
        });
        // 执行计算操作
        skinComputeManager.start(); // 开始计算
    }
}
