package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.yoka.mrskin.model.managers.YKMyCollectionManager;
import com.yoka.mrskin.model.managers.YKMyExperienceManager;
import com.yoka.mrskin.model.managers.YKMyReplyManager;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.YKSharelUtil;
import com.yoka.mrskin.viewpager.NoScrollViewPager;

/**
 * 心得列表
 * 
 * @author wangnan
 * 
 */
public class MyExperienceListActivity extends BaseActivity implements
OnCheckedChangeListener
{

    public static final String EXPERIENCE_INTENT_USERID = "expreience_intent_userid";

    private List<View> mViews = new ArrayList<View>();
    private List<XListView> mXListViews = new ArrayList<XListView>();
    private List<RelativeLayout> mErrorViews = new ArrayList<RelativeLayout>();
    private List<RelativeLayout> mNullViews = new ArrayList<RelativeLayout>();

    private List<List<YKExperience>> mList = new ArrayList<List<YKExperience>>();
    private List<ListViewAdaper> mAdapers = new ArrayList<ListViewAdaper>();
    private int[] mPn = new int[] { 0, 0, 0 };
    private LinearLayout mExperienceTab,mBackLayout;
    private NoScrollViewPager mViewPager;
    private CustomButterfly mCustomButterfly = null;
    private Context mContext;

    private String mUserId;
    private int mCurrentItem = 0;
    private XListViewFooter mListViewFooter;
    private ImageView mAddExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_experience);
        mContext = this;
        mUserId = getIntent().getStringExtra(EXPERIENCE_INTENT_USERID);
        findViewById();
        try {
            mCustomButterfly = CustomButterfly.show(this);
        } catch (Exception e) {
            mCustomButterfly = null;
        }
        initExperienceData(mCurrentItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        //点击收藏页面返回后刷新收藏
        if(mCurrentItem==1) {
        	mPn[mCurrentItem] = 0;
            initData(mCurrentItem);
        }
        MobclickAgent.onPageStart("MyExperienceListActivity");
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("MyExperienceListActivity");
        MobclickAgent.onPause(this);
    }

    private void findViewById() {
        initListViews();
        mExperienceTab = (LinearLayout) this.findViewById(R.id.experienceTab);
        RadioGroup rg_select = (RadioGroup) mExperienceTab
                .findViewById(R.id.listRadioGroup);
        rg_select.setOnCheckedChangeListener(this);
        mViewPager = (NoScrollViewPager) mExperienceTab
                .findViewById(R.id.noScrollViewPager);
        mViewPager.setAdapter(new ViewPagerAdapter());
    }

    private void initListViews() {
        for (int i = 0; i < mPn.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.my_experience_listview,null);
            mBackLayout = (LinearLayout) findViewById(R.id.read_back_layout);
            mBackLayout.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
            
            mAddExperience = (ImageView) findViewById(R.id.addexperienceImageView);
            mAddExperience.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    if (YKCurrentUserManager.getInstance().isLogin()) {
                        Intent writeExperience = new Intent(mContext,
                                WriteExperienceActivity.class);
                        startActivity(writeExperience);
                    } else {
                        Intent goLogin = new Intent(mContext, LoginActivity.class);
                        startActivity(goLogin);
                    }
                    
                }
            });
            final XListView listView = (XListView) view
                    .findViewById(R.id.experienceListView);
            listView.setXListViewListener(new ListViewClick(i));
            mListViewFooter = new XListViewFooter(mContext);
            listView.setOnScrollListener(new OnScrollListener()
            {
                
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState)
                {
                    if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                            mListViewFooter.setState(XListViewFooter.STATE_READY);
                            listView.startLoadMore();
                        }
                    }
                }
                
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                        int visibleItemCount, int totalItemCount)
                {
                    
                }
            });

            RelativeLayout mDataNullLayout = (RelativeLayout) view
                    .findViewById(R.id.experienceno_null_layout);
           

            RelativeLayout mDataNullTextLayout = (RelativeLayout) view
                    .findViewById(R.id.experience_go_test_layout);

            TextView mDateNullTextView = (TextView) view
                    .findViewById(R.id.experience_null_text);
            if (i == 1) {
                mDataNullTextLayout.setVisibility(View.GONE);
                mDateNullTextView.setText(getString(R.string.null_collection));
            } else if (i == 2) {
                mDataNullTextLayout.setVisibility(View.GONE);
                mDateNullTextView.setText(getString(R.string.null_reply));
            }
            mDataNullTextLayout.setOnClickListener(new NullViewClick(i));
            RelativeLayout mErrorRelativeLayout = (RelativeLayout) view
                    .findViewById(R.id.experienceErrorLayout);
            RelativeLayout mError = (RelativeLayout) mErrorRelativeLayout
                    .findViewById(R.id.errorReleaseRelativeLayout);
            mError.setOnClickListener(new ErrorViewClick(i));

            List<YKExperience> list = new ArrayList<YKExperience>();
            ListViewAdaper adaper = new ListViewAdaper(list);
            listView.setAdapter(adaper);

            mViews.add(view);
            mXListViews.add(listView);
            mErrorViews.add(mErrorRelativeLayout);
            mNullViews.add(mDataNullLayout);
            mAdapers.add(adaper);
            mList.add(list);
        }

    }

    private void initData(int type) {
        mErrorViews.get(type).setVisibility(View.GONE);
        mNullViews.get(type).setVisibility(View.GONE);
        switch (type) {
        case 0:
            initExperienceData(type);
            break;
        case 1:
            initCollectionData(type);
            break;
        case 2:
            initReplyData(type);
            break;

        default:
            initExperienceData(0);
            initCollectionData(1);
            initReplyData(2);
            break;
        }
    }

    private void initExperienceData(final int type) {

        YKMyExperienceManager.getInstance().postYKExperienceData(
                mPn[type] + "", mUserId, new YKMyExperienceManager.Callback() {

                    @Override
                    public void callback(YKResult result,
                            ArrayList<YKExperience> topicData) {
                        complete(type, result, topicData);
                        AppUtil.dismissDialogSafe(mCustomButterfly);
                    }
                });

    }

    private void initCollectionData(final int type) {

        YKMyCollectionManager.getInstance().postYKMyCollectionData(
                mPn[type] + "", mUserId, new YKMyCollectionManager.Callback() {

                    @Override
                    public void callback(YKResult result,
                            ArrayList<YKExperience> topicData) {
                        complete(type, result, topicData);
                        AppUtil.dismissDialogSafe(mCustomButterfly);
                    }
                });

    }

    private void initReplyData(final int type) {
        YKMyReplyManager.getInstance().postYKMyReplyData(mPn[type] + "",
                mUserId, new YKMyReplyManager.Callback() {

            @Override
            public void callback(YKResult result,
                    ArrayList<YKExperience> topicData) {
                complete(type, result, topicData);
                AppUtil.dismissDialogSafe(mCustomButterfly);
            }
        });

    }

    private void complete(int currentItem, YKResult result,
            ArrayList<YKExperience> topicData) {

        mXListViews.get(currentItem).stopLoadMore();
        mXListViews.get(currentItem).stopRefresh();
        if (result.isSucceeded()) {
            if (mPn[currentItem] == 0)
                mList.get(currentItem).clear();
            if (topicData != null && topicData.size() != 0) {
                for (int i = 0; i < topicData.size(); i++) {
                    mList.get(currentItem).add(topicData.get(i));
                }
                mPn[currentItem]++;
            }
            if (mList.get(currentItem).size() == 0) {
                mNullViews.get(currentItem).setVisibility(View.VISIBLE);
            }
            mXListViews.get(currentItem).setPullLoadEnable(true);
            mAdapers.get(currentItem).notifyDataSetChanged();
        } else {
            if (mList.get(currentItem) == null
                    || mList.get(currentItem).size() == 0) {
                mErrorViews.get(currentItem).setVisibility(View.VISIBLE);
            }
            if (AppUtil.isNetworkAvailable(this)) {
                Toast.makeText(this, R.string.intent_error, Toast.LENGTH_SHORT)
                .show();

            } else {
                Toast.makeText(this, R.string.intent_no, Toast.LENGTH_SHORT)
                .show();
            }
        }
    }

    private class ViewPagerAdapter extends PagerAdapter
    {

        @Override
        public Object instantiateItem(View container, int position) {
            ((NoScrollViewPager) container).addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((NoScrollViewPager) container).removeView(mViews.get(position));
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    class ListViewAdaper extends BaseAdapter
    {

        private List<YKExperience> mList;
        private ViewHolder viewHolder = null;
        private DisplayImageOptions options;

        public ListViewAdaper(List<YKExperience> list)
        {
            this.mList = list;
            options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.list_default_bg)
            .cacheInMemory(true).cacheOnDisk(true)
            .resetViewBeforeLoading(true).considerExifParams(true)
            .build();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final YKExperience dto = mList.get(position);
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.my_experience_list_item, null, false);
                viewHolder = new ViewHolder();
                viewHolder.experienceHeadImageView = (RoundImage) convertView
                        .findViewById(R.id.myExperienceHeadImageView);
                viewHolder.experienceImageView = (ImageView) convertView
                        .findViewById(R.id.myExperienceImageView);
                viewHolder.experienceNameTextView = (TextView) convertView
                        .findViewById(R.id.myExperienceNameTextView);
                viewHolder.experienceAgeTextView = (TextView) convertView
                        .findViewById(R.id.myExperienceAgeTextView);
                viewHolder.experienceHeadTimeTextView = (TextView) convertView
                        .findViewById(R.id.myExperienceHeadTimeTextView);
                viewHolder.experienceTitleTextView = (TextView) convertView
                        .findViewById(R.id.myExperienceTitleTextView);
                viewHolder.experienceConentTextView = (TextView) convertView
                        .findViewById(R.id.myExperienceConentTextView);
                viewHolder.experienceSkinTextView = (TextView) convertView
                        .findViewById(R.id.myExperienceSkinTextView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (dto.getAuthor().getAge() == 0) {
                viewHolder.experienceAgeTextView.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.experienceAgeTextView.setVisibility(View.VISIBLE);
            }
            if (dto.getImages() != null && dto.getImages().size() != 0) {
                viewHolder.experienceImageView.setVisibility(View.VISIBLE);
               /* Glide.with(mContext).load(dto.getImages().get(0).getmURL())
        		.into( viewHolder.experienceImageView);*/
               
                ImageLoader.getInstance().displayImage(
                        dto.getImages().get(0).getmURL(),
                        viewHolder.experienceImageView, options);
            } else {
                viewHolder.experienceImageView.setVisibility(View.GONE);
            }
          /*  Glide.with(mContext).load(dto.getAuthor().getAvatar().getmURL())
    		.into( viewHolder.experienceHeadImageView).onLoadStarted(getResources().getDrawable(R.drawable.default_user_bg));*/
            ImageLoader.getInstance().displayImage(
            		dto.getAuthor().getAvatar().getmURL(),
                    viewHolder.experienceHeadImageView, options);

            viewHolder.experienceHeadTimeTextView.setText(TimeUtil
                    .forTimeForYearMonthDayShorthandNew(dto.getTime()));
            viewHolder.experienceAgeTextView.setText(dto.getAuthor().getAge()
                    + getString(R.string.experience_age));
            viewHolder.experienceTitleTextView.setText(dto.getTitle());
            viewHolder.experienceNameTextView
            .setText(dto.getAuthor().getName());
            viewHolder.experienceConentTextView.setText(dto.getContent());
            String type = "";
            int complexion = dto.getAuthor().getComplexion();
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
                viewHolder.experienceSkinTextView.setVisibility(View.GONE);
            } else {
                viewHolder.experienceSkinTextView.setVisibility(View.VISIBLE);
            }
            viewHolder.experienceSkinTextView.setText(type);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    String webPager = null;

                    if (dto != null) {
                        webPager = YKSharelUtil.tryToGetWebPagemUrl(mContext,
                                dto.getUrl());
                    }
                    Intent shareTop = new Intent(mContext,
                            YKWebViewActivity.class);

                    if (AppUtil.isNetworkAvailable(mContext)) {
                        if (webPager != null) {
                            String url = dto.getUrl();
                            Uri uri = Uri.parse(url);
                            shareTop.putExtra("experienceurl",
                                    uri.getQueryParameter("url"));
                            shareTop.putExtra("title", dto.getTitle());
                            shareTop.putExtra("track_type", "home_topic_topic");
                            //shareTop.putExtra("identification", "comment_list");
                            shareTop.putExtra("track_type_id", dto.getID());
                            startActivity(shareTop);
//                            TrackManager.getInstance().addTrack(
//                                    TrackUrl.ITEM_CLICK + dto.getID()
//                                    + "&type=home_topic_topic");
                        } else {
                            Toast.makeText(mContext,
                                    getString(R.string.intent_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, getString(R.string.intent_no),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
            return convertView;
        }

    }

    static class ViewHolder
    {
    	ImageView experienceImageView;
        TextView experienceNameTextView, experienceAgeTextView,
        experienceHeadTimeTextView, experienceTitleTextView,
        experienceConentTextView, experienceSkinTextView;
        RoundImage experienceHeadImageView;
    }

    class ErrorViewClick implements OnClickListener
    {
        private int mType;

        public ErrorViewClick(int type)
        {
            this.mType = type;
        }

        @Override
        public void onClick(View v) {
            initData(mType);

        }

    }

    class NullViewClick implements OnClickListener
    {
        private int mType;

        public NullViewClick(int type)
        {
            this.mType = type;
        }

        @Override
        public void onClick(View v) {
            if (mType == 0) {
                if (YKCurrentUserManager.getInstance(mContext).isLogin()) {
                    Intent writeIntent = new Intent(mContext,
                            WriteExperienceActivity.class);
                    startActivity(writeIntent);
                } else {
                    Intent loginIntent = new Intent(mContext,
                            LoginActivity.class);
                    startActivityForResult(loginIntent, 0);
                }
            }

        }

    }

    class ListViewClick implements IXListViewListener
    {
        private int mType;

        public ListViewClick(int type)
        {
            this.mType = type;
        }

        @Override
        public void onRefresh() {
            mPn[mType] = 0;
            initData(mType);

        }

        @Override
        public void onLoadMore() {
            initData(mType);

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
        case R.id.leftRadioButton:
            mCurrentItem = 0;
            mViewPager.setCurrentItem(mCurrentItem);
            break;
        case R.id.centerRadioButton:
            mCurrentItem = 1;
            mViewPager.setCurrentItem(mCurrentItem);
            break;
        case R.id.rightRadioButton:
            mCurrentItem = 2;
            mViewPager.setCurrentItem(mCurrentItem);
            break;

        }
        if (mList.get(mCurrentItem) == null
                || mList.get(mCurrentItem).size() == 0) {
            AppUtil.showDialogSafe(mCustomButterfly);
            initData(mCurrentItem);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            boolean haveLogin = data.getBooleanExtra(LoginActivity.UESR, false);
            if (haveLogin) {
                initData(mCurrentItem);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
