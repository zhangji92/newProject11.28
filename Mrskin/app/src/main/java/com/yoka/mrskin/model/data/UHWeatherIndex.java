package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class UHWeatherIndex extends YKData {

    private static final String TAG_DESCRIPTION = "level";
    private static final String TAG_NAME = "name";

    private String mName;
    private String mContent;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }
    
    @Override
    public String toString() {
        return "name = " + mName + ",  mContent = " + mContent;
    }

    public void parseData(JSONObject object) {
        if (object == null) {
            return;
        }
        super.parseData(object);

        String tmpString;
        try {
            tmpString = object.getString(TAG_NAME);
            if (tmpString != null) {
                setName(tmpString);
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_DESCRIPTION);
            if (tmpString != null) {
                setContent(tmpString);
            }
        } catch (Exception e) {
        }
    }

    public String toJsonString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        if (mName != null) {
            buffer.append("\"" + TAG_NAME + "\":");
            buffer.append("\"");
            buffer.append(mName);
            buffer.append("\"");
        }
        if (mContent != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_DESCRIPTION + "\":");
            buffer.append("\"");
            buffer.append(mContent);
            buffer.append("\"");
        }
        buffer.append("}");
        return buffer.toString();
    }
}
