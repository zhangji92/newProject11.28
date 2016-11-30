package com.yoka.mrskin.activity;

import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.data.YKProductShareInfo;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;
import com.yoka.mrskin.util.SharePopupWindow;
import com.yoka.mrskin.util.YKSharelUtil;
import com.yoka.share.manager.ShareQzoneManager;
import com.yoka.share.manager.ShareSinaManager;
import com.yoka.share.manager.ShareWxManager;

public class HomeFourTopic extends AddTaskWebViewActivity{
    private LinearLayout mBackText;
    private TextView mTitle;
    private String mUrl1, mUrl2, mUrl3, mTitlee, mFocusUrl, mTwoUrl, mSubUrl, mProductDetalis;
    private SharePopupWindow mPopupWindow;
    private View mShareLayout;
    private static YKProductShareInfo mShareInfo;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_four_topic);
        YKActivityManager.getInstance().addActivity(this);
        getData();
        init();
        mWebView.loadUrl(HomeFourTopic.this, mTwoUrl);
        mWebView.setURIHandler(new YKURIHandler() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean handleURI(String url) {
                System.out.println("home four topic url = " + url);
                String tryLogin = YKSharelUtil.tryTotryLoginmUrl(HomeFourTopic.this, url);
                if (tryLogin != null) {
                    // auth
                    if (!YKCurrentUserManager.getInstance().isLogin()) {
                        Intent login = new Intent(HomeFourTopic.this, LoginActivity.class);
                        startActivity(login);
                    } else {
                        // url =
                        // "http://try.yoka.com/fujun/index.php?action=apply&try_id=2819&auth=efc8PEx5BJUSCag9yVynhYiC2%2FgVy7Y3z9puEuMq4ZlUIIcfNPd38aM&clientID=09464f5f-038d-4947-88e7-cd588b1471b2";
                        mWebView.loadUrl(HomeFourTopic.this, url);
                    }
                    return true;
                } else if (mTwoUrl != null) {
                    String urlall = url;
                    Uri uri = Uri.parse(url);
                    String query = uri.getQuery();
                    String spStr[] = query.split("&");
                    String param;
                    for (int i = 0; i < spStr.length; ++i) {
                        param = spStr[i];
                        String paramSplit[] = param.split("=");
                        if (paramSplit.length != 2) {
                            continue;
                        }
                        URLDecoder.decode(paramSplit[1]);
                    }
                    if (!TextUtils.isEmpty(uri.getQueryParameter("url")) && !TextUtils.isEmpty(uri.getQueryParameter("title"))) {
                        Intent ping = new Intent(HomeFourTopic.this, HomeTopShare.class);

                        ping.putExtra("url", uri.getQueryParameter("url"));
                        ping.putExtra("urlall", urlall);

                        if (mUrl1 != null) {
                            ping.putExtra("title", getString(R.string.home_topic_h));
                        } else if (mUrl2 != null) {
                            ping.putExtra("title", getString(R.string.home_topic_be));
                        } else if (mUrl3 != null) {
                            ping.putExtra("title", getString(R.string.home_subject));
                        }
                        startActivity(ping);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void init() {

        mBackText = (LinearLayout) findViewById(R.id.home_four_back_layout);
        mBackText.setOnClickListener(this);

        mWebView = (ProgressWebView) findViewById(R.id.webView);

        mTitle = (TextView) findViewById(R.id.home_four_title);
        mTitle.setText(mTitlee);

        if (mFocusUrl != null) {
            mTwoUrl = mFocusUrl;
        } else if (mUrl1 != null) {
            mTwoUrl = mUrl1;
        } else if (mUrl2 != null) {
            mTwoUrl = mUrl2;
        } else if (mUrl3 != null) {
            mTwoUrl = mUrl3;
        } else if (mSubUrl != null) {
            mTwoUrl = mSubUrl;
        } else if (mProductDetalis != null) {
            mTwoUrl = mProductDetalis + mToken;
        }

        mShareLayout = findViewById(R.id.home_four_share_layout);
        mShareLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mWebView.loadUrl(HomeFourTopic.this, "javascript:shareDataAndroidFun()");
                showSharePop();
            }
        });
    }

    private void getData() {
        Intent getUrl = getIntent();

        mUrl1 = getUrl.getStringExtra("four_url1");
        mUrl2 = getUrl.getStringExtra("four_url2");
        mUrl3 = getUrl.getStringExtra("four_url3");
        mTitlee = getUrl.getStringExtra("title");

        // 从HomeFragment跳转
        mFocusUrl = getUrl.getStringExtra("focusurl");

        // 从HomeTopShare跳转
        mSubUrl = getUrl.getStringExtra("subject");

        // 从productfragment跳转
        mProductDetalis = getUrl.getStringExtra("probation_detail_url");
        mToken = getUrl.getStringExtra("probation_detail_token");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.home_four_back_layout:
            finish();
            break;

        default:
            break;
        }
    }

    private void showSharePop() {
        mPopupWindow = new SharePopupWindow(HomeFourTopic.this, itemsOnClick);
        mPopupWindow.showAtLocation(HomeFourTopic.this.findViewById(R.id.home_four_share_img),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private OnClickListener itemsOnClick = new OnClickListener()
    {

        public void onClick(View v)
        {
            SharePopupWindow.getInstance().dismiss();
            switch (v.getId()) {
            case R.id.popup_friend:
                if (mShareInfo != null) {

                    ShareWxManager.getInstance().shareWxCircle(
                            mShareInfo.getmTitle(), mShareInfo.getmDesc(),
                            mShareInfo.getmImgUrl(), mShareInfo.getmImgUrl());
                }
                dismissPopupWindow();
                break;
            case R.id.popup_wei:
                if (mShareInfo != null) {
                    ShareWxManager.getInstance().shareWx(
                            mShareInfo.getmTitle(), mShareInfo.getmDesc(),
                            mShareInfo.getmImgUrl(), mShareInfo.getmImgUrl(),HomeFourTopic.this);
                }
                dismissPopupWindow();
                break;
            case R.id.popup_sina:
                if (mShareInfo != null) {
                            ShareSinaManager.getInstance().shareSina("肤君分享", mShareInfo.getmTitle(), "", mShareInfo.getmImgUrl(),HomeFourTopic.this);
                }
                dismissPopupWindow();
                break;
            case R.id.popup_qzone:
                if (mShareInfo != null) {
                    ShareQzoneManager.getInstance().shareQzone(mShareInfo.getmTitle(), mShareInfo.getmDesc(),null,mShareInfo.getmImgUrl(),HomeFourTopic.this);
                }
                dismissPopupWindow();
                break;
            }
        }
    };

    private void dismissPopupWindow()
    {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume()
    {
        super.onResume();
        // if(YKCurrentUserManager.getInstance().isLogin()){
        // YKUser user = YKCurrentUserManager.getInstance().getUser();
        // mNewTokin = user.getToken();
        // String urla =
        // "http://try.yoka.com/fujun/index.php?action=apply&try_id=2819&auth=efc8PEx5BJUSCag9yVynhYiC2%2FgVy7Y3z9puEuMq4ZlUIIcfNPd38aM&clientID=09464f5f-038d-4947-88e7-cd588b1471b2";
        // mWebView.loadUrl(HomeFourTopic.this, urla);
        // }
        MobclickAgent.onPageStart("HomeFourTopic"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause()
    {
        super.onPause();
        MobclickAgent.onPageEnd("HomeFourTopic"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }

    public static class getShareInfo
    {
        @JavascriptInterface
        public void getShare(String shareInfo)
        {
            mShareInfo = new YKProductShareInfo();
            String imgUrl = null;
            String title = null;
            String desc = null;
            String link = null;
            try {
                JSONObject json = new JSONObject(shareInfo);
                imgUrl = json.optString("imgUrl");
                title = json.optString("title");
                desc = json.optString("desc");
                link = json.optString("link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                mShareInfo.setmImgUrl(imgUrl);
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                mShareInfo.setmTitle(title);
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                mShareInfo.setmDesc(desc);
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                mShareInfo.setmLink(link);
            }
            Toast.makeText(AppContext.getInstance(), shareInfo,
                    Toast.LENGTH_LONG).show();
        }
    }
}
