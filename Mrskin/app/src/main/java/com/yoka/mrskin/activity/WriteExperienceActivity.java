package com.yoka.mrskin.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.addimage.BitmapUtil;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.data.YKUpDataExperience;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKPublishManager;
import com.yoka.mrskin.model.managers.YKPublishManager.Callback;
import com.yoka.mrskin.model.managers.YKUploadImageManager;
import com.yoka.mrskin.model.managers.YKUploadImageManager.UploadImageCallback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.BitmapUtilImage;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.WritePhotoPopupWindow;
import com.yoka.mrskin.util.YKFile;
import com.yoka.mrskin.util.YKUtil;
/**
 * @author yuhailong
 * 撰写心得
 *
 */
public class WriteExperienceActivity extends BaseActivity implements OnClickListener
{
    private static String CACHE_DRAFT = "fujun_draft";
    private static String CACHE_PRODUCT = "fujun_draft_product";
    private static String CACHE_TITLE = "writedefaulititle";

    private LinearLayout mBack,mWriteText,mWriteImage,mWriteProduct;

    private ArrayList<YKUpDataExperience> mExperience = new ArrayList<YKUpDataExperience>();
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private WritePhotoPopupWindow mPopupWindow;
    private YKProduct mProduct;
    private GridImageAdapter mGridImageAdapter;

    private boolean mHasDraft = false;
    private EditText mDefaultTitle;
    private TextView mWritePublish;
    private ListView mListView;
    private File mOutputFile;
    private String mSearchText;
    private CustomButterfly mCustomButterfly = null;
    private int mUploadImageCount;
    private int mTextNumber;
    private int mTotalUploadImageCount;

    private ArrayList<YKUpDataExperience> mDraftExperience;
    private String mDraftTitle;

