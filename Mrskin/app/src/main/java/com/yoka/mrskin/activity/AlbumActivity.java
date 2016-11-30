/**  
 * @Title: AlbumActivity.java 
 * @Package com.example.adapter 
 * @Description: TODO() 
 * @author Derek 
 * @email renchun525@gmail.com
 * @date 2013-11-12 下午3:39:27 
 * @version V1.0  
 */ 
package com.yoka.mrskin.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.util.YKFile;


/** 
 * @ClassName: AlbumActivity 
 * @Description: TODO() 
 * @author Y H L
 */

public class AlbumActivity extends BaseActivity {

	public static final int SELECT_OK = 0;
	private GridView mGridView;
	private ArrayList<String> dataList = new ArrayList<String>();
	private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();
	private ArrayList<String> selectedDataList = new ArrayList<String>();
	private ArrayList<String> endSelectDataList = new ArrayList<String>();
	private String bucketId = "";
	private int mSizeNumber;
	private final String IMG_JPG = "image/jpg";
	private final String IMG_JPEG = "image/jpeg";
	private final String IMG_PNG = "image/png";
	private final String IMG_GIF = "image/gif";
	private ProgressBar progressBar;
	private AlbumGridViewAdapter mAlbumGridViewAdapter;
	private TextView mCenterText,mSuccessText;
	public static Handler mHandler;
	private LinearLayout mBack;
	public static String CACHE_PHOTO = "writephoto";
	public static String CACHE_MOREPHOTO = "writemorephoto";
	private static AlbumActivity singleton = null;
	private static Object lock = new Object();
	private DisplayImageOptions options;

