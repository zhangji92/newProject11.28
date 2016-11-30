package cc.wxf.androiddemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by ccwxf on 2016/6/17.
 *
 */
public class WrapContentListView extends ListView {

    private int height;

    public WrapContentListView(Context context) {
        super(context);
    }

    public WrapContentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        View item = adapter.getView(0, null, null);
        item.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int itemHeight = item.getMeasuredHeight();
        int count = adapter.getCount();
        height = itemHeight * count + getResources().getDimensionPixelSize(R.dimen.divider) * count;
        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), height);
    }

    public int getRealHeight(){
        return height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}
