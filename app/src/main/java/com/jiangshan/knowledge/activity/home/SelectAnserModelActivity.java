package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.ExamHistoryListAdapter;
import com.jiangshan.knowledge.activity.home.adapter.QuetionCountAdapter;
import com.jiangshan.knowledge.http.api.GetExamHistoryListApi;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.ExamHistory;
import com.jiangshan.knowledge.http.entity.MemberInfo;
import com.jiangshan.knowledge.http.entity.QuetionCount;
import com.jiangshan.knowledge.http.entity.Subject;
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

    private FrameLayout flHistoryInfo;
    private LinearLayout llQuestionCount;

    private RecyclerView rvQuestionCount;
    private QuetionCountAdapter quetionCountAdapter;
    private List<QuetionCount> quetionCounts = new ArrayList<>();


    private RecyclerView rvExam;
    private ExamHistoryListAdapter examAdapter;
    private List<ExamHistory> datas = new ArrayList<>();

    private int pageNum = 1;
    private int memberType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anser_model);
        setTitle("??????????????????");
        setBackViewVisiable();

        memberType = getIntent().getIntExtra("memberType", 0);

        initView();
        updateUI();
        initLoadMore();
    }

    private void initView() {
        flHistoryInfo = findViewById(R.id.fl_history_info);
        llQuestionCount = findViewById(R.id.ll_question_count);
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
                intent.putExtra("examType", datas.get(position).getExamType());
                intent.putExtra("billId", datas.get(position).getId());
                intent.putExtra("showAnalysis", true);
                intent.putExtra("showUserAnalysis", true);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        subjectName = findViewById(R.id.tv_subject_name);
        courseName = findViewById(R.id.tv_course);

        rbAnalysis = findViewById(R.id.rb_bg_analysis);
        rbAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canEdit(memberType)) {
                    Intent intent = new Intent(SelectAnserModelActivity.this, AnswerActivity.class);
                    intent.putExtra("examName", getIntent().getStringExtra("examName"));
                    intent.putExtra("examCode", getIntent().getStringExtra("examCode"));
                    intent.putExtra("examType", getIntent().getIntExtra("examType", 1));
                    intent.putExtra("random", getIntent().getBooleanExtra("random", false));
                    intent.putExtra("questionTypeQtySet", getquestionTypeQtySet());
                    intent.putExtra("showAnalysis", true);
                    startActivityForResult(intent, RESULT_OK);
                } else {
                    ToastUtils.show("??????????????????????????????????????????");
                }
            }
        });

        rbAnser = findViewById(R.id.rb_anser);
        rbAnser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canEdit(memberType)) {
                    Intent intent = new Intent(SelectAnserModelActivity.this, AnswerActivity.class);
                    intent.putExtra("examName", getIntent().getStringExtra("examName"));
                    intent.putExtra("examCode", getIntent().getStringExtra("examCode"));
                    intent.putExtra("examType", getIntent().getIntExtra("examType", 1));
                    intent.putExtra("random", getIntent().getBooleanExtra("random", false));
                    intent.putExtra("questionTypeQtySet", getquestionTypeQtySet());
                    startActivityForResult(intent, RESULT_OK);
                } else {
                    ToastUtils.show("??????????????????????????????????????????");
                }
            }
        });
        excuteRandom();
    }

    /**
     * ????????????????????????
     */
    private void excuteRandom() {
        boolean random = getIntent().getBooleanExtra("random", false);
        if (random) {
            flHistoryInfo.setVisibility(View.GONE);
            llQuestionCount.setVisibility(View.VISIBLE);

            Course course = LocalDataUtils.getCourse(this);
            if (null != course.getQuestionTypeSet()) {
                String[] strs = course.getQuestionTypeSet().split(",");
                for (int i = 0; i < strs.length; i++) {
                    quetionCounts.add(new QuetionCount(Integer.valueOf(strs[i]), 10));
                }
            }
            if(quetionCounts.size()==0){
               ToastUtils.show("?????????????????????");
               finish();
            }
            rvQuestionCount = findViewById(R.id.rv_question_count);
            quetionCountAdapter = new QuetionCountAdapter(R.layout.item_radom_question, quetionCounts);
            rvQuestionCount.setAdapter(quetionCountAdapter);
            rvQuestionCount.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private String getquestionTypeQtySet() {
        return new Gson().toJson(quetionCounts);
    }

    private void updateUI() {
        String data = LocalDataUtils.getLocalData(SelectAnserModelActivity.this, LocalDataUtils.localDataName, LocalDataUtils.keySubject);
        if (null != data) {
            Subject subject = new Gson().fromJson(data, Subject.class);
            if (null != subject) {
                subjectName.setText(subject.getSubjectName());
                String courseStr = LocalDataUtils.getLocalData(SelectAnserModelActivity.this, LocalDataUtils.localDataName, LocalDataUtils.keyCourse);
                Course course = new Gson().fromJson(courseStr, Course.class);
                if (null != course) {
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
                .api(new GetExamHistoryListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setPageNum(pageNum).setExamType(getIntent().getIntExtra("examType", 1)).setExamCode(getIntent().getStringExtra("examCode")).setPageNum(pageNum))
                .request(new HttpCallback<HttpListData<ExamHistory>>(this) {
                    @Override
                    public void onSucceed(HttpListData<ExamHistory> result) {
                        if (result.isSuccess()) {
                            examAdapter.getLoadMoreModule().setEnableLoadMore(true);
                            if (result.getData().getList().size() < result.getData().getPageSize()) {
                                //??????????????????,??????????????????????????????
                                examAdapter.getLoadMoreModule().loadMoreEnd();
                            } else {
                                examAdapter.getLoadMoreModule().loadMoreComplete();
                            }

                            if(1==pageNum){
                                datas.clear();
                            }
                            datas.addAll(result.getData().getList());
                            examAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * ?????????????????????
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
        //??????????????????????????????????????????????????????????????????????????????????????????(?????????true)
        examAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        pageNum = 1;
        examAdapter.getLoadMoreModule().setEnableLoadMore(true);
        updateUI();
    }

    private boolean canEdit(int memberType) {
        MemberInfo memberInfo = LocalDataUtils.getMemberInfo(this);
        if (0 < memberType && (null == memberInfo || (null != memberInfo && 0 == memberInfo.getMemberType()))) {
            return false;
        }
        return true;
    }
}
