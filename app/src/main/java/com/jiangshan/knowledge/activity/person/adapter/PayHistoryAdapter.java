package com.jiangshan.knowledge.activity.person.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.PayHistory;

import java.util.List;

/**
 * auth s_yz  2021/10/17
 */
public class PayHistoryAdapter extends BaseQuickAdapter<PayHistory, BaseViewHolder> implements LoadMoreModule {

    public PayHistoryAdapter(int layoutResId, @Nullable List<PayHistory> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PayHistory data) {
        baseViewHolder.setText(R.id.tv_pay_time, data.getBeginDate());
        baseViewHolder.setText(R.id.tv_pay_type, data.getOrderType());
        baseViewHolder.setText(R.id.tv_pay_long, data.getNum() + "个月");
        baseViewHolder.setText(R.id.tv_pay_status, data.getStatusDesc());
    }
}
