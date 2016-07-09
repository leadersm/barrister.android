package com.lsm.barrister.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.app.VersionHelper;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.LogoutReq;
import com.lsm.barrister.ui.UIHelper;


/**
 * 设置页
 */
public class SettingsActivity extends BaseActivity {


    AQuery aq;

    boolean isLogouting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToolbar();
        aq = new AQuery(this);
        aq.id(R.id.btn_settings_about).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goDocActivity(SettingsActivity.this,"关于我们", Constants.DOC_ABOUT);
            }
        });
        aq.id(R.id.btn_settings_feedback).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,FeedbackActivity.class);
                startActivity(intent);
            }
        });
        aq.id(R.id.btn_settings_help).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goDocActivity(SettingsActivity.this,"帮助", Constants.DOC_ABOUT);
            }
        });
        aq.id(R.id.btn_settings_update).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionHelper.instance().check(SettingsActivity.this,true);
            }
        });

        //注销
        User user = AppConfig.getUser(this);
        if(user==null)
            aq.id(R.id.btn_settings_logout).gone();
        else
            aq.id(R.id.btn_settings_logout).visible();

        aq.id(R.id.btn_settings_logout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isLogouting){
                    return ;
                }

                //跳转登录页
                UIHelper.goLoginActivity(SettingsActivity.this);

                new LogoutReq(SettingsActivity.this).execute(new Action.Callback<Boolean>() {

                    @Override
                    public void progress() {
                        isLogouting = true;
                    }

                    @Override
                    public void onError(int errorCode, String msg) {
                        isLogouting = false;
                        //登出
//                        UserHelper.getInstance().logout(getApplicationContext());

                        // TODO 清除用户数据
                        AppConfig.removeUser(getApplicationContext());

                        finish();

                    }

                    @Override
                    public void onCompleted(Boolean aBoolean) {
                        isLogouting = false;

                        //登出
//                        UserHelper.getInstance().logout(getApplicationContext());

                        // TODO 清除用户数据
                        AppConfig.removeUser(getApplicationContext());

                        finish();

                    }
                });

            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.settings);
    }


}
