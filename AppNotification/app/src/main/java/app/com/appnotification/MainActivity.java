package app.com.appnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //图片的路径
    private Uri imageUri;
    //图片的控件
    ImageView imageView_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //通知控件
        Button bnt_notification = (Button) findViewById(R.id.notification);
        bnt_notification.setOnClickListener(this);
        //测试短信
        Button bnt_message = (Button) findViewById(R.id.message);
        bnt_message.setOnClickListener(this);
        //调用相机
        Button bnt_camera = (Button) findViewById(R.id.camera);
        bnt_camera.setOnClickListener(this);
        //调用图库
        Button bnt_chart = (Button) findViewById(R.id.chart);
        bnt_chart.setOnClickListener(this);
        //播放音频
        Button bnt_media = (Button) findViewById(R.id.media_player);
        bnt_media.setOnClickListener(this);
        //播放音频
        Button bnt_video = (Button) findViewById(R.id.video);
        bnt_video.setOnClickListener(this);

        //显示照片
        imageView_photo = (ImageView) findViewById(R.id.camera_img);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification:
                sendNotification();
                break;
            case R.id.message:
                Intent intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.camera:
                callCamera();
                break;
            case R.id.chart:
                callChart();
                break;
            case R.id.media_player:
                startActivity( new Intent(this, MediaPlayerActivity.class));
                break;
            case R.id.video:
                startActivity( new Intent(this, VideoActivity.class));
                break;
        }
    }

    /**
     * 调用图库裁剪图片
     */
    private void callChart() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        //图片类型
        intent.setType("image/*");
        //是否裁剪
        intent.putExtra("crop", true);
        //是否缩放
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent, 300);
    }

    /**
     * 调用照相机
     */
    private void callCamera() {
        //创建File对象，用于存储拍照后的照片
        File outFile = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
        //判断是否有这个文件
        if (outFile.exists()) {
            //删除这个文件
            outFile.delete();
        } else {
            try {
                //创建这个文件
                outFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //file转成uri
        imageUri = Uri.fromFile(outFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //启动相机程序
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                trimImage(imageUri);
                break;
            case 200:
                Bitmap bitmap = callImage(imageUri);
                displayPhoto(bitmap);
                break;
            case 300:
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null) {
                    //此处注释掉的部分是针对android 4.4路径修改的一个测试
                    //有兴趣的读者可以自己调试看看
                    imageUri = data.getData();
                    trimImage(imageUri);
                }
                break;
        }
    }


    /**
     * 裁剪图片
     */
    private void trimImage(Uri uri) {
        //跳到裁剪页面
        Intent intent = new Intent("com.android.camera.action.CROP");
        //数据和类型
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        //是否缩放
        intent.putExtra("scale", true);
        //剪切图片-->把剪切出来图片的路径放入imageUri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 启动裁剪程序
        startActivityForResult(intent, 200);
    }

    /**
     * 解析图片
     */
    private Bitmap callImage(Uri uri) {
        Bitmap bitmap = null;
        try {
            //把uri对象转成InputStream流
            InputStream inputStream = getContentResolver().openInputStream(uri);
            //把InputStream解析成Bitmap
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据bitmap压缩图片
     *
     * @param bitmap
     */
    private void displayPhoto(Bitmap bitmap) {
        //获取DisplayMetrics实例-->比例压缩(尺寸)
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        if (bitmap.getWidth() <= screenWidth) {
            imageView_photo.setImageBitmap(bitmap);
        } else {
            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight() * screenWidth / bitmap.getWidth(), true);
            imageView_photo.setImageBitmap(bmp);
        }
    }

    /**
     * 1.首先需要一个 NotificationManager
     * 2.需要创建一个 Notification 对象
     */
    private void sendNotification() {
        //获取系统服务
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //需要创建一个 Notification 对象
        Notification.Builder builder = new Notification.Builder(this);
        //设置通知图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //提示信息
        builder.setTicker("显示信息");
        //标题
        builder.setContentTitle("This is title");
        //内容
        builder.setContentText("This is Context");
        //创建通知的时间
        builder.setWhen(System.currentTimeMillis());
        //音乐
        builder.setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "5"));

        //条转页面
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        //点击之后通知取消
        builder.setAutoCancel(true);

        //震动
        builder.setVibrate(new long[]{0, 1000, 1000, 1000});

        //取消通知
//        Notification notification=builder.build();
//        notification.flags=Notification.FLAG_AUTO_CANCEL;
//        notification.ledARGB= Color.BLUE;
//        notification.ledOnMS=1000;
//        notification.ledOffMS=1000;
//        notification.flags=Notification.FLAG_SHOW_LIGHTS;
        //
        builder.setDefaults(Notification.DEFAULT_ALL);
        //发生同知
        notificationManager.notify(100, builder.build());
//        notificationManager.notify(100,notification);


    }

}
