package com.jiangshan.knowledge.activity.person;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.person.adapter.PayHistoryAdapter;
import com.jiangshan.knowledge.http.api.GetPayHistoryApi;
import com.jiangshan.knowledge.http.entity.PayHistory;
import com.jiangshan.knowledge.http.model.HttpListDataAll;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/11/14
 */
public class PayHistoryActivity extends BaseActivity {

    private RecyclerView rvExam;
    private PayHistoryAdapter adapter;
    private List<PayHistory> datas = new ArrayList<>();
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay_history);
        setTitle("会员订单记录");
        setBackViewVisiable();

        initHistoryView();
        getData();
    }

    private void initHistoryView() {
        rvExam = findViewById(R.id.rv_exam);
        adapter = new PayHistoryAdapter(R.layout.item_pay_history, datas);
        rvExam.setAdapter(adapter);
        rvExam.setLayoutManager(new LinearLayoutManager(this));
    }


    private void getData() {
        EasyHttp.get(this)
                .api(new GetPayHistoryApi())
                .request(new HttpCallback<HttpListDataAll<PayHistory>>(this) {
                    @Override
                    public void onSucceed(HttpListDataAll<PayHistory> result) {
                        if (result.isSuccess()) {
                            datas.addAll(result.getData());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
