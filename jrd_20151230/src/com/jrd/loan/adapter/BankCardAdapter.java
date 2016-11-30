package com.jrd.loan.adapter;

import java.util.List;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.BankCardBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonEventListener;

public class BankCardAdapter extends BaseAdapter {

    private List<BankCardBean> cardList;
    private Context mContext;
    private String cardNum = "**** ";
    private int cardType;
    private Handler mHandler;

    public BankCardAdapter(List<BankCardBean> cardList, Context mContext, Handler mHandler) {
        this.cardList = cardList;
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return cardList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View converView, ViewGroup arg2) {
        final ViewHolder holder;
        if (converView == null) {
            holder = new ViewHolder();
            converView = View.inflate(mContext, R.layout.loan_card_item_layout, null);
            holder.bankNum = (TextView) converView.findViewById(R.id.bank_item_text);
            holder.cardType = (RelativeLayout) converView.findViewById(R.id.bank_item_layout);
            holder.deleteImage = (ImageView) converView.findViewById(R.id.bank_item_delete_image);
            holder.deleteLayout = (LinearLayout) converView.findViewById(R.id.bank_item_delete_layout);
            holder.deleteTV = (TextView) converView.findViewById(R.id.bank_item_delete_text);
            converView.setTag(holder);
        } else {
            holder = (ViewHolder) converView.getTag();
        }

        if (mHandler != null) {
            holder.deleteLayout.setVisibility(View.VISIBLE);
        } else {
            holder.deleteLayout.setVisibility(View.GONE);
        }

        if (cardList.get(position).isShowDel()) {
            holder.deleteTV.setVisibility(View.VISIBLE);
        } else {
            holder.deleteTV.setVisibility(View.GONE);
        }
        String cardCode = cardList.get(position).getBankCode();
        if (cardCode != null && !cardCode.isEmpty()) {
            cardType = Integer.valueOf(cardCode);
        }

        switch (cardType) {
            case 104:
                holder.cardType.setBackgroundResource(R.drawable.loan_zhongguoyinhang_icon);
                break;
            case 102:
                holder.cardType.setBackgroundResource(R.drawable.loan_gongshangyinghang_icon);
                break;
            case 103:
                holder.cardType.setBackgroundResource(R.drawable.loan_nongyeyinghang_icon);
                break;
            case 105:
                holder.cardType.setBackgroundResource(R.drawable.loan_jiansheyinghang_icon);
                break;
            case 308:
                holder.cardType.setBackgroundResource(R.drawable.loan_zhaoshang);
                break;
            case 403:
                holder.cardType.setBackgroundResource(R.drawable.loan_youzheng_icon);
                break;
            case 302:
                holder.cardType.setBackgroundResource(R.drawable.loan_zhongxinyinghang_icon);
                break;
            case 303:
                holder.cardType.setBackgroundResource(R.drawable.loan_guangda_icon);
                break;
            case 309:
                holder.cardType.setBackgroundResource(R.drawable.loan_xingyeyinghang_icon);
                break;
        }
        String num = cardList.get(position).getCardNumber();
        StringBuilder sb = new StringBuilder(cardNum);
        sb.append(num.substring(num.length() - 4, num.length()));
        holder.bankNum.setText(sb.toString());

        holder.deleteImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (cardList.get(position).isShowDel()) {
                    CloseAnim(position, holder);

                } else {
                    StartAnim(position, holder);
                }
            }

        });

        holder.deleteTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                DialogUtils.ShowDelCardDialog(mContext, "删除", "取消", "确定要删除这张银行卡吗？", "6214******8304", R.drawable.loan_zhaoshang_small, new OnButtonEventListener() {

                    @Override
                    public void onConfirm() {
                        Message msg = Message.obtain();
                        msg.arg1 = position;
                        msg.what = Const.HandlerCode.CARD_DELETE_HANDLER_CODE;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });

        return converView;
    }

    /**
     * 开启动画
     *
     * @param position
     * @param holder
     */
    private void StartAnim(final int position, final ViewHolder holder) {
        holder.deleteTV.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(holder.deleteImage, "rotation", 0, 90);
        animator.setDuration(500);
        animator.start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(holder.deleteTV, "translationX", 0F, 50f);
        animator1.setDuration(800);
        animator1.setInterpolator(new BounceInterpolator());
        animator1.start();
        cardList.get(position).setShowDel(true);
    }

    /**
     * 关闭动画
     *
     * @param position
     * @param holder
     */
    private void CloseAnim(final int position, final ViewHolder holder) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(holder.deleteImage, "rotation", 90, 0);
        animator.setDuration(500);
        animator.start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(holder.deleteTV, "translationX", 50F, 0f);
        animator1.setDuration(800);
        animator1.setInterpolator(new BounceInterpolator());
        animator1.start();
        holder.deleteTV.setVisibility(View.GONE);
        cardList.get(position).setShowDel(false);
    }

    class ViewHolder {

        RelativeLayout cardType;
        TextView bankNum;
        LinearLayout deleteLayout;
        ImageView deleteImage;
        TextView deleteTV;

    }
}
