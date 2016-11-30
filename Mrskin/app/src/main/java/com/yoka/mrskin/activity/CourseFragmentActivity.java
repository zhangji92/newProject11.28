package com.yoka.mrskin.activity;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.xlistview.XListView;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.managers.YKBeautyInformationManager;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.BitmapUtilImage;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;
import com.yoka.mrskin.util.YKSharelUtil;

public class CourseFragmentActivity extends AddTaskWebViewActivity {
    public static final String COURSE_INFORMATION_INTENT="course_information_intent";
    private String mCourseUrl;
    private LinearLayout mBack,courseTitleLinearLayout;
    private Button mBeautyMakeupButton, mBeautyInformationButton;
    private XListView mCoursexListView;
    private Context mContext;
    private List<YKTopicData> mListTopic = new ArrayList<YKTopicData>();
    private CourseListAdapter mAdapter;
    private int pn = 1;
    private CustomButterfly mCustomButterfly = null;
    private RelativeLayout mErrorRelativeLayout;
    private TextView courseTitleTextView;
    private XListViewFooter mListViewFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_fragment);
        mContext = this;
    
        init();
        //TrackManager.getInstance().addTrack(TrackUrl.PAGE_OPEN + "CourseFragmentActivity");
    }

    @Override
    protected void onDestroy() {
        //TrackManager.getInstance().addTrack(TrackUrl.PAGE_CLOSE + "CourseFragmentActivity");
        super.onDestroy();
    }

    public void init() {
        courseTitleLinearLayout = (LinearLayout) findViewById(R.id.courseTitleLinearLayout);
        courseTitleTextView=(TextView)findViewById(R.id.courseTitleTextView);
        mBeautyMakeupButton = (Button) findViewById(R.id.beautyMakeupButton);
        mBeautyInformationButton = (Button) findViewById(R.id.beautyInformationButton);

        mBack = (LinearLayout) findViewById(R.id.read_back_layout);

        mBack.setOnClickListener(this);
        mBeautyMakeupButton.setOnClickListener(this);
        mBeautyInformationButton.setOnClickListener(this);
        // mRefresh = (TextView) getActivity().findViewById(R.id.read_refresh);
        // mRefresh.setOnClickListener(this);

        mWebView = (ProgressWebView) findViewById(R.id.webView_read);
        // 教程
        mWebView.loadUrl(this, YKManager.getFour() + "news/course");
        mWebView.setURIHandler(new YKURIHandler() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean handleURI(String url) {
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
                    mCourseUrl = uri.getQueryParameter("url");
                    if (mCourseUrl != null) {
                        Intent course = new Intent(CourseFragmentActivity.this, YKWebViewActivity.class);
                        course.putExtra("url", mCourseUrl);
                        course.putExtra("title", getString(R.string.course_class));
                        startActivity(course);
                    }
                    return true;
                }
                return false;
            }
        });
        initListView();
        /** 判断哪里跳转，修改标题栏*/
        if(getIntent().getStringExtra(COURSE_INFORMATION_INTENT)!=null){
            courseTitleLinearLayout.setVisibility(View.GONE);
            courseTitleTextView.setVisibility(View.VISIBLE);
            if(getIntent().getStringExtra(COURSE_INFORMATION_INTENT).equals(COURSE_INFORMATION_INTENT)){
                courseTitleTextView.setText(getString(R.string.read_news));
                mCoursexListView.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
            }else{
                courseTitleTextView.setText(getString(R.string.read_read));
            }
        }
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
        MobclickAgent.onPageStart("CourseFragmentActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
    }

    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
        MobclickAgent.onPageEnd("CourseFragmentActivity"); // 保证 onPageEnd
        // 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.read_back_layout:
            finish();
            break;
        case R.id.beautyMakeupButton:
            mBeautyMakeupButton.setBackgroundResource(R.drawable.ic_news_left_press);
            mBeautyMakeupButton.setTextColor(getResources().getColor(R.color.location_title));
            mBeautyInformationButton.setBackgroundResource(R.drawable.ic_news_right_default);
            mBeautyInformationButton.setTextColor(getResources().getColor(R.color.white));
            mCoursexListView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            break;
        case R.id.beautyInformationButton:
            initInformation();
            break;
        case R.id.courseErrorLayout:
            try {
                mCustomButterfly = CustomButterfly.show(this);
            } catch (Exception e) {
                mCustomButterfly = null;
            }
            pn = 1;
            initData();
            break;
        default:
            break;
        }
    }

    private void initInformation(){
        mBeautyMakeupButton.setBackgroundResource(R.drawable.ic_news_left_default);
        mBeautyMakeupButton.setTextColor(getResources().getColor(R.color.white));
        mBeautyInformationButton.setBackgroundResource(R.drawable.ic_news_right_press);
        mBeautyInformationButton.setTextColor(getResources().getColor(R.color.location_title));
        mCoursexListView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
    }

    private void initListView() {
        mErrorRelativeLayout = (RelativeLayout) findViewById(R.id.courseErrorLayout);
        mErrorRelativeLayout.setOnClickListener(this);
        mCoursexListView = (XListView) findViewById(R.id.coursexListView);
        mListViewFooter = new XListViewFooter(mContext);
        mCoursexListView.setOnScrollListener(new OnScrollListener()
        {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                        mListViewFooter.setState(XListViewFooter.STATE_READY);
                        mCoursexListView.startLoadMore();
                    }
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount)
            {
                
            }
        });
        mAdapter = new CourseListAdapter();
        mCoursexListView.setAdapter(mAdapter);
        mCoursexListView.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                pn = 1;
                initData();

            }

            @Override
            public void onLoadMore() {
                initData();

            }
        });
        initData();
    }

    private void initData() {
        String type = "after_id";
        String id = "5";
        if (pn != 1) {
            type = "before_id";
            id = mListTopic.get(mListTopic.size() - 1).getID();
        }
        mErrorRelativeLayout.setVisibility(View.GONE);
        YKBeautyInformationManager.getInstance().postYKTopicData(type, id, new YKBeautyInformationManager.Callback() {

            @Override
            public void callback(YKResult result, ArrayList<YKTopicData> topicData) {
                AppUtil.dismissDialogSafe(mCustomButterfly);
                mCoursexListView.stopLoadMore();
                mCoursexListView.stopRefresh();
                if (result.isSucceeded()) {
                    if (pn == 1)
                        mListTopic.clear();
                    if (topicData != null && topicData.size() != 0) {
                        for (int i = 0; i < topicData.size(); i++) {
                            mListTopic.add(topicData.get(i));
                        }
                        ++pn;
                    }
                    mCoursexListView.setPullLoadEnable(true);
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
        });
    }

    class CourseListAdapter extends BaseAdapter {
        private ViewHolder viewHolder = null;
        private DisplayImageOptions options;

        public CourseListAdapter() {
            options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.list_default_bg).cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).considerExifParams(true).build();
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
            YKTopicData dto = mListTopic.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.information_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mBeautyListItemImageView = (ImageView) convertView.findViewById(R.id.informationListItemImageView);
                viewHolder.mBeautyListItemTextView = (TextView) convertView.findViewById(R.id.informationListItemTextView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mBeautyListItemTextView.setText(dto.getmTopicTitle());
            if(dto.getmCover()!=null){
            	  int[] size = BitmapUtilImage.getImageSize(dto.getmCover().getMwidth(), dto.getmCover().getMheight());
                  viewHolder.mBeautyListItemImageView.setLayoutParams(new LinearLayout.LayoutParams(size[0], size[1]));
                  ImageLoader.getInstance().displayImage(dto.getmCover().getmURL(), viewHolder.mBeautyListItemImageView, options);
               //   Glide.with(mContext).load(dto.getmCover().getmURL()).into(viewHolder.mBeautyListItemImageView).onLoadStarted(getResources().getDrawable(R.drawable.list_default_bg));
            }else{
            	viewHolder.mBeautyListItemImageView.setBackgroundResource(R.drawable.list_default_bg);
            }
          
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String webPager = null;
                    YKTopicData dto = mListTopic.get(position);
                    if (dto != null) {
                        webPager = YKSharelUtil.tryToGetWebPagemUrl(mContext, dto.getmTopicUrl());
                    }
                    Intent shareTop = new Intent(mContext, YKWebViewActivity.class);

                    if (AppUtil.isNetworkAvailable(mContext)) {
                        if (webPager != null) {
                            String url = mListTopic.get(position).getmTopicUrl();
                            Uri uri = Uri.parse(url);
                            shareTop.putExtra("url", uri.getQueryParameter("url"));
                            shareTop.putExtra("title", dto.getmTopicTitle());
                            shareTop.putExtra("track_type", "home_topic_topic");
                            shareTop.putExtra("track_type_id", dto.getID());
                            startActivity(shareTop);
//                            TrackManager.getInstance()
//                            .addTrack(TrackUrl.ITEM_CLICK + dto.getID() + "&type=home_topic_topic");
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
        TextView mBeautyListItemTextView;
        ImageView mBeautyListItemImageView;
    }
}
