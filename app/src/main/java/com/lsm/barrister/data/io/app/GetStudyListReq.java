package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

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

    public GetStudyListReq(Context context, int page) {
        super(context);
        this.page = page;

        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));

        addUserParams();
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

        IO.GetStudyListResult result = Test.getStudyListResult();//getFromGson(json, new TypeToken<IO.GetStudyListResult>() {});

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(result);

            }

            return result;

        }else{
            throw new Exception("解析错误");
        }
    }

    @Override
    public int method() {
        return POST;
    }
}
