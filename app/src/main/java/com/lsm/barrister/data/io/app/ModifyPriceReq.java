package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/9/26.
 */
public class ModifyPriceReq extends Action{

    String imPrice;
    String appointmentPrice;

    public ModifyPriceReq(Context context, String imPrice, String appointmentPrice) {
        super(context);
        this.imPrice = imPrice;
        this.appointmentPrice = appointmentPrice;

        params("priceIM",imPrice);
        params("priceAppointment",appointmentPrice);

        addUserParams();
    }

    @Override
    public String getName() {
        return ModifyPriceReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_MODIFY_PRICE;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        CommonResult result = parseCommonResult(json);
        return result;
    }

    @Override
    public int method() {
        return POST;
    }
}
