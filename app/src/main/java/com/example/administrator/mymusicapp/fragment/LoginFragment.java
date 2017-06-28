package com.example.administrator.mymusicapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mymusicapp.MainActivity;
import com.example.administrator.mymusicapp.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Jugeny on 2017/6/5.
 */

public class LoginFragment extends BaseFragment {
    private static final String TAG ="LoginFragment" ;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.bt_login)
    Button btLogin;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * View 创建完毕后会执行的方法，在改方法里面可以获取View，设置View的监听等操作
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_register, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_register:
                RegisterFragment registerFragment=new RegisterFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.login_content,registerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.bt_login:
                String name=etName.getText().toString();
                String password=etPassword.getText().toString();
                if (name==null || "".equals(name)){
                    Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                }else if(name.length()<3||name.length()>10){
                    Toast.makeText(getActivity(), "用户名只允许3~10个字符", Toast.LENGTH_SHORT).show();
                }
                if (password==null || "".equals(password)){
                    Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
                }else if(password.length()<3||password.length()>10){
                    Toast.makeText(getActivity(), "密码只允许3~10个字符", Toast.LENGTH_SHORT).show();
                }else {
                    login(name, password);
                }
                break;
        }
    }

    private void login(String name, String password) {
        showProgressDialog("登录中…");
//        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
        OkHttpClient client=new OkHttpClient();
        String url="https://leancloud.cn:443/1.1/login?username=" + name + "&password=" + password;
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
                e.printStackTrace();
                Log.e(TAG,"登录失败");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result=response.body().string();
                Log.d(TAG,"onResponse:"+result);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code()==200){//获取请结果码
                            Intent intent=new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                        }
                        closeProgressDialog();
                    }
                });
            }
        });


    }
}
