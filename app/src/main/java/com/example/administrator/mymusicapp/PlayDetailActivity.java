package com.example.administrator.mymusicapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.mymusicapp.bean.LrcBean;
import com.example.administrator.mymusicapp.bean.NewPlayListResultsBean;
import com.example.administrator.mymusicapp.bean.PlayList;
import com.example.administrator.mymusicapp.service.MusicService;
import com.example.administrator.mymusicapp.widget.DiscView;
import com.example.administrator.mymusicapp.widget.LrcView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayDetailActivity extends AppCompatActivity {
    public static final String DETAILS_KEY = "details";
    public static final String RESULTSBEEN_KEY = "mResultsBeen";
    public static final String INDEX_KEY = "position";
    private static final String TAG = "PlayDetailActivity";
    private ImageView bgDetail;
    private ImageView ivBack;
    //歌单的歌曲列表
    ArrayList<NewPlayListResultsBean> mResultsBeen;
    PlayList mPlayList;
    private TextView detailSong;
    private TextView detailArtist;
    private DiscView dv;
    private ImageView playSong;
    private PlayList.Music music;
    private LrcView lrcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_detail);
        mPlayList=getIntent().getParcelableExtra(RESULTSBEEN_KEY);
        Log.d(TAG, "onCreate: "+mPlayList);
        //获取下标
        int position=getIntent().getIntExtra(INDEX_KEY,0);
        music = mPlayList.getMusics().get(position);
        String url = "http://ac-kCFRDdr9.clouddn.com/e3e80803c73a099d96a5.jpg";
       // NewPlayListResultsBean bean=getIntent().getParcelableExtra(DETAILS_KEY);
        if(music.getAlbumPicUrl()!=null){
            url= music.getAlbumPicUrl();
        }
        dv = (DiscView) findViewById(R.id.dv);
        detailSong = (TextView) findViewById(R.id.tv_detail_song_name);
        detailArtist = (TextView) findViewById(R.id.tv_detail_artist);
        detailSong.setText(music.getTitle());
        detailArtist.setText(music.getArtist());

        bgDetail = (ImageView) findViewById(R.id.iv_bg_detail);
        ivBack = (ImageView) findViewById(R.id.iv_back_detail);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView lastSong= (ImageView) findViewById(R.id.iv_last_song);
        lastSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.playLast();
            }
        });
        ImageView nextSong= (ImageView) findViewById(R.id.iv_next_song);
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.playNext();
            }
        });
        playSong = (ImageView) findViewById(R.id.iv_play_song);
        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBinder.isPlaying()){
                    musicBinder.pause();
                    playSong.setImageResource(R.mipmap.play_rdi_btn_play);
                    dv.pauseAnim();
                }else {
                    musicBinder.play();
                    dv.playAnim();
                    playSong.setImageResource(R.mipmap.play_rdi_btn_pause);
                }
//                playSong.setSelected(! playSong.isSelected());
//            if (playSong.isSelected()){
//                playSong.setImageResource(R.mipmap.play_rdi_btn_pause);
//            }else {
//                playSong.setImageResource(R.mipmap.play_rdi_btn_play);
//            }
//            dv.pause();
        }
        });
        Glide.with(this)
                .load(url)
                .bitmapTransform(new BlurTransformation(this,10,3){
                })
                .into(bgDetail);
        //这里想要拿到更新标题的通知
        dv.setDiscChangListener(discChangeListener);
        dv.setMusicData(mPlayList,position);
        bindMusicService();
        lrcView = (LrcView) findViewById(R.id.lrcView);
        lrcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lrcView.setVisibility(View.GONE);
                dv.setVisibility(View.VISIBLE);
            }
        });
