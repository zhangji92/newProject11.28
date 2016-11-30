package com.yoka.mrskin.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.R.string;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKComment;
import com.yoka.mrskin.model.data.YKReplytoreplylist;
import com.yoka.mrskin.model.managers.YKCommentManager;
import com.yoka.mrskin.model.managers.YKCommentManager.CommentCallback;
import com.yoka.mrskin.model.managers.YKCommentreplyManager;
import com.yoka.mrskin.model.managers.YKCommentreplyManager.IDReplyCallback;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKDeleteInformationAndTipsAllManager;
import com.yoka.mrskin.model.managers.YKDeleteInformationAndTipsAllManager.deleteAllYKInformationAndTipsCallback;
import com.yoka.mrskin.model.managers.YKDeleteInformationAndTipsManager;
import com.yoka.mrskin.model.managers.YKDeleteInformationAndTipsManager.deleteYKInformationAndTipsCallback;
import com.yoka.mrskin.model.managers.YKThumbupManager;
import com.yoka.mrskin.model.managers.YKThumbupManager.ThumbupCallback;
import com.yoka.mrskin.model.managers.YKaddInformationAndTipsManager;
import com.yoka.mrskin.model.managers.YKaddInformationAndTipsManager.AddYKInformationAndTipsCallback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.NetWorkUtil;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.YKUtil;

/**
 * 评论列表页
 */


//1063  1091	1113 	 这几行有注释

public class CommentActivity extends BaseActivity implements OnClickListener,IXListViewListener,OnLayoutChangeListener
{
    private TextView mSed;
    private MyAdapter mAdapter;
    private LinearLayout mBack;
    private YKComment mCommentLoad = null;
    private static Context mContext;
    private XListView mListView;
    private EditText mCommentEdit;
    private ImageView mCommentImage;
    private boolean isComment = true;
    //private boolean isAllorThree = false;
    private RelativeLayout mButtonLayout;
    private String mCommentID;
    private String mClassType;
    private String mReplytoID;
    private String mReplytouserid;
    private String mReplytousername;
    private XListViewFooter mListViewFooter;
    private CustomButterfly mCustomButterfly = null;
    private ArrayList<YKComment> mArrayList = new ArrayList<YKComment>();

