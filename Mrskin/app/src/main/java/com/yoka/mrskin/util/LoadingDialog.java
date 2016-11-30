package com.yoka.mrskin.util;

import android.app.AlertDialog;
import android.content.Context;

public class LoadingDialog extends AlertDialog
{

    private LoadingAnimUtil mAnim;
    public LoadingDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener)
    
    {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public LoadingDialog(Context context, int theme)
    {
        super(context, theme);
        init(context);
    }

    public LoadingDialog(Context context)
    {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mAnim = new LoadingAnimUtil(context);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(mAnim!=null){
            mAnim.clearAnimation();
        }
    }

    @Override
    public void show() {
        super.show();
        if(mAnim!=null){
            setContentView(mAnim);
            mAnim.startAnim();
        }
    }

}
