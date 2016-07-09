package com.lsm.barrister.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.Case;
import com.lsm.barrister.ui.UIHelper;

import java.util.List;

/**
 * 学习中心适配器
 */
public class CaseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Case> items;
    private final LoadMoreListener mListener;

    boolean isHome = false;

    public CaseListAdapter(List<Case> items, LoadMoreListener loadMoreListener,boolean isHome) {
        this.items = items;
        mListener = loadMoreListener;
        this.isHome = isHome;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case_list, parent, false);

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

            if(isHome){
                footer.hide();
                return;
            }

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
        public Case mItem;
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

        public void bind(Case item) {
            mItem = item;
            aq.id(R.id.tv_item_case_title).text(item.getTitle());
            aq.id(R.id.tv_item_case_serial_num).text("案源号:"+item.getId());
            if(TextUtils.isEmpty(item.getAddTime())){
                aq.id(R.id.tv_item_case_date).gone();
            }else{
                aq.id(R.id.tv_item_case_date).text(item.getAddTime()).visible();
            }

//            public static final String STATUS_CONSULTING = "case.status.consulting";//咨询
//            public static final String STATUS_INTERVIEW = "case.status.interview";//面谈
//            public static final String STATUS_SIGNATORY = "case.status.signatory";//签约
//            public static final String STATUS_FOLLOWUP = "case.status.followup";//跟进
//            public static final String STATUS_CLEARING = "case.status.clearing";//结算

            String status = "咨询";
            if(item.getStatus()!=null){
                if(item.getStatus().equals(Case.STATUS_CONSULTING)){
                    status = "咨询";
                }else if(item.getStatus().equals(Case.STATUS_INTERVIEW)){
                    status = "面谈";
                }else if(item.getStatus().equals(Case.STATUS_SIGNATORY)){
                    status = "签约";
                }else if(item.getStatus().equals(Case.STATUS_FOLLOWUP)){
                    status = "跟进";
                }else if(item.getStatus().equals(Case.STATUS_CLEARING)){
                    status = "结算";
                }
            }

            aq.id(R.id.tv_item_case_status).text("状态："+status+" , 联系方式："+ item.getContactPhone());

            aq.id(R.id.tv_item_case_type).text(item.getArea());

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
