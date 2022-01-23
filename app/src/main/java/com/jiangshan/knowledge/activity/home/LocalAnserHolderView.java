package com.jiangshan.knowledge.activity.home;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.EasyLog;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.home.adapter.AnswerItemAdapter;
import com.jiangshan.knowledge.http.api.ExamAnswerApi;
import com.jiangshan.knowledge.http.entity.Answer;
import com.jiangshan.knowledge.http.entity.Question;
import com.jiangshan.knowledge.http.entity.QuestionOption;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * auth s_yz  2021/10/13
 */
public class LocalAnserHolderView extends Holder<Question> {

    private TextView chapterCount;
    private TextView chapterName;
    private TextView tvRank;
    private TextView tvQuestionType;
    private WebView tvQuestionContent;
    private WebView tvAnswerAnalysis;
    private TextView tvChoiceAnswer;

    private TextView tvAly;
    private TextView tvRightAnswer;

    private TextView tvDetailQuestion;
    private TextView tvAnswer;

    private LinearLayout llAnswerAnalysis;

    private View itemView;
    private Answer answer = new Answer();

    private AnswerActivity answerActivity;

    private RecyclerView rvAnswerItem;
    private AnswerItemAdapter answerItemAdapter;
    private List<QuestionOption> questionOptionList = new ArrayList<>();

    private boolean answerRight;//答题是否正确

    public LocalAnserHolderView(View itemView, AnswerActivity answerActivity) {
        super(itemView);
        this.itemView = itemView;
        this.answerActivity = answerActivity;
    }

    @Override
    protected void initView(View itemView) {
        chapterCount = itemView.findViewById(R.id.tv_chapter_count);
        chapterName = itemView.findViewById(R.id.tv_chapter_name);
        tvRank = itemView.findViewById(R.id.tv_rank);
        tvQuestionType = itemView.findViewById(R.id.tv_question_type);
        tvQuestionContent = itemView.findViewById(R.id.tv_question_content);

        tvAnswerAnalysis = itemView.findViewById(R.id.tv_answer_analysis);
        tvChoiceAnswer = itemView.findViewById(R.id.tv_choice_answer);

        tvDetailQuestion = itemView.findViewById(R.id.tv_detail_question);
        tvAnswer = itemView.findViewById(R.id.tv_answer);

        llAnswerAnalysis = itemView.findViewById(R.id.ll_answer_analysis);
        rvAnswerItem = itemView.findViewById(R.id.rv_answer_item);

        tvAly = itemView.findViewById(R.id.tv_aly);
        tvRightAnswer = itemView.findViewById(R.id.tv_right_answer);
    }

