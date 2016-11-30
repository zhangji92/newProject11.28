/**
 * 
 */
package com.yoka.mrskin.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adsame.main.AdListener;
import com.adsame.main.AdMediaPlayerCallback;
import com.adsame.main.AdsameBannerAd;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.login.YKUserInfo;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.HomeData;
import com.yoka.mrskin.model.data.YKFlag;
import com.yoka.mrskin.model.data.YKNewExperience;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.data.YKVote;
import com.yoka.mrskin.model.data.YKVoteResult;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKVoteManager;
import com.yoka.mrskin.model.managers.YKVoteManager.VoteCallback;
import com.yoka.mrskin.util.RoundImage;

/**
 * @author zlz
 * @date 2016年6月15日
 * 首页适配器
 */
public class HomeAdapter extends BaseAdapter{
	private static final String TAG = HomeAdapter.class.getSimpleName();
	private List<HomeData> homeList = new ArrayList<>();
	private Context mContext;
	private static final int REQUEST_CODE = 13;
	private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.showImageOnLoading(R.drawable.list_default_bg)
			.build();
	private  DisplayImageOptions     optionsUserImage = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.showImageOnLoading(R.drawable.right_nologin)
			.build();

	private int windowWidth;
	/**
	 * @param mListRecommendation
	 * @param mContext
	 */
	public HomeAdapter(List<HomeData> homeList,
			Context mContext) {
		this.homeList = homeList;
		this.mContext = mContext;
		WindowManager wm = (WindowManager) this.mContext
				.getSystemService(Context.WINDOW_SERVICE);
		windowWidth= wm.getDefaultDisplay().getWidth();

	}

	@Override
	public int getCount() {
		return null == homeList ? 0 : homeList.size();
	}

	@Override
	public Object getItem(int position) {
		if (null == homeList) {
			return null;
		}
		return homeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void update(List<HomeData> _homeList,boolean isRefresh){
		if(isRefresh){
			this.homeList.clear();
		}
		this.homeList.addAll(_homeList);
		notifyDataSetChanged();

	}


	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {

		HomeHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.home_list_item, null);

			holder = new HomeHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (HomeHolder)convertView.getTag();
		}

		HomeData data = homeList.get(position);
		holder.showView(data.getType());
		holder.setData(data);


