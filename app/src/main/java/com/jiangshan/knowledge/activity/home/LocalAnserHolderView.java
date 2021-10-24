package com.jiangshan.knowledge.activity.home;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bigkoo.convenientbanner.holder.Holder;
import com.jiangshan.knowledge.R;
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

    private RadioGroup rgAnswer;
    private RadioButton rbAnswerA;
    private RadioButton rbAnswerB;
    private RadioButton rbAnswerC;
    private RadioButton rbAnswerD;

    private View itemView;
    public LocalAnserHolderView(View itemView) {
        super(itemView);
        this.itemView=itemView;
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

        rgAnswer = itemView.findViewById(R.id.rg_answer);
        rbAnswerA = itemView.findViewById(R.id.rb_answer_a);
        rbAnswerB = itemView.findViewById(R.id.rb_answer_b);
        rbAnswerC = itemView.findViewById(R.id.rb_answer_c);
        rbAnswerD = itemView.findViewById(R.id.rb_answer_d);

        addListener();
    }

    private void addListener() {
        rgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                resetButtonDrawable();

                Drawable[] drawables;
                Drawable left;
                switch (checkedId) {
                    case R.id.rb_answer_a:
                        drawables = rbAnswerA.getCompoundDrawables();
                        left = itemView.getResources().getDrawable(R.mipmap.rb_answer_right);
                        left.setBounds(drawables[0].getBounds());
                        rbAnswerA.setCompoundDrawables(left, drawables[1], drawables[2], drawables[3]);
                        break;
                    case R.id.rb_answer_b:
                        drawables = rbAnswerB.getCompoundDrawables();
                        left = itemView.getResources().getDrawable(R.mipmap.rb_answer_right);
                        left.setBounds(drawables[0].getBounds());
                        rbAnswerB.setCompoundDrawables(left, drawables[1], drawables[2], drawables[3]);
                        break;
                    case R.id.rb_answer_c:
                        drawables = rbAnswerC.getCompoundDrawables();
                        left = itemView.getResources().getDrawable(R.mipmap.rb_answer_right);
                        left.setBounds(drawables[0].getBounds());
                        rbAnswerC.setCompoundDrawables(left, drawables[1], drawables[2], drawables[3]);
                        break;
                    case R.id.rb_answer_d:
                        drawables = rbAnswerD.getCompoundDrawables();
                        left = itemView.getResources().getDrawable(R.mipmap.rb_answer_right);
                        left.setBounds(drawables[0].getBounds());
                        rbAnswerD.setCompoundDrawables(left, drawables[1], drawables[2], drawables[3]);
                        break;
                }
            }
        });
    }

    private void resetButtonDrawable() {

        Drawable[] drawablesA = rbAnswerA.getCompoundDrawables();
        Drawable leftA = itemView.getResources().getDrawable(R.mipmap.rb_answer_a);
        leftA.setBounds(drawablesA[0].getBounds());
        rbAnswerA.setCompoundDrawables(leftA, drawablesA[1], drawablesA[2], drawablesA[3]);

        Drawable[] drawablesB = rbAnswerB.getCompoundDrawables();
        Drawable leftB = itemView.getResources().getDrawable(R.mipmap.rb_answer_b);
        leftB.setBounds(drawablesB[0].getBounds());
        rbAnswerB.setCompoundDrawables(leftB, drawablesB[1], drawablesB[2], drawablesB[3]);

        Drawable[] drawablesC = rbAnswerC.getCompoundDrawables();
        Drawable leftC = itemView.getResources().getDrawable(R.mipmap.rb_answer_c);
        leftC.setBounds(drawablesC[0].getBounds());
        rbAnswerC.setCompoundDrawables(leftC, drawablesC[1], drawablesC[2], drawablesC[3]);

        Drawable[] drawablesD = rbAnswerD.getCompoundDrawables();
        Drawable leftD = itemView.getResources().getDrawable(R.mipmap.rb_answer_d);
        leftD.setBounds(drawablesD[0].getBounds());
        rbAnswerD.setCompoundDrawables(leftD, drawablesD[1], drawablesD[2], drawablesD[3]);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateUI(Question data) {
        chapterCount.setText("/" + data.getTotal());
        String examName=((AnswerActivity)itemView.getContext()).getIntent().getStringExtra("examName");
        chapterName.setText(examName);
        tvRank.setText(data.getRank() + "");
        tvQuestionType.setText(data.getQuestionTypeDesc());
        tvChoiceAnswer.setText(data.getChoiceAnswer());
        tvQuestionContent.setText("             " + Html.fromHtml(data.getContent(), Html.FROM_HTML_MODE_COMPACT));
        tvAnswerAnalysis.setText(Html.fromHtml(data.getAnswerAnalysis(), Html.FROM_HTML_MODE_COMPACT));

        for (int i = 0; i < data.getQuestionOptionList().size(); i++) {
            String content = Html.fromHtml(data.getQuestionOptionList().get(i).getContent(), Html.FROM_HTML_MODE_COMPACT).toString();
            content = content.replace("\n", "");
            if (0 == i) {
                rbAnswerA.setText(content);
            } else if (1 == i) {
                rbAnswerB.setText(content);
            }
            if (2 == i) {
                rbAnswerC.setText(content);
            }
            if (3 == i) {
                rbAnswerD.setText(content);
            }
        }


        boolean showAnalysis=((AnswerActivity)itemView.getContext()).getIntent().getBooleanExtra("showAnalysis",false);
        if(showAnalysis){
            llAnswerAnalysis.setVisibility(View.VISIBLE);
        }

    }
}
