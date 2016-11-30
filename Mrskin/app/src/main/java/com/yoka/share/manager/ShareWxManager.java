package com.yoka.share.manager;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yoka.mrskin.R;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKShareRewardManager;
import com.yoka.mrskin.model.managers.YKShareRewardManager.ShareRewardCallback;
import com.yoka.mrskin.util.CustomButterfly;

public class ShareWxManager
{
    private Activity mContext;
    private static ShareWxManager mShareWxManager;
    
    public static ShareWxManager getInstance()
    {
        if (mShareWxManager == null) {
            mShareWxManager = new ShareWxManager();
        }
        return mShareWxManager;
    }

    public void init(Activity context)
    {
        mContext = context;
      
    }

    public void shareWx(String content, String title, String targerUrl,String imageUrl,Activity activity)
    {

        UMImage uImage;
        if(TextUtils.isEmpty(imageUrl)){
        	uImage = new UMImage(mContext,
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher));
        }else{
        	uImage=new UMImage(mContext, imageUrl);
        }
        Config.dialog = CustomButterfly.getInstance(activity).show(activity);
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).withTitle(title)
        .withText(content)
        .withMedia(uImage)
        //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
//         .withText("hello umeng")
         .withTargetUrl(targerUrl)
        .share();
    }

    public void shareWxCircle(String content, String title, String targerUrl,String image)
    {
        // 设置微信朋友圈分享内容

        
        UMImage uImage;
        if(TextUtils.isEmpty(image)){
        	uImage = new UMImage(mContext,
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher));
        
        }else{
        	uImage=new UMImage(mContext, image);
        }
        new ShareAction(mContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener).withTitle(title)
        .withText(content)
        .withMedia(uImage)
        //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
//         .withText("hello umeng")
         .withTargetUrl(targerUrl)
        .share();
    }

  
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
          
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(mContext,"微信" + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                if(YKCurrentUserManager.getInstance().isLogin()){
                    String auth = YKCurrentUserManager.getInstance().getUser().getToken();
                    YKShareRewardManager.getInstance().postShareReward(auth, new ShareRewardCallback()
                    {
                        
                        @Override
                        public void callback(YKResult result)
                        {
                            // TODO Auto-generated method stub
                            
                        }
                    });
                }
                Toast.makeText(mContext, "微信" + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext,"微信" + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
          //  Toast.makeText(mContext,"微信"+ " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
