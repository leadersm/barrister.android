package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 学习中心列表接口
 * <p/>
 * getStudyList
 *   提交方式：get
 *   参数:page,pageSize
 *   返回值：resultCode，resultMsg , List<Item> items；total
 *   备注：无
 */
public class GetStudyListReq extends Action{
    int page ;
    public static int pageSize = 20;
    String id;

    public GetStudyListReq(Context context,String id, int page) {
        super(context);
        this.id = id;
        this.page = page;

        params("channelId",id);
        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));

//        addUserParams();
    }

    @Override
    public String getName() {
        return GetStudyListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_STUDY_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetStudyListResult result = getFromGson(json, new TypeToken<IO.GetStudyListResult>() {});//Test.getStudyListResult();//

        if(result!=null){

            if(result.resultCode == 200){

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
