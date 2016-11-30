package com.allactivity.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allactivity.R;
import com.allactivity.util.MyApplication;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by 张继 on 2016/11/22.
 * volley框架加载图片
 */

public class VolleyImageLoader extends Fragment {
    private ImageView mImageView;
    private NetworkImageView mNetworkImageView;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.volley_image_loader,container,false);
        initViews(view);
        initData();
        return view;

    }

    private void initData() {
        String url="http://www.baidu.com/img/bdlogo.png";
        ImageLoader loader=new ImageLoader(MyApplication.getQueue(),new BitmapCache());
        mNetworkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        mNetworkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        mNetworkImageView.setImageUrl(url,loader);

        //图片缓存
        //ImageLoader loader=new ImageLoader(MyApplication.getQueue(),new BitmapCache());
        //ImageLoader.ImageListener loaderListener=ImageLoader.getImageListener(mImageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        //loader.get(url,loaderListener);

//        ImageRequest request=new ImageRequest(url, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                mImageView.setImageBitmap(bitmap);
//            }
//        }, 200, 200, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                mImageView.setImageResource(R.mipmap.ic_launcher);
//                Log.e("abc","VolleyError"+volleyError.toString());
//            }
//        });
//        request.setTag("imgGet");
//        MyApplication.getQueue().add(request);
    }

    private void initViews(View view) {
        mImageView= (ImageView) view.findViewById(R.id.volley_img);
        mNetworkImageView= (NetworkImageView) view.findViewById(R.id.netWorkImageView);
    }
}
