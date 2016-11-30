package com.yoka.mrskin.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.util.YKFile;

public class WriteTextLayout extends BaseActivity implements OnClickListener
{
    private EditText mEditText;
    private TextView mSuccess/*,mTitle*/;
    private LinearLayout mBack;
    private String mPosition;
    private static String CACHE_TEXT = "writetitle";
    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_text_layout);
        init();
        getIntentText();
    }
    private void getIntentText(){
        Intent getText = getIntent();
        String newGetText = getText.getStringExtra("text");
        mPosition = getText.getStringExtra("position");
        if(newGetText != null){
            mEditText.setText(newGetText);
        }
    }

    private void init(){

        mEditText = (EditText) findViewById(R.id.write_text_write);
        mSuccess = (TextView) findViewById(R.id.write_text_success);
        //mTitle = (TextView) findViewById(R.id.write_edit_title);
        mBack = (LinearLayout) findViewById(R.id.write_text_layout_back);
        mBack.setOnClickListener(this);
        mSuccess.setOnClickListener(this);
//        mEditText.setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    hideKeyBord();
//                    mSearchText = mEditText.getText().toString().trim();
//                    if (TextUtils.isEmpty(mSearchText)) {
//                        hideKeyBord();
//
//                    } else {
//                        TrackManager.getInstance().addTrack(
//                                TrackUrl.SEARCH_CLICK + mSearchText);
//                        Intent successText = new Intent(WriteTextLayout.this,WriteExperienceActivity.class);
//                        successText.putExtra("text", mSearchText);
//                        setResult(RESULT_OK,successText);
//                        finish(); 
//                    }
//                }
//                return false;
//            }
//        });


    }

    @Override
    public void onClick(View v)
    {
        String text = mEditText.getText().toString().trim();
        switch (v.getId()) {
        case R.id.write_text_success:
            Intent successText = new Intent(WriteTextLayout.this,WriteExperienceActivity.class);
            successText.putExtra("text", text);
            successText.putExtra("position", mPosition);
            setResult(RESULT_OK,successText);
            finish(); 
            break;
        case R.id.write_text_layout_back:
            if(text.length() == 0){
                finish();
            }else{
                initDialog();
            }
            break;
        default:
            break;
        }
    }

    private void initDialog() {
        AlertDialog.Builder builder = new Builder(WriteTextLayout.this);
        builder.setMessage("确认退出吗?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                WriteTextLayout.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //在onKeyDown(int keyCode, KeyEvent event)方法中调用此方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            String text = mEditText.getText().toString().trim();
            if(text.length() == 0){
                finish();
            }else{
                initDialog();
            }
        }
        return false;
    }

    public String getTextData() {
        return loadDataFromFile();
    }

    public  boolean saveDataToFile(String text) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(text);
            data = baos.toByteArray();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }

        YKFile.save(CACHE_TEXT, data);
        return true;
    }

    private String loadDataFromFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_TEXT);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            String objectData = (String) ois
            .readObject();
            return objectData;
        } catch (Exception e) {
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
}