    private Question data;
    private boolean showAnalysis;
    private boolean answerShow;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateUI(Question data) {
        answerShow = LocalDataUtils.getLocalDataBoolean((AnswerActivity) itemView.getContext(), LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerShow);

        boolean showUserAnalysis = ((AnswerActivity) itemView.getContext()).getIntent().getBooleanExtra("showUserAnalysis", false);
        if(showUserAnalysis){
            data.setHasAnswer(true);
        }

        int fontSizeValue = LocalDataUtils.getLocalDataInteger((AnswerActivity) itemView.getContext(), LocalDataUtils.settingDataName, LocalDataUtils.fontSizeValue);

        String bgColorValue = LocalDataUtils.getLocalData((AnswerActivity) itemView.getContext(), LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue);
        if (null == bgColorValue || bgColorValue.length() == 0) {
            bgColorValue = "#ffffff";
        }

        Set<String> userAnswerList = data.getUserAnswerList();
        if (null == userAnswerList) {
            userAnswerList = new HashSet<>();
            data.setUserAnswerList(userAnswerList);
        }

        rvAnswerItem.setLayoutManager(new LinearLayoutManager((AnswerActivity) itemView.getContext()));
        questionOptionList.clear();

        if (null != data.getQuestionOptionList()) {
            questionOptionList.addAll(data.getQuestionOptionList());
        }

        answerItemAdapter = new AnswerItemAdapter(R.layout.item_question_option, questionOptionList);
        answerItemAdapter.setChoiceAnswerList(data.getChoiceAnswerList());
        answerItemAdapter.setUserAnswerList(userAnswerList);
        answerItemAdapter.setHasAnswer(data.isHasAnswer());
        rvAnswerItem.setAdapter(answerItemAdapter);

        Set<String> finalUserAnswerList = userAnswerList;

        tvQuestionContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerActivity.nextQuestion(null);
            }
        });

        answerItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if(!showUserAnalysis){
                    if (1 == data.getQuestionType()) {//单选
                        finalUserAnswerList.clear();
                    }
                    if (finalUserAnswerList.contains(questionOptionList.get(position).getOptionNo())) {
                        finalUserAnswerList.remove(questionOptionList.get(position).getOptionNo());
                    } else {
                        finalUserAnswerList.add(questionOptionList.get(position).getOptionNo());
                        answerItemAdapter.setHasAnswer(true);
                    }
                    answerItemAdapter.notifyDataSetChanged();
                    onOptionClick();
                }
            }
        });

        this.data = data;
        showAnalysis = ((AnswerActivity) itemView.getContext()).getIntent().getBooleanExtra("showAnalysis", false);

        answer.setQuestionId(data.getId());
        answer.setBillId(data.getBillId());

        chapterCount.setText("/" + data.getTotal());
        String examName = ((AnswerActivity) itemView.getContext()).getIntent().getStringExtra("examName");
        chapterName.setText(examName);

        tvRank.setText(data.getRank() + "");

        tvQuestionType.setText(data.getQuestionTypeDesc());

        tvChoiceAnswer.setText(data.getChoiceAnswer());
        String questionContent = data.getContent();
        questionContent = questionContent.replaceAll("//img.51kpm.com", "https://img.51kpm.com");
        questionContent = questionContent.replace("<img", "<img style=\"max-width:100%;height:auto\"");
        if (4 == data.getQuestionType()) {
            //案例分析
            questionContent = questionContent.replace("&nbsp;", "");
            questionContent = questionContent.replace("<br/></p><p>", "<br/>");
            questionContent = questionContent.replace("</p><p><br/>", "");
        } else if (5 == data.getQuestionType()) {
            //论文
            questionContent = questionContent.replace("</p><p><br/>", "");
        }
        questionContent = questionContent.replace("</p><p>", "<br/>");
        String answerAnalysis = data.getAnswerAnalysis();
        answerAnalysis = answerAnalysis.replaceAll("//img.51kpm.com", "https://img.51kpm.com");
        answerAnalysis = answerAnalysis.replace("<img", "<img style=\"max-width:100%;height:auto\"");
