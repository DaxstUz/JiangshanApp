package com.jiangshan.knowledge.activity.home.adapter;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.home.SelectAnserModelActivity;
import com.jiangshan.knowledge.http.entity.Chapter;

import java.util.List;

/**
 * auth s_yz  2021/10/20
 */
public class ChapterChirdAdapter extends BaseQuickAdapter<Chapter, BaseViewHolder> {

    public ChapterChirdAdapter(int layoutResId, @Nullable List<Chapter> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Chapter chapter) {
        baseViewHolder.setText(R.id.tv_chapter_name, chapter.getChapterName());
    }

}
