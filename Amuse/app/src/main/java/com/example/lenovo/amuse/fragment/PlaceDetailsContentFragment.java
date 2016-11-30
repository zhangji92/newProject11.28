package com.example.lenovo.amuse.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.amuse.util.MyListView;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.activity.PlaceDetails;
import com.example.lenovo.amuse.mode.ShopMode;

/**
 * Created by lenovo on 2016/10/20.
 * 服务
 */

public class PlaceDetailsContentFragment extends Fragment {

    ShopMode shopMode;
    TextView textView_contentH;
//    private getInstanceHeight getInstanceHeight;
//
//    public void setGetInstanceHeight(PlaceDetailsContentFragment.getInstanceHeight getInstanceHeight) {
//        this.getInstanceHeight = getInstanceHeight;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_details3, container, false);
        //获取数据
        shopMode = ((PlaceDetails) getActivity()).shopMode;
        //简介
        TextView textView_synopsis = (TextView) view.findViewById(R.id.shop_synopsis);
        textView_synopsis.setText(shopMode.getResultCode().getIntroduce());

        //内容
        TextView textView_content = (TextView) view.findViewById(R.id.shop_content);
        textView_content.setText(shopMode.getResultCode().getContent());
        MyListView listView = (MyListView) view.findViewById(R.id.shop_list);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return shopMode.getResultCode().getPrice().size() == 0 ? 0 : shopMode.getResultCode().getPrice().size();
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
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.shop_details2, null);
                //包厢名称
                TextView textView_box= (TextView) view1.findViewById(R.id.box);
                textView_box.setText(shopMode.getResultCode().getPrice().get(position).getName());


                textView_contentH= (TextView) view1.findViewById(R.id.content);
                textView_contentH.setText(shopMode.getResultCode().getPrice().get(position).getContent());


                TextView textView_money= (TextView) view1.findViewById(R.id.money);
                textView_money.setText(shopMode.getResultCode().getPrice().get(position).getNowprice());
                return view1;
            }
        });
//        getInstanceHeight.getHeight((int) (listView.getRealHeight()*2));

        return view;
    }

//    public interface getInstanceHeight{
//        void getHeight(int height);
//    }
}
