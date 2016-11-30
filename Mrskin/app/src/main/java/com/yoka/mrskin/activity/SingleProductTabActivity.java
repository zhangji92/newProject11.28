package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.fragment.SingleProductFragment;
/**
 * 单品列表 Tab排序
 * 妆品库-->分类/功效/品牌单品-->点击进入
 * @author zlz
 * @Data 2016年7月1日
 */
public class SingleProductTabActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = SingleProductTabActivity.class.getSimpleName();
	public static String mId,mType;
	private String mTitle;

	private RadioGroup group;
	private RadioButton rb_drfault,rb_newest,rb_hottest;
	private TextView title_tv;
	private LinearLayout back;
	private Fragment defaultFragment,newestFragment,hottestFragment,currentFragment;
	public ImageView return_top;//置顶按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_product_tabactivity);

		getinitIntent();
		initView();


	}
	/**
	 * 初始化组件 && 设置监听
	 */
	private void initView() {
		back = (LinearLayout) findViewById(R.id.single_product_back);
		title_tv = (TextView) findViewById(R.id.single_product_title);
		return_top = (ImageView) findViewById(R.id.lv_to_top);
		back.setOnClickListener(this);
		return_top.setOnClickListener(this);
		title_tv.setText(mTitle);

		group = (RadioGroup) findViewById(R.id.tab_group);
		rb_drfault = (RadioButton) findViewById(R.id.rb_left);
		rb_drfault.setText("默认");
		rb_newest = (RadioButton) findViewById(R.id.rb_right);
		rb_newest.setText("最新");
		rb_hottest = (RadioButton) findViewById(R.id.rb_center);
		rb_hottest.setText("最热");

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				defaultFragment = fm.findFragmentByTag("default");
				newestFragment = fm.findFragmentByTag("newest");
				hottestFragment = fm.findFragmentByTag("hottest");
				if(null != defaultFragment){
					ft.hide(defaultFragment);
				}
				if(null != newestFragment){
					ft.hide(newestFragment);
				}
				if(null != hottestFragment){
					ft.hide(hottestFragment);
				}
				switch (checkedId) {
				case R.id.rb_left://默认
					if(null == defaultFragment){
						defaultFragment = new SingleProductFragment(3);
						ft.add(R.id.fragment_container,defaultFragment, "default");
					}else{
						ft.show(defaultFragment);
					}
					currentFragment = defaultFragment;
					break;

				case R.id.rb_right://最新
					if(null == newestFragment){
						newestFragment = new SingleProductFragment(2);
						ft.add(R.id.fragment_container,newestFragment, "newest");
					}else{
						ft.show(newestFragment);
					}
					currentFragment = newestFragment;
					break;

				case R.id.rb_center://最热
					if(null == hottestFragment){
						hottestFragment = new SingleProductFragment(1);
						ft.add(R.id.fragment_container,hottestFragment, "hottest");
					}else{
						ft.show(hottestFragment);
					}
					currentFragment = hottestFragment;
					break;

				default:
					break;
				}
				//判断  return_top 按钮是否出现
				if(((SingleProductFragment)currentFragment).isScrolled()){
					return_top.setVisibility(View.VISIBLE);
				}else{
					return_top.setVisibility(View.GONE);
				}
				
				ft.commit();
				
				

			}
		});
		//添加默认页面
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		defaultFragment = new SingleProductFragment(3);
		ft.add(R.id.fragment_container,defaultFragment, "default");
		ft.commit();
		currentFragment = defaultFragment;


	}

	private void getinitIntent(){
		if(null != getIntent()){
			Intent pro = getIntent();
			mId = pro.getStringExtra("id");
			mType = pro.getStringExtra("type");
			mTitle = pro.getStringExtra("title");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.single_product_back:
			finish();
			break;
		case R.id.lv_to_top:        
			((SingleProductFragment)currentFragment).smoothTop();

			break;

		default:
			break;
		}

	}

}
