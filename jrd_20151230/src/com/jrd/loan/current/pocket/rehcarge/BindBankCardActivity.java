package com.jrd.loan.current.pocket.rehcarge;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.adapter.BankCardItemAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.api.UserApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.BankListBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.bean.PersonalInfoBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.constant.Const.Extra;
import com.jrd.loan.gesture.lock.GestureEditActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.IDCardVerify;
import com.jrd.loan.util.KeyBoardUtil;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class BindBankCardActivity extends BaseActivity implements OnClickListener {

    private Context mContext;
    private WindowView windowView;
    private EditText nameEdit;
    private EditText cardNumEdit;
    private EditText bankCardNumEdit;
    private ImageView nameHintImg;
    private ImageView bankCardHintImg;
    private ImageView checkAgreementImg;
    private boolean isRealName; // 是否实名认证
    private PopupWindow paymentPopWin;
    private LinearLayout selectLayout;
    private LinearLayout bankLayout;
    private TextView bankNameTV;
    private TextView selectTV;
    private ImageView bankIcon;
    private List<BankListBean.RecordsEntity> mList;
    private boolean isBindCard = false;//是否绑卡

    private String bankCode;
    private int getIntentCode;
    private TextView jumpTV;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_bindbankcard;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.INVISIBLE);
        windowView.setTitle("绑定银行卡");
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        mContext = BindBankCardActivity.this;
        getIntentCode = getIntent().getIntExtra("intent", 0);

        selectLayout = (LinearLayout) findViewById(R.id.loan_bindcard_selectLayout);
        selectLayout.setOnClickListener(this);
        selectTV = (TextView) findViewById(R.id.loan_bindcard_selectTV);
        selectTV.setVisibility(View.VISIBLE);
        bankLayout = (LinearLayout) findViewById(R.id.loan_bindcard_bankLayout);
        bankLayout.setVisibility(View.GONE);
        bankNameTV = (TextView) findViewById(R.id.loan_bindcard_bankName);
        bankIcon = (ImageView) findViewById(R.id.loan_bindcard_bankImage);

        nameEdit = (EditText) findViewById(R.id.loan_bindcard_personNameEdit);
        cardNumEdit = (EditText) findViewById(R.id.loan_bindcard_personCardNumEdit);
        bankCardNumEdit = (EditText) findViewById(R.id.loan_bindcard_bandcardNumEdit);
        FormatUtils.CardEdit(bankCardNumEdit);
        nameHintImg = (ImageView) findViewById(R.id.loan_bindcard_personNameImg);
        nameHintImg.setOnClickListener(this);
        bankCardHintImg = (ImageView) findViewById(R.id.loan_bindcard_bankcardImg);
        bankCardHintImg.setOnClickListener(this);
        TextView agreementTV = (TextView) findViewById(R.id.loan_bindcard_agreementTV);
        agreementTV.setOnClickListener(this);
        String text = agreementTV.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new RelativeSizeSpan(1.2f), 2, text.length(), 0);
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.loan_current_text_color_blue)), 2, text.length(), 0);
        agreementTV.setText(builder);
        FrameLayout agreeMentLayout = (FrameLayout) findViewById(R.id.loan_bindcard_agreementLayout);
        agreeMentLayout.setOnClickListener(this);
        checkAgreementImg = (ImageView) findViewById(R.id.loan_bindcard_agreementImg);
        checkAgreementImg.setSelected(true);
        Button bindBtn = (Button) findViewById(R.id.loan_bindcard_enterBtn);
        bindBtn.setOnClickListener(this);

        jumpTV = (TextView) findViewById(R.id.loan_bindcard_jumpTV);
        jumpTV.setOnClickListener(this);
        String jumpText = jumpTV.getText().toString();
        SpannableStringBuilder builder1 = new SpannableStringBuilder(jumpText);
        builder1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.loan_color_btn_normal)), 4, jumpText.length(), 0);
        jumpTV.setText(builder1);
        // 如果是注册界面跳过来的
        if (getIntentCode == Const.IntentCode.REGIST_TO_BINDCARD_INTENT_CODE) {
            jumpTV.setVisibility(View.VISIBLE);
            bindBtn.setText("实名认证");
        } else {
            jumpTV.setVisibility(View.GONE);
        }

        RequestData();

        setNoNetworkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestData();
            }
        });
    }

    private void SetVisible() {
        // 如果已实名认证
        if (isRealName) {
            nameHintImg.setVisibility(View.INVISIBLE);
            bankCardHintImg.setVisibility(View.INVISIBLE);
            // 输入框不可编辑
            nameEdit.setFocusable(false);
            nameEdit.setFocusableInTouchMode(false);
            cardNumEdit.setFocusable(false);
            cardNumEdit.setFocusableInTouchMode(false);
        } else {
            nameHintImg.setVisibility(View.VISIBLE);
            bankCardHintImg.setVisibility(View.VISIBLE);
            cardNumEdit.setFocusable(true);
            cardNumEdit.setFocusableInTouchMode(true);
            cardNumEdit.requestFocus();
            // 输入框可编辑
            nameEdit.setFocusable(true);
            nameEdit.setFocusableInTouchMode(true);
            nameEdit.requestFocus();
        }
    }

    // 请求数据
    private void RequestData() {
        UserApi.GetUserInfo(mContext, UserInfoPrefs.getUserId(), new OnHttpTaskListener<PersonalInfoBean>() {

            @Override
            public void onStart() {
                ShowDrawDialog(mContext);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(PersonalInfoBean bean) {
                DismissDialog();
                if (bean != null && bean.getResultCode() == 0) {
                    if (bean.getPersonalInfo() != null) {
                        windowView.setVisibility(View.VISIBLE);
                        if (bean.getPersonalInfo().getIdNumberFlag() != null && bean.getPersonalInfo().getIdNumberFlag().equals("1")) {
                            isRealName = true;
                            nameEdit.setText(bean.getPersonalInfo().getUserName());
                            cardNumEdit.setText(bean.getPersonalInfo().getIdNumberInfo());
                        } else {
                            isRealName = false;
                        }
                        SetVisible();
                    }
                }
            }
        });
    }

    /**
     * 实名认证并绑卡
     */
    private void BindCardAndRealName() {
        PocketApi.BindCard(mContext, nameEdit.getText().toString(), cardNumEdit.getText().toString(), bankCode, bankCardNumEdit.getText().toString().replace(" ", ""), new OnHttpTaskListener<BaseBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowProDialog(mContext);
            }

            @Override
            public void onFinishTask(BaseBean bean) {
                DismissDialog();
                if (bean != null) {
                    if (bean.getResultCode() == 0) {
                        PersonalInfo personal = new PersonalInfo();
                        personal.setIdNumberFlag("1");
                        personal.setBoundCardFlag("1");
                        UserInfoPrefs.saveUserProfileInfo(personal);
                        // 从我的银行卡界面过来的
                        if (getIntentCode == Const.IntentCode.MYCARD_TO_ADDCRAD_ITNENT_CODE) {
                            setResult(RESULT_OK);
                            ToastUtil.showShort(mContext, R.string.loan_bindcard_successful);
                        } else if (getIntentCode == Const.IntentCode.REGIST_TO_BINDCARD_INTENT_CODE) {
                            Intent mIntent = new Intent(BindBankCardActivity.this, GestureEditActivity.class);
                            mIntent.putExtra(Extra.Select_ID, 1);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mIntent);
                        }
                        finish();
                    } else {
                        ToastUtil.showShort(mContext, bean.getResultMsg());
                    }
                }
            }
        });
    }

    /**
     * 绑卡
     */
    private void BindCard() {
        PocketApi.BindCard(mContext, bankCode, bankCardNumEdit.getText().toString().replace(" ", ""), new OnHttpTaskListener<BaseBean>() {

            @Override
            public void onStart() {
                ShowProDialog(mContext);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(BaseBean bean) {
                DismissDialog();
                if (bean != null) {
                    if (bean.getResultCode() == 0) {
                        PersonalInfo personal = new PersonalInfo();
                        personal.setIdNumberFlag("1");
                        personal.setBoundCardFlag("1");
                        UserInfoPrefs.saveUserProfileInfo(personal);
                        // 从我的银行卡界面过来的
                        if (getIntentCode == Const.IntentCode.MYCARD_TO_ADDCRAD_ITNENT_CODE) {
                            setResult(RESULT_OK);
                        }
                        ToastUtil.showShort(mContext, R.string.loan_bindcard_successful);
                        finish();
                    } else {
                        ToastUtil.showShort(mContext, bean.getResultMsg());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_bindcard_personNameImg:
                DialogUtils.showWarningDialog(mContext, R.string.loan_bindcard_name_waning, null);
                break;
            case R.id.loan_bindcard_bankcardImg:
                DialogUtils.showWarningDialog(mContext, R.string.loan_bindcard_bankcard_waning, null);
                break;
            case R.id.loan_bindcard_agreementLayout:
                if (checkAgreementImg.isSelected()) {
                    checkAgreementImg.setSelected(false);
                } else {
                    checkAgreementImg.setSelected(true);
                }
                break;
            case R.id.loan_bindcard_enterBtn:
                KeyBoardUtil.closeKeybord(jumpTV, BindBankCardActivity.this);
                CheckEmpty();
                break;
            case R.id.loan_bindcard_selectLayout:
                KeyBoardUtil.closeKeybord(selectTV, mContext);
                getBankListData();
                break;
            case R.id.loan_bindcard_agreementTV:// 银行卡绑定协议
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("htmlUrl", JrdConfig.getBaseUrl() + PocketApi.BINDCARD_AGREE);
                intent.putExtra("htmlTitle", windowView.getTitle());
                startActivity(intent);
                break;
            case R.id.loan_bindcard_jumpTV:
                getUserInfo();
                break;
        }
    }

    private void getBankListData() {

        BankApi.getBankList(mContext, new OnHttpTaskListener<BankListBean>() {

            @Override
            public void onStart() {
                ShowProDialog(mContext);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(BankListBean bean) {
                DismissDialog();
                if (bean != null && bean.getResultCode() == 0) {
                    if (bean.getRecords() != null && bean.getRecords().size() > 0) {

                        mList = bean.getRecords();

                        initPopupWindow();

                    }
                }

            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 透明的 0-1
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 检查输入是否为空
     */
    private void CheckEmpty() {
        if (nameEdit.getText().toString().isEmpty()) {
            ToastUtil.showShort(mContext, R.string.loan_warning_empty_realname);
            return;
        } else if (cardNumEdit.getText().toString().isEmpty()) {
            ToastUtil.showShort(mContext, R.string.loan_warning_empty_idnum);
            return;
        } else if (!isRealName && !IDCardVerify.IDCardValidate(cardNumEdit.getText().toString())) {
            ToastUtil.showShort(mContext, R.string.loan_warning_wrong_idnumt);
            return;
        } else if(!isBindCard){
            ToastUtil.showShort(mContext, R.string.loan_warning_empty_select_bankcard);
            return;
        }else if (bankCardNumEdit.getText().toString().isEmpty()) {
            ToastUtil.showShort(mContext, R.string.loan_warning_input_card_number_text);
            return;
        } else if (!checkAgreementImg.isSelected()) {
            ToastUtil.showShort(mContext, R.string.loan_select_agreement);
            return;
        }
        if (isRealName) {// 已经实名认证
            BindCard();
        } else {// 没有实名认证
            BindCardAndRealName();
        }
    }

    /**
     * 初始化选择银行卡页面
     */
    private void initPopupWindow() {
        View PopupView = this.getLayoutInflater().inflate(R.layout.loan_payment_dialog, null);

        TextView title = (TextView) PopupView.findViewById(R.id.loan_payment_title);
        title.setText("请选择银行卡（仅限储蓄卡）");

        ListView mListView = (ListView) PopupView.findViewById(R.id.loan_payment_ListView);

        paymentPopWin = new PopupWindow(PopupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);

        // 设置SelectPicPopupWindow弹出窗体的背景
        paymentPopWin.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体的宽
        paymentPopWin.setWidth(LayoutParams.MATCH_PARENT);

        // 设置SelectPicPopupWindow弹出窗体的高
        paymentPopWin.setHeight(LayoutParams.WRAP_CONTENT);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        paymentPopWin.setAnimationStyle(R.style.AnimBottom);

        // 设置点击窗口外边窗口消失
        paymentPopWin.setOutsideTouchable(true);

        // 设置此参数活的焦点，否在无法点击
        paymentPopWin.setFocusable(true);

        // mListView.setOnItemClickListener(mListViewItem);

        paymentPopWin.setOnDismissListener(new OnDismissListener() {

            // 在dismiss中恢复透明度
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        BankCardItemAdapter adapter = new BankCardItemAdapter(mList, mContext);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectTV.setVisibility(View.GONE);
                bankLayout.setVisibility(View.VISIBLE);

                bankCode = mList.get(position).getBankCode();
                bankNameTV.setText(mList.get(position).getBankName());

                backgroundAlpha(1f);
                isBindCard = true;
                paymentPopWin.dismiss();

            }
        });

        if (!paymentPopWin.isShowing()) {
            paymentPopWin.showAsDropDown(selectLayout);
        }
        backgroundAlpha(1.4f);
    }

    /**
     * 获取用户信息接口
     *
     */
    private void getUserInfo() {
        UserApi.GetUserInfo(BindBankCardActivity.this, UserInfoPrefs.getUserId(), new OnHttpTaskListener<PersonalInfoBean>() {

            @Override
            public void onStart() {
                ShowProDialog(BindBankCardActivity.this);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
                if (resposeCode == 0) {
                    ToastUtil.showShort(BindBankCardActivity.this, "服务器无响应,连接超时");
                }
            }

            @Override
            public void onFinishTask(PersonalInfoBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    UserInfoPrefs.saveUserProfileInfo(bean.getPersonalInfo());
                    Intent mIntent = new Intent(BindBankCardActivity.this, GestureEditActivity.class);
                    mIntent.putExtra(Extra.Select_ID, 1);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();
                    LogUtil.d("HttpRequest", "idNumberInfo-->" + UserInfoPrefs.loadLastLoginUserProfile().idNumberInfo);
                } else {
                    ToastUtil.showShort(BindBankCardActivity.this, bean.getResultMsg());
                }
            }
        });
    }
}
