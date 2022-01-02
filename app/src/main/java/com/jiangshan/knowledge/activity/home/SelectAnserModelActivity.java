package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.Gson;
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
 * auth s_yz  2021/10/18
 */
public class SelectAnserModelActivity extends BaseActivity {

    private TextView subjectName;
    private TextView courseName;

    private RadioButton rbAnser;
    private RadioButton rbAnalysis;

    private RecyclerView rvExam;
    private ExamHistoryListAdapter examAdapter;
    private List<ExamHistory> datas = new ArrayList<>();

    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anser_model);
        setTitle("江山老师题库");
        setBackViewVisiable();

        initView();
        updateUI();

        initLoadMore();
    }

    private void initView() {

        rvExam = findViewById(R.id.rv_exam);
        examAdapter = new ExamHistoryListAdapter(R.layout.item_exam_history_list, datas);
        rvExam.setAdapter(examAdapter);
        rvExam.setLayoutManager(new LinearLayoutManager(this));
        examAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(SelectAnserModelActivity.this, AnswerActivity.class);
                intent.putExtra("examCode", datas.get(position).getExamCode());
                intent.putExtra("examName", datas.get(position).getExamName());
                intent.putExtra("showAnalysis",true);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        subjectName=findViewById(R.id.tv_subject_name);
        courseName=findViewById(R.id.tv_course);

        rbAnalysis=findViewById(R.id.rb_bg_analysis);
        rbAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectAnserModelActivity.this,AnswerActivity.class);
                intent.putExtra("examName",getIntent().getStringExtra("examName"));
                intent.putExtra("examCode",getIntent().getStringExtra("examCode"));
                intent.putExtra("examType",getIntent().getIntExtra("examType",1));
                intent.putExtra("showAnalysis",true);
                startActivityForResult(intent,RESULT_OK);
            }
        });

        rbAnser=findViewById(R.id.rb_anser);
        rbAnser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectAnserModelActivity.this,AnswerActivity.class);
                intent.putExtra("examName",getIntent().getStringExtra("examName"));
                intent.putExtra("examCode",getIntent().getStringExtra("examCode"));
                intent.putExtra("examType",getIntent().getIntExtra("examType",1));
                startActivityForResult(intent,RESULT_OK);
            }
        });
    }

    private void updateUI() {
        String data= LocalDataUtils.getLocalData(SelectAnserModelActivity.this,LocalDataUtils.localDataName,LocalDataUtils.keySubject);
        if(null!=data){
            Subject subject= new Gson().fromJson(data, Subject.class);
            if(null!=subject){
                subjectName.setText(subject.getSubjectName());
                String courseStr = LocalDataUtils.getLocalData(SelectAnserModelActivity.this, LocalDataUtils.localDataName, LocalDataUtils.keyCourse);
                Course course= new Gson().fromJson(courseStr, Course.class);
                if(null!=course){
                    courseName.setText(course.getCourseName());
                }
            }
        }

        getData();
    }

    private void getData() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        if (null == subject || null == course) {
            return;
        }

        EasyHttp.get(this)
                .api(new GetExamHistoryListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setPageNum(pageNum).setExamType(getIntent().getIntExtra("examType",1)).setExamCode(getIntent().getStringExtra("examCode")))
                .request(new HttpCallback<HttpListData<ExamHistory>>(this) {
                    @Override
                    public void onSucceed(HttpListData<ExamHistory> result) {
                        if (result.isSuccess()) {
                            examAdapter.getLoadMoreModule().setEnableLoadMore(true);
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
