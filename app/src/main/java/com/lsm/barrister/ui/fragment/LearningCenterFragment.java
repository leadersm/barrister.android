package com.lsm.barrister.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.Channel;
import com.lsm.barrister.data.entity.LearningItem;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetStudyChannelListReq;
import com.lsm.barrister.data.io.app.GetStudyListReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.adapter.EmptyController;
import com.lsm.barrister.ui.adapter.LearningRecyclerViewAdapter;
import com.lsm.barrister.ui.adapter.LoadMoreListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 学习中心
 */
public class LearningCenterFragment extends Fragment  {

    public LearningCenterFragment() {
    }

    // TODO: Customize parameter initialization
    public static LearningCenterFragment newInstance() {
        LearningCenterFragment fragment = new LearningCenterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Channel channel = new Channel();
        channel.title = "视频";
        channel.id = "1";

        data.add(channel);

        channel = new Channel();
        channel.title = "书籍";
        channel.id = "2";
        data.add(channel);

        channel = new Channel();
        channel.title = "微电影";
        channel.id = "3";

        data.add(channel);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning_center, container, false);

        init(view);

        return view;
    }

    MyPagerAdapter mAdapter;
    ViewPager viewPager;

    List<Channel> data = new ArrayList<>();

    SmartTabLayout viewPagerTab;

    View view;
    private void init(View view) {
        this.view = view;
        loadChannel();
    }

    private void loadChannel() {

        new GetStudyChannelListReq(getContext()).execute(new Action.Callback<IO.GetChannelListResult>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                if(isAdded()){
                    showReloadChannelDialog();
                }
            }

            @Override
            public void onCompleted(IO.GetChannelListResult result) {
                if(result.items!=null && !result.items.isEmpty()){
                    data.clear();
                    data.addAll(result.items);

//                    mAdapter.notifyDataSetChanged();
//                    viewPagerTab.setViewPager(viewPager);

                    mAdapter = new MyPagerAdapter(getChildFragmentManager());
                    viewPager = (ViewPager) view.findViewById(R.id.viewpager);
                    viewPager.setAdapter(mAdapter);

                    viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
                    viewPagerTab.setViewPager(viewPager);
                }
            }
        });
    }

    private void showReloadChannelDialog() {
        new AlertDialog.Builder(getContext()).setTitle("请重新加载频道").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadChannel();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }


    /**
     * @author lsm
     * @date 2015-4-6
     */
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position).title;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = LearningChannelFragment.newInstance(data.get(position));

            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

    }



   static class LearningChannelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

        List<LearningItem> items = new ArrayList<>();
        SwipeRefreshLayout mSwipeRefreshLayout;


        Channel mChannel;
        public static LearningChannelFragment newInstance(Channel channel){
            LearningChannelFragment fragment = new LearningChannelFragment();
            Bundle args = new Bundle();
            args.putSerializable("channel",channel);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mChannel = (Channel) getArguments().getSerializable("channel");
        }

        @Override
        public void onRefresh() {
            load();
        }

        boolean isLoadingMore = false;

        private void loadMore(){

            if(isLoadingMore)
                return;

            new GetStudyListReq(getContext(),mChannel.getId(),++page).execute(new Action.Callback<IO.GetStudyListResult>() {

                @Override
                public void progress() {
                    isLoadingMore = true;
                }

                @Override
                public void onError(int errorCode, String msg) {
                    isLoadingMore = false;

                    --page;
                    UIHelper.showToast(getContext(),msg);
                }

                @Override
                public void onCompleted(IO.GetStudyListResult result) {
                    isLoadingMore = false;

                    if(result!=null && result.items!=null){
                        items.clear();
                        items.addAll(result.items);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.common_recycler_list, container, false);

            init(view);

            return view;
        }

        LearningRecyclerViewAdapter mAdapter;
        EmptyController mEmptyController;
        private void init(View view) {

            Context context = view.getContext();

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                    android.R.color.holo_red_light, android.R.color.holo_blue_light);

            mSwipeRefreshLayout.setOnRefreshListener(this);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            View emptyView = view.findViewById(R.id.emptyLayout);

            mEmptyController = new EmptyController(mSwipeRefreshLayout, emptyView, new EmptyController.Callback() {

                @Override
                public void doRefresh() {
                    onRefresh();
                }
            });

            mAdapter = new LearningRecyclerViewAdapter(items, new LoadMoreListener() {
                @Override
                public void onLoadMore() {
                    loadMore();
                }

                @Override
                public boolean hasMore() {
                    return refreshReq != null && page + 1 <= refreshReq.getTotalPage(total,GetStudyListReq.pageSize);
                }
            });

            recyclerView.setAdapter(mAdapter);

            load();
        }

        int page = 1;
        int total;
        GetStudyListReq refreshReq;

        private void load() {
            refreshReq = new GetStudyListReq(getContext(),mChannel.getId(),page = 1);
            refreshReq.execute(new Action.Callback<IO.GetStudyListResult>() {
                @Override
                public void progress() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    mEmptyController.showLoading();
                }

                @Override
                public void onError(int errorCode, String msg) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mEmptyController.showError(errorCode,msg);
                    UIHelper.showToast(getContext(),msg);
                }

                @Override
                public void onCompleted(IO.GetStudyListResult result) {

                    mSwipeRefreshLayout.setRefreshing(false);

                    if(result!=null && result.items!=null){
                        total = result.total;

                        items.clear();
                        items.addAll(result.items);
                        mAdapter.notifyDataSetChanged();

                        mEmptyController.showContent();

                    }else{

                        mEmptyController.showEmpty();
                    }
                }
            });
        }

    }

}
