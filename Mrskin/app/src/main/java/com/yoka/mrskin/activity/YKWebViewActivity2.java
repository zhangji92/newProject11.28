package com.yoka.mrskin.activity;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProductReport;
import com.yoka.mrskin.model.data.YKProductShareInfo;
import com.yoka.mrskin.model.data.YKShareInfo;
import com.yoka.mrskin.model.managers.YKAvailManagers;
import com.yoka.mrskin.model.managers.YKAvailManagers.AvailCallback;
import com.yoka.mrskin.model.managers.YKCollectManagers;
import com.yoka.mrskin.model.managers.YKCollectManagers.ColectCallback;
import com.yoka.mrskin.model.managers.YKCommentreplyManager;
import com.yoka.mrskin.model.managers.YKCommentreplyManager.IDReplyCallback;
import com.yoka.mrskin.model.managers.YKCommentreplyManager.ReplyCallback;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKCurrentUserManager.ILoginCallback;
import com.yoka.mrskin.model.managers.YKPonitCommentManagers;
import com.yoka.mrskin.model.managers.YKPonitCommentManagers.PonitCommentCallback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.PersonalWebView;
import com.yoka.mrskin.util.PersonalWebView.YKURIHandler;
import com.yoka.mrskin.util.PersonalWebView.YKUrlTitle;
import com.yoka.mrskin.util.PersonalWebView.onLoadStateListener;
import com.yoka.mrskin.util.SharePopupWindow;
import com.yoka.mrskin.util.YKDeviceInfo;
import com.yoka.mrskin.util.YKSharelUtil;
import com.yoka.share.manager.ShareQzoneManager;
import com.yoka.share.manager.ShareSinaManager;
import com.yoka.share.manager.ShareWxManager;

/**
 * 产品&心得&试用底层页
 * 
 * @author z l l
 * 
 */
