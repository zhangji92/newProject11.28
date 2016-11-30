package com.jrd.loan.current.pocket.rehcarge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.myaccount.MyBankCardActivity;
import com.jrd.loan.widget.WindowView;

/**
 * 充值失败
 *
 * @author Aaron
 */
public class RechargeFailureActivity extends BaseActivity {
    private Context context;

    private TextView FailureInformationTV;// 错误信息
    private TextView AccountPhoneTV;// 电话
    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loan_AccountDetailsBtn: // 账户明细
                    showAccountDetailsBtn();
                    break;
                case R.id.loan_RetryBtn: // 重试
                    showImmediateInvestmentBtn();
                    break;
                case R.id.loan_AccountPhoneTV: // 客服电话
                    showcontactTV();
                    break;
            }
        }
    };
    private Button AccountDetailsBtn;// 账户明细
    private Button ImmediateInvestmentBtn;// 立即投资
    private int getIntentCode; //从我的银行卡界面跳转过来的

    @Override
    protected int loadWindowLayout() {
        context = RechargeFailureActivity.this;
        return R.layout.loan_recharge_failure_layout;
    }

    @Override
    protected void initViews() {

        FailureInformationTV = (TextView) findViewById(R.id.loan_FailureInformationTV);

        AccountPhoneTV = (TextView) findViewById(R.id.loan_AccountPhoneTV);
        AccountPhoneTV.setOnClickListener(this.clickListener);

        AccountDetailsBtn = (Button) findViewById(R.id.loan_AccountDetailsBtn);
        AccountDetailsBtn.setOnClickListener(this.clickListener);

        ImmediateInvestmentBtn = (Button) findViewById(R.id.loan_RetryBtn);
        ImmediateInvestmentBtn.setOnClickListener(this.clickListener);

        initData();
    }

    /*
     * 初始化数据
     */
    private void initData() {
        String ret_code = getIntent().getExtras().getString("ret_code", "");
        if (ret_code.equals("6666")) {
            FailureInformationTV.setText("落单系统错误");
        } else if (ret_code.equals("1000")) {
            FailureInformationTV.setText("支付授权令牌失效或错误");
        } else if (ret_code.equals("1001")) {
            FailureInformationTV.setText("商户请求签名错误");
        } else if (ret_code.equals("1002")) {
            FailureInformationTV.setText("支付服务超时，请重新支付");
        } else if (ret_code.equals("1003")) {
            FailureInformationTV.setText("正在支付中,请稍后");
        } else if (ret_code.equals("1004")) {
            FailureInformationTV.setText("商户请求参数校验错误(具体参数名)");
        } else if (ret_code.equals("1105")) {
            FailureInformationTV.setText("卡号在黑名单中");
        } else if (ret_code.equals("1106")) {
            FailureInformationTV.setText("允许的输入PIN次数超限");
        } else if (ret_code.equals("1107")) {
            FailureInformationTV.setText("您的银行卡暂不支持在线支付业务");
        } else if (ret_code.equals("1108")) {
            FailureInformationTV.setText("您输入的证件号、 姓名或手机号有误");
        } else if (ret_code.equals("1109")) {
            FailureInformationTV.setText("卡号和证件号不符");
        } else if (ret_code.equals("1110")) {
            FailureInformationTV.setText("卡状态异常");
        } else if (ret_code.equals("1111")) {
            FailureInformationTV.setText("交易异常，支付失败");
        } else if (ret_code.equals("1112")) {
            FailureInformationTV.setText("证件号有误");
        } else if (ret_code.equals("1113")) {
            FailureInformationTV.setText("持卡人姓名有误");
        } else if (ret_code.equals("1114")) {
            FailureInformationTV.setText("手机号有误");
        } else if (ret_code.equals("1115")) {
            FailureInformationTV.setText("该卡未预留手机号");
        } else if (ret_code.equals("1200")) {
            FailureInformationTV.setText("用户选择的银行暂不支持， 请重新选择其他银行进行支付/签约");
        } else if (ret_code.equals("1900")) {
            FailureInformationTV.setText("短信码校验错误");
        } else if (ret_code.equals("1901")) {
            FailureInformationTV.setText("短信码已失效");
        } else if (ret_code.equals("2004")) {
            FailureInformationTV.setText("签约处理中");
        } else if (ret_code.equals("2005")) {
            FailureInformationTV.setText("原交易已在进行处理， 请勿重复提交");
        } else if (ret_code.equals("2006")) {
            FailureInformationTV.setText("交易已过期");
        } else if (ret_code.equals("2007")) {
            FailureInformationTV.setText("交易已支付成功");
        } else if (ret_code.equals("2008")) {
            FailureInformationTV.setText("交易处理中");
        } else if (ret_code.equals("3001")) {
            FailureInformationTV.setText("非法商户");
        } else if (ret_code.equals("3002")) {
            FailureInformationTV.setText("商户无此业务权限");
        } else if (ret_code.equals("3003")) {
            FailureInformationTV.setText("用户签约失败");
        } else if (ret_code.equals("3004")) {
            FailureInformationTV.setText("用户解约失败");
        } else if (ret_code.equals("3005")) {
            FailureInformationTV.setText("暂时不支持该银行卡支付");
        } else if (ret_code.equals("3006")) {
            FailureInformationTV.setText("无效的银行卡信息");
        } else if (ret_code.equals("3007")) {
            FailureInformationTV.setText("用户信息查询失败");
        } else if (ret_code.equals("4000")) {
            FailureInformationTV.setText("解约失败，请联系发卡行");
        } else if (ret_code.equals("5001")) {
            FailureInformationTV.setText("卡bin校验失败");
        } else if (ret_code.equals("5002")) {
            FailureInformationTV.setText("原始交易不存在");
        } else if (ret_code.equals("5003")) {
            FailureInformationTV.setText("退款金额错误");
        } else if (ret_code.equals("5004")) {
            FailureInformationTV.setText("商户状态异常，无法退款");
        } else if (ret_code.equals("5005")) {
            FailureInformationTV.setText("退款失败，请重试");
        } else if (ret_code.equals("5006")) {
            FailureInformationTV.setText("商户账户余额不足");
        } else if (ret_code.equals("5007")) {
            FailureInformationTV.setText("累计退款金额大于原交易金额");
        } else if (ret_code.equals("5008")) {
            FailureInformationTV.setText("原交易未成功");
        } else if (ret_code.equals("5501")) {
            FailureInformationTV.setText("大额行号查询失败");
        } else if (ret_code.equals("5502")) {
            FailureInformationTV.setText("信用卡不支持提现");
        } else if (ret_code.equals("6001")) {
            FailureInformationTV.setText("卡余额不足");
        } else if (ret_code.equals("6002")) {
            FailureInformationTV.setText("该卡号未成功进行首次验证");
        } else if (ret_code.equals("8000")) {
            FailureInformationTV.setText("用户信息不存在");
        } else if (ret_code.equals("8001")) {
            FailureInformationTV.setText("用户状态异常");
        } else if (ret_code.equals("8888")) {
            FailureInformationTV.setText("交易申请成功,需要再次进行验证");
        } else if (ret_code.equals("8901")) {
            FailureInformationTV.setText("没有记录");
        } else if (ret_code.equals("8911")) {
            FailureInformationTV.setText("没有风控记录");
        } else if (ret_code.equals("9901")) {
            FailureInformationTV.setText("请求报文非法");
        } else if (ret_code.equals("9902")) {
            FailureInformationTV.setText("请求参数缺失{0}");
        } else if (ret_code.equals("9903")) {
            FailureInformationTV.setText("请求参数错误{0}");
        } else if (ret_code.equals("9904")) {
            FailureInformationTV.setText("支付参数和原创建支付单参数不一致");
        } else if (ret_code.equals("9091")) {
            FailureInformationTV.setText("创建支付失败");
        } else if (ret_code.equals("9092")) {
            FailureInformationTV.setText("业务信息非法");
        } else if (ret_code.equals("9093")) {
            FailureInformationTV.setText("无对应的支付单信息");
        } else if (ret_code.equals("9094")) {
            FailureInformationTV.setText("请求银行扣款失败");
        } else if (ret_code.equals("9700")) {
            FailureInformationTV.setText("短信验证码错误");
        } else if (ret_code.equals("9701")) {
            FailureInformationTV.setText("短信验证码和手机不匹配");
        } else if (ret_code.equals("9702")) {
            FailureInformationTV.setText("验证码错误次数超过最大次数,请重新获取进行验证");
        } else if (ret_code.equals("9703")) {
            FailureInformationTV.setText("短信验证码失效,请重新获取");
        } else if (ret_code.equals("9704")) {
            FailureInformationTV.setText("短信发送异常,请稍后重试");
        } else if (ret_code.equals("9902")) {
            FailureInformationTV.setText("接口调用异常");
        } else if (ret_code.equals("9910")) {
            FailureInformationTV.setText("风险等级过高");
        } else if (ret_code.equals("9911")) {
            FailureInformationTV.setText("金额超过指定额度");
        } else if (ret_code.equals("9912")) {
            FailureInformationTV.setText("该卡不支持");
        } else if (ret_code.equals("9913")) {
            FailureInformationTV.setText("该卡已签约成功");
        } else if (ret_code.equals("9970")) {
            FailureInformationTV.setText("银行系统日切处理中");
        } else if (ret_code.equals("9999")) {
            FailureInformationTV.setText("系统错误");
        } else if (ret_code.equals("9990")) {
            FailureInformationTV.setText("银行交易出错，请稍后重试");
        } else if (ret_code.equals("9907")) {
            FailureInformationTV.setText("银行编码与卡不一致");
        } else if (ret_code.equals("9000")) {
            FailureInformationTV.setText("银行维护中，请稍后再试");
        }
    }

    /*
     * 账户明细
     */
    private void showAccountDetailsBtn() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currMenu", 2);
        startActivity(intent);
    }

    /*
     * 重试
     */
    private void showImmediateInvestmentBtn() {
        Intent intent = new Intent(this, RechargeAgainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*
     * 客服电话
     */
    private void showcontactTV() {
        String tel = AccountPhoneTV.getText().toString().replace("-", "");
        // 用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        startActivity(intent);
    }

    @Override
    protected void initTitleBar() {
        getIntentCode = getIntent().getIntExtra("recharge", 0);

        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        if (getIntentCode != 0) {
            windowView.setTitle("绑卡失败");
        } else {
            windowView.setTitle("充值失败");
        }
        windowView.setLeftButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinishAct();
            }
        });
    }

    @Override
    public void onBackPressed() {
        FinishAct();
    }

    private void FinishAct() {
        if (getIntentCode != 0) {
            Intent intent = new Intent(RechargeFailureActivity.this, MyBankCardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            finish();
        }
    }
}
