/**
 * 
 */
package com.yoka.mrskin.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * 心得列表适配
 * @author zlz
 * @date 2016年6月16日
 */
public class ExperienceAdapter extends BaseAdapter
{

    private ViewHolder viewHolder = null;
    private Context mContext;
    private ArrayList<YKExperience> mListTopic = new ArrayList<>();

    private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true).considerExifParams(true).showImageOnLoading(R.drawable.list_default_bg)
            .build();
    private  DisplayImageOptions     options1 = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true).considerExifParams(true).showImageOnLoading(R.drawable.right_nologin)
            .build();

    /**
     * @param mContext
     * @param mListTopic
     */
    public ExperienceAdapter(Context mContext,
            ArrayList<YKExperience> mListTopic) {
        this.mContext = mContext;
        this.mListTopic = mListTopic;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public int getCount() {
        return (int) Math.ceil((double) mListTopic.size() / 2);
    }

    @Override
    public Object getItem(int position) {
        return mListTopic.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /* (non-Javadoc)
     * @see android.widget.BaseAdapter#notifyDataSetChanged()
     */

    public void update(ArrayList<YKExperience> _mListTopic,boolean isRefresh) {
        //刷新时 清空列表
        if(isRefresh){
            mListTopic.clear();
        }
        mListTopic.addAll(_mListTopic);
        notifyDataSetChanged();
    }

    public void removeAll(){
        mListTopic.clear();
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView,
            ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_experience_item,
                    null, false);
            viewHolder = new ViewHolder();
            viewHolder.experienceHeadImageView1 = (RoundImage) convertView
                    .findViewById(R.id.experienceHeadImageView1);
            viewHolder.experienceHeadImageView2 = (RoundImage) convertView
                    .findViewById(R.id.experienceHeadImageView2);
            viewHolder.experienceImageView1 = (ImageView) convertView
                    .findViewById(R.id.experienceImageView1);
            viewHolder.experienceImageView2 = (ImageView) convertView
                    .findViewById(R.id.experienceImageView2);
            viewHolder.experienceNameTextView1 = (TextView) convertView
                    .findViewById(R.id.experienceNameTextView1);
            viewHolder.experienceNameTextView2 = (TextView) convertView
                    .findViewById(R.id.experienceNameTextView2);
            viewHolder.experienceAgeTextView1 = (TextView) convertView
                    .findViewById(R.id.experienceAgeTextView1);
            viewHolder.experienceAgeTextView2 = (TextView) convertView
                    .findViewById(R.id.experienceAgeTextView2);
            viewHolder.experienceHeadTimeTextView1 = (TextView) convertView
                    .findViewById(R.id.experienceHeadTimeTextView1);
            viewHolder.experienceHeadTimeTextView2 = (TextView) convertView
                    .findViewById(R.id.experienceHeadTimeTextView2);
            viewHolder.experienceTitleTextView1 = (TextView) convertView
                    .findViewById(R.id.experienceTitleTextView1);
            viewHolder.experienceTitleTextView2 = (TextView) convertView
                    .findViewById(R.id.experienceTitleTextView2);
            viewHolder.experienceHeadVipImageView1 = (ImageView) convertView
                    .findViewById(R.id.experienceHeadVipImageView1);
            viewHolder.experienceHeadVipImageView2 = (ImageView) convertView
                    .findViewById(R.id.experienceHeadVipImageView2);

            viewHolder.homeExperienceRightLinearLayout = (LinearLayout) convertView
                    .findViewById(R.id.homeExperienceRightLinearLayout);
            viewHolder.homeExperienceLeftLinearLayout = (LinearLayout) convertView
                    .findViewById(R.id.homeExperienceLeftLinearLayout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int pos01 = position * 2;
        int pos02 = position * 2 + 1;
        setView(mListTopic.get(pos01),
                viewHolder.homeExperienceLeftLinearLayout,
                viewHolder.experienceHeadImageView1,
                viewHolder.experienceImageView1,
                viewHolder.experienceNameTextView1,
                viewHolder.experienceAgeTextView1,
                viewHolder.experienceHeadTimeTextView1,
                viewHolder.experienceTitleTextView1,
                viewHolder.experienceHeadVipImageView1);

        if (pos02 != mListTopic.size()) {
            setView(mListTopic.get(pos02),
                    viewHolder.homeExperienceRightLinearLayout,
                    viewHolder.experienceHeadImageView2,
                    viewHolder.experienceImageView2,
                    viewHolder.experienceNameTextView2,
                    viewHolder.experienceAgeTextView2,
                    viewHolder.experienceHeadTimeTextView2,
                    viewHolder.experienceTitleTextView2,
                    viewHolder.experienceHeadVipImageView2);
            viewHolder.homeExperienceRightLinearLayout
            .setVisibility(View.VISIBLE);
        } else {
            viewHolder.homeExperienceRightLinearLayout
            .setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void setView(final YKExperience dto, LinearLayout linearLayout,
            RoundImage roundImage, ImageView imageView, TextView name,
            TextView age, TextView time, TextView title, ImageView vip) {

        if (dto.getImages() != null && dto.getImages().size() != 0) {
            /*	Glide.with(MrSkinApplication.getApplication()).load(dto.getImages().get(0).getmURL())
			.into(imageView);*/

            ImageLoader.getInstance().displayImage(dto.getImages().get(0).getmURL(), imageView, options);
            /*  ImageUtils.getImageLoader().displayImage(
                    dto.getImages().get(0).getmURL(), imageView, options);*/

        }
        String url = dto.getAuthor().getAvatar().getmURL();
        if(!TextUtils.isEmpty(url)){}

        ImageLoader.getInstance().displayImage(url, roundImage, options1);

        //Glide.with(MrSkinApplication.getApplication()).load(url).into(roundImage).onLoadStarted(mContext.getResources().getDrawable(R.drawable.default_user_bg));;

        title.setText(dto.getTitle());
        name.setText(dto.getAuthor().getName());
        if (dto.getAuthor().getAge() == 0) {
            age.setVisibility(View.INVISIBLE);
        } else {
            age.setVisibility(View.VISIBLE);
        }

        if (dto.getAuthor().getVip()==0) {
            vip.setVisibility(View.INVISIBLE);
        } else {
            vip.setVisibility(View.VISIBLE);
        }
        age.setText(dto.getAuthor().getAge()
                + mContext.getString(R.string.experience_age));
        time.setText(TimeUtil.forTimeForYearMonthDayShorthandNew(dto
                .getTime()));
        linearLayout.setOnClickListener(new OnClickListener() {

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
                        shareTop.putExtra("identification", "comment_list");
                        shareTop.putExtra("title", dto.getTitle());
                        shareTop.putExtra("track_type", "home_topic_topic");
                        shareTop.putExtra("track_type_id", dto.getID());
                        mContext.startActivity(shareTop);
                        //                            TrackManager.getInstance().addTrack(
                        //                                    TrackUrl.ITEM_CLICK + dto.getID()
                        //                                    + "&type=home_topic_topic");
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
    }

    class ViewHolder
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
}

