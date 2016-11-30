package com.yoka.mrskin.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.adapter.AeniorReplayAdapter2;
import com.yoka.mrskin.addimage.BitmapUtil;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.YKCommentreplyManager;
import com.yoka.mrskin.model.managers.YKCommentreplyManager.ReplyCallback;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKUploadImageManager;
import com.yoka.mrskin.model.managers.YKUploadImageManager.UploadImageCallback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.BitmapUtilImage;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.MyListView;
import com.yoka.mrskin.util.NetWorkUtil;
import com.yoka.mrskin.util.YKUtil;
/**
 * 高级回复（多图压缩上传）
 * @author zlz
 * @Data 2016年7月27日
 */
public class AeniorReplayActivity2 extends BaseActivity implements OnClickListener{
    private LinearLayout mTakingPictures,mPhotoAlbum,mBack;
    public static TextView mCommit;
    private EditText mAeniorEdit;
    private MyListView mList;
    private AeniorReplayAdapter2 mAdapter;
    private Context mContext;
    private ArrayList<String> mImageList = new ArrayList<String>();//本地图片路径集合
    private ArrayList<Bitmap> mapList = new ArrayList<Bitmap>();//本地图片路径集合
    private ArrayList<String> mImageUploadUrl = new ArrayList<String>();//上传成功后返回路径集合
    private ArrayList<String> mtempList = new ArrayList<String>();//压缩后图片路径集合

    private CustomButterfly mCustomButterfly;
    private File mOutputFile;
    private Uri uritempFile;

    private String mGetEditText = "",mCommentID,mmCommentIDInfo;
    private int type = -1;

