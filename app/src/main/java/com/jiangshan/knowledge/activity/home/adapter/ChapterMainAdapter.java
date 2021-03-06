package com.jiangshan.knowledge.activity.home.adapter;

import android.graphics.Paint;
import android.widget.TextView;

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

    private int singleTotal = -1;

    public void setSingleTotal(int singleTotal) {
        this.singleTotal = singleTotal;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public ChapterMainAdapter(int layoutResId, @Nullable List<Question> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Question data) {
        if (1 == data.getCollectFlag()) {
            TextView chapter_no = baseViewHolder.findView(R.id.tv_chapter_no);
            chapter_no.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            chapter_no.getPaint().setAntiAlias(true);//抗锯齿
        }

        baseViewHolder.setBackgroundResource(R.id.tv_chapter_no, R.drawable.chapter_main_bg);
        baseViewHolder.setTextColorRes(R.id.tv_chapter_no, R.color.b3);

        if (null != data.getUserAnswerList() && null != data.getChoiceAnswerList() && data.getUserAnswerList().size() > 0) {
            if (data.getChoiceAnswerList().containsAll(data.getUserAnswerList()) && data.getChoiceAnswerList().size() == data.getUserAnswerList().size()) {
                baseViewHolder.setBackgroundResource(R.id.tv_chapter_no, R.drawable.chapter_main_right_bg);
                baseViewHolder.setTextColorRes(R.id.tv_chapter_no, R.color.colorGreen);
            } else {
                baseViewHolder.setBackgroundResource(R.id.tv_chapter_no, R.drawable.chapter_main_error_bg);
                baseViewHolder.setTextColorRes(R.id.tv_chapter_no, R.color.colorRed);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (singleTotal > 0) {
            holder.setText(R.id.tv_chapter_no, (position + 1 + singleTotal) + "");
        } else {
            holder.setText(R.id.tv_chapter_no, (position + 1) + "");
        }
    }
}
