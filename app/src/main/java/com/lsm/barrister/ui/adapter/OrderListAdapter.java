package com.lsm.barrister.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.OrderItem;
import com.lsm.barrister.ui.activity.OrderDetailActivity;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private final List<OrderItem> items;
    private final LoadMoreListener mListener;

    public OrderListAdapter(List<OrderItem> items, LoadMoreListener listener) {
        this.items = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = items.get(position);
        holder.mNicknameView.setText("用户"+position);
        holder.mDateView.setText("2016-04-07 00:00:00");
        holder.mStatusView.setText("已完成");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
                intent.putExtra("item",items.get(position));
                v.getContext().startActivity(intent);
            }
        });

//        holder.mThumbView.setImageURI(Uri.parse(items.get(position).thumb));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNicknameView;
        public final TextView mDateView;
        public final TextView mStatusView;
        public final SimpleDraweeView mThumbView;
        public OrderItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            view.setClickable(true);
            mNicknameView = (TextView) view.findViewById(R.id.tv_item_case_nickname);
            mDateView = (TextView) view.findViewById(R.id.tv_item_case_date);
            mStatusView = (TextView) view.findViewById(R.id.tv_item_case_status);
            mThumbView = (SimpleDraweeView) view.findViewById(R.id.image_item_thumb);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}
