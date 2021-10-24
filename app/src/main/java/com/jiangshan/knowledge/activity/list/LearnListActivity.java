package com.jiangshan.knowledge.activity.list;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.list.adapter.RankAdapter;
import com.jiangshan.knowledge.http.api.GetRankApi;
import com.jiangshan.knowledge.http.entity.Rank;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.jiangshan.knowledge.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

public class LearnListActivity extends BaseActivity {

    private RecyclerView rvNews;

    private RankAdapter rankAdapter;

    private List<Rank> datas = new ArrayList<>();

//    private RadioButton rbWeek;
//    private RadioButton rbDay;
//    private RadioButton rbMonth;

    private RadioGroup rgRank;

    private SwipeRefreshLayout swipeLayout;

    String rankType = "day";
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_list);
        setTitle("江山老师题库");
        initView();

        initLoadMore();

        getRankData();
    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        rankAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        rankAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNum++;
                getRankData();
            }
        });
        rankAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        rankAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void initView() {
        rvNews = findViewById(R.id.rv_news);
//        rbWeek = findViewById(R.id.rb_week);
//        rbDay = findViewById(R.id.rb_day);
//        rbMonth = findViewById(R.id.rb_month);

        swipeLayout = findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                swipeLayout.setRefreshing(true);
                rankAdapter.getLoadMoreModule().setEnableLoadMore(false);
                getRankData();
            }
        });

        rgRank = findViewById(R.id.rg_rank);
        rgRank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_day:
                        rankType = GetRankApi.rankTypeDay;
                        break;
                    case R.id.rb_week:
                        rankType = GetRankApi.rankTypeWeek;
                        break;
                    case R.id.rb_month:
                        rankType = GetRankApi.rankTypeYear;
                        break;
                }
                pageNum = 1;
                swipeLayout.setRefreshing(true);
                rankAdapter.getLoadMoreModule().setEnableLoadMore(false);
                getRankData();
            }

        });

        rankAdapter = new RankAdapter(R.layout.item_rank, datas);
        rvNews.setAdapter(rankAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getRankData() {
        Subject subject = LocalDataUtils.getSubject(this);
        EasyHttp.get(this)
                .api(new GetRankApi().setSujectCode(subject.getSubjectCode()).setPageNum(pageNum).setRankType(rankType))
                .request(new HttpCallback<HttpListData<Rank>>(this) {
                    @Override
                    public void onSucceed(HttpListData<Rank> result) {

                        if (result.isSuccess()) {
                            swipeLayout.setRefreshing(false);
                            rankAdapter.getLoadMoreModule().setEnableLoadMore(true);
                            if (result.getData().getList().size() < result.getData().getPageSize()) {
                                //如果不够一页,显示没有更多数据布局
                                rankAdapter.getLoadMoreModule().loadMoreEnd();
                            } else {
                                rankAdapter.getLoadMoreModule().loadMoreComplete();
                            }
                            if (1 == pageNum) {
                                datas.clear();
                            }
                            datas.addAll(result.getData().getList());
                            rankAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        swipeLayout.setRefreshing(false);
                        rankAdapter.getLoadMoreModule().setEnableLoadMore(true);
                        rankAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }

}
