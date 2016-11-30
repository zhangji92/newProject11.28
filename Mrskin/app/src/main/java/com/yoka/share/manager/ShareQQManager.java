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
import com.umeng.socialize.utils.Log;
import com.yoka.mrskin.R;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKShareRewardManager;
import com.yoka.mrskin.model.managers.YKShareRewardManager.ShareRewardCallback;
import com.yoka.mrskin.util.CustomButterfly;

public class ShareQQManager
{
    private Activity mContext;
    private static ShareQQManager mShareQQManager;

    public static ShareQQManager getInstance()
    {
	if (mShareQQManager == null) {
	    mShareQQManager = new ShareQQManager();
	}
	return mShareQQManager;
    }
    public void init(Activity context)
    {
	mContext = context;

    }

    public void shareQQ(String title, String content, String targerUrl,String imageUrl,Activity activity)
    {

	UMImage uImage;
	if(TextUtils.isEmpty(imageUrl)){
	    uImage = new UMImage(mContext,
		    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher));
	}else{
	    uImage=new UMImage(mContext, imageUrl);
	}
	Config.dialog = CustomButterfly.getInstance(activity).show(activity);
	new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener).withTitle(title)
	.withText(content)
	.withMedia(uImage)
	.withTargetUrl(targerUrl)
	.share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
	@Override
	public void onResult(SHARE_MEDIA platform) {
	    Log.d("plat","platform"+platform);
	    if(platform.name().equals("QQ")){
		//Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
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
	    }

	}

	@Override
	public void onError(SHARE_MEDIA platform, Throwable t) {
	   // Toast.makeText(mContext,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
	    if(t!=null){
		Log.d("throw","throw:"+t.getMessage());
	    }
	}

	@Override
	public void onCancel(SHARE_MEDIA platform) {
	   // Toast.makeText(mContext,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
	}
    };
}
