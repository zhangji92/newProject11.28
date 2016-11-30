package cc.wxf.androiddemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import cc.wxf.androiddemo.indicator.TabPageIndicator;

public class MainActivity extends FragmentActivity {

    private TabPageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        View header = View.inflate(this, R.layout.header, null);
        indicator = (TabPageIndicator) header.findViewById(R.id.indicator);
        listView.addHeaderView(header);
        listView.setAdapter(new CustomAdapter(this));
    }

    public class CustomAdapter extends BaseAdapter{

        private View view;
        private WrapContentViewPager viewPager;

        public CustomAdapter(Context context) {
            view = View.inflate(context, R.layout.item_main, null);
            viewPager = (WrapContentViewPager) view.findViewById(R.id.viewPager);
            viewPager.setAdapter(new ListFragmentAdapter(viewPager, getSupportFragmentManager()));
            indicator.setViewPager(viewPager);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    viewPager.resetHeight(position);
                    indicator.setCurrentItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            viewPager.resetHeight(0);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return view;
        }
    }
}
