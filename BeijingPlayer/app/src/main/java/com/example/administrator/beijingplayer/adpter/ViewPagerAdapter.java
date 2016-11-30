package com.example.administrator.beijingplayer.adpter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.beijingplayer.mode.Pictury;
import com.example.administrator.beijingplayer.util.ModeCode;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ViewPagerAdapter extends PagerAdapter{

    List<Pictury> picList = new ArrayList<>();
    List<ImageView> imgList = new ArrayList<>();
    Context context;

    public ViewPagerAdapter( List<Pictury> picList,Context context){

        this.picList.addAll(picList);
        this.context = context;
    }
    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        imgList.add(img);

        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(img, ModeCode.HTTP+picList.get(position).getPic());
        container.addView(img);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView(imgList.get(position));
    }

    public List<Pictury> getPicList() {
        return picList;
    }

    public void setPicList(List<Pictury> picList) {
        this.picList.addAll(picList);
    }
}
