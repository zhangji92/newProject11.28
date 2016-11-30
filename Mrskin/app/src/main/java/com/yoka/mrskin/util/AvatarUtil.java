/**
 * 
 */
package com.yoka.mrskin.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

/**
 * 头像工具类
 * @author zlz
 * @date 2016年6月13日
 */
public class AvatarUtil {
	private static final String TAG = AvatarUtil.class.getSimpleName();
	private static AvatarUtil avatarUtil = null;
	private Context mContext;
	//	private Handler handler;
	private AvatarUtil(Context context)
	{
		this.mContext = context;
		//		this.handler = handler;
	}
	public static AvatarUtil getInstance(Context context) {
		if (avatarUtil == null) {
			avatarUtil = new AvatarUtil(context);
		}
		return avatarUtil;
	}

	public void AsyncHttpFileDown(String path,final Handler handler) {
		if (TextUtils.isEmpty(path))
			return;
		//	path =http://ucenter.yoka.com/data/avatar/003/35/88/08_avatar_middle.jpg


		AsyncHttpClient client = new AsyncHttpClient();
		client.get(path, new FileAsyncHttpResponseHandler(getFile(path)) {
			@Override
			public void onSuccess(int statusCode, Header[] hander, File file) {
				if (statusCode == 200) {
					Log.d(TAG, file.getAbsolutePath());
					Message msg = new Message();
					msg.what = 201;
					msg.obj = file.getAbsolutePath();
					handler.sendMessage(msg);
					//存储头像路径
//					Glide.with().load(file.getAbsolutePath()).into(mHeadImg);
					saveToSp(file.getAbsolutePath());
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] hander,
					Throwable throwable, File file) {
				throwable.printStackTrace();
			}

		});
	}

	public File getFile(String path) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间   
		File file = new File(mContext.getFilesDir(), "yk_avatar"+formatter.format(curDate)+".jpg");
		return file;
	}

	private void saveToSp(String path){
		// 获取SharedPreferences对象  
		SharedPreferences sp = mContext.getSharedPreferences("ykavatar", Activity.MODE_PRIVATE);  
		Editor editor = sp.edit();
		editor.putString("avatar", path);  
		editor.commit();  

	}
	public String getAvatarPath(){
		SharedPreferences sp = mContext.getSharedPreferences("ykavatar", Activity.MODE_PRIVATE);
		Log.d(TAG, sp.getString("avatar", ""));
		return sp.getString("avatar", "");

	}


	/**  
	 * <br>功能简述:4.4及以上获取图片的方法 
	 * <br>功能详细描述: 
	 * <br>注意: 
	 * @param context 
	 * @param uri 
	 * @return 
	 */  
	@TargetApi(Build.VERSION_CODES.KITKAT)  
	public static String getPath(final Context context, final Uri uri) {  

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  

		// DocumentProvider  
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
			// ExternalStorageProvider  
			if (isExternalStorageDocument(uri)) {  
				final String docId = DocumentsContract.getDocumentId(uri);  
				final String[] split = docId.split(":");  
				final String type = split[0];  

				if ("primary".equalsIgnoreCase(type)) {  
					return Environment.getExternalStorageDirectory() + "/" + split[1];  
				}  
			}  
			// DownloadsProvider  
			else if (isDownloadsDocument(uri)) {  

				final String id = DocumentsContract.getDocumentId(uri);  
				final Uri contentUri = ContentUris.withAppendedId(  
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  

				return getDataColumn(context, contentUri, null, null);  
			}  
			// MediaProvider  
			else if (isMediaDocument(uri)) {  
				final String docId = DocumentsContract.getDocumentId(uri);  
				final String[] split = docId.split(":");  
				final String type = split[0];  

				Uri contentUri = null;  
				if ("image".equals(type)) {  
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
				} else if ("video".equals(type)) {  
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
				} else if ("audio".equals(type)) {  
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
				}  

				final String selection = "_id=?";  
				final String[] selectionArgs = new String[] { split[1] };  

				return getDataColumn(context, contentUri, selection, selectionArgs);  
			}  
		}  
		// MediaStore (and general)  
		else if ("content".equalsIgnoreCase(uri.getScheme())) {  

			// Return the remote address  
			if (isGooglePhotosUri(uri))  
				return uri.getLastPathSegment();  

			return getDataColumn(context, uri, null, null);  
		}  
		// File  
		else if ("file".equalsIgnoreCase(uri.getScheme())) {  
			return uri.getPath();  
		}  

		return null;  
	}  


	public static String getDataColumn(Context context, Uri uri, String selection,  
			String[] selectionArgs) {  

		Cursor cursor = null;  
		final String column = "_data";  
		final String[] projection = { column };  

		try {  
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
					null);  
			if (cursor != null && cursor.moveToFirst()) {  
				final int index = cursor.getColumnIndexOrThrow(column);  
				return cursor.getString(index);  
			}  
		} finally {  
			if (cursor != null)  
				cursor.close();  
		}  
		return null;  
	}  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
		return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  

	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  

	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
		return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  

	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}

}
