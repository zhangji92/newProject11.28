package com.jrd.loan.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseFragment;

public class FinanceFragment extends BaseFragment {
    private DirectFragment mDirectFragment;
    private CurrentPocketFragment currentPocketFragment;

    private Fragment mFragment;

    private RadioGroup mRadioGroup;
    private RadioButton mDirect; // 直投项目
    private RadioButton currentPocket;//活期口袋

    private int menuIndex;

    private OnControlBottomBarListener controlBottomBarListener;

    public void setOnControlBottomBarListener(OnControlBottomBarListener controlBottomBarListener) {
        this.controlBottomBarListener = controlBottomBarListener;
    }

    public FinanceFragment() {
    }

    public void setMenuIndex(int menuIndex) {
        this.menuIndex = menuIndex;
    }

    @Override
    protected int getLoadViewId() {
        return R.layout.loan_finance_products;
    }

    @Override
    protected void initView(View view) {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.loan_finance_radio);

        // 直投项目
        mDirect = (RadioButton) view.findViewById(R.id.loan_finance_direct);

        // 活期口袋
        currentPocket = (RadioButton) view.findViewById(R.id.currentPocket);

        if (this.menuIndex == 0) {
            mDirect.setChecked(true);
        } else {
            currentPocket.setChecked(true);
        }

        this.initFragment();
    }

    @Override
    protected void initData() {
        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.loan_finance_direct: // 直投项目
                        replaceFragment(mFragment, mDirectFragment);
                        mDirect.setChecked(true);
                        controlBottomBarListener.show();
                        break;
                    case R.id.currentPocket: // 活期口袋
                        replaceFragment(mFragment, currentPocketFragment);
                        controlBottomBarListener.hide();
                        break;
                }
            }
        });
    }

    @SuppressLint("NewApi")
    public void initFragment() {
        mDirectFragment = new DirectFragment();
        currentPocketFragment = new CurrentPocketFragment();

        if (this.menuIndex == 0) {
            mFragment = mDirectFragment;
        } else {
            mFragment = currentPocketFragment;
        }

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.loan_finance_frame, mFragment).commit();
    }

    @SuppressLint("NewApi")
    protected void replaceFragment(Fragment fragment, Fragment replaceFragment) {
        if (mFragment != replaceFragment) {
            mFragment = replaceFragment;
            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction tran = manager.beginTransaction();

            if (!replaceFragment.isAdded()) { // 先判断是否被add过
                tran.hide(fragment).replace(R.id.loan_finance_frame, replaceFragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                tran.hide(fragment).replace(R.id.loan_finance_frame, replaceFragment).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public interface OnControlBottomBarListener {
        void show();

        void hide();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (this.menuIndex == 0) {
                mDirect.setChecked(true);
            } else {
                currentPocket.setChecked(true);
            }
        }
    }
}
