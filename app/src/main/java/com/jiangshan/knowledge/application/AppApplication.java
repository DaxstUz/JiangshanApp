package com.jiangshan.knowledge.application;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestInterceptor;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpParams;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.BuildConfig;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.model.RequestHandler;
import com.jiangshan.knowledge.http.server.ReleaseServer;
import com.jiangshan.knowledge.http.server.TestServer;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.jiangshan.knowledge.uitl.Utils;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mmkv.MMKV;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import okhttp3.OkHttpClient;

public class AppApplication extends Application {

    private static Application application;

    public AppApplication() {
        // TODO Auto-generated constructor stub
        application = this;
    }

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initUmeng();
        initHttp();
        initH5Web();
    }


    private void initUmeng() {

        UMConfigure.init(this, "6178aec6e0f9bb492b3ee55f"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, null);

//        PlatformConfig.setWeixin("wxfa7d7f1550fb111f", "0d31dc970eabf53d8c29e21a15911487"); //微信 appid appsecret
        PlatformConfig.setWeixin("wxfa7d7f1550fb111f", "0d31dc970eabf53d8c29e21a15911487"); //微信 appid appsecret
//        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
////        //新浪微博 appkey appsecret
//        PlatformConfig.setQQZone("1105588074", "r7lMWqiBL0YZBSU4");// QQ和Qzone appid appkey
    }

    private String token;

    private void initHttp() {
        ToastUtils.init(this);
        MMKV.initialize(this);

        // 网络请求框架初始化
        IRequestServer server;
        if (BuildConfig.DEBUG) {
            server = new TestServer();
        } else {
            server = new ReleaseServer();
        }

        User user;
        String userStr = LocalDataUtils.getLocalData(this, LocalDataUtils.localUserName, LocalDataUtils.keyUser);
        if (null != userStr) {
            user = new Gson().fromJson(userStr, User.class);
            if (null != user) {
                token = user.getToken();
            }
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                //.setLogEnabled(BuildConfig.DEBUG)
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler(this))
                // 设置请求参数拦截器
                .setInterceptor(new IRequestInterceptor() {
                    @Override
                    public void interceptArguments(IRequestApi api, HttpParams params, HttpHeaders headers) {
                        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    }
                })
                // 设置请求重试次数
                .setRetryCount(1)
                // 设置请求重试时间
                .setRetryTime(2000)
                // 添加全局请求参数
                .addParam("token", token)
                // 添加全局请求头
                .addHeader("Authorization", token)
                .into();
    }

    void initH5Web() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };

        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

}
