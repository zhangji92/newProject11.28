package com.yoka.mrskin.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.managers.YKCosmeticBagManagers;
import com.yoka.mrskin.model.managers.YKCosmeticBagManagers.CommentBagCallback;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKDeleteCosmesticBagManagers;
import com.yoka.mrskin.model.managers.YKDeleteCosmesticBagManagers.CosmesticBagCallback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.SharePopupWindow;
import com.yoka.share.manager.ShareQzoneManager;
import com.yoka.share.manager.ShareSinaManager;
import com.yoka.share.manager.ShareWxManager;

/**
 * 我的化妆包
 */
public class SildingMenuMyBagActivity extends BaseActivity implements
IXListViewListener
{
    private XListView mXListView;
    private ArrayList<YKProduct> mProducts;
    private RightMeBagAdapter mAdapter;
    private int mIndex = 1;
    private LinearLayout mNoBagLayout, mNoIntent;
    private SharePopupWindow mPopupWindow;
    private TextView mBag;
    private CustomButterfly mCustomButterfly = null;
    public static final int REQUEST_CODE = 11;
    private XListViewFooter mListViewFooter;
    private String mShareTitle, mShareImage, mNewReadUrl;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.right_me_bug);

        options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true).considerExifParams(true)
                .build();
        init();
        initData(true, false);
        //        TrackManager.getInstance().addTrack(
        //                TrackUrl.PAGE_OPEN + "SildingMenuMyBagActivity");
    }

    private void init()
    {
        mXListView = (XListView) findViewById(R.id.right_bug_list);
        mXListView.setXListViewListener(this);
        mListViewFooter = new XListViewFooter(SildingMenuMyBagActivity.this);
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

        mNoBagLayout = (LinearLayout) findViewById(R.id.right_no_bug_layout);
        mBag = (TextView) findViewById(R.id.right_goto_product);
        mBag.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                SildingMenuMyBagActivity.this.setResult(RESULT_OK);
                finish();
            }
        });
        mNoIntent = (LinearLayout) findViewById(R.id.right_no_intent_layout);
        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(true);
        mAdapter = new RightMeBagAdapter();
        mXListView.setDividerHeight(10);
        mXListView.setAdapter(mAdapter);

        View v = findViewById(R.id.right_bug_back_layout);
        v.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                SildingMenuMyBagActivity.this.finish();
            }
        });
    }

    private void initData(final boolean isRefresh, final boolean isLoadMore)
    {
        if (isRefresh) {
            try {
                mCustomButterfly = CustomButterfly.show(this);
            } catch (Exception e) {
                mCustomButterfly = null;
            }
        }
        YKCosmeticBagManagers.getInstance().postYKcommentBag(new CommentBagCallback()
        {
            @Override
            public void callback(YKResult result, ArrayList<YKProduct> product)
            {
                AppUtil.dismissDialogSafe(mCustomButterfly);
                mXListView.stopRefresh();
                mXListView.stopLoadMore();
                if(result.isSucceeded()){
                    mXListView.setVisibility(View.VISIBLE);
                    mNoBagLayout.setVisibility(View.GONE);
                    mNoIntent.setVisibility(View.GONE);
                    if(product != null && product.size() > 0){
                        if(isLoadMore){
                            mProducts.addAll(product);
                        }else{
                            if(mProducts == null){
                                mProducts = product;
                            }else{
                                mProducts.clear();
                                mProducts.addAll(product);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        if (mProducts.size() > 0) {
                            ++mIndex;
                            mAdapter.notifyDataSetChanged();
                        } 
                    }else{
                        if(isLoadMore){
                            Toast.makeText(SildingMenuMyBagActivity.this,
                                    R.string.task_no_task, Toast.LENGTH_SHORT).show();
                        }else{
                            mNoIntent.setVisibility(View.GONE);
                            mXListView.setVisibility(View.GONE);
                            mNoBagLayout.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    mXListView.setVisibility(View.GONE);
                    mNoIntent.setVisibility(View.VISIBLE);
                    mNoBagLayout.setVisibility(View.GONE);
                    Toast.makeText(SildingMenuMyBagActivity.this,
                            (String) result.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, mIndex, 10,
        YKCurrentUserManager.getInstance(this).getUser().getId());
    }

    public class RightMeBagAdapter extends BaseAdapter
    {
        private ViewHolder viewHolder = null;

        @Override
        public int getCount()
        {
            if (mProducts == null) {
                return 0;
            }
            return mProducts.size();
        }

        @Override
        public Object getItem(int position)
        {
            if (mProducts == null) {
                return null;
            }
            return mProducts.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent)
        {

            final YKProduct pro = mProducts.get(position);
            if (convertView == null) {
                convertView = LayoutInflater
                        .from(SildingMenuMyBagActivity.this).inflate(
                                R.layout.right_me_grass_item, null);
                viewHolder = new ViewHolder();
                viewHolder.sTitle = (TextView) convertView
                        .findViewById(R.id.right_grass_title);
                viewHolder.sPrice = (TextView) convertView
                        .findViewById(R.id.right_grass_price);
                viewHolder.sMl = (TextView) convertView
                        .findViewById(R.id.right_grass_ml);
                viewHolder.sImage = (ImageView) convertView
                        .findViewById(R.id.right_grass_image);
                viewHolder.sBar = (RatingBar) convertView
                        .findViewById(R.id.right_grass_bar);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (pro != null) {
                if (!TextUtils.isEmpty(pro.getTitle())) {
                    viewHolder.sTitle.setText(pro.getTitle());
                }
                if (pro.getSpecification() != null) {
                    float price = 0f;
                    if (pro.getSpecification().getPrice() != 0) {
                        price = pro.getSpecification().getPrice();
                        // 构造方法的字符格式这里如果小数不足2位,会以0补足
                        DecimalFormat decimalFormat = new DecimalFormat("0");
                        // format 返回的是字符串
                        String newPric = decimalFormat.format(price);
                        viewHolder.sPrice.setText("￥" + newPric);
                    } else {
                        viewHolder.sPrice
                        .setText(getString(R.string.mybag_price));
                    }
                }
                if (!TextUtils.isEmpty(pro.getSpecification().getTitle())) {
                    viewHolder.sMl.setText("  "+"|"+"  "
                            + pro.getSpecification().getTitle());
                }
                try {

                    /*Glide.with(SildingMenuMyBagActivity.this).load(pro.getCover().getmURL())
                  		.into(viewHolder.sImage);*/
                    ImageLoader.getInstance().displayImage(pro.getCover().getmURL(), viewHolder.sImage, options);
                } catch (Exception e) {
                    viewHolder.sImage
                    .setBackgroundResource(R.drawable.list_default_bg);
                }
            }
            if (pro.getRating() != null) {
                viewHolder.sBar.setRating(pro.getRating());
            }
            convertView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent newRead = new Intent(SildingMenuMyBagActivity.this,
                            YKWebViewActivity.class);
                    Uri uri = Uri.parse(pro.getDescription_url());
                    String newreadUrl = uri.getQueryParameter("url");
                    newRead.putExtra("productdetalis", newreadUrl);
                    startActivity(newRead);
                }
            });

            convertView.setOnLongClickListener(new OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    openDialog(pro, position);
                    mShareTitle = pro.getTitle();
                    mShareImage = pro.getCover().getmURL();
                    Uri uri = Uri.parse(pro.getDescription_url());
                    mNewReadUrl = uri.getQueryParameter("url");
                    return false;
                }
            });
            return convertView;
        }
    }

    /**
     * 删除对话框
     * 
     * @param product_id
     * @param user_id
     */
    private void openDialog(final YKProduct product, final int position)
    {
        AlertDialog.Builder builder = new Builder(SildingMenuMyBagActivity.this);
        builder.setTitle(getString(R.string.mybag_title));
        builder.setPositiveButton(getString(R.string.mybag_delete),
                new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog,
                    final int which)
            {
                YKDeleteCosmesticBagManagers.getInstance()
                .postYKCosmesticShop(
                        new CosmesticBagCallback()
                        {
                            @Override
                            public void callback(YKResult result)
                            {
                                if (result.isSucceeded()) {
                                    mProducts.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    Toast.makeText(
                                            SildingMenuMyBagActivity.this,
                                            getString(R.string.mybag_delete_success),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    String message = (String) result
                                            .getMessage();
                                    Toast.makeText(
                                            SildingMenuMyBagActivity.this,
                                            message,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }, product.getID(),
                        String.valueOf(product.getRating()));
            }
        });
        builder.setNegativeButton(getString(R.string.share_share),
                new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 实例化SelectPicPopupWindow
                mPopupWindow = new SharePopupWindow(
                        SildingMenuMyBagActivity.this, itemsOnClick);
                // 显示窗口
                mPopupWindow.showAtLocation(
                        SildingMenuMyBagActivity.this
                        .findViewById(R.id.right_bug_list),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0);
                // 设置layout在PopupWindow中显示的位置
            }
        });
        builder.create().show();
    }

    class ViewHolder
    {
        private ImageView sImage;
        private TextView sTitle, sPrice, sMl;
        private RatingBar sBar;

    }

    @Override
    public void onRefresh()
    {
        mIndex = 1;
        initData(false, false);
    }

    @Override
    public void onLoadMore()
    {
        initData(false, true);
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener()
    {

        public void onClick(View v)
        {
            SharePopupWindow.getInstance().dismiss();
            switch (v.getId()) {
            case R.id.popup_friend:
                ShareWxManager.getInstance().shareWxCircle(mShareTitle, null,
                        mNewReadUrl, mShareImage);
                // }
                // });
                mPopupWindow.dismiss();
                break;
            case R.id.popup_wei:
                ShareWxManager.getInstance().shareWx(mShareTitle, null,
                        mNewReadUrl, mShareImage,SildingMenuMyBagActivity.this);
                // }
                // });
                mPopupWindow.dismiss();
                break;
            case R.id.popup_sina:
//                Glide.with(SildingMenuMyBagActivity.this).asBitmap().  load(mShareImage)      
//                .into(new SimpleTarget<Bitmap>(250, 250) {
                
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        ShareSinaManager.getInstance().sendShare(
//                                resource, mShareTitle, "",
//                                "//" + mNewReadUrl);
                        ShareSinaManager.getInstance().shareSina("肤君分享", mShareTitle, mNewReadUrl, mShareImage,SildingMenuMyBagActivity.this);
//                    }      
//                }
//                        ); 
                mPopupWindow.dismiss();
                break;
            case R.id.popup_qzone:
                ShareQzoneManager.getInstance().shareQzone(mShareTitle,null,mNewReadUrl, mShareImage,SildingMenuMyBagActivity.this);
                mPopupWindow.dismiss();
                break;
            default:
                break;
            }
        }
    };

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume()
    {
        super.onResume();
        if(mXListView != null){
            onRefresh();
        }
        MobclickAgent.onPageStart("SildingMenuMyBagActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    // 保证 onPageEnd在onPause之前调用,因为 onPause中会保存信息
    public void onPause()
    {
        super.onPause();
        MobclickAgent.onPageEnd("SildingMenuMyBagActivity");
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //        TrackManager.getInstance().addTrack(
        //                TrackUrl.PAGE_CLOSE + "SildingMenuMyBagActivity");
    }
}
