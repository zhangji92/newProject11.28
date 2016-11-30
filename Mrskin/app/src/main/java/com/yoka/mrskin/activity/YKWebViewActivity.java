package com.yoka.mrskin.activity;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.yoka.mrskin.R;
import com.yoka.mrskin.R.string;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKComment;
import com.yoka.mrskin.model.data.YKFocusImage;
import com.yoka.mrskin.model.data.YKProductReport;
import com.yoka.mrskin.model.data.YKProductShareInfo;
import com.yoka.mrskin.model.data.YKShareInfo;
import com.yoka.mrskin.model.data.YKWebComment;
import com.yoka.mrskin.model.data.YKWebentry;
import com.yoka.mrskin.model.managers.YKAvailManagers;
import com.yoka.mrskin.model.managers.YKAvailManagers.AvailCallback;
import com.yoka.mrskin.model.managers.YKCollectManagers;
import com.yoka.mrskin.model.managers.YKCollectManagers.ColectCallback;
import com.yoka.mrskin.model.managers.YKCommentreplyManager;
import com.yoka.mrskin.model.managers.YKCommentreplyManager.IDReplyCallback;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKCurrentUserManager.ILoginCallback;
import com.yoka.mrskin.model.managers.YKDeleteInformationAndTipsAllManager;
import com.yoka.mrskin.model.managers.YKDeleteInformationAndTipsAllManager.deleteAllYKInformationAndTipsCallback;
import com.yoka.mrskin.model.managers.YKDeleteInformationAndTipsManager;
import com.yoka.mrskin.model.managers.YKDeleteInformationAndTipsManager.deleteYKInformationAndTipsCallback;
import com.yoka.mrskin.model.managers.YKEnquiryCheckAddManagers;
import com.yoka.mrskin.model.managers.YKEnquiryCheckAddManagers.EnquiryPointAddCallback;
import com.yoka.mrskin.model.managers.YKEnquiryPointPraiseManagers;
import com.yoka.mrskin.model.managers.YKEnquiryPointPraiseManagers.EnquiryPointPraiseCallback;
import com.yoka.mrskin.model.managers.YKPonitCommentManagers;
import com.yoka.mrskin.model.managers.YKPonitCommentManagers.PonitCommentCallback;
import com.yoka.mrskin.model.managers.YKThumbupManager;
import com.yoka.mrskin.model.managers.YKThumbupManager.ThumbupCallback;
import com.yoka.mrskin.model.managers.YKaddInformationAndTipsManager;
import com.yoka.mrskin.model.managers.YKaddInformationAndTipsManager.AddYKInformationAndTipsCallback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.PersonalWebView;
import com.yoka.mrskin.util.PersonalWebView.YKURIHandler;
import com.yoka.mrskin.util.PersonalWebView.YKUrlTitle;
import com.yoka.mrskin.util.PersonalWebView.onLoadStateListener;
import com.yoka.mrskin.util.PersonalWebView.onWebViewInputListener;
import com.yoka.mrskin.util.SharePopupWindow;
import com.yoka.mrskin.util.YKDeviceInfo;
import com.yoka.mrskin.util.YKSharelUtil;
import com.yoka.mrskin.util.YKUtil;
import com.yoka.share.manager.ShareQQManager;
import com.yoka.share.manager.ShareQzoneManager;
import com.yoka.share.manager.ShareSinaManager;
import com.yoka.share.manager.ShareWxManager;

/**
 * 产品&心得&试用底层页
 * 
 * @author z l l
 * 
 */
public class YKWebViewActivity extends BaseActivity implements onLoadStateListener, onWebViewInputListener,OnLayoutChangeListener {
    public static final String URL_INTENT_TILTE = "url_intent_tilte";
    public static final String U_NUM_INTENT = "u_num_intent";
    private static final int RESULT_CODE = 8;
    private static final int TIRAL_RESULT_CODE = 1;
    private static boolean IS_TIRAL_SUCCESS = false;
    public static final int REQUEST_CODE = 14;
    // 用来记录赚优币页面是否发表心得，每次在从我的页面设定false
    public static boolean SHOP_CHANGE = false;
    private PersonalWebView mWebView;
    private View mShareLayout, mDoneLayout;
    public View mBackLayout;
    private static EditText mEditText;
    private SharePopupWindow mPopupWindow;
    private static YKProductShareInfo mInfo;
    private static YKProductReport mProduct;
    private String mUrl, mWriteUrl, mProductUrl;
    private YKShareInfo mShareInfo;
    private String mToken;
    private static TextView mTitle;
    private TextView mUExchangeTextView;
    private TextView mSendText;
    private boolean isTrial = false;
    private boolean isLogin = false;
    private boolean isClearHistory = false;
    private String mTrackType, mTrackTypeId, mCommentID, mCommentInfourID, mShowSpID;
    private static String mReplytoID, mClassType, mReplytousername, mReplyID, mReplytouserid;
    private boolean isSkinTest;
    private ImageView point, comment, mAeniorImage;
    private static CustomButterfly mCustomButterfly = null;
    private String mCollect, mAvail, mExperienceUrl;
    private boolean isHomeTop = false;
    private boolean isFocusAd = false;
    public static int FILECHOOSER_RESULTCODE = 16;
    private ValueCallback<Uri> mUploadMessage = null;
    private YKFocusImage mFocusImage = null;
    private YKWebentry mWebentry = null;
    private String mRefer = null;
    private static Context mContext;
    private RelativeLayout mBottomLayout;
    private boolean isGoneorVisible = false;
    private LinearLayout mLinear;
    private String addurlString;// 处理外链广告逻辑

