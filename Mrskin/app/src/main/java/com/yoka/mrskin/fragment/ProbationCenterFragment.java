package com.yoka.mrskin.fragment;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.data.YKTrialProduct;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKTrialProductCallback;
import com.yoka.mrskin.model.managers.YKTrialProductManager;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.DoubleOnClick;
import com.yoka.mrskin.util.YKTiralStore;

/**
 * 试用列表
 * 
 * @author z l l
 * 
 */
public class ProbationCenterFragment extends Fragment implements
IXListViewListener, Observer
{
    private static final int REQUEST_CODE = 8;
    private static final int RESULT_CODE = 1;
    private XListView mListView;
    private MyAdapter mAdapter;
    private ArrayList<YKTrialProduct> mProducts;
    private int page = 2;
    private YKTiralStore mTiralStore;
    private int mOpenContentPosition;
    private RelativeLayout mDoubleTop;
    private CustomButterfly mCustomButterfly = null;
    private String mUserId;
    private View contentView;
    private XListViewFooter mListViewFooter;

    // private TextView mProbationShowNumber;

    private Activity mActivity;
    private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.resetViewBeforeLoading(true).considerExifParams(true)
			.build();
    @Override
	public void onAttach(Activity activity) {
		this.mActivity=activity;
		super.onAttach(activity);
	}

	@Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return contentView = View.inflate(mActivity,R.layout.probation_center_fragment, null);
    }
      
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initView();
        getData(true);
    }

    private void initView()
    {
        mTiralStore = new YKTiralStore(mActivity, "tiral_store");
        // mProbationShowNumber = (TextView) contentView
        // .findViewById(R.id.probation_show_number);
        mDoubleTop = (RelativeLayout) contentView
                .findViewById(R.id.probation_center_top_layout);
        YKTaskManager.getInstnace().addObserver(this);
        mProducts = new ArrayList<YKTrialProduct>();

        mListView = (XListView) contentView.findViewById(R.id.probation_center_listview);
        mListViewFooter = new XListViewFooter(mActivity);
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
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3)
            {
                int headCount = mListView.getHeaderViewsCount();
                arg2 -= headCount;
                mOpenContentPosition = arg2;
                if(mProducts != null && mProducts.size() > 0 ){
                    startProductDetailActivity(mProducts.get(arg2).getmUrl(),
                            mProducts.get(arg2).getmId());
                }
//                TrackManager.getInstance().addTrack(
//                        TrackUrl.ITEM_CLICK + mProducts.get(arg2).getmId()
//                        + "&type=trial");
            }
        });
        mDoubleTop.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DoubleOnClick.doubleClick(mListView, null);
            }
        });
        // if (RigthSildingMenu.isShowNumber()) {
        // mProbationShowNumber.setVisibility(View.VISIBLE);
        // } else {
        // mProbationShowNumber.setVisibility(View.GONE);
        // }

    }

    private void getData(boolean isAnim)
    {
        if (YKCurrentUserManager.getInstance().isLogin()) {
            mUserId = YKCurrentUserManager.getInstance().getUser().getId();
        } else {
            mUserId = null;
        }
        if (AppUtil.isNetworkAvailable(mActivity)) {
            if (isAnim) {
                try {
                    mCustomButterfly = CustomButterfly.show(mActivity);
                } catch (Exception e) {
                    mCustomButterfly = null;
                }
            }
            YKTrialProductManager.getInstance().requestTrialProductsList(1,
                    mUserId, new YKTrialProductCallback()
            {
                @Override
                public void callback(YKResult result,
                        ArrayList<YKTrialProduct> products)
                {
                    mListView.stopRefresh();
                    mListView.stopLoadMore();
                    AppUtil.dismissDialogSafe(mCustomButterfly);
                    System.out.println("result.getCode()"+result.getCode());
                    if (result.isSucceeded()) {
                        if (products != null) {
                            if (mProducts == null) {
                                mProducts = products;
                                mProducts.addAll(products);
                            } else {
                                mProducts.clear();
                                mProducts.addAll(products);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mActivity,
                                getString(R.string.intent_error),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(mActivity, getString(R.string.intent_no),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void loadMore()
    {

        YKTrialProductManager.getInstance().requestTrialProductsList(page,
                mUserId, new YKTrialProductCallback()
        {

            @Override
            public void callback(YKResult result,
                    ArrayList<YKTrialProduct> products)
            {
                if (result.isSucceeded()) {
                    if (products != null) {
                        mProducts.addAll(products);
                    }
                    if (products.size() > 0) {
                        page++;
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(
                        		mActivity,
                                getString(R.string.probation_center_no_data),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity,
                            getString(R.string.intent_no),
                            Toast.LENGTH_LONG).show();
                }
                mListView.stopLoadMore();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void startProductDetailActivity(String url, String id)
    {
        Intent intent = new Intent(mActivity, YKWebViewActivity.class);
        intent.putExtra("probation_detail_url", url);
        intent.putExtra("istrial_product", true);
        intent.putExtra("track_type", "trial");
        intent.putExtra("track_type_id", id);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (mOpenContentPosition == -1) {
                return;
            }
            if (resultCode == RESULT_CODE) {
                YKTrialProduct product = mProducts.get(mOpenContentPosition);
                if (product != null) {
                    mTiralStore.putReadData(product.getmId());
                    mTiralStore.saveDataToFile(mActivity);
                }
                mAdapter.setSelectItem(mOpenContentPosition);
                mAdapter.notifyDataSetInvalidated();
                mOpenContentPosition = -1;
            }
        }
    }

    @Override
    public void onRefresh()
    {
        page = 2;
        getData(false);
    }

    @Override
    public void onLoadMore()
    {
        loadMore();
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume()
    {
        super.onResume();
        MobclickAgent.onPageStart("ProbationCenterFragment"); // 统计页面
        MobclickAgent.onResume(mActivity); // 统计时长
    }

    private class MyAdapter extends BaseAdapter
    {
        // private DisplayImageOptions options = new
        // DisplayImageOptions.Builder()
        // .showImageOnLoading(R.drawable.list_default_bg).build();

        private ViewHolder viewHolder;

        @Override
        public int getCount()
        {
            if (mProducts != null) {
                return mProducts.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position)
        {
            if (mProducts != null) {
                return mProducts.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        public void setSelectItem(int selectItem)
        {
            YKTrialProduct tiralProduct = mProducts.get(selectItem);
            tiralProduct.setRead(true);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(
                        R.layout.probation_center_listview_item_layout, null);
                viewHolder.bigCover = (ImageView) convertView
                        .findViewById(R.id.probation_center_item_bigimg);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.probation_center_item_title);
                viewHolder.spec = (TextView) convertView
                        .findViewById(R.id.probation_center_item_spec);
                viewHolder.count = (TextView) convertView
                        .findViewById(R.id.probation_center_item_count);
                viewHolder.istrial = (TextView) convertView
                        .findViewById(R.id.probation_center_item_istrial);
                viewHolder.tagYesLayout = convertView
                        .findViewById(R.id.probation_center_item_tag_yes_layout);
                viewHolder.tagNoLayout = convertView
                        .findViewById(R.id.probation_center_item_tag_no_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            YKTrialProduct trialProduct = mProducts.get(position);
            YKProduct product = mProducts.get(position).getProduct();
            viewHolder.title.setText(product.getTitle());
            viewHolder.spec.setText(trialProduct.getSpec());
            viewHolder.count.setText(trialProduct.getAmount()
                    + getString(R.string.probation_center_fen));
            if (mTiralStore.isExsit(trialProduct.getmId())) {
                trialProduct.setRead(true);
            }
            if (trialProduct.isTrialProduct()) {
                viewHolder.istrial
                .setText(getString(R.string.probation_center_is_not));
            } else {
                viewHolder.istrial
                .setText(getString(R.string.probation_center_is));
            }
            if (!trialProduct.isRead()) {
                if (trialProduct.getmIntState() == 1) {
                    viewHolder.tagYesLayout.setVisibility(View.GONE);
                    viewHolder.tagNoLayout.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tagYesLayout.setVisibility(View.VISIBLE);
                    viewHolder.tagNoLayout.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tagYesLayout.setVisibility(View.VISIBLE);
                viewHolder.tagNoLayout.setVisibility(View.GONE);
            }
            if (!YKCurrentUserManager.getInstance().isLogin()) {
                viewHolder.tagYesLayout.setVisibility(View.GONE);
                viewHolder.tagNoLayout.setVisibility(View.VISIBLE);
            }
            ImageLoader.getInstance().displayImage(product.getCover().getmURL(), viewHolder.bigCover, options);
           /* Glide.with(ProbationCenterFragment.this).load(product.getCover().getmURL())
    		.into( viewHolder.bigCover);*/
//            TrackManager.getInstance().addTrack(
//                    TrackUrl.ITEM_EXPOSURE + trialProduct.getmId()
//                    + "&type=trial");
            return convertView;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mTiralStore != null) {
            mTiralStore.releaseData();
            mTiralStore = null;
            AppUtil.dismissDialogSafe(mCustomButterfly);
        }
        YKTaskManager.getInstnace().deleteObserver(this);
    }

    private class ViewHolder
    {
        View tagYesLayout, tagNoLayout;
        ImageView bigCover;
        TextView title, spec, count, istrial;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        // if (RigthSildingMenu.isShowNumber()) {
        // mProbationShowNumber.setVisibility(View.VISIBLE);
        // } else {
        // mProbationShowNumber.setVisibility(View.GONE);
        // }

    }
}
