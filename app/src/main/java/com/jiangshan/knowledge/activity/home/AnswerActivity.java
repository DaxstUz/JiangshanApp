package com.jiangshan.knowledge.activity.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.ExamStartApi;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Question;
import com.jiangshan.knowledge.http.entity.QuestionInfo;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 第一步：考试列表
 * http://127.0.0.1:8080/exam/list/100101/10010101?examType=1&pageNum=1&pageSize=30
 * <p>
 * 第二步：答题模式（按钮）
 * http://127.0.0.1:8080/user/exam/start/100101/10010101
 * 入参：
 * examCode: XG201411
 * examType: 1
 * 出参：billId: 147
 * <p>
 * 第三步：加载考题
 * http://127.0.0.1:8080/user/exam/questionList/147?pageNum=1&pageSize=75
 * <p>
 * ------------------
 * <p>
 * 第一步：考试列表
 * http://127.0.0.1:8080/exam/list/100101/10010101?examType=1&pageNum=1&pageSize=30
 * 第二步：背景解析（按钮）
 * http://127.0.0.1:8080/exam/questionList/100101/10010101?examCode=XG201811&examType=1
 * 背景解析只有两步
 * ===============
 * <p>
 * 答题第四步，用户选择ABCD操作请求：http://127.0.0.1:8080/user/exam/answer/114
 * 入参为JSON格式的
 * {"optionNo":"C","questionId":91,"lastQuestionIndex":15}
 * <p>
 * 答题第五步：交卷操作
 * http://127.0.0.1:8080/user/exam/finish/114
 * <p>
 * <p>
 * auth s_yz  2021/10/21
 */
public class AnswerActivity extends BaseActivity {

    private ConvenientBanner answer;
    private List<Question> questionDatas = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        setTitle("江山老师题库");
        setBackViewVisiable();


        examStart();


        initView();
    }

    private void getQuestion(int billId) {
        EasyHttp.get(this).api(new IRequestApi() {
            @Override
            public String getApi() {
                return "/user/exam/questionList/" + billId + "?pageNum=1&pageSize=75";
            }
        }).request(new HttpCallback<HttpListData<Question>>(this) {
            @Override
            public void onSucceed(HttpListData<Question> result) {
                if (result != null) {
                    questionDatas.addAll(result.getData().getList());
                    for (int i = 0; i < questionDatas.size(); i++) {
                        questionDatas.get(i).setTotal(result.getData().getTotal());
                    }
                    answer.notifyDataSetChanged();
                }
            }
        });
    }

    private void initView() {
        answer = findView(R.id.answer);
        answer.setPages(
                new CBViewHolderCreator() {
                    @Override
                    public LocalAnserHolderView createHolder(View itemView) {
                        return new LocalAnserHolderView(itemView);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_answer;
                    }
                }, questionDatas);
    }

    private void examStart() {
        int examType = getIntent().getIntExtra("examType", 1);
        String examCode = getIntent().getStringExtra("examCode");
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        EasyHttp.post(this)
                .api(new ExamStartApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setExamType(examType).setExamCode(examCode))
                .request(new HttpCallback<HttpData<QuestionInfo>>(this) {
                    @Override
                    public void onSucceed(HttpData<QuestionInfo> result) {
                        if (result.isSuccess()) {
                            getQuestion(result.getData().getBillId());
                        }
                    }
                });
    }

    @Override
    public void onStart(Call call) {
        showDialog();
    }


    @Override
    public void onEnd(Call call) {
        hideDialog();
    }
}
