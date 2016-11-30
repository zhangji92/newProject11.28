package com.jrd.loan.util;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jrd.loan.MainActivity.FileDownloadStateListener;
import com.jrd.loan.R;
import com.jrd.loan.adapter.CityAdapter;
import com.jrd.loan.bean.CityBean;
import com.jrd.loan.myaccount.AccountRollOutActivity;
import com.jrd.loan.net.download.FileDownload;

public class DialogUtils {

  /**
   * 显示确认对话框(只有一个button, button显示的文字可以自由设置)
   *
   * @param context      上下文索引
   * @param msgId        dialog显示的消息内容的字符串ID
   * @param isCancel     是否可以消失dialog
   * @param buttonTextId button显示的文字的字符串ID buttonEventListener 用户点击button时的监听器,
   *                     此参数可以传null
   */
  public final static void showConfirmDialog(Context context, String msg, int buttonTextId, boolean isCancel, final OnButtonEventListener buttonEventListener) {
    showConfirmDialog(context, msg, context.getString(buttonTextId), isCancel, buttonEventListener);
  }
    /**
     * 显示确认对话框(只有一个button, button显示的文字可以自由设置)
     *
     * @param context      上下文索引
     * @param msgId        dialog显示的消息内容的字符串ID
     * @param isCancel     是否可以消失dialog
     * @param buttonTextId button显示的文字的字符串ID buttonEventListener 用户点击button时的监听器,
     *                     此参数可以传null
     */
    public final static void showConfirmDialog(Context context, int msgId, int buttonTextId, boolean isCancel, final OnButtonEventListener buttonEventListener) {
        showConfirmDialog(context, context.getString(msgId), context.getString(buttonTextId), isCancel, buttonEventListener);
    }

    /**
     * 只显示title提示的dialog
     *
     * @param mContext
     * @param leftBtnText
     * @param rightBtnText
     * @param title
     * @param buttonEventListener
     */
    public static void showTextDialog(Context mContext, int leftBtnText, int rightBtnText, int title, final OnButtonEventListener buttonEventListener) {
        ShowDelCardDialog(mContext, leftBtnText, rightBtnText, title, null, 0, buttonEventListener);
    }

    /**
     * 只显示title提示的dialog
     *
     * @param mContext
     * @param leftBtnText
     * @param rightBtnText
     * @param title
     * @param buttonEventListener
     */
    public static void showTextDialog(Context mContext, String leftBtnText, String rightBtnText, String title, final OnButtonEventListener buttonEventListener) {
        ShowDelCardDialog(mContext, leftBtnText, rightBtnText, title, null, 0, buttonEventListener);
    }

    /**
     * 删除银行卡时的dialog
     *
     * @param mContext            上下文
     * @param leftBtnText         左边btn的文字
     * @param rightBtnText        右边Btn的文字
     * @param title               标题头
     * @param cardNum             银行卡号
     * @param buttonEventListener 监听事件
     */
    public static void ShowDelCardDialog(Context mContext, int leftBtnText, int rightBtnText, int title, String cardNum, int resId, final OnButtonEventListener buttonEventListener) {
        ShowDelCardDialog(mContext, mContext.getString(leftBtnText), mContext.getString(rightBtnText), mContext.getString(title), cardNum, resId, buttonEventListener);
    }

    /**
     * 显示对话框(有两个button, button显示的文字可以自由设置)
     *
     * @param context          上下文索引
     * @param msgId            dialog显示的消息内容的字符串ID
     * @param btnConfirmTextId button显示的文字的字符串ID
     * @param btnCancelTextId  button显示的文字的字符串ID buttonEventListener 用户点击button时的监听器,
     *                         此参数可以传null
     */
    public final static void showDialog(Context context, String msg, int btnConfirmTextId, int btnCancelTextId, final OnButtonEventListener buttonEventListener) {
      showDialog(context, msg, context.getString(btnConfirmTextId), context.getString(btnCancelTextId), true, buttonEventListener);
    }
    /**
     * 显示对话框(有两个button, button显示的文字可以自由设置)
     *
     * @param context          上下文索引
     * @param msgId            dialog显示的消息内容的字符串ID
     * @param btnConfirmTextId button显示的文字的字符串ID
     * @param btnCancelTextId  button显示的文字的字符串ID buttonEventListener 用户点击button时的监听器,
     *                         此参数可以传null
     */
    public final static void showDialog(Context context, int msgId, int btnConfirmTextId, int btnCancelTextId, final OnButtonEventListener buttonEventListener) {
        showDialog(context, context.getString(msgId), context.getString(btnConfirmTextId), context.getString(btnCancelTextId), true, buttonEventListener);
    }

