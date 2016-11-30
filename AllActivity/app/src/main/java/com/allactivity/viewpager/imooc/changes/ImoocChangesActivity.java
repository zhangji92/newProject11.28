package com.allactivity.viewpager.imooc.changes;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allactivity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/11/23.
 * imooc千变万化的viewPager
 */

public class ImoocChangesActivity extends Activity {
    private ViewPager mViewPager;
    private int[] mImgIds=new int[]{R.drawable.guide_image1,R.drawable.guide_image2,R.drawable.guide_image3};
    private List<ImageView> mList=new ArrayList<>();
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view=inflater.inflate(R.layout.imooc_changes_activity,container,false);
//        mViewPager= (ViewPager) view.findViewById(R.id.imooc_viewPager);
//
//        return view;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imooc_changes_activity);
        mViewPager= (ViewPager) findViewById(R.id.imooc_viewPager);
        //为ViewPager添加动画效果只用3.0以上有效果
        mViewPager.setPageTransformer(true,new DepthPageTransformer());
//        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView=new ImageView(ImoocChangesActivity.this);
                imageView.setImageResource(mImgIds[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView);
                mList.add(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return mImgIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
    }
}
