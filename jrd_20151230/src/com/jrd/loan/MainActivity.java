package com.jrd.loan;

import java.io.File;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.api.AppUpgradeApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.AppInfoBean;
import com.jrd.loan.bean.VersionInfo;
import com.jrd.loan.constant.Const.Extra;
import com.jrd.loan.fragment.BoutiqueCommendFragment;
import com.jrd.loan.fragment.BoutiqueCommendFragment.OnCurrentPocketListener;
import com.jrd.loan.fragment.FinanceFragment;
import com.jrd.loan.fragment.FinanceFragment.OnControlBottomBarListener;
import com.jrd.loan.fragment.MyAccountFragment;
import com.jrd.loan.net.download.DownloadStateListener;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ActivityCollector;
import com.jrd.loan.util.AppInfoUtil;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonEventListener;
import com.jrd.loan.util.FileUtil;
import com.jrd.loan.util.ToastUtil;
import com.umeng.message.PushAgent;

public class MainActivity extends BaseActivity {

  private final static int BOTTOM_MENU_HOME_PAGE = 0;
  private final static int BOTTOM_MENU_FINANCE = 1;
  public final static int BOTTOM_MENU_MY_ACCOUNT = 2;
  private int currMenu = BOTTOM_MENU_HOME_PAGE;

  private FragmentManager fragmentManager;

  private BoutiqueCommendFragment homeFragment;
  private FinanceFragment financeFragment;
  private MyAccountFragment myAccountFragment;

  private ImageButton btnHome; // 首页
  private ImageButton btnFinance; // 理财
  private ImageButton btnMyAccount; // 我的账户

  private LinearLayout bottomBarLayout;

  private int financeMenuIndex = 0;
  private long singTime = 0L;

  private Dialog dialog;
  private Dialog dialog2;

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_main;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    PushAgent mPushAgent = PushAgent.getInstance(this);
    mPushAgent.enable();
    PushAgent.getInstance(this).onAppStart();

    AppInfoPrefs.setFirstStartMainScreen(false);

