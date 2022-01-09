package com.jiangshan.knowledge.activity.home.adapter;

import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.Exam;

import java.util.List;

/**
 * auth s_yz  2021/10/17
 */
public class ExamAdapter extends BaseQuickAdapter<Exam, BaseViewHolder> {

    public ExamAdapter(int layoutResId, @Nullable List<Exam> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Exam data) {
        baseViewHolder.setText(R.id.tv_rank_name, data.getExamName());


        ProgressBar progressBar=baseViewHolder.findView(R.id.pb_answer);

        if(data.getQuestionQty()>0){
            int process=(int)(data.getAnswerQuestionQty()*100/data.getQuestionQty());
            progressBar.setProgress(process);
        }
        baseViewHolder.setText(R.id.tv_anser_info, data.getAnswerQuestionQty()+"/"+data.getQuestionQty()+"道题");

        if (0 < data.getMemberType()) {
            baseViewHolder.setImageResource(R.id.iv_edit_icon, R.mipmap.vip);
        }
    }

}
