package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.Message;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetMyMsgsReq;
import com.lsm.barrister.data.io.app.GetStudyListReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.adapter.LoadMoreListener;
import com.lsm.barrister.ui.adapter.MsgListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表页
 * 1.收到预约
 * 2.评价
 * 3.到账
 * 4.提现记录
 */
public class MsgsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    GetMyMsgsReq refreshReq;

    SwipeRefreshLayout mSwipeRefreshLayout;

    MsgListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        setupToolbar();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MsgListAdapter(data, new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {
                return refreshReq != null && page + 1 <= refreshReq.getTotalPage(total, GetStudyListReq.pageSize);
            }
        });


        recyclerView.setAdapter(mAdapter);

        load();
    }


    boolean isLoadingMore = false;
    private void loadMore(){

        if(isLoadingMore)
            return;

        new GetMyMsgsReq(this,++page).execute(new Action.Callback<IO.GetMyMsgsResult>() {

            @Override
            public void progress() {
                isLoadingMore = true;
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoadingMore = false;

                --page;
                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(IO.GetMyMsgsResult result) {
                isLoadingMore = false;

                if(result!=null && result.msgs!=null){
                    data.clear();
                    data.addAll(result.msgs);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    int page = 1;
    int total;
    List<Message> data = new ArrayList<>();

    private void load() {
        refreshReq = new GetMyMsgsReq(this,page = 1);
        refreshReq.execute(new Action.Callback<IO.GetMyMsgsResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCompleted(IO.GetMyMsgsResult result) {

                mSwipeRefreshLayout.setRefreshing(false);

                List<Message> msgs = result.msgs;
                if(msgs!=null){
                    data.clear();
                    data.addAll(msgs);
                    total = result.total;

                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_msg);
    }

    @Override
    public void onRefresh() {
        load();
    }

}
