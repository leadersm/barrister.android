package com.lsm.barrister.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.LearningItem;
import com.lsm.barrister.ui.UIHelper;

import java.util.List;

/**
 * 学习中心适配器
 */
public class LearningRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<LearningItem> items;
    private final LoadMoreListener mListener;

    public LearningRecyclerViewAdapter(List<LearningItem> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_learning_center, parent, false);

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
        public final TextView mTitleView;
        public final TextView mDateView;
        public LearningItem mItem;

        public ItemHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.tv_item_title);
            mDateView = (TextView) view.findViewById(R.id.tv_item_date);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goWebViewActivity(v.getContext(),mItem.getUrl(),mItem.getTitle());
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }

        public void bind(LearningItem learningItem) {
            mItem = learningItem;
            mTitleView.setText(mItem.getId());
            mDateView.setText(mItem.getTitle());

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
