package com.example.lenovo.amuse.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.adapter.CommentAdapter;
import com.example.lenovo.amuse.mode.CommentSuccess;
import com.example.lenovo.amuse.mode.GiveSuccess;
import com.example.lenovo.amuse.mode.RegisterSuccess;
import com.example.lenovo.amuse.mode.SnapShortDetailsMode;
import com.example.lenovo.amuse.util.BaseUri;
import com.example.lenovo.amuse.util.ServiceMessage;
import com.makeramen.roundedimageview.RoundedImageView;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/10/25.
 * 快拍详情
 */

public class SnapShortDetailsActivity extends BaseActivity implements View.OnClickListener {

    private SnapShortDetailsMode mSnapShortDetailsMode;
    //头像
    private RoundedImageView roundedImageView;
    //自己的头像
    private RoundedImageView roundedImageView_own;
    //用户名
    private TextView text_user;
    //时间
    private TextView text_time;
    //关注
    private TextView text_follow;
    //主题
    private TextView text_believe;
    //评论
    private TextView text_comment;
    //点赞
    private TextView text_give;
    //读取数
    private TextView text_read;
    //评论按钮
    private TextView text_commentB;
    private EditText editText_content;
    //视频
    private VideoView videoView;
    private ListView listView;
    //图片
    private FinalBitmap mFinalBitmap;
    private List<SnapShortDetailsMode.ResultCodeBean.CommentBean> mList = new ArrayList<>();
    //list View的适配器
    CommentAdapter commentAdapter;

    private String token;
    private String snapId;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.SNAP_CODE:
                    mSnapShortDetailsMode = parseMode(msg.obj);

                    mFinalBitmap.display(roundedImageView, BaseUri.BASE + mSnapShortDetailsMode.getResultCode().getPic());
                    String own = ((MyApplication) getApplication()).getSuccessMode().getResultCode().getImgUrl();
                    mFinalBitmap.display(roundedImageView_own, BaseUri.BASE + own);

                    text_user.setText(mSnapShortDetailsMode.getResultCode().getNickname());
                    text_time.setText(mSnapShortDetailsMode.getResultCode().getAddtime());
                    text_believe.setText(mSnapShortDetailsMode.getResultCode().getTitle());
                    text_comment.setText(mSnapShortDetailsMode.getResultCode().getCommentnum());
                    text_give.setText(mSnapShortDetailsMode.getResultCode().getIspraise());
                    text_read.setText(mSnapShortDetailsMode.getResultCode().getCount());

                    videoView.setVideoURI(Uri.parse(mSnapShortDetailsMode.getResultCode().getVideo()));
                    MediaController mediaController = new MediaController(SnapShortDetailsActivity.this);
                    videoView.setMediaController(mediaController);

