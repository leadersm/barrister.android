package com.lsm.barrister.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.lsm.barrister.utils.DLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvshimin on 16/7/6.
 */
public class AudioHelper {

    private static final String TAG = AudioHelper.class.getSimpleName();

    private static AudioHelper instance = null;

    private AudioHelper() {
        init();
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;

                DLog.d(TAG,"duration:"+mp.getDuration());

                for(Callback temp:callbacks){
                    temp.onPrepared(mp);
                }

                mp.start();

                handler.sendEmptyMessage(0);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                DLog.d(TAG,"播放完成");
                handler.removeMessages(0);
                mediaPlayer.seekTo(0);

                for(Callback temp:callbacks){
                    temp.onCompletion(mp);
                }
            }
        });
    }

    public static AudioHelper getInstance(){
        if(instance==null){
            instance = new AudioHelper();
        }
        return instance;
    }

    MediaPlayer mediaPlayer;

    boolean isPrepared = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            updateProgress();

        }
    };

    private void updateProgress() {
        DLog.d(TAG,"current:"+mediaPlayer.getCurrentPosition());

        for(Callback temp:callbacks){
            temp.onUpdateProgress(mediaPlayer.getCurrentPosition());
        }

        handler.sendEmptyMessageDelayed(0,1000);
    }


    String url;

    public boolean isPlaying(String url){

        if(this.url!=null && this.url.equals(url) && isPrepared){
            return true;
        }

        return false;

    }

    public AudioHelper start(final Context context, String url){
        try {

            this.url = url;

            if(isPrepared){
                stop();
            }

            init();

            mediaPlayer.setDataSource(context, Uri.parse(url));
            isPrepared = false;
            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public void stop(){

        if(mediaPlayer==null || !mediaPlayer.isPlaying())
            return;

        handler.removeMessages(0);

        try {
//            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;

            for(Callback temp:callbacks){
                temp.onStop();
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    List<Callback> callbacks = new ArrayList<>();

    public void addCallback(Callback callback) {
        if(!callbacks.contains(callback)){
            callbacks.add(callback);
        }
    }

    public void release() {
        url = null;

        stop();

        callbacks.clear();
    }

    public void clearListener() {
        callbacks.clear();
    }

    public interface Callback{
        public void onPrepared(MediaPlayer mp);
        public void onUpdateProgress(int progress);
        public void onStop();
        public void onCompletion(MediaPlayer mp);
    }

}
