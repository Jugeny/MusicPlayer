package com.example.administrator.mymusicapp.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.bean.NewPlayListResultsBean;
import com.example.administrator.mymusicapp.bean.PlayList;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. 新建类，继承RelativeLayout
 */
public class DiscView extends RelativeLayout {
    private static final String TAG = "DiscView";
    ViewPager mVp;
    ImageView iv_needle;
    List<View> mViews;
    //歌曲列表
    //ArrayList<NewPlayListResultsBean> mResultsBeen;
    ArrayList<ObjectAnimator> mObjectAnimators=new ArrayList<>();
     // 唱针抬起角度
    private float NEEDLE_UP_ROTATION;
     // 唱盘距顶部大小
    private float DISC_MARGIN_TOP;
    //比例
    private static float RATIO = 0;
    private float NEEDLE_WIDTH;
    private float NEEDLE_HEIGHT;

    //唱针距离左边大小
    private float NEEDLE_MARGIN_LEFT;
    //中心点
    private float NEEDLE_PIVOT_X;
    //中心点
    private float NEEDLE_PIVOT_Y;
    //唱针距离顶部大小
    private float NEEDLE_MARGIN_TOP;
    //唱盘大小
    private float DISC_SIZE;
    //唱盘白色背景大小
    private float DISC_BG_SIZE;
    //唱盘头像大小
    private float PIC_SIZE;
    private VPAdapter mVpAdapter;
    private ImageView bgDisc;
    private ImageView iv;
    private ImageView ivHead;

    public DiscView(Context context) {
        this(context, null);
    }

