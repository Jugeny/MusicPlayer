package com.example.administrator.mymusicapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.mymusicapp.fragment.tab.FoundFragment;
import com.example.administrator.mymusicapp.fragment.tab.FriendFragment;
import com.example.administrator.mymusicapp.fragment.tab.MyFragment;
import com.example.administrator.mymusicapp.service.MusicService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.d1)
    DrawerLayout d1;


    @BindView(R.id.vp_main)
    ViewPager vpMain;

    ArrayList<Fragment> fragments;
    ArrayList<ImageView> mImageViews;

    @BindView(R.id.iv_play_Status)
    ImageView playStatus;

    private ImageView my;
    private ImageView found;
    private ImageView friend;
    private PlayBroadcastReceiver mPlayBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        my = (ImageView) findViewById(R.id.my);
        found = (ImageView) findViewById(R.id.found);
        friend = (ImageView) findViewById(R.id.friend);

        mImageViews = new ArrayList<>();
        mImageViews.add(my);
        mImageViews.add(found);
        mImageViews.add(friend);

        fragments = new ArrayList<>();
        fragments.add(new MyFragment());
        fragments.add(new FoundFragment());
        fragments.add(new FriendFragment());
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        vpMain.setAdapter(adapter);
        vpMain.setCurrentItem(1);
        found.setSelected(true);
        vpMain.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switchTab(position);
            }
        });
        registerBroadcast();
        bindMusicService();
    }

    @OnClick(R.id.iv_play_Status)
    public void onViewClicked1() {
        if (musicBinder.isPlaying()){
            musicBinder.pause();
            playStatus.setImageResource(R.mipmap.a2s);
        }else {
            musicBinder.play();
            playStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
        }
    }

    class PlayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            playStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
        }
    }

    private void registerBroadcast() {
        mPlayBroadcastReceiver = new PlayBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Action.PLAY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPlayBroadcastReceiver, intentFilter);

    }

    public void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    MusicService.MusicBinder musicBinder;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayBroadcastReceiver);
    }

    private void switchTab(int position) {
        for (int i = 0; i < mImageViews.size(); i++) {
            if (i == position) {
                mImageViews.get(i).setSelected(true);
            } else {
                mImageViews.get(i).setSelected(false);
            }
        }
    }

    @OnClick(R.id.iv_menu)
    public void showMenu(View view) {
        d1.openDrawer(Gravity.LEFT);
    }

    @OnClick({R.id.my, R.id.found, R.id.friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my:
                vpMain.setCurrentItem(0);
                break;
            case R.id.found:
                vpMain.setCurrentItem(1);
                break;
            case R.id.friend:
                vpMain.setCurrentItem(2);
                break;
        }
    }


    class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

//    private void getHome() {
//        String url=Constant.URL.HOME+"?include=playList%2CplayList.author&";
//        OkHttpClient client=new OkHttpClient();
//        Request request= HttpUtils.requestGET(url);
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result=response.body().string();
//                Log.d(TAG,"onResponse:"+result);
//                Gson gson=new Gson();
//                final HomeResponse homeResponse=gson.fromJson(result,HomeResponse.class);
//                HashMap<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>> hashMap=new HashMap<>();
//                for (int i=0;i<homeResponse.getResults().size();i++){
//                    HomeResponse.ResultsBean resultsBean=homeResponse.getResults().get(i);
//                    HomeResponse.ResultsBean.PlayListBean playListBean=homeResponse.getResults().get(i).getPlayList();
//                    //获取“最新音乐”、“推荐音乐”
//                    String Item=resultsBean.getItem();
//                    if (hashMap.containsKey(Item)){
//                        //包含
//                        ArrayList<HomeResponse.ResultsBean.PlayListBean> resultsBeans=hashMap.get(Item);
//                        resultsBeans.add(playListBean);
//                    }else {
//                        //不包含Home.ResultsBean.PlayListBean使用类中的内部类中的内部类
//                        ArrayList<HomeResponse.ResultsBean.PlayListBean> resultBean=new ArrayList<HomeResponse.ResultsBean.PlayListBean>();
//                        resultBean.add(playListBean);
//                        hashMap.put(Item,resultBean);
//                    }
//
//                }
//                Set<Map.Entry<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>>> entrySet=hashMap.entrySet();
//                for (Map.Entry<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>> entry:entrySet){
//                    String name=entry.getKey();
//                    ArrayList<HomeResponse.ResultsBean.PlayListBean> playList=entry.getValue();
//                    Home home=new Home();
//                    home.setName(name);
//                    home.setPlayListBeen(playList);
//                    homes.add(home);
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        homeAdapter.notifyDataSetChanged();
//                    }
//                });
//                Log.d(TAG,"onResponse:" +homes);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode==111){
////            ArrayList<Home> results=data.getParcelableArrayListExtra("homes");
////            homes.clear();
////            homes.addAll(results);
////            homeAdapter.notifyDataSetChanged();
////            rv_home.scrollToPosition(0);
////        }
//    }
//
//    //获取广告数据
//    private void getBanner() {
//        String url = "https://leancloud.cn:443/1.1/classes/Banner?limit=6";
//        OkHttpClient client=new OkHttpClient();
//        Request request=new Request.Builder()
//                .url(url)
//                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
//                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
//                .addHeader("Content-Type", "application/json")
//                .get()
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result=response.body().string();
//                Log.d(TAG,"onResponse:"+result);
//                try{
//                    JSONObject jsonObject=new JSONObject(result);
//                    JSONArray jsonArray=jsonObject.getJSONArray("results");
//                    for (int i=0; i<jsonArray.length();i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String picurl = object.getString("picurl");
//                        String desc = object.getString("desc");
//                        String createdAt = object.getString("createdAt");
//                        String updatedAt = object.getString("updatedAt");
//                        String objectId = object.getString("objectId");
//                        Result resu = new Result(picurl, desc, createdAt, updatedAt, objectId);
//                        SmartImageView smartImageView = new SmartImageView(MainActivity.this);
//                        //如果是通过代码new出来的View，不能使用该方法，必须注动创建LayoutParams对象
//                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT);
//                        //设置View的参数
//                        smartImageView.setLayoutParams(layoutParams);
//                        //设置图片
//                        resu.setSmartImageView(smartImageView);
//                        results.add(resu);
//                        //更新ViewPager数据
//                        //不能在子线程更新UI      需要切换回主线程
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                bannerAdapter.notifyDataSetChanged();
//                            }
//                        });
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

}
