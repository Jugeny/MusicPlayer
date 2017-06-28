package com.example.administrator.mymusicapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mymusicapp.Constant;
import com.example.administrator.mymusicapp.PlayDetailActivity;
import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.bean.NewPlayListResultsBean;
import com.example.administrator.mymusicapp.bean.PlayList;

import java.util.ArrayList;

/**
 * Created by Jugeny on 2017/6/11.
 */

public class PlayListAdapter extends RecyclerView.Adapter {
    private static final int TYPE_HEAD = 0;//头布局
    private static final int TYPE_FOOTER = 1;//尾布局
    private static final int TYPE_NORMAL = 2;//默认的布局
    public static final String PLAYDATA_KEY = "playData";

    //ArrayList<NewPlayListResultsBean> mResultsBeen;
    PlayList mPlayList;
    //记录上一次操作的bean,再次点击，修改该bean播放状态，并且重写赋值
    //NewPlayListResultsBean mLastBean;
    PlayList.Music mLastBean;

    private View mHeadView;
    private View mFooterView;

//    public PlayListAdapter(ArrayList<NewPlayListResultsBean> mResultsBeen) {
//        this.mResultsBeen = mResultsBeen;
//    }
    public PlayListAdapter(PlayList playList){
        this.mPlayList=playList;
    }
    /**
     * 设置头部的View
     * @param view
     */
    public void setHeadView(View view) {
        mHeadView = view;
    }

    /**
     * 设置尾部的View
     * @param view
     */
    public void setFooterView(View view) {
        mFooterView = view;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadView != null && position == 0) {
            return TYPE_HEAD;
        }
        if (mFooterView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //判断当前是否应该设置头布局
        if (viewType==TYPE_HEAD){
            return new HeadViewHolder(mHeadView);
        }
        if (viewType==TYPE_FOOTER){
            return new FooterViewHolder(mFooterView);
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        ItemViewHolder itemViewHolder=new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (isHead(position)){
            return;
        }
        if (isFooter(position)){
            return;
        }
        if ((mHeadView !=null)){
            position=position-1;

        }
        //final NewPlayListResultsBean bean=mResultsBeen.get(position);
        final PlayList.Music bean=mPlayList.getMusics().get(position);
        final ItemViewHolder item= (ItemViewHolder) holder;

        item.tv_name.setText(bean.getTitle()+"");
        item.tv_album.setText(bean.getArtist()+"-"+bean.getAlbum()+"");
        if (bean.isPlayStatus()){
            item.iv_playStatus.setVisibility(View.VISIBLE);
            item.tv_number.setVisibility(View.INVISIBLE);
        }else {
            item.iv_playStatus.setVisibility(View.INVISIBLE);
            item.tv_number.setVisibility(View.VISIBLE);
            item.tv_number.setText((position+1)+"");
        }
        final int finalPosition=position;
        item.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前条目和最后一次记录的对象是否相等
                if (bean.isPlayStatus()){
                    Intent intent=new Intent(holder.itemView.getContext(), PlayDetailActivity.class);
                    //因为在播放详情界面，需要拿到所有的歌曲列表，所以需要将集合传递给PlayDetailActivity
                    intent.putExtra(PlayDetailActivity.RESULTSBEEN_KEY,mPlayList);
                    intent.putExtra(PlayDetailActivity.INDEX_KEY,finalPosition);
                    intent.putExtra(PlayDetailActivity.DETAILS_KEY,bean);
                    ((Activity) holder.itemView.getContext()).startActivity(intent);
                }else {
//                    if (mLastBean !=null){
//                        mLastBean.setPlayStatus(false);
//                    }
                    for (int i=0;i<mPlayList.getMusics().size();i++){
                        mPlayList.getMusics().get(i).setPlayStatus(false);
                    }
                    bean.setPlayStatus(true);
                    notifyDataSetChanged();
                    //mLastBean=bean;//重新赋值
                    //本地广播
                    Intent intent=new Intent(Constant.Action.ACTION_PLAY);
                    intent.putExtra(PLAYDATA_KEY,mPlayList);
                    //获取广播管理器
                    LocalBroadcastManager manager=LocalBroadcastManager.getInstance(holder.itemView.getContext());
                    manager.sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayList.getMusics().size() + (mHeadView != null ? 1 : 0) + (mFooterView != null ? 1 : 0);
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder {
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_number;
        TextView tv_name;
        TextView tv_album;
        ImageView iv_playStatus;
        public ItemViewHolder(View view) {
            super(view);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_album = (TextView) itemView.findViewById(R.id.tv_album);
            iv_playStatus= (ImageView) itemView.findViewById(R.id.iv_playstatus);
        }
    }
    private boolean isHead(int position) {
        return mHeadView != null && position == 0;
    }

    private boolean isFooter(int position) {
        //getItemCount 获取item的个数
        return mFooterView != null && position == getItemCount() - 1;
    }
}
