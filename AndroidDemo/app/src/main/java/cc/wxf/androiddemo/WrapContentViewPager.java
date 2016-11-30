package cc.wxf.androiddemo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

public class WrapContentViewPager extends ViewPager {

    private Map<Integer, Integer> maps = new HashMap<Integer, Integer>();
    private int current;

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentViewPager(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if(maps.size() > current){
            height = maps.get(current + 1);
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current){
        this.current = current;
        if(maps.size() > current){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            if(layoutParams == null){
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, maps.get(current + 1));
            }else{
                layoutParams.height = maps.get(current + 1);
            }
            setLayoutParams(layoutParams);
        }
    }

    public void calculate(int type, int height){
        maps.put(type, height);
    }
}
