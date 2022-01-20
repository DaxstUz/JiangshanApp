package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.ExamMarkAdapter;
import com.jiangshan.knowledge.activity.person.LoginActivity;
import com.jiangshan.knowledge.http.api.GetMarkExamListApi;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Exam;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpListDataAll;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/10/24
 */
public class ExamMarkActivity extends BaseActivity {

    private TextView rvAnswerAll;
    private TextView tv_mark_title;

    private RecyclerView rvExam;
    private ExamMarkAdapter examAdapter;
    private List<Exam> datas = new ArrayList<>();

    private TextView tvShowAll;

    private RadioGroup rgAnswerMark;

    private int examType = 1;
    private int markType = 1;

    private int countALl = 0;

    private Switch switchShowAnylysis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_mark);

        setTitle(getIntent().getStringExtra("title"));
        setBackViewVisiable();

        initView();
        getData();
    }

    private void initView() {
        switchShowAnylysis = findViewById(R.id.switch_show_anylysis);

        rvAnswerAll = findViewById(R.id.rv_answer_all);

        tv_mark_title = findViewById(R.id.tv_mark_title);
        if ("error".equals(getIntent().getStringExtra("type"))) {
            tv_mark_title.setText("我的错题总数");
            markType=2;
        } else if ("collect".equals(getIntent().getStringExtra("type"))) {
            tv_mark_title.setText("我的收藏总数");
            markType=1;
        }

        rgAnswerMark = findViewById(R.id.rg_answer_mark);
        rgAnswerMark.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_mark_true:
                        examType = 1;
//                        markType = 1;
                        break;
                    case R.id.rb_mark_moni:
                        examType = 2;
//                        markType = 2;
                        break;
                }
                getData();
            }
        });

        tvShowAll = findViewById(R.id.tv_show_all);
        tvShowAll.setText("查看全部" + getIntent().getStringExtra("title"));
        tvShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExamMarkActivity.this, AnswerActivity.class);
                intent.putExtra("type", getIntent().getStringExtra("type"));
                intent.putExtra("ismark", true);
                intent.putExtra("examName", getIntent().getStringExtra("title"));
                intent.putExtra("showAnalysis", switchShowAnylysis.isChecked());
                startActivityForResult(intent, RESULT_OK);
            }
        });

        rvExam = findViewById(R.id.rv_exam);
        examAdapter = new ExamMarkAdapter(R.layout.item_exam_mark, datas);
        rvExam.setAdapter(examAdapter);
        rvExam.setLayoutManager(new LinearLayoutManager(this));
        examAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(ExamMarkActivity.this, AnswerActivity.class);
                intent.putExtra("examCode", datas.get(position).getExamCode());
                intent.putExtra("examName", datas.get(position).getExamName());
                intent.putExtra("showAnalysis", switchShowAnylysis.isChecked());
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
                .api(new GetMarkExamListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setExamType(examType).setMarkType(markType))
                .request(new HttpCallback<HttpListDataAll<Exam>>(this) {
                    @Override
                    public void onSucceed(HttpListDataAll<Exam> result) {
                        if (result.isSuccess()) {
                            datas.clear();
                            datas.addAll(result.getData());
                            examAdapter.notifyDataSetChanged();
                            countALl = 0;
                            for (int i = 0; i < datas.size(); i++) {
                                countALl = countALl + datas.get(i).getQuestionQty();
                            }
                            rvAnswerAll.setText(countALl + "");
                        } else {
                            if (403 == result.getCode()) {
                                ToastUtils.show(result.getMsg());
                                startActivityForResult(new Intent(ExamMarkActivity.this, LoginActivity.class), RESULT_OK);
                            }

                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
    }
}