    //Activity最外层的Layout视图  
    private View activityRootView;  
    //屏幕高度  
    private int screenHeight = 0;  
    //软件盘弹起后所占高度阀值  
    private int keyHeight = 0; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_comment);

	YKActivityManager.getInstance().addActivity(this);
	initGetInit();
	init();
	initData(true);
    }
    private void initGetInit(){
	Intent getCommentID = getIntent();
	mCommentID = getCommentID.getStringExtra("commentid");
	mClassType = getCommentID.getStringExtra("type");
    }

    private void init() {

	mContext = CommentActivity.this;
	mBack = (LinearLayout) findViewById(R.id.activity_comment_back);
	mBack.setOnClickListener(this);
	mListView = (XListView) findViewById(R.id.activity_comment_listview);
	mButtonLayout = (RelativeLayout) findViewById(R.id.activity_comment_bottom_layout);

	mAdapter = new MyAdapter();
	mListView.setAdapter(mAdapter);
	mListViewFooter = new XListViewFooter(CommentActivity.this);
	mListView.setOnScrollListener(new OnScrollListener()
	{

	    @Override
	    public void onScrollStateChanged(AbsListView view, int scrollState)
	    {
		if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
		    if (view.getLastVisiblePosition() == (view.getCount() - 1)){
			mListViewFooter.setState(XListViewFooter.STATE_READY);
			mListView.startLoadMore();
		    }
		}
	    }

	    @Override
	    public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount)
	    {

	    }
	});

	mListView.setXListViewListener(this);
	mListView.setPullLoadEnable(true);
	mCommentEdit = (EditText) findViewById(R.id.activity_comment_edit);
	mCommentImage = (ImageView) findViewById(R.id.comment_bottom_image);

	mCommentImage.setOnClickListener(this);
	mListView.setOnTouchListener(new OnTouchListener() {

	    public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		mListView.setFocusable(true);
		mListView.setFocusableInTouchMode(true);
		mListView.requestFocus();
		YKUtil.hideKeyBoard(CommentActivity.this, mCommentEdit);
		return false;
	    }
	});
	mSed = (TextView) findViewById(R.id.sed);
	// 键盘改为发送键的事件
	mCommentEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
	mCommentEdit.setOnEditorActionListener(new OnEditorActionListener() {

	    @Override
	    public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {

		}
		return false;
	    }
	});
	mCommentEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {  

	    @Override  
	    public void onFocusChange(View v, boolean hasFocus) {  
		if(hasFocus){//获得焦点  
		    if(YKCurrentUserManager.getInstance().isLogin()){
			mCommentImage.setVisibility(View.GONE);
		    }else{
			Intent login = new Intent(CommentActivity.this,LoginActivity.class);
			startActivity(login);
			mCommentEdit.clearFocus();
		    }
		}else{//失去焦点  
		    mReplytoID = null;
		    mReplytouserid = null;
		    mReplytousername = null;
		    mCommentEdit.setHint("发表评论");
		    isComment = true;
		    mCommentImage.setVisibility(View.VISIBLE);
		}  
	    }             
	});
	mSed.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		if(!YKCurrentUserManager.getInstance().isLogin()){
		    Intent login = new Intent(CommentActivity.this,LoginActivity.class);
		    startActivity(login);
		    return;
		}
		final String tokin = YKCurrentUserManager.getInstance().getUser().getToken();
		final String context = mCommentEdit.getText().toString().trim();
		if(TextUtils.isEmpty(context)){
		    Toast.makeText(CommentActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
		    return;
		}
		loadingView();
		if(isComment){
		    //普通的回复列表
		    if(mClassType.equals("1")){
			sedComment(tokin,context);
		    }else{
			sedTopic(tokin,context);
		    }
		}else{
		    //回复再回复
		    if(mClassType.equals("1")){
			commentNew(Integer.valueOf(mClassType),tokin,Integer.valueOf(mReplytoID),"",context,mReplytouserid,mReplytousername);
		    }else{
			TipsNew(Integer.valueOf(mClassType),tokin,Integer.valueOf(mReplytoID),"",context,mReplytouserid,mReplytousername);
		    }
		}
	    }
	});

	activityRootView = findViewById(R.id.activity_comment_layout);
	//获取屏幕高度  
	screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();  
	//阀值设置为屏幕高度的1/3  
	keyHeight = screenHeight/3;
    }
    private void initData(boolean isLoading){
	String authtoken =null;
	if(YKCurrentUserManager.getInstance().isLogin()){
	    authtoken = YKCurrentUserManager.getInstance().getUser().getToken();
	}
	if(isLoading){
	    loadingView();
	}
	mListView.setPullLoadEnable(false);
	int commentID;
	int after_id = 0;
	if(!TextUtils.isEmpty(mCommentID)){
	    //1 == 心得   !1 == 资讯
	    commentID = Integer.parseInt(mCommentID);
	    if(mClassType.equals("1")){
		PostComment(after_id,commentID,authtoken);
	    }else{
		PostTopic(after_id,commentID,authtoken);
	    }
	}
    }

    private void initLoadMoreData(boolean isLoading){
	String authtoken =null;
	if(YKCurrentUserManager.getInstance().isLogin()){
	    authtoken = YKCurrentUserManager.getInstance().getUser().getToken();
	}
	if(isLoading){
	    loadingView();
	}
	mListView.setPullRefreshEnable(false);
	int commentID = Integer.parseInt(mCommentID);
	int size = mArrayList.size();
	if(size > 0 ){
	    int before_id = Integer.parseInt(mArrayList.get(size - 1).getID());
	    if(mClassType.equals("1")){
		commentListMore(before_id,commentID,authtoken);
	    }else if(mClassType.equals("2")){
		commentListTopicMore(before_id,commentID,authtoken);
	    }
	}
    }

    private class MyAdapter extends BaseAdapter
    {
	private ViewHolder viewHolder;
	private DisplayImageOptions options;

	public MyAdapter() {
	    options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
		    .bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true).considerExifParams(true)
		    .build();
	}

	@Override
	public int getCount() {
	    if (mArrayList != null) {
		return mArrayList.size();
	    }
	    return 0;
	}

	@Override
	public Object getItem(int position) {
	    if (mArrayList != null) {
		return mArrayList.get(position);
	    }
	    return null;
	}

	@Override
	public long getItemId(int position) {
	    return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

	    final YKComment  comment = mArrayList.get(position);

	    if (convertView == null) {

		viewHolder = new ViewHolder();
		convertView = LayoutInflater.from(CommentActivity.this).inflate(R.layout.activity_comment_item, null);

		viewHolder.imageView = (RoundImage) convertView.findViewById(R.id.comment_userimage);
		viewHolder.userName = (TextView) convertView.findViewById(R.id.comment_username);
		viewHolder.userSkin = (TextView) convertView.findViewById(R.id.comment_userskin);
		viewHolder.userAge = (TextView) convertView.findViewById(R.id.comment_userage);
		viewHolder.userDate = (TextView) convertView.findViewById(R.id.comment_userdate);
		viewHolder.userContext = (TextView) convertView.findViewById(R.id.comment_context);
		viewHolder.commentImageLayout = (LinearLayout) convertView.findViewById(R.id.comment_image_layout);
		viewHolder.imageOne = (ImageView) convertView.findViewById(R.id.comment_image_one);
		viewHolder.imageTwo = (ImageView) convertView.findViewById(R.id.comment_image_two);
		viewHolder.imageThree = (ImageView) convertView.findViewById(R.id.comment_image_three);
		viewHolder.ageNamemrsk = (RelativeLayout) convertView.findViewById(R.id.comment_age_name_mrsk);

		//添加回复再回复布局
		viewHolder.mCommentagincommentLayout = (LinearLayout) convertView.findViewById(R.id.commentagincomment_layout);

		//回复再回复
		viewHolder.commentnewLayout = (RelativeLayout) convertView.findViewById(R.id.commentnew_layout);
		viewHolder.commentnewNumber = (TextView) convertView.findViewById(R.id.commentnew_text_number);

		//更多
		viewHolder.commentMore = (TextView) convertView.findViewById(R.id.comment_more);

		//点赞
		viewHolder.thumLayout = (RelativeLayout) convertView.findViewById(R.id.thum_layout);
		viewHolder.thumNumber = (TextView) convertView.findViewById(R.id.thum_text_number);
		viewHolder.thumImage = (ImageView) convertView.findViewById(R.id.thum_image);

		convertView.setTag(viewHolder);

	    } else {
		viewHolder = (ViewHolder) convertView.getTag();
	    }
	    ImageLoader.getInstance().displayImage(comment.getmAuthor().getAvatar().getmURL(), viewHolder.imageView, options);

	    viewHolder.userName.setText(comment.getmAuthor().getName());
	    int skin = comment.getmAuthor().getComplexion();
	    if(skin == 0){
		viewHolder.userSkin.setText("");
	    }else if(skin == 1){
		viewHolder.userSkin.setText("");
	    }else if(skin == 2){
		viewHolder.userSkin.setText("");
	    }else if(skin == 3){
		viewHolder.userSkin.setText("");
	    }else if(skin == 4){
		viewHolder.userSkin.setText("");
	    }
	    viewHolder.userAge.setText(String.valueOf(comment.getmAuthor().getAge())+"岁");
	    viewHolder.userAge.setVisibility(View.GONE);
	    String time = TimeUtil.forTimeForYearMonthDayShortDayHour(comment.getmAddDate().getmMills());
	    viewHolder.userDate.setText(time);
	    String commentText = comment.getmContext();
	    if(TextUtils.isEmpty(commentText)){
		viewHolder.userContext.setVisibility(View.GONE);
	    }else{
		viewHolder.userContext.setVisibility(View.VISIBLE);
		viewHolder.userContext.setText(commentText);

	    }
	    //            TrackManager.getInstance().addTrack(
	    //                    TrackUrl.ITEM_EXPOSURE + trialProduct.getmId()
	    //                    + "&type=trial");
	    if(null == comment || null == comment.getmImage()){
		return convertView;
	    }
	    int  imageSize = comment.getmImage().size();
	    if(imageSize > 0){
		viewHolder.commentImageLayout.setVisibility(View.VISIBLE);
	    }else{
		viewHolder.commentImageLayout.setVisibility(View.GONE);
	    }
	    if(imageSize == 1){
		viewHolder.imageOne.setVisibility(View.VISIBLE);
		viewHolder.imageTwo.setVisibility(View.INVISIBLE);
		viewHolder.imageThree.setVisibility(View.INVISIBLE);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(0), viewHolder.imageOne, options);
	    }else if(imageSize == 2){
		viewHolder.imageOne.setVisibility(View.VISIBLE);
		viewHolder.imageTwo.setVisibility(View.VISIBLE);
		viewHolder.imageThree.setVisibility(View.INVISIBLE);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(0), viewHolder.imageOne, options);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(1), viewHolder.imageTwo, options);
	    }else if(imageSize == 3){
		viewHolder.imageOne.setVisibility(View.VISIBLE);
		viewHolder.imageTwo.setVisibility(View.VISIBLE);
		viewHolder.imageThree.setVisibility(View.VISIBLE);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(0), viewHolder.imageOne, options);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(1), viewHolder.imageTwo, options);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(2), viewHolder.imageThree, options);
	    }else if(imageSize >= 4){
		viewHolder.imageOne.setVisibility(View.VISIBLE);
		viewHolder.imageTwo.setVisibility(View.VISIBLE);
		viewHolder.imageThree.setVisibility(View.VISIBLE);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(0), viewHolder.imageOne, options);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(1), viewHolder.imageTwo, options);
		ImageLoader.getInstance().displayImage(comment.getmImage().get(2), viewHolder.imageThree, options);
	    }
	    viewHolder.commentnewNumber.setText(comment.getmReplynum());
	    viewHolder.thumNumber.setText(comment.getmLikenum());

	    if(comment.getmCommentlikeflag().equals("0")){
		viewHolder.thumImage.setBackgroundResource(R.drawable.thum_image_n);
	    }else{
		viewHolder.thumImage.setBackgroundResource(R.drawable.thum_image_y);
	    }
	    //没登陆
	    if(YKCurrentUserManager.getInstance().isLogin()){
		if(YKCurrentUserManager.getInstance().getUser().getId().equals(comment.getmAuthor().getID())){
		    viewHolder.commentMore.setBackgroundResource(R.drawable.comment_detllent);
		}else{
		    viewHolder.commentMore.setBackgroundResource(R.drawable.comment_report);
		}
	    }else{
		viewHolder.commentMore.setBackgroundResource(R.drawable.comment_report);
	    }
	    viewHolder.mCommentagincommentLayout.removeAllViews();
	    viewHolder.mCommentagincommentLayout.setVisibility(View.GONE);
	    if(comment.getmReplytoreplylist() != null && comment.getmReplytoreplylist().size() > 0){
		viewHolder.mCommentagincommentLayout.setVisibility(View.VISIBLE);
		for (int i = 0;i < comment.getmReplytoreplylist().size(); i++) {
		    View view = LayoutInflater.from(CommentActivity.this).inflate(R.layout.activity_reply_item, null);
		    final int pos = i;
		    //自己的回复
		    LinearLayout usernameLayout = (LinearLayout) view.findViewById(R.id.username_me_layout);
		    TextView username_me = (TextView) view.findViewById(R.id.username_me);
		    //自己对别人的回复
		    LinearLayout usernameyouLayout = (LinearLayout) view.findViewById(R.id.user_my_you_layout);
		    TextView reply_name = (TextView) view.findViewById(R.id.reply_name);
		    int nameColor = CommentActivity.this.getResources().getColor(R.color.text_my_experience_tab_default);
		    // 自己的回复跟他人的回复显示
		    if(i== 0){
			usernameLayout.setVisibility(View.VISIBLE);
			usernameyouLayout.setVisibility(View.GONE);
			if(comment.getmReplytoreplylist().get(i).getmReplytouserinfo() != null && 
				comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytouserid() != null &&
				comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytousername() != null){
			    String username_me_String = comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytousername();
			    String newName = "";
			    newName += "<font color='"+nameColor+"'>"+username_me_String+"</font>";// 回复人用户名
			    newName += "<font color='"+nameColor+"'>:   </font>";// 冒号
			    newName += comment.getmReplytoreplylist().get(i).getmContent();
			    username_me.setText(Html.fromHtml(newName));
			}else{
			    String username_me_String = comment.getmReplytoreplylist().get(i).getmUserinfo().getmUsername();
			    String newName = "";
			    newName += "<font color='"+nameColor+"'>"+username_me_String+"</font>";// 回复人用户名
			    newName += "<font color='"+nameColor+"'>:   </font>";// 冒号
			    newName += comment.getmReplytoreplylist().get(i).getmContent();
			    username_me.setText(Html.fromHtml(newName));
			}
		    }else{
			if(comment.getmReplytoreplylist().get(i).getmUserinfo() != null &&
				comment.getmReplytoreplylist().get(i).getmUserinfo().getmUserid() != null &&
				comment.getmReplytoreplylist().get(i).getmUserinfo().getmUsername() != null &&
				comment.getmReplytoreplylist().get(i).getmReplytouserinfo() != null && 
				comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytouserid() != null &&
				comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytousername() != null){
			    usernameyouLayout.setVisibility(View.VISIBLE);
			    usernameLayout.setVisibility(View.GONE);
			    String reply_name_String = comment.getmReplytoreplylist().get(i).getmUserinfo().getmUsername();
			    String name_you = comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytousername();
			    String content  =comment.getmReplytoreplylist().get(i).getmContent();
			    String newReplyName = "";
			    newReplyName += "<font color='"+nameColor+"'>"+reply_name_String+"</font>";// 回复人用户名  "";
			    newReplyName += "回复<font color='"+nameColor+"'>"+name_you+"</font>";// 被回复人用户名
			    newReplyName += "<font color='"+nameColor+"'>:   </font>";// 冒号
			    newReplyName += content;
			    reply_name.setText(Html.fromHtml(newReplyName));
			}else{
			    usernameLayout.setVisibility(View.VISIBLE);
			    usernameyouLayout.setVisibility(View.GONE);

			    if(comment.getmReplytoreplylist().get(i).getmReplytouserinfo() != null && 
				    comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytouserid() != null &&
				    comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytousername() != null){
				String username_me_String = comment.getmReplytoreplylist().get(i).getmReplytouserinfo().getmReplytousername();
				String newName = "";
				newName += "<font color='"+nameColor+"'>"+username_me_String+"</font>";// 回复人用户名
				newName += "<font color='"+nameColor+"'>:   </font>";// 冒号
				newName += comment.getmReplytoreplylist().get(i).getmContent();
				username_me.setText(Html.fromHtml(newName));
			    }else{
				String username_me_String = comment.getmReplytoreplylist().get(i).getmUserinfo().getmUsername();
				String newName = "";
				newName += "<font color='"+nameColor+"'>"+username_me_String+"</font>";// 回复人用户名
				newName += "<font color='"+nameColor+"'>:   </font>";// 冒号
				newName += comment.getmReplytoreplylist().get(i).getmContent();
				username_me.setText(Html.fromHtml(newName));
			    }
			}
		    }

		    //自己回复的点击
		    usernameLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			    if(!AppUtil.isNetworkAvailable(mContext)){
				Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
				return;
			    }
			    if(comment.getmReplytoreplylist().get(pos).getmUserinfo().getmUsername().equals(YKCurrentUserManager.getInstance().getUser().getName())){
				mCommentEdit.clearFocus();
				YKUtil.hideKeyBoard(mContext, mCommentEdit);
				new AlertDialog.Builder(CommentActivity.this).setTitle("确认删除") 
				.setIcon(android.R.drawable.ic_dialog_info) 
				.setPositiveButton("确认", new DialogInterface.OnClickListener() { 

				    @Override 
				    public void onClick(DialogInterface dialog, int which) { 
					deleteComment(comment,pos);
				    } 
				}) 
				.setNegativeButton("取消", new DialogInterface.OnClickListener() { 

				    @Override 
				    public void onClick(DialogInterface dialog, int which) { 
					// 点击“返回”后的操作,这里不设置没有任何操作 
				    } 
				}).show(); 
			    }else{
				
				mReplytouserid = comment.getmReplytoreplylist().get(pos).getmUserinfo().getmUserid();
				mReplytousername = comment.getmReplytoreplylist().get(pos).getmUserinfo().getmUsername();

				mCommentEdit.setFocusable(true);
				mCommentEdit.setFocusableInTouchMode(true);
				mCommentEdit.requestFocus();
				mCommentEdit.setHint("回复：" + mReplytousername);
				YKUtil.showKeyBoard(CommentActivity.this, mCommentEdit);
				isComment = false;
				mCommentLoad = comment;
				mReplytoID = comment.getID();
			    }

			}
		    });

		    reply_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    if(!AppUtil.isNetworkAvailable(mContext)){
				Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
				return;
			    }
			    if(YKCurrentUserManager.getInstance().getUser().getName().equals(comment.getmReplytoreplylist().get(pos).getmUserinfo().getmUsername())){
				mCommentEdit.clearFocus();
				YKUtil.hideKeyBoard(mContext, mCommentEdit);
				new AlertDialog.Builder(CommentActivity.this).setTitle("确认删除") 
				.setIcon(android.R.drawable.ic_dialog_info) 
				.setPositiveButton("确认", new DialogInterface.OnClickListener() { 

				    @Override 
				    public void onClick(DialogInterface dialog, int which) { 
					deleteComment(comment,pos);
				    } 
				}) 
				.setNegativeButton("取消", new DialogInterface.OnClickListener() { 

				    @Override 
				    public void onClick(DialogInterface dialog, int which) { 
					// 点击“返回”后的操作,这里不设置没有任何操作 
				    } 
				}).show(); 
			    }else{

				mReplytouserid = comment.getmReplytoreplylist().get(pos).getmUserinfo().getmUserid();
				mReplytousername = comment.getmReplytoreplylist().get(pos).getmUserinfo().getmUsername();

				mCommentEdit.setFocusable(true);
				mCommentEdit.setFocusableInTouchMode(true);
				mCommentEdit.requestFocus();
				mCommentEdit.setHint("回复：" + mReplytousername);
				YKUtil.showKeyBoard(CommentActivity.this, mCommentEdit);
				isComment = false;
				mCommentLoad = comment;
				mReplytoID = comment.getID();
			    }
			}
		    });
		    //		    name_me.setOnClickListener(new OnClickListener() {
		    //			@Override
		    //			public void onClick(View v) {
		    //			    if(YKCurrentUserManager.getInstance().getUser().getName().equals(comment.getmReplytoreplylist().get(pos).getmReplytouserinfo().getmReplytousername())){
		    //				new AlertDialog.Builder(CommentActivity.this).setTitle("确认删除") 
		    //				.setIcon(android.R.drawable.ic_dialog_info) 
		    //				.setPositiveButton("确认", new DialogInterface.OnClickListener() { 
		    //
		    //				    @Override 
		    //				    public void onClick(DialogInterface dialog, int which) { 
		    //					YKDeleteInformationAndTipsManager.getInstance().postDeleteYKInformationAndTips(new deleteYKInformationAndTipsCallback() {
		    //
		    //					    @Override
		    //					    public void callback(YKResult result) {
		    //						// TODO Auto-generated method stub
		    //						String message = (String) result.getMessage();
		    //						if(result.isSucceeded()){
		    //						    onRefresh();
		    //						    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		    //						}else{
		    //						    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		    //						}
		    //					    }
		    //					}, Integer.valueOf(mClassType), YKCurrentUserManager.getInstance().getUser().getToken(), Integer.valueOf(comment.getID()), comment.getmReplytoreplylist().get(pos).getID());
		    //				    } 
		    //				}) 
		    //				.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
		    //
		    //				    @Override 
		    //				    public void onClick(DialogInterface dialog, int which) { 
		    //					// 点击“返回”后的操作,这里不设置没有任何操作 
		    //				    } 
		    //				}).show(); 
		    //			    }else{
		    //
		    //				mReplytouserid = comment.getmReplytoreplylist().get(pos).getmReplytouserinfo().getmReplytouserid();
		    //				mReplytousername = comment.getmReplytoreplylist().get(pos).getmReplytouserinfo().getmReplytousername();
		    //
		    //				mCommentEdit.setFocusable(true);
		    //				mCommentEdit.setFocusableInTouchMode(true);
		    //				mCommentEdit.requestFocus();
		    //				mCommentEdit.setHint("回复：" + mReplytousername);
		    //				YKUtil.showKeyBoard(CommentActivity.this, mCommentEdit);
		    //				isComment = false;
		    //				mReplytoID = comment.getID();
		    //			    }
		    //			}
		    //		    });

		    viewHolder.mCommentagincommentLayout.addView(view);
		}
	    }else{
		viewHolder.mCommentagincommentLayout.setVisibility(View.GONE);
	    }
	    viewHolder.imageOne.setOnClickListener(new OnClickListener()
	    {
		@Override
		public void onClick(View v)
		{
		    String path = comment.getmImage().get(0);
		    if(!TextUtils.isEmpty(path)){
			Intent showImage = new Intent(CommentActivity.this,ShowBigImageActivity.class);
			showImage.putExtra("imagePath", path);
			startActivity(showImage);
		    }
		}
	    });
	    //点击内容
	    viewHolder.userContext.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    if(!AppUtil.isNetworkAvailable(mContext)){
			Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
			return;
		    }
		    mCommentEdit.setFocusable(true);
		    mCommentEdit.setFocusableInTouchMode(true);
		    mCommentEdit.requestFocus();
		    mCommentEdit.setHint("回复" + comment.getmAuthor().getName() + ":");
		    YKUtil.showKeyBoard(CommentActivity.this, mCommentEdit);
		    isComment = false;
		    mCommentLoad = comment;
		    mReplytoID = comment.getID();
		}
	    });
	    viewHolder.imageTwo.setOnClickListener(new OnClickListener()
	    {
		@Override
		public void onClick(View v)
		{
		    String path = comment.getmImage().get(1);
		    if(!TextUtils.isEmpty(path)){
			Intent showImage = new Intent(CommentActivity.this,ShowBigImageActivity.class);
			showImage.putExtra("imagePath", path);
			startActivity(showImage);
		    }
		}
	    });
	    viewHolder.imageThree.setOnClickListener(new OnClickListener()
	    {
		@Override
		public void onClick(View v)
		{
		    String path = comment.getmImage().get(2);
		    if(!TextUtils.isEmpty(path)){
			Intent showImage = new Intent(CommentActivity.this,ShowBigImageActivity.class);
			showImage.putExtra("imagePath", path);
			startActivity(showImage);
		    }
		}
	    });
	    //点击信息
	    viewHolder.commentnewLayout.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    if(!AppUtil.isNetworkAvailable(mContext)){
			Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
			return;
		    }
		    mCommentEdit.setFocusable(true);
		    mCommentEdit.setFocusableInTouchMode(true);
		    mCommentEdit.requestFocus();
		    mCommentEdit.setHint("回复" + comment.getmAuthor().getName() + ":");
		    YKUtil.showKeyBoard(CommentActivity.this, mCommentEdit);
		    isComment = false;
		    mCommentLoad = comment;
		    mReplytoID = comment.getID();
		}
	    });
	    //点赞
	    viewHolder.thumLayout.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    if(!AppUtil.isNetworkAvailable(mContext)){
			Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
			return;
		    }
		    String authtoken = "";
		    if(YKCurrentUserManager.getInstance().isLogin()){
			authtoken = YKCurrentUserManager.getInstance().getUser().getToken();
		    }else{
			viewHolder.thumImage.setBackgroundResource(R.drawable.thum_image_n);
			Intent login = new Intent(CommentActivity.this,LoginActivity.class);
			startActivity(login);
			return;
		    }
		    YKThumbupManager.getInstance().postYKThumbup(new ThumbupCallback() {

			@Override
			public void callback(YKResult result) {
			    if(result.isSucceeded()){
				if(comment.getmCommentlikeflag().equals("0")){
				    comment.setmCommentlikeflag("1");
				    int number = Integer.valueOf(comment.getmLikenum());
				    int nenumberw = number + 1;
				    comment.setmLikenum(String.valueOf(nenumberw));
				    viewHolder.thumNumber.setText(comment.getmLikenum());
				    viewHolder.thumImage.setBackgroundResource(R.drawable.thum_image_y);

				}else{
				    comment.setmCommentlikeflag("0");
				    int number = Integer.valueOf(comment.getmLikenum());
				    int nenumberw = number - 1;
				    comment.setmLikenum(String.valueOf(nenumberw));
				    viewHolder.thumNumber.setText(comment.getmLikenum());
				    viewHolder.thumImage.setBackgroundResource(R.drawable.thum_image_n);
				}
				mAdapter.notifyDataSetChanged();
			    }
			}
		    }, Integer.valueOf(mClassType), authtoken, Integer.valueOf(comment.getID()));
		}
	    });
	    viewHolder.commentMore.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub
		    if(!AppUtil.isNetworkAvailable(mContext)){
			Toast.makeText(mContext, string.intent_no, Toast.LENGTH_SHORT).show();
			return;
		    }
		    if(!YKCurrentUserManager.getInstance().isLogin()){
			new AlertDialog.Builder(CommentActivity.this).setTitle("确认举报") 
			.setIcon(android.R.drawable.ic_dialog_info) 
			.setPositiveButton("举报", new DialogInterface.OnClickListener() { 

			    @Override 
			    public void onClick(DialogInterface dialog, int which) { 
				Toast.makeText(CommentActivity.this, "举报成功，我们会及时处理！！！", Toast.LENGTH_SHORT).show();
			    } 
			}) 
			.setNegativeButton("取消", new DialogInterface.OnClickListener() { 

			    @Override 
			    public void onClick(DialogInterface dialog, int which) { 
				// 点击“返回”后的操作,这里不设置没有任何操作 
			    } 
			}).show(); 
		    }else{
		    if(YKCurrentUserManager.getInstance().getUser().getId().equals(comment.getmAuthor().getID())){
			mCommentEdit.clearFocus();
			YKUtil.hideKeyBoard(mContext, mCommentEdit);
			new AlertDialog.Builder(CommentActivity.this).setTitle("确认删除") 
			.setIcon(android.R.drawable.ic_dialog_info) 
			.setPositiveButton("确认", new DialogInterface.OnClickListener() { 

			    @Override 
			    public void onClick(DialogInterface dialog, int which) { 
				deleteAllComment(comment);
			    } 
			}) 
			.setNegativeButton("取消", new DialogInterface.OnClickListener() { 

			    @Override 
			    public void onClick(DialogInterface dialog, int which) { 
				// 点击“返回”后的操作,这里不设置没有任何操作 
			    } 
			}).show(); 

		    }else{
			mCommentEdit.clearFocus();
			YKUtil.hideKeyBoard(mContext, mCommentEdit);
			new AlertDialog.Builder(CommentActivity.this).setTitle("确认举报") 
			.setIcon(android.R.drawable.ic_dialog_info) 
			.setPositiveButton("举报", new DialogInterface.OnClickListener() { 

			    @Override 
			    public void onClick(DialogInterface dialog, int which) { 
				Toast.makeText(CommentActivity.this, "举报成功，我们会及时处理！！！", Toast.LENGTH_SHORT).show();
			    } 
			}) 
			.setNegativeButton("取消", new DialogInterface.OnClickListener() { 

			    @Override 
			    public void onClick(DialogInterface dialog, int which) { 
				// 点击“返回”后的操作,这里不设置没有任何操作 
			    } 
			}).show(); 
		    }
		    }
		}
	    });
	    //	    viewHolder.commentLayoutMore.setOnClickListener(new OnClickListener() {
	    //
	    //		@Override
	    //		public void onClick(View v) {
	    //		    if(isAllorThree){
	    //			isAllorThree = false;
	    //		    }else{
	    //			isAllorThree = true;
	    //		    }  
	    //		    mAdapter.notifyDataSetChanged();
	    //		}
	    //	    });
	    return convertView;
	}
    }

    /**
     * 存放列表项控件句柄
     */
    private class ViewHolder {
	private RoundImage imageView;
	private TextView commentMore,thumNumber,commentnewNumber,commentLayoutMore;
	private ImageView thumImage,commentnewImage;
	private RelativeLayout ageNamemrsk,commentnewLayout,thumLayout; 
	private LinearLayout commentImageLayout,mCommentagincommentLayout;
	private ImageView imageOne,imageTwo,imageThree;
	private TextView userName, userSkin,userAge,userDate,userContext;
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.activity_comment_back:
	    finish();
	    break;
	case R.id.comment_bottom_image:
	    Intent commentImage = new Intent(CommentActivity.this,AeniorReplayActivity2.class);
	    if(mClassType.equals("1")){
		commentImage.putExtra("commmentID", mCommentID);
	    }else{
		commentImage.putExtra("mCommentInfourID", mCommentID);
	    }
	    startActivityForResult(commentImage,22);
	    break;

	default:
	    break;
	}
    }
    private void loadingView(){
	try {
	    mCustomButterfly = CustomButterfly.show(mContext);
	} catch (Exception e) {
	    mCustomButterfly = null;
	}
    }
    private void PostComment(int after_id,int commentID,String authtoken){
	YKCommentManager.getInstance().postComment(0,after_id,commentID,authtoken,new CommentCallback()
	{
	    @Override
	    public void callback(YKResult result, ArrayList<YKComment> comment)
	    {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if(result.isSucceeded()){
		    if(comment.size() > 0){
			mArrayList.clear();
			mArrayList.addAll(comment);
			mAdapter.notifyDataSetChanged();
			mListView.setPullLoadEnable(true);
		    }
		}else{
		    String message = (String) result.getMessage();
		    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }
    private void PostTopic(int after_id,int commentID,String authtoken){
	YKCommentManager.getInstance().postTopicReply(0,after_id,commentID,authtoken,new CommentCallback()
	{
	    @Override
	    public void callback(YKResult result, ArrayList<YKComment> comment)
	    {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if(result.isSucceeded()){
		    if(comment.size() > 0){
			mArrayList.clear();
			mArrayList.addAll(comment);
			mAdapter.notifyDataSetChanged();
			mListView.setPullLoadEnable(true);
		    }
		}else{
		    String message = (String) result.getMessage();
		    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    //发布心得listmore
    private void commentListMore(int before_id,int commentID,String authtoken){
	YKCommentManager.getInstance().postComment(1,before_id,commentID,authtoken,new CommentCallback()
	{
	    @Override
	    public void callback(YKResult result, ArrayList<YKComment> comment)
	    {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		AppUtil.dismissDialogSafe(mCustomButterfly);
		mListView.setPullRefreshEnable(true);
		if(result.isSucceeded()){
		    if(comment.size() > 0){
			mArrayList.addAll(comment);
			mAdapter.notifyDataSetChanged();
		    }else{
			Toast.makeText(CommentActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
		    }
		}else{
		    String message = (String) result.getMessage();
		    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }
    //发布资讯listmore
    private void commentListTopicMore(int before_id,int commentID,String authtoken){
	YKCommentManager.getInstance().postTopicReply(1,before_id,commentID,authtoken,new CommentCallback()
	{
	    @Override
	    public void callback(YKResult result, ArrayList<YKComment> comment)
	    {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		AppUtil.dismissDialogSafe(mCustomButterfly);
		mListView.setPullRefreshEnable(true);
		if(result.isSucceeded()){
		    if(comment.size() > 0){
			mArrayList.addAll(comment);
			mAdapter.notifyDataSetChanged();
		    }else{
			Toast.makeText(CommentActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
		    }
		}else{
		    String message = (String) result.getMessage();
		    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }
    //发布心得
    private void sedComment(String tokin,String context){
	YKCommentreplyManager.getInstance().postCommentReply(tokin, mCommentID, context,null, new IDReplyCallback()
	{
	    @Override
	    public void callback(YKResult result,String replyID)
	    {
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if(result.isSucceeded()){
		    mCommentEdit.setText("");
		    mCommentEdit.clearFocus();
		    isComment = true;
		    YKUtil.hideKeyBoard(CommentActivity.this, mCommentEdit);
		    Toast.makeText(CommentActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
		    onRefresh();
		}else{
		    String  message = (String) result.getMessage();
		    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }
    //发布资讯
    private void  sedTopic(String tokin,String context){
	YKCommentreplyManager.getInstance().postTopicReply(tokin, mCommentID, context,null, new IDReplyCallback()
	{
	    @Override
	    public void callback(YKResult result,String replyID)
	    {
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if(result.isSucceeded()){
		    Intent commentOk = new Intent();
		    mCommentEdit.setText("");
		    mCommentEdit.clearFocus();
		    YKUtil.hideKeyBoard(CommentActivity.this, mCommentEdit);
		    Toast.makeText(CommentActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
		    isComment = true;
		    onRefresh();
		    commentOk.putExtra("ok","0");
		    setResult(55,commentOk);
		}else{
		    String  message = (String) result.getMessage();
		    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    //回复再回复心得
    //replytoID 此条心得的ID,replytouserid 被回复者的userid,如果回复一楼可为空,replytousername,被回复者的username,如果回复一楼可为空
    public void commentNew(int iSCommentOrTips,String authtoken,final int replytoID,String replyID,final String content,final String replytouserid,final String replytousername){
	YKaddInformationAndTipsManager.getInstance().postYKInformationAndTips(new AddYKInformationAndTipsCallback() {

	    @Override
	    public void callback(YKResult result,YKComment comment) {
		// TODO Auto-generated method stub
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if(result.isSucceeded()){
		    addComment(mCommentLoad,comment);
		    mCommentEdit.setText("");
		    mCommentEdit.clearFocus();
		    isComment = true;
		    YKUtil.hideKeyBoard(CommentActivity.this, mCommentEdit);
		    Toast.makeText(CommentActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
		    //TODO 本地刷新在列表里面找到你回复的那条评论在他的回复列表里面添加一条回复在回复数据
		    mAdapter.notifyDataSetChanged();
		    //mAdapter.update(mArrayList);
		}else{

		    String  message = (String) result.getMessage();
		    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		}

	    }
	}, iSCommentOrTips, authtoken, replytoID, "",content, replytouserid, replytousername);
    }

    //回复再回复资讯
    //replytoID 此条资讯的ID,replytouserid 被回复者的userid,如果回复一楼可为空,replytousername,被回复者的username,如果回复一楼可为空
    private void TipsNew(int iSCommentOrTips,String authtoken,int replytoID,String replyID,final String content,final String replytouserid,final String replytousername){
	YKaddInformationAndTipsManager.getInstance().postYKInformationAndTips(new AddYKInformationAndTipsCallback() {

	    @Override
	    public void callback(YKResult result,YKComment comment) {
		// TODO Auto-generated method stub
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if(result.isSucceeded()){

		    addComment(mCommentLoad,comment);
		    mCommentEdit.setText("");
		    mCommentEdit.clearFocus();
		    isComment = true;
		    YKUtil.hideKeyBoard(CommentActivity.this, mCommentEdit);
		    Toast.makeText(CommentActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
		    //		    //TODO 本地刷新在列表里面添加一条数据
		    mAdapter.notifyDataSetChanged();

		}else{

		    String  message = (String) result.getMessage();
		    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		}

	    }
	}, iSCommentOrTips, authtoken, replytoID, "",content, replytouserid, replytousername);
    }

    private void removeComment(String replyID,YKComment comment){
	int size = mArrayList.size();
	for(int i=0;i < size; i++){
	    YKComment com = mArrayList.get(i);
	    if(comment.getID().equals(com.getID())){
		int number = Integer.valueOf(comment.getmReplynum());
		int nenumberw = number - 1;
		com.setmReplynum(String.valueOf(nenumberw));
		ArrayList<YKReplytoreplylist> YKReplytoreplylist = com.getmReplytoreplylist();
		for (int j = 0; j < YKReplytoreplylist.size(); j++) {
		    YKReplytoreplylist reply = YKReplytoreplylist.get(j);
		    if(replyID.equals(reply.getID())){
			YKReplytoreplylist.remove(j);
			break;
		    }
		}
	    }
	}
    }

    private void removeCommentBig(YKComment comment){
	int size = mArrayList.size();
	for(int i=0;i<size;i++){
	    if(comment.getID().equals(mArrayList.get(i).getID())){
		mArrayList.remove(i);
		break;
	    }
	}
    }

    private void addComment(YKComment loadComment,YKComment IntenComment){
	if(isComment){
	    //一级回复
	}else{
	    //回复再回复
	    if(null != loadComment.getmReplytoreplylist()){
		for (int i = 0; i < mArrayList.size(); i++) {
		    YKComment com = mArrayList.get(i);
		    if(loadComment.getID().equals(com.getID())){
			int number = Integer.valueOf(loadComment.getmReplynum());
			int nenumberw = number + 1;
			com.setmReplynum(String.valueOf(nenumberw));
			YKReplytoreplylist data = IntenComment.getmReplytoreplylist().get(0);
			com.getmReplytoreplylist().add(data);
			return;
		    }
		    //	    	  }
		//	    }else{
		    //	    	if(IntenComment.getmReplytoreplylist()!=null){
		    //	    		int count = IntenComment.getmReplytoreplylist().size();
		    //	    		if(count>0){
		    //	    			 for (int i = 0; i < mArrayList.size(); i++) {
		    //	   	    		  YKComment com = mArrayList.get(i);
		    //	   	    		  if(loadComment.getID().equals(com.getID())){
		    //	   	    			  com.setmReplytoreplylist(new ArrayList<YKReplytoreplylist>()) ;
		    //	   	    			  com.getmReplytoreplylist().addAll(IntenComment.getmReplytoreplylist());
		    //	   	    			  return;
		    //	   	    		  }
		    //	   	    	  }
		    //	    		}
		    //	    	}else{
		}
	    }
	}
    }
//    private void addComment(YKComment loadComment,YKComment IntenComment){
//    	int size = loadComment.getmReplytoreplylist().size();
//    	if(isComment){
//    		//一级回复
//    	}else{
//    		//回复再回复
//    		if(null != loadComment.getmReplytoreplylist() && size > 0 ){
//    			
//    			if(IntenComment.getmReplytoreplylist().get(0).getmReplytouserinfo() == null){
//    				for (int i = 0; i < mArrayList.size(); i++) {
//    					if(loadComment.getID().equals(mArrayList.get(i).getID())){
//    						YKComment comment = mArrayList.get(i);
//    						ArrayList<YKReplytoreplylist> replyList  = mArrayList.get(i).getmReplytoreplylist();
//    						YKReplytoreplylist replyto = new YKReplytoreplylist();
//    						YKUserinfo usertoinfo = new YKUserinfo();
//    						usertoinfo.setmUserid(IntenComment.getmReplytoreplylist().get(0).getmUserinfo().getmUserid());
//    						usertoinfo.setmUsername(IntenComment.getmReplytoreplylist().get(0).getmUserinfo().getmUsername());
//    						replyto.setmUserinfo(usertoinfo);
//    						replyto.setmContent(IntenComment.getmContext());
//    						replyList.add(replyto);
//    						comment.setmReplytoreplylist(replyList);
//    						mArrayList.add(comment);
//    						break;
//    					}
//    				}
//    			}else{
//    				for (int i = 0; i < mArrayList.size(); i++) {
//    					if(loadComment.getID().equals(mArrayList.get(i).getID())){
//    						for (int j = 0; j < mArrayList.size(); j++) {
//    							if(loadComment.getmReplytoreplylist().get(j).getID().equals(mArrayList.get(j).getmReplytoreplylist().get(j).getID())){
//    								YKComment comment = mArrayList.get(j);
//    								ArrayList<YKReplytoreplylist> replyList  = mArrayList.get(j).getmReplytoreplylist();
//    								YKReplytoreplylist replyto = new YKReplytoreplylist();
//    								
//    								YKUserinfo usertoinfo = new YKUserinfo();
//    								usertoinfo.setmUserid(IntenComment.getmReplytoreplylist().get(0).getmUserinfo().getmUserid());
//    								usertoinfo.setmUsername(IntenComment.getmReplytoreplylist().get(0).getmUserinfo().getmUsername());
//    								replyto.setmUserinfo(usertoinfo);
//    								
//    								YKReplytouserinfo replyinfo = new  YKReplytouserinfo();
//    								replyinfo.setmReplytouserid(IntenComment.getmReplytoreplylist().get(0).getmReplytouserinfo().getmReplytouserid());
//    								replyinfo.setmReplytousername(IntenComment.getmReplytoreplylist().get(0).getmReplytouserinfo().getmReplytousername());
//    								replyto.setmReplytouserinfo(replyinfo);
//    								
//    								replyto.setmContent(IntenComment.getmContext());
//    								
//    								replyList.add(replyto);
//    								comment.setmReplytoreplylist(replyList);
//    								mArrayList.add(comment);
//    								break;
//    							}
//    						}
//    					}
//    				}
//    			}
//    		}else{
//    			if(IntenComment.getmReplytoreplylist().get(0).getmReplytouserinfo() == null){
//    				for (int i = 0; i < mArrayList.size(); i++) {
//    					if(loadComment.getID().equals(mArrayList.get(i).getID())){
//    						YKComment comment = mArrayList.get(i);
//    						ArrayList<YKReplytoreplylist> replyList  = new ArrayList<YKReplytoreplylist>();
//    						YKReplytoreplylist replyto = new YKReplytoreplylist();
//    						YKUserinfo usertoinfo = new YKUserinfo();
//    						usertoinfo.setmUserid(IntenComment.getmReplytoreplylist().get(0).getmUserinfo().getmUserid());
//    						usertoinfo.setmUsername(IntenComment.getmReplytoreplylist().get(0).getmUserinfo().getmUsername());
//    						replyto.setmUserinfo(usertoinfo);
//    						replyto.setmContent(IntenComment.getmContext());
//    						replyList.add(replyto);
//    						comment.setmReplytoreplylist(replyList);
//    						mArrayList.add(comment);
//    						break;
//    					}
//    				}
//    			}else{
//    				for (int i = 0; i < mArrayList.size(); i++) {
//    					if(loadComment.getID().equals(mArrayList.get(i).getID())){
//    						for (int j = 0; j < mArrayList.size(); j++) {
//    							if(loadComment.getmReplytoreplylist().get(j).getID().equals(mArrayList.get(j).getmReplytoreplylist().get(j).getID())){
//    								YKComment comment = mArrayList.get(j);
//    								ArrayList<YKReplytoreplylist> replyList  = new ArrayList<YKReplytoreplylist>();
//    								YKReplytoreplylist replyto = new YKReplytoreplylist();
//    								
//    								YKUserinfo usertoinfo = new YKUserinfo();
//    								usertoinfo.setmUserid(IntenComment.getmReplytoreplylist().get(0).getmUserinfo().getmUserid());
//    								usertoinfo.setmUsername(IntenComment.getmReplytoreplylist().get(0).getmUserinfo().getmUsername());
//    								replyto.setmUserinfo(usertoinfo);
//    								
//    								YKReplytouserinfo replyinfo = new  YKReplytouserinfo();
//    								replyinfo.setmReplytouserid(IntenComment.getmReplytoreplylist().get(0).getmReplytouserinfo().getmReplytouserid());
//    								replyinfo.setmReplytousername(IntenComment.getmReplytoreplylist().get(0).getmReplytouserinfo().getmReplytousername());
//    								replyto.setmReplytouserinfo(replyinfo);
//    								
//    								replyto.setmContent(IntenComment.getmContext());
//    								
//    								replyList.add(replyto);
//    								comment.setmReplytoreplylist(replyList);
//    								mArrayList.add(comment);
//    								break;
//    							}
//    						}
//    					}
//    				}
//    			}
//    		}
//    		
//    	}
//    }
    public void deleteAllComment(final YKComment comment){
	YKDeleteInformationAndTipsAllManager.getInstance().postDeleteAllYKInformationAndTips(new deleteAllYKInformationAndTipsCallback() {

	    @Override
	    public void callback(YKResult result) {
		// TODO Auto-generated method stub
		if(result.isSucceeded()){
		    //TODO 本地刷新在列表里面找到这条数据移除
		    removeCommentBig(comment);
		    mAdapter.notifyDataSetChanged();
		    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
		}else{
		    Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
		}

	    }
	}, Integer.valueOf(mClassType), YKCurrentUserManager.getInstance().getUser().getToken(), Integer.valueOf(comment.getID()));
    }

    private void  deleteComment(final YKComment comment,final int position){
	YKDeleteInformationAndTipsManager.getInstance().postDeleteYKInformationAndTips(new deleteYKInformationAndTipsCallback() {

	    @Override
	    public void callback(YKResult result) {
		// TODO Auto-generated method stub
		String message = (String) result.getMessage();
		if(result.isSucceeded()){
		    removeComment(comment.getmReplytoreplylist().get(position).getID(),comment);
		    mAdapter.notifyDataSetChanged();
		    Toast.makeText(CommentActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
		}else{
		    Toast.makeText(CommentActivity.this, message, Toast.LENGTH_SHORT).show();
		}
	    }
	}, Integer.valueOf(mClassType), YKCurrentUserManager.getInstance().getUser().getToken(), Integer.valueOf(comment.getID()), comment.getmReplytoreplylist().get(position).getID());
    }
    @Override
    public void onRefresh()
    {
	initData(false);
    }

    @Override
    public void onLoadMore()
    {
	if(!NetWorkUtil.isNetworkAvailable(CommentActivity.this)){
	    Toast.makeText(CommentActivity.this, R.string.intent_no, Toast.LENGTH_SHORT).show();
	    mListView.stopLoadMore();
	    return;
	}
	initLoadMoreData(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if(resultCode == 77){
	    YKActivityManager.getInstance().registerRootActivity(this);
	    initData(true);
	}else if(resultCode == 88){
	    YKActivityManager.getInstance().registerRootActivity(this);
	    initData(true);
	}
	if(null != data){
	    if(requestCode == 22){
		YKActivityManager.getInstance().registerRootActivity(this);
		onRefresh();

	    }
	}

	super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
	super.onResume();
	//添加layout大小发生改变监听器  
	activityRootView.addOnLayoutChangeListener(this); 
	MobclickAgent.onPageStart("CommentActivity"); // 统计页面
	MobclickAgent.onResume(this); // 统计时长
    }

    public void onPause() {
	super.onPause();
	MobclickAgent.onPageEnd("CommentActivity"); // 保证 onPageEnd
	// 在onPause
	// 之前调用,因为 onPause
	// 中会保存信息
	MobclickAgent.onPause(this);
    }
    @Override
    protected void onDestroy() {
	super.onDestroy();
	YKActivityManager.getInstance().removeActivity(this);
    }
    
    private Handler mHandler = new Handler() { };
    @Override
    public void onLayoutChange(View v, int left, int top, int right,
	    int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
	// TODO Auto-generated method stub
	if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){  

            //Toast.makeText(mContext, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();  
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){  
        	mHandler.postDelayed(new Runnable() {  
  	           public void run() {  
  	        	  mCommentEdit.clearFocus();
  			    mCommentEdit.setHint("发表评论");
  			    mCommentEdit.setText("");
  			    isComment = true;
  			    YKUtil.hideKeyBoard(mContext, mCommentEdit);
  			    //Toast.makeText(mContext, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();  
  	          }  
  	     }, 100); 
        }  
    }
}
