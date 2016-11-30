package com.jrd.loan.finance;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.FinanceApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.FirstInvestInfo;
import com.jrd.loan.bean.ProjectDetailBean;
import com.jrd.loan.bean.ProjectList;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jrd.loan.pulltorefresh.library.PullToRefreshScrollView;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DigitUtil;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.RoundProgressBar;
import com.jrd.loan.widget.WindowView;

/**
 * 项目介绍
 * 
 * @author Jacky
 */
public class ProjectIntroduceActivity extends BaseActivity {
	private WindowView windowView;
	private PullToRefreshScrollView pullToRefreshView;
	private RoundProgressBar circleProgress;
	public static ProjectList projectList;
	private Button btnBidNow; // 立即投标
	private boolean isRefresh;

	@Override
	protected int loadWindowLayout() {
		return R.layout.loan_project_introduce_layout;
	}

	@Override
	protected void initViews() {
		this.pullToRefreshView = (PullToRefreshScrollView) findViewById(R.id.loan_project_refreshscrollview);
		this.pullToRefreshView.setPullToRefreshEnabled(true);
		this.pullToRefreshView.setPullToRefreshOverScrollEnabled(false);
		this.pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				isRefresh = true;
				obtainProjectIntroduce();
			}
		});

		this.circleProgress = (RoundProgressBar) findViewById(R.id.circlePro);
		int proSize = getResources().getDisplayMetrics().widthPixels * 3 / 5;
		LayoutParams layoutParams = new LayoutParams(proSize, proSize);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		layoutParams.topMargin = (int) getResources().getDimension(R.dimen.loan_bid_dis_top);
		layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.loan_round_pro_margin_bottom);
		this.circleProgress.setLayoutParams(layoutParams);

		// 更多详情
		TextView moreDetail = (TextView) findViewById(R.id.moreDetail);
		moreDetail.setOnClickListener(this.btnClickListener);

		// 投标记录
		LinearLayout bidPersonLayout = (LinearLayout) findViewById(R.id.bidPersonLayout);
		bidPersonLayout.setOnClickListener(this.btnClickListener);

		// 立即投标button
		this.btnBidNow = (Button) findViewById(R.id.btnBidNow);
		this.btnBidNow.setEnabled(false);
		this.btnBidNow.setBackgroundColor(getResources().getColor(R.color.loan_btn_color_gray));
		this.btnBidNow.setVisibility(View.GONE);
		this.obtainProjectIntroduce();

		setNoNetworkClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				obtainProjectIntroduce();
			}
		});
	}

	private void startRoundProgressAnim(int currPro) {
		ValueAnimator animator = ValueAnimator.ofInt(0, currPro);
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator value) {
				circleProgress.setProgress((Integer) value.getAnimatedValue());
			}
		});
		animator.setDuration(1000);
		animator.start();
	}

	private void obtainProjectIntroduce() {
		FinanceApi.obtainProjectIntroduce(this, getIntent().getStringExtra("mockId"), new OnHttpTaskListener<ProjectDetailBean>() {

			@Override
			public void onTaskError(int resposeCode) {
				DismissDialog();
				if (isRefresh) {
					pullToRefreshView.onRefreshComplete();
					isRefresh = false;
				}
			}

			@Override
			public void onStart() {
				if (!isRefresh) {
					ShowDrawDialog(ProjectIntroduceActivity.this);
				}
			}

			@Override
			public void onFinishTask(ProjectDetailBean bean) {
				DismissDialog();
				if (isRefresh) {
					pullToRefreshView.onRefreshComplete();
					isRefresh = false;
				}
				if (bean.getResultCode() == 0) {
					windowView.setVisibility(View.VISIBLE);
					btnBidNow.setVisibility(View.VISIBLE);
					projectList = bean.getProjectInfo();
					updateProjectData(projectList);
					if (projectList.getIsFirstInvest() == 1) {// 有先声夺人
						FirstInvestInfo firstInvestInfo = bean.getFirstInvestInfo();
						updateFirstInvestInfo(firstInvestInfo, projectList.getStatus());
						LinearLayout rsdrLayout = (LinearLayout) findViewById(R.id.rsdrLayout);
						rsdrLayout.setVisibility(View.VISIBLE);
						rsdrLayout.setTag(firstInvestInfo.getFirstInvestUrl());
						rsdrLayout.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								Intent intent = new Intent(ProjectIntroduceActivity.this, WebViewActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("htmlUrl", (String) view.getTag());
								intent.putExtra("htmlTitle", getString(R.string.loan_str_rsdr));
								startActivity(intent);
							}
						});
					} else {
						LinearLayout rsdrLayout = (LinearLayout) findViewById(R.id.rsdrLayout);
						rsdrLayout.setVisibility(View.GONE);
					}
				} else {
					ToastUtil.showShort(ProjectIntroduceActivity.this, bean.getResultMsg());
				}
			}
		});
	}

	private void updateProjectData(ProjectList projectList) {
		// 立即投标
		String status = projectList.getStatus();
		if (status.equals("110")) {// 预上线
			String comingInfo = projectList.getComingInfo();
			this.btnBidNow.setText(TextUtils.isEmpty(comingInfo) ? "" : comingInfo);
		} else if (status.equals("201")) {// 投标中
			this.btnBidNow.setEnabled(true);
			this.btnBidNow.setBackgroundResource(R.drawable.loan_bid_btn_selector);
			this.btnBidNow.setOnClickListener(this.btnClickListener);
		} else if (status.equals("209")) {// 投标完成
			this.btnBidNow.setText(R.string.loan_btn_bid_state_full);
		} else if (status.equals("301")) {// 还款中
			this.btnBidNow.setText(R.string.loan_btn_bid_state_payback);
		} else if (status.equals("309")) {// 还款完成
			this.btnBidNow.setText(R.string.loan_btn_bid_state_pay_complete);
		} else if (status.equals("401")) {// 关闭---已满额
			this.btnBidNow.setText(R.string.loan_btn_bid_state_closed);
		}

		// 项目名称和编码
		TextView projectName = (TextView) findViewById(R.id.projectNameAndCode);
		projectName.setText(projectList.getProjectName());
		projectName.append("  ");
		projectName.append(projectList.getProjectCode());

		// 融资产品类型
		ImageView proType = (ImageView) findViewById(R.id.proType);
		String type = projectList.getType();
		if (type.equals("100")) {// 企
			proType.setImageResource(R.drawable.loan_pro_enterprises);
		} else if (type.equals("200") || type.equals("600")) {// 房
			proType.setImageResource(R.drawable.loan_pro_real);
		} else if (type.equals("300")) {// 供
			proType.setImageResource(R.drawable.loan_pro_type);
		} else if (type.equals("400")) {// 核
			proType.setImageResource(R.drawable.loan_pro_nuclear);
		} else if (type.equals("500")) {// 消
			proType.setImageResource(R.drawable.loan_pro_fire);
		} else {
			proType.setVisibility(View.GONE);
		}

		// 标的拥有者
		TextView bidOwner = (TextView) findViewById(R.id.bidOwner);
		bidOwner.setText(projectList.getBrandName());

		// 年化利率
		TextView yearRate = (TextView) findViewById(R.id.yearRate);
		TextView yearRateAddSign = (TextView) findViewById(R.id.yearRateAddSign);
		TextView yearRatePercentNum = (TextView) findViewById(R.id.yearRatePercentNum);
		StringBuffer yearRateBuffer = new StringBuffer();
		yearRateBuffer.append("<big>").append(projectList.getAnnualRate());
		yearRateBuffer.append("</big>");
		yearRate.setText(Html.fromHtml(yearRateBuffer.toString()));

		if (TextUtils.isEmpty(projectList.getRewardRate()) || projectList.getRewardRate().equals("0") || projectList.getRewardRate().equals("0.0")) {
			yearRateAddSign.setVisibility(View.GONE);
			yearRatePercentNum.setVisibility(View.GONE);
		} else {
			yearRateAddSign.setVisibility(View.VISIBLE);
			yearRatePercentNum.setVisibility(View.VISIBLE);
			yearRateAddSign.setText(Html.fromHtml("<big>+</big>"));
			yearRatePercentNum.setText(projectList.getRewardRate());
		}

		// 项目期限
		TextView projectPeriod = (TextView) findViewById(R.id.projectPeriod);
		projectPeriod.setText(R.string.loan_Project_duration);
		projectPeriod.append(": ");
		projectPeriod.append(DigitUtil.getMonth(projectList.getInvestmentPeriod()));
		projectPeriod.append(getString(R.string.loan_a_month));

		// 可投金额
		TextView canInvestMoney = (TextView) findViewById(R.id.canInvestMoney);
		canInvestMoney.setText(R.string.loan_amount_investment);
		canInvestMoney.append(": ");
		canInvestMoney.append(DigitUtil.getMoney(projectList.getInvestmentAmount()));

		// 标的投资进度
		this.circleProgress.setMax(100);
		this.startRoundProgressAnim((int) Double.parseDouble(projectList.getInvestPercentage()));

		// 投标人数
		if (this.projectList.getInverstRecordCount() > 0) {
			TextView bidRecord = (TextView) findViewById(R.id.bidRecord);
			bidRecord.setText(getString(R.string.loan_bid_person_count, this.projectList.getInverstRecordCount()));
		}

		// 融资金额
		TextView financingAmount = (TextView) findViewById(R.id.financingAmount);
		financingAmount.setText(R.string.loan_rz_money);
		financingAmount.append("\n");
		StringBuffer amountBuffer = new StringBuffer();
		amountBuffer.append("<font color='#1193D8'>");
		amountBuffer.append(DigitUtil.getMoney(projectList.getFinanceAmt()));
		amountBuffer.append("</font>");
		financingAmount.append(Html.fromHtml(amountBuffer.toString()));

		// 还款方式
		TextView repayWay = (TextView) findViewById(R.id.repayWay);
		String repaymentType = projectList.getRepaymentType();
		if (repaymentType.contains("，")) {
			String[] repayArr = repaymentType.split("，");
			repayWay.setText(repayArr[0]);
			repayWay.append("\n");
			repayWay.append(repayArr[1]);
		} else {
			repayWay.setText(repaymentType);
		}
	}

	private void updateFirstInvestInfo(FirstInvestInfo firstInvestInfo, String status) {
		// 先声夺人
		String userOneAccount = firstInvestInfo.getUserOneAccount();
		TextView tvXsdr = (TextView) findViewById(R.id.tvXsdr);

		if (TextUtils.isEmpty(userOneAccount)) {// 没有, 显示虚位以待
			tvXsdr.setText(R.string.loan_reward_xwyd);
		} else {
			tvXsdr.setText(R.string.loan_reward_owner);
			tvXsdr.append("\n");
			tvXsdr.append(userOneAccount);
		}

		// 一鸣惊人
		TextView tvYmjr = (TextView) findViewById(R.id.tvYmjr);
		String userTwoAmount = firstInvestInfo.getUserTwoAmount();

		if (TextUtils.isEmpty(userTwoAmount) || status.equals("110")) {// 虚位以待
			tvYmjr.setText(R.string.loan_reward_xwyd);
		} else if (status.equals("201")) {// 满标前
			tvYmjr.setText(R.string.loan_first_temp);
			tvYmjr.append("\n");
			tvYmjr.append(firstInvestInfo.getUserTwoAccount());
			tvYmjr.append("\n");
			StringBuffer ymjrBuffer = new StringBuffer();
			ymjrBuffer.append("<font color='#DF7D94'>");
			ymjrBuffer.append(DigitUtil.getMoney(firstInvestInfo.getUserTwoAmount()));
			ymjrBuffer.append("</font>");
			tvYmjr.append(Html.fromHtml(ymjrBuffer.toString()));
		} else {// 满标后
			tvYmjr.setText(R.string.loan_reward_owner);
			tvYmjr.append("\n");
			tvYmjr.append(firstInvestInfo.getUserTwoAccount());
			tvYmjr.append("\n");
			StringBuffer ymjrBuffer = new StringBuffer();
			ymjrBuffer.append("<font color='#DF7D94'>");
			ymjrBuffer.append(DigitUtil.getMoney(firstInvestInfo.getUserTwoAmount()));
			ymjrBuffer.append("</font>");
			tvYmjr.append(Html.fromHtml(ymjrBuffer.toString()));
		}

		// 完美收官
		TextView tvWmsg = (TextView) findViewById(R.id.tvWmsg);
		if (status.equals("201") || status.equals("110")) {// 未满标时显示虚位以待
			tvWmsg.setText(R.string.loan_reward_xwyd);
		} else {// 满标后, 显示最后一个投资者
			tvWmsg.setText(R.string.loan_reward_owner);
			tvWmsg.append(firstInvestInfo.getUserThreeAccount());
		}
	}

	private OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.moreDetail: // 更多详情
					showMoreDetailScreen();
					break;
				case R.id.bidPersonLayout: // 投标记录
					showBidRecordScreen();
					break;
				case R.id.btnBidNow: // 立即投标
					showBidNowScreen();
					break;
			}
		}
	};

	private void showMoreDetailScreen() {
		Intent intent = new Intent(this, MoreDetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	// 投标记录
	private void showBidRecordScreen() {
		Intent intent = new Intent(this, TenderRecordAct.class);
		intent.putExtra("mockId", getIntent().getStringExtra("mockId"));
		startActivity(intent);
	}

	private void showBidNowScreen() {
		LogUtil.d("HttpRequest", "idNumberInfo-->" + UserInfoPrefs.loadLastLoginUserProfile().idNumberInfo);
		if (!UserInfoPrefs.isLogin()) {// 未登录
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("come_from", "projectIntroduce");
			intent.putExtra("mockId", getIntent().getStringExtra("mockId"));
			startActivity(intent);
			overridePendingTransition(R.anim.loan_bottom_to_top, R.anim.loan_default);
		} else if (UserInfoPrefs.loadLastLoginUserProfile().idNumberFlag.equals("0")) {// 身份未验证
			Intent intent = new Intent(this, IdCheckActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("come_from", "projectIntroduce");
			intent.putExtra("mockId", getIntent().getStringExtra("mockId"));
			startActivity(intent);
		} else {
			if (this.projectList.getFirstFlag() == 0 && this.projectList.getIsNovice().equals("1")) {// 新手标的,
				// 非第一次投资
				ToastUtil.showShort(this, R.string.loan_only_new_hand_can_purchase);
			} else {// 第一次投资
				Intent intent = new Intent(this, TenderActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("projectList", this.projectList);
				startActivity(intent);
			}
		}
	}

	@Override
	protected void initTitleBar() {
		windowView = (WindowView) findViewById(R.id.windowView);
		windowView.setVisibility(View.INVISIBLE);
		// 项目介绍
		windowView.setTitle(R.string.loan_project_introduce);
		// 返回按钮单击事件
		windowView.setLeftButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
}
