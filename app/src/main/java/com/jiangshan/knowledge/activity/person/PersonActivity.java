package com.jiangshan.knowledge.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.ExamMarkActivity;
import com.jiangshan.knowledge.activity.home.HistoryAnswerActivity;
import com.jiangshan.knowledge.http.api.GetExamHistoryStatisticsApi;
import com.jiangshan.knowledge.http.api.GetMarkCountApi;
import com.jiangshan.knowledge.http.api.GetMemberInfoApi;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.HistoryStatistics;
import com.jiangshan.knowledge.http.entity.MarkCount;
import com.jiangshan.knowledge.http.entity.MemberInfo;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.DateUtil;
import com.jiangshan.knowledge.uitl.LocalDataUtils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class PersonActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rlPersonInfo;
    private RelativeLayout rlCharge;
    private LinearLayout llSubject;
    private ImageButton btnSet;

    private TextView tvSubjectName;
    private TextView tvUserName;

    private TextView tvErrorCount;
    private TextView tvCollectCount;
    private TextView tvHistoryCount;

    private TextView tvVipTips;

    private CircularImageView ivUserHead;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        setTitle("个人中心");

        initView();
        initItemConf();
        updateUI();
    }

    private void getMarkCountData() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        if (null == subject || null == course) {
            return;
        }
        EasyHttp.get(this)
                .api(new GetMarkCountApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()))
                .request(new HttpCallback<HttpData<MarkCount>>(this) {
                    @Override
                    public void onSucceed(HttpData<MarkCount> result) {
                        if (result.isSuccess()) {
                            tvErrorCount.setText(result.getData().getWrongTotal() + "");
                            tvCollectCount.setText(result.getData().getColletTotal() + "");
                        }
                    }
                });

        EasyHttp.get(this)
                .api(new GetExamHistoryStatisticsApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()))
                .request(new HttpCallback<HttpData<HistoryStatistics>>(this) {

                    @Override
                    public void onSucceed(HttpData<HistoryStatistics> result) {
                        if (result.isSuccess()) {
                            tvHistoryCount.setText(result.getData().getAnswerCount() + "");
                        }
                    }
                });
    }

    private void initItemConf() {
        LinearLayout answerAnalysis = findView(R.id.ll_answer_analysis);
        answerAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, HistoryAnswerActivity.class);
                intent.putExtra("examType", 2);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        RelativeLayout feedback = findView(R.id.item_conf_feedback);
        ImageView ivFeedback = feedback.findViewById(R.id.iv_icon_conf);
        ivFeedback.setImageResource(R.mipmap.feedback);
        feedback.setOnClickListener(this);

        RelativeLayout question = findView(R.id.item_conf_question);
        TextView tvQuestion = question.findViewById(R.id.tv_item_conf_name);
        tvQuestion.setText("常见问题");
        ImageView ivQuestion = question.findViewById(R.id.iv_icon_conf);
        ivQuestion.setImageResource(R.mipmap.question);
        question.setOnClickListener(this);

        RelativeLayout share = findView(R.id.item_conf_share);
        TextView tvShare = share.findViewById(R.id.tv_item_conf_name);
        tvShare.setText("推荐给好友");
        ImageView ivShare = share.findViewById(R.id.iv_icon_conf);
        ivShare.setImageResource(R.mipmap.share);
        share.setOnClickListener(this);

        RelativeLayout define = findView(R.id.item_conf_define);
        TextView tvDefine = define.findViewById(R.id.tv_item_conf_name);
        tvDefine.setText("免责声明");
        ImageView ivDefine = define.findViewById(R.id.iv_icon_conf);
        ivDefine.setImageResource(R.mipmap.define);
        define.setOnClickListener(this);

        RelativeLayout about = findView(R.id.item_conf_about);
        TextView tvAbout = about.findViewById(R.id.tv_item_conf_name);
        tvAbout.setText("关于我们");
        ImageView ivAbout = about.findViewById(R.id.iv_icon_conf);
        ivAbout.setImageResource(R.mipmap.about);
        about.setOnClickListener(this);
    }

    private void initView() {
        tvVipTips = findView(R.id.tv_vip_tips);


        tvErrorCount = findView(R.id.tv_error_count);
        rlCharge = findView(R.id.rl_charge);
        rlCharge.setOnClickListener(this);
        tvCollectCount = findView(R.id.tv_collect_count);
        tvHistoryCount = findView(R.id.tv_history_count);
        tvUserName = findViewById(R.id.tv_user_name);
        ivUserHead = findViewById(R.id.iv_user_head);
        tvSubjectName = findViewById(R.id.tv_subject_name);

        rlPersonInfo = findViewById(R.id.rl_person_info);
        llSubject = findViewById(R.id.ll_subject);
        llSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), SelectSubjectActivity.class), RESULT_OK);
            }
        });

        btnSet = findViewById(R.id.btn_set);
        btnSet.setOnClickListener(this);
        rlPersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == user) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });
    }

    private void updateUI() {
        String data = LocalDataUtils.getLocalData(this, LocalDataUtils.localDataName, LocalDataUtils.keySubject);
        if (null != data) {
            Subject subject = new Gson().fromJson(data, Subject.class);
            if (null != subject) {
                tvSubjectName.setText(subject.getSubjectName());
            }
        }

        String userStr = LocalDataUtils.getLocalData(this, LocalDataUtils.localUserName, LocalDataUtils.keyUser);
        if (null != userStr) {
            user = new Gson().fromJson(userStr, User.class);
            if (null != user) {
                tvUserName.setText(user.getNickname());
                Glide.with(this).load(user.getAvatar()).into(ivUserHead);
            }
        }

        getMarkCountData();

        getMemberData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = null;
        updateUI();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.item_conf_feedback:
                String appId = "wxfa7d7f1550fb111f"; // 填移动应用(App)的 AppId
                IWXAPI api = WXAPIFactory.createWXAPI(PersonActivity.this, appId);

// 判断当前版本是否支持拉起客服会话
                if (api.getWXAppSupportAPI() >= Build.SUPPORT_OPEN_CUSTOMER_SERVICE_CHAT) {
                    WXOpenCustomerServiceChat.Req req = new WXOpenCustomerServiceChat.Req();
                    req.corpId = "wwba19f094dd191e2a";                                  // 企业ID
                    req.url = "https://work.weixin.qq.com/kfid/kfcaf303c420214e8bf";    // 客服URL
                    api.sendReq(req);
                }
                break;
            case R.id.rl_charge:
                startActivity(new Intent(PersonActivity.this, PayActivity.class));
                break;
            case R.id.item_conf_share:
//                share(0, "测试分享标题", "测试风险内容", "http://www.baidu.com");
                break;
            case R.id.btn_set:
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                break;
            case R.id.ll_error:
                intent = new Intent(PersonActivity.this, ExamMarkActivity.class);
                intent.putExtra("title", "错题集");
                intent.putExtra("type", "error");
                startActivityForResult(intent, RESULT_OK);
                break;
            case R.id.ll_collect:
                intent = new Intent(PersonActivity.this, ExamMarkActivity.class);
                intent.putExtra("title", "收藏");
                intent.putExtra("type", "collect");
                startActivityForResult(intent, RESULT_OK);
                break;
            case R.id.item_conf_question:
                intent = new Intent(PersonActivity.this, SpecialContentActivity.class);
                intent.putExtra("specialTypeId", 3);
                startActivity(intent);
                break;
            case R.id.item_conf_about:
                intent = new Intent(PersonActivity.this, SpecialContentActivity.class);
                intent.putExtra("specialTypeId", 1);
                startActivity(intent);
                break;
            case R.id.item_conf_define:
                intent = new Intent(PersonActivity.this, SpecialContentActivity.class);
                intent.putExtra("specialTypeId", 2);
                startActivity(intent);
                break;

        }
    }



    private void getMemberData() {
        Subject subject=LocalDataUtils.getSubject(this);
        if(null==subject){
            return;
        }
        EasyHttp.get(this)
                .api(new GetMemberInfoApi().setSubjectCode(subject.getSubjectCode()))
                .request(new HttpCallback<HttpData<MemberInfo>>(this) {
                    @Override
                    public void onSucceed(HttpData<MemberInfo> result) {
                        if (result.isSuccess()) {
                            MemberInfo memberInfo=result.getData();
                            if(null!=memberInfo && memberInfo.getEndDate()>0 ){
                                tvVipTips.setText("VIP会员 "+ DateUtil.paseFromStr(memberInfo.getEndDate())+" 到期");
                            }
                        }
                    }
                });
    }
}
