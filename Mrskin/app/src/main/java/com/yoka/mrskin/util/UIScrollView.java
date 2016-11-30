package com.yoka.mrskin.util;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

public class UIScrollView extends HorizontalScrollView{
	private static final String TAG = UIScrollView.class.getSimpleName();

	private static final int size = 2;
	private View inner;
	private float x;
	private Rect normal = new Rect();
	private Handler handler;

	public UIScrollView(Context context) {
		super(context);
	}

	public UIScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner == null) {
			return super.onTouchEvent(ev);
		} else {
			commOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			x = ev.getX();
			break;
		case MotionEvent.ACTION_UP:
			if (isNeedAnimation()) {
				Log.d(TAG, "will up and animation");
				animation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float preX = x;
			float nowX = ev.getX();
			int deltaX = (int) (preX - nowX) / size;
			x = nowX;
			if (isNeedMove()) {
				if (normal.isEmpty()) {
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
					return;
				}
				int xx = inner.getLeft() - deltaX;

				inner.layout(xx, inner.getTop(), inner.getRight() - deltaX,
						inner.getBottom());
			}
			break;
		default:
			break;
		}
	}

	public void animation() {
		TranslateAnimation ta = new TranslateAnimation(inner.getRight(), normal.right, 0,
				0);
		ta.setDuration(100);
		inner.startAnimation(ta);
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();
	}

	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	public boolean isNeedMove() {
		int offset = inner.getMeasuredWidth() - getWidth();
		int scrollX = getScrollX();
		if (scrollX == 0 || scrollX == offset) {
			return true;
		}
		return false;
	}


}
