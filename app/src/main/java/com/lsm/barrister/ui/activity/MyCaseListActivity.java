package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.Case;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetMyCaseListReq;
import com.lsm.barrister.data.io.app.GetStudyListReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.adapter.CaseListAdapter;
import com.lsm.barrister.ui.adapter.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 个人中心：我的案源列表
 */
public class MyCaseListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    List<Case> items = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_list);
        setupToolbar();

        init();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    CaseListAdapter mAdapter;
    private void init() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CaseListAdapter(items, new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public boolean hasMore() {
                return refreshReq != null && page + 1 <= refreshReq.getTotalPage(total, GetStudyListReq.pageSize);
            }
        },false);

        recyclerView.setAdapter(mAdapter);

        load();
    }

    int page = 1;
    int total;
    GetMyCaseListReq refreshReq;
    private void load() {
        refreshReq = new GetMyCaseListReq(getApplicationContext(),page = 1);
        refreshReq.execute(new Action.Callback<IO.GetCaseListResult>() {
            @Override
            public void progress() {
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onError(int errorCode, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(IO.GetCaseListResult result) {
                mSwipeRefreshLayout.setRefreshing(false);
                if(result!=null && result.cases!=null){
                    total = result.total;

                    items.clear();
                    items.addAll(result.cases);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    boolean isLoadingMore = false;
    private void loadMore(){

        if(isLoadingMore)
            return;

        new GetMyCaseListReq(this,++page).execute(new Action.Callback<IO.GetCaseListResult>() {

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
            public void onCompleted(IO.GetCaseListResult result) {
                isLoadingMore = false;

                if(result!=null && result.cases!=null){
                    items.addAll(result.cases);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    
    @Override
    public void onRefresh() {
        load();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_case_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add_case){
            UIHelper.goUploadCaseActivity(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
