package com.jiangshan.knowledge.activity.home;

import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;

import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.EasyLog;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.api.ExamAnswerApi;
import com.jiangshan.knowledge.http.entity.Answer;
import com.jiangshan.knowledge.http.entity.Question;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * auth s_yz  2021/10/13
 */
public class LocalAnserHolderView extends Holder<Question> implements View.OnClickListener {

    private TextView chapterCount;
    private TextView chapterName;
    private TextView tvRank;
    private TextView tvQuestionType;
    private TextView tvQuestionContent;
    private TextView tvAnswerAnalysis;
    private TextView tvChoiceAnswer;

    private LinearLayout llAnswerAnalysis;

    private TextView tvAnswerA;
    private TextView tvAnswerB;
    private TextView tvAnswerC;
    private TextView tvAnswerD;

    private ImageView ivAnswerA;
    private ImageView ivAnswerB;
    private ImageView ivAnswerC;
    private ImageView ivAnswerD;

    private LinearLayout llAnswerA;
    private LinearLayout llAnswerB;
    private LinearLayout llAnswerC;
    private LinearLayout llAnswerD;

    private View itemView;
    private Answer answer = new Answer();

    private AnswerActivity answerActivity;

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

        llAnswerAnalysis = itemView.findViewById(R.id.ll_answer_analysis);

        tvAnswerA = itemView.findViewById(R.id.rb_answer_a);
        tvAnswerB = itemView.findViewById(R.id.rb_answer_b);
        tvAnswerC = itemView.findViewById(R.id.rb_answer_c);
        tvAnswerD = itemView.findViewById(R.id.rb_answer_d);

        ivAnswerA = itemView.findViewById(R.id.iv_answer_a);
        ivAnswerB = itemView.findViewById(R.id.iv_answer_b);
        ivAnswerC = itemView.findViewById(R.id.iv_answer_c);
        ivAnswerD = itemView.findViewById(R.id.iv_answer_d);

