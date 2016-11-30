package com.jrd.loan.util;

import android.widget.ImageView;

import com.jrd.loan.R;

public final class BankLogoUtil {
	public static void setBankLog(ImageView imgView, String bankCode) {
		setBankLog(imgView, Integer.parseInt(bankCode));
	}

	public static void setBankLog(ImageView imgView, int bankCode) {
		switch (bankCode) {
			case 103: // 中国农业银行
				imgView.setImageResource(R.drawable.loan_bank_nongye);
				break;
			case 104: // 中国银行
				imgView.setImageResource(R.drawable.loan_bank_zhongguo);
				break;
			case 105: // 中国建设银行
				imgView.setImageResource(R.drawable.loan_bank_jianshe);
				break;
			case 303: // 中国光大银行
				imgView.setImageResource(R.drawable.loan_bank_guangda);
				break;
			case 309: // 兴业银行
				imgView.setImageResource(R.drawable.loan_bank_xingye);
				break;
			case 302: // 中信银行
				imgView.setImageResource(R.drawable.loan_bank_zhongxin);
				break;
			case 308: // 招商银行
				imgView.setImageResource(R.drawable.loan_bank_zhaoshang);
				break;
			case 305: // 中国民生银行
				imgView.setImageResource(R.drawable.loan_bank_minsheng);
				break;
			case 304: // 华夏银行
				imgView.setImageResource(R.drawable.loan_bank_huaxia);
				break;
			case 102: // 中国工商银行
				imgView.setImageResource(R.drawable.loan_bank_gongshang);
				break;
			case 403: // 中国邮政储蓄银行
				imgView.setImageResource(R.drawable.loan_bank_youchu);
				break;
			case 310: // 浦发银行
				imgView.setImageResource(R.drawable.loan_bank_pufa);
				break;
			case 307: // 深圳平安银行
				imgView.setImageResource(R.drawable.loan_bank_pingan);
				break;
		}
	}
}