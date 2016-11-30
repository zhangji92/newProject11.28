package com.allactivity.conflict;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.allactivity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/11/22.
 *
 */

public class SlidingConflictFragment extends Fragment {
    private List<String> data=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sliding_conflict_fragment,container,false);
        ListView listView= (ListView) view.findViewById(R.id.sliding_fragment_listView);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,getStringData()));
        return view;
    }
    private List<String> getStringData(){

        for (int i=0;i<20;i++){
            data.add("listView"+i);
        }
        return data;
    }
}
