package com.example.administrator.mymusicapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.mymusicapp.AdjustColumnActivity;
import com.example.administrator.mymusicapp.Constant;
import com.example.administrator.mymusicapp.MainActivity;
import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.adapter.BannerAdapter;
import com.example.administrator.mymusicapp.adapter.HomeHeadFooterAdapter;
import com.example.administrator.mymusicapp.bean.Home;
import com.example.administrator.mymusicapp.bean.HomeResponse;
import com.example.administrator.mymusicapp.bean.Result;
import com.example.administrator.mymusicapp.loopj.android.image.SmartImageView;
import com.example.administrator.mymusicapp.utils.HttpUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.administrator.mymusicapp.R.id.vp;


/**
 * Created by Jugeny on 2017/6/13.
 */

public class RecommendFragment extends Fragment {
    private static final String TAG ="RecommendFragment" ;
    ArrayList<Result> results=new ArrayList<>();
    private HomeHeadFooterAdapter homeHeadFooterAdapter;
    ArrayList<Home> homes = new ArrayList<>();
    private BannerAdapter bannerAdapter;
    private ViewPager vp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recommend,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv= (RecyclerView) view.findViewById(R.id.rv_recommend);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeHeadFooterAdapter=new HomeHeadFooterAdapter(homes);
        View headView= LayoutInflater.from(getActivity()).inflate(R.layout.layout_home_header,null);
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_home_footer,null);
        Button btnAdjust= (Button) footerView.findViewById(R.id.btn_adjust);
        btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),AdjustColumnActivity.class);
                intent.putParcelableArrayListExtra("homes",homes);
                startActivityForResult(intent,111);
            }
        });
        homeHeadFooterAdapter.setHeadView(headView);
        homeHeadFooterAdapter.setFooterView(footerView);
        rv.setAdapter(homeHeadFooterAdapter);
        vp = (ViewPager) headView.findViewById(R.id.vp_head);
        //homeHeadFooterAdapter.setItemSelector(tempRecommendItemSelector);
        bannerAdapter=new BannerAdapter(results);
        //vp.setAdapter(bannerAdapter);
        getBanner();
        getHome();
    }
    HomeHeadFooterAdapter.RecommendedItemSelector tempRecommendItemSelector;
    public void setItemSelector(HomeHeadFooterAdapter.RecommendedItemSelector selector){
        tempRecommendItemSelector=selector;
    }
    //获取广告数据
    private void getBanner() {
        String url = "https://leancloud.cn:443/1.1/classes/Banner?limit=6";
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Log.d(TAG,"onResponse"+result);
                try{
                    JSONObject jsonObject=new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("results");
                    for (int i=0; i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String picurl = object.getString("picurl");
                        String desc = object.getString("desc");
                        String createdAt = object.getString("createdAt");
                        String updatedAt = object.getString("updatedAt");
                        String objectId = object.getString("objectId");
                        Result resu = new Result(picurl, desc, createdAt, updatedAt, objectId);
                        SmartImageView smartImageView = new SmartImageView(getActivity());
                        //如果是通过代码new出来的View，不能使用该方法，必须注动创建LayoutParams对象
                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        //设置View的参数
                        smartImageView.setLayoutParams(layoutParams);
                        //设置图片
                        resu.setSmartImageView(smartImageView);
                        results.add(resu);
                        //更新ViewPager数据
                        //不能在子线程更新UI      需要切换回主线程
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             //   bannerAdapter.notifyDataSetChanged();
                                vp.setAdapter(bannerAdapter);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getHome() {
        String url= Constant.URL.HOME+"?include=playList%2CplayList.author&";
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
                Gson gson=new Gson();
                final HomeResponse homeResponse=gson.fromJson(result,HomeResponse.class);
                HashMap<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>> hashMap=new HashMap<>();
                for (int i=0;i<homeResponse.getResults().size();i++){
                    HomeResponse.ResultsBean resultsBean=homeResponse.getResults().get(i);
                    HomeResponse.ResultsBean.PlayListBean playListBean=homeResponse.getResults().get(i).getPlayList();
                    //获取“最新音乐”、“推荐音乐”
                    String Item=resultsBean.getItem();
                    if (hashMap.containsKey(Item)){
                        //包含
                        ArrayList<HomeResponse.ResultsBean.PlayListBean> resultsBeans=hashMap.get(Item);
                        resultsBeans.add(playListBean);
                    }else {
                        //不包含Home.ResultsBean.PlayListBean使用类中的内部类中的内部类
                        ArrayList<HomeResponse.ResultsBean.PlayListBean> resultBean=new ArrayList<HomeResponse.ResultsBean.PlayListBean>();
                        resultBean.add(playListBean);
                        hashMap.put(Item,resultBean);
                    }

                }
                Set<Map.Entry<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>>> entrySet=hashMap.entrySet();
                for (Map.Entry<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>> entry:entrySet){
                    String name=entry.getKey();
                    ArrayList<HomeResponse.ResultsBean.PlayListBean> playList=entry.getValue();
                    Home home=new Home();
                    home.setName(name);
                    home.setPlayListBeen(playList);
                    homes.add(home);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeHeadFooterAdapter.notifyDataSetChanged();
                    }
                });
                Log.d(TAG,"onResponse:" +homes);
            }
        });
    }


}
