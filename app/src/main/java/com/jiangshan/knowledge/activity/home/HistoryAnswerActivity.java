package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.ExamHistoryListAdapter;
import com.jiangshan.knowledge.http.api.GetExamHistoryListApi;
import com.jiangshan.knowledge.http.api.GetExamHistoryStatisticsApi;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.ExamHistory;
import com.jiangshan.knowledge.http.entity.HistoryStatistics;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.jiangshan.knowledge.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/10/24
 */
public class HistoryAnswerActivity extends BaseActivity {

    private TextView rvAnswerAll;
    private TextView rvAnswerCommit;
    private TextView rvAnswerLong;

    private RecyclerView rvExam;
    private ExamHistoryListAdapter examAdapter;
    private List<ExamHistory> datas = new ArrayList<>();

    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_answer);

        setTitle("答题历史");
        setBackViewVisiable();

        initView();
        getData();
        initLoadMore();
    }

    private void initView() {
        rvAnswerAll = findViewById(R.id.rv_answer_all);
        rvAnswerCommit = findViewById(R.id.rv_answer_commit);
        rvAnswerLong = findViewById(R.id.rv_answer_long);

        rvExam = findViewById(R.id.rv_exam);
        examAdapter = new ExamHistoryListAdapter(R.layout.item_exam_history_list, datas);
        rvExam.setAdapter(examAdapter);
        rvExam.setLayoutManager(new LinearLayoutManager(this));
        examAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(HistoryAnswerActivity.this, AnswerActivity.class);
                intent.putExtra("examCode", datas.get(position).getExamCode());
                intent.putExtra("examName", datas.get(position).getExamName());
                intent.putExtra("examType", datas.get(position).getExamType());
                intent.putExtra("billId", datas.get(position).getId());
                intent.putExtra("showAnalysis",true);
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    private void getData() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        if (null == subject || null == course) {
            startActivityForResult(new Intent(this, SubjectDetailActivity.class), 0);
            return;
        }

        EasyHttp.get(this)
                .api(new GetExamHistoryStatisticsApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()))
                .request(new HttpCallback<HttpData<HistoryStatistics>>(this) {

                    @Override
                    public void onSucceed(HttpData<HistoryStatistics> result) {
                        if (result.isSuccess()) {
                            rvAnswerAll.setText(result.getData().getAnswerCount() + "");
                            rvAnswerCommit.setText(result.getData().getFinishCount() + "次");
                            rvAnswerLong.setText(result.getData().getFinishCount() + "分钟");

                        }
                    }
                });

        EasyHttp.get(this)
                .api(new GetExamHistoryListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setPageNum(pageNum))
                .request(new HttpCallback<HttpListData<ExamHistory>>(this) {
                    @Override
                    public void onSucceed(HttpListData<ExamHistory> result) {
                        if (result.isSuccess()) {

//                            examAdapter.getLoadMoreModule().setEnableLoadMore(true);
                            if (result.getData().getList().size() < result.getData().getPageSize()) {
                                //如果不够一页,显示没有更多数据布局
                                examAdapter.getLoadMoreModule().loadMoreEnd();
                            } else {
                                examAdapter.getLoadMoreModule().loadMoreComplete();
                            }

                            datas.addAll(result.getData().getList());
                            examAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        examAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        examAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNum++;
                getData();
            }
        });
        examAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        examAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }
}
