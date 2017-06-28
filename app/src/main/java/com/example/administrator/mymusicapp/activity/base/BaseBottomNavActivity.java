package com.example.administrator.mymusicapp.activity.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mymusicapp.Constant;
import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.bean.PlayList;
import com.example.administrator.mymusicapp.service.MusicService;

import java.util.ArrayList;

public class BaseBottomNavActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base_bottom_nav);
//    }
    public static String TAG = "BaseBottomNavActivity";
    protected MusicService.MusicBinder mMusicBinder;
    //播放按钮
    protected ImageView ivPlayStatus;
    //监听播放状态
    private MusicChangeBroadcastReceiver mPlayBroadcastReceiver;
    public BottomSheetBehavior mBehavior;
    PlayList currPlayList=new PlayList();
    PlayListAdapter mPlayListAdapter;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        //加载父类的布局
        View view= LayoutInflater.from(this).inflate(R.layout.activity_base_bottom_nav,null);
//        LinearLayout llContent= (LinearLayout) view.findViewById(R.id.ll_content);
//        llContent.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                return true;
//            }
//        });
        ImageButton ib= (ImageButton) view.findViewById(R.id.ib_bottom);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBehavior();
            }
        });
        initBottomView(view);
        FrameLayout root= (FrameLayout) view.findViewById(R.id.root);
        //加载子类布局
        View childView=LayoutInflater.from(this).inflate(layoutResID,root,false);
        root.addView(childView);
        setContentView(view);
        //获取类型名字
        TAG=getClass().getName();
        bindMusicService();
        registerBroadcast();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    public void bindMusicService(){
        Intent intent=new Intent(this,MusicService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }
    ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBinder= (MusicService.MusicBinder) service;
            //进入到界面，应该判断当前音乐的播放状态
            if (mMusicBinder.isPlaying()){
                ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
            }else {
                ivPlayStatus.setImageResource(R.mipmap.a2s);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 更新播放列表的状态
     * @param playList
     * @param currIndex
     */
    public void updatePlayListStatus(PlayList playList,int currIndex){

    }

    /**
     * 初始化底部按钮的点击事件
     * @param bottomView
     */
    private void initBottomView(View bottomView) {
        ivPlayStatus= (ImageView) bottomView.findViewById(R.id.iv_play_Status);
        ivPlayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicBinder !=null){
                    if (mMusicBinder.isPlaying()){
                        mMusicBinder.pause();
                        ivPlayStatus.setImageResource(R.mipmap.a2s);
                    }else {
                        mMusicBinder.play();
                        ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
                    }
                }
            }
        });
        LinearLayout llPlaylist= (LinearLayout) bottomView.findViewById(R.id.ll_playlist);
        ImageView ivShowList= (ImageView) bottomView.findViewById(R.id.iv_show_list);
        RecyclerView rv= (RecyclerView) bottomView.findViewById(R.id.rv_draw);
        mBehavior=BottomSheetBehavior.from(llPlaylist);

        ivShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //获取正在播放的歌单      使用临时变量记录获取到的数据
                PlayList tempCurrPlayList= MusicService.getCurrPlayList();
                if (tempCurrPlayList==null){
                    Toast.makeText(BaseBottomNavActivity.this, "播放列表为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //RecyclerView.Adapter 使用的是观察者模式
                //理解：观察者模式会一直去观察一个对象，如果对象的数据发生改变，那么就会自动或被动去更新（做一些操作）
                //不改变对象引用地址的情况下更新数据
                currPlayList.setMusics(tempCurrPlayList.getMusics());
                currPlayList.setObjectId(tempCurrPlayList.getObjectId());
                mPlayListAdapter.notifyDataSetChanged();
            }
        });
        initPlaylist(rv);
    }
    protected void hideBehavior() {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    private void initPlaylist(RecyclerView rv) {
//        ArrayList<String> strings=new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            strings.add(i+" ");
//        }

        rv.setLayoutManager(new LinearLayoutManager(this));
        mPlayListAdapter=new PlayListAdapter(currPlayList);
        rv.setAdapter(mPlayListAdapter);

    }
    class PlayListAdapter extends RecyclerView.Adapter{
//        ArrayList<String> strings;
        PlayList playList;

        public PlayListAdapter(PlayList playList) {
            this.playList = playList;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_nav_rv,parent,false);
            ItemViewHolder viewHolder=new ItemViewHolder(view);
            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ItemViewHolder viewHolder= (ItemViewHolder) holder;
            final PlayList.Music music=playList.getMusics().get(position);
            if (music.isPlayStatus()){
                viewHolder.playStatus.setVisibility(View.VISIBLE);
            }else {
                viewHolder.playStatus.setVisibility(View.GONE);
            }
            viewHolder.bottomTitle.setText(playList.getMusics().get(position).getTitle());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<playList.getMusics().size();i++){
                        playList.getMusics().get(i).setPlayStatus(false);
                    }
                    //获取点击的歌曲
                    music.setPlayStatus(true);
                    notifyDataSetChanged();
                    long start=System.currentTimeMillis();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mMusicBinder.play(playList);
                        }
                    }).start();
                    long end=System.currentTimeMillis();

                }
            });
        }

        @Override
        public int getItemCount() {
            return playList.getMusics().size();
        }


        private class ItemViewHolder extends RecyclerView.ViewHolder {
            ImageView playStatus;
            TextView bottomTitle;
            public ItemViewHolder(View itemView) {
                super(itemView);
                playStatus= (ImageView) itemView.findViewById(R.id.iv_bottom_status);
                bottomTitle= (TextView) itemView.findViewById(R.id.tv_bottom_title);
            }
        }
    }
    private void registerBroadcast(){
        mPlayBroadcastReceiver=new MusicChangeBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Constant.Action.PLAY);
        intentFilter.addAction(Constant.Action.PAUSE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPlayBroadcastReceiver,intentFilter);

    }

//音乐播放的广播
    class MusicChangeBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //如果监听两个广播，那么需要判断接收到的广播类型
            switch (intent.getAction()){
                case Constant.Action.PLAY:
                    ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
                    musicStatusChange();
                    break;
                case Constant.Action.PAUSE:
                    ivPlayStatus.setImageResource(R.mipmap.a2s);
                    break;
            }
        }
    }
    //音乐的状态发送改变（播放新的音乐）
public void musicStatusChange(){}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面销毁，需要解绑服务和 广播
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayBroadcastReceiver);
        unbindService(mConnection);
    }
}
