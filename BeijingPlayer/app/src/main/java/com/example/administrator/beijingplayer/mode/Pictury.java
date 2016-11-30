package com.example.administrator.beijingplayer.mode;

/**
 * 图片对象
 */
public class Pictury {

    private int id;

    private String title;

    private String pic;

    private String link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Pictury(int id, String title, String pic, String link) {
        this.id = id;
        this.title = title;
        this.pic = pic;
        this.link = link;
    }

    public Pictury() {
    }
}