    // 检查应用是否有更新
    this.checkAppNewVersion();
  }

  @Override
  protected void initViews() {
    this.bottomBarLayout = (LinearLayout) findViewById(R.id.wallet_main_buttom_rg);
    this.fragmentManager = getFragmentManager();

    // 首页
    this.btnHome = (ImageButton) findViewById(R.id.btnHomePage);
    this.btnHome.setSelected(true);
    LinearLayout homeLayout = (LinearLayout) findViewById(R.id.homePageLayout);
    homeLayout.setOnClickListener(this.btnClickListener);

    // 理财
    this.btnFinance = (ImageButton) findViewById(R.id.btnFinance);
    LinearLayout financeLayout = (LinearLayout) findViewById(R.id.financeLayout);
    financeLayout.setOnClickListener(this.btnClickListener);

    // 我的账户
    this.btnMyAccount = (ImageButton) findViewById(R.id.btnMyAccount);
    LinearLayout myAccountLayout = (LinearLayout) findViewById(R.id.myAccountLayout);
    myAccountLayout.setOnClickListener(this.btnClickListener);

    this.currMenu = getIntent().getIntExtra("currMenu", -1);

    if (this.currMenu == -1) {
      this.currMenu = BOTTOM_MENU_HOME_PAGE;
    }

    // 注册成功后跳转带值赋值
    if (getIntent().getIntExtra(Extra.Select_Info, 0) == 1) {
      this.currMenu = BOTTOM_MENU_FINANCE;
    } else if (getIntent().getIntExtra(Extra.Select_Info, 0) == 2) {
      this.currMenu = BOTTOM_MENU_MY_ACCOUNT;
    }

    if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 首页
      homeLayout.performClick();
    } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 理财
      financeLayout.performClick();
    } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {// 我的账户
      myAccountLayout.performClick();
    }

  }

  private final OnClickListener btnClickListener = new OnClickListener() {

    @Override
    public void onClick(View view) {
      switch (view.getId()) {
        case R.id.homePageLayout: // 首页
          showHomePage();
          break;
        case R.id.financeLayout: // 理财
          financeMenuIndex = 0;
          showFinance();
          break;
        case R.id.myAccountLayout: // 我的账户
          showMyAccount();
          break;
      }
    }
  };

  private void showHomePage() {
    bottomBarLayout.setVisibility(View.VISIBLE);
    this.changeMenu(BOTTOM_MENU_HOME_PAGE);
  }

  private void showFinance() {
    this.changeMenu(BOTTOM_MENU_FINANCE);
  }

  private void showMyAccount() {
    if (UserInfoPrefs.isLogin()) {
      this.changeMenu(BOTTOM_MENU_MY_ACCOUNT);
    } else {
      startActivity(new Intent(MainActivity.this, LoginActivity.class));
      overridePendingTransition(R.anim.loan_bottom_to_top, R.anim.loan_default);
    }
  }

  private void changeMenu(int menu) {
    this.currMenu = menu;
    this.setCurrMenuState();
    this.changeFragment();
  }

  private void setCurrMenuState() {
    if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 君融贷
      this.btnHome.setSelected(true);
      this.btnFinance.setSelected(false);
      this.btnMyAccount.setSelected(false);
    } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 理财
      this.btnHome.setSelected(false);
      this.btnFinance.setSelected(true);
      this.btnMyAccount.setSelected(false);
    } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {// 我的账户
      this.btnHome.setSelected(false);
      this.btnFinance.setSelected(false);
      this.btnMyAccount.setSelected(true);
    }
  }

  private void changeFragment() {
    FragmentTransaction trans = this.fragmentManager.beginTransaction();

    if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 精选推荐
      if (homeFragment == null) {
        this.homeFragment = new BoutiqueCommendFragment();
        trans.add(R.id.contentLayout, this.homeFragment);
      } else {
        trans.show(this.homeFragment);
      }

      this.homeFragment.setOnCurrentPocketListener(this.currentPocketListener);

      if (this.financeFragment != null) {
        trans.hide(this.financeFragment);
      }

      if (this.myAccountFragment != null) {
        trans.hide(this.myAccountFragment);
      }
    } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 理财
      if (this.homeFragment != null) {
        trans.hide(this.homeFragment);
      }

      if (financeFragment == null) {
        this.financeFragment = new FinanceFragment();
        trans.add(R.id.contentLayout, this.financeFragment);
        this.financeFragment.setOnControlBottomBarListener(this.controlBottomBarListener);
      } else {
        trans.show(this.financeFragment);
      }
      this.financeFragment.setMenuIndex(this.financeMenuIndex);

      if (this.myAccountFragment != null) {
        trans.hide(this.myAccountFragment);
      }
    } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {// 我的账户
      if (this.homeFragment != null) {
        trans.hide(this.homeFragment);
      }

      if (this.financeFragment != null) {
        trans.hide(this.financeFragment);
      }

      if (myAccountFragment == null) {
        this.myAccountFragment = new MyAccountFragment();
        trans.add(R.id.contentLayout, this.myAccountFragment);
      } else {
        // trans.replace(R.id.contentLayout, this.myAccountFragment);
        trans.show(this.myAccountFragment);
      }
    }

    trans.commit();
  }

  @Override
  protected void initTitleBar() {}

  private OnCurrentPocketListener currentPocketListener = new OnCurrentPocketListener() {
    @Override
    public void onShowCurrentPocketScreen() {
      bottomBarLayout.setVisibility(View.GONE);

      financeMenuIndex = 1;
      showFinance();
    }
  };

  @Override
  public void onBackPressed() {
    if (btnHome.isSelected()) {
      if (this.singTime > 0) {
        long nowTime = System.currentTimeMillis();
        if ((nowTime - this.singTime) < 2000) {
          BaseActivity.setIsPresseHomeKey(true);
          BaseActivity.setLastTimes(0L);
          finish();
        } else {
          this.singTime = nowTime;
          Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
      } else {
        this.singTime = System.currentTimeMillis();
        Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
      }
    } else {
      showHomePage();
    }
  }

  private OnControlBottomBarListener controlBottomBarListener = new OnControlBottomBarListener() {
    @Override
    public void show() {
      if (bottomBarLayout.getVisibility() != View.VISIBLE) {
        bottomBarLayout.setVisibility(View.VISIBLE);
      }
    }

    @Override
    public void hide() {
      bottomBarLayout.setVisibility(View.GONE);
    }
  };


  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
      View v = getCurrentFocus();
      if (isShouldHideInput(v, ev)) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
          imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
      }
      return super.dispatchTouchEvent(ev);
    }
    // 必不可少，否则所有的组件都不会有TouchEvent了
    if (getWindow().superDispatchTouchEvent(ev)) {
      return true;
    }
    return onTouchEvent(ev);
  }

  public boolean isShouldHideInput(View v, MotionEvent event) {
    if (v != null && (v instanceof EditText)) {
      int[] leftTop = {0, 0};
      // 获取输入框当前的location位置
      v.getLocationInWindow(leftTop);
      int left = leftTop[0];
      int top = leftTop[1];
      int bottom = top + v.getHeight();
      int right = left + v.getWidth();
      if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
        // 点击的是输入框区域，保留点击EditText的事件
        return false;
      } else {
        return true;
      }
    }
    return false;
  }

  // 版本更新
  private void checkAppNewVersion() {
    AppUpgradeApi.upgradeApp(this, new OnHttpTaskListener<AppInfoBean>() {

      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {}

      @Override
      public void onFinishTask(AppInfoBean bean) {
        // 服务器版本号
        VersionInfo versionInfo = bean.getVerInfo();

        if (versionInfo == null) {
          return;
        }
        if (versionInfo.getIsShowUpdateMsg().equals("0")) {
          return;
        }

        int serverVersion = Integer.valueOf(versionInfo.getVersionName().replace(".", ""));

        // app下载地址
        final String downloadUrl = versionInfo.getAppUrl();

        // 本地app版本号
        int appVersionCode = Integer.valueOf(AppInfoUtil.getVersionName().replace(".", ""));

        // appVersionCode = 0;
        // 服务器版本大于本地版本, 需要升级程序
        if (serverVersion > appVersionCode) {
          if (versionInfo.getMustUpdate().equals("0")) {
            DialogUtils.showDialog(MainActivity.this, versionInfo.getUpdateMsg(), R.string.loan_immediate_updates, R.string.loan_later_on, new OnButtonEventListener() {

              @Override
              public void onConfirm() {
                // Intent intent = new Intent(BaseActivity.this,
                // StateBarDownloadService.class);
                // intent.putExtra(StateBarDownloadService.DOWNLOAD_URL,
                // TextUtils.isEmpty(downloadUrl) ? "" :
                // downloadUrl);
                // startService(intent);
                dialog2 = DialogUtils.ShowUpdateProDialog(MainActivity.this, downloadUrl, new FileDownloadStateListener(downloadUrl));
              }

              @Override
              public void onCancel() {
                // ActivityCollector.finishAll();
              }
            });
          } else if (versionInfo.getMustUpdate().equals("1")) {
            DialogUtils.showConfirmDialog(MainActivity.this, versionInfo.getUpdateMsg(), R.string.loan_immediate_updates, false, new OnButtonEventListener() {

              @Override
              public void onCancel() {
                ActivityCollector.finishAll();
              }

              @Override
              public void onConfirm() {
                dialog2 = DialogUtils.ShowUpdateProDialog(MainActivity.this, downloadUrl, new FileDownloadStateListener(downloadUrl));
              }
            });
          }
        }
      }
    });
  }
  
  
  public class FileDownloadStateListener implements DownloadStateListener {

    private String downloadUrl;
    private TextView tvPercent; // 文件下载百分比
    private ProgressBar proBar; // 文件下载进度

    public FileDownloadStateListener(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setDownloadPro(TextView tvPercent, ProgressBar proBar) {
        this.tvPercent = tvPercent;
        this.proBar = proBar;
    }

    @Override
    public void onStartDownload(int max) {
        proBar.setMax(max);
        tvPercent.setText("0%");
    }

    @Override
    public void onProgress(int size) {
        proBar.setProgress(size + proBar.getProgress());
        tvPercent.setText(calDownloadPercent(proBar.getProgress(), proBar.getMax()));
    }

    @Override
    public void onEndDownload() {
        proBar.setProgress(proBar.getMax());
        tvPercent.setText("100%");
        dialog2.dismiss();
        this.installApk();
    }

    @Override
    public void onDownloadFail() {
        this.handDownloadFail();
    }

    // 处理下载失败的情况
    private void handDownloadFail() {
        dialog2.dismiss();
        ToastUtil.showShort(MainActivity.this, R.string.loan_state_bar_download_fail);
    }

    // 成功下载结束后自动启动安装程序
    private void installApk() {
        BaseActivity.setIsPresseHomeKey(true);
        AppInfoUtil.installApk(new File(FileUtil.UPGRADE_APK_DOWNLOAD_PATH, downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1)).getAbsolutePath());
    }

    private String calDownloadPercent(int currSize, int maxSize) {
        return new StringBuffer(String.valueOf((int) ((currSize * 1.0 / maxSize) * 100))).append("%").toString();
    }
}
}
