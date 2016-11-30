package com.jrd.loan.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jrd.loan.R;

/**
 * 手势密码容器类
 */
public class GestureContentView extends ViewGroup {
	private final static int baseNum = 5;

	private int screenWidth;

	/**
	 * 每个点区域的宽度
	 */
	private int blockWidth;

	/**
	 * 声明一个集合用来封装坐标集合
	 */
	private List<GesturePoint> list;
	private Context context;
	private GestureDrawline gestureDrawline;

	/**
	 * 包含9个ImageView的容器，初始化
	 * 
	 * @param context
	 * @param isVerify
	 *            是否为校验手势密码
	 * @param passWord
	 *            用户传入密码
	 * @param callBack
	 *            手势绘制完毕的回调
	 */
	public GestureContentView(Context context, boolean isVerify, String passWord, GestureCallBack callBack) {
		super(context);

		this.screenWidth = getResources().getDisplayMetrics().widthPixels;

		blockWidth = this.screenWidth / 4;
		this.list = new ArrayList<GesturePoint>();
		this.context = context;

		// 添加9个图标
		addChild();

		// 初始化一个可以画线的view
		gestureDrawline = new GestureDrawline(context, list, isVerify, passWord, callBack);
	}

	private void addChild() {
		for (int i = 0; i < 9; i++) {
			ImageView image = new ImageView(context);
			image.setBackgroundResource(R.drawable.loan_gesture_node_normal);
			this.addView(image);

			invalidate();

			// 第几行
			int row = i / 3;

			// 第几列
			int col = i % 3;

			// 定义点的每个属性
			int leftX = col * blockWidth + blockWidth / baseNum + blockWidth / 2;
			int topY = row * blockWidth + blockWidth / baseNum + blockWidth / 2;
			int rightX = col * blockWidth + blockWidth - blockWidth / baseNum + blockWidth / 2;
			int bottomY = row * blockWidth + blockWidth - blockWidth / baseNum + blockWidth / 2;

			GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY, image, i + 1);

			this.list.add(p);
		}
	}

	public void setParentView(ViewGroup parent) {
		// 得到屏幕的宽度
		LayoutParams layoutParams = new LayoutParams(this.screenWidth, this.screenWidth);
		this.setLayoutParams(layoutParams);

		this.gestureDrawline.setLayoutParams(layoutParams);

		parent.addView(this.gestureDrawline);
		parent.addView(this);
	}

	public String getInputCode() {
		return this.gestureDrawline.getInputCode();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int row = 0;
		int col = 0;
		View view = null;

		for (int i = 0; i < getChildCount(); i++) {
			// 第几行
			row = i / 3;
			// 第几列
			col = i % 3;

			view = getChildAt(i);

			view.layout(col * blockWidth + blockWidth / baseNum + blockWidth / 2, row * blockWidth + blockWidth / baseNum + blockWidth / 2, col * blockWidth + blockWidth - blockWidth / baseNum + blockWidth / 2, row * blockWidth + blockWidth - blockWidth / baseNum + blockWidth / 2);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 遍历设置每个子view的大小
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			view.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	/**
	 * 保留路径delayTime时间长
	 * 
	 * @param delayTime
	 */
	public void clearDrawlineState(long delayTime) {
		gestureDrawline.clearDrawlineState(delayTime);
	}

	private class GestureDrawline extends View {
		private int mov_x;// 声明起点坐标
		private int mov_y;
		private Paint paint;// 声明画笔
		private Canvas canvas;// 画布
		private Bitmap bitmap;// 位图
		private List<GesturePoint> list;// 装有各个view坐标的集合
		private List<Pair<GesturePoint, GesturePoint>> lineList;// 记录画过的线
		private Map<String, GesturePoint> autoCheckPointMap;// 自动选中的情况点
		private boolean isDrawEnable = true; // 是否允许绘制

		private int screenWidth;

		/**
		 * 手指当前在哪个Point内
		 */
		private GesturePoint currentPoint;
		/**
		 * 用户绘图的回调
		 */
		private GestureCallBack callBack;

		/**
		 * 用户当前绘制的图形密码
		 */
		private StringBuilder passWordSb;

		/**
		 * 是否为校验
		 */
		private boolean isVerify;

		/**
		 * 用户传入的passWord
		 */
		private String passWord;

		public GestureDrawline(Context context, List<GesturePoint> list, boolean isVerify, String passWord, GestureCallBack callBack) {
			super(context);

			this.screenWidth = getResources().getDisplayMetrics().widthPixels;

			paint = new Paint(Paint.DITHER_FLAG);// 创建一个画笔
			bitmap = Bitmap.createBitmap(this.screenWidth, this.screenWidth, Bitmap.Config.ARGB_8888); // 设置位图的宽高

			canvas = new Canvas();
			canvas.setBitmap(bitmap);
			paint.setStyle(Style.STROKE);// 设置非填充
			paint.setStrokeWidth(10);// 笔宽5像素
			paint.setColor(Color.rgb(245, 142, 33));// 设置默认连线颜色
			paint.setAntiAlias(true);// 不显示锯齿

			this.list = list;
			this.lineList = new ArrayList<Pair<GesturePoint, GesturePoint>>();

			initAutoCheckPointMap();
			this.callBack = callBack;

			// 初始化密码缓存
			this.isVerify = isVerify;
			this.passWordSb = new StringBuilder();
			this.passWord = passWord;
		}

		private void initAutoCheckPointMap() {
			autoCheckPointMap = new HashMap<String, GesturePoint>();
			autoCheckPointMap.put("1,3", getGesturePointByNum(2));
			autoCheckPointMap.put("1,7", getGesturePointByNum(4));
			autoCheckPointMap.put("1,9", getGesturePointByNum(5));
			autoCheckPointMap.put("2,8", getGesturePointByNum(5));
			autoCheckPointMap.put("3,7", getGesturePointByNum(5));
			autoCheckPointMap.put("3,9", getGesturePointByNum(6));
			autoCheckPointMap.put("4,6", getGesturePointByNum(5));
			autoCheckPointMap.put("7,9", getGesturePointByNum(8));
		}

		private GesturePoint getGesturePointByNum(int num) {
			for (GesturePoint point : list) {
				if (point.getNum() == num) {
					return point;
				}
			}
			return null;
		}

		// 画位图
		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(bitmap, 0, 0, null);
		}

		public String getInputCode() {
			return this.passWordSb.toString();
		}

		private void setCurrPointState() {
			if (this.currentPoint != null) {
				this.currentPoint.setPointState(Constants.POINT_STATE_WRONG);
			}
		}

		// 触摸事件
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (isDrawEnable == false) {
				// 当期不允许绘制
				return true;
			}

			paint.setColor(Color.rgb(45, 92, 158));// 设置默认连线颜色

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mov_x = (int) event.getX();
					mov_y = (int) event.getY();
					// 判断当前点击的位置是处于哪个点之内
					currentPoint = getPointAt(mov_x, mov_y);

					if (currentPoint != null) {
						currentPoint.setPointState(Constants.POINT_STATE_SELECTED);
						passWordSb.append(currentPoint.getNum());
					}

					invalidate();
					break;
				case MotionEvent.ACTION_MOVE:
					clearScreenAndDrawList();

					// 得到当前移动位置是处于哪个点内
					GesturePoint pointAt = getPointAt((int) event.getX(), (int) event.getY());
					// 代表当前用户手指处于点与点之前
					if (currentPoint == null && pointAt == null) {
						return true;
					} else {// 代表用户的手指移动到了点上
						if (currentPoint == null) {// 先判断当前的point是不是为null
							// 如果为空，那么把手指移动到的点赋值给currentPoint
							currentPoint = pointAt;
							// 把currentPoint这个点设置选中为true;
							currentPoint.setPointState(Constants.POINT_STATE_SELECTED);
							passWordSb.append(currentPoint.getNum());
						}
					}
					if (pointAt == null || currentPoint.equals(pointAt) || Constants.POINT_STATE_SELECTED == pointAt.getPointState()) {
						// 点击移动区域不在圆的区域，或者当前点击的点与当前移动到的点的位置相同，或者当前点击的点处于选中状态
						// 那么以当前的点中心为起点，以手指移动位置为终点画线
						canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), event.getX(), event.getY(), paint);// 画线
					} else {
						// 如果当前点击的点与当前移动到的点的位置不同
						// 那么以前前点的中心为起点，以手移动到的点的位置画线
						canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), pointAt.getCenterX(), pointAt.getCenterY(), paint);// 画线
						pointAt.setPointState(Constants.POINT_STATE_SELECTED);

						// 判断是否中间点需要选中
						GesturePoint betweenPoint = getBetweenCheckPoint(currentPoint, pointAt);
						if (betweenPoint != null && Constants.POINT_STATE_SELECTED != betweenPoint.getPointState()) {
							// 存在中间点并且没有被选中
							Pair<GesturePoint, GesturePoint> pair1 = new Pair<GesturePoint, GesturePoint>(currentPoint, betweenPoint);
							lineList.add(pair1);
							passWordSb.append(betweenPoint.getNum());
							Pair<GesturePoint, GesturePoint> pair2 = new Pair<GesturePoint, GesturePoint>(betweenPoint, pointAt);
							lineList.add(pair2);
							passWordSb.append(pointAt.getNum());
							// 设置中间点选中
							betweenPoint.setPointState(Constants.POINT_STATE_SELECTED);
							// 赋值当前的point;
							currentPoint = pointAt;
						} else {
							Pair<GesturePoint, GesturePoint> pair = new Pair<GesturePoint, GesturePoint>(currentPoint, pointAt);
							lineList.add(pair);
							passWordSb.append(pointAt.getNum());
							// 赋值当前的point;
							currentPoint = pointAt;
						}
					}
					invalidate();
					break;
				case MotionEvent.ACTION_UP:// 当手指抬起的时候
					if (isVerify) {
						// 手势密码校验
						// 清掉屏幕上所有的线，只画上集合里面保存的线
						if (passWord.equals(passWordSb.toString())) {
							// 代表用户绘制的密码手势与传入的密码相同
							callBack.checkedSuccess();
						} else {
							// 用户绘制的密码与传入的密码不同。
							callBack.checkedFail();
						}
					} else {
						callBack.onGestureCodeInput(passWordSb.toString());
					}
					break;
				default:
					break;
			}
			return true;
		}

		/**
		 * 指定时间去清除绘制的状态
		 * 
		 * @param delayTime
		 *            延迟执行时间
		 */
		public void clearDrawlineState(long delayTime) {
			if (delayTime > 0) {
				// 绘制红色提示路线
				isDrawEnable = false;
				drawErrorPathTip();
			}

			new Handler().postDelayed(new clearStateRunnable(), delayTime);
		}

		/**
		 * 清除绘制状态的线程
		 */
		final class clearStateRunnable implements Runnable {
			@Override
			public void run() {
				// 重置passWordSb
				passWordSb = new StringBuilder();
				// 清空保存点的集合
				lineList.clear();
				// 重新绘制界面
				clearScreenAndDrawList();
				for (GesturePoint p : list) {
					p.setPointState(Constants.POINT_STATE_NORMAL);
				}
				invalidate();
				isDrawEnable = true;
			}
		}

		/**
		 * 通过点的位置去集合里面查找这个点是包含在哪个Point里面的
		 * 
		 * @param x
		 * @param y
		 * @return 如果没有找到，则返回null，代表用户当前移动的地方属于点与点之间
		 */
		private GesturePoint getPointAt(int x, int y) {

			for (GesturePoint point : list) {
				// 先判断x
				int leftX = point.getLeftX();
				int rightX = point.getRightX();
				if (!(x >= leftX && x < rightX)) {
					// 如果为假，则跳到下一个对比
					continue;
				}

				int topY = point.getTopY();
				int bottomY = point.getBottomY();
				if (!(y >= topY && y < bottomY)) {
					// 如果为假，则跳到下一个对比
					continue;
				}

				// 如果执行到这，那么说明当前点击的点的位置在遍历到点的位置这个地方
				return point;
			}

			return null;
		}

		private GesturePoint getBetweenCheckPoint(GesturePoint pointStart, GesturePoint pointEnd) {
			int startNum = pointStart.getNum();
			int endNum = pointEnd.getNum();
			String key = null;
			if (startNum < endNum) {
				key = startNum + "," + endNum;
			} else {
				key = endNum + "," + startNum;
			}
			return autoCheckPointMap.get(key);
		}

		/**
		 * 清掉屏幕上所有的线，然后画出集合里面的线
		 */
		private void clearScreenAndDrawList() {
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			for (Pair<GesturePoint, GesturePoint> pair : lineList) {
				canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(), pair.second.getCenterY(), paint);// 画线
			}
		}

		/**
		 * 校验错误/两次绘制不一致提示
		 */
		private void drawErrorPathTip() {
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			paint.setColor(Color.rgb(201, 50, 74));// 设置默认线路颜色

			if (lineList.isEmpty()) {// 为空, 说明只点中一个点
				this.setCurrPointState();
			} else {
				for (Pair<GesturePoint, GesturePoint> pair : lineList) {
					pair.first.setPointState(Constants.POINT_STATE_WRONG);
					pair.second.setPointState(Constants.POINT_STATE_WRONG);
					canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(), pair.second.getCenterY(), paint);// 画线
				}
			}

			invalidate();
		}
	}

	// 手势密码点的状态
	private class Constants {
		public static final int POINT_STATE_NORMAL = 0; // 正常状态

		public static final int POINT_STATE_SELECTED = 1; // 按下状态

		public static final int POINT_STATE_WRONG = 2; // 错误状态
	}

	private class GesturePoint {
		/**
		 * 左边x的值
		 */
		private int leftX;
		/**
		 * 右边x的值
		 */
		private int rightX;
		/**
		 * 上边y的值
		 */
		private int topY;
		/**
		 * 下边y的值
		 */
		private int bottomY;
		/**
		 * 这个点对应的ImageView控件
		 */
		private ImageView image;

		/**
		 * 中心x值
		 */
		private int centerX;

		/**
		 * 中心y值
		 */
		private int centerY;

		/**
		 * 状态值
		 */
		private int pointState;

		/**
		 * 代表这个Point对象代表的数字，从1开始(直接感觉从1开始)
		 */
		private int num;

		public GesturePoint(int leftX, int rightX, int topY, int bottomY, ImageView image, int num) {
			super();
			this.leftX = leftX;
			this.rightX = rightX;
			this.topY = topY;
			this.bottomY = bottomY;
			this.image = image;

			this.centerX = (leftX + rightX) / 2;
			this.centerY = (topY + bottomY) / 2;

			this.num = num;
		}

		public int getLeftX() {
			return leftX;
		}

		public void setLeftX(int leftX) {
			this.leftX = leftX;
		}

		public int getRightX() {
			return rightX;
		}

		public void setRightX(int rightX) {
			this.rightX = rightX;
		}

		public int getTopY() {
			return topY;
		}

		public void setTopY(int topY) {
			this.topY = topY;
		}

		public int getBottomY() {
			return bottomY;
		}

		public void setBottomY(int bottomY) {
			this.bottomY = bottomY;
		}

		public ImageView getImage() {
			return image;
		}

		public void setImage(ImageView image) {
			this.image = image;
		}

		public int getCenterX() {
			return centerX;
		}

		public void setCenterX(int centerX) {
			this.centerX = centerX;
		}

		public int getCenterY() {
			return centerY;
		}

		public void setCenterY(int centerY) {
			this.centerY = centerY;
		}

		public int getPointState() {
			return pointState;
		}

		public void setPointState(int state) {
			pointState = state;
			switch (state) {
				case Constants.POINT_STATE_NORMAL:
					this.image.setBackgroundResource(R.drawable.loan_gesture_node_normal);
					break;
				case Constants.POINT_STATE_SELECTED:
					this.image.setBackgroundResource(R.drawable.loan_gesture_node_pressed);
					break;
				case Constants.POINT_STATE_WRONG:
					this.image.setBackgroundResource(R.drawable.loan_gesture_node_wrong);
					break;
				default:
					break;
			}
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + bottomY;
			result = prime * result + ((image == null) ? 0 : image.hashCode());
			result = prime * result + leftX;
			result = prime * result + rightX;
			result = prime * result + topY;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			GesturePoint other = (GesturePoint) obj;
			if (bottomY != other.bottomY) {
				return false;
			}
			if (image == null) {
				if (other.image != null) {
					return false;
				}
			} else if (!image.equals(other.image)) {
				return false;
			}
			if (leftX != other.leftX) {
				return false;
			}
			if (rightX != other.rightX) {
				return false;
			}
			if (topY != other.topY) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "Point [leftX=" + leftX + ", rightX=" + rightX + ", topY=" + topY + ", bottomY=" + bottomY + "]";
		}
	}

	public static interface GestureCallBack {
		/**
		 * 用户设置/输入了手势密码
		 */
		public abstract void onGestureCodeInput(String inputCode);

		/**
		 * 代表用户绘制的密码与传入的密码相同
		 */
		public abstract void checkedSuccess();

		/**
		 * 代表用户绘制的密码与传入的密码不相同
		 */
		public abstract void checkedFail();
	}
}