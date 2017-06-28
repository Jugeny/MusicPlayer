package com.example.administrator.mymusicapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.mymusicapp.bean.LrcBean;
import com.example.administrator.mymusicapp.service.MusicService;

import java.util.ArrayList;

/**
 * Created by Jugeny on 2017/6/22.
 */

public class LrcView extends View {
    ArrayList<LrcBean> lrcBeen;
    Paint mPaint;
    Paint mHPaint;
    int mWidth=0;
    int mHeight=0;
    public LrcView(Context context) {
        this(context,null);
    }

    public LrcView(Context context,@Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LrcView(Context context,@Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(30);
        //高亮
        mHPaint=new Paint();
        mHPaint.setColor(Color.RED);
        mHPaint.setAntiAlias(true);
        mHPaint.setTextAlign(Paint.Align.CENTER);
        mHPaint.setTextSize(30);
    }

    public void setLrcBeen(ArrayList<LrcBean> lrcData) {
        this.lrcBeen = lrcData;
        invalidate();
    }
    int lastPosition=0;
    //获取当前位置的歌词
    int currentPosition=0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth=getWidth();
        mHeight=getHeight();
        if (lrcBeen==null || lrcBeen.isEmpty()){
            canvas.drawText("没有歌词",mWidth/2,mHeight/2,mPaint);
            return;
        }
        //当前播放时间
        int currentMill= MusicService.getCurrentPosition();
        //找出和时间匹配的下标
        for (int i=0;i<lrcBeen.size();i++){
            if (currentMill >=lrcBeen.get(i).getStartTime()&& currentMill<lrcBeen.get(i).getEndTime()){
                currentPosition=i;
            }
        }
        drawLrc(canvas);
        long start=lrcBeen.get(currentPosition).getStartTime();
        int y = currentMill >start ? currentPosition * 50 : lastPosition * 50;
        setScrollY(y);
        lastPosition=currentPosition;
        postInvalidateDelayed(100);
    }
    public void drawLrc(Canvas canvas){
        for (int i=0;i<lrcBeen.size();i++){
            if (i==currentPosition) {
                canvas.drawText(lrcBeen.get(i).getContent(), mWidth / 2, mHeight / 2 + 50 * i, mHPaint);
            }else {
                canvas.drawText(lrcBeen.get(i).getContent(), mWidth / 2, mHeight / 2 + 50 * i, mPaint);
            }
        }
    }
}
