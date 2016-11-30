package com.example.administrator.beijingplayer.util;

/**
 * Created by Administrator on 2016/8/30.
 */
public class ModeCode {
    //用户对象key
    public static final String USER = "user";

    public static final int PHOTO_ALBUM = 0x10;

    public  static final int START_CAMERA =0x12;
    //登录状态
    public static final  String IS_LOGIN = "islogin";


    public static final String TAG="-------------";
    //网址
    public static final String HTTP ="http://tc.ceol8.com";
    //get首页信息地址
    public static final String HOME = HTTP+"/service/index.php?model=home&action=home_new";
   //player信息地址
    public static final String PLAYER = HTTP+"/service/index.php?model=city&action=indexcity";
    /***
     * 第三个信息
     */
    public static final String THREE = HTTP+"/service/index.php?model=favorable&action=favorableshoplist";
    /**
     * 注册接口
     */
    public static final String REGISTER = HTTP+"/service/index.php?model=user&action=register";
    /***
     * 登录接口
     */
    public static final String LOGIN =HTTP+"/service/index.php?model=user&action=login";
    /***
     * 验证码接口
     */
    public static final String YANZHENG = HTTP+"/service/index.php?model=user&action=verifycode";
    /***
     * 头像接口
     */
    public static final String PHOTO =HTTP+"/service/index.php?model=user&action=uploaduserpic";
    /***
     * 修改个人信息接口
     */
    public static final String SET =HTTP+"/service/index.php?model=user&action=edituser";
    /**
     * 快拍列表接口
     */
    public static final String KUAIPAI = HTTP+"/service/index.php?model=city&action=quickphotolist";
    /**
     * 快拍详情
     */
    public static final String KUAIPAI_DETAILS = HTTP+"/service/index.php?model=city&action=quickphotoinfobyid1";
    /***
     * 场所
     */
    public static final String CHANGSUO = HTTP+"/service/index.php?model=city&action=placelist";
    /***
     * 场所筛选
     */
    public static final String CHANGSUO_SHAIXUAN = HTTP+"/service/index.php?model=user&action=getcategorybytype";

    public static final String WINEAGENT = HTTP+"/service/index.php?model=city&action=agent";
    /***
     * 服务器获取数据的标识 首页
     */
    public static final int HOME_WHAT =0x11111;
    /***
     * 发送轮播信息
     */
    public static final int VIEW_PAGER =0x11100;
    /***
     * 百度地图地位成功信息
     */
    public static final int ADDRESS =0x11110;
    /***
     * 服务器获取数据的标识 第二页
     */
    public static final int PLAYER_WHAT =0x20000;
    /***
     * 服务器获取数据的标识 第三页
     */
    public static final int THREE_WHAT = 0x22222;

    /***
     * 欢迎页跳转首页
     */
    public static final int WELCOME_WHAT =0x10000;
    /***
     * 验证码
     */
    public static final int REGISTE_VALIDATE = 0x1a2f;
    /***
     * 注册标识
     */
    public static final int REGISTER_WHAT =0x1e2c;
    //登录标识
    public static final int LOGIN_WHAT =0x1c2c3c;
    /***
     * 修改图片
     */
    public static final int PHOTO_WHAT =0x1c2c3D;
    /***
     *修改个人信息
     */
    public static final int SET_WHAT =0x1c2672;
    /***
     * 快拍列表
     */
    public static final int KUAIPAI_WHAT = 0x0987a;
    /***
     * 快拍详情
     */
    public static final int KUAIPAI_WHAT_DETAILS = 0x0987a;
    /***
     * 酒水代理
     */
    public static final int WINEAGENT_WHAT = 0x000ff0;
    /**
     * 场所
     */
    public static final int CHANGSUO_WHAT = 0x00acd0;
    /**
     * 场所筛选
     */
    public static final int CHANGSUO_SHAIXUAN_WHAT = 0x00abcd;
}
