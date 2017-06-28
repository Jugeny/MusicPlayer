package com.example.administrator.mymusicapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mymusicapp.PlayListActivity;
import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.bean.Home;
import com.example.administrator.mymusicapp.bean.HomeResponse;
import com.example.administrator.mymusicapp.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by Jugeny on 2017/6/9.
 */

public class HomeHeadFooterAdapter extends RecyclerView.Adapter {
    private static final int TYPE_HEAD = 0;//头布局
    private static final int TYPE_FOOTER = 1;//尾布局
    private static final int TYPE_NORMAL = 2;//默认的布局
    ArrayList<Home> homes;
    private View mHeadView;
    private View mFooterView;

    public HomeHeadFooterAdapter(ArrayList<Home> homes) {
        this.homes = homes;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent,false);
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
        ((ItemViewHolder) holder).tv_name.setText(homes.get(position).getName());
        ((ItemViewHolder) holder).tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemSelector !=null){
                    mItemSelector.selectorTab();
                }
            }
        });
        //新专辑上架 3
        //最新音乐 2
        //推荐歌单 1
        int spanCount=2;
        if (homes.get(position).getName().equals("新专辑上架")){
            spanCount=3;
        }else if (homes.get(position).getName().equals("最新音乐")){
            spanCount=2;
        }else if (homes.get(position).getName().equals("推荐歌单")){
            spanCount=1;
        }
        HomeAdapter adapter=new HomeAdapter(homes.get(position).getPlayListBeen());
        ((ItemViewHolder) holder).rl.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(),spanCount));
        ((ItemViewHolder) holder).rl.setAdapter(adapter);
    }
    class HomeAdapter extends RecyclerView.Adapter{
        ArrayList<HomeResponse.ResultsBean.PlayListBean> playListBeen;

        public HomeAdapter(ArrayList<HomeResponse.ResultsBean.PlayListBean> playListBeen) {
            this.playListBeen = playListBeen;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_child,parent,false);
            HomeItemViewHolder holder=new HomeItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            HomeItemViewHolder itemViewHolder= (HomeItemViewHolder) holder;
            final HomeResponse.ResultsBean.PlayListBean playListBean=playListBeen.get(position);
            itemViewHolder.siv.setImageUrl(playListBean.getPicUrl());
            itemViewHolder.tv_album.setText(playListBean.getPlayListName());
            itemViewHolder.tv_singer_name.setText(playListBean.getAuthor().getUsername());
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //启动歌单列表界面
                    //有View的地方就有上下文（Context）
                    Intent intent=new Intent(holder.itemView.getContext(), PlayListActivity.class);
                    intent.putExtra(PlayListActivity.AUTHOR_KEY,playListBean.getAuthor().getUsername());
                    intent.putExtra(PlayListActivity.PLAYLISTBEAN_KEY,playListBean);
                    ((Activity)(holder.itemView.getContext())).startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return playListBeen.size();
        }

        private class HomeItemViewHolder extends RecyclerView.ViewHolder {
            SmartImageView siv;
            TextView tv_album;
            TextView tv_singer_name;
            public HomeItemViewHolder(View itemView) {
                super(itemView);
                siv= (SmartImageView) itemView.findViewById(R.id.siv);
                tv_album= (TextView) itemView.findViewById(R.id.tv_album);
                tv_singer_name= (TextView) itemView.findViewById(R.id.tv_singer_name);
            }
        }
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
        return  homes.size() + (mHeadView != null ? 1 : 0) + (mFooterView != null ? 1 : 0);
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
        TextView tv_name;
        RecyclerView rl;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_name= (TextView) itemView.findViewById(R.id.tv_home_title);
            rl= (RecyclerView) itemView.findViewById(R.id.rl);
        }
    }
    private RecommendedItemSelector mItemSelector;//持有接口的引用

    public void setItemSelector(RecommendedItemSelector mItemSelector) {
        this.mItemSelector = mItemSelector;
    }

    public interface RecommendedItemSelector{
        void selectorTab();
    }
}
