package com.yoka.mrskin.fragment;

import java.net.URLDecoder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.HomeTopShare;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;

public class CourseFragment extends Fragment implements OnClickListener
{
    // private TextView mRefresh;
    private LinearLayout mBack;
    private ProgressWebView mWebView;
    private String mCourseUrl;

    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.course_fragment, container,
                false);
        return rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        mBack = (LinearLayout)
                getActivity().findViewById(R.id.read_back_layout);
        mBack.setOnClickListener(this);
        // mRefresh = (TextView) getActivity().findViewById(R.id.read_refresh);
        // mRefresh.setOnClickListener(this);

        mWebView = (ProgressWebView) getActivity().findViewById(
                R.id.webView_read);
        // 教程

        mWebView.loadUrl(getActivity(), YKManager.getFour() + "news/course");
        // http://192.168.57.86:10086/fujun/web/news/read
        mWebView.setURIHandler(new YKURIHandler() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean handleURI(String url) {
                if (url != null) {
                    Uri uri = Uri.parse(url);
                    String query = uri.getQuery();
                    String spStr[] = query.split("&");
                    String param;
                    for (int i = 0; i < spStr.length; ++i) {
                        param = spStr[i];
                        String paramSplit[] = param.split("=");
                        if (paramSplit.length != 2) {
                            continue;
                        }
                        URLDecoder.decode(paramSplit[1]);
                        // //类型
                        // String type = uri.getQueryParameter("platform");
                        // System.out.println(type);
                        // //标题
                        // String titlee = uri.getQueryParameter("title");
                        // System.out.println(titlee);
                        // //副标题
                        // String summary = uri.getQueryParameter("summary");
                        // System.out.println(summary);
                        // //图片路径
                        // String picc = uri.getQueryParameter("pic");
                        // System.out.println(picc);
                    }
                    mCourseUrl = uri.getQueryParameter("url");
                    if (mCourseUrl != null) {
                        Intent course = new Intent(getActivity(),
                                HomeTopShare.class);
                        course.putExtra("url", mCourseUrl);
                        course.putExtra("title",
                                getString(R.string.course_class));
                        startActivity(course);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
        MobclickAgent.onPageStart("CourseFragment"); // 统计页面
        MobclickAgent.onResume(getActivity()); // 统计时长
    }

    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
        MobclickAgent.onPageEnd("CourseFragment"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
        case R.id.read_back_layout:
            break;
        default:
            break;
        } 
    }
}