        llAnswerA = itemView.findViewById(R.id.ll_answer_a);
        llAnswerB = itemView.findViewById(R.id.ll_answer_b);
        llAnswerC = itemView.findViewById(R.id.ll_answer_c);
        llAnswerD = itemView.findViewById(R.id.ll_answer_d);
        llAnswerA.setOnClickListener(this);
        llAnswerB.setOnClickListener(this);
        llAnswerC.setOnClickListener(this);
        llAnswerD.setOnClickListener(this);
    }


    private void resetButtonDrawable() {
        ivAnswerA.setImageResource(R.mipmap.rb_answer_a);
        ivAnswerB.setImageResource(R.mipmap.rb_answer_b);
        ivAnswerC.setImageResource(R.mipmap.rb_answer_c);
        ivAnswerD.setImageResource(R.mipmap.rb_answer_d);
    }

    private static final String ALLANSWER = "ABCD";

    private void setSelect() {
        //用户的答案
        int userIndex = (null == data.getUserAnswer() ? -1 : ALLANSWER.indexOf(data.getUserAnswer().trim()) + 1);
        //正确的答案
        int rightIndex = ALLANSWER.indexOf(data.getChoiceAnswer()) + 1;

        switch (userIndex) {
            case 1:
                ivAnswerA.setImageResource(R.mipmap.rb_answer_right);
                break;
            case 2:
                ivAnswerB.setImageResource(R.mipmap.rb_answer_right);
                break;
            case 3:
                ivAnswerC.setImageResource(R.mipmap.rb_answer_right);
                break;
            case 4:
                ivAnswerD.setImageResource(R.mipmap.rb_answer_right);
                break;
        }

        if (showAnalysis || (answerShow && rightIndex == userIndex)) {//显示答案解析
            llAnswerAnalysis.setVisibility(View.VISIBLE);
        } else {
            llAnswerAnalysis.setVisibility(View.GONE);
        }

        if (!showAnalysis) {
            return;
        }

        switch (rightIndex) {
            case 1:
                ivAnswerA.setImageResource(R.mipmap.rb_answer_right);
                break;
            case 2:
                ivAnswerB.setImageResource(R.mipmap.rb_answer_right);
                break;
            case 3:
                ivAnswerC.setImageResource(R.mipmap.rb_answer_right);
                break;
            case 4:
                ivAnswerD.setImageResource(R.mipmap.rb_answer_right);
                break;
        }

        switch (userIndex) {
            case 1:
                if (rightIndex == 1) {
                    ivAnswerA.setImageResource(R.mipmap.rb_answer_right);
                } else {
                    ivAnswerA.setImageResource(R.mipmap.rb_answer_error);
                }
                break;
            case 2:
                if (rightIndex == 2) {
                    ivAnswerB.setImageResource(R.mipmap.rb_answer_right);
                } else {
                    ivAnswerB.setImageResource(R.mipmap.rb_answer_error);
                }
                break;
            case 3:
                if (rightIndex == 3) {
                    ivAnswerC.setImageResource(R.mipmap.rb_answer_right);
                } else {
                    ivAnswerC.setImageResource(R.mipmap.rb_answer_error);
                }
                break;
            case 4:
                if (rightIndex == 4) {
                    ivAnswerD.setImageResource(R.mipmap.rb_answer_right);
                } else {
                    ivAnswerD.setImageResource(R.mipmap.rb_answer_error);
                }
                break;
        }

    }

    private Question data;
    private boolean showAnalysis;
    private boolean answerShow;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateUI(Question data) {
        this.data = data;
        showAnalysis = ((AnswerActivity) itemView.getContext()).getIntent().getBooleanExtra("showAnalysis", false);
        answerShow = LocalDataUtils.getLocalDataBoolean((AnswerActivity) itemView.getContext(), LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerShow);
        resetButtonDrawable();
        setSelect();

        answer.setQuestionId(data.getId());
        answer.setBillId(data.getBillId());

        chapterCount.setText("/" + data.getTotal());
        String examName = ((AnswerActivity) itemView.getContext()).getIntent().getStringExtra("examName");
        chapterName.setText(examName);

        tvRank.setText(data.getRank() + "");

        tvQuestionType.setText(data.getQuestionTypeDesc());
        tvChoiceAnswer.setText(data.getChoiceAnswer());
        tvQuestionContent.setText("          " + Html.fromHtml(data.getContent(), Html.FROM_HTML_MODE_COMPACT));
        tvAnswerAnalysis.setText(Html.fromHtml(data.getAnswerAnalysis(), Html.FROM_HTML_MODE_COMPACT));

        for (int i = 0; i < data.getQuestionOptionList().size(); i++) {
            String content = Html.fromHtml(data.getQuestionOptionList().get(i).getContent(), Html.FROM_HTML_MODE_COMPACT).toString();
            content = content.replace("\n", "");
            if (0 == i) {
                tvAnswerA.setText(content);
            } else if (1 == i) {
                tvAnswerB.setText(content);
            }
            if (2 == i) {
                tvAnswerC.setText(content);
            }
            if (3 == i) {
                tvAnswerD.setText(content);
            }
        }
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

    @Override
    public void onClick(View v) {
        if (showAnalysis) {
            return;
        }

        Set<String> userAnswerList = data.getUserAnswerList();
        if (null == userAnswerList) {
            data.setUserAnswerList(new HashSet<>());
        }
        switch (v.getId()) {
            case R.id.ll_answer_a:
                resetButtonDrawable();
                ivAnswerA.setImageResource(R.mipmap.rb_answer_right);
                answer.setOptionNo(answer.getOptionNo() + ",A");
                data.getUserAnswerList().add("A");
                if ("A".equals(data.getChoiceAnswer())) {
                    answerRight = true;
                }
                break;
            case R.id.ll_answer_b:
                resetButtonDrawable();
                ivAnswerB.setImageResource(R.mipmap.rb_answer_right);
                answer.setOptionNo(answer.getOptionNo() + ",B");
                data.getUserAnswerList().add("B");
                if ("B".equals(data.getChoiceAnswer())) {
                    answerRight = true;
                }
                break;
            case R.id.ll_answer_c:
                resetButtonDrawable();
                ivAnswerC.setImageResource(R.mipmap.rb_answer_right);
                answer.setOptionNo(answer.getOptionNo() + ",C");
                data.getUserAnswerList().add("C");
                if ("C".equals(data.getChoiceAnswer())) {
                    answerRight = true;
                }
                break;
            case R.id.ll_answer_d:
                resetButtonDrawable();
                ivAnswerD.setImageResource(R.mipmap.rb_answer_right);
                answer.setOptionNo(answer.getOptionNo() + ",D");
                data.getUserAnswerList().add("D");
                if ("D".equals(data.getChoiceAnswer())) {
                    answerRight = true;
                }
                break;
        }

        setSelect();
        if (1 == data.getQuestionType()) {//单选
            answer.setOptionNo(answer.getOptionNo().substring(1));
//            EasyLog.print(new Gson().toJson(answer));
            examCommit();
            answerActivity.nextQuestion(answerRight);
        } else if (2 == data.getQuestionType()) {//多选

        }
        if (answerShow && answerRight) {
            llAnswerAnalysis.setVisibility(View.VISIBLE);
        }

        answerRight = false;
    }
}
