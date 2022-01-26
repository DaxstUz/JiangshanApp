package com.jiangshan.knowledge.activity.person;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.ChangePsdApi;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.model.HttpData;

/**
 * auth s_yz  2022/1/7
 */
public class ChangePsdActivity extends BaseActivity {

    private EditText etOldPsd;
    private EditText etNewPsd;
    private EditText etSecondNewPsd;

    private LinearLayout llOldPsd;

    int firstChangePassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psd);
        setTitle("修改密码");
        setBackViewVisiable();

        firstChangePassword = getIntent().getIntExtra("firstChangePassword", 1);
        initView();

    }

    private void initView() {
        etOldPsd = findView(R.id.et_old_psd);
        etNewPsd = findView(R.id.et_new_psd);
        etSecondNewPsd = findView(R.id.et_second_new_psd);

        llOldPsd = findView(R.id.ll_old_psd);
        if (0 == firstChangePassword) {
            llOldPsd.setVisibility(View.GONE);
        }
    }

    public void onMyCLick(View view) {
        if (R.id.ll_commit_psd == view.getId()) {
            changePsd();
        }
    }


    private void changePsd() {//18900711210
        ChangePsdApi changePsdApi = new ChangePsdApi();
        if (firstChangePassword > 0) {
            if (etOldPsd.getText().length() == 0) {
                ToastUtils.show("请输入旧密码！");
                return;
            }
            changePsdApi.setOldPassword(etOldPsd.getText().toString());
        }
        if (etNewPsd.getText().length() == 0) {
            ToastUtils.show("请输入新密码！");
            return;
        }
        if (etSecondNewPsd.getText().length() == 0) {
            ToastUtils.show("请再次输入新密码！");
            return;
        }
        changePsdApi.setFirstNewPassword(etNewPsd.getText().toString()).setSecondNewPassword(etSecondNewPsd.getText().toString());
        EasyHttp.post(this)
                .api(changePsdApi)
                .request(new HttpCallback<HttpData<User>>(this) {

                    @Override
                    public void onSucceed(HttpData<User> result) {
                        if (result.isSuccess()) {
                            EasyConfig.getInstance().addParam("token", result.getData().getToken());
                            EasyConfig.getInstance().addHeader("Authorization", result.getData().getToken());
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            ToastUtils.show(result.getMsg());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        ToastUtils.show(e.getMessage());
                    }
                });
    }

}
