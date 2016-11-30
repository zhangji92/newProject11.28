package com.allactivity.pracitic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.allactivity.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.allactivity.volley.VolleyRequest.context;

/**
 * Created by 张继 on 2016/11/24.
 * 练习
 */

public class PracticeActivity extends Activity implements View.OnClickListener{
    private PracticeMode practiceMode;
    File sdFile = null;//json文件

    List<PracticeMode.ExperienceBean> mList = new ArrayList<>();
    private ListView mListView;
    Button bnt_share;
    Button bnt_dictation;//听写
    LinearLayout linearLayout;
    LinearLayout linearLayout_outer_linear;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_activity);
        initViews();
        readFile();//读取文件
        JsonParseObject();
        initData();
    }


    /**
     * 初始化控件
     */
    private void initViews() {
        mListView = (ListView) findViewById(R.id.practice_listView);
        bnt_share = (Button) findViewById(R.id.practice_share);
        bnt_dictation = (Button) findViewById(R.id.practice_dictation);
        linearLayout = (LinearLayout) findViewById(R.id.practice_linear);
        imageView = (ImageView) findViewById(R.id.practice_file_img);
        //practice_outer_linear
        linearLayout_outer_linear = (LinearLayout) findViewById(R.id.practice_outer_linear);
        bnt_share.setOnClickListener(this);
        bnt_dictation.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.practice_share:
                linearLayout.setVisibility(View.GONE);
                //去除title    
//                requestWindowFeature(Window.FEATURE_NO_TITLE);
                //去掉Activity上面的状态栏
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                File f = saveBitmap(imgShare(PracticeActivity.this));
                readImg(f, imageView);
                linearLayout_outer_linear.setBackgroundResource(R.drawable.guide_image1);
                break;
            case R.id.practice_dictation:
                startActivity(new Intent(this,DictationActivity.class));
                break;
        }
    }



    /**
     * 保存图片
     *
     * @param bitmap 图片
     */
    private File saveBitmap(Bitmap bitmap) {
        File sdFile = null;//路径
        File saveFile = null;//图片文件
        FileOutputStream fos = null;//字符输出流
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            sdFile = Environment.getExternalStorageDirectory();
            saveFile = new File(sdFile, "img.png");
        }
        try {
            fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
            Log.e("PracticeActivity", saveFile + "practiceMode ");
            //可以使用广播把根目录下的图片无痕迹的带到相册
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri uri = Uri.fromFile(saveFile);
//            intent.setData(uri);
//            this.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return saveFile;
    }

    private void readImg(File file, ImageView img) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            Bitmap b = BitmapFactory.decodeStream(bis);
            img.setImageBitmap(b);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 分享
     */
    private Bitmap imgShare(Activity activity) {
        View view = activity.getWindow().getDecorView();//获取需要截图的view
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b = view.getDrawingCache();
        //获取状态栏的高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);
        //获取屏幕宽和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        //去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(b, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 加载数据
     */
    private void initData() {
        for (int i = 0; i < practiceMode.getExperience().size(); i++) {
            mList.add(practiceMode.getExperience().get(i));
        }
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size() == 0 ? 0 : mList.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(PracticeActivity.this).inflate(R.layout.item_layout, null);
                TextView textView_content = (TextView) view.findViewById(R.id.item_layout_content);
                textView_content.setText(mList.get(position).getContent());
                TextView textView_title = (TextView) view.findViewById(R.id.item_layout_title);
                textView_title.setText(mList.get(position).getTitle());
                ImageView imageView = (ImageView) view.findViewById(R.id.item_layout_img);
                Picasso.with(PracticeActivity.this).load(mList.get(position).getImages().get(0).getUrl()).into(imageView);
                return view;
            }
        });

    }

    /**
     * 读取文件
     *
     * @return
     */
    public File readFile() {
        File downFile = null;//外置的存储目录
        BufferedReader br = null;//字符输入流
        String tempString = "";//临时变量接受json字符串
        BufferedWriter bw = null;//字符输出流
        try {
            //判断sd卡是否存在
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //外置的存储目录
                downFile = Environment.getExternalStorageDirectory();
                //把下载下来的文件写到sd卡文件里面
                sdFile = new File(downFile, "zj.txt");
            }
            //创建字符输入流（创建字节输入流对象和文件相关联）
            br = new BufferedReader(new InputStreamReader(getAssets().open("data.txt")));
            //创建字符输出流（创建字符输出流对象和文件相关联）
            bw = new BufferedWriter(new FileWriter(sdFile));
            //遍历文件内容
            while ((tempString = br.readLine()) != null) {
                bw.write(tempString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null && bw != null) {
                try {
                    br.close();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sdFile;
    }

    /**
     *
     */
    private void JsonParseObject() {
        String str = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(sdFile));
            String tempString = "";
            while ((tempString = br.readLine()) != null) {
                str += tempString;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        practiceMode = gson.fromJson(str, PracticeMode.class);

        Log.e("PracticeActivity", "practiceMode " + practiceMode.getExperience().get(0).getImages().get(0).getUrl());

    }

}
