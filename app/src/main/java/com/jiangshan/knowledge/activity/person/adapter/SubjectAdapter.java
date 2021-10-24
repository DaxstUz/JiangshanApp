package com.jiangshan.knowledge.activity.person.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.person.SelectedSubjectItem;
import com.jiangshan.knowledge.http.entity.Subject;

import java.util.List;

/**
 * auth s_yz  2021/10/16
 */
public class SubjectAdapter extends BaseQuickAdapter<Subject, BaseViewHolder> {

    public SubjectAdapter(int layoutResId, @Nullable List<Subject> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Subject data) {
        baseViewHolder.setText(R.id.tv_subject, data.getSubjectName());
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (position == SelectedSubjectItem.getSlectedNavItem()) {
            holder.setBackgroundResource(R.id.ll_subject, R.drawable.subject_bg_select);
        } else {
            holder.setBackgroundResource(R.id.ll_subject, R.drawable.subject_bg_monal);
        }
    }
}
