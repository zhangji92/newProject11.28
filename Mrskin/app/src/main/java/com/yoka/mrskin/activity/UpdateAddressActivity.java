package com.yoka.mrskin.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKUserAddress;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.UpdateAddressCallback;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.YKGeneralCallBack;

public class UpdateAddressActivity extends BaseActivity implements
OnClickListener
{
    private View mBackBtn;
    private Boolean isChangeAddress = false;
    private YKUserAddress mAddresInfo;
    private TextView mSaveText;
    private EditText mNameEdit, mPhoneEdit, mAreaEdit, mDetailEdit,mNumberEdit;
    private String mName = "", mPhone = "", mArea = "", mAddress = "",
            mZipNumber = "", mCity = "", mProvince = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        initView();
        getData();
    }

    private void initView() {
        mBackBtn = findViewById(R.id.update_address_back);
        mSaveText = (TextView) findViewById(R.id.update_address_save);
        mSaveText.setTextColor(0XFFFF5934);

        mBackBtn.setOnClickListener(this);
        mSaveText.setOnClickListener(this);

        mNameEdit = (EditText) findViewById(R.id.update_address_name_edit);
        mPhoneEdit = (EditText) findViewById(R.id.update_address_phone_edit);
        mAreaEdit = (EditText) findViewById(R.id.update_address_area_edit);
        mDetailEdit = (EditText) findViewById(R.id.update_address_detail_edit);
        mNumberEdit = (EditText) findViewById(R.id.update_address_number_edit);
    }   

    private void getData() {
        YKUpdateUserInfoManager.getInstance().requestAddress(
                new UpdateAddressCallback() {

                    @Override
                    public void callback(YKResult result, YKUserAddress address) {
                        if (result.isSucceeded() && address != null) {
                            mAddresInfo = address;
                            updateUI(address);
                        }
                    }
                });
    }

    private void updateUI(YKUserAddress address) {
        if (!TextUtils.isEmpty(address.getmName())) {
            mNameEdit.setText(address.getmName());
        }
        if (!TextUtils.isEmpty(address.getmTtelephone())) {
            mPhoneEdit.setText(address.getmTtelephone());
        }
        if (!TextUtils.isEmpty(address.getmCity())) {
            mCity = address.getmCity();
            mAreaEdit.setText(mCity + " " + mProvince);
        }
        if (!TextUtils.isEmpty(address.getmProvince())) {
            mProvince = address.getmProvince();
            mAreaEdit.setText(mCity + " " + mProvince);
        }
        if (!TextUtils.isEmpty(address.getmAddress())) {
            mDetailEdit.setText(address.getmAddress());
        }

        if (!TextUtils.isEmpty(address.getmZipcode())) {
            mNumberEdit.setText(address.getmZipcode());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.update_address_back:
            isChangeAddress = iSChangeAddress();
            if(isChangeAddress){
                finish();
            }else{
                exitDialog();
            }
            break;
        case R.id.update_address_save:
            checkInfo();
            break;
        }
    }
    private boolean iSChangeAddress(){
        if(mAddresInfo != null){
            //收货人
            if(!TextUtils.isEmpty(mAddresInfo.getmName())){
                if(mAddresInfo.getmName().toString().trim().equals(mNameEdit.getText().toString().trim())){
                    //手机号
                    if(!TextUtils.isEmpty(mAddresInfo.getmTtelephone())){
                        if(mAddresInfo.getmTtelephone().toString().trim().equals(mPhoneEdit.getText().toString().trim())){
                            //地区
                            if(!TextUtils.isEmpty(mAddresInfo.getmCity())){
                                if(mAddresInfo.getmCity().toString().trim().equals(mAreaEdit.getText().toString().trim())){
                                    //地址
                                    if(!TextUtils.isEmpty(mAddresInfo.getmAddress())){
                                        if(mAddresInfo.getmAddress().toString().trim().equals(mDetailEdit.getText().toString().trim())){
                                            //邮编
                                            if(!TextUtils.isEmpty(mAddresInfo.getmZipcode())){
                                                if(mAddresInfo.getmZipcode().toString().trim().equals(mNumberEdit.getText().toString().trim())){
                                                    return true;
                                                }else{
                                                    return false;
                                                }
                                            }
                                        }else{
                                            return false;
                                        }
                                    }
                                }else{
                                    return false;
                                }
                            }
                        }else{
                            return false;
                        }
                    }
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    private void checkInfo() {
        if (TextUtils.isEmpty(mNameEdit.getText())) {
            Toast.makeText(UpdateAddressActivity.this, "请输入姓名",
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mPhoneEdit.getText())) {
            Toast.makeText(UpdateAddressActivity.this, "请输入手机号码",
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mAreaEdit.getText())) {
            Toast.makeText(UpdateAddressActivity.this, "请选择地区",
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mDetailEdit.getText())) {
            Toast.makeText(UpdateAddressActivity.this, "请输入详细地址",
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mNumberEdit.getText())) {
            Toast.makeText(UpdateAddressActivity.this, "请输入邮政编码",
                    Toast.LENGTH_SHORT).show();
            //mSaveText.setTextColor(0XFFDEDEDE);
        } else {
            //mSaveText.setEnabled(true);
            //mSaveText.setTextColor(0XFFFF5934);
            mName = mNameEdit.getText().toString();
            mPhone = mPhoneEdit.getText().toString();
            mCity = mAreaEdit.getText().toString();
            mAddress = mDetailEdit.getText().toString();
            mZipNumber = mNumberEdit.getText().toString();
            YKUserAddress address = new YKUserAddress();
            address.setmName(mName);
            address.setmTtelephone(mPhone);
            address.setmCity(mCity);
            address.setmProvince(mProvince);
            address.setmAddress(mAddress);
            address.setmZipcode(mZipNumber);
            saveAddress(address);
        }
    }

    private void saveAddress(YKUserAddress address) {
        YKUpdateUserInfoManager.getInstance().requestUpdateAddress(address,
                new YKGeneralCallBack() {

            @Override
            public void callback(YKResult result,String imageUrl) {
                if (result.isSucceeded()) {
                    Toast.makeText(UpdateAddressActivity.this, "修改成功",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateAddressActivity.this,
                            result.getMessage().toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void exitDialog() {
        AlertDialog.Builder builder = new Builder(UpdateAddressActivity.this);
        builder.setMessage("是否放弃当前编辑 ");  /*builder.setTitle("提示");*/  
        builder.setPositiveButton("否", new DialogInterface.OnClickListener() { 

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();    
            }
        });  
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {  

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UpdateAddressActivity.this.finish();
            }
        });  builder.create().show();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            isChangeAddress = iSChangeAddress();
            if(isChangeAddress){
                finish();
            }else{
                exitDialog();
            }
        }
        return false;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_CITY && resultCode == RESULT_CODE) {
//            if (data != null) {
//                mCity = data.getExtras().getString("city_name");
//                mProvince = data.getExtras().getString("province_name");
//                if (TextUtils.isEmpty(mCity)) {
//                    mCity = "";
//                }
//                if (TextUtils.isEmpty(mProvince)) {
//                    mProvince = "";
//                }
//                mAreaEdit.setText(mCity + " " + mProvince);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