    //Activity最外层的Layout视图  
    private View activityRootView;  
    //屏幕高度  
    private int screenHeight = 0;  
    //软件盘弹起后所占高度阀值  
    private int keyHeight = 0; 

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    switch (msg.what) {
	    case 0:
		mShareLayout.setVisibility(View.GONE);
		mDoneLayout.setVisibility(View.VISIBLE);
		break;
	    case 1:
		mShareLayout.setVisibility(View.VISIBLE);
		mDoneLayout.setVisibility(View.GONE);
		break;
	    case 2:
		mShareLayout.setVisibility(View.INVISIBLE);
		mUExchangeTextView.setVisibility(View.GONE);
		break;
	    case 3:
		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.u_shop_layout, null);
		TextView uNumTextView = (TextView) layout.findViewById(R.id.uShopNumTextView);
		uNumTextView.setText(getIntent().getStringExtra(U_NUM_INTENT));
		TextView uShopTextView = (TextView) layout.findViewById(R.id.uShopMoreTextView);
		uShopTextView.setOnClickListener(new View.OnClickListener() {

		    @Override
		    public void onClick(View v) {
			Intent shareTop = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
			String url = YKManager.getFour() + "user/signlist?1=1";
			shareTop.putExtra("probation_detail_url", url);
			shareTop.putExtra(YKWebViewActivity.URL_INTENT_TILTE, "赚优币");
			startActivityForResult(shareTop, REQUEST_CODE);

		    }
		});
		mUExchangeTextView.setVisibility(View.VISIBLE);
		mShareLayout.setVisibility(View.INVISIBLE);
		break;
	    }
	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.yk_webview_activity);

	getUrl();
	init();
	initView();

	// TrackManager.getInstance().addTrack(
	// TrackUrl.PAGE_OPEN + "YKWebViewActivity");
    }

    private void initView() {

	mWebView.addJavaScriptInterface(this,"fujunComment");

	mWebView.setURIHandler(new YKURIHandler() {

	    @Override
	    public boolean handleURI(String url) {
		System.out.println("probation detail activity url = " + url);
		String webAdver = YKSharelUtil.webAdvertmUrl(YKWebViewActivity.this, url);
		String webpage = YKSharelUtil.tryToGetWebPagemUrl(YKWebViewActivity.this, url);
		if (!TextUtils.isEmpty(webpage)) {
		    Uri uri = Uri.parse(url);
		    if (!TextUtils.isEmpty(uri.getQueryParameter("url"))) {
			String mWebPager = uri.getQueryParameter("url");
			Intent newActivity = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
			newActivity.putExtra("productdetalis", mWebPager);
			newActivity.putExtra("identification", "html");
			startActivity(newActivity);
			return true;
		    }
		}

		String report = YKSharelUtil.tryToReportmUrl(YKWebViewActivity.this, url);
		if (report != null) {
		    setupProduct(url);
		    if (mProduct != null) {
			Intent intent = new Intent(YKWebViewActivity.this, CommitReportActivity.class);
			intent.putExtra("trial_probation_id", mProduct.getmId());
			intent.putExtra("trial_probation_image_url", mProduct.getImage());
			intent.putExtra("trial_probation_name", mProduct.getTitle());
			intent.putExtra("is_trial_product", false);
			startActivity(intent);
			return true;
		    }
		}

		String commentList = YKSharelUtil.tryToCommenListtmUrl(YKWebViewActivity.this, url);
		if (commentList != null) {
		    Intent intent = new Intent(YKWebViewActivity.this, CommentActivity.class);
		    Uri uri = Uri.parse(url);
		    mCommentID = uri.getQueryParameter("commentId");
		    mClassType = uri.getQueryParameter("type");
		    intent.putExtra("commentid", mCommentID);
		    intent.putExtra("type", mClassType);
		    if (mClassType.equals("1")) {
			startActivity(intent);
		    } else {
			startActivityForResult(intent, 55);
		    }
		    return true;
		}

		String commentImage = YKSharelUtil.commentImageUrl(YKWebViewActivity.this, url);
		if (commentImage != null) {
		    Intent intent = new Intent(YKWebViewActivity.this, ShowBigImageActivity.class);
		    Uri uri = Uri.parse(url);
		    String imageUrl = uri.getQueryParameter("imageurl");
		    intent.putExtra("imagePath", imageUrl);
		    startActivity(intent);
		    return true;
		}

		String tryLogin = YKSharelUtil.tryTotryLoginmUrl(YKWebViewActivity.this, url);
		if (tryLogin != null || url.equals("fujun://fujunaction.com/login")) {
		    isLogin = true;
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			String tmpurl = mWebView.getCurrentURL();
			tmpurl = setupTryURL(tmpurl);
			mWebView.loadUrl(YKWebViewActivity.this, tmpurl);
		    } else {
			YKActivityManager.getInstance().showLogin(YKWebViewActivity.this, new ILoginCallback() {

			    @Override
			    public void loginCallback(YKResult result, YKUser user) {
				if (result.isSucceeded()) {
				    String tmpurl = mWebView.getCurrentURL();
				    tmpurl = setupTryURL(tmpurl);
				    mWebView.loadUrl(YKWebViewActivity.this, tmpurl);
				    isClearHistory = true;
				    isLogin = false;
				}
			    }
			});
		    }
		    return true;
		}

		String experienceWeb = YKSharelUtil.experiencemUrl(YKWebViewActivity.this, url);
		if (experienceWeb != null) {
		    Intent intent = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
		    Uri uri = Uri.parse(url);
		    mExperienceUrl = uri.getQueryParameter("url");
		    intent.putExtra("experienceurl", mExperienceUrl);
		    startActivity(intent);
		    return true;
		}

		String tryWeb = YKSharelUtil.tryWeb(YKWebViewActivity.this, url);
		if (tryWeb != null) {
		    Uri uri = Uri.parse(url);
		    Intent intent = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
		    String newUrl  = tryWeb.substring(4, tryWeb.length());
		    String id = uri.getQueryParameter("try_id");
		    intent.putExtra("istrial_product", true);
		    intent.putExtra("track_type", "trial");
		    intent.putExtra("track_type_id", id);
		    intent.putExtra("probation_detail_url", newUrl);
		    startActivity(intent);
		    return true;
		}

		String topicWeb = YKSharelUtil.productwebUrl(YKWebViewActivity.this, url);
		if (topicWeb != null) {
		    Intent intent = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
		    Uri uri = Uri.parse(url);
		    intent.putExtra("productdetalis", url);
		    startActivity(intent);
		    return true;
		}

		String productWeb = YKSharelUtil.ProWeb(YKWebViewActivity.this, url);
		if (productWeb != null) {
		    Intent intent = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
		    Uri uri = Uri.parse(url);
		    mProductUrl = uri.getQueryParameter("url");
		    intent.putExtra("productdetalis", mProductUrl);
		    startActivity(intent);
		    return true;
		}

		String share = YKSharelUtil.tryToGetShareFormUrl(YKWebViewActivity.this, url);
		if (share != null) {
		    setupShareInfo(url);
		    Uri uri = Uri.parse(url);
		    if (!TextUtils.isEmpty(uri.getQueryParameter("platform"))) {
			String type = uri.getQueryParameter("platform");
			if (type.equals("sinaweibo") || type.equals("sinawei")) {
			    if (mShareInfo != null) {
				ShareSinaManager.getInstance().shareSina("肤君分享", mShareInfo.getmTitle(),
					mShareInfo.getmUrl(), mShareInfo.getmPic(), YKWebViewActivity.this);
			    } else {
				ShareSinaManager.getInstance().shareSina("肤君分享", "肤君分享", "",
					"//" + mShareInfo.getmUrl(), YKWebViewActivity.this);
			    }
			} else if (type.equals("weixin_timeline")) {
			    if (mShareInfo != null) {
				ShareWxManager.getInstance().shareWxCircle(mShareInfo.getmTitle(),
					mShareInfo.getmSummary(), mShareInfo.getmUrl(), mShareInfo.getmPic());
			    }
			} else if (type.equals("weixin_session")) {
			    if (mShareInfo != null) {
				ShareWxManager.getInstance().shareWx(mShareInfo.getmTitle(), mShareInfo.getmSummary(),
					mShareInfo.getmUrl(), mShareInfo.getmPic(), YKWebViewActivity.this);
			    }
			} else if (type.equals("qzone")) {
			    if (mShareInfo != null) {
				ShareQzoneManager.getInstance().shareQzone(mShareInfo.getmTitle(),
					mShareInfo.getmSummary(), mShareInfo.getmUrl(), mShareInfo.getmPic(),
					YKWebViewActivity.this);
			    }
			}
		    }
		    return true;
		} else if (url.equals("fujun://fujunaction.com/share")) {
		    mWebView.loadUrl(YKWebViewActivity.this, "javascript:shareDataAndroidFun()");
		    showSharePop();
		    return true;
		} else if (url.contains("news/document")) {
		    mTrackType = "information";
		    setTrackId(url);
		    if (url.contains("news/document")) {
			if (YKCurrentUserManager.getInstance().isLogin()) {
			    getExperienceInfourID(url);
			} else {
			    addUrl(url);
			}
			// 显示
			isGoneorVisible = true;
			// checkPointState();
		    } else {
			Uri uriUrl = Uri.parse(url);
			String locat = uriUrl.getQueryParameter("url");
			if (!TextUtils.isEmpty(locat) && locat.contains("news/document")) {
			    if (YKCurrentUserManager.getInstance().isLogin()) {
				getExperienceInfourID(locat);
			    } else {
				addUrl(locat);
			    }
			    isGoneorVisible = true;
			    // checkPointState();
			} else {
			    mUrl = url;
			}
		    }
		    return false;
		} else if (url.contains("product/comment")) {
		    mTrackType = "experience";
		    setTrackId(url);
		    Intent a = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
		    a.putExtra("experienceurl", url);
		    startActivity(a);
		    return true;
		} else if (url.contains("cosmetics/show")) {
		    mTrackType = "product";
		    setTrackId(url);
		    return false;
		} else if (url.contains("try.yoka.com")) {
		    isTrial = true;
		    return false;
		}else if (url.contains("fujun://fujunaction.com/userhome")) {
		    YKWebViewActivity.this.setResult(RESULT_OK);
		    finish();
		    return true;
		} else if (url.contains("fujun://fujunaction.com/publishexperience")) {
		    Intent writeIntent = new Intent(YKWebViewActivity.this, WriteExperienceActivity.class);
		    startActivity(writeIntent);
		    return true;
		} else if (url.contains("fujun://fujunaction.com/editpersonalinfo")) {
		    Intent writeIntent = new Intent(YKWebViewActivity.this, UpdateUserInfoActivity.class);
		    startActivity(writeIntent);
		    return true;
		} else if (url.contains("fujun://fujunaction.com/cosmetic")) {
		    Intent intent = new Intent(YKWebViewActivity.this, MainActivity.class);
		    intent.putExtra("tab", "3");
		    startActivity(intent);
		    return true;
		} else if (url.contains("fujun://fujunaction.com/topiclike")) {
		    Uri uriUrl = Uri.parse(url);
		    mShowSpID = uriUrl.getQueryParameter("commentId");
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			String uid = YKCurrentUserManager.getInstance().getUser().getId();
			YKEnquiryCheckAddManagers.getInstance().postYKEnquiryPointAdd(new EnquiryPointAddCallback() {
			    @Override
			    public void callback(YKResult result) {
				if (result.isSucceeded()) {
				    mWebView.loadUrl(mContext, "javascript:changeLike()");
				    Toast.makeText(YKWebViewActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
				}
			    }
			}, mShowSpID, uid);
		    }
		    return true;
		} else if (url.contains("fujun://fujunaction.com/signlist")) {
		    Intent shareTop = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
		    String mUrl = YKManager.getFour() + "user/signlist?1=1";
		    shareTop.putExtra("probation_detail_url", mUrl);
		    shareTop.putExtra(YKWebViewActivity.URL_INTENT_TILTE, "赚优币");
		    startActivityForResult(shareTop, REQUEST_CODE);
		    return true;
		} else if (!TextUtils.isEmpty(webAdver)) {

		    int select = webAdver.indexOf("=");
		    String newWebAdver = webAdver.substring(select + 1, webAdver.length());
		    Intent intent = new Intent(mContext, YKWebViewActivity.class);
		    intent.putExtra("add_http", newWebAdver);
		    startActivity(intent);
		    return true;
		} else {
		    mUrl = url;
		}
		return false;
	    }
	});
	if (addurlString != null) {
	    mWebView.setURIHandler(new YKURIHandler() {

		@Override
		public boolean handleURI(String url) {
		    mWebView.loadUrl(mContext, url);
		    return true;
		}
	    });
	}
	setTitle();
    }

    private String setupTryURL(String url) {
	Uri uri = Uri.parse(url);
	if (TextUtils.isEmpty(uri.getQuery())) {
	    url += "?";
	} else {
	    url += "&";
	}
	if (YKCurrentUserManager.getInstance().isLogin()) {
	    url += "auth=" + YKCurrentUserManager.getInstance().getUser().getAuth();
	}
	return url;
    }

    // 设置网页Title
    private void setTitle() {
	String mTitleString = getIntent().getStringExtra(URL_INTENT_TILTE);
	if (!TextUtils.isEmpty(mTitleString)) {
	    mTitle.setText(mTitleString);
	    mShareLayout.setVisibility(View.INVISIBLE);
	}
	mWebView.setTitleHandler(new YKUrlTitle() {

	    @Override
	    public String getTitle(String title) {
		if (!TextUtils.isEmpty(title)) {
		    if (title.equals("我的皮肤属性") || title.equals("肌肤测试")) {
			mHandler.sendEmptyMessage(0);
			mTitle.setText("肌肤测试结果");
		    } else if (title.equals("签到规则") || title.equals("赚优币") || title.equals("兑换记录")) {
			mTitle.setText(title);
			mHandler.sendEmptyMessage(2);
		    } else if (title.equals("优币商城")) {
			mTitle.setText(title);
			mHandler.sendEmptyMessage(3);
		    } else {
			mTitle.setText(title);
			mHandler.sendEmptyMessage(1);
		    }
		}
		return title;
	    }
	});
    }

    private void setTrackId(String url) {
	if (url != null) {
	    Uri uri = Uri.parse(url);
	    if (!TextUtils.isEmpty(uri.getQueryParameter("id"))) {
		mTrackTypeId = uri.getQueryParameter("id");
	    }
	}
    }

    private void setupProduct(String url) {
	if (url != null) {
	    Uri uri = Uri.parse(url);
	    mProduct = new YKProductReport();
	    if (!TextUtils.isEmpty(uri.getQueryParameter("product_id"))) {
		String id = uri.getQueryParameter("product_id");
		mProduct.setmId(id);
	    }
	    if (!TextUtils.isEmpty(uri.getQueryParameter("image"))) {
		String imgUrl = uri.getQueryParameter("image");
		mProduct.setImage(imgUrl);
	    }
	    if (!TextUtils.isEmpty(uri.getQueryParameter("title"))) {
		String title = uri.getQueryParameter("title");
		mProduct.setTitle(title);
	    }
	}
    }

    private void setupShareInfo(String url) {
	if (url != null) {
	    Uri uri = Uri.parse(url);
	    mShareInfo = new YKShareInfo();
	    // 任务id或者文章id
	    if (!TextUtils.isEmpty(uri.getQueryParameter("try_id"))) {
		String tryId = uri.getQueryParameter("try_id");
		mShareInfo.setmTryId(tryId);
	    }
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

    private void getUrl() {
	String reportUrl = getIntent().getStringExtra("report_url");
	String probationUrl = getIntent().getStringExtra("probation_detail_url");
	String url = getIntent().getStringExtra("url");
	addurlString = getIntent().getStringExtra("add_http");
	mWriteUrl = getIntent().getStringExtra("writeurl");
	mProductUrl = getIntent().getStringExtra("productdetalis");
	String afternoonUrl = getIntent().getStringExtra("afternoon_url");
	mTrackType = getIntent().getStringExtra("track_type");
	mTrackTypeId = getIntent().getStringExtra("track_type_id");
	isTrial = getIntent().getBooleanExtra("istrial_product", false);
	isSkinTest = getIntent().getBooleanExtra("report_skin_result", false);
	mExperienceUrl = getIntent().getStringExtra("experienceurl");
	isHomeTop = getIntent().getBooleanExtra("isHomeTop", false);
	isFocusAd = getIntent().getBooleanExtra("isFocusAd", false);
	mFocusImage = (YKFocusImage) getIntent().getSerializableExtra("focusImage");
	mWebentry = (YKWebentry) getIntent().getSerializableExtra("ykwebentry");
	if (!TextUtils.isEmpty(probationUrl)) {
	    if (probationUrl.contains("news/document")) {
		mClassType = "2";
		if (YKCurrentUserManager.getInstance().isLogin()) {
		    getExperienceInfourID(probationUrl);
		} else {
		    addUrl(probationUrl);
		}
		isGoneorVisible = true;
		// checkPointState();
	    } else {
		addUrl(probationUrl);
	    }
	    // TrackManager.getInstance().addTrack(mUrl+"&refer="+mIdentification+TrackUrl.REFER_START);
	} else if (!TextUtils.isEmpty(reportUrl)) {
	    mUrl = reportUrl;
	} else if (!TextUtils.isEmpty(url)) {
	    if (url.contains("news/document")) {
		mClassType = "1";
		if (YKCurrentUserManager.getInstance().isLogin()) {
		    getExperienceInfourID(url);
		} else {
		    addUrl(url);
		}
		isGoneorVisible = true;
		// checkPointState();
	    } else {
		Uri uriUrl = Uri.parse(url);
		String locat = uriUrl.getQueryParameter("url");
		if (!TextUtils.isEmpty(locat) && locat.contains("news/document")) {
		    mClassType = "2";
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			getExperienceInfourID(locat);
		    } else {
			addUrl(locat);
		    }
		    isGoneorVisible = true;
		    // checkPointState();
		} else if (!TextUtils.isEmpty(locat) && locat.contains("news/showspecial")) {
		    mClassType = "2";
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			getShowSpecialID(locat);
		    } else {
			addUrl(locat);
		    }
		} else if (url.contains("news/showspecial")) {
		    mClassType = "2";
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			getShowSpecialID(url);
		    } else {
			addUrl(url);
		    }
		} else if (!TextUtils.isEmpty(locat) && locat.contains("product/comment")) {
		    mClassType = "2";
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			getExperienceCommentID();
		    } else {
			addUrl(locat);
		    }
		    isGoneorVisible = true;
		    // checkPointComment();
		} else {
		    mUrl = url;
		}
	    }
	    // TrackManager.getInstance().addTrack1(mUrl,TrackUrl.REFER_START,mIdentification);
	    // Toast.makeText(YKWebViewActivity.this, mUrl +
	    // "&refer="+mIdentification + TrackUrl.REFER_START, 10).show();
	} else if (!TextUtils.isEmpty(mProductUrl)) {
	    // String webPage =
	    // YKSharelUtil.tryToGetWebPagemUrl(YKWebViewActivity.this,
	    // mProductUrl);
	    // if(!TextUtils.isEmpty(webPage)){
	    Uri uriUrl = Uri.parse(mProductUrl);
	    String locat = uriUrl.getQueryParameter("url");
	    if (!TextUtils.isEmpty(locat) && locat.contains("news/document")) {
		mClassType = "2";
		if (YKCurrentUserManager.getInstance().isLogin()) {
		    getExperienceInfourID(locat);
		} else {
		    addUrl(locat);
		}
		isGoneorVisible = true;
		// checkPointState();
	    } else {
		// mUrl = mProductUrl;
		addUrl(mProductUrl);
	    }
	    // }
	    // TrackManager.getInstance().addTrack(mUrl+"&refer="+mIdentification+TrackUrl.REFER_START);
	} else if (!TextUtils.isEmpty(afternoonUrl)) {
	    Uri uriUrl = Uri.parse(afternoonUrl);
	    String afterUrl = uriUrl.getQueryParameter("url");
	    if (afterUrl.contains("news/showspecial")) {
		mClassType = "2";
		if (YKCurrentUserManager.getInstance().isLogin()) {
		    getShowSpecialID(afterUrl);
		} else {
		    addUrl(afterUrl);
		}
	    } else if (afterUrl.contains("product/comment")) {
		mClassType = "1";
		if (YKCurrentUserManager.getInstance().isLogin()) {
		    getAftoonCommentID(afterUrl);
		} else {
		    addUrl(afterUrl);
		}
		isGoneorVisible = true;
		// checkPointComment();
	    } else {
		mUrl = afterUrl;
	    }
	    // TrackManager.getInstance().addTrack(mUrl+"&refer="+mIdentification+TrackUrl.REFER_START);
	} else if (!TextUtils.isEmpty(mWriteUrl)) {
	    mClassType = "1";
	    if (YKCurrentUserManager.getInstance().isLogin()) {
		getWriteExperienceCommentID();
	    } else {
		addUrl(mExperienceUrl);
	    }
	    isGoneorVisible = true;
	    // checkPointComment();
	} else if (!TextUtils.isEmpty(mExperienceUrl)) {
	    mClassType = "1";
	    if (YKCurrentUserManager.getInstance().isLogin()) {
		getExperienceCommentID();
	    } else {
		addUrl(mExperienceUrl);
	    }
	    isGoneorVisible = true;
	    // checkPointComment();
	} else if (!TextUtils.isEmpty(addurlString)) {
	    mUrl = addurlString;
	}
    }

    // 登陆获取CommentID
    private void getExperienceCommentID() {
	if(!TextUtils.isEmpty(mExperienceUrl)){
	    addUrl(mExperienceUrl);
	    String newUrl = null;
	    Uri uriUrl = Uri.parse(mExperienceUrl);
	    mCommentID = uriUrl.getQueryParameter("id");
	    if (TextUtils.isEmpty(mCommentID)) {
		newUrl = uriUrl.getQueryParameter("url");
		uriUrl = Uri.parse(newUrl);
		if (uriUrl != null) {
		    mCommentID = uriUrl.getQueryParameter("id");
		}
	    }
	}
    }

    // 登陆撰写心得CommentID
    private void getWriteExperienceCommentID() {
	addUrl(mWriteUrl);
	String newUrl = null;
	Uri uriUrl = Uri.parse(mWriteUrl);
	mCommentID = uriUrl.getQueryParameter("id");
	if (TextUtils.isEmpty(mCommentID)) {
	    newUrl = uriUrl.getQueryParameter("url");
	    if (newUrl != null) {
		uriUrl = Uri.parse(newUrl);
		mCommentID = uriUrl.getQueryParameter("id");
	    }
	}
    }

    // 美丽下午茶登陆心得CommentID
    private void getAftoonCommentID(String afterUrl) {
	addUrl(afterUrl);
	String newUrl = null;
	Uri uriUrl = Uri.parse(afterUrl);
	mCommentID = uriUrl.getQueryParameter("id");
	if (TextUtils.isEmpty(mCommentID)) {
	    newUrl = uriUrl.getQueryParameter("url");
	    uriUrl = Uri.parse(newUrl);
	    if (uriUrl != null) {
		mCommentID = uriUrl.getQueryParameter("id");
	    }
	}
    }

    // 登陆资讯CommentID
    private void getExperienceInfourID(String url) {
	addUrl(url);
	String newUrl = null;
	Uri uriUrl = Uri.parse(url);
	mCommentInfourID = uriUrl.getQueryParameter("id");
	if (TextUtils.isEmpty(mCommentInfourID)) {
	    newUrl = uriUrl.getQueryParameter("url");
	    uriUrl = Uri.parse(newUrl);
	    if (uriUrl != null) {
		mCommentInfourID = uriUrl.getQueryParameter("id");
	    }
	}
    }

    // 登陆专题CommentID
    private void getShowSpecialID(String url) {
	addUrl(url);
	String newUrl = null;
	Uri uriUrl = Uri.parse(url);
	mShowSpID = uriUrl.getQueryParameter("id");
	if (TextUtils.isEmpty(mShowSpID)) {
	    newUrl = uriUrl.getQueryParameter("url");
	    uriUrl = Uri.parse(newUrl);
	    if (uriUrl != null) {
		mShowSpID = uriUrl.getQueryParameter("id");
	    }
	}
    }

    // 获取点赞收藏状态
    private void getPointAvail(final ImageView point, final ImageView comment) {
	YKPonitCommentManagers.getInstance().postYKPonitComment(new PonitCommentCallback() {
	    @Override
	    public void callback(YKResult result, String collect, String avail) {
		if (result.isSucceeded()) {
		    mCollect = collect;
		    mAvail = avail;
		    point.setFocusable(true);
		    comment.setFocusable(true);
		    mSendText.setFocusable(true);
		    if (!TextUtils.isEmpty(mAvail)) {
			if (mAvail.equals("0")) {
			    point.setBackgroundResource(R.drawable.experience_point_gry);
			} else {
			    point.setBackgroundResource(R.drawable.experience_point);
			}
		    } else {
			point.setBackgroundResource(R.drawable.experience_point_gry);
		    }
		    if (!TextUtils.isEmpty(mCollect)) {
			if (mCollect.equals("0")) {
			    comment.setBackgroundResource(R.drawable.experience_comment_gry);
			} else {
			    comment.setBackgroundResource(R.drawable.experience_comment);
			}
		    } else {
			comment.setBackgroundResource(R.drawable.experience_comment_gry);
		    }
		} else {
		    point.setBackgroundResource(R.drawable.experience_point_gry);
		    comment.setBackgroundResource(R.drawable.experience_comment_gry);
		}
	    }
	}, mCommentID, YKCurrentUserManager.getInstance().getUser().getId());
    }

    // 获取点赞状态
    private void getAvailCkeck(final ImageView point) {
	YKEnquiryPointPraiseManagers.getInstance().postYKEnquiryPointPraise(new EnquiryPointPraiseCallback() {

	    @Override
	    public void callback(YKResult result, String avail) {
		if (result.isSucceeded()) {
		    mAvail = avail;
		    if (!TextUtils.isEmpty(mAvail)) {
			if (mAvail.equals("0")) {
			    point.setBackgroundResource(R.drawable.experience_point_gry);
			} else {
			    point.setBackgroundResource(R.drawable.experience_point);
			}
		    } else {
			point.setBackgroundResource(R.drawable.experience_point_gry);
		    }
		} else {
		    point.setBackgroundResource(R.drawable.experience_point_gry);
		}

	    }
	}, YKCurrentUserManager.getInstance().getUser().getId(), mCommentInfourID);

    }

    private void addUrl(String url) {
	if(TextUtils.isEmpty(url)){
	    return;
	}
	if (url.contains("clientid")) {
	    if (YKCurrentUserManager.getInstance().isLogin()) {
		mToken = YKCurrentUserManager.getInstance().getUser().getToken();
		mUrl = url + YKDeviceInfo.getClientID() + "&auth=" + mToken;
	    } else {
		mUrl = url + YKDeviceInfo.getClientID();
	    }
	} else {
	    if (YKCurrentUserManager.getInstance().isLogin()) {
		mToken = YKCurrentUserManager.getInstance().getUser().getToken();
		mUrl = url + "&clientid=" + YKDeviceInfo.getClientID() + "&auth=" + mToken;
	    } else {
		mUrl = url + "&clientid=" + YKDeviceInfo.getClientID();
	    }
	}
    }

    @SuppressWarnings("deprecation")
    private void catchUrl(String url) {
	if (url.contains("fujun://")) {
	    if (url != null) {
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
		// url
		String newUrl = uri.getQueryParameter("url");
		if (isSkinTest || !isHomeTop) {
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			mWebView.loadUrl(this, newUrl + "&clientid=" + YKDeviceInfo.getClientID() + "&auth=" + mToken);
			mUrl = newUrl + "&clientid=" + YKDeviceInfo.getClientID() + "&auth=" + mToken;
		    } else {
			mWebView.loadUrl(this, newUrl + "&clientid=" + YKDeviceInfo.getClientID());
			mUrl = newUrl + "&clientid=" + YKDeviceInfo.getClientID();
		    }
		} else {
		    mWebView.loadUrl(this, newUrl);
		}
	    }
	} else {
	    mWebView.loadUrl(this, url);
	}
    }

    @Override
    public void onBackPressed() {
	if (isTrial) {
	    if (IS_TIRAL_SUCCESS) {
		YKWebViewActivity.this.setResult(TIRAL_RESULT_CODE);
	    } else {
		YKWebViewActivity.this.setResult(RESULT_CODE);
	    }
	    finish();
	} else {
	    if (mWebView != null && mWebView.canGoBack()) {
		mWebView.goBack();
		setTitle();
	    } else {
		YKWebViewActivity.this.setResult(RESULT_CODE);
		finish();
	    }
	}
    }

    // 解决个别机型 webview 返回不刷新 title
    public static void reSetTitle(String title) {
	if (null != mTitle) {
	    mTitle.setText(title);
	}

    }

    private void showSharePop() {
	mPopupWindow = new SharePopupWindow(YKWebViewActivity.this, itemsOnClick);
	mPopupWindow.showAtLocation(YKWebViewActivity.this.findViewById(R.id.probation_detail_share_img),
		Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private String getShareUrl(String url) {
	if (!TextUtils.isEmpty(url)) {
	    Uri uri = Uri.parse(url);
	    if (!TextUtils.isEmpty(uri.getQueryParameter("u"))) {
		return uri.getQueryParameter("u");
	    } else if (!TextUtils.isEmpty(uri.getQueryParameter("url"))) {
		return uri.getQueryParameter("url");
	    } else {
		return url;
	    }
	}
	return "";
    }

    private OnClickListener itemsOnClick = new OnClickListener() {

	public void onClick(View v) {
	    SharePopupWindow.getInstance().dismiss();
	    // String shareDefUrl =
	    // "http://p5.yokacdn.com/pic/div/2016/products/fujun/images/down-logo.png";//70*70
	    String shareDefUrl = "http://ss1.yokacdn.com/cosmetics/fujun/img/400400.jpg";// 400*400
	    switch (v.getId()) {
	    case R.id.popup_friend:
		dismissPopupWindow();
		// 注册微信分享
		// ShareWxManager.getInstance().init(YKWebViewActivity.this,
		// UmSharedAppID.SHARE_WX_APP_ID);
		if (isFocusAd) {
		    if (mFocusImage != null) {
			ShareWxManager.getInstance().shareWxCircle("点击查看详情", "来自肤君分享", mFocusImage.getmUrl(),
				mFocusImage.getmImage().getmURL());
			// TrackManager.getInstance().addTrack(
			// TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
			// + "&type=" + mTrackType
			// + "&platform=wx");
		    }
		} else {
		    if (mInfo != null) {
			ShareWxManager.getInstance().shareWxCircle(mInfo.getmDesc(), mInfo.getmTitle(),
				/* mInfo.getmLink() */mUrl, mInfo.getmImgUrl());
			// TrackManager.getInstance().addTrack(
			// TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
			// + "&type=" + mTrackType
			// + "&platform=wx");
		    } else if (mWebentry != null) {
			ShareWxManager.getInstance().shareWxCircle(mWebentry.getmSubTitle(), mWebentry.getmTitle(),
				/* getShareUrl(mWebentry.getmUrl()) */mUrl, mWebentry.getmCover().getmURL());
		    } else {
			ShareWxManager.getInstance().shareWxCircle("点击查看详情", "来自肤君分享", mUrl, shareDefUrl);
		    }
		}
		break;
	    case R.id.popup_wei:
		dismissPopupWindow();
		if (isFocusAd) {
		    if (mFocusImage != null) {
			ShareWxManager.getInstance().shareWx("点击查看详情", "来自肤君分享", mFocusImage.getmUrl(),
				mFocusImage.getmImage().getmURL(), YKWebViewActivity.this);
			// TrackManager.getInstance().addTrack(
			// TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
			// + "&type=" + mTrackType
			// + "&platform=wx");
		    }
		} else {
		    if (mInfo != null) {
			ShareWxManager.getInstance().shareWx(mInfo.getmDesc(), mInfo.getmTitle(),
				mInfo.getmLink(), mInfo.getmImgUrl(), YKWebViewActivity.this);
			// TrackManager.getInstance().addTrack(
			// TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
			// + "&type=" + mTrackType
			// + "&platform=wx");
			// }
		    } else if (mWebentry != null) {
			ShareWxManager.getInstance().shareWx(mWebentry.getmSubTitle(), mWebentry.getmTitle(),
				/* getShareUrl(mWebentry.getmUrl()) */mUrl, mWebentry.getmCover().getmURL(),
				YKWebViewActivity.this);
		    } else {
			ShareWxManager.getInstance().shareWx("点击查看详情", "来自肤君分享", mUrl, shareDefUrl,
				YKWebViewActivity.this);
		    }
		}
		break;
	    case R.id.popup_sina:
		dismissPopupWindow();
		if (isFocusAd) {
		    if (mFocusImage != null) {
			ShareSinaManager.getInstance().shareSina("肤君分享", mFocusImage.getmTitle(),
				mFocusImage.getmShowUrl() + "@肤君", mFocusImage.getmUrl(), YKWebViewActivity.this);
		    }
		} else {
		    if (null != mInfo) {
			ShareSinaManager.getInstance().shareSina(mInfo.getmDesc(), mInfo.getmTitle() + "@肤君",
				/* mInfo.getmLink() */mInfo.getmLink(), mInfo.getmImgUrl(), YKWebViewActivity.this);

		    } else if (mWebentry != null) {

			if (!TextUtils.isEmpty(mWebentry.getmUrl())) {
			    ShareSinaManager.getInstance().shareSina("@肤君", mWebentry.getmTitle(),
				    mWebentry.getmSubTitle(),
				    "//" + /*
				     * getShareUrl(mWebentry.getmUrl())
				     */mUrl, YKWebViewActivity.this);
			} else {
			    ShareSinaManager.getInstance().shareSina("来自肤君分享", mWebentry.getmTitle(), "@肤君" + mUrl,
				    shareDefUrl, YKWebViewActivity.this);
			}

		    } else {
			ShareSinaManager.getInstance().shareSina("点击查看详情", "来自肤君分享@肤君", mUrl, shareDefUrl,
				YKWebViewActivity.this);
		    }
		}
		break;
	    case R.id.popup_qzone:
		dismissPopupWindow();
		ShareQzoneManager.getInstance().init(YKWebViewActivity.this);
		if (isFocusAd) {
		    if (mFocusImage != null) {
			ShareQzoneManager.getInstance().shareQzone("来自肤君分享", "点击查看详情",
				getShareUrl(mFocusImage.getmUrl()), mFocusImage.getmImage().getmURL(),
				YKWebViewActivity.this);
			// TrackManager.getInstance().addTrack(
			// TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
			// + "&type=" + mTrackType
			// + "&platform=qzone");
		    }
		} else {
		    if (mInfo != null) {
			ShareQzoneManager.getInstance().shareQzone(mInfo.getmTitle(), mInfo.getmDesc(),
				mInfo.getmLink(), mInfo.getmImgUrl(), YKWebViewActivity.this);
			// TrackManager.getInstance().addTrack(
			// TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
			// + "&type=" + mTrackType
			// + "&platform=qzone");
		    } else if (mWebentry != null) {
			ShareQzoneManager.getInstance().shareQzone(mWebentry.getmTitle(), mWebentry.getmSubTitle(),
				/* getShareUrl(mWebentry.getmUrl()) */mUrl, mWebentry.getmCover().getmURL(),
				YKWebViewActivity.this);
		    } else {
			ShareQzoneManager.getInstance().shareQzone("来自肤君分享", "点击查看详情", mUrl, shareDefUrl,
				YKWebViewActivity.this);
		    }
		}
		break;
	    case R.id.popup_qqfriend:
		dismissPopupWindow();
		if (isFocusAd) {
		    if (mFocusImage != null) {
			ShareQQManager.getInstance().shareQQ("来自肤君分享", "点击查看详情", getShareUrl(mFocusImage.getmUrl()),
				mFocusImage.getmImage().getmURL(), YKWebViewActivity.this);
			// TrackManager.getInstance().addTrack(
			// TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
			// + "&type=" + mTrackType
			// + "&platform=qzone");
		    }
		} else {
		    if (mInfo != null) {
			ShareQQManager.getInstance().shareQQ(mInfo.getmTitle(), mInfo.getmDesc(),
				mInfo.getmLink(), mInfo.getmImgUrl(), YKWebViewActivity.this);
			// TrackManager.getInstance().addTrack(
			// TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
			// + "&type=" + mTrackType
			// + "&platform=qzone");
		    } else if (mWebentry != null) {
			ShareQQManager.getInstance().shareQQ(mWebentry.getmTitle(), mWebentry.getmSubTitle(),
				/* getShareUrl(mWebentry.getmUrl()) */mUrl, mWebentry.getmCover().getmURL(),
				YKWebViewActivity.this);

		    } else {
			ShareQQManager.getInstance().shareQQ("点击查看详情", "来自肤君分享", mUrl, shareDefUrl,
				YKWebViewActivity.this);
		    }
		}
		break;
	    case R.id.popup_copyurl:
		ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		// 将文本内容放到系统剪贴板里。
		cm.setText(mUrl);
		Toast.makeText(YKWebViewActivity.this, "复制成功，可以发给朋友们了。", Toast.LENGTH_LONG).show();
		break;
	    }
	}
    };

    private void dismissPopupWindow() {
	if (mPopupWindow != null && mPopupWindow.isShowing()) {
	    mPopupWindow.dismiss();
	}
    }

    // 微博分享和QQ空间分享必须重写此方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (SHOP_CHANGE && ("优币商城").equals(mTitle.getText())) {
	    // 用来刷新优币商城积分
	    SHOP_CHANGE = false;
	    mWebView.reFresh();
	}
	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	    YKWebViewActivity.this.setResult(RESULT_OK);
	    finish();
	}
	switch (requestCode) {
	case 33:
	    getExperienceCommentID();
	    if (YKCurrentUserManager.getInstance().isLogin()) {
		YKActivityManager.getInstance().registerRootActivity(this);
		getPointAvail(point, comment);
	    }
	    break;
	case 44:
	    getExperienceInfourID(mUrl);
	    if (YKCurrentUserManager.getInstance().isLogin()) {
		YKActivityManager.getInstance().registerRootActivity(this);
		getAvailCkeck(point);
	    } else {
		mEditText.clearFocus();
	    }
	    break;
	case 55:
	    if (null != data) {
		if (YKCurrentUserManager.getInstance().isLogin()) {
		    Intent getchange = getIntent();
		    String iSSucce = getchange.getStringExtra("ok");
		    if (!TextUtils.isEmpty(iSSucce)) {
			mWebView.loadUrl(mContext, "javascript:changeComment()");
		    }
		} else {
		    mEditText.clearFocus();

		}
	    }
	    break;
	case 66:// 高级回复返回刷新
	    if (null != data) {
		ArrayList<String> imageList = new ArrayList<String>();
		String textChange = data.getStringExtra("textChange");
		imageList = data.getStringArrayListExtra("ListViewImag");
		String url = YKCurrentUserManager.getInstance().getUser().getAvatar().getmHeadUrl();
		String name = YKCurrentUserManager.getInstance().getUser().getName();
		Long time = System.currentTimeMillis();
		String newTime = TimeUtil.forTimeForYearMonthDays(time);
		if (TextUtils.isEmpty(textChange)) {
		    textChange = "";
		} else if (imageList.size() < 0) {
		    imageList.add(url);
		    imageList.add(url);
		    imageList.add(url);
		}
		String imageStr = "";
		switch (imageList.size()) {
		case 0:
		    imageStr = "";
		    break;
		case 1:
		    imageStr = "'" + imageList.get(0) + "'";
		    break;
		case 2:
		    imageStr = "'" + imageList.get(0) + "','" + imageList.get(1) + "'";
		    break;
		case 3:
		    imageStr = "'" + imageList.get(0) + "','" + imageList.get(1) + "','" + imageList.get(2) + "'";
		    break;

		default:
		    break;
		}

		mWebView.loadUrl(mContext, "javascript:appendComment('" + url + "','" + name + "','" + newTime + "','"
			+ textChange + "',[" + imageStr + "])");
	    }
	    break;
	}

	if (requestCode == FILECHOOSER_RESULTCODE) {
	    if (requestCode == FILECHOOSER_RESULTCODE) {
		if (null == mUploadMessage)
		    return;
		Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
		mUploadMessage.onReceiveValue(result);
		mUploadMessage = null;
	    }
	}
	UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageStart() {
	try {
	    if (mCustomButterfly == null) {
		mCustomButterfly = CustomButterfly.show(this);
	    }
	} catch (Exception e) {
	    mCustomButterfly = null;
	}
    }

    @Override
    public void onPageFinished() {
	if (!TextUtils.isEmpty(mTrackType) && !TextUtils.isEmpty(mTrackTypeId)) {
	    // TrackManager.getInstance().addTrack(
	    // TrackUrl.ITEM_DISPLAY + mTrackTypeId + "&type="
	    // + mTrackType);
	}
	AppUtil.dismissDialogSafe(mCustomButterfly);
	mCustomButterfly = null;
	if (isClearHistory) {
	    mWebView.clearHistory();
	    isClearHistory = false;
	}
	setTitle();

    }

    @Override
    public void onRecevicedError() {
	AppUtil.dismissDialogSafe(mCustomButterfly);
    }

    @Override
    public void onLoadResource() {
	AppUtil.dismissDialogSafe(mCustomButterfly);
    }

    public static class getTiralInfo {
	@JavascriptInterface
	public void getTiralState(boolean state) {
	    IS_TIRAL_SUCCESS = state;
	}
    }

    public static class getShareInfo {
	@JavascriptInterface
	public void getShare(String shareInfo) {
	    mInfo = new YKProductShareInfo();
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
		mInfo.setmImgUrl(imgUrl);
	    }
	    if (!TextUtils.isEmpty(title)) {
		mInfo.setmTitle(title);
	    }
	    if (!TextUtils.isEmpty(desc)) {
		mInfo.setmDesc(desc);
	    }
	    if (!TextUtils.isEmpty(link)) {
		mInfo.setmLink(link);
	    }
	}
    }

    /**
     * 回复再回复 点赞 删除 举报
     */
    @JavascriptInterface
    public void commentlike(String data) {
	if(!AppUtil.isNetworkAvailable(mContext)){
	    Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
	    return;
	}
	if(!YKCurrentUserManager.getInstance().isLogin()){
	    Intent login  = new Intent(mContext,LoginActivity.class);
	    startActivity(login);
	    return;
	}
	mReplytoID = null;
	try {
	    JSONObject json = new JSONObject(data);
	    mReplytoID = json.optString("replytoID");
	} catch (JSONException e) {
	    e.printStackTrace();
	}

	YKThumbupManager.getInstance().postYKThumbup(new ThumbupCallback() {

	    @Override
	    public void callback(YKResult result) {
		if (result.isSucceeded()) {
		    mWebView.post(new Runnable() {
			@Override
			public void run() {
			    mWebView.loadUrl(mContext, "javascript:zanCallback()");
			}
		    });
		}
	    }
	}, Integer.valueOf(mClassType), YKCurrentUserManager.getInstance().getUser().getToken(),
	Integer.valueOf(mReplytoID));
    }

    @JavascriptInterface
    public void reportcomment() {
	reportDial();
    }

    YKWebComment commentWeb;

    @JavascriptInterface
    public void deleteCommentItem(String data) {
	if(!AppUtil.isNetworkAvailable(mContext)){
	    Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
	    return;
	}
	if(!YKCurrentUserManager.getInstance().isLogin()){
	    Intent login  = new Intent(mContext,LoginActivity.class);
	    startActivity(login);
	    return;
	}
	if(commentWeb==null){
	    commentWeb = new YKWebComment();
	}
	try {
	    JSONObject json = new JSONObject(data);
	    commentWeb.setmReplyID(json.optString("replytoID"));
	} catch (JSONException e) {
	    e.printStackTrace();
	}
	boolean isNumber = AppUtil.isNumeric(commentWeb.getmReplyID());
	if (isNumber) {
	    deleteAllComment(commentWeb);
	}
    }

    @JavascriptInterface
    public void commentreply(String data) {
	if(!AppUtil.isNetworkAvailable(mContext)){
	    Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
	    return;
	}
	if(!YKCurrentUserManager.getInstance().isLogin()){
	    Intent login  = new Intent(mContext,LoginActivity.class);
	    startActivity(login);
	    return;
	}
	if(commentWeb==null){
	    commentWeb = new YKWebComment();
	}
	try {
	    JSONObject json = new JSONObject(data);
	    commentWeb.setmReplytoID(json.optString("replytoID"));// 评论id
	    commentWeb.setmReplytousername(json.optString("replytousername"));// 被回复者昵称
	    commentWeb.setmReplytoID(json.optString("replytoID"));
	    commentWeb.setmReplyID(json.optString("replyID"));
	    commentWeb.setmReplytouserid(json.optString("replytouserid"));
	    commentWeb.setmReplytousername(json.optString("replytousername"));
	} catch (JSONException e) {
	    e.printStackTrace();
	}
	//这里通过判断返回的数据结构有没有replyID区分是一级回复还是二级,一级没有replyID
	if (YKCurrentUserManager.getInstance().getUser().getName()
		.equals(commentWeb.getmReplytousername())
		&&!TextUtils.isEmpty(commentWeb.getmReplyID())) {

	    if(!AppUtil.isNetworkAvailable(mContext)){
		Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
		return;
	    }
	    if(!YKCurrentUserManager.getInstance().isLogin()){
		Intent login  = new Intent(mContext,LoginActivity.class);
		startActivity(login);
		return;
	    }
	    new AlertDialog.Builder(mContext).setTitle("确认删除").setIcon(android.R.drawable.ic_dialog_info)
	    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
		    YKDeleteInformationAndTipsManager.getInstance()
		    .postDeleteYKInformationAndTips(new deleteYKInformationAndTipsCallback() {

			@Override
			public void callback(YKResult result) {
			    // TODO Auto-generated method
			    // stub
			    if (result.isSucceeded()) {
				mWebView.post(new Runnable() {
				    @Override
				    public void run() {
					mEditText.clearFocus();
					YKUtil.hideKeyBoard(mContext, mEditText);
					mWebView.loadUrl(mContext, "javascript:deleteCallback2()");
				    }
				});
				Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
			    } else {
				Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
			    }
			}
		    }, Integer.valueOf(mClassType),
		    YKCurrentUserManager.getInstance().getUser().getToken(),
		    Integer.valueOf(commentWeb.getmReplytoID()), commentWeb.getmReplyID());
		}
	    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
		    // 点击“返回”后的操作,这里不设置没有任何操作
		}
	    }).show();
	}else{
	    mHandler.postDelayed(new Runnable() {  
		public void run() {  
		    mEditText.setHint("回复" + commentWeb.getmReplytousername() + ":");
		    mEditText.setFocusable(true);
		    mEditText.requestFocus();
		    YKUtil.showKeyBoard(mContext, mEditText);
		}  
	    }, 100); 
	}  
    }

    // 举报弹出的对话框
    private  void reportDial() {
	new AlertDialog.Builder(mContext).setTitle("确认举报").setIcon(android.R.drawable.ic_dialog_info)
	.setPositiveButton("举报", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		mEditText.clearFocus();
		YKUtil.hideKeyBoard(mContext, mEditText);
		Toast.makeText(mContext, "举报成功，我们会及时处理！！！", Toast.LENGTH_SHORT).show();
	    }
	}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		// 点击“返回”后的操作,这里不设置没有任何操作
	    }
	}).show();
    }

    // 删除评论
    private  void deleteAllComment(final YKWebComment commentWeb) {
	mEditText.clearFocus();
	YKUtil.hideKeyBoard(mContext, mEditText);
	new AlertDialog.Builder(mContext).setTitle("确认删除").setIcon(android.R.drawable.ic_dialog_info)
	.setPositiveButton("删除", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		YKDeleteInformationAndTipsAllManager.getInstance()
		.postDeleteAllYKInformationAndTips(new deleteAllYKInformationAndTipsCallback() {

		    @Override
		    public void callback(YKResult result) {
			// TODO Auto-generated method stub
			if (result.isSucceeded()) {
			    mWebView.post(new Runnable() {
				@Override
				public void run() {
				    mWebView.loadUrl(mContext, "javascript:deleteCallback()");
				}
			    });
			}

		    }
		}, Integer.valueOf(mClassType), YKCurrentUserManager.getInstance().getUser().getToken(),
		Integer.valueOf(commentWeb.getmReplyID()));
	    }
	}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		// 点击“返回”后的操作,这里不设置没有任何操作
	    }
	}).show();

    }

    private  void TipsNew(int iSCommentOrTips, String authtoken, int replytoID, String replyID, String content,
	    String replytouserid, String replytousername) {
	YKaddInformationAndTipsManager.getInstance().postYKInformationAndTips(new AddYKInformationAndTipsCallback() {
	    //			{"message":"","result":{"replytoreplylist":{"id":"554","content":"Pppppp",
	    //					"replytouserinfo":[],"userinfo":{"userid":"3358682","username":"marym"}}},"code":0}
	    @Override
	    public void callback(YKResult result,final YKComment resultobject) {
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if (result.isSucceeded()) {
		    mEditText.clearFocus();
		    YKUtil.hideKeyBoard(mContext, mEditText);
		    mWebView.post(new Runnable() {
			@Override
			public void run() {
			    JSONObject object = new JSONObject();
			    String	 replyID = resultobject.getID();
			    try {
				if (mClassType.equals("1")) {
				    object.put("replytoID", commentWeb.getmReplytoID());
				    object.put("replyID", replyID);
				    object.put("replytousername", mReplytousername);
				    object.put("replytouserid", "");
				    object.put("replytousername", "");
				    object.put("content", mEditText.getText().toString().trim());
				    object.put("replyuserid", YKCurrentUserManager.getInstance().getUser().getId());
				    object.put("replyusername", YKCurrentUserManager.getInstance().getUser().getName());
				} else {
				    object.put("replytoID", commentWeb.getmReplytoID());
				    object.put("replyuserid",YKCurrentUserManager.getInstance().getUser().getId() );
				    object.put("replyID", replyID);
				    object.put("replytousername", "");
				    object.put("replytouserid", mReplytouserid);
				    object.put("replytousername", mReplytousername);
				    object.put("content", mEditText.getText().toString().trim());
				    object.put("replyusername",YKCurrentUserManager.getInstance().getUser().getName() );
				}

			    } catch (JSONException e) {
				e.printStackTrace();
			    }
			    mWebView.loadUrl(mContext, "javascript:commentCallback('" + object + "')");
			    mEditText.setText("");
			    mEditText.setHint("发表评论");
			    commentWeb = null;
			    Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
			}
		    });

		} else {

		    String message = (String) result.getMessage();
		    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}

	    }
	}, iSCommentOrTips, authtoken, replytoID, "", content, replytouserid, replytousername);
    }

    private void init() {
	mContext = YKWebViewActivity.this;
	activityRootView = findViewById(R.id.rootView);
	mTitle = (TextView) findViewById(R.id.probation_detail_title);
	mWebView = (PersonalWebView) findViewById(R.id.probation_detail_webview);
	mWebView.setLoadStateListener(this);
	mWebView.setWebViewInputListener(this);
	mShareLayout = findViewById(R.id.probation_detail_share_layout);
	mBottomLayout = (RelativeLayout) findViewById(R.id.probation_bottom_layout);
	mUExchangeTextView = (TextView) findViewById(R.id.uExchangeTextView);
	mUExchangeTextView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent shareTop = new Intent(YKWebViewActivity.this, YKWebViewActivity.class);
		String url = YKManager.getFour() + "mall/myrecord?1=1";
		shareTop.putExtra("probation_detail_url", url);
		shareTop.putExtra(YKWebViewActivity.URL_INTENT_TILTE, "兑换记录");
		startActivity(shareTop);
	    }
	});
	mShareLayout.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showSharePop();
		mWebView.loadUrl(YKWebViewActivity.this, "javascript:shareDataAndroidFun()");
	    }
	});
	mBackLayout = findViewById(R.id.probation_detail_back_layout);
	mBackLayout.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (isTrial) {
		    if (IS_TIRAL_SUCCESS) {
			YKWebViewActivity.this.setResult(TIRAL_RESULT_CODE);
		    } else {
			YKWebViewActivity.this.setResult(RESULT_CODE);
		    }
		    finish();
		} else {
		    if (mWebView != null && mWebView.canGoBack()) {
			mWebView.goBack();

		    } else {
			YKWebViewActivity.this.setResult(RESULT_CODE);
			finish();
		    }
		}
	    }
	});

	mDoneLayout = findViewById(R.id.probation_detail_done_layout);
	mDoneLayout.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		YKWebViewActivity.this.setResult(RESULT_CODE);
		finish();
	    }
	});
	if (!TextUtils.isEmpty(mUrl)) {
	    catchUrl(mUrl);
	}

	if (isGoneorVisible) {
	    mBottomLayout.setVisibility(View.VISIBLE);
	} else {
	    mBottomLayout.setVisibility(View.GONE);
	}

	//获取屏幕高度  
	screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();  
	//阀值设置为屏幕高度的1/3  
	keyHeight = screenHeight/3;

	// 底部添加的不布局
	// 心得 资讯
	mEditText = (EditText) findViewById(R.id.enquiry_edit);
	mAeniorImage = (ImageView) findViewById(R.id.enquiry_image);
	mSendText = (TextView) findViewById(R.id.enquiry_send);

	mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
	    @Override
	    public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {// 获得焦点
		    if (YKCurrentUserManager.getInstance().isLogin()) {
		    } else {
			Intent login = new Intent(YKWebViewActivity.this, LoginActivity.class);
			if (mClassType.equals("1")) {
			    startActivity(login);
			}else if(mClassType.equals("2")) {
			    startActivityForResult(login, 44);
			    mEditText.clearFocus();
			}

		    }
		}
	    }
	});

	mSendText.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (YKCurrentUserManager.getInstance().isLogin()) {
		    String tokin = YKCurrentUserManager.getInstance().getUser().getToken();
		    final String context = mEditText.getText().toString().trim();
		    if (TextUtils.isEmpty(context)) {
			Toast.makeText(YKWebViewActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
		    } else {
			if(commentWeb==null){
			    if(mClassType.equals("1")){
				YKCommentreplyManager.getInstance().postCommentReply(tokin, mCommentID, context, null,
					new IDReplyCallback() {
				    @Override
				    public void callback(YKResult result, String replyID) {
					if (result.isSucceeded()) {
					    mEditText.setText("");
					    mEditText.clearFocus();
					    String url = YKCurrentUserManager.getInstance().getUser().getAvatar()
						    .getmHeadUrl();
					    String name = YKCurrentUserManager.getInstance().getUser().getName();
					    Long time = System.currentTimeMillis();
					    String newTime = TimeUtil.forTimeForYearMonthDays(time);
					    mWebView.loadUrl(mContext,
						    "javascript:appendComment('" + url + "','" + name + "','"
							    + newTime + "','" + context + "',[" + "" + "],'"
							    + replyID + "')");

					    Toast.makeText(YKWebViewActivity.this, "评论成功", Toast.LENGTH_SHORT)
					    .show();
					    commentWeb = null;
					} else {
					    String message = (String) result.getMessage();
					    Toast.makeText(YKWebViewActivity.this, message, Toast.LENGTH_SHORT)
					    .show();
					}
					YKUtil.hideKeyBoard(mContext, mEditText);
				    }
				});
			    }else{
				YKCommentreplyManager.getInstance().postTopicReply(tokin, mCommentInfourID, context, null,
					new IDReplyCallback() {
				    @Override
				    public void callback(YKResult result, String replyID) {
					if (result.isSucceeded()) {
					    mEditText.setText("");
					    mEditText.clearFocus();
					    String url = YKCurrentUserManager.getInstance().getUser().getAvatar()
						    .getmHeadUrl();
					    String name = YKCurrentUserManager.getInstance().getUser().getName();
					    Long time = System.currentTimeMillis();
					    String newTime = TimeUtil.forTimeForYearMonthDays(time);
					    mWebView.loadUrl(mContext,
						    "javascript:appendComment('" + url + "','" + name + "','"
							    + newTime + "','" + context + "',[" + "" + "],'"
							    + replyID + "')");

					    Toast.makeText(YKWebViewActivity.this, "评论成功", Toast.LENGTH_SHORT)
					    .show();
					    commentWeb = null;
					} else {
					    String message = (String) result.getMessage();
					    Toast.makeText(YKWebViewActivity.this, message, Toast.LENGTH_SHORT)
					    .show();
					}
					YKUtil.hideKeyBoard(mContext, mEditText);
				    }
				});
			    }

			}else{
			    if (TextUtils.isEmpty(commentWeb.getmReplyID())
				    &&!TextUtils.isEmpty(commentWeb.getmReplytoID())
				    && !TextUtils.isEmpty(commentWeb.getmReplytousername())) {
				mReplytoID = commentWeb.getmReplytoID();
				mReplytousername = commentWeb.getmReplytousername();
				TipsNew(Integer.valueOf(mClassType),
					YKCurrentUserManager.getInstance().getUser().getToken(),
					Integer.valueOf(commentWeb.getmReplytoID()),null, context, null, null);
			    } else if (!TextUtils.isEmpty(commentWeb.getmReplyID())
				    && !TextUtils.isEmpty(commentWeb.getmReplytouserid())) {
				mReplytoID = commentWeb.getmReplytoID();
				mReplytousername = commentWeb.getmReplytousername();
				mReplyID = commentWeb.getmReplyID();
				mReplytouserid = commentWeb.getmReplytouserid();
				TipsNew(Integer.valueOf(mClassType),
					YKCurrentUserManager.getInstance().getUser().getToken(),
					Integer.valueOf(commentWeb.getmReplytoID()), mReplyID, context, mReplytouserid,
					mReplytousername);
			    }
			}
		    }
		} else {
		    Intent login = new Intent(YKWebViewActivity.this, LoginActivity.class);
		    startActivityForResult(login, 33);
		    point.setFocusable(false);
		    comment.setFocusable(false);
		    mSendText.setFocusable(false);
		}
	    }
	});
	mLinear = (LinearLayout) findViewById(R.id.experience_ponitandcomment);
	point = (ImageView) findViewById(R.id.experience_ponit);
	comment = (ImageView) findViewById(R.id.experience_comment);
	if(TextUtils.isEmpty(mClassType)){
	    return;
	}
	if (mClassType.equals("1")) {
	    comment.setVisibility(View.VISIBLE);
	    point.setVisibility(View.VISIBLE);
	    if (YKCurrentUserManager.getInstance().isLogin()) {
		getPointAvail(point, comment);
	    } else {
		comment.setBackgroundResource(R.drawable.experience_comment_gry);
		point.setBackgroundResource(R.drawable.experience_point_gry);
	    }

	    comment.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			final String type;
			final String uid = YKCurrentUserManager.getInstance().getUser().getId();
			if (mCollect != null) {
			    if (mCollect.equals("0")) {
				type = "1";
			    } else {
				type = "2";
			    }
			    YKCollectManagers.getInstance().postYKCollect(new ColectCallback() {
				@Override
				public void callback(YKResult result) {
				    if (result.isSucceeded()) {
					if (type.equals("1")) {
					    mCollect = "1";
					    comment.setBackgroundResource(R.drawable.experience_comment);
					    mWebView.loadUrl(mContext, "javascript:addCollectionState()");
					    Toast.makeText(YKWebViewActivity.this, "收藏成功", Toast.LENGTH_LONG).show();
					} else {
					    mCollect = "0";
					    comment.setBackgroundResource(R.drawable.experience_comment_gry);
					    mWebView.loadUrl(mContext, "javascript:CancleCollectState()");
					    Toast.makeText(YKWebViewActivity.this, "取消收藏", Toast.LENGTH_LONG).show();
					}
				    } else {
					String message = (String) result.getMessage();
					Toast.makeText(YKWebViewActivity.this, message, Toast.LENGTH_LONG).show();
				    }
				}
			    }, mCommentID, uid, type);
			}
		    } else {
			Intent login = new Intent(YKWebViewActivity.this, LoginActivity.class);
			startActivityForResult(login, 33);
			point.setFocusable(false);
			comment.setFocusable(false);
			mSendText.setFocusable(false);
		    }
		}
	    });

	    point.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			final String uid = YKCurrentUserManager.getInstance().getUser().getId();
			if (mAvail != null) {
			    if (mAvail.equals("0")) {
				YKAvailManagers.getInstance().postYKAvail(new AvailCallback() {
				    @Override
				    public void callback(YKResult result) {
					if (result.isSucceeded()) {
					    point.setBackgroundResource(R.drawable.experience_point);
					    mAvail = "1";
					    mWebView.loadUrl(mContext, "javascript:setAvailState()");
					    Toast.makeText(YKWebViewActivity.this, "点赞成功", Toast.LENGTH_LONG).show();
					} else {
					    String message = (String) result.getMessage();
					    Toast.makeText(YKWebViewActivity.this, message, Toast.LENGTH_LONG).show();
					}
				    }
				}, mCommentID, uid);
			    }
			} else {
			    comment.setFocusable(false);
			}
		    } else {
			Intent login = new Intent(YKWebViewActivity.this, LoginActivity.class);
			startActivityForResult(login, 33);
			point.setFocusable(false);
			comment.setFocusable(false);
			//			mSendText.setFocusable(false);
		    }
		}
	    });

	    mAeniorImage.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			Intent aenior = new Intent(YKWebViewActivity.this, AeniorReplayActivity2.class);
			aenior.putExtra("commmentID", mCommentID);
			startActivityForResult(aenior, 66);
		    } else {
			Intent login = new Intent(YKWebViewActivity.this, LoginActivity.class);
			startActivity(login);
		    }

		}
	    });
	} else {
	    comment.setVisibility(View.GONE);
	    point.setVisibility(View.VISIBLE);
	    if (YKCurrentUserManager.getInstance().isLogin()) {
		getAvailCkeck(point);
	    } else {
		point.setBackgroundResource(R.drawable.experience_point_gry);
	    }

	    point.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			final String uid = YKCurrentUserManager.getInstance().getUser().getId();
			if (mAvail != null) {
			    if (mAvail.equals("0")) {
				YKEnquiryCheckAddManagers.getInstance()
				.postYKEnquiryPointAdd(new EnquiryPointAddCallback() {
				    @Override
				    public void callback(YKResult result) {
					if (result.isSucceeded()) {
					    point.setBackgroundResource(R.drawable.experience_point);
					    mAvail = "1";
					    mWebView.loadUrl(mContext, "javascript:setAvailState()");
					    Toast.makeText(YKWebViewActivity.this, "点赞成功", Toast.LENGTH_SHORT)
					    .show();
					} else {
					    String message = (String) result.getMessage();
					    Toast.makeText(YKWebViewActivity.this, message, Toast.LENGTH_SHORT)
					    .show();
					}
				    }
				}, mCommentInfourID, uid);
			    }
			}
		    } else {
			Intent login = new Intent(YKWebViewActivity.this, LoginActivity.class);
			startActivityForResult(login, 44);
			point.setFocusable(false);
			//			mSendText.setFocusable(false);
		    }
		}
	    });

	    mAeniorImage.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    if (YKCurrentUserManager.getInstance().isLogin()) {
			Intent enquiry = new Intent(YKWebViewActivity.this, AeniorReplayActivity2.class);
			enquiry.putExtra("mCommentInfourID", mCommentInfourID);
			startActivityForResult(enquiry, 66);
		    } else {
			Intent login = new Intent(YKWebViewActivity.this, LoginActivity.class);
			startActivity(login);
		    }
		}
	    });
	    //	    mEditText.addTextChangedListener(new TextWatcher() {
	    //
	    //		@Override
	    //		public void onTextChanged(CharSequence s, int start, int before, int count) {
	    //		    if (s.length() > 0) {
	    //			mLinear.setVisibility(View.GONE);
	    //			mSendText.setVisibility(View.VISIBLE);
	    //		    } else {
	    //			mLinear.setVisibility(View.VISIBLE);
	    //			mSendText.setVisibility(View.GONE);
	    //		    }
	    //		}
	    //
	    //		@Override
	    //		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	    //
	    //		}
	    //
	    //		@Override
	    //		public void afterTextChanged(Editable s) {
	    //
	    //		}
	    //	    });
	}

    }

    /**
     * 友盟统计需要的俩个方法
     */
    @Override
    public void onResume() {
	super.onResume();
	activityRootView.addOnLayoutChangeListener(this); 
	if (isLogin) {
	    mWebView.loadUrl(YKWebViewActivity.this, mUrl);
	}
	if (mWebView != null) {
	    mWebView.onResume();
	}
	//		YKUtil.hideKeyBoard(YKWebViewActivity.this, mEditText);
	MobclickAgent.onPageStart("YKWebViewActivity"); // 统计页面
	MobclickAgent.onResume(this); // 统计时长
	JPushInterface.onResume(this);
    }

    public void onPause() {
	super.onPause();
	if (mWebView != null) {
	    mWebView.onPause();
	}
	MobclickAgent.onPageEnd("YKWebViewActivity"); // 保证 onPageEnd
	// 在onPause
	// 之前调用,因为 onPause 中会保存信息
	MobclickAgent.onPause(this);
	JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
	// if(!TextUtils.isEmpty(mUrl)){
	// TrackManager.getInstance().addTrack(mUrl+"&refer="+mIdentification+TrackUrl.REFER_CLOSE);
	// }else if(!TextUtils.isEmpty(mExperienceUrl)){
	// TrackManager.getInstance().addTrack(mExperienceUrl+"&refer="+mIdentification+TrackUrl.REFER_CLOSE);
	// }
	// 关闭监听器
	IS_TIRAL_SUCCESS = false;
	super.onDestroy();
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
	mUploadMessage = uploadMsg;
	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	i.addCategory(Intent.CATEGORY_OPENABLE);
	i.setType("image/*");
	YKWebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

    }

    @Override
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
	mUploadMessage = uploadMsg;
	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	i.addCategory(Intent.CATEGORY_OPENABLE);
	i.setType("*/*");
	YKWebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);

    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
	mUploadMessage = uploadMsg;
	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	i.addCategory(Intent.CATEGORY_OPENABLE);
	i.setType("image/*");
	YKWebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

    }

    @Override
    protected void onStart() {
	if (!TextUtils.isEmpty(mUrl)) {
	    Uri uri = Uri.parse(mUrl);
	    mRefer = uri.getQueryParameter("refer");
	    if (!TextUtils.isEmpty(mRefer)) {
	    }
	    // TrackManager.getInstance().addTrack1(mUrl, TrackUrl.REFER_START,
	    // mIdentification);
	} else if (!TextUtils.isEmpty(mExperienceUrl)) {
	    // TrackManager.getInstance().addTrack1(mExperienceUrl,
	    // TrackUrl.REFER_START, mIdentification);
	}
	super.onStart();
    }

    @Override
    public void onStop() {
	if (!TextUtils.isEmpty(mRefer)) {
	}
	if (!TextUtils.isEmpty(mUrl)) {
	    // TrackManager.getInstance().addTrack1(mUrl, TrackUrl.REFER_CLOSE,
	    // mIdentification);
	} else if (!TextUtils.isEmpty(mExperienceUrl)) {
	    // TrackManager.getInstance().addTrack1(mExperienceUrl,
	    // TrackUrl.REFER_CLOSE, mIdentification);
	}
	super.onStop();
    }

    private void loadingView() {
	try {
	    CustomButterfly.getInstance(YKWebViewActivity.this);
	    CustomButterfly.show(YKWebViewActivity.this);
	    // mCustomButterfly.show(this);
	} catch (Exception e) {
	    mCustomButterfly = null;
	}
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
	    int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
	if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){  

	    mHandler.postDelayed(new Runnable() {  
		public void run() {  
		    mAeniorImage.setVisibility(View.GONE);
		    mLinear.setVisibility(View.GONE);
		    mSendText.setVisibility(View.VISIBLE);
		}  
	    }, 100); 
	}else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){  
	    mHandler.postDelayed(new Runnable() {  
		public void run() {  
		    mEditText.setHint("发表评论");
		    mEditText.setText("");
		    commentWeb = null;
		    mAeniorImage.setVisibility(View.VISIBLE);
		    mLinear.setVisibility(View.VISIBLE);
		    mSendText.setVisibility(View.GONE);
		}  
	    }, 100); 
	}  
    }
}