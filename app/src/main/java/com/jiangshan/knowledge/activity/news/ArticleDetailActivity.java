package com.jiangshan.knowledge.activity.news;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.GetArticleApi;
import com.jiangshan.knowledge.http.api.GetExamFouseDetailApi;
import com.jiangshan.knowledge.http.entity.Article;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.view.web.WebViewJavaScriptFunction;
import com.jiangshan.knowledge.view.web.X5WebView;
import com.tencent.smtt.sdk.WebSettings;

/**
 * auth s_yz  2021/10/10
 */
public class ArticleDetailActivity extends BaseActivity {

    private X5WebView newsWebview;

    private ImageView iv_operate;
    private LinearLayout ll_operate;

    private Article article;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        initView();
    }

    private void initView() {
        setBackViewVisiable();
        Article aiticle = (Article) getIntent().getSerializableExtra("aiticle");
        setTitle(aiticle.getTitle());

        newsWebview = findViewById(R.id.news_webview);

        iv_operate = findViewById(R.id.iv_operate);
        iv_operate.setImageResource(R.mipmap.operate_share);

        ll_operate = findViewById(R.id.ll_operate);
        ll_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(0, article.getTitle(), article.getIntro(), article.getUrl());
            }
        });

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        newsWebview.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        //???????????????
        newsWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        newsWebview.getSettings().setLoadWithOverviewMode(true);

//        newsWebview.getSettings().setDefaultFontSize(35);

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


        boolean examFouse = getIntent().getBooleanExtra("examFouse", false);
        if (examFouse) {
            getExamFouseDetail(aiticle.getId());
        } else {
            getArticleDetail(aiticle.getId());
        }
    }

    private void getExamFouseDetail(int articleId) {
        EasyHttp.get(this)
                .api(new GetExamFouseDetailApi().setArticleId(articleId).getApi())
                .request(new HttpCallback<HttpData<Article>>(this) {
                    @Override
                    public void onSucceed(HttpData<Article> result) {
                        if (result.isSuccess()) {
                            article = result.getData();
                            ll_operate.setVisibility(View.VISIBLE);
                            if (result.getData().getUrl() == null) {
                                String contentData = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"  \n" +
                                        " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">  \n" +
                                        " <html xmlns=\"http://www.w3.org/1999/xhtml\">  \n" +
                                        " <html lang=\"zh-CN\">  \n" +
                                        " <head>  \n" +
                                        " <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">  \n" +
                                        " <meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;\" name=\"viewport\">  \n" +
                                        " <title>xxxxx</title>  \n" +
                                        " </head>  \n" +
                                        "   <body>" + result.getData().getContent() +
                                        "</body> " +
                                        " </html> ";
                                contentData=contentData.replaceAll("</p><p><br/></p><p>","<br/>");
                                contentData=contentData.replaceAll("</p><p>","<br/>");
                                contentData=contentData.replaceAll("</p><p>","<br/>");
                                contentData=contentData.replaceAll("<p>","");
                                contentData=contentData.replaceAll("</p>","");
                                newsWebview.loadData(contentData, "text/html; charset=UTF-8", null);//??????????????????????????????
                            } else {
                                newsWebview.loadUrl(result.getData().getUrl());
                            }
                        }
                    }
                });
    }

    private void getArticleDetail(int articleId) {
        EasyHttp.get(this)
                .api(new GetArticleApi().setArticleId(articleId).getApi())
                .request(new HttpCallback<HttpData<Article>>(this) {
                    @Override
                    public void onSucceed(HttpData<Article> result) {
                        if (result.isSuccess()) {
                            article = result.getData();
                            ll_operate.setVisibility(View.VISIBLE);
                            if (result.getData().getUrl() == null) {
                                String contentData = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"  \n" +
                                        " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">  \n" +
                                        " <html xmlns=\"http://www.w3.org/1999/xhtml\">  \n" +
                                        " <html lang=\"zh-CN\">  \n" +
                                        " <head>  \n" +
                                        " <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">  \n" +
                                        " <meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;\" name=\"viewport\">  \n" +
                                        " <title>xxxxx</title>  \n" +
                                        " </head>  \n" +
                                        "   <body> \n" + result.getData().getContent() +
                                        "</body>  \n" +
                                        " </html> ";
                                contentData=contentData.replaceAll("</p><p><br/></p><p>","<br/>");
                                contentData=contentData.replaceAll("</p><p>","<br/>");
                                contentData=contentData.replaceAll("<p>","");
                                contentData=contentData.replaceAll("</p>","");
                                newsWebview.loadData(contentData, "text/html; charset=UTF-8", null);//??????????????????????????????
                            } else {
                                newsWebview.loadUrl(result.getData().getUrl());
                            }
                        }
                    }
                });
    }


    // /////////////////////////////////////////
    // ???webview????????????
    private void enableX5FullscreenFunc() {

        if (newsWebview.getX5WebViewExtension() != null) {
            Toast.makeText(this, "??????X5??????????????????", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true?????????????????????false??????X5????????????????????????false???

            data.putBoolean("supportLiteWnd", false);// false??????????????????true?????????????????????????????????true???

            data.putInt("DefaultVideoScreen", 2);// 1??????????????????????????????2?????????????????????????????????????????????1

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
            Toast.makeText(this, "??????webkit????????????", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true??????????????????????????????onShowCustomView()???false??????X5????????????????????????false???

            data.putBoolean("supportLiteWnd", false);// false??????????????????true?????????????????????????????????true???

            data.putInt("DefaultVideoScreen", 2);// 1??????????????????????????????2?????????????????????????????????????????????1

            newsWebview.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (newsWebview.getX5WebViewExtension() != null) {
            Toast.makeText(this, "??????????????????", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true??????????????????????????????onShowCustomView()???false??????X5????????????????????????false???

            data.putBoolean("supportLiteWnd", true);// false??????????????????true?????????????????????????????????true???

            data.putInt("DefaultVideoScreen", 2);// 1??????????????????????????????2?????????????????????????????????????????????1

            newsWebview.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (newsWebview.getX5WebViewExtension() != null) {
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true??????????????????????????????onShowCustomView()???false??????X5????????????????????????false???

            data.putBoolean("supportLiteWnd", false);// false??????????????????true?????????????????????????????????true???

            data.putInt("DefaultVideoScreen", 1);// 1??????????????????????????????2?????????????????????????????????????????????1

            newsWebview.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }
}
