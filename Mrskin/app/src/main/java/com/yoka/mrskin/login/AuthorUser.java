package com.yoka.mrskin.login;

/**
 * yk服务器登录接口上传数据结构
 * 
 * @author Jack
 * 
 */
public class AuthorUser
{

    private String access_token;
    private String nickname;
    private String user_id;
    private String avatar_url;
    private String type;
    private String gender;
    
    public String getAccess_token()
    {
        return access_token;
    }

    public void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getAvatar_url()
    {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url)
    {
        this.avatar_url = avatar_url;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

}
