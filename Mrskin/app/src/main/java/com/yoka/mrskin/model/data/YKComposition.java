package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

/**
 * 成分
 * 
 * @author Y H L
 * 
 */
@SuppressWarnings("serial")
public class YKComposition extends YKData
{
    /**
     * 成份中文名
     */
    private String mName;

    /**
     * 成份英文名
     */
    private String mName_en;

    /**
     * 成份别名
     */
    private String mName_alias;

    /**
     * 成份描述
     */
    private String mDescription;

    /**
     * 刺激性
     */
    private String mAllergic;

    /**
     * 致粉刺
     */
    private String mAcne;

    /**
     * 敏感成份，荧光剂等等
     */
    private String mPerformance;
    /**
     * 含有该成份的商品
     */
    private ArrayList<YKProduct> mProducts;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmName_en() {
        return mName_en;
    }

    public void setmName_en(String mName_en) {
        this.mName_en = mName_en;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPerformance() {
        return mPerformance;
    }

    public void setPerformance(String performance) {
        this.mPerformance = performance;
    }

    public ArrayList<YKProduct> getmProducts() {
        return mProducts;
    }

    public String getmAllergic() {
        return mAllergic;
    }

    public void setmAllergic(String mAllergy) {
        this.mAllergic = mAllergy;
    }

    public String getmAcne() {
        return mAcne;
    }

    public void setmAcne(String mAcne) {
        this.mAcne = mAcne;
    }

    public String getmPerformance() {
        return mPerformance;
    }

    public void setmPerformance(String mPerformance) {
        this.mPerformance = mPerformance;
    }

    public void setmProducts(ArrayList<YKProduct> mProducts) {
        this.mProducts = mProducts;
    }

    public String getmName_alias() {
        return mName_alias;
    }

    public void setmName_alias(String mName_alias) {
        this.mName_alias = mName_alias;
    }

    public static YKComposition parse(JSONObject object) {
        YKComposition composition = new YKComposition();
        if (object != null) {
            composition.parseData(object);
        }
        return composition;
    }

    protected void parseData(JSONObject object) {
        JSONObject obj = null;
        try {
            obj = object.getJSONObject("composition");
        } catch (JSONException e) {
            obj = object;
            e.printStackTrace();
        }
        if (obj != null) {
            super.parseData(obj);
            try {
                mName = obj.getString("name");
            } catch (JSONException e) {
            }
            try {
                mName_en = obj.getString("name_en");
            } catch (JSONException e) {
            }
            try {
                mName_alias = obj.getString("name_alias");
            } catch (JSONException e) {
            }
            try {
                mDescription = obj.getString("description");
            } catch (JSONException e) {
            }
            try {
                mAllergic = obj.getString("allergic");
            } catch (JSONException e) {
            }
            try {
                mPerformance = obj.getString("performance");
            } catch (JSONException e) {
            }
            try {
                mAcne = obj.getString("acne");
            } catch (JSONException e) {
            }
        }

        try {
            JSONArray data = object.getJSONArray("related_products");
            if (data == null) {
                return;
            }
            for (int i = 0; i < data.length(); i++) {
                JSONObject object2 = data.getJSONObject(i);
                if (object2 == null) {
                    continue;
                }
                if (mProducts == null) {
                    mProducts = new ArrayList<YKProduct>();
                }
                mProducts.add(YKProduct.parse(object2));
            }
        } catch (JSONException e) {
        }
    }
}
