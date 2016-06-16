package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 获取消息列表
 * 提交方式：post
 *   参数:userId,verifyCode,page,pageSize
 *   返回值：resultCode，resultMsg  ,List<Message> msgs ,total；
 *   备注：TDB，例如审核失败了的消息
 */
public class GetMyMsgsReq extends Action{

    int page;
    int pageSize = 20;

    public GetMyMsgsReq(Context context, int page) {
        super(context);
        this.page = page;

        params("page",page+"");
        params("pageSize",String.valueOf(pageSize));

        addUserParams();
    }

    @Override
    public String getName() {
        return GetMyMsgsReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_MY_MSGS;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        IO.GetMyMsgsResult result = Test.getMyMsgsResult();//getFromGson(json, new TypeToken<IO.GetMyMsgsResult>() {});

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
