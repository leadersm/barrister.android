package com.lsm.barrister.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AudioHelper;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.data.entity.OrderDetail;
import com.lsm.barrister.data.entity.OrderItem;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.AgreeRequestCancelReq;
import com.lsm.barrister.data.io.app.DisagreeRequestCancelReq;
import com.lsm.barrister.data.io.app.FinishOrderReq;
import com.lsm.barrister.data.io.app.GetOrderDetailReq;
import com.lsm.barrister.data.io.app.MakeCallReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.adapter.CallHistoryAdapter;
import com.lsm.barrister.utils.DateFormatUtils;
import com.lsm.barrister.utils.OrderUtils;
import com.lsm.barrister.utils.TextHandler;

import java.util.Date;
import java.util.Locale;

/**
 * 订单详情页
 */
public class OrderDetailActivity extends BaseActivity {

    private static final String TAG = OrderDetailActivity.class.getSimpleName();
    AQuery aq;

    GetOrderDetailReq mGetOrderDetailReq;
    String mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setupToolbar();

        mOrderId = getIntent().getStringExtra("id");

        aq = new AQuery(this);
        aq.id(R.id.btn_call_make).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OrderDetailActivity.this)
                        .setTitle(R.string.tip)
                        .setMessage("确定要拨打电话吗？")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doMakeCall();
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();

        loadDetail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add_summary){

            if(mDetail==null)
                return false;

            //订单完成或订单已取消可以添加小结
            if(mDetail.getStatus().equals(OrderDetail.STATUS_DONE) || mDetail.getStatus().equals(OrderDetail.STATUS_CANCELED)){
                UIHelper.goAddOrderSummaryActivity(this,mOrderId);
            }else{
                UIHelper.showToast(getApplicationContext(),"订单还没有结束。");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_detail,menu);
        return true;
    }

    OrderDetail mDetail;

    private void loadDetail() {
        mGetOrderDetailReq = new GetOrderDetailReq(this,mOrderId);

        mGetOrderDetailReq.execute(new Action.Callback<IO.GetOrderDetailResult>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

            }

            @Override
            public void onCompleted(IO.GetOrderDetailResult result) {
                mDetail = result.orderDetail;
                bind();
            }
        });
    }

    /**
     * 绑定数据
     */
    private void bind() {

        if(mDetail==null){
            System.err.println("加载订单详情出错");
            return;
        }

        String status = OrderUtils.getStatusString(mDetail.getStatus());
        int statusColor = OrderUtils.getStatusColor(mDetail.getStatus());

        //status
        aq.id(R.id.tv_order_status).text(status).textColor(statusColor);

        //num
        aq.id(R.id.tv_order_num).text("订单号："+(TextUtils.isEmpty(mDetail.getOrderNo())?"":mDetail.getOrderNo()));

        //案件类型
        aq.id(R.id.tv_order_case_type).text("案例类型:"+(TextUtils.isEmpty(mDetail.getCaseType())?"未知案例类型":mDetail.getCaseType()));

        //时间
        String time = null;
        if(mDetail.getType().equals(OrderItem.TYPE_APPOINTMENT)){
            time = mDetail.getStartTime() + "~" + mDetail.getEndTime();
        }else{
            time = mDetail.getPayTime();
        }
        aq.id(R.id.tv_order_time).text(time);

        //金额
        aq.id(R.id.tv_order_payment).text(String.format(Locale.CHINA,"%.1f",mDetail.getPaymentAmount()));

        //备注
        aq.id(R.id.tv_order_remark).text("备注："+ (TextUtils.isEmpty(mDetail.getRemarks())?"":mDetail.getRemarks()));

        //小结
        if(TextUtils.isEmpty(mDetail.getLawFeedback())){
            aq.id(R.id.tv_order_summary).gone();
        }else {
            aq.id(R.id.tv_order_summary).text("律师总结："+mDetail.getLawFeedback()).visible();
        }

        //评论
        if(TextUtils.isEmpty(mDetail.getComment())){
            aq.id(R.id.tv_order_comment).gone();
        }else {
            aq.id(R.id.tv_order_comment).text("用户评论："+mDetail.getComment()).visible();
        }

        //用户昵称
        String hidePhone = TextHandler.getHidePhone(mDetail.getCustomerPhone());
        //昵称
        aq.id(R.id.tv_order_nickname).text(TextUtils.isEmpty(mDetail.getCustomerNickname())?hidePhone:mDetail.getCustomerNickname());
        //手机号码
        aq.id(R.id.tv_order_phone_number).text("点击拨打电话");//.text(hidePhone);
        //头像
        SimpleDraweeView sdv = (SimpleDraweeView) aq.id(R.id.image_order_custom_icon).getView();
        if(!TextUtils.isEmpty(mDetail.getCustomerIcon())){
            sdv.setImageURI(Uri.parse(mDetail.getCustomerIcon()));
        }

        //通话记录
        if(mDetail.getCallHistories()!=null && !mDetail.getCallHistories().isEmpty()){
            CallHistoryAdapter mHistoryAdapter = new CallHistoryAdapter(this,mDetail.getCallHistories());
            aq.id(R.id.listview_call_history).adapter(mHistoryAdapter);

            //有通话记录可以结束订单
            if(!mDetail.getStatus().equals(OrderDetail.STATUS_DONE)){
                aq.id(R.id.layout_order_finish).visible();
                aq.id(R.id.btn_order_finish).clicked(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFinishOrderDialog();
                    }
                });
            }else{
                aq.id(R.id.layout_order_finish).gone();
            }


        }else{
            //没有通话记录不能结束
            aq.id(R.id.layout_order_finish).gone();
        }

        //取消？？？
        if(mDetail.getStatus().equals(OrderDetail.STATUS_REQUEST_CANCELED)){
            aq.id(R.id.layout_order_cancel_handle).visible();
            aq.id(R.id.btn_order_agree_cancel).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeCancel();
                }
            });
            aq.id(R.id.btn_order_disagree_cancel).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    disagreeCancel();
                }
            });
        }else{
            aq.id(R.id.layout_order_cancel_handle).gone();
        }
    }

    boolean isOrderFinishing = false;

    /**
     * 结束订单提示
     */
    private void showFinishOrderDialog() {

        if(isOrderFinishing)
            return;


        new AlertDialog.Builder(this).setTitle("提示").setMessage("请确认已经解答客户咨询问题？")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                doFinishOrder();

            }}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();


    }

    /**
     * 结束订单，等待用户评价后系统将服务费划分到律师账号
     */
    private void doFinishOrder() {

        if(isOrderFinishing)
            return;

        new FinishOrderReq(OrderDetailActivity.this,mOrderId).execute(new Action.Callback<Boolean>() {
            @Override
            public void progress() {
                isOrderFinishing = true;
            }

            @Override
            public void onError(int errorCode, String msg) {
                isOrderFinishing = false;
                UIHelper.showToast(getApplicationContext(),"结束订单失败："+msg);
            }

            @Override
            public void onCompleted(Boolean aBoolean) {

                isOrderFinishing = false;

                //loadDetail();

                showSummaryTipDialog();

            }
        });
    }

    /**
     * 提示请总结
     */
    private void showSummaryTipDialog() {
        new AlertDialog.Builder(OrderDetailActivity.this)
                .setTitle(R.string.tip).setMessage(getString(R.string.tip_add_summary))
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UIHelper.goAddOrderSummaryActivity(OrderDetailActivity.this,mOrderId);
                    }
                }).create().show();
    }

    boolean isDisagreeing = false;

    /**
     * 同意取消
     */
    private void disagreeCancel() {

        if(isDisagreeing)
            return;

        new AlertDialog.Builder(this).setTitle("提示").setMessage("您确定拒绝用户的取消请求吗？")
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                new DisagreeRequestCancelReq(OrderDetailActivity.this,mOrderId).execute(new Action.Callback<Boolean>() {
                    @Override
                    public void progress() {
                        isDisagreeing = true;
                    }

                    @Override
                    public void onError(int errorCode, String msg) {
                        isDisagreeing = false;
                        UIHelper.showToast(getApplicationContext(),"处理失败");
                    }

                    @Override
                    public void onCompleted(Boolean aBoolean) {

                        isDisagreeing = false;

                        aq.id(R.id.layout_order_cancel_handle).gone();

                        //拒绝成功，提示联系用户
                        showRefuseTipDialog();

                        loadDetail();
                    }

                    /**
                     * 已拒绝，提示
                     */
                    private void showRefuseTipDialog() {
                        new AlertDialog.Builder(OrderDetailActivity.this)
                                .setTitle(R.string.tip)
                                .setMessage(getString(R.string.tip_refused_request))
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                });
            }
        }).create().show();


    }

    boolean isAgreeing = false;
    /**
     * 不同意取消
     */
    private void agreeCancel() {

        if(isAgreeing)
            return;

        new AlertDialog.Builder(OrderDetailActivity.this).setTitle("提示").setMessage("确定同意用户的取消订单请求吗？")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new AgreeRequestCancelReq(OrderDetailActivity.this,mOrderId).execute(new Action.Callback<Boolean>() {
                            @Override
                            public void progress() {
                                isAgreeing = true;
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                isAgreeing = false;
                                UIHelper.showToast(getApplicationContext(),"处理失败");
                            }

                            @Override
                            public void onCompleted(Boolean aBoolean) {
                                isAgreeing = false;

                                aq.id(R.id.layout_order_cancel_handle).gone();

                                UIHelper.showToast(getApplicationContext(),"处理成功");

                                loadDetail();
                            }
                        });
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();


    }

    MakeCallReq mMakeCallReq;
    boolean isWaitingCall = false;
    /**
     * 打电话
     */
    private void doMakeCall() {

        if(isWaitingCall){
            return;
        }

        if(!checkCanMakeCall()){
            UIHelper.showToast(getApplicationContext(),"当前订单状态不能拨打电话");
            return;
        }

        mMakeCallReq = new MakeCallReq(this,mOrderId);
        mMakeCallReq.execute(new Action.Callback<Action.CommonResult>() {

            @Override
            public void progress() {
                isWaitingCall = true;
                progressDialog.setMessage("正在呼叫，请稍候...");
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                isWaitingCall = false;
                UIHelper.showToast(getApplicationContext(),"拨号失败:"+msg);

            }

            @Override
            public void onCompleted(Action.CommonResult result) {
                progressDialog.setMessage("拨号成功,请等待回拨");
                progressDialog.show();

                AQUtility.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isWaitingCall = false;
                    }
                },60*1000);//60s等待回拨
            }
        });
    }

    /**
     * 检查是否可以拨打电话
     * @return
     */
    private boolean checkCanMakeCall() {

        if(mDetail==null)
            return false;

        //订单状态（请求取消、已取消、已完成）不能打电话
        if(!mDetail.getStatus().equals(OrderDetail.STATUS_DOING)//进行中
                && !mDetail.getStatus().equals(OrderDetail.STATUS_WAITING)){//待处理
            return false;
        }

        //当前时间进入预约时间
        String startTime = mDetail.getStartTime();//预约时间
        Date start = DateFormatUtils.parse(startTime);
        Date now = new Date();

        //还没有到预约时间
        if(now.before(start))
            return false;

        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_order_detail);
    }


    @Override
    protected void onPause() {
        super.onPause();
        AudioHelper.getInstance().clearListener();
        AudioHelper.getInstance().release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mGetOrderDetailReq != null && mGetOrderDetailReq.isLoading()){
            mGetOrderDetailReq.cancel();
            mGetOrderDetailReq = null;
        }

        if(mMakeCallReq != null && mMakeCallReq.isLoading()){
            mMakeCallReq.cancel();
            mMakeCallReq = null;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== Constants.REQUEST_CODE_ADD_SUMMARY){
            loadDetail();
        }
    }
}
