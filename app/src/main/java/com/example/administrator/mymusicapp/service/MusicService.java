package com.example.administrator.mymusicapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.mymusicapp.Constant;
import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.bean.PlayList;
import com.example.administrator.mymusicapp.widget.MyMusicWidgetProvider;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.example.administrator.mymusicapp.widget.MyMusicWidgetProvider.WIDGET_LAST_ACTION;
import static com.example.administrator.mymusicapp.widget.MyMusicWidgetProvider.WIDGET_NEXT;
import static com.example.administrator.mymusicapp.widget.MyMusicWidgetProvider.WIDGET_NEXT_ACTION;
import static com.example.administrator.mymusicapp.widget.MyMusicWidgetProvider.WIDGET_PLAY_ACTION;

/**
 * Created by Jugeny on 2017/6/12.
 */

public class MusicService extends Service{
    private static MediaPlayer mediaPlayer=new MediaPlayer();
    public static PlayList mPlayList;
    //当前播放的下标
    public static int mCurrIndex=0;

    @Override
    public void onCreate() {
        super.onCreate();
        initMusicServiceReceiver();
        initNotification();
    }

    private void initMusicServiceReceiver() {
        MusicServiceReceiver receiver=new MusicServiceReceiver();
        IntentFilter intentFiler=new IntentFilter();
        intentFiler.addAction(WIDGET_LAST_ACTION);
        intentFiler.addAction(MyMusicWidgetProvider.WIDGET_PLAY_ACTION);
        intentFiler.addAction(WIDGET_NEXT_ACTION);
        registerReceiver(receiver,intentFiler);
    }
    private void initNotification(){
        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.layout_notification);
        //上一首
        Intent intentLast=new Intent(WIDGET_LAST_ACTION);
        PendingIntent pendingIntentLast=PendingIntent.getBroadcast(this,0,intentLast,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_last,pendingIntentLast);
        //播放
        Intent intentPlay=new Intent(WIDGET_PLAY_ACTION);
        PendingIntent pendingIntentPlay=PendingIntent.getBroadcast(this,0,intentPlay,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_play,pendingIntentPlay);
        //下一首
        Intent intentNext=new Intent(WIDGET_NEXT_ACTION);
        PendingIntent pendingIntentNext=PendingIntent.getBroadcast(this,0,intentNext,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next,pendingIntentNext);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        Notification notification=builder.setContentTitle("我是通知栏标题")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .build();
        startForeground(111,notification);

    }
    MusicBinder mMusicBinder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mMusicBinder=new MusicBinder();
        return mMusicBinder;
    }

    public class MusicBinder extends Binder {
        public void play(){
            mediaPlayer.start();
            Intent intent=new Intent(Constant.Action.PLAY);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);
            updateWidget(mPlayList.getMusics().get(mCurrIndex));
            handler.sendMessageAtTime(Message.obtain(),500);
            PlayList.Music music=mPlayList.getMusics().get(mCurrIndex);
            updateNotification(music);
            updateWidget(music);
        }
        /**
         * 播放
         */
        public void play(PlayList playList){
//            if (mediaPlayer==null){
//                mediaPlayer=new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setLooping(false);
//            }
            mPlayList=playList;
            //mCurrIndex=position;

            String url="";
            //获取当前播放的url
            for (int i=0;i<playList.getMusics().size();i++){
                PlayList.Music music=playList.getMusics().get(i);
                if (music.isPlayStatus()){
                    url=music.getMusicUrl();
                    mCurrIndex=i;
                }
            }
          playUrl(url);
            handler.sendMessageAtTime(Message.obtain(),500);
            PlayList.Music music=mPlayList.getMusics().get(mCurrIndex);
            updateNotification(music);
        }
        /**
         * 播放  position播放的下标
         */
        public void play(int position) {
//            if (mediaPlayer==null){
//                mediaPlayer=new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setLooping(false);
//            }
            //mPlayList=playList;
            mCurrIndex = position;


            //重置所有的播放状态
            for (int i=0;i<mPlayList.getMusics().size();i++){
                mPlayList.getMusics().get(i).setPlayStatus(false);
            }
            PlayList.Music music=mPlayList.getMusics().get(mCurrIndex);
            music.setPlayStatus(true);
            String url = music.getMusicUrl();
            playUrl(url);

        }

        /**
         * 播放规定的url
         * @param url
         */
        public void playUrl(String url){
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            Intent intent=new Intent(Constant.Action.PLAY);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);
            updateWidget(mPlayList.getMusics().get(mCurrIndex));
            handler.sendMessageAtTime(Message.obtain(),500);
            PlayList.Music music=mPlayList.getMusics().get(mCurrIndex);
            updateWidget(music);
            updateNotification(music);
        }
        //暂停
        public  void pause(){
            mediaPlayer.pause();
            Intent intent=new Intent(Constant.Action.PAUSE);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);
            PlayList.Music music=mPlayList.getMusics().get(mCurrIndex);
            updateWidget(music);
            updateNotification(music);
        }
        /**
         * 获取播放状态
         */
        public boolean isPlaying(){
            if (mediaPlayer!=null){
                return mediaPlayer.isPlaying();
            }else {
                return false;
            }
        }
    }

    /**
     * 获取当前播放的歌单
     * @return
     */
    public static PlayList getCurrPlayList(){
        return mPlayList;
    }

    /**
     * 获取当前歌曲的下标
     * @return
     */
    public  static  int getCurrPlayIndex(){
        return mCurrIndex;
    }
    /**
     * 获取当前播放的进度
     */
    public static int getCurrentPosition(){
        if (mediaPlayer !=null){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }
    public static int getDuration(){
        if (mediaPlayer!=null){
            return mediaPlayer.getDuration();
        }
        return 0;
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mMusicBinder.isPlaying()){

            }
        }
    };
    private void updateWidget(PlayList.Music music){
        if (music!=null){
            RemoteViews remoteViews=new RemoteViews(getPackageName(), R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.widget_content,music.getTitle());
            if (mMusicBinder.isPlaying()){
                remoteViews.setImageViewResource(R.id.widget_play,R.mipmap.tr);

            }else {
                remoteViews.setImageViewResource(R.id.widget_play,R.mipmap.b13);
            }
            remoteViews.setProgressBar(R.id.widget_progress,getDuration(),getCurrentPosition(),false);
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(MusicService.this);
            appWidgetManager.updateAppWidget(new ComponentName(MusicService.this,MyMusicWidgetProvider.class),remoteViews);
        }
    }
    private void updateNotification(final PlayList.Music music){
        final RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.layout_notification);
        if (mMusicBinder.isPlaying()){
            remoteViews.setImageViewResource(R.id.widget_play,R.mipmap.tr);
        }else {
            remoteViews.setImageViewResource(R.id.widget_play,R.mipmap.b13);
        }
        remoteViews.setTextViewText(R.id.widget_content,music.getTitle());
        //Ctrl+Alt+V生成返回值
       final NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
       final Notification notification=builder
                .setContent(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        //如果id相同，那么直接更新通知栏的属性
        manager.notify(111,notification);
        //加载图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                try {
                    Bitmap bitmap= Glide
                            .with(MusicService.this)
                            .load(music.getAlbumPicUrl())
                            .asBitmap()
                            .centerCrop()
                            .into(150,150)
                            .get();
                    remoteViews.setImageViewBitmap(R.id.widget_image,bitmap);
                    manager.notify(111,notification);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    class MusicServiceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case WIDGET_LAST_ACTION:
                    if (mCurrIndex==0){
                        Toast.makeText(context, "第一首", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mCurrIndex=mCurrIndex-1;
                    mMusicBinder.play(mCurrIndex);
                    updateWidget(mPlayList.getMusics().get(mCurrIndex));
                    handler.sendMessageAtTime(Message.obtain(),500);

                    break;
                case MyMusicWidgetProvider.WIDGET_PLAY_ACTION:
                    Toast.makeText(context, "播放", Toast.LENGTH_SHORT).show();
                    if (mMusicBinder.isPlaying()){
                        mMusicBinder.pause();
                    }else {
                        mMusicBinder.play();
                    }
                    updateWidget(mPlayList.getMusics().get(mCurrIndex));
                    handler.sendMessageAtTime(Message.obtain(),500);
                    break;
                case WIDGET_NEXT_ACTION:
                    if (mCurrIndex==mPlayList.getMusics().size()-1){
                        Toast.makeText(context, "最后一首了", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mCurrIndex=mCurrIndex+1;
                    mMusicBinder.play(mCurrIndex);
                    updateWidget(mPlayList.getMusics().get(mCurrIndex));
                    handler.sendMessageAtTime(Message.obtain(),500);
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