    public DiscView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        initSize();
        //加载布局
        // this 表示的是 DiscView
        // 传入this 表示当前加载的布局的父View
        View.inflate(context, R.layout.layout_discview, this);
        initDiscBg();
        initNeedle();
        initViewPager(context);
    }


    public void initDiscBg() {
        bgDisc = (ImageView) findViewById(R.id.iv_disc_bg);
        LayoutParams params = (LayoutParams) bgDisc.getLayoutParams();
        params.width = (int) DISC_BG_SIZE;
        params.height = (int) DISC_BG_SIZE;
        params.topMargin = (int) DISC_MARGIN_TOP;
        bgDisc.setLayoutParams(params);
    }

    /**
     * 初始化唱针
     */
    public void initNeedle() {
        iv_needle = (ImageView) findViewById(R.id.iv_needle);
        LayoutParams params = (LayoutParams) iv_needle.getLayoutParams();
        params.height = (int) NEEDLE_HEIGHT;
        params.width = (int) NEEDLE_WIDTH;
        params.leftMargin = (int) NEEDLE_MARGIN_LEFT;
        params.topMargin = (int) NEEDLE_MARGIN_TOP * -1;
        iv_needle.setPivotY(NEEDLE_PIVOT_Y);
        iv_needle.setPivotX(NEEDLE_PIVOT_X);
        iv_needle.setLayoutParams(params);
    }
    /**
     * ViewPager 里面的View 使用集合来存储
     * 监听ViewPager的滑动时间
     */
    public void initViewPager(Context context) {
        mVp = (ViewPager) findViewById(R.id.vp_disc);
        //mResultsBeen = new ArrayList<>();
        mViews = new ArrayList<>();


        mVpAdapter = new VPAdapter();
        mVp.setAdapter(mVpAdapter);

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPositionOffsetPixels;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //positionOffset
                Log.e(TAG, "positionOffset: " + positionOffset);
                Log.e(TAG, "positionOffsetPixels: " + positionOffsetPixels);
                if (lastPositionOffsetPixels > positionOffsetPixels) {
                    //Log.e(TAG, "onPageScrolled: 右滑"  );
                    if (positionOffset < 0.5) {
                        Log.e(TAG, "onPageScrolled: 更新标题");
                        if (mDiscChangeListener != null) {
                            mDiscChangeListener.onActionbarChanged(mPlayList.getMusics().get(position));
                        }
                    } else {
                        mDiscChangeListener.onActionbarChanged(mPlayList.getMusics().get(mVp.getCurrentItem()));
                    }

                } else if (lastPositionOffsetPixels < positionOffsetPixels) {
                    if (positionOffset > 0.5) {
                        Log.e(TAG, "onPageScrolled: 更新标题");
                        if (mDiscChangeListener != null) {
                            mDiscChangeListener.onActionbarChanged(mPlayList.getMusics().get(position + 1));
                        }
                    } else {
                        mDiscChangeListener.onActionbarChanged(mPlayList.getMusics().get(position));
                    }
                }
                lastPositionOffsetPixels = positionOffsetPixels;
            }
            //记录上一次选中的下标
            int currentPosition=0;


            @Override
            public void onPageSelected(int position) {
                if (position>currentPosition){
                    //下一首
                    mDiscChangeListener.onNext(position);
                }else {
                    //上一首
                    mDiscChangeListener.onLast(position);
                }
                currentPosition=position;

            }
            /**
             * 1  正在滑动
             * 2  滑动完毕
             * 0  静止
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e(TAG, "onPageScrollStateChanged:   " + state);
                pageScrollOperation(state);
            }
        });
    }


    PlayList mPlayList;
    public void setMusicData(PlayList playList, int position) {
        mPlayList=playList;
//        //清楚缓存数据
//        mResultsBeen.clear();
//        mResultsBeen.addAll(resultsBeen);
        for (int i = 0; i <mPlayList.getMusics().size() ; i++) {
            PlayList.Music bean =mPlayList.getMusics().get(i);
            //循环歌曲列表，创建View
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_vi_disc, mVp, false);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDiscChangeListener!=null){
                        mDiscChangeListener.onItemClick();
                    }
                }
            });
            itemView.setPivotY(DISC_MARGIN_TOP+(DISC_SIZE/2));
            itemView.setPivotX(((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth()/2);
            //黑色边框
            iv = (ImageView) itemView.findViewById(R.id.iv_dic2);
            LayoutParams params = (LayoutParams) iv.getLayoutParams();
            params.height = (int) DISC_SIZE;
            params.width = (int) DISC_SIZE;
            params.topMargin = (int) DISC_MARGIN_TOP;
            iv.setLayoutParams(params);
            iv.setImageResource(R.mipmap.play_disc);
            //专辑头像
            ivHead = (ImageView) itemView.findViewById(R.id.iv_pic_disc);
            LayoutParams picParams = (LayoutParams) ivHead.getLayoutParams();
            picParams.width = (int) PIC_SIZE;
            picParams.height = (int) PIC_SIZE;
            //设置顶部距离
            picParams.topMargin = (int) (DISC_MARGIN_TOP + ((DISC_SIZE - PIC_SIZE) / 2));
            ivHead.setLayoutParams(picParams);
            //TODO
            if (bean.getAlbumPicUrl() != null) {
                String url = bean.getAlbumPicUrl();
                Glide.with(getContext()).load(url).into(ivHead);
            }
            ObjectAnimator animator=ObjectAnimator.ofFloat(itemView,View.ROTATION,0,360);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setDuration(10000);
            if (i==position){
                animator.start();
            }
            mObjectAnimators.add(animator);
            mViews.add(itemView);
        }
        mVpAdapter.notifyDataSetChanged();
        mVp.setCurrentItem(position);
    }


    public DiscChangeListener mDiscChangeListener;
    //接口回调
    /**
     * DiscView 发送变动的接口回调
     */
    public interface DiscChangeListener {
        //标题栏更新
        public void onActionbarChanged(PlayList.Music bean);
        //下一首
        public  void onNext(int position);
        //上一首
        public void onLast(int position);
        void onItemClick();

    }
    public void setDiscChangListener(DiscChangeListener discChangeListener) {
        this.mDiscChangeListener = discChangeListener;
    }
    public void pageScrollOperation(int state) {
        switch (state) {
            //滑动完毕和静止状态
            case ViewPager.SCROLL_STATE_IDLE:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                needleDown();
                mObjectAnimators.get(mVp.getCurrentItem()).start();
                break;
            case ViewPager.SCROLL_STATE_DRAGGING://如果是滑动状态，那么需要将唱针抬起
                needleUp();
                mObjectAnimators.get(mVp.getCurrentItem()).pause();//取消动画
                break;

        }
    }


    class VPAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }
    }

    public void needleUp(){
        ObjectAnimator animatorUp=ObjectAnimator.ofFloat(iv_needle,View.ROTATION,0,NEEDLE_UP_ROTATION);
        animatorUp.setDuration(500);
        animatorUp.setInterpolator(new LinearInterpolator());
        animatorUp.start();
    }
    public void needleDown(){
        ObjectAnimator animatorDown=ObjectAnimator.ofFloat(iv_needle,View.ROTATION,NEEDLE_UP_ROTATION,0);
        animatorDown.setDuration(500);
        animatorDown.setInterpolator(new LinearInterpolator());
        animatorDown.start();
    }
    //记录当前播放的状态
    int mMusicPlayStatus=MusicPlayStatus.PLAY;
    public void setMusicPlayStatus(int status){
        mMusicPlayStatus=status;
    }
    //播放状态
    public interface MusicPlayStatus{
        int PLAY=0;
        int PAUSE=1;
    }
    public void pauseAnim(){
        needleUp();
        mObjectAnimators.get(mVp.getCurrentItem()).pause();
        mMusicPlayStatus=MusicPlayStatus.PAUSE;
    }
    public void playAnim(){
        //播放
        needleDown();
        mMusicPlayStatus=MusicPlayStatus.PLAY;
        mObjectAnimators.get(mVp.getCurrentItem()).resume();
    }

