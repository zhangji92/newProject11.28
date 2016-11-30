package com.yoka.mrskin.model.http;

public interface IHttpTaskObserver
{
    public void taskCreated(YKHttpTask task);
    public void taskFinished(YKHttpTask task);
}
