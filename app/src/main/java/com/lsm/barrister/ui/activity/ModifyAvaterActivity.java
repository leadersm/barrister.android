package com.lsm.barrister.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.androidquery.AQuery;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lsm.barrister.R;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.UpdateUserInfoReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.DLog;
import com.lsm.barrister.utils.TextHandler;

import java.util.HashMap;


/**
 * 修改用户信息页
 */
public class ModifyAvaterActivity extends BaseActivity {

    UpdateUserInfoReq mUpdateUserReq;

    AQuery aq;
    String key;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);
        aq = new AQuery(this);

        initActionBar();

        key = getIntent().getStringExtra(Constants.KEY);
        value = getIntent().getStringExtra(Constants.KEY_USER_INFO_VALUE);

        aq.id(R.id.et_modify_user).text(value).getEditText().setSelection(value==null?0:value.length());

        aq.id(R.id.et_modify_user).getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCount(s);
            }
        });

        init();

        updateCount(aq.id(R.id.et_modify_user).getEditable());
    }

    int maxLength = -1;

    private void updateCount(Editable text) {

        if(maxLength == -1){
            aq.id(R.id.tv_modify_text_count).invisible();
            return;
        }

        if(text==null||text.length()==0){
            aq.id(R.id.tv_modify_text_count).textColor(Color.parseColor("#2bacff")).text(String.valueOf(maxLength)).visible();
        }else if(text.length() < maxLength){
            aq.id(R.id.tv_modify_text_count).textColor(Color.parseColor("#2bacff")).text(String.valueOf(maxLength - text.length())).visible();
        }else{
            aq.id(R.id.tv_modify_text_count).textColor(Color.parseColor("#cb1d33")).text(String.valueOf(maxLength - text.length())).visible();
        }
    }

    private void init() {

        if(key.equals(User.KEY_INTRODUCTION)){//修改签名

            maxLength = Constants.MAX_INTRODUCTION;

            getSupportActionBar().setTitle(R.string.title_modify_signature);

        }else if(key.equals(User.KEY_PHONE)){//手机

            maxLength = -1;

            getSupportActionBar().setTitle(R.string.user_phone);

        }else if(key.equals(User.KEY_EMAIL)){//邮箱

            maxLength = -1;
            getSupportActionBar().setTitle(R.string.user_email);

        }else if(key.equals(User.KEY_AREA)){//地区

            maxLength = -1;
            getSupportActionBar().setTitle(R.string.user_area);

            //定位，显示到et
            doRequestLocation();

        }else if(key.equals(User.KEY_COMPANY)){//律所

            maxLength = -1;
            getSupportActionBar().setTitle(R.string.user_company);

        }else if(key.equals(User.KEY_EMPLOYMENT_YEAR)){
            maxLength = -1;
            getSupportActionBar().setTitle("从业时间");

            aq.id(R.id.et_modify_user).getEditText().setHint("格式：2010-01-01");
            aq.id(R.id.et_modify_user).getEditText().setRawInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        }

        aq.id(R.id.btn_modify_user_save).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                doSave();
            }
        });
    }


    //定位相关的设置信息
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    private int span = 500;

    String province, city, mLocation;

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            if(location!=null){

                aq.id(R.id.et_modify_user).text(location.getProvince()+","+location.getCity());

                DLog.i("Location", "location.getProvince:"+location.getProvince());

                province = location.getProvince();

                city = location.getCity();

                mLocation = location.getLongitude() + "," + location.getLatitude();

            }

        }

    }

    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 0x1001;

    /**
     * 定位
     */
    private void doRequestLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
        }

        // TODO Auto-generated method stub
        mLocationClient = new LocationClient(this);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(span);// 设置发起定位请求的间隔时间为500ms,如果不设置或设置的值小于1000，则只获取一次地址
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    /**
     * 保存
     */
    protected void doSave() {

        final String content = aq.id(R.id.et_modify_user).getEditable().toString();

        if(TextUtils.isEmpty(content)){
            return;
        }

        if(key.equals(User.KEY_NICKNAME) && !TextHandler.validateNickname(content)){//修改昵称
            //验证用户名，支持中英文（包括全角字符）、数字、下划线和减号 （全角及汉字算两位）,长度为4-20位,中文按二位计数
            UIHelper.showToast(this, R.string.tip_invalid_nickname);
            return;
        }

        if(key.equals(User.KEY_PHONE) && !TextHandler.isMobileNum(content)){
            UIHelper.showToast(getApplicationContext(), R.string.tip_invalid_phone);
            return;
        }else if(key.equals(User.KEY_EMAIL) && !TextHandler.isEmail(content)){
            UIHelper.showToast(getApplicationContext(), R.string.tip_invalid_email);
            return;
        }else if(key.equals(User.KEY_INTRODUCTION) && content.length() > Constants.MAX_INTRODUCTION){
            UIHelper.showToast(getApplicationContext(), R.string.tip_invalid_indtroduction);
            return;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(key, content);

        if(key.equals(User.KEY_AREA)){

            if(!TextUtils.isEmpty(mLocation))
                params.put("location",mLocation);
            if(!TextUtils.isEmpty(city))
                params.put("city",city);
            if(!TextUtils.isEmpty(province))
                params.put("state",province);
        }

        mUpdateUserReq = new UpdateUserInfoReq(this, params);
        mUpdateUserReq.execute(new Action.Callback<User>() {

            @Override
            public void progress() {
                progressDialog.setMessage(getString(R.string.tip_loading));
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                mUpdateUserReq = null;

                progressDialog.dismiss();

                new AlertDialog.Builder(ModifyAvaterActivity.this)
                        .setTitle(R.string.title_dialog_upload_failed)
                        .setMessage(getString(R.string.msg_dialog_upload_failed))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doSave();
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
            }

            @Override
            public void onCompleted(User t) {
                mUpdateUserReq = null;

                progressDialog.dismiss();

                UIHelper.showToast(getApplicationContext(), R.string.tip_modify_user_success);

                //修改成功
                Intent intent = new Intent(ModifyAvaterActivity.this,AvatarDetailActivity.class);
                intent.putExtra(Constants.KEY, key);
                intent.putExtra(Constants.KEY_CONTENT, content);
                setResult(RESULT_OK,intent);

                finish();

            }
        });

    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUpdateUserReq!=null && mUpdateUserReq.isLoading()){
            mUpdateUserReq.cancel();
            mUpdateUserReq = null;
        }

        if(mLocationClient!=null)
            mLocationClient.stop();
    }
}