    private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true).considerExifParams(true)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.write_experience_listview);
        init();

        loadDraft();
        if(mHasDraft){
            showDraftDialog();
        }

    }
    //删除保存的草稿
    private void deleteFile(){
        YKFile.remove(CACHE_DRAFT);
        YKFile.remove(CACHE_TITLE);
        YKFile.remove(CACHE_PRODUCT);
    }

    //加载标题
    private void loadDraftTitle(){
        String title = loadDefaultTitleDataFromFile();
        if(TextUtils.isEmpty(title)){
            mHasDraft = mHasDraft || false;
        }else{
            mDraftTitle = title;
            mHasDraft = true;
        }
    }
    //加载数据
    private void loadDraftExperience(){
        if(loadExperienceFile() != null){
            mDraftExperience = loadExperienceFile();
            mHasDraft = (mDraftExperience != null && mDraftExperience.size() > 0) || mHasDraft;
        }
    }

    private void init(){

        mListView = (ListView) findViewById(R.id.write_add_layout);
        mDefaultTitle = (EditText) findViewById(R.id.write_default_title);
        mWritePublish = (TextView) findViewById(R.id.write_publish);
        mWritePublish.setOnClickListener(this);

        mWriteText = (LinearLayout) findViewById(R.id.write_text_layout);
        mWriteText.setOnClickListener(this);
        mBack = (LinearLayout) findViewById(R.id.write_layout_back);
        mBack.setOnClickListener(this);
        mWriteImage = (LinearLayout) findViewById(R.id.write_image_layout);
        mWriteImage.setOnClickListener(this);
        mWriteProduct = (LinearLayout) findViewById(R.id.write_product_layout);
        mWriteProduct.setOnClickListener(this);

        mDefaultTitle.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideKeyBord();
                    mSearchText = mDefaultTitle.getText().toString().trim();
                    if (TextUtils.isEmpty(mSearchText)) {
                        hideKeyBord();
                    } else {
                        mDefaultTitle.setText(mSearchText);
                        saveDraftTitle(mSearchText);
                        mHasDraft = true;
                        mDefaultTitle.setCursorVisible(false);
                    }
                }
                return false;
            }
        });

        mGridImageAdapter = new GridImageAdapter(WriteExperienceActivity.this);
        mListView.setAdapter(mGridImageAdapter);

    }

    private void showSelectImageDialog(){
        mPopupWindow = new WritePhotoPopupWindow(WriteExperienceActivity.this, itemsOnClick);
        mPopupWindow.showAtLocation(
                WriteExperienceActivity.this.findViewById(R.id.write_add_layout),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private OnClickListener itemsOnClick = new OnClickListener()
    {
        public void onClick(View v)
        {
            WritePhotoPopupWindow.getInstance().dismiss();
            ArrayList<String> mImageList = new ArrayList<String>();

            for (int i = 0; i < mExperience.size(); i++) {
                if(mExperience.get(i).getUrl() != null){
                    String path = mExperience.get(i).getUrl();
                    mImageList.add(path);
                }
            }
            if(mImageList.size() >= 9){
                Toast.makeText(WriteExperienceActivity.this, R.string.write_nonice_image, Toast.LENGTH_SHORT).show();
                return;
            }

            switch (v.getId()) {
            //相册选择
            case R.id.popupwindow_image:
                Intent intentImage = new Intent(WriteExperienceActivity.this,ImageListActivity.class);
                intentImage.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataList",mImageList);
                bundle.putSerializable("size",9);
                intentImage.putExtras(bundle);
                startActivityForResult(intentImage, 0);
                break;
            case R.id.popupwindow_photograph:
                //相机选择
                mOutputFile = new File(Environment.getExternalStorageDirectory() + "/",BitmapUtil.getPhotoFileName());
                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                // 判断存储卡是否可以用
                if (YKUtil.hasSdcard()) {
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(mOutputFile));
                }
                startActivityForResult(intentFromCapture, 1);  
                break;
            }
            mPopupWindow.dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        String title = null;
        switch (v.getId()) {
        case R.id.write_layout_back:
            backButton(title);
            break;
        case R.id.write_text_layout:
            Intent writeText = new Intent(WriteExperienceActivity.this,WriteTextLayout.class);
            writeText.putExtra("position", "");
            startActivityForResult(writeText,2);
            break;
        case R.id.write_image_layout:
            showSelectImageDialog();
            break;
        case R.id.write_product_layout:
            productButton();
            break;
        case R.id.write_publish:
            publishButton(title);
            break;
        default:
            break;
        }
    }

    //发布心得
    private void requestPublish(ArrayList<YKUpDataExperience> experience){
        YKUser user = YKCurrentUserManager.getInstance().getUser();
        String title = mDefaultTitle.getText().toString();
        YKPublishManager.getInstance().postYKPublishData(user.getId(),title,experience, new Callback()
        {
            @Override
            public void callback(YKResult result,String url)
            {
                AppUtil.dismissDialogSafe(mCustomButterfly);
                if(result.isSucceeded()){
                    Toast.makeText(WriteExperienceActivity.this, R.string.write_publish_success, Toast.LENGTH_SHORT).show();
                    Intent experoenceIntent = new Intent(WriteExperienceActivity.this,YKWebViewActivity.class);
                    //                    experoenceIntent.putExtra(ExperienceListActivity.EXPERIENCE_INTENT_USERID, 
                    //                            YKCurrentUserManager.getInstance(WriteExperienceActivity.this).getUser().getId());
                    experoenceIntent.putExtra("writeurl", url);
                    startActivity(experoenceIntent);
                    YKWebViewActivity.SHOP_CHANGE=true;
                    finish();
                    deleteFile();
                    mExperience.clear();
                    mDefaultTitle.setText("");
                    mGridImageAdapter.notifyDataSetChanged();
                }else{
                    String meaaage = (String) result.getMessage();
                    Toast.makeText(WriteExperienceActivity.this, meaaage, Toast.LENGTH_SHORT).show();
                }
            }
        }); 
    }

    public class GridImageAdapter extends BaseAdapter {

        private Context mContext;
        private ViewHolder viewHolder = null;

        public GridImageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            if(mExperience == null){
                return 0;
            }
            return mExperience.size();
        }

        @Override
        public Object getItem(int position) {
            if (mExperience == null) {
                return null;
            }
            return mExperience.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final YKUpDataExperience mWriteTextIMage = mExperience.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.write_experience_item, null);
                viewHolder = new ViewHolder();
                viewHolder.sTitle = (TextView) convertView.findViewById(R.id.write_experience_text);
                viewHolder.sBigImage = (ImageView) convertView.findViewById(R.id.write_experience_image);
                viewHolder.sProductLayout =(RelativeLayout) convertView.findViewById(R.id.write_experience_product_layout);
                viewHolder.sTextLayout = (RelativeLayout) convertView.findViewById(R.id.write_experience_text_layout);
                viewHolder.sImageLayout = (RelativeLayout) convertView.findViewById(R.id.write_experience_image_layout);
                viewHolder.sProImage = (ImageView) convertView.findViewById(R.id.write_experience_product_image);
                viewHolder.sProductTitle = (TextView) convertView.findViewById(R.id.write_experience_title);    
                viewHolder.sRatingBar = (RatingBar) convertView.findViewById(R.id.write_experience_bar);
                viewHolder.sMl = (TextView) convertView.findViewById(R.id.write_experience_ml);
                viewHolder.sPrice = (TextView) convertView.findViewById(R.id.write_experience_price);

                viewHolder.sTextDelete = (ImageView) convertView.findViewById(R.id.write_experience_text_delete);
                viewHolder.sImageDelete = (ImageView) convertView.findViewById(R.id.write_experience_image_delete);
                viewHolder.sProductDelete = (ImageView) convertView.findViewById(R.id.write_experience_product_delete);
                viewHolder.imageLayout = (RelativeLayout) convertView.findViewById(R.id.write_image_layout);

                convertView.setTag(R.id.write_add_layout, viewHolder);
            } else{
                viewHolder = (ViewHolder) convertView.getTag(R.id.write_add_layout);
            }
            //相机
            if(mWriteTextIMage.getmType() != null && mWriteTextIMage.getmType().equals("1")){
                if (mWriteTextIMage.getUrl() != null){
                    String path = mWriteTextIMage.getUrl();
                    // calculate image height

                    WindowManager wm = (WindowManager) WriteExperienceActivity.this
                            .getSystemService(Context.WINDOW_SERVICE);
                    @SuppressWarnings("deprecation")
                    int screenWidth = (wm.getDefaultDisplay().getWidth())
                    - YKUtil.dip2px(mContext, 20);

                    int height = 0;
                    int width = 0;
                    int tmpHeight = 0;
                    if(path != null){
                        height = mWriteTextIMage.getmHeight();
                        width = mWriteTextIMage.getmWidth();
                    }else{
                        height = 750;
                        width = 750;
                    }

                    tmpHeight = screenWidth * height / width;
                    viewHolder.imageLayout
                    .setLayoutParams(new RelativeLayout.LayoutParams(
                            screenWidth, tmpHeight));


                    //Bitmap bit = BitmapFactory.decodeFile(path);
                    if(!TextUtils.isEmpty(path)){
                        //   viewHolder.sBigImage.setImageBitmap(bit);
                        ImageLoader.getInstance().displayImage("file://"+path, viewHolder.sBigImage, options);

                        //Glide.with(WriteExperienceActivity.this).load(path).into(viewHolder.sBigImage);
                        viewHolder.sImageLayout.setVisibility(View.VISIBLE);
                    }else{
                        mExperience.remove(position);
                        viewHolder.sImageLayout.setVisibility(View.GONE);
                        Toast.makeText(WriteExperienceActivity.this, "拍照失败重新拍照", Toast.LENGTH_SHORT).show();
                    }

                    //                    SelectImage.from(WriteExperienceActivity.this)
                    //                    .displayImage(viewHolder.sBigImage, path);

                    viewHolder.sProductLayout.setVisibility(View.GONE);
                    viewHolder.sTextLayout.setVisibility(View.GONE);

                }
                //相册
            }else if(mWriteTextIMage.getmType() != null && mWriteTextIMage.getmType().equals("0")){
                if(mWriteTextIMage.getUrl() != null){
                    String path = mWriteTextIMage.getUrl();

                    // calculate image height
                    WindowManager wm = (WindowManager) WriteExperienceActivity.this
                            .getSystemService(Context.WINDOW_SERVICE);
                    @SuppressWarnings("deprecation")
                    int screenWidth = (wm.getDefaultDisplay().getWidth())
                    - YKUtil.dip2px(mContext, 20);

                    //Bitmap bit =   convertToBitmap(path);
                    int[] n = BitmapUtilImage.BitmapSiz(path);
                    int height = 0;
                    int width = 0;
                    int tmpHeight = 0;

                    if(n != null){
                        height = n[1];
                        width = n[0];
                    }else{
                        height = 750;
                        width = 750;
                    }

                    tmpHeight = screenWidth * height / width;
                    viewHolder.imageLayout
                    .setLayoutParams(new RelativeLayout.LayoutParams(
                            screenWidth, tmpHeight));
                    ImageLoader.getInstance().displayImage("file://"+path, viewHolder.sBigImage, options);

                    /*Glide.with(WriteExperienceActivity.this).load(path)
            		.into(viewHolder.sBigImage);*/
                    viewHolder.sProductLayout.setVisibility(View.GONE);
                    viewHolder.sTextLayout.setVisibility(View.GONE);
                    viewHolder.sImageLayout.setVisibility(View.VISIBLE);

                }
                //文本
            }else if(mWriteTextIMage.getContent() != null){
                String text = mWriteTextIMage.getContent();
                viewHolder.sTitle.setText(text);
                viewHolder.sProductLayout.setVisibility(View.GONE);
                viewHolder.sTextLayout.setVisibility(View.VISIBLE);
                viewHolder.sImageLayout.setVisibility(View.GONE);
                //妆品
            }else if(mWriteTextIMage.getProdut_id() != null){
                if(mProduct.getCover() != null){
                    String path = mProduct.getCover().getmURL();
                    /*Glide.with(WriteExperienceActivity.this).load(path)
            		.into(viewHolder.sProImage);*/
                    ImageLoader.getInstance().displayImage(path, viewHolder.sProImage, options);

                    viewHolder.sProductTitle.setText(mProduct.getTitle());
                    viewHolder.sRatingBar.setRating(mProduct.getRating());
                    if (!TextUtils.isEmpty(mProduct.getSpecification().getTitle())) {
                        viewHolder.sMl.setText("/"  
                                + mProduct.getSpecification()
                                .getTitle());
                    } else {
                        viewHolder.sMl.setText(" ");
                    }
                    float price = 0f;
                    if (mProduct.getSpecification().getPrice() != null) {
                        price = mProduct.getSpecification()
                                .getPrice();
                        // 构造方法的字符格式这里如果小数不足2位,会以0补足
                        DecimalFormat decimalFormat = new DecimalFormat("0");
                        // format 返回的是字符串
                        if (price == 0) {
                            viewHolder.sPrice
                            .setText(getString(R.string.mybag_price));
                        } else {
                            String newPric = decimalFormat.format(price);
                            viewHolder.sPrice.setText("¥" + newPric);
                        }
                    }
                    viewHolder.sProductLayout.setVisibility(View.VISIBLE);
                    viewHolder.sTextLayout.setVisibility(View.GONE);
                    viewHolder.sImageLayout.setVisibility(View.GONE);
                }
            }

            viewHolder.sTextDelete.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder = new Builder(WriteExperienceActivity.this);
                    builder.setMessage("确认删除文本?");
                    builder.setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mExperience.remove(position);
                            YKFile.remove(CACHE_DRAFT);
                            mHasDraft = true;
                            mGridImageAdapter.notifyDataSetChanged();
                            Toast.makeText(mContext, "删除文本成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.dialog_delete_cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });

            viewHolder.sImageDelete.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v){
                    AlertDialog.Builder builder = new Builder(WriteExperienceActivity.this);
                    builder.setMessage("确认删除图片?");
                    builder.setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mExperience.remove(position).getUrl();
                            YKFile.remove(CACHE_DRAFT);
                            mHasDraft = true;
                            mGridImageAdapter.notifyDataSetChanged();
                            Toast.makeText(mContext, "删除图片成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.dialog_delete_cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            viewHolder.sProductDelete.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder = new Builder(WriteExperienceActivity.this);
                    builder.setMessage("确认删除妆品?");
                    builder.setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mExperience.remove(position);
                            mProduct = null;
                            mHasDraft = true;
                            YKFile.remove(CACHE_PRODUCT);
                            mGridImageAdapter.notifyDataSetChanged();
                            Toast.makeText(mContext, "删除妆品成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.dialog_delete_cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            viewHolder.sProductLayout.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent goProduct = new Intent(WriteExperienceActivity.this,WriteSearchProductActivity.class);
                    String setposition = String.valueOf(position);
                    goProduct.putExtra("position", setposition);
                    startActivityForResult(goProduct, 3);
                }
            });
            viewHolder.sTextLayout.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent goText = new Intent(WriteExperienceActivity.this,WriteTextLayout.class);
                    if(mExperience.get(position).getContent() != null){
                        String setposition = String.valueOf(position);
                        String textName = mExperience.get(position).getContent();
                        goText.putExtra("text",textName);
                        goText.putExtra("position", setposition);
                    }
                    startActivityForResult(goText,2);
                }
            });

            //显示大图
            viewHolder.sBigImage.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent showImage = new Intent(WriteExperienceActivity.this,ShowBigImageActivity.class);
                    String path = mWriteTextIMage.getUrl();
                    showImage.putExtra("imagePath", path);
                    startActivity(showImage);
                }
            });
            return convertView;
        }
    }

    private class ViewHolder
    {
        private TextView sTitle,sProductTitle, sMl,sPrice;
        private ImageView sBigImage,sTextDelete,sImageDelete,sProductDelete;
        private ImageView  sProImage;
        private RatingBar sRatingBar;
        private RelativeLayout sTextLayout,imageLayout,sProductLayout,sImageLayout;
    }

    @SuppressLint("SdCardPath")
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //相册
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                ArrayList<String> tDataList = (ArrayList<String>)bundle.getSerializable("dataList");
                if (tDataList != null) {
                    for (int i = 0; i < tDataList.size(); i++) {
                        YKUpDataExperience textImageProduct = new YKUpDataExperience();
                        textImageProduct.setUrl(tDataList.get(i));
                        textImageProduct.setmType("0");
                        mExperience.add(textImageProduct);
                    }
                    mGridImageAdapter.notifyDataSetChanged();
                }
            }
            //拍照
        }else if(requestCode == 1){
            if (mOutputFile != null) {
                if(resultCode == RESULT_OK){
                    if (YKUtil.hasSdcard()) {
                        startPhotoZoom(Uri.fromFile(mOutputFile));
                    } else {
                        Toast.makeText(WriteExperienceActivity.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_SHORT).show();
                    }
                }  
            }
        }else if(requestCode == 2) {
            // 文本
            if(data != null){
                String getText = data.getExtras().getString("text");
                String getPosition = data.getExtras().getString("position");
                if(resultCode == RESULT_OK){
                    if(" ".equals(getText) || getText.length() ==0){
                        Toast.makeText(WriteExperienceActivity.this, "没有添加任何内容吖", Toast.LENGTH_SHORT).show();
                    }else{
                        YKUpDataExperience EditText = new YKUpDataExperience();
                        EditText.setContent(getText);
                        if(TextUtils.isEmpty(getPosition)){
                            mExperience.add(EditText);
                        }else{
                            mExperience.set(Integer.parseInt(getPosition),EditText);
                        }
                        mGridImageAdapter.notifyDataSetChanged();
                    }
                }
            }
        }else if(requestCode == 3){
            // 妆品
            if(resultCode == RESULT_OK){
                YKProduct pro = (YKProduct) data.getSerializableExtra("product");
                String getPosition = data.getStringExtra("position");
                if(pro != null){
                    saveDataToFile(pro);
                    mHasDraft = true;
                    YKUpDataExperience addPro = new YKUpDataExperience();
                    addPro.setProdut_id(pro.getID());
                    mProduct = pro;
                    if(TextUtils.isEmpty(getPosition)){
                        mExperience.add(addPro); 
                    }else{
                        mExperience.set(Integer.valueOf(getPosition),addPro);
                    }
                }
                mGridImageAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == 4){
            // 相机回调裁剪
            String path = mOutputFile.getAbsolutePath();
            if(path != null){
//                int width = photoImage.getWidth();
//                int height = photoImage.getHeight();
                YKUpDataExperience Photo = new YKUpDataExperience();
                Photo.setUrl(path);
                Photo.setmType("1");
                Photo.setmWidth(750);
                Photo.setmHeight(750);
                mExperience.add(Photo);
            }else{
                Toast.makeText(WriteExperienceActivity.this, "拍照失败重新拍照", Toast.LENGTH_SHORT).show();
            }
        }

        //保存返回的数据
        saveDraftExperience(mExperience);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void initDialog(final String title) {
        AlertDialog.Builder builder = new Builder(WriteExperienceActivity.this);
        builder.setMessage("是否保存为草稿?");
        builder.setPositiveButton(R.string.setting_edit_send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titl = mDefaultTitle.getText().toString().trim();
                if(!TextUtils.isEmpty(titl)){
                    saveDraftTitle(titl);
                }
                if(mExperience.size() > 0){
                    saveDraftExperience(mExperience);
                }
                if (mProduct != null) {
                    saveDataToFile(mProduct);
                }
                dialog.dismiss();
                WriteExperienceActivity.this.finish();
            }
        });

        builder.setNeutralButton(R.string.dialog_delete_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFile();
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(R.string.dialog_delete_cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    //保存标题
    private  boolean saveDraftTitle(String text) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(text);
            data = baos.toByteArray();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }

        YKFile.save(CACHE_TITLE, data);
        return true;
    }

    //保存默认标题
    private String loadDefaultTitleDataFromFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_TITLE);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            String objectData = (String) ois
                    .readObject();
            return objectData;
        } catch (Exception e) {
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 调用系统裁剪图片的方法，可以选择参数为Uri对象  或者  Bitmap对象
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 750);
        intent.putExtra("outputY", 750);
        intent.putExtra("return-data", false);
        intent.putExtra("output", Uri.fromFile(mOutputFile)); // 专入目标文件
        startActivityForResult(intent, 4);
    }

    private void hideKeyBord() {
        // 先隐藏键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
        .hideSoftInputFromWindow(WriteExperienceActivity.this
                .getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGridImageAdapter.notifyDataSetChanged();
    }

    @Override    // 点击手机自带的返回按钮
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            String title = mDefaultTitle.getText().toString().trim();
            if("".equals(title) && mExperience.size() == 0 || mExperience == null){
                finish();
                deleteFile();
            }else{
                initDialog(title);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //保存数据
    private  boolean saveDraftExperience(ArrayList<YKUpDataExperience> experience) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(experience);
            data = baos.toByteArray();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }

        YKFile.save(CACHE_DRAFT, data);
        return true;
    }

    //加载数据
    private ArrayList<YKUpDataExperience> loadExperienceFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_DRAFT);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            ArrayList<YKUpDataExperience> objectData = (ArrayList<YKUpDataExperience>) ois
            .readObject();
            return objectData;
        } catch (Exception e) {
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    //加载妆品
    private void loadDraftProduct() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_PRODUCT);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            mProduct = (YKProduct) ois
                    .readObject();
            return;
        } catch (Exception e) {
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
    }
    //保存妆品
    public boolean saveDataToFile(YKProduct product) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(product);
            data = baos.toByteArray();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }

        YKFile.save(CACHE_PRODUCT, data);
        return true;
    }

    //加载保存的数据
    private void loadDraft() {
        loadDraftTitle();
        loadDraftExperience();
    }

    //首次进入页面显示showDraftDialog判断是否加载草稿
    private void showDraftDialog() {
        AlertDialog.Builder builder = new Builder(WriteExperienceActivity.this);
        builder.setMessage("肤君为你保留了一份草稿哦");
        builder.setPositiveButton("加载草稿", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mDraftExperience != null){
                    mExperience = mDraftExperience;
                    // load product data
                    loadDraftProduct();
                    mGridImageAdapter.notifyDataSetChanged();
                }
                if(!TextUtils.isEmpty(mDraftTitle)){
                    mDefaultTitle.setText(mDraftTitle);
                }
            }
        });
        builder.setNegativeButton("重新撰写", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFile();
                if(mExperience != null && mExperience.size() > 0 ){
                    mExperience.clear();
                }
                mDefaultTitle.setText("");
                mGridImageAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void backButton(String title){
        title = mDefaultTitle.getText().toString().trim();
        if("".equals(title) && mExperience.size() == 0 ){
            finish();
            deleteFile();
        }else{
            initDialog(title);
        }
    }

    private void productButton(){
        Intent intent = new Intent(WriteExperienceActivity.this,WriteSearchProductActivity.class);
        if(mProduct == null){
            intent.putExtra("position", "");
            startActivityForResult(intent, 3);
        }else{
            Toast.makeText(WriteExperienceActivity.this, "只能添加一款妆品", Toast.LENGTH_SHORT).show();
        }
    }

    private void publishButton(String title){
        if(YKCurrentUserManager.getInstance().isLogin()) {
            title = mDefaultTitle.getText().toString().trim();
            if(TextUtils.isEmpty(title)){
                Toast.makeText(WriteExperienceActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
            }else if(title.length() < 6){
                Toast.makeText(WriteExperienceActivity.this, "标题字数不能少于6个汉字", Toast.LENGTH_SHORT).show();
            }else if(title.length() > 25){
                Toast.makeText(WriteExperienceActivity.this, "标题字数不能多于25个字符", Toast.LENGTH_SHORT).show();
            }else{
                if(mExperience.size() == 0){
                    Toast.makeText(WriteExperienceActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        mCustomButterfly = CustomButterfly.show(this);
                    } catch (Exception e) {
                        mCustomButterfly = null;
                    }
                    mUploadImageCount = 0;
                    mTextNumber = 0;
                    int imageNumber = 0;
                    int textNumber = 0;
                    for (int i = 0; i < mExperience.size(); i++) {
                        if(mExperience.get(i).getUrl() != null){
                            ++imageNumber;
                        }
                        if(mExperience.get(i).getContent() != null){
                            ++textNumber;
                        }
                    }
                    mTextNumber            = textNumber;
                    mTotalUploadImageCount = imageNumber;
                    if(imageNumber > 0) {
                        for (int i = 0; i < mExperience.size(); i++) {
                            final YKUpDataExperience experienceItem = mExperience.get(i);
                            if(experienceItem.getUrl() != null){
                                String path = experienceItem.getUrl();
                                YKUploadImageManager.getInstance().uploadImages(path, new UploadImageCallback()
                                {
                                    @Override
                                    public void callback(YKResult result, String imageUrl)
                                    {
                                        ++mUploadImageCount;
                                        if(result.isSucceeded()){
                                            experienceItem.setmUploadURL(imageUrl);
                                        }

                                        if (mUploadImageCount >= mTotalUploadImageCount) {
                                            if(mTextNumber > 0){
                                                requestPublish(mExperience);
                                            }else{
                                                AppUtil.dismissDialogSafe(mCustomButterfly);
                                                Toast.makeText(WriteExperienceActivity.this, "文本不能为空", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        if(mTextNumber > 0){
                            requestPublish(mExperience);
                        }else{
                            AppUtil.dismissDialogSafe(mCustomButterfly);
                            Toast.makeText(WriteExperienceActivity.this, "文本不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }else{
            Intent goLogin = new Intent(WriteExperienceActivity.this,LoginActivity.class);
            startActivity(goLogin);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }
}

