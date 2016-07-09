package com.lsm.barrister.ui.adapter;

import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister.R;

public class EmptyController {

    View contentView;
    View emptyView;

    AQuery aq;

    public interface Callback {
        void doRefresh();
    }

    private int status = 0;

    public static final int STATUS_LOADING = 0;
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_EMPTY = 2;
    public static final int STATUS_ERROR = 3;
    public static final int STATUS_NETWORK_ERROR = 4;

    public void setStatus(int status) {
        this.status = status;
    }

    Callback callback;

    public EmptyController(View contentView, View emptyView, Callback callback) {
        this.contentView = contentView;
        this.emptyView = emptyView;
        this.callback = callback;

        contentView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);

        aq = new AQuery(emptyView);
    }

    public void showContent() {
        contentView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
    }

    public void showLoading() {

//        contentView.setVisibility(View.GONE);
//        emptyView.setVisibility(View.VISIBLE);
//
//        ImageView img = holder.id(R.id.image_empty).image(0).getImageView();
//        Animation loadAnimation = AnimationUtils.loadAnimation(holder.getContext(), R.anim.loading);
//        img.setAnimation(loadAnimation);
//        img.setBackgroundResource(R.drawable.loading);
//
//        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
//
//        // Start the animation (looped playback by default).
//        frameAnimation.start();
//
//        loadAnimation.start();
//
//        holder.id(R.id.tv_empty_tip).text("Loading...");
//        holder.id(R.id.btn_empty_refresh).gone();
    }

    public void showError(int errorCode, String errorMsg) {

        aq.id(R.id.image_empty).getImageView().clearAnimation();

        if (errorCode == -101) {

            showNetworkError();

        } else {

            contentView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);

            aq.id(R.id.image_empty).image(R.drawable.ic_launcher).getImageView().setBackgroundResource(0);
            aq.id(R.id.tv_empty_tip).text("服务器出错啦！");
            aq.id(R.id.btn_empty_refresh).clicked(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    callback.doRefresh();
                }
            }).visible();
        }
    }

    public void showEmpty() {
        aq.id(R.id.image_empty).getImageView().clearAnimation();

        contentView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);

        aq.id(R.id.image_empty).image(R.drawable.ic_launcher).getImageView().setBackgroundResource(0);
        aq.id(R.id.tv_empty_tip).text("暂无数据");
        aq.id(R.id.btn_empty_refresh).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.doRefresh();
            }
        }).visible();
    }

    public void showMessage(String message) {
        aq.id(R.id.image_empty).getImageView().clearAnimation();

        contentView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);

        aq.id(R.id.image_empty).image(R.drawable.ic_launcher).getImageView().setBackgroundResource(0);
        aq.id(R.id.tv_empty_tip).text(message);
        aq.id(R.id.btn_empty_refresh).gone();
    }

    public void showNetworkError() {

        aq.id(R.id.image_empty).getImageView().clearAnimation();

        contentView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        aq.id(R.id.image_empty).image(R.drawable.ic_launcher).getImageView().setBackgroundResource(0);
        aq.id(R.id.tv_empty_tip).text("网络出错啦！");
        aq.id(R.id.btn_empty_refresh).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.doRefresh();
            }
        }).visible();
    }
}
