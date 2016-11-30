package com.example.lenovo.amuse.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.LovePlayMode;
import com.example.lenovo.amuse.mode.PlaceMode;
import com.example.lenovo.amuse.mode.VerificationCode;
import com.example.lenovo.amuse.util.BaseUri;
import com.example.lenovo.amuse.util.ServiceMessage;

import net.tsz.afinal.FinalBitmap;

/**
 * Created by 张继 on 2016/10/27.
 * 添加拼桌接口
 */

public class TableAddActivity extends BaseActivity implements View.OnClickListener {
    private PlaceMode.ResultCodeBean mPlaceMode;
    private ImageView imageView;
    TextView textView_title;
    TextView textView_single;
    TextView textView_address;
    TextView textView_distance;
    TextView textView_content;
    //发布
    TextView textView_release;
    EditText editText_name;
    EditText editText_title;

    FinalBitmap mFinalBitmap;
    String token;
    String shopId;
    String bid;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.TABLE_ADD_CODE:
                    parserMode(msg.obj);
                    finish();
                    break;
            }
        }
    };

    private void parserMode(Object obj) {
        VerificationCode verificationCode = null;
        if (obj != null && obj instanceof VerificationCode) {
            verificationCode = (VerificationCode) obj;
        }
        if (verificationCode != null) {
            if (verificationCode.getMessage().contains("成功")) {
                Toast.makeText(TableAddActivity.this, "发布" + verificationCode.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(TableAddActivity.this, "发布" + verificationCode.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_add);
        mFinalBitmap = FinalBitmap.create(this);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        imageView = (ImageView) findViewById(R.id.table_add_add);
        imageView.setOnClickListener(this);
        //发布
        textView_release = (TextView) findViewById(R.id.toolBar_release);
        textView_release.setOnClickListener(this);
        //主题
        textView_title = (TextView) findViewById(R.id.table_add_title);
        //接单数量
        textView_single = (TextView) findViewById(R.id.table_add_single);
        //地址
        textView_address = (TextView) findViewById(R.id.table_add_address);
        //距离
        textView_distance = (TextView) findViewById(R.id.table_add_location);
        //内容
        textView_content = (TextView) findViewById(R.id.table_add_content);
        //群聊名称
        editText_name = (EditText) findViewById(R.id.table_add_name);
        //拼桌标题
        editText_title = (EditText) findViewById(R.id.table_add_juvenile);
        token = ((MyApplication) getApplication()).getSuccessMode().getResultCode().getToken();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (data != null) {
                mPlaceMode = (PlaceMode.ResultCodeBean) data.getSerializableExtra("mode");
                textView_title.setText(mPlaceMode.getShopname());
                Drawable drawable = getResources().getDrawable(R.mipmap.location);
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView_distance.setCompoundDrawables(drawable, null, null, null);
                textView_distance.setText(mPlaceMode.getJuli());
                textView_address.setText(mPlaceMode.getAddress());
                textView_single.setText("接单数量" + mPlaceMode.getOrdernum() + "单");
                textView_content.setText(mPlaceMode.getContent());
                String img = BaseUri.BASE + mPlaceMode.getPic();
                mFinalBitmap.display(imageView, img);
                bid = mPlaceMode.getBid();
                shopId = mPlaceMode.getId();

            } else {
                Toast.makeText(this, "没选中内容", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.table_add_add:
                Intent intent = new Intent(TableAddActivity.this, PlaceActivity.class);
                intent.putExtra("Table", "flag");
                startActivityForResult(intent, BaseUri.ACTIVITY_RETURN);
                break;
            case R.id.toolBar_release:
                String name = editText_name.getText().toString();
                String title = editText_title.getText().toString();
                String uri = BaseUri.TABLE_LIST_ADD + "&token=" + token + "&bid=" + bid + "&shopid=" + shopId + "&lat=" + "1" + "&lng=" + "1" + "&name=" + name + "&title=" + title;
                ServiceMessage<VerificationCode> serviceMessage = new ServiceMessage<VerificationCode>(uri, BaseUri.TABLE_ADD_CODE, new VerificationCode());
                httpTools.getServiceMessage(mHandler, serviceMessage);
                break;

        }
    }
}
