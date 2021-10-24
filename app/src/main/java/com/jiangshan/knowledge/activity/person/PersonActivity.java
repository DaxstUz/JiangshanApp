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
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.User;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

public class PersonActivity extends BaseActivity {

    private RelativeLayout rlPersonInfo;
    private LinearLayout llSubject;

    private ImageButton btnSet;

    private TextView tvSubjectName;
    private TextView tvUserName;

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

    private void initItemConf() {
        RelativeLayout feedback=findView(R.id.item_conf_feedback);
        ImageView ivFeedback=feedback.findViewById(R.id.iv_icon_conf);
        ivFeedback.setImageResource(R.mipmap.feedback);

        RelativeLayout question=findView(R.id.item_conf_question);
        TextView tvQuestion=question.findViewById(R.id.tv_item_conf_name);
        tvQuestion.setText("常见问题");
        ImageView ivQuestion=question.findViewById(R.id.iv_icon_conf);
        ivQuestion.setImageResource(R.mipmap.question);

        RelativeLayout share=findView(R.id.item_conf_share);
        TextView tvShare=share.findViewById(R.id.tv_item_conf_name);
        tvShare.setText("推荐给好友");
        ImageView ivShare=share.findViewById(R.id.iv_icon_conf);
        ivShare.setImageResource(R.mipmap.share);

        RelativeLayout define=findView(R.id.item_conf_define);
        TextView tvDefine=define.findViewById(R.id.tv_item_conf_name);
        tvDefine.setText("免责声明");
        ImageView ivDefine=define.findViewById(R.id.iv_icon_conf);
        ivDefine.setImageResource(R.mipmap.define);

        RelativeLayout about=findView(R.id.item_conf_about);
        TextView tvAbout=about.findViewById(R.id.tv_item_conf_name);
        tvAbout.setText("关于我们");
        ImageView ivAbout=about.findViewById(R.id.iv_icon_conf);
        ivAbout.setImageResource(R.mipmap.about);
    }

    private void initView() {
        tvUserName = findViewById(R.id.tv_user_name);
        ivUserHead = findViewById(R.id.iv_user_head);
        tvSubjectName = findViewById(R.id.tv_subject_name);

        rlPersonInfo = findViewById(R.id.rl_person_info);
        llSubject = findViewById(R.id.ll_subject);
        llSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), SelectSubjectActivity.class),RESULT_OK);
            }
        });

        btnSet = findViewById(R.id.btn_set);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            }
        });
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
        String data= LocalDataUtils.getLocalData(this,LocalDataUtils.localDataName,LocalDataUtils.keySubject);
        if(null!=data){
            Subject subject= new Gson().fromJson(data, Subject.class);
            if(null!=subject){
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        user=null;
        updateUI();
    }
}
