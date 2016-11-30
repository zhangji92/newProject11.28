package com.jrd.loan.base;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jrd.loan.util.DialogUtils;

public abstract class BaseFragment extends Fragment {

    protected View view;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(this.getLoadViewId(), container, false);

        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        this.initData();
        super.onActivityCreated(savedInstanceState);
    }

    protected abstract int getLoadViewId();

    protected abstract void initView(View view);

    protected abstract void initData();

    /**
     * 显示dialog进度条
     *
     * @param context
     */
    public void ShowDrawDialog(Context context) {
        if (dialog == null) {
            // dialog = DialogUtils.ShowProDialog(context);
            dialog = DialogUtils.ShowDefaultProDialog(context, null);
        } else {
            dialog.show();
        }
    }

    /**
     * 显示dialog进度条
     *
     * @param context
     */
    public void ShowProDialog(Context context) {
        if (dialog == null) {
            dialog = DialogUtils.ShowDefaultProDialog(context, null);
        } else {
            dialog.show();
        }
    }

    /**
     * 关闭dialog进度条
     */
    public void DismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}