                    for (int i = 0; i < mSnapShortDetailsMode.getResultCode().getComment().size(); i++) {
                        mList.add(mSnapShortDetailsMode.getResultCode().getComment().get(i));
                    }
                    listView.setAdapter(commentAdapter);
                    //计算list高度
                    getViewHright(listView);
                    commentAdapter.notifyDataSetChanged();
                    break;
                case BaseUri.SNAP_COMMENT_CODE:
                    CommentSuccess commentSuccess = (CommentSuccess) msg.obj;
                    if (commentSuccess != null) {
                        Toast.makeText(SnapShortDetailsActivity.this, "评论" + commentSuccess.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    //计算list高度
                    getViewHright(listView);
                    //刷新当前的Activity
                    commentAdapter.notifyDataSetChanged();
                    break;
                case BaseUri.SNAP_FABULOUS_CODE:
                    GiveSuccess giveSuccess = (GiveSuccess) msg.obj;
                    if (giveSuccess != null) {
                        if (giveSuccess.getResultCode().getPraise().equals("1")) {
                            Drawable nav_up = getResources().getDrawable(R.drawable.give_y);
                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                            text_give.setCompoundDrawables(null, null, nav_up, null);
                            Toast.makeText(SnapShortDetailsActivity.this, "点赞" + giveSuccess.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Drawable nav_up = getResources().getDrawable(R.drawable.give_w);
                            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                            text_give.setCompoundDrawables(null, null, nav_up, null);
                            Toast.makeText(SnapShortDetailsActivity.this, "取消点赞", Toast.LENGTH_LONG).show();
                        }
                    }
                    text_give.setText(mSnapShortDetailsMode.getResultCode().getIspraise());
                    //计算list高度
                    getViewHright(listView);
                    //刷新当前的Activity
                    commentAdapter.notifyDataSetChanged();
                    break;
                case BaseUri.SNAP_FOLLOW_CODE:
                    RegisterSuccess registerSuccess = (RegisterSuccess) msg.obj;
                    if (registerSuccess != null) {
                        if (registerSuccess.getMessage().contains("成功")) {
                            text_follow.setText("已关注");
                            text_follow.setCompoundDrawables(null, null, null, null);
                            Toast.makeText(SnapShortDetailsActivity.this, "关注"+registerSuccess.getMessage(), Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(SnapShortDetailsActivity.this, registerSuccess.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    //计算list高度
                    getViewHright(listView);
                    //刷新当前的Activity
                    commentAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private SnapShortDetailsMode parseMode(Object obj) {
        SnapShortDetailsMode snapShortDetailsMode = null;
        if (obj != null && obj instanceof SnapShortDetailsMode) {
            snapShortDetailsMode = (SnapShortDetailsMode) obj;
        }
        return snapShortDetailsMode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap_short_details);
        token = ((MyApplication) getApplication()).getSuccessMode().getResultCode().getToken();
        //初始化控件
        initView();
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        snapId = getIntent().getStringExtra("snapId");
        String url = BaseUri.SNAPSHORT_DETAILS + "&token=" + token + "&qpid=" + snapId + "&pageNumber=" + "1" + "&pageSize=" + "50";
        ServiceMessage<SnapShortDetailsMode> serviceMessage = new ServiceMessage<SnapShortDetailsMode>(url, BaseUri.SNAP_CODE, new SnapShortDetailsMode());
        httpTools.getServiceMessage(mHandler, serviceMessage);
    }


    /**
     * 初始化控件
     */
    private void initView() {
        //头像
        roundedImageView = (RoundedImageView) findViewById(R.id.snap_details_img);
        roundedImageView_own = (RoundedImageView) findViewById(R.id.snap_details_own);
        //用户名
        text_user = (TextView) findViewById(R.id.snap_details_user);
        //时间
        text_time = (TextView) findViewById(R.id.snap_details_time);
        //关注
        text_follow = (TextView) findViewById(R.id.snap_details_follow);
        text_follow.setOnClickListener(this);
        //相信自己
        text_believe = (TextView) findViewById(R.id.snap_details_believe);
        //评论条数
        text_comment = (TextView) findViewById(R.id.snap_details_comment);
        //点赞
        text_give = (TextView) findViewById(R.id.snap_details_give);
        text_give.setOnClickListener(this);
        //读取数
        text_read = (TextView) findViewById(R.id.snap_details_read);
        //评论按钮
        text_commentB = (TextView) findViewById(R.id.snap_details_commentB);
        text_commentB.setOnClickListener(this);
        ImageView imageView_return = (ImageView) findViewById(R.id.snap_details_return);
        imageView_return.setOnClickListener(this);


        //评论内容
        editText_content = (EditText) findViewById(R.id.snap_details_content);
        //视频
        videoView = (VideoView) findViewById(R.id.snap_details_video);
        //listView
        listView = (ListView) findViewById(R.id.snap_details_listView);
        commentAdapter = new CommentAdapter(mList, this);
        mFinalBitmap = FinalBitmap.create(this);

        //scroll+listView设置一起滚动
        ScrollView zhiding = (ScrollView) findViewById(R.id.snap_details_scroll);
        zhiding.setFocusable(true);
        zhiding.setFocusableInTouchMode(true);
        zhiding.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snap_details_commentB:
                String content = editText_content.getText().toString();
                String url = BaseUri.SNAPSHORT_COMMENT + "&token=" + token + "&qpid=" + snapId + "&content=" + content;
                ServiceMessage<CommentSuccess> serviceMessage = new ServiceMessage<CommentSuccess>(url, BaseUri.SNAP_COMMENT_CODE, new CommentSuccess());
                httpTools.getServiceMessage(mHandler, serviceMessage);
                break;
            case R.id.snap_details_give:
                String uri = BaseUri.SNAPSHORT_FABULOUS + "&token=" + token + "&qpid=" + snapId;
                ServiceMessage<GiveSuccess> service = new ServiceMessage<GiveSuccess>(uri, BaseUri.SNAP_FABULOUS_CODE, new GiveSuccess());
                httpTools.getServiceMessage(mHandler, service);
                break;
            case R.id.snap_details_return:
                startActivity(new Intent(SnapShortDetailsActivity.this, SnapShortActivity.class));
                break;
            case R.id.snap_details_follow:
                String uri_follow = BaseUri.SNAPSHORT_FOLLOW + "&token=" + token + "&id=" + snapId;
                ServiceMessage<RegisterSuccess> service_follow = new ServiceMessage<RegisterSuccess>(uri_follow, BaseUri.SNAP_FOLLOW_CODE, new RegisterSuccess());
                httpTools.getServiceMessage(mHandler, service_follow);
                break;

        }
    }

    /**
     * 测量ListView的高度
     *
     * @param listView
     */
    public void getViewHright(ListView listView) {

        Adapter adapter = listView.getAdapter();
        int gd = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, listView);
            view.measure(0, 0);
            int item = view.getMeasuredHeight();
            gd += item;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = gd + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
