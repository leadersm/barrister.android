package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.Account;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.DLog;

import java.util.List;


/**
 * 我的银行卡
 */
public class MyBankCardActivity extends BaseActivity implements UserHelper.OnAccountUpdateListener{

    public static final String KEY = "bankcard";
    private static final String TAG = MyBankCardActivity.class.getSimpleName();

    Account.BankCard bankCard;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bankcard);
        setupToolbar();
        aq = new AQuery(this);

        bankCard = (Account.BankCard) getIntent().getSerializableExtra(KEY);

        setupView();

    }

    private void setupView() {

        List<AppConfig.Bank> banks = AppConfig.getInstance().getBanks();
        if (banks == null || banks.isEmpty()) {
            AppConfig.getInstance().initBankIcons();
        }

        if (bankCard != null) {
            DLog.d(TAG, bankCard.toString());

            aq.id(R.id.layout_bankcard_detail).visible();
            aq.id(R.id.btn_bankcard_add).gone();
            aq.id(R.id.tv_bankcard_bankname).text(bankCard.getBankCardName());
            aq.id(R.id.tv_bankcard_num).text(bankCard.getBankCardNum());
            aq.id(R.id.tv_bankcard_phone).text(bankCard.getBankPhone());
            aq.id(R.id.image_bankcard_logo).image(bankCard.getLogoName(), true, true);

            SimpleDraweeView logoView = (SimpleDraweeView) findViewById(R.id.image_bankcard_logo);

            if (!TextUtils.isEmpty(bankCard.getLogoName())) {

                int logoId = getResources().getIdentifier(bankCard.getLogoName().replace(".png",""),"drawable",getPackageName());
                aq.id(R.id.image_bankcard_logo).image(logoId);

            } else {

                for (AppConfig.Bank bank : banks) {
                    if (bankCard.getBankCardName().startsWith(bank.name)) {

                        int logoId = getResources().getIdentifier(bank.icon.replace(".png",""),"drawable",getPackageName());
                        aq.id(R.id.image_bankcard_logo).image(logoId);

                        break;
                    }
                }
            }


        } else {
            aq.id(R.id.layout_bankcard_detail).gone();
            aq.id(R.id.btn_bankcard_add).visible().clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goSetBankcardActivity(MyBankCardActivity.this);
                }
            });
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.mybankcard);
    }


    @Override
    public void onUpdateAccount(Account account) {
        bankCard = account.getBankCard();
        setupView();
    }
}
