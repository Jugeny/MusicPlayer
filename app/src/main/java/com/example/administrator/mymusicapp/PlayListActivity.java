package com.example.administrator.mymusicapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.mymusicapp.activity.base.BaseBottomNavActivity;
import com.example.administrator.mymusicapp.adapter.PlayListAdapter;
import com.example.administrator.mymusicapp.bean.HomeResponse;
import com.example.administrator.mymusicapp.bean.NewPlayListResponse;
import com.example.administrator.mymusicapp.bean.NewPlayListResultsBean;
import com.example.administrator.mymusicapp.bean.PlayList;
import com.example.administrator.mymusicapp.loopj.android.image.SmartImageView;
import com.example.administrator.mymusicapp.service.MusicService;
import com.example.administrator.mymusicapp.utils.HttpUtils;
import com.example.administrator.mymusicapp.utils.MyMusicUrlJoint;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayListActivity extends BaseBottomNavActivity {
    //歌单对象
    PlayList mPlayList=new PlayList();
    public static final String OBJECTID_KEY = "objectId";
    public static final String PLAYLISTBEAN_KEY = "PlayListBean";
    public static final String AUTHOR_KEY = "AUTHOR";
    private static final String TAG = "PlayListActivity";
    //MusicService.MusicBinder mMusicBinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_actionbar)
    RelativeLayout rlActionbar;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;


//    @BindView(R.id.activity_play_list)
//    FrameLayout activityPlayList;
//    @BindView(R.id.iv_play_Status)
//    ImageView ivPlayStatus;
//  @BindView(R.id.sv_playlist_head)
//    SmartImageView svPlaylistHead;
    private HomeResponse.ResultsBean.PlayListBean mPlayListBean;
    ArrayList<NewPlayListResultsBean> mResultsBeans;
    private PlayListAdapter mPlayListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        ButterKnife.bind(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rlActionbar.getLayoutParams();
        layoutParams.height = layoutParams.height + getStatusBarHeight(this);
        rlActionbar.setLayoutParams(layoutParams);


        mPlayListBean = getIntent().getParcelableExtra(PLAYLISTBEAN_KEY);
        String author = getIntent().getStringExtra(AUTHOR_KEY);
        mResultsBeans = new ArrayList<>();
        getNewPlayList(mPlayListBean.getObjectId());
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mPlayListAdapter = new PlayListAdapter(mPlayList);
        View headView = LayoutInflater.from(this).inflate(R.layout.layout_playlist_head, rvList, false);
        ImageView head_iv_bg = (ImageView) headView.findViewById(R.id.bg_head);
        //模糊背景
        Glide.with(this)
                .load(mPlayListBean.getPicUrl())
                .bitmapTransform(new BlurTransformation(this, 10, 5) {
                })
                .into(head_iv_bg);
        ImageView iv_play = (ImageView) headView.findViewById(R.id.iv_play);
        iv_play.setColorFilter(Color.BLACK);
        //设置歌单封面
        SmartImageView siv_pic = (SmartImageView) headView.findViewById(R.id.siv_pic);
        siv_pic.setImageUrl(mPlayListBean.getPicUrl());
        //设置歌单和作者名字
        TextView tv_playListName = (TextView) headView.findViewById(R.id.tv_playListName);
        TextView tv_author = (TextView) headView.findViewById(R.id.tv_author);
        tv_playListName.setText(mPlayListBean.getPlayListName());
        tv_author.setText(author);
        RelativeLayout ll = (RelativeLayout) headView.findViewById(R.id.ll);
        RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        rlParams.topMargin = layoutParams.height + getStatusBarHeight(this);
        ll.setLayoutParams(rlParams);

        mPlayListAdapter.setHeadView(headView);
        rvList.setAdapter(mPlayListAdapter);
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Log.e(TAG, "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View headView = null;
                //获取第一个Item
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int findFirstVisibleItemPosition = manager.findFirstVisibleItemPosition();//获取屏幕显示的第一个条目的下标
                if (findFirstVisibleItemPosition == 0) {
                    headView = recyclerView.getChildAt(findFirstVisibleItemPosition);
                }
                float alpha = 0;
                //如果headView不是空的，那么标题View不透明
                if (headView == null) {
                    alpha = 1;
                } else {
                    alpha = Math.abs(headView.getTop() * 0.1f / headView.getHeight());
                }
                if (alpha > 0.5) {
                    tvTitle.setText(mPlayListBean.getPlayListName());

                } else {
                    tvTitle.setText("歌单");
                }
                ivBg.setAlpha(alpha);
            }
        });
        registerBroadcast();
       // bindMusicService();
    }

    @Override
    public void musicStatusChange() {
        PlayList currPlayList=MusicService.getCurrPlayList();
        if (mPlayList.getObjectId().equals(currPlayList.getMusics())){
            mPlayList.setMusics(currPlayList.getMusics());
            mPlayListAdapter.notifyDataSetChanged();
        }

    }

    private static int getStatusBarHeight(Context context) {
        //获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private void getNewPlayList(String objectId) {
        OkHttpClient client = new OkHttpClient();
        String url = Constant.URL.NEWPLAYLIST + MyMusicUrlJoint.getNewPlayListUrl(objectId);
        Request request = HttpUtils.requestGET(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "onResponse:" + result);
                NewPlayListResponse newPlayListResponse = new Gson().fromJson(result, NewPlayListResponse.class);
                //将所有数据添加到adapter持有的集合里面
                mResultsBeans.addAll(newPlayListResponse.getResults());
                mPlayList.setObjectId(mPlayListBean.getObjectId());
                ArrayList<PlayList.Music> musics=new ArrayList<PlayList.Music>();
                //歌曲列表
                for(int i=0;i<newPlayListResponse.getResults().size();i++){
                    NewPlayListResultsBean bean=newPlayListResponse.getResults().get(i);
                    String albumPic=bean.getAlbumPic()==null ? "":bean.getAlbumPic().getUrl();
                    //歌词下载链接
                    String lrcUrl=bean.getLrc()==null ? "":bean.getLrc().getUrl();
                    PlayList.Music music=new PlayList.Music(
                            bean.getObjectId(),
                            bean.getTitle(),
                            bean.getArtist(),
                            bean.getFileUrl().getUrl(),
                            albumPic,
                            bean.getAlbum(),
                            lrcUrl
                    );
                    //直接使用服务的静态方法
                    int currIndex=MusicService.getCurrPlayIndex();
                    PlayList playList=MusicService.getCurrPlayList();
                    if (currIndex !=-1&& playList!=null && playList.getObjectId().equals(mPlayListBean.getObjectId())){
                        if (i==currIndex){
                            music.setPlayStatus(true);
                        }

                    }
                    musics.add(music);
                }
                mPlayList.setMusics(musics);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPlayListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void registerBroadcast() {
        PlayBroadcastReceiver play = new PlayBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Action.ACTION_PLAY);
        LocalBroadcastManager.getInstance(this).registerReceiver(play, intentFilter);
    }

    @OnClick(R.id.iv_back)
    void onBack() {
        finish();
    }

    class PlayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "已成功接收到广播", Toast.LENGTH_SHORT).show();
            PlayList bean = intent.getParcelableExtra(PlayListAdapter.PLAYDATA_KEY);
            //tvName.setText(bean.getTitle());
            //svPlaylistHead.setImageUrl(bean.getAlbumPic().getUrl());
            mMusicBinder.play(bean);
            ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
        }
    }
}
