package com.jrd.loan.api;

import android.content.Context;

import com.jrd.loan.api.WebApi.AppApi;
import com.jrd.loan.bean.BannerInfoBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.ProjectInfoBean;
import com.jrd.loan.bean.SfnInfoBean;
import com.jrd.loan.net.framework.HttpTask;
import com.jrd.loan.net.framework.OnHttpTaskListener;

public class ProjectInfoApi {
    /**
     * 广告位banner信息
     */
    public static void getIdxBannerInfo(Context context, OnHttpTaskListener<BannerInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, AppApi.GETBANNERINFO, HttpTask.METHOD_POST);
        httpTask.runTask(BannerInfoBean.class, httpTaskListener);
    }

    /**
     * 广告位banner信息
     */
    public static void getIdxBannerInfoNew(Context context, OnHttpTaskListener<BannerInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, AppApi.OBTAIN_BANNER_INFO, HttpTask.METHOD_POST);
        httpTask.runTask(BannerInfoBean.class, httpTaskListener);
    }

    /**
     * 首页7付你信息
     */
    public static void get7fnInfo(Context context, OnHttpTaskListener<SfnInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.SevenPayYou.GET7FNINFO, HttpTask.METHOD_POST);
        httpTask.runTask(SfnInfoBean.class, httpTaskListener);
    }

    /**
     * 7付你投资
     */
    public static void get7fnTransaction(Context context, String userId, String projectId, String investAmount, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.SevenPayYou.G7FNTRANSACTION, HttpTask.METHOD_POST);
        httpTask.putParam("userId", userId);
        httpTask.putParam("projectId", projectId);
        httpTask.putParam("investAmount", investAmount);
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 首页标的信息
     */
    public static void getIdxProjectInfo(Context context, int pageSize, int pageNo, OnHttpTaskListener<ProjectInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.GETIDPROJECTINFO, HttpTask.METHOD_POST);
        httpTask.putParam("pageNo", pageNo);
        httpTask.putParam("pageSize", pageSize);
        httpTask.runTask(ProjectInfoBean.class, httpTaskListener);
    }

    /**
     * 理财直投项目标的信息
     */
    public static void getProjectListInfo(Context context, int pageSize, int pageNo, OnHttpTaskListener<ProjectInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.GETPROJECTLISTINFO, HttpTask.METHOD_POST);
        httpTask.putParam("pageNo", pageNo);
        httpTask.putParam("pageSize", pageSize);
        httpTask.runTask(ProjectInfoBean.class, httpTaskListener);
    }
}
