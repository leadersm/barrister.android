package com.lsm.barrister.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.OrderDetail;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetOrderDetailReq;
import com.lsm.barrister.data.io.app.MakeCallReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.adapter.CallHistoryAdapter;
import com.lsm.barrister.utils.DLog;
import com.lsm.barrister.utils.TextHandler;

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
                doMakeCall();
            }
        });

        loadDetail();

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

    RecyclerView mRecyclerView;
    /**
     * 绑定数据
     */
    private void bind() {

        if(mDetail==null){
            System.err.println("加载订单详情出错");
            return;
        }

        String status = null;
        int statusColor = Color.parseColor("#cccccc");
        if(mDetail.getStatus().equals(OrderDetail.STATUS_WAITING)){
            status = "待办";
            statusColor = Color.parseColor("#ffef87");//ffef87;
        }else if(mDetail.getStatus().equals(OrderDetail.STATUS_DOING)){
            status = "进行中";
            statusColor = Color.parseColor("#45cd87");//45cd87;
        }else if(mDetail.getStatus().equals(OrderDetail.STATUS_DONE)){
            status = "已完成";
            statusColor = Color.parseColor("#59E1FA");//59E1FA;
        }else if(mDetail.getStatus().equals(OrderDetail.STATUS_CANCELED)){
            status = "已取消";
            statusColor = Color.parseColor("#848284");//848284;
        }else if(mDetail.getStatus().equals(OrderDetail.STATUS_REFUND)){
            status = "退款中";
            statusColor = Color.parseColor("#a9f82e");//a9f82e;
        }

        //status
        aq.id(R.id.tv_order_status).text(status).textColor(statusColor);
        //num
        aq.id(R.id.tv_order_num).text(mDetail.getOrderNo());
        //案件类型
        aq.id(R.id.tv_order_case_type).text(mDetail.getCaseType());
        //时间
        aq.id(R.id.tv_order_time).text(mDetail.getPayTime());
        //金额
        aq.id(R.id.tv_order_payment).text(String.format(Locale.CHINA,"%.1f",mDetail.getPaymentAmount()));
        //备注
        aq.id(R.id.tv_order_remark).text(mDetail.getRemarks());
        //用户昵称
        aq.id(R.id.tv_order_nickname).text(mDetail.getCustomerNickname());
        //手机号码
        aq.id(R.id.tv_order_phone_number).text(TextHandler.getHidePhone(mDetail.getCustomerPhone()));
        //通话记录
        if(mDetail.getCallHistories()!=null && !mDetail.getCallHistories().isEmpty()){
            CallHistoryAdapter mHistoryAdapter = new CallHistoryAdapter(this,mDetail.getCallHistories());
            aq.id(R.id.listview_call_history).adapter(mHistoryAdapter);
        }

    }

    MakeCallReq mMakeCallReq;
    /**
     * 打电话
     * 提示正在呼叫。。
     */
    private void doMakeCall() {
//        ECHelper.getInstance().makeVoiceCall("8011846500000003");//网络语音电话

        if(TextUtils.isEmpty(mOrderId)){
            DLog.e(TAG,"orderId is null");
            return;
        }

        mMakeCallReq = new MakeCallReq(this,mOrderId);
        mMakeCallReq.execute(new Action.Callback<Action.CommonResult>() {

            @Override
            public void progress() {
                UIHelper.showToast(getApplicationContext(),"正在呼叫，请稍候...");
            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getApplicationContext(),"拨号失败..."+msg);

            }

            @Override
            public void onCompleted(Action.CommonResult result) {
                UIHelper.showToast(getApplicationContext(),"拨号成功...");

            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_order_detail);
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

    //
}
