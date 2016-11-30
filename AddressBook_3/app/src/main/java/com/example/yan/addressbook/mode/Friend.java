package com.example.yan.addressbook.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/16.
 */
public class Friend implements Comparable<Friend>,Serializable {

    private String friendId;

    private String friendName;

    private String friendNub;

    private String friendCompany;

    private String friendEmail;

    public Friend(String friendId,String friendName, String friendNub) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendNub = friendNub;
    }

    public Friend(String friendName, String friendNub) {
        this.friendName = friendName;
        this.friendNub = friendNub;
    }

    public Friend(){}

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendNub() {
        return friendNub;
    }

    public void setFriendNub(String friendNub) {
        this.friendNub = friendNub;
    }

    public String getFriendCompany() {
        return friendCompany;
    }

    public void setFriendCompany(String friendCompany) {
        this.friendCompany = friendCompany;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    @Override
    public int compareTo(Friend another) {

        return 0;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }
}
