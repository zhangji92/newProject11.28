package app.com.appnotification;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import java.io.File;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener{
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView= (VideoView) findViewById(R.id.video_view);

        Button bnt_start = (Button) findViewById(R.id.start);
        bnt_start.setOnClickListener(this);

        Button bnt_pause = (Button) findViewById(R.id.pause);
        bnt_pause.setOnClickListener(this);

        Button bnt_stop = (Button) findViewById(R.id.stop);
        bnt_stop.setOnClickListener(this);
        //初始化
        init();
    }

    private void init() {
        //初始化视频
        videoView.setVideoPath((new File(Environment.getExternalStorageDirectory(),"VID.mp4")).getPath());
    }
    /**
     * 开始播放
     */
    private void startPlayer() {
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    /**
     * 暂停播放
     */
    private void pausePlayer() {
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    /**
     * 停止播放
     */
    private void stopPlayer() {
        if (videoView.isPlaying()) {
            videoView.resume();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                startPlayer();
                break;
            case R.id.pause:
                pausePlayer();
                break;
            case R.id.stop:
                stopPlayer();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        videoView.suspend();
    }
}
