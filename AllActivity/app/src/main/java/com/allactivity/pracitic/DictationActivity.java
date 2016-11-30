package com.allactivity.pracitic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.allactivity.R;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import java.util.ArrayList;


/**
 * Created by 93836 on 2016/11/29.
 * 语音听写
 */

public class DictationActivity extends Activity implements View.OnClickListener {


    MyRecognizerListener myRecognizerListener;
    Button bnt_dictation;
    SpeechRecognizer mIat;
    String strDictation="";
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dication_activity);
        initViews();
        initData();


    }


    private void initData() {
        myRecognizerListener = new MyRecognizerListener();
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(this, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        bnt_dictation.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dication_bnt:
                editText.setText(null);
                strDictation="";
                //3.开始听写
                mIat.startListening(myRecognizerListener);
                break;
        }
    }

    private void initViews() {
        bnt_dictation = (Button) findViewById(R.id.dication_bnt);
        editText= (EditText) findViewById(R.id.dictation_edit);

    }
    //听写监听器
    class MyRecognizerListener implements RecognizerListener {

        //音量值0~30
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            Log.e("Result", "onVolumeChanged" + i);
        }

        //开始录音
        @Override
        public void onBeginOfSpeech() {
            Log.e("Result", "start");
        }

        //结束录音
        @Override
        public void onEndOfSpeech() {
            Log.e("Result", "end");
        }


        //听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        //关于解析Json的代码可参见MscDemo中JsonParser类；
        //isLast等于true时会话结束。
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            Log.e("Result", recognizerResult.getResultString());

            strDictation+=parseVoice(recognizerResult.getResultString());
            editText.setText(strDictation);
            Log.e("Result", strDictation);

        }

        //会话发生错误回调接口
        @Override
        public void onError(SpeechError speechError) {
            //获取错误码描述
            speechError.getPlainDescription(true);
        }

        //扩展用接口
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }
    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }
    /**
     * 语音对象封装
     */
    public class Voice {

        public ArrayList<WSBean> ws;

        public class WSBean {
            public ArrayList<CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }



}
