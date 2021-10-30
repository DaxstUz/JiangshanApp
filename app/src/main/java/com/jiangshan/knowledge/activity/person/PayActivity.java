package com.jiangshan.knowledge.activity.person;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * auth s_yz  2021/10/30
 */
public class PayActivity extends BaseActivity {

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
                payWeixin();
            }
        });
    }

    private void payWeixin() {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

        // 将该app注册到微信
        msgApi.registerApp("wxfa7d7f1550fb111f");

        PayReq request = new PayReq();
        request.appId = "wxfa7d7f1550fb111f";
        request.partnerId = "1581836751";
        request.prepayId = "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr = "1101000000140429eb40476f8896f4c9";
//        request.timeStamp = "1398746574";
        request.timeStamp = System.currentTimeMillis()+"";
//        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
        request.sign = "60ade2cce178322bbeaedd7cf7bbfd74";
        msgApi.sendReq(request);

    }

    ;


}
