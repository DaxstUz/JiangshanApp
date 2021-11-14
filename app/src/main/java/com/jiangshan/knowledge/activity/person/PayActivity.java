package com.jiangshan.knowledge.activity.person;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.api.GetMemberInfoApi;
import com.jiangshan.knowledge.http.api.GetPayHistoryApi;
import com.jiangshan.knowledge.http.api.MemberBuyApi;
import com.jiangshan.knowledge.http.entity.MemberInfo;
import com.jiangshan.knowledge.http.entity.PayHistory;
import com.jiangshan.knowledge.http.entity.Prepay;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.http.model.HttpListDataAll;
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

    private TextView tv_pay;

    private TextView tvUserName;
    private CircularImageView ivUserHead;

    private LinearLayout ll_level_one;
    private LinearLayout ll_level_two;
    private LinearLayout ll_level_three;

    private TextView tvMoneyLevelOne;
    private TextView tvMoneyLevelTwo;
    private TextView tvMoneyLevelThree;

    private int policyId = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay);
        setTitle("会员中心");
        setBackViewVisiable();

        Utils.payUtil = this;

        initView();

        getMemberData();
    }

    private void initView() {
        ll_level_one = findViewById(R.id.ll_level_one);
        ll_level_two = findViewById(R.id.ll_level_two);
        ll_level_three = findViewById(R.id.ll_level_three);

        tvMoneyLevelOne = findViewById(R.id.tv_money_level_one);
        tvMoneyLevelTwo = findViewById(R.id.tv_money_level_two);
        tvMoneyLevelThree = findViewById(R.id.tv_money_level_three);
        tvMoneyLevelOne.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvMoneyLevelTwo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvMoneyLevelThree.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        tvUserName = findViewById(R.id.tv_user_name);
        ivUserHead = findViewById(R.id.iv_user_head);
        tv_pay = findViewById(R.id.tv_pay);
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayOrder();
            }
        });

    }


    private void getPayOrder() {
        Subject subject = LocalDataUtils.getSubject(this);
        EasyHttp.post(this)
                .api(new MemberBuyApi().setPolicyId(policyId).setSubjectCode(subject.getSubjectCode()))
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rv_charge_record:
                startActivity(new Intent(PayActivity.this,PayHistoryActivity.class));
                break;
            case R.id.ll_level_one:
                policyId = 1;
                resetSelectBg();
                tv_pay.setText("开通会员(6个月)");
                ll_level_one.setBackgroundResource(R.drawable.charge_bg_money_select);
                break;
            case R.id.ll_level_two:
                policyId = 2;
                resetSelectBg();
                tv_pay.setText("开通会员(12个月)");
                ll_level_two.setBackgroundResource(R.drawable.charge_bg_money_select);
                break;
            case R.id.ll_level_three:
                policyId = 3;
                resetSelectBg();
                tv_pay.setText("开通会员(24个月)");
                ll_level_three.setBackgroundResource(R.drawable.charge_bg_money_select);
                break;
        }
    }

    private void resetSelectBg() {
        ll_level_one.setBackgroundResource(R.drawable.charge_bg_money);
        ll_level_two.setBackgroundResource(R.drawable.charge_bg_money);
        ll_level_three.setBackgroundResource(R.drawable.charge_bg_money);
    }

    private void getMemberData() {
        Subject subject=LocalDataUtils.getSubject(this);
        EasyHttp.get(this)
                .api(new GetMemberInfoApi().setSubjectCode(subject.getSubjectCode()))
                .request(new HttpCallback<HttpData<MemberInfo>>(this) {
                    @Override
                    public void onSucceed(HttpData<MemberInfo> result) {
                        if (result.isSuccess()) {
                            MemberInfo memberInfo=result.getData();
                            if(1==memberInfo.getMemberType()){
                                tvUserName.setText(memberInfo.getCreator()+"(非会员)");
                            }else{
                                tvUserName.setText(memberInfo.getCreator()+"(会员)");
                            }

                            String userStr = LocalDataUtils.getLocalData(PayActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyUser);
                            if (null != userStr) {
                                User user = new Gson().fromJson(userStr, User.class);
                                if (null != user) {
                                    Glide.with(PayActivity.this).load(user.getAvatar()).into(ivUserHead);
                                }
                            }
                        }
                    }
                });
    }

}
