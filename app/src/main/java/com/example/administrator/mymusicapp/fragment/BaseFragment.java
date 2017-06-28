package com.example.administrator.mymusicapp.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;

/**
 * Created by Jugeny on 2017/6/7.
 */

public class BaseFragment extends Fragment {
    ProgressDialog mProgressDialog;

    /**
     * 显示提示框
     * @param msg 提示的文本
     */
    public void showProgressDialog(String msg){
        if (mProgressDialog==null){
            mProgressDialog=new ProgressDialog(getActivity());
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    public void closeProgressDialog(){
        if (mProgressDialog !=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
