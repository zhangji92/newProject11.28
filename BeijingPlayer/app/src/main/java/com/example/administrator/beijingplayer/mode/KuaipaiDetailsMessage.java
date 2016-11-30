package com.example.administrator.beijingplayer.mode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class KuaipaiDetailsMessage implements Serializable{

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"id":"78","uid":"34","title":"相信自己","video":"http://mpv.videocc.net/9a303e67dd/e/9a303e67dddecb479d4799518cab22ae_1.mp4","vid":"9a303e67dddecb479d4799518cab22ae_9","vimage":"http://img.videocc.net/uimage/9/9a303e67dd/e/9a303e67dddecb479d4799518cab22ae_0.jpg","count":"10","status":"2","likecount":"0","addtime":"2015-11-19 16:59:14","nickname":"龙的传人","pic":"/data/upfiles/201511/1400102060.jpg","ispraise":"2","commentnum":"1","comment":[{"id":"109","qpid":"78","uid":"90","content":"URL","addtime":"2016-09-26 15:35:55","nickname":"摸摸明","pic":"/data/upfiles/201609/1214572862.jpg"}]}
     */

    private String code;
    private String message;
    /**
     * id : 78
     * uid : 34
     * title : 相信自己
     * video : http://mpv.videocc.net/9a303e67dd/e/9a303e67dddecb479d4799518cab22ae_1.mp4
     * vid : 9a303e67dddecb479d4799518cab22ae_9
     * vimage : http://img.videocc.net/uimage/9/9a303e67dd/e/9a303e67dddecb479d4799518cab22ae_0.jpg
     * count : 10
     * status : 2
     * likecount : 0
     * addtime : 2015-11-19 16:59:14
     * nickname : 龙的传人
     * pic : /data/upfiles/201511/1400102060.jpg
     * ispraise : 2
     * commentnum : 1
     * comment : [{"id":"109","qpid":"78","uid":"90","content":"URL","addtime":"2016-09-26 15:35:55","nickname":"摸摸明","pic":"/data/upfiles/201609/1214572862.jpg"}]
     */

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
        private String id;
        private String uid;
        private String title;
        private String video;
        private String vid;
        private String vimage;
        private String count;
        private String status;
        private String likecount;
        private String addtime;
        private String nickname;
        private String pic;
        private String ispraise;
        private String commentnum;
        /**
         * id : 109
         * qpid : 78
         * uid : 90
         * content : URL
         * addtime : 2016-09-26 15:35:55
         * nickname : 摸摸明
         * pic : /data/upfiles/201609/1214572862.jpg
         */

        private List<CommentBean> comment;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getVimage() {
            return vimage;
        }

        public void setVimage(String vimage) {
            this.vimage = vimage;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLikecount() {
            return likecount;
        }

        public void setLikecount(String likecount) {
            this.likecount = likecount;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getIspraise() {
            return ispraise;
        }

        public void setIspraise(String ispraise) {
            this.ispraise = ispraise;
        }

        public String getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(String commentnum) {
            this.commentnum = commentnum;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public static class CommentBean {
            private String id;
            private String qpid;
            private String uid;
            private String content;
            private String addtime;
            private String nickname;
            private String pic;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getQpid() {
                return qpid;
            }

            public void setQpid(String qpid) {
                this.qpid = qpid;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }
}
