package com.jiangshan.knowledge.activity.home;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.EasyLog;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.FeedbackItemAdapter;
import com.jiangshan.knowledge.http.api.QuestionFeedbackApi;
import com.jiangshan.knowledge.http.entity.FeedbackOption;
import com.jiangshan.knowledge.http.entity.Question;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.AlertButtonClick;
import com.jiangshan.knowledge.uitl.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/12/26
 */
public class QuestionFeedbackActivity extends BaseActivity {

    private TextView tvQuestionContent;
    private TextView tv_feedback_commit;

    private RecyclerView rvAnswerItem;
    private FeedbackItemAdapter answerItemAdapter;
    private List<FeedbackOption> questionOptionList = new ArrayList<>();

    private EditText etFeedbackContent;

    private Question question;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_feedback);
        setBackViewVisiable();
        setTitle("答题反馈");

        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView() {
        etFeedbackContent = findView(R.id.et_feedback_content);
        tv_feedback_commit = findView(R.id.tv_feedback_commit);
        tv_feedback_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitFeedback();
            }
        });

        tvQuestionContent = findView(R.id.tv_question_content);
        question = (Question) getIntent().getSerializableExtra("question");
        EasyLog.print(new Gson().toJson(question));
        tvQuestionContent.setText(Html.fromHtml(question.getContent(), Html.FROM_HTML_MODE_COMPACT));


        questionOptionList.add(new FeedbackOption(1, "答案有误", false));
        questionOptionList.add(new FeedbackOption(2, "答案与解析不相符", false));
        questionOptionList.add(new FeedbackOption(3, "题中有错别字", false));
        questionOptionList.add(new FeedbackOption(4, "选项有问题", false));
        questionOptionList.add(new FeedbackOption(0, "其他", false));

        rvAnswerItem = findViewById(R.id.rv_answer_item);
        rvAnswerItem.setLayoutManager(new LinearLayoutManager(this));
        answerItemAdapter = new FeedbackItemAdapter(R.layout.item_question_feedback_option, questionOptionList);
        rvAnswerItem.setAdapter(answerItemAdapter);
        answerItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                questionOptionList.get(position).setChecked(!questionOptionList.get(position).isChecked());
                answerItemAdapter.notifyDataSetChanged();
            }
        });
    }

    private void commitFeedback() {
        String feedbackType = "";
        for (int i = 0; i < questionOptionList.size(); i++) {
            if (questionOptionList.get(i).isChecked()) {
                feedbackType += questionOptionList.get(i).getId() + ",";
            }
        }

        if (feedbackType.length() > 1) {
            feedbackType = feedbackType.substring(0, feedbackType.length() - 1);
        }

        if (feedbackType.length() < 1) {
            ToastUtils.show("请勾选反馈类型！");
            return;
        }

        if (etFeedbackContent.getText().length() <= 10) {
            ToastUtils.show("反馈内容字数不要小于10个！");
            return;
        }

        EasyHttp.post(this)
                .api(new QuestionFeedbackApi().setExamCode(getIntent().getStringExtra("examCode")).setFeedbackType(feedbackType).setQuestionId(question.getId()).setContent(etFeedbackContent.getText().toString()))
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        if (result.isSuccess()) {
                            DialogUtil.DialogAttrs attrs = new DialogUtil.DialogAttrs();
                            attrs.msg = "感谢您提交本题问题反馈！";
                            attrs.textGravity = Gravity.CENTER;
                            attrs.btnVal = new String[]{"取消","确定"};
                            attrs.isCancelable = Boolean.FALSE;
                            DialogUtil.alertDialog(QuestionFeedbackActivity.this, attrs, new AlertButtonClick() {
                                @Override
                                public void leftBtnClick(AlertDialog dlg) {
                                    dlg.dismiss();
                                    finish();
                                }

                                @Override
                                public void rightBtnClick(AlertDialog dlg) {
                                    dlg.dismiss();
                                    finish();
                                }
                            });
                        }
                    }
                });
    }

}
