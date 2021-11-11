package com.jiangshan.knowledge.activity.home.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.List;

/**
 * auth s_yz  2021/10/16
 */
public class SubjectDetailAdapter extends BaseQuickAdapter<Course, BaseViewHolder> {

    public SubjectDetailAdapter(int layoutResId, @Nullable List<Course> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Course course) {
        baseViewHolder.setText(R.id.tv_subject_detail_name, course.getCourseName());
        Course temp = LocalDataUtils.getCourse(getContext());
        if(null!=temp && temp.getCourseId()==course.getCourseId()){
            baseViewHolder.setBackgroundResource(R.id.ll_course_item,R.drawable.subject_bg_select);
        }
    }
}
