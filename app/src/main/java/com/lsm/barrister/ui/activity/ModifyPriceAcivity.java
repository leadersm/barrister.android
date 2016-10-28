package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.Account;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.ModifyPriceReq;
import com.lsm.barrister.ui.UIHelper;

/**
 * Created by lvshimin on 16/9/26.
 */
public class ModifyPriceAcivity extends BaseActivity {

    private static final float MIN_APPOINTMENT_PRICE = 50.0f;
    private static final float MIN_IM_PIRCE = 16.0f;

    AQuery aq;

    ModifyPriceReq mModifyReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_price);

        aq = new AQuery(this);

        initActionBar();

        aq.id(R.id.btn_commit).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 提现
                doCommit();
            }
        });

        aq.id(R.id.btn_modify_price_doc).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (aq.id(R.id.btn_modify_price_doc).isChecked()) {
                    aq.id(R.id.btn_commit).visible();
                } else {
                    aq.id(R.id.btn_commit).invisible();

                }
            }
        });

        aq.id(R.id.btn_modify_price_doc).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UIHelper.goDocActivity(ModifyPriceAcivity.this, getString(R.string.doc_modify_price), Constants.DOC_MODIFY_PRICE);
            }
        });


    }

    boolean isLoading = false;

    /**
     * 保存
     */
    protected void doCommit() {
        if (isLoading)
            return;

        Account account = UserHelper.getInstance().getAccount();
        if (account == null) {
            UIHelper.showToast(getApplicationContext(), "获取账户信息失败.");
            return;
        }

        String status = account.getBankCardBindStatus();
        if (status.equals(Account.CARD_STATUS_NOT_BOUND)) {
            UIHelper.showToast(getApplicationContext(), getString(R.string.tip_bind_card));
            return;
        }

        final String imprice = aq.id(R.id.et_im_price).getEditable().toString();
        final String appointmentprice = aq.id(R.id.et_appointment_price).getEditable().toString();

        if (TextUtils.isEmpty(imprice) && TextUtils.isEmpty(appointmentprice)) {
            UIHelper.showToast(getApplicationContext(), "请输入提现金额...");
            return;
        }

        if (!TextUtils.isEmpty(imprice)) {
            float im = Float.parseFloat(imprice);
            if (im < MIN_IM_PIRCE) {
                UIHelper.showToast(getApplicationContext(), getString(R.string.tip_invalid_price));
                return;
            }
        }

        if (!TextUtils.isEmpty(appointmentprice)) {
            float appointment = Float.parseFloat(appointmentprice);
            if (appointment < MIN_APPOINTMENT_PRICE) {
                UIHelper.showToast(getApplicationContext(), getString(R.string.tip_invalid_price));
                return;
            }
        }

        mModifyReq = new ModifyPriceReq(this, imprice, appointmentprice);
        mModifyReq.execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {
                isLoading = true;
                progressDialog.setMessage(getString(R.string.tip_loading));
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoading = false;

                mModifyReq = null;

                progressDialog.dismiss();

                UIHelper.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onCompleted(Boolean t) {
                isLoading = false;

                mModifyReq = null;

                progressDialog.dismiss();

                UIHelper.showToast(getApplicationContext(), getString(R.string.tip_modify_price_success));

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
        getSupportActionBar().setTitle(R.string.title_modify_price);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mModifyReq != null && mModifyReq.isLoading()) {
            mModifyReq.cancel();
            mModifyReq = null;
        }
    }
}
