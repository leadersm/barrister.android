package com.lsm.barrister.ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AudioHelper;
import com.lsm.barrister.data.entity.CallHistory;

import java.util.List;
import java.util.Locale;

/**
 * Created by lvshimin on 16/5/22.
 */
public class CallHistoryAdapter extends BaseAdapter {

    List<CallHistory> data;
    Context context;

    public CallHistoryAdapter(Context context, List<CallHistory> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public CallHistory getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_call_history, parent, false);
            holder = new ViewHolder();
            holder.holder = new AQuery(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bind(getItem(position));

        return convertView;
    }


    class ViewHolder implements AudioHelper.Callback {

        AQuery holder;

        @Override
        public void onPrepared(MediaPlayer mp) {
            if(AudioHelper.getInstance().isPlaying(item.getRecordUrl())){
                holder.id(R.id.seekbar).getSeekBar().setMax(mp.getDuration());
                holder.id(R.id.btn_callhistory_pause).visible();
                holder.id(R.id.btn_callhistory_play).gone();
            }
        }

        @Override
        public void onUpdateProgress(int progress) {

            if(AudioHelper.getInstance().isPlaying(item.getRecordUrl())){
                holder.id(R.id.seekbar).getSeekBar().setProgress(progress);
                holder.id(R.id.tv_callhistory_runtime).text(String.valueOf(progress / 1000));
            }
        }

        @Override
        public void onStop() {
            if(AudioHelper.getInstance().isPlaying(item.getRecordUrl())){
                holder.id(R.id.btn_callhistory_play).visible();
                holder.id(R.id.btn_callhistory_pause).gone();

                holder.id(R.id.seekbar).getSeekBar().setProgress(0);
                holder.id(R.id.tv_callhistory_runtime).text(String.valueOf(0));

            }

        }

        @Override
        public void onCompletion(MediaPlayer mp) {

            if(AudioHelper.getInstance().isPlaying(item.getRecordUrl())){

                holder.id(R.id.btn_callhistory_play).visible();
                holder.id(R.id.btn_callhistory_pause).gone();

                holder.id(R.id.seekbar).getSeekBar().setProgress(0);
                holder.id(R.id.tv_callhistory_runtime).text(String.valueOf(0));
            }

        }

        CallHistory item;

        private void bind(final CallHistory item) {
            this.item = item;

            holder.id(R.id.tv_callhistory_date).text(item.getStartTime());
            holder.id(R.id.tv_callhistory_duration).text(String.format(Locale.CHINA, "通话时长%d秒", item.getDuration()));

            if(!TextUtils.isEmpty(item.getRecordUrl()) && AudioHelper.getInstance().isPlaying(item.getRecordUrl())){
                holder.id(R.id.btn_callhistory_play).gone();
                holder.id(R.id.btn_callhistory_pause).visible();
            }else {
                holder.id(R.id.btn_callhistory_play).visible();
                holder.id(R.id.btn_callhistory_pause).gone();
            }

            holder.id(R.id.btn_callhistory_play).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioHelper.getInstance().stop();

                    AudioHelper.getInstance().start(v.getContext(), item.getRecordUrl());
                }

            });

            holder.id(R.id.btn_callhistory_pause).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AudioHelper.getInstance().stop();
                }
            });

            if (!TextUtils.isEmpty(item.getRecordUrl())) {

                holder.id(R.id.layout_play_record).visible();

                AudioHelper.getInstance().addCallback(this);

            } else {
                holder.id(R.id.layout_play_record).gone();
            }
        }
    }


}
