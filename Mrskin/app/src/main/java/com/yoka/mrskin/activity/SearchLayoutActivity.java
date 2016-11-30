package com.yoka.mrskin.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKExperience;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.managers.YKSearchManager;
import com.yoka.mrskin.model.managers.YKSearchManager.YKSearchType;
import com.yoka.mrskin.model.managers.YKSearchManager.searchCallback;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.YKSharelUtil;

public class SearchLayoutActivity extends BaseActivity implements
OnClickListener, IXListViewListener
{
    private String mSearchText;
    private EditText mSearchEdit;
    private XListView mXListView;
    private XListView mXExperienceListView;
    private XListView mXInformationListView;
    private LinearLayout mTagLayout;
    private ArrayList<YKProduct> mProducts;
    private ProductAdapter mProductAdapter;
    private TopicDataAdapter mTopicDataAdapter;
    private TopicNewAdapter mTopicNewAdapter;
    private ArrayList<YKExperience> mTopicDatas = new ArrayList<YKExperience>();
    private ArrayList<YKTopicData> mInformationDatas = new ArrayList<YKTopicData>();
    private TextView mCancle, mNoSearchText;
    private RadioButton mTagPro, mTagXin, mTagNew;
    private int mProductPageIndex = 2;
    private int mExperiencePageIndex = 2;
    private int mInformationPageIndex = 2;
    private YKSearchType mCurrentTab;
    private Context mContext;
    private XListViewFooter mListViewFooter;
    private CustomButterfly mCustomButterfly = null;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).considerExifParams(true)
				.build();
        mProductAdapter = new ProductAdapter();
        mTopicDataAdapter = new TopicDataAdapter(mTopicDatas);
        mTopicNewAdapter = new TopicNewAdapter();
        mContext=this;
        init();
        //initgetData();
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_OPEN + "SearchLayoutActivity");
    }

    @Override
    protected void onDestroy() {
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_CLOSE + "SearchLayoutActivity");
        super.onDestroy();
    }

    private void init() {
        mCurrentTab = YKSearchType.SEARCHTYPE_PRODUCT;
        mCancle = (TextView) findViewById(R.id.all_search_cancle);
        mCancle.setOnClickListener(this);
        mTagPro = (RadioButton) findViewById(R.id.search_layout_tagpro);
        mTagPro.setOnClickListener(this);
        mTagPro.setChecked(true);
        mTagXin = (RadioButton) findViewById(R.id.search_layout_tagxin);
        mTagXin.setOnClickListener(this);
        mTagNew = (RadioButton) findViewById(R.id.search_layout_tagnew);
        mTagNew.setOnClickListener(this);

        mNoSearchText = (TextView) findViewById(R.id.search_no_text);

        mSearchEdit = (EditText) findViewById(R.id.all_search_edittext);
        mXListView = (XListView) findViewById(R.id.search_layout_list);
        mXListView.setXListViewListener(this);
        mListViewFooter = new XListViewFooter(mContext);
        mXListView.setOnScrollListener(new OnScrollListener()
        {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                        mListViewFooter.setState(XListViewFooter.STATE_READY);
                        mXListView.startLoadMore();
                    }
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount)
            {
                
            }
        });
        mXListView.setAdapter(mProductAdapter);
        mXListView.setVisibility(View.VISIBLE);
        mXListView.setPullLoadEnable(true);

        mXExperienceListView = (XListView) findViewById(R.id.search_layout_list_experience);
        mXExperienceListView.setXListViewListener(this);
        mXExperienceListView.setAdapter(mTopicDataAdapter);
        mXExperienceListView.setPullLoadEnable(true);
        mXExperienceListView.setOnScrollListener(new OnScrollListener()
        {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                        mListViewFooter.setState(XListViewFooter.STATE_READY);
                        mXListView.startLoadMore();
                    }
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount)
            {
                
            }
        });

        mXInformationListView = (XListView) findViewById(R.id.search_layout_list_information);
        mXInformationListView.setXListViewListener(this);
        mXInformationListView.setAdapter(mTopicNewAdapter);
        mXInformationListView.setPullLoadEnable(true);
        mXInformationListView.setOnScrollListener(new OnScrollListener()
        {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                        mListViewFooter.setState(XListViewFooter.STATE_READY);
                        mXListView.startLoadMore();
                    }
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount)
            {
                
            }
        });

        mTagLayout = (LinearLayout) findViewById(R.id.search_layout_taglayout);
        mTagLayout.setVisibility(View.GONE);
        mXListView.setVisibility(View.GONE);
        mSearchEdit.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_DOWN){  
                    hideKeyBord();
                    mSearchText = mSearchEdit.getText().toString();
                    if(!TextUtils.isEmpty(mSearchText)){
//                        TrackManager.getInstance().addTrack(
//                                TrackUrl.SEARCH_CLICK + mSearchText);
                        
                        refreshData(true);
                    }
                }  
                return false;
            }
        });
    }

    private void hideKeyBord() {
        // 先隐藏键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
        .hideSoftInputFromWindow(SearchLayoutActivity.this
                .getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 产品适配器
    private class ProductAdapter extends BaseAdapter
    {

        private ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            if (mProducts == null) {
                return 0;
            }
            return mProducts.size();
        }

        @Override
        public Object getItem(int position) {
            if (mProducts == null) {
                return null;
            }
            return mProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            final YKProduct pro = mProducts.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchLayoutActivity.this)
                        .inflate(R.layout.search_layout_item, null);
                viewHolder = new ViewHolder();

                viewHolder.sTitle = (TextView) convertView
                        .findViewById(R.id.search_layout_title);
                viewHolder.sPrice = (TextView) convertView
                        .findViewById(R.id.search_layout_price);
                viewHolder.sMl = (TextView) convertView
                        .findViewById(R.id.search_layout_ml);
                viewHolder.sRatBar = (RatingBar) convertView
                        .findViewById(R.id.search_layout_bar);
                viewHolder.sNoLayout = (TextView) convertView

                        .findViewById(R.id.search_no_layout);

                viewHolder.sBigImage = (ImageView) convertView
                        .findViewById(R.id.search_layout_image);

                convertView.setTag(R.id.search_layout_list, viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView
                        .getTag(R.id.search_layout_list);
            }
            if (pro == null) {
                viewHolder.sNoLayout.setVisibility(View.VISIBLE);
                mSearchText = mSearchEdit.getText().toString();
            } else {
                viewHolder.sNoLayout.setVisibility(View.GONE);
                if (pro.getCover() == null
                        && !TextUtils.isEmpty(pro.getCover().getmURL())) {

                    viewHolder.sBigImage
                    .setBackgroundResource(R.drawable.list_default_bg);
                } else {
                    //                    if(mXListView.isScrolling()){
                    //                        viewHolder.sBigImage.setUrl(null,false);
                    //                    }else{
                    //                    }
                  /*  Glide.with(SearchLayoutActivity.this).load(pro.getCover().getmURL())
            		.into( viewHolder.sBigImage);*/
                    ImageLoader.getInstance().displayImage(pro.getCover().getmURL(), viewHolder.sBigImage, options); 
                }
                if (!TextUtils.isEmpty(pro.getSpecification().getTitle())) {
                    viewHolder.sMl.setText("/"
                            + pro.getSpecification().getTitle());
                }
                float price = 0f;
                if (pro.getSpecification().getPrice() != null) {
                    price = pro.getSpecification().getPrice();
                    // 构造方法的字符格式这里如果小数不足2位,会以0补足
                    DecimalFormat decimalFormat = new DecimalFormat("0");
                    // format 返回的是字符串
                    if (price == 0) {
                        viewHolder.sPrice.setText(getString(R.string.mybag_price));
                    } else {
                        String newPric = decimalFormat.format(price);
                        viewHolder.sPrice.setText("￥" + newPric);
                    }
                }
                viewHolder.sTitle.setText(pro.getTitle());
                viewHolder.sRatBar.setRating(pro.getRating());
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent productPlandetalis = new Intent(
                            SearchLayoutActivity.this, YKWebViewActivity.class);
                    productPlandetalis.putExtra("productdetalis",
                            pro.getDescription_url());
                    productPlandetalis.putExtra("identification", "search_result");
                    productPlandetalis.putExtra("track_type", "product");
                    productPlandetalis.putExtra("track_type_id", pro.getID());
                    startActivity(productPlandetalis);
//                    TrackManager.getInstance()
//                    .addTrack(
//                            TrackUrl.ITEM_CLICK + pro.getID()
//                            + "&type=product");
                }
            });
