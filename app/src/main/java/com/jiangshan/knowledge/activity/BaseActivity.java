package com.jiangshan.knowledge.activity;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.home.SubjectDetailActivity;
import com.jiangshan.knowledge.activity.person.LoginActivity;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import okhttp3.Call;


/**
 * 基础类
 * Created by DaxstUz on 2016/6/29.
 */
public class BaseActivity extends AppCompatActivity implements OnHttpListener<Object> {

    private Vibrator myVibrator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获得系统的Vibrator实例
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        //可以将一下代码加到你的MainActivity中，或者在任意一个需要调用分享功能的activity当中
//        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
//        ActivityCompat.requestPermissions(this, mPermissionList, 100);
    }

    /**
     * 调用震动
     */
    protected void vibrator(){
        myVibrator.cancel();
        myVibrator.vibrate(300);
//        myVibrator.vibrate(new long[]{100, 200, 100, 200}, 0);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        if (null != tv_title) {
            tv_title.setText(title);
        }
    }

    /**
     * 设置返回按钮可见
     */
    protected void setBackViewVisiable() {
        ImageView iv_back = findView(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    /**
     * 获取控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 简单弹框
     *
     * @param text
     */
    protected void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    private final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN,
                    SHARE_MEDIA.WEIXIN_CIRCLE
//                        SHARE_MEDIA.SINA,
//                        SHARE_MEDIA.QQ,
//                        SHARE_MEDIA.QZONE
            };

    /**
     * 分享 type 0表示网页，1表示文件，2表示……
     */
    protected void share(int type, String title, String content, String url) {


        UMImage image = new UMImage(BaseActivity.this,
                BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app));
        ShareAction shareAction = new ShareAction(this).setDisplayList(displaylist)
                .setCallback(umShareListener);

        switch (type) {
            case 0:
                UMWeb umWeb = new UMWeb(url);
                umWeb.setThumb(image);
                umWeb.setTitle(title);
                umWeb.setDescription(content);
                shareAction.withMedia(umWeb);
                break;
        }

        shareAction.open();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(BaseActivity.this,platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(BaseActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(BaseActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载对话框
     */
    private ProgressDialog mDialog;

    /**
     * 对话框数量
     */
    private int mDialogTotal;

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage(getResources().getString(R.string.http_loading));
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mDialogTotal++;
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        if (mDialogTotal == 1) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
        if (mDialogTotal > 0) {
            mDialogTotal--;
        }
    }

    @Override
    public void onStart(Call call) {
    }

    @Override
    public void onSucceed(Object result) {
        if (result instanceof HttpData) {
            ToastUtils.show(((HttpData<?>) result).getMsg());
        }
    }

    @Override
    public void onFail(Exception e) {
        ToastUtils.show(e.getMessage());
    }

    @Override
    public void onEnd(Call call) {
//        hideDialog();
    }

    protected boolean judgeSuject(){
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        if(null==subject || null==course){
            startActivity(new Intent(getApplicationContext(), SubjectDetailActivity.class));
            return false;
        }
        return true;
    }

    protected boolean judgeLogin(){
        String userStr = LocalDataUtils.getLocalData(this, LocalDataUtils.localUserName, LocalDataUtils.keyUser);
        if (null != userStr) {
            return true;
        }else{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        return false;
    }
}
