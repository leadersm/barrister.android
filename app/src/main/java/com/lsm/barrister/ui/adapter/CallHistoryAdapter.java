package com.lsm.barrister.ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.CallHistory;
import com.lsm.barrister.utils.DateFormatUtils;

import java.util.Date;
import java.util.List;

import nl.changer.audiowife.AudioWife;

/**
 * Created by lvshimin on 16/5/22.
 */
public class CallHistoryAdapter extends BaseAdapter{

    List<CallHistory> data;
    Context context;
    public CallHistoryAdapter(Context context,List<CallHistory> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null?0:data.size();
    }

    @Override
    public CallHistory getItem(int position) {
        return data==null?null:data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AQuery holder = null;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_call_history,parent,false);
            holder = new AQuery(convertView);
            convertView.setTag(holder);
        }else {
            holder = (AQuery) convertView.getTag();
        }

        bind(getItem(position),holder);

        return convertView;
    }

    private void bind(CallHistory item, AQuery holder) {

        holder.id(R.id.tv_callhistory_date).text(item.getStartTime());
        holder.id(R.id.tv_callhistory_duration).text(DateFormatUtils.format(new Date(item.getDuration()),"HH:mm:ss"));
        holder.id(R.id.btn_callhistory_play).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View play = holder.id(R.id.btn_callhistory_play).getView();
        View pause = holder.id(R.id.btn_callhistory_pause).getView();
        SeekBar seekBar = holder.id(R.id.progress_callhistory).getSeekBar();
        TextView runtime = holder.id(R.id.tv_callhistory_runtime).getTextView();

        AudioWife.getInstance()
                .init(context, Uri.parse(item.getRecordUrl()))
                .setPlayView(play)
                .setPauseView(pause)
                .setSeekBar(seekBar)
                .setRuntimeView(runtime)
//                .setTotalTimeView(mTotalTime)
                .addOnCompletionListener( new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(context, "播放完成", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
