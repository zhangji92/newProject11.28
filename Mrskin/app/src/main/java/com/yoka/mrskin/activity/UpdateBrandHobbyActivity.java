package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKUpdateEntity;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.UpdateUserEntitiesCallback;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.YKGeneralCallBack;

/**
 * 美妆偏好
 * 
 * @author z l l
 * 
 */
public class UpdateBrandHobbyActivity extends BaseActivity
{
    private static final int TYPE_HOBBY = 11;
    private static final int RESULT_CODE = 0;
    private View mBackBtn;
    private ListView mListView;
    private ArrayList<YKUpdateEntity> mUserEntities;
    private BrandHobbyAdapter mAdapter;
    private HashMap<String, YKUpdateEntity> mSelectMap;
    private TreeMap<String, String> data = new TreeMap<String, String>();
    private String mBrandLikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_brand_hobby);
        getData();
    }

    private void initView() {
        mBackBtn = findViewById(R.id.update_brand_hobby_back);
        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mAdapter!=null){
                    String newIds = getTypeValueStr(mAdapter.getSelectTagList());
                    if(newIds.length() <= 0){
                        Toast.makeText(UpdateBrandHobbyActivity.this, "至少选择一项", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent();
                        saveData(newIds);
                        intent.putExtra("brandlike", newIds);
                        setResult(RESULT_CODE, intent);
                    }
                }else{
                    finish();
                }

            }
        });

        mListView = (ListView) findViewById(R.id.update_brand_hobbt_listview);
        mAdapter = new BrandHobbyAdapter();
        mListView.setAdapter(mAdapter);
    }

    private void getData() {
        mBrandLikeId = getIntent().getStringExtra("brandlike");
        YKUpdateUserInfoManager.getInstance().requestUserEntities(TYPE_HOBBY,
                new UpdateUserEntitiesCallback() {

            @Override
            public void callback(YKResult result,
                    ArrayList<YKUpdateEntity> userEneities) {
                if (result.isSucceeded() && userEneities != null) {
                    mUserEntities = userEneities;
                    for (int i = 0; i < mUserEntities.size(); i++) {
                        data.put(mUserEntities.get(i).getmId(),
                                mUserEntities.get(i).getmName());
                    }
                    mSelectMap = new HashMap<String, YKUpdateEntity>();
                    for (YKUpdateEntity lable : getLableList(
                            mBrandLikeId, data)) {
                        mSelectMap.put(lable.getmId(), lable);
                    }
                    initView();
                }
            }
        });
    }

    private void saveData(String value) {
        YKUpdateUserInfoManager.getInstance().requestUpdateUSerInfo(
                "brandlike", value, new YKGeneralCallBack() {

                    @Override
                    public void callback(YKResult result,String imageUrl) {
                        if (result.isSucceeded()) {
                            Toast.makeText(UpdateBrandHobbyActivity.this,
                                    "修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UpdateBrandHobbyActivity.this,
                                    result.getMessage().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static String getTypeValueStr(List<YKUpdateEntity> data) {
        StringBuilder sb = new StringBuilder();
        for (YKUpdateEntity s : data) {
            sb.append(s.getmId()).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static List<YKUpdateEntity> getLableList(String str,
            TreeMap<String, String> data) {
        List<YKUpdateEntity> resultList = new ArrayList<YKUpdateEntity>();
        if (TextUtils.isEmpty(str)) {
            return resultList;
        }
        String[] split = str.split(",");
        try {
            for (String key : split) {
                String keyValue = key;
                if (data.containsKey(keyValue)) {
                    YKUpdateEntity entity = new YKUpdateEntity();
                    entity.setmId(key);
                    entity.setmName(data.get(keyValue));
                    resultList.add(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public void onBackPressed() {
        if(mAdapter!=null){
            String newIds = getTypeValueStr(mAdapter.getSelectTagList());
            if(newIds.length() <= 0){
                Toast.makeText(UpdateBrandHobbyActivity.this, "至少选择一项", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent();
                saveData(newIds);
                intent.putExtra("brandlike", newIds);
                setResult(RESULT_CODE, intent);
            }
        }else{
            finish();
        }

    }

    private class BrandHobbyAdapter extends BaseAdapter
    {

        private HashMap<String, YKUpdateEntity> selectTags;

        public BrandHobbyAdapter()
        {
            selectTags = new HashMap<String, YKUpdateEntity>();
            selectTags.putAll(mSelectMap);
        }

        @Override
        public int getCount() {
            if (mUserEntities != null) {
                return mUserEntities.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mUserEntities != null) {
                return mUserEntities.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater
                        .from(UpdateBrandHobbyActivity.this).inflate(
                                R.layout.update_brand_hobbt_item_layout, null);
                viewHolder.content = convertView
                        .findViewById(R.id.update_brand_hobby_item_content);
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.update_brand_hobby_item_name);
                viewHolder.checkbox = (CheckBox) convertView
                        .findViewById(R.id.update_brand_hobby_item_checkbox);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final YKUpdateEntity entity = mUserEntities.get(position);
            if (!TextUtils.isEmpty(entity.getmName())) {
                viewHolder.name.setText(entity.getmName());
            }

            viewHolder.content.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    viewHolder.checkbox.setChecked(!viewHolder.checkbox
                            .isChecked());
                    System.out.println("brandlike position = " + position);
                    if (viewHolder.checkbox.isChecked()) {
                        selectTags.put(entity.getmId(), entity);
                    } else {
                        selectTags.remove(entity.getmId());
                    }
                }
            });
            viewHolder.checkbox
            .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                        boolean isChecked) {
                    //                            viewHolder.checkbox.setChecked(!viewHolder.checkbox
                    //                                    .isChecked());
                    //                            System.out.println("brandlike position = "
                    //                                    + position);
                    //                            Toast.makeText(UpdateBrandHobbyActivity.this,
                    //                                    position + "", Toast.LENGTH_SHORT).show();
                    if (viewHolder.checkbox.isChecked()) {
                        selectTags.put(entity.getmId(), entity);
                    } else {
                        selectTags.remove(entity.getmId());
                    }
                }
            });

            if (selectTags.containsKey(entity.getmId())) {
                viewHolder.checkbox.setChecked(true);
            } else {
                viewHolder.checkbox.setChecked(false);
            }
            // 编辑
            if (mSelectMap.size() > 0) {
                if (mSelectMap.containsKey(entity.getmId())) {
                    viewHolder.checkbox.setChecked(true);
                } else {
                    viewHolder.checkbox.setChecked(false);
                }
            }
            return convertView;
        }

        public List<YKUpdateEntity> getSelectTagList() {
            ArrayList<YKUpdateEntity> result = new ArrayList<YKUpdateEntity>();
            result.addAll(selectTags.values());
            return result;
        }
    }

    private class ViewHolder
    {
        View content;
        TextView name;
        CheckBox checkbox;
    }

    @Override
    protected void onDestroy() {
        mBrandLikeId = "";
        if (mAdapter != null) {
            mAdapter.selectTags.clear();
        }
        super.onDestroy();
    }
}
