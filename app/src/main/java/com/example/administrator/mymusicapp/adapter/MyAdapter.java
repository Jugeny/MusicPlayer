package com.example.administrator.mymusicapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.bean.Home;

import java.util.ArrayList;

/**
 * Created by Jugeny on 2017/6/8.
 */

public class MyAdapter extends RecyclerView.Adapter {
    ArrayList<Home> homes=new ArrayList<>();
    Context context;

    public MyAdapter(ArrayList<Home> homes, Context context) {
        this.homes = homes;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Home home=homes.get(position);
        ((ItemViewHolder) holder).txt.setText(home.getName());
    }

    @Override
    public int getItemCount() {
        return homes.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public ItemViewHolder(View itemView) {
            super(itemView);
            txt= (TextView) itemView.findViewById(R.id.txt);
        }
    }
}