//            TrackManager.getInstance().addTrack(
//                    TrackUrl.ITEM_EXPOSURE + pro.getID() + "&type=product");
            return convertView;
        }
    }

    // YKTopicDataXin
    private class TopicDataAdapter extends BaseAdapter
    {

        private List<YKExperience> mList;
        private ViewHolderExperience viewHolder = null;
        private DisplayImageOptions options;

        public TopicDataAdapter(List<YKExperience> list)
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
                viewHolder = new ViewHolderExperience();
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
                viewHolder = (ViewHolderExperience) convertView.getTag();
            }

            if (dto.getAuthor().getAge() == 0) {
                viewHolder.experienceAgeTextView.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.experienceAgeTextView.setVisibility(View.VISIBLE);
            }
            if (dto.getImages() != null && dto.getImages().size() != 0) {
                viewHolder.experienceImageView.setVisibility(View.VISIBLE);
           
          /*      Glide.with(SearchLayoutActivity.this).load(dto.getImages().get(0).getmURL())
        		.into( viewHolder.experienceImageView);*/
                ImageLoader.getInstance().displayImage(
                        dto.getImages().get(0).getmURL(),
                        viewHolder.experienceImageView, options);
            } else {
                viewHolder.experienceImageView.setVisibility(View.GONE);
            }
           // Glide.with(SearchLayoutActivity.this).load(dto.getImages().get(0).getmURL()).into( viewHolder.experienceHeadImageView).onLoadStarted(getResources().getDrawable(R.drawable.list_default_bg));
            ImageLoader.getInstance().displayImage(dto.getAuthor().getAvatar().getmURL(), viewHolder.experienceHeadImageView, options); 

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
                viewHolder.experienceSkinTextView.setVisibility(View.INVISIBLE);
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
                            shareTop.putExtra("identification", "search_result");
                            shareTop.putExtra("title", dto.getTitle());
                            shareTop.putExtra("track_type", "home_topic_topic");
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
    static class ViewHolderExperience
    {
    	ImageView experienceImageView;
        TextView experienceNameTextView, experienceAgeTextView,
        experienceHeadTimeTextView, experienceTitleTextView,
        experienceConentTextView, experienceSkinTextView;
        RoundImage experienceHeadImageView;
    }
    // YKTopicDataNew
    private class TopicNewAdapter extends BaseAdapter
    {

        private ViewHolder viewHolder = null;
        private DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.list_default_bg).build();

        @Override
        public int getCount() {
            if (mInformationDatas == null) {
                return 0;
            }
            return mInformationDatas.size();
        }

        @Override
        public Object getItem(int position) {
            if (mInformationDatas == null) {
                return null;
            }
            return mInformationDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            final YKTopicData topic = mInformationDatas.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchLayoutActivity.this)
                        .inflate(R.layout.search_layout_itemnew, null);
                viewHolder = new ViewHolder();

                viewHolder.sTitle = (TextView) convertView
                        .findViewById(R.id.search_layout_newtitle);
                viewHolder.sAuto = (TextView) convertView
                        .findViewById(R.id.search_layout_newauto);

                viewHolder.sRoudImage = (RoundImage) convertView
                        .findViewById(R.id.search_layout_newimage);

                convertView.setTag(R.id.search_layout_list, viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView
                        .getTag(R.id.search_layout_list);
            }
            viewHolder.sTitle.setText(topic.getmTopicTitle());
            if (topic.getmCover() == null
                    && !TextUtils.isEmpty(topic.getmCover().getmURL())) {
                viewHolder.sRoudImage
                .setBackgroundResource(R.drawable.list_default_bg);
            } else {
                try {
                	 ImageLoader.getInstance().displayImage(topic.getmCover().getmURL(), viewHolder.sRoudImage, options); 
       /*             Glide.with(SearchLayoutActivity.this).load(topic.getmCover().getmURL())
            		.into( viewHolder.sRoudImage).onLoadStarted(getResources().getDrawable(R.drawable.default_user_bg));*/
                } catch (Exception e) {
                    viewHolder.sRoudImage.setBackgroundResource(R.drawable.right_nologin);
                }
            }
            viewHolder.sAuto.setText(topic.getmUser());

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent productPlandetalis = new Intent(
                            SearchLayoutActivity.this, YKWebViewActivity.class);
                    productPlandetalis.putExtra("productdetalis",topic.getmTopicUrl());
                    productPlandetalis.putExtra("identification", "search_result");
                    productPlandetalis.putExtra("track_type", "information");
                    productPlandetalis.putExtra("track_type_id", topic.getID());
                    startActivity(productPlandetalis);
