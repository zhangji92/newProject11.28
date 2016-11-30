package com.jrd.loan.umeng;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public final class UMengEvent {
    // 事件ID
    public final static String EVENT_ID_REGISTER = "register";
    public final static String EVENT_ID_INVEST = "invest";
    public final static String EVENT_ID_RECHARGE = "recharge";
    public final static String EVENT_ID_WITHDRAW = "withdraw";
    public final static String EVENT_ID_TRANSFERIN = "transferIn";
    public final static String EVENT_ID_TRANSFEROUT = "transferOut";

    // 模块名称
    public final static String EVENT_MODULE_TRANSFERIN_BANKCARD = "银行卡";
    public final static String EVENT_MODULE_TRANSFERIN_ACCOUNTBALANCE = "账号余额";
    public final static String EVENT_MODULE_TRANSFEROUT_BANKCARD = "银行卡";
    public final static String EVENT_MODULE_TRANSFEROUT_ACCOUNTBALANCE = "账户余额";

    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEvent(Context context, String eventId, String moduleName) {
        MobclickAgent.onEvent(context, eventId, moduleName);
    }
}