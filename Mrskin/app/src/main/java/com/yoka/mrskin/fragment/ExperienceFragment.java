package com.yoka.mrskin.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.SearchLayoutActivity;
import com.yoka.mrskin.activity.WriteExperienceActivity;
import com.yoka.mrskin.adapter.ExperienceAdapter;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKExperience;
import com.yoka.mrskin.model.data.YKExperienceTag;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKExperienceManager;
import com.yoka.mrskin.model.managers.YKExperienceTagManager;
import com.yoka.mrskin.model.managers.YKExperienceTagManager.Callback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.RoundImage;
/**
 * 美妆心得
 * @author zlz
 * @date 2016年6月16日
 */
public class ExperienceFragment extends Fragment implements OnClickListener,IXListViewListener
{
	//private static final String TAG = ExperienceFragment.class.getSimpleName();
	private Context mContext;
	private RelativeLayout mDataNullLayout;
	private RelativeLayout mErrorRelativeLayout;
	private TextView searchTv;

	//--------撰写心得----------
	private ImageView mExperienceWrite;
	private CustomButterfly mCustomButterfly = null;
	private View mRootView;

	//------------Tab选项 Layout------
	private LinearLayout mTagLayout;
	//Tag集合
	private ArrayList<YKExperienceTag> mExperienceTags = new ArrayList<YKExperienceTag>();
	//用于标记Tab上次选中
	private  int lastCheckedId = -1;
	private String mExperienceTagId = null;//当前TagId

