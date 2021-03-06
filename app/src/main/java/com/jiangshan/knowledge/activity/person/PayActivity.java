package com.jiangshan.knowledge.activity.person;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.jiangshan.knowledge.activity.home.SubjectDetailActivity;
import com.jiangshan.knowledge.http.api.GetMemberInfoApi;
import com.jiangshan.knowledge.http.api.MemberBuyApi;
import com.jiangshan.knowledge.http.entity.MemberInfo;
import com.jiangshan.knowledge.http.entity.Prepay;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.DateUtil;
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

    private TextView tvPay;

    private TextView tvUserName;
    private CircularImageView ivUserHead;

    private LinearLayout llLevelOne;
    private LinearLayout llLevelTwo;
    private LinearLayout llLevelThree;

    private TextView tvMoneyLevelOne;
    private TextView tvMoneyLevelTwo;
    private TextView tvMoneyLevelThree;

    private TextView tvMonthCount;
    private TextView tvChargeCount;

    private TextView tvVipTips;
    private ImageView ivVipTips;

    private int policyId = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay);
        setTitle("????????????");
        setBackViewVisiable();

        Utils.payUtil = this;

        initView();

        getMemberData();
    }

    private void initView() {
        llLevelOne = findViewById(R.id.ll_level_one);
        llLevelTwo = findViewById(R.id.ll_level_two);
        llLevelThree = findViewById(R.id.ll_level_three);

        tvMonthCount = findViewById(R.id.tv_month_count);
        tvChargeCount = findViewById(R.id.tv_charge_count);
        tvVipTips = findViewById(R.id.tv_vip_tips);
        ivVipTips = findView(R.id.iv_vip_tips);

        tvMoneyLevelOne = findViewById(R.id.tv_money_level_one);
        tvMoneyLevelTwo = findViewById(R.id.tv_money_level_two);
        tvMoneyLevelThree = findViewById(R.id.tv_money_level_three);
        tvMoneyLevelOne.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvMoneyLevelTwo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvMoneyLevelThree.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        tvUserName = findViewById(R.id.tv_user_name);
        ivUserHead = findViewById(R.id.iv_user_head);
        tvPay = findViewById(R.id.tv_pay);
        tvPay.setOnClickListener(new View.OnClickListener() {
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
                        ToastUtils.show("???????????????");
                    }
                });
    }

    private void payWeixin(Prepay prepay) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, prepay.getAppId());
        // ??????app???????????????
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
                ToastUtils.show("???????????????");
                this.finish();
                break;
            case -2:
                ToastUtils.show("???????????????");
                break;
            case -1:
                ToastUtils.show("???????????????");
                break;
            default:
                ToastUtils.show("???????????????");
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
                tvPay.setText("????????????(6??????)");
                llLevelOne.setBackgroundResource(R.drawable.charge_bg_money_select);
                break;
            case R.id.ll_level_two:
                policyId = 2;
                resetSelectBg();
                tvPay.setText("????????????(12??????)");
                llLevelTwo.setBackgroundResource(R.drawable.charge_bg_money_select);
                break;
            case R.id.ll_level_three:
                policyId = 3;
                resetSelectBg();
                tvPay.setText("????????????(24??????)");
                llLevelThree.setBackgroundResource(R.drawable.charge_bg_money_select);
                break;
        }
    }

    private void resetSelectBg() {
        llLevelOne.setBackgroundResource(R.drawable.charge_bg_money);
        llLevelTwo.setBackgroundResource(R.drawable.charge_bg_money);
        llLevelThree.setBackgroundResource(R.drawable.charge_bg_money);
    }

    private void getMemberData() {
        Subject subject=LocalDataUtils.getSubject(this);
        if(null==subject){
            startActivityForResult(new Intent(this, SubjectDetailActivity.class), 0);
            return;
        }
        EasyHttp.get(this)
                .api(new GetMemberInfoApi().setSubjectCode(subject.getSubjectCode()))
                .request(new HttpCallback<HttpData<MemberInfo>>(this) {
                    @Override
                    public void onSucceed(HttpData<MemberInfo> result) {
                        if (result.isSuccess()) {
                            String userStr = LocalDataUtils.getLocalData(PayActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyUser);
                            if (null != userStr) {
                                User user = new Gson().fromJson(userStr, User.class);
                                if (null != user) {
                                    Glide.with(PayActivity.this).load(user.getAvatar()).into(ivUserHead);
                                    tvUserName.setText(user.getNickname()+"(?????????)");
                                }
                            }
                            MemberInfo memberInfo=result.getData();
                            if(null==memberInfo){
                                return;
                            }
                            Gson gson = new Gson();
                            String member = gson.toJson(result.getData());
                            LocalDataUtils.saveLocalData(PayActivity.this, LocalDataUtils.localUserName, LocalDataUtils.keyMember, member);
                            if(null==memberInfo||1>memberInfo.getMemberType()){
                                tvUserName.setText(memberInfo.getCreator()+"(?????????)");
                            }else{
                                tvUserName.setText(memberInfo.getCreator()+"(??????)");
                                tvMonthCount.setText(memberInfo.getMemberMonth()+"");
                                tvChargeCount.setText(memberInfo.getTotalQty()+"");
                                tvVipTips.setText("VIP?????? "+ DateUtil.paseFromStr(memberInfo.getEndDate())+" ??????");
                                ivVipTips.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMemberData();
    }
}
