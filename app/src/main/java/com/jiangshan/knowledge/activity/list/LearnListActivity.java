package com.jiangshan.knowledge.activity.list;

import static com.umeng.socialize.utils.ContextUtil.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.SubjectDetailActivity;
import com.jiangshan.knowledge.activity.list.adapter.RankAdapter;
import com.jiangshan.knowledge.http.api.GetMyRankApi;
import com.jiangshan.knowledge.http.api.GetRankApi;
import com.jiangshan.knowledge.http.entity.Rank;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.jiangshan.knowledge.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

public class LearnListActivity extends BaseActivity {

    private RecyclerView rvNews;
    private RankAdapter rankAdapter;

    private List<Rank> datas = new ArrayList<>();
    private RadioGroup rgRank;

    private SwipeRefreshLayout swipeLayout;

    private String rankType = "day";
    private int pageNum = 1;

    private TextView tvRankName;
    private TextView tvRate;
    private TextView tvNo;
    private TextView tvRise;
    private TextView tvAnserInfo;
    private ImageView ivRate;

    private CircularImageView iv_user_head_one;
    private TextView tv_rank_name_one;
    private TextView tv_anser_info_one;
    private TextView tv_rate_one;

    private CircularImageView iv_user_head_two;
    private TextView tv_rank_name_two;
    private TextView tv_anser_info_two;
    private TextView tv_rate_two;

    private CircularImageView iv_user_head_three;
    private TextView tv_rank_name_three;
    private TextView tv_anser_info_three;
    private TextView tv_rate_three;

    private LinearLayout llMy;

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

        llMy = findViewById(R.id.ll_my);

        iv_user_head_one = findViewById(R.id.iv_user_head_one);
        tv_rank_name_one = findViewById(R.id.tv_rank_name_one);
        tv_anser_info_one = findViewById(R.id.tv_anser_info_one);
        tv_rate_one = findViewById(R.id.tv_rate_one);

        iv_user_head_two = findViewById(R.id.iv_user_head_two);
        tv_rank_name_two = findViewById(R.id.tv_rank_name_two);
        tv_anser_info_two = findViewById(R.id.tv_anser_info_two);
        tv_rate_two = findViewById(R.id.tv_rate_two);

        iv_user_head_three = findViewById(R.id.iv_user_head_three);
        tv_rank_name_three = findViewById(R.id.tv_rank_name_three);
        tv_anser_info_three = findViewById(R.id.tv_anser_info_three);
        tv_rate_three = findViewById(R.id.tv_rate_three);

        tvRankName = findViewById(R.id.tv_rank_name);
        tvRate = findViewById(R.id.tv_rate);
        tvNo = findViewById(R.id.tv_no);
        tvRise = findViewById(R.id.tv_rise);
        tvAnserInfo = findViewById(R.id.tv_anser_info);
        ivRate = findViewById(R.id.iv_rate);

        rvNews = findViewById(R.id.rv_news);

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
        if (null == subject) {
            startActivityForResult(new Intent(this, SubjectDetailActivity.class), 0);
            return;
        }
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
                            updateRankUi();
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

        getRankDataSelf();
    }

    private void getRankDataSelf() {
        Subject subject = LocalDataUtils.getSubject(this);
        EasyHttp.get(this)
                .api(new GetMyRankApi().setSujectCode(subject.getSubjectCode()).setRankType(rankType))
                .request(new HttpCallback<HttpData<Rank>>(this) {
                    @Override
                    public void onSucceed(HttpData<Rank> result) {

                        if (result.isSuccess()) {
                            llMy.setVisibility(View.GONE);
                            updateSelfRank(result.getData());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    private void updateSelfRank(Rank rank) {
        if (null == rank) {
            return;
        }

        llMy.setVisibility(View.VISIBLE);
        tvRankName.setText(rank.getNickname());
        tvRate.setText(rank.getRightRate() + "%");
        tvNo.setText(rank.getNo() + "");

        if (0 != rank.getRise()) {
            if (0 < rank.getRise()) {
                tvRise.setText(rank.getRise() + "");
                ivRate.setImageResource(R.mipmap.up);
                tvRise.setTextColor(getContext().getResources().getColor(R.color.colorRed));
            } else {
                tvRise.setText(Math.abs(rank.getRise()) + "");
                tvRise.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                ivRate.setImageResource(R.mipmap.down);
            }

        }
        tvAnserInfo.setText(rank.getRightQty() + "/" + rank.getAnswerQty());
        CircularImageView userHead = findView(R.id.iv_user_head);
        Glide.with(getContext()).load(rank.getFigureUrl()).into(userHead);
    }


    private void updateRankUi() {

        if (datas.size() >= 3 && 1 == pageNum) {
            Rank rankOne = datas.get(0);
            tv_rank_name_one.setText(rankOne.getNickname());
            tv_rate_one.setText("正确率:" + rankOne.getRightRate() + "%");
            tv_anser_info_one.setText(rankOne.getRightQty() + "");
            Glide.with(getContext()).load(rankOne.getFigureUrl()).into(iv_user_head_one);

            Rank rankTwo = datas.get(1);
            tv_rank_name_two.setText(rankTwo.getNickname());
            tv_rate_two.setText("正确率:" + rankTwo.getRightRate() + "%");
            tv_anser_info_two.setText(rankTwo.getRightQty() + "");
            Glide.with(getContext()).load(rankTwo.getFigureUrl()).into(iv_user_head_two);

            Rank rankThree = datas.get(2);
            tv_rank_name_three.setText(rankThree.getNickname());
            tv_rate_three.setText("正确率:" + rankThree.getRightRate() + "%");
            tv_anser_info_three.setText(rankThree.getRightQty() + "");
            Glide.with(getContext()).load(rankThree.getFigureUrl()).into(iv_user_head_three);

            datas.remove(rankOne);
            datas.remove(rankTwo);
            datas.remove(rankThree);
            rankAdapter.notifyDataSetChanged();
        } else {
            rankAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (1 == pageNum) {
            getRankData();
        }
    }
}
