package com.measureactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by lenovo on 2016/10/21.
 *
 */

public class ListViewActivity extends FragmentActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);
        ListView listView= (ListView) findViewById(R.id.listView);
        //ListView顶部布局
        View header=View.inflate(this,R.layout.header,null);
        listView.addHeaderView(header);
        listView.setAdapter(new CustomAdapter(this));
    }
    public class CustomAdapter extends BaseAdapter{

        private View view;
        private  CustomViewPager customViewPager;

        public CustomAdapter(Context context) {
            view=View.inflate(context,R.layout.item_main,null);
            customViewPager= (CustomViewPager) view.findViewById(R.id.viewPager);
            customViewPager.setAdapter(new ListFragmentAdapter(customViewPager,getSupportFragmentManager()));
            customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    customViewPager.resetHeight(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            customViewPager.resetHeight(0);
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
