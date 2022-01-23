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
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
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
import com.jiangshan.knowledge.view.CustomLoadMoreView;

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

        memberInfo = LocalDataUtils.getMemberInfo(this);
        initLoadMore();
        getExamData();
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
                    intent.putExtra("examType", datas.get(position).getExamType());
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
                .api(new GetExamApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setExamType(examType).setPageNum(pageNum))
                .request(new HttpCallback<HttpListData<Exam>>(this) {
                    @Override
                    public void onSucceed(HttpListData<Exam> result) {
                        if (result.isSuccess()) {
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

    private int pageNum = 1;

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        examAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        examAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNum++;
                getExamData();
            }
        });
        examAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        examAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

}

