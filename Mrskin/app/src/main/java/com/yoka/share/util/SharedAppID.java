package com.yoka.share.util;

public class SharedAppID
{
    // public static final String SHARE_WX_APP_ID = "wx44693ce99b5f2e3c";
    // public static final String SHARE_WX_APP_SECRET =
    // "5151b746a7e34d93f724bbb607a0a569";
    // public static final String SHARE_QQ_APP_ID = "1103753960";
    // public static final String SHARE_QQ_APP_SECRET = "Y0M8oAGlpr9sdqOb";

    public static final String SHARE_WX_APP_ID = "wx2fa9e775ae670330";
    public static final String SHARE_WX_APP_SECRET = "301c95689ae5323ca6448d6b02e3dcd8";
    public static final String SHARE_QQ_APP_ID = "1103753960";
    public static final String SHARE_QQ_APP_SECRET = "Y0M8oAGlpr9sdqOb";

    // 新浪微博分享3080290044 2420382721
    public static final String APP_KEY = "2420382721";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    private String shareWxAppId;
    private String shareWxAppSecret;
    private String shareQQAppID;
    private String shareQQAppSecret;
    private String shareSinaAppKey;

    public SharedAppID(String shareWxAppId, String shareWxAppSecret,
            String shareQQAppID, String shareQQAppSecret, String shareSinaAppKey)
    {
        super();
        this.shareWxAppId = shareWxAppId;
        this.shareWxAppSecret = shareWxAppSecret;
        this.shareQQAppID = shareQQAppID;
        this.shareQQAppSecret = shareQQAppSecret;
        this.shareSinaAppKey = shareSinaAppKey;
    }

    public String getShareWxAppId() {
        return shareWxAppId;
    }

    public void setShareWxAppId(String shareWxAppId) {
        this.shareWxAppId = shareWxAppId;
    }

    public String getShareWxAppSecret() {
        return shareWxAppSecret;
    }

    public void setShareWxAppSecret(String shareWxAppSecret) {
        this.shareWxAppSecret = shareWxAppSecret;
    }

    public String getShareQQAppID() {
        return shareQQAppID;
    }

    public void setShareQQAppID(String shareQQAppID) {
        this.shareQQAppID = shareQQAppID;
    }

    public String getShareQQAppSecret() {
        return shareQQAppSecret;
    }

    public void setShareQQAppSecret(String shareQQAppSecret) {
        this.shareQQAppSecret = shareQQAppSecret;
    }

    public String getShareSinaAppKey() {
        return shareSinaAppKey;
    }

    public void setShareSinaAppKey(String shareSinaAppKey) {
        this.shareSinaAppKey = shareSinaAppKey;
    }
}