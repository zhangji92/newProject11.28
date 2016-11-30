package com.example.lenovo.amuse.util;

/**
 * Created by 张继 on 2016/10/25.
 * 请求数据的工具类
 */

public class ServiceMessage<T> {
    private String url;

    private int what;

    private T t;

    public ServiceMessage(String url, int what, T t) {
        this.url = url;
        this.what = what;
        this.t = t;
    }

    public ServiceMessage() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

}
