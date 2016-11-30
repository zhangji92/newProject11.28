package com.example.administrator.beijingplayer.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.example.administrator.beijingplayer.util.HttpTools;

/**
 * Created by Administrator on 2016/8/30.
 */
public abstract class FatherFragment extends Fragment implements View.OnClickListener{

    public HttpTools httpTools = HttpTools.getHttpTools();

    protected abstract void initView(View view);

    protected abstract void getServerMessage(Object object);


}