	//----------心得列表------
	private XListView xliistView;
	private ArrayList<YKExperience> mListTopic = new ArrayList<YKExperience>();
	private ExperienceAdapter mAdapter;
	private int mPn = 0;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.experience_list, container, false);
		return mRootView; 
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		init();
	}

	private void init() {
		mDataNullLayout = (RelativeLayout) mRootView.findViewById(
				R.id.experienceno_null_layout);
		mDataNullLayout.setOnClickListener(this);
		mErrorRelativeLayout = (RelativeLayout) mRootView.findViewById(
				R.id.experienceErrorLayout);
		mErrorRelativeLayout.setOnClickListener(this);
		mExperienceWrite = (ImageView) mRootView.findViewById(R.id.home_experience_write);
		mExperienceWrite.setOnClickListener(this);
		searchTv = (TextView) mRootView.findViewById(R.id.exper_search_tv);
		searchTv.setOnClickListener(this);

		//Tab选项
		mTagLayout = (LinearLayout) mRootView.findViewById(R.id.sub_horlistview);

		//心得列表
		mAdapter = new ExperienceAdapter(mContext,mListTopic);
		xliistView = (XListView) mRootView.findViewById(R.id.experiencenewListView);
		xliistView.setXListViewListener(this);
		xliistView.setAdapter(mAdapter);

		initUpdateExperienceCache();
		initUpdateExperienceData();
		tagClassification();
	}

	//缓存的分类信息
	private void initUpdateExperienceCache(){
		ArrayList<YKExperienceTag> experienceCache = YKExperienceTagManager.getInstance().getExperienceData();
		if(experienceCache != null && experienceCache.size() > 0){
			mExperienceTags = experienceCache;
		}
	}

	//缓存的数据流
	private void initUpdateExperienceData(){
		ArrayList<YKExperience> experienceData = YKExperienceManager.getInstance().getExperienceData();
		if(experienceData != null && experienceData.size() > 0){
			mListTopic = experienceData;
			mAdapter.update(mListTopic, true);
			xliistView.setPullLoadEnable(true);
		}
	}
	/**
	 * 获取对应集合列表
	 * @param id
	 * 
	 */
	private void initData(String id) {
		mDataNullLayout.setVisibility(View.GONE);
		xliistView.setVisibility(View.VISIBLE);

		YKExperienceManager.getInstance().postYKExperienceData(mPn + "", id,new YKExperienceManager.Callback() {

			@Override
			public void callback(YKResult result,
					ArrayList<YKExperience> topicData) {
				AppUtil.dismissDialogSafe(mCustomButterfly);
				xliistView.stopLoadMore();
				xliistView.stopRefresh();
				complete(result, topicData);

			}
		});
	}


	/**
	 * 加载更多
	 * @param result 请求结果
	 * @param topicData 请求数据集合
	 * @param list 展示Listview
	 */
	private void complete(YKResult result, ArrayList<YKExperience> topicData) {
		if (result.isSucceeded()) {
			if(0 == mPn){//切换 头部 tab listview回滚到顶部
				mAdapter.update(topicData, true);

				xliistView.smoothScrollToPosition(0);
			}else{
				if(null != topicData && topicData.size() > 0){
					mAdapter.update(topicData, false);//加载更多现实下一页

				}else{

					Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();

				}
			}


			if (mListTopic.size() == 0) {
				mDataNullLayout.setVisibility(View.VISIBLE);
			}
			xliistView.setPullLoadEnable(true);

			mPn++;

		} else {
			if(lastCheckedId == 0){//显示缓存
				return;
			}
			if (topicData == null || topicData.size() == 0) {
				xliistView.setVisibility(View.GONE);
				mDataNullLayout.setVisibility(View.VISIBLE);
			}
			if (AppUtil.isNetworkAvailable(mContext)) {
				Toast.makeText(mContext, R.string.intent_error,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, R.string.intent_no, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.read_back_layout:
			// finish();
			break;
			// case R.id.addexperienceImageView:
			// if (YKCurrentUserManager.getInstance(mContext).isLogin()) {
			// Intent writeIntent = new Intent(mContext,
			// WriteExperienceActivity.class);
			// startActivity(writeIntent);
			// } else {
			// Intent loginIntent = new Intent(mContext, LoginActivity.class);
			// startActivityForResult(loginIntent, 0);
			// }
			// break;
		case R.id.experienceErrorLayout://点击错误页 重新加载，首页起
			mPn = 0;
			initData(mExperienceTagId);
			break;
		case R.id.experienceno_null_layout:
			//空白页面点击不处理---v2.6.1
			/*if (YKCurrentUserManager.getInstance(mContext).isLogin()) {
				Intent writeIntent = new Intent(mContext,WriteExperienceActivity.class);
				startActivity(writeIntent);
			} else {
				Intent loginIntent = new Intent(mContext, LoginActivity.class);
				startActivity(loginIntent);
			}*/
			break;
		case R.id.home_experience_write:
			if (YKCurrentUserManager.getInstance().isLogin()) {
				Intent writeExperience = new Intent(mContext,WriteExperienceActivity.class);
				startActivity(writeExperience);
			} else {
				Intent goLogin = new Intent(mContext, LoginActivity.class);
				startActivity(goLogin);
			}
			break;
		case R.id.exper_search_tv:
			Intent searchIntent = new Intent(mContext,SearchLayoutActivity.class);
			startActivity(searchIntent);
			break;
		default:
			break;
		}
	}

	/**
	 * 添加Tag 选项
	 */
	private void addTagView(){
		if(null == mExperienceTags){
			return;
		}

		float density = getResources().getDisplayMetrics().density;
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.experience_tag_layout, null);
		view.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		final RadioGroup radGroupCach = (RadioGroup) view.findViewById(R.id.experience_radiogroup);
		RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

		int margin = (int)(6*density);
		params_rb.setMargins(margin, 0, margin * 2,0);
		for (int i = 0; i < mExperienceTags.size(); i++) {
			final RadioButton radioCach = new RadioButton(mContext);
			radioCach.setId(i);
			radioCach.setText(mExperienceTags.get(i).getmName());
			radioCach.setButtonDrawable(android.R.color.transparent);
			radioCach.setBackgroundResource(R.drawable.tab_selector);
			radioCach.setTextColor(getActivity().getResources().getColor(R.color.text_my_experience_tab_default));
			radioCach.setTextSize(15);
			radioCach.setGravity(Gravity.CENTER);
			radioCach.setPadding(0, 0, 0, 20); 
			if(i == 0){
				radioCach.setChecked(true); 
				radioCach.setTextColor(Color.RED);
				lastCheckedId  = 0;
				initData(mExperienceTags.get(0).getID());

			}else{
				radioCach.setChecked(false);
				radioCach.setTextColor(getActivity().getResources().getColor(R.color.text_my_experience_tab_default));
			}
			radioCach.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{

					if(radioCach.isChecked()){

						((RadioButton)radGroupCach.findViewById(lastCheckedId)).setTextColor(getActivity().getResources().getColor(R.color.text_my_experience_tab_default));
						radioCach.setTextColor(Color.RED);
						lastCheckedId = radioCach.getId();
					}else{
						radioCach.setTextColor(getActivity().getResources().getColor(R.color.text_my_experience_tab_default));
					}
				}
			});

			radGroupCach.addView(radioCach,params_rb);
		}
		//添加到Tag布局中
		mTagLayout.addView(view);
		radGroupCach.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				//int radioButtonId = group.getCheckedRadioButtonId(); 
				try {
					mCustomButterfly = CustomButterfly.show(getActivity());
				} catch (Exception e) {
					mCustomButterfly = null;
				}
				mExperienceTagId = mExperienceTags.get(checkedId).getID();
				//首页开始加载
				mPn = 0;
				initData(mExperienceTagId);
			}
		});
	}

	/**
	 * 获取Tag列表 展示
	 */
	private void tagClassification(){
		try {
			mCustomButterfly = CustomButterfly.show(getActivity());
		} catch (Exception e) {
			mCustomButterfly = null;
		}
		YKExperienceTagManager.getInstance().postYKExperienceTagData(new Callback()
		{
			@Override
			public void callback(YKResult result,
					ArrayList<YKExperienceTag> experienceTag)
			{
				AppUtil.dismissDialogSafe(mCustomButterfly);
				if(result.isSucceeded()){
					mExperienceTags = experienceTag;
				}
				addTagView();
			}
		});
	}

	static class ViewHolder
	{
		ImageView experienceImageView1, experienceImageView2;
		ImageView 
		experienceHeadVipImageView2, experienceHeadVipImageView1;
		TextView experienceNameTextView1, experienceAgeTextView1,
		experienceAgeTextView2, experienceHeadTimeTextView1,
		experienceHeadTimeTextView2, experienceTitleTextView1,
		experienceNameTextView2, experienceTitleTextView2;
		RoundImage experienceHeadImageView1, experienceHeadImageView2;
		LinearLayout homeExperienceRightLinearLayout,
		homeExperienceLeftLinearLayout;
	}
	/**
	 * 刷新 请求首页
	 */
	@Override
	public void onRefresh()
	{
		mPn = 0;

		initData(TextUtils.isEmpty(mExperienceTagId) ? "" : mExperienceTagId);

	}
	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore()
	{

		initData(mExperienceTagId);

	}
}
