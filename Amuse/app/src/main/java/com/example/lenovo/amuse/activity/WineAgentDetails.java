package com.example.lenovo.amuse.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.adapter.WineAgentAdapter;
import com.example.lenovo.amuse.adapter.WineDetailsAdapter;
import com.example.lenovo.amuse.mode.AgentMode;
import com.example.lenovo.amuse.mode.WineDetails;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/18.
 * 酒水代理详情
 */

public class WineAgentDetails extends BaseActivity {
    private TextView textView_title;
    private ImageView imageView;
    private TextView textView_money;
    private TextView textView_brand;
    private TextView textView_varieties;
    private TextView textView_person;
    private TextView textView_contact;
    private TextView textView_address;
    private TextView textView_content;

    ListView listView;
    WineDetailsAdapter wineDetailsAdapter;
    //数据集合
    List<String> resultCodeBeanList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.WINE_AGENT_DETAILS:
                    WineDetails wineDetails = parseMode(msg.obj);
                    wineDetails.getResultCode().getIntropics();
                    if (wineDetails != null) {
                        FinalBitmap finalBitmap = FinalBitmap.create(WineAgentDetails.this);
                        finalBitmap.display(imageView, BaseUri.BASE + wineDetails.getResultCode().getPic());
                        textView_title.setText(wineDetails.getResultCode().getShopname());
                        textView_money.setText("价格：" + wineDetails.getResultCode().getMoney());
                        textView_brand.setText(wineDetails.getResultCode().getSbrand());
                        textView_varieties.setText(wineDetails.getResultCode().getSbreed());
                        textView_person.setText(wineDetails.getResultCode().getPerson());
                        textView_address.setText(wineDetails.getResultCode().getAddress());
                        textView_contact.setText(wineDetails.getResultCode().getPhone());
                        textView_content.setText(wineDetails.getResultCode().getIntroduce());
                    }
                    //添加信息
                    for (int i = 0; i < wineDetails.getResultCode().getIntropics().size(); i++) {
                        resultCodeBeanList.add(wineDetails.getResultCode().getIntropics().get(i));
                    }
//                    resultCodeBeanList=wineDetails.getResultCode().getIntropics();

                    //测量LIstView
                    getViewHright(listView);
//                    wineDetailsAdapter.notifyDataSetChanged();

                    break;

            }
        }
    };

    //解析数据
    private WineDetails parseMode(Object obj) {
        WineDetails successMode = null;
        if (obj != null && obj instanceof WineDetails) {
            successMode = (WineDetails) obj;
        }
        return successMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wine_agent_details);
        //商品ID
        String id = ((MyApplication) getApplication()).getGoodsId();


        imageView = (ImageView) findViewById(R.id.details_img);
        //主题
        textView_title = (TextView) findViewById(R.id.details_title);
        //价格
        textView_money = (TextView) findViewById(R.id.details_money);
        //品牌
        textView_brand = (TextView) findViewById(R.id.details_brand);
        //品种
        textView_varieties = (TextView) findViewById(R.id.details_varieties);
        //联系人
        textView_person = (TextView) findViewById(R.id.details_person);
        //联系方式
        textView_contact = (TextView) findViewById(R.id.details_contact);
        //地址
        textView_address = (TextView) findViewById(R.id.details_address);
        //内容
        textView_content = (TextView) findViewById(R.id.details_content);

        //请求数据
        httpTools.getPlace(mHandler, "1", null, null, null, "10", null, id, 3);

        //listView图片集合
        listView = (ListView) findViewById(R.id.details_list);
        wineDetailsAdapter = new WineDetailsAdapter(resultCodeBeanList, WineAgentDetails.this);
        listView.setAdapter(wineDetailsAdapter);

        //scroll+listView设置一起滚动
        ScrollView zhiding = (ScrollView) findViewById(R.id.details_scroll);
        zhiding.setFocusable(true);
        zhiding.setFocusableInTouchMode(true);
        zhiding.requestFocus();
        wineDetailsAdapter.notifyDataSetChanged();

    }

    /**
     * 测量ListView的高度
     *
     * @param listView
     */
    public void getViewHright(ListView listView) {

        Adapter adapter = listView.getAdapter();
        int gd = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, listView);
            view.measure(0, 0);
            int item = view.getMeasuredHeight();
            gd += item;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = gd + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
