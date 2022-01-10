package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.jiangshan.knowledge.activity.home.adapter.ExamAdapter;
import com.jiangshan.knowledge.http.api.GetExamApi;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Exam;
import com.jiangshan.knowledge.http.entity.MemberInfo;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/10/21
 */
public class ExamListActivity extends BaseActivity {

    private RecyclerView rvExam;
    private ExamAdapter examAdapter;
    private List<Exam> datas = new ArrayList<>();

    private MemberInfo memberInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);
        setTitle("江山老师题库");
        setBackViewVisiable();
        initView();

        getExamData();
        memberInfo = LocalDataUtils.getMemberInfo(this);
    }

    private void initView() {
        rvExam = findViewById(R.id.rv_exam);
        examAdapter = new ExamAdapter(R.layout.item_exam, datas);
        rvExam.setAdapter(examAdapter);
        rvExam.setLayoutManager(new LinearLayoutManager(this));
        examAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

                if (canEdit(datas.get(position))) {
                    Intent intent = new Intent(ExamListActivity.this, SelectAnserModelActivity.class);
                    intent.putExtra("examCode", datas.get(position).getExamCode());
                    intent.putExtra("examName", datas.get(position).getExamName());
                    startActivityForResult(intent, RESULT_OK);
                } else {
                    ToastUtils.show("这是会员专享，请去开通会员。");
                }

            }
        });
    }

    private boolean canEdit(Exam exam) {
//        EasyLog.print("会员信息："+new Gson().toJson(memberInfo));
        if (0 < exam.getMemberType() && (null == memberInfo || (null != memberInfo && 0 == memberInfo.getMemberType()))) {
            return false;
        }
        return true;
    }

    private void getExamData() {
        int examType = getIntent().getIntExtra("examType", 1);
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        EasyHttp.get(this)
                .api(new GetExamApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setExamType(examType))
                .request(new HttpCallback<HttpListData<Exam>>(this) {
                    @Override
                    public void onSucceed(HttpListData<Exam> result) {
                        if (result.isSuccess()) {
                            datas.addAll(result.getData().getList());
                            examAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}

