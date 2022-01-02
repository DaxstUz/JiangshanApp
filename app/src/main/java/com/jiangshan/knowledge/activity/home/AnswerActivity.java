package com.jiangshan.knowledge.activity.home;

import static com.umeng.socialize.utils.ContextUtil.getContext;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.jiangshan.knowledge.activity.home.adapter.AnswerBgColorAdapter;
import com.jiangshan.knowledge.activity.home.adapter.ChapterMainAdapter;
import com.jiangshan.knowledge.activity.person.SettingActivity;
import com.jiangshan.knowledge.http.api.ExamEndApi;
import com.jiangshan.knowledge.http.api.ExamStartApi;
import com.jiangshan.knowledge.http.api.GetExamCollectListApi;
import com.jiangshan.knowledge.http.api.GetExamErrorListApi;
import com.jiangshan.knowledge.http.api.QuestionMarkApi;
import com.jiangshan.knowledge.http.api.UnCollectApi;
import com.jiangshan.knowledge.http.entity.AnswerBgColor;
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
    private LinearLayout ll_answer_main;
    private LinearLayout llSettingLine;
    private boolean showDiaglog = true;

    private ImageView ivCollect;
    private TextView tvCollectCount;
    private TextView tvAnswerRight;
    private TextView tvAnswerError;

    private TextView tvFontSize;
    private TextView tvModel;
    private TextView tvModelRead;
    private ImageView ivModel;
    private ImageView ivModelRead;

    private RecyclerView rv_bg_color;
    private AnswerBgColorAdapter bgColorAdapter;
    private List<AnswerBgColor> colorDatas=new ArrayList<>();

    private RecyclerView rvChapterMain;
    private ChapterMainAdapter chapterMainAdapter;

    private SeekBar sbLight;

    private boolean answerNext;//开启答对跳转下一题
    private boolean settingVibrator;//震动

    private int billId;

    private int fontSizeValue;

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
                        chapterMainAdapter.setSelectIndex(0);
                        chapterMainAdapter.notifyDataSetChanged();
                        llAnswerCount.setVisibility(View.VISIBLE);

                        if(questionDatas.size()>0){
                            updateCount(questionDatas.get(0));
                        }
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
                return "/user/exam/questionList/" + billId + "?pageNum=1&pageSize=100";
            }
        }).request(new HttpCallback<HttpListData<Question>>(this) {
            @Override
            public void onSucceed(HttpListData<Question> result) {
                if (result != null) {
                    questionDatas.addAll(result.getData().getList());
                    for (int i = 0; i < questionDatas.size(); i++) {
                        questionDatas.get(i).setRank(i + 1);
                        questionDatas.get(i).setBillId(billId);
                        questionDatas.get(i).setTotal(result.getData().getTotal());
                    }
                    answer.notifyDataSetChanged();
                    chapterMainAdapter.setSelectIndex(0);
                    chapterMainAdapter.notifyDataSetChanged();
                    llAnswerCount.setVisibility(View.VISIBLE);
                    updateCount(questionDatas.get(0));
                }
            }
        });
    }

    private void initView() {
        answerNext = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerNext);
        settingVibrator = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.keyVibrator);

        llSettingLine = findView(R.id.ll_setting_line);
        tvFontSize = findView(R.id.tv_font_size);
        sbLight = findView(R.id.sb_light);
        ll_answer_main = findView(R.id.ll_answer_main);

        ivModelRead = findView(R.id.iv_model_read);
        tvModelRead = findView(R.id.tv_model_read);

        ivModel = findView(R.id.iv_model);
        tvModel = findView(R.id.tv_model);
        ivCollect = findView(R.id.iv_collect);
        tvCollectCount = findView(R.id.tv_collect_count);
        tvAnswerRight = findView(R.id.tv_answer_right);
        tvAnswerError = findView(R.id.tv_answer_error);

        rv_bg_color = findView(R.id.rv_bg_color);
        rv_bg_color.setLayoutManager(new GridLayoutManager(this,6));
        colorDatas.add(new AnswerBgColor("#1d1d1f"));
        colorDatas.add(new AnswerBgColor("#fdf2dc"));
        colorDatas.add(new AnswerBgColor("#e6cdae"));
        colorDatas.add(new AnswerBgColor("#d2ecd3"));
        colorDatas.add(new AnswerBgColor("#f0e1e6"));
        colorDatas.add(new AnswerBgColor("#f3f7f9"));
        bgColorAdapter=new AnswerBgColorAdapter(R.layout.item_answer_bg,colorDatas);
        rv_bg_color.setAdapter(bgColorAdapter);

        rvChapterMain = findView(R.id.rv_chapter_main);
        chapterMainAdapter = new ChapterMainAdapter(R.layout.item_adapter_main, questionDatas);
        rvChapterMain.setAdapter(chapterMainAdapter);
        rvChapterMain.setLayoutManager(new GridLayoutManager(this, 6));

        llAnswerCount = findView(R.id.ll_answer_count);

        operate = findView(R.id.ll_operate);
        operate.setVisibility(View.VISIBLE);

        answer = findView(R.id.answer);

        String  bgColorValue = LocalDataUtils.getLocalData(getContext(), LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue);
        if(null==bgColorValue||bgColorValue.length()==0){
            bgColorValue="#ffffff";
        }
        answer.setBackgroundColor(Color.parseColor(bgColorValue));
        setModel();
        setListener();
    }

    private void setListener() {
        bgColorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                LocalDataUtils.saveLocalData(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue, colorDatas.get(position).getCorlorStr());
                answer.notifyDataSetChanged();
                answer.setBackgroundColor(Color.parseColor(colorDatas.get(position).getCorlorStr()));
                llSettingLine.setVisibility(View.GONE);
            }
        });


        fontSizeValue = LocalDataUtils.getLocalDataInteger(this, LocalDataUtils.settingDataName, LocalDataUtils.fontSizeValue);
        tvFontSize.setText(fontSizeValue + "");
        int modelLight = LocalDataUtils.getLocalDataInteger(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.lightValue);
        sbLight.setProgress(modelLight);
        sbLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LocalDataUtils.saveLocalDataInteger(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.lightValue, progress);
                setBrightness(AnswerActivity.this, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        chapterMainAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                rvChapterMain.setVisibility(View.GONE);
                answer.setCurrentItem(position, false);
                chapterMainAdapter.setSelectIndex(position);
                chapterMainAdapter.notifyDataSetChanged();
            }
        });

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

        operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examEnd();
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSettingLine.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        answerNext = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerNext);
        settingVibrator = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.keyVibrator);
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
        tvCollectCount.setText(collectCount + "");
        tvAnswerRight.setText(rightCount + "");
        tvAnswerError.setText(errorCount + "");
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

    public void nextQuestion(boolean answerRight) {
        if (answerRight && answerNext && questionDatas.size() - 1 != answer.getCurrentItem()) {
            answer.setCurrentItem(answer.getCurrentItem() + 1, false);
        } else if (settingVibrator) {
            vibrator();
        }
    }

    public void answerClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rv_font_size_del:
                if (fontSizeValue >= 19) {
                    fontSizeValue--;
                    LocalDataUtils.saveLocalDataInteger(this, LocalDataUtils.settingDataName, LocalDataUtils.fontSizeValue, fontSizeValue);
                    tvFontSize.setText(fontSizeValue + "");
                    answer.notifyDataSetChanged();
                } else {
                    ToastUtils.show("最小字号为18！");
                }
                break;
            case R.id.rv_font_size_add:
                if (fontSizeValue <= 32) {
                    fontSizeValue++;
                    LocalDataUtils.saveLocalDataInteger(this, LocalDataUtils.settingDataName, LocalDataUtils.fontSizeValue, fontSizeValue);
                    tvFontSize.setText(fontSizeValue + "");
                    answer.notifyDataSetChanged();
                } else {
                    ToastUtils.show("最大字号为33！");
                }
                break;
            case R.id.ll_collect:
                postCollect();
                break;
            case R.id.ll_close:
                llSettingLine.setVisibility(View.GONE);
                break;
            case R.id.ll_model_read:
                boolean modelLight = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.modelLight);
                LocalDataUtils.saveLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.modelLight, !modelLight);

                if(modelLight){
                    LocalDataUtils.saveLocalData(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue,"#B3000000");
                    answer.setBackgroundColor(Color.parseColor("#B3000000"));
                }else{
                    LocalDataUtils.saveLocalData(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue,"#ffffff");
                    answer.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                setModel();
                answer.notifyDataSetChanged();
                llSettingLine.setVisibility(View.GONE);
                break;
            case R.id.ll_answer_count:
                rvChapterMain.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_setting_more:
                llSettingLine.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_setting:
                intent = new Intent(AnswerActivity.this, SettingActivity.class);
                startActivityForResult(intent, 0);
                llSettingLine.setVisibility(View.GONE);
                break;
            case R.id.ll_question_feedback:
                intent = new Intent(AnswerActivity.this, QuestionFeedbackActivity.class);
                intent.putExtra("question", questionDatas.get(answer.getCurrentItem()));
                intent.putExtra("examCode", getIntent().getStringExtra("examCode"));
                startActivityForResult(intent, 0);
                llSettingLine.setVisibility(View.GONE);
                break;
            case R.id.ll_switch_model:
                boolean showAnalysis = getIntent().getBooleanExtra("showAnalysis", false);
                getIntent().putExtra("showAnalysis", !showAnalysis);
                setModel();
                answer.notifyDataSetChanged();
                llSettingLine.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 设置当前Activity显示时的亮度
     * 屏幕亮度最大数值一般为255，各款手机有所不同
     * screenBrightness 的取值范围在[0,1]之间
     */
    public static void setBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    private void setModel() {
        boolean showAnalysis = getIntent().getBooleanExtra("showAnalysis", false);
        if (showAnalysis) {
            tvModel.setText("答题模式");
            ivModel.setImageResource(R.mipmap.model_answer);
        } else {
            tvModel.setText("背景模式");
            ivModel.setImageResource(R.mipmap.model_read);
        }

        boolean modelLight = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.modelLight);
        if (modelLight) {
            tvModelRead.setText("夜间模式");
            ivModelRead.setImageResource(R.mipmap.model_night);
        } else {
            tvModelRead.setText("日间模式");
            ivModelRead.setImageResource(R.mipmap.model_light);
        }
    }

}
