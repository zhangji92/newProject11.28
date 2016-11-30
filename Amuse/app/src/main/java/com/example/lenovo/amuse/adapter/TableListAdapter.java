package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.activity.ConversationActivity;
import com.example.lenovo.amuse.activity.PlaceDetails;
import com.example.lenovo.amuse.mode.TableListMode;
import com.example.lenovo.amuse.util.BaseUri;
import com.example.lenovo.amuse.util.HttpTools;
import com.example.lenovo.amuse.util.ServiceMessage;
import com.makeramen.roundedimageview.RoundedImageView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by 张继 on 2016/10/26.
 * 拼桌列表接口
 */

public class TableListAdapter extends BaseAdapter {

    private List<TableListMode.ResultCodeBean> mList;
    private Context context;
    private LayoutInflater layoutInflater;
    FinalBitmap mFinalBitmap;

    private OnClickButton mOnClickButton;

    public void setmOnClickButton(OnClickButton mOnClickButton) {
        this.mOnClickButton = mOnClickButton;
    }

    public TableListAdapter(List<TableListMode.ResultCodeBean> mList, Context context) {
        this.mList = mList;
        this.context = context;
        mFinalBitmap = FinalBitmap.create(context);
    }

    @Override
    public int getCount() {
        return mList.size() == 0 ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.from(context).inflate(R.layout.table_list_adapter, null);
        //个人头像
        RoundedImageView roundedImageView_user = (RoundedImageView) view.findViewById(R.id.table_adapter_userImg);
        mFinalBitmap.display(roundedImageView_user, BaseUri.BASE + mList.get(position).getUpic());
        //用户名
        TextView text_user = (TextView) view.findViewById(R.id.table_adapter_user);
        text_user.setText(mList.get(position).getNickname());
        //时间
        TextView text_time = (TextView) view.findViewById(R.id.table_adapter_time);
        text_time.setText(mList.get(position).getAddtime());
        //距离
        TextView text_distance = (TextView) view.findViewById(R.id.table_adapter_location);
        text_distance.setText(mList.get(position).getAway());
        //群聊人数
        TextView text_number = (TextView) view.findViewById(R.id.table_adapter_number);
        text_number.setText("群聊人数:" + mList.get(position).getStarlevel());
        //图片
        RoundedImageView roundedImageView_viewUser = (RoundedImageView) view.findViewById(R.id.table_adapter_viewImg);
        mFinalBitmap.display(roundedImageView_viewUser, BaseUri.BASE + mList.get(position).getPic());
        //主题
        TextView text_title = (TextView) view.findViewById(R.id.table_adapter_title);
        text_title.setText(mList.get(position).getTitle());
        //shopName
        TextView text_shop = (TextView) view.findViewById(R.id.table_adapter_viewTitle);
        text_shop.setText(mList.get(position).getShopname());
        //地址
        TextView text_address = (TextView) view.findViewById(R.id.table_adapter_viewAddress);
        text_address.setText(mList.get(position).getAddress());
        //内容
        TextView text_content = (TextView) view.findViewById(R.id.table_adapter_viewContent);
        text_content.setText(mList.get(position).getContent());

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_view);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickButton != null) {
                    mOnClickButton.onClick2(position);
                }
            }
        });
        TextView text_join = (TextView) view.findViewById(R.id.table_adapter_join);
        text_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickButton != null) {
                    mOnClickButton.onClick(position);

                }
            }
        });
        return view;
    }

    public interface OnClickButton {
        void onClick(int position);
        void onClick2(int position);
    }
}