//        downloadLrc(music.getLrcUrl());
    }

    /**
     * 下载歌词
     * @param lrcUrl  下载地址
     */
    private void downloadLrc(String lrcUrl){
        //生成一个唯一的文件名，这个文件名和歌词的下载链接绑定
        //将lrcUrl的连接生成一个md5字符串
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(lrcUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //每次都需要去请求歌词，可以将歌词存储到文件里面，
                //下一次请求，判断是否存在改文件，如果存在，则直接使用
                String result=response.body().string();
                parseLrc(result);
            }
        });
    }
    ArrayList<LrcBean> lrcBeen=new ArrayList<>();
    /**
     * 解析歌词
     * @param lrcStr
     */
    public void parseLrc(String lrcStr){
        String[] split=lrcStr.split("\n");
        Log.e(TAG,"parseLrc:"+ Arrays.toString(split));
        for (int i=0;i<split.length;i++){
            //获取每一行
            String line=split[i];
            String[] arr=line.split("\\]");
            //arr[0]  [00:00.00     [00
            //分
            String min=arr[0].split(":")[0].replace("[","");
            //秒
            String sec=arr[0].split(":")[1].split("\\.")[0];
            //毫秒
            String mills=arr[0].split(":")[1].split("\\.")[1];
            String content;
            if (arr.length>1){
                content=arr[1];
            }else {
                content="music";
            }
            //将获取到的数据封装为对象
            long startTime=Long.valueOf(min)*60*1000+Long.valueOf(sec)*1000+Long.valueOf(mills);
            LrcBean lrcBean=new LrcBean(content,startTime,0);
            lrcBeen.add(lrcBean);
            if (lrcBeen.size()>1){
                lrcBeen.get(lrcBeen.size()-2).setEndTime(startTime);
            }
            if (i==split.length-1){
                lrcBeen.get(lrcBeen.size()-1).setEndTime(startTime);
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lrcView.setLrcBeen(lrcBeen);
            }
        });
    }

    public void bindMusicService(){
        Intent intent=new Intent(this, MusicService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }
    MusicService.MusicBinder musicBinder;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder= (MusicService.MusicBinder) service;
            Log.e(TAG, "onServiceConnected: " + "MainActivity 服务连接啦");
            //服务链接成功，判断播放按钮状态

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * 改变播放按钮状态
     */
    public void setPlayBtnStatus(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicBinder.isPlaying()){
                    playSong.setImageResource(R.mipmap.play_rdi_btn_pause);
                    dv.setMusicPlayStatus(DiscView.MusicPlayStatus.PLAY);
                    dv.playAnim();
                }else {
                    playSong.setImageResource(R.mipmap.play_rdi_btn_play);
                    dv.setMusicPlayStatus(DiscView.MusicPlayStatus.PAUSE);
                    dv.pauseAnim();
                }
            }
        });
    }
    DiscView.DiscChangeListener discChangeListener=new DiscView.DiscChangeListener() {
        @Override
        public void onActionbarChanged(PlayList.Music bean) {
            detailSong.setText(bean.getTitle());
            detailArtist.setText(bean.getArtist());
        }

        @Override
        public void onNext(final int position) {
           // Toast.makeText(PlayDetailActivity.this, "下一首", Toast.LENGTH_SHORT).show();
            if (musicBinder !=null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        musicBinder.play(position);
                        setPlayBtnStatus();
                    }
                }).start();

            }
        }

        @Override
        public void onItemClick() {
            Toast.makeText(PlayDetailActivity.this, "我被点击了", Toast.LENGTH_SHORT).show();
            lrcView.setVisibility(View.VISIBLE);
            dv.setVisibility(View.GONE);
            if (lrcBeen.isEmpty()){
                downloadLrc(music.getLrcUrl());
            }else {
                lrcView.setLrcBeen(lrcBeen);
            }
        }

        @Override
        public void onLast(final int position) {
           // Toast.makeText(PlayDetailActivity.this, "上一首", Toast.LENGTH_SHORT).show();
            if (musicBinder !=null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        musicBinder.play(position);
                        setPlayBtnStatus();
                    }
                }).start();

        }
        }
    };



}
