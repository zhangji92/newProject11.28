package com.allactivity.weChat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.allactivity.R;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;

/**
 * Created by 张继 on 2016/10/24.
 *
 */

public class ChangeColorIconWithText extends View {
    //颜色
    private int mColor = 0x45c01A;
    //图pain
    private Bitmap mIconBitmap;
    //标题
    private String mText = "微信";
    //字体大小
    private int mTextSize = (int) applyDimension(COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());


    //绘制对象
    private Canvas mCanvas;
    //图片对象
    private Bitmap mBitmap;
    //画笔对象
    private Paint mPaint;
    //透明度
    private float mAlpha;

    private Rect mIconRect;
    private Rect mTextBound;
    //字体画笔
    private Paint mTextPaint;

    public ChangeColorIconWithText(Context context) {
        this(context, null);
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 获取自定义属性的值
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ChangeColorIconWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIconWithText);
        int n = typedArray.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorIconWithText_icon:
                    //获取图片
                    BitmapDrawable drawable = (BitmapDrawable) typedArray.getDrawable(attr);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.ChangeColorIconWithText_color:
                    mColor = typedArray.getColor(attr, 0xff45c01a);
                    break;
                case R.styleable.ChangeColorIconWithText_text:
                    mText = typedArray.getString(attr);
                    break;
                case R.styleable.ChangeColorIconWithText_text_size:
                    mTextSize = (int) typedArray.getDimension(attr, COMPLEX_UNIT_DIP);
                    break;
            }
        }
        //回收
        typedArray.recycle();
        mTextBound=new Rect();
        mTextPaint=new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0Xff555555);
        //测量字的范围
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth=Math.min(getMeasuredWidth()-getPaddingStart()-getPaddingEnd(),getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height());
        int left=getMeasuredWidth()/2-iconWidth/2;
        int top=getMeasuredHeight()/2-(mTextBound.height()+iconWidth)/2;
        //
        mIconRect=new Rect(left,top,left+iconWidth,top+iconWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制原图
        canvas.drawBitmap(mIconBitmap,null,mIconRect,null);

        int alpha= (int) Math.ceil(255*mAlpha);

        //内存准备没Bitmap,setAlpha,纯色,xfrmode,图标
        setupTargetBitmap(alpha);

        //1、绘制原文本; 2、绘制变色的文本
        drawSourceText(canvas,alpha);
        drawTargetText(canvas,alpha);
        canvas.drawBitmap(mBitmap,0,0,null);
    }

    /**
     * 绘制变色的文本
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {

        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y=mIconRect.bottom+mTextBound.height();
        canvas.drawText(mText,x,y,mTextPaint);
    }

    /**
     * 绘制源文本
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {
        //文本颜色
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255-alpha);
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y=mIconRect.bottom+mTextBound.height();
        canvas.drawText(mText,x,y,mTextPaint);
    }

    /**
     * 在内存中绘制可变色的Icon
     */
    private void setupTargetBitmap(int alpha) {
        mBitmap=Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas(mBitmap);
        mPaint=new Paint();
        mPaint.setColor(mColor);
        //去除锯齿
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect,mPaint);
        //绘制纯色
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        //绘制图标
        mCanvas.drawBitmap(mIconBitmap,null,mIconRect,mPaint);
    }

    public void setIconAlpha(float alpha){
        this.mAlpha=alpha;
        invalidateView();
    }

    /**
     * 重绘
     */
    private void invalidateView() {
        if (Looper.getMainLooper()==Looper.myLooper()){
            invalidate();
        }else {
            postInvalidate();
        }
    }

    private static final String INSTANCE_STATUS="instance_status";
    private static final String STATUS_ALPHA="status_alpha";


    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle=new Bundle();
        //保存父级的操作
        bundle.putParcelable(INSTANCE_STATUS,super.onSaveInstanceState());
        //保存自己的变量
        bundle.putFloat(STATUS_ALPHA,mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof  Bundle){
            Bundle bundle=(Bundle)state;
            mAlpha=bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
