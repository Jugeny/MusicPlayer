package com.example.administrator.mymusicapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.bean.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Jugeny on 2017/6/6.
 */

public class RegisterFragment extends BaseFragment {
    private static final String TAG = "RegisterFragment";
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_register)
    Button btRegister;
    Unbinder unbinder;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void register(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        //将两个对象封装成Json对象{"password":"123456","username":"lff"}
        LoginResponse user = new LoginResponse(username, password);
        //将LoginResponse转为Json格式的数据
        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.i(TAG, "register" + json);
        //告诉服务器，上传的格式是json格式
        MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSONTYPE, json);
        String url = "https://leancloud.cn:443/1.1/users";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "run:" + result);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (response.code() == 201) {
                            Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                        }else if (response.code()==202){
                            Toast.makeText(getActivity(), "用户名已经存在", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                        }
                        closeProgressDialog();
                    }
                });
            }
        });
    }

    @OnClick({R.id.ib_back, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                ((Activity) view.getContext()).onBackPressed();
                break;
            case R.id.bt_register:
                showProgressDialog("请等待…");
                String username = etName.getText().toString();
                String password = etPassword.getText().toString();
                if (username == null || "".equals(username)) {
                    Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 3 || username.length() > 10) {
                    Toast.makeText(getActivity(), "用户名只允许3~10个字符", Toast.LENGTH_SHORT).show();
                }
                if (password == null || "".equals(password)) {
                    Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 3 || password.length() > 10) {
                    Toast.makeText(getActivity(), "密码只允许3~10个字符", Toast.LENGTH_SHORT).show();
                } else {
                    register(username, password);
                }
                break;
        }
    }
}