    private Bitmap tempBitmap;
    private int size = 0;
    private int times = 0;
    private Handler mHandler = new Handler(){

	@Override
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case 200:
		//图片上传完成开始 提交回复
		commitReplay();
		break;
	    case 201:
		uploadImage();
		break;
	    case 203:
		mCommit.setEnabled(true);
		AppUtil.dismissDialogSafe(mCustomButterfly);
		break;
	    default:
		break;
	    }
	}

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aenior_replay_activity);
	mContext = AeniorReplayActivity2.this;
	YKActivityManager.getInstance().addActivity(this);
	//心得高级回复
	if(!TextUtils.isEmpty(getIntent().getStringExtra("commmentID"))){
	    type = 1;
	    mCommentID  = getIntent().getStringExtra("commmentID");
	}
	//资讯高级回复
	if(!TextUtils.isEmpty(getIntent().getStringExtra("mCommentInfourID"))){
	    type = 2;
	    mmCommentIDInfo = getIntent().getStringExtra("mCommentInfourID");
	}

	initView();
    }


    /**
     * 实例化组件 & 设置监听
     */
    private void initView() {

	mList = (MyListView) findViewById(R.id.aenior_list);
	mCommit = (TextView) findViewById(R.id.aenior_comment);
	mAeniorEdit = (EditText) findViewById(R.id.aenior_reply_text);
	mTakingPictures = (LinearLayout) findViewById(R.id.taking_pictures_layout);
	mPhotoAlbum = (LinearLayout) findViewById(R.id.photo_album_layout);

	mTakingPictures.setOnClickListener(this);
	mPhotoAlbum.setOnClickListener(this);
	mCommit.setOnClickListener(this);
	//设置适配器
	mAdapter = new AeniorReplayAdapter2(mContext, mapList);
	mList.setAdapter(mAdapter);

	mAeniorEdit.setOnTouchListener(new OnTouchListener() {
	    public boolean onTouch(View view, MotionEvent event) {
		view.getParent().requestDisallowInterceptTouchEvent(true);
		return false;
	    }
	});

	mAeniorEdit.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
		mGetEditText = s.toString();

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		/*发送按钮是否可点击*/
		if(mAeniorEdit.getText().length() > 0){
		    mCommit.setTextColor(getResources().getColor(R.color.red));
		    mCommit.setEnabled(true);
		}else{

		    if(null != mAdapter.getList() && mAdapter.getList().size() > 0){//监听图片
			mCommit.setTextColor(getResources().getColor(R.color.red));
			mCommit.setEnabled(true);
		    }else{

			mCommit.setTextColor(getResources().getColor(R.color.location_city_gps));
			mCommit.setEnabled(false);
		    }
		}

		/*是否超出255字符*/

		if(!TextUtils.isEmpty(mGetEditText)){
		    String limitSubstring = YKUtil.getLimitSubstring(mGetEditText);
		    if (!TextUtils.isEmpty(limitSubstring)) {
			if (!limitSubstring.equals(mGetEditText)) {
			    Toast.makeText(mContext, "字数已超过限制",
				    Toast.LENGTH_SHORT).show();
			    mAeniorEdit.setText(limitSubstring);
			    mAeniorEdit.setSelection(limitSubstring.length());
			}

		    }

		}

	    }
	});


    }
    /**
     * 返回监听
     * @param v
     */
    public void AeniorBack(View v){
	mAeniorEdit.clearFocus();
	YKUtil.hideKeyBoard(AeniorReplayActivity2.this, mAeniorEdit);
	finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
	switch (requestCode) {
	case 11://图片选择 返回---zlz

	    if(resultCode == RESULT_OK){
		ArrayList<String> selected = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);

		mAdapter.refresh(selected);
		//ImageList.size > 0 ? 可提交（红色） ：不可提交（黑色）
		if(canCommitFromImage()){
		    mCommit.setTextColor(getResources().getColor(R.color.red));
		    mCommit.setEnabled(true);
		}else{
		    mCommit.setTextColor(getResources().getColor(R.color.location_city_gps));
		    mCommit.setEnabled(false);
		}
	    }

	    break;

	    /*拍照返回调用相机*/
	case 1:

	    if (resultCode == Activity.RESULT_OK) {
		if (YKUtil.hasSdcard()) {
		    startPhotoZoom(Uri.fromFile(mOutputFile));

		} else {
		    Toast.makeText(mContext, "未找到存储卡，无法存储照片！",
			    Toast.LENGTH_SHORT).show();
		}
	    } 
	    break;
	case 4:

	    //适用于小米
	    try {
		Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
		Log.d("Aenior", "uritempFile"+uritempFile+"-8=-"+getContentResolver().openInputStream(uritempFile));
		addCropPicture((uritempFile+"").replace("file:////", ""));
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
		Log.d("tag", e.toString());
	    }  
	    break;

	default:
	    break;
	}



    }

    private void addCropPicture(String path){
	if(path != null){
	    mAdapter.update(path);
	    /*提交按钮可点击*/
	    mCommit.setTextColor(getResources().getColor(R.color.red));
	    mCommit.setEnabled(true);

	}else{
	    Toast.makeText(mContext, "裁剪失败，请重试一下吧~", Toast.LENGTH_SHORT).show();
	    mCommit.setTextColor(getResources().getColor(R.color.location_city_gps));
	    mCommit.setEnabled(false);
	}
    }
    /**
     * 判断图片 是否可 提交
     * @return
     */
    private boolean canCommitFromImage(){
	boolean canCommit = false;
	if(mAdapter.getList().size() <= 0){
	    canCommit = false;
	}else{
	    canCommit = true;
	}
	return canCommit;

    }


    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	/*提交按钮*/
	case R.id.aenior_comment:
	    mCustomButterfly = CustomButterfly.show(mContext);
	    // 是否登录 ？提交 ：跳转登录页面
	    if(YKCurrentUserManager.getInstance().isLogin()){
		/*连网判断*/
		if(!NetWorkUtil.isNetworkAvailable(mContext)){
		    Toast.makeText(mContext, R.string.intent_no, Toast.LENGTH_SHORT).show();
		    return;
		}
		//防止多次点击
		mCommit.setEnabled(false);
		/*是否选择图片*/
		if(mAdapter.getList().size() > 0){//上传图片

		    zoomPicture();

		}else if(mGetEditText.length() > 0){//提交回复
		    commitReplay();
		}else{
		    Toast.makeText(mContext, "图片文字必填一项~", Toast.LENGTH_SHORT).show();
		    mCommit.setEnabled(false);
		    mCustomButterfly.dismiss();
		}

	    }else{
		mCustomButterfly.cancel();//关闭加载页
		Intent goLogin = new Intent(mContext,LoginActivity.class);
		startActivity(goLogin);
	    }

	    break;
	    //相册按钮
	case R.id.photo_album_layout:
	    if(mAdapter.getList().size() >= 3){
		Toast.makeText(mContext, R.string.aenior_nonice_image, Toast.LENGTH_SHORT).show();
		return;
	    }

	    Intent intentImage = new Intent(mContext,SelectAlbumActivity.class);
	    //			intentImage.putExtra(SelectPictureActivity.INTENT_MAX_NUM, 3);//选择三张
	    intentImage.putExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE,mAdapter.getList());
	    startActivityForResult(intentImage,11);

	    break;
	    //拍照按钮
	case R.id.taking_pictures_layout:

	    if(mAdapter.getList().size() >= 3){
		Toast.makeText(mContext, R.string.aenior_nonice_image, Toast.LENGTH_SHORT).show();
		return;
	    }

	    mOutputFile = new File(Environment.getExternalStorageDirectory() + "/",BitmapUtil.getPhotoFileName());

	    Intent intentFromCapture = new Intent(
		    MediaStore.ACTION_IMAGE_CAPTURE);
	    // 判断存储卡是否可以用
	    if (YKUtil.hasSdcard()) {
		intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mOutputFile));

	    }else{
		Toast.makeText(mContext, "储存卡不可用~", Toast.LENGTH_SHORT).show();
	    }
	    startActivityForResult(intentFromCapture, 1);
	    break;
	case R.id.aenior_back_layout:
	    mAeniorEdit.clearFocus();
            YKUtil.hideKeyBoard(AeniorReplayActivity2.this, mAeniorEdit);
	    finish();
	default:
	    break;
	}

    }
    private void zoomPicture() {
	new Thread(){

	    @Override
	    public void run() {
		//循环挨个压缩
		for(int i = 0; i < mAdapter.getList().size();i++){

		    tempBitmap = BitmapUtilImage.getLocationBitmap(mAdapter.getList().get(i));
		    if(tempBitmap != null){
			String path = BitmapUtil.saveMyBitmap("temp"+i, tempBitmap);
			mtempList.add(path);

			if(null != tempBitmap){

			    tempBitmap.recycle();
			}
		    }else{
			mHandler.sendEmptyMessage(203);
		    }

		}

		mHandler.sendEmptyMessage(201);
	    }
	}.start();


    }


    /**
     * 请求网络 
     * 1.上传图片
     * 2.完成开始提交
     */
    private void uploadImage() {


	for (String tempPath : mtempList) {
	    YKUploadImageManager.getInstance().uploadImages(tempPath, new UploadImageCallback()
	    {
		@Override
		public void callback(YKResult result, String imageUrl)
		{
		    times ++;
		    if(result.isSucceeded()){
			size ++;
			mImageUploadUrl.add(imageUrl);

		    }else{
			Toast.makeText(mContext, "这一张失败了…", Toast.LENGTH_SHORT).show();
		    }
		    if(times == mAdapter.getList().size()){
			mHandler.sendEmptyMessage(200);
		    }
		}
	    });
	}
    }


    /**
     * 请求网络 提交回复
     */
    private void commitReplay() {

	String typeId = "";
	switch (type) {
	//心得高级回复
	case 1:
	    typeId = mCommentID;
	    break;
	    //	资讯高级回复
	case 2:
	    typeId = mmCommentIDInfo;
	    break;

	default:
	    break;
	}

	YKCommentreplyManager.getInstance().postAeniorReply(YKCurrentUserManager.getInstance().getUser().getToken(), typeId, mGetEditText, mImageUploadUrl,type,new ReplyCallback()
	{
	    @Override
	    public void callback(YKResult result)
	    {

		mCommit.setEnabled(true);
		AppUtil.dismissDialogSafe(mCustomButterfly);

		if(result.isSucceeded()){
		    Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
		    Intent mIntent = new Intent();  
		    mIntent.putStringArrayListExtra("ListViewImag", mImageUploadUrl);  
		    mIntent.putExtra("textChange", mGetEditText);  
		    setResult(66, mIntent);
		    setResult(22,mIntent);
		    finish();
		}else{
		    Toast.makeText(mContext, result.getMessage().toString(), Toast.LENGTH_SHORT).show();
		}

	    }
	});



    }


    /**
     * 调用系统裁剪图片的方法，可以选择参数为Uri对象  或者  Bitmap对象
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {

	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");
	// 设置裁剪
	intent.putExtra("crop", "true");
	// aspectX aspectY 是宽高的比例
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	// outputX outputY
	// 是裁剪图片宽高http://page.yy.com/login_layer/?action=login&fromClient=pcyy&uid=227811995&ticket=boIBvzCCAbugAwIBBaEDAgEOogcDBQAAAAAAo4IBL2GCASswggEnoAMCAQWhERsPeXkuY29tAAAEAAENlCKbohEwD6ADAgEBoQgwBhsENTA2MKOB%2BTCB9qAEAgIBFaEDAgECooHoBIHlOAJQkzgCUJPQll2T0JZdk2bOoWwXKn8GUiSdnMvpa0aWAfgHxRPixJypC6j5SEVy2r3OXSiW8sQdRciMZXm8OQkwqS8E7DaCU7ady%2Blk5CzzXkN1wcKCzmR88XFvYF7HvRFod8UT4jR82WtYp%2FyZzIX60Eylgnsp%2Fuk9OssUL1BuMz4vOjawC24X0ggfdye26NWZXTUMjzI7TIe%2FbryVBS6sQYePJgzsZLB9jJ%2FphDWN7oyVqiceCUq6nUCNrfkipZhbGkB1Zukyzrnt3Xpj8oZGYeAoq21nBQMCAQuTUAHQk1AB0KRzMHGgAwIBEaJqBGgUbAaVVXwLn0%2FoymtCyIklZVERmfHmw2omM6VGZ0ODpaSY%2BXJ8sFBDY6BaYO9BlThzj6L9Yt6jOkOjiviOVv%2B%2F5heejcXeHihjaa0JTycOYDvw7fd37LeyozFtgLFDUYT6BsngxiyAsg%3D%3D#
	intent.putExtra("outputX", 750);
	intent.putExtra("outputY", 750);
	intent.putExtra("return-data", false);

	//uritempFile为Uri类变量，实例化uritempFile  
	uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" +BitmapUtil.getPhotoFileName());
	intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);  
	intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); 


	startActivityForResult(intent, 4);
    }

    public boolean canCommit(){
	if(mGetEditText.length() > 0){
	    return true;
	}
	return false;
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
	super.onResume();
	MobclickAgent.onPageStart("AeniorReplayActivity2"); // 统计页面
	MobclickAgent.onResume(this); // 统计时长
    }

    public void onPause() {
	super.onPause();
	MobclickAgent.onPageEnd("AeniorReplayActivity2"); // 保证 onPageEnd
	// 在onPause
	// 之前调用,因为 onPause
	// 中会保存信息
	MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
	tempBitmap = null;
	YKActivityManager.getInstance().removeActivity(this);
	super.onDestroy();
    }




}
