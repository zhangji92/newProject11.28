package com.example.administrator.beijingplayer.mode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class KuaipaiMessage {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"recommend":[],"quickphoto":[{"id":"87","title":"图片","video":"http://mpv.videocc.net/9a303e67dd/8/9a303e67dddc5c15fb06274f3f0a1bd8_1.mp4","count":"29","nickname":"GAME0058","vimage":"http://img.videocc.net/uimage/9/9a303e67dd/8/9a303e67dddc5c15fb06274f3f0a1bd8_0.jpg","vid":"9a303e67dddc5c15fb06274f3f0a1bd8_9"},{"id":"78","title":"相信自己","video":"http://mpv.videocc.net/9a303e67dd/e/9a303e67dddecb479d4799518cab22ae_1.mp4","count":"10","nickname":"龙的传人","vimage":"http://img.videocc.net/uimage/9/9a303e67dd/e/9a303e67dddecb479d4799518cab22ae_0.jpg","vid":"9a303e67dddecb479d4799518cab22ae_9"},{"id":"77","title":"小水果","video":"http://mpv.videocc.net/9a303e67dd/1/9a303e67ddde7102f6cf8bef69d780d1_1.mp4","count":"11","nickname":"龙的传人","vimage":"http://img.videocc.net/uimage/9/9a303e67dd/1/9a303e67ddde7102f6cf8bef69d780d1_0.jpg","vid":"9a303e67ddde7102f6cf8bef69d780d1_9"},{"id":"73","title":"测试","video":"http://mpv.videocc.net/9a303e67dd/3/9a303e67dd3a0e582c04b76e28821f63_1.mp4","count":"10","nickname":"晃荡的青春","vimage":"http://img.videocc.net/uimage/9/9a303e67dd/3/9a303e67dd3a0e582c04b76e28821f63_0.jpg","vid":"9a303e67dd3a0e582c04b76e28821f63_9"},{"id":"62","title":"完成","video":"http://mpv.videocc.net/9a303e67dd/8/9a303e67dd2906eadec269e820728158_1.mp4","count":"3","nickname":"晃荡的青春","vimage":"http://img.videocc.net/uimage/9/9a303e67dd/8/9a303e67dd2906eadec269e820728158_0.jpg","vid":"9a303e67dd2906eadec269e820728158_9"},{"id":"58","title":"快来瞧一瞧","video":"http://mpv.videocc.net/9a303e67dd/5/9a303e67dd16a6d7d5c2898b1b8e7c75_1.mp4","count":"9","nickname":"龙的传人","vimage":"http://img.videocc.net/uimage/9/9a303e67dd/5/9a303e67dd16a6d7d5c2898b1b8e7c75_0.jpg","vid":"9a303e67dd16a6d7d5c2898b1b8e7c75_9"}]}
     */
    private String code;
    private String message;
    private ResultCodeBean resultCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultCodeBean getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCodeBean resultCode) {
        this.resultCode = resultCode;
    }

    public static class ResultCodeBean {
        private List<?> recommend;
        /**
         * id : 87
         * title : 图片
         * video : http://mpv.videocc.net/9a303e67dd/8/9a303e67dddc5c15fb06274f3f0a1bd8_1.mp4
         * count : 29
         * nickname : GAME0058
         * vimage : http://img.videocc.net/uimage/9/9a303e67dd/8/9a303e67dddc5c15fb06274f3f0a1bd8_0.jpg
         * vid : 9a303e67dddc5c15fb06274f3f0a1bd8_9
         */

        private List<QuickphotoBean> quickphoto;

        public List<?> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<?> recommend) {
            this.recommend = recommend;
        }

        public List<QuickphotoBean> getQuickphoto() {
            return quickphoto;
        }

        public void setQuickphoto(List<QuickphotoBean> quickphoto) {
            this.quickphoto = quickphoto;
        }

        public static class QuickphotoBean implements Serializable{
            private String id;
            private String title;
            private String video;
            private String count;
            private String nickname;
            private String vimage;
            private String vid;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getVimage() {
                return vimage;
            }

            public void setVimage(String vimage) {
                this.vimage = vimage;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }
        }
    }
}
