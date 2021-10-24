package com.jiangshan.knowledge.activity.news.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.Article;

import java.util.List;

/**
 * auth s_yz  2021/10/10
 */
public class NewsArticleAdapter extends BaseQuickAdapter<Article, BaseViewHolder> implements LoadMoreModule {

    public NewsArticleAdapter(int layoutResId, @Nullable List<Article> data) {
        super(layoutResId, data);
    }

    @NonNull
    @Override
    protected BaseViewHolder onCreateDefViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Article bean) {
        baseViewHolder.setText(R.id.tv_news_title, bean.getTitle());
        baseViewHolder.setText(R.id.tv_news_intro, bean.getIntro());
        baseViewHolder.setText(R.id.tv_news_count, bean.getReadQty()+"次阅读");

        ImageView newsIcon=baseViewHolder.getView(R.id.iv_news_icon);
        Glide.with(getContext()).load(bean.getImgUrl()).into(newsIcon);
    }

}
