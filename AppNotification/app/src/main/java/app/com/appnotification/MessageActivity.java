package app.com.appnotification;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity{
    private MessageReceiver messageReceiver;
    private SendReceiver sendReceiver;
    //号码
    private TextView textView_sender;
    private TextView textView_content;
    private EditText editText_to;
    private EditText editText_imput;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    MessageMode m = (MessageMode) msg.obj;
                    textView_sender.setText(m.address);
                    textView_content.setText(m.content);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //获取广播接收器的实例
        messageReceiver = new MessageReceiver();
        messageReceiver.setHandler(handler);
        //获取过滤器的实例
        IntentFilter intentFilter = new IntentFilter();
        //一条值为android.provider.Telephony.SMS_RECEIVED 的广播
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //注册广播
        registerReceiver(messageReceiver, intentFilter);

        //短信号码和内容
        textView_sender = (TextView) findViewById(R.id.sender);
        textView_content = (TextView) findViewById(R.id.content);


        IntentFilter sendFilter=new IntentFilter();
        //优先级
        sendFilter.setPriority(100);
        //发送短信的权限
        sendFilter.addAction("SENT_SMS_ACTION");
        //获取广播接收器的实例
        sendReceiver=new SendReceiver();
        //注册
        registerReceiver(sendReceiver, sendFilter);
        //获取发送短信地址的控件
        editText_to= (EditText) findViewById(R.id.to);
        //获取发送短信内容的控件
        editText_imput= (EditText) findViewById(R.id.msg_input);

        Button button_send= (Button) findViewById(R.id.send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取SmsManager实例
                SmsManager smsManager=SmsManager.getDefault();
                //发起信息（意图）
                Intent sentIntent = new Intent("SENT_SMS_ACTION");
                //PendingIntent相当于延迟的Intent-->获取PendingIntent对象
                PendingIntent pi = PendingIntent.getBroadcast(MessageActivity.this, 0, sentIntent, 0);
                //发送短信
                /**
                 *  String destinationAddress, String scAddress, String text,PendingIntent sentIntent, PendingIntent deliveryIntent
                 *  1.第一个参数是指定发送短信的号码
                 *  2.
                 *  3.第三个参数指定发送短信的内容
                 *  4.
                 *
                 */
                smsManager.sendTextMessage(editText_to.getText().toString(),null,editText_imput.getText().toString(),pi,null);
            }
        });
    }

    /***
     * 程序退出时注销
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        unregisterReceiver(messageReceiver);
        unregisterReceiver(sendReceiver);
    }


}
