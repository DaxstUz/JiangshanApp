package com.jiangshan.knowledge.activity.home;

import static com.umeng.socialize.utils.ContextUtil.getContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.jiangshan.knowledge.http.api.ExamRandomStartApi;
import com.jiangshan.knowledge.http.api.ExamStartApi;
import com.jiangshan.knowledge.http.api.GetExamCollectListApi;
import com.jiangshan.knowledge.http.api.GetExamErrorListApi;
import com.jiangshan.knowledge.http.api.QuestionMarkApi;
import com.jiangshan.knowledge.http.api.UnCollectApi;
import com.jiangshan.knowledge.http.entity.AnswerBgColor;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Question;
import com.jiangshan.knowledge.http.entity.QuestionInfo;
import com.jiangshan.knowledge.http.entity.QuetionCount;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.http.model.HttpListDataAll;
import com.jiangshan.knowledge.uitl.AlertButtonClick;
import com.jiangshan.knowledge.uitl.DialogUtil;
import com.jiangshan.knowledge.uitl.FloatingWindowUtils;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * ????????????????????????
 * http://127.0.0.1:8080/exam/list/100101/10010101?examType=1&pageNum=1&pageSize=30
 * <p>
 * ????????????????????????????????????
 * http://127.0.0.1:8080/user/exam/start/100101/10010101
 * ?????????
 * examCode: XG201411
 * examType: 1
 * ?????????billId: 147
 * <p>
 * ????????????????????????
 * http://127.0.0.1:8080/user/exam/questionList/147?pageNum=1&pageSize=75
 * <p>
 * ------------------
 * <p>
 * ????????????????????????
 * http://127.0.0.1:8080/exam/list/100101/10010101?examType=1&pageNum=1&pageSize=30
 * ????????????????????????????????????
 * http://127.0.0.1:8080/exam/questionList/100101/10010101?examCode=XG201811&examType=1
 * ????????????????????????
 * ===============
 * <p>
 * ??????????????????????????????ABCD???????????????http://127.0.0.1:8080/user/exam/answer/114
 * ?????????JSON?????????
 * {"optionNo":"C","questionId":91,"lastQuestionIndex":15}
 * <p>
 * ??????????????????????????????
 * http://127.0.0.1:8080/user/exam/finish/114
 * <p>
 * <p>
 * auth s_yz  2021/10/21
 */
public class AnswerActivity extends BaseActivity {

    private ConvenientBanner answerPager;
    private List<Question> questionDatas = new ArrayList();

    private LinearLayout llAnswerCommit;
    private LinearLayout llAnswerCount;
    private NestedScrollView llChapter;
    private LinearLayout llSettingLine;
    private boolean showDiaglog = true;

    private ImageView ivCollect;
    private TextView tvCollectCount;
    private TextView tvAnswerRight;
    private TextView tvAnswerError;

    private TextView tv_more_question;
    private TextView tv_anli_question;

    private TextView tvFontSize;
    private TextView tvModel;
    private TextView tvModelRead;
    private ImageView ivModel;
    private ImageView ivModelRead;

    private RecyclerView rv_bg_color;
    private AnswerBgColorAdapter bgColorAdapter;
    private List<AnswerBgColor> colorDatas = new ArrayList<>();

    private RecyclerView rvChapterMain;
    private ChapterMainAdapter chapterMainAdapter;
    private List<Question> questionDatas1 = new ArrayList();

    private RecyclerView rvChapterMain2;
    private ChapterMainAdapter chapterMainAdapter2;
    private List<Question> questionDatas2 = new ArrayList();

    private RecyclerView rvChapterMain3;
    private ChapterMainAdapter chapterMainAdapter3;
    private List<Question> questionDatas3 = new ArrayList();

    private SeekBar sbLight;

    private boolean answerNext;//???????????????????????????
    private boolean settingVibrator;//??????

    private int billId;

    private int fontSizeValue;
    private boolean showAnalysis;

