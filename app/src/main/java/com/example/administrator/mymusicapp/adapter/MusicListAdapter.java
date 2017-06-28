package com.example.administrator.mymusicapp.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.bean.MusicListResponse;
import com.example.administrator.mymusicapp.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by Jugeny on 2017/6/13.
 */

public class MusicListAdapter extends RecyclerView.Adapter {
    private static final int TYPE_HEAD = 0;//头布局
    private static final int TYPE_FOOTER = 1;//尾布局
    private static final int TYPE_NORMAL = 2;//默认的布局
    ArrayList<MusicListResponse.ResultsBean> musicList;
    private View mHeadView;
    private View mFooterView;

    public MusicListAdapter(ArrayList<MusicListResponse.ResultsBean> musicList) {
        this.musicList = musicList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        if (manager instanceof  GridLayoutManager){
            final GridLayoutManager gridLayoutManager= (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemType=getItemViewType(position);
                    if (mHeadView !=null&&itemType==TYPE_HEAD){
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;//所占位置
                }
            });
        }
    }

    public void setHeadView(View view) {
        mHeadView = view;
    }

    public void setFooterView(View view) {
        mFooterView = view;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadView !=null && position==0){
            return TYPE_HEAD;
        }
        if (mFooterView !=null && position==getItemCount()-1){
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //判断当前是否应该设置头布局
        if (viewType == TYPE_HEAD) {
            return new HeadViewHolder(mHeadView);
        }
        if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(mFooterView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_list, parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHead(position)){
            return;
        }
        if (isFooter(position)){
            return;
        }
        //这里需要注意，取值应该是从position-1 开始，因为position ==0 已经被mHeadView占用了
        if (mHeadView !=null){
            position=position-1;
        }
        ItemViewHolder item= (ItemViewHolder) holder;
        final MusicListResponse.ResultsBean musicBean=musicList.get(position);
        item.sivList.setImageUrl(musicBean.getPicUrl().getUrl());
        item.tvIntro.setText(musicBean.getName());

    }
    private boolean isHead(int position) {
        return mHeadView != null && position == 0;
    }

    private boolean isFooter(int position) {
        //getItemCount 获取item的个数
        return mFooterView != null && position == getItemCount() - 1;
    }
    @Override
    public int getItemCount() {
        return  musicList.size() + (mHeadView != null ? 1 : 0) + (mFooterView != null ? 1 : 0);
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
       SmartImageView sivList;
        TextView tvIntro;
        public ItemViewHolder(View itemView) {
            super(itemView);
            sivList= (SmartImageView) itemView.findViewById(R.id.siv_list);
            tvIntro= (TextView) itemView.findViewById(R.id.tv_list_intro);
        }
    }
}
