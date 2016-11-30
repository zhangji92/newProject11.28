package app.com.appnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

/**
 * 这个广播接收器用于监听短信发送的状态
 */
public class SendReceiver extends BroadcastReceiver {
    public SendReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断getResultCode()的值等于RESULT_OK就会提示短信发送成功
        if (getResultCode() == RESULT_OK) {
            // 短信发送成功
            Toast.makeText(context, "Send succeeded",Toast.LENGTH_LONG).show();
        }else {
            // 短信发送失败
            Toast.makeText(context, "Send failed",Toast.LENGTH_LONG).show();
        }

    }
}
