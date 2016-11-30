package com.allactivity.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.allactivity.R;

/**
 * Created by 张继 on 2016/11/10.
 * 自定义图片
 */

public class CustomImageView extends ImageView {


    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MaskImage, 0, 0);
        int mImageSource = typedArray.getResourceId(R.styleable.MaskImage_img, 0);
        int mMaskSource = typedArray.getResourceId(R.styleable.MaskImage_mask, 0);
        float mx=typedArray.getDimension(R.styleable.MaskImage_mx, 0);
        float my=typedArray.getDimension(R.styleable.MaskImage_my,0);

        RuntimeException mException=null;
        if (mImageSource == 0 || mMaskSource == 0) {
            mException = new IllegalArgumentException(typedArray.getPositionDescription() +
                    ": The content attribute is required and must refer to a valid image.");
        }

        if (mException != null)
            throw mException;
        //获取图片的资源文件
        Bitmap original = BitmapFactory.decodeResource(getResources(), mImageSource);
        //获取遮罩层图片
        Bitmap mask = BitmapFactory.decodeResource(getResources(), mMaskSource);
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        //将遮罩层的图片放到画布中
        Paint paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        Canvas mCanvas = new Canvas(result);
        mCanvas.drawBitmap(mask,mx,my,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制图片
        mCanvas.drawBitmap(original, 0, 0, paint);
        setImageBitmap(result);
        setScaleType(ScaleType.CENTER);

        typedArray.recycle();

//        this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        //自定义属性
//        typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MaskImage, 0, 0);
//        //显示图片资源
//        mImageSource = typedArray.getResourceId(R.styleable.MaskImage_img, 0);
//        //遮罩形状
//        mMaskSource = typedArray.getResourceId(R.styleable.MaskImage_mask, 0);
//        //图片大小
//        mx = typedArray.getDimension(R.styleable.MaskImage_mx, 0);
//        my = typedArray.getDimension(R.styleable.MaskImage_my, 0);
//        //异常
//        RuntimeException mException = null;
//        if (mImageSource == 0 || mMaskSource == 0) {
//            mException = new IllegalArgumentException(typedArray.getPositionDescription() +
//                    ": The content attribute is required and must refer to a valid image.");
//        }
//
//        if (mException != null) {
//            throw mException;
//        }
//        typedArray.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    //    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        //获取图片的资源文件
//        Bitmap original = BitmapFactory.decodeResource(getResources(), mImageSource);
//        //获取遮罩层图片
//        Bitmap mask = BitmapFactory.decodeResource(getResources(), mMaskSource);
//
//        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
//
////        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
//
//        //将遮罩层的图片放到画布中
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);//设置抗锯齿
//        //绘制遮罩
////        mCanvas.drawBitmap(mask,mx,my,paint);
//        canvas.drawBitmap(mask, 0, 0, paint);
////        canvas.drawCircle(100,100,200,paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        // 绘制图片
////        mCanvas.drawBitmap(original, 0, 0, paint);
//        canvas.drawBitmap(original, 0, 0, paint);
////        setImageBitmap(result);
////        //图片类型
//        setScaleType(ScaleType.CENTER_CROP);
//
//    }
}
