package com.jrd.loan.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jrd.loan.R;
import com.jrd.loan.api.FinanceApi;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.DetailBean;
import com.jrd.loan.bean.PocketDetailsBean;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.widget.ProgressWebView;

/**
 * 项目详情
 *
 * @author Aaron
 */
public class ProjectDetailsFragment extends BaseFragment {

    private String FLAG = "0";
    private ProgressWebView webView;

    @Override
    protected int getLoadViewId() {
        return R.layout.loan_details_layout;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView(View view) {
        webView = (ProgressWebView) view.findViewById(R.id.loan_details_webview);
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        // 10010是活期标详情   10011是普通标详情
        if (getArguments().getInt("flag") == 10010) {
            PocketDetails();
        } else {
            DetailInfoData();
        }
    }

    /**
     * 普通标的详情
     *
     * @param mockId
     * @param flag
     */
    private void DetailInfoData() {
        FinanceApi.getDetailInfo(getActivity(), getArguments().getString("mockId"), FLAG, new OnHttpTaskListener<DetailBean>() {

            @Override
            public void onStart() {
                ShowDrawDialog(getActivity());
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(DetailBean bean) {
                DismissDialog();
                if (bean != null && bean.getResultCode() == 0) {
                    webView.loadDataWithBaseURL(null, bean.getInfoDetail(), "text/html", "UTF-8", null);
                }
            }
        });
    }

    /**
     * 活期标的详情
     *
     * @param flag
     */
    private void PocketDetails() {
        PocketApi.PocketDetails(getActivity(), FLAG, new OnHttpTaskListener<PocketDetailsBean>() {

            @Override
            public void onStart() {
                ShowDrawDialog(getActivity());
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(PocketDetailsBean bean) {
                DismissDialog();
                if (bean != null && bean.getResultCode() == 0) {
                    webView.loadDataWithBaseURL(null, bean.getInfoHtml(), "text/html", "UTF-8", null);
                }
            }
        });
    }
}
