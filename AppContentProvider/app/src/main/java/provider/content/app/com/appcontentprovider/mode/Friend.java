package provider.content.app.com.appcontentprovider.mode;

/**
 * Created by lenovo on 2016/9/18.
 * 联系人工具类
 */
public class Friend {
    //联系人的id
    private int friendId;
    //联系人姓名
    private String friendName;
    //联系人的email
    private String friendEmail;
    //联系人的号码
    private String friendNo;
    private String friendCompany;

    public Friend(String friendName, String friendNo, int friendId) {
        this.friendName = friendName;
        this.friendNo = friendNo;
        this.friendId = friendId;
    }

    public Friend(String friendName, String friendNo) {
        this.friendName = friendName;
        this.friendNo = friendNo;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(String friendNo) {
        this.friendNo = friendNo;
    }

    public String getFriendCompany() {
        return friendCompany;
    }

    public void setFriendCompany(String friendCompany) {
        this.friendCompany = friendCompany;
    }

}
