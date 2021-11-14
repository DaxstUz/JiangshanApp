package com.jiangshan.knowledge.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.ChapterMainAdapter;
import com.jiangshan.knowledge.http.api.ExamEndApi;
import com.jiangshan.knowledge.http.api.ExamStartApi;
import com.jiangshan.knowledge.http.api.GetExamCollectListApi;
import com.jiangshan.knowledge.http.api.GetExamErrorListApi;
import com.jiangshan.knowledge.http.api.QuestionMarkApi;
import com.jiangshan.knowledge.http.api.UnCollectApi;
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

    private LinearLayout operate;

    private LinearLayout llAnswerCount;
    private LinearLayout llCollect;

    private boolean showDiaglog = true;

    private ImageView ivCollect;
    private TextView tv_collect_count;
    private TextView tv_answer_right;
    private TextView tv_answer_error;

    private RecyclerView rvChapterMain;
    private ChapterMainAdapter chapterMainAdapter;

    private int billId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        setTitle("江山老师题库");
        setBackViewVisiable();
        initView();

        boolean ismark = getIntent().getBooleanExtra("ismark", false);
        if (ismark) {
            getMarkData();
        } else {
            examStart();
        }

    }

    private int pageNum = 1;

    private void getMarkData() {
        String type = getIntent().getStringExtra("type");
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);

        String api = null;
        if ("error".equals(type)) {
            api = new GetExamErrorListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setPageNum(pageNum).getApi();
        }
        if ("collect".equals(type)) {
            api = new GetExamCollectListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setPageNum(pageNum).getApi();
        }

        if (null == api) {
            return;
        }

        EasyHttp.get(this).api(api).request(new HttpCallback<HttpListData<Question>>(this) {
            @Override
            public void onSucceed(HttpListData<Question> result) {
                if (result != null) {
                    questionDatas.addAll(result.getData().getList());
                    if (result.getData().getList().size() < result.getData().getPageSize()) {
                        //如果不够一页,显示没有更多数据布局
                        for (int i = 0; i < questionDatas.size(); i++) {
                            questionDatas.get(i).setRank(i + 1);
                            questionDatas.get(i).setBillId(billId);
                            questionDatas.get(i).setTotal(result.getData().getTotal());
                        }
                        answer.notifyDataSetChanged();
                        llAnswerCount.setVisibility(View.VISIBLE);
                        updateCount(questionDatas.get(0));
                    } else {
                        pageNum++;
                        getMarkData();
                    }
                }
            }
        });
    }

    private void getQuestion(int billId) {
        this.billId = billId;
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
                        questionDatas.get(i).setBillId(billId);
                        questionDatas.get(i).setTotal(result.getData().getTotal());
                    }
                    answer.notifyDataSetChanged();
                    chapterMainAdapter.setSelectQuestionNo(questionDatas.get(0).getQuestionNo());
                    chapterMainAdapter.notifyDataSetChanged();
                    llAnswerCount.setVisibility(View.VISIBLE);
                    updateCount(questionDatas.get(0));
//                    answer.setCurrentItem(10,false);
                }
            }
        });
    }

    private void initView() {
        ivCollect = findView(R.id.iv_collect);
        tv_collect_count = findView(R.id.tv_collect_count);
        tv_answer_right = findView(R.id.tv_answer_right);
        tv_answer_error = findView(R.id.tv_answer_error);

        rvChapterMain = findView(R.id.rv_chapter_main);
        chapterMainAdapter = new ChapterMainAdapter(R.layout.item_adapter_main, questionDatas);
        rvChapterMain.setAdapter(chapterMainAdapter);
        rvChapterMain.setLayoutManager(new GridLayoutManager(this, 6));
        chapterMainAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                rvChapterMain.setVisibility(View.GONE);
                answer.setCurrentItem(position,false);
                chapterMainAdapter.setSelectQuestionNo(questionDatas.get(position).getQuestionNo());
                chapterMainAdapter.notifyDataSetChanged();
            }
        });

        llCollect = findView(R.id.ll_collect);
        llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCollect();
            }
        });

        llAnswerCount = findView(R.id.ll_answer_count);
        llAnswerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvChapterMain.setVisibility(View.VISIBLE);
            }
        });
        operate = findView(R.id.ll_operate);
        operate.setVisibility(View.VISIBLE);
        operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examEnd();
            }
        });

        answer = findView(R.id.answer);
        answer.setPages(
                new CBViewHolderCreator() {
                    @Override
                    public LocalAnserHolderView createHolder(View itemView) {
                        return new LocalAnserHolderView(itemView, AnswerActivity.this);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_answer;
                    }
                }, questionDatas);

        answer.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onPageSelected(int index) {
                Question question = questionDatas.get(index);
                updateCount(question);
            }
        });
    }

    private void updateCount(Question question) {
        if (0 == question.getCollectFlag()) {
            ivCollect.setImageResource(R.mipmap.answer_collect);
        } else {
            ivCollect.setImageResource(R.mipmap.answer_uncollect);
        }
        setCollectCount();
    }

    private void postCollect() {
        Question question = questionDatas.get(answer.getCurrentItem());

        String apiPath;
        if (0 == question.getCollectFlag()) {
            apiPath = new UnCollectApi().setQuestionId(question.getId()).getApi();
            EasyHttp.delete(this)
                    .api(apiPath)
                    .request(new HttpCallback<HttpData<QuestionInfo>>(this) {
                        @Override
                        public void onSucceed(HttpData<QuestionInfo> result) {
                            if (result.isSuccess()) {
                                ToastUtils.show("取消收藏成功！");
                                question.setCollectFlag(1);
                                updateCount(question);
                            }
                        }
                    });
        } else {
            apiPath = new QuestionMarkApi().setExamCode(question.getExamCode()).setQuestionId(question.getId()).getApi();
            EasyHttp.post(this)
                    .api(apiPath)
                    .request(new HttpCallback<HttpData<QuestionInfo>>(this) {
                        @Override
                        public void onSucceed(HttpData<QuestionInfo> result) {
                            if (result.isSuccess()) {
                                ToastUtils.show("收藏成功！");
                                question.setCollectFlag(0);
                                updateCount(question);
                            }
                        }
                    });
        }
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

    private void setCollectCount() {
        int collectCount = 0;
        int errorCount = 0;
        int rightCount = 0;
        for (Question question : questionDatas
        ) {
            if (null != question.getCollectFlag() && 0 == question.getCollectFlag()) {
                collectCount++;
            }
            if (null != question.getWrongFlag() && 1 == question.getWrongFlag()) {
                errorCount++;
            }
            if (null != question.getWrongFlag() && 0 == question.getWrongFlag()) {
                rightCount++;
            }
        }
        tv_collect_count.setText(collectCount + "");
        tv_answer_right.setText(rightCount + "");
        tv_answer_error.setText(errorCount + "");
    }


    private void examEnd() {
        EasyHttp.post(this)
                .api(new ExamEndApi().setBillId(billId))
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        if (result.isSuccess()) {
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onStart(Call call) {
        if (showDiaglog) {
            showDialog();
            showDiaglog = false;
        }

    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }
}
