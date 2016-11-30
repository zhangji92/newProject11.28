package com.yoka.mrskin.activity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.data.YKTrialProduct;
import com.yoka.mrskin.model.managers.YKConfirmProductManager;
import com.yoka.mrskin.model.managers.YKConfirmProductManager.YKGeneralCallBack;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKTrialProductCallback;
import com.yoka.mrskin.model.managers.YKTrialProductManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;

/**
 * 试用列表
 * 
 * @author z l l
 * 
 */
public class MeProbationActivity extends BaseActivity implements OnClickListener,
IXListViewListener
{

    private static final int ALL = 1;
    private static final int SUCCESS = 2;
    private static final int FAILED = 3;
    private static final int WATTING = 4;
    private static final int REQUSET_CODE = 6;
    private  int TypeRefresh = 0;
    private XListView mListView;
    private TextView mFilterBtn;
    private View mFilterView;
    private View mNoProductLayout;
    private Dialog mFilterDialog;
    private TextView mFilterAll, mFilterSuccess, mFilterIng, mFilterFailed;
    private ImageView mFilterDialogClose;
    private MyAdapter mAdapter;
    private ArrayList<YKTrialProduct> mProducts;
    private int page = 2;
    private String mUserId;
    private int mTempType;
    private View mNoAllLayout;
    private View mHeaderView;
    private LinearLayout mBackBtn;
    private XListViewFooter mListViewFooter;
    private CustomButterfly mCustomButterfly = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.probation_activity);

        initView();
        initReportDialog();
        getData(true, ALL, true);
        //        TrackManager.getInstance().addTrack(
                //                TrackUrl.PAGE_OPEN + "ProbationActivity");
    }

    @Override
    protected void onDestroy() {
        //        TrackManager.getInstance().addTrack(
        //                TrackUrl.PAGE_CLOSE + "ProbationActivity");
        YKActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    private void initView() {
        mUserId = YKCurrentUserManager.getInstance().getUser().getId();
        mProducts = new ArrayList<YKTrialProduct>();
        mBackBtn = (LinearLayout) findViewById(R.id.probation_back_text);
        mFilterBtn = (TextView) findViewById(R.id.probation_filter_text);
        mNoProductLayout = findViewById(R.id.probation_no_product_layout);
        mNoAllLayout = findViewById(R.id.probation_no_product_go_layout);
        mNoAllLayout.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mFilterBtn.setOnClickListener(this);
        mHeaderView = LayoutInflater.from(MeProbationActivity.this).inflate(
                R.layout.no_net_header_view, null);
        mListView = (XListView) findViewById(R.id.probation_xlistview);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListViewFooter = new XListViewFooter(MeProbationActivity.this);
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
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                //                 YKTrialProduct trialProduct = mProducts.get(arg2 - 1);
                //                 YKProduct product = trialProduct.getProduct();
                //                 Intent intent = new Intent(MeProbationActivity.this,
                //                 CommitReportActivity.class);
                //                 intent.putExtra("trial_probation_id", trialProduct.getmId());
                //                 intent.putExtra("trial_probation_name", product.getTitle());
                //                 intent.putExtra("trial_probation_image_url",
                //                 product.getCover()
                //                 .getmURL());
                //                 startActivityForResult(intent, 0);
                if(mProducts.size() <= 0){
                    return;
                }
                startProductDetailActivity(mProducts.get(arg2 - 1).getmUrl(),
                        mProducts.get(arg2 - 1).getmId());
                //                TrackManager.getInstance().addTrack(
                //                        TrackUrl.ITEM_CLICK + mProducts.get(arg2 - 1).getmId()
                //                        + "&type=trial");
                //                MeProbationActivity.this.setResult(RESULT_OK);
                //                finish();
            }
        });
    }

    private void startProductDetailActivity(String url, String id) {
        String token = YKCurrentUserManager.getInstance().getUser().getToken();
        Intent intent = new Intent(this, YKWebViewActivity.class);
        intent.putExtra("probation_detail_token", token);
        intent.putExtra("probation_detail_url", url);
        intent.putExtra("identification", "html");
        intent.putExtra("track_type", "trial");
        intent.putExtra("track_type_id", id);

        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    private void initReportDialog() {
        mFilterView = getLayoutInflater().inflate(
                R.layout.probation_filter_dialog_layout, null);
        mFilterDialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        mFilterDialog.setContentView(mFilterView, new LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = mFilterDialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mFilterDialog.onWindowAttributesChanged(wl);
        mFilterDialog.setCanceledOnTouchOutside(true);

        mFilterDialogClose = (ImageView) mFilterView
                .findViewById(R.id.probation_filter_dialog_close);
        mFilterAll = (TextView) mFilterView
                .findViewById(R.id.probation_filter_dialog_all);
        mFilterSuccess = (TextView) mFilterView
                .findViewById(R.id.probation_filter_dialog_success);
        mFilterIng = (TextView) mFilterView
                .findViewById(R.id.probation_filter_dialog_ing);
        mFilterFailed = (TextView) mFilterView
                .findViewById(R.id.probation_filter_dialog_failed);
        mFilterDialogClose.setOnClickListener(this);
        mFilterAll.setOnClickListener(this);
        mFilterSuccess.setOnClickListener(this);
        mFilterIng.setOnClickListener(this);
        mFilterFailed.setOnClickListener(this);
    }

    private void getData(final boolean isRefresh, int type, boolean isAnim) {
        if (isAnim) {
            try {
                mCustomButterfly = CustomButterfly.show(this);
            } catch (Exception e) {
                mCustomButterfly = null;
            }
        }
        mTempType = type;
        YKTrialProductManager.getInstance().requestMyTrialProductsList(1,
                mUserId, type, new YKTrialProductCallback() {

            @Override
            public void callback(YKResult result,
                    ArrayList<YKTrialProduct> products) {
                AppUtil.dismissDialogSafe(mCustomButterfly );
                mListView.stopRefresh();
                mListView.stopLoadMore();
                if (result.isSucceeded()) {
                    mListView.removeHeaderView(mHeaderView);
                    if (products.size() > 0) {
                        mNoProductLayout.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    } else {
                        mNoProductLayout.setVisibility(View.VISIBLE);
                        if (mTempType == 1) {
                            mNoAllLayout.setVisibility(View.VISIBLE);
                        } else {
                            mNoAllLayout.setVisibility(View.GONE);
                        }
                        mListView.setVisibility(View.GONE);
                    }
                    if (isRefresh) {
                        mProducts.clear();
                        mProducts.addAll(products);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        for (int j = 0; j < mProducts.size(); j++) {
                            YKTrialProduct product = mProducts.get(j);
                            boolean isfind = false;
                            for (int i = 0; i < mProducts.size(); i++) {
                                if (product.getID().equals(
                                        mProducts.get(i).getID())) {
                                    isfind = true;
                                }
                            }
                            if (!isfind) {
                                mProducts.add(product);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    // mListView.setVisibility(View.GONE);
                    mListView.removeHeaderView(mHeaderView);
                    mListView.addHeaderView(mHeaderView);
                }
            }
        });
    }

    private void loadMore(int type) {
        YKTrialProductManager.getInstance().requestMyTrialProductsList(page,
                mUserId, type, new YKTrialProductCallback() {

            @Override
            public void callback(YKResult result,
                    ArrayList<YKTrialProduct> products) {
                if (result.isSucceeded()) {
                    if (products != null) {
                        if (mProducts == null) {
                            mProducts = products;
                        } else {
                            for (int j = 0; j < products.size(); j++) {
                                YKTrialProduct product = products
                                        .get(j);
                                boolean isfind = false;
                                for (int i = 0; i < mProducts.size(); i++) {
                                    if (product.getID().equals(
                                            mProducts.get(i).getID())) {
                                        isfind = true;
                                    }
                                }
                                if (!isfind) {
                                    mProducts.add(product);
                                }
                            }
                        }
                    }
                    if (products.size() > 0) {
                        page++;
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MeProbationActivity.this,
                                "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }
                }
                mListView.stopLoadMore();
            }
        });
    }

    private void closeFilterDialog() {
        if (mFilterDialog != null && mFilterDialog.isShowing()) {
            mFilterDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.probation_back_text:
            finish();
            break;
        case R.id.probation_filter_text:
            if (mFilterDialog != null) {
                mFilterDialog.show();
            }
            break;
        case R.id.probation_filter_dialog_close:
            closeFilterDialog();
            break;
        case R.id.probation_filter_dialog_all:
            closeFilterDialog();
            getData(true, ALL, true);
            TypeRefresh = ALL;
            break;
        case R.id.probation_filter_dialog_success:
            closeFilterDialog();
            getData(true, SUCCESS, true);
            TypeRefresh = SUCCESS;
            break;
        case R.id.probation_filter_dialog_failed:
            closeFilterDialog();
            getData(true, FAILED, true);
            TypeRefresh = FAILED;
            break;
        case R.id.probation_filter_dialog_ing:
            closeFilterDialog();
            getData(true, WATTING, true);
            TypeRefresh = WATTING;
            break;
        case R.id.probation_no_product_go_layout:
            MeProbationActivity.this.setResult(RESULT_OK);
            this.finish();
            break;
        }
    }

    @Override
    public void onRefresh() {
        page = 2;
        if(TypeRefresh == ALL){
            getData(true, ALL, false);
        }else if(TypeRefresh == SUCCESS){
            getData(true, SUCCESS, false);
        }else if(TypeRefresh == FAILED){
            getData(true, FAILED, false);
        }else if(TypeRefresh == WATTING){
            getData(true, WATTING, false);
        }else{
            getData(true, ALL, false);
        }
    }

    @Override
    public void onLoadMore() {
        loadMore(mTempType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSET_CODE) {
            getData(true, ALL, true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MyAdapter extends BaseAdapter
    {
        private DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.list_default_bg).build();
        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            if (mProducts != null) {
                return mProducts.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mProducts != null) {
                return mProducts.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MeProbationActivity.this)
                        .inflate(R.layout.probation_listview_item_layout, null);
                viewHolder.mImage = (ImageView) convertView
                        .findViewById(R.id.probation_item_image);
                viewHolder.mTitle = (TextView) convertView
                        .findViewById(R.id.probation_item_title_text);
                viewHolder.mState = (TextView) convertView
                        .findViewById(R.id.probation_item_state_text);
                viewHolder.mDate = (TextView) convertView
                        .findViewById(R.id.probation_item_date_text);
                viewHolder.mTagText = (TextView) convertView
                        .findViewById(R.id.probation_item_info);
                viewHolder.mTagView = convertView
                        .findViewById(R.id.probation_item_tag_layout);
                viewHolder.mTagImg = (ImageView) convertView
                        .findViewById(R.id.probation_item_image_tag);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final YKTrialProduct trialProduct = mProducts.get(position);
            final YKProduct product = trialProduct.getProduct();
            if (trialProduct.isChange()) {
                viewHolder.mTagImg.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mTagImg.setVisibility(View.GONE);
            }
            if (product.getTitle().trim() != null) {
                viewHolder.mTitle.setText(product.getTitle());
            }
            viewHolder.mDate
            .setText("申请日期："
                    + timestampToString(trialProduct.getmApplyTime()
                            .getTime()));
            int type = trialProduct.getmIntState();
            viewHolder.mState.setText(getState(type));
            if (type == 3) {
                viewHolder.mTagView.setVisibility(View.VISIBLE);
                viewHolder.mTagText.setText("确认收货");
                viewHolder.mTagView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showDialog(trialProduct.getmId());
                    }
                });
            } else if (type == 4) {
                viewHolder.mTagView.setVisibility(View.VISIBLE);
                viewHolder.mTagText.setText("提交报告");
                viewHolder.mTagView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MeProbationActivity.this,
                                CommitReportActivity.class);
                        intent.putExtra("trial_probation_id",
                                trialProduct.getmId());
                        intent.putExtra("trial_probation_name",
                                product.getTitle());
                        intent.putExtra("trial_probation_image_url", product
                                .getCover().getmURL());
                        intent.putExtra("is_trial_product", true);
                        startActivityForResult(intent, REQUSET_CODE);
                    }
                });
            } else {
                viewHolder.mTagView.setVisibility(View.GONE);
            }
            ImageLoader.getInstance().displayImage(product.getCover().getmURL(), viewHolder.mImage, options);
       /*     Glide.with(MeProbationActivity.this).load(product.getCover().getmURL())
            .into( viewHolder.mImage).onLoadStarted(getResources().getDrawable(R.drawable.list_default_bg));;*/

            //            TrackManager.getInstance().addTrack(
            //                    TrackUrl.ITEM_EXPOSURE + trialProduct.getmId()
            //                    + "&type=trial");
            return convertView;
        }
    }

    private class ViewHolder
    {
        TextView mTitle, mState, mDate, mTagText;
        View mTagView;
        ImageView mImage, mTagImg;
    }

    private void showDialog(final String id) {
        new AlertDialog.Builder(this).setTitle("确认收货？")
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmProduct(id);
                dialog.dismiss();
            }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void confirmProduct(String productId) {
        YKConfirmProductManager.getInstnace().requestConfirmProduct(productId,
                mUserId, new YKGeneralCallBack() {

            @Override
            public void callback(YKResult result) {
                if (result.isSucceeded()) {
                    getData(true, ALL, true);
                    Toast.makeText(MeProbationActivity.this, "确认收货成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MeProbationActivity.this, "请求失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getState(int type) {
        if (type == 1) {
            return "未申请";
        } else if (type == 2) {
            return "申请成功";
        } else if (type == 3) {
            return "已发货";
        } else if (type == 4) {
            return "待提交";
        } else if (type == 5) {
            return "已完成";
        } else if (type == 6) {
            return "申请失败";
        } else if (type == 7) {
            return "正在审核";
        }
        return "";
    }

    /**
     * 将int类型时间戳转换为时间
     * 
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String timestampToString(long time) {
        // int转long时，先进行转型再进行计算，否则会是计算结束后在转型
        long temp = time * 1000;
        Timestamp ts = new Timestamp(temp);
        String tsStr = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            tsStr = dateFormat.format(ts);
            System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ProbationActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ProbationActivity"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TrackManager.getInstance().addTrack1(YKManager.FOUR + "rank/page1v5", "start", "rank_list");
    }
    @Override
    public void onStop() {
        super.onStart();
        //TrackManager.getInstance().addTrack1(YKManager.FOUR + "rank/page1v5", "end", "rank_list");
    }
}
