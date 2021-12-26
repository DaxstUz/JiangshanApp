package com.jiangshan.knowledge.activity.home.adapter;

import android.os.Build;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.FeedbackOption;

import java.util.List;

/**
 * auth s_yz  2021/12/20
 */
public class FeedbackItemAdapter extends BaseQuickAdapter<FeedbackOption, BaseViewHolder> {


    public FeedbackItemAdapter(int layoutResId, @Nullable List<FeedbackOption> data) {
        super(layoutResId, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FeedbackOption data) {
        String content = Html.fromHtml(data.getContent(), Html.FROM_HTML_MODE_COMPACT).toString();
        content = content.replace("\n", "");
        baseViewHolder.setText(R.id.tv_answer_content, content);

        baseViewHolder.setText(R.id.tv_answer_option, "");
        if (data.isChecked()) {
            baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_right);
        }else{
            baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.drawable.answer_option_bg);
        }

    }

}