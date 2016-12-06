package com.lsm.barrister.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.Account;
import com.lsm.barrister.data.entity.Case;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.BuyCaseReq;
import com.lsm.barrister.data.io.app.GiveBackCaseReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.IntentUtil;
import com.lsm.barrister.utils.OrderUtils;
import com.lsm.barrister.utils.TextHandler;

import java.util.Locale;

/**
 * 案源详情页
 * 接单，退回，更新进度
 */
public class CaseDetailActivity extends BaseActivity {

    private static final String TAG = CaseDetailActivity.class.getSimpleName();

    AQuery aq;

    Case item;

//    private static final float DEFAULT_PRICE = 16.0f;//默认购买金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        setupToolbar();

        item = (Case) getIntent().getSerializableExtra("item");

        aq = new AQuery(this);

        bind();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    boolean canMakeCall = false;

    /**
     * 绑定数据
     */
    private void bind() {

        String statusStr = OrderUtils.getCaseStatusString(item.getStatus());
        int statusColor = OrderUtils.getCaseStatusColor(item.getStatus());

        //status
        aq.id(R.id.tv_case_status).text(statusStr).textColor(statusColor);

        //num
        aq.id(R.id.tv_case_num).text("案源号："+(TextUtils.isEmpty(item.getId())?"":item.getId()));

        //案件类型
        aq.id(R.id.tv_case_case_type).gone();

        //时间
        aq.id(R.id.tv_case_time).text("发布时间："+item.getAddTime());

        //金额 默认50元代理费
        aq.id(R.id.tv_case_payment).text(String.format(Locale.CHINA,"%.2f",item.getPrice())+"￥");

        aq.id(R.id.tv_tip_case_buy_money).text(String.format(Locale.CHINA,getString(R.string.fmt_bug_case),item.getPrice()));

        //备注
        aq.id(R.id.tv_case_remark).text("备注："+ (TextUtils.isEmpty(item.getCaseInfo())?"":item.getCaseInfo()));


        String status = item.getStatus();

        if(status.equals(Case.STATUS_0_INIT)){
            //str = "未发布";
            //隐藏：接单按钮，用户信息，更新按钮，退回按钮
            aq.id(R.id.layout_case_cinfo).gone();
            aq.id(R.id.layout_case_buy).gone();
            aq.id(R.id.layout_case_giveback).gone();
            aq.id(R.id.layout_case_update_progress).gone();

            canMakeCall = false;

        }else if(status.equals(Case.STATUS_1_PUBLISHED)){
            //str = "已发布";可以接单
            aq.id(R.id.layout_case_cinfo).gone();
            aq.id(R.id.layout_case_giveback).gone();
            aq.id(R.id.layout_case_update_progress).gone();
            aq.id(R.id.layout_case_buy).visible();

            canMakeCall = false;

        }else if(status.equals(Case.STATUS_2_WAIT_UPDATE)){
            //str = "待更新";可以更新，退回操作
            aq.id(R.id.layout_case_cinfo).gone();
            aq.id(R.id.layout_case_giveback).visible();

            aq.id(R.id.layout_case_update_progress).visible();
            aq.id(R.id.layout_case_buy).gone();

            canMakeCall = true;

        }else if(status.equals(Case.STATUS_3_WAIT_CLEARING)){
            //str = "待结算";
            aq.id(R.id.layout_case_cinfo).gone();
            aq.id(R.id.layout_case_giveback).gone();
            aq.id(R.id.layout_case_update_progress).visible();
            //更新以后，可以再次更新，方式填写错误
            aq.id(R.id.tv_tip_update).text(getString(R.string.tip_reupdate_case));
            aq.id(R.id.layout_case_buy).gone();
            canMakeCall = true;
        }else if(status.equals(Case.STATUS_4_WAIT_CLEARED)){
            //str = "已结算";
            aq.id(R.id.layout_case_cinfo).gone();
            aq.id(R.id.layout_case_giveback).gone();
            aq.id(R.id.layout_case_update_progress).gone();
            aq.id(R.id.layout_case_buy).gone();
            canMakeCall = true;
        }

        aq.id(R.id.btn_call_make).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canMakeCall){
                    IntentUtil.makeCall(CaseDetailActivity.this,item.getContactPhone());
                }
            }
        });

        String phone;

        if(canMakeCall){
            phone = (TextUtils.isEmpty(item.getContact())?"":item.getContact());
        }else {
            phone = (TextUtils.isEmpty(item.getContact())?"": TextHandler.getHidePhone(item.getContact()));
        }

        //联系人
        aq.id(R.id.tv_case_cname).text("联系人：" + phone);
        //手机号码 //联系电话
        // String hidePhone = TextHandler.getHidePhone();
        aq.id(R.id.tv_case_cphone).text(item.getContactPhone());

        aq.id(R.id.btn_case_pay).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //支付
                tryToBuy();
            }
        });

        aq.id(R.id.btn_case_update_progress).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //更新
                UIHelper.goUpdateCaseActivity(CaseDetailActivity.this,item);
            }
        });

        aq.id(R.id.btn_case_giveback).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //退回
                showGivebackDialog();
            }
        });
    }

    /**
     * 显示退回案源对话框
     */
    private void showGivebackDialog() {

        new AlertDialog.Builder(this).setTitle(R.string.tip)
                .setMessage("您是否要退回案源？").setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doGivebackCase();
            }
        }).create().show();

    }

    /**
     * 退回案源
     */
    private void doGivebackCase() {
        new GiveBackCaseReq(this,item.getId()).execute(new Action.Callback<Boolean>() {
            @Override
            public void progress() {
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                progressDialog.dismiss();
                UIHelper.showToast(getApplicationContext(),"操作失败："+msg);
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                progressDialog.dismiss();
                UIHelper.showToast(getApplicationContext(),"操作成功");
                finish();
            }
        });
    }


    /**
     * 支付代理费
     */
    private void doBuyCase() {

        Account account = UserHelper.getInstance().getAccount();

        if(account==null){
            UIHelper.showToast(getApplicationContext(),"获取账户信息失败.");
            return;
        }

        float remainingBalance =  account.getRemainingBalance();
        if(remainingBalance<item.getPrice()){
            UIHelper.showToast(getApplicationContext(),"您的账户余额不足，请充值");
            return;
        }

        new BuyCaseReq(this,item.getId()).execute(new Action.Callback<Boolean>() {
            @Override
            public void progress() {
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();

                aq.id(R.id.btn_case_pay).enabled(false);
            }

            @Override
            public void onError(int errorCode, String msg) {

                progressDialog.dismiss();

                aq.id(R.id.btn_case_pay).enabled(true);

                UIHelper.showToast(getApplicationContext(),"支付失败:"+msg);

            }

            @Override
            public void onCompleted(Boolean b) {
                progressDialog.dismiss();

                UIHelper.showToast(getApplicationContext(),"支付成功");
                item.setStatus(Case.STATUS_2_WAIT_UPDATE);
                bind();
            }
        });
    }

    /**
     * 确定要代理，需要支付%.2f元代理费
     */
    private void tryToBuy() {

        //10单

        String msg = String.format(Locale.CHINA,getString(R.string.fmt_bug_case),item.getPrice());

        new AlertDialog.Builder(this).setTitle(R.string.tip).setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                doBuyCase();

            }}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();


    }



    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_case_detail);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
