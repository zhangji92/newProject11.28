package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
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
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKBrand;
import com.yoka.mrskin.model.data.YKCategory;
import com.yoka.mrskin.model.data.YKEfficacy;
import com.yoka.mrskin.model.data.YKImage;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.managers.YKSearchManager;
import com.yoka.mrskin.model.managers.YKSearchManager.DataCallback;
import com.yoka.mrskin.model.managers.YKSearchManager.RecommendationCallback;
import com.yoka.mrskin.model.managers.YKSearchManager.TopicCallback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CharacterParser;
import com.yoka.mrskin.util.LoadingDialog;
import com.yoka.mrskin.util.PinyinComparator;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.SideBar;
import com.yoka.mrskin.util.SideBar.OnTouchingLetterChangedListener;

enum SearchTab {
    SEARCH_TAB_BRAND, SEARCH_TAB_CATEGORY, SEARCH_TAB_EFFICACY, SEARCH_TAB_EXPERIENCE, SEARCH_TAB_INFORMATION
};

@SuppressLint({ "DefaultLocale", "InflateParams" })
public class SearchAllActivity extends BaseActivity implements OnClickListener,
IXListViewListener
{
    private SearchTab mCurrentSearchTab;
    private SideBar mSideBar;
    private XListView mListView;
    private ListView mListViewSour;

    private FrameLayout mSourLayout;
    private SortAdapter mSortAdapter;
    private android.widget.LinearLayout.LayoutParams mLayoutParams;
    private RelativeLayout mSearchLayout;
    private ArrayList<YKEfficacy> mEfficacies;
    private ArrayList<YKCategory> mCategories;
    private ArrayList<YKTopicData> mYkTopicDatas;
    private ArrayList<YKTopicData> mInformationDatas;
    private View mOne, mTwo, mThree, mFour, mFive, mBack;
    private SearchEffectAdapter mSearchEffectAdapter;
    private LinearLayout mSearchTagOne, mSearchTagTwo;
    private SearchRecommendAdapter mSearchRecommendAdapter;
    private SearchExperienceAdapter mSearchExperienceAdapter;
    private SearchInformationAdapter mSearchInformationAdapter;
    private TextView mTagTextOne, mTagTextTwo, mTagTextThree, mTagTextFour,
    mTagTextFive, mTagTextSix;
    private ArrayList<TextView> mRecommendationTextViews;
    private TextView mSearchbrand, mSearchEffect, mSearchType,
    mSearchExperience, mSearchRead;
    private View mCategoryEfficacyScrollView;
    private XListViewFooter mListViewFooter;
    private LinearLayout mCategoryEfficacyContainer;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private ArrayList<YKBrand> mBrands = new ArrayList<YKBrand>();
    private ArrayList<YKBrand> mRecommendationBrands;
    private ArrayList<YKCategory> mRecommendationCategoryies;
    private ArrayList<YKEfficacy> mRecommendationEfficacies;
    private DisplayImageOptions options;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bigall);

        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).considerExifParams(true)
				.build();
        setupView();
        initGeneralData();
        initRecommendationData();
        mCurrentSearchTab = SearchTab.SEARCH_TAB_BRAND;
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_OPEN + "SearchAllActivity");
    }

    private void initRecommendationData() {
        YKSearchManager.getInstance().requestRecommendations(
                new RecommendationCallback() {

                    @Override
                    public void callback(YKResult result,
                            ArrayList<YKBrand> brands,
                            ArrayList<YKCategory> categories,
                            ArrayList<YKEfficacy> efficacies,
                            ArrayList<YKImage> mCover)
                    {
                        if (result.isSucceeded()) {
                            mRecommendationBrands = brands;
                            mRecommendationCategoryies = categories;
                            mRecommendationEfficacies = efficacies;
                            if (mRecommendationBrands != null) {
                                TextView textView;
                                YKBrand brand;
                                for (int i = 0; i < mRecommendationBrands
                                        .size(); ++i) {
                                    if (i >= mRecommendationTextViews.size())
                                        break;
                                    textView = mRecommendationTextViews.get(i);
                                    brand = mRecommendationBrands.get(i);
                                    textView.setText(brand.getmTitle());
                                    textView.setVisibility(View.VISIBLE);
                                }

                                for (int i = mRecommendationBrands.size(); i < mRecommendationTextViews
                                        .size(); ++i) {
                                    textView = mRecommendationTextViews.get(i);
                                    textView.setVisibility(View.INVISIBLE);
                                }
                            }

                        }

                    }
                });
    }

    private void initCategories() {
        if (mRecommendationCategoryies == null) {
            YKSearchManager.getInstance().requestRecommendations(
                    new RecommendationCallback() {

                        @Override
                        public void callback(YKResult result,
                                ArrayList<YKBrand> brands,
                                ArrayList<YKCategory> categories,
                                ArrayList<YKEfficacy> efficacies,
                                ArrayList<YKImage> mCover)
                        {
                            if (result.isSucceeded()) {
                                mRecommendationCategoryies = categories;
                                setupRecommendationCategory();
                            }

                        }
                    });
        } else {
            setupRecommendationCategory();
        }

        if (mCategories == null) {
            YKSearchManager.getInstance().requestGeneralData(
                    new DataCallback() {
                        @Override
                        public void callback(YKResult result, String version,
                                ArrayList<YKBrand> brands,
                                ArrayList<YKCategory> categories,
                                ArrayList<YKEfficacy> efficacies) {
                            // mListView.stopRefresh();
                            if (result.isSucceeded()) {
                                mBrands.clear();
                                mBrands.addAll(filledData(brands));
                                mCategories = categories;
                                mEfficacies = efficacies;
                                setupCategories();

                            } else {
                                try {
                                    if (TextUtils.isEmpty((String) result
                                            .getMessage())) {
                                        Toast.makeText(SearchAllActivity.this,
                                                (String) result.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    });
        } else {
            setupCategories();
        }
    }

    private void initEfficacies() {
        if (mRecommendationEfficacies == null
                || (mRecommendationEfficacies != null && mRecommendationEfficacies
                .size() == 0)) {
            YKSearchManager.getInstance().requestRecommendations(
                    new RecommendationCallback() {

                        @Override
                        public void callback(YKResult result,
                                ArrayList<YKBrand> brands,
                                ArrayList<YKCategory> categories,
                                ArrayList<YKEfficacy> efficacies,
                                ArrayList<YKImage> mCover)
                        {
                            if (result.isSucceeded()) {

                                mEfficacies = efficacies;
                                setupRecommendationEfficacy();
                            }

                        }
                    });
        } else {
            setupRecommendationEfficacy();
        }

        if (mEfficacies == null) {
            YKSearchManager.getInstance().requestGeneralData(
                    new DataCallback() {
                        @Override
                        public void callback(YKResult result, String version,
                                ArrayList<YKBrand> brands,
                                ArrayList<YKCategory> categories,
                                ArrayList<YKEfficacy> efficacies) {

                            if (result.isSucceeded()) {
                                mBrands.clear();
                                mBrands.addAll(filledData(brands));
                                mCategories = categories;
                                mEfficacies = efficacies;
                                setupEfficacies();

                            } else {
                                try {
                                    if (TextUtils.isEmpty((String) result
                                            .getMessage())) {
                                        Toast.makeText(SearchAllActivity.this,
                                                (String) result.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    });
        } else {
            setupEfficacies();
        }
    }

    private void refreshExperience() {
        // final LoadingDialog dialog = new LoadingDialog(
        // SearchAllActivity.this);
        // AppUtil.showDialogSafe(dialog);
        YKSearchManager.getInstance().requestComment("0", new TopicCallback() {
            @Override
            public void callback(YKResult result, ArrayList<YKTopicData> topics) {
                mListView.stopRefresh();
                // AppUtil.dismissDialogSafe(dialog);
                if (result.isSucceeded()) {
                    mYkTopicDatas = topics;
                    if (mYkTopicDatas != null) {
                        // mSourLayout.setVisibility(View.GONE);
                        // mListView.setVisibility(View.VISIBLE);
                        mSearchExperienceAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void inintExperience() {
        final LoadingDialog dialog = new LoadingDialog(SearchAllActivity.this);
        AppUtil.showDialogSafe(dialog);
        YKSearchManager.getInstance().requestComment("0", new TopicCallback() {
            @Override
            public void callback(YKResult result, ArrayList<YKTopicData> topics) {
                mListView.stopRefresh();
                AppUtil.dismissDialogSafe(dialog);
                if (result.isSucceeded()) {
                    mYkTopicDatas = topics;
                    if (mYkTopicDatas != null) {
                        // mSourLayout.setVisibility(View.GONE);
                        // mListView.setVisibility(View.VISIBLE);
                        mSearchExperienceAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void initformation() {
        final LoadingDialog dialog = new LoadingDialog(SearchAllActivity.this);
        AppUtil.showDialogSafe(dialog);
        YKSearchManager.getInstance().requestInformation("0",
                new TopicCallback() {
            @Override
            public void callback(YKResult result,
                    ArrayList<YKTopicData> topics) {
                mListView.stopRefresh();
                AppUtil.dismissDialogSafe(dialog);
                if (result.isSucceeded()) {
                    mInformationDatas = topics;
                    mSearchInformationAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadmoreExperience() {
        // final LoadingDialog dialog = new LoadingDialog(
        // SearchAllActivity.this);
        // AppUtil.showDialogSafe(dialog);
        YKTopicData topic = mYkTopicDatas.get(mYkTopicDatas.size() - 1);
        YKSearchManager.getInstance().requestComment(topic.getID(),
                new TopicCallback() {
            @Override
            public void callback(YKResult result,
                    ArrayList<YKTopicData> topics) {
                mListView.stopLoadMore();
                // AppUtil.dismissDialogSafe(dialog);
                if (result.isSucceeded()) {

                    if (topics != null) {
                        mYkTopicDatas.addAll(topics);
                        mSearchExperienceAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void loadmoreInformation() {
        // final LoadingDialog dialog = new LoadingDialog(
        // SearchAllActivity.this);
        // AppUtil.showDialogSafe(dialog);
        YKTopicData topic = mInformationDatas.get(mInformationDatas.size() - 1);
        YKSearchManager.getInstance().requestInformation(topic.getID(),
                new TopicCallback() {
            @Override
            public void callback(YKResult result,
                    ArrayList<YKTopicData> topics) {
                mListView.stopLoadMore();
                // AppUtil.dismissDialogSafe(dialog);
                if (result.isSucceeded()) {
                    if (topics != null) {
                        mInformationDatas.addAll(topics);
                        mSearchInformationAdapter
                        .notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void requestInformation() {
        // final LoadingDialog dialog = new LoadingDialog(
        // SearchAllActivity.this);
        // AppUtil.showDialogSafe(dialog);
        YKSearchManager.getInstance().requestInformation("0",
                new TopicCallback() {
            @Override
            public void callback(YKResult result,
                    ArrayList<YKTopicData> topics) {
                mListView.stopRefresh();
                // AppUtil.dismissDialogSafe(dialog);
                if (result.isSucceeded()) {
                    mInformationDatas = topics;
                    mSearchInformationAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initGeneralData() {
        final LoadingDialog dialog = new LoadingDialog(SearchAllActivity.this);
        AppUtil.showDialogSafe(dialog);
        YKSearchManager.getInstance().requestGeneralData(new DataCallback() {
            @Override
            public void callback(YKResult result, String version,
                    ArrayList<YKBrand> brands,
                    ArrayList<YKCategory> categories,
                    ArrayList<YKEfficacy> efficacies) {
                AppUtil.dismissDialogSafe(dialog);
                // mListView.stopRefresh();
                if (result.isSucceeded()) {
                    mBrands.clear();
                    mBrands.addAll(filledData(brands));
                    mCategories = categories;
                    mEfficacies = efficacies;

                    // setup brand
                    if (mBrands != null && mBrands.size() > -1) {
                        // 根据a-z进行排序源数据
                        mSourLayout.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        Collections.sort(mBrands, pinyinComparator);
                        mSortAdapter.notifyDataSetChanged();

                    }
                } else {
                    try {
                        if (TextUtils.isEmpty((String) result.getMessage())) {
                            Toast.makeText(SearchAllActivity.this,
                                    (String) result.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private void setupView() {
        // setup listview
        mListView = (XListView) findViewById(R.id.search_bigall_list);
        mListView.setDivider(null);
        mListView.setXListViewListener(this);
        mListView.setPullLoadEnable(true);
        mListViewFooter = new XListViewFooter(SearchAllActivity.this);
        mListView.setOnScrollListener(new OnScrollListener()
        {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                        mListViewFooter.setState(XListViewFooter.STATE_READY);
                        mListView.startLoadMore();
                    }
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount)
            {
                
            }
        });
        mListViewSour = (ListView) findViewById(R.id.search_bigall_listtwo);
        mSourLayout = (FrameLayout) findViewById(R.id.search_sour_fragment);
        mSearchTagOne = (LinearLayout) findViewById(R.id.search_tag_layoutone);
        mSearchTagTwo = (LinearLayout) findViewById(R.id.search_tag_layouttwo);

        mCategoryEfficacyScrollView = findViewById(R.id.search_home_category_efficacy_container);
        mCategoryEfficacyContainer = (LinearLayout) findViewById(R.id.search_container);

        // back
        mBack = findViewById(R.id.search_bigall_cancle);
        mBack.setOnClickListener(this);

        // search
        mSearchLayout = (RelativeLayout) findViewById(R.id.search);
        mSearchLayout.setOnClickListener(this);

        // side bar only for brand
        mSideBar = (SideBar) findViewById(R.id.search_sour_sidrbar);

        // 设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mSortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListViewSour.setSelection(position);
                }
            }
        });

        mSearchbrand = (TextView) findViewById(R.id.search_bigall_brand);
        mSearchbrand.setOnClickListener(this);
        mSearchEffect = (TextView) findViewById(R.id.search_bigall_effect);
        mSearchEffect.setOnClickListener(this);
        mSearchExperience = (TextView) findViewById(R.id.search_bigall_experience);
        mSearchExperience.setOnClickListener(this);
        mSearchRead = (TextView) findViewById(R.id.search_bigall_read);
        mSearchRead.setOnClickListener(this);
        mSearchType = (TextView) findViewById(R.id.search_bigall_type);
        mSearchType.setOnClickListener(this);

        // give tab brand category efficacy ...
        mOne = findViewById(R.id.search_view_one);
        mTwo = findViewById(R.id.search_view_two);
        mThree = findViewById(R.id.search_view_three);
        mFour = findViewById(R.id.search_view_four);
        mFive = findViewById(R.id.search_view_five);

        mTagTextOne = (TextView) findViewById(R.id.search_tag_text_one);
        mTagTextOne.setOnClickListener(this);
        mTagTextTwo = (TextView) findViewById(R.id.search_tag_text_two);
        mTagTextTwo.setOnClickListener(this);
        mTagTextThree = (TextView) findViewById(R.id.search_tag_text_three);
        mTagTextThree.setOnClickListener(this);
        mTagTextFour = (TextView) findViewById(R.id.search_tag_text_four);
        mTagTextFour.setOnClickListener(this);
        mTagTextFive = (TextView) findViewById(R.id.search_tag_text_five);
        mTagTextFive.setOnClickListener(this);
        mTagTextSix = (TextView) findViewById(R.id.search_tag_text_six);
        mTagTextSix.setOnClickListener(this);

        mRecommendationTextViews = new ArrayList<TextView>();
        mRecommendationTextViews.add(mTagTextOne);
        mRecommendationTextViews.add(mTagTextTwo);
        mRecommendationTextViews.add(mTagTextThree);
        mRecommendationTextViews.add(mTagTextFour);
        mRecommendationTextViews.add(mTagTextFive);
        mRecommendationTextViews.add(mTagTextSix);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        // 品牌适配器
        mSortAdapter = new SortAdapter(mBrands);
        mListViewSour.setAdapter(mSortAdapter);

        // 分类适配器
        mSearchRecommendAdapter = new SearchRecommendAdapter();
        mListView.setAdapter(mSearchRecommendAdapter);

        // 资讯适配器
        mSearchInformationAdapter = new SearchInformationAdapter();
        mListView.setAdapter(mSearchInformationAdapter);

        // 心得适配器
        mSearchExperienceAdapter = new SearchExperienceAdapter();
        // mListView.setAdapter(mSearchExperienceAdapter);

        // 功效适配器
        mSearchEffectAdapter = new SearchEffectAdapter();
        mListView.setAdapter(mSearchEffectAdapter);

    }

    /**
     * 为ListView填充数据
     * 
     * @param date
     * @return
     */
    private ArrayList<YKBrand> filledData(ArrayList<YKBrand> date) {
        ArrayList<YKBrand> mSortList = new ArrayList<YKBrand>();

        for (int i = 0; i < date.size(); i++) {
            YKBrand sortModel = new YKBrand();
            sortModel.setmTitle(date.get(i).getmTitle());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getmTitle());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setmSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setmSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 分类适配器
     */
    public class SearchRecommendAdapter extends BaseAdapter
    {
        private ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            if (mCategories == null) {
                return 0;
            }
            return mCategories.size();
        }

        @Override
        public Object getItem(int position) {
            if (mCategories == null) {
                return null;
            }
            return mCategories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final YKCategory category = mCategories.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchAllActivity.this)
                        .inflate(R.layout.search_fragment_test, null);
                viewHolder = new ViewHolder();
                viewHolder.sTextType = (TextView) convertView
                        .findViewById(R.id.search_text_face);
                viewHolder.sLineanLayout = (LinearLayout) convertView
                        .findViewById(R.id.search_all_vertical_lauoyt);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.sTextType.setText(category.getTitle());
            viewHolder.sLineanLayout.removeAllViews();
            try {
                for (int i = 0; i < category.getSubCategories().size() / 3; i++) {

                    LinearLayout layout = new LinearLayout(
                            SearchAllActivity.this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    mLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView textone = new TextView(SearchAllActivity.this);
                    textone.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT, 1f));
                    TextView texttwo = new TextView(SearchAllActivity.this);
                    texttwo.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT, 1f));
                    TextView texthtree = new TextView(SearchAllActivity.this);
                    texthtree.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT, 1f));
                    ;

                    textone.setText(category.getSubCategories().get(i)
                            .getTitle());
                    texttwo.setText(category.getSubCategories().get(i + 1)
                            .getTitle());
                    texthtree.setText(category.getSubCategories().get(i + 2)
                            .getTitle());
                    // viewHolder.sSearchTextOne.setText(category.getSubCategories().get(i).getTitle());
                    // viewHolder.sSearchTextTwo.setText(category.getSubCategories().get(i+1).getTitle());
                    // viewHolder.sSearchTextThree.setText(category.getSubCategories().get(i+2).getTitle());
                    layout.addView(textone);
                    layout.addView(texttwo);
                    layout.addView(texthtree);
                    viewHolder.sLineanLayout.addView(layout, mLayoutParams);
                }
            } catch (Exception e) {
            }

            return convertView;
        }
    }

    /**
     * 心得
     */
    public class SearchExperienceAdapter extends BaseAdapter
    {
        private ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            if (mYkTopicDatas == null) {
                return 0;
            }
            return mYkTopicDatas.size();
        }

        @Override
        public Object getItem(int position) {
            if (mYkTopicDatas == null) {
                return null;
            }
            return mYkTopicDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final YKTopicData topic = mYkTopicDatas.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchAllActivity.this)
                        .inflate(R.layout.search_experience_listitem, null);
                viewHolder = new ViewHolder();
                viewHolder.sTextTitle = (TextView) convertView
                        .findViewById(R.id.search_experience_title);
                viewHolder.sTextDesc = (TextView) convertView
                        .findViewById(R.id.search_experience_desc);
                viewHolder.sTextType = (TextView) convertView
                        .findViewById(R.id.search_experience_user);
                viewHolder.sImage = (RoundImage) convertView
                        .findViewById(R.id.search_experience_image);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.sTextTitle.setText(topic.getmTopicTitle());
            viewHolder.sTextDesc.setText(topic.getmTopicDesc());
            viewHolder.sTextType.setText(topic.getmUser());

            try {
            	/* Glide.with(SearchAllActivity.this).load( topic.getImage().getmURL())
         		.into( viewHolder.sImage);*/
            	 ImageLoader.getInstance().displayImage(topic.getImage().getmURL(), viewHolder.sImage, options);
            } catch (Exception e) {
                viewHolder.sImage
                .setBackgroundResource(R.drawable.list_default_bg);
            }
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent experience = new Intent(SearchAllActivity.this,
                            YKWebViewActivity.class);
                    Uri uri = Uri.parse(topic.getmTopicUrl());
                    String newexperienceUrl = uri.getQueryParameter("url");
                    experience.putExtra("url", newexperienceUrl);
                    experience.putExtra("track_type",
                            "experience_recommendation");
                    experience.putExtra("track_type_id", topic.getID());
                    experience.putExtra("identification", "search_result");
                    if (!TextUtils.isEmpty(newexperienceUrl)) {
                        startActivity(experience);
//                        TrackManager.getInstance().addTrack(
//                                TrackUrl.ITEM_CLICK + topic.getID()
//                                + "&type=experience_recommendation");
                    }
                }
            });
//            TrackManager.getInstance().addTrack(
//                    TrackUrl.ITEM_EXPOSURE + topic.getID()
//                    + "&type=experience_recommendation");
            return convertView;
        }
    }

    /**
     * 功效适配器
     */
    public class SearchEffectAdapter extends BaseAdapter
    {
        private ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            if (mEfficacies == null) {
                return 0;
            }
            return mEfficacies.size();
        }

        @Override
        public Object getItem(int position) {
            if (mEfficacies == null) {
                return null;
            }
            return mEfficacies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final YKEfficacy efficacy = mEfficacies.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchAllActivity.this)
                        .inflate(R.layout.search_fragment_test, null);
                viewHolder = new ViewHolder();
                viewHolder.sTextType = (TextView) convertView
                        .findViewById(R.id.search_text_face);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.sTextType.setText(efficacy.getTitle());

            return convertView;
        }
    }

    /**
     * 资讯
     */
    public class SearchInformationAdapter extends BaseAdapter
    {
        private ViewHolder viewHolder = null;

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
        public View getView(int position, View convertView, ViewGroup parent) {

            final YKTopicData topic = mInformationDatas.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchAllActivity.this)
                        .inflate(R.layout.search_info_listitem, null);
                viewHolder = new ViewHolder();
                viewHolder.sTextTitle = (TextView) convertView
                        .findViewById(R.id.search_info_title);
                viewHolder.sTextType = (TextView) convertView
                        .findViewById(R.id.search_info_type);
                viewHolder.sImage = (RoundImage) convertView
                        .findViewById(R.id.search_info_image);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (!TextUtils.isEmpty(topic.getmTopic().getmTopicTitle())) {
                viewHolder.sTextTitle.setText(topic.getmTopic()
                        .getmTopicTitle());
            }
            viewHolder.sTextType.setText(topic.getmTopic().getmType());

            try {
            	ImageLoader.getInstance().displayImage(topic.getmTopic().getImage().getmURL(), viewHolder.sImage, options);  
          /* 	 Glide.with(SearchAllActivity.this).load( topic.getmTopic().getImage().getmURL())
      		.into( viewHolder.sImage);*/
            } catch (Exception e) {
                viewHolder.sImage
                .setBackgroundResource(R.drawable.list_default_bg);
            }
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent newRead = new Intent(SearchAllActivity.this,
                            YKWebViewActivity.class);
                    Uri uri = Uri.parse(topic.getmTopic().getmTopicUrl());
                    String newreadUrl = uri.getQueryParameter("url");
                    newRead.putExtra("url", newreadUrl);
                    newRead.putExtra("track_type", "information_recommendation");
                    newRead.putExtra("identification", "search_result");
                    newRead.putExtra("track_type_id", topic.getmTopic().getID());
                    startActivity(newRead);
//                    TrackManager.getInstance().addTrack(
//                            TrackUrl.ITEM_CLICK + topic.getID()
//                            + "&type=information_recommendation");
                }
            });
//            TrackManager.getInstance().addTrack(
//                    TrackUrl.ITEM_EXPOSURE + topic.getID()
//                    + "&type=experience_recommendation");
            return convertView;
        }
    }

    private class SortAdapter extends BaseAdapter implements SectionIndexer
    {

        private ArrayList<YKBrand> list = null;
        private ViewHolder viewHolder = null;

        public SortAdapter(ArrayList<YKBrand> list)
        {
            this.list = list;
        }

        public int getCount() {
            if (mBrands == null) {
                return 0;
            }
            return mBrands.size();
        }

        public Object getItem(int position) {
            if (mBrands == null) {
                return null;
            }
            return mBrands.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                ViewGroup parent) {
            final YKBrand brand = mBrands.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(SearchAllActivity.this)
                        .inflate(R.layout.search_sour_item, null);
                viewHolder.tvTitle = (TextView) convertView
                        .findViewById(R.id.title);
                viewHolder.tvLetter = (TextView) convertView
                        .findViewById(R.id.catalog);
                viewHolder.tSourView = convertView
                        .findViewById(R.id.search_sour_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tSourView.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(brand.getmSortLetters());
            } else {
                viewHolder.tvLetter.setVisibility(View.GONE);
                viewHolder.tSourView.setVisibility(View.GONE);
            }

            viewHolder.tvTitle.setText(brand.getmTitle());
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent brandLayout = new Intent(SearchAllActivity.this,
                            SearchLayoutActivity.class);
                    brandLayout.putExtra("searchBrand", brand.getmTitle());
                    startActivity(brandLayout);

                }
            });
            return convertView;

        }

        /**
         * 根据ListView的当前位置获取分类的首字母的Char ascii值
         */
        public int getSectionForPosition(int position) {
            return list.get(position).getmSortLetters().charAt(0);
        }

        /**
         * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
         */
        public int getPositionForSection(int section) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = list.get(i).getmSortLetters();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }

            return -1;
        }

        /**
         * 提取英文的首字母，非英文字母用#代替。
         * 
         * @param str
         * @return
         */
        @SuppressWarnings("unused")
        private String getAlpha(String str) {
            String sortStr = str.trim().substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortStr.matches("[A-Z]")) {
                return sortStr;
            } else {
                return "#";
            }
        }

        @Override
        public Object[] getSections() {
            return null;
        }
    }

    class ViewHolder
    {
        // searchexperience
        private TextView sTextTitle, sTextDesc;
        private RoundImage sImage;

        // searchrecommend
        private TextView sTextType;
        private LinearLayout sLineanLayout;

        // searchsort
        private TextView tvLetter, tvTitle;
        private View tSourView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        // 品牌
        case R.id.search_bigall_brand:
            initRecommendationData();
            mOne.setVisibility(View.VISIBLE);
            mTwo.setVisibility(View.INVISIBLE);
            mThree.setVisibility(View.INVISIBLE);
            mFour.setVisibility(View.INVISIBLE);
            mFive.setVisibility(View.INVISIBLE);
            mSearchTagOne.setVisibility(View.VISIBLE);
            mSearchTagTwo.setVisibility(View.VISIBLE);
            mSearchbrand.setTextColor(0xffffffff);
            mSearchEffect.setTextColor(0xffD3D3D3);
            mSearchExperience.setTextColor(0xffD3D3D3);
            mSearchRead.setTextColor(0xffD3D3D3);
            mSearchType.setTextColor(0xffD3D3D3);
            mListView.setVisibility(View.GONE);
            mCategoryEfficacyScrollView.setVisibility(View.GONE);
            mSourLayout.setVisibility(View.VISIBLE);
            mCurrentSearchTab = SearchTab.SEARCH_TAB_BRAND;
            break;
        case R.id.search:
            Intent it = new Intent(SearchAllActivity.this,
                    SearchLayoutActivity.class);
            startActivity(it);
            break;
        case R.id.search_bigall_cancle:
            finish();
            break;
            // 功效
        case R.id.search_bigall_effect:
            mOne.setVisibility(View.INVISIBLE);
            mTwo.setVisibility(View.INVISIBLE);
            mThree.setVisibility(View.VISIBLE);
            mFour.setVisibility(View.INVISIBLE);
            mFive.setVisibility(View.INVISIBLE);
            mSearchTagOne.setVisibility(View.VISIBLE);
            mSearchTagTwo.setVisibility(View.VISIBLE);
            mSearchbrand.setTextColor(0xffD3D3D3);
            mSearchEffect.setTextColor(0xffffffff);
            mSearchExperience.setTextColor(0xffD3D3D3);
            mSearchRead.setTextColor(0xffD3D3D3);
            mSearchType.setTextColor(0xffD3D3D3);
            initEfficacies();

            mCurrentSearchTab = SearchTab.SEARCH_TAB_EFFICACY;
            mSourLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mCategoryEfficacyScrollView.setVisibility(View.VISIBLE);
            break;
            // 心得
        case R.id.search_bigall_experience:
            mOne.setVisibility(View.INVISIBLE);
            mTwo.setVisibility(View.INVISIBLE);
            mThree.setVisibility(View.INVISIBLE);
            mFour.setVisibility(View.VISIBLE);
            mFive.setVisibility(View.INVISIBLE);
            mSearchTagOne.setVisibility(View.GONE);
            mSearchTagTwo.setVisibility(View.GONE);
            mSearchbrand.setTextColor(0xffD3D3D3);
            mSearchEffect.setTextColor(0xffD3D3D3);
            mSearchExperience.setTextColor(0xffffffff);
            mSearchRead.setTextColor(0xffD3D3D3);
            mSearchType.setTextColor(0xffD3D3D3);

            mListView.setAdapter(mSearchExperienceAdapter);
            mSearchExperienceAdapter.notifyDataSetChanged();
            mCategoryEfficacyScrollView.setVisibility(View.GONE);
            mSourLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mCurrentSearchTab = SearchTab.SEARCH_TAB_EXPERIENCE;
            if (mYkTopicDatas == null
                    || (mYkTopicDatas != null && mYkTopicDatas.size() <= 0)) {
                inintExperience();
            }
            break;
            // 资讯
        case R.id.search_bigall_read:
            mOne.setVisibility(View.INVISIBLE);
            mTwo.setVisibility(View.INVISIBLE);
            mThree.setVisibility(View.INVISIBLE);
            mFour.setVisibility(View.INVISIBLE);
            mFive.setVisibility(View.VISIBLE);
            mSearchTagOne.setVisibility(View.GONE);
            mSearchTagTwo.setVisibility(View.GONE);
            mSearchbrand.setTextColor(0xffD3D3D3);
            mSearchEffect.setTextColor(0xffD3D3D3);
            mSearchExperience.setTextColor(0xffD3D3D3);
            mSearchRead.setTextColor(0xffffffff);
            mSearchType.setTextColor(0xffD3D3D3);
            mListView.setAdapter(mSearchInformationAdapter);
            mSearchInformationAdapter.notifyDataSetChanged();

            mCurrentSearchTab = SearchTab.SEARCH_TAB_INFORMATION;
            mSourLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mCategoryEfficacyScrollView.setVisibility(View.GONE);
            if (mInformationDatas == null
                    || (mInformationDatas != null && mInformationDatas.size() <= 0)) {
                initformation();
            }
            break;
            // 分类
        case R.id.search_bigall_type:
            mOne.setVisibility(View.INVISIBLE);
            mTwo.setVisibility(View.VISIBLE);
            mThree.setVisibility(View.INVISIBLE);
            mFour.setVisibility(View.INVISIBLE);
            mFive.setVisibility(View.INVISIBLE);
            mSearchTagOne.setVisibility(View.VISIBLE);
            mSearchTagTwo.setVisibility(View.VISIBLE);
            mSearchbrand.setTextColor(0xffD3D3D3);
            mSearchEffect.setTextColor(0xffD3D3D3);
            mSearchExperience.setTextColor(0xffD3D3D3);
            mSearchRead.setTextColor(0xffD3D3D3);
            mSearchType.setTextColor(0xffffffff);
            initCategories();
            mListView.setAdapter(mSearchRecommendAdapter);
            mSearchRecommendAdapter.notifyDataSetChanged();
            mCurrentSearchTab = SearchTab.SEARCH_TAB_CATEGORY;
            mListView.setVisibility(View.GONE);
            mSourLayout.setVisibility(View.GONE);
            mCategoryEfficacyScrollView.setVisibility(View.VISIBLE);
            break;
        case R.id.search_tag_text_one:
            if (AppUtil.isNetworkAvailable(SearchAllActivity.this)) {

                Intent search_tag_one = new Intent(SearchAllActivity.this,
                        SearchLayoutActivity.class);
                search_tag_one.putExtra("searchBrand", mTagTextOne.getText()
                        .toString());
                startActivity(search_tag_one);
            } else {
                Toast.makeText(SearchAllActivity.this, R.string.intent_no,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.search_tag_text_two:
            if (AppUtil.isNetworkAvailable(SearchAllActivity.this)) {

                Intent search_tag_two = new Intent(SearchAllActivity.this,
                        SearchLayoutActivity.class);
                search_tag_two.putExtra("searchBrand", mTagTextTwo.getText()
                        .toString());
                startActivity(search_tag_two);
            } else {
                Toast.makeText(SearchAllActivity.this, R.string.intent_no,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.search_tag_text_three:
            if (AppUtil.isNetworkAvailable(SearchAllActivity.this)) {

                Intent search_tag_three = new Intent(SearchAllActivity.this,
                        SearchLayoutActivity.class);
                search_tag_three.putExtra("searchBrand", mTagTextThree
                        .getText().toString());
                startActivity(search_tag_three);
            } else {
                Toast.makeText(SearchAllActivity.this, R.string.intent_no,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.search_tag_text_four:
            if (AppUtil.isNetworkAvailable(SearchAllActivity.this)) {

                Intent search_tag_four = new Intent(SearchAllActivity.this,
                        SearchLayoutActivity.class);
                search_tag_four.putExtra("searchBrand", mTagTextFour.getText()
                        .toString());
                startActivity(search_tag_four);
            } else {
                Toast.makeText(SearchAllActivity.this, R.string.intent_no,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.search_tag_text_five:
            if (AppUtil.isNetworkAvailable(SearchAllActivity.this)) {

                Intent search_tag_five = new Intent(SearchAllActivity.this,
                        SearchLayoutActivity.class);
                search_tag_five.putExtra("searchBrand", mTagTextFive.getText()
                        .toString());
                startActivity(search_tag_five);
            } else {
                Toast.makeText(SearchAllActivity.this, R.string.intent_no,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.search_tag_text_six:
            if (AppUtil.isNetworkAvailable(SearchAllActivity.this)) {

                Intent search_tag_six = new Intent(SearchAllActivity.this,
                        SearchLayoutActivity.class);
                search_tag_six.putExtra("searchBrand", mTagTextSix.getText()
                        .toString());
                startActivity(search_tag_six);
            } else {
                Toast.makeText(SearchAllActivity.this, R.string.intent_no,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onRefresh() {
        if (mCurrentSearchTab == SearchTab.SEARCH_TAB_EXPERIENCE) {
            refreshExperience();
        } else if (mCurrentSearchTab == SearchTab.SEARCH_TAB_INFORMATION) {
            requestInformation();
        }
    }

    @Override
    public void onLoadMore() {
        if (mCurrentSearchTab == SearchTab.SEARCH_TAB_EXPERIENCE) {
            loadmoreExperience();
        } else if (mCurrentSearchTab == SearchTab.SEARCH_TAB_INFORMATION) {
            loadmoreInformation();
        }

    }

    private void setupCategories() {
        mCategoryEfficacyContainer.removeAllViews();
        YKCategory category;
        for (int i = 0; i < mCategories.size(); ++i) {
            category = mCategories.get(i);

            // setup title
            View itemView = LayoutInflater.from(SearchAllActivity.this)
                    .inflate(R.layout.search_fragment_test, null);
            TextView titleView = (TextView) itemView
                    .findViewById(R.id.search_text_face);
            titleView.setText(category.getTitle());

            // setup subcategories
            ArrayList<YKCategory> subCategory = category.getSubCategories();
            LinearLayout subCategoryContainer = (LinearLayout) itemView
                    .findViewById(R.id.search_all_vertical_lauoyt);
            int j = 0;
            for (j = 0; j < subCategory.size() / 3; ++j) {
                // setup
                LinearLayout container = new LinearLayout(this);
                LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                container.setOrientation(LinearLayout.HORIZONTAL);
                container.setLayoutParams(containerParam);

                TextView tmpTextView = createCategoryItem();
                tmpTextView.setText(subCategory.get(j * 3).getTitle());
                container.addView(tmpTextView);

                tmpTextView = createCategoryItem();
                tmpTextView.setText(subCategory.get(j * 3 + 1).getTitle());
                container.addView(tmpTextView);

                tmpTextView = createCategoryItem();
                tmpTextView.setText(subCategory.get(j * 3 + 2).getTitle());
                container.addView(tmpTextView);

                subCategoryContainer.addView(container);
            }

            if (subCategory.size() > j * 3) {
                LinearLayout container = new LinearLayout(this);
                LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                container.setOrientation(LinearLayout.HORIZONTAL);
                container.setLayoutParams(containerParam);
                for (int m = 0; m < (subCategory.size() - j * 3); m++) {
                    TextView tmpTextView = createCategoryItem();
                    tmpTextView.setText(subCategory.get(j * 3 + m).getTitle());
                    container.addView(tmpTextView);
                }

                subCategoryContainer.addView(container);
            }

            mCategoryEfficacyContainer.addView(itemView);
        }
    }

    private void setupEfficacies() {
        mCategoryEfficacyContainer.removeAllViews();
        YKEfficacy efficacy;
        for (int i = 0; i < mEfficacies.size(); ++i) {
            efficacy = mEfficacies.get(i);

            // setup title
            View itemView = LayoutInflater.from(SearchAllActivity.this)
                    .inflate(R.layout.search_fragment_test, null);
            TextView titleView = (TextView) itemView
                    .findViewById(R.id.search_text_face);
            titleView.setText(efficacy.getTitle());

            // setup subcategories
            ArrayList<YKEfficacy> subefficacies = efficacy.getSubEfficacies();
            LinearLayout subCategoryContainer = (LinearLayout) itemView
                    .findViewById(R.id.search_all_vertical_lauoyt);
            int j = 0;
            for (j = 0; j < subefficacies.size() / 3; ++j) {
                // setup
                LinearLayout container = new LinearLayout(this);
                LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                container.setOrientation(LinearLayout.HORIZONTAL);
                container.setLayoutParams(containerParam);

                TextView tmpTextView = createCategoryItem();
                tmpTextView.setText(subefficacies.get(j * 3).getTitle());
                container.addView(tmpTextView);

                tmpTextView = createCategoryItem();
                tmpTextView.setText(subefficacies.get(j * 3 + 1).getTitle());
                container.addView(tmpTextView);

                tmpTextView = createCategoryItem();
                tmpTextView.setText(subefficacies.get(j * 3 + 2).getTitle());
                container.addView(tmpTextView);

                subCategoryContainer.addView(container);
            }

            if (subefficacies.size() > j * 3) {
                LinearLayout container = new LinearLayout(this);
                LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                container.setOrientation(LinearLayout.HORIZONTAL);
                container.setLayoutParams(containerParam);
                for (int m = 0; m < (subefficacies.size() - j * 3); m++) {
                    TextView tmpTextView = createCategoryItem();
                    tmpTextView
                    .setText(subefficacies.get(j * 3 + m).getTitle());
                    container.addView(tmpTextView);
                }

                subCategoryContainer.addView(container);
            }

            mCategoryEfficacyContainer.addView(itemView);
        }
    }

    private TextView createCategoryItem() {
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);

        final TextView tmpTextView = new TextView(this);
        tmpTextView.setPadding(0, 10, 0, 10);
        tmpTextView.setTextSize(14f);
        tmpTextView.setLayoutParams(textParam);
        tmpTextView.setTextColor(0xff000000);
        tmpTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent search_tag_five = new Intent(SearchAllActivity.this,
                        SearchLayoutActivity.class);
                search_tag_five.putExtra("searchBrand", tmpTextView.getText()
                        .toString());
                startActivity(search_tag_five);
            }
        });
        return tmpTextView;
    }

    private void setupRecommendationCategory() {
        TextView textView;
        YKCategory category;
        for (int i = 0; i < mRecommendationCategoryies.size(); ++i) {
            textView = mRecommendationTextViews.get(i);
            category = mRecommendationCategoryies.get(i);
            textView.setText(category.getTitle());
        }
        for (int i = mRecommendationCategoryies.size(); i < mRecommendationTextViews
                .size(); ++i) {
            textView = mRecommendationTextViews.get(i);
            textView.setVisibility(View.INVISIBLE);
        }
    }

    private void setupRecommendationEfficacy() {
        TextView textView;
        YKEfficacy efficacy;
        for (int i = 0; i < mRecommendationEfficacies.size(); ++i) {
            textView = mRecommendationTextViews.get(i);
            efficacy = mRecommendationEfficacies.get(i);
            textView.setText(efficacy.getTitle());
        }
        for (int i = mRecommendationEfficacies.size(); i < mRecommendationTextViews
                .size(); ++i) {
            textView = mRecommendationTextViews.get(i);
            textView.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_CLOSE + "SearchAllActivity");
        super.onDestroy();
    }

    /**
     * 有Fragment
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SearchAllActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SearchAllActivity"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }
}