//        answerAnalysis = answerAnalysis.replace("&nbsp;", "");
        answerAnalysis = answerAnalysis.replace("</p><p>", "</br>");
        if (bgColorValue.length() > 7) {
            questionContent = questionContent.replace("<p>", "<p style=\"color: #cdcdcd \">");
            answerAnalysis = answerAnalysis.replace("<p>", "<p style=\"color: #cdcdcd \">");
        }
        tvQuestionContent.loadDataWithBaseURL(null, questionContent, "text/html", "UTF-8", null);
        tvAnswerAnalysis.loadDataWithBaseURL(null, answerAnalysis, "text/html", "UTF-8", null);

        // 设置背景色
        tvAnswerAnalysis.setBackgroundColor(0);
        // 设置填充透明度
        tvAnswerAnalysis.getBackground().setAlpha(0);

        // 设置背景色
        tvQuestionContent.setBackgroundColor(0);
        // 设置填充透明度
        tvQuestionContent.getBackground().setAlpha(0);

        tvRank.setTextSize(fontSizeValue);
        tvChoiceAnswer.setTextSize(fontSizeValue);
        chapterName.setTextSize(fontSizeValue);
        chapterCount.setTextSize(fontSizeValue);
        tvQuestionType.setTextSize(fontSizeValue);
        tvAnswerAnalysis.getSettings().setDefaultFontSize(fontSizeValue);
        tvQuestionContent.getSettings().setDefaultFontSize(fontSizeValue);
        tvAly.setTextSize(fontSizeValue);
        tvRightAnswer.setTextSize(fontSizeValue);
        tvDetailQuestion.setTextSize(fontSizeValue);
        tvAnswer.setTextSize(fontSizeValue);

        tvDetailQuestion.setBackgroundColor(Color.parseColor(bgColorValue));

        if ("#1d1d1f".equals(bgColorValue)) {
            setTextColor("#78787a");
        } else if ("#fdf2dc".equals(bgColorValue)) {
            setTextColor("#483a2f");
        } else if ("#e6cdae".equals(bgColorValue)) {
            setTextColor("#362f27");
        } else if ("#d2ecd3".equals(bgColorValue)) {
            setTextColor("#1f3721");
        } else if ("#f0e1e6".equals(bgColorValue)) {
            setTextColor("#752935");
        } else if ("#f3f7f9".equals(bgColorValue)) {
            setTextColor("#333333");
        } else if ("#ffffff".equals(bgColorValue)) {
            setTextColor("#333333");
        } else if ("#B3000000".equals(bgColorValue)) {
            setTextColor("#cdcdcd");
        } else {
            setTextColor("#000000");
        }

        if (showAnalysis || data.isShowAnswer()) {
            llAnswerAnalysis.setVisibility(View.VISIBLE);
        } else {
            llAnswerAnalysis.setVisibility(View.GONE);
        }
    }

    private void setTextColor(String txtColor) {
        tvChoiceAnswer.setTextColor(Color.parseColor(txtColor));
        chapterCount.setTextColor(Color.parseColor(txtColor));
        tvRightAnswer.setTextColor(Color.parseColor(txtColor));
        tvDetailQuestion.setTextColor(Color.parseColor(txtColor));
    }

    private void examCommit() {
        EasyHttp.post((LifecycleOwner) answerActivity)
                .api(new ExamAnswerApi().setBillId(answer.getBillId()).setOptionNo(answer.getOptionNo()).setQuestionId(answer.getQuestionId()))
                .request(new HttpCallback<String>((OnHttpListener) answerActivity) {
                    @Override
                    public void onSucceed(String result) {
                        super.onSucceed(result);
                    }
                });
    }


    public void onOptionClick() {
        if (0 == data.getUserAnswerList().size()) {
            return;
        }
        String optionStr = "";
        for (String option : data.getUserAnswerList()
        ) {
            optionStr += "," + option;
        }
        answer.setOptionNo(optionStr.substring(1));

        if (1 == data.getQuestionType()) {//单选

            if (data.getChoiceAnswerList().size() == data.getUserAnswerList().size() && data.getChoiceAnswerList().containsAll(data.getUserAnswerList())) {
                answerRight = true;
            } else {
                answerRight = false;
            }
            examCommit();
            answerActivity.nextQuestion(answerRight);
        } else if (2 == data.getQuestionType()) {//多选
            if (null != data.getUserAnswerList()) {
                EasyLog.print(new Gson().toJson(answer.getOptionNo()));
                if (data.getChoiceAnswerList().size() == data.getUserAnswerList().size() && data.getChoiceAnswerList().containsAll(data.getUserAnswerList())) {
                    answerRight = true;
                    examCommit();
                    answerActivity.nextQuestion(answerRight);
                }
                if (!data.getChoiceAnswerList().containsAll(data.getUserAnswerList())) {
                    answerRight = false;
                    examCommit();
                    answerActivity.nextQuestion(answerRight);
                }
            }
        }

        data.setHasAnswer(true);

        if (showAnalysis || (answerShow)) {
            llAnswerAnalysis.setVisibility(View.VISIBLE);
        } else {
            llAnswerAnalysis.setVisibility(View.GONE);
        }
    }
}
