package com.yoka.mrskin.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.fragment.BrandFragment;
import com.yoka.mrskin.model.data.YKBrand;
import com.yoka.mrskin.model.managers.YKSearchManager.YKSearchType;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.YKUtil;
/**
 * 品牌落地页
 * @author zlz
 * @Data 2016年7月4日
 */
public class BrandTabActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = BrandTabActivity.class.getSimpleName();
	private RadioGroup group1;
	private RadioButton rb_product,rb_experience,rb_news;
	private RadioGroup group2;
	private RadioButton rb_product2,rb_experience2,rb_news2;

	/*listView头部显示*/
	public View headerView;
	private TextView brand_title_tv,brand_desc_tv;
	private LinearLayout back;//返回按键
	private ImageView brandImg,img_open;//品牌背景图  & 文字展开更多
	private RoundImage brandIcon;//品牌标志图
	public LinearLayout tabLayout_old,tabLayout_new;
	public ImageView return_top;//置顶按钮
	public View titleView;//随滑动渐变的title
	/*子页面  产品 & 资讯 & 心得*/
	private Fragment productFragment,newsFragment,experienceFragment
	,currentFragment;//当前显示fragment

	private boolean isOpen = false;
	private YKBrand brand;
	public int brandId;
	public String brandName;

	private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.resetViewBeforeLoading(true).considerExifParams(true).showImageOnLoading(R.drawable.list_default_bg)
			.build();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brand_tabactivity_layout);


		initView();
		initData();
	}
	private void initData() {
		brand_title_tv = (TextView) headerView.findViewById(R.id.brand_title);
		brand_title_tv.setText(brand.getmTitle());
		brand_desc_tv.setText(brand.getBrandprofiles());
		brand_desc_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, YKUtil.sp2px(BrandTabActivity.this,14));
		if(measureTextViewHeight() <= 2){
			img_open.setVisibility(View.GONE);
		}else{
			brand_desc_tv.setMaxLines(2);
			img_open.setVisibility(View.VISIBLE);
		}
		ImageLoader.getInstance().displayImage(brand.getmImgBig().getmURL(), brandImg, options);
		ImageLoader.getInstance().displayImage(brand.getmImg().getmURL(), brandIcon, options);
	}
	
	/*总的高度*/
	private int measureTextViewHeight() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;     // 屏幕宽度（像素）

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		brand_desc_tv.measure(widthMeasureSpec, heightMeasureSpec);
		Log.d(TAG, "=="+brand_desc_tv.getMeasuredHeight());
		return brand_desc_tv.getMeasuredHeight()/YKUtil.sp2px(BrandTabActivity.this,14);
	}

	/**
	 * 实例化 and 设置监听
	 */
	private void initView() {
		initTabLayout();

		brand = (YKBrand) getIntent().getSerializableExtra("brand");
		brandId = Integer.parseInt("".equals(brand.getID()) ? "0" : brand.getID());
		brandName = brand.getmTitle();

		headerView = LayoutInflater.from(BrandTabActivity.this).inflate(R.layout.brand_headview, null);
		tabLayout_old = (LinearLayout) headerView.findViewById(R.id.head_tab_layout);
		titleView = findViewById(R.id.brand_title_layout);
		back = (LinearLayout) findViewById(R.id.brand_back);
		back.setOnClickListener(this);
		brandImg = (ImageView) headerView.findViewById(R.id.brand_img);
		brandIcon = (RoundImage) headerView.findViewById(R.id.brand_name_img);
		brand_desc_tv = (TextView) headerView.findViewById(R.id.brand_desc_tv);
		img_open = (ImageView) headerView.findViewById(R.id.img_open);
		img_open.setOnClickListener(this);
		return_top = (ImageView) findViewById(R.id.lv_to_top);
		return_top.setOnClickListener(this);

		group1 = (RadioGroup) headerView.findViewById(R.id.tab_group);
		rb_product = (RadioButton) headerView.findViewById(R.id.rb_left);
		rb_product.setText("产品");
		rb_experience = (RadioButton) headerView.findViewById(R.id.rb_center);
		rb_experience.setText("心得");
		rb_news = (RadioButton) headerView.findViewById(R.id.rb_right);
		rb_news.setText("资讯");


		group1.setOnCheckedChangeListener(groupListener);
		//添加默认
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		productFragment = new BrandFragment(YKSearchType.SEARCHTYPE_PRODUCT);
		ft.add(R.id.fragment_container,productFragment, "product");
		ft.commit();
		currentFragment = productFragment;


	}
	private OnCheckedChangeListener groupListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			productFragment = fm.findFragmentByTag("product");
			experienceFragment = fm.findFragmentByTag("experience");
			newsFragment = fm.findFragmentByTag("news");
			if(null != productFragment){
				ft.hide(productFragment);
			}
			if(null != experienceFragment){
				ft.hide(experienceFragment);
			}
			if(null != newsFragment){
				ft.hide(newsFragment);
			}

			switch (checkedId) {
			case R.id.rb_left:
				if(null == productFragment){
					productFragment = new BrandFragment(YKSearchType.SEARCHTYPE_PRODUCT);
					ft.add(R.id.fragment_container,productFragment, "product");
				}else{
					ft.show(productFragment);
				}
				currentFragment = productFragment;
				rb_product2.setChecked(true);

				break;
			case R.id.rb_center:
				if(null == experienceFragment){
					experienceFragment = new BrandFragment(YKSearchType.SEARCHTYPE_EXPERIENCE);
					ft.add(R.id.fragment_container,experienceFragment, "experience");
				}else{
					ft.show(experienceFragment);
				}
				currentFragment = experienceFragment;
				rb_experience2.setChecked(true);
				break;
			case R.id.rb_right:
				if(null == newsFragment){
					newsFragment = new BrandFragment(YKSearchType.SEARCHTYPE_INFORMATION);
					ft.add(R.id.fragment_container,newsFragment, "news");
				}else{
					ft.show(newsFragment);
				}
				currentFragment = newsFragment;
				rb_news2.setChecked(true);
				break;

			default:
				break;
			}


			ft.commit();

		}
	};


	private void initTabLayout() {
		tabLayout_new = (LinearLayout) findViewById(R.id.tab_layout);
		group2 = (RadioGroup) findViewById(R.id.tab_group_new);
		rb_product2 = (RadioButton) findViewById(R.id.rb_left_new);
		rb_product2.setText("产品");
		rb_experience2 = (RadioButton) findViewById(R.id.rb_center_new);
		rb_experience2.setText("心得");
		rb_news2 = (RadioButton) findViewById(R.id.rb_right_new);
		rb_news2.setText("资讯");
		group2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rb_left_new:
					if(!rb_product.isChecked()){

						rb_product.setChecked(true);
					}

					break;
				case R.id.rb_center_new:
					if(!rb_experience.isChecked()){

						rb_experience.setChecked(true);
					}
					break;
				case R.id.rb_right_new:
					if(!rb_news.isChecked()){

						rb_news.setChecked(true);
					}

					break;

				default:
					break;
				}


			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.brand_back:
			finish();
			break;
		case R.id.img_open://展开品牌描述
			if(isOpen){
				img_open.setImageResource(R.drawable.arraw_open);
				brand_desc_tv.setMaxLines(2);
				isOpen = false;
			}else{
				img_open.setImageResource(R.drawable.arrow_up);
				brand_desc_tv.setMaxLines(10);
				isOpen = true;
			}
			break;
		case R.id.lv_to_top:
			((BrandFragment)currentFragment).smoothTop();

			break;

		default:
			break;
		}

	}


}
