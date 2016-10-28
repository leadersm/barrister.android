package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.LawApp;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetLawAppListReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.adapter.LawAppAdapter;
import com.lsm.barrister.utils.DLog;

import java.util.ArrayList;
import java.util.List;


/**
 * 应用大全列表
 */
public class LawAppListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = LawApp.class.getSimpleName();

    List<LawApp> items = new ArrayList<>();

    SwipeRefreshLayout mSwipeRefreshLayout;

    LawAppAdapter mLawAppListAdapter;
    GridLayoutManager mLawAppListLayoutManager;
    RecyclerView mLawAppListView;


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
        getSupportActionBar().setTitle(R.string.lawapplist);
    }

    private void init() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLawAppListView = (RecyclerView) findViewById(R.id.recyclerview);
        mLawAppListLayoutManager = new GridLayoutManager(this, 3);
        mLawAppListAdapter = new LawAppAdapter(items);
        mLawAppListView.setItemAnimator(new DefaultItemAnimator());
        mLawAppListView.setLayoutManager(mLawAppListLayoutManager);
        mLawAppListView.setAdapter(mLawAppListAdapter);

        load();
    }

    private void load() {
        new GetLawAppListReq(this).execute(new Action.Callback<IO.GetLawAppListResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(IO.GetLawAppListResult result) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (result.legalList != null) {

                    items.clear();
                    items.addAll(result.legalList);

                    mLawAppListAdapter.notifyDataSetChanged();
                }else {
                    DLog.e(TAG,"lawAppList is null");
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        load();
    }
}
