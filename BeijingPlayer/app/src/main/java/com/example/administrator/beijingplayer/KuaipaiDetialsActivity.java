package com.example.administrator.beijingplayer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.mode.KuaipaiDetailsMessage;
import com.example.administrator.beijingplayer.mode.KuaipaiMessage;
import com.example.administrator.beijingplayer.util.HttpTools;
import com.example.administrator.beijingplayer.util.ModeCode;
import com.example.administrator.beijingplayer.util.ServiceMessage;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

public class KuaipaiDetialsActivity extends AppCompatActivity {

    private FinalBitmap finalBitmap = FinalBitmap.create(this);
    private HttpTools httpTools = HttpTools.getHttpTools();

    private ImageView userHead;
    private TextView username,time,pinglun,zan;
    private VideoView video;


    private KuaipaiDetailsMessage details;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
               switch (msg.what){
                   case ModeCode.KUAIPAI_WHAT_DETAILS:getKuaipaiMessage(msg.obj); break;
               }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuaipai_detials);

        initView();
    }

    /***
     * 初始化控件，获服务器数据
     */
    private void initView(){
        Intent intent = getIntent();
        String token = (String) intent.getSerializableExtra("token");
        String quick = (String) intent.getSerializableExtra("kuaipai");

        String uri = ModeCode.KUAIPAI_DETAILS+"&token="+token+"&qpid="+quick;
        Log.e("uri: ","-------"+uri );
        ServiceMessage<KuaipaiDetailsMessage> serviceMessage = new ServiceMessage<KuaipaiDetailsMessage>(uri,ModeCode.KUAIPAI_WHAT_DETAILS,new KuaipaiDetailsMessage());
        httpTools.getServiceMessage(handler,serviceMessage);

        userHead = (ImageView) findViewById(R.id.kuaipai_details_head);
        username =(TextView) findViewById(R.id.kuaipai_details_name);
        time = (TextView) findViewById(R.id.kuaipai_details_time);
        pinglun =(TextView) findViewById(R.id.kuaipai_details_pinglun);
        zan = (TextView) findViewById(R.id.kuaipai_details_like);
        video = (VideoView) findViewById(R.id.kuaipai_details_video);
    }

    /***
     * 获取从服务器获取到的数据
     * @param obj
     */
    private void getKuaipaiMessage(Object obj){
        if(obj != null&&obj instanceof KuaipaiDetailsMessage ){
             details = (KuaipaiDetailsMessage) obj;
            Log.e( "getKuaipaiMessage:??? ",details.getResultCode().getNickname() );
             setMessage(details);
        }

    }

    /***
     * 设置控件内容
     */
    private void setMessage(KuaipaiDetailsMessage details){
        finalBitmap.display(userHead,ModeCode.HTTP+details.getResultCode().getPic());
        username.setText(details.getResultCode().getNickname());
        time.setText(details.getResultCode().getAddtime());
        pinglun.setText(details.getResultCode().getCommentnum());
        zan.setText(details.getResultCode().getLikecount());
        video.setVideoPath(details.getResultCode().getVideo());
       // video.start();
        MediaController controller = new MediaController(this);
        video.setMediaController(controller);
        controller.setAnchorView(video);

    }
}