		return convertView;
	}



	/**
	 * 是否为当天
	 * @param date
	 * @return
	 */
	private boolean formatDate(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("d");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		if (str.equals(String.valueOf(date))) {
			return true;
		} else {
			return false;
		}

	}

	private class HomeHolder{

		View flagLayout,topicLayout,experLayout,titleLayout,voteLayout,adLayout;
		FlagHolder flagHolder;
		TopicHolder topicHolder;
		ExperHolder experHolder;
		TitleHolder titleHolder;
		VoteHolder voteHolder;
		AdHolder adHolder;
		int type;


		public HomeHolder(View convertView) {
			flagLayout = convertView.findViewById(R.id.home_flag_item);
			topicLayout = convertView.findViewById(R.id.home_topic_item);
			titleLayout = convertView.findViewById(R.id.home_title_item);
			experLayout = convertView.findViewById(R.id.home_expert_item);
			voteLayout = convertView.findViewById(R.id.home_vote_item);
			adLayout = convertView.findViewById(R.id.home_ad_item);

			flagHolder = new FlagHolder(flagLayout);
			topicHolder = new TopicHolder(topicLayout);
			experHolder = new ExperHolder(experLayout);
			titleHolder = new TitleHolder(titleLayout);
			voteHolder = new VoteHolder(voteLayout);
			adHolder = new AdHolder(adLayout);

		}


		private void showView(int type){
			this.type = type;
			switch (type) {
			case 1://Flag 日期
				flagLayout.setVisibility(View.VISIBLE);
				topicLayout.setVisibility(View.GONE);
				experLayout.setVisibility(View.GONE);
				titleLayout.setVisibility(View.GONE);
				voteLayout.setVisibility(View.GONE);
				adLayout.setVisibility(View.GONE);
				break;
			case 2://头条
				flagLayout.setVisibility(View.GONE);
				topicLayout.setVisibility(View.VISIBLE);
				experLayout.setVisibility(View.GONE);
				titleLayout.setVisibility(View.GONE);
				voteLayout.setVisibility(View.GONE);
				adLayout.setVisibility(View.GONE);
				break;
			case 3://晒物志
				flagLayout.setVisibility(View.GONE);
				topicLayout.setVisibility(View.GONE);
				experLayout.setVisibility(View.VISIBLE);
				titleLayout.setVisibility(View.GONE);
				voteLayout.setVisibility(View.GONE);
				adLayout.setVisibility(View.GONE);

				break;
			case 4://标题
				flagLayout.setVisibility(View.GONE);
				topicLayout.setVisibility(View.GONE);
				experLayout.setVisibility(View.GONE);
				voteLayout.setVisibility(View.GONE);
				titleLayout.setVisibility(View.VISIBLE);
				adLayout.setVisibility(View.GONE);

				break;

			case 5://投票
				flagLayout.setVisibility(View.GONE);
				topicLayout.setVisibility(View.GONE);
				experLayout.setVisibility(View.GONE);
				titleLayout.setVisibility(View.GONE);
				voteLayout.setVisibility(View.VISIBLE);
				adLayout.setVisibility(View.GONE);
				break;
			case 6://广告
				flagLayout.setVisibility(View.GONE);
				topicLayout.setVisibility(View.GONE);
				experLayout.setVisibility(View.GONE);
				titleLayout.setVisibility(View.GONE);
				voteLayout.setVisibility(View.GONE);
				adLayout.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}

		private void setData(HomeData data){
			switch (type) {
			case 1://falg
				flagHolder.setData(data.getYkFlag());
				break;
			case 2://头条
				topicHolder.setData(data.getTopic());

				break;
			case 3://晒物志
				experHolder.setData(data.getExpert());

				break;
			case 4://标题
				titleHolder.setData(data.getTitle());

				break;
			case 5://投票
				voteHolder.setData(data.getVote());
			case 6://广告
				adHolder.setData(data.getBannerAdId());

			default:
				break;
			}

		}


	}
	/**
	 * 日期Holder
	 */
	private class FlagHolder{
		TextView sDate,sMounth,sLogan,topArticle, sTitle;
		ImageView sTimeImage;
		/**
		 * 
		 */
		public FlagHolder(View view) {
			sDate = (TextView) view
					.findViewById(R.id.home_fragment_day);
			sMounth = (TextView) view
					.findViewById(R.id.home_fragment_date_month);
			sLogan = (TextView) view
					.findViewById(R.id.home_fragment_slogan);
			sTimeImage = (ImageView) view
					.findViewById(R.id.home_date_image);
		}
		@SuppressLint("ResourceAsColor")
		private void setData(YKFlag flag){
			if(null == flag){
				return;
			}

			if(formatDate(String.valueOf(flag.getDate().getmDay()))){
				sTimeImage.setBackgroundResource(R.drawable.date2);
				sMounth.setTextColor(mContext.getResources().getColor(R.color.red_f30));
			}else{
				sTimeImage.setBackgroundResource(R.drawable.date1);
				sMounth.setTextColor(mContext.getResources().getColor(R.color.gray_6c6d));
			}
			sDate.setText(String.valueOf(flag.getDate().getmDay()));
			sLogan.setText(flag.getBrief());
			sMounth.setText(getEnglishMon(flag.getDate().getmMonth()) + ". " + flag.getDate().getmYear());
		}


	}
	/**
	 * 投票Holder
	 * @author zlz
	 * @Data 2016年8月18日
	 */
	private class VoteHolder{
		TextView voteTitle,voteYes,voteNo,voteResultYes,voteResultNo,votePercentYes,votePercentNo,linkTitle;
		ProgressBar voteProgress,voteResultProgress;
		RelativeLayout voteLayout,resultLayout,relateLayout;
		private YKVote vote;

		public VoteHolder(View v){
			voteTitle = (TextView) v.findViewById(R.id.vote_title);
			//投票部分
			voteLayout = (RelativeLayout) v.findViewById(R.id.vote_click_rl);
			voteYes = (TextView) v.findViewById(R.id.vote_yes);
			voteNo = (TextView) v.findViewById(R.id.vote_no);
			voteProgress = (ProgressBar) v.findViewById(R.id.vote_percent_bar);
			//投票显示部分
			resultLayout = (RelativeLayout) v.findViewById(R.id.vote_result_rl);
			voteResultYes = (TextView) v.findViewById(R.id.vote_yes_tv);
			voteResultNo = (TextView) v.findViewById(R.id.vote_no_tv);

			voteResultProgress = (ProgressBar) v.findViewById(R.id.vote_result_bar);
			votePercentYes =  (TextView) v.findViewById(R.id.vote_yes_percent);
			votePercentNo =  (TextView) v.findViewById(R.id.vote_no_percent);

			//关联资讯
			relateLayout = (RelativeLayout) v.findViewById(R.id.relative_article_rl);
			linkTitle = (TextView) v.findViewById(R.id.vote_relative_title);

		}

		private void setData(final YKVote vote){
			if(null == vote){
				return;
			}
			this.vote = vote;
			voteTitle.setText(vote.getVote_title());
			voteYes.setText(vote.getChoice_one());
			voteNo.setText(vote.getChoice_two());
			voteProgress.setProgress(50);//支持 反对按钮 长度各半
			voteProgress.setSecondaryProgress(51);//分割线

			if(0 == vote.getUservoteflag() && vote.getVoteflag() == 1 ){//未投票 有效期内
				voteLayout.setVisibility(View.VISIBLE);
				resultLayout.setVisibility(View.GONE);

				relateLayout.setVisibility(View.GONE);
			}else{//已投票--显示投票结果
				voteLayout.setVisibility(View.GONE);
				resultLayout.setVisibility(View.VISIBLE);

				relateLayout.setVisibility(View.VISIBLE);//已投票 展示相关链接
			}

			voteResultYes.setText(vote.getChoice_one());
			voteResultNo.setText(vote.getChoice_two());

			setProgress();

			//赞成票

			voteYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(YKCurrentUserManager.getInstance().isLogin()){

						if(vote.getVoteflag() == 1){//投票有效期内

							requestVote(Integer.parseInt(vote.getId()),1);
						}else{
							Toast.makeText(mContext, "该投票已无效~", Toast.LENGTH_SHORT).show();
						}
					}else{//跳转登录页面

						toLogin();

					}
				}
			});


			//反对票
			voteNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(YKCurrentUserManager.getInstance().isLogin()){
						if(vote.getVoteflag() == 1){//投票有效期内
							requestVote(Integer.parseInt(vote.getId()),2);
						}else{	
							Toast.makeText(mContext, "该投票已无效~", Toast.LENGTH_SHORT).show();
						}
					}else{
						toLogin();
					}
				}		
			});	

			//相关链接
			if(!TextUtils.isEmpty(vote.getArticle_title())){
				linkTitle.setText("相关阅读："+vote.getArticle_title());
				relateLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 跳转相关文章
						Intent intent = new Intent(mContext,
								YKWebViewActivity.class);
						intent.putExtra("url", vote.getArticle_links());
						intent.putExtra("identification", "index");
						mContext.startActivity(intent);
					}
				});

			}else{
				relateLayout.setVisibility(View.GONE);
			}



		}

		/**
		 * 请求网络 投票
		 * @param voteId
		 * @param user_choice
		 */
		private void requestVote(int voteId,int user_choice){
			/*用户token*/
			String authToken = "";
			if(null != YKCurrentUserManager.getInstance().getUser()){

				authToken = YKCurrentUserManager.getInstance().getUser().getToken();
			}

			YKVoteManager.getInstnace().postVote(authToken, voteId, user_choice, new VoteCallback() {


				@Override
				public void callback(YKResult result, YKVoteResult voteResult) {
					if(result.isSucceeded()){
						YKUserInfo info = YKCurrentUserManager.getInstance().getYkUserInfo(mContext);
						info.setMoney((Integer.parseInt(info.getMoney()) + 2) +"");//本地增加2优币
						if(null != info){

							YKCurrentUserManager.getInstance().saveYkUserInfo(info, mContext);
						}

						Toast toast = Toast.makeText(mContext, "投票成功 +"+voteResult.getYobicount()+"优币", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();

						vote.setUservoteflag(1);//本地更改状态
						vote.setChoice_one_percent(voteResult.getChoice_one_percent());
						vote.setChoice_two_percent(voteResult.getChoice_two_percent());

						setProgress();

						voteLayout.setVisibility(View.GONE);
						resultLayout.setVisibility(View.VISIBLE);

						if(!TextUtils.isEmpty(vote.getArticle_title())){

							relateLayout.setVisibility(View.VISIBLE);//已投票 展示相关链接
						}else{
							relateLayout.setVisibility(View.GONE);//已投票 无相关链接
						}


					}else{
						Toast.makeText(mContext, ""+result.getMessage().toString(), Toast.LENGTH_SHORT).show();
					}


				}
			});

		}
		/**
		 * 登录 --可投票
		 */
		private void toLogin(){
			Intent intent = new Intent(mContext,
					LoginActivity.class);
			((MainActivity)mContext).startActivityForResult(intent, 11);

		}

		private void setProgress(){
			votePercentYes.setText(vote.getChoice_one_percent()+"%");
			votePercentNo.setText(vote.getChoice_two_percent()+"%");

			//投票百分比进度 (处理进度条两边预留)
			voteResultProgress.setProgress(Integer.parseInt(vote.getChoice_one_percent()) == 0 ? 
					1 : Integer.parseInt(vote.getChoice_one_percent()) == 99 ? 
							98 : Integer.parseInt(vote.getChoice_one_percent()) == 100 ?
									98:Integer.parseInt(vote.getChoice_one_percent()));
			voteResultProgress.setSecondaryProgress(Integer.parseInt(vote.getChoice_one_percent()) == 0 ?
					2 : Integer.parseInt(vote.getChoice_one_percent()) == 99 ?
							99 :Integer.parseInt(vote.getChoice_one_percent()) == 100 ?
									99 : Integer.parseInt(vote.getChoice_one_percent()) + 1);
		}


	}



	/**
	 * 头条Holder
	 */
	private class TopicHolder{
		TextView title,subTitle;
		ImageView image,titleImage;
		View mask,content;
		public TopicHolder(View view) {
			this.content = view;
			image = (ImageView) view
					.findViewById(R.id.home_toparticle_image);
			mask = view.findViewById(R.id.home_view_mask);
			title = (TextView) view
					.findViewById(R.id.home_toparticle_title);
			titleImage = (ImageView) view
					.findViewById(R.id.home_toparticle_titleimage);
			subTitle = (TextView) view
					.findViewById(R.id.home_toparticle_subtitle);

		}

		private void setData(final YKTopicData topic){
			if(null == topic){
				return;
			}
			int imageWidth = topic.getImage().getMwidth();
			int imageHeight = topic.getImage().getMheight();
			int tmpHeight = 0;
			if (imageWidth == 0 && imageHeight == 0) {
				imageWidth = 640;
				imageHeight = 450;
			}
			// calculate image height
			WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			int screenWidth = wm.getDefaultDisplay().getWidth();
			tmpHeight = screenWidth * imageHeight / imageWidth;
			image.setLayoutParams(new FrameLayout.LayoutParams(screenWidth, tmpHeight));

			mask.setLayoutParams(new FrameLayout.LayoutParams(screenWidth, tmpHeight));
			image.setScaleType(ScaleType.CENTER_CROP);
			//			Uri uri = Uri.parse("http://e.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2a5a25d15243fbf2c579ae25804ee6b8/6f061d950a7b020897f23f3863d9f2d3572cc834.jpg");
			//		        DraweeController contoroller = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
			//		        mGifView.setController(contoroller);
			ImageLoader.getInstance().displayImage(topic.getImage().getmURL(), image,options);
			/*Glide.with(MrSkinApplication.getApplication()).load(topic.getImage().getmURL()).into(image);*/

			if (topic.getmType().equals("视频评测")) {
				title.setVisibility(View.GONE);
				titleImage.setVisibility(View.VISIBLE);
				titleImage.setBackgroundResource(R.drawable.video);
			} else {
				titleImage.setVisibility(View.GONE);
				title.setVisibility(View.VISIBLE);
				title.setText(topic.getmSubTitle());
			}

			subTitle.setText(topic.getmTopicTitle());
			content.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,YKWebViewActivity.class);
					intent.putExtra("url", topic.getmTopicUrl());
					intent.putExtra("identification", "index");
					((Activity)mContext).startActivityForResult(intent, REQUEST_CODE);
				}
			});

		}
	}
	/**
	 * 晒物志Holder
	 * @author zlz
	 * @date 2016年6月17日
	 */
	private class ExperHolder{
		ImageView image;
		RoundImage userImage;
		TextView relatTitle,desc,likenum,replynum;
		View content;
		public ExperHolder(View view) {
			this.content = view;
			image = (ImageView) view
					.findViewById(R.id.shaiwuzhi_relat_image);
			userImage = (RoundImage) view
					.findViewById(R.id.shaiwuzhi_relat_userimage);
			relatTitle = (TextView) view
					.findViewById(R.id.shaiwuzhi_relat_title);
			desc = (TextView) view
					.findViewById(R.id.shaiwuzhi_relat_desc);
			likenum = (TextView) view
					.findViewById(R.id.shaiwuzhi_xin_text);
			replynum = (TextView) view
					.findViewById(R.id.shaiwuzhi_ping_text);

		}

		private void setData(final YKNewExperience exper){

			int imageWidth = exper.getmImage().getMwidth();
			int imageHeight = exper.getmImage().getMheight();
			int tmpHeight = 0;
			if (imageWidth == 0 && imageHeight == 0) {
				imageWidth = 640;
				imageHeight = 400;
			}
			// calculate image height
			WindowManager wm = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			int screenWidth = wm.getDefaultDisplay().getWidth();
			tmpHeight = screenWidth * imageHeight / imageWidth;
			image.setLayoutParams(new FrameLayout.LayoutParams(screenWidth, tmpHeight));
			image.setScaleType(ScaleType.CENTER_CROP);
			/*Glide.with(MrSkinApplication.getApplication()).load(exper.getmImage().getmURL())
			.into(image);*/
			ImageLoader.getInstance().displayImage(exper.getmImage().getmURL(), image, options);
			//			Uri uri = Uri.parse(exper.getmImage().getmURL());
			//			DraweeController contoroller = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
			//		        mGifCView.setController(contoroller);
			String userUrl = exper.getmAvatar().getmHeadUrl();
			if (TextUtils.isEmpty(userUrl)) {
				userImage.setBackgroundResource(R.drawable.right_nologin);
			} else {
				/*	Glide.with(MrSkinApplication.getApplication()).load(userUrl).
				into(userImage);*/
				ImageLoader.getInstance().displayImage(userUrl, userImage, optionsUserImage);
			}
			relatTitle.setText(exper.getmTitle());
			desc.setText(exper.getmBrief());
			likenum.setText(exper.getmLikenum());
			replynum.setText(exper.getmReplynum());
			content.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,
							YKWebViewActivity.class);
					intent.putExtra("experienceurl", exper.getmUrl());
					intent.putExtra("identification", "index");
					((Activity)mContext).startActivityForResult(intent, REQUEST_CODE);
				}
			});
		}



	}

	/**
	 * 标题Holder
	 */
	private class TitleHolder{

		TextView mTitle;
		public TitleHolder(View view) {
			mTitle = (TextView)view.findViewById(R.id.home_title);
		}

		private void setData(String title){
			mTitle.setText(title);
		}
	}

	/**
	 * 广告Holder
	 * @author zlz
	 * @Data 2016年9月13日
	 */
	private class AdHolder{
		LinearLayout parentLayout;
		public AdHolder(View view) {
			parentLayout = (LinearLayout) view;
		}

		private void setData(String adId){
			parentLayout.removeAllViews();
			final AdsameBannerAd bannerAd = new AdsameBannerAd(mContext,adId,windowWidth, (windowWidth/3) * 2);
			parentLayout.addView(bannerAd);
			bannerAd.setAdListener(new AdListener() {

				@Override
				public void onSwitchAd(AdsameBannerAd arg0) {

				}

				@Override
				public void onReceiveFailed(AdsameBannerAd arg0, int arg1) {
					parentLayout.removeView(bannerAd);
				}

				@Override
				public void onReceiveAd(AdsameBannerAd arg0) {

				}

				@Override
				public void onReadyAd(AdsameBannerAd arg0) {

				}

				@Override
				public boolean onClickAd(String url) {
					Intent intent = new Intent(mContext,YKWebViewActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("identification", "index");
					mContext.startActivity(intent);
					return false;
				}
			});
		}

	}


	/**
	 * 
	 * @param month
	 * @return 月份对应 英文
	 */
	private String getEnglishMon(int month){
		String showMonth = "";
		if (month == 1) {
			showMonth = "Jan";
		} else if (month == 2) {
			showMonth = "Feb";
		} else if (month == 3) {
			showMonth = "Mar";
		} else if (month == 4) {
			showMonth = "Apr";
		} else if (month == 5) {
			showMonth = "May";
		} else if (month == 6) {
			showMonth = "June";
		} else if (month == 7) {
			showMonth = "July";
		} else if (month == 8) {
			showMonth = "Aug";
		} else if (month == 9) {
			showMonth = "Sept";
		} else if (month == 10) {
			showMonth = "Oct";
		} else if (month == 11) {
			showMonth = "Nov";
		} else if (month == 12) {
			showMonth = "Dec ";
		}
		return showMonth;
	}


}
