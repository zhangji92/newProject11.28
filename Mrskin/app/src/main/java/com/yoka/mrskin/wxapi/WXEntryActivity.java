package com.yoka.mrskin.wxapi;

import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity
{
}
//package com.yoka.mrskin.wxapi;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.tencent.mm.sdk.constants.ConstantsAPI;
//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.yoka.mrskin.R;
//import com.yoka.mrskin.login.AuthorUser;
//import com.yoka.mrskin.login.UmSharedAppID;
//import com.yoka.mrskin.login.WeiXinInfoCallBack;
//import com.yoka.mrskin.login.YKUser;
//import com.yoka.mrskin.login.YkWeiXinLoginManager;
//import com.yoka.mrskin.model.base.YKResult;
//import com.yoka.mrskin.model.managers.YKLoginCallback;
//import com.yoka.mrskin.model.managers.YKLoginManager;
//
//public class WXEntryActivity extends Activity implements IWXAPIEventHandler
//{
//    private static final String TAG = "WXEntryActivity";
//    // IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;
//    @SuppressWarnings("unused")
//    private static final String ACTION_NAME = "com.yoka.login.success";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // 通过WXAPIFactory工厂，获取IWXAPI的实例
//        // api = WXAPIFactory.createWXAPI(this, UmSharedAppID.SHARE_WX_APP_ID,
//        // true);
//        api = WXAPIFactory.createWXAPI(this, UmSharedAppID.SHARE_WX_APP_ID,
//                false);
//        api.handleIntent(getIntent(), this);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        setIntent(intent);
//        api.handleIntent(intent, this);
//    }
//
//    // 微信发送请求到第三方应用时，会回调到该方法
//    @Override
//    public void onReq(BaseReq req) {
//        // switch (req.getType()) {
//        // case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//        // goToGetMsg();
//        // break;
//        // case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//        // goToShowMsg((ShowMessageFromWX.Req) req);
//        // break;
//        // default:
//        // break;
//        // }
//    }
//
//    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
//    @Override
//    public void onResp(BaseResp resp) {
//        int result = 0;
//        Log.d(TAG, "on resp code = " + resp.errCode);
//
//        Bundle bundle = new Bundle();
//        switch (resp.errCode) {
//        case BaseResp.ErrCode.ERR_OK:
//
//            if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//
//                resp.toBundle(bundle);
//                Resp sp = new Resp(bundle);
//                final String code = sp.code;
//                if (code != null) {
//
//                    Log.e("----------code", code);
//                    YkWeiXinLoginManager.getInstance().requestGetWeixinInfo(
//                            code, new WeiXinInfoCallBack() {
//
//                                @Override
//                                public void InfoCallBackSuccess(AuthorUser user) {
//                                    // TODO Auto-generated method stub
//                                    Log.e("---------", user.toString());
//
//                                    YKLoginManager.getInstance().requestLogin(
//                                            WXEntryActivity.this, user,
//                                            new YKLoginCallback() {
//
//                                                @Override
//                                                public void callback(
//                                                        YKResult result,
//                                                        YKUser user) {
//                                                    // TODO Auto-generated
//                                                    // method stub
//                                                    Toast.makeText(
//                                                            WXEntryActivity.this,
//                                                            R.string.login_sucess,
//                                                            Toast.LENGTH_LONG)
//                                                            .show();
//                                                    sendLoginData(user);
//                                                }
//
//                                                @Override
//                                                public void callbackFaile(
//                                                        String message) {
//                                                    // TODO Auto-generated
//                                                    // method stub
//                                                    Toast.makeText(
//                                                            WXEntryActivity.this,
//                                                            R.string.login_faile,
//                                                            Toast.LENGTH_LONG)
//                                                            .show();
//                                                }
//
//                                            });
//
//                                }
//
//                                @Override
//                                public void CallBackFaile() {
//                                    // TODO Auto-generated method stub
//                                    Log.e("----------info", "faile----");
//                                    Toast.makeText(WXEntryActivity.this,
//                                            R.string.login_faile,
//                                            Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                } else {
//                    // 失败
//                    Toast.makeText(WXEntryActivity.this, R.string.login_faile,
//                            Toast.LENGTH_LONG).show();
//                }
//            } else {
//                result = R.string.share_successed;
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//            }
//
//            break;
//        case BaseResp.ErrCode.ERR_AUTH_DENIED:
//            Log.e("-----", "result = 认证失败");
//            if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//                result = R.string.auth_error;
//            } else {
//                result = R.string.login_faile;
//            }
//            Toast.makeText(this, resp.errCode + "", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//            break;
//        case BaseResp.ErrCode.ERR_USER_CANCEL:
//            Log.e("------resp.getType()---", resp.getType() + "-");
//            if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//                result = R.string.auth_cancle;
//            } else {
//                result = R.string.share_cancle;
//            }
//            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//            break;
//        default:
//            if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//                result = R.string.auth_error;
//            } else {
//                result = R.string.login_faile;
//            }
//            Log.e("-----", "result = 未知");
//            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//            break;
//        }
//
//        finish();
//    }
//
//    private void sendLoginData(YKUser user) {
//        System.out.println(user.toString());
//    }
//
//    public interface onWxShareSuccessedListener
//    {
//        public void onShareSucceed();
//    }
//
// }
