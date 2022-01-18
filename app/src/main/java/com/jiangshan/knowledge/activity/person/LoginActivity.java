package com.jiangshan.knowledge.activity.person;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.EasyLog;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.GetMemberInfoApi;
import com.jiangshan.knowledge.http.api.GetPassportApi;
import com.jiangshan.knowledge.http.api.GetTicketApi;
import com.jiangshan.knowledge.http.api.LoginApi;
import com.jiangshan.knowledge.http.api.LoginWeixinApi;
import com.jiangshan.knowledge.http.entity.MemberInfo;
import com.jiangshan.knowledge.http.entity.Passport;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.entity.UserWeixin;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * 测试账号密码：15013211890/123456
 * auth s_yz  2021/10/13
 */
public class LoginActivity extends BaseActivity {

    private LinearLayout llLoginWeixin;
    private LinearLayout llLoginPhone;
    private LinearLayout llCaptcha;

    private EditText etAccount;
    private EditText etPsd;
    private EditText etCaptcha;

    private ImageView ivCaptcha;

    private TextView tv_private_policy;
    private CheckBox cb_private_policy;

    private String ticket;
    private int errorCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
//        setBackViewVisiable();
        initView();
        getTicket();
//        hideBottomUIMenu();
    }

    private void loginPhone() {//18900711210
        LoginApi loginApi = new LoginApi();
        if (etPsd.getText().length() != 0 && etAccount.getText().length() != 0) {
            if (errorCount >= 3) {
                if (etPsd.getText().length() != 0) {
                    loginApi.setCaptchaCode(etCaptcha.getText().toString());
                } else {
                    ToastUtils.show("请输入验证码！");
                    return;
                }
            }
            if(!cb_private_policy.isChecked()){
                ToastUtils.show("请同意隐私政策！");
                return;
            }

            EasyHttp.post(this)
                    .api(loginApi.setTicket(ticket).setMobileNumber(etAccount.getText().toString()).setUserPassword(etPsd.getText().toString()))
                    .request(new HttpCallback<HttpData<User>>(this) {

                        @Override
                        public void onSucceed(HttpData<User> result) {
                            if (result.isSuccess()) {
                                Gson gson = new Gson();
                                String user = gson.toJson(result.getData());
                                LocalDataUtils.saveLocalData(LoginActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyUser, user);
                                EasyConfig.getInstance().addParam("token", result.getData().getToken());
                                EasyConfig.getInstance().addHeader("Authorization", result.getData().getToken());
                                setResult(RESULT_OK);
                                getMemberData();
                                getInitData();
                                finish();
                            } else {
                                ToastUtils.show(result.getMsg());
                                errorCount++;
                                if (errorCount >= 3 || 412 == result.getCode() || 411 == result.getCode()) {
                                    llCaptcha.setVisibility(View.VISIBLE);
                                }
                                getTicket();
                            }
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

    private void getMemberData() {
        Subject subject = LocalDataUtils.getSubject(this);
        if(null==subject){
            return;
        }
        EasyHttp.get(this)
                .api(new GetMemberInfoApi().setSubjectCode(subject.getSubjectCode()))
                .request(new HttpCallback<HttpData<MemberInfo>>(this) {
                    @Override
                    public void onSucceed(HttpData<MemberInfo> result) {
                        if (result.isSuccess()) {
                            Gson gson = new Gson();
                            String member = gson.toJson(result.getData());
                            LocalDataUtils.saveLocalData(LoginActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyMember, member);
                        }
                    }
                });
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
                        Glide.with(LoginActivity.this).load("https://api.51kpm.com/app/passport/captcha/" + ticket + "?" + System.currentTimeMillis()).into(ivCaptcha);
                    }
                });

    }

    private void initView() {
        cb_private_policy = findViewById(R.id.cb_private_policy);

        tv_private_policy = findViewById(R.id.tv_private_policy);
        tv_private_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SpecialContentActivity.class);
                intent.putExtra("specialTypeId", -1);
                startActivity(intent);
            }
        });

        llCaptcha = findViewById(R.id.ll_captcha);
        etCaptcha = findViewById(R.id.et_captcha);
        ivCaptcha = findViewById(R.id.iv_captcha);
        ivCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(LoginActivity.this).load("https://api.51kpm.com/app/passport/captcha/" + ticket + "?" + System.currentTimeMillis()).into(ivCaptcha);
            }
        });

        etAccount = findViewById(R.id.et_account);
        etPsd = findViewById(R.id.et_psd);

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

        if(!cb_private_policy.isChecked()){
            ToastUtils.show("请同意隐私政策！");
            return;
        }

        UMShareAPI.get(this).getPlatformInfo(this, shareMedia, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                EasyLog.print(" platformLogin onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                EasyLog.print(" platformLogin onComplete" + new Gson().toJson(map));
                UserWeixin userWeixin = new Gson().fromJson(new Gson().toJson(map), UserWeixin.class);
                userWeixin.setOpenId(userWeixin.getOpenid());
                EasyConfig.getInstance().addParam("token", userWeixin.getToken());
                EasyConfig.getInstance().addHeader("Authorization", userWeixin.getToken());
                LocalDataUtils.saveLocalData(LoginActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyUser, new Gson().toJson(userWeixin));
                userWeixin.setTicket(ticket);
                loginWeixin(userWeixin);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtils.show(throwable.getMessage().substring(throwable.getMessage().indexOf("错误信息：") + 5));
                EasyLog.print(" platformLogin onError" + i + " error ===>" + throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
            }
        });
    }

    private void loginWeixin(UserWeixin user) {
        EasyHttp.post(this)
                .api(new LoginWeixinApi())
                .json(new Gson().toJson(user))
                .request(new HttpCallback<HttpData<User>>(this) {

                    @Override
                    public void onSucceed(HttpData<User> result) {
                        if (result.isSuccess()) {
                            Gson gson = new Gson();
                            String user = gson.toJson(result.getData());
//                            EasyLog.print("user ===>" + user);
                            LocalDataUtils.saveLocalData(LoginActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyUser, user);
                            EasyConfig.getInstance().addParam("token", result.getData().getToken());
                            EasyConfig.getInstance().addHeader("Authorization", result.getData().getToken());
                            setResult(RESULT_OK);
                            getInitData();

                            if (result.getData().getMobileNumber().length() == 0) {
                                Intent intent = new Intent(LoginActivity.this, ChangePhoneActivity.class);
                                intent.putExtra("firstChangePassword", 0);
                                LoginActivity.this.startActivity(intent);
                            } else if (result.getData().getFirstChangePassword() == 0) {
                                Intent intent = new Intent(LoginActivity.this, ChangePsdActivity.class);
                                intent.putExtra("firstChangePassword", 0);
                                LoginActivity.this.startActivity(intent);
                            }
                            finish();
                        }else{
                            if(100107==result.getCode()){
                                Intent intent = new Intent(LoginActivity.this, ChangePhoneActivity.class);
                                intent.putExtra("firstChangePassword", 0);
                                LoginActivity.this.startActivity(intent);
                            }
                            finish();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        ToastUtils.show("登录失败！");
                        getTicket();
                    }
                });
    }

    private void wXLaunchMiniProgram() {
        String appId = "wxfa7d7f1550fb111f"; // 填移动应用(App)的 AppId，非小程序的 AppID
        IWXAPI api = WXAPIFactory.createWXAPI(this, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_2886a12a246e"; // 填小程序原始id
        req.path = "pages/user/mine/mine?accessType=2";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    private void getInitData() {
        EasyHttp.post(this)
                .api(new GetPassportApi())
                .request(new HttpCallback<HttpData<Passport>>(this) {
                    @Override
                    public void onSucceed(HttpData<Passport> result) {
                        Passport passport = result.getData();
                        LocalDataUtils.saveLocalData(LoginActivity.this, LocalDataUtils.localUserName, LocalDataUtils.passport, new Gson().toJson(passport));
                    }
                });
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            _window.setAttributes(params);
        }
    }
}
