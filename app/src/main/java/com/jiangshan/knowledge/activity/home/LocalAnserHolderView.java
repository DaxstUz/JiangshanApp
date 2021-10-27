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
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.api.ExamAnswerApi;
import com.jiangshan.knowledge.http.entity.Answer;
import com.jiangshan.knowledge.http.entity.Question;

/**
 * auth s_yz  2021/10/13
 */
public class LocalAnserHolderView extends Holder<Question> {

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

    public LocalAnserHolderView(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    private AnswerActivity answerActivity;

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

        addListener();
    }

    private void addListener() {
        llAnswerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonDrawable();
                ivAnswerA.setImageResource(R.mipmap.rb_answer_right);
                data.setChooseIndex(1);
                answer.setOptionNo("A");
                examCommit();
            }
        });
        llAnswerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonDrawable();
                ivAnswerB.setImageResource(R.mipmap.rb_answer_right);
                data.setChooseIndex(2);
                answer.setOptionNo("B");
                examCommit();
            }
        });
        llAnswerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonDrawable();
                ivAnswerC.setImageResource(R.mipmap.rb_answer_right);
                data.setChooseIndex(3);
                answer.setOptionNo("C");
                examCommit();
            }
        });
        llAnswerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonDrawable();
                ivAnswerD.setImageResource(R.mipmap.rb_answer_right);
                data.setChooseIndex(4);
                answer.setOptionNo("D");
                examCommit();
            }
        });
    }

    private void resetButtonDrawable() {
        ivAnswerA.setImageResource(R.mipmap.rb_answer_a);
        ivAnswerB.setImageResource(R.mipmap.rb_answer_b);
        ivAnswerC.setImageResource(R.mipmap.rb_answer_c);
        ivAnswerD.setImageResource(R.mipmap.rb_answer_d);
    }

    private void setSelect(int selectIndex) {
        switch (selectIndex) {
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
    }

    private Question data;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateUI(Question data) {
        this.data = data;
//        System.out.println("updateUI===>" + new Gson().toJson(data));
        resetButtonDrawable();
        setSelect(data.getChooseIndex());
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

        boolean showAnalysis = ((AnswerActivity) itemView.getContext()).getIntent().getBooleanExtra("showAnalysis", false);
        if (showAnalysis) {
            llAnswerAnalysis.setVisibility(View.VISIBLE);
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
}
