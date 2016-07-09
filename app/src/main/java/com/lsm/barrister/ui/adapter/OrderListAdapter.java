package com.lsm.barrister.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.OrderItem;
import com.lsm.barrister.ui.activity.OrderDetailActivity;
import com.lsm.barrister.utils.OrderUtils;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<OrderItem> items;
    private final LoadMoreListener mListener;
    boolean isHome;
    public OrderListAdapter(List<OrderItem> items, LoadMoreListener listener,boolean isHome) {
        this.items = items;
        this.isHome = isHome;
        mListener = listener;
    }

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    @Override
    public int getItemViewType(int position) {


        if (!isHome && position + 1 == getItemCount()) {

            // 最后一个item设置为footerView
            return TYPE_FOOTER;

        }else {
            return TYPE_ITEM;

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);

            holder = new ItemHolder(view);

            return holder;

        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more, parent, false);

            return holder = new FooterViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

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

    @Override
    public int getItemCount() {
        return items == null || items.isEmpty() ? 0 : (isHome?items.size() : (items.size() + 1));
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public OrderItem mItem;
        AQuery aq;
        public ItemHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
                    intent.putExtra("id",mItem.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void bind(OrderItem item){
            this.mItem = item;

            String phoneNumber = mItem.getClientPhone();

            String showPhone = phoneNumber;

            if(!TextUtils.isEmpty(phoneNumber)){
                showPhone = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            }

            aq.id(R.id.tv_item_case_nickname).text(showPhone);

            String status = OrderUtils.getStatusString(item.getStatus());
            int statusColor = OrderUtils.getStatusColor(item.getStatus());

            //status
            aq.id(R.id.tv_item_case_status).text(status).textColor(statusColor);

            String type ;

            if(mItem.getType()!=null && mItem.getType().equals(OrderItem.TYPE_APPOINTMENT)){
                type = aq.getContext().getString(R.string.type_appointment);
            }else{
                type = aq.getContext().getString(R.string.type_im);
            }

            aq.id(R.id.tv_item_case_type).text(type);

            aq.id(R.id.tv_item_case_date).text(mItem.getDate());
            SimpleDraweeView userIcon = (SimpleDraweeView) aq.id(R.id.image_item_thumb).getView();
            if(!TextUtils.isEmpty(mItem.getUserIcon())){
                userIcon.setImageURI(Uri.parse(mItem.getUserIcon()));
            }

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

    }
}
