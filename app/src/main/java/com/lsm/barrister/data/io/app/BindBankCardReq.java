package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

import java.io.File;


/**
 * Created by lvshimin on 16/5/8.
 * 绑定银行卡接口
 * <p/>
 * bindBankCard
 *   提交方式：post
 *   参数:userId,verifyCode,cardNum（卡号）,cardholderName（持卡人姓名），bankName(银行名称)，bankAddress(开户行),file
 *   返回值：resultCode，resultMsg  ；
 *   备注：是否提供更改？？TBD
 */
public class BindBankCardReq extends Action {

    String cardNum;
    String cardholderName;
    String cardholderPhone;
    String bankName;
    String cardType;
    String bankAddress;
    String logoName;
    File file;

//    cardNum, cardholderName,bankType, bankName, bankAddress
    public BindBankCardReq(Context context, String cardNum, String cardholderName, String cardholderPhone, String cardType, String bankName, String bankAddress, String logoName, File file) {
        super(context);
        this.cardNum = cardNum;
        this.cardholderName = cardholderName;
        this.cardholderPhone = cardholderPhone;
        this.cardType = cardType;
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.logoName = logoName;
        this.file = file;

        params("cardNum",cardNum);
        params("cardholderName",cardholderName);
        params("bankPhone",cardholderPhone);
        params("bankName",bankName);
        params("bankAddress",bankAddress);
        params("cardType",cardType);
        params("logoName",logoName);

        if(file!=null && file.exists()){
            addFile("file",file);
        }

        User user = AppConfig.getUser(context);

        params("userId",user.getId());
        params("verifyCode",user.getVerifyCode());

    }

    @Override
    public String getName() {
        return BindBankCardReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_BIND_BANK_CARD;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        final IO.BindBankcardResult result = getFromGson(json,new TypeToken<IO.BindBankcardResult>(){});//Test.getBindcardResult();//

        if(result!=null && result.resultCode == 200){
            onSafeCompleted(result);
        }
        return result;
    }

    @Override
    public int method() {
        return POST;
    }
}
