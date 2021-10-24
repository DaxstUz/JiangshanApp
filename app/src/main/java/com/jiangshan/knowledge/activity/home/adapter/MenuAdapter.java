package com.jiangshan.knowledge.activity.home.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.Menu;

import java.util.List;

/**
 * auth s_yz  2021/10/14
 */
public class MenuAdapter extends BaseQuickAdapter<Menu, BaseViewHolder> {

    public MenuAdapter(int layoutResId, @Nullable List<Menu> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Menu data) {
        baseViewHolder.setText(R.id.tv_menu_name, data.getName());
        switch (data.getId()){
            case 1:
                baseViewHolder.setImageResource(R.id.iv_icon_conf,R.mipmap.icon_conf_random);
                break;
            case 2:
                baseViewHolder.setImageResource(R.id.iv_icon_conf,R.mipmap.icon_conf_course_anser);
                break;
            case 3:
                baseViewHolder.setImageResource(R.id.iv_icon_conf,R.mipmap.icon_conf_true_question);
                break;
            case 4:
                baseViewHolder.setImageResource(R.id.iv_icon_conf,R.mipmap.icon_conf_moni);
                break;
            case 5:
                baseViewHolder.setImageResource(R.id.iv_icon_conf,R.mipmap.icon_conf_error_anser);
                break;
            case 6:
                baseViewHolder.setImageResource(R.id.iv_icon_conf,R.mipmap.icon_conf_history_anser);
                break;
            case 7:
                baseViewHolder.setImageResource(R.id.iv_icon_conf,R.mipmap.icon_conf_collect);
                break;
            case 8:
                baseViewHolder.setImageResource(R.id.iv_icon_conf,R.mipmap.icon_conf_rember);
                break;
            default:
                break;
        }
    }

}
