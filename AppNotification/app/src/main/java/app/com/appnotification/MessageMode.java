package app.com.appnotification;

/**
 * Created by lenovo on 2016/9/20.
 * 短信实体类
 */

public class MessageMode {
    String address;
    String content;

    public MessageMode(String address, String content) {
        this.address = address;
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
