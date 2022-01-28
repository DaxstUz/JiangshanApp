package com.jiangshan.knowledge.activity.person;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.SubjectDetailActivity;
import com.jiangshan.knowledge.http.api.ClearExamHistoryApi;
import com.jiangshan.knowledge.http.api.ClearWrongQuestionApi;
import com.jiangshan.knowledge.http.api.LogoutApi;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.AlertButtonClick;
import com.jiangshan.knowledge.uitl.DialogUtil;
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
    private Switch switchShowResult;

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
                logout();
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

        switchShowResult = findViewById(R.id.switch_show_result);
        switchShowResult.setChecked(LocalDataUtils.getLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyResultShow));

    }


    public void onClick(View view) {
        DialogUtil.DialogAttrs attrs = new DialogUtil.DialogAttrs();
        switch (view.getId()) {
            case R.id.rl_clear_hisory:
                attrs.title = "清空答题记录";
                attrs.msg = "确定清空所有答题历史记录吗？";
                attrs.textGravity = Gravity.CENTER;
                attrs.btnVal = new String[]{"取消", "确定"};
                attrs.isCancelable = Boolean.FALSE;
                DialogUtil.alertDialog(SettingActivity.this, attrs, new AlertButtonClick() {
                    @Override
                    public void leftBtnClick(AlertDialog dlg) {
                        dlg.dismiss();
                    }

                    @Override
                    public void rightBtnClick(AlertDialog dlg) {
                        dlg.dismiss();
                        clearHistory();
                    }
                });
                break;

            case R.id.rl_clear_wrong:
                attrs.title = "清空错题集";
                attrs.msg = "确定清空所有错题集吗？";
                attrs.textGravity = Gravity.CENTER;
                attrs.btnVal = new String[]{"取消", "确定"};
                attrs.isCancelable = Boolean.FALSE;
                DialogUtil.alertDialog(SettingActivity.this, attrs, new AlertButtonClick() {
                    @Override
                    public void leftBtnClick(AlertDialog dlg) {
                        dlg.dismiss();
                    }

                    @Override
                    public void rightBtnClick(AlertDialog dlg) {
                        dlg.dismiss();
                        clearWrong();
                    }
                });
                break;
            case R.id.rl_change_psd:
                startActivity(new Intent(SettingActivity.this, ChangePsdActivity.class));
                break;
            case R.id.switch_show_result:
                LocalDataUtils.saveLocalDataBoolean(SettingActivity.this, LocalDataUtils.settingDataName, LocalDataUtils.keyResultShow, switchShowResult.isChecked());
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

    private void clearHistory() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        if (null == subject || null == course) {
            startActivityForResult(new Intent(this, SubjectDetailActivity.class), 0);
            return;
        }
        EasyHttp.delete(this)
                .api(new ClearExamHistoryApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()))
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        if (result.isSuccess()) {
                            ToastUtils.show("已清空答题历史记录！");
                        }
                    }
                });
    }


    private void clearWrong() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        if (null == subject || null == course) {
            startActivityForResult(new Intent(this, SubjectDetailActivity.class), 0);
            return;
        }
        EasyHttp.delete(this)
                .api(new ClearWrongQuestionApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()))
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        if (result.isSuccess()) {
                            ToastUtils.show("已清空错题集！");
                        }
                    }
                });
    }

    private void logout() {
        EasyHttp.get(this)
                .api(new LogoutApi())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        if (result.isSuccess()) {
                            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                            EasyConfig.getInstance().addParam("token", "");
                            EasyConfig.getInstance().addHeader("Authorization", "");
                            finish();
                        }
                    }
                });
    }

}
