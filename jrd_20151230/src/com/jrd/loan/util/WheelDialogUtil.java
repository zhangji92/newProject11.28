package com.jrd.loan.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.wheel.widget.OnWheelChangedListener;
import com.jrd.loan.wheel.widget.WheelView;
import com.jrd.loan.wheel.widget.adapters.ArrayWheelAdapter;
import com.jrd.loan.widget.CustomDialog;

public final class WheelDialogUtil {
    private static String mCurrentProviceName;
    private static String cityName;

    /*
     * 显示齿轮dialog
     *
     * @param context 上下文索引
     *
     * @param strList 数据源
     *
     * @param cyclic 是否循环显示数据(true 循环显示, false 不循环显示)
     *
     * @param wheelListener 用户点击确定button时的监听器
     */
    public final static void showWheelDialog(final Context context, List<String> strList, int currItemPosition, final OnWheelListener wheelListener) {
        String[] strArr = new String[strList.size()];
        strList.toArray(strArr);

        showWheelDialog(context, strArr, currItemPosition, wheelListener);
    }

    /*
     * 显示齿轮dialog
     *
     * @param context 上下文索引
     *
     * @param strArr 数据源
     *
     * @param cyclic 是否循环显示数据(true 循环显示, false 不循环显示)
     *
     * @param wheelListener 用户点击确定button时的监听器
     */
    public final static void showWheelDialog(final Context context, final String[] strArr, int currItemPosition, final OnWheelListener wheelListener) {
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_choose_wheel_layout, null);

        final WheelView questionWheel = (WheelView) layout.findViewById(R.id.questionWheel);
        questionWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, strArr));
        questionWheel.setVisibleItems(7);

        final CustomDialog dialog = new CustomDialog(context, layout);

        OnClickListener btnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tvConfirm:
                        if (wheelListener != null) {
                            wheelListener.onConfirm(strArr[questionWheel.getCurrentItem()]);
                            wheelListener.onCurrentItem(questionWheel.getCurrentItem());
                        }

                        dialog.dismiss();
                        break;
                    case R.id.tvCancel:
                        dialog.dismiss();
                        break;
                }
            }
        };

        // 确定
        TextView tvConfirm = (TextView) layout.findViewById(R.id.tvConfirm);
        tvConfirm.setOnClickListener(btnClickListener);

        // 取消
        TextView tvCancel = (TextView) layout.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(btnClickListener);

        dialog.show();
    }

    /**
     * 显示选择城市的对话框
     *
     * @param context
     * @param provinceData
     * @param cityData
     * @param wheelListener
     */
    public final static void showCityWheelDialog(final Context context, final String[] provinceData, final Map<String, String[]> cityData, final OnWheelListener wheelListener) {
        LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loan_choose_city_wheel_layout, null);

        final WheelView provinceWheel = (WheelView) layout.findViewById(R.id.provinceWheel);
        final WheelView cityWheel = (WheelView) layout.findViewById(R.id.cityWheel);
        provinceWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, provinceData));
        provinceWheel.setVisibleItems(7);
        cityWheel.setVisibleItems(7);

        UpdateCitis(context, provinceData, cityData, provinceWheel, cityWheel);

        provinceWheel.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                UpdateCitis(context, provinceData, cityData, provinceWheel, cityWheel);

            }
        });

        cityWheel.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                cityName = cityData.get(mCurrentProviceName)[cityWheel.getCurrentItem()];

            }
        });

        final CustomDialog dialog = new CustomDialog(context, layout);

        OnClickListener btnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tvConfirm:
                        if (wheelListener != null) {
                            StringBuffer sb = new StringBuffer(mCurrentProviceName);
                            sb.append(cityName);
                            wheelListener.onConfirm(sb.toString());
                        }

                        dialog.dismiss();
                        break;
                    case R.id.tvCancel:
                        dialog.dismiss();
                        break;
                }
            }
        };

        // 确定
        TextView tvConfirm = (TextView) layout.findViewById(R.id.tvConfirm);
        tvConfirm.setOnClickListener(btnClickListener);

        // 取消
        TextView tvCancel = (TextView) layout.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(btnClickListener);

        dialog.show();

    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private static void UpdateCitis(final Context context, final String[] provinceData, final Map<String, String[]> cityData, final WheelView provinceWheel, final WheelView cityWheel) {
        int pCurrent = provinceWheel.getCurrentItem();
        mCurrentProviceName = provinceData[pCurrent];

        String[] cities = cityData.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        cityWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        cityWheel.setCurrentItem(0);
        cityName = cityData.get(mCurrentProviceName)[cityWheel.getCurrentItem()];
    }

    public static interface OnWheelListener {
        public void onConfirm(String text);

        public void onCurrentItem(int currentItem);
    }

}