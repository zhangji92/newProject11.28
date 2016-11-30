/**  
 * @Title: ImageList.java 
 * @Package com.example.multiimagechooser 
 * @Description: TODO() 
 * @author Derek 
 * @email renchun525@gmail.com
 * @date 2013-11-12 下午3:11:17 
 * @version V1.0  
 */ 
package com.yoka.mrskin.activity;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.data.YKPhotoDirInfo;


/** 
 * @ClassName: ImageList 
 * @Description: TODO() 
 * @author Y H L
 */

public class ImageListActivity extends BaseActivity{

    private ArrayList<String> selectedDataList = new ArrayList<String>();
    private ListView mListView;
    private ImageListAdapter mAdapter;
    private ArrayList<YKPhotoDirInfo> mDirInfos;
    private final String IMG_JPG="image/jpg";
    private final String IMG_JPEG="image/jpeg";
    private final String IMG_PNG="image/png";
    private final String IMG_GIF="image/gif";
    private LinearLayout mBack;
    private int sizeNumber;

    /**
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iamgelist_layout);
        InitgetIntent();
        init();

    }
    @SuppressWarnings("unchecked")
    private void InitgetIntent(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedDataList = (ArrayList<String>)bundle.getSerializable("dataList");
        sizeNumber = bundle.getInt("size");
    }
    private void init(){

        mBack = (LinearLayout) findViewById(R.id.write_imagelist_layout_back);
        mListView= (ListView) findViewById(R.id.imageListView);

        mDirInfos = getImageDir(this);

        mAdapter=new ImageListAdapter(this, mDirInfos);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                YKPhotoDirInfo photoDirInfo = mDirInfos.get(position);
                Intent intent = new Intent(ImageListActivity.this,AlbumActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataList",selectedDataList);
                bundle.putSerializable("size",sizeNumber);
                bundle.putString("bucketId", photoDirInfo.getDirId());
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

        mBack.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    private synchronized ArrayList<YKPhotoDirInfo> getImageDir(Context context) {

        ArrayList<YKPhotoDirInfo> mPhotoDirInfos = null ;
        YKPhotoDirInfo mDirInfo = null;
        ContentResolver mResolver = context.getContentResolver();
        String[] IMAGE_PROJECTION=new String[]{ImageColumns.BUCKET_ID,ImageColumns.BUCKET_DISPLAY_NAME,ImageColumns.DATA,"COUNT(*) AS "+ImageColumns.DATA};

        String selection=" 1=1 AND "+ImageColumns.MIME_TYPE+" IN (?,?,?,?)) GROUP BY ("+Images.ImageColumns.BUCKET_ID+") ORDER BY ("+Images.ImageColumns.BUCKET_DISPLAY_NAME;

        String[] selectionArgs=new String[] {IMG_JPG,IMG_JPEG,IMG_PNG,IMG_GIF};
        //String selection=ImageColumns.MIME_TYPE+" IN ("+IMG_JPG+","+IMG_PNG+")) GROUP BY ("+ImageColumns.BUCKET_ID+") ORDER BY ("+Images.ImageColumns.BUCKET_DISPLAY_NAME;

        Cursor cursor = mResolver.query(Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION,selection,selectionArgs,null);
        if(null!=cursor){
            if(cursor.getCount()>0){
                mPhotoDirInfos = new ArrayList<YKPhotoDirInfo>();
                while(cursor.moveToNext()){
                    mDirInfo=new YKPhotoDirInfo();
                    mDirInfo.setDirId(cursor.getString(0));
                    mDirInfo.setDirName(cursor.getString(1));
                    mDirInfo.setFirstPicPath(cursor.getString(2));
                    //Log.e("PicPath==>", cursor.getString(2));
                    mDirInfo.setPicCount(cursor.getInt(3));
                    mDirInfo.setUserOtherPicSoft(false);
                    mPhotoDirInfos.add(mDirInfo);
                }
            }
            cursor.close();
        }
        return mPhotoDirInfos;
    }

    public static class ImageListAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private Context mContext;
        private ArrayList<YKPhotoDirInfo> listDir;
        private DisplayImageOptions options;
        public ImageListAdapter(Context context,ArrayList<YKPhotoDirInfo> objects) {
            super();
            this.mContext=context;
            listDir=objects;
            inflater=LayoutInflater.from(mContext);
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
    				.resetViewBeforeLoading(true).considerExifParams(true)
    				.build();
        }

        /**
         * @return
         * @see android.widget.ArrayAdapter#getCount()
         */
        @Override
        public int getCount() {
            return listDir!=null?listDir.size():0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * @param position
         * @param convertView
         * @param parent
         * @return
         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            YKPhotoDirInfo localPhotoDirInfo = listDir.get(position);
            ViewHolder viewHolder;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView = inflater.inflate(R.layout.imagelist_item, parent, false);
                viewHolder.imageView=(ImageView)convertView.findViewById(R.id.iamge_icon);
                viewHolder.textView=(TextView)convertView.findViewById(R.id.iamge_name);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder) convertView.getTag();
            }

            viewHolder.textView.setText(localPhotoDirInfo.getDirName()+"("+localPhotoDirInfo.getPicCount()+")");
            String path = localPhotoDirInfo.getFirstPicPath();
         //   Glide.with(mContext).asBitmap().load(path).into(viewHolder.imageView);
            ImageLoader.getInstance().displayImage("file://"+path, viewHolder.imageView, options);
            return convertView;
        }

        /**
         * 存放列表项控件句柄
         */
        private class ViewHolder {
            public ImageView imageView;
            public TextView textView;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                @SuppressWarnings("unchecked")
                ArrayList<String> tDataList = (ArrayList<String>)bundle.getSerializable("dataList");
                if (tDataList != null) {
                    selectedDataList.clear();
                    selectedDataList.addAll(tDataList);
                }
                Intent intent = new Intent();
              Bundle bundleto = new Bundle();
              bundleto.putSerializable("dataList",selectedDataList);
              intent.putExtras(bundle);
              setResult(RESULT_OK, intent);
              finish();
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("dataList",selectedDataList);
//        intent.putExtras(bundle);
//        setResult(RESULT_OK, intent);
//        finish();
//        //super.onBackPressed();
//    }
}
