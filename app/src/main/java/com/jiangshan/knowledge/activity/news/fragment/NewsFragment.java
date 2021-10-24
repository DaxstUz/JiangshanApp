package com.jiangshan.knowledge.activity.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.jiangshan.knowledge.activity.BaseFragment;
import com.jiangshan.knowledge.activity.news.ArticleDetailActivity;
import com.jiangshan.knowledge.activity.news.adapter.NewsArticleAdapter;
import com.jiangshan.knowledge.http.api.SearchArticleApi;
import com.jiangshan.knowledge.http.entity.Article;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.view.CustomLoadMoreView;
import com.jiangshan.knowledge.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/10/9
 */
public class NewsFragment extends BaseFragment {

    private View view;

    private String categoryId;

    private RecyclerView rvNews;
    private NewsArticleAdapter newsArticleAdapter;
    private List<Article> datas = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNum = 1;

    public static NewsFragment newInstance(String categoryId) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        Bundle bundle = getArguments();
        categoryId = (String) bundle.getSerializable("categoryId");

        view = inflater.inflate(R.layout.fragment_news, container, false);
        initView();
        getNewsData();
        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView() {
        rvNews = view.findViewById(R.id.rv_news);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                swipeRefreshLayout.setRefreshing(true);
                newsArticleAdapter.getLoadMoreModule().setEnableLoadMore(false);
                getNewsData();
            }
        });

        newsArticleAdapter = new NewsArticleAdapter(R.layout.item_news_list, datas);
        newsArticleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra("aiticle", datas.get(position));
                startActivity(intent);
            }
        });
        //创建布局管理
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
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
        EasyHttp.get(this)
                .api(new SearchArticleApi().setCategoryId(categoryId).setPageNum(pageNum))
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
        if(1==pageNum){
            datas.clear();
        }
        datas.addAll(articles);
        newsArticleAdapter.notifyDataSetChanged();
    }
}
