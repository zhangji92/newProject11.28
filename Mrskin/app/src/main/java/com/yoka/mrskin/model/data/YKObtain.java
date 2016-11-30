package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKObtain extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int mCode;
    private String mMessage;
    public int getmCode()
    {
        return mCode;
    }
    public void setmCode(int mCode)
    {
        this.mCode = mCode;
    }
    public String getmMessage()
    {
        return mMessage;
    }
    public void setmMessage(String mMessage)
    {
        this.mMessage = mMessage;
    }

    public YKObtain(int mCode, String mMessage)
    {
        super();
        this.mCode = mCode;
        this.mMessage = mMessage;
    }

    public YKObtain()
    {
        super();
    }
    public static YKObtain parse(JSONObject object) {
        YKObtain obtain = new YKObtain();
        if (object != null) {
            obtain.parseData(object);
        }
        return obtain;
    }

    protected void parseData(JSONObject object) {

        try {
            mCode = object.getInt("code");
        } catch (JSONException e) {
        }

        try {
            mMessage = object.getString("message");
        } catch (JSONException e) {
        }
    }


}
