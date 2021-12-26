package com.jiangshan.knowledge.activity.home.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.AnswerBgColor;

import java.util.List;

/**
 * auth s_yz  2021/10/20
 */
public class AnswerBgColorAdapter extends BaseQuickAdapter<AnswerBgColor, BaseViewHolder> {

    public AnswerBgColorAdapter(int layoutResId, @Nullable List<AnswerBgColor> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, AnswerBgColor data) {
        ImageView iv_answer_bg = baseViewHolder.findView(R.id.iv_answer_bg);
        iv_answer_bg.setBackgroundColor(Color.parseColor(data.getCorlorStr()));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
