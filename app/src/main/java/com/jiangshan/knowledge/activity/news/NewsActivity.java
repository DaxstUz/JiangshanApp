package com.jiangshan.knowledge.activity.news;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.news.adapter.MyFragmentPagerAdapter;
import com.jiangshan.knowledge.activity.news.fragment.NewsFragment;
import com.jiangshan.knowledge.http.api.SearchArticleCatetoryApi;
import com.jiangshan.knowledge.http.model.HttpListDataAll;
import com.jiangshan.knowledge.view.NewsTitleHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends BaseActivity implements NewsTitleHorizontalScrollView.OnItemClickListener {

    private ViewPager vpNews;
    private List<Fragment> listfragment = new ArrayList<Fragment>();
    private MyFragmentPagerAdapter fragmentPagerAdapter;

    private NewsTitleHorizontalScrollView newsTitleHorizontalScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
        getCategoryData();
    }

    private void initView() {
        setTitle("发现");
        vpNews = findViewById(R.id.vp_news);

        newsTitleHorizontalScrollView = findViewById(R.id.myHorizeontal);
        newsTitleHorizontalScrollView.setOnItemClickListener(this);

        FragmentManager fm = this.getSupportFragmentManager();
        fragmentPagerAdapter = new MyFragmentPagerAdapter(fm, listfragment); //new myFragmentPagerAdater记得带上两个参数

        vpNews.setAdapter(fragmentPagerAdapter);
        vpNews.setCurrentItem(0); //设置当前页是第一页

        vpNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (newsTitleHorizontalScrollView != null) {
                    newsTitleHorizontalScrollView.setPagerChangeListenerToTextView(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
    }

    private void getCategoryData() {
        EasyHttp.get(this)
                .api(new SearchArticleCatetoryApi())
                .request(new HttpCallback<HttpListDataAll<SearchArticleCatetoryApi.Bean>>(this) {
                    @Override
                    public void onSucceed(HttpListDataAll<SearchArticleCatetoryApi.Bean> result) {
                        if (result.isSuccess()) {
                            analyzeData(result.getData());
                        }
                    }
                });
    }

    private void analyzeData(List<SearchArticleCatetoryApi.Bean> datas) {
        for (int i = 0; i < datas.size(); i++) {
            newsTitleHorizontalScrollView.addTextViewTitle(datas.get(i).getCategoryName(), this);
            listfragment.add(NewsFragment.newInstance(datas.get(i).getId() + ""));
        }

        fragmentPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int pos) {
        vpNews.setCurrentItem(pos);
    }

}
