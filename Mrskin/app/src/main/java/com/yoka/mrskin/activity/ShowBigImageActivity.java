package com.yoka.mrskin.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.util.YKUtil;

public class ShowBigImageActivity extends BaseActivity
{
    private ImageView mImage;

    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.show_image);
	options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true).considerExifParams(true).build();
	init();
	getImage();
    }
    private void init(){
	mImage = (ImageView) findViewById(R.id.show_image);
    }

    private void getImage(){

	Intent getImage = getIntent();
	String path = getImage.getStringExtra("imagePath");

	// calculate image height
	WindowManager wm = (WindowManager) ShowBigImageActivity.this.getSystemService(Context.WINDOW_SERVICE);
	@SuppressWarnings("deprecation")
	int screenWidth = (wm.getDefaultDisplay().getWidth())
	- YKUtil.dip2px(ShowBigImageActivity.this, 20);
	Bitmap bm = BitmapFactory.decodeFile(path);
	if(bm != null){
	    
	    int imageHeight = bm.getHeight();
	    int imageWidth  = bm.getWidth();
	    int tmpHeight = 0;
	    
	    tmpHeight = screenWidth * imageHeight / imageWidth;
	    
	    mImage.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, tmpHeight));
	    ImageLoader.getInstance().displayImage("file://" + path, mImage, options); 
	    
	}else{
	    
	    mImage.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    ImageLoader.getInstance().displayImage(path, mImage, options); 
	}
	mImage.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	    }
	});
    }
}
