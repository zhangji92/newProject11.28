package com.yoka.mrskin.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgressBar extends ProgressBar
{
    private String text_progress;
    private Paint mPaint;// 画笔

    public MyProgressBar(Context context)
    {
        super(context);
        initPaint();
    }

    public MyProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initPaint();
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initPaint();
    }

    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        setTextProgress(progress);
    }

    @SuppressLint("DrawAllocation")
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text_progress, 0,
                this.text_progress.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text_progress, x, y, this.mPaint);
    }

    private void initPaint() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setTextSize(25f);
    }

    private void setTextProgress(int progress) {
        int i = (int) ((progress * 1.0f / this.getMax()) * 100);
        this.text_progress = String.valueOf(i) + "%";
    }

}
