package com.yoka.mrskin.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.managers.YKVideoManager;
import com.yoka.mrskin.model.managers.YKVideoManager.Callback;
import com.yoka.mrskin.util.YKSharelUtil;

public class VideoListActivity extends BaseActivity implements OnClickListener,IXListViewListener
{
    private LinearLayout mBack;
    private XListView mXListView;
    private ArrayList<YKTopicData> mListTopic;
    private Context mContext;
    private MyAdapter myAdapter;
    private int mIndex = 2;
    private XListViewFooter mListViewFooter;
    private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.resetViewBeforeLoading(true).considerExifParams(true)
			.build();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list);
        init();
        getData();
    }
    private void init(){

        mContext =  this;
        mBack = (LinearLayout) findViewById(R.id.video_back_layout);
        mBack.setOnClickListener(this);
        mXListView = (XListView) findViewById(R.id.video_listview);
        mXListView.setXListViewListener(this);
        mXListView.setPullLoadEnable(true);
        mXListView.setPullRefreshEnable(true);
        mListViewFooter = new XListViewFooter(mContext);
        mXListView.setOnScrollListener(new OnScrollListener()
        {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                        mListViewFooter.setState(XListViewFooter.STATE_READY);
                        mXListView.startLoadMore();
                    }
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount)
            {
                
            }
        });

        myAdapter = new MyAdapter();
        mXListView.setAdapter(myAdapter);
    }

    private void getData(){
        mIndex = 0;
        String index = String.valueOf(mIndex);
        YKVideoManager.getInstance().requestVideo(index, 10, new Callback()
        {
            @Override
            public void callback(ArrayList<YKTopicData> topic, YKResult result)
            {
                mXListView.stopRefresh();
                if(result.isSucceeded()){
                    mListTopic = topic;
                    myAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private class MyAdapter extends BaseAdapter
    {

        private ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            if (mListTopic == null) {
                return 0;
            }
            return mListTopic.size();
        }

        @Override
        public Object getItem(int position) {
            if (mListTopic == null) {
                return null;
            }
            return mListTopic.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {

            final YKTopicData topic = mListTopic.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.video_list_item, null);
                viewHolder = new ViewHolder();

                viewHolder.sTitle = (TextView) convertView
                        .findViewById(R.id.video_item_title);
                viewHolder.sBigImage = (ImageView) convertView
                        .findViewById(R.id.video_item_image);


                convertView.setTag(R.id.video_listview, viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag(R.id.video_listview);
            }
            viewHolder.sTitle.setText(topic.getmTopicTitle());
        
            ImageLoader.getInstance().displayImage(topic.getmCover().getmURL(), viewHolder.sBigImage, options);
          	/*Glide.with(VideoListActivity.this).load(topic.getmCover().getmURL())
    		.into(viewHolder.sBigImage);*/
          	
            convertView.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    String webPager = null;
                    if (topic.getmTopicUrl() != null) {
                        webPager = YKSharelUtil.tryToGetWebPagemUrl(
                                VideoListActivity.this, topic.getmTopicUrl());
                    }
                    Intent shareTop = new Intent(VideoListActivity.this,
                            YKWebViewActivity.class);
                    if (webPager != null) {
                        String url = topic.getmTopicUrl();
                        Uri uri = Uri.parse(url);
                        shareTop.putExtra("url",
                                uri.getQueryParameter("url"));
                        shareTop.putExtra("title", topic.getmTopicUrl());
                        shareTop.putExtra("track_type", "home_topic_topic");
                        shareTop.putExtra("identification", "html");
                        shareTop.putExtra("track_type_id", topic.getmTopicUrl());
                        startActivity(shareTop);
//                        TrackManager.getInstance().addTrack(
//                                TrackUrl.ITEM_CLICK
//                                + topic.getID()
//                                + "&type=home_topic_topic");
                    }
                }
            });
            return convertView;
        }
    }

    private class ViewHolder
    {
        private TextView sTitle;
        private ImageView sBigImage;
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
        case R.id.video_back_layout:
            finish();
            break;

        default:
            break;
        }
    }
    @Override
    public void onRefresh()
    {
        getData();

    }
    @Override
    public void onLoadMore()
    {
        String index = String.valueOf(mIndex);
        YKVideoManager.getInstance().requestVideo(index, 10, new Callback()
        {
            @Override
            public void callback(ArrayList<YKTopicData> topic, YKResult result)
            {
                mXListView.stopLoadMore();
                if(result.isSucceeded()){
                    if(topic == null){
                        Toast.makeText(VideoListActivity.this, "没有更多的视频了", Toast.LENGTH_SHORT).show();
                    }else{
                        mListTopic.addAll(topic);
                        myAdapter.notifyDataSetChanged();
                        ++mIndex;
                    }
                }

            }
        });
    }
}
