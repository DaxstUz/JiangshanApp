package com.jiangshan.knowledge.activity.person.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.person.SelectedSubjectCategoryItem;
import com.jiangshan.knowledge.http.entity.SubjectCategory;

import java.util.List;

/**
 * auth s_yz  2021/10/16
 */
public class SubjectCategoryAdapter extends BaseQuickAdapter<SubjectCategory, BaseViewHolder> {

    public SubjectCategoryAdapter(int layoutResId, @Nullable List<SubjectCategory> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, SubjectCategory subjectCategory) {
        baseViewHolder.setText(R.id.tv_subject_catgory, subjectCategory.getCategoryName());
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(position == SelectedSubjectCategoryItem.getSlectedNavItem()){
            holder.setBackgroundResource(R.id.ll_subject_category,R.color.b1);
            holder.setTextColorRes(R.id.tv_subject_catgory,R.color.bg_title);
        }else{
            holder.setBackgroundResource(R.id.ll_subject_category,R.color.E3);
            holder.setTextColorRes(R.id.tv_subject_catgory,R.color.colorBlack0);
        }
    }
}
