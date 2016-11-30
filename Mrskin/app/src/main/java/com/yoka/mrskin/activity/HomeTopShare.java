package com.yoka.mrskin.activity;

import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.model.data.YKShareInfo;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;
import com.yoka.mrskin.util.ProgressWebView.YKUrlTitle;
import com.yoka.mrskin.util.SharePopupWindow;
import com.yoka.mrskin.util.YKSharelUtil;
import com.yoka.share.manager.ShareQzoneManager;
import com.yoka.share.manager.ShareSinaManager;
import com.yoka.share.manager.ShareWxManager;

public class HomeTopShare extends AddTaskWebViewActivity
{
    private TextView mTextTitle;
    private boolean mNoButton = true;
    private LinearLayout mBackText;
    private RelativeLayout mShareBut;
    private SharePopupWindow mPopupWindow;
    private static YKShareInfo mShareInfo;
    private static long mShowLoginTime = -1;
    private String shareDefUrl = "http://p5.yokacdn.com/pic/div/2016/products/fujun/images/down-logo.png";

    private String mURL, mUrlAll;
    private String mTitle, mTitleProduct, mExtras;
    private boolean mTryLoginSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homefragment_top_shar);
        YKActivityManager.getInstance().addActivity(this);
        getData();
        init();
    }

    private void init()
    {
        mBackText = (LinearLayout) findViewById(R.id.home_share_back_layout);
        mBackText.setOnClickListener(this);
        mShareBut = (RelativeLayout) findViewById(R.id.home_top_sharelayout);
        mShareBut.setOnClickListener(this);

        mWebView = (ProgressWebView) findViewById(R.id.webView);
        mTextTitle = (TextView) findViewById(R.id.home_fragment_title);

        if (mUrlAll != null) {
            setupShareInfo(mUrlAll);
        }

        if (mNoButton) {
            mShareBut.setVisibility(View.GONE);
        } else {
            mShareBut.setVisibility(View.VISIBLE);
        }
        mWebView.setURIHandler(new YKURIHandler()
        {
            @Override
            public boolean handleURI(String url)
            {
                String webpage = YKSharelUtil.tryToGetWebPagemUrl(
                        HomeTopShare.this, url);
                if (!TextUtils.isEmpty(webpage)) {
                    Uri uri = Uri.parse(url);
                    if (!TextUtils.isEmpty(uri.getQueryParameter("url"))) {
                        String mWebPager = uri.getQueryParameter("url");
                        mWebView.loadUrl(HomeTopShare.this, mWebPager);
                        return true;
                    }
                }

                String share = YKSharelUtil.tryToGetShareFormUrl(
                        HomeTopShare.this, url);
                if (share != null) {
                    Uri uri = Uri.parse(url);
                    if (!TextUtils.isEmpty(uri.getQueryParameter("platform"))) {
                        String type = uri.getQueryParameter("platform");
                        if (type.equals("sinaweibo") || type.equals("sinawei")) {
                            if (mShareInfo != null) {
//                                Glide.with(HomeTopShare.this).asBitmap()   .load(mShareInfo.getmPic()) 
//                                .into(new SimpleTarget<Bitmap>(250, 250) {

//                                    @Override
//                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                                        ShareSinaManager.getInstance().sendShare(resource,
//                                                mShareInfo.getmTitle(), "",
//                                                "//" + mShareInfo.getmUrl());
                                        ShareSinaManager.getInstance().shareSina("肤君分享", mShareInfo.getmTitle(), mShareInfo.getmUrl(), mShareInfo.getmPic(),HomeTopShare.this);
//                                    }
//                                }
//                                        ); 
                            } else {
                                ShareSinaManager.getInstance().shareSina("肤君分享", mShareInfo.getmTitle(), mShareInfo.getmUrl(), shareDefUrl,HomeTopShare.this);
                            }
                        } else if (type.equals("weixin_timeline")) {
                            if (mShareInfo != null) {
                                // YKImageManager.getInstance().requestImage(
                                // mShareInfo.getmPic(), new Callback() {
                                // @Override
                                // public void callback(
                                // YKImageTask task,
                                // YKMultiMediaObject bitmap) {
                                // Bitmap shareImage =
                                // handleDownloadedImage(bitmap);
                                // Bitmap NewBirmap = zoomImage(
                                // shareImage,
                                // shareImage.getWidth(),
                                // shareImage.getHeight());
                                ShareWxManager.getInstance().shareWxCircle(
                                        mShareInfo.getmTitle(),
                                        mShareInfo.getmSummary(),
                                        mShareInfo.getmUrl(),
                                        mShareInfo.getmPic());
                                // }
                                // });
                            }
                            // else {
                            // Bitmap rawBitmapd = BitmapFactory
                            // .decodeResource(getResources(),
                            // R.drawable.task_img1);
                            // // ShareWxManager.getInstance().shareWxCircle(
                            // // mShareInfo.getmTitle(),
                            // // mShareInfo.getmSummary(),
                            // // mShareInfo.getmUrl(), rawBitmapd);
                            // }
                        } else if (type.equals("weixin_session")) {
                            if (mShareInfo != null) {
                                // YKImageManager.getInstance().requestImage(
                                // mShareInfo.getmPic(), new Callback() {
                                // @Override
                                // public void callback(
                                // YKImageTask task,
                                // YKMultiMediaObject bitmap) {
                                // Bitmap shareImage =
                                // handleDownloadedImage(bitmap);
                                // Bitmap NewBirmap = zoomImage(
                                // shareImage,
                                // shareImage.getWidth(),
                                // shareImage.getHeight());
                                ShareWxManager.getInstance().shareWx(
                                        mShareInfo.getmTitle(),
                                        mShareInfo.getmSummary(),
                                        mShareInfo.getmUrl(),
                                        mShareInfo.getmPic(),HomeTopShare.this);
                                // }
                                // });
                            }
                            // else {
                            // Bitmap rawBitmap = BitmapFactory
                            // .decodeResource(getResources(),
                            // R.drawable.task_img1);
                            // // ShareWxManager.getInstance().shareWx(
                            // // mShareInfo.getmTitle(),
                            // // mShareInfo.getmSummary(),
                            // // mShareInfo.getmUrl(), rawBitmap);
                            // }
                        } else if (type.equals("qzone")) {
                            if (mShareInfo != null) {
                                ShareQzoneManager.getInstance().shareQzone(mShareInfo.getmTitle(),mShareInfo.getmSummary(),mShareInfo.getmUrl(),mShareInfo.getmPic(),HomeTopShare.this);
                            }
                        }
                    }
                    return true;
                } else {
                    String tryLogin = YKSharelUtil.tryTotryLoginmUrl(
                            HomeTopShare.this, url);
                    if (tryLogin != null) {
                        if (YKCurrentUserManager.getInstance().isLogin()) {
                        } else {
                            Intent intent = new Intent(HomeTopShare.this,
                                    LoginActivity.class);
                            intent.putExtra("needresult", true);
                            startActivityForResult(intent, 1937);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        setTitle();

        if (!TextUtils.isEmpty(mExtras)) {
            try {
                JSONObject dataJson = new JSONObject(mExtras);
                String response = dataJson.getString("target");
                @SuppressWarnings("deprecation")
                String urlDecodedString = URLDecoder.decode(response);
                setupShareInfo(urlDecodedString);

                // String mNewURL = uri.getQueryParameter("url");
                mWebView.loadUrl(HomeTopShare.this, mShareInfo.getmUrl());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        if (!TextUtils.isEmpty(mURL)) {
            mWebView.loadUrl(this, mURL);
        }
        if (mURL.startsWith("http://try.yoka.com/fujun/")) {
            mURL = setupTryURL(mURL);
        }
    }

    private void setupShareInfo(String url)
    {
        if (url != null) {
            Uri uri = Uri.parse(url);
            mShareInfo = new YKShareInfo();
            // 任务id或者文章id
            if (!TextUtils.isEmpty(uri.getQueryParameter("id"))) {
                String id = uri.getQueryParameter("id");
                mShareInfo.setID(id);
            }
            // 标题
            if (!TextUtils.isEmpty(uri.getQueryParameter("title"))) {
                String title = uri.getQueryParameter("title");
                mShareInfo.setmTitle(title);
            }
            // 图片路径
            if (!TextUtils.isEmpty(uri.getQueryParameter("pic"))) {
                String pic = uri.getQueryParameter("pic");
                mShareInfo.setmPic(pic);
            }
            // url
            if (!TextUtils.isEmpty(uri.getQueryParameter("url"))) {
                String Surl = uri.getQueryParameter("url");
                mShareInfo.setmUrl(Surl);
            }
            // 副标题
            if (!TextUtils.isEmpty(uri.getQueryParameter("summary"))) {
                String summary = uri.getQueryParameter("summary");
                mShareInfo.setmSummary(summary);
            }
            // 类型
            if (!TextUtils.isEmpty(uri.getQueryParameter("platform"))) {
                String platform = uri.getQueryParameter("platform");
                mShareInfo.setmPlatform(platform);
            }

        }
    }

    // 获取标题
    private void setTitle()
    {
        mWebView.setTitleHandler(new YKUrlTitle()
        {
            @Override
            public String getTitle(String title)
            {
                if (title != null) {
                    mTitleProduct = title;
                    if (mTitleProduct
                            .equals(getString(R.string.product_detalis_product_detalis))) {
                        mTextTitle.setText(mTitleProduct);
                    } else if (mTitleProduct
                            .equals(getString(R.string.product_detalis_product_xin))) {
                        mTextTitle.setText(mTitleProduct);
                    } else {
                        mTextTitle.setText(mTitle);
                    }
                }
                return mTitleProduct;
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
        case R.id.home_share_back_layout:
            if (mWebView.canGoBack() && !mTryLoginSuccess) {
                mWebView.goBack();
                mTextTitle.setText(R.string.product_detalis_product_detalis);
            } else {
                finish();
            }
            break;
        case R.id.home_top_sharelayout:
            // 实例化SelectPicPopupWindow
            mPopupWindow = new SharePopupWindow(HomeTopShare.this, itemsOnClick);
            // 显示窗口
            mPopupWindow.showAtLocation(
                    HomeTopShare.this.findViewById(R.id.home_top_sharelayout),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            // 设置layout在PopupWindow中显示的位置
            break;
        default:
            break;
        }
    }

    private void getData()
    {
        Intent shareTop = getIntent();

        mURL = shareTop.getStringExtra("url");
        mTitle = shareTop.getStringExtra("title");
        mUrlAll = shareTop.getStringExtra("urlall");
        mNoButton = shareTop.getBooleanExtra("noShareBut", false);

        // 极光推送接收
        Bundle bundle = shareTop.getExtras();
        mExtras = bundle.getString(JPushInterface.EXTRA_EXTRA);

    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener()
    {

        public void onClick(View v)
        {
            SharePopupWindow.getInstance().dismiss();
            switch (v.getId()) {
            case R.id.popup_friend:
                if (mShareInfo != null) {
                    // YKImageManager.getInstance().requestImage(
                    // mShareInfo.getmPic(), new Callback() {
                    // @Override
                    // public void callback(YKImageTask task,
                    // YKMultiMediaObject bitmap) {
                    // Bitmap shareImage = handleDownloadedImage(bitmap);
                    // Bitmap NewBirmap = zoomImage(shareImage,
                    // shareImage.getWidth(),
                    // shareImage.getHeight());
                    ShareWxManager.getInstance().shareWxCircle(
                            mShareInfo.getmTitle(), mShareInfo.getmSummary(),
                            mShareInfo.getmUrl(), mShareInfo.getmPic());
                    mPopupWindow.dismiss();
                    // }
                    // });
                }
                // else {
                // Bitmap rawBitmapd = BitmapFactory.decodeResource(
                // getResources(), R.drawable.task_img1);
                // ShareWxManager.getInstance().shareWxCircle(
                // mShareInfo.getmTitle(), mShareInfo.getmSummary(),
                // mShareInfo.getmUrl());
                // mPopupWindow.dismiss();
                // }
                break;
            case R.id.popup_wei:
                if (mShareInfo != null) {
                    // YKImageManager.getInstance().requestImage(
                    // mShareInfo.getmPic(), new Callback() {
                    // @Override
                    // public void callback(YKImageTask task,
                    // YKMultiMediaObject bitmap) {
                    // Bitmap shareImage = handleDownloadedImage(bitmap);
                    // Bitmap NewBirmap = zoomImage(shareImage,
                    // shareImage.getWidth(),
                    // shareImage.getHeight());
                    ShareWxManager.getInstance().shareWx(
                            mShareInfo.getmTitle(), mShareInfo.getmSummary(),
                            mShareInfo.getmUrl(), mShareInfo.getmPic(),HomeTopShare.this);
                    mPopupWindow.dismiss();
                    // }
                    // });
                }
                // else {
                // Bitmap rawBitmap = BitmapFactory.decodeResource(
                // getResources(), R.drawable.task_img1);
                // ShareWxManager.getInstance().shareWx(
                // mShareInfo.getmTitle(), mShareInfo.getmSummary(),
                // mShareInfo.getmUrl());
                // mPopupWindow.dismiss();
                // }
                break;
            case R.id.popup_sina:
                if (mShareInfo != null) {
//                    Glide.with(HomeTopShare.this).asBitmap()   .load(mShareInfo.getmPic()) 
//                    .into(new SimpleTarget<Bitmap>(250, 250) {
//
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            ShareSinaManager.getInstance().sendShare(resource,
//                                    mShareInfo.getmTitle(), "",
//                                    "//" + mShareInfo.getmUrl());
                            ShareSinaManager.getInstance().shareSina("肤君分享", mShareInfo.getmTitle(), mShareInfo.getmUrl(), mShareInfo.getmPic(),HomeTopShare.this);
//                        }
//                    }
//                            ); 
                } else {
                    ShareSinaManager.getInstance().shareSina("肤君分享", mShareInfo.getmTitle(), mShareInfo.getmUrl(),shareDefUrl,HomeTopShare.this);
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.popup_qzone:
                if (mShareInfo != null) {
                    ShareQzoneManager.getInstance().shareQzone(mShareInfo.getmTitle(), mShareInfo.getmSummary(),mShareInfo.getmUrl(),mShareInfo.getmPic(),HomeTopShare.this);
                    mPopupWindow.dismiss();
                }
                break;
            default:
                break;
            }
        }
    };

    public static class getIntentInfo
    {
        public void getShare(String getShare)
        {
            String spStr[] = getShare.split("&");
            String param;
            mShareInfo = new YKShareInfo();
            for (int i = 0; i < spStr.length; ++i) {
                param = spStr[i];
                String paramSplit[] = param.split("=");
                if (paramSplit.length != 2) {
                    continue;
                }
                // mTitle
                if (paramSplit[0].equals("title")) {
                    @SuppressWarnings("deprecation")
                    String title = URLDecoder.decode(paramSplit[1]);
                    mShareInfo.setmTitle(title);
                }
                // summary
                if (paramSplit[0].equals("summary")) {
                    @SuppressWarnings("deprecation")
                    String summary = URLDecoder.decode(paramSplit[1]);
                    mShareInfo.setmSummary(summary);
                }
                // pic
                if (paramSplit[0].equals("pic")) {
                    @SuppressWarnings("deprecation")
                    String pic = URLDecoder.decode(paramSplit[1]);
                    mShareInfo.setmPic(pic);
                }
                // url
                if (paramSplit[0].equals("url")) {
                    @SuppressWarnings("deprecation")
                    String url = URLDecoder.decode(paramSplit[1]);
                    mShareInfo.setmUrl(url);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == 1937) {
            if (resultCode == RESULT_OK) {
                if (YKCurrentUserManager.getInstance().isLogin()) {
                    long currentTime = System.currentTimeMillis();

                    if (currentTime - mShowLoginTime > 2000) {
                        mShowLoginTime = currentTime;
                        String url = mWebView.getCurrentURL();
                        url = setupTryURL(url);
                        mWebView.loadUrl(HomeTopShare.this, url);
                        mTryLoginSuccess = true;
                    }
                }
            } else {

                mWebView.goBack();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume()
    {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
        MobclickAgent.onPageStart("HomeTopShare"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause()
    {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
        MobclickAgent.onPageEnd("HomeTopShare"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }

    private String setupTryURL(String url)
    {
        Uri uri = Uri.parse(url);
        if (TextUtils.isEmpty(uri.getQuery())) {
            url += "?";
        } else {
            url += "&";
        }
        if (YKCurrentUserManager.getInstance().isLogin()) {
            url += "auth="
                    + YKCurrentUserManager.getInstance().getUser().getAuth();
        }
        return url;
    }

    // 计算Bitmap图片的宽高
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
            double newHeight)
    {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
            if (mWebView.canGoBack() && !mTryLoginSuccess) {
                System.out.println("webview go back");
                mWebView.goBack();
                mTextTitle.setText(R.string.product_detalis_product_detalis);
            } else {
                System.out.println("HomeTopShare finish");
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
