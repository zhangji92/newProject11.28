package app.com.appnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.TokenWatcher;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {
    Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            //获取的信息放入bundle
            Bundle bundle = intent.getExtras();
            //提取短息消息-->使用pdu密钥来提取一个SMS pdus数组，每一个pud表示一条短信
            Object[] pdus = (Object[]) bundle.get("pdus");

            SmsMessage[] messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            //获取发送号码
            String address = messages[0].getOriginatingAddress();
            String fullMessage = "";
            for (SmsMessage message : messages) {
                //获取短信内容
                fullMessage += message.getMessageBody();
            }
            MessageMode m = new MessageMode(address, fullMessage);
            Message message = new Message();
            message.what = 100;
            message.obj = m;
            handler.sendMessage(message);

//            Toast.makeText(context,address,Toast.LENGTH_LONG).show();
//            Toast.makeText(context,fullMessage,Toast.LENGTH_LONG).show();

            //拦截广播
            abortBroadcast();
        }
    }
}
