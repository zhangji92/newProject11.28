package com.yoka.mrskin.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.model.data.YKExperience;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.YKSharelUtil;

public class ExperienceLvAdapter extends BaseAdapter{


	private List<YKExperience> mList;
	private ViewHolderExperience viewHolder = null;
	private DisplayImageOptions options;
	private Context mContext;

	public ExperienceLvAdapter(Context context,List<YKExperience> list)
	{
		this.mList = list;
		this.mContext = context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.list_default_bg)
		.cacheInMemory(true).cacheOnDisk(true)
		.resetViewBeforeLoading(true).considerExifParams(true)
		.build();
	}

	public void update(List<YKExperience> _mList,boolean isRefresh){

		if(isRefresh){
			mList.clear();
		}
		mList.addAll(_mList);
		notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final YKExperience dto = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.my_experience_list_item, null, false);
			viewHolder = new ViewHolderExperience();
			viewHolder.experienceHeadImageView = (RoundImage) convertView
					.findViewById(R.id.myExperienceHeadImageView);
			viewHolder.experienceImageView = (ImageView) convertView
					.findViewById(R.id.myExperienceImageView);
			viewHolder.experienceNameTextView = (TextView) convertView
					.findViewById(R.id.myExperienceNameTextView);
			viewHolder.experienceAgeTextView = (TextView) convertView
					.findViewById(R.id.myExperienceAgeTextView);
			viewHolder.experienceHeadTimeTextView = (TextView) convertView
					.findViewById(R.id.myExperienceHeadTimeTextView);
			viewHolder.experienceTitleTextView = (TextView) convertView
					.findViewById(R.id.myExperienceTitleTextView);
			viewHolder.experienceConentTextView = (TextView) convertView
					.findViewById(R.id.myExperienceConentTextView);
			viewHolder.experienceSkinTextView = (TextView) convertView
					.findViewById(R.id.myExperienceSkinTextView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderExperience) convertView.getTag();
		}

		if (dto.getAuthor().getAge() == 0) {
			viewHolder.experienceAgeTextView.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.experienceAgeTextView.setVisibility(View.VISIBLE);
		}
		if (dto.getImages() != null && dto.getImages().size() != 0) {
			viewHolder.experienceImageView.setVisibility(View.VISIBLE);

			/*      Glide.with(SearchLayoutActivity.this).load(dto.getImages().get(0).getmURL())
    		.into( viewHolder.experienceImageView);*/
			ImageLoader.getInstance().displayImage(
					dto.getImages().get(0).getmURL(),
					viewHolder.experienceImageView, options);
		} else {
			viewHolder.experienceImageView.setVisibility(View.GONE);
		}
		// Glide.with(SearchLayoutActivity.this).load(dto.getImages().get(0).getmURL()).into( viewHolder.experienceHeadImageView).onLoadStarted(getResources().getDrawable(R.drawable.list_default_bg));
		ImageLoader.getInstance().displayImage(dto.getAuthor().getAvatar().getmURL(), viewHolder.experienceHeadImageView, options); 

		viewHolder.experienceHeadTimeTextView.setText(TimeUtil
				.forTimeForYearMonthDayShorthandNew(dto.getTime()));
		viewHolder.experienceAgeTextView.setText(dto.getAuthor().getAge()
				+ mContext.getString(R.string.experience_age));
		viewHolder.experienceTitleTextView.setText(dto.getTitle());
		viewHolder.experienceNameTextView
		.setText(dto.getAuthor().getName());
		viewHolder.experienceConentTextView.setText(dto.getContent());
		String type = "";
		int complexion = dto.getAuthor().getComplexion();
		if (complexion == 1) {
			type = mContext.getString(R.string.experience_tpye_mixed);
		} else if (complexion == 2) {
			type = mContext.getString(R.string.experience_tpye_dry);
		} else if (complexion == 3) {
			type = mContext.getString(R.string.experience_tpye_oily);
		} else if (complexion == 4) {
			type = mContext.getString(R.string.experience_tpye_sensitive);
		} else if ((complexion == 5)) {
			type = mContext.getString(R.string.experience_tpye_neutral);
		} else {
			type = mContext.getString(R.string.experience_tpye_unknown);
		}
		if (complexion == 0) {
			viewHolder.experienceSkinTextView.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.experienceSkinTextView.setVisibility(View.VISIBLE);
		}
		viewHolder.experienceSkinTextView.setText(type);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String webPager = null;

				if (dto != null) {
					webPager = YKSharelUtil.tryToGetWebPagemUrl(mContext,
							dto.getUrl());
				}
				Intent shareTop = new Intent(mContext,
						YKWebViewActivity.class);

				if (AppUtil.isNetworkAvailable(mContext)) {
					if (webPager != null) {
						String url = dto.getUrl();
						Uri uri = Uri.parse(url);
						shareTop.putExtra("experienceurl",
								uri.getQueryParameter("url"));
						shareTop.putExtra("identification", "search_result");
						shareTop.putExtra("title", dto.getTitle());
						shareTop.putExtra("track_type", "home_topic_topic");
						shareTop.putExtra("track_type_id", dto.getID());
						mContext.startActivity(shareTop);
						//                        TrackManager.getInstance().addTrack(
						//                                TrackUrl.ITEM_CLICK + dto.getID()
						//                                + "&type=home_topic_topic");
					} else {
						Toast.makeText(mContext,
								mContext.getString(R.string.intent_error),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mContext, mContext.getString(R.string.intent_no),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		return convertView;
	}
	class ViewHolderExperience
	{
		ImageView experienceImageView;
		TextView experienceNameTextView, experienceAgeTextView,
		experienceHeadTimeTextView, experienceTitleTextView,
		experienceConentTextView, experienceSkinTextView;
		RoundImage experienceHeadImageView;
	}

}
