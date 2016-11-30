package com.yoka.mrskin.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yoka.mrskin.R;

public class SharePopupWindow extends PopupWindow
{
    private Button mWeiFriend, mWei, mSina, mQzone, mCancle,mQQ,mCopy;
    private View mMenuView;

    private static SharePopupWindow singleton = null;
    private static Object lock = new Object();

    public static SharePopupWindow getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new SharePopupWindow();
            }
        }
        return singleton;
    }

    public SharePopupWindow(Activity context)
    {

        super(context);
    }

    public SharePopupWindow(Context context, AttributeSet def)
    {

        super(context);
    }

    public SharePopupWindow(Activity context, OnClickListener itemsOnClick)
    {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.share_popupwindow, null);
        mWeiFriend = (Button) mMenuView.findViewById(R.id.popup_friend);
        mWei = (Button) mMenuView.findViewById(R.id.popup_wei);
        mSina = (Button) mMenuView.findViewById(R.id.popup_sina);
        mQzone = (Button) mMenuView.findViewById(R.id.popup_qzone);
        mQQ = (Button) mMenuView.findViewById(R.id.popup_qqfriend);
        mCopy = (Button) mMenuView.findViewById(R.id.popup_copyurl);
        mCancle = (Button) mMenuView.findViewById(R.id.popup_cancle);
        // 取消按钮
        mCancle.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
        mWeiFriend.setOnClickListener(itemsOnClick);
        mWei.setOnClickListener(itemsOnClick);
        mSina.setOnClickListener(itemsOnClick);
        mQzone.setOnClickListener(itemsOnClick);
        mQQ.setOnClickListener(itemsOnClick);
        mCopy.setOnClickListener(itemsOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // //设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.popup_big_layout)
                        .getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public SharePopupWindow()
    {
        super();
    }

}
