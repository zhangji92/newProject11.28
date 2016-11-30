package com.yoka.mrskin.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.data.YKAdBoot;
import com.yoka.mrskin.model.managers.YKAdRecordManager;
import com.yoka.mrskin.util.AdBootUtil;

public class AdBootActivity extends FragmentActivity {

    private Context mContext;
    private ImageView mSkipImageView;
    private ImageView mYKMultiMediaView;
    private YKAdBoot mYKAdBoot;
    private String[] clikUrl;
    private String[] showUrl;
    private String mSplit = "<@y>";
    private boolean mItent = true;
    private Bundle bundle;
    private String mExtras="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_boot_layout);
        mContext = this;
        findViewById();
        init();
    }

    private void findViewById() {
        mSkipImageView = (ImageView) findViewById(R.id.adBootImageView);
        mYKMultiMediaView = (ImageView) findViewById(R.id.adBootYKMultiMediaView);
        mSkipImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                initMain();
            }
        });
    }

    private void init() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mExtras = bundle.getString("push_info");
        }
        mYKAdBoot = AdBootUtil.getInstance(mContext).getYkAdBoot();
        //hh为了防止内存溢出
        //	if(TextUtils.isEmpty(AdBootUtil.getInstance(mContext).getAdBootError())){
        AdBootUtil.getInstance(mContext).saveAdBootError();
   
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).considerExifParams(true)
				.build();
      //  ImageLoader.getInstance().displayImage(mYKAdBoot.Data.get(0).phourl, mYKMultiMediaView, options);
        ImageLoader.getInstance().displayImage("file://"+AdBootUtil.getInstance(mContext).getFile(mYKAdBoot.Data.get(0).phourl).toString(), mYKMultiMediaView);
      //  Glide.with(mContext).load(AdBootUtil.getInstance(mContext).getFile(mYKAdBoot.Data.get(0).phourl)).into(mYKMultiMediaView);
 
        AdBootUtil.getInstance(mContext).clearAdBootError();;
        //	}

        mYKMultiMediaView.setScaleType(ScaleType.CENTER_CROP);

        String clickurlString = mYKAdBoot.Data.get(0).adurl.clickurl;
        if(!TextUtils.isEmpty(clickurlString))clikUrl=clickurlString.split(mSplit);
        String showUrlString=mYKAdBoot.Data.get(0).adurl.showurl;
        if(!TextUtils.isEmpty(showUrlString))showUrl=showUrlString.split(mSplit);

        YKAdRecordManager.getInstance().requestAdBootRecord(mContext,YKAdRecordManager.BootInterFaceId,mYKAdBoot.Data.get(0).id,
                mYKAdBoot.Data.get(0).adurl.showurl, YKAdRecordManager.BootAdShow);
        if (showUrl != null && showUrl.length != 0) {
            for (int i = 0; i < showUrl.length; i++) {
                YKAdRecordManager.getInstance().requestAdUrl(showUrl[i]);
            }
        }
        mYKMultiMediaView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("2".equals(mYKAdBoot.Data.get(0).ptype))
                    return;
                if (clikUrl != null && clikUrl.length != 0) {
                    
                    for (int i = 0; i < clikUrl.length; i++) {
                        YKAdRecordManager.getInstance().requestAdUrl(clikUrl[i]);
                    }
                }
                YKAdRecordManager.getInstance().requestAdBootRecord(mContext,YKAdRecordManager.BootInterFaceId,mYKAdBoot.Data.get(0).id,
                        mYKAdBoot.Data.get(0).adurl.clickurl, YKAdRecordManager.BootAdClick);
                initMain();
                if ("1".equals(mYKAdBoot.Data.get(0).ptype)) {
                    Intent shareTop = new Intent(AdBootActivity.this, YKWebViewActivity.class);
                    shareTop.putExtra("url", mYKAdBoot.Data.get(0).linkurl);
                    startActivity(shareTop);
                } else {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(mYKAdBoot.Data.get(0).linkurl);
                    intent.setData(content_url);
                    startActivity(intent);
                }

            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mItent)
                    initMain();
            }
        }, Integer.parseInt(mYKAdBoot.showTime));
    }


    private void initMain() {
        mItent=false;
        Intent intent;
        intent = new Intent(mContext, MainActivity.class);
        if(!TextUtils.isEmpty(mExtras))intent.putExtra("push_info", mExtras);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AdBootActivity");
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AdBootActivity");
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    public void onBackPressed() {
        initMain();
    }

}
