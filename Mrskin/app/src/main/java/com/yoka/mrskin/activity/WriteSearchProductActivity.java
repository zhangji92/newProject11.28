package com.yoka.mrskin.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;

public class WriteSearchProductActivity extends BaseActivity implements
OnClickListener, IXListViewListener
{
    private String mSearchText;
    private EditText mSearchEdit;
    private XListView mXListView;
    private ArrayList<YKProduct> mProducts;
    private ProductAdapter mProductAdapter;
    private TextView mCancle, mNoSearchText;
    private int mProductPageIndex = 2;
    private YKSearchType mCurrentTab;
    private String mPosition;
    private CustomButterfly mCustomButterfly = null;
    private XListViewFooter mListViewFooter;
    private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.resetViewBeforeLoading(true).considerExifParams(true)
			.build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_search_product_layout);


        init();
        initgetData();
        //TrackManager.getInstance().addTrack(TrackUrl.PAGE_OPEN + "SearchLayoutActivity");
    }

    @Override
    protected void onDestroy() {
        //TrackManager.getInstance().addTrack(TrackUrl.PAGE_CLOSE + "SearchLayoutActivity");
        super.onDestroy();
    }

    private void init() {
        mCurrentTab = YKSearchType.SEARCHTYPE_PRODUCT;
        mCancle = (TextView) findViewById(R.id.all_search_cancle);
        mCancle.setOnClickListener(this);


        mNoSearchText = (TextView) findViewById(R.id.search_no_text);

        mSearchEdit = (EditText) findViewById(R.id.all_search_edittext);
        mXListView = (XListView) findViewById(R.id.search_layout_list);
        mXListView.setXListViewListener(this);
        mListViewFooter = new XListViewFooter(WriteSearchProductActivity.this);
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
        mProductAdapter = new ProductAdapter();
        mXListView.setAdapter(mProductAdapter);
        mXListView.setVisibility(View.VISIBLE);
        mXListView.setPullLoadEnable(true);



        mXListView.setVisibility(View.GONE);
        mSearchEdit.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    hideKeyBord();
                    mSearchText = mSearchEdit.getText().toString();
                    if (TextUtils.isEmpty(mSearchText)) {
                        hideKeyBord();
                    } else {
//                        TrackManager.getInstance().addTrack(
//                                TrackUrl.SEARCH_CLICK + mSearchText);
                        initData();
                    }
                }
                return false;
            }
        });
    }

    private void hideKeyBord() {
        // 先隐藏键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
        .hideSoftInputFromWindow(WriteSearchProductActivity.this
                .getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initgetData() {
        Intent searchBrand = getIntent();
        mSearchText = searchBrand.getStringExtra("searchBrand");
        mPosition = searchBrand.getStringExtra("position");

        if (!TextUtils.isEmpty(mSearchText)) {
            mSearchEdit.setText(mSearchText);
            initData();
        }
    }

    private void initData() {

        refreshData(true);
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
                convertView = LayoutInflater.from(WriteSearchProductActivity.this)
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
                 	/*Glide.with(WriteSearchProductActivity.this).load(pro.getCover().getmURL())
            		.into(viewHolder.sBigImage);*/
                 	 ImageLoader.getInstance().displayImage(pro.getCover().getmURL(), viewHolder.sBigImage, options);
                    //                    }
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
                            WriteSearchProductActivity.this, WriteExperienceActivity.class);
                    productPlandetalis.putExtra("product", pro);
                    productPlandetalis.putExtra("position", mPosition);
                    setResult(RESULT_OK,productPlandetalis);
                    finish();
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

    private class ViewHolder
    {
        // Product
        private TextView sTitle, sPrice, sMl, sNoLayout;
        private ImageView sBigImage;
        private RatingBar sRatBar;
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
            if (mProducts == null) {
                refreshData(true);
            } else {
                // mXListView.setAdapter(mProductAdapter);
                // mProductAdapter.notifyDataSetChanged();
            }
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
        }
        YKSearchManager.getInstance().requestSearchData(mSearchText,
                mCurrentTab, index, new searchCallback() {
            @Override
            public void callback(YKResult result,
                    ArrayList<YKProduct> product,
                    ArrayList<YKExperience> experience,
                    ArrayList<YKTopicData> information) {
                mXListView.stopLoadMore();
                if (result.isSucceeded()) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            WriteSearchProductActivity.this
                            .getCurrentFocus()
                            .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (mCurrentTab == YKSearchType.SEARCHTYPE_PRODUCT) {
                        ++mProductPageIndex;
                        if (mProducts != null) {

                            mProducts.addAll(product);
                            mProductAdapter.notifyDataSetChanged();

                        }
                    } 
                }
            }
        });
    }

    public void refreshData(final boolean showDialog) {
        if (showDialog) {
            try {
                CustomButterfly.getInstance(WriteSearchProductActivity.this);
                mCustomButterfly = CustomButterfly.show(this);
            } catch (Exception e) {
                mCustomButterfly = null;
            }
        }
        if (mCurrentTab == YKSearchType.SEARCHTYPE_PRODUCT) {
            mProductPageIndex = 1;
        } 
        YKSearchManager.getInstance().requestSearchData(mSearchText,
                mCurrentTab, 1, new searchCallback() {
            @Override
            public void callback(YKResult result,
                    ArrayList<YKProduct> product,
                    ArrayList<YKExperience> experience,
                    ArrayList<YKTopicData> information) {
                mXListView.stopRefresh();
                mXListView.stopLoadMore();
                AppUtil.dismissDialogSafe(mCustomButterfly);
                if (result.isSucceeded()) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            WriteSearchProductActivity.this
                            .getCurrentFocus()
                            .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (mCurrentTab == YKSearchType.SEARCHTYPE_PRODUCT) {
                        if (product != null && product.size() > 0) {
                            ++mProductPageIndex;
                            mProducts = product;
                            mXListView.setVisibility(View.VISIBLE);
                            mNoSearchText.setVisibility(View.GONE);
                            // mXListView.setAdapter(mProductAdapter);
                            mProductAdapter.notifyDataSetChanged();

                        } else {
                            mXListView.setVisibility(View.VISIBLE);
                            mNoSearchText.setVisibility(View.VISIBLE);
                            mNoSearchText.setText("没有与“" + mSearchText
                                    + "”相关的产品，换个词再试一试");
                        }
                    } 
                } else {
                    Toast.makeText(WriteSearchProductActivity.this,
                            (String) result.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}