public class YKWebViewActivity2 extends BaseActivity implements onLoadStateListener
{
	public static final String URL_INTENT_TILTE = "url_intent_tilte";
	public static final String U_NUM_INTENT = "u_num_intent";
	private static final int RESULT_CODE = 8;
	private static final int TIRAL_RESULT_CODE = 1;
	private static boolean IS_TIRAL_SUCCESS = false;
	public static final int REQUEST_CODE = 14;
	public static String mRefer  = null;
	private PersonalWebView mWebView;
	private View mShareLayout, mDoneLayout;
	public View mBackLayout;
	private SharePopupWindow mPopupWindow;
	private static YKProductShareInfo mInfo;
	private static YKProductReport mProduct;
	private String mUrl, mWriteUrl;
	private YKShareInfo mShareInfo;
	private String mToken;
	private TextView mTitle, mUExchangeTextView, mText;
	private String mExtras;
	private boolean isTrial = false;
	private boolean isLogin = false;
	private boolean isClearHistory = false;
	private String mTrackType, mTrackTypeId, mCommentID;
	private boolean isSkinTest;
	private boolean isComment = true;
	private ImageView point, comment;
	private CustomButterfly mCustomButterfly = null;
	private String mCollect, mAvail, mExperienceUrl;
	private boolean isHomeTop = false;
	//private String shareDefUrl = "http://p5.yokacdn.com/pic/div/2016/products/fujun/images/down-logo.png";//70*70
	private String shareDefUrl = "http://ss1.yokacdn.com/cosmetics/fujun/img/400400.jpg";//400*400

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
				LinearLayout layout = (LinearLayout) getLayoutInflater()
				.inflate(R.layout.u_shop_layout, null);
				TextView uNumTextView = (TextView) layout
						.findViewById(R.id.uShopNumTextView);
				uNumTextView.setText(getIntent().getStringExtra(U_NUM_INTENT));
				TextView uShopTextView = (TextView) layout
						.findViewById(R.id.uShopMoreTextView);
				uShopTextView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent shareTop = new Intent(YKWebViewActivity2.this,
								YKWebViewActivity.class);
						String url = YKManager.getFour() + "user/signlist?1=1";
						Uri uri = Uri.parse(url);
						shareTop.putExtra("probation_detail_url", url);
						shareTop.putExtra(YKWebViewActivity.URL_INTENT_TILTE,"赚优币");
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
		initView();
		// initShare();
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_OPEN + "YKWebViewActivity");
		ShareWxManager.getInstance().init(this);
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		mTitle = (TextView) findViewById(R.id.probation_detail_title);
		mWebView = (PersonalWebView) findViewById(R.id.probation_detail_webview);
		mWebView.setLoadStateListener(this);
		mShareLayout = findViewById(R.id.probation_detail_share_layout);
		mUExchangeTextView = (TextView) findViewById(R.id.uExchangeTextView);
		mUExchangeTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent shareTop = new Intent(YKWebViewActivity2.this,
						YKWebViewActivity.class);
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
				mWebView.loadUrl(YKWebViewActivity2.this,
						"javascript:shareDataAndroidFun2()");
			}
		});
		mBackLayout = findViewById(R.id.probation_detail_back_layout);
		mBackLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isTrial) {
					if (IS_TIRAL_SUCCESS) {
						YKWebViewActivity2.this.setResult(TIRAL_RESULT_CODE);
					} else {
						YKWebViewActivity2.this.setResult(RESULT_CODE);
					}
					finish();
				} else {
					if (mWebView != null && mWebView.canGoBack()) {
						mWebView.goBack();
					} else {
						YKWebViewActivity2.this.setResult(RESULT_CODE);
						finish();
					}
				}
			}
		});
		mDoneLayout = findViewById(R.id.probation_detail_done_layout);
		mDoneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YKWebViewActivity2.this.setResult(RESULT_CODE);
				finish();
			}
		});
		if (!TextUtils.isEmpty(mUrl)) {
			catchUrl(mUrl);
		}
		mWebView.setURIHandler(new YKURIHandler() {

			@Override
			public boolean handleURI(String url) {
				System.out.println("probation detail activity url = " + url);
				String webpage = YKSharelUtil.tryToGetWebPagemUrl(
						YKWebViewActivity2.this, url);
				if (!TextUtils.isEmpty(webpage)) {
					Uri uri = Uri.parse(url);
					if (!TextUtils.isEmpty(uri.getQueryParameter("url"))) {
						String mWebPager = uri.getQueryParameter("url");
						Intent newActivity = new Intent(
								YKWebViewActivity2.this,
								YKWebViewActivity.class);
						newActivity.putExtra("url", mWebPager);
						startActivity(newActivity);
						return true;
					}
				}

				String report = YKSharelUtil.tryToReportmUrl(
						YKWebViewActivity2.this, url);
				if (report != null) {
					setupProduct(url);
					if (mProduct != null) {
						Intent intent = new Intent(YKWebViewActivity2.this,
								CommitReportActivity.class);
						intent.putExtra("trial_probation_id", mProduct.getmId());
						intent.putExtra("trial_probation_image_url",
								mProduct.getImage());
						intent.putExtra("trial_probation_name",
								mProduct.getTitle());
						intent.putExtra("is_trial_product", false);
						startActivity(intent);
						return true;
					}
				}

				String commentList = YKSharelUtil.tryToCommenListtmUrl(
						YKWebViewActivity2.this, url);
				if (commentList != null) {
					Intent intent = new Intent(YKWebViewActivity2.this,
							CommentActivity.class);
					Uri uri = Uri.parse(url);
					mCommentID = uri.getQueryParameter("commentId");
					intent.putExtra("commentid", mCommentID);
					startActivity(intent);
					return true;
				}

				String tryLogin = YKSharelUtil.tryTotryLoginmUrl(
						YKWebViewActivity2.this, url);
				if (tryLogin != null
						|| url.equals("fujun://fujunaction.com/login")) {
					isLogin = true;
					if (YKCurrentUserManager.getInstance().isLogin()) {
						addUrl(url);
						mWebView.loadUrl(YKWebViewActivity2.this, url);
					} else {
						YKActivityManager.getInstance().showLogin(
								YKWebViewActivity2.this, new ILoginCallback() {

									@Override
									public void loginCallback(YKResult result,
											YKUser user) {
										if (result.isSucceeded()) {
											String tmpurl = mWebView
													.getCurrentURL();
											tmpurl = setupTryURL(tmpurl);
											mWebView.loadUrl(
													YKWebViewActivity2.this,
													tmpurl);
											isClearHistory = true;
											isLogin = false;
										}
									}
								});
					}
					return true;
				}

				String experienceWeb = YKSharelUtil.experiencemUrl(
						YKWebViewActivity2.this, url);
				if (experienceWeb != null) {
					Intent intent = new Intent(YKWebViewActivity2.this,
							YKWebViewActivity.class);
					Uri uri = Uri.parse(url);
					mExperienceUrl = uri.getQueryParameter("url");
					intent.putExtra("experienceurl", mExperienceUrl);
					startActivity(intent);
					return true;
				}

				String share = YKSharelUtil.tryToGetShareFormUrl(
						YKWebViewActivity2.this, url);
				if (share != null) {
					setupShareInfo(url);
					Uri uri = Uri.parse(url);
					if (!TextUtils.isEmpty(uri.getQueryParameter("platform"))) {
						String type = uri.getQueryParameter("platform");
						if (type.equals("sinaweibo") || type.equals("sinawei")) {
							if (mShareInfo != null) {
								//                                Glide.with(YKWebViewActivity2.this).asBitmap()  .load( mShareInfo.getmPic())      
								//                                .into(new SimpleTarget<Bitmap>(250, 250) {
								//                                    
								//                                    @Override
								//                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
								//                                        ShareSinaManager.getInstance().sendShare(resource,mShareInfo.getmTitle(),"","//"+ mShareInfo.getmUrl());
								ShareSinaManager.getInstance().shareSina("肤君分享", mShareInfo.getmTitle(), "", mShareInfo.getmPic(),YKWebViewActivity2.this);
								//                                    }      
								//                                }
								//                                        ); 
							} else {
								ShareSinaManager.getInstance().shareSina("肤君分享", mShareInfo.getmTitle(), "", shareDefUrl,YKWebViewActivity2.this);
							}
						} else if (type.equals("weixin_timeline")) {
							if (mShareInfo != null) {
								ShareWxManager.getInstance().shareWxCircle(
										mShareInfo.getmTitle(),
										mShareInfo.getmSummary(),
										mShareInfo.getmUrl(),
										mShareInfo.getmPic());

							}
						} else if (type.equals("weixin_session")) {
							if (mShareInfo != null) {
								ShareWxManager.getInstance().shareWx(
										mShareInfo.getmTitle(),
										mShareInfo.getmSummary(),
										mShareInfo.getmUrl(),
										mShareInfo.getmPic(),YKWebViewActivity2.this);
							}
						} else if (type.equals("qzone")) {
							if (mShareInfo != null) {
								ShareQzoneManager.getInstance().shareQzone(
										mShareInfo.getmTitle(),
										mShareInfo.getmSummary(),
										mShareInfo.getmUrl(),
										mShareInfo.getmPic(),YKWebViewActivity2.this);
							}
						}
					}
					return true;
				} else if (url.equals("fujun://fujunaction.com/share")) {
					mWebView.loadUrl(YKWebViewActivity2.this,
							"javascript:shareDataAndroidFun()");
					showSharePop();
					return true;
				} else if (url.contains(YKManager.getFour() + "news/document")) {
					mTrackType = "information";
					setTrackId(url);
					return false;
				} else if (url.contains(YKManager.getFour() + "product/comment")) {
					mTrackType = "experience";
					setTrackId(url);
					Intent a = new Intent(YKWebViewActivity2.this,
							YKWebViewActivity.class);
					a.putExtra("experienceurl", url);
					startActivity(a);
					return true;
				} else if (url.contains(YKManager.getFour() + "cosmetics/show")) {
					mTrackType = "product";
					setTrackId(url);
					return false;
				} else if (url.contains("fujun://fujunaction.com/userhome")) {
					YKWebViewActivity2.this.setResult(RESULT_OK);
					finish();
					return true;
				} else if (url
						.contains("fujun://fujunaction.com/publishexperience")) {
					Intent writeIntent = new Intent(YKWebViewActivity2.this,
							WriteExperienceActivity.class);
					startActivity(writeIntent);
					return true;
				} else if (url.contains("fujun://fujunaction.com/signlist")) {
					Intent shareTop = new Intent(YKWebViewActivity2.this,
							YKWebViewActivity.class);
					String mUrl = YKManager.getFour() + "user/signlist?1=1";
					shareTop.putExtra("probation_detail_url", mUrl);
					shareTop.putExtra(YKWebViewActivity.URL_INTENT_TILTE, "赚优币");
					startActivityForResult(shareTop, REQUEST_CODE);
					return true;
				}
				return false;
			}
		});
		setTitle();
		// push
		if (!TextUtils.isEmpty(mExtras)) {
			try {
				JSONObject dataJson = new JSONObject(mExtras);
				String response = dataJson.getString("target");
				String urlDecodedString = URLDecoder.decode(response);
				setupShareInfo(urlDecodedString);
				String url = mShareInfo.getmUrl();
				if(!TextUtils.isEmpty(url)){
					Log.e("Url","JSONObject = "+dataJson.toString()+"response = "+response.toString()+"urlDecodedString = "+urlDecodedString.toString());
					if (url.contains("action=try")) {
						isTrial = true;
					} else {
						isTrial = false;
					}
					if (url.contains(YKManager.getFour() + "product/comment")) {
						if (YKCurrentUserManager.getInstance().isLogin()) {
							mExperienceUrl = url;
							getExperienceCommentID();
						} else {
							mExperienceUrl = url;
							addUrl(mExperienceUrl);
						}
						//checkPointComment();
					}
				}


				//判断跳转本地页面 or 加载网页----zlz
				if(null == YKSharelUtil.pushLocalUrl(YKWebViewActivity2.this, urlDecodedString)){
					//不进行任何跳转
				}else{
					//跳转主页面
					String tab = YKSharelUtil.pushLocalUrl(YKWebViewActivity2.this, urlDecodedString);
					Intent intent = new Intent(YKWebViewActivity2.this,MainActivity.class);
					Log.d("MainActivity", intent.toString()+"--tab ="+tab);
					intent.putExtra("tab", tab);
					startActivity(intent);
					finish();
				}
				if (YKCurrentUserManager.getInstance().isLogin()) {
					if (!TextUtils.isEmpty(mShareInfo.getmTryId())) {
						String newUrl = url
								+ "&try_id="
										+ mShareInfo.getmTryId()
										+ "&clientid="
										+ YKDeviceInfo.getClientID()
										+ "&auth="
										+ YKCurrentUserManager.getInstance().getUser()
										.getToken();
						mWebView.loadUrl(YKWebViewActivity2.this, newUrl);
					} else {
						mWebView.loadUrl(YKWebViewActivity2.this, url);
					}
				} else {
					if (!TextUtils.isEmpty(mShareInfo.getmTryId())) {
						String tmpUrl = mShareInfo.getmUrl() + "&try_id="
								+ mShareInfo.getmTryId() + "&clientid="
								+ YKDeviceInfo.getClientID();
						mWebView.loadUrl(YKWebViewActivity2.this, tmpUrl);
						mUrl = tmpUrl;
					} else {
						// String temUrl = mShareInfo.getmUrl() + "&clientid="
						// + YKDeviceInfo.getClientID();
						String temUrl = mShareInfo.getmUrl();
						mWebView.loadUrl(YKWebViewActivity2.this, temUrl);
						mUrl = temUrl;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	private String setupTryURL(String url) {
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
					} else if (title.equals("签到规则") || title.equals("赚优币")
							|| title.equals("兑换记录")) {
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
			//跳转flag
			if (!TextUtils.isEmpty(uri.getQueryParameter("type"))) {
				String platform = uri.getQueryParameter("type");
				mShareInfo.setmPlatform(platform);
			}
		}
	}

	private void getUrl() {
		String reportUrl = getIntent().getStringExtra("report_url");
		String probationUrl = getIntent()
				.getStringExtra("probation_detail_url");
		String url = getIntent().getStringExtra("url");
		mWriteUrl = getIntent().getStringExtra("writeurl");
		String productUrl = getIntent().getStringExtra("productdetalis");
		String afternoonUrl = getIntent().getStringExtra("afternoon_url");
		mTrackType = getIntent().getStringExtra("track_type");
		mTrackTypeId = getIntent().getStringExtra("track_type_id");
		isTrial = getIntent().getBooleanExtra("istrial_product", false);
		isSkinTest = getIntent().getBooleanExtra("report_skin_result", false);
		mExperienceUrl = getIntent().getStringExtra("experienceurl");
		isHomeTop = getIntent().getBooleanExtra("isHomeTop", false);
		if (!TextUtils.isEmpty(probationUrl)) {
			addUrl(probationUrl);
		} else if (!TextUtils.isEmpty(reportUrl)) {
			mUrl = reportUrl;
		} else if (!TextUtils.isEmpty(url)) {
			mUrl = url;
		} else if (!TextUtils.isEmpty(productUrl)) {
			addUrl(productUrl);
		} else if (!TextUtils.isEmpty(afternoonUrl)) {
			mUrl = afternoonUrl;
		} else if (!TextUtils.isEmpty(mWriteUrl)) {
			addUrl(mWriteUrl);
		} else if (!TextUtils.isEmpty(mExperienceUrl)) {
			if (YKCurrentUserManager.getInstance().isLogin()) {
				getExperienceCommentID();
			} else {
				addUrl(mExperienceUrl);
			}
			//checkPointComment();
		}

		// 极光推送接收
		// Bundle bundle = getIntent().getExtras();
		if (!TextUtils.isEmpty(getIntent().getStringExtra("push_info"))) {
			mExtras = getIntent().getStringExtra("push_info");
		}
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (!TextUtils
					.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
				mExtras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			}
		}
	}

	// 登陆获取CommentID
	private void getExperienceCommentID() {
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

	private void getPointAvail(final ImageView point, final ImageView comment) {
		if (YKCurrentUserManager.getInstance().isLogin()) {
			YKPonitCommentManagers.getInstance().postYKPonitComment(
					new PonitCommentCallback() {
						@Override
						public void callback(YKResult result, String collect,
								String avail) {
							if (result.isSucceeded()) {
								mCollect = collect;
								mAvail = avail;
								point.setFocusable(true);
								comment.setFocusable(true);
								mText.setFocusable(true);
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

							}
						}
					}, mCommentID,
					YKCurrentUserManager.getInstance().getUser().getId());
		}

	}

	private void addUrl(String url) {
		// if (url.contains("yoka.com")) {
		if (url.contains("clientid")) {
			if (YKCurrentUserManager.getInstance().isLogin()) {
				mToken = YKCurrentUserManager.getInstance().getUser()
						.getToken();
				mUrl = url + YKDeviceInfo.getClientID() + "&auth=" + mToken;
			} else {
				mUrl = url + YKDeviceInfo.getClientID();
			}
		} else {
			if (YKCurrentUserManager.getInstance().isLogin()) {
				mToken = YKCurrentUserManager.getInstance().getUser()
						.getToken();
				mUrl = url + "&clientid=" + YKDeviceInfo.getClientID()
						+ "&auth=" + mToken;
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
						mWebView.loadUrl(this, newUrl + "&clientid="
								+ YKDeviceInfo.getClientID() + "&auth="
								+ mToken);
						mUrl = newUrl + "&clientid="
								+ YKDeviceInfo.getClientID() + "&auth="
								+ mToken;
					} else {
						mWebView.loadUrl(this, newUrl + "&clientid="
								+ YKDeviceInfo.getClientID());
						mUrl = newUrl + "&clientid="
								+ YKDeviceInfo.getClientID();
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
				YKWebViewActivity2.this.setResult(TIRAL_RESULT_CODE);
			} else {
				YKWebViewActivity2.this.setResult(RESULT_CODE);
			}
			finish();
		} else {
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				setTitle();
			} else {
				YKWebViewActivity2.this.setResult(RESULT_CODE);
				finish();
			}
		}
	}

	private void showSharePop() {
		mPopupWindow = new SharePopupWindow(YKWebViewActivity2.this,
				itemsOnClick);
		mPopupWindow.showAtLocation(YKWebViewActivity2.this
				.findViewById(R.id.probation_detail_share_img), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			SharePopupWindow.getInstance().dismiss();
			switch (v.getId()) {
			case R.id.popup_friend:
				dismissPopupWindow();
				// 注册微信分享
				// ShareWxManager.getInstance().init(YKWebViewActivity.this,
						// UmSharedAppID.SHARE_WX_APP_ID);
				if (mInfo != null) {
					ShareWxManager.getInstance().shareWxCircle(
							mInfo.getmDesc(), mInfo.getmTitle(),
							mInfo.getmLink(), mInfo.getmImgUrl());
					//                    TrackManager.getInstance().addTrack(
					//                            TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
					//                            + "&type=" + mTrackType + "&platform=wx");
				}else{
					String shareUrl = null;
					if(!TextUtils.isEmpty(mUrl)){
						shareUrl = mUrl;
					}else if(!TextUtils.isEmpty(mExperienceUrl)){
						shareUrl = mExperienceUrl;
					}
					ShareWxManager.getInstance().shareWxCircle(
							"点击查看详情", "来自肤君分享",
							shareUrl,shareDefUrl);
				}
				break;
			case R.id.popup_wei:
				dismissPopupWindow();
				if (mInfo != null) {
					ShareWxManager.getInstance().shareWx(mInfo.getmDesc(),
							mInfo.getmTitle(), mInfo.getmLink(),
							mInfo.getmImgUrl(),YKWebViewActivity2.this);
					//                    TrackManager.getInstance().addTrack(
							//                            TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
					//                            + "&type=" + mTrackType + "&platform=wx");
				}else{
					String shareUrl = null;
					if(!TextUtils.isEmpty(mUrl)){
						shareUrl = mUrl;
					}else if(!TextUtils.isEmpty(mExperienceUrl)){
						shareUrl = mExperienceUrl;
					}
					ShareWxManager.getInstance().shareWx(
							"点击查看详情", "来自肤君分享",
							shareUrl,shareDefUrl,YKWebViewActivity2.this);
				}
				break;
			case R.id.popup_sina:
				dismissPopupWindow();
				// 注册新浪微博分享
				if (mInfo != null) {
					//                    Glide.with(YKWebViewActivity2.this).asBitmap() .	load( mInfo.getmImgUrl())      
					//                    .into(new SimpleTarget<Bitmap>(250, 250) {
					//
					//                        @Override
					//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
					//                            ShareSinaManager.getInstance().sendShare(
					//                                    resource, mInfo.getmTitle(), "",
					//                                    "//" + mInfo.getmLink());
					ShareSinaManager.getInstance().shareSina("肤君分享", mInfo.getmTitle(), mInfo.getmLink()+"@肤君", mInfo.getmImgUrl(),YKWebViewActivity2.this);
					//                        }      
					//                    }
					//                            ); 
				} else {
					ShareSinaManager.getInstance().shareSina("肤君分享", "来自肤君分享", "@肤君", shareDefUrl,YKWebViewActivity2.this);
				}
				//                    TrackManager.getInstance().addTrack(
				//                            TrackUrl.ITEM_SHARE_SUCCESSED + mTrackTypeId
				//                            + "&type=" + mTrackType + "&platform=sina");
				break;
			case R.id.popup_qzone:
				dismissPopupWindow();
				if (mInfo != null) {
					ShareQzoneManager.getInstance().shareQzone(
							mInfo.getmTitle(), mInfo.getmDesc(),
							mInfo.getmLink(), mInfo.getmImgUrl(),YKWebViewActivity2.this);
					//                    TrackManager.getInstance()
					//                    .addTrack(
					//                            TrackUrl.ITEM_SHARE_SUCCESSED
					//                            + mTrackTypeId + "&type="
					//                            + mTrackType + "&platform=qzone");
				}else{
					String shareUrl = null;
					if(!TextUtils.isEmpty(mUrl)){
						shareUrl = mUrl;
					}else if(!TextUtils.isEmpty(mExperienceUrl)){
						shareUrl = mExperienceUrl;
					}
					ShareQzoneManager.getInstance().shareQzone(
							"来自肤君分享","点击查看详情",
							shareUrl, shareDefUrl,YKWebViewActivity2.this);
				}
				break;
			}
		}
	};

	private void dismissPopupWindow() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			YKWebViewActivity2.this.setResult(RESULT_OK);
			finish();
		}
		switch (requestCode) {
		case 33:
			getExperienceCommentID();
			getPointAvail(point, comment);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPageStart() {
		try {
			CustomButterfly.getInstance(YKWebViewActivity2.this);
			CustomButterfly.show(YKWebViewActivity2.this);
			//mCustomButterfly = CustomButterfly.show(this);
		} catch (Exception e) {
			mCustomButterfly = null;
		}
	}

	@Override
	public void onPageFinished() {
		if (!TextUtils.isEmpty(mTrackType) && !TextUtils.isEmpty(mTrackTypeId)) {
			//            TrackManager.getInstance().addTrack(
			//                    TrackUrl.ITEM_DISPLAY + mTrackTypeId + "&type="
			//                            + mTrackType);
		}
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if (isClearHistory) {
			mWebView.clearHistory();
			isClearHistory = false;
		}
	}

	@Override
	public void onRecevicedError() {
		AppUtil.dismissDialogSafe(mCustomButterfly);
	}

	@Override
	public void onLoadResource() {
		AppUtil.dismissDialogSafe(mCustomButterfly);
	}

	public static class getTiralInfo
	{
		@JavascriptInterface
		public void getTiralState(boolean state) {
			IS_TIRAL_SUCCESS = state;
		}
	}

	public static class getShareInfo
	{
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
			if (TextUtils.isEmpty(imgUrl)) {
				mInfo.setmImgUrl(imgUrl);
			}else{
				mInfo.setmImgUrl("http://www.yoka.com");
			}
			if (TextUtils.isEmpty(title)) {
				mInfo.setmTitle(title);
			}else{
				mInfo.setmTitle("肤君");
			}
			if (TextUtils.isEmpty(desc)) {
				mInfo.setmDesc(desc);
			}else{
				mInfo.setmDesc("肤君肤君");
			}
			if (TextUtils.isEmpty(link)) {
				mInfo.setmLink(link);
			}else{
				mInfo.setmLink("我的肤君");
			}
		}
	}
	
	/**
	 * 评论点赞
	 */
	public static class getCommentlike{
	    
	}

	/**
	 * 友盟统计需要的俩个方法
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (isLogin) {
			mWebView.loadUrl(YKWebViewActivity2.this, mUrl);
		}
		if (mWebView != null) {
			mWebView.onResume();
		}
		MobclickAgent.onPageStart("YKWebViewActivity2"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		JPushInterface.onResume(this);
	}

	public void onPause() {
		super.onPause();
		if (mWebView != null) {
			mWebView.onPause();
		}
		MobclickAgent.onPageEnd("YKWebViewActivity2"); // 保证 onPageEnd
		// 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	@Override
	protected void onDestroy() {
		AppUtil.savePushState(this, false);
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_CLOSE + "YKWebViewActivity");
		IS_TIRAL_SUCCESS = false;
		AppUtil.dismissDialogSafe(mCustomButterfly);
		super.onDestroy();
	}
}