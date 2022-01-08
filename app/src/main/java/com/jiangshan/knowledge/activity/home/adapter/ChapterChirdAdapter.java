package com.jiangshan.knowledge.activity.home.adapter;

import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
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
        baseViewHolder.setText(R.id.tv_anser_info, chapter.getAnswerQuestionQty()+"/"+chapter.getQuestionQty()+"道题");
        ProgressBar progressBar = baseViewHolder.findView(R.id.pb_answer);
        if(chapter.getQuestionQty()>0){
                    int progress=(int)(100*(chapter.getAnswerQuestionQty()/chapter.getQuestionQty()));
        progressBar.setProgress(progress);
        }else{
            baseViewHolder.setVisible(R.id.ll_chapter_answer_info,false);
        }
//        if (0 < chapter.getMemberType()) {
//            baseViewHolder.setImageResource(R.id.iv_edit_icon, R.mipmap.vip);
//        }
    }

}
