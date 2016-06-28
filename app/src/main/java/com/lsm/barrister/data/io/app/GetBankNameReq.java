package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/26.
 */
public class GetBankNameReq extends Action{
    String num;
    String apiKey = "732bbaf70af9fc28eaad5c5d28991262";

    public GetBankNameReq(Context context, String num) {
        super(context);
        this.num = num;

        params("cardnum",num);

        headers.put("apikey",apiKey);

    }

    @Override
    public String getName() {
        return GetBankNameReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_BANK_INFO;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        IO.GetBankInfoResult result = getFromGson(json,new TypeToken<IO.GetBankInfoResult>(){});//Test.getBankInfoResult();//
        if(result!=null){

            if(result.data!=null){
                onSafeCompleted(result);
            }

            return result;

        }

        return null;
    }

    @Override
    public int method() {
        return GET;
    }
}
