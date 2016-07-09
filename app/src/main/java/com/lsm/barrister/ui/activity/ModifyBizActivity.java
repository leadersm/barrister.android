package com.lsm.barrister.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.BusinessArea;
import com.lsm.barrister.data.entity.BusinessType;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetBizTypeAreaListReq;
import com.lsm.barrister.data.io.app.UpdateUserInfoReq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消息列表页
 * 1.收到预约
 * 2.评价
 * 3.到账
 * 4.提现记录
 */
public class ModifyBizActivity extends BaseActivity {

    AQuery aq;
    ArrayAdapter<BusinessArea> mBizAreaAdapter;
    ArrayAdapter<BusinessType> mBizTypeAdapter;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_biz);
        setupToolbar();

        user = AppConfig.getUser(this);

        aq = new AQuery(this);

        mBizAreaAdapter = new ArrayAdapter<BusinessArea>(this, R.layout.item_biz_modity, R.id.tv_modify_biz, bizAreas){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final CheckBox cb = (CheckBox) super.getView(position, convertView, parent);
                cb.setChecked(getItem(position).isChecked());
                cb.setText(getItem(position).getName());
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bizAreas.get(position).setChecked(cb.isChecked());
                    }
                });
                return cb;
            }
        };

        mBizTypeAdapter = new ArrayAdapter<BusinessType>(this, R.layout.item_biz_modity, R.id.tv_modify_biz, bizTypes){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final CheckBox cb = (CheckBox) super.getView(position, convertView, parent);
                cb.setChecked(getItem(position).isChecked());
                cb.setText(getItem(position).getName());
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bizTypes.get(position).setChecked(cb.isChecked());
                    }
                });
                return cb;
            }
        };

        aq.id(R.id.gridview_biz_areas).adapter(mBizAreaAdapter).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view;
                bizAreas.get(position).setChecked(cb.isChecked());
            }
        });

        aq.id(R.id.gridview_biz_types).adapter(mBizTypeAdapter).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view;
                bizTypes.get(position).setChecked(cb.isChecked());
            }
        });

        aq.id(R.id.btn_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });

        load();

    }

    /**
     * 提交
     */
    private void doCommit() {
        HashMap<String, String> params = new HashMap<>();

        StringBuffer sb = new StringBuffer();
        for (BusinessArea temp : bizAreas) {
            if (temp.isChecked()) {
                sb.append(temp.getId() + ",");
            }
        }

        params.put("bizArea", sb.toString());

        sb = new StringBuffer();
        for (BusinessType temp : bizTypes) {
            if (temp.isChecked()) {
                sb.append(temp.getId() + ",");
            }
        }

        params.put("bizType", sb.toString());

        new UpdateUserInfoReq(this, params).execute(new Action.Callback<User>() {

            @Override
            public void progress() {
                progressDialog.setMessage(getString(R.string.tip_loading));
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                progressDialog.dismiss();

                progressDialog.dismiss();

                new AlertDialog.Builder(ModifyBizActivity.this)
                        .setTitle(R.string.title_dialog_upload_failed)
                        .setMessage(getString(R.string.msg_dialog_upload_failed))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doCommit();
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
            }

            @Override
            public void onCompleted(User user) {

                progressDialog.dismiss();

                AppConfig.setUser(getApplicationContext(),user);
                UserHelper.getInstance().notifyUpdateUser();

                finish();
            }
        });
    }


    List<BusinessArea> bizAreas = new ArrayList<>();
    List<BusinessType> bizTypes = new ArrayList<>();

    private void load() {

        new GetBizTypeAreaListReq(this).execute(new Action.Callback<IO.GetBizTypeAreaListResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

            }

            @Override
            public void onCompleted(IO.GetBizTypeAreaListResult result) {

                List<BusinessArea> bizAreas = result.bizAreas;
                List<BusinessType> bizTypes = result.bizTypes;

                if (bizAreas != null) {
                    ModifyBizActivity.this.bizAreas.clear();
                    ModifyBizActivity.this.bizAreas.addAll(bizAreas);
                }


                if(bizTypes != null){
                    ModifyBizActivity.this.bizTypes.clear();
                    ModifyBizActivity.this.bizTypes.addAll(bizTypes);
                }

                List<BusinessArea> userBizAreas = user.getBizAreas();
                List<BusinessType> userBizTypes = user.getBizTypes();

                if (userBizAreas != null)
                    flag_area:for (BusinessArea ubiz : userBizAreas) {
                        for (BusinessArea temp : ModifyBizActivity.this.bizAreas) {
                            if (ubiz.getId().equals(temp.getId())) {
                                temp.setChecked(true);
                                continue flag_area;
                            }
                        }
                    }

                if (userBizTypes != null)
                    flag_type:for (BusinessType ubiz : userBizTypes) {
                        for (BusinessType temp : ModifyBizActivity.this.bizTypes) {
                            if (ubiz.getId().equals(temp.getId())) {
                                temp.setChecked(true);
                                continue flag_type;
                            }
                        }
                    }


                mBizAreaAdapter.notifyDataSetChanged();
                mBizTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_msg);
    }


}
