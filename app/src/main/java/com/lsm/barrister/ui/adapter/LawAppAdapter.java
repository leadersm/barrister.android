package com.lsm.barrister.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.LawApp;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.ui.UIHelper;

import java.util.List;

/**
 */
public class LawAppAdapter extends RecyclerView.Adapter<LawAppAdapter.ViewHolder> {

    private final List<LawApp> items;

    public LawAppAdapter(List<LawApp> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_law_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LawApp mItem;

        AQuery aq;

        public ViewHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    User user = UserHelper.getInstance().getUser(v.getContext());

                    if(user ==null){
                        UIHelper.goLoginActivity((Activity) v.getContext());
                        return;
                    }

                    if(mItem!=null) {
                        String url = mItem.getUrl();// + "&phone="+user.getPhone();//访问验证
                        UIHelper.goWebViewActivity(v.getContext(), url,mItem.getName());
                    }
                }
            });
        }

        public void bind(LawApp item) {
            mItem = item;
            aq.id(R.id.tv_item_biz_name).text(item.getName());

            SimpleDraweeView userIconView = (SimpleDraweeView) aq.id(R.id.image_item_casetype_icon).getView();
            if(!TextUtils.isEmpty(item.getIcon())){
                userIconView.setImageURI(Uri.parse(item.getIcon()));
            }

        }


    }
}