    /**
     * 显示对话框(有两个button, button显示的文字可以自由设置) button按钮位白色，文字为黑色
     *
     * @param context 上下文索引
     */
    public final static void showDialogs(Context context, final OnButtonEventListener buttonEventListener) {
        showDialog(context, true, buttonEventListener);
    }

    /**
     * 显示对话框(有两个button, button显示的文字可以自由设置) button按钮位白色，文字为黑色
     *
     * @param context 上下文索引
     */
    public final static void showDialog(Context context, String cardName, String cardNum, final OnButtonEventListener buttonEventListener) {
        showDialog(context, cardName, cardNum, true, buttonEventListener);
    }

    /**
     * 显示确认对话框(只有一个button, button显示的文字可以自由设置)
     *
     * @param context 上下文索引
     * @param msg     dialog显示的消息内容
     * @param btnText button显示的文字 buttonEventListener 用户点击button时的监听器, 此参数可以传null
     */
    public final static void showConfirmDialog(Context context, String msg, String btnText, boolean isCancel, final OnButtonEventListener buttonEventListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(isCancel);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_confirm_dialog_layout, null);
        TextView msgText = (TextView) layout.findViewById(R.id.msg);
        msgText.setText(msg);
        Button btCommit = (Button) layout.findViewById(R.id.btnConfirm);
        btCommit.setText(btnText);
        btCommit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onConfirm();
                }
            }
        });
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_60dip)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 给用户提示警告的dialog
     *
     * @param context
     * @param msg
     * @param buttonEventListener
     */
    public final static void showWarningDialog(Context context, int msg, final OnButtonEventListener buttonEventListener) {
        showWarningDialog(context, context.getString(msg), buttonEventListener);
    }

    /**
     * 给用户提示警告的dialog
     *
     * @param context
     * @param msg
     * @param buttonEventListener
     */
    public final static void showWarningDialog(Context context, String msg, final OnButtonEventListener buttonEventListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_warning_dialog_layout, null);
        TextView msgText = (TextView) layout.findViewById(R.id.msg);
        msgText.setText(msg);
        Button btCommit = (Button) layout.findViewById(R.id.btnConfirm);
        btCommit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onConfirm();
                }
            }
        });
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_30dip)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 显示确认对话框(只有一个button, button显示的文字可以自由设置)
     *
     * @param context 上下文索引
     * @param msg     dialog显示的消息内容
     * @param btnText button显示的文字 buttonEventListener 用户点击button时的监听器, 此参数可以传null
     */
    public final static void showCouponsDialog(Context context, String msg, String btnText, boolean isCancel, final OnButtonEventListener buttonEventListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(isCancel);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_coupons_dialog_layout, null);
        TextView msgText = (TextView) layout.findViewById(R.id.msg);
        msgText.setText(msg);
        Button btCommit = (Button) layout.findViewById(R.id.btnConfirm);
        btCommit.setText(btnText);
        btCommit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onConfirm();
                }
            }
        });
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_60dip)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 显示对话框(有两个button, button显示的文字可以自由设置)
     *
     * @param mContext            上下文索引
     * @param version             最新版本号
     * @param versionSize         新版本大小
     * @param updContent          更新的内容
     * @param buttonEventListener 用户点击button时的监听器, 此参数可以传null
     */
    public final static void ShowUpdateDialog(Context mContext, String version, String versionSize, String updContent, final OnButtonEventListener buttonEventListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_dialog_version_update, null);
        TextView versionText = (TextView) layout.findViewById(R.id.loan_latest_version);
        versionText.setText(version);
        TextView versionSizeText = (TextView) layout.findViewById(R.id.loan_new_version_size);
        versionSizeText.setText(versionSize);
        TextView updateContentText = (TextView) layout.findViewById(R.id.loan_update_content);
        updateContentText.setText(updContent);
        Button leftBtn = (Button) layout.findViewById(R.id.loan_dialog_leftBtn);
        leftBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onConfirm();
                }
            }
        });
        Button rightBtn = (Button) layout.findViewById(R.id.loan_dialog_rightBtn);
        rightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onCancel();
                }
            }
        });
        int screenWidth = ScreenUtil.getScreenWidth(mContext);
        dialog.getWindow().setLayout((int) (screenWidth - mContext.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 显示对话框(有两个button, button显示的文字可以自由设置)
     *
     * @param context        上下文索引
     * @param msg            dialog显示的消息内容的字符串ID
     * @param btnConfirmText button显示的文字
     * @param canceled       按返回键是否关闭对话框 true 关闭 false 不关闭
     * @param btnCancelText  button显示的文字 buttonEventListener 用户点击button时的监听器, 此参数可以传null
     */
    public final static void showDialog(Context context, String msg, String btnConfirmText, String btnCancelText, boolean canceled, final OnButtonEventListener buttonEventListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(canceled);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_dialog_layout, null);
        TextView msgText = (TextView) layout.findViewById(R.id.msg);
        msgText.setText(msg);
        Button btCommit = (Button) layout.findViewById(R.id.btnConfirm);
        btCommit.setText(btnConfirmText);
        btCommit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onConfirm();
                }
            }
        });
        Button btCancel = (Button) layout.findViewById(R.id.btnCancel);
        btCancel.setText(btnCancelText);
        btCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onCancel();
                }
            }
        });
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_margin)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 显示对话框(有两个button, button显示的文字可以自由设置) button按钮位白色，文字为黑色
     *
     * @param context  上下文索引
     * @param canceled 按返回键是否关闭对话框 true 关闭 false 不关闭
     */
    public final static void showDialog(Context context, boolean canceled, final OnButtonEventListener buttonEventListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(canceled);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_dialogs_layout, null);

        Button btCommit = (Button) layout.findViewById(R.id.btnConfirm);
        btCommit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onConfirm();
                }
            }
        });

        Button btCancel = (Button) layout.findViewById(R.id.btnCancel);
        btCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onCancel();
                }
            }
        });

        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_margin)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 绑卡信息确认的dialog
     * <p/>
     * 显示对话框(有两个button, button显示的文字可以自由设置) button按钮位白色，文字为黑色
     *
     * @param context  上下文索引
     * @param canceled 按返回键是否关闭对话框 true 关闭 false 不关闭
     */
    public final static void showDialog(Context context, String cardName, String cardNum, boolean canceled, final OnButtonEventListener buttonEventListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(canceled);
        dialog.show();

        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_recharge_dialog_layout, null);

        // 银行卡
        TextView cardNameTV = (TextView) layout.findViewById(R.id.loan_cardName);
        cardNameTV.setText(new StringBuffer().append("银行：").append(cardName).toString());
        // 银行卡号
        TextView cardNumTV = (TextView) layout.findViewById(R.id.loan_cardNum);
        cardNumTV.setText(new StringBuffer().append("卡号：").append(cardNum).toString());
        // 绑卡信息提示
        TextView cardInformationTV = (TextView) layout.findViewById(R.id.loan_CardInformation);
        cardInformationTV.setText(AccountRollOutActivity.ToDBC(context.getResources().getString(R.string.loan_TieCardInformationPrompt)));

        Button btCommit = (Button) layout.findViewById(R.id.btnConfirm);
        btCommit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onConfirm();
                }
            }
        });

        Button btCancel = (Button) layout.findViewById(R.id.btnCancel);
        btCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onCancel();
                }
            }
        });

        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_margin)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 删除银行卡时的dialog
     *
     * @param mContext            上下文
     * @param leftBtnText         左边btn的文字
     * @param rightBtnText        右边Btn的文字
     * @param title               标题头
     * @param cardNum             银行卡号
     * @param buttonEventListener 监听事件
     */
    public final static void ShowDelCardDialog(Context mContext, String leftBtnText, String rightBtnText, String title, String cardNum, int resId, final OnButtonEventListener buttonEventListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_custom_dialog_layout, null);
        TextView msgText = (TextView) layout.findViewById(R.id.loan_dialog_title_tv);
        msgText.setText(title);
        TextView numText = (TextView) layout.findViewById(R.id.loan_dialog_card_numtv);
        numText.setText(cardNum);
        ImageView icon = (ImageView) layout.findViewById(R.id.loan_dialog_card_image);
        icon.setImageResource(resId);
        if (cardNum == null) {
            layout.findViewById(R.id.loan_dialog_card_layout).setVisibility(View.GONE);
        }
        Button leftBtn = (Button) layout.findViewById(R.id.loan_dialog_leftBtn);
        leftBtn.setText(leftBtnText);
        leftBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onConfirm();
                }
            }
        });
        Button rightBtn = (Button) layout.findViewById(R.id.loan_dialog_rightBtn);
        rightBtn.setText(rightBtnText);
        rightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                if (buttonEventListener != null) {
                    buttonEventListener.onCancel();
                }
            }
        });
        int screenWidth = ScreenUtil.getScreenWidth(mContext);
        dialog.getWindow().setLayout((int) (screenWidth - mContext.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 绑定手机号dialog
     *
     * @param mContext
     * @param buttonListener
     * @return
     */
    public static Dialog ShowVerifyDialog(final Context mContext, final OnButtonVerfifyListener buttonListener) {
        final Dialog dialog = new Dialog(mContext, R.style.loan_custom_dialog_style);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_dialog_bind_phone_layout, null);
        final EditText phoneEdit = (EditText) layout.findViewById(R.id.loan_bind_phone_edit);
        final EditText verifyEdit = (EditText) layout.findViewById(R.id.loan_bind_verify_edit);
        Button enterBtn = (Button) layout.findViewById(R.id.loan_bind_enterBtn);
        enterBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 手机号码判空
                if (TextUtils.isEmpty(phoneEdit.getText().toString())) {
                    ToastUtil.showShort(mContext, R.string.loan_warning_empty_telnum);
                    return;
                }
                // 校验手机号码格式
                if (!FormatUtils.isPhoneNumber(phoneEdit.getText().toString())) {
                    ToastUtil.showShort(mContext, R.string.loan_warning_wrong_numformat);
                    return;
                }
                // 验证码判空
                if (TextUtils.isEmpty(verifyEdit.getText().toString())) {
                    ToastUtil.showShort(mContext, R.string.loan_warning_empty_vcode);
                    return;
                }
                // 验证码长度
                if (verifyEdit.getText().toString().length() < 6) {
                    ToastUtil.showShort(mContext, R.string.loan_warning_short_vcode);
                    return;
                }
                if (buttonListener != null) {
                    // dialog.dismiss();
                    buttonListener.onConfirm(phoneEdit.getText().toString(), verifyEdit.getText().toString());
                }
            }
        });
        Button cancelBtn = (Button) layout.findViewById(R.id.loan_bind_cancelBtn);
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                if (buttonListener != null) {
                    buttonListener.onCancel();
                }
            }
        });
        final Button sendBtn = (Button) layout.findViewById(R.id.loan_bind_getVerify_btn);
        sendBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 手机号码判空
                if (TextUtils.isEmpty(phoneEdit.getText().toString())) {
                    ToastUtil.showShort(mContext, R.string.loan_warning_empty_telnum);
                    return;
                }
                // 校验手机号码格式
                if (!FormatUtils.isPhoneNumber(phoneEdit.getText().toString())) {
                    ToastUtil.showShort(mContext, R.string.loan_warning_wrong_numformat);
                    sendBtn.setEnabled(false);
                    return;
                }
                if (buttonListener != null) {
                    buttonListener.onSendVerify(phoneEdit.getText().toString(), sendBtn);
                }
            }
        });
        int screenWidth = ScreenUtil.getScreenWidth(mContext);
        dialog.getWindow().setLayout((int) (screenWidth - mContext.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }

    /**
     * 显示progressDialog
     *
     * @param context
     */
    public static Dialog ShowProDialog(Context context) {
        return ShowProDialog(context, null);
    }

    /**
     * 可取消的proDialog
     *
     * @param context
     * @param buttonCancelListener
     */
    public static Dialog ShowProDialog(Context context, final OnButtonCancelListener buttonCancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.loan_custom_prodialog_style);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        if (buttonCancelListener != null) {
            dialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    if (buttonCancelListener != null) {
                        dialog.dismiss();
                        buttonCancelListener.onCancel();
                    }
                }
            });
        } else {
            dialog.setCancelable(false);
        }
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_progress_dialog_layout, null);
        ImageView loadingDialog = (ImageView) layout.findViewById(R.id.loadingDialog);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingDialog.getBackground();
        animationDrawable.start();
        dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.y = -60;
        lp.x = 35;
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    /**
     * 显示默认的请求进度条
     *
     * @param context
     * @param buttonCancelListener
     */
    public static Dialog ShowDefaultProDialog(Context context, final OnButtonCancelListener buttonCancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        if (buttonCancelListener != null) {
            dialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    if (buttonCancelListener != null) {
                        dialog.dismiss();
                        buttonCancelListener.onCancel();
                    }
                }
            });
        } else {
            dialog.setCancelable(false);
        }
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_pro_dialog_layout, null);
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout(screenWidth * 1 / 3, LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }

    /**
     * 更新进度
     */
    public static Dialog ShowUpdateProDialog(final Context context, String downloadUrl, final FileDownloadStateListener downloadStateListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_dialog_progressbar, null);
        // 进度
        ProgressBar Dialog = (ProgressBar) layout.findViewById(R.id.loan_progressBar);
        // 进度百分比
        TextView UpdSchedule = (TextView) layout.findViewById(R.id.loan_update_schedule);
        // download file
        downloadStateListener.setDownloadPro(UpdSchedule, Dialog);
        new FileDownload(downloadUrl, downloadStateListener).startDownloadFile();
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }

    /**
     * 显示城市的dialog
     *
     * @param context
     * @param cityList
     */
    public static Dialog ShowCityDialog(Context context, List<CityBean> cityList, OnItemClickListener itemListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.show();
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_dialog_city_layout, null);
        ListView listView = (ListView) layout.findViewById(R.id.loan_city_listview);
        listView.setAdapter(new CityAdapter(cityList, context));
        listView.setOnItemClickListener(itemListener);
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }

    /**
     * 输入交易密码的dialog
     *
     * @param context
     * @param listener
     */
    public static void showTradePwdDialog(final Context context, final OnPaymentAccountListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loan_custom_dialog_style);
        dialog.setCanceledOnTouchOutside(false);
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_dialog_tradepwd_layout, null);

        final EditText edit = (EditText) layout.findViewById(R.id.loan_trade_edit);

        layout.findViewById(R.id.loan_trade_enterBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm(edit.getText().toString(), dialog);
                }
            }
        });

        layout.findViewById(R.id.loan_trade_cancelBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                KeyBoardUtil.closeKeybord(edit, context);
                dialog.dismiss();
            }
        });
        KeyBoardUtil.openKeybord(edit, context);
        dialog.show();

        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);

    }

    /**
     * 确认实名信息dialog
     *
     * @param context
     * @param listener
     */
    public static void showIdCardInfoDialog(final Context context, String name, String idnum, final OnButtonEventListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loan_custom_dialog_style);
        dialog.setCanceledOnTouchOutside(false);
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_confirm_idcard_info_dialog_layout, null);

        TextView nameTV = (TextView) layout.findViewById(R.id.loan_IDCardInfo_nameTV);
        nameTV.setText("姓    名：" + name);
        TextView idnumTV = (TextView) layout.findViewById(R.id.loan_IDCardInfo_idnumTV);
        idnumTV.setText("证件号：" + idnum);

        layout.findViewById(R.id.loan_IDCardInfo_enterBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onConfirm();
                }
            }
        });

        layout.findViewById(R.id.loan_IDCardInfo_cancelBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        KeyBoardUtil.closeKeybord(nameTV, context);
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);

    }

    /**
     * 确认绑卡信息dialog
     *
     * @param context
     * @param listener
     */
    public static void showBankCardInfoDialog(final Context context, String name, String cardnum, final OnButtonEventListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loan_custom_dialog_style);
        dialog.setCanceledOnTouchOutside(false);
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_confirm_bankcard_info_dialog_layout, null);

        TextView nameTV = (TextView) layout.findViewById(R.id.loan_bankCardInfo_nameTV);
        nameTV.setText("银行：" + name);
        TextView idnumTV = (TextView) layout.findViewById(R.id.loan_bankCardInfo_idnumTV);
        idnumTV.setText("卡号：" + cardnum);

        layout.findViewById(R.id.loan_bankCardInfo_enterBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onConfirm();
                }
            }
        });

        layout.findViewById(R.id.loan_bankCardInfo_cancelBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        KeyBoardUtil.closeKeybord(nameTV, context);
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    /**
     * 确认提现信息dialog
     *
     * @param context
     * @param listener
     */
    public static void showWithdrawInfoDialog(final Context context, boolean isShow, String amount, String cost, String reailty, String name, String num, String open, String address, final OnTransPasswdEventListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loan_custom_dialog_style);
        dialog.setCanceledOnTouchOutside(false);
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_confirm_withdraw_info_dialog_layout, null);

        LinearLayout view = (LinearLayout) layout.findViewById(R.id.loan_withdrawInfo_layout);
        if (isShow) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }

        TextView amountTV = (TextView) layout.findViewById(R.id.loan_withdrawInfo_amountTV);
        amountTV.setText("提现金额：" + DoubleUtil.getMoney(amount) + "元");
        TextView costTV = (TextView) layout.findViewById(R.id.loan_withdrawInfo_costTV);
        costTV.setText("提现费用：" + DoubleUtil.getMoney(cost) + "元");
        TextView reailtyTV = (TextView) layout.findViewById(R.id.loan_withdrawInfo_reailtyTV);
        reailtyTV.setText("实际扣除：" + DoubleUtil.getMoney(reailty) + "元");
        TextView nameTV = (TextView) layout.findViewById(R.id.loan_withdrawInfo_nameTV);
        nameTV.setText("所属银行：" + name);
        TextView numberTV = (TextView) layout.findViewById(R.id.loan_withdrawInfo_numberTV);
        String startString = num.substring(0, 4);
        String endString = num.substring(num.length() - 4, num.length());
        numberTV.setText("储蓄卡号：" + new StringBuilder(startString).append(" **** **** **** ").append(endString));
        TextView openTV = (TextView) layout.findViewById(R.id.loan_withdrawInfo_openTV);
        openTV.setText("开户省市：" + open);
        TextView addressTV = (TextView) layout.findViewById(R.id.loan_withdrawInfo_addressTV);
        addressTV.setText("支行/分行：" + address);

        final EditText etTransPasswd = (EditText) layout.findViewById(R.id.etTransPasswd);

        layout.findViewById(R.id.loan_bankCardInfo_enterBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm(etTransPasswd.getText().toString(), dialog);
                }
            }
        });

        layout.findViewById(R.id.loan_bankCardInfo_cancelBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        KeyBoardUtil.closeKeybord(nameTV, context);
        int screenWidth = ScreenUtil.getScreenWidth(context);
        dialog.getWindow().setLayout((int) (screenWidth - context.getResources().getDimension(R.dimen.loan_dialog_height)), LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(layout);
    }

    public interface OnTransPasswdEventListener {

        void onConfirm(String transPasswd, Dialog dialog);

        void onCancel();
    }

    public interface OnButtonEventListener {

        void onConfirm();

        void onCancel();
    }

    public interface OnButtonCancelListener {

        void onCancel();
    }

    public interface OnButtonVerfifyListener {

        void onConfirm(String phone, String verify);

        void onCancel();

        void onSendVerify(String phone, Button sendBtn);
    }

    public interface OnPaymentAccountListener {

        void onConfirm(String type, Dialog dialog);

    }
}
