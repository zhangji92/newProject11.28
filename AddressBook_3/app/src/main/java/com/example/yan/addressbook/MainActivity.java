package com.example.yan.addressbook;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yan.addressbook.util.ContentUtil;
import com.example.yan.addressbook.mode.Friend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BaseAdapter baseAdapter;

    private ContentUtil contentUtil;

    private ContentResolver contentResolver;

    private ListView listView;
    private ImageView addFriend;

    private List<Friend> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }
    private void initView(){
        contentUtil = ContentUtil.getContentUtil();
        listView =(ListView) findViewById(R.id.main_list);
        //查询数据
        friends = new ArrayList<>();
        friends.addAll(contentUtil.selectAllPhone(this));
        addBase();
        listView.setAdapter(baseAdapter);

        addFriend =(ImageView) findViewById(R.id.main_tianjia);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddFriendActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addBase(){
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return friends.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(MainActivity.this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setPadding(0,40,40,40);
                Paint paint = textView.getPaint();
                paint.setFakeBoldText(true);
                textView.setText(friends.get(position).getFriendName());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                        intent.putExtra("friend",  friends.get(position));
                        startActivity(intent);
                    }
                });
                textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setItems(new String[]{"查看", "编辑", "删除"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0://查看
                                        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                                        intent.putExtra("friend",  friends.get(position));
                                        startActivity(intent);
                                        break;
                                    case 1://编辑
                                        Intent bianji = new Intent(MainActivity.this,AddFriendActivity.class);
                                        bianji.putExtra("edit",  friends.get(position));
                                        startActivity(bianji);
                                        break;
                                    case 2://删除
                                       int res = contentUtil.deleteFriend(MainActivity.this,friends.get(position));
                                        if(res>0){
                                            Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                            friends.clear();
                                            friends.addAll(contentUtil.selectAllPhone(MainActivity.this));
                                            baseAdapter.notifyDataSetChanged();
                                        }else{
                                            Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                                        }
                                        break;

                                }
                            }
                        });
                        AlertDialog alert =   builder.create();
                        Window window = alert.getWindow();
                        alert.show();
                        window.setGravity(Gravity.BOTTOM);

                        return true;
                    }
                });
                return textView;
            }
        };
    }

    @Override
    protected void onRestart() {
        friends.clear();
        friends.addAll(contentUtil.selectAllPhone(this));
        baseAdapter.notifyDataSetChanged();
        super.onRestart();

    }


}