//                    TrackManager.getInstance().addTrack(
//                            TrackUrl.ITEM_CLICK + topic.getID()
//                            + "&type=information");
                }
            });
//            TrackManager.getInstance().addTrack(
//                    TrackUrl.ITEM_EXPOSURE + topic.getID()
//                    + "&type=information");
            return convertView;
        }
    }

    private class ViewHolder
    {
        // Product
        private TextView sTitle, sPrice, sMl, sNoLayout;
        private ImageView sBigImage;
        private RatingBar sRatBar;

        // TopicData
        private TextView sAuto;
        private RoundImage sRoudImage;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.all_search_cancle:
            finish();
            break;
        case R.id.search_layout_tagpro:
            mCurrentTab = YKSearchType.SEARCHTYPE_PRODUCT;
            mXListView.setVisibility(View.VISIBLE);
            mXExperienceListView.setVisibility(View.GONE);
            mXInformationListView.setVisibility(View.GONE);
            if (mProducts == null) {
                refreshData(true);
            } else {
                // mXListView.setAdapter(mProductAdapter);
                // mProductAdapter.notifyDataSetChanged();
            }
            mXListView.setSelection(0);
            if(!mSearchEdit.getText().equals(mSearchText)){
                refreshData(true);
            }
            break;
        case R.id.search_layout_tagxin:
            mCurrentTab = YKSearchType.SEARCHTYPE_EXPERIENCE;
            mXListView.setVisibility(View.GONE);
            mXExperienceListView.setVisibility(View.VISIBLE);
            mXInformationListView.setVisibility(View.GONE);
            if (mTopicDatas == null) {
                refreshData(true);
            } else {
                //                 mXListView.setAdapter(mTopicDataAdapter);
                //                 mTopicDataAdapter.notifyDataSetChanged();
            }
            if(!mSearchEdit.getText().equals(mSearchText)){
                refreshData(true);
            }
            mXExperienceListView.setSelection(0);
            break;
        case R.id.search_layout_tagnew:
            mCurrentTab = YKSearchType.SEARCHTYPE_INFORMATION;
            mXListView.setVisibility(View.GONE);
            mXExperienceListView.setVisibility(View.GONE);
            mXInformationListView.setVisibility(View.VISIBLE);
            if (mInformationDatas == null) {
                refreshData(true);
            } else {
                // mXListView.setAdapter(mTopicNewAdapter);
                // mTopicNewAdapter.notifyDataSetChanged();
            }
            if(!mSearchEdit.getText().equals(mSearchText)){
                refreshData(true);
            }
            mXInformationListView.setSelection(0);
            break;
        default:
            break;
        }
    }

    @Override
    public void onRefresh() {
        mSearchText = mSearchEdit.getText().toString();
        refreshData(false);
    }

    @Override
    public void onLoadMore() {
        int index = 2;
        if (mCurrentTab == YKSearchType.SEARCHTYPE_PRODUCT) {
            index = mProductPageIndex;
        } else if (mCurrentTab == YKSearchType.SEARCHTYPE_EXPERIENCE) {
            index = mExperiencePageIndex;
        } else if (mCurrentTab == YKSearchType.SEARCHTYPE_INFORMATION) {
            index = mInformationPageIndex;
        }
        YKSearchManager.getInstance().requestSearchData(mSearchText,
                mCurrentTab, index, new searchCallback() {
            @Override
            public void callback(YKResult result,
                    ArrayList<YKProduct> product,
                    ArrayList<YKExperience> experience,
                    ArrayList<YKTopicData> information) {
                mXListView.stopLoadMore();
                mXExperienceListView.stopLoadMore();
                mXInformationListView.stopLoadMore();
                hideKeyBord();
                AppUtil.dismissDialogSafe(mCustomButterfly);
                if (result.isSucceeded()) {
                    if (mCurrentTab == YKSearchType.SEARCHTYPE_PRODUCT) {
                        ++mProductPageIndex;
                        if (mProducts != null) {
                            mProducts.addAll(product);
                            mTagLayout.setVisibility(View.VISIBLE);
                            mProductAdapter.notifyDataSetChanged();

                        }
                    } else if (mCurrentTab == YKSearchType.SEARCHTYPE_EXPERIENCE) {
                        ++mExperiencePageIndex;
                        if (mTopicDatas != null) {
                            mTopicDatas.addAll(experience);
                            mTagLayout.setVisibility(View.VISIBLE);
                            mTopicDataAdapter.notifyDataSetChanged();

                        }
                    } else if (mCurrentTab == YKSearchType.SEARCHTYPE_INFORMATION) {
                        ++mInformationPageIndex;
                        if (mInformationDatas != null) {
                            mInformationDatas.addAll(information);
                            mTagLayout.setVisibility(View.VISIBLE);
                            mTopicNewAdapter.notifyDataSetChanged();

                        }
                    }
                }
            }
        });
    }

    private void refreshData(final boolean showDialog) {
        if (showDialog) {
            try {
                mCustomButterfly = CustomButterfly.show(this);
            } catch (Exception e) {
                mCustomButterfly = null;
            }
        }
        if (mCurrentTab == YKSearchType.SEARCHTYPE_PRODUCT) {
            mProductPageIndex = 1;
        } else if (mCurrentTab == YKSearchType.SEARCHTYPE_EXPERIENCE) {
            mExperiencePageIndex = 1;
        } else if (mCurrentTab == YKSearchType.SEARCHTYPE_INFORMATION) {
            mInformationPageIndex = 1;
        }

        YKSearchManager.getInstance().requestSearchData(mSearchText,
                mCurrentTab, 1, new searchCallback() {
            @Override
            public void callback(YKResult result,
                    ArrayList<YKProduct> product,
                    ArrayList<YKExperience> experience,
                    ArrayList<YKTopicData> information) {
                mXListView.stopRefresh();
                mXExperienceListView.stopRefresh();
                mXInformationListView.stopRefresh();
                AppUtil.dismissDialogSafe(mCustomButterfly);
                mXExperienceListView.setSelection(0);
                mXInformationListView.setSelection(0);
                mXListView.setSelection(0);
                if (result.isSucceeded()) {
                    if (mCurrentTab == YKSearchType.SEARCHTYPE_PRODUCT) {
                        if (product != null && product.size() > 0) {
                            ++mProductPageIndex;
                            mProducts = product;
                            mXListView.setVisibility(View.VISIBLE);
                            mTagLayout.setVisibility(View.VISIBLE);
                            mNoSearchText.setVisibility(View.GONE);
                            mProductAdapter.notifyDataSetChanged();

                        } else {
                            mTagLayout.setVisibility(View.VISIBLE);
                            mXListView.setVisibility(View.GONE);
                            mNoSearchText.setVisibility(View.VISIBLE);
                            mXExperienceListView
                            .setVisibility(View.GONE);
                            mXInformationListView
                            .setVisibility(View.GONE);
                            mNoSearchText.setText("没有与“" + mSearchText
                                    + "”相关的产品，换个词再试一试");

                        }
                    } else if (mCurrentTab == YKSearchType.SEARCHTYPE_EXPERIENCE) {
                        if (experience != null && experience.size() > 0) {
                            ++mExperiencePageIndex;
                            if(mTopicDatas != null){
                                mTopicDatas.clear();
                            }
                            mTopicDatas.addAll(experience);
                            mTagLayout.setVisibility(View.VISIBLE);
                            mXListView.setVisibility(View.GONE);
                            mNoSearchText.setVisibility(View.GONE);
                            mXExperienceListView
                            .setVisibility(View.VISIBLE);
                            mXInformationListView
                            .setVisibility(View.GONE);
                            mTopicDataAdapter.notifyDataSetChanged();
                        } else {
                            mXExperienceListView
                            .setVisibility(View.GONE);
                            mXInformationListView
                            .setVisibility(View.GONE);
                            mXListView.setVisibility(View.GONE);
                            mNoSearchText.setVisibility(View.VISIBLE);
                            mNoSearchText.setText("没有与“" + mSearchText
                                    + "”相关的心得，换个词再试一试");
                        }
                    } else if (mCurrentTab == YKSearchType.SEARCHTYPE_INFORMATION) {
                        if (information != null
                                && information.size() > 0) {
                            ++mInformationPageIndex;
                            mInformationDatas = information;
                            mTagLayout.setVisibility(View.VISIBLE);
                            mXListView.setVisibility(View.GONE);
                            mNoSearchText.setVisibility(View.GONE);
                            mXExperienceListView
                            .setVisibility(View.GONE);
                            mXInformationListView
                            .setVisibility(View.VISIBLE);
                            mXInformationListView.setDivider(null);
                            mTopicNewAdapter.notifyDataSetChanged();
                        } else {
                            mXExperienceListView
                            .setVisibility(View.GONE);
                            mXInformationListView
                            .setVisibility(View.GONE);
                            mXListView.setVisibility(View.GONE);
                            mNoSearchText.setVisibility(View.VISIBLE);
                            mNoSearchText.setText("没有与“" + mSearchText
                                    + "”相关的资讯，换个词再试一试");
                        }
                    }
                    hideKeyBord();
                } else {
                    Toast.makeText(SearchLayoutActivity.this,
                            (String) result.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
