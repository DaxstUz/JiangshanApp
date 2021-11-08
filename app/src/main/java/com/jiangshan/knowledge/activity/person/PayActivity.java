package com.jiangshan.knowledge.activity.person;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.MemberBuyApi;
import com.jiangshan.knowledge.http.entity.Prepay;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.jiangshan.knowledge.uitl.PayUtil;
import com.jiangshan.knowledge.uitl.Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * auth s_yz  2021/10/30
 */
public class PayActivity extends BaseActivity implements PayUtil {

    private Button btn_pay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay);
        setTitle("支付");
        setBackViewVisiable();

        btn_pay = findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayOrder();
            }
        });
        Utils.payUtil = this;
    }


    private void getPayOrder() {
        Subject subject = LocalDataUtils.getSubject(this);
        EasyHttp.post(this)
                .api(new MemberBuyApi().setPolicyId(1).setSubjectCode(subject.getSubjectCode()))
                .request(new HttpCallback<HttpData<Prepay>>(this) {

                    @Override
                    public void onSucceed(HttpData<Prepay> result) {
                        payWeixin(result.getData());
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        ToastUtils.show("登录失败！");
                    }
                });
    }

    private void payWeixin(Prepay prepay) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, prepay.getAppId());
        // 将该app注册到微信
        msgApi.registerApp(prepay.getAppId());
        PayReq request = new PayReq();
        request.appId = prepay.getAppId();
        request.partnerId = "1581836751";
        request.prepayId = prepay.getPrepay_id();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = prepay.getNonceStr();
        request.timeStamp = prepay.getTimeStamp();
        request.sign = prepay.getSignType();//MD5
        msgApi.sendReq(request);
    }


    @Override
    public void payResult(int result) {
        switch (result) {
            case 0:
                ToastUtils.show("支付成功！");
                this.finish();
                break;
            case -2:
                ToastUtils.show("支付取消！");
                break;
            case -1:
                ToastUtils.show("支付失败！");
                break;
            default:
                ToastUtils.show("支付出错！");
                break;
        }
    }
}
