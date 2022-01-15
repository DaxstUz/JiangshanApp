package com.jiangshan.knowledge.activity.home.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.entity.QuetionCount;

import java.util.List;

/**
 * auth s_yz  2021/10/17
 */
public class QuetionCountAdapter extends BaseQuickAdapter<QuetionCount, BaseViewHolder> implements LoadMoreModule {

    public QuetionCountAdapter(int layoutResId, @Nullable List<QuetionCount> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, QuetionCount data) {
//        题目类型:1.选择题;2.多选题;3.判断题;4.案例解析题;5.论文写作
        String questionName = "选择题";
        switch (data.getId()) {
            case 1:
                questionName = "选择题";
                break;
            case 2:
                questionName = "多选题";
                break;
            case 3:
                questionName = "判断题";
                break;
            case 4:
                questionName = "案例解析题";
                break;
            case 5:
                questionName = "论文写作";
                break;
        }
        baseViewHolder.setText(R.id.tv_question_name, questionName + ":");
        baseViewHolder.setText(R.id.et_question_count, data.getCount() + "");

        EditText questionCount=baseViewHolder.findView(R.id.et_question_count);
        questionCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()>0){
                    data.setCount(Integer.valueOf(s.toString()));
                }else{
                    data.setCount(1);
                }
            }
        });
    }
}
