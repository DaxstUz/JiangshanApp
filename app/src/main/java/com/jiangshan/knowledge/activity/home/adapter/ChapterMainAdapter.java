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

    private int selectIndex;

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public ChapterMainAdapter(int layoutResId, @Nullable List<Question> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Question data) {
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.setText(R.id.tv_chapter_no, (position+1)+"");
        if(selectIndex==position){
            holder.setBackgroundResource(R.id.tv_chapter_no,R.drawable.chapter_main_select_bg);
        }
    }
}
