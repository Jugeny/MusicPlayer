package com.example.administrator.mymusicapp.fragment.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.adapter.HomeHeadFooterAdapter;
import com.example.administrator.mymusicapp.fragment.PlayListFragment;
import com.example.administrator.mymusicapp.fragment.RecommendFragment;

import java.util.ArrayList;

/**
 * Created by Jugeny on 2017/6/12.
 */

public class FoundFragment extends Fragment {
    TabLayout tl;
    ViewPager vpFound;
    ArrayList<FoundItem> foundItems;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_found,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tl= (TabLayout) view.findViewById(R.id.tl);
        vpFound= (ViewPager) view.findViewById(R.id.vp_found);
        foundItems=new ArrayList<>();
        //只是创建了RecommendFragment的对象，并没有执行Fragment中的生命周期方法
        //生命周期方法只有在讲Fragment显示，或者加载的时候执行
        RecommendFragment recommendFragment=new RecommendFragment();

        foundItems.add(new FoundItem("个性推荐",recommendFragment));
        foundItems.add(new FoundItem("歌单",new PlayListFragment()));
        FoundAdapter adapter=new FoundAdapter(getChildFragmentManager());
        vpFound.setAdapter(adapter);
        tl.setupWithViewPager(vpFound);
        recommendFragment.setItemSelector(new HomeHeadFooterAdapter.RecommendedItemSelector() {
            @Override
            public void selectorTab() {
                vpFound.setCurrentItem(1);
            }
        });
    }
    class FoundAdapter extends FragmentPagerAdapter{

        public FoundAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return foundItems.get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return foundItems.get(position).getF();
        }

        @Override
        public int getCount() {
            return foundItems.size();
        }
    }
    class FoundItem{
        String title;
        Fragment f;

        public FoundItem(String title, Fragment f) {
            this.title = title;
            this.f = f;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Fragment getF() {
            return f;
        }

        public void setF(Fragment f) {
            this.f = f;
        }
    }
}
