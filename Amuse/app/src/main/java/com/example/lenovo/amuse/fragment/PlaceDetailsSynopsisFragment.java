package com.example.lenovo.amuse.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.util.MyListView;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.activity.PlaceDetails;
import com.example.lenovo.amuse.mode.ShopMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

/**
 * Created by lenovo on 2016/10/20.
 * 商家详情数据列表
 */

public class PlaceDetailsSynopsisFragment extends Fragment {

    ShopMode shopMode;
    getInstanceHeight getInstanceHeight;

    public void setGetInstanceHeight(PlaceDetailsSynopsisFragment.getInstanceHeight getInstanceHeight) {
        this.getInstanceHeight = getInstanceHeight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.shop_details1, container, false);
        //获取数据
        shopMode = ((PlaceDetails) getActivity()).shopMode;
        //简介
        TextView textView_synopsis = (TextView) view.findViewById(R.id.shop_synopsis);
        textView_synopsis.setText(shopMode.getResultCode().getIntroduce());
        //内容
        TextView textView_content = (TextView) view.findViewById(R.id.shop_content);
        textView_content.setText(shopMode.getResultCode().getContent());
        //
        MyListView listView = (MyListView) view.findViewById(R.id.shop_list);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return shopMode.getResultCode().getIntropics().size() == 0 ? 0 : shopMode.getResultCode().getIntropics().size();
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
                View view1=LayoutInflater.from(getActivity()).inflate(R.layout.wine_details,null);
                ImageView imageView = (ImageView) view1.findViewById(R.id.details_adapter_img);
                FinalBitmap mFinalBitmap = FinalBitmap.create(getActivity());
                mFinalBitmap.display(imageView, BaseUri.BASE + shopMode.getResultCode().getIntropics().get(position));
                return view1;
            }
        });
        int height= (int) (listView.getRealHeight()*1.35);
        getInstanceHeight.getHeight(height);

        return view;
    }
    public interface getInstanceHeight{
        void getHeight(int height);
    }
}
