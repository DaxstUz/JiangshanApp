package com.jiangshan.knowledge.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.MenuAdapter;
import com.jiangshan.knowledge.activity.news.ArticleDetailActivity;
import com.jiangshan.knowledge.http.api.BannerApi;
import com.jiangshan.knowledge.http.api.GetPassportApi;
import com.jiangshan.knowledge.http.entity.Article;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Menu;
import com.jiangshan.knowledge.http.entity.Passport;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.http.model.HttpListData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private RelativeLayout rlTitle;

    private ConvenientBanner banner;
    private List<Article> bannerData = new ArrayList<>();

    private RecyclerView rvMenu;
    private MenuAdapter menuAdapter;
    private List<Menu> menuDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initBanner();
        initView();
        getBannerData();
        updateUI();

        getInitData();
    }

    public void updateUI() {
        Course course = LocalDataUtils.getCourse(this);
        if (null == course || course.getCourseName().length() == 0) {
            setTitle("请选择科目");
        } else {
            setTitle(course.getCourseName());
        }
    }

    private void initView() {
        rlTitle = findView(R.id.rl_title);
        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SubjectDetailActivity.class));
            }
        });

        rvMenu = findView(R.id.rv_menu);
        menuDatas.add(new Menu(1, "随机练习"));
        menuDatas.add(new Menu(2, "章节练习"));
        menuDatas.add(new Menu(3, "真题练习"));
        menuDatas.add(new Menu(4, "模拟考试"));
        menuDatas.add(new Menu(5, "错题集"));
        menuDatas.add(new Menu(6, "答题历史"));
        menuDatas.add(new Menu(7, "收藏"));
        menuDatas.add(new Menu(8, "考点必记"));
        menuAdapter = new MenuAdapter(R.layout.item_home_menu, menuDatas);
        rvMenu.setAdapter(menuAdapter);
        rvMenu.setLayoutManager(new GridLayoutManager(this, 4));
        menuAdapter.setOnItemClickListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Subject subject = LocalDataUtils.getSubject(HomeActivity.this);
                if(null!=subject){
                    Intent intent;
                    switch (position) {
                        case 0:
                            intent = new Intent(HomeActivity.this, SelectAnserModelActivity.class);
                            intent.putExtra("examType", 4);
                            startActivityForResult(intent, RESULT_OK);
                            break;
                        case 1:
                            intent = new Intent(HomeActivity.this, ChapterListActivity.class);
                            intent.putExtra("examType", 3);
                            startActivityForResult(intent, RESULT_OK);
                            break;
                        case 2:
                            intent = new Intent(HomeActivity.this, ExamListActivity.class);
                            intent.putExtra("examType", 1);//examType考试类型:1.历届真题;2.模拟考试;3.章节练习题;4.随机
                            startActivityForResult(intent, RESULT_OK);
                            break;
                        case 3:
                            intent = new Intent(HomeActivity.this, ExamListActivity.class);
                            intent.putExtra("examType", 2);
                            startActivityForResult(intent, RESULT_OK);
                            break;
                        case 4:
                            intent = new Intent(HomeActivity.this, ExamMarkActivity.class);
                            intent.putExtra("title", "错题集");
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
                            intent.putExtra("title", "收藏");
                            intent.putExtra("type", "collect");
                            startActivityForResult(intent, RESULT_OK);
                            break;

                        case 7:
                            intent = new Intent(HomeActivity.this, ExamFocusListActivity.class);
                            intent.putExtra("examType", 2);
                            startActivityForResult(intent, RESULT_OK);
                            break;

                    }
                }else{
                    ToastUtils.show("请选择科目！");
                }

            }
        });

    }

    private void initBanner() {
        banner = findView(R.id.banner);
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
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
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getApplicationContext(), ArticleDetailActivity.class);
                        intent.putExtra("aiticle", bannerData.get(position));
                        startActivity(intent);
                    }
                })
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
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
    }

    private void getInitData() {
        EasyHttp.post(this)
                .api(new GetPassportApi())
                .request(new HttpCallback<HttpData<Passport>>(this) {

                    @Override
                    public void onSucceed(HttpData<Passport> result) {
                        Passport passport= result.getData();
                        LocalDataUtils.saveLocalData(HomeActivity.this,LocalDataUtils.localUserName,LocalDataUtils.passport,new Gson().toJson(passport));
                    }
                });
    }
}
