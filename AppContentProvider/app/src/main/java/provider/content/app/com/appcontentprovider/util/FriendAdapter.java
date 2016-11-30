package provider.content.app.com.appcontentprovider.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import provider.content.app.com.appcontentprovider.R;
import provider.content.app.com.appcontentprovider.mode.Friend;

/**
 * Created by lenovo on 2016/9/18.
 * 获取联系人的适配器
 */
public class FriendAdapter extends BaseAdapter{
    private List<Friend> list;
    private LayoutInflater layoutInflater;
    private Context context;


    @Override
    public int getCount() {
        return list.size()==0?0:list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=layoutInflater.from(context).inflate(R.layout.friend,null);

        return view;
    }
}
