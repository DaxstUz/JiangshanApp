package com.jiangshan.knowledge.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.home.HomeActivity;
import com.jiangshan.knowledge.activity.list.LearnListActivity;
import com.jiangshan.knowledge.activity.news.NewsActivity;
import com.jiangshan.knowledge.activity.person.PersonActivity;
import com.jiangshan.knowledge.uitl.ClickUtil;
import com.jiangshan.knowledge.uitl.SysUtil;

public class MainActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener, ClickUtil {

    private RadioGroup bottomRg;

    private TabHost tabhost;

    private ImageView iv_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Utils.clickUtil = this;
        initView();

        iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
//        iv_welcome.animate()
//                .alpha(0f)
//                .setDuration(5000)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        iv_welcome.setVisibility(View.GONE);
//                    }
//                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_welcome.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void initView() {
        bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
        bottomRg.setOnCheckedChangeListener(this);

        tabhost = getTabHost();

        Intent homeActivity = new Intent(this, HomeActivity.class);
        tabhost.addTab(tabhost.newTabSpec("tabhome").setIndicator("首页").setContent(homeActivity));

        Intent cardActivity = new Intent(this, NewsActivity.class);
        tabhost.addTab(tabhost.newTabSpec("tabcard").setIndicator("发现").setContent(cardActivity));

        Intent methActivity = new Intent(this, LearnListActivity.class);
        tabhost.addTab(tabhost.newTabSpec("tabmethod").setIndicator("榜单").setContent(methActivity));

        Intent personActivity = new Intent(this, PersonActivity.class);
        tabhost.addTab(tabhost.newTabSpec("tabperson").setIndicator("我的").setContent(personActivity));

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
                SysUtil.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }

}
