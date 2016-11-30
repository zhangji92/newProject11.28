package com.allactivity.speech;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.allactivity.R;
import com.allactivity.custom.calendar.CustomCheckBox;

/**
 * Created by 张继 on 2016/11/21.
 * 演示
 */

public class SpeechFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.speech_activity, container, false);
        initViews(view);


        return view;
    }

    private void initViews(View view) {
        CustomCheckBox checkBox= (CustomCheckBox) view.findViewById(R.id.checkBox);

    }
}
