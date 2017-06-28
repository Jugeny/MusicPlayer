package com.example.administrator.mymusicapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mymusicapp.Constant;

import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.adapter.MusicListAdapter;
import com.example.administrator.mymusicapp.bean.MusicListResponse;
import com.example.administrator.mymusicapp.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jugeny on 2017/6/13.
 */

public class PlayListFragment extends Fragment {
    private static final String TAG ="PlayListFragment" ;
    ArrayList<MusicListResponse.ResultsBean> musics=new ArrayList<>();

    private MusicListAdapter musicAdapter;
    RecyclerView rv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playlist,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv= (RecyclerView) view.findViewById(R.id.rv_playList);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        musicAdapter=new MusicListAdapter(musics);
        View headView= LayoutInflater.from(getActivity()).inflate(R.layout.layout_list_head,rv,false);
        musicAdapter.setHeadView(headView);
        rv.setAdapter(musicAdapter);
        getData();
    }

    private void getData() {
        String url= Constant.URL.PLAYLIST+"?include=int&";
        OkHttpClient client=new OkHttpClient();
        Request request= HttpUtils.requestGET(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Log.d(TAG,"onResponse:"+result);
                MusicListResponse list=new Gson().fromJson(result,MusicListResponse.class);
                musics.addAll(list.getResults());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        musicAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

    }
}
