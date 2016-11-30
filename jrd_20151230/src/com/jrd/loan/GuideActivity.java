package com.jrd.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.umeng.analytics.MobclickAgent;

public class GuideActivity extends Activity {
    private View[] pageArr;
    private ImageView dot1;
    private ImageView dot2;
    private ImageView dot3;
    private ImageView dot4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_guide_layout);

        this.initViews();
    }

    protected void initViews() {
        this.pageArr = new View[]{
                View.inflate(GuideActivity.this, R.layout.loan_guide_one_layout, null),
                View.inflate(GuideActivity.this, R.layout.loan_guide_second_layout, null),
                View.inflate(GuideActivity.this, R.layout.loan_guide_third_layout, null),
                View.inflate(GuideActivity.this, R.layout.loan_guide_four_layout, null)};

        this.dot1 = (ImageView) findViewById(R.id.dot1);
        this.dot2 = (ImageView) findViewById(R.id.dot2);
        this.dot3 = (ImageView) findViewById(R.id.dot3);
        this.dot4 = (ImageView) findViewById(R.id.dot4);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final GuideAdpater guideAdpater = new GuideAdpater(this.pageArr);
        viewPager.setAdapter(guideAdpater);

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    dot1.setImageResource(R.drawable.loan_dot_pressed);
                    dot2.setImageResource(R.drawable.loan_dot_normal);
                    dot3.setImageResource(R.drawable.loan_dot_normal);
                    dot4.setImageResource(R.drawable.loan_dot_normal);
                } else if (position == 1) {
                    dot1.setImageResource(R.drawable.loan_dot_normal);
                    dot2.setImageResource(R.drawable.loan_dot_pressed);
                    dot3.setImageResource(R.drawable.loan_dot_normal);
                    dot4.setImageResource(R.drawable.loan_dot_normal);
                } else if (position == 2) {
                    dot1.setImageResource(R.drawable.loan_dot_normal);
                    dot2.setImageResource(R.drawable.loan_dot_normal);
                    dot3.setImageResource(R.drawable.loan_dot_pressed);
                    dot4.setImageResource(R.drawable.loan_dot_normal);
                } else if (position == 3) {
                    dot1.setImageResource(R.drawable.loan_dot_normal);
                    dot2.setImageResource(R.drawable.loan_dot_normal);
                    dot3.setImageResource(R.drawable.loan_dot_normal);
                    dot4.setImageResource(R.drawable.loan_dot_pressed);

                    Button btnStart = (Button) pageArr[position].findViewById(R.id.btnStart);
                    btnStart.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AppInfoPrefs.setFirstStartApp(false);

                            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                            //							if(AppInfoPrefs.isSetGestureLock()) {//如果设置了手势密码锁
                            //								Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                            //								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //								startActivity(intent);
                            //								finish();
                            //							} else {
                            //								Intent intent = new Intent(GuideActivity.this, GestureEditActivity.class);
                            //								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //								startActivity(intent);
                            //								finish();
                            //							}
                        }
                    });
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
    }

    private class GuideAdpater extends PagerAdapter {
        private View[] pageArr;

        public GuideAdpater(View[] pageArr) {
            this.pageArr = pageArr;
        }

        @Override
        public int getCount() {
            return this.pageArr.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(this.pageArr[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(this.pageArr[position]);

            return this.pageArr[position];
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }
}