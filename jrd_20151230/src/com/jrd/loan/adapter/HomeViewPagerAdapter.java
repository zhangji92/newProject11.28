package com.jrd.loan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.bean.BannerList;
import com.jrd.loan.net.imageloader.ImageLoader;

public class HomeViewPagerAdapter extends PagerAdapter {
    private final List<BannerList> imageList;
    private final List<View> viewList;
    private final LayoutInflater inflater;
    private final Context context;

    public HomeViewPagerAdapter(Context context, List<BannerList> imageList) {
        super();
        this.context = context;
        this.imageList = imageList;
        this.viewList = new ArrayList<View>();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.initViews();
    }

    private void initViews() {
        for (BannerList banner : imageList) {
            View view = inflater.inflate(R.layout.loan_banner_image_item, null);
            this.viewList.add(view);
        }
    }

    @Override
    public int getCount() {
        return this.viewList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(this.viewList.get(position));

        if (!TextUtils.isEmpty(this.imageList.get(position).getImgSrc())) {
            ImageLoader.loadImage((ImageView) this.viewList.get(position).findViewById(R.id.loan_banner_image), this.imageList.get(position).getImgSrc());
        }
        View view = viewList.get(position);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, WebViewActivity.class);
                mIntent.putExtra("htmlUrl", imageList.get(position).getImgHref());
                mIntent.putExtra("htmlTitle", imageList.get(position).getImgTitlle());
                context.startActivity(mIntent);
            }
        });
        return this.viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(this.viewList.get(position));
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }
}
