package com.lsm.barrister.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.data.entity.Account;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.BindBankCardReq;
import com.lsm.barrister.data.io.app.GetBankNameReq;
import com.lsm.barrister.ui.UIHelper;

import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;


/**
 * 设置银行卡页
 */
public class SetBankCardActivity extends BaseActivity {

    public static final String KEY = "bankcard";
    private static final int MY_SCAN_REQUEST_CODE = 0x1001;

    Account.BankCard bankCard;
    AQuery aq;
    List<AppConfig.Bank> banks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bankcard);
        setupToolbar();
        aq = new AQuery(this);
        aq.id(R.id.btn_setbank_image).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScanCard();
            }


        });
        aq.id(R.id.btn_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });

        banks = AppConfig.getInstance().getBanks();
    }






    private void doScanCard() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    private void doCommit() {

        String cardNum = aq.id(R.id.et_bankcard_num).getEditable().toString();
        String cardholderName = aq.id(R.id.et_bankcard_holder_name).getEditable().toString();
        String bankName = aq.id(R.id.et_bankcard_bankname).getEditable().toString();
        String bankType = aq.id(R.id.et_bankcard_type).getEditable().toString();
        String bankAddress = aq.id(R.id.et_bankcard_bankaddress).getEditable().toString();
        String phone = aq.id(R.id.et_bankcard_phone).getEditable().toString();


        if(TextUtils.isEmpty(cardNum)
                ||TextUtils.isEmpty(cardholderName)
                ||TextUtils.isEmpty(bankName)
                ||TextUtils.isEmpty(bankAddress)
                ||TextUtils.isEmpty(bankType)
                ||TextUtils.isEmpty(phone)){

            UIHelper.showToast(getApplicationContext(),"请填写完整信息");

            return;
        }

        String logoName = "";

        for(AppConfig.Bank bank :banks){
            if(bankName.startsWith(bank.name)){
                logoName = bank.icon;
                break;
            }
        }

        new BindBankCardReq(this, cardNum, cardholderName,phone,bankType, bankName, bankAddress,logoName).execute(new Action.Callback<IO.BindBankcardResult>() {
            @Override
            public void progress() {
                progressDialog.setMessage(getString(R.string.tip_loading));
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                progressDialog.dismiss();
                UIHelper.showToast(getApplicationContext(),"上传失败："+msg);
            }

            @Override
            public void onCompleted(IO.BindBankcardResult result) {
                progressDialog.dismiss();
                if(result!=null){
                    finish();
                }

            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.mybankcard);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;

            String bankcardNum;
            String expire;//

            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {

                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                bankcardNum = scanResult.getFormattedCardNumber();

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n" +"scanResult.getFormattedCardNumber()："+scanResult.getFormattedCardNumber();
                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }

                aq.id(R.id.et_bankcard_num).text(bankcardNum);

                new GetBankNameReq(SetBankCardActivity.this,bankcardNum.replaceAll(" ","")).execute(new Action.Callback<IO.GetBankInfoResult>() {
                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                    }

                    @Override
                    public void onCompleted(IO.GetBankInfoResult result) {

                        if(result!=null && result.data!=null){

                            System.out.println(result.toString());

                            aq.id(R.id.et_bankcard_bankname).text(result.data.bankname);
                            aq.id(R.id.et_bankcard_type).text(result.data.cardtype);

                        }


                    }
                });

//                String name = BankCardBin.getNameOfBank(bankcardNum.replaceAll(" ",""), 0);//获取银行卡的信息


            }else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);

            System.out.println("===>"+resultDisplayStr);
        }
        // else handle other activity results
    }
}
