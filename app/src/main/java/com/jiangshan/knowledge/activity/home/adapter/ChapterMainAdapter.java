package com.jiangshan.knowledge.activity.home.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.Question;

import java.util.List;

/**
 * auth s_yz  2021/10/20
 */
public class ChapterMainAdapter extends BaseQuickAdapter<Question, BaseViewHolder> {

    private String selectQuestionNo;

    public void setSelectQuestionNo(String selectQuestionNo) {
        this.selectQuestionNo = selectQuestionNo;
    }

    public ChapterMainAdapter(int layoutResId, @Nullable List<Question> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Question data) {
        baseViewHolder.setText(R.id.tv_chapter_no, data.getQuestionNo());
        if(data.getQuestionNo().equals(selectQuestionNo)){
            baseViewHolder.setBackgroundResource(R.id.tv_chapter_no,R.drawable.chapter_main_select_bg);
        }

    }

}
