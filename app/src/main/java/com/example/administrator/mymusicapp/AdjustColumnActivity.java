package com.example.administrator.mymusicapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mymusicapp.adapter.MyAdapter;
import com.example.administrator.mymusicapp.bean.Home;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class AdjustColumnActivity extends AppCompatActivity {

    private static final String TAG ="AdjustColumnActivity" ;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_adjust)
    RecyclerView rvAdjust;
    private ArrayList<Home> homes;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_column);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("homes",homes);
                setResult(222, intent);
                finish();
            }
        });

        //如果传递对象类型中包含自定义类型的ArrayList，那么该自定义类型也需要实现Parcelable接口
        homes = getIntent().getParcelableArrayListExtra("homes");
        Log.d(TAG,"onCreate:"+homes);
        rvAdjust.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(homes, this);
        //adapter=new MyAdapter();

        rvAdjust.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //viewHolder 拖动的ViewHolder
                //target  目标的ViewHolder
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
//            Log.e(TAG, "onMove: viewHolder " +fromPosition  +"   target" + toPosition);


                //交换集合中的数据
                Collections.swap(homes, fromPosition, toPosition);


                //交换ViewHolder
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        helper.attachToRecyclerView(rvAdjust);

    }
//    class MyAdapter extends RecyclerView.Adapter{
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(AdjustColumnActivity.this).inflate(R.layout.item_txt,null);
//            MyViewHolder myViewHolder = new MyViewHolder(view);
//            return myViewHolder;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder{
//            TextView tv;
//            public MyViewHolder(View itemView) {
//                super(itemView);
//                tv = (TextView) itemView.findViewById(R.id.txt);
//            }
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            Home home = homes.get(position);
//
//            ((MyViewHolder)holder).tv.setText(home.getName());
//        }
//
//        @Override
//        public int getItemCount() {
//            return homes.size();
//        }
//    }

}