	public static AlbumActivity getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new AlbumActivity();
			}
		}
		return singleton;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		getIntentInit();
		init();
		initListener(mSizeNumber);

	}
	@SuppressWarnings("unchecked")
	private void getIntentInit(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		selectedDataList =  (ArrayList<String>) bundle.getSerializable("dataList");
		bucketId = bundle.getString("bucketId");
		mSizeNumber = bundle.getInt("size");

	}

	private void init() {
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true).cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.resetViewBeforeLoading(true)
		.build();

		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		progressBar.setVisibility(View.GONE);
		mGridView = (GridView) findViewById(R.id.myGrid);
		mBack = (LinearLayout) findViewById(R.id.album_layout_back);
		mBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				finish();

			}
		});
		mSuccessText = (TextView) findViewById(R.id.album_enter);
		mAlbumGridViewAdapter = new AlbumGridViewAdapter(this,dataList,selectedDataList);
		mGridView.setAdapter(mAlbumGridViewAdapter);
		refreshData();
		mCenterText = (TextView) findViewById(R.id.album_center_text);

		initSelectImage(mSizeNumber);

	}

	private void initSelectImage(int size) {
		if (selectedDataList == null)
			return;
		for (final String path : selectedDataList) {
			ImageView imageView = (ImageView) LayoutInflater.from(AlbumActivity.this)
				.inflate(R.layout.choose_imageview,null, false);
			hashMap.put(path, imageView);
			ImageLoader.getInstance().displayImage("file://"+path, imageView, options);
//			 Glide.with(AlbumActivity.this).load(path).into( imageView);
			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					removePath(path,mSizeNumber);
					mAlbumGridViewAdapter.notifyDataSetChanged();
				}
			});
		}
		mCenterText.setText("已经选择(" + selectedDataList.size() +"/"+ String.valueOf(size)+")");
	}

	private void initListener(final int size) {

		mAlbumGridViewAdapter
		.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final ToggleButton toggleButton,
					int position, final String path, boolean isChecked) {

				if (selectedDataList.size() >= size) {
					toggleButton.setChecked(false);
					if (!removePath(path,mSizeNumber)) {
						Toast.makeText(AlbumActivity.this, "只能选择"+String.valueOf(size)+"张图片",
								Toast.LENGTH_SHORT).show();
					}
					return;
				}

				if (isChecked) {
					if (!hashMap.containsKey(path)) {
						ImageView imageView = (ImageView) LayoutInflater
								.from(AlbumActivity.this).inflate(
										R.layout.choose_imageview,
										null, false);

						hashMap.put(path, imageView);
						selectedDataList.add(path);
						endSelectDataList.add(path);



						//   Glide.with(AlbumActivity.this).load(path).into( imageView);

						ImageLoader.getInstance().displayImage("file://"+path, imageView, options);

						imageView
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								toggleButton.setChecked(false);
								removePath(path,mSizeNumber);
							}
						});
						mCenterText.setText("已经选择("+ selectedDataList.size() + "/" + String.valueOf(size)+")");
					}
				} else {
					removePath(path,mSizeNumber);
				}

			}
		});

		mSuccessText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("dataList", endSelectDataList);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	private boolean removePath(String path,int size) {
		if (hashMap.containsKey(path)) {
			hashMap.remove(path);
			removeOneData(selectedDataList, path);
			removeOneData(endSelectDataList, path);
			mCenterText.setText("已经选择(" + selectedDataList.size() + "/" + String.valueOf(size)+")");
			return true;
		} else {
			return false;
		}
	}

	private void removeOneData(ArrayList<String> arrayList, String s) {
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).equals(s)) {
				arrayList.remove(i);
				return;
			}
		}
	}

	private void refreshData() {

		new AsyncTask<Void, Void, ArrayList<String>>() {

			@Override
			protected void onPreExecute() {
				progressBar.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}

			@Override
			protected ArrayList<String> doInBackground(Void... params) {
				ArrayList<String> mPhotoDirInfoList = new ArrayList<String>();
				/*
				 * ArrayList<String> listDirlocal = listAlldir( new
				 * File(cameraDir)); ArrayList<String> listDiranjuke = new
				 * ArrayList<String>(); listDiranjuke.addAll(listDirlocal);
				 * 
				 * for (int i = 0; i < listDiranjuke.size(); i++){ listAllfile(
				 * new File( listDiranjuke.get(i) ),tmpList); }
				 */

				ContentResolver mResolver = AlbumActivity.this.getContentResolver();
				String[] IMAGE_PROJECTION = new String[] { ImageColumns.DATA };

				String selection = ImageColumns.BUCKET_ID + "= ?  AND "
						+ ImageColumns.MIME_TYPE + " IN (?,?,?,?)";

				String[] selectionArgs = new String[] { bucketId, IMG_JPG,
						IMG_JPEG, IMG_PNG,IMG_GIF};

				Cursor cursor = mResolver.query(
						Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
						selection, selectionArgs, null);
				if (null != cursor) {
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							mPhotoDirInfoList.add(cursor.getString(0));
						}
					}
					cursor.close();
				}
				return mPhotoDirInfoList;
			}

			protected void onPostExecute(ArrayList<String> tmpList) {

				if (AlbumActivity.this == null
						|| AlbumActivity.this.isFinishing()) {
					return;
				}
				progressBar.setVisibility(View.GONE);
				dataList.clear();
				dataList.addAll(tmpList);
				mAlbumGridViewAdapter.notifyDataSetChanged();
				return;
			};
		}.execute();

	}

	//    @Override
	//    public void onBackPressed() {
	//        Intent intent = new Intent();
	//        Bundle bundle = new Bundle();
	//        // intent.putArrayListExtra("dataList", dataList);
	//        bundle.putSerializable("dataList", selectedDataList);
	//        intent.putExtras(bundle);
	//        setResult(RESULT_OK, intent);
	//        finish();
	//        // super.onBackPressed();
	//    }

	/*
	 * @Override public void finish() { // TODO Auto-generated method stub
	 * super.finish(); //
	 * ImageManager2.from(AlbumActivity.this).recycle(dataList); }
	 */

	public class AlbumGridViewAdapter extends BaseAdapter implements OnClickListener{

		private Context mContext;
		private ArrayList<String> dataList;
		private ArrayList<String> selectedDataList;
		private DisplayMetrics dm;
		private ViewHolder viewHolder = null;
		private String path = null;
		private OnItemClickListener mOnItemClickListener;

		public AlbumGridViewAdapter(Context c, ArrayList<String> dataList,
				ArrayList<String> selectedDataList) {

			mContext = c;
			this.dataList = dataList;
			this.selectedDataList = selectedDataList;
			dm = new DisplayMetrics();
			((Activity) mContext).getWindowManager().getDefaultDisplay()
			.getMetrics(dm);

		}

		@Override
		public int getCount() {
			if(dataList != null){
				return dataList.size();
			}
			return 0; 
		}

		@Override
		public Object getItem(int position) {
			if(dataList.get(position) != null){
				return dataList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.select_imageview, parent, false);
				viewHolder.imageView = (ImageView) convertView
						.findViewById(R.id.image_view);
				viewHolder.toggleButton = (ToggleButton) convertView
						.findViewById(R.id.toggle_button);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (dataList != null && dataList.size() > position){
				path = dataList.get(position);
				Log.e("path==>", ""+path);
				ImageLoader.getInstance().displayImage("file://"+path, viewHolder.imageView, options);

				//   Glide.with(mContext).load(path).into( viewHolder.imageView);
			}
			viewHolder.toggleButton.setTag(position);
			viewHolder.toggleButton.setOnClickListener(this);
			if (isInSelectedDataList(path)) {
				viewHolder.toggleButton.setChecked(true);
			} else {
				viewHolder.toggleButton.setChecked(false);
			}

			return convertView;
		}

		private boolean isInSelectedDataList(String selectedString) {
			for (int i = 0; i < selectedDataList.size(); i++) {
				if (selectedDataList.get(i).equals(selectedString)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void onClick(View view) {
			if (view instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) view;
				int position = (Integer) toggleButton.getTag();
				if (dataList != null && mOnItemClickListener != null
						&& position < dataList.size()) {
					mOnItemClickListener.onItemClick(toggleButton, position,
							dataList.get(position), toggleButton.isChecked());
				}
			}
		}

		public void setOnItemClickListener(OnItemClickListener l) {
			mOnItemClickListener = l;
		}

	}

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public ToggleButton toggleButton;
	}

	public interface OnItemClickListener {
		public void onItemClick(ToggleButton view, int position, String path,
				boolean isChecked);
	}
	//保存相册
	public ArrayList<String> getPhotoData() {
		return loadDataFromFile();
	}

	public  boolean saveDataToFile(ArrayList<String> text) {
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

		YKFile.save(CACHE_PHOTO, data);
		return true;
	}

	private ArrayList<String> loadDataFromFile() {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		byte[] data = YKFile.read(CACHE_PHOTO);
		try {
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			@SuppressWarnings("unchecked")
			ArrayList<String> objectData = (ArrayList<String>) ois
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

	//保存拍照
	public ArrayList<String> getMorePhotoData() {
		return loadMorePhotoDataFromFile();
	}

	public  boolean saveMorePhotoOreDataToFile(ArrayList<String> text) {
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

		YKFile.save(CACHE_MOREPHOTO, data);
		return true;
	}

	private ArrayList<String> loadMorePhotoDataFromFile() {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		byte[] data = YKFile.read(CACHE_MOREPHOTO);
		try {
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			@SuppressWarnings("unchecked")
			ArrayList<String> objectData = (ArrayList<String>) ois
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
