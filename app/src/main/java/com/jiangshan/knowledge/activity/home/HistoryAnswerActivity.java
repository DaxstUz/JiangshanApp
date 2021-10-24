package com.jiangshan.knowledge.activity.home;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;

/**
 * auth s_yz  2021/10/24
 */
public class HistoryAnswerActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_answer);

        setTitle("答题历史");
        setBackViewVisiable();

    }
}
