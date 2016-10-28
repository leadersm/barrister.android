package com.lsm.barrister.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.IncomeDetailItem;

import java.util.List;

/**
 * 学习中心适配器
 */
public class IncomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<IncomeDetailItem> items;
    private final LoadMoreListener mListener;

    public IncomeListAdapter(List<IncomeDetailItem> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income_detail, parent, false);

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
        public IncomeDetailItem mItem;
        AQuery aq;

        public ItemHolder(View view) {
            super(view);
            mView = view;
            aq = new AQuery(view);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(IncomeDetailItem item) {
            mItem = item;

            String type = "其他";
            String pm = "-";
            int color = Color.parseColor("#cccccc");;
            if(item.getType().equals(IncomeDetailItem.TYPE_GET_MONEY)){
                type = "提现";
                color = aq.getContext().getResources().getColor(R.color.money_out);
                pm = "-";
            }else if(item.getType().equals(IncomeDetailItem.TYPE_ORDER)){
                type = "订单收入";
                color = aq.getContext().getResources().getColor(R.color.money_in);
                pm = "+";

            }else if(item.getType().equals(IncomeDetailItem.TYPE_REWARD)){
                type = "打赏";
                pm = "+";
                color = aq.getContext().getResources().getColor(R.color.money_in);
            }else if(item.getType().equals(IncomeDetailItem.TYPE_BUY_CASE)){
                type = "购买案源";
                color = aq.getContext().getResources().getColor(R.color.money_out);
                pm = "-";
            }

            aq.id(R.id.tv_item_income_serial_num).text("流水号:"+item.getSerialNum());
            aq.id(R.id.tv_item_income_type).text(type);
            aq.id(R.id.tv_item_income_cash).text(pm+item.getMoney()+"元").textColor(color);

            aq.id(R.id.tv_item_income_date).text(item.getDate());
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
