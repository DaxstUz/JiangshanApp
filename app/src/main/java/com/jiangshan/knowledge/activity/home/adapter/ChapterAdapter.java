package com.jiangshan.knowledge.activity.home.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.hjq.http.EasyLog;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.home.SelectAnserModelActivity;
import com.jiangshan.knowledge.http.entity.Chapter;

import java.util.List;

/**
 * auth s_yz  2021/10/20
 */
public class ChapterAdapter extends BaseQuickAdapter<Chapter, BaseViewHolder> {

    public ChapterAdapter(int layoutResId, @Nullable List<Chapter> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Chapter chapter) {
        baseViewHolder.setText(R.id.tv_chapter_name, chapter.getChapterName());
//        EasyLog.print("chapterCode==> "+new Gson().toJson(chapter));
        String chapterNo=chapter.getChapterCode();
        baseViewHolder.setText(R.id.tv_chapter_no, "【"+chapterNo+"】");

        RecyclerView rvChapter = baseViewHolder.findView(R.id.rv_chapter);
        if(chapter.isOpen()){
            baseViewHolder.setImageResource(R.id.iv_chapter_conf,R.mipmap.chapter_less);
            rvChapter.setVisibility(View.VISIBLE);
            initChirdView(baseViewHolder,chapter);
        }else{
            baseViewHolder.setImageResource(R.id.iv_chapter_conf,R.mipmap.chapter_more);
            rvChapter.setVisibility(View.GONE);
        }

        FrameLayout flItem=baseViewHolder.findView(R.id.fl_item);
        flItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapter.setOpen(!chapter.isOpen());
                notifyDataSetChanged();
            }
        });

    }

    private void initChirdView(BaseViewHolder baseViewHolder, Chapter chapter) {
        RecyclerView rvChapter = baseViewHolder.findView(R.id.rv_chapter);
        ChapterChirdAdapter chapterAdapter = new ChapterChirdAdapter(R.layout.item_chapter_chird, chapter.getChildren());
        rvChapter.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChapter.setAdapter(chapterAdapter);
        chapterAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent=new Intent(getContext(), SelectAnserModelActivity.class);
                intent.putExtra("examCode",chapter.getChildren().get(position).getChapterCode());
                intent.putExtra("examName",chapter.getChildren().get(position).getChapterName());
                intent.putExtra("examType",3);
                getContext().startActivity(intent);
            }
        });
    }
}
