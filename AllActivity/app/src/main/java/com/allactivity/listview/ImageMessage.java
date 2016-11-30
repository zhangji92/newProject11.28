package com.allactivity.listview;

import com.allactivity.R;

/**
 * Created by 张继 on 2016/11/10.
 * 个人信息
 */

public class ImageMessage {
    private String title;
    private String content;
    private int pic;

    public ImageMessage() {
    }

    public ImageMessage(String title, String content, int pic) {
        this.title = title;
        this.content = content;
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
