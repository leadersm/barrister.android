package com.lsm.barrister.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.db.PushMessage;
import com.lsm.barrister.data.entity.Message;
import com.lsm.barrister.ui.UIHelper;

import java.util.List;

/**
 * 学习中心适配器
 */
public class MsgListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Message> items;
    private final LoadMoreListener mListener;

    public MsgListAdapter(List<Message> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_list, parent, false);

            holder = new ItemHolder(view);

            return holder;

        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more, parent, false);

            return holder = new FooterViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemHolder){

            ItemHolder temp = (ItemHolder) holder;
            temp.bind(items.get(position));

        }else if (holder instanceof FooterViewHolder) {

            FooterViewHolder footer = (FooterViewHolder) holder;

            if (mListener.hasMore()) {
                footer.showLoadMore();
                mListener.onLoadMore();
            } else {
                footer.showNoMore();
            }

        }

    }

    public String getTitle(Message msg){

        String title = "";

        if (msg.getType().equals(PushMessage.TYPE_ORDER_MONEY)) {
            //订单服务费到账
           title = "订单服务费到账通知";
        }else if (msg.getType().equals(PushMessage.TYPE_ORDER_REWARD)) {
            //打赏
           title = "打赏通知";
        }else if (msg.getType().equals(PushMessage.TYPE_ORDER_NEW)) {

            //新订单
           title = "订单通知";

        }else if (msg.getType().equals(PushMessage.TYPE_ORDER_STATUS)) {

            //订单状态
           title = "订单通知";
        }else if (msg.getType().equals(PushMessage.TYPE_RECEIVE_STAR)) {

            //收到评价
           title = "订单评价通知";
        }else if (msg.getType().equals(PushMessage.TYPE_RECHARGE)) {

            //充值
           title = "充值通知";
        }else if (msg.getType().equals(PushMessage.TYPE_VERIFY)) {

            //律师段认证通知
           title = "认证通知";
        }else if (msg.getType().equals(PushMessage.TYPE_LEARNING)) {

            //学习中心
           title = "学习中心";

        }else if (msg.getType().equals(PushMessage.TYPE_GET_MONEY)) {

            //提现
           title = "提现通知";

        }else if (msg.getType().equals(PushMessage.TYPE_ORDER_BACK_MONEY)) {

            //订单取消退款
           title = "退款通知";
        }else if (msg.getType().equals(PushMessage.TYPE_SYSTEM_MSG)) {
            //处理
           title = "系统通知";

        }else if (msg.getType().equals(PushMessage.TYPE_HANG_OFF)){
            //处理
            title = "系统通知";
        }

        return title;
    }

    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {

            // 最后一个item设置为footerView
            return TYPE_FOOTER;

        }else {
            return TYPE_ITEM;

        }
    }

    @Override
    public int getItemCount() {
        return items == null || items.isEmpty() ? 0 : (items.size() + 1);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Message mItem;
        AQuery aq;

        public ItemHolder(View view) {
            super(view);
            mView = view;
            aq = new AQuery(view);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItem==null){
                        return;
                    }

                    String type = mItem.getType();

                    if (type.equals(PushMessage.TYPE_ORDER_STATUS) || type.equals(PushMessage.TYPE_ORDER_NEW)|| type.equals(PushMessage.TYPE_HANG_OFF)) {

                        UIHelper.goOrderDetailActivity(v.getContext(),mItem.getContentId());

                    } else if (type.equals(PushMessage.TYPE_LEARNING)) {


                    }else if (type.equals(PushMessage.TYPE_ORDER_REWARD)||
                            type.equals(PushMessage.TYPE_RECHARGE)||
                            type.equals(PushMessage.TYPE_GET_MONEY)||
                            type.equals(PushMessage.TYPE_ORDER_BACK_MONEY)||
                            type.equals(PushMessage.TYPE_RECEIVE_STAR)) {

                        UIHelper.goMyAccountActivity(v.getContext());

                    }else if(type.equals(PushMessage.TYPE_SYSTEM_MSG)){

                    }
                }
            });
        }

        public void bind(Message item) {
            mItem = item;
            aq.id(R.id.tv_item_msg_title).text(getTitle(item));
            aq.id(R.id.tv_item_msg_content).text(item.getTitle());
            aq.id(R.id.tv_item_msg_date).text(item.getDate());
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        View view;

        public FooterViewHolder(View view) {
            super(view);
            this.view = view;

        }

        public void showNoMore() {
            // TODO Auto-generated method stub
            TextView tv = (TextView) view.findViewById(R.id.tv_item_loadingmore);

            view.findViewById(R.id.progress_item_loadingmore).setVisibility(View.GONE);

            tv.setText("没有更多了");
        }

        public void showLoadMore() {
            TextView tv = (TextView) view.findViewById(R.id.tv_item_loadingmore);

            view.findViewById(R.id.progress_item_loadingmore).setVisibility(View.VISIBLE);

            tv.setText("加载中,请稍候...");
        }

        public void hide(){

            TextView tv = (TextView) view.findViewById(R.id.tv_item_loadingmore);
            tv.setText("查看更多");

            view.findViewById(R.id.progress_item_loadingmore).setVisibility(View.GONE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goCaseListAcitivity(v.getContext());
                }
            });
        }

    }
}
