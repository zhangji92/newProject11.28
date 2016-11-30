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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yoka.mrskin.R;

public class WritePhotoPopupWindow extends PopupWindow
{
    private TextView mPhoto, mPhotograph, mCancle;
    private View mMenuView;

    private static WritePhotoPopupWindow singleton = null;
    private static Object lock = new Object();

    public static WritePhotoPopupWindow getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new WritePhotoPopupWindow();
            }
        }
        return singleton;
    }

    public WritePhotoPopupWindow(Activity context)
    {

        super(context);
    }

    public WritePhotoPopupWindow(Context context, AttributeSet def)
    {

        super(context);
    }

    public WritePhotoPopupWindow(Activity context, OnClickListener itemsOnClick)
    {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.write_popupwindow, null);
        mPhoto = (TextView) mMenuView.findViewById(R.id.popupwindow_image);
        mPhotograph = (TextView) mMenuView.findViewById(R.id.popupwindow_photograph);
        mCancle = (TextView) mMenuView.findViewById(R.id.popupwindow_cancle);
        // 取消按钮
        mCancle.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
        mPhoto.setOnClickListener(itemsOnClick);
        mPhotograph.setOnClickListener(itemsOnClick);
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

                int height = mMenuView.findViewById(R.id.popupwindow_big_layout)
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

    public WritePhotoPopupWindow()
    {
        super();
    }

}
