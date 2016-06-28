package com.lsm.barrister.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.OrderItem;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetMyOrderListReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.adapter.EmptyController;
import com.lsm.barrister.ui.adapter.LoadMoreListener;
import com.lsm.barrister.ui.adapter.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单:即时，预约
 */
public class MyOrdersFragment extends Fragment {


    public MyOrdersFragment() {
    }

    public static MyOrdersFragment newInstance() {
        MyOrdersFragment fragment = new MyOrdersFragment();
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

    AQuery aq;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        init(view);
        return view;
    }

    private void init(View view) {

        aq = new AQuery(view);

        aq.id(R.id.consult_tab_bespeak).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        aq.id(R.id.consult_tab_phone).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        setupViewPager(view);

    }

    ViewPager mViewPager;

    SectionsPagerAdapter mSectionsPagerAdapter;

    private void setupViewPager(View view) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {

                    case 0:
                        aq.id(R.id.consult_tab_bespeak).checked(true);
                        break;
                    case 1:
                        aq.id(R.id.consult_tab_phone).checked(true);

                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String type = null;
            if(position==0){
                type = GetMyOrderListReq.TYPE_APPOINTMENT;
            }else{
                type = GetMyOrderListReq.TYPE_IM;

            }

            OrderListFragement orderListFragement = OrderListFragement.newInstance(type);

            return orderListFragement;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    public static class OrderListFragement extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

        public OrderListFragement() {

        }

        String type;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            type = getArguments().getString("type");
        }

        public static OrderListFragement newInstance(String type) {
            OrderListFragement fragment = new OrderListFragement();

            Bundle args = new Bundle();
            args.putString("type",type);
            fragment.setArguments(args);

            return fragment;
        }


        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            super.onDetach();

            items = null;
        }

        List<OrderItem> items = new ArrayList<>();

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.common_recycler_list,container,false);
            init(view);
            return view;
        }

        SwipeRefreshLayout mSwipeRefreshLayout;
        RecyclerView mRecyclerView;
        LinearLayoutManager mLayoutManager;

        OrderListAdapter mAdapter;

        EmptyController mEmptyController;

        private void init(View view){

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                    android.R.color.holo_red_light, android.R.color.holo_blue_light);

            mSwipeRefreshLayout.setOnRefreshListener(this);
            mRecyclerView.setHasFixedSize(true);

            View emptyView = view.findViewById(R.id.emptyLayout);

            mEmptyController = new EmptyController(mSwipeRefreshLayout, emptyView, new EmptyController.Callback() {

                @Override
                public void doRefresh() {
                    onRefresh();
                }
            });

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new OrderListAdapter(items, new LoadMoreListener() {

                @Override
                public void onLoadMore() {
                    loadMore();
                }

                @Override
                public boolean hasMore() {

                    return mGetOrderListReq != null
                            && mRefreshResult!=null
                            && page + 1 <= mGetOrderListReq.getTotalPage(mRefreshResult.total,GetMyOrderListReq.pageSize);
                }
            });


            mRecyclerView.setAdapter(mAdapter);


            refresh();
        }

        GetMyOrderListReq mGetOrderListReq;
        IO.GetMyOrdersResult mRefreshResult;
        int page = 1;

        private void refresh() {

            if(mGetOrderListReq==null){
                mGetOrderListReq = new GetMyOrderListReq(getActivity(),page = 1, type);
            }

            mGetOrderListReq.execute(new Action.Callback<IO.GetMyOrdersResult>() {

                @Override
                public void progress() {
                    mEmptyController.showLoading();
                }

                @Override
                public void onError(int errorCode, String msg) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mEmptyController.showError(errorCode,msg);
//                    UIHelper.showToast(getContext(),msg);
                }

                @Override
                public void onCompleted(IO.GetMyOrdersResult getMyOrdersResult) {

                    mSwipeRefreshLayout.setRefreshing(false);

                    mRefreshResult = getMyOrdersResult;

                    if(mRefreshResult.orders !=null){
                        mEmptyController.showContent();

                        items.clear();
                        items.addAll(mRefreshResult.orders);
                        mAdapter.notifyDataSetChanged();

                    }else {

                        mEmptyController.showEmpty();

                    }
                }
            });

        }

        /**
         * 加载更多
         */
        public void loadMore(){

            new GetMyOrderListReq(getActivity(), ++page, type)
                    .execute(new Action.Callback<IO.GetMyOrdersResult>() {

                        @Override
                        public void progress() {

                        }

                        @Override
                        public void onError(int errorCode, String msg) {

                            --page;

                            UIHelper.showToast(getContext(),msg);
                        }

                        @Override
                        public void onCompleted(IO.GetMyOrdersResult getMyOrdersResult) {

                            if(getMyOrdersResult.orders !=null){

                                items.addAll(getMyOrdersResult.orders);

                                mAdapter.notifyDataSetChanged();

                                mEmptyController.showContent();

                            }
                        }
                    });

        }

        @Override
        public void onRefresh() {
            refresh();
        }
    }

}
