package com.jrd.loan.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jrd.loan.R;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.LoginVCodeBean;
import com.jrd.loan.bean.PersonalInfoBean;
import com.jrd.loan.bean.RegisterBean;
import com.jrd.loan.net.framework.HttpTask;
import com.jrd.loan.net.framework.OnHttpTaskListener;

public class UserApi {

    /**
     * 获取验证码
     *
     * @param phone_num
     * @param checkUser
     */
    public static void GetVerifyCode(Context context, String phone_num, boolean checkUser, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.GETMOBILECODE, HttpTask.METHOD_POST);
        httpTask.putParam("mobileNumber", phone_num);
        httpTask.putParam("checkUser", checkUser);
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 获取登录验证码
     *
     * @param deviceid
     */
    public static void GetLoginVCode(Context context, String deviceid, OnHttpTaskListener<LoginVCodeBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.GETLOGINVCODE, HttpTask.METHOD_POST);
        httpTask.putParam("deviceid", deviceid);
        httpTask.runTask(LoginVCodeBean.class, httpTaskListener);
    }

    /**
     * 重置支付密码第一步调用
     *
     * @param userId
     * @param mobileNumber
     * @param identifyingCode
     */
    public static void CheckVCode(Context context, String userId, String mobileNumber, String identifyingCode, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.VERIFYMOBILECODE, HttpTask.METHOD_POST);
        httpTask.putParam("userId", userId);
        httpTask.putParam("mobileNumber", mobileNumber);
        httpTask.putParam("identifyingCode", identifyingCode);
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 登录
     *
     * @param userAccount
     * @param password
     * @param identifyingCode
     */
    public static void Login(Context context, String userAccount, String password, String identifyingCode, OnHttpTaskListener<RegisterBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.LOGIN, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("userAccount", userAccount);
        httpTask.putParam("password", password);
        httpTask.putParam("identifyingCode", identifyingCode);
        httpTask.runTask(RegisterBean.class, httpTaskListener);
    }

    /**
     * 绑定手机
     *
     * @param userId
     * @param mobileNumber
     * @param identifyingCode
     */
    public static void BindMobile(Context context, String userId, String mobileNumber, String identifyingCode, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.BINDMOBILE, HttpTask.METHOD_POST);
        httpTask.putParam("userId", userId);
        httpTask.putParam("mobileNumber", mobileNumber);
        httpTask.putParam("identifyingCode", identifyingCode);
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 登出
     */
    public static void LogOut(Context context, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.LOGINOUT, HttpTask.METHOD_POST);
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 修改登录密码
     *
     * @param userId
     * @param identifyCode
     * @param password
     * @param mobileNumber
     */
    public static void UpdateLoginPsw(Context context, String userId, String identifyCode, String password, String mobileNumber, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.UPDLOGINPWD, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("userId", userId);
        httpTask.putParam("identifyCode", identifyCode);// 验证码
        httpTask.putParam("password", password);// 新密码
        httpTask.putParam("mobileNumber", mobileNumber);// 绑定手机号
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 注册第一步，注册手机号
     *
     * @param mobileNumber
     * @param identifyingCode
     * @param invitedCode
     */
    public static void RegistMobile(Context context, String phone_num, String identifyingCode, String invitedCode, OnHttpTaskListener<RegisterBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.REGISTER, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("mobileNumber", phone_num);
        httpTask.putParam("identifyingCode", identifyingCode);// 验证码
        httpTask.putParam("invitedCode", invitedCode);// 邀请码
        httpTask.putParam("platform", 0);
        httpTask.runTask(RegisterBean.class, httpTaskListener);
    }

    /**
     * 注册第二步，设置登录密码
     *
     * @param userId
     * @param password
     */
    public static void RegistSetPsw(Context context, String userId, String password, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.REGISTERPSW, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("userId", userId);
        httpTask.putParam("password", password);
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 注册第三步，实名认证(也是个人信息中实名认证的接口)
     *
     * @param userId
     * @param userName
     * @param idNumber
     */
    public static void Verified(Context context, String userId, String userName, String idNumber, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.Account.REGISTERREALNAME, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("userId", userId);

        try {
            // 用户真实姓名
            httpTask.putParam("userName", URLEncoder.encode(userName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpTask.putParam("idNumber", idNumber);// 身份证号
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 设置交易密码
     *
     * @param userId
     * @param transPwd
     */
    public static void SetTransPsw(Context context, String userId, String transPwd, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.SETTRANSPWD, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("userId", userId);
        httpTask.putParam("transPwd", transPwd);// 交易密码
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 修改交易密码
     *
     * @param userId
     * @param oldTransPwd
     * @param transPwd
     */
    public static void UpdateTransPsw(Context context, String userId, String oldTransPwd, String transPwd, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.UPDTRANSPWD, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("userId", userId);
        httpTask.putParam("oldTransPwd", oldTransPwd);// 原交易密码
        httpTask.putParam("transPwd", transPwd);// 新交易密码
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 查询个人信息
     *
     * @param userId
     */
    public static void GetUserInfo(Context context, String userId, OnHttpTaskListener<PersonalInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.MyAccountInfoApi.GETPERSONALINFO, HttpTask.METHOD_POST);
        httpTask.putParam("userId", userId);
        httpTask.runTask(PersonalInfoBean.class, httpTaskListener);
    }

    /**
     * 拨打客服电话
     */
    public static void ServiceCall(Context context) {
        String telNum = context.getResources().getString(R.string.loan_customerservice_tips_callandtime).toString().trim();
        Intent mIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telNum));
        context.startActivity(mIntent);
    }
}
