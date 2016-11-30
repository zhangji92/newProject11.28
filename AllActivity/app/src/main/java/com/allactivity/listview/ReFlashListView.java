package com.allactivity.listview;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allactivity.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 张继 on 2016/11/10.
 * 自定义listView
 */

public class ReFlashListView extends ListView implements AbsListView.OnScrollListener {
    //顶部布局文件
    private View header;
    //底部布局
    private View footer;

    //顶部布局文件的高度
    private int headerHeight;
    //当前第一个可见的Item的位置
    private int firstVisibleItem;
    //总数量
    private int totalItemCount;
    //最后一个可见的Item
    private int lastVisibleItem;
    //正在加载
    boolean isLoading;

    //listView当前滚动状态
    private int scrollState;
    //标记，当前是在listView最顶端按下的
    boolean isRemark;
    //按下是的Y值
    int startY;
    //当前的状态
    int state;
    //正常状态
    final int NONE = 0;
    //提示下拉状态
    final int PULL = 1;
    //提示释放状态
    final int RELESE = 2;
    //下拉刷新状态
    final int REFLASHRING = 3;
    //刷新数据的接口

    private IReflashListener iReflashListener;

    public void setiReflashListener(IReflashListener iReflashListener) {
        this.iReflashListener = iReflashListener;
    }

    public ReFlashListView(Context context) {
        this(context, null);
    }

    public ReFlashListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReFlashListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    /**
     * 初始化界面，添加顶部布局文件到listView
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        //获取LayoutInflater实例对象
        LayoutInflater inflater = LayoutInflater.from(context);
        //获取布局文件
        header = inflater.inflate(R.layout.header_layout, null);
        //获取底部布局
        footer = inflater.inflate(R.layout.footer_layout, null);
        //隐藏布局
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
        //通知副布局获取高度
        measureView(header);
        //顶部布局文件的高度
        headerHeight = header.getMeasuredHeight();
        Log.e("aaa", "headerHeight:" + headerHeight);

        topPadding(-headerHeight);
        //把布局文件添加到listView头部
        this.addHeaderView(header);
        //把布局文件添加到listView底部
        this.addFooterView(footer);
        //listView滚动事件
        this.setOnScrollListener(this);
    }

    /**
     * 通知父布局占用的宽高
     *
     * @param view
     */
    private void measureView(View view) {
        //获取布局文件的属性
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            //设置宽高
            /**
             * 第一个参数是外部的边距
             * 第二个参数是内部部的边距
             * 第三个参数是布局的宽度
             */
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, layoutParams.width);
        int height;
        //临时高度
        int tempHeight = layoutParams.height;
        if (tempHeight > 0) {
            //高度为空填充布局
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        //view赋值
        view.measure(width, height);
    }

    /**
     * 设置header布局的上边距
     *
     * @param topPadding 值
     */
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
        //重新加载
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        //滑动到最低端且正在滚动
        if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading=true;
                //显示正在加载布局
                footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                //加载更多
                iReflashListener.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    //标记的Y值
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFLASHRING;
                    //加载最新数据
                    reFlashViewByState();
                    //接口的回调
                    iReflashListener.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reFlashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断移动过程中的操作
     *
     * @param ev
     */
    private void onMove(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        //当前移动的位置
        int tempY = (int) ev.getY();
        // 移动的距离
        int space = tempY - startY;

        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    reFlashViewByState();
                }
                break;
            //下拉状态
            case PULL:
                topPadding(topPadding);
                //距离大于头布局且是在滚动状态就可以提示
                if (space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reFlashViewByState();
                }
                break;
            //提示释放状态
            case RELESE:
                topPadding(topPadding);
                //小于一定的高度变成
                if (space < headerHeight + 30) {
                    state = PULL;
                    reFlashViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    isRemark = false;
                    reFlashViewByState();
                }
                break;
        }
    }

    /**
     * 根据当前状态改变界面显示
     */
    private void reFlashViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
        ProgressBar progressBar = (ProgressBar) header.findViewById(R.id.progress);

        RotateAnimation animation = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        RotateAnimation animation1 = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(500);
        animation1.setFillAfter(true);
        switch (state) {
            case NONE:
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL:
                //显示
                arrow.setVisibility(View.VISIBLE);
                //隐藏
                progressBar.setVisibility(View.GONE);
                tip.setText("下拉可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(animation1);
                break;
            case RELESE:
                //显示
                arrow.setVisibility(View.VISIBLE);
                //隐藏
                progressBar.setVisibility(View.GONE);
                tip.setText("松开可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(animation);
                break;
            case REFLASHRING:
                topPadding(50);
                //显示
                arrow.setVisibility(View.GONE);
                //隐藏
                progressBar.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                arrow.clearAnimation();
                break;
        }
    }

    /**
     * 获取完数据
     */
    public void reFlashComplete() {
        state = NONE;
        isRemark = false;
        reFlashViewByState();
        TextView lastupDatetime = (TextView) header.findViewById(R.id.lastupdataTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        lastupDatetime.setText(time);
    }

    /**
     * 加载完毕
     */
    public void loadComplete(){
        isLoading=false;
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
    }

    /**
     * 刷新数据接口
     */
    public interface IReflashListener {
        void onReflash();
        //加载更多
        void onLoad();
    }
}