//    public void pause(){
//        //应该获取到当前View的播放状态：如果正在播放，就需要执行暂停；如果是暂停，就需要播放
//        if (mMusicPlayStatus==MusicPlayStatus.PLAY){
//            //暂停
//            needleUp();
//            mObjectAnimators.get(mVp.getCurrentItem()).pause();
//            mMusicPlayStatus=MusicPlayStatus.PAUSE;
//        }else if (mMusicPlayStatus==MusicPlayStatus.PAUSE){
//            //播放
//            needleDown();
//            mMusicPlayStatus=MusicPlayStatus.PLAY;
//            mObjectAnimators.get(mVp.getCurrentItem()).resume();
//        }
//    }
    //播放上一首歌
    public void playLast(){
        mVp.setCurrentItem(mVp.getCurrentItem()-1,true);
    }
    //播放下一首
    public void playNext(){
        mVp.setCurrentItem(mVp.getCurrentItem()+1,true);
    }

    /**
     * 初始化View大小值
     */
    public void initSize() {
        RATIO = 1F * (getResources().getDisplayMetrics().heightPixels / 1920.0F);
        NEEDLE_UP_ROTATION = -30;

        /*唱针宽高、距离等比例*/
        NEEDLE_WIDTH = (float) (276.0 * RATIO);
        NEEDLE_HEIGHT = (float) (413.0 * RATIO);

        NEEDLE_MARGIN_LEFT = (float) (550.0 * RATIO);
        NEEDLE_PIVOT_X = (float) (43.0 * RATIO);
        NEEDLE_PIVOT_Y = (float) (43.0 * RATIO);
        NEEDLE_MARGIN_TOP = (float) (43.0 * RATIO);

        /*唱盘比例*/
        DISC_SIZE = (float) (804.0 * RATIO);
        DISC_BG_SIZE = (float) (810.0 * RATIO);

        DISC_MARGIN_TOP = (190 * RATIO);

        /*专辑图片比例*/
        PIC_SIZE = (float) (533.0 * RATIO);


    }
}
