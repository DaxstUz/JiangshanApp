package com.jiangshan.knowledge.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.http.model.HttpData;

import okhttp3.Call;


/**
 * 基础类
 * Created by DaxstUz on 2016/6/29.
 */
public class BaseActivity extends AppCompatActivity implements OnHttpListener<Object> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //可以将一下代码加到你的MainActivity中，或者在任意一个需要调用分享功能的activity当中
//        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
//        ActivityCompat.requestPermissions(this, mPermissionList, 100);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        if(null!=tv_title){
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

//    @Override
//    public void formatData(String url, int ret, String code, String msg, int count, Object data) {
//        Log.d("tag", url + "  " + ret + "  " + code + "  " + msg + "  ");
//    }

    /**
     * 分享
     */
//    protected void shareInfo(){
//        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//                {
//                        SHARE_MEDIA.WEIXIN,
//                        SHARE_MEDIA.WEIXIN_CIRCLE,
////                        SHARE_MEDIA.SINA,
//                        SHARE_MEDIA.QQ,
//                        SHARE_MEDIA.QZONE
//                };
//
//        UMImage image = new UMImage(BaseActivity.this,
//                BitmapFactory.decodeResource(getResources(), R.mipmap.xinle_icon));
//
//        new ShareAction(this).setDisplayList( displaylist )
//                .withText("因为信了，自然有了")
//                .withTitle("信了")
//                .withTargetUrl(AddrInterf.HOSTSERVER +AddrInterf.ROOT+ "a/userRegister?vid=" + Utils.vipId)
////                .withTargetUrl("http://www.baidu.com")
//                .withMedia(image)
//                .setListenerList(umShareListener)
//                .open();
//
//    }
//
//   private UMShareListener umShareListener= new UMShareListener() {
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
////            Toast.makeText(BaseActivity.this,platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
////            Toast.makeText(BaseActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
////            Toast.makeText(BaseActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//        }
//    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
    }

    /** 加载对话框 */
    private ProgressDialog mDialog;

    /** 对话框数量 */
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
//        showDialog();
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

}
