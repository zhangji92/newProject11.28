package com.jrd.loan.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jrd.loan.R;
import com.jrd.loan.adapter.HomeFragmentAdapter;
import com.jrd.loan.api.ProjectInfoApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.BannerInfoBean;
import com.jrd.loan.bean.BannerList;
import com.jrd.loan.bean.ProjectInfoBean;
import com.jrd.loan.bean.ProjectList;
import com.jrd.loan.finance.ProjectIntroduceActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.MyBanner;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

public class HomeFragment extends BaseFragment implements IXListViewListener {
    private Context mContext;
    private final static int PAGESIZE = 10;
    private int PAGENO = 1;

    private LinearLayout homeLayout;
    private MyBanner mViewPager;

    private View header;

    private XListView mListView;
    private List<ProjectList> mProjectList;
    private HomeFragmentAdapter mHomeAdapter;

    private ArrayList<BannerList> imageList; // 滑动的图片集合
    private int currentItem = 0; // 当前图片的索引号

    private OnSevenPayYouListener sevenPayYouListener;

    @Override
    protected int getLoadViewId() {
        return R.layout.loan_home_layout;
    }

    public void setOnSevenPayYouListener(OnSevenPayYouListener sevenPayYouListener) {
        this.sevenPayYouListener = sevenPayYouListener;
    }

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        imageList = new ArrayList<BannerList>();

        homeLayout = (LinearLayout) view.findViewById(R.id.loan_home_layoutId);
        homeLayout.setVisibility(View.GONE);

        mListView = (XListView) view.findViewById(R.id.loan_home_ListView);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        this.mListView.setOnItemClickListener(this.itemClickListener);

        header = LayoutInflater.from(getActivity()).inflate(R.layout.loan_home_header_layout, null);
        mListView.addHeaderView(header);
    }

    private final OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ProjectList projectList = null;

            if (position >= 2) {
                projectList = (ProjectList) mHomeAdapter.getItem(position - 2);
            } else {
                projectList = (ProjectList) mHomeAdapter.getItem(position);
            }

            if (projectList.getType().equals("-1")) {// 7付你标的
                this.show7PayYouScreen();
            } else {// 直投标的
                this.showBidScreen(projectList);
            }
        }

        private void show7PayYouScreen() {
            sevenPayYouListener.onShow7PayYouScreen();
        }

        private void showBidScreen(ProjectList projectList) {
            Intent intent = new Intent(mContext, ProjectIntroduceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("mockId", projectList.getMockId());
            startActivity(intent);
        }
    };

    @Override
    protected void initData() {
        getIdxBannerInfo();
        ProjectInfo(PAGESIZE, 1);
    }

    public void initImgData() {
        mViewPager = (MyBanner) header.findViewById(R.id.banner);
        mViewPager.setData(imageList);
        mViewPager.setDurtion(4.0f);
    }

    // 刷新
    @Override
    public void onRefresh() {
        refreshPageFrist(PAGESIZE);
    }

    // 加载更多
    @Override
    public void onLoadMore() {
        ProjectInfo(PAGESIZE, PAGENO);
    }

    // 查询广告位banner信息
    private void getIdxBannerInfo() {
        ProjectInfoApi.getIdxBannerInfo(mContext, new OnHttpTaskListener<BannerInfoBean>() {
            @Override
            public void onStart() {
                DismissDialog();
            }

            @Override
            public void onTaskError(int resposeCode) {
                // ShowProDialog(mContext);
            }

            @Override
            public void onFinishTask(BannerInfoBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    homeLayout.setVisibility(View.VISIBLE);
                    imageList = bean.getBannerList();
                    initImgData();
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }
            }
        });
    }

    // 查询首页标的信息
    private void refreshPageFrist(int pageSize) {
        ProjectInfoApi.getIdxProjectInfo(mContext, pageSize, 1, new OnHttpTaskListener<ProjectInfoBean>() {
            @Override
            public void onTaskError(int resposeCode) {
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinishTask(ProjectInfoBean bean) {
                if (bean.getResultCode() == 0) {
                    if (bean.getProjectList().isEmpty()) {
                        return;
                    }

                    PAGENO = 2;
                    if (mHomeAdapter == null) {
                        mHomeAdapter = new HomeFragmentAdapter(mContext, bean.getProjectList());
                        mListView.setAdapter(mHomeAdapter);
                    } else {
                        mHomeAdapter.updatePageFirstBid(bean.getProjectList());
                    }
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }

                mListView.stopRefresh();
                mListView.stopLoadMore();
            }
        });
    }

    // 查询首页标的信息
    private void ProjectInfo(int pageSize, final int pageNo) {
        ProjectInfoApi.getIdxProjectInfo(mContext, pageSize, pageNo, new OnHttpTaskListener<ProjectInfoBean>() {
            private View noNetworkLayoutView;

            @Override
            public void onTaskError(int resposeCode) {
                if (pageNo == 1) {
                    this.noNetworkLayoutView.setVisibility(View.VISIBLE);
                    this.noNetworkLayoutView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData();
                        }
                    });

                    DismissDialog();
                }

                mListView.stopRefresh();
                mListView.stopLoadMore();
            }

            @Override
            public void onStart() {
                if (pageNo == 1) {
                    this.noNetworkLayoutView = getView().findViewById(R.id.noNetworkLayout);
                    this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    this.noNetworkLayoutView.setVisibility(View.GONE);

                    ShowDrawDialog(mContext);
                }
            }

            @Override
            public void onFinishTask(ProjectInfoBean bean) {
                if (pageNo == 1) {
                    DismissDialog();
                }

                if (bean.getResultCode() == 0) {
                    if (bean.getProjectList().isEmpty()) {
                        return;
                    }

                    PAGENO++;
                    if (mHomeAdapter == null) {
                        mHomeAdapter = new HomeFragmentAdapter(mContext, bean.getProjectList());
                        mListView.setAdapter(mHomeAdapter);
                    } else {
                        mHomeAdapter.updateMoreBids(bean.getProjectList());
                    }
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }

                mListView.stopRefresh();
                mListView.stopLoadMore();
            }
        });
    }

    public static interface OnSevenPayYouListener {
        public void onShow7PayYouScreen();
    }
}
