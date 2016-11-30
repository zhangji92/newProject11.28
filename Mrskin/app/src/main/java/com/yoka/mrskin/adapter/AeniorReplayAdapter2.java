package com.yoka.mrskin.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.AeniorReplayActivity2;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.BitmapUtilImage;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.YKDeviceInfo;
import com.yoka.mrskin.util.YKUtil;

public class AeniorReplayAdapter2 extends BaseAdapter
{
	private DisplayImageOptions options ;
	private ViewHolder viewHolder;
	private ArrayList<Bitmap> mapList;
	private ArrayList<String> mList = new ArrayList<>();
	private Context mContext;
	private CustomButterfly butterfly = null;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 200:
				AppUtil.dismissDialogSafe(butterfly);
				notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};

	private int screenWidth;

	public AeniorReplayAdapter2(Context mContext,ArrayList<Bitmap> mImageUrl) {
		super();
		this.mapList = mImageUrl;
		this.mContext = mContext;
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.list_default_bg)
				.showImageForEmptyUri(R.drawable.list_default_bg).showImageOnFail(R.drawable.list_default_bg)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = (wm.getDefaultDisplay().getWidth()) - YKUtil.dip2px(mContext, 20);
	}
	public void refresh(final ArrayList<String> _list){
		mList = _list;
		butterfly = CustomButterfly.show(mContext);
		mapList.clear();
		new Thread(){
			public void run() {
				for (String path : _list) {
					mapList.add(getLocationBitmap(path, 6));
				}
				mHandler.sendEmptyMessage(200);

			};
		}.start();


		//		

	}

	public void update(final String path){
		mList.add(path);
		butterfly = CustomButterfly.show(mContext);
		new Thread(){
			public void run() {

				mapList.add(getLocationBitmap(path,2));
				mHandler.sendEmptyMessage(200);
			};
		}.start();


	}
	public ArrayList<String> getList(){
		return mList;
	}


	@Override
	public int getCount() {
		if (mapList != null) {
			return mapList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mapList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.aenior_reply_item, null);

			viewHolder.mImage = (ImageView) convertView.findViewById(R.id.aenior_reply_image);
			viewHolder.mImageLayout = (RelativeLayout) convertView.findViewById(R.id.aenior_reply_layout);
			viewHolder.mImageDelete = (ImageView) convertView.findViewById(R.id.aenior_reply_deleteimage);

			convertView.setTag(viewHolder);

		} else {

			viewHolder = (ViewHolder) convertView.getTag();
		}


		int tmpHeight = 0;
		tmpHeight = screenWidth * 750 / 750;
		viewHolder.mImageLayout.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, tmpHeight));

		//		String url = mList.get(position);
		viewHolder.mImage.setImageBitmap((Bitmap)getItem(position));
		//		String url = "file://"+mList.get(position);
		//		ImageLoader.getInstance().displayImage((String)parent.getTag(), viewHolder.mImage,options);
		viewHolder.mImageDelete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setMessage("确认删除图片?");
				builder.setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mapList.remove(position);
						mList.remove(position);
						notifyDataSetChanged();

						//ImageList.size > 0 ? 可提交（红色） ：不可提交（黑色）
						if(mapList.size() > 0){
							((AeniorReplayActivity2)mContext).mCommit.setTextColor(mContext.getResources().getColor(R.color.red));
							((AeniorReplayActivity2)mContext).mCommit.setEnabled(true);
						}else{
							if(((AeniorReplayActivity2)mContext).canCommit()){//监听文字
								((AeniorReplayActivity2)mContext).mCommit.setTextColor(mContext.getResources().getColor(R.color.red));
								((AeniorReplayActivity2)mContext).mCommit.setEnabled(true);
							}else{

								((AeniorReplayActivity2)mContext).mCommit.setTextColor(mContext.getResources().getColor(R.color.location_city_gps));
								((AeniorReplayActivity2)mContext).mCommit.setEnabled(false);
							}
						}
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
		return convertView;
	}

	class ViewHolder
	{
		private ImageView mImage,mImageDelete;
		private RelativeLayout mImageLayout;
	}
	public Bitmap getLocationBitmap(String url,int size){
		size = BitmapUtilImage.getZoomSize(url);
		Bitmap bitmap = null;
		//GT-N7102三星
		/*设备型号*/
		String phonemodel = YKDeviceInfo.getDeviceModel();
		try {

			FileInputStream fis = new FileInputStream(url);
			BitmapFactory.Options options = new  BitmapFactory.Options();

			options.inJustDecodeBounds =  false;
			/*if(!phonemodel.equals("GT-N7102") && !url.contains("Camera")){
				options.inPreferredConfig = Bitmap.Config.ARGB_4444;
			}*/

			options.inSampleSize =  size;   // width，hight设为原来的十分一
			int orientation = BitmapUtilImage.readPictureDegree(url);//获取旋转角度

			bitmap =  BitmapFactory.decodeStream(fis, null,  options);
			if(Math.abs(orientation) > 0){
				bitmap =  BitmapUtilImage.rotaingImageView(orientation, bitmap);//旋转图片
			}
			return bitmap;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;

	}





}

