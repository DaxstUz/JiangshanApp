package com.jiangshan.knowledge.activity.person;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.GetTicketApi;
import com.jiangshan.knowledge.http.api.LoginApi;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

/**
 * 测试账号密码：15013211890/123456
 * auth s_yz  2021/10/13
 */
public class LoginActivity extends BaseActivity {

    private LinearLayout llLoginWeixin;
    private LinearLayout llLoginPhone;

    private EditText et_account;
    private EditText et_psd;

    private String ticket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
        setBackViewVisiable();

        initView();
        getTicket();
    }

    private void login() {
        if(et_psd.getText().length()!=0&&et_account.getText().length()!=0){
            EasyHttp.post(this)
                    .api(new LoginApi().setTicket(ticket).setMobileNumber(et_account.getText().toString()).setUserPassword(et_psd.getText().toString()))
                    .request(new HttpCallback<HttpData<User>>(this) {

                        @Override
                        public void onSucceed(HttpData<User> result) {
                            Gson gson = new Gson();
                            String user = gson.toJson(result.getData());
                            LocalDataUtils.saveLocalData(LoginActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyUser, user);
                            EasyConfig.getInstance().addParam("token", result.getData().getToken());
                            EasyConfig.getInstance().addHeader("Authorization", result.getData().getToken());
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onFail(Exception e) {
                            super.onFail(e);
                            ToastUtils.show("登录失败！");
                            getTicket();
                        }
                    });
        }else{
            ToastUtils.show("请输入账号和密码！");
        }
    }

    /**
     * 每次ticket只能用一次
     */
    private void getTicket() {
        EasyHttp.post(this)
                .api(new GetTicketApi())
                .request(new HttpCallback<HttpData<String>>(this) {

                    @Override
                    public void onSucceed(HttpData<String> result) {
                        ticket = result.getData();
                    }
                });

    }

    private void initView() {
        et_account = findViewById(R.id.et_account);
        et_psd = findViewById(R.id.et_psd);

        llLoginWeixin = findViewById(R.id.ll_login_weixin);
        llLoginPhone = findViewById(R.id.ll_login_phone);
        llLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=ticket&&ticket.length()>0){
                    login();
                }else{
                    ToastUtils.show("获取登录ticket失败！");
                }
            }
        });
    }
}
