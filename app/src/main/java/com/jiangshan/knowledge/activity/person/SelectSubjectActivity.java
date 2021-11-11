package com.jiangshan.knowledge.activity.person;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.SubjectDetailActivity;
import com.jiangshan.knowledge.activity.person.adapter.SubjectAdapter;
import com.jiangshan.knowledge.activity.person.adapter.SubjectCategoryAdapter;
import com.jiangshan.knowledge.http.api.SubjectCategoryApi;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.SubjectCategory;
import com.jiangshan.knowledge.http.model.HttpListDataAll;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/10/16
 */
public class SelectSubjectActivity extends BaseActivity {

    private RecyclerView rvSelectLeft;
    private RecyclerView rvSelectRight;

    private SubjectCategoryAdapter subjectCategoryAdapter;
    private List<SubjectCategory> subjectCategories=new ArrayList<>();

    private SubjectAdapter subjectAdapter;
    private List<Subject> subjects=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);
        setTitle("请选择考试类型");
        setBackViewVisiable();

        initView();

        getSubjectData();
    }

    private void initView() {
        rvSelectLeft=findViewById(R.id.rv_suject_left);
        subjectCategoryAdapter=new SubjectCategoryAdapter(R.layout.item_subject_category,subjectCategories);
        rvSelectLeft.setAdapter(subjectCategoryAdapter);
        rvSelectLeft.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

        rvSelectRight=findViewById(R.id.rv_suject_right);
        subjectAdapter=new SubjectAdapter(R.layout.item_subject,subjects);
        rvSelectRight.setAdapter(subjectAdapter);
        rvSelectRight.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        bindListerner();
    }

    private void bindListerner() {
        subjectCategoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SelectedSubjectCategoryItem.setSlectedNavItem(position);
                SelectedSubjectItem.setSlectedNavItem(0);
                subjects.clear();
                subjects.addAll(subjectCategories.get(position).getSubjectInfoList());
                subjectAdapter.notifyDataSetChanged();
                subjectCategoryAdapter.notifyDataSetChanged();
            }
        });

        subjectAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Gson gson = new Gson();
                String subject=gson.toJson(subjects.get(position));
                LocalDataUtils.saveLocalData(SelectSubjectActivity.this,LocalDataUtils.localDataName, LocalDataUtils.keySubject,subject);
                SelectedSubjectItem.setSlectedNavItem(position);
                subjectAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getSubjectData() {
        EasyHttp.get(this)
                .api(new SubjectCategoryApi())
                .request(new HttpCallback<HttpListDataAll<SubjectCategory>>(this) {

                    @Override
                    public void onSucceed(HttpListDataAll<SubjectCategory> result) {
                        subjectCategories.addAll(result.getData());
                        if(null!=result.getData()&&result.getData().size()>0){
                            subjects.addAll(result.getData().get(SelectedSubjectCategoryItem.getSlectedNavItem()).getSubjectInfoList());
                        }
                        subjectCategoryAdapter.notifyDataSetChanged();
                        subjectAdapter.notifyDataSetChanged();
                    }
                });
    }

}
