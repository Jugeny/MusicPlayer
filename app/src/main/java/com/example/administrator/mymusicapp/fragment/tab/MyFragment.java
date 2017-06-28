package com.example.administrator.mymusicapp.fragment.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.activity.NearFriendActivity;

/**
 * Created by Jugeny on 2017/6/13.
 */

public class MyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my,null);
        final Button start= (Button) view.findViewById(R.id.startNearFriend);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), NearFriendActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
