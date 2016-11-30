package com.yoka.mrskin.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.login.YKAvatar;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKUpdateEntity;
import com.yoka.mrskin.model.data.YKUpdateUserInfo;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.UpdateUserEntitiesCallback;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.UpdateUserInfoCallback;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.YKGeneralCallBack;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.AvatarUtil;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.YKUtil;

/**
 * 修改个人资料
 * 
 * @author z l l
 * 
 */
public class UpdateUserInfoActivity extends BaseActivity implements
OnClickListener
{

    private static final String TAG = UpdateUserInfoActivity.class.getSimpleName();
    private static final int TYPE_SKIN = 12;
    private static final int TYPE_BUY = 22;
    private static final int TYPE_AGE = 33;
    private static final int TYPE_SEX = 44;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int REQUEST_CODE_CITY = 0;
    private static final int RESULT_CODE = 0;
    private static final int REQUEST_CODE_PHONE = 8;
    private static final int REQUEST_CODE_EMAIL = 9;
    private static final int REQUEST_CODE_BRAND = 10;
    private static final String IMAGE_FILE_NAME = "head.jpg";
    private String mCheckIds = "";
    private String[] items = new String[] { "选择本地图片", "拍照" };
    private String[] mSkinType = new String[] { "中性", "油性", "干性", "混合性", "敏感肌肤" };
    private String[] mSexType = new String[] { "女", "男"};
    public File tempFile;
    // private View mNameLayout, mUlevelLayout, mHlevelLayout;
    private View mBackLayout, mHeadLayout, mSkinLayout, mHobbyLayout,
    mBuyLayout, mAgeLayout, mAreaLayout, mPhoneLayout, mEmailLayout,
    mAddressLayout,mSexLayout;
    private TextView mNameText, mUlevelText, mHlevelText, mSkinText,mHobbyText, mBuyText, mAgeText, mAreaText, mPhoneText, mEmailText,mSexText;
    private RoundImage mHeadImg;
    private ImageView mPhoneMore;
    private YKUpdateUserInfo mUpdateUserInfo;

    private DisplayImageOptions options;

    private Uri uritempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_update_user_info);
	options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
		.resetViewBeforeLoading(true).considerExifParams(true)
		.build();
	initView();
	getWholeData();
    }

    private void initView() {
	mHeadImg = (RoundImage) findViewById(R.id.update_user_info_head_img);
	mBackLayout = findViewById(R.id.update_user_info_back);
	mHeadLayout = findViewById(R.id.update_user_info_head);

	mPhoneMore = (ImageView) findViewById(R.id.update_user_info_phone_more);
	// mNameLayout = findViewById(R.id.update_user_info_name);
	// mUlevelLayout = findViewById(R.id.update_user_info_ulevel);
	// mHlevelLayout = findViewById(R.id.update_user_info_hlevel);
	mSkinLayout = findViewById(R.id.update_user_info_skin);
	mHobbyLayout = findViewById(R.id.update_user_info_hobby);
	mBuyLayout = findViewById(R.id.update_user_info_buy);
	mAgeLayout = findViewById(R.id.update_user_info_age);
	mAreaLayout = findViewById(R.id.update_user_info_area);
	mPhoneLayout = findViewById(R.id.update_user_info_phone);
	mEmailLayout = findViewById(R.id.update_user_info_email);
	mAddressLayout = findViewById(R.id.update_user_info_address);
	mSexLayout = findViewById(R.id.update_user_info_sex);

	mBackLayout.setOnClickListener(this);
	mHeadLayout.setOnClickListener(this);
	// mNameLayout.setOnClickListener(this);
	// mUlevelLayout.setOnClickListener(this);
	// mHlevelLayout.setOnClickListener(this);
	mSkinLayout.setOnClickListener(this);
	mHobbyLayout.setOnClickListener(this);
	mBuyLayout.setOnClickListener(this);
	mAgeLayout.setOnClickListener(this);
	mAreaLayout.setOnClickListener(this);
	mPhoneLayout.setOnClickListener(this);
	mEmailLayout.setOnClickListener(this);
	mAddressLayout.setOnClickListener(this);
	mSexLayout.setOnClickListener(this);

	mNameText = (TextView) findViewById(R.id.update_user_info_name_text);
	mSexText = (TextView) findViewById(R.id.update_user_info_sex_text);
	mUlevelText = (TextView) findViewById(R.id.update_user_info_ulevel_text);
	mHlevelText = (TextView) findViewById(R.id.update_user_info_hlevel_text);
	mSkinText = (TextView) findViewById(R.id.update_user_info_skin_text);
	mHobbyText = (TextView) findViewById(R.id.update_user_info_hobby_text);
	mBuyText = (TextView) findViewById(R.id.update_user_info_buy_text);
	mAgeText = (TextView) findViewById(R.id.update_user_info_age_text);
	mAreaText = (TextView) findViewById(R.id.update_user_info_area_text);
	mPhoneText = (TextView) findViewById(R.id.update_user_info_phone_text);
	mEmailText = (TextView) findViewById(R.id.update_user_info_email_text);
    }

    private void getWholeData() {

	YKUpdateUserInfoManager.getInstance().requestUserInfo(
		new UpdateUserInfoCallback() {

		    @Override
		    public void callback(YKResult result,
			    YKUpdateUserInfo userInfo) {
			if (result.isSucceeded() && userInfo != null) {
			    mUpdateUserInfo = userInfo;
			    updateUI(userInfo);
			}else{
			    if (AppUtil.isNetworkAvailable(UpdateUserInfoActivity.this)) {
				Toast.makeText(UpdateUserInfoActivity.this, R.string.intent_error,
					Toast.LENGTH_SHORT).show();
			    } else {
				Toast.makeText(UpdateUserInfoActivity.this, R.string.intent_no, Toast.LENGTH_SHORT).show();
			    }
			}
		    }
		});
    }

    private void getDialogData(final int type) {
	YKUpdateUserInfoManager.getInstance().requestUserEntities(type,
		new UpdateUserEntitiesCallback() {

	    @Override
	    public void callback(YKResult result,
		    ArrayList<YKUpdateEntity> userEntities) {
		if (result.isSucceeded() && userEntities != null) {
		    int size = userEntities.size();
		    String[] entities = new String[size];
		    for (int i = 0; i < userEntities.size(); i++) {
			entities[i] = userEntities.get(i).getmName();
		    }
		    switch (type) {
		    case TYPE_SKIN:
			showDialog("选择肤质", entities, TYPE_SKIN);
			break;
		    case TYPE_BUY:
			showDialog("美妆月消费", entities, TYPE_BUY);
			break;
		    case TYPE_AGE:
			showDialog("选择年龄", entities, TYPE_AGE);
			break;
		    }

		}
	    }
	});
    }

    private void updateUI(YKUpdateUserInfo userInfo) {
	if (userInfo != null) {
	    String city = "";
	    String province = "";
	    String avatarpath = AvatarUtil.getInstance(UpdateUserInfoActivity.this).getAvatarPath();
	    if(!"".equals(avatarpath)){

		ImageLoader.getInstance().displayImage("file://"+avatarpath, mHeadImg, options);

		/*      Glide.with(UpdateUserInfoActivity.this).load(avatarpath)
                .into(mHeadImg);*/
	    }else if (!TextUtils.isEmpty(userInfo.getmAvatar().getmHeadUrl())) {

		Log.d("ImageUrl=", avatarpath);
		ImageLoader.getInstance().displayImage("file://"+userInfo.getmAvatar().getmHeadUrl(), mHeadImg, options);
		/*    Glide.with(UpdateUserInfoActivity.this).load(userInfo.getmAvatar().getmHeadUrl())
                .into(mHeadImg);*/

	    }


	    if (!TextUtils.isEmpty(userInfo.getmName())) {
		mNameText.setText(userInfo.getmName());
	    }
	    if (!TextUtils.isEmpty(userInfo.getmName())) {
		mSexText.setText(userInfo.getmSex());
	    }
	    if (!TextUtils.isEmpty(userInfo.getmHLevel())) {
		mHlevelText.setText(userInfo.getmHLevel());
	    }
	    if (!TextUtils.isEmpty(userInfo.getmUserLevel())) {
		mUlevelText.setText(userInfo.getmUserLevel());
	    }
	    if (!TextUtils.isEmpty(userInfo.getmCity())) {
		city = userInfo.getmCity();
	    }
	    if (!TextUtils.isEmpty(userInfo.getmProvince())) {
		province = userInfo.getmProvince();
	    }

	    mAreaText.setText(province.equals("") ? province : province +"—" + city);

	    if (!TextUtils.isEmpty(userInfo.getmSkin())) {
		mSkinText.setText(userInfo.getmSkin());
	    }
	    if (!TextUtils.isEmpty(userInfo.getmUserMoney())) {
		mBuyText.setText(userInfo.getmUserMoney());
	    }
	    if (!TextUtils.isEmpty(userInfo.getmUserAge())) {
		mAgeText.setText(userInfo.getmUserAge());
	    }
	    if (!TextUtils.isEmpty(userInfo.getmSex())) {
		String sexSelect = null;
		if(userInfo.getmSex().equals("1")){
		    sexSelect = "男";
		}else{
		    sexSelect = "女";
		}
		mSexText.setText(sexSelect);
	    }
	    if (!TextUtils.isEmpty(userInfo.getmTelephone())) {
		mPhoneText.setText(userInfo.getmTelephone());
		mPhoneMore.setVisibility(View.INVISIBLE);
	    }else{
		mPhoneMore.setVisibility(View.VISIBLE);
	    }
	    if (!TextUtils.isEmpty(userInfo.getmEmail())) {
		mEmailText.setText(userInfo.getmEmail());
	    }
	    if (!TextUtils.isEmpty(userInfo.getmBrandLike())) {
		mCheckIds = userInfo.getmBrandLike();
	    }
	}
    }



    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	// 返回
	case R.id.update_user_info_back:
	    finish();
	    break;
	    // 头像
	case R.id.update_user_info_head:
	    showChooseImageDialog();
	    break;
	    //性别
	case R.id.update_user_info_sex:
	    showDialog("选择性别",mSexType,TYPE_SEX);
	    break;
	    // 肤质
	case R.id.update_user_info_skin:
	    showDialog("选择肤质", mSkinType, TYPE_SKIN);
	    break;
	    // 美妆偏好
	case R.id.update_user_info_hobby:
	    // startActivity(UpdateBrandHobbyActivity.class);
	    Intent brandlike = new Intent(UpdateUserInfoActivity.this,
		    UpdateBrandHobbyActivity.class);
	    brandlike.putExtra("brandlike", mCheckIds);
	    startActivityForResult(brandlike, REQUEST_CODE_BRAND);
	    break;
	    // 美容月消费
	case R.id.update_user_info_buy:
	    getDialogData(TYPE_BUY);
	    break;
	    // 年龄区间
	case R.id.update_user_info_age:
	    getDialogData(TYPE_AGE);
	    break;
	    // 地区
	case R.id.update_user_info_area:
	    // startActivity(UpdateCityActivity.class);
	    Intent areaIntent = new Intent(UpdateUserInfoActivity.this,
		    UpdateCityActivity2.class);
	    startActivityForResult(areaIntent, REQUEST_CODE_CITY);
	    break;
	    // 手机号
	case R.id.update_user_info_phone:
	    if(mUpdateUserInfo != null){
		if(TextUtils.isEmpty(mUpdateUserInfo.getmTelephone())){
		    Intent phoneIntent = new Intent(UpdateUserInfoActivity.this,
			    UpdatePhoneNumberActivity.class);
		    startActivityForResult(phoneIntent, REQUEST_CODE_PHONE);
		}
	    }
	    // startActivity(UpdatePhoneNumberActivity.class);

	    break;
	    // 邮箱
	case R.id.update_user_info_email:
	    // startActivity(UpdateEmailActivity.class);
	    Intent emailIntent = new Intent(UpdateUserInfoActivity.this,
		    UpdateEmailActivity.class);
	    emailIntent.putExtra("email", mEmailText.getText().toString());
	    startActivityForResult(emailIntent, REQUEST_CODE_EMAIL);
	    break;
	    // 地址管理
	case R.id.update_user_info_address:
	    startActivity(UpdateAddressActivity.class);
	    break;
	}
    }

    @SuppressWarnings("rawtypes")
    private void startActivity(Class c) {
	Intent intent = new Intent(this, c);
	startActivity(intent);
    }


    private void showDialog(String title, final String[] items, final int type) {
	Dialog dialog = new AlertDialog.Builder(this).setTitle(title)
		.setCancelable(false)
		.setItems(items, new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			switch (type) {
			case TYPE_SKIN:
			    mSkinText.setText(items[which]);
			    updateUserEntities("skin", which + 1 + "");
			    break;
			case TYPE_BUY:
			    mBuyText.setText(items[which]);
			    updateUserEntities("usermoney", which + 1 + "");
			    break;
			case TYPE_AGE:
			    mAgeText.setText(items[which]);
			    updateUserEntities("userage", which + 1 + "");
			    break;
			case TYPE_SEX:
			    mSexText.setText(items[which]);
			    updateUserEntities("sex", String.valueOf(which));
			    break;
			}
		    }
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {


		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			switch (type) {
			case TYPE_SKIN:
			    mSkinText.setText(items[which]);
			    updateUserEntities("skin", which + 1 + "");
			    break;
			case TYPE_BUY:
			    mBuyText.setText(items[which]);
			    updateUserEntities("usermoney", which + 1 + "");
			    break;
			case TYPE_AGE:
			    mAgeText.setText(items[which]);
			    updateUserEntities("userage", which + 1 + "");
			    break;
			}
		    }
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		    }
		}).create();
	dialog.show();
    }

    private void updateUserEntities(String key, String value) {
	YKUpdateUserInfoManager.getInstance().requestUpdateUSerInfo(key, value,
		new YKGeneralCallBack() {

	    @Override
	    public void callback(YKResult result,String imageUrl) {
		if (result.isSucceeded()) {
		    Toast.makeText(UpdateUserInfoActivity.this, "修改成功",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    /**
     * 相册选择图片或拍照
     */
    private void showChooseImageDialog() {

	new AlertDialog.Builder(this)
	.setTitle("设置头像")
	.setItems(items, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case 0://选择本地图片
		tempFile = new File(Environment.getExternalStorageDirectory()+ "/"+ IMAGE_FILE_NAME);
		Intent intentFromGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE); 
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
		break;
		case 1://拍照
		    tempFile = new File(Environment.getExternalStorageDirectory()+ "/"+ IMAGE_FILE_NAME);
		    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    // 判断存储卡是否可以用，可用进行存储
		    if (YKUtil.hasSdcard()) {
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
		    }
		    startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
		    break;
		}
	    }
	})
	.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	}).show();


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == REQUEST_CODE_CITY && resultCode == RESULT_CODE) {
	    if (data != null) {
		String city = data.getExtras().getString("city_name");
		String province = data.getExtras().getString("province_name");
		if (TextUtils.isEmpty(city)) {
		    city = "";
		}
		if (TextUtils.isEmpty(province)) {
		    province = "";
		}
		mAreaText.setText(province + "—" + city);
	    }
	}

	if (requestCode == REQUEST_CODE_PHONE && resultCode == RESULT_CODE) {
	    if (data != null) {
		String email = data.getExtras().getString("phone");
		if (!TextUtils.isEmpty(email)) {
		    mPhoneText.setText(email);
		    mPhoneMore.setVisibility(View.INVISIBLE);
		    mPhoneLayout.setFocusable(false);
		}
	    }
	}

	if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_CODE) {
	    if (data != null) {
		String email = data.getExtras().getString("email");
		if (!TextUtils.isEmpty(email)) {
		    mEmailText.setText(email);
		}
	    }
	}

	if (requestCode == REQUEST_CODE_BRAND && resultCode == RESULT_CODE) {
	    if (data != null) {
		String ids = data.getExtras().getString("brandlike");
		if (!TextUtils.isEmpty(ids)) {
		    mCheckIds = "";
		    mCheckIds = ids;
		}
	    }
	}

	switch (requestCode) {
	case IMAGE_REQUEST_CODE:
	    if (resultCode == Activity.RESULT_OK) {
		String  path = AvatarUtil.getPath(UpdateUserInfoActivity.this, data.getData());  

		startPhotoZoom( Uri.fromFile(new File(path)));
	    }
	    break;
	case CAMERA_REQUEST_CODE:
	    if (resultCode == Activity.RESULT_OK) {
		if (YKUtil.hasSdcard()) {
		    startPhotoZoom(Uri.fromFile(tempFile));
		} else {
		    Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case RESULT_REQUEST_CODE://返回裁剪后图片
	//适用于小米
	    try {
		Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
		Log.d(TAG, "uritempFile"+uritempFile+"-8=-"+getContentResolver().openInputStream(uritempFile));
		updateHeadImg((uritempFile+"").replace("file:////", ""));
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }  
	    break;
	}
    }


    /**
     * 裁剪图片
     * 
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");
	// 设置裁剪
	intent.putExtra("crop", "true");
	// aspectX aspectY 是宽高的比例
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	// outputX outputY
	// 是裁剪图片宽高http://page.yy.com/login_layer/?action=login&fromClient=pcyy&uid=227811995&ticket=boIBvzCCAbugAwIBBaEDAgEOogcDBQAAAAAAo4IBL2GCASswggEnoAMCAQWhERsPeXkuY29tAAAEAAENlCKbohEwD6ADAgEBoQgwBhsENTA2MKOB%2BTCB9qAEAgIBFaEDAgECooHoBIHlOAJQkzgCUJPQll2T0JZdk2bOoWwXKn8GUiSdnMvpa0aWAfgHxRPixJypC6j5SEVy2r3OXSiW8sQdRciMZXm8OQkwqS8E7DaCU7ady%2Blk5CzzXkN1wcKCzmR88XFvYF7HvRFod8UT4jR82WtYp%2FyZzIX60Eylgnsp%2Fuk9OssUL1BuMz4vOjawC24X0ggfdye26NWZXTUMjzI7TIe%2FbryVBS6sQYePJgzsZLB9jJ%2FphDWN7oyVqiceCUq6nUCNrfkipZhbGkB1Zukyzrnt3Xpj8oZGYeAoq21nBQMCAQuTUAHQk1AB0KRzMHGgAwIBEaJqBGgUbAaVVXwLn0%2FoymtCyIklZVERmfHmw2omM6VGZ0ODpaSY%2BXJ8sFBDY6BaYO9BlThzj6L9Yt6jOkOjiviOVv%2B%2F5heejcXeHihjaa0JTycOYDvw7fd37LeyozFtgLFDUYT6BsngxiyAsg%3D%3D#
	intent.putExtra("outputX", 450);
	intent.putExtra("outputY", 450);
	//		intent.putExtra("return-data", true);

	//uritempFile为Uri类变量，实例化uritempFile  
	uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");  
	intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);  
	intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); 

	startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    public void getImageToView(Intent data) {
	String filePath = null;
	if (tempFile != null && tempFile.exists()) {
	    filePath = tempFile.getAbsolutePath();
	}
	updateHeadImg(filePath);
    }

    private void updateHeadImg(String filePath) {

	if (YKCurrentUserManager.getInstance().isLogin()) {
	    //	Log.d(TAG, "imageUrl-old="+filePath);

	    YKUpdateUserInfoManager.getInstance().requestUpdateUserHead(
		    YKCurrentUserManager.getInstance().getUser().getAuth(),
		    filePath, new YKGeneralCallBack() {


			@Override
			public void callback(YKResult result,String imageUrl) {
			    if (result.isSucceeded()) {
				if(!TextUtils.isEmpty(imageUrl)){
				    //                                  
				    YKAvatar avat = new YKAvatar(); 
				    avat.setmHeadUrl(imageUrl);
				    YKCurrentUserManager.getInstance().getUser().setAvatar(avat);
				    //保存到本地  成功之后handler展示图片
				    AvatarUtil.getInstance(UpdateUserInfoActivity.this).AsyncHttpFileDown(imageUrl,handler);

				}
				if (tempFile != null && tempFile.exists()) {
				    tempFile.delete();
				}
			    } else {
				test(result.getMessage().toString());
				// Toast.makeText(UpdateUserInfoActivity.this,
				// result.getMessage().toString() + "",
				// Toast.LENGTH_SHORT)
				// .show();
			    }
			}
		    });

	}
    }
    /* (non-Javadoc)
     * @see com.yoka.mrskin.activity.base.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	String imageUrl = AvatarUtil.getInstance(UpdateUserInfoActivity.this).getAvatarPath();
	// Glide.with(UpdateUserInfoActivity.this).load(imageUrl).into(mHeadImg);
	ImageLoader.getInstance().displayImage("file://"+imageUrl, mHeadImg, options);
	super.onResume();
    }

    private void test(String title) {
	Dialog dialog = new AlertDialog.Builder(this).setTitle(title)
		.setCancelable(false)
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		    }
		}).create();
	dialog.show();
    }
    //	
    private Handler handler = new Handler(){

	@Override
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case 201:
		String path = (String) msg.obj;
		ImageLoader.getInstance().displayImage("file://"+path, mHeadImg, options);
		// Glide.with(MrSkinApplication.getApplication()).load(path).into(mHeadImg);

		break;

	    default:
		break;
	    }
	    super.handleMessage(msg);
	}

    };
}
