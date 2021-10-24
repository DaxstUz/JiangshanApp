package com.jiangshan.knowledge.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

/**
 * auth s_yz  2021/10/12
 */
public class SettingActivity extends BaseActivity {

    private RelativeLayout rl_clear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_setting);

        setTitle("设置");
        setBackViewVisiable();

        rl_clear=findViewById(R.id.rl_clear);
        rl_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDataUtils.saveLocalData(SettingActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyUser, "");
                startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

}
