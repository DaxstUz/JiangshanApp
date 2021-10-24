package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

/**
 * auth s_yz  2021/10/18
 */
public class SelectAnserModelActivity extends BaseActivity {

    private TextView subjectName;
    private TextView courseName;

    private RadioButton rbAnser;
    private RadioButton rbAnalysis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anser_model);
        setTitle("江山老师题库");
        setBackViewVisiable();

        initView();
        updateUI();
    }

    private void initView() {
        subjectName=findViewById(R.id.tv_subject_name);
        courseName=findViewById(R.id.tv_course);

        rbAnalysis=findViewById(R.id.rb_bg_analysis);
        rbAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectAnserModelActivity.this,AnswerActivity.class);
                intent.putExtra("examName",getIntent().getStringExtra("examName"));
                intent.putExtra("examCode",getIntent().getStringExtra("examCode"));
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
    }

}
