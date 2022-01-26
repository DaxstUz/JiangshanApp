package com.jiangshan.knowledge.activity.list.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.Rank;

import java.util.List;

/**
 * auth s_yz  2021/10/17
 */
public class RankAdapter extends BaseQuickAdapter<Rank, BaseViewHolder> implements LoadMoreModule {

    public RankAdapter(int layoutResId, @Nullable List<Rank> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Rank rank) {
        baseViewHolder.setText(R.id.tv_rank_name, rank.getNickname());
        baseViewHolder.setText(R.id.tv_rate, rank.getRightRate() + "%");
        baseViewHolder.setText(R.id.tv_no, rank.getNo()+ "");

        baseViewHolder.setText(R.id.tv_rise, "");
        ImageView iv_rate=baseViewHolder.getView(R.id.iv_rate);
        iv_rate.setVisibility(View.GONE);

        if (0 != rank.getRise()) {
            iv_rate.setVisibility(View.VISIBLE);
            if (0 < rank.getRise()) {
                baseViewHolder.setText(R.id.tv_rise, rank.getRise() + "");
                baseViewHolder.setImageResource(R.id.iv_rate, R.mipmap.up);
                baseViewHolder.setTextColor(R.id.tv_rise, getContext().getResources().getColor(R.color.colorRed));
            } else {
                baseViewHolder.setText(R.id.tv_rise, Math.abs(rank.getRise()) + "");
                baseViewHolder.setTextColor(R.id.tv_rise, getContext().getResources().getColor(R.color.colorGreen));
                baseViewHolder.setImageResource(R.id.iv_rate, R.mipmap.down);
            }
        }

        baseViewHolder.setText(R.id.tv_anser_info, rank.getRightQty() + "/" + rank.getAnswerQty());
        CircularImageView userHead = baseViewHolder.findView(R.id.iv_user_head);
        Glide.with(getContext()).load(rank.getFigureUrl()).into(userHead);
    }
}
