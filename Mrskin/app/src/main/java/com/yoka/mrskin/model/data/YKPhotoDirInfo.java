/**  
 * @Title: PhotoDirInfo.java 
 * @Package com.example.util 
 * @Description: TODO() 
 * @author Derek 
 * @email renchun525@gmail.com
 * @date 2013-11-12 下午3:33:21 
 * @version V1.0  
 */ 
package com.yoka.mrskin.model.data;

import java.io.Serializable;

/** 
 * @ClassName: PhotoDirInfo 
 * @author Y H L
 */

public class YKPhotoDirInfo implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dirId;
    private String dirName;
    private String firstPicPath;
    private boolean isUserOtherPicSoft;
    private int picCount = 0;

    public YKPhotoDirInfo()
    {
        
    }

    public YKPhotoDirInfo(String paramString, boolean paramBoolean)
    {
        this.dirName = paramString;
        this.isUserOtherPicSoft = paramBoolean;
    }

    public String getDirId()
    {
        return this.dirId;
    }

    public String getDirName()
    {
        return this.dirName;
    }

    public String getFirstPicPath()
    {
        return this.firstPicPath;
    }

    public int getPicCount()
    {
        return this.picCount;
    }

    public boolean isUserOtherPicSoft()
    {
        return this.isUserOtherPicSoft;
    }

    public void setDirId(String paramString)
    {
        this.dirId = paramString;
    }

    public void setDirName(String paramString)
    {
        this.dirName = paramString;
    }

    public void setFirstPicPath(String paramString)
    {
        this.firstPicPath = paramString;
    }

    public void setPicCount(int paramInt)
    {
        this.picCount = paramInt;
    }

    public void setUserOtherPicSoft(boolean paramBoolean)
    {
        this.isUserOtherPicSoft = paramBoolean;
    }
}
