package com.jiangshan.knowledge.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

/**
 * auth s_yz  2021/10/12
 */
public class SettingActivity extends BaseActivity {

    private RelativeLayout rlClear;

    private Switch switchErrorAnswer;
    private Switch switchAnswerNext;
    private Switch switchAnswerShow;
    private Switch switchHand;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_setting);

        setTitle("设置");
        setBackViewVisiable();

        rlClear = findViewById(R.id.rl_clear);
        rlClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDataUtils.saveLocalData(SettingActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyUser, "");
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                finish();
            }
        });

        initView();
    }

    private void initView() {
        switchErrorAnswer = findViewById(R.id.switch_error_answer);
        switchErrorAnswer.setChecked(LocalDataUtils.getLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyVibrator));

        switchAnswerNext = findViewById(R.id.switch_answer_next);
        switchAnswerNext.setChecked(LocalDataUtils.getLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerNext));

        switchAnswerShow = findViewById(R.id.switch_answer_show);
        switchAnswerShow.setChecked(LocalDataUtils.getLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerShow));

        switchHand = findViewById(R.id.switch_hand);
        switchHand.setChecked(LocalDataUtils.getLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyHand));

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_change_psd:
                startActivity(new Intent(SettingActivity.this,ChangePsdActivity.class));
                break;
            case R.id.switch_answer_show:
                LocalDataUtils.saveLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerShow, switchAnswerShow.isChecked());
                break;
            case R.id.switch_hand:
                LocalDataUtils.saveLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyHand, switchHand.isChecked());
                break;
            case R.id.switch_answer_next:
                LocalDataUtils.saveLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyAnsewerNext, switchAnswerNext.isChecked());
                break;
            case R.id.switch_error_answer:
                LocalDataUtils.saveLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyVibrator, switchErrorAnswer.isChecked());
                if (switchErrorAnswer.isChecked()) {
                    vibrator();
                }
                break;
        }
    }
}
