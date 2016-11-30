package com.yoka.mrskin.model.base;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

@SuppressWarnings("serial")
public class YKData implements Serializable
{

    private static final String TAG = "YKData";

    protected String mID;

    public YKData()
    {
        mID = "";
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    protected void parseData(JSONObject object) {
        try {
            mID = object.optString("id");
        } catch (Exception e) {
        }
    }

    public void log() {
        Log.d(TAG, "ID = " + mID);
    }
}
