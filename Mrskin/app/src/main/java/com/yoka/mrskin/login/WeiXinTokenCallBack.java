package com.yoka.mrskin.login;

public interface WeiXinTokenCallBack
{

    public abstract void TokenCallBackSuccess(WeiXinToken token);

    public abstract void CallBackFaile();

}