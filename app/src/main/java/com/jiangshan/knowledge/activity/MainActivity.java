package com.jiangshan.knowledge.activity;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.hjq.http.EasyHttp;
import com.hjq.http.EasyLog;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.home.HomeActivity;
import com.jiangshan.knowledge.activity.list.LearnListActivity;
import com.jiangshan.knowledge.activity.news.NewsActivity;
import com.jiangshan.knowledge.activity.person.PersonActivity;
import com.jiangshan.knowledge.http.api.GetPassportApi;
import com.jiangshan.knowledge.http.entity.Passport;
import com.jiangshan.knowledge.http.model.HttpData;
import com.jiangshan.knowledge.uitl.ClickUtil;
import com.jiangshan.knowledge.uitl.SysUtil;

public class MainActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener, ClickUtil {

    private RadioGroup bottomRg;

    private TabHost tabhost;

//    private ImageView iv_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

//        iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
//        iv_welcome.animate()
//                .alpha(0f)
//                .setDuration(1000)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        iv_welcome.setVisibility(View.GONE);
//                    }
//                });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                iv_welcome.setVisibility(View.GONE);
//            }
//        }, 100);

    }

    private void initView() {
        bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
        bottomRg.setOnCheckedChangeListener(this);

        tabhost = getTabHost();

        Intent homeActivity = new Intent(this, HomeActivity.class);
        tabhost.addTab(tabhost.newTabSpec("tabhome").setIndicator("??????").setContent(homeActivity));

        Intent cardActivity = new Intent(this, NewsActivity.class);
        tabhost.addTab(tabhost.newTabSpec("tabcard").setIndicator("??????").setContent(cardActivity));

        Intent methActivity = new Intent(this, LearnListActivity.class);
        tabhost.addTab(tabhost.newTabSpec("tabmethod").setIndicator("??????").setContent(methActivity));

        Intent personActivity = new Intent(this, PersonActivity.class);
        tabhost.addTab(tabhost.newTabSpec("tabperson").setIndicator("??????").setContent(personActivity));

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                this.tabhost.setCurrentTabByTag("tabhome");
                break;
            case R.id.rb_card:
                this.tabhost.setCurrentTabByTag("tabcard");
                break;
            case R.id.rb_method:
                this.tabhost.setCurrentTabByTag("tabmethod");
                break;
            case R.id.rb_person:
                this.tabhost.setCurrentTabByTag("tabperson");
                break;
        }
    }

    @Override
    public void update() {
        ((RadioButton) bottomRg.getChildAt(1)).setChecked(true);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                SysUtil.makeText(this, "????????????????????????", Toast.LENGTH_SHORT);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }

}
