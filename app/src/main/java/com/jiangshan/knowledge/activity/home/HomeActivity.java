package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.EasyLog;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.ExamHistoryListAdapter;
import com.jiangshan.knowledge.activity.home.adapter.MenuAdapter;
import com.jiangshan.knowledge.activity.news.ArticleDetailActivity;
import com.jiangshan.knowledge.http.api.BannerApi;
import com.jiangshan.knowledge.http.api.GetExamHistoryListApi;
import com.jiangshan.knowledge.http.api.GetMemberInfoApi;
import com.jiangshan.knowledge.http.api.GetPassportApi;
import com.jiangshan.knowledge.http.entity.Article;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.ExamHistory;
import com.jiangshan.knowledge.http.entity.MemberInfo;
import com.jiangshan.knowledge.http.entity.Menu;
import com.jiangshan.knowledge.http.entity.Passport;
import com.jiangshan.knowledge.http.entity.Question;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.SubjectInfo;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.uitl.DateUtil;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.jiangshan.knowledge.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private RelativeLayout rlTitle;

    private ConvenientBanner banner;
    private List<Article> bannerData = new ArrayList<>();

    private RecyclerView rvMenu;
    private MenuAdapter menuAdapter;
    private List<Menu> menuDatas = new ArrayList<>();

    private TextView tvExamDay;
    private TextView tvTips;

    //????????????
    private RecyclerView rvExam;
    private ExamHistoryListAdapter examAdapter;
    private List<ExamHistory> datas = new ArrayList<>();
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initBanner();
        initView();
        initHistoryView();
        getBannerData();
        initLoadMore();

        updateUI();

        switchLastActivity();
    }

    private void switchLastActivity() {
        int billid = LocalDataUtils.getLocalDataInteger(this, LocalDataUtils.localUserName, LocalDataUtils.keyBillid);
        String activityFlag = LocalDataUtils.getLocalData(this, LocalDataUtils.activityName, LocalDataUtils.activityName);
        Question question = new Gson().fromJson(LocalDataUtils.getLocalData(this, LocalDataUtils.anwserQuestion, LocalDataUtils.keyLastQuestion), Question.class);
        if (billid > 0 && "AnswerActivity".equals(activityFlag) && null != question) {
            int examType = LocalDataUtils.getLocalDataInteger(this, LocalDataUtils.anwserQuestion, LocalDataUtils.keyExamType);
            String examName = LocalDataUtils.getLocalData(this, LocalDataUtils.anwserQuestion, LocalDataUtils.keyExamName);
            Intent intent = new Intent(HomeActivity.this, AnswerActivity.class);
            intent.putExtra("examCode", question.getExamCode());
            intent.putExtra("examName", examName);
            intent.putExtra("examType", examType);
            intent.putExtra("billId", billid);
            intent.putExtra("showUserAnalysis", true);
            startActivity(intent);
        }
        LocalDataUtils.saveLocalData(this, LocalDataUtils.activityName, LocalDataUtils.activityName, "HomeActivity");
    }

    public void updateUI() {
        Course course = LocalDataUtils.getCourse(this);
        if (null == course || course.getCourseName().length() == 0) {
            setTitle("???????????????");
        } else {
            setTitle(course.getCourseName());
        }
        pageNum = 1;
        datas.clear();
        getInitData();
        getData();
        getMemberData();

    }


    private void initView() {
        tvTips = findView(R.id.tv_tips);
        tvExamDay = findView(R.id.tv_exam_day);

        rlTitle = findView(R.id.rl_title);
        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SubjectDetailActivity.class));
            }
        });

        rvMenu = findView(R.id.rv_menu);
        menuDatas.add(new Menu(1, "????????????"));
        menuDatas.add(new Menu(2, "????????????"));
        menuDatas.add(new Menu(3, "????????????"));
        menuDatas.add(new Menu(4, "????????????"));
        menuDatas.add(new Menu(5, "?????????"));
        menuDatas.add(new Menu(6, "????????????"));
        menuDatas.add(new Menu(7, "??????"));
        menuDatas.add(new Menu(8, "????????????"));
        menuAdapter = new MenuAdapter(R.layout.item_home_menu, menuDatas);
        rvMenu.setAdapter(menuAdapter);
        rvMenu.setLayoutManager(new GridLayoutManager(this, 4));
        menuAdapter.setOnItemClickListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (judgeLogin()) {
                    if (judgeSuject()) {
                        Intent intent;
                        switch (position) {
                            case 0:
                                intent = new Intent(HomeActivity.this, SelectAnserModelActivity.class);
                                intent.putExtra("examType", 4);
                                intent.putExtra("examName", "????????????");
                                intent.putExtra("random", true);
                                startActivityForResult(intent, RESULT_OK);
                                break;
                            case 1:
                                intent = new Intent(HomeActivity.this, ChapterListActivity.class);
                                intent.putExtra("examType", 3);
                                startActivityForResult(intent, RESULT_OK);
                                break;
                            case 2:
                                intent = new Intent(HomeActivity.this, ExamListActivity.class);
                                intent.putExtra("examType", 1);//examType????????????:1.????????????;2.????????????;3.???????????????;4.??????
                                startActivityForResult(intent, RESULT_OK);
                                break;
                            case 3:
                                intent = new Intent(HomeActivity.this, ExamListActivity.class);
                                intent.putExtra("examType", 2);
                                startActivityForResult(intent, RESULT_OK);
                                break;
                            case 4:
                                intent = new Intent(HomeActivity.this, ExamMarkActivity.class);
                                intent.putExtra("title", "?????????");
                                intent.putExtra("type", "error");
                                startActivityForResult(intent, RESULT_OK);
                                break;
                            case 5:
                                intent = new Intent(HomeActivity.this, HistoryAnswerActivity.class);
                                intent.putExtra("examType", 2);
                                startActivityForResult(intent, RESULT_OK);
                                break;
                            case 6:
                                intent = new Intent(HomeActivity.this, ExamMarkActivity.class);
                                intent.putExtra("title", "??????");
                                intent.putExtra("type", "collect");
                                startActivityForResult(intent, RESULT_OK);
                                break;
                            case 7:
                                intent = new Intent(HomeActivity.this, ExamFocusListActivity.class);
                                intent.putExtra("examType", 2);
                                startActivityForResult(intent, RESULT_OK);
                                break;
                        }
                    }
                }
            }
        });

    }

    private void initBanner() {
        banner = findView(R.id.banner);
        //???????????????Holder?????????????????????????????????????????????????????????????????????????????????????????????
        banner.setPages(
                new CBViewHolderCreator() {
                    @Override
                    public LocalImageHolderView createHolder(View itemView) {
                        return new LocalImageHolderView(itemView);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_banner;
                    }
                }, bannerData)
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????,????????????????????????????????????
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getApplicationContext(), ArticleDetailActivity.class);
                        intent.putExtra("aiticle", bannerData.get(position));
                        startActivity(intent);
                    }
                })
                //????????????????????????
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    private void initHistoryView() {
        rvExam = findViewById(R.id.rv_exam);
        examAdapter = new ExamHistoryListAdapter(R.layout.item_exam_history_list, datas);
        rvExam.setAdapter(examAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return super.canScrollVertically();
            }
        };
        rvExam.setLayoutManager(linearLayoutManager);
        examAdapter.setOnItemClickListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(HomeActivity.this, AnswerActivity.class);
                intent.putExtra("examCode", datas.get(position).getExamCode());
                intent.putExtra("examName", datas.get(position).getExamName());
                intent.putExtra("examType", datas.get(position).getExamType());
                intent.putExtra("billId", datas.get(position).getId());
                if (4 == datas.get(position).getExamType()) {
                    intent.putExtra("random", true);
                }
                intent.putExtra("showAnalysis", true);
                intent.putExtra("showUserAnalysis", true);
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    private void getBannerData() {
        EasyHttp.get(this)
                .api(new BannerApi()
                        .setPageNum(1).setPageSize(5))
                .request(new HttpCallback<HttpListData<Article>>(this) {
                    @Override
                    public void onSucceed(HttpListData<Article> result) {
                        bannerData.addAll(result.getData().getList());
                        banner.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        setExamDay();
    }

    private void getInitData() {
        EasyHttp.post(this)
                .api(new GetPassportApi())
                .request(new HttpCallback<HttpData<Passport>>(this) {

                    @Override
                    public void onSucceed(HttpData<Passport> result) {
                        Passport passport = result.getData();
                        LocalDataUtils.saveLocalData(HomeActivity.this, LocalDataUtils.localUserName, LocalDataUtils.passport, new Gson().toJson(passport));
                        setExamDay();
                        updateApk();
                    }
                });
    }

    private void setExamDay() {
        Passport passport = new Gson().fromJson(LocalDataUtils.getLocalData(this, LocalDataUtils.localUserName, LocalDataUtils.passport), Passport.class);
        if (null != passport) {
            Subject subject = LocalDataUtils.getSubject(this);
            if (null == subject || null == passport.getSubjectInfoList()) {
                return;
            }
            SubjectInfo subjectInfo = null;
            for (int i = 0; i < passport.getSubjectInfoList().size(); i++) {
                if (
                        passport.getSubjectInfoList().get(i).getSubjectCode().equals(subject.getSubjectCode())
                ) {
                    subjectInfo = passport.getSubjectInfoList().get(i);
                }
            }
            if (null != subjectInfo) {
                int day = (int) ((DateUtil.paseFromLong(subjectInfo.getExamTime()).getTime() - new Date().getTime()) / 24 / 60 / 60 / 1000);
                if (day > 0) {
                    tvExamDay.setText(day + "???");
                } else {
                    tvTips.setText("??????????????????????????????????????????");
                    tvExamDay.setVisibility(View.GONE);
                }
            }
        }
    }


    private void getData() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        if (null == subject || null == course) {
            return;
        }

        EasyHttp.get(this)
                .api(new GetExamHistoryListApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()).setPageNum(pageNum))
                .request(new HttpCallback<HttpListData<ExamHistory>>(this) {
                    @Override
                    public void onSucceed(HttpListData<ExamHistory> result) {
                        if (result.isSuccess()) {
                            if (result.getData().getList().size() < result.getData().getPageSize()) {
                                //??????????????????,??????????????????????????????
                                examAdapter.getLoadMoreModule().loadMoreEnd();
                            } else {
                                examAdapter.getLoadMoreModule().loadMoreComplete();
                            }
                            if (1 == pageNum) {
                                datas.clear();
                            }
                            datas.addAll(result.getData().getList());
                            examAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * ?????????????????????
     */
    private void initLoadMore() {
        examAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        examAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNum++;
                getData();
            }
        });
        examAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //??????????????????????????????????????????????????????????????????????????????????????????(?????????true)
        examAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void getMemberData() {
        Subject subject = LocalDataUtils.getSubject(this);
        if (null == subject) {
            return;
        }
        User user = LocalDataUtils.getUser(this);
        if (null == user) {
            return;
        }
        EasyHttp.get(this)
                .api(new GetMemberInfoApi().setSubjectCode(subject.getSubjectCode()))
                .request(new HttpCallback<HttpData<MemberInfo>>(this) {
                    @Override
                    public void onSucceed(HttpData<MemberInfo> result) {
                        if (result.isSuccess()) {
                            Gson gson = new Gson();
                            String member = gson.toJson(result.getData());
                            LocalDataUtils.saveLocalData(HomeActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyMember, member);
                        }
                    }
                });
    }
}
