package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKComment;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKCommentManager extends YKManager {
    private static final String PATHEXPER = getBase() + "index/commentreply";
    private static final String PATHTOPIC = getBase() + "index/topicreply";

    private static YKCommentManager singleton = null;
    private static Object lock = new Object();

    public static YKCommentManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKCommentManager();
            }
        }
        return singleton;
    }

    public void postComment(int type,int befordorafter, int comment_id,String authtoken,final CommentCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if(type == 0){
            parameters.put("after_id", befordorafter);
        }else{
            parameters.put("before_id", befordorafter);
        }
        parameters.put("commentID", comment_id);
        parameters.put("authtoken", authtoken);
        
        super.postURL(PATHEXPER, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {
                
                ArrayList<YKComment> mComments = null;
                
                if (result.isSucceeded() && object != null) {
                    JSONArray tmpArray = object.optJSONArray("result");
                    if (tmpArray != null) {
                        mComments = new ArrayList<YKComment>();
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                mComments.add(YKComment.parse(tmpArray.getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if (callback != null) {
                    callback.callback(result,mComments);
                }
            }
        });
    }
    
    public void postTopicReply(int type,int befordorafter, int comment_id,String authtoken,final CommentCallback callback) {
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        if(type == 0){
            parameters.put("after_id", befordorafter);
        }else{
            parameters.put("before_id", befordorafter);
        }
        parameters.put("topicID", comment_id);
        parameters.put("authtoken", authtoken);
        super.postURL(PATHTOPIC, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {
                ArrayList<YKComment> mComments = null;
                
                if (result.isSucceeded() && object != null) {
                	
                    JSONArray tmpArray = object.optJSONArray("result");
                    if (tmpArray != null) {
                        mComments = new ArrayList<YKComment>();
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                mComments.add(YKComment.parse(tmpArray.getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if (callback != null) {
                    callback.callback(result,mComments);
                }
            }
        });
    }

    public interface CommentCallback {
        public void callback(YKResult result,ArrayList<YKComment> comment);
    }
}
