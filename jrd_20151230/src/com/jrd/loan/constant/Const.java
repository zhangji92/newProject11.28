package com.jrd.loan.constant;

/**
 * 常量
 */
public class Const {

  /**
   * 统计事件名称
   * @author Javen
   *
   */
  public static class EventName {

    public static final String START = "启动";
    public static final String LOGIN = "登陆";
    public static final String TRANSFEROUT = "转出";
    public static final String TRANSFERIN = "转入";
    public static final String WITHDRAW = "提现";
    public static final String INVEST = "投标";
    public static final String RECHARGE = "充值";
    public static final String TIECARD = "绑卡";
    public static final String REALNAME = "实名";
    public static final String REGISTER = "注册";
  }
  
  /**
   * 统计事件ID
   * @author Javen
   *
   */
  public static class EventId {
    public static final String START = "start";
    public static final String LOGIN = "login";
    public static final String TRANSFEROUT = "transferOut";
    public static final String TRANSFERIN = "transferIn";
    public static final String WITHDRAW = "withdraw";
    public static final String INVEST = "invest";
    public static final String RECHARGE = "recharge";
    public static final String TIECARD = "tiecard";
    public static final String REALNAME = "realName";
    public static final String REGISTER = "register";
  }

  public static class Type {
    /**
     * 支付密码
     * <p/>
     * 1.重置交易密码
     * <p/>
     * 2.修改交易密码
     * <p/>
     * 3.设置交易密码
     * <p/>
     * 登录密码
     * <p/>
     * 4.修改登录密码
     * <p/>
     * 5.未登录重置登录密码
     */
    public static final int Complete_Psw_Transaction_Reset = 1;
    public static final int Complete_Psw_Transaction_Update = 2;
    public static final int Complete_Psw_Transaction_Set = 3;
    public static final int Complete_Psw_Login_Update = 4;
    public static final int Complete_Psw_Login_Reset = 5;

    /**
     * 58 密码状态
     * <p/>
     * 0.未设置
     * <p/>
     * 1.已设置
     */
    public static final String Psw_NotSet = "未设置";
    public static final String Psw_Set = "已设置";

    /**
     * 提现界面跳到忘记交易密码界面
     */
    public static final int WITHDRAW_TO_TRANS = 101;
    /**
     * 修改交易密码界面跳到忘记交易密码界面
     */
    public static final int UPDATE_TO_TRANS = 102;

    public static final String TRANS = "trans";

    /**
     * 设置手势密码
     * <p/>
     * 1.注册之后设置手势密码
     */
    public static final int SET_GESTURE_REGISTER = 1;
    /**
     * 注册之后跳转到我的优惠券
     */
    public static final int REGIST_MYCOUPON = 103;

  }

  public static class Extra {
    public static final String Select_ID = "select_id";// 选择id
    public static final String Select_Info = "select_info";// 选择信息
    public static final String USER_ID = "userid";// 用户ID
    public static final String USER_NAME = "userName";// 用户名
    public static final String Token = "token";
    public static final String Login_Account = "login_account";// 登录账号
    public static final String SecurityLevel = "securityLevel";// 安全等级
    public static final String MobileNumber = "mobileNumber";// 手机号码
    public static final String IdNumberInfo = "idNumberInfo";// 身份验证（身份证号）
    public static final String IdNumberFlag = "idNumberFlag";// 身份验证（身份证号）
    public static final String PswInfo = "passwordInfo";// 登录密码是否设置
    public static final String TransInfo = "transPwdInfo";// 交易密码是否设置
    public static final String TransFlag = "transPwdFlag";// 交易密码是否设置
    public static final String BindCardInfo = "boundCardFlag";// 是否绑卡
    public static final String QuickCardInfo = "quickCardInfo";// 是否绑快捷卡

  }

  public static class HandlerCode {
    /**
     * 删除银行卡时的handler code
     */
    public static final int CARD_DELETE_HANDLER_CODE = 1002;
  }

  public static class IntentCode {

    /**
     * 提现界面跳转到选择银行卡时传的intent code
     */
    public static final int WITHDRAW_SELECTOR_CARD_INTENT_RESULT_CODE = 2001;


    /**
     * 从提现界面跳转到添加银行卡界面
     */
    public static final int WITHDRAW_TO_ADDCRAD_ITNENT_CODE = 2002;
    /**
     * 从我的银行卡界面跳转到添加银行卡界面
     */
    public static final int MYCARD_TO_ADDCRAD_ITNENT_CODE = 2003;

    /**
     * 从提现或者充值界面跳转到身份验证界面
     */
    public static final int RECHARGE_TO_IDCHECK_INTENT_CODE = 2004;

    /**
     * 从绑定银行卡界面跳转到开户行界面
     */
    public static final int ADDCORD_TO_OPENING_INTENT_CODE = 2005;
    /**
     * 从充值界面跳转到webview界面
     */
    public static final int ADDCORD_TO_WEBVIEW_INTENT_CODE = 2006;
    /**
     * 跳转到选择银行卡界面
     */
    public static final int INTENT_TO_SELECT_ACTIVITY = 2007;
    /**
     * 从注册界面跳转到绑卡界面
     */
    public static final int REGIST_TO_BINDCARD_INTENT_CODE = 2008;

    /**
     * 从选择银行卡跳转到绑卡界面
     */
    public static final int SELECT_TO_ADD_BANK_CARD_INTENT_CODE = 2009;

    /**
     * 从我的银行卡界面跳转到充值并绑卡界面
     */
    public static final int MYBANKCARD_TO_RECHARGE_INTENT_CODE = 2010;
  }
}
