package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.ExamFouseAdapter;
import com.jiangshan.knowledge.activity.news.ArticleDetailActivity;
import com.jiangshan.knowledge.activity.news.adapter.NewsArticleAdapter;
import com.jiangshan.knowledge.http.api.GetExamFouseApi;
import com.jiangshan.knowledge.http.api.SearchArticleApi;
import com.jiangshan.knowledge.http.entity.Article;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.jiangshan.knowledge.view.CustomLoadMoreView;
import com.jiangshan.knowledge.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/10/21
 */
public class ExamFocusListActivity extends BaseActivity {

    private RecyclerView rvNews;
    private ExamFouseAdapter newsArticleAdapter;
    private List<Article> datas = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_rember);
        setTitle("江山老师题库");
        setBackViewVisiable();

        initView();
        getNewsData();
    }

    private void initView() {
        rvNews = findViewById(R.id.rv_news);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                swipeRefreshLayout.setRefreshing(true);
                newsArticleAdapter.getLoadMoreModule().setEnableLoadMore(false);
                getNewsData();
            }
        });

        newsArticleAdapter = new ExamFouseAdapter(R.layout.item_exam_fouse_list, datas);
        newsArticleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(ExamFocusListActivity.this, ArticleDetailActivity.class);
                intent.putExtra("aiticle", datas.get(position));
                intent.putExtra("examFouse", true);
                startActivity(intent);
            }
        });
        //创建布局管理
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(newsArticleAdapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        rvNews.addItemDecoration(decoration);

        initLoadMore();
    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        newsArticleAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        newsArticleAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNum++;
                getNewsData();
            }
        });
        newsArticleAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        newsArticleAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void getNewsData() {
        Subject subject= LocalDataUtils.getSubject(this);
        Course course=LocalDataUtils.getCourse(this);
        EasyHttp.get(this)
                .api(new GetExamFouseApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setPageNum(pageNum))
                .request(new HttpCallback<HttpListData<Article>>(this) {

                    @Override
                    public void onSucceed(HttpListData<Article> result) {
                        if (result.isSuccess()) {
                            swipeRefreshLayout.setRefreshing(false);
                            newsArticleAdapter.getLoadMoreModule().setEnableLoadMore(true);
                            if (result.getData().getList().size() < result.getData().getPageSize()) {
                                //如果不够一页,显示没有更多数据布局
                                newsArticleAdapter.getLoadMoreModule().loadMoreEnd();
                            } else {
                                newsArticleAdapter.getLoadMoreModule().loadMoreComplete();
                            }
                            analyzeData(result.getData().getList());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        swipeRefreshLayout.setRefreshing(false);
                        newsArticleAdapter.getLoadMoreModule().setEnableLoadMore(true);
                        newsArticleAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }

    private void analyzeData(List<Article> articles) {
        if (1 == pageNum) {
            datas.clear();
        }
        datas.addAll(articles);
        newsArticleAdapter.notifyDataSetChanged();
    }

}

