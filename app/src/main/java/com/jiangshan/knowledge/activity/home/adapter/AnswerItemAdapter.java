package com.jiangshan.knowledge.activity.home.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.QuestionOption;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

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

        int fontSizeValue = LocalDataUtils.getLocalDataInteger(getContext(), LocalDataUtils.settingDataName, LocalDataUtils.fontSizeValue);
        TextView tv_answer_content = baseViewHolder.findView(R.id.tv_answer_content);
        tv_answer_content.setTextSize(fontSizeValue);

        String content = Html.fromHtml(data.getContent(), Html.FROM_HTML_MODE_COMPACT).toString();
        content = content.replace("\n", "");
        baseViewHolder.setText(R.id.tv_answer_content, content);


        String bgColorValue = LocalDataUtils.getLocalData(getContext(), LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue);
        if (null == bgColorValue || bgColorValue.length() == 0) {
            bgColorValue = "#ffffff";
        }
        if ("#1d1d1f".equals(bgColorValue)) {
            bgColorValue = "#78787a";
        } else if ("#fdf2dc".equals(bgColorValue)) {
            bgColorValue = "#483a2f";
        } else if ("#e6cdae".equals(bgColorValue)) {
            bgColorValue = "#362f27";
        } else if ("#d2ecd3".equals(bgColorValue)) {
            bgColorValue = "#1f3721";
        } else if ("#f0e1e6".equals(bgColorValue)) {
            bgColorValue = "#752935";
        } else if ("#f3f7f9".equals(bgColorValue)) {
            bgColorValue = "#333333";
        } else if ("#ffffff".equals(bgColorValue)) {
            bgColorValue = "#333333";
        } else if ("#B3000000".equals(bgColorValue)) {
            bgColorValue = "#cdcdcd";
        }
        tv_answer_content.setTextColor(Color.parseColor(bgColorValue));

        String optionNo = data.getOptionNo();
        if (null != userAnswerList && null != choiceAnswerList) {
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
        } else {
            baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.drawable.answer_option_bg);
            baseViewHolder.setText(R.id.tv_answer_option, data.getOptionNo());
        }

        boolean showAnalysis = ((Activity) getContext()).getIntent().getBooleanExtra("showAnalysis", false);
        if (showAnalysis && choiceAnswerList.contains(optionNo)) {
            baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_right);
            baseViewHolder.setText(R.id.tv_answer_option, "");
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