    public void showAnswer() {
        int currentIndex = answerPager.getCurrentItem();
        Question question = questionDatas.get(currentIndex);
        question.setShowAnswer(!question.isShowAnswer());
        answerPager.notifyDataSetChanged();
        answerPager.setCurrentItem(currentIndex, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        setTitle("??????????????????");
        setBackViewVisiable();
        initView();

        LocalDataUtils.saveLocalData(this, LocalDataUtils.activityName, LocalDataUtils.activityName, "AnswerActivity");

        showAnalysis = getIntent().getBooleanExtra("showAnalysis", false);
        billId = getIntent().getIntExtra("billId", 0);
        boolean ismark = getIntent().getBooleanExtra("ismark", false);//??????
        if (billId > 0) {
            llAnswerCommit.setVisibility(View.GONE);
            LocalDataUtils.saveLocalDataInteger(AnswerActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyBillid, billId);
            getQuestion(billId);
        } else if (ismark) {
            llAnswerCommit.setVisibility(View.GONE);
            getMarkData();
        } else {
            if (showAnalysis) {
                llAnswerCommit.setVisibility(View.GONE);
                getShowQuestion();
            } else {
                examStart();
            }

        }
        getPermisson();

        FloatingWindowUtils.getInstance().init(this);
    }

    private void setSeeView() {
        boolean keyHand = LocalDataUtils.getLocalDataBoolean(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyHand);
        if (!showAnalysis && keyHand) {
            FloatingWindowUtils.getInstance().showFloatingWindow(R.layout.floating_view);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSeeView();
    }

    private void getPermisson() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            boolean falg = Settings.canDrawOverlays(this);
            if (!falg) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 10086);
            }
        }
    }

    private int pageNum = 1;

    private void getMarkData() {
        String type = getIntent().getStringExtra("type");
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);

        String api = null;
        if ("error".equals(type)) {
            api = new GetExamErrorListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setExamCode(getIntent().getStringExtra("examCode")).setPageNum(pageNum).setPageSize(getIntent().getIntExtra("pageSize", 0)).getApi();
        }
        if ("collect".equals(type)) {
            api = new GetExamCollectListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setExamCode(getIntent().getStringExtra("examCode")).setPageNum(pageNum).setPageSize(getIntent().getIntExtra("pageSize", 0)).getApi();
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
                        //??????????????????,??????????????????????????????
                        for (int i = 0; i < questionDatas.size(); i++) {
                            questionDatas.get(i).setRank(i + 1);
                            questionDatas.get(i).setBillId(billId);
                            questionDatas.get(i).setTotal(result.getData().getTotal());

                            if (1 == questionDatas.get(i).getQuestionType()) {
                                questionDatas1.add(questionDatas.get(i));
                            } else if (2 == questionDatas.get(i).getQuestionType()) {
                                questionDatas2.add(questionDatas.get(i));
                            } else if (4 == questionDatas.get(i).getQuestionType()) {
                                questionDatas3.add(questionDatas.get(i));
                            }
                        }
                        answerPager.notifyDataSetChanged();
                        chapterMainAdapter.setSelectIndex(0);
                        chapterMainAdapter.notifyDataSetChanged();
                        if (questionDatas2.size() > 0) {
                            tv_more_question.setVisibility(View.VISIBLE);
                            chapterMainAdapter2.setSingleTotal(questionDatas1.size());
                            chapterMainAdapter2.notifyDataSetChanged();
                        }
                        if (questionDatas3.size() > 0) {
                            tv_anli_question.setVisibility(View.VISIBLE);
                            chapterMainAdapter3.setSingleTotal(questionDatas1.size() + questionDatas2.size());
                            chapterMainAdapter3.notifyDataSetChanged();
                        }
                        llAnswerCount.setVisibility(View.VISIBLE);

                        if (questionDatas.size() > 0) {
                            updateCount(questionDatas.get(0));
                        }
                        setCollectCount();
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
                return "/user/exam/questionList/" + billId + "?pageNum=1&pageSize=3000";
            }
        }).request(new HttpCallback<HttpListData<Question>>(this) {
            @Override
            public void onSucceed(HttpListData<Question> result) {
                if (result.isSuccess()) {
                    questionDatas.addAll(result.getData().getList());
                    for (int i = 0; i < questionDatas.size(); i++) {
                        questionDatas.get(i).setRank(i + 1);
                        questionDatas.get(i).setBillId(billId);
                        questionDatas.get(i).setTotal(result.getData().getTotal());

                        if (1 == questionDatas.get(i).getQuestionType()) {
                            questionDatas1.add(questionDatas.get(i));
                        } else if (2 == questionDatas.get(i).getQuestionType()) {
                            questionDatas2.add(questionDatas.get(i));
                        } else if (4 == questionDatas.get(i).getQuestionType()) {
                            questionDatas3.add(questionDatas.get(i));
                        }
                    }
                    answerPager.notifyDataSetChanged();
                    chapterMainAdapter.setSelectIndex(0);
                    chapterMainAdapter.notifyDataSetChanged();
                    if (questionDatas2.size() > 0) {
                        tv_more_question.setVisibility(View.VISIBLE);
                        chapterMainAdapter2.setSingleTotal(questionDatas1.size());
                        chapterMainAdapter2.notifyDataSetChanged();
                    }

                    if (questionDatas3.size() > 0) {
                        tv_anli_question.setVisibility(View.VISIBLE);
                        chapterMainAdapter3.setSingleTotal(questionDatas1.size() + questionDatas2.size());
                        chapterMainAdapter3.notifyDataSetChanged();
                    }
                    llAnswerCount.setVisibility(View.VISIBLE);
                    setCollectCount();
                    updateCount(questionDatas.get(0));
                    setLastIndex();
                } else {
                    if (0 == result.getCode()) {
                        ToastUtils.show("????????????????????????????????????");
                    } else {
                        ToastUtils.show(result.getMsg());
                    }
                }
            }
        });
    }

    private void getShowQuestion() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);

        boolean random = getIntent().getBooleanExtra("random", false);
        String urlPath = null;
        if (random) {
            getShowRandomQuestion();
        } else {
            urlPath = "/exam/questionList/" + subject.getSubjectCode() + "/" + course.getCourseCode() + "?examCode=" + getIntent().getStringExtra("examCode") + "&examType=" + getIntent().getIntExtra("examType", 1);
            String finalUrlPath = urlPath;
            EasyHttp.get(this).api(new IRequestApi() {
                @Override
                public String getApi() {
                    return finalUrlPath;
                }
            }).request(new HttpCallback<HttpListDataAll<Question>>(this) {
                @Override
                public void onSucceed(HttpListDataAll<Question> result) {
                    if (result.isSuccess()) {
                        questionDatas.addAll(result.getData());
                        for (int i = 0; i < questionDatas.size(); i++) {
                            questionDatas.get(i).setRank(i + 1);
                            questionDatas.get(i).setBillId(billId);
                            questionDatas.get(i).setTotal(result.getData().size());

                            if (1 == questionDatas.get(i).getQuestionType()) {
                                questionDatas1.add(questionDatas.get(i));
                            } else if (2 == questionDatas.get(i).getQuestionType()) {
                                questionDatas2.add(questionDatas.get(i));
                            } else if (4 == questionDatas.get(i).getQuestionType()) {
                                questionDatas3.add(questionDatas.get(i));
                            }
                        }
                        answerPager.notifyDataSetChanged();
                        chapterMainAdapter.setSelectIndex(0);
                        chapterMainAdapter.notifyDataSetChanged();
                        if (questionDatas2.size() > 0) {
                            tv_more_question.setVisibility(View.VISIBLE);
                            chapterMainAdapter2.setSingleTotal(questionDatas1.size());
                            chapterMainAdapter2.notifyDataSetChanged();
                        }

                        if (questionDatas3.size() > 0) {
                            tv_anli_question.setVisibility(View.VISIBLE);
                            chapterMainAdapter3.setSingleTotal(questionDatas1.size() + questionDatas2.size());
                            chapterMainAdapter3.notifyDataSetChanged();
                        }
                        llAnswerCount.setVisibility(View.VISIBLE);
                        if (questionDatas.size() > 0) {
                            updateCount(questionDatas.get(0));
                        }
                        setLastIndex();
                        setCollectCount();
                    }
                }
            });
        }
    }

    private void getShowRandomQuestion() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);

        String urlPath = null;
        Map<String, Object> paramAll = new HashMap<>();
        Type userListType = new TypeToken<ArrayList<QuetionCount>>() {
        }.getType();
        ArrayList<QuetionCount> quetionCounts = new Gson().fromJson(getIntent().getStringExtra("questionTypeQtySet"), userListType);
        Map<String, Object> param = new HashMap<>();
        for (int i = 0; i < quetionCounts.size(); i++) {
            param.put(quetionCounts.get(i).getId() + "", quetionCounts.get(i).getCount());
        }
        paramAll.put("courseCode", course.getCourseCode());
        paramAll.put("questionTypeQtySet", param);
        paramAll.put("examType", getIntent().getIntExtra("examType", 1));
        urlPath = "/exam/randQuestionList/" + subject.getSubjectCode() + "/" + course.getCourseCode();
        String finalUrlPath = urlPath;
        EasyHttp.post(this).api(new IRequestApi() {
            @Override
            public String getApi() {
                return finalUrlPath;
            }
        }).json(new Gson().toJson(paramAll))
                .request(new HttpCallback<HttpListDataAll<Question>>(this) {
                    @Override
                    public void onSucceed(HttpListDataAll<Question> result) {
                        if (result.isSuccess()) {
                            questionDatas.addAll(result.getData());
                            for (int i = 0; i < questionDatas.size(); i++) {
                                questionDatas.get(i).setRank(i + 1);
                                questionDatas.get(i).setBillId(billId);
                                questionDatas.get(i).setTotal(result.getData().size());

                                if (1 == questionDatas.get(i).getQuestionType()) {
                                    questionDatas1.add(questionDatas.get(i));
                                } else if (2 == questionDatas.get(i).getQuestionType()) {
                                    questionDatas2.add(questionDatas.get(i));
                                } else if (4 == questionDatas.get(i).getQuestionType()) {
                                    questionDatas3.add(questionDatas.get(i));
                                }
                            }
                            answerPager.notifyDataSetChanged();
                            chapterMainAdapter.setSelectIndex(0);
                            chapterMainAdapter.notifyDataSetChanged();
                            if (questionDatas2.size() > 0) {
                                tv_more_question.setVisibility(View.VISIBLE);
                                chapterMainAdapter2.setSingleTotal(questionDatas1.size());
                                chapterMainAdapter2.notifyDataSetChanged();
                            }
                            if (questionDatas3.size() > 0) {
                                tv_anli_question.setVisibility(View.VISIBLE);
                                chapterMainAdapter3.setSingleTotal(questionDatas1.size() + questionDatas2.size());
                                chapterMainAdapter3.notifyDataSetChanged();
                            }
                            llAnswerCount.setVisibility(View.VISIBLE);
                            if (questionDatas.size() > 0) {
                                updateCount(questionDatas.get(0));
                            }

                            setLastIndex();
                        }
                    }
                });


    }

    private void initView() {
        answerNext = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerNext);
        settingVibrator = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.keyVibrator);

        llAnswerCommit = findView(R.id.ll_answer_commit);
        llSettingLine = findView(R.id.ll_setting_line);
        tvFontSize = findView(R.id.tv_font_size);
        sbLight = findView(R.id.sb_light);
        llChapter = findView(R.id.ll_chapter);

        tv_more_question = findView(R.id.tv_more_question);
        tv_anli_question = findView(R.id.tv_anli_question);

        ivModelRead = findView(R.id.iv_model_read);
        tvModelRead = findView(R.id.tv_model_read);

        ivModel = findView(R.id.iv_model);
        tvModel = findView(R.id.tv_model);
        ivCollect = findView(R.id.iv_collect);
        tvCollectCount = findView(R.id.tv_collect_count);
        tvAnswerRight = findView(R.id.tv_answer_right);
        tvAnswerError = findView(R.id.tv_answer_error);

        rv_bg_color = findView(R.id.rv_bg_color);
        rv_bg_color.setLayoutManager(new GridLayoutManager(this, 6));
        colorDatas.add(new AnswerBgColor("#faf3dd"));
        colorDatas.add(new AnswerBgColor("#e1ceb0"));
        colorDatas.add(new AnswerBgColor("#d6ebd4"));
        colorDatas.add(new AnswerBgColor("#eee1e4"));
        colorDatas.add(new AnswerBgColor("#f3f6f8"));
        colorDatas.add(new AnswerBgColor("#ffffff"));
        bgColorAdapter = new AnswerBgColorAdapter(R.layout.item_answer_bg, colorDatas);
        rv_bg_color.setAdapter(bgColorAdapter);

        rvChapterMain = findView(R.id.rv_chapter_main);
        chapterMainAdapter = new ChapterMainAdapter(R.layout.item_adapter_main, questionDatas1);
        rvChapterMain.setAdapter(chapterMainAdapter);
        rvChapterMain.setLayoutManager(new GridLayoutManager(this, 6));

        rvChapterMain2 = findView(R.id.rv_chapter_main2);
        chapterMainAdapter2 = new ChapterMainAdapter(R.layout.item_adapter_main, questionDatas2);
        rvChapterMain2.setAdapter(chapterMainAdapter2);
        rvChapterMain2.setLayoutManager(new GridLayoutManager(this, 6));

        rvChapterMain3 = findView(R.id.rv_chapter_main3);
        chapterMainAdapter3 = new ChapterMainAdapter(R.layout.item_adapter_main, questionDatas3);
        rvChapterMain3.setAdapter(chapterMainAdapter3);
        rvChapterMain3.setLayoutManager(new GridLayoutManager(this, 6));

        llAnswerCount = findView(R.id.ll_answer_count);

        answerPager = findView(R.id.answer);

        String bgColorValue = LocalDataUtils.getLocalData(getContext(), LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue);
        if (null == bgColorValue || bgColorValue.length() == 0) {
            bgColorValue = "#ffffff";
        }
        answerPager.setBackgroundColor(Color.parseColor(bgColorValue));
        rvChapterMain.setBackgroundColor(Color.parseColor(bgColorValue));
        llAnswerCount.setBackgroundColor(Color.parseColor(bgColorValue));
        setModel();
        setListener();
    }

    private void setListener() {
        bgColorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                LocalDataUtils.saveLocalData(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue, colorDatas.get(position).getCorlorStr());
                answerPager.notifyDataSetChanged();
                answerPager.setBackgroundColor(Color.parseColor(colorDatas.get(position).getCorlorStr()));
                rvChapterMain.setBackgroundColor(Color.parseColor(colorDatas.get(position).getCorlorStr()));
                llAnswerCount.setBackgroundColor(Color.parseColor(colorDatas.get(position).getCorlorStr()));
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
                llChapter.setVisibility(View.GONE);
                answerPager.setCurrentItem(position, false);
                updateCount(questionDatas.get(position));
                chapterMainAdapter.setSelectIndex(position);
                chapterMainAdapter.notifyDataSetChanged();
            }
        });

        chapterMainAdapter2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                llChapter.setVisibility(View.GONE);
                answerPager.setCurrentItem(position + questionDatas1.size(), false);
                updateCount(questionDatas1.get(position));
                chapterMainAdapter.setSelectIndex(position);
                chapterMainAdapter2.notifyDataSetChanged();
            }
        });

        chapterMainAdapter3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                llChapter.setVisibility(View.GONE);
                answerPager.setCurrentItem(position + questionDatas1.size() + questionDatas2.size(), false);
                updateCount(questionDatas3.get(position));
                chapterMainAdapter.setSelectIndex(position);
                chapterMainAdapter3.notifyDataSetChanged();
            }
        });

        answerPager.setPages(
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

        answerPager.setOnPageChangeListener(new OnPageChangeListener() {
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

        answerPager.setOnClickListener(new View.OnClickListener() {
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
        if (1 == question.getCollectFlag()) {
            ivCollect.setImageResource(R.mipmap.answer_collect);
        } else {
            ivCollect.setImageResource(R.mipmap.answer_uncollect);
        }
        setCollectCount();
    }

    private void postCollect() {
        Question question = questionDatas.get(answerPager.getCurrentItem());

        String apiPath;
        if (1 == question.getCollectFlag()) {
            apiPath = new UnCollectApi().setQuestionId(question.getId()).getApi();
            EasyHttp.delete(this)
                    .api(apiPath)
                    .request(new HttpCallback<HttpData<QuestionInfo>>(this) {
                        @Override
                        public void onSucceed(HttpData<QuestionInfo> result) {
                            if (result.isSuccess()) {
                                ToastUtils.show("?????????????????????");
                                question.setCollectFlag(0);
                                updateCount(question);
                                chapterMainAdapter.notifyDataSetChanged();
                                chapterMainAdapter2.notifyDataSetChanged();
                            }
                        }
                    });
        } else {
            apiPath = new QuestionMarkApi().setExamCode(question.getExamCode()).setQuestionId(question.getId()).setMarkType("1").getApi();
            EasyHttp.post(this)
                    .api(apiPath)
                    .request(new HttpCallback<HttpData<QuestionInfo>>(this) {
                        @Override
                        public void onSucceed(HttpData<QuestionInfo> result) {
                            if (result.isSuccess()) {
                                ToastUtils.show("???????????????");
                                question.setCollectFlag(1);
                                updateCount(question);
                                chapterMainAdapter.notifyDataSetChanged();
                                chapterMainAdapter2.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private String examCode;
    private int examType;

    private void examStart() {
        examType = getIntent().getIntExtra("examType", 1);
        examCode = getIntent().getStringExtra("examCode");
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);

        IRequestApi apiUrl = null;
        boolean random = getIntent().getBooleanExtra("random", false);
        if (random) {
            Type userListType = new TypeToken<ArrayList<QuetionCount>>() {
            }.getType();
            ArrayList<QuetionCount> quetionCounts = new Gson().fromJson(getIntent().getStringExtra("questionTypeQtySet"), userListType);
            Map<String, Integer> param = new HashMap<>();
            for (int i = 0; i < quetionCounts.size(); i++) {
                param.put(quetionCounts.get(i).getId() + "", quetionCounts.get(i).getCount());
            }
            apiUrl = new ExamRandomStartApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setExamType(examType).setQuestionTypeQtySet(param);
        } else {
            apiUrl = new ExamStartApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setExamType(examType).setExamCode(examCode);
        }
        if (null == apiUrl) {
            finish();
        }
        EasyHttp.post(this)
                .api(apiUrl)
                .request(new HttpCallback<HttpData<QuestionInfo>>(this) {
                    @Override
                    public void onSucceed(HttpData<QuestionInfo> result) {
                        if (result.isSuccess()) {
                            LocalDataUtils.saveLocalDataInteger(AnswerActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyBillid, result.getData().getBillId());
                            getQuestion(result.getData().getBillId());
                        } else {
                            if (0 == result.getCode()) {
                                ToastUtils.show("????????????????????????????????????");
                            } else {
                                ToastUtils.show(result.getMsg());
                            }
                            finish();
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
            if (null != question.getCollectFlag() && 1 == question.getCollectFlag()) {
                collectCount++;
            }

            if (null != question.getUserAnswerList()
                    && null != question.getChoiceAnswerList()
                    && question.getUserAnswerList().size() > 0) {
                if (question.getChoiceAnswerList().containsAll(question.getUserAnswerList()) && question.getChoiceAnswerList().size() == question.getUserAnswerList().size()) {
                    rightCount++;
                } else {
                    errorCount++;
                }
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
                            LocalDataUtils.saveLocalDataInteger(AnswerActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyBillid, 0);
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

    public void nextQuestion(Boolean answerRight) {
        llChapter.setVisibility(View.GONE);
        setCollectCount();
        if (null == answerRight) {
            return;
        }
        if (answerRight && answerNext && questionDatas.size() - 1 != answerPager.getCurrentItem()) {
            answerPager.setCurrentItem(answerPager.getCurrentItem() + 1, false);
        } else if (settingVibrator && !answerRight) {
            vibrator();
        }

        //?????????????????????????????????
        if (null != examCode && examCode.length() > 0 && questionDatas.size() > 0) {
            Question question = questionDatas.get(answerPager.getCurrentItem());
            LocalDataUtils.saveLocalData(this, LocalDataUtils.anwserQuestion, examCode, new Gson().toJson(question));
            LocalDataUtils.saveLocalData(this, LocalDataUtils.anwserQuestion, LocalDataUtils.keyLastQuestion, new Gson().toJson(question));
            LocalDataUtils.saveLocalDataInteger(this, LocalDataUtils.anwserQuestion, LocalDataUtils.keyExamType, examType);
            LocalDataUtils.saveLocalData(this, LocalDataUtils.anwserQuestion, LocalDataUtils.keyExamName, getIntent().getStringExtra("examName"));
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
                    answerPager.notifyDataSetChanged();
                } else {
                    ToastUtils.show("???????????????18???");
                }
                break;
            case R.id.rv_font_size_add:
                if (fontSizeValue <= 32) {
                    fontSizeValue++;
                    LocalDataUtils.saveLocalDataInteger(this, LocalDataUtils.settingDataName, LocalDataUtils.fontSizeValue, fontSizeValue);
                    tvFontSize.setText(fontSizeValue + "");
                    answerPager.notifyDataSetChanged();
                } else {
                    ToastUtils.show("???????????????33???");
                }
                break;
            case R.id.ll_answer_commit:
                DialogUtil.DialogAttrs attrs = new DialogUtil.DialogAttrs();
                attrs.title = "??????";
                attrs.msg = "????????????????????????";
                attrs.textGravity = Gravity.CENTER;
                attrs.btnVal = new String[]{"??????", "??????"};
                attrs.isCancelable = Boolean.FALSE;
                DialogUtil.alertDialog(AnswerActivity.this, attrs, new AlertButtonClick() {
                    @Override
                    public void leftBtnClick(AlertDialog dlg) {
                        dlg.dismiss();
                    }

                    @Override
                    public void rightBtnClick(AlertDialog dlg) {
                        dlg.dismiss();
                        examEnd();
                    }
                });
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

                if (modelLight) {
                    LocalDataUtils.saveLocalData(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue, "#B3000000");
                    answerPager.setBackgroundColor(Color.parseColor("#B3000000"));
                } else {
                    LocalDataUtils.saveLocalData(AnswerActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.bgColorValue, "#ffffff");
                    answerPager.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                setModel();
                answerPager.notifyDataSetChanged();
                llSettingLine.setVisibility(View.GONE);
                break;
            case R.id.ll_answer_count:
                llChapter.setVisibility(View.VISIBLE);
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
                intent.putExtra("question", questionDatas.get(answerPager.getCurrentItem()));
                intent.putExtra("examCode", getIntent().getStringExtra("examCode"));
                startActivityForResult(intent, 0);
                llSettingLine.setVisibility(View.GONE);
                break;
            case R.id.ll_switch_model:
                showAnalysis = getIntent().getBooleanExtra("showAnalysis", false);
                getIntent().putExtra("showAnalysis", !showAnalysis);
                setModel();
                answerPager.notifyDataSetChanged();
                llSettingLine.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * ????????????Activity??????????????????
     * ?????????????????????????????????255???????????????????????????
     * screenBrightness ??????????????????[0,1]??????
     */
    public static void setBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    private void setModel() {
        showAnalysis = getIntent().getBooleanExtra("showAnalysis", false);
        if (showAnalysis) {
            tvModel.setText("????????????");
            ivModel.setImageResource(R.mipmap.model_answer);
        } else {
            tvModel.setText("????????????");
            ivModel.setImageResource(R.mipmap.model_read);
        }

        boolean modelLight = LocalDataUtils.getLocalDataBoolean(this, LocalDataUtils.settingDataName, LocalDataUtils.modelLight);
        if (modelLight) {
            tvModelRead.setText("????????????");
            ivModelRead.setImageResource(R.mipmap.model_night);
        } else {
            tvModelRead.setText("????????????");
            ivModelRead.setImageResource(R.mipmap.model_light);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        FloatingWindowUtils.getInstance().hideFloatWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatingWindowUtils.getInstance().unInit();
    }

    private void setLastIndex() {
        if (null != examCode && examCode.length() > 0) {
            Question question = new Gson().fromJson(LocalDataUtils.getLocalData(this, LocalDataUtils.anwserQuestion, examCode), Question.class);
            if (null != question) {
                for (int i = 0; i < questionDatas.size(); i++) {
//                    questionDatas.get(i).setHasAnswer(true);
                    if (question.getId() == questionDatas.get(i).getId()) {
                        answerPager.setCurrentItem(i, false);
                        setCollectCount();
                        ToastUtils.show("?????????????????????????????????" + (i + 1) + "???");
                        break;
                    }
                }
            }
        }
    }
}
