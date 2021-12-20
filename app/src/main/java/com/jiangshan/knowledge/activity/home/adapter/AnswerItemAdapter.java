package com.jiangshan.knowledge.activity.home.adapter;

import android.os.Build;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.QuestionOption;

import java.util.List;
import java.util.Set;

/**
 * auth s_yz  2021/12/20
 */
public class AnswerItemAdapter extends BaseQuickAdapter<QuestionOption, BaseViewHolder> {

    private Set<String> choiceAnswerList;

    private Set<String> userAnswerList;

    public AnswerItemAdapter(int layoutResId, @Nullable List<QuestionOption> data) {
        super(layoutResId, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, QuestionOption data) {
        String content = Html.fromHtml(data.getContent(), Html.FROM_HTML_MODE_COMPACT).toString();
        content = content.replace("\n", "");
        baseViewHolder.setText(R.id.tv_answer_content, content);

        if (null != userAnswerList && null != choiceAnswerList) {
            String optionNo = data.getOptionNo();
            if (userAnswerList.contains(optionNo) && choiceAnswerList.contains(optionNo)) {
                baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_right);
                baseViewHolder.setText(R.id.tv_answer_option, "");
            } else if (userAnswerList.contains(optionNo) && !choiceAnswerList.contains(optionNo)) {
                baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_error);
                baseViewHolder.setText(R.id.tv_answer_option, "");
            } else {
                baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.drawable.answer_option_bg);
                baseViewHolder.setText(R.id.tv_answer_option, data.getOptionNo());
            }
        }else{
            baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.drawable.answer_option_bg);
            baseViewHolder.setText(R.id.tv_answer_option, data.getOptionNo());
        }

    }

    public Set<String> getChoiceAnswerList() {
        return choiceAnswerList;
    }

    public void setChoiceAnswerList(Set<String> choiceAnswerList) {
        this.choiceAnswerList = choiceAnswerList;
    }

    public Set<String> getUserAnswerList() {
        return userAnswerList;
    }

    public void setUserAnswerList(Set<String> userAnswerList) {
        this.userAnswerList = userAnswerList;
    }
}