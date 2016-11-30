package com.example.lenovo.amuse.mode;

import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 * 快拍列表
 */

public class SnapShortMode {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"recommend":[],"quickphoto":[{"id":"87","title":"图片","video":"http://mpv.videocc.net/9a303e67dd/8/9a303e67dddc5c15fb06274f3f0a1bd8_1.mp4","count":"38","nickname":"GAME0058","vimage":"http://img.videocc.net/uimage/9/9a303e67dd/8/9a303e67dddc5c15fb06274f3f0a1bd8_0.jpg","vid":"9a303e67dddc5c15fb06274f3f0a1bd8_9"}]}
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
         * count : 38
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

        public static class QuickphotoBean {
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
