package com.yoka.mrskin.viewpager;

import support.vx.t3party.pagerindicator.IconPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.yoka.mrskin.R;

public class InfinitePagerAdapter extends
com.yoka.mrskin.infiniteviewpager.InfinitePagerAdapter implements
IconPagerAdapter {

	public InfinitePagerAdapter(PagerAdapter adapter) {
		super(adapter);
	}

	@Override
	public int getIconResId(int index) {
		//TODO : 待确定
		if(0 != getRealCount()){
			
			index = index % getRealCount();
			return R.drawable.point_selector;
		}
		return 0;
	}

}
