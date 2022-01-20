package com.jiangshan.knowledge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hjq.http.EasyHttp;
import com.hjq.http.EasyLog;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.api.GetPassportApi;
import com.jiangshan.knowledge.http.entity.Passport;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.WindowPermissionCheck;

/**
 * auth s_yz  2022/1/15
 */
public class SplashActivity extends BaseActivity implements WindowPermissionCheck.OnWindowPermissionListener {

    private ImageView iv_welcome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv_welcome = (ImageView) findViewById(R.id.iv_welcome);

        Boolean permission=WindowPermissionCheck.checkPermission(this);
        if(permission){
            getInitData();
        }
        EasyLog.print("SplashActivity permission :"+permission);
    }

    private void getInitData() {
        EasyHttp.post(this)
                .api(new GetPassportApi())
                .request(new HttpCallback<HttpData<Passport>>(this) {

                    @Override
                    public void onSucceed(HttpData<Passport> result) {
                        Passport passport = result.getData();
                        if (null != passport && null != passport.getWelcomePicPath()) {
                            Glide.with(SplashActivity.this).load(passport.getWelcomePicPath()).into(iv_welcome);
                        } else {
                            Glide.with(SplashActivity.this).load(R.mipmap.welcome).into(iv_welcome);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                SplashActivity.this.finish();
                            }
                        }, 3000);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        Glide.with(SplashActivity.this).load(R.mipmap.welcome).into(iv_welcome);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        SplashActivity.this.finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        WindowPermissionCheck.onActivityResult(this,requestCode, resultCode, data,this);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess() {
        getInitData();
    }

    @Override
    public void onFailure() {
        finish();
    }
}
