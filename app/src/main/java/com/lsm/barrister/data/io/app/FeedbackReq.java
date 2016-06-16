package com.lsm.barrister.data.io.app;

import android.content.Context;

import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 反馈接口
 * 提交方式：post
 *   参数:userId,verifyCode,content (内容),contact(联系方式)
 *   返回值：resultCode，resultMsg  ,List<Message> msgs；
 *   备注：如果没登录，不能反馈（跳转登陆界面）
 */
public class FeedbackReq extends Action{

    String content;
    String contact;

    public FeedbackReq(Context context, String content, String contact) {
        super(context);
        this.content = content;
        this.contact = contact;

        params("content",content);
        params("contact",contact);

        addUserParams();

    }

    @Override
    public String getName() {
        return FeedbackReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_FEEDBACK;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        return parseCommonResult(json);
    }

    @Override
    public int method() {
        return POST;
    }
}
