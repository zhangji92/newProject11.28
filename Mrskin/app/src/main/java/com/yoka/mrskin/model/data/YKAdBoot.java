package com.yoka.mrskin.model.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKAdBoot extends YKData {

    /**
     * 
     */
    private static final long serialVersionUID = 1819863049284088177L;
    public String showTime;
    public String timeout;
    public String TotalCount;
    public List<AdData> Data = new ArrayList<AdData>();

    public class AdData {
        public String btime;
        public String id;
        public String linkurl;
        public String phourl;
        public String etime;
        public String ptype;
        public AdUrl adurl = new AdUrl();

        public void parseData(JSONObject object) {
            try {
                btime = object.getString("btime");
            } catch (JSONException e) {
            }

            try {
                id = object.getString("id");
            } catch (JSONException e) {
            }

            try {
                linkurl = object.getString("linkurl");
            } catch (JSONException e) {
            }
            try {
                phourl = object.getString("phourl");
            } catch (JSONException e) {
            }
            try {
                etime = object.getString("etime");
            } catch (JSONException e) {
            }
            try {
                ptype = object.getString("ptype");
            } catch (JSONException e) {
            }
            try {
                adurl.parseData(object.getJSONObject("adurl"));
            } catch (JSONException e) {
            }
        }
    }

    public class AdUrl {
        public String clickurl;
        public String showurl;
        public String zhhUrl;
        public String bgUrl;

        public void parseData(JSONObject object) {

            try {
                clickurl = object.getString("clickurl");
            } catch (JSONException e) {
            }

            try {
                showurl = object.getString("showurl");
            } catch (JSONException e) {
            }

            try {
                zhhUrl = object.getString("zhhUrl");
            } catch (JSONException e) {
            }
            try {
                bgUrl = object.getString("bgUrl");
            } catch (JSONException e) {
            }
        }
    }

    public void parseData(JSONObject object) {

        try {
            showTime = object.getString("showTime");
        } catch (JSONException e) {
        }

        try {
            timeout = object.getString("timeout");
        } catch (JSONException e) {
        }

        try {
            TotalCount = object.getString("TotalCount");
        } catch (JSONException e) {
        }

        try {
            JSONArray tmpObject = object.getJSONArray("Data");
            if(tmpObject!=null){
                for (int i = 0; i < tmpObject.length(); i++) {
                    AdData data = new AdData();
                    data.parseData(tmpObject.getJSONObject(i));
                    Data.add(data);
                }
            }

        } catch (JSONException e) {
        }

    }
}
