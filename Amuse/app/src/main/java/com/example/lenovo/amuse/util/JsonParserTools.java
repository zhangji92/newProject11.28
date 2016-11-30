package com.example.lenovo.amuse.util;

import com.example.lenovo.amuse.mode.AgentMode;
import com.example.lenovo.amuse.mode.CommentSuccess;
import com.example.lenovo.amuse.mode.FirstPageMode;
import com.example.lenovo.amuse.mode.LovePlayMode;
import com.example.lenovo.amuse.mode.PlaceMode;
import com.example.lenovo.amuse.mode.PreferentialMode;
import com.example.lenovo.amuse.mode.RegisterSuccess;
import com.example.lenovo.amuse.mode.ShopMode;
import com.example.lenovo.amuse.mode.SnapShortDetailsMode;
import com.example.lenovo.amuse.mode.SnapShortMode;
import com.example.lenovo.amuse.mode.SuccessMode;
import com.example.lenovo.amuse.mode.VerificationCode;
import com.example.lenovo.amuse.mode.WineDetails;
import com.example.lenovo.amuse.mode.WineMode;
import com.google.gson.Gson;

/**
 * Created by lenovo on 2016/9/26.
 * json转成Mode实体类
 */

public class JsonParserTools {

    public static Object parserMode(String s, int flags) {
        Object obj = null;
        Gson gson = new Gson();
        if (s != null) {
            if (flags == 1) {
                obj = gson.fromJson(s, FirstPageMode.class);
            } else if (flags == 2) {
                obj = gson.fromJson(s, LovePlayMode.class);
            }else if (flags==3){
                obj = gson.fromJson(s, PreferentialMode.class);
            }else if (flags==4){
                obj = gson.fromJson(s, VerificationCode.class);
            }else if (flags==5){
                obj = gson.fromJson(s, RegisterSuccess.class);
            }else if (flags==6){
                obj = gson.fromJson(s, SuccessMode.class);
            }else if (flags==7){
                obj = gson.fromJson(s, SnapShortMode.class);
            }else if (flags==8){
                obj = gson.fromJson(s, WineMode.class);
            }else if (flags==9){
                obj = gson.fromJson(s, AgentMode.class);
            }else if (flags==10){
                obj = gson.fromJson(s, WineDetails.class);
            }else if (flags==11){
                obj = gson.fromJson(s, ShopMode.class);
            }else if (flags==12){
                obj = gson.fromJson(s, SnapShortDetailsMode.class);
            }else if (flags==13){
                obj = gson.fromJson(s, CommentSuccess.class);
            }else if (flags==14){
                obj = gson.fromJson(s, PlaceMode.class);
            }
        }
        return obj;
    }


}
