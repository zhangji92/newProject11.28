package com.yoka.mrskin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.util.RoundImage;

public class NewsAdapter extends BaseAdapter
{


	private Context mContext;
	private ArrayList<YKTopicData> mInformationDatas = new ArrayList<YKTopicData>();

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.list_default_bg)
	.cacheInMemory(true).cacheOnDisk(true)
	.resetViewBeforeLoading(true).considerExifParams(true)
	.showImageOnLoading(R.drawable.list_default_bg)
	.build();



	public NewsAdapter(Context mContext,
			ArrayList<YKTopicData> mInformationDatas) {
		super();
		this.mContext = mContext;
		this.mInformationDatas = mInformationDatas;
	}


	public void update(ArrayList<YKTopicData> _mInformationDatas,boolean isRefresh){

		if(isRefresh){
			mInformationDatas.clear();
		}

		mInformationDatas.addAll(_mInformationDatas);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if (mInformationDatas == null) {
			return 0;
		}
		return mInformationDatas.size();
	}

	@Override
	public Object getItem(int position) {
		if (mInformationDatas == null) {
			return null;
		}
		return mInformationDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		final YKTopicData topic = mInformationDatas.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.search_layout_itemnew, null);
			viewHolder = new ViewHolder();

			viewHolder.sTitle = (TextView) convertView
					.findViewById(R.id.search_layout_newtitle);
			viewHolder.sAuto = (TextView) convertView
					.findViewById(R.id.search_layout_newauto);

			viewHolder.sRoudImage = (RoundImage) convertView
					.findViewById(R.id.search_layout_newimage);

			convertView.setTag(R.id.search_layout_list, viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView
					.getTag(R.id.search_layout_list);
		}
		viewHolder.sTitle.setText(topic.getmTopicTitle());
		if (topic.getmCover() == null
				&& !TextUtils.isEmpty(topic.getmCover().getmURL())) {
			viewHolder.sRoudImage
			.setBackgroundResource(R.drawable.list_default_bg);
		} else {
			try {
				ImageLoader.getInstance().displayImage(topic.getmCover().getmURL(), viewHolder.sRoudImage, options); 
				/*             Glide.with(SearchLayoutActivity.this).load(topic.getmCover().getmURL())
        		.into( viewHolder.sRoudImage).onLoadStarted(getResources().getDrawable(R.drawable.default_user_bg));*/
			} catch (Exception e) {
				viewHolder.sRoudImage.setBackgroundResource(R.drawable.right_nologin);
			}
		}
		viewHolder.sAuto.setText(topic.getmUser());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent productPlandetalis = new Intent(
						mContext, YKWebViewActivity.class);
				productPlandetalis.putExtra("productdetalis",topic.getmTopicUrl());
				productPlandetalis.putExtra("identification", "search_result");
				productPlandetalis.putExtra("track_type", "information");
				productPlandetalis.putExtra("track_type_id", topic.getID());
				mContext.startActivity(productPlandetalis);
				//                TrackManager.getInstance().addTrack(
				//                        TrackUrl.ITEM_CLICK + topic.getID()
				//                        + "&type=information");
			}
		});
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.ITEM_EXPOSURE + topic.getID()
		//                + "&type=information");
		return convertView;
	}

	class ViewHolder{
		private TextView sAuto, sTitle;
		private RoundImage sRoudImage;
	}
}