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
import com.jiangshan.knowledge.http.api.LoginWeixinApi;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.entity.UserWeixin;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    private void loginPhone() {
        if (et_psd.getText().length() != 0 && et_account.getText().length() != 0) {
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
        } else {
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
        llLoginWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                platformLogin(SHARE_MEDIA.WEIXIN);
            }
        });

        llLoginPhone = findViewById(R.id.ll_login_phone);
        llLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != ticket && ticket.length() > 0) {
                    loginPhone();
                } else {
                    ToastUtils.show("获取登录ticket失败！");
                }
            }
        });
    }

    private void platformLogin(SHARE_MEDIA shareMedia) {
        UMShareAPI.get(this).getPlatformInfo(this, shareMedia, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                System.out.println(" platformLogin onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                System.out.println(" platformLogin onComplete" + new Gson().toJson(map));
                UserWeixin userWeixin = new Gson().fromJson(new Gson().toJson(map), UserWeixin.class);
                loginWeixin(userWeixin);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtils.show(throwable.getMessage().substring(throwable.getMessage().indexOf("错误信息：") + 5));
//                System.out.println(" platformLogin onError"+i+" error ===>"+throwable.getMessage());

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
            }
        });
    }

    ;


    private void loginWeixin(UserWeixin user) {
        EasyHttp.post(this)
                .api(new LoginWeixinApi())
                .json(new Gson().toJson(user))
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
    }

}
