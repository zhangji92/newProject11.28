package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKSpecification extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 规格名称
     */
    private String title;
    /**
     * 价格
     */
    private Float price;
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public Float getPrice()
    {
        return price;
    }
    public void setPrice(Float price)
    {
        this.price = price;
    }
    public YKSpecification(String title, Float price)
    {
        super();
        this.title = title;
        this.price = price;
    }
    public YKSpecification()
    {
        super();
    }

    public static YKSpecification parse(JSONObject object) {
        YKSpecification specification = new YKSpecification();
        if (object != null) {
            specification.parseData(object);
        }
        return specification;
    }

    protected void parseData(JSONObject object) {
        
        super.parseData(object);
        
        try {
            title = object.getString("title");
        } catch (JSONException e) {
        }

        try {
            price = (float) object.getDouble("price");
        } catch (JSONException e) {
        }
    }
    
    public JSONObject toJson(){
        JSONObject object = new JSONObject();
        try {
            object.put("title", title);
        } catch (Exception e) {}
        try {
            object.put("price", price);
        } catch (Exception e) {}
        return object;
    }
}
