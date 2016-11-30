package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKSearchRecommend extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 服务器标注产品类型
     */
    public static final int SEARCH_TYPE_PRODUCT = 101;
    /**
     * 服务器标注成份类型
     */
    public static final int SEARCH_TYPE_COMPOSITION = 102;

    private int mType;

    /**
     * 成份对象
     */
    private YKComposition mCompositions;

    /**
     * 商品对象
     */
    private YKProduct mProducts;

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public YKComposition getmCompositions() {
        return mCompositions;
    }

    public void setmCompositions(YKComposition mCompositions) {
        this.mCompositions = mCompositions;
    }

    public YKProduct getmProducts() {
        return mProducts;
    }

    public void setmProducts(YKProduct mProducts) {
        this.mProducts = mProducts;
    }

    public static YKSearchRecommend parse(JSONObject object) {
        YKSearchRecommend searchResult = new YKSearchRecommend();
        if (object != null) {
            searchResult.parseData(object);
        }
        return searchResult;
    }

    protected void parseData(JSONObject object) {
        super.parseData(object);

        try {
            mType = object.getInt("type");
        } catch (JSONException e) {
        }
        JSONObject obj = null;
        switch (mType) {
        case SEARCH_TYPE_COMPOSITION:
            try {
                obj = object.getJSONObject("composition");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCompositions = YKComposition.parse(obj);
            break;
        case SEARCH_TYPE_PRODUCT:
            try {
                obj = object.getJSONObject("product");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProducts = YKProduct.parse(obj);
            break;
        default:
            break;
        }
    }

}
