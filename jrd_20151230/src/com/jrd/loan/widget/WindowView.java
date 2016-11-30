package com.jrd.loan.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;

@SuppressLint("ResourceAsColor")
public class WindowView extends LinearLayout {

	private RelativeLayout titleBarLayout; // title bar布局
	private FrameLayout leftBtnLayout;
	private FrameLayout rightBtnLayout;
	private ImageView leftImage;// 左边按钮图片
	private ImageView rightImage;// 右边按钮图片
	private Button leftButton; // 左侧button
	private Button rightButton; // 右侧button
	private LinearLayout noNetworkLayout;

	public WindowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initTitleBar(context);
		if (!isInEditMode()) {
			setId(R.id.windowView);
		}
	}

	/**
	 * 初始化title bar(默认右侧button隐藏)
	 * 
	 * @param context
	 */
	private void initTitleBar(Context context) {
		if (!isInEditMode()) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.titleBarLayout = (RelativeLayout) inflater.inflate(R.layout.loan_title_bar_layout, null);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.loan_title_bar_height));
			this.titleBarLayout.setLayoutParams(layoutParams);
			this.leftImage = (ImageView) this.titleBarLayout.findViewById(R.id.imageLeft);
			this.rightImage = (ImageView) this.titleBarLayout.findViewById(R.id.imageRight);
			this.leftBtnLayout = (FrameLayout) this.titleBarLayout.findViewById(R.id.leftBtnLayout);
			this.rightBtnLayout = (FrameLayout) this.titleBarLayout.findViewById(R.id.rightBtnLayout);
			this.leftButton = (Button) this.titleBarLayout.findViewById(R.id.btnLeft);
			this.rightButton = (Button) this.titleBarLayout.findViewById(R.id.btnRight);
			this.leftImage.setVisibility(View.GONE);
			this.leftButton.setVisibility(View.VISIBLE);
			addView(this.titleBarLayout);
			this.noNetworkLayout = (LinearLayout) inflater.inflate(R.layout.loan_no_network_layout, null);
			LayoutParams layoutParams2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			this.noNetworkLayout.setLayoutParams(layoutParams2);
			this.noNetworkLayout.setVisibility(View.GONE);
			addView(this.noNetworkLayout);
		}
	}

	public void setNoNetworkClick(OnClickListener clickListener) {
		this.noNetworkLayout.setOnClickListener(clickListener);
	}

	public void hideContentLayout() {
		this.noNetworkLayout.setVisibility(View.GONE);
		int childCount = getChildCount();
		if (childCount > 2) {
			for (int i = 2; i < childCount; i++) {
				getChildAt(i).setVisibility(View.GONE);
			}
		}
	}

	public void showNoNetworkLayout() {
		this.setVisibility(View.VISIBLE);
		this.noNetworkLayout.setVisibility(View.VISIBLE);
		int childCount = getChildCount();
		if (childCount > 2) {
			for (int i = 2; i < childCount; i++) {
				getChildAt(i).setVisibility(View.GONE);
			}
		}
	}

	public void hideNoNetworkLayout() {
		this.noNetworkLayout.setVisibility(View.GONE);
		int childCount = getChildCount();
		if (childCount > 2) {
			for (int i = 2; i < childCount; i++) {
				getChildAt(i).setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 设置界面的title
	 * 
	 * @param titleId
	 *            字符串资源的ID
	 */
	public void setTitle(int titleId) {
		((TextView) this.titleBarLayout.findViewById(R.id.tvTitle)).setText(titleId);
	}

	/**
	 * 设置界面的title
	 * 
	 * @param title
	 *            title字符串
	 */
	public void setTitle(String title) {
		((TextView) this.titleBarLayout.findViewById(R.id.tvTitle)).setText(title);
	}

	/**
	 * 获取title文本
	 * 
	 * @return
	 */
	public String getTitle() {
		return ((TextView) this.titleBarLayout.findViewById(R.id.tvTitle)).getText().toString();
	}

	/**
	 * 显示左侧关闭按钮
	 */
	public void ShowCloseImage() {
		this.leftButton.setVisibility(View.GONE);
		this.leftImage.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置右边button的背景 必须是图片的资源ＩＤ
	 * 
	 * @param resId
	 */
	public void setRightButtonBackground(int resId) {
		this.rightImage.setImageResource(resId);

	}

	/**
	 * 为title bar左侧的button设置可见性
	 * 
	 * @param visibility
	 */
	public void setLeftButtonVisibility(int visibility) {
		this.leftButton.setVisibility(visibility);
	}

	/**
	 * 为title bar右侧的button设置可见性
	 * 
	 * @param visibility
	 */
	public void setRightButtonVisibility(int visibility) {
		this.rightBtnLayout.setVisibility(visibility);
	}

	/**
	 * 为title bar左侧的button设置监听器
	 * 
	 * @param clickListener
	 *            点击事件监听器
	 */
	public void setLeftButtonClickListener(OnClickListener clickListener) {
		if (clickListener != null) {
			this.leftBtnLayout.setOnClickListener(clickListener);
		}
	}

	/**
	 * 为title bar右侧的button设置监听器
	 * 
	 * @param clickListener
	 *            点击事件监听器
	 */
	public void setRightButtonClickListener(OnClickListener clickListener) {
		this.rightBtnLayout.setVisibility(View.VISIBLE);
		this.rightButton.setVisibility(View.GONE);
		this.rightImage.setVisibility(View.VISIBLE);
		if (clickListener != null) {
			this.rightBtnLayout.setOnClickListener(clickListener);
		}
	}

	/**
	 * 带文字的右侧按钮事件
	 * 
	 * @param text
	 * @param clickListener
	 */
	public void setRightButtonClickListener(String text, OnClickListener clickListener) {
		this.rightBtnLayout.setVisibility(View.VISIBLE);
		this.rightButton.setVisibility(View.VISIBLE);
		this.rightImage.setVisibility(View.GONE);
		this.rightButton.setText(text);
		if (clickListener != null) {
			this.rightBtnLayout.setOnClickListener(clickListener);
		}
	}

	/**
	 * 为title bar背景颜色
	 * 
	 * @param color颜色
	 */
	public void setTitleBarBackground(int color) {
		this.titleBarLayout.setBackgroundResource(color);
	}

	/**
	 * 为title bar背景图片
	 * 
	 * @param img图片
	 */
	public void setTitleBarBackgroundImg(int img) {
		this.titleBarLayout.setBackgroundDrawable(getResources().getDrawable(img));
	}

	/**
	 * 设置界面的title字体颜色
	 * 
	 * @param titleId
	 *            字符串资源的ID
	 */
	public void setTitleTextColor(int color) {
		((TextView) this.titleBarLayout.findViewById(R.id.tvTitle)).setTextColor(getResources().getColor(color));
	}
}
