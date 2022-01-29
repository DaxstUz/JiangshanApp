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
import com.hjq.http.EasyLog;
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

    private boolean hasAnswer;

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
        if ("#ffffff".equals(bgColorValue)) {
            bgColorValue = "#333333";
        }else if ("#B3000000".equals(bgColorValue)) {
            bgColorValue = "#cdcdcd";
        }else{
            bgColorValue = "#000000";
        }
        tv_answer_content.setTextColor(Color.parseColor(bgColorValue));

        String optionNo = data.getOptionNo();

//        EasyLog.print("hasAnswer "+hasAnswer+" userAnswerList :"+userAnswerList.size()+" choiceAnswerList: "+choiceAnswerList.size());

        boolean settingResultShow=LocalDataUtils.getLocalDataBoolean(getContext(), LocalDataUtils.settingDataName, LocalDataUtils.keyResultShow);
        boolean showAnalysis = ((Activity) getContext()).getIntent().getBooleanExtra("showAnalysis", false);

        baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.drawable.answer_option_bg);
        baseViewHolder.setText(R.id.tv_answer_option, data.getOptionNo());

        if(!showAnalysis){//不是背景模式且开启显示正确答案
            if (hasAnswer && null != userAnswerList && null != choiceAnswerList) {
                if (userAnswerList.contains(optionNo)){
                    baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_right);
                    baseViewHolder.setText(R.id.tv_answer_option, "");
                }
                if (userAnswerList.contains(optionNo) && choiceAnswerList.contains(optionNo)) {
                    baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_right);
                    baseViewHolder.setText(R.id.tv_answer_option, "");
                } else if (userAnswerList.contains(optionNo) && !choiceAnswerList.contains(optionNo) && settingResultShow) {
                    baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_error);
                    baseViewHolder.setText(R.id.tv_answer_option, "");
                }
            }
        }else{
            if (hasAnswer && null != userAnswerList && null != choiceAnswerList) {
                if (userAnswerList.contains(optionNo)){
                    baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_right);
                    baseViewHolder.setText(R.id.tv_answer_option, "");
                }
                if (userAnswerList.contains(optionNo) && choiceAnswerList.contains(optionNo)) {
                    baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_right);
                    baseViewHolder.setText(R.id.tv_answer_option, "");
                } else if (userAnswerList.contains(optionNo) && !choiceAnswerList.contains(optionNo) ) {
                    baseViewHolder.setBackgroundResource(R.id.tv_answer_option, R.mipmap.rb_answer_error);
                    baseViewHolder.setText(R.id.tv_answer_option, "");
                }
            }
        }

       //显示所有正确答案
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

    public void setHasAnswer(boolean hasAnswer) {
        this.hasAnswer = hasAnswer;
    }
}