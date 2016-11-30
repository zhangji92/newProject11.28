package com.jrd.loan.api;

public final class WebApi {
	public static class AppApi {
		// 应用升级
		public final static String APP_UPGRADE = "appapi/appUpdate";
		// 查询广告位
		public final static String GETBANNERINFO = "appapi/getIdxBannerInfo";
		// 新的获取banner广告接口
		public final static String OBTAIN_BANNER_INFO = "appapi/idxBannerNew";
	}

	public static class Account {
		// 登录
		public final static String LOGIN = "appapi/login";
		// 退出登录
		public final static String LOGINOUT = "appapi/logout";
		public final static String REGISTER = "appapi/registerMobile";
		public final static String REGISTERPSW = "appapi/registerPassword";
		// 绑定手机号
		public final static String BINDMOBILE = "appapi/bindMobile";
		// 注册身份验证
		public final static String REGISTERREALNAME = "appapi/registerRealName";
		// 注册协议 html
		public final static String GETREGAGREEMENTINFO = "appapi/getRegAgreementInfoHtml";
		// 发送手机验证码接口
		public final static String GETMOBILECODE = "appapi/getMobileCode";
		// 获取登录验证码接口 html
		public final static String GETLOGINVCODE = "appapi/getValidCodeHtml";
		// 修改登陆密码接口
		public final static String UPDLOGINPWD = "appapi/updLoginPwd";
	}

	public static class MyAccountInfoApi {

		// 查询我的账户信息接口
		public final static String GETACCOUNTINFO = "appapi/getAccountInfo";
		// 查询个人信息接口
		public final static String GETPERSONALINFO = "appapi/getPersonalInfo";
	}

	public static class SevenPayYou {

		// 查询7付你加入记录接口 html
		public final static String GET7FNRECORDINFO = "appapi/get7fnRecordInfoHtml";
		// 7付你投资接口
		public final static String G7FNTRANSACTION = "appapi/7fnTransaction";
		// 查询7付你信息接口
		public final static String GET7FNINFO = "appapi/get7fnInfo";
		// 7付你产品详情业务介绍 Html
		public final static String GET7FNDETAILINFO = "appapi/get7fnDetailInfoHtml";
		// 7付你产品详情风控管理信息 html
		public final static String GET7FNRISKINFO = "appapi/get7fnRiskInfoHtml";
	}

	public static class BankApi {

		// 查询银行所属地接口
		public final static String GETBANKAREAINFO = "appapi/getBankAreaList";
		// 绑定银行卡接口
		public final static String ADDBANKCARD = "appapi/addBankCard";
		// 查询已绑定银行卡接口
		public final static String GETOWNBANKLIST = "appapi/getOwnBankList";

		public final static String WITHDRAW_ADDBANGCARD = "appapi/tieBankCard";
	}

	public static class PayApi {

		// 提现费用计算接口
		public final static String PRESENTCOST = "appapi/presentCost";
		// 充值接口
		public final static String TORECHARGE = "appapi/toRechargeHtml";
		// 提现接口
		public final static String WITHDRAWCASH = "appapi/withdrawCash";

		// 充值获取订单信息3
		public final static String OBTAIN_RECHARGE_ORDER = "appapi/appRechargeOrder";

		// 重置支付密码第一步调用
		public final static String VERIFYMOBILECODE = "appapi/verifyMobileCode";
		// 设置交易密码
		public final static String SETTRANSPWD = "appapi/setTransPwd";
		// 修改交易密码
		public final static String UPDTRANSPWD = "appapi/updTransPwd";
		// 查询我的总资产接口
		public final static String GETTOTALMONEY = "appapi/getTotalMoneyHtml";
		// 查询我的投资记录接口
		public final static String GETUBVESTRECORD = "appapi/getInvestRecord";
		// 查询回款记录接口
		public final static String GETINVESTREPAYMENTPLAN = "appapi/getInvestRepaymentPlanHtml";
		// 查询交易记录接口
		public final static String GETTRANSRECORD = "appapi/getTransRecordHtml";
		// 付款账号查询2
		public final static String PAYMENTACCOUNT = "appapi/paymentAccount";
		// 活期转入(充值)2账户余额充值
		public final static String USERHUOQIINVEST = "appapi/userHuoqiInvest";
		// 活期转入(充值)2银行卡充值H5
		public final static String USERHUOQIINVESTWAP = "appapi/userHuoqiInvestWap";
		// 活期转出(提取)2
		public final static String USERHUOQITAKEOUT = "appapi/userHuoqiTakeout";
		// 活期投资协议2
		public final static String HUOQIAGREEMENT = "appapi/huoqiAgreementInfoHtml";
		// 提现详细介绍2
		public final static String WITHDRAWAGREEMENT = "appapi/withdrawAgreementHtml";
		// 活期银行卡转入详细结果
		public final static String USERHUOQIINVESTRESULT = "appapi/userHuoqiInvestResult";
	}

	public static class FinanceApi {

		// 查询标的详情接口
		public final static String GETPROJECTINFO = "appapi/getProjectInfo";
		// 查询标的项目详情接口
		public final static String GETPROJECTDETAILINFO = "appapi/getProjectDetailInfo";
		// 项目介绍--投标记录
		public final static String GETPROJECTRECORDS = "appapi/getProjectRecordInfo";
		// 标的投标接口
		public final static String INVEST = "appapi/invest";
		// 查询可用优惠券
		public final static String COUPON_LIST = "appapi/getPacketBylimit";
		// 查询投资协议内容接口 html
		public final static String GETAGREEMENTINFO = "appapi/getAgreementInfoHtml";
		// 查询标的投标记录接口
		public final static String GEPROJECTRECORDINFO = "appapi/getProjectRecordInfo";
		// 查询首页标的信息接口
		public final static String GETIDPROJECTINFO = "appapi/getIdxProjectInfo";
		// 查询直投项目列表接口
		public final static String GETPROJECTLISTINFO = "appapi/getProjectListInfo";
		// 查询我的优惠券接口
		public final static String GETCOUPONINFO = "appapi/getCouponInfo";
		// 优惠券兑换接口
		public final static String EXCHANGEREDPACKET = "appapi/exchangeRedPacket";

		// 服务协议及风险提示
		public final static String SERVICE_COMPACT_AND_REMIND_TIPS = "appapi/bankBindAgreementHtml";
	}

	public static class PocketApi {

		// 实名认证以及绑卡
		public final static String BINDCARDANDREALNAME = "appapi/realNameCard";
		// 绑定银行卡
		public final static String BINDCARD = "appapi/tieBankCard";
		// （转入记录）
		public final static String INTORECORD = "appapi/userHuoqiInvestRecord";
		// （转出记录）
		public final static String OUTORECORD = "appapi/userHuoqiTakeoutRecord";
		// 转入详情
		public final static String INTODETAILS = "appapi/userHuoqiInvestDetail";
		// 转出详情
		public final static String OUTODETAILS = "appapi/userHuoqiTakeoutDetail";
		// 收益明细
		public final static String INCOMEDETAILS = "appapi/userHuoqiProfitRecord";
		// 活期标的详情
		public final static String POCIKETDETAILS = "appapi/huoqiProjectDetail";
		// 活期标的投资记录
		public final static String POCIKETINVESTRECORD = "appapi/huoqiProjectRecord";
	}
}
