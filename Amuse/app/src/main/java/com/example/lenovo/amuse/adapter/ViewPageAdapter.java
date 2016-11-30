package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lenovo.amuse.mode.FirstPageMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by lenovo on 2016/9/26.
 * 广告适配器
 */

public class ViewPageAdapter extends PagerAdapter {
    private List<FirstPageMode.ResultCodeBean.AdBean> adList;
    private List<View> viewList;
    FinalBitmap finalBitmap;

    public ViewPageAdapter(Context context){
        finalBitmap=FinalBitmap.create(context);//初始化FinalBitmap模块
        // fb.configLoadingImage()
    }

    public List<FirstPageMode.ResultCodeBean.AdBean> getAdList() {
        return adList;
    }

    public void setAdList(List<FirstPageMode.ResultCodeBean.AdBean> adList) {
        this.adList = adList;
    }

    public List<View> getViewList() {
        return viewList;
    }

    public void setViewList(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return adList.size() == 0 ? 0 : adList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = (ImageView) viewList.get(position);
        finalBitmap.display(imageView, BaseUri.BASE + adList.get(position).getPic());
        container.addView(imageView);
        return imageView;
    }
}
