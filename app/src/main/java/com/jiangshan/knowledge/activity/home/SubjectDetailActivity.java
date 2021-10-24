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
import com.google.gson.Gson;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.SubjectDetailAdapter;
import com.jiangshan.knowledge.activity.person.SelectSubjectActivity;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/10/16
 */
public class SubjectDetailActivity extends BaseActivity {

    private TextView tvChangeSubject;
    private TextView tvSubjectName;

    private RecyclerView rvSubjectDetail;
    private SubjectDetailAdapter subjectDetailAdapter;
    private List<Course> courseList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);
        setTitle("江山老师题库");
        setBackViewVisiable();

        initView();
    }

    private void initView() {
        rvSubjectDetail =findViewById(R.id.rv_subject_detail);
        tvChangeSubject =findViewById(R.id.tv_change_subject);
        tvSubjectName =findViewById(R.id.tv_subject_name);
        tvChangeSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), SelectSubjectActivity.class),0);
            }
        });
        subjectDetailAdapter=new SubjectDetailAdapter(R.layout.item_subject_detail,courseList);
        subjectDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                LocalDataUtils.saveLocalData(SubjectDetailActivity.this,LocalDataUtils.localDataName,LocalDataUtils.keyCourse,new Gson().toJson(courseList.get(position)));
                setResult(RESULT_OK);
                finish();
            }
        });
        rvSubjectDetail.setAdapter(subjectDetailAdapter);
        rvSubjectDetail.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        updateUI();
    }

    private void updateUI() {
        String data=LocalDataUtils.getLocalData(SubjectDetailActivity.this,LocalDataUtils.localDataName,LocalDataUtils.keySubject);
        if(null!=data){
            Subject subject= new Gson().fromJson(data, Subject.class);
            if(null!=subject){
                tvSubjectName.setText(subject.getSubjectName());
                courseList.clear();
                courseList.addAll(subject.getCourseList());
                subjectDetailAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK==resultCode){
            updateUI();
        }
    }
}
