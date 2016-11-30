package app.com.appnotification;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        Button bnt_start = (Button) findViewById(R.id.start);
        bnt_start.setOnClickListener(this);

        Button bnt_pause = (Button) findViewById(R.id.pause);
        bnt_pause.setOnClickListener(this);

        Button bnt_stop = (Button) findViewById(R.id.stop);
        bnt_stop.setOnClickListener(this);
        init();
    }

    private void init() {
        //初始化音频
        //mediaPlayer = MediaPlayer.create(this, R.raw.shi);

        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "shi.mp3")));
        try {
            //异步加载--同步加载崩溃(mediaPlayer.prepare();)
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mediaPlayer=new MediaPlayer();
//        try {
//            File file=new File(Environment.getExternalStorageDirectory(),"shi.mp3");
//            mediaPlayer.setDataSource(file.getAbsolutePath());
//            mediaPlayer.prepareAsync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 开始播放
     */
    private void startPlayer() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    private void pausePlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 停止播放
     */
    private void stopPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            init();
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
        //停止
        mediaPlayer.stop();
        //释放资源
        mediaPlayer.release();
    }
}
