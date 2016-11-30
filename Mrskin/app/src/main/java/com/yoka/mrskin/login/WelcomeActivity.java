package com.yoka.mrskin.login;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.main.MainActivity;


/**
 * 
 * @author Y H L
 * @功能描述：引导界面activity类
 *
 */
public class WelcomeActivity extends BaseActivity implements OnPageChangeListener
{

    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 定义各个界面View对象 // 定义开始按钮对象
    private View view1,view2, view3, btnViewStart;
    private ImageView mImageView1,mImageView2,mImageView3,mImageView4;
    //private String result = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_guide);
        initView();

    }

    /**
     * 初始化
     */
    private void initView() {
        //        mImageView = (YKMultiMediaView) findViewById(R.id.commmengt_fs);
        //        mImageView.setResouceId(R.drawable.video);

        //        try {
        //            InputStream in = getResources().getAssets().open("video.gif");
        //            int lenght = in.available();
        //          //创建byte数组
        //          byte[]  buffer = new byte[lenght];
        //          //将文件中的数据读到byte数组中
        //          in.read(buffer);
        //          result = EncodingUtils.getString(buffer, ENCODING);
        //
        //            mImageView.setIsForGifView(in);
        //        } catch (IOException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }

        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // 实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.welcome_viewone, null);
        view2 = mLi.inflate(R.layout.welcome_viewtwo, null);
        view3 = mLi.inflate(R.layout.welcome_viewthree, null);
        btnViewStart = mLi.inflate(R.layout.welcome_viewfour, null);

        mImageView1 = (ImageView) view1.findViewById(R.id.view_one);
        mImageView2 = (ImageView) view2.findViewById(R.id.view_two);
        mImageView3 = (ImageView) view3.findViewById(R.id.view_three);
        mImageView4 = (ImageView) btnViewStart.findViewById(R.id.view_four);

        mImageView1.setBackgroundResource(R.drawable.view_one);
        mImageView2.setBackgroundResource(R.drawable.view_two);
        mImageView3.setBackgroundResource(R.drawable.view_three);
        mImageView4.setBackgroundResource(R.drawable.view_four);

        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(btnViewStart);

        // 设置监听
        viewPager.setOnPageChangeListener(this);
        // 设置适配器数据
        viewPager.setAdapter(new ViewPagerAdapter(views));

        // 给开始按钮设置监听
        btnViewStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 存入数据并提交
                SplashActivity.sp.edit()
                .putInt("VERSION", SplashActivity.VERSION).commit();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    /**
     * 滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    /**
     * 当前页面滑动时调用
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * 新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int arg0) {
    }

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mImageView1!=null){
            mImageView1.setBackgroundResource(0);
        }
        if(mImageView2!=null){
            mImageView2.setBackgroundResource(0);
        }
        if(mImageView3!=null){
            mImageView3.setBackgroundResource(0);
        }
        if(mImageView4!=null){
            mImageView4.setBackgroundResource(0);
        }
	}

}
