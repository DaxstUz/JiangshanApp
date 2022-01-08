package com.jiangshan.knowledge.activity.person;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.ChangePhoneApi;
import com.jiangshan.knowledge.http.api.GetTicketApi;
import com.jiangshan.knowledge.http.api.SmsCodeApi;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.entity.UserWeixin;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

/**
 * auth s_yz  2022/1/7
 */
public class ChangePhoneActivity extends BaseActivity {

    private EditText etPhone;
    private EditText etCaptcha;
    private EditText etPhoneCode;

    private TextView tvPhoneCode;

    private ImageView ivCaptcha;
    private String ticket;

    private UserWeixin user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        setTitle("绑定手机号");
        setBackViewVisiable();

        initView();
        String userStr = LocalDataUtils.getLocalData(this, LocalDataUtils.localUserName, LocalDataUtils.keyUser);
        if (null != userStr) {
            user = new Gson().fromJson(userStr, UserWeixin.class);
        }
        getTicket();
    }

    int second = 60;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (second > 1) {
                second--;
                tvPhoneCode.setText(second + "秒后获取验证码");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendMessage(Message.obtain());
                    }
                }, 1000);
            } else {
                second = 60;
                tvPhoneCode.setText("获取验证码");
            }
        }
    };

    private void initView() {

        tvPhoneCode = findView(R.id.tv_phone_code);
        tvPhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (second == 60) {
                    handler.sendMessage(Message.obtain());
                    getSmsCode();
                }
            }
        });

        etPhone = findView(R.id.et_phone);
        etCaptcha = findView(R.id.et_captcha);
        etPhoneCode = findView(R.id.et_phone_code);

        ivCaptcha = findView(R.id.iv_captcha);
        ivCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ChangePhoneActivity.this).load("https://api.51kpm.com/app/passport/captcha/" + ticket + "?" + System.currentTimeMillis()).into(ivCaptcha);
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
                        Glide.with(ChangePhoneActivity.this).load("https://api.51kpm.com/app/passport/captcha/" + ticket + "?" + System.currentTimeMillis()).into(ivCaptcha);
                    }
                });

    }

    public void onMyCLick(View view) {
        if (R.id.ll_commit_psd == view.getId()) {
            changePsd();
        }
    }

    private void getSmsCode(){
        if (etPhone.getText().length() == 0) {
            ToastUtils.show("请输入手机号！");
            return;
        }
        if (etCaptcha.getText().length() == 0) {
            ToastUtils.show("请输入图形验证码！");
            return;
        }
                EasyHttp.post(this)
                .api(new SmsCodeApi().setCaptchaCode(etCaptcha.getText().toString()).setMobileNumber(etPhone.getText().toString()).setOpenid(user.getOpenId()).setTicket(ticket))
                .request(new HttpCallback<HttpData<String>>(this) {

                    @Override
                    public void onSucceed(HttpData<String> result) {
                        if (result.isSuccess()) {
                        } else {
                            ToastUtils.show(result.getMsg());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        ToastUtils.show(e.getMessage());
                    }
                });
    }

    private void changePsd() {//18900711210
        ChangePhoneApi changePonewApi = new ChangePhoneApi();

        if (etPhone.getText().length() == 0) {
            ToastUtils.show("请输入手机号！");
            return;
        }
        if (etCaptcha.getText().length() == 0) {
            ToastUtils.show("请输入图形验证码！");
            return;
        }
        if (etPhoneCode.getText().length() == 0) {
            ToastUtils.show("请输入手机验证码！");
            return;
        }
        changePonewApi.setSmsCode(etCaptcha.getText().toString()).setMobileNumber(etPhone.getText().toString()).setOpenid(user.getOpenId());
        EasyHttp.post(this)
                .api(changePonewApi)
                .request(new HttpCallback<HttpData<User>>(this) {

                    @Override
                    public void onSucceed(HttpData<User> result) {
                        if (result.isSuccess()) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            ToastUtils.show(result.getMsg());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        ToastUtils.show(e.getMessage());
                    }
                });
    }

}
