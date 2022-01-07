package com.jiangshan.knowledge.activity.person;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

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

    private EditText et_old_psd;
    private EditText et_new_psd;
    private EditText et_second_new_psd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psd);
        setTitle("修改密码");
        setBackViewVisiable();

        initView();

    }

    private void initView() {
        et_old_psd = findView(R.id.et_old_psd);
        et_new_psd = findView(R.id.et_new_psd);
        et_second_new_psd = findView(R.id.et_second_new_psd);
    }

    public void onMyCLick(View view) {
        if (R.id.ll_commit_psd == view.getId()) {
            changePsd();
        }
    }


    private void changePsd() {//18900711210

        if (et_old_psd.getText().length() == 0) {
            ToastUtils.show("请输入旧密码！");
            return;
        }
        if (et_new_psd.getText().length() == 0) {
            ToastUtils.show("请输入新密码！");
            return;
        }
        if (et_second_new_psd.getText().length() == 0) {
            ToastUtils.show("请再次输入新密码！");
            return;
        }

        EasyHttp.post(this)
                .api(new ChangePsdApi().setOldPassword(et_old_psd.getText().toString()).setFirstNewPassword(et_new_psd.getText().toString()).setSecondNewPassword(et_second_new_psd.getText().toString()))
                .request(new HttpCallback<HttpData<User>>(this) {

                    @Override
                    public void onSucceed(HttpData<User> result) {
                        if (result.isSuccess()) {
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
