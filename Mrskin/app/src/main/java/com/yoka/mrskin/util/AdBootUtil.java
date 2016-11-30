package com.yoka.mrskin.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKAdBoot;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.YKAdBootManager;
import com.yoka.mrskin.model.managers.base.Callback;

public class AdBootUtil
{

    public static final String AD_FLAG = "ad_flage";

    private static AdBootUtil adBootUtil = null;

    private Context mContext;

    private YKAdBoot ykAdBoot;

    private AdBootUtil(Context context)
    {
        this.mContext = context;
    }

    public static AdBootUtil getInstance(Context context) {
        if (adBootUtil == null) {
            adBootUtil = new AdBootUtil(context);
        }
        return adBootUtil;
    }

    public void getAdd() {
        YKAdBootManager.getInstance().requestAdBootData(mContext,
                new Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        if (result.isSucceeded() && object != null) {
                            saveAdCache(object.toString());
                            setYkAdBoot(object.toString());
                            if (ykAdBoot != null && ykAdBoot.Data.size() != 0)
                                AsyncHttpFileDown(ykAdBoot.Data.get(0).phourl);
                        }
                    }
                });
    }

    public boolean isAdd() {
        String adData = getAdCache();
        // 判断是否有缓存
        if (!TextUtils.isEmpty(adData)) {
            setYkAdBoot(adData);
            // 判断缓存有广告
            if (ykAdBoot == null || ykAdBoot.Data == null
                    || ykAdBoot.Data.size() == 0)
                return false;
            // 判断广告是否有效
            if (!isAdTime(ykAdBoot.Data.get(0).btime,
                    ykAdBoot.Data.get(0).etime))
                return false;
            // 判断文件是否存在
            if (getFile(ykAdBoot.Data.get(0).phourl).exists()) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdTime(String btime, String etime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long mBitme, mEtime;
        long time = System.currentTimeMillis();
        try {
            mBitme = sdf.parse(btime).getTime();
            mEtime = sdf.parse(etime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        if (mBitme < time && time < mEtime)
            return true;
        return false;
    }

    private void AsyncHttpFileDown(String path) {
        if (TextUtils.isEmpty(path))
            return;
        // path="http://modp1.yokacdn.com/pic/2016/03/25/f8d88ce0fb7e48c884365706997ff93a.jpg";
        // path = "http://ww1.sinaimg.cn/bmiddle/7cefdfa5jw1e21a4kvrkng.gif";

        if (getFile(path).exists())
            return;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(path, new FileAsyncHttpResponseHandler(getFile(path)) {
            @Override
            public void onSuccess(int statusCode, Header[] hander, File file) {
                if (statusCode == 200) {
                    String type = getType(hander);
                    if (type != null)
                        saveAdBootType(type);
                    clearAdBootError();
                    // Toast.makeText(mContext, "文件下载成功", 0).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] hander,
                    Throwable throwable, File file) {
                throwable.printStackTrace();
            }

        });
    }

    public File getFile(String path) {
        File file = new File(mContext.getFilesDir(), path.substring(path
                .lastIndexOf("/") + 1));
        return file;
    }

    public YKAdBoot getYkAdBoot() {
        return ykAdBoot;
    }

    public void setYkAdBoot(String object) {
        this.ykAdBoot = new YKAdBoot();
        try {
            JSONObject myJsonObject = new JSONObject(object);
            ykAdBoot.parseData(myJsonObject.getJSONObject("Contents"));
        } catch (JSONException e) {
            ykAdBoot = null;
            e.printStackTrace();
        }
    }

    public String getAdCache() {
        return AppUtil.getAdBootData(mContext);
    }

    public void clearAdCache() {
        AppUtil.saveAdBootData(mContext, "");
    }

    public void saveAdCache(String string) {
        AppUtil.saveAdBootData(mContext, string);
    }

    public void saveAdBootError() {
        AppUtil.saveAdBootErrorData(mContext, "error");
    }

    public void clearAdBootError() {
        AppUtil.saveAdBootErrorData(mContext, "");
    }

    public String getAdBootError() {
        return AppUtil.getAdBootErrorData(mContext);
    }

    public String getType(Header[] header) {
        Map<String, String> map = getHeadersInfo(header);
        if (map == null)
            return null;
        return map.get("Content-Type");
    }

    public void saveAdBootType(String type) {
        AppUtil.saveAdBootContentTypeData(mContext, type);
    }

    public String getAdBootType() {
        return AppUtil.getAdBootContentTypeData(mContext);
    }

    private Map<String, String> getHeadersInfo(Header[] header) {
        if (header.length == 0)
            return null;
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < header.length; i++) {
            String key = header[i].getName();
            String value = header[i].getValue();
            map.put(key, value);
        }
        return map;
    }

   

}
