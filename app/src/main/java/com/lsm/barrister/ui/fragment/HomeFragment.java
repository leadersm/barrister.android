package com.lsm.barrister.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.Account;
import com.lsm.barrister.data.entity.Ad;
import com.lsm.barrister.data.entity.Case;
import com.lsm.barrister.data.entity.OrderItem;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetCaseListReq;
import com.lsm.barrister.data.io.app.GetLunboAdsReq;
import com.lsm.barrister.data.io.app.GetMyAccountReq;
import com.lsm.barrister.data.io.app.GetUserHomeReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.activity.WebViewActivity;
import com.lsm.barrister.ui.adapter.CaseListAdapter;
import com.lsm.barrister.ui.adapter.LoadMoreListener;
import com.lsm.barrister.ui.adapter.OrderListAdapter;
import com.lsm.barrister.ui.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements UserHelper.UserActionListener,UserHelper.OnAccountUpdateListener{

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    AQuery aq;
    ViewPager mViewPager;
    AdsPagerAdapter mAdsAdapter;
    CirclePageIndicator indicator;

    GetUserHomeReq mGetHomeReq;
    GetLunboAdsReq mGetAdsReq;

    RecyclerView mTodoListView,mCaseListView;
    LinearLayoutManager mTodoListLayoutManager,mCaseListLayoutManager;

    OrderListAdapter mTodoListAdapter;
    CaseListAdapter mCaseListAdapter;

    View view;

    private void init(View view) {
        this.view = view;

        aq = new AQuery(view);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_home_images);
        mAdsAdapter = new AdsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdsAdapter);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);

        aq.id(R.id.btn_get_money).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goGetMoneyActivity(getActivity());
            }
        });

        aq.id(R.id.btn_home_more_cases).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goCaseListAcitivity(getActivity());
            }
        });

        mTodoListView = (RecyclerView) view.findViewById(R.id.recyclerview_home_todos);
        mCaseListView = (RecyclerView) view.findViewById(R.id.recyclerview_home_cases);

        mTodoListLayoutManager = new LinearLayoutManager(getActivity());
        mCaseListLayoutManager = new LinearLayoutManager(getActivity());

        mTodoListView.setLayoutManager(mTodoListLayoutManager);
        mTodoListView.setItemAnimator(new DefaultItemAnimator());

        mCaseListView.setLayoutManager(mCaseListLayoutManager);
        mCaseListView.setItemAnimator(new DefaultItemAnimator());


        mTodoListAdapter = new OrderListAdapter(todos, new LoadMoreListener() {

            @Override
            public void onLoadMore() {

            }

            @Override
            public boolean hasMore() {
                return false;
            }
        },true);


        mTodoListView.setAdapter(mTodoListAdapter);

        mCaseListAdapter = new CaseListAdapter(cases, new LoadMoreListener() {

            @Override
            public void onLoadMore() {

            }

            @Override
            public boolean hasMore() {
                return false;
            }
        },true);

        mCaseListView.setAdapter(mCaseListAdapter);

        refresh();
    }

    List<OrderItem> todos = new ArrayList<>();
    List<Case> cases = new ArrayList<>();

    public void refresh() {
        if (mGetHomeReq == null) {
            mGetHomeReq = new GetUserHomeReq(getContext());
        }

        mGetHomeReq.execute(new Action.Callback<IO.HomeResult>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getContext(), msg);
            }

            @Override
            public void onCompleted(IO.HomeResult homeResult) {

                bindHomeData(homeResult);
            }
        });

        if (mGetAdsReq == null) {
            mGetAdsReq = new GetLunboAdsReq(getActivity());
        }

        mGetAdsReq.execute(new Action.Callback<IO.GetLunboAdsResult>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getContext(), msg);
            }

            @Override
            public void onCompleted(IO.GetLunboAdsResult getLunboAdsResult) {
                if (getLunboAdsResult != null && getLunboAdsResult.list != null) {

                    ads.clear();
                    ads.addAll(getLunboAdsResult.list);
                    mAdsAdapter.notifyDataSetChanged();

                }
            }
        });

        new GetCaseListReq(getContext(),1,3).execute(new Action.Callback<IO.GetCaseListResult>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

            }

            @Override
            public void onCompleted(IO.GetCaseListResult result) {

                if (result.cases != null) {
                    cases.clear();
                    cases.addAll(result.cases);
                    mCaseListAdapter.notifyDataSetChanged();
                }

            }
        });

    }


    private void bindHomeData(IO.HomeResult homeResult) {

        int orderQty = homeResult.orderQty;

        String remainingBalance = homeResult.remainingBalance;
        String status = homeResult.status;


        List<OrderItem> todoList = homeResult.todoList;

        String totalIncome = homeResult.totalIncome;

        aq.id(R.id.tv_home_yue).text(remainingBalance + "元");

        aq.id(R.id.tv_home_income).text(totalIncome + "元");


        if (status.equals(User.ORDER_STATUS_CAN)) {

            aq.id(R.id.tv_home_accept_status).text("可以接单");
        } else {

            aq.id(R.id.tv_home_accept_status).text("不可接单");
        }


        aq.id(R.id.tv_home_order_qty).text("累计订单" + orderQty);


        if (todoList != null) {
            todos.clear();
            todos.addAll(todoList);
            mTodoListAdapter.notifyDataSetChanged();
        }


    }

    public class AdsPagerAdapter extends FragmentPagerAdapter {

        public AdsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return AdFragment.newInstance(ads.get(position));
        }

        @Override
        public int getCount() {
            return ads.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

    }

    public static class AdFragment extends Fragment {

        private static final String AD = "item.ad";

        public AdFragment() {
        }

        Ad ad;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();
            if (args != null && args.getSerializable(AD) != null) {
                ad = (Ad) args.getSerializable(AD);
            }
        }

        public static AdFragment newInstance(Ad ad) {
            AdFragment fragment = new AdFragment();
            Bundle args = new Bundle();
            args.putSerializable(AD, ad);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            SimpleDraweeView draweeView = (SimpleDraweeView) inflater.inflate(R.layout.item_image, container, false);

            draweeView.setImageURI(Uri.parse(ad.getImage()));

//            Picasso.with(getActivity()).load(image.getThumb()).into(draweeView);

            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.KEY_URL, ad.getUrl());
                    intent.putExtra(WebViewActivity.KEY_TITLE, "");
                    startActivity(intent);
                }
            });
            return draweeView;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        UserHelper.getInstance().addOnUserActionListener(this);
        UserHelper.getInstance().addOnAccountUpdateListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UserHelper.getInstance().removeListener(this);
        UserHelper.getInstance().removeAccountListener(this);
    }

    List<Ad> ads = new ArrayList<>();

    @Override
    public void onSSOLoginCallback(User user) {
    }

    @Override
    public void onLoginCallback(User user) {
        //请求账户信息，余额，累计消费
        loadMyAccount();
    }

    @Override
    public void onLogoutCallback() {
        //余额，累计消费 显示 0.0
        onUpdateAccount(null);
    }

    @Override
    public void onUpdateUser() {

    }


    @Override
    public void onUpdateAccount(Account account) {
        float remainingBalance = 0f;
        float totalConsume = 0f;

        if(account != null){
            remainingBalance = account.getRemainingBalance();
            totalConsume = account.getTotalIncome();
        }

        aq.id(R.id.tv_home_yue).text(String.format(Locale.CHINA,"%.1f元", remainingBalance));
        aq.id(R.id.tv_home_income).text(String.format(Locale.CHINA,"%.1f元", totalConsume));
    }

    private void loadMyAccount(){

        User user = AppConfig.getUser(getContext());

        if(user==null)
            return;

        new GetMyAccountReq(getActivity()).execute(new Action.Callback<IO.GetAccountResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
            }

            @Override
            public void onCompleted(IO.GetAccountResult result) {
                UserHelper.getInstance().setAccount(result.account);
                UserHelper.getInstance().updateAccount();
            }
        });

    }
}
