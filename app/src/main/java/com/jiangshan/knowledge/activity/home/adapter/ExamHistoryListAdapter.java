package com.jiangshan.knowledge.activity.home.adapter;

import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.ExamHistory;
import com.jiangshan.knowledge.uitl.DateUtil;

import java.util.List;

/**
 * auth s_yz  2021/10/17
 */
public class ExamHistoryListAdapter extends BaseQuickAdapter<ExamHistory, BaseViewHolder> implements LoadMoreModule {

    public ExamHistoryListAdapter(int layoutResId, @Nullable List<ExamHistory> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ExamHistory data) {
        baseViewHolder.setText(R.id.tv_rank_name, data.getExamName());
        ProgressBar progressBar = baseViewHolder.findView(R.id.pb_answer);
        progressBar.setProgress(data.getRightRate());
        baseViewHolder.setText(R.id.tv_anser_info, data.getQuestionQty() + "道题");
        baseViewHolder.setText(R.id.tv_anser_rate, "正确率" + data.getRightRate() + "%");

        baseViewHolder.setText(R.id.tv_anser_time, DateUtil.paseFromStr(data.getExamTime()));
    }
}
