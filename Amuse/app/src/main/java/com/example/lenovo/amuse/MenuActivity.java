package com.example.lenovo.amuse;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.amuse.fragment.FirstPage;
import com.example.lenovo.amuse.fragment.LovePlay;
import com.example.lenovo.amuse.fragment.Preferential;
import com.example.lenovo.amuse.fragment.Who;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    //首页
    private LinearLayout linearLayout_first;
    //同城爱玩
    private LinearLayout linearLayout_play;
    //优惠专区
    private LinearLayout linearLayout_preferential;
    //我的
    private LinearLayout linearLayout_who;

    //首页的实例
    private FirstPage firstPage;
    private LovePlay lovePlay;
    private Preferential preferential;
    private Who who;
    //图片
    private ImageView imageView_first;
    private ImageView imageView_play;
    private ImageView imageView_preferential;
    private ImageView imageView_who;
    //字体
    private TextView textView_first;
    private TextView textView_play;
    private TextView textView_preferential;
    private TextView textView_who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout_first = (LinearLayout) findViewById(R.id.first_page);
        linearLayout_first.setOnClickListener(this);

        linearLayout_play = (LinearLayout) findViewById(R.id.love_play);
        linearLayout_play.setOnClickListener(this);

        linearLayout_preferential = (LinearLayout) findViewById(R.id.preferential);
        linearLayout_preferential.setOnClickListener(this);

        linearLayout_who = (LinearLayout) findViewById(R.id.who);
        linearLayout_who.setOnClickListener(this);

        imageView_first = (ImageView) findViewById(R.id.first_img);
        imageView_play = (ImageView) findViewById(R.id.play_img);
        imageView_preferential = (ImageView) findViewById(R.id.preferential_img);
        imageView_who = (ImageView) findViewById(R.id.who_img);

        textView_first = (TextView) findViewById(R.id.first_text);
        textView_play = (TextView) findViewById(R.id.play_text);
        textView_preferential = (TextView) findViewById(R.id.preferential_text);
        textView_who = (TextView) findViewById(R.id.who_text);
        //初始化
        init();
    }

    /**
     * 初始化Fragment
     */
    private void init() {
        textColor();
        //获取Fragment的管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启Fragment事物
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        firstPage = new FirstPage();
        fragmentTransaction.add(R.id.id_frame, firstPage, "firstPage");
        textView_first.setTextColor(ContextCompat.getColor(this, R.color.text));
        imageView_first.setImageResource(R.mipmap.first_y);
        fragmentTransaction.commit();
    }

    /**
     * 所有字体都为默认颜色字体
     */
    public void textColor() {
        textView_first.setTextColor(ContextCompat.getColor(this, R.color.txt));
        textView_play.setTextColor(ContextCompat.getColor(this, R.color.txt));
        textView_preferential.setTextColor(ContextCompat.getColor(this, R.color.txt));
        textView_who.setTextColor(ContextCompat.getColor(this, R.color.txt));
    }

    /**
     * 所有图片都为默认图片
     */
    public void imgShow() {
        imageView_first.setImageResource(R.mipmap.first_w);
        imageView_play.setImageResource(R.mipmap.love_w);
        imageView_preferential.setImageResource(R.mipmap.preferential_w);
        imageView_who.setImageResource(R.mipmap.who_w);
    }

    @Override
    public void onClick(View v) {
        //获取Fragment的管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启Fragment事物
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        imgShow();
        textColor();
        hidFragment(fragmentTransaction);
        if (v.getId() == linearLayout_first.getId()) {
            textView_first.setTextColor(ContextCompat.getColor(this, R.color.text));
            imageView_first.setImageResource(R.mipmap.first_y);
            if (firstPage == null) {
                firstPage = new FirstPage();
                fragmentTransaction.add(R.id.id_frame, firstPage, "first");
            } else {
                fragmentTransaction.show(firstPage);
            }
        } else if (v.getId() == linearLayout_play.getId()) {
            imageView_play.setImageResource(R.mipmap.love_y);
            textView_play.setTextColor(ContextCompat.getColor(this, R.color.text));
            if (lovePlay == null) {
                lovePlay = new LovePlay();
                fragmentTransaction.add(R.id.id_frame, lovePlay, "play");
            } else {
                fragmentTransaction.show(lovePlay);
            }
        } else if (v.getId() == linearLayout_preferential.getId()) {
            imageView_preferential.setImageResource(R.mipmap.preferential_y);
            textView_preferential.setTextColor(ContextCompat.getColor(this, R.color.text));
            if (preferential == null) {
                preferential = new Preferential();
                fragmentTransaction.add(R.id.id_frame, preferential, "pre");
            } else {
                fragmentTransaction.show(preferential);
            }
        } else if (v.getId() == linearLayout_who.getId()) {
            imageView_who.setImageResource(R.mipmap.who_y);
            textView_who.setTextColor(ContextCompat.getColor(this, R.color.text));
            if (who == null) {
                who = new Who();
                fragmentTransaction.add(R.id.id_frame, who, "who");
            } else {
                fragmentTransaction.show(who);
            }
        }
        //提交
        fragmentTransaction.commit();
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction Fragment事物
     */
    private void hidFragment(FragmentTransaction fragmentTransaction) {
        if (firstPage != null) {
            fragmentTransaction.hide(firstPage);
        }
        if (lovePlay != null) {
            fragmentTransaction.hide(lovePlay);
        }
        if (preferential != null) {
            fragmentTransaction.hide(preferential);
        }
        if (who != null) {
            fragmentTransaction.hide(who);
        }
    }
}
