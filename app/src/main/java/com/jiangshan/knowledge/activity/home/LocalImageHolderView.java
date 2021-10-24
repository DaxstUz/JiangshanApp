package com.jiangshan.knowledge.activity.home;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.application.AppApplication;
import com.jiangshan.knowledge.http.entity.Article;

/**
 * auth s_yz  2021/10/13
 */
public class LocalImageHolderView extends Holder<Article> {

    private ImageView imageView;

    public LocalImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        imageView = itemView.findViewById(R.id.iv_banner);
    }

    @Override
    public void updateUI(Article data) {
        Glide.with(AppApplication.getApplication()).load(data.getImgUrl()).into(imageView);
    }
}
