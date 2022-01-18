package com.jiangshan.knowledge.activity.person;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.GetSpecialContentApi;
import com.jiangshan.knowledge.http.entity.Article;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.view.web.WebViewJavaScriptFunction;
import com.jiangshan.knowledge.view.web.X5WebView;
import com.tencent.smtt.sdk.WebSettings;

/**
 * 免责声明
 * /article/specialContent/2
 * 关于我们
 * /article/specialContent/1
 * 常见问题
 * /article/specialContent/3
 * <p>
 * auth s_yz  2021/10/25
 */
public class SpecialContentActivity extends BaseActivity {

    private X5WebView newsWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_count);
        setBackViewVisiable();
        initView();

    }

    private void initView() {
        newsWebview = findViewById(R.id.news_webview);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        newsWebview.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        //自适应屏幕
        newsWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        newsWebview.getSettings().setLoadWithOverviewMode(true);

        newsWebview.getSettings().setDefaultFontSize(38);

        newsWebview.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {
                // TODO Auto-generated method stub

            }

            @JavascriptInterface
            public void onX5ButtonClicked() {
                enableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onCustomButtonClicked() {
                disableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onLiteWndButtonClicked() {
                enableLiteWndFunc();
            }

            @JavascriptInterface
            public void onPageVideoClicked() {
                enablePageVideoFunc();
            }
        }, "Android");

        int specialTypeId = getIntent().getIntExtra("specialTypeId", 0);
        getArticleDetail(specialTypeId);
    }

    private void getArticleDetail(int articleId) {
        if(-1==articleId){
            setTitle("隐私政策");
            newsWebview.loadUrl("https://img.51kpm.com/files/app/private_policy.html");
            return;
        }
        EasyHttp.get(this)
                .api(new GetSpecialContentApi().setSpecialTypeId(articleId).getApi())
                .request(new HttpCallback<HttpData<Article>>(this) {
                    @Override
                    public void onSucceed(HttpData<Article> result) {
                        if (result.isSuccess()) {
                            if (result.getData().getUrl() == null) {
                                setTitle(result.getData().getTitle());
                                newsWebview.loadData(result.getData().getContent(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
                            } else {
                                newsWebview.loadUrl(result.getData().getUrl());
                            }
                        }
                    }
                });
    }


    // /////////////////////////////////////////
    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (newsWebview.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            newsWebview.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        try {
            super.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableX5FullscreenFunc() {
        if (newsWebview.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            newsWebview.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (newsWebview.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            newsWebview.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (newsWebview.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            newsWebview.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }
}