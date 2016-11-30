package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKExperience;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKExperienceManager;
import com.yoka.mrskin.model.managers.YKMyCollectionManager;
import com.yoka.mrskin.model.managers.YKMyExperienceManager;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.YKSharelUtil;

public class ExperienceListActivity extends BaseActivity implements OnClickListener {

    public static final String EXPERIENCE_INTENT_USERID = "expreience_intent_userid";
    private XListView mCommentsListView;
    private Context mContext;
    private LinearLayout mBackLinearLayout;
    private RelativeLayout mDataNullLayout;
    private LayoutInflater inflater;
    private RelativeLayout mErrorRelativeLayout;
    private List<YKExperience> mListTopic = new ArrayList<YKExperience>();
    private Adapter mAdapter;
    private int mPn = 0;
    private String mUserId;
    private XListViewFooter mListViewFooter;
    private CustomButterfly mCustomButterfly = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.experience_list);
	mContext = this;
	init();
    }

    @Override
    public void onResume() {
	super.onResume();
	MobclickAgent.onPageStart("ExperienceListActivity");
	MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    public void onPause() {
	super.onPause();
	MobclickAgent.onPageEnd("ExperienceListActivity");
	MobclickAgent.onPause(this);
    }

    private void init() {
	inflater = LayoutInflater.from(mContext);
	mCommentsListView = (XListView) findViewById(R.id.experienceListView);
	mDataNullLayout = (RelativeLayout) findViewById(R.id.experienceno_null_layout);
	mDataNullLayout.setOnClickListener(this);
	mBackLinearLayout = (LinearLayout) findViewById(R.id.read_back_layout);
	mBackLinearLayout.setOnClickListener(this);
	mErrorRelativeLayout = (RelativeLayout) findViewById(R.id.experienceErrorLayout);
	mErrorRelativeLayout.setOnClickListener(this);
	//        mAddCommentsImageView = (ImageView) findViewById(R.id.addexperienceImageView);
	//        mAddCommentsImageView.setOnClickListener(this);
	mCommentsListView.setXListViewListener(new IXListViewListener() {

	    @Override
	    public void onRefresh() {
		mPn = 0;
		initData();
	    }

	    @Override
	    public void onLoadMore() {
		initData();
	    }
	});
	mListViewFooter = new XListViewFooter(mContext);
	mCommentsListView.setOnScrollListener(new OnScrollListener()
	{

	    @Override
	    public void onScrollStateChanged(AbsListView view, int scrollState)
	    {
		if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
		    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
			mListViewFooter.setState(XListViewFooter.STATE_READY);
			mCommentsListView.startLoadMore();
		    }
		}
	    }

	    @Override
	    public void onScroll(AbsListView view, int firstVisibleItem,
		    int visibleItemCount, int totalItemCount)
	    {

	    }
	});
	mUserId = getIntent().getStringExtra(EXPERIENCE_INTENT_USERID);
	mBackLinearLayout.setOnClickListener(this);
	mAdapter = new Adapter();
	mCommentsListView.setAdapter(mAdapter);
	try {
	    mCustomButterfly = CustomButterfly.show(this);
	} catch (Exception e) {
	    mCustomButterfly = null;
	}
	initData();
    }

    private void initData() {
	mErrorRelativeLayout.setVisibility(View.GONE);
	mDataNullLayout.setVisibility(View.GONE);
	if (TextUtils.isEmpty(mUserId)) {
	    YKExperienceManager.getInstance().postYKExperienceData(mPn + "","",new YKExperienceManager.Callback() {

		@Override
		public void callback(YKResult result, ArrayList<YKExperience> topicData) {
		    complete(result, topicData);
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    if (result.isSucceeded()) {

		    }
		}
	    });
	} else {

	    YKMyExperienceManager.getInstance().postYKExperienceData(mPn + "", mUserId, new YKMyExperienceManager.Callback() {
		@Override
		public void callback(YKResult result, ArrayList<YKExperience> topicData) {
		    complete(result, topicData);
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    if (result.isSucceeded()) {
		    }
		}

	    });

	    YKMyCollectionManager.getInstance().postYKMyCollectionData(mPn + "", mUserId, new YKMyCollectionManager.Callback() {
		@Override
		public void callback(YKResult result, ArrayList<YKExperience> topicData) {
		    /*   complete(result, topicData);
                    AppUtil.dismissDialogSafe(dialog);
                    if (result.isSucceeded()) {
                    }*/
		}

	    });
	}

    }

    private void complete(YKResult result, ArrayList<YKExperience> topicData) {

	mCommentsListView.stopLoadMore();
	mCommentsListView.stopRefresh();
	if (result.isSucceeded()) {
	    if (mPn == 0)
		mListTopic.clear();
	    if (topicData != null && topicData.size() != 0) {
		for (int i = 0; i < topicData.size(); i++) {
		    mListTopic.add(topicData.get(i));
		}
		mPn++;
	    }
	    if (mListTopic.size() == 0) {
		mDataNullLayout.setVisibility(View.VISIBLE);
	    }
	    mCommentsListView.setPullLoadEnable(true);
	    mAdapter.notifyDataSetChanged();
	} else {
	    if (mListTopic == null || mListTopic.size() == 0) {
		mErrorRelativeLayout.setVisibility(View.VISIBLE);
	    }
	    if (AppUtil.isNetworkAvailable(mContext)) {
		Toast.makeText(mContext, R.string.intent_error, Toast.LENGTH_SHORT).show();

	    } else {
		Toast.makeText(mContext, R.string.intent_no, Toast.LENGTH_SHORT).show();
	    }
	}

    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.read_back_layout:
	    finish();
	    break;
	    //        case R.id.addexperienceImageView:
	    //            if (YKCurrentUserManager.getInstance(mContext).isLogin()) {
	    //                Intent writeIntent = new Intent(mContext, WriteExperienceActivity.class);
	    //                startActivity(writeIntent);
	    //            } else {
	    //                Intent loginIntent = new Intent(mContext, LoginActivity.class);
	    //                startActivityForResult(loginIntent, 0);
	    //            }
	    //            break;
	case R.id.experienceErrorLayout:
	    mPn = 0;
	    initData();
	    break;
	case R.id.experienceno_null_layout:
	    if (YKCurrentUserManager.getInstance(mContext).isLogin()) {
		Intent writeIntent = new Intent(mContext, WriteExperienceActivity.class);
		startActivity(writeIntent);
	    } else {
		Intent loginIntent = new Intent(mContext, LoginActivity.class);
		startActivityForResult(loginIntent, 0);
	    }
	    break;
	default:
	    break;
	}

    }

    class Adapter extends BaseAdapter {

	private ViewHolder viewHolder = null;
	private DisplayImageOptions options;

	private Adapter() {
	    options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.list_default_bg).cacheInMemory(true)
		    .cacheOnDisk(true).resetViewBeforeLoading(true).considerExifParams(true).build();

	}

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return mListTopic.size();
	}

	@Override
	public Object getItem(int position) {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public long getItemId(int position) {
	    // TODO Auto-generated method stub
	    return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	    YKExperience dto = mListTopic.get(position);
	    if (convertView == null) {
		convertView = inflater.inflate(R.layout.experience_list_item, null, false);
		viewHolder = new ViewHolder();
		viewHolder.experienceHeadImageView = (RoundImage) convertView.findViewById(R.id.experienceHeadImageView);
		viewHolder.experienceImageView1 = (ImageView) convertView.findViewById(R.id.experienceImageView1);
		viewHolder.experienceImageView2 = (ImageView) convertView.findViewById(R.id.experienceImageView2);
		viewHolder.experienceImageView3 = (ImageView) convertView.findViewById(R.id.experienceImageView3);

		viewHolder.experienceNameTextView = (TextView) convertView.findViewById(R.id.experienceNameTextView);
		viewHolder.experienceAgeTextView = (TextView) convertView.findViewById(R.id.experienceAgeTextView);
		viewHolder.experienceHeadTimeTextView = (TextView) convertView.findViewById(R.id.experienceHeadTimeTextView);
		viewHolder.experienceTitleTextView = (TextView) convertView.findViewById(R.id.experienceTitleTextView);
		viewHolder.experienceSkinTextView = (TextView) convertView.findViewById(R.id.experienceSkinTextView);
		viewHolder.experienceConentTextView = (TextView) convertView.findViewById(R.id.experienceConentTextView);

		viewHolder.experienceConentLinearLayout = (LinearLayout) convertView
			.findViewById(R.id.experienceConentLinearLayout);

		convertView.setTag(viewHolder);
	    } else {
		viewHolder = (ViewHolder) convertView.getTag();
	    }
	    viewHolder.experienceImageView1.setVisibility(View.GONE);
	    viewHolder.experienceImageView2.setVisibility(View.GONE);
	    viewHolder.experienceImageView3.setVisibility(View.GONE);

	    if (dto.getImages() != null && dto.getImages().size() != 0) {
		viewHolder.experienceConentLinearLayout.setVisibility(View.GONE);
		for (int n = 0; n < dto.getImages().size(); n++) {
		    if (n == 0) {
			viewHolder.experienceImageView1.setVisibility(View.VISIBLE);

			ImageLoader.getInstance().displayImage(dto.getImages().get(0).getmURL(), viewHolder.experienceImageView1, options);
			//  Glide.with(mContext).load(dto.getImages().get(0).getmURL()).into(viewHolder.experienceImageView1).onLoadStarted(getResources().getDrawable(R.drawable.default_user_bg));
		    }
		    if (n == 1) {
			viewHolder.experienceImageView2.setVisibility(View.VISIBLE);

			ImageLoader.getInstance().displayImage(dto.getImages().get(0).getmURL(), viewHolder.experienceImageView1, options);
			// Glide.with(mContext).load(dto.getImages().get(0).getmURL()).into(viewHolder.experienceImageView1).onLoadStarted(getResources().getDrawable(R.drawable.default_user_bg));
		    }
		    if (n == 2) {
			viewHolder.experienceImageView3.setVisibility(View.VISIBLE);

			ImageLoader.getInstance().displayImage(dto.getImages().get(2).getmURL(), viewHolder.experienceImageView3, options);
			//  Glide.with(mContext).load(dto.getImages().get(2).getmURL()).into(viewHolder.experienceImageView3).onLoadStarted(getResources().getDrawable(R.drawable.default_user_bg));

		    }
		}

	    } else {
		viewHolder.experienceConentLinearLayout.setVisibility(View.VISIBLE);
	    }
	    ImageLoader.getInstance().displayImage(dto.getAuthor().getAvatar().getmURL(), viewHolder.experienceHeadImageView, options);
	    //    Glide.with(mContext).load(dto.getAuthor().getAvatar().getmURL()).into(viewHolder.experienceHeadImageView).onLoadStarted(getResources().getDrawable(R.drawable.default_user_bg));;

	    viewHolder.experienceTitleTextView.setText(dto.getTitle());
	    viewHolder.experienceNameTextView.setText(dto.getAuthor().getName());
	    if (dto.getAuthor().getAge() == 0) {
		viewHolder.experienceAgeTextView.setVisibility(View.INVISIBLE);
	    } else {
		viewHolder.experienceAgeTextView.setVisibility(View.VISIBLE);
	    }
	    viewHolder.experienceAgeTextView.setText(dto.getAuthor().getAge() + getString(R.string.experience_age));
	    viewHolder.experienceHeadTimeTextView.setText(TimeUtil.forTimeForYearMonthDayShorthand(dto.getTime()));
	    int complexion = dto.getAuthor().getComplexion();
	    String type = "";
	    if (complexion == 1) {
		type = getString(R.string.experience_tpye_mixed);
	    } else if (complexion == 2) {
		type = getString(R.string.experience_tpye_dry);
	    } else if (complexion == 3) {
		type = getString(R.string.experience_tpye_oily);
	    } else if (complexion == 4) {
		type = getString(R.string.experience_tpye_sensitive);
	    } else if ((complexion == 5)) {
		type = getString(R.string.experience_tpye_neutral);
	    } else {
		type = getString(R.string.experience_tpye_unknown);
	    }
	    if (complexion == 0) {
		viewHolder.experienceSkinTextView.setVisibility(View.INVISIBLE);
	    } else {
		viewHolder.experienceSkinTextView.setVisibility(View.VISIBLE);
	    }
	    viewHolder.experienceSkinTextView.setText(type);
	    viewHolder.experienceConentTextView.setText(getString(R.string.experience_conmment, dto.getContent()));

	    // viewHolder.experienceConentLinearLayout.setVisibility(View.VISIBLE);
	    /*
	     * viewHolder.experienceConentTextView.setText(getString(R.string.
	     * experience_conmment, "TextViewTextV" +
	     * "iewTextViewTextViewTextViewTextViewTextViewTextViewTextViewTextViewTextViewTextViewTextVie"
	     * + "wTextViewTextViewTextViewTextViewTextView"));
	     */

	    convertView.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {

		    String webPager = null;
		    YKExperience dto = mListTopic.get(position);
		    if (dto != null) {
			webPager = YKSharelUtil.tryToGetWebPagemUrl(mContext, dto.getUrl());
		    }
		    Intent shareTop = new Intent(mContext, YKWebViewActivity.class);

		    if (AppUtil.isNetworkAvailable(mContext)) {
			if (webPager != null) {
			    String url = mListTopic.get(position).getUrl();
			    Uri uri = Uri.parse(url);
			    shareTop.putExtra("url", uri.getQueryParameter("url"));
			    shareTop.putExtra("title", dto.getTitle());
			    shareTop.putExtra("identification", "comment_list");
			    shareTop.putExtra("track_type", "home_topic_topic");
			    shareTop.putExtra("track_type_id", dto.getID());
			    startActivity(shareTop);
			    //TrackManager.getInstance().addTrack(TrackUrl.ITEM_CLICK + dto.getID() + "&type=home_topic_topic");
			} else {
			    Toast.makeText(mContext, getString(R.string.intent_error), Toast.LENGTH_SHORT).show();
			}
		    } else {
			Toast.makeText(mContext, getString(R.string.intent_no), Toast.LENGTH_SHORT).show();
		    }

		}
	    });
	    return convertView;
	}

    }

    static class ViewHolder {
	ImageView experienceImageView1, experienceImageView2, experienceImageView3;
	TextView experienceNameTextView, experienceAgeTextView, experienceHeadTimeTextView, experienceTitleTextView,
	experienceConentTextView, experienceSkinTextView;
	RoundImage experienceHeadImageView;
	LinearLayout experienceConentLinearLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == 0 && resultCode == RESULT_OK) {
	    boolean haveLogin = data.getBooleanExtra(LoginActivity.UESR, false);
	    if (haveLogin) {
		initData();
	    }
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

}
