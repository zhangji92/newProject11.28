package com.yoka.mrskin.model.managers.base;

/**
 * 接口请求头参数
 * 
 * @ClassName Header
 * @Description Request的Header信息
 * @author huangke@yoka.com
 * @date 2012-2-22 下午04:45:08
 * 
 */
public class Header
{
	/**
	 * @Fields USER_AGENT 用户代理
	 */
	public static final String USER_AGENT = "User-Agent";
	
    /**
     * @Fields CUSTOMER_ID 产品标识
     */
    public static final String CUSTOMER_ID = "hc";

    /**
     * @Fields MODEL 设备机型
     */
    public static final String MODEL = "hmd";

    /**
     * @Fields SCREEN_SIZE 设备分辨率
     */
    public static final String SCREEN_SIZE = "hsz";

    /**
     * @Fields VERSION 客户端软件版本
     */
    public static final String VERSION = "hv";

    /**
     * @Fields SYSTEM_VERSION 系统版本
     */
    public static final String SYSTEM_VERSION = "hsv";

    /**
     * @Fields USER_ID 用户标识
     */
    public static final String USER_ID = "hu";

    /**
     * @Fields AGENCY 渠道标识
     */
    public static final String AGENCY = "ha";

    /**
     * @Fields ACCESS_MODE 接入方式
     */
    public static final String ACCESS_MODE = "ham";

    /**
     * @Fields INTERFACE 接口名称
     */
    public static final String INTERFACE = "hi";
    /**
     * @Fields UID 用户id
     */
    public static final String UID = "hucid";
    /**
     * @Fields 广告标识符
     */
    public static final String IDFA = "hucid";
}
