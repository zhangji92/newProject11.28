package com.example.administrator.beijingplayer;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.beijingplayer.application.App;
import com.example.administrator.beijingplayer.fragment.ButtomFragment1;
import com.example.administrator.beijingplayer.fragment.ButtomFragment2;
import com.example.administrator.beijingplayer.fragment.ButtomFragment3;
import com.example.administrator.beijingplayer.fragment.ButtomFragment4;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //底部菜单布局
    LinearLayout linear1;
    LinearLayout linear2;
    LinearLayout linear3;
    LinearLayout linear4;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;

    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;

    List<LinearLayout> layoutList;
    List<ImageView> imgList;
    List<TextView> textList;
    List<Integer> imgResult1;
    List<Integer> imgResult2;

    FragmentManager fragmentManager = getSupportFragmentManager();


    //碎片
    ButtomFragment1 btnf1;
    ButtomFragment2 btnf2;
    ButtomFragment3 btnf3;
    ButtomFragment4 btnf4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //加载控件
        loadView();
        //加载碎片
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        btnf1 = new ButtomFragment1();

        transaction.add(R.id.main_frame,btnf1,"tag"+R.id.main_buttomMenu1);
        transaction.commit();

    }

    /**
     * 加载控件
     */
    private void loadView(){
        layoutList = new ArrayList<>();
        linear1 = (LinearLayout) findViewById(R.id.main_buttomMenu1);
        linear1.setOnClickListener(this);
        layoutList.add(linear1);
        linear2 = (LinearLayout) findViewById(R.id.main_buttomMenu2);
        linear2.setOnClickListener(this);
        layoutList.add(linear2);
        linear3 = (LinearLayout) findViewById(R.id.main_buttomMenu3);
        linear3.setOnClickListener(this);
        layoutList.add(linear3);
        linear4 = (LinearLayout) findViewById(R.id.main_buttomMenu4);
        linear4.setOnClickListener(this);
        layoutList.add(linear4);

        imgList = new ArrayList<>();
        img1 = (ImageView) findViewById(R.id.main_menu1_img);
        imgList.add(img1);
        img2 = (ImageView) findViewById(R.id.main_menu2_img);
        imgList.add(img2);
        img3 = (ImageView) findViewById(R.id.main_menu3_img);
        imgList.add(img3);
        img4 = (ImageView) findViewById(R.id.main_menu4_img);
        imgList.add(img4);

        textList = new ArrayList<>();
        text1 = (TextView) findViewById(R.id.main_menu1_text);
        textList.add(text1);
        text2 = (TextView) findViewById(R.id.main_menu2_text);
        textList.add(text2);
        text3 = (TextView) findViewById(R.id.main_menu3_text);
        textList.add(text3);
        text4 = (TextView) findViewById(R.id.main_menu4_text);
        textList.add(text4);

        imgResult1 = new ArrayList<>();
        imgResult1.add(R.drawable.menu_home1);
        imgResult1.add(R.drawable.menu_play1);
        imgResult1.add(R.drawable.menu_discount1);
        imgResult1.add(R.drawable.menu_my1);

        imgResult2 = new ArrayList<>();
        imgResult2.add(R.drawable.menu_home2);
        imgResult2.add(R.drawable.menu_play2);
        imgResult2.add(R.drawable.menu_discount2);
        imgResult2.add(R.drawable.menu_my2);
    }

    /**
     * 底部菜单点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.main_buttomMenu1:
                changeFragment(viewId);
                break;
            case R.id.main_buttomMenu2:
                changeFragment(viewId);
                break;
            case R.id.main_buttomMenu3:
                changeFragment(viewId);
                break;
            case R.id.main_buttomMenu4:
                changeFragment(viewId);
                break;
        }
    }

    /**
     * 显示点击事件对应的fragment
     * @param viewid
     */
    private void changeFragment(int viewid){
        FragmentTransaction transation = fragmentManager.beginTransaction();
        hideFragment(transation);
        changeColor(viewid);
        switch (viewid){
            case R.id.main_buttomMenu1:
                transation.show(btnf1); break;
            case R.id.main_buttomMenu2:
                if(btnf2==null){
                    btnf2 = new ButtomFragment2();
                    transation.add(R.id.main_frame,btnf2,"tag"+R.id.main_buttomMenu2);
                }else{
                    transation.show(btnf2);
                }
                break;
            case R.id.main_buttomMenu3:
                if(btnf3==null){
                    btnf3 = new ButtomFragment3();
                    transation.add(R.id.main_frame,btnf3,"tag"+R.id.main_buttomMenu3);
                }else{
                    transation.show(btnf3);
                }
                break;
            case R.id.main_buttomMenu4:
                if(btnf4==null){
                    btnf4 = new ButtomFragment4();
                    transation.add(R.id.main_frame,btnf4,"tag"+R.id.main_buttomMenu4);
                }else{
                    transation.show(btnf4);
                }
                break;
        }
        transation.commit();
    }
    /**
     * 改变layout控件的颜色
     * @param id
     */
    private void changeColor(int id){
        for(int i=0;i<imgList.size();i++){
            ImageView img =imgList.get(i);
            TextView text = textList.get(i);
            if(id == layoutList.get(i).getId()){
                img.setImageResource(imgResult2.get(i));
                text.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
            }else{
                img.setImageResource(imgResult1.get(i));
                text.setTextColor(ContextCompat.getColor(this,R.color.textcolor));
            }
        }
    }

    /**
     * 隐藏所有的fragment
     * @param transation
     */
    private void hideFragment( FragmentTransaction transation){
        if(btnf1!=null){
            transation.hide(btnf1);
        }
        if(btnf2!=null){
            transation.hide(btnf2);
        }
        if(btnf3!=null){
            transation.hide(btnf3);
        }
        if(btnf4!=null){
            transation.hide(btnf4);
        }
    }

}
