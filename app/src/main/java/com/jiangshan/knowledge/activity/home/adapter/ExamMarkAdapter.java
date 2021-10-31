package com.jiangshan.knowledge.activity.home.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.Exam;

import java.util.List;

/**
 * auth s_yz  2021/10/17
 */
public class ExamMarkAdapter extends BaseQuickAdapter<Exam, BaseViewHolder> {

    public ExamMarkAdapter(int layoutResId, @Nullable List<Exam> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Exam data) {
        baseViewHolder.setText(R.id.tv_rank_name, data.getExamName());
        baseViewHolder.setText(R.id.tv_anser_info, data.getQuestionQty() + "道题");
    }

